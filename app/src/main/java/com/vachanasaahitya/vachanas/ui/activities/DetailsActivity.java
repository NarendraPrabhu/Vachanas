package com.vachanasaahitya.vachanas.ui.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.databinding.DetailsBinding;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;
import com.vachanasaahitya.vachanas.ui.ShowMeaningCallback;
import com.vachanasaahitya.vachanas.ui.bind.DetailsHolder;
import com.vachanasaahitya.vachanas.ui.events.DetailsEventListener;

import java.util.List;

/**
 * Created by narensmac on 26/02/18.
 */

public class DetailsActivity extends Activity implements DetailsEventListener{

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
        binding.setEvents(this);
        binding.setDetails(details);
        if(!TextUtils.isEmpty(details.getTitle())) {
            setTitle(details.getTitle());
        }
        if(details.isVachana()) {
            AutoCompleteTextView searchView = (AutoCompleteTextView) findViewById(R.id.detail_search_word);
            String[] cursorMapping = new String[]{DatabaseHelper.COLUMN_WORD, DatabaseHelper.COLUMN_MEANING};
            int[] views = new int[]{R.id.detail_search_entry_word, R.id.detail_search_entry_meaning};
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.detail_word_search_entry, null, cursorMapping, views);
            cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {
                    return DatabaseHelper.queryMeanings(getApplicationContext(), charSequence.toString());
                }
            });
            searchView.setAdapter(cursorAdapter);
        }
    }

    @Override
    public void share(DetailsHolder details) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, getData(details));
        PackageManager pm = getPackageManager();
        if(pm != null){
            List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
            if(infos == null){
                Toast.makeText(this, R.string.warning_no_app, Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(Intent.createChooser(intent, getString(R.string.select_app)));
        }
    }

    @Override
    public void copy(DetailsHolder details) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(details.getTitle(), getData(details));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.vachana_copied, Toast.LENGTH_SHORT).show();
    }

    private String getData(DetailsHolder details){
        return String.format("%s\n-\n%s",details.getInfo(), details.getTitle());
    }

    @Override
    public void check(DetailsHolder details, boolean b) {
        DatabaseHelper.setFavorite(this, new Vachana(details.getTitle(), details.getInfo(), details.isFavorite()), b);
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
