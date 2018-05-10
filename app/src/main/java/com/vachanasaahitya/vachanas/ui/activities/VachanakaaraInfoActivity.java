package com.vachanasaahitya.vachanas.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.databinding.DetailsBinding;
import com.vachanasaahitya.vachanas.ui.bind.DetailsHolder;
import com.vachanasaahitya.vachanas.ui.utils.StringUtils;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanakaaraInfoActivity extends Activity{

    public static final String EXTRA_VACHANAKAARA = "vachanakaara";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        Vachanakaara vachanakaara = getIntent().getParcelableExtra(EXTRA_VACHANAKAARA);
        DetailsBinding binding = DetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ((TextView)findViewById(R.id.detail_vachana)).setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
        DetailsHolder details = new DetailsHolder(StringUtils.vachanakaaraConsolidatedDetails(this, vachanakaara), vachanakaara.getName(), false, false);
        binding.setDetails(details);
        setTitle(vachanakaara.getName());
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
