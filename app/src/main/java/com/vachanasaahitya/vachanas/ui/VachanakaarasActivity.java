package com.vachanasaahitya.vachanas.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanakaarasActivity extends ListActivity {

    private static final String KEY_SORT_BY = "sort_by";

    private VachanakaarasAdapter mAdapter = new VachanakaarasAdapter();
    private DatabaseHelper.SortVachanaKaaras sortBy = DatabaseHelper.SortVachanaKaaras.BY_NAME;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        setListAdapter(mAdapter);
        Cursor cursor = mAdapter.getCursor("");
        mAdapter.swapCursor(cursor);
        sortBy = getSortBy();
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
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(mAdapter);
        searchView.setQueryHint(getString(R.string.search_hint_vachanakaara));
        searchView.setIconified(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        sortBy = getSortBy();
        menu.findItem(R.id.sort_by_name).setChecked(sortBy == DatabaseHelper.SortVachanaKaaras.BY_NAME);
        menu.findItem(R.id.sort_by_numbers).setChecked(sortBy == DatabaseHelper.SortVachanaKaaras.BY_NUMBERS);
        Cursor cursor = mAdapter.getCursor("");
        mAdapter.swapCursor(cursor);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.info){
            VachanaFragment vf = new VachanaFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VachanaFragment.EXTRA_VACHANA, getString(R.string.search_info_vachanakaararu));
            bundle.putString(VachanaFragment.EXTRA_SHEERSHIKE, getString(R.string.info_title));
            vf.setArguments(bundle);
            vf.show(getFragmentManager(), "search_hint");
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
        return super.onOptionsItemSelected(item);
    }

    private  View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Vachanakaara v = (Vachanakaara)view.getTag();
            if(v == null){
                return;
            }
            VachanakaaraFragment vf = new VachanakaaraFragment();
            Bundle b = new Bundle();
            b.putParcelable(VachanakaaraFragment.EXTRA_VACHANAKAARA, v);
            vf.setArguments(b);
            vf.show(getFragmentManager(), v.getName());
        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Vachanakaara vachanakaara = (Vachanakaara)v.getTag();
        if(vachanakaara != null){
            Intent intent = new Intent(this, VachanasActivity.class);
            intent.putExtra(VachanasActivity.EXTRA_PARAM_VACHANAKAARA, vachanakaara);
            startActivity(intent);
        }
    }

    private class VachanakaarasAdapter extends CursorAdapter implements SearchView.OnQueryTextListener, Filterable{

        public VachanakaarasAdapter() {
            super(VachanakaarasActivity.this, null, false);
        }

        private Cursor getCursor(String query){
            return DatabaseHelper.searchVachanakaara(VachanakaarasActivity.this, query, sortBy);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.item_vachanakaara, null);
            v.findViewById(R.id.vachanakaara_info).setOnClickListener(onClickListener);
            v.findViewById(R.id.vachanakaara_info).setFocusable(false);
            v.findViewById(R.id.vachanakaara_info).setFocusableInTouchMode(false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String details = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS));
            Vachanakaara v = new Vachanakaara(name, details);
            ((TextView)view.findViewById(R.id.vachanakaara_name)).setText(v.getName());
            ((TextView)view.findViewById(R.id.vachanakaara_details)).setText(details.replace("(ಆಧಾರ: ಸಮಗ್ರ ವಚನ ಸಂಪುಟ)", ""));
            view.findViewById(R.id.vachanakaara_info).setTag(v);
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
                    Cursor cursor = getCursor(constraint.toString());
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
