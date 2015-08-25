package com.zero.hkdnews.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zero.hkdnews.R;
import com.zero.hkdnews.app.AppContext;
import com.zero.hkdnews.beans.UploadNews;
import com.zero.hkdnews.util.L;
import com.zero.hkdnews.util.T;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by zero on 15/5/26.
 */
public class ShareUploadActivity extends BaseActivity {

    //上传的进度框
    private ProgressDialog mProgressDialog;
    private Button mTakePhotoBtn;
    private Button mUsePhotoBtn;
    private Button mUploadBtn;
    private EditText mContentEt;
    private ImageView mPhotoIv;

    private Uri mImageUri;
    private File mOutputImage;

    private WeakReferenceHander weakReferenceHander;

    private Bitmap mUploadBp;

    private static final int TAKE_PHOTO_CODE = 1;

    static class WeakReferenceHander extends Handler {
        private final WeakReference<ShareUploadActivity> mActivity;

        public WeakReferenceHander(ShareUploadActivity activity) {
            mActivity = new WeakReference<ShareUploadActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null) {
               mActivity.get().handleReceiveMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareupload);
        initView();
        weakReferenceHander = new WeakReferenceHander(this);

        //点击拍照，调用相机
        mTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    L.v("TestFile","SD card is not avaiable/writeable right now.");
                    T.showShort(ShareUploadActivity.this,"SD卡不可用！");
                    return;
                }

                final double r = Math.random()*100000000+1;
                final String name = (int)r + "";
                mOutputImage = new File(Environment.getExternalStorageDirectory(), "/hkdnews/"+name+"_img.jpg");

                try {
                    if (mOutputImage.exists()) {
                        mOutputImage.delete();
                    }
                    mOutputImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_CODE);
            }
        });

        //点击上传
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mContentEt.getText().toString().equals("") && mOutputImage.exists()) {
                    showPd();
                    Thread uploadPic = new Thread(new Runnable() {
                      @Override
                      public void run() {
                          final BmobFile photo = new BmobFile(mOutputImage);
                          photo.uploadblock(getApplicationContext(), new UploadFileListener() {
                              @Override
                              public void onSuccess() {
                                  Message msg = Message.obtain();
                                  msg.obj = photo;
                                  msg.what = 0x22;
                                  weakReferenceHander.sendMessage(msg);
                              }
                              @Override
                              public void onFailure(int i, String s) {}
                          });
                      }});
                    uploadPic.start();
                } else {
                    T.showShort(getApplicationContext(),"输入有误！");
                }
            }
        });
    }

    //上传完照片后，存储记录到Bmob中
    public void handleReceiveMessage(Message msg) {
        if (msg.what == 0x22){
            UploadNews data = new UploadNews();
            data.setAuthor(AppContext.getUserName());
            if (AppContext.getMyHead()!=null) {
                data.setHead(AppContext.getMyHead());
            }
            data.setPhoto((BmobFile) msg.obj);
            data.setContent(mContentEt.getText().toString());
            data.setLove(0);
            data.save(getApplicationContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    closePd();
                    T.showShort(getApplicationContext(), "上传成功");
                    finish();
                }
                @Override
                public void onFailure(int i, String s) {
                    closePd();
                    T.showShort(getApplicationContext(),s+" 失败！");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO_CODE:
                if(resultCode == RESULT_OK){
//                   upload_bp = decodeFile(imageUri);
                    Uri uri = data.getData();
                    if (uri != null) {
//                        upload_bp = BitmapFactory.decodeFile(uri.getPath());
                        mUploadBp = decodeFile(uri);
                    }
                    if (mUploadBp == null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            mUploadBp = (Bitmap) bundle.get("data");
                        } else {
                            T.showShort(ShareUploadActivity.this,"读取错误！");
                            return;
                        }
                    }

                    if(mUploadBp != null){
                        mPhotoIv.setImageBitmap(mUploadBp);
                        mPhotoIv.setVisibility(View.VISIBLE);
                        bitmapToFile(mUploadBp);
                    }else{
                        T.showShort(this,"设备错误！");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 将bitmap压缩写入文件，outputImage
     * @param bitmap
     */
    private void bitmapToFile(Bitmap bitmap){
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();

            final double r = Math.random()*100000000+1;
            final String name = (int)r+ "";
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/hkdnews";
            //创建hkdnews目录
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }

            mOutputImage = new File(saveDir, name+"_img.jpg");

            if (!mOutputImage.exists()) {
                mOutputImage.createNewFile();
            }

            fos = new FileOutputStream(mOutputImage);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
//            pictureDir = outputImage.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void initView() {
        mTakePhotoBtn = (Button) findViewById(R.id.share_upload_take_photo_btn);
        mUsePhotoBtn = (Button) findViewById(R.id.share_upload_use_photo_btn);
        mUploadBtn = (Button) findViewById(R.id.share_upload_ok_btn);

        mContentEt = (EditText) findViewById(R.id.share_upload_edit_text);
        mPhotoIv = (ImageView) findViewById(R.id.share_upload_imageview);
        mPhotoIv.setImageResource(R.mipmap.login_bg);

    }

    /**
     * 处理图片，防止内存溢出
     */
    private Bitmap decodeFile(Uri uri) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 200;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * 显示进度对话框
     */
    private void showPd(){
        mProgressDialog = new ProgressDialog(ShareUploadActivity.this);
        mProgressDialog.setTitle("分享");
        mProgressDialog.setMessage("正在上传...");
        // 设置对话框不能用“取消”按钮关闭
        mProgressDialog.setCancelable(false);
        // 设置对话框的进度条风格
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closePd(){
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

}

