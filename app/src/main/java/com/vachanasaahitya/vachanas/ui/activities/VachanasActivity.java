package com.vachanasaahitya.vachanas.ui.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.ui.adapters.VachanasAdapter;
import com.vachanasaahitya.vachanas.ui.bind.DetailsHolder;
import com.vachanasaahitya.vachanas.ui.events.VachanaItemEventListener;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanasActivity extends ListActivity implements VachanaItemEventListener {

    public static final String EXTRA_PARAM_VACHANAKAARA = "vachanakaara";
    public static final String EXTRA_PARAM_FAVORITE = "favorite";

    private VachanasAdapter mAdapter = null;
    private Vachanakaara mVachanakaara = null;
    private SearchView mSearchView = null;
    private boolean favorite = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        setContentView(R.layout.list);
        mVachanakaara = getIntent().getParcelableExtra(EXTRA_PARAM_VACHANAKAARA);
        favorite = getIntent().getBooleanExtra(EXTRA_PARAM_FAVORITE, false);
        mAdapter = new VachanasAdapter(this, mVachanakaara, this);
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
            String name = (mVachanakaara == null) ? getString(R.string.favorite_info) :  mVachanakaara.getName();
            DetailsHolder details = new DetailsHolder(String.format(getString(R.string.search_info_vachanagaLu), name), getString(R.string.info_title), false, false);
            startDetails(details);
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
    public void select(Vachana vachana) {
        DetailsHolder details = new DetailsHolder(vachana.getVachana(), vachana.getName(), vachana.isFavorite(), true);
        startDetails(details);
    }

    private void startDetails(DetailsHolder details){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_DETAILS, details);
        startActivity(intent);
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
