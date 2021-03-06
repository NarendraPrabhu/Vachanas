package com.vachanasaahitya.vachanas.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;
import com.vachanasaahitya.vachanas.ui.adapters.VachanakaarasAdapter;
import com.vachanasaahitya.vachanas.ui.bind.DetailsHolder;
import com.vachanasaahitya.vachanas.ui.events.VachanakaaraItemEventListener;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanakaarasActivity extends BaseListActivity implements VachanakaaraItemEventListener {

    public static final String KEY_SORT_BY = "sort_by";

    private VachanakaarasAdapter mVachanakaarasAdapter = null;
    private SearchView mSearchView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        mVachanakaarasAdapter = new VachanakaarasAdapter(this, this);
        setListAdapter(mVachanakaarasAdapter);
        Cursor cursor = mVachanakaarasAdapter.getCursor("");
        mVachanakaarasAdapter.swapCursor(cursor);
    }

    private DatabaseHelper.SortVachanaKaaras getSortBy(){
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int i = preferences.getInt(KEY_SORT_BY, 0);
        return  DatabaseHelper.SortVachanaKaaras.values()[i];
    }

    private void setSortBy(DatabaseHelper.SortVachanaKaaras sortBy){
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_SORT_BY, DatabaseHelper.SortVachanaKaaras.BY_NAME == sortBy ? 0 : 1).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(mVachanakaarasAdapter);
        mSearchView.setQueryHint(getString(R.string.search_hint_vachanakaara));
        mSearchView.setIconified(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DatabaseHelper.SortVachanaKaaras sortBy = getSortBy();
        menu.findItem(R.id.sort_by_name).setChecked(sortBy == DatabaseHelper.SortVachanaKaaras.BY_NAME);
        menu.findItem(R.id.sort_by_numbers).setChecked(sortBy == DatabaseHelper.SortVachanaKaaras.BY_NUMBERS);
        mVachanakaarasAdapter.setSortBy(sortBy);
        Cursor cursor = mVachanakaarasAdapter.getCursor("");
        mVachanakaarasAdapter.swapCursor(cursor);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.info){
            DetailsHolder details = new DetailsHolder(getString(R.string.search_info_vachanakaararu), getString(R.string.info_title));
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra(InfoActivity.EXTRA_DETAILS, details);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.sort_by_name){
            setSortBy(DatabaseHelper.SortVachanaKaaras.BY_NAME);
            invalidateOptionsMenu();
            return true;
        }
        if(item.getItemId() == R.id.sort_by_numbers){
            setSortBy(DatabaseHelper.SortVachanaKaaras.BY_NUMBERS);
            invalidateOptionsMenu();
            return true;
        }
        if(item.getItemId() == R.id.favorite){
            Intent intent = new Intent(this, VachanasActivity.class);
            intent.putExtra(VachanasActivity.EXTRA_PARAM_FAVORITE, true);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void info(Vachanakaara vachanakaara, int cursorPosition) {
        Intent intent = new Intent(this, VachanakaaraDetailsActivity.class);
        String query = "";
        if(mSearchView != null){
            query = mSearchView.getQuery().toString();
        }
        intent.putExtra(VachanakaaraDetailsActivity.EXTRA_VACHANAKAARA, vachanakaara);
        intent.putExtra(VachanakaaraDetailsActivity.EXTRA_CURSOR_POSITION, cursorPosition);
        intent.putExtra(VachanakaaraDetailsActivity.EXTRA_QUERY, query);
        intent.putExtra(VachanakaaraDetailsActivity.EXTRA_SORT_BY, getSortBy().ordinal());
        startActivity(intent);
    }

    @Override
    public void select(Vachanakaara vachanakaara) {
        Intent intent = new Intent(this, VachanasActivity.class);
        intent.putExtra(VachanasActivity.EXTRA_PARAM_VACHANAKAARA, vachanakaara);
        startActivity(intent);
    }
}
