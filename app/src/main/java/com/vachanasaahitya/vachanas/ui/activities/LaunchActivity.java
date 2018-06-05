package com.vachanasaahitya.vachanas.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;
import com.vachanasaahitya.vachanas.ui.ShowMeaningCallback;

/**
 * Created by narensmac on 01/03/18.
 */

public class LaunchActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        setData("...");
        findViewById(R.id.launch_enter).postDelayed(new Runnable() {
            @Override
            public void run() {
                setData(DatabaseHelper.getTodaysVachana(LaunchActivity.this));
            }
        }, 1000);
        findViewById(R.id.launch_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaunchActivity.this, VachanakaarasActivity.class));
                finish();
            }
        });
    }

    private void setData(String data){
        data = String.format(getString(R.string.entry_launch_info), data);
        TextView tv = findViewById(R.id.launch_text);
        tv.setText(data);
        ShowMeaningCallback meaningCallback = new ShowMeaningCallback(tv);
        tv.setCustomSelectionActionModeCallback(meaningCallback);
        tv.setTextIsSelectable(true);
    }
}
