package com.vachanasaahitya.vachanas.ui;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanasActivity extends ListActivity {

    public static final String EXTRA_PARAM_VACHANAKAARA = "vachanakaara";

    private VachanasAdapter mAdapter = new VachanasAdapter();
    private Vachanakaara mVachanakaara = null;

    private SearchView mSearchView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVachanakaara = getIntent().getParcelableExtra(EXTRA_PARAM_VACHANAKAARA);
        if(mVachanakaara == null){
            finish();
        }
        setListAdapter(mAdapter);
        Cursor cursor = mAdapter.getCursor("");
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
            vf.setArguments(bundle);
            vf.show(getFragmentManager(), "Vachana_details");
        }
    }

    @Override
    public void onBackPressed() {
        if(closeSearchView()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && closeSearchView()){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean closeSearchView(){
        if(mSearchView != null && mSearchView.isIconified()){
            mSearchView.setIconified(false);
            return  true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(mAdapter);
        mSearchView.setOnCloseListener(onCloseListener);
        return true;
    }

    private SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            Cursor cursor = DatabaseHelper.searchVachanas(VachanasActivity.this, mVachanakaara.getName(), "");
            mAdapter.swapCursor(cursor);
            return false;
        }
    };

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
        return super.onOptionsItemSelected(item);
    }

    private class VachanasAdapter extends CursorAdapter implements SearchView.OnQueryTextListener, Filterable {

        public VachanasAdapter() {
            super(VachanasActivity.this, null, false);
        }

        private Cursor getCursor(String query){
            return DatabaseHelper.searchVachanas(VachanasActivity.this, mVachanakaara.getName(), query);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.item_vachana, null);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String vachana = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_VACHANA));
            Vachana v = new Vachana(name, vachana);
            ((TextView)view.findViewById(R.id.vachana_name)).setText(name);
            ((TextView)view.findViewById(R.id.vachana_vachana)).setText(vachana);
            view.setTag(v);
        }

        @Override
        public boolean onQueryTextSubmit(String s) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            getFilter().filter(s);
            return true;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    Cursor cursor = DatabaseHelper.searchVachanas(VachanasActivity.this, constraint.toString());
                    swapCursor(cursor);
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    return new FilterResults();
                }
            };
        }
    }

}
