package com.vachanasaahitya.vachanas.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.databinding.VachanaDetailsItemBinding;
import com.vachanasaahitya.vachanas.databinding.VachanaDetailsLayoutBinding;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;
import com.vachanasaahitya.vachanas.ui.PageIndicatorView;
import com.vachanasaahitya.vachanas.ui.ShowMeaningCallback;
import com.vachanasaahitya.vachanas.ui.events.VachanaDetailsEventListener;

import java.util.List;

public class VachanaDetailsActivity extends BaseActivity implements VachanaDetailsEventListener, ViewPager.OnPageChangeListener{

    public static final String EXTRA_PARAM_VACHANA = VachanaDetailsActivity.class.getCanonicalName()+".VACHANA";
    public static final String EXTRA_PARAM_VACHANAKAARA = VachanaDetailsActivity.class.getCanonicalName()+".VACHANAKAARA";
    public static final String EXTRA_PARAM_FAVORITE = VachanaDetailsActivity.class.getCanonicalName()+".FAVORITE";
    public static final String EXTRA_PARAM_CURSOR_POSTION = VachanaDetailsActivity.class.getCanonicalName()+".CURSOR_POSTION";
    public static final String EXTRA_PARAM_QUERY = VachanaDetailsActivity.class.getCanonicalName()+".QUERY";

    private VachanaDetailsLayoutBinding mBinding;
    private Cursor mCursor = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = VachanaDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Vachana v = getIntent().getParcelableExtra(EXTRA_PARAM_VACHANA);
        Vachanakaara vk = getIntent().getParcelableExtra(EXTRA_PARAM_VACHANAKAARA);
        boolean isFavorite = getIntent().getBooleanExtra(EXTRA_PARAM_FAVORITE, false);
        final int cursorPosition = getIntent().getIntExtra(EXTRA_PARAM_CURSOR_POSTION, 0);
        String query = getIntent().getStringExtra(EXTRA_PARAM_QUERY);
        if(v == null){
            finish();
        }
        mBinding.setVachana(v);
        mBinding.setEvents(this);

        if(vk == null || !TextUtils.isEmpty(query)){
            mCursor = DatabaseHelper.searchVachanas(getApplicationContext(), query, isFavorite);
        }else {
            mCursor = DatabaseHelper.searchVachanas(getApplicationContext(), vk.getName(), query, isFavorite);
        }

        VachanaDetailsAdapter vda = new VachanaDetailsAdapter();
        mBinding.setAdapter(vda);
        final ViewPager pager = findViewById(R.id.vachana_details_pager);
        pager.post(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(cursorPosition, true);
            }
        });
        PageIndicatorView indicator = findViewById(R.id.indicator);
        pager.addOnPageChangeListener(indicator);
        pager.addOnPageChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        ((ViewPager)findViewById(R.id.vachana_details_pager)).removeOnPageChangeListener(this);
    }

    @Override
    public void share(Vachana vachana) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, String.format("%s\n-\n%s",vachana.getVachana(), vachana.getName()));
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
    public void copy(Vachana vachana) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(vachana.getName(), vachana.getVachana());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.vachana_copied, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void check(Vachana vachana, boolean b) {
        DatabaseHelper.setFavorite(this, vachana, b);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Vachana v = getVachana(position);
        mBinding.setVachana(v);
        setTitle(v.getName());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private Vachana getVachana(int position){
        mCursor.moveToPosition(position);
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        String vachana = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_VACHANA));
        boolean favorite = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COLUMN_FAVORITE)) == 1;
        Vachana v = new Vachana(name, vachana, favorite);
        return v;
    }

    private class VachanaDetailsAdapter extends PagerAdapter{

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            VachanaDetailsItemBinding binding = VachanaDetailsItemBinding.inflate(getLayoutInflater());
            binding.setVachana(getVachana(position));
            View v = binding.getRoot();
            container.addView(v);
            TextView tv = v.findViewById(R.id.detail_vachana);
            tv.setTextIsSelectable(true);
            ShowMeaningCallback meaningCallback = new ShowMeaningCallback(tv);
            tv.setCustomSelectionActionModeCallback(meaningCallback);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
