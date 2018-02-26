package com.vachanasaahitya.vachanas.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by narensmac on 26/02/18.
 */

public class DatabaseHelper {

    private static final int version = 1;
    private static final String DB_NAME = "vachanas.db";

    private static final String TABLE_VACHANAKAARAS = "Vachanakaaraas";
    private static final String TABLE_VACHANAAS = "Vachanas";
    private static final String COLUMN_VACHANA = "vachana";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_ID = "_id";

    public static boolean copyFile(Context context){
        boolean value = false;
        File file = new File(context.getFilesDir().getAbsolutePath(), DB_NAME);
        if(file.exists() && file.length() > 0){
            value = true;
        }else {
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                FileOutputStream fos = context.openFileOutput(DB_NAME, Context.MODE_APPEND);
                value = (IOUtils.copy(is, fos) > 0);
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(fos);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return value;
    }

    private static SQLiteDatabase getDB(Context context){
        File file = new File(context.getFilesDir(), DB_NAME);
        return SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, 0);
    }

    public static Cursor searchVachanakaara(Context context, String value){
        String selection = null;
        String[] selectionArgs = null;
        if(!TextUtils.isEmpty(value)){
            selection = COLUMN_NAME+"=?";
            selectionArgs = new String[]{value};
        }
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        return db.query(TABLE_VACHANAKAARAS, new String[]{COLUMN_NAME, COLUMN_DETAILS}, selection, selectionArgs, null, null, null);
    }

    public static Cursor searchVachanas(Context context, String value){
        String selection = null;
        String[] selectionArgs = null;
        if(!TextUtils.isEmpty(value)){
            selection = COLUMN_VACHANA+" LIKE '%?%'";
            selectionArgs = new String[]{value};
        }
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        return db.query(TABLE_VACHANAAS, new String[]{COLUMN_VACHANA}, selection, selectionArgs, null, null, null);
    }


    public static Cursor getVachanas(Context context, String vachanakaara){
        String selection = null;
        String[] selectionArgs = null;
        if(!TextUtils.isEmpty(vachanakaara)){
            selection = COLUMN_NAME+"=?";
            selectionArgs = new String[]{vachanakaara};
        }
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        return db.query(TABLE_VACHANAAS, new String[]{COLUMN_VACHANA}, selection, selectionArgs, null, null, null);

    }
}
