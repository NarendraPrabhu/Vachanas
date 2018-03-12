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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by narensmac on 26/02/18.
 */

public class DatabaseHelper {

    public enum SortVachanaKaaras{
        BY_NAME,
        BY_NUMBERS
    }

    private static final int version = 1;
    private static final String DB_NAME = "1.vc";

    private static final String TABLE_VACHANAKAARAS = "Vachanakaaraas";
    private static final String TABLE_VACHANAAS = "Vachanas";
    private static final String TABLE_MEANINGS = "meanings";

    private static final String COLUMN_URL = "url";
    private static final String COLUMN_ID = "_id";

    public static final String COLUMN_VACHANA = "vachana";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DETAILS = "details";

    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_MEANING = "meaning";


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

    public static Cursor searchVachanakaara(Context context, String value, SortVachanaKaaras sort){
        Cursor cursor = null;
        if(sort == SortVachanaKaaras.BY_NAME) {
            String selection = null;
            if (!TextUtils.isEmpty(value)) {
                selection = COLUMN_NAME + " LIKE '%" + value + "%' OR " + COLUMN_DETAILS + " LIKE '%" + value + "%'";
            }
            SQLiteDatabase db = getDB(context);
            if (db == null) {
                return null;
            }
            try {
                cursor = db.query(TABLE_VACHANAKAARAS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DETAILS}, selection, null, null, null, COLUMN_NAME);
            } catch (SQLiteException se) {
                se.printStackTrace();
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        }else{
            cursor = searchVachanakaarasByNumbers(context, value);
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

    public static List<String> findMeaning(Context context, String word){
        List<String> meanings = new ArrayList<>();
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        String selection = COLUMN_WORD+" LIKE '%"+word+"%'";

        Cursor cursor = db.query(TABLE_MEANINGS, new String[]{COLUMN_WORD, COLUMN_MEANING}, selection, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                meanings.add(cursor.getString(0)+" : "+cursor.getString(1));
            }while (cursor.moveToNext());
            cursor.close();
        }
        cursor = null;
        return  meanings;
    }

    public static Cursor queryMeanings(Context context, String word){
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        String selection = COLUMN_WORD+" LIKE '%"+word+"%'";
        return db.query(TABLE_MEANINGS, new String[]{COLUMN_ID, COLUMN_WORD, COLUMN_MEANING}, selection, null, null, null, null);
    }

    private static Cursor searchVachanakaarasByNumbers(Context context, String search){
        if(TextUtils.isEmpty(search)){
            search = "";
        }else{
            search =  " where Vachanas.name LIKE '%"+search+"%' ";
        }
        String query = "select Vachanakaaraas._id, Vachanakaaraas.name, Vachanakaaraas.details, count(Vachanas.name) as counter from Vachanas inner join Vachanakaaraas on Vachanakaaraas.name=Vachanas.name"+search+" group by Vachanas.name order by counter desc;";
        SQLiteDatabase db = getDB(context);
        if(db == null){
            return null;
        }
        return db.rawQuery(query, null);
    }


    public static String getTodaysVachana(Context context){
        String vachana = "";

        SharedPreferences preferences = context.getSharedPreferences("Vachanas", Context.MODE_PRIVATE);
        int i = preferences.getInt(TABLE_VACHANAKAARAS, -1);
        ++i;
        String name = null;
        Cursor cursor = searchVachanakaara(context, "", SortVachanaKaaras.BY_NAME);
        if(cursor != null && cursor.moveToFirst()){
            int count = cursor.getCount();
            i = i%count;
            cursor.move(i);

            preferences.edit().putInt(TABLE_VACHANAKAARAS, i).commit();

            name =cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            cursor.close();
        }
        if(TextUtils.isEmpty(name)){
            return vachana;
        }

        i = preferences.getInt(name, -1);
        ++i;
        cursor = searchVachanas(context, name, "");
        if(cursor != null && cursor.moveToFirst()){
            int count = cursor.getCount();
            i = i%count;
            cursor.move(i);

            preferences.edit().putInt(name, i).commit();

            vachana = cursor.getString(cursor.getColumnIndex(COLUMN_VACHANA));
            vachana += "\n-\n"+name+"\n\n";
            cursor.close();
        }

        return vachana;
    }

}
