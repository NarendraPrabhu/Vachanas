package com.vachanasaahitya.vachanas.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.databinding.DetailsBinding;
import com.vachanasaahitya.vachanas.databinding.VachanakaaraDetailLayoutBinding;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;
import com.vachanasaahitya.vachanas.ui.PageIndicatorView;
import com.vachanasaahitya.vachanas.ui.bind.DetailsHolder;
import com.vachanasaahitya.vachanas.ui.utils.StringUtils;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanakaaraDetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    public interface VachanasOpener{
        void openVachanas(Vachanakaara vachanakaara);
    }

    public static final String EXTRA_VACHANAKAARA = VachanakaaraDetailsActivity.class.getCanonicalName()+".VACHANAKAARA";
    public static final String EXTRA_QUERY = VachanakaaraDetailsActivity.class.getCanonicalName()+".QUERY";
    public static final String EXTRA_CURSOR_POSITION = VachanakaaraDetailsActivity.class.getCanonicalName()+".CURSOR_POSITION";
    public static final String EXTRA_SORT_BY = VachanakaaraDetailsActivity.class.getCanonicalName()+".SORT_BY";

    private Cursor mCursor = null;
    private VachanakaaraDetailLayoutBinding mLayoutBinding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String query = getIntent().getStringExtra(EXTRA_QUERY);
        final int cursorPosition = getIntent().getIntExtra(EXTRA_CURSOR_POSITION, 0);
        Vachanakaara vk = getIntent().getParcelableExtra(EXTRA_VACHANAKAARA);
        int sortBy = getIntent().getIntExtra(EXTRA_SORT_BY, 0);

        mCursor = DatabaseHelper.searchVachanakaara(getApplicationContext(), query, DatabaseHelper.SortVachanaKaaras.values()[sortBy]);
        VachanakaaraDetailsAdapter adapter = new VachanakaaraDetailsAdapter();

        mLayoutBinding = VachanakaaraDetailLayoutBinding.inflate(getLayoutInflater());
        setContentView(mLayoutBinding.getRoot());
        mLayoutBinding.setAdapter(adapter);
        mLayoutBinding.setVachanakaara(vk);
        mLayoutBinding.setOpener(adapter);
        final ViewPager pager = findViewById(R.id.vachanakaara_details_pager);
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
    protected void onDestroy() {
        super.onDestroy();
        if(mCursor != null){
            mCursor.close();;
            mCursor = null;
        }
        ((ViewPager)findViewById(R.id.vachanakaara_details_pager)).removeOnPageChangeListener(this);
    }

    private DatabaseHelper.SortVachanaKaaras getSortBy(){
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int i = preferences.getInt(VachanakaarasActivity.KEY_SORT_BY, 0);
        return  DatabaseHelper.SortVachanaKaaras.values()[i];
    }

    private Vachanakaara getVachanakaara(int position){
        mCursor.moveToPosition(position);
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        String details = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS));
        Vachanakaara v = new Vachanakaara(name, details);
        return v;
    }

    private class VachanakaaraDetailsAdapter extends PagerAdapter implements VachanasOpener{

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            DetailsBinding binding = DetailsBinding.inflate(getLayoutInflater());
            Vachanakaara v = getVachanakaara(position);
            String d = StringUtils.vachanakaaraConsolidatedDetails(getApplicationContext(), v);
            DetailsHolder dh = new DetailsHolder(d, v.getName());
            binding.setDetails(dh);
            container.addView(binding.getRoot());
            return binding.getRoot();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        public void openVachanas(Vachanakaara vachanakaara){
            Intent intent = new Intent(getApplicationContext(), VachanasActivity.class);
            intent.putExtra(VachanasActivity.EXTRA_PARAM_VACHANAKAARA, vachanakaara);
            startActivity(intent);
            finish();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Vachanakaara vk = getVachanakaara(position);
        mLayoutBinding.setVachanakaara(vk);
        setTitle(vk.getName());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
