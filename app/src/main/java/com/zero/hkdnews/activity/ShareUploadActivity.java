package com.zero.hkdnews.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
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

import android.os.Handler;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by luowei on 15/5/26.
 */
public class ShareUploadActivity extends BaseActivity {

    //上传的进度框
    private ProgressDialog progressDialog;

    private Button takePhotoBtn;
    private Button usePhotoBtn;
    private Button okBtn;

    private EditText contentEt;
    private ImageView imageView;

    private Uri imageUri;

    private File outputImage;

    private Handler mHandler;

    private Bitmap upload_bp;

    private static final int TAKE_PHOTO_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareupload);
        initView();

        initData();


        //点击拍照，调用相机
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    L.v("TestFile",
                            "SD card is not avaiable/writeable right now.");
                    T.showShort(ShareUploadActivity.this,"SD卡不可用！");
                    return;
                }


                final double r = Math.random()*100000000+1;
                final String name = (int)r + "";
                outputImage = new File(Environment.getExternalStorageDirectory(), "/hkdnews/"+name+"_img.jpg");

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, TAKE_PHOTO_CODE);


            }
        });


        //点击上传
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!contentEt.getText().toString().equals("") && outputImage.exists()) {

                    showPd();

                  Thread uploadPic = new Thread(new Runnable() {
                      @Override
                      public void run() {

                          final BmobFile photo = new BmobFile(outputImage);

                          photo.uploadblock(getApplicationContext(), new UploadFileListener() {
                              @Override
                              public void onSuccess() {
                                  Message msg = Message.obtain();
                                  msg.obj = photo;
                                  msg.what = 0x22;
                                  mHandler.sendMessage(msg);
                              }
                              @Override
                              public void onFailure(int i, String s) {

                              }
                          });

                      }
                  });
                    uploadPic.start();

                }else{
                    T.showShort(getApplicationContext(),"输入有误！");
                }
            }
        });
    }

    private void initData() {
        mHandler = new Handler() {
            public void  handleMessage(Message msg){
                if (msg.what == 0x22){
                    UploadNews data = new UploadNews();
                    data.setAuthor(AppContext.getUserName());
                    data.setHead(AppContext.getMyHead());
                    data.setPhoto((BmobFile) msg.obj);
                    data.setContent(contentEt.getText().toString());
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
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case TAKE_PHOTO_CODE:
                if(resultCode == RESULT_OK){
//                   upload_bp = decodeFile(imageUri);

                    Uri uri = data.getData();
                    if (uri != null) {
//                        upload_bp = BitmapFactory.decodeFile(uri.getPath());
                        upload_bp = decodeFile(uri);
                    }
                    if (upload_bp == null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            upload_bp = (Bitmap) bundle.get("data");
                        } else {
                            T.showShort(ShareUploadActivity.this,"读取错误！");
                            return;
                        }
                    }

                    if(upload_bp != null){
                        imageView.setImageBitmap(upload_bp);
                        imageView.setVisibility(View.VISIBLE);
                        bitmapToFile(upload_bp);
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

            final double r = Math.random()*10000000+1;
            final String name = (int)r+ "";
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/hkdnews";

            //创建hkdnews目录
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }

            outputImage = new File(saveDir, name+"_img.jpg");

            if (!outputImage.exists()) {
                outputImage.createNewFile();
            }

            fos = new FileOutputStream(outputImage);
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
        takePhotoBtn = (Button) findViewById(R.id.share_upload_take_photo_btn);
        usePhotoBtn = (Button) findViewById(R.id.share_upload_use_photo_btn);
        okBtn = (Button) findViewById(R.id.share_upload_ok_btn);

        contentEt = (EditText) findViewById(R.id.share_upload_edit_text);
        imageView = (ImageView) findViewById(R.id.share_upload_imageview);
        imageView.setImageResource(R.mipmap.login_bg);

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


    private void showPd(){
        progressDialog = new ProgressDialog(ShareUploadActivity.this);
        progressDialog.setTitle("分享");
        progressDialog.setMessage("正在上传...");
        // 设置对话框不能用“取消”按钮关闭
        progressDialog.setCancelable(false);
        // 设置对话框的进度条风格
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    private void closePd(){
        progressDialog.dismiss();
        progressDialog = null;
    }

}
