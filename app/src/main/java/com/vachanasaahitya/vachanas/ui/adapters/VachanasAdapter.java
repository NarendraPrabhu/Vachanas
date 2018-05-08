package com.vachanasaahitya.vachanas.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

public class VachanasAdapter extends CursorAdapter implements SearchView.OnQueryTextListener, Filterable {

    private Activity mActivity = null;
    private Vachanakaara mVachanakaara = null;
    private boolean favorite = false;
    public VachanasAdapter(Activity activity, Vachanakaara vachanakaara) {
        super(activity, null, false);
        this.mActivity = activity;
        this.mVachanakaara = vachanakaara;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Cursor getCursor(String query){
        Cursor cursor = null;
        if(mVachanakaara == null){
            cursor = DatabaseHelper.searchVachanas(mActivity, "", favorite);
        }else {
            cursor = DatabaseHelper.searchVachanas(mActivity, mVachanakaara.getName(), query, favorite);
        }
        return  cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = mActivity.getLayoutInflater().inflate(R.layout.item_vachana, null);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        String vachana = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_VACHANA));
        boolean favorite = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAVORITE)) == 1;
        Vachana v = new Vachana(name, vachana, favorite);
        ((TextView)view.findViewById(R.id.vachana_name)).setText(name);
        ((TextView)view.findViewById(R.id.vachana_vachana)).setText(vachana);
        ((CheckedTextView)view.findViewById(R.id.vachana_favorite)).setChecked(favorite);
        view.setTag(v);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(TextUtils.isEmpty(s)){
            Cursor cursor = getCursor("");
            swapCursor(cursor);
        }else {
            getFilter().filter(s);
        }
        return true;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Cursor cursor = DatabaseHelper.searchVachanas(mActivity, constraint.toString(), favorite);
                swapCursor(cursor);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return new FilterResults();
            }
        };
    }
}