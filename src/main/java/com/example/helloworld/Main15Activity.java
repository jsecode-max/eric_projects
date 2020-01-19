package com.example.helloworld;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main15Activity extends AppCompatActivity {

    private static final String TAG = "=========";

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    private ImageView picture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main15);
        Button takePhoto = (Button) findViewById(R.id.take_pic);
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture = (ImageView) findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    //将照片文件outputImage通过FileProvider共享
                    //这个文件outputImage一定位于 @xml/fiel_paths 文件中添加的子目录里面
                    //将文件的file://转化成content://的Uri对象
                    imageUri = FileProvider.getUriForFile(Main15Activity.this,
                            "com.example.helloworld.fileprovider",  //这个对应的就是FileProvider的AndroidManifest.xml注册信息
                            outputImage);
                } else {
                    //Content://格式的Uri对象
                    imageUri = Uri.fromFile(outputImage);
                }

                //调用摄像头
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //拍的照片存在imageUri中
                startActivityForResult(intent, TAKE_PHOTO); //拍好之后，通过回调查看是否成功，如果拍照成功，那就展示
            }
        });
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Main15Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Main15Activity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum() {
        //发送的intent通过getData可以获取到Uri
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);   //打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //如果拍照成功，那么通过FileProvider共享的照片imageUri，解析成Bitmap
                        //相册的方式不一样，相册通过Cursor，根据Uri获取到String类型的path值，通过BitmapFactory.decodeFile(path)
                        //相机采用流的方式解析，因为imageUri对应的是刚拍的照片路径；而相册这里采用的是文件夹路径
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    /***
                     * 可以继续考虑照片的压缩
                     */
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以上的系统使用的处理方式，返回封装过的Uri，需要特殊解析
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下使用的处理方式，直接返回的是Content Uri
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        //4.4以下使用的处理方式，直接返回的是Content Uri
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void handleImageOnKitKat(Intent data) {
        //4.4以上的系统使用的处理方式，返回封装过的Uri，需要特殊解析
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
            //Meta20 pro
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d(TAG, "handleImageOnKitKat: " + docId);

            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //如果authority是media类型，docId需要进一步分割出后半部分真正的ID
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;

                /***
                 * 这里通过拼装获得具体照片的Uri，给采用流的方式调用
                 */
//                String xUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/" + id;
//                Log.d(TAG, "getImagePath: " + xUri);
//                Uri yUri = Uri.parse(xUri);
//                displayImageByUri(yUri);
//                displayImage(xUri); //这样不行：Unable to decode stream: java.io.FileNotFoundException: content:/media/external/images/media/312769 (No such file or directory)

                //MediaStore.Images.Media.EXTERNAL_CONTENT_URI  ->  content://media/external/images/media
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //如果是普通的document类型，docId就是普通的数字字符串，所以这里采用了Long.valueOf(docId)
                //Appends the given ID to the end of the path.
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(imageUri, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection获取图片的路径，如果是Content类型的Uri，直接从ContentProvider取
        //参考Main13Activity中获取通讯录的Uri：ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d(TAG, "PATH: " + path);
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayImageByUri(Uri uri) {
        /***
         * 这段代码模仿相机，采用getContentResolver().openInputStream(y)方式获取文件流
         * 进而解码得到位图
         */
        try {
            Bitmap b = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            picture.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
