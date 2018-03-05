package com.vachanasaahitya.vachanas.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
    private static final String DB_NAME = "1.vc";

    private static final String TABLE_VACHANAKAARAS = "Vachanakaaraas";
    private static final String TABLE_VACHANAAS = "Vachanas";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_ID = "_id";

    public static final String COLUMN_VACHANA = "vachana";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DETAILS = "details";

    public static boolean copyFile(Context context){
        boolean value = false;

        File file = new File(context.getFilesDir().getAbsolutePath(), DB_NAME);
        if(isVersionDifferent(context)){
            if(file.exists()){
                file.delete();
            }
        }
        if(file.exists() && file.length() > 0){
            value = true;
        }else {
            try {
                InputStream is = context.getAssets().open("1.vc");
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

    private static boolean isVersionDifferent(Context context){
        boolean value = false;
        SharedPreferences prefs = context.getSharedPreferences("vachanas", Context.MODE_PRIVATE);
        int version = prefs.getInt("version", 0);
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            value = (version != info.versionCode);
            prefs.edit().putInt("version", info.versionCode).commit();
        }catch (PackageManager.NameNotFoundException nnfe){
            nnfe.printStackTrace();
        }
        return value;
    }

    private static SQLiteDatabase getDB(Context context){
        File file = new File(context.getFilesDir(), DB_NAME);
        SQLiteDatabase db = null;
        try{
            db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, 0);
        }catch (SQLiteException se){
            se.printStackTrace();
        }
        return db;
    }

    public static Cursor searchVachanakaara(Context context, String value){
        String selection = null;
        if(!TextUtils.isEmpty(value)){
            selection = COLUMN_NAME+" LIKE '%"+value+"%' OR "+COLUMN_DETAILS+" LIKE '%"+value+"%'";
        }
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        Cursor cursor = null;
        try{
            cursor = db.query(TABLE_VACHANAKAARAS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DETAILS}, selection, null, null, null, COLUMN_NAME);
        }catch (SQLiteException se){
            se.printStackTrace();
        }catch (IllegalArgumentException iae){
            iae.printStackTrace();
        }
        return cursor;
    }

    public static Cursor searchVachanas(Context context, String value){
        String selection = null;
        if(!TextUtils.isEmpty(value)){
            selection = COLUMN_VACHANA+" LIKE '%"+value+"%' OR "+COLUMN_NAME+" LIKE '%"+value+"%'";
        }
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        return db.query(TABLE_VACHANAAS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_VACHANA}, selection, null, null, null, COLUMN_NAME);
    }

    public static Cursor searchVachanas(Context context, String vachanakaara, String value){

        String selection = "";

        if(!TextUtils.isEmpty(vachanakaara)){
            selection = COLUMN_NAME+" LIKE '"+vachanakaara+"'";
        }

        if(!TextUtils.isEmpty(value)){
            if(!TextUtils.isEmpty(selection)){
                selection += " AND ";
            }
            selection += COLUMN_VACHANA+" LIKE '%"+value+"%'";
        }
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        return db.query(TABLE_VACHANAAS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_VACHANA}, selection, null, null, null, COLUMN_NAME);
    }
}
