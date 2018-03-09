package com.vachanasaahitya.vachanas.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.vachanasaahitya.vachanas.R;

/**
 * Created by narensmac on 01/03/18.
 */

public class LaunchActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        findViewById(R.id.launch_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaunchActivity.this, VachanakaarasActivity.class));
                finish();
            }
        });

        findViewById(R.id.launch_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crashlytics.getInstance().crash();
            }
        });
    }
}
