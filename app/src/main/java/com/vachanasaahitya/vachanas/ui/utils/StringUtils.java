package com.vachanasaahitya.vachanas.ui.utils;

import android.content.Context;
import android.text.TextUtils;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachanakaara;

public class StringUtils {
    
    public static String vachanakaaraConsolidatedDetails(Context context, Vachanakaara vachanakaara){
        StringBuffer buffer = new StringBuffer();
        if(!TextUtils.isEmpty(vachanakaara.getPenName())){
            buffer.append(String.format(context.getString(R.string.details_pen_name), vachanakaara.getPenName()));
        }
        if(!TextUtils.isEmpty(vachanakaara.getPeriod())){
            if(!TextUtils.isEmpty(buffer.toString())){
                buffer.append("\n\n");
            }
            buffer.append(String.format(context.getString(R.string.details_birth_time), vachanakaara.getPeriod()));
        }
        if(!TextUtils.isEmpty(vachanakaara.getBirthPlace())){
            if(!TextUtils.isEmpty(buffer.toString())){
                buffer.append("\n\n");
            }
            buffer.append(String.format(context.getString(R.string.details_place), vachanakaara.getBirthPlace()));
        }
        if(!TextUtils.isEmpty(vachanakaara.getParents())){
            if(!TextUtils.isEmpty(buffer.toString())){
                buffer.append("\n\n");
            }
            buffer.append(String.format(context.getString(R.string.details_parents), vachanakaara.getParents()));
        }
        if(!TextUtils.isEmpty(vachanakaara.getObtainedNumbers())){
            if(!TextUtils.isEmpty(buffer.toString())){
                buffer.append("\n\n");
            }
            buffer.append(String.format(context.getString(R.string.details_obtained), vachanakaara.getObtainedNumbers()));
        }
        if(!TextUtils.isEmpty(vachanakaara.getDetails())){
            if(!TextUtils.isEmpty(buffer.toString())){
                buffer.append("\n\n");
            }
            buffer.append(String.format(context.getString(R.string.details_more), vachanakaara.getDetails()));
        }
        return  buffer.toString();
    }
}
