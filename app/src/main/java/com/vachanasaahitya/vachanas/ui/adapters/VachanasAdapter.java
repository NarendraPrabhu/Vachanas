package com.vachanasaahitya.vachanas.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.databinding.VachanaListItemBinding;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;
import com.vachanasaahitya.vachanas.ui.events.VachanaItemEventListener;

public class VachanasAdapter extends CursorAdapter implements SearchView.OnQueryTextListener, Filterable {

    private Activity mActivity = null;
    private Vachanakaara mVachanakaara = null;
    private boolean favorite = false;
    private VachanaItemEventListener eventListener = null;
    public VachanasAdapter(Activity activity, Vachanakaara vachanakaara, VachanaItemEventListener eventListener) {
        super(activity, null, false);
        this.mActivity = activity;
        this.mVachanakaara = vachanakaara;
        this.eventListener = eventListener;
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
        VachanaListItemBinding binding = VachanaListItemBinding.inflate(mActivity.getLayoutInflater());
        binding.setEvents(eventListener);
        return binding.getRoot();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        String vachana = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_VACHANA));
        boolean favorite = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAVORITE)) == 1;
        Vachana v = new Vachana(name, vachana, favorite);
        VachanaListItemBinding binding = DataBindingUtil.getBinding(view);
        binding.setVachana(v);
        binding.setCursorPosition(cursor.getPosition());
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