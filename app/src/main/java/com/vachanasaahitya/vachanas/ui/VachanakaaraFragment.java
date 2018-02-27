package com.vachanasaahitya.vachanas.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() != null){
            ((TextView)getView().findViewById(R.id.detail_vachana)).setText("");
        }
    }
}
