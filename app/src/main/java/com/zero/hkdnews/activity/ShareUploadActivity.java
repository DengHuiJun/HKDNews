package com.zero.hkdnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.zero.hkdnews.util.T;

import java.io.File;
import java.io.FileNotFoundException;

import android.os.Handler;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by luowei on 15/5/26.
 */
public class ShareUploadActivity extends BaseActivity {

    private Button takePhotoBtn;
    private Button usePhotoBtn;
    private Button okBtn;

    private EditText contentEt;
    private ImageView imageView;

    private Uri imageUri;

    private File outputImage;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareupload);
        initView();

        initData();


        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);


            }
        });


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!contentEt.getText().toString().equals("") && outputImage.exists()) {
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
                            T.showShort(getApplicationContext(),"上传成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            T.showShort(getApplicationContext(),s+" 失败！");

                        }
                    });
                }

            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(requestCode == RESULT_OK){
                    Intent i = new Intent("com.android.camera.action.CROP");
                    i.setDataAndType(imageUri,"image/*");
                    i.putExtra("scale", true);
                    i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(i,2);
                }
                break;
            case 2:
                if(requestCode == RESULT_OK){
                    try{
                        Bitmap bitmap = decodeFile(imageUri);
                        imageView.setImageBitmap(bitmap);
                 //       Picasso.with(getApplicationContext()).load(imageUri).resize(200,200).into(imageView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        takePhotoBtn = (Button) findViewById(R.id.share_upload_take_photo_btn);
        usePhotoBtn = (Button) findViewById(R.id.share_upload_use_photo_btn);
        okBtn = (Button) findViewById(R.id.share_upload_ok_btn);

        contentEt = (EditText) findViewById(R.id.share_upload_edit_text);
        imageView = (ImageView) findViewById(R.id.share_upload_imageview);

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
            final int REQUIRED_SIZE = 70;
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




}
