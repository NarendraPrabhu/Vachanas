package com.vachanasaahitya.vachanas.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

public class VachanakaarasAdapter extends CursorAdapter implements SearchView.OnQueryTextListener, Filterable, View.OnClickListener{

    public interface OnVachanakaaraInfoClickListener{
        void onVachanakaaraInfoClick(Vachanakaara vachana);
    }

    private DatabaseHelper.SortVachanaKaaras sortBy = DatabaseHelper.SortVachanaKaaras.BY_NAME;
    private Activity mActivity = null;
    private OnVachanakaaraInfoClickListener mVachanakaaraInfoClickListener = null;

    public VachanakaarasAdapter(Activity activity, OnVachanakaaraInfoClickListener vachanakaaraInfoClickListener) {
        super(activity, null, false);
        this.mVachanakaaraInfoClickListener = vachanakaaraInfoClickListener;
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
        View v = mActivity.getLayoutInflater().inflate(R.layout.item_vachanakaara, null);
        v.findViewById(R.id.vachanakaara_info).setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        Vachanakaara v = (Vachanakaara)view.getTag();
        if(v == null || mVachanakaaraInfoClickListener == null){
            return;
        }
        mVachanakaaraInfoClickListener.onVachanakaaraInfoClick(v);

    }

}