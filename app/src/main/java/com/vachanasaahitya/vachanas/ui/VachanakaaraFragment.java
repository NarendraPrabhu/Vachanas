package com.vachanasaahitya.vachanas.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachanakaara;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanakaaraFragment extends DialogFragment{

    public static final String EXTRA_VACHANAKAARA = "vachanakaara";
    private Vachanakaara vachanakaara = null;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        vachanakaara = (Vachanakaara)args.getParcelable(EXTRA_VACHANAKAARA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_vachana, null, false);
        ((TextView)view.findViewById(R.id.detail_vachana)).setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() != null){

            getView().findViewById(R.id.detail_tools).setVisibility(View.GONE);

            ((TextView)getView().findViewById(R.id.detail_vachana)).setTextIsSelectable(false);
            getDialog().setTitle(vachanakaara.getName());
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            StringBuffer buffer = new StringBuffer();
            if(!TextUtils.isEmpty(vachanakaara.getPenName())){
                buffer.append(String.format(getString(R.string.details_pen_name), vachanakaara.getPenName()));
            }
            if(!TextUtils.isEmpty(vachanakaara.getPeriod())){
                if(!TextUtils.isEmpty(buffer.toString())){
                    buffer.append("\n\n");
                }
                buffer.append(String.format(getString(R.string.details_birth_time), vachanakaara.getPeriod()));
            }
            if(!TextUtils.isEmpty(vachanakaara.getBirthPlace())){
                if(!TextUtils.isEmpty(buffer.toString())){
                    buffer.append("\n\n");
                }
                buffer.append(String.format(getString(R.string.details_place), vachanakaara.getBirthPlace()));
            }
            if(!TextUtils.isEmpty(vachanakaara.getParents())){
                if(!TextUtils.isEmpty(buffer.toString())){
                    buffer.append("\n\n");
                }
                buffer.append(String.format(getString(R.string.details_parents), vachanakaara.getParents()));
            }
            if(!TextUtils.isEmpty(vachanakaara.getObtainedNumbers())){
                if(!TextUtils.isEmpty(buffer.toString())){
                    buffer.append("\n\n");
                }
                buffer.append(String.format(getString(R.string.details_obtained), vachanakaara.getObtainedNumbers()));
            }
            if(!TextUtils.isEmpty(vachanakaara.getDetails())){
                if(!TextUtils.isEmpty(buffer.toString())){
                    buffer.append("\n\n");
                }
                buffer.append(String.format(getString(R.string.details_more), vachanakaara.getDetails()));
            }
            ((TextView)getView().findViewById(R.id.detail_vachana)).setText(buffer.toString());
        }
    }
}
