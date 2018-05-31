package com.vachanasaahitya.vachanas.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.databinding.VachanakaaraListItemBinding;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;
import com.vachanasaahitya.vachanas.ui.events.VachanakaaraItemEventListener;

public class VachanakaarasAdapter extends CursorAdapter implements SearchView.OnQueryTextListener, Filterable{

    private DatabaseHelper.SortVachanaKaaras sortBy = DatabaseHelper.SortVachanaKaaras.BY_NAME;
    private Activity mActivity = null;
    private VachanakaaraItemEventListener eventListener = null;

    public VachanakaarasAdapter(Activity activity, VachanakaaraItemEventListener eventListener) {
        super(activity, null, false);
        this.eventListener = eventListener;
        this.mActivity = activity;
    }

    public Cursor getCursor(String query){
        return DatabaseHelper.searchVachanakaara(mActivity, query, sortBy);
    }

    public void setSortBy(DatabaseHelper.SortVachanaKaaras sortBy) {
        this.sortBy = sortBy;
    }

    public DatabaseHelper.SortVachanaKaaras sortBy() {
        return sortBy;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        VachanakaaraListItemBinding binding = VachanakaaraListItemBinding.inflate(mActivity.getLayoutInflater());
        binding.setEvents(eventListener);
        return binding.getRoot();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        String details = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS));
        Vachanakaara v = new Vachanakaara(name, details);
        VachanakaaraListItemBinding binding = DataBindingUtil.getBinding(view);
        binding.setVachanakaara(v);
        binding.setCursorPosition(cursor.getPosition());
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