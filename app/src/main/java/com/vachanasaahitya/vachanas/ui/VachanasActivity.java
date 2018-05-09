package com.vachanasaahitya.vachanas.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.ui.adapters.VachanasAdapter;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanasActivity extends ListActivity implements VachanaFragment.OnVachanaViewCompleteListener{

    public static final String EXTRA_PARAM_VACHANAKAARA = "vachanakaara";
    public static final String EXTRA_PARAM_FAVORITE = "favorite";

    private VachanasAdapter mAdapter = null;
    private Vachanakaara mVachanakaara = null;
    private SearchView mSearchView = null;
    private boolean favorite = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        mVachanakaara = getIntent().getParcelableExtra(EXTRA_PARAM_VACHANAKAARA);
        favorite = getIntent().getBooleanExtra(EXTRA_PARAM_FAVORITE, false);
        mAdapter = new VachanasAdapter(this, mVachanakaara);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh(){
        String query = "";
        if(mSearchView != null){
            query = mSearchView.getQuery().toString();
        }
        mAdapter.setFavorite(favorite);
        Cursor cursor = mAdapter.getCursor(query);
        mAdapter.swapCursor(cursor);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Vachana vachana = (Vachana) v.getTag();
        if(vachana != null){
            VachanaFragment vf = new VachanaFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VachanaFragment.EXTRA_VACHANA, vachana.getVachana());
            bundle.putString(VachanaFragment.EXTRA_SHEERSHIKE, vachana.getName());
            bundle.putBoolean(VachanaFragment.EXTRA_IS_FAVORITE, vachana.isFavorite());
            bundle.putBoolean(VachanaFragment.EXTRA_IS_VACHANA, true);
            vf.setArguments(bundle);
            vf.setOnVachanaViewCompleteListener(this);
            vf.show(getFragmentManager(), "Vachana_details");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(mAdapter);
        mSearchView.setQueryHint(getString(R.string.search_hint_vachana));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.sort).setVisible(false);
        menu.findItem(R.id.favorite).setCheckable(true);
        menu.findItem(R.id.favorite).setChecked(favorite);
        menu.findItem(R.id.favorite).setIcon(favorite ? R.drawable.favorite_selected : R.drawable.favorite_normal);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.info){
            VachanaFragment vf = new VachanaFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VachanaFragment.EXTRA_SHEERSHIKE, getString(R.string.info_title));
            String info = String.format(getString(R.string.search_info_vachanagaLu), mVachanakaara.getName());
            bundle.putString(VachanaFragment.EXTRA_VACHANA, info);
            vf.setArguments(bundle);
            vf.show(getFragmentManager(), "search_hint");
            return true;
        }
        if(item.getItemId() == R.id.favorite){
            boolean isChecked = item.isChecked();
            isChecked = !isChecked;
            item.setChecked(isChecked);
            favorite = isChecked;
            refresh();
            item.setIcon(favorite ? R.drawable.favorite_selected : R.drawable.favorite_normal);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void done(Vachana vachana) {
        refresh();
    }
}
