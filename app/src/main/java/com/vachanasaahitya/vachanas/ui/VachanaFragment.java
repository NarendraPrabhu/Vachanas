package com.vachanasaahitya.vachanas.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanaFragment extends DialogFragment{

    public static final String EXTRA_VACHANA = "vachana";
    public static final String EXTRA_SHEERSHIKE = "sheershike";
    private String vachana = null;
    private String title = null;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        vachana = args.getString(EXTRA_VACHANA);
        title = args.getString(EXTRA_SHEERSHIKE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_vachana, null, false);
        TextView tv = (TextView)view.findViewById(R.id.detail_vachana);
        tv.setCustomSelectionActionModeCallback(new ShowMeaningCallback(tv));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() != null){
            if(!TextUtils.isEmpty(title)) {
                getDialog().setTitle(title);
            }
            setStyle(DialogFragment.STYLE_NORMAL, 0);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((TextView)getView().findViewById(R.id.detail_vachana)).setText(vachana);
        }
    }

    private class ShowMeaningCallback implements ActionMode.Callback{

        private TextView mTextView = null;

        private ShowMeaningCallback(TextView textView){
            this.mTextView = textView;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            menu.add(0, 100001, 0, getString(R.string.meanings));
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            List<Integer> ids = new ArrayList<>();
            for(int i = 0; i < menu.size(); i++){
                if(menu.getItem(i).getItemId() != 100001){
                    ids.add(menu.getItem(i).getItemId());
                }
            }
            for(int id : ids){
                menu.removeItem(id);
            }
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int min = 0;
            int max = mTextView.getText().length();
            if (mTextView.isFocused()) {
                final int selStart = mTextView.getSelectionStart();
                final int selEnd = mTextView.getSelectionEnd();

                min = Math.max(0, Math.min(selStart, selEnd));
                max = Math.max(0, Math.max(selStart, selEnd));
            }
            final CharSequence selectedText = mTextView.getText().subSequence(min, max);
            List<String> meanings = DatabaseHelper.findMeaning(getActivity(), selectedText.toString());
            showMeanings(meanings);
            actionMode.finish();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }

    private void showMeanings(List<String> meanings){
        StringBuffer buffer = new StringBuffer();
        for(String value : meanings){
            buffer.append(value);
            buffer.append("\n\n");
        }
        VachanaFragment fragment = new VachanaFragment();
        Bundle b = new Bundle();
        b.putString(VachanaFragment.EXTRA_SHEERSHIKE, getString(R.string.meanings));
        b.putString(VachanaFragment.EXTRA_VACHANA, buffer.toString());
        fragment.setArguments(b);
        fragment.show(getFragmentManager(), "meanings");
    }

}
