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
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static void loadImage(@NonNull Context context,
                                 @NonNull Uri uri,
                                 @NonNull ImageView imageView) {
        try {
            Bitmap bitmap;
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//            Log.i("Bowen", getRealPathFromURI(context, uri));
            if (bitmap == null) {
                String path = getRealPathFromURI(context, uri);
                File imgFile = new File(path);
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
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