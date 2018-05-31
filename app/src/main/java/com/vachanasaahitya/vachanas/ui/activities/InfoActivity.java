package com.vachanasaahitya.vachanas.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.databinding.DetailsBinding;
import com.vachanasaahitya.vachanas.ui.ShowMeaningCallback;
import com.vachanasaahitya.vachanas.ui.bind.DetailsHolder;

/**
 * Created by narensmac on 26/02/18.
 */

public class InfoActivity extends Activity{

    public static final String EXTRA_DETAILS = "details";
    private DetailsHolder details;
    private DetailsBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        details = getIntent().getParcelableExtra(EXTRA_DETAILS);
        binding = DetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        TextView tv = view.findViewById(R.id.detail_vachana);
        tv.setCustomSelectionActionModeCallback(new ShowMeaningCallback(tv));
        setContentView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.setDetails(details);
        if(!TextUtils.isEmpty(details.getTitle())) {
            setTitle(details.getTitle());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
}
