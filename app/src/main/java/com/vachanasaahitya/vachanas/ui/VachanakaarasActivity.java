package com.vachanasaahitya.vachanas.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class VachanakaarasActivity extends ListActivity {


    private VachanakaarasAdapter mAdapter = new VachanakaarasAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(mAdapter);
        Cursor cursor = mAdapter.getCursor("");
        mAdapter.swapCursor(cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(mAdapter);
        searchView.setIconified(false);
        return true;
    }

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
            return DatabaseHelper.searchVachanakaara(VachanakaarasActivity.this, query);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.item_vachanakaara, null);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String details = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS));
            Vachanakaara v = new Vachanakaara(name, details);
            ((TextView)view.findViewById(R.id.vachanakaara_name)).setText(v.getName());
            ((TextView)view.findViewById(R.id.vachanakaara_details)).setText(details);
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
