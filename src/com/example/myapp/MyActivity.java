package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;

public class MyActivity extends Activity {
    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 4;
    private static int IMAGE_CROP_CODE = 6;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //开始编写代码
        //这是摄像头按钮的相应事件
        Button btn_camera = ((Button) findViewById(R.id.btn_camera));
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        //这是图库按钮的相应事件
        Button btn_gallery = ((Button) findViewById(R.id.btn_gallery));
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                } else {
                    Bitmap bitmap = extras.getParcelable("data");
//                    this.startImageEdit(this.convertUri(this.saveOnSD(bitmap)));
                    ImageView imageView = ((ImageView) findViewById(R.id.imageView));
                    imageView.setImageBitmap(bitmap);
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Bundle bundle = data.getExtras();
//                Uri uri = data.getData();
//                Toast.makeText(MyActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
                //要对file类型的数据进行操作
//                uri = convertUri(uri);
//                Toast.makeText(MyActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
//                this.startImageEdit(uri);
                Bitmap bitmap = bundle.getParcelable("data");
//                    this.startImageEdit(this.convertUri(this.saveOnSD(bitmap)));
                ImageView imageView = ((ImageView) findViewById(R.id.imageView));
                imageView.setImageBitmap(bitmap);

            }
        } else if (requestCode == IMAGE_CROP_CODE) {
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");//这句话很有深意
            ImageView imageView = ((ImageView) findViewById(R.id.imageView));
            imageView.setImageBitmap(bitmap);
        }
    }

    private void startImageEdit(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        this.startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    private Uri convertUri(Uri uri) {
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();//关闭inputStream
            return saveOnSD(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri saveOnSD(Bitmap bitmap) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + File.separator + "GLF-myapp");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File tmpFile = new File(tmpDir.getAbsolutePath() + File.separator + "GLF-tmp.png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(tmpFile);
    }
}
