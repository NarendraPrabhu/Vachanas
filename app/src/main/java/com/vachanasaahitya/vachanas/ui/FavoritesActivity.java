package com.vachanasaahitya.vachanas.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.data.Vachana;
import com.vachanasaahitya.vachanas.data.Vachanakaara;
import com.vachanasaahitya.vachanas.ui.adapters.VachanasAdapter;

public class FavoritesActivity extends ListActivity implements VachanaFragment.OnVachanaViewCompleteListener{


    public static final String EXTRA_PARAM_VACHANAKAARA = "vachanakaara";

    private VachanasAdapter mAdapter = null;
    private Vachanakaara vachanakaara = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        vachanakaara = getIntent().getParcelableExtra(EXTRA_PARAM_VACHANAKAARA);
        mAdapter = new VachanasAdapter(this, vachanakaara);
        mAdapter.setFavorite(true);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        vachanakaara = getIntent().getParcelableExtra(EXTRA_PARAM_VACHANAKAARA);
        mAdapter = new VachanasAdapter(this, vachanakaara);
        mAdapter.setFavorite(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh(){
        Cursor cursor = mAdapter.getCursor("");
        mAdapter.swapCursor(cursor);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Object o = v.getTag();
        if(o instanceof Vachana){
            Vachana vachana = (Vachana)o;
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
    public void done(Vachana vachana) {
        refresh();
    }
}
