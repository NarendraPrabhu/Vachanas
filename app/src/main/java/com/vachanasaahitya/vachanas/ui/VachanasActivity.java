package com.vachanasaahitya.vachanas.ui;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

public class VachanasActivity extends ListActivity {

    public static final String EXTRA_PARAM_VACHANAKAARA = "vachanakaara";

    private VachanasAdapter mAdapter = new VachanasAdapter();

    private Vachanakaara mVachanakaara = null;

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
        String vachana = (String)v.getTag();
        if(!TextUtils.isEmpty(vachana)){
            VachanaFragment vf = new VachanaFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VachanaFragment.EXTRA_VACHANA, vachana);
            vf.setArguments(bundle);
            vf.show(getFragmentManager(), "Vachana_details");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(mAdapter);
        return true;
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
            ((TextView)view.findViewById(R.id.vachana_name)).setText(name);
            ((TextView)view.findViewById(R.id.vachana_vachana)).setText(vachana);
            view.setTag(vachana);
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
