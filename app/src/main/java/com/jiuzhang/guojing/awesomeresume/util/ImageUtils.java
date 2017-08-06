package com.jiuzhang.guojing.awesomeresume.util;

/**
 * Created by apple on 3/25/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageUtils {

    public static void loadImage(@NonNull Context context,
                                 @NonNull String imagePath,
                                 @NonNull ImageView imageView) {
//        InputStream is = null;
//        if (uri.getAuthority() != null) {
//            try {
//                is = context.getContentResolver().openInputStream(uri);
//                Log.i("Bowen", is.toString());
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
////                return writeToTempImageAndGetPathUri(context, bmp).toString();
////                if (bitmap == null) {
//                    String path = writeToTempImageAndGetPathUri(context, bitmap).toString();
//                    Log.i("Bowen", path);
//                    File imgFile = new File(path);
//                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
////                }
//            imageView.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }finally {
////                try {
////                    is.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//            }
//        }
//        return;
        
//        try {
//            Bitmap bitmap;
//            Log.i("Bowen", getRealPathFromURI(context, uri));
//            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//
//            if (bitmap == null) {
//                String path = getRealPathFromURI(context, uri);
//                File imgFile = new File(path);
//                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            }
//            imageView.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

            // correct !!!
            Log.i("Bowen", imagePath);
            File imgFile = new File(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
    }

    private static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}