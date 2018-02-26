package com.vachanasaahitya.vachanas.db;

import android.content.Context;

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
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return value;
    }


}
