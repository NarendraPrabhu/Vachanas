package com.vachanasaahitya.vachanas.ui;

import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

import java.util.List;

/**
 * Created by narensmac on 26/02/18.
 */

public class VachanaFragment extends DialogFragment{

    public static final String EXTRA_VACHANA = "vachana";
    public static final String EXTRA_SHEERSHIKE = "sheershike";
    public static final String EXTRA_IS_VACHANA = "is_vachana";
    private String vachana = null;
    private String title = null;
    private boolean isVachana = false;

    private PopupWindow mPopupWindow = null;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        vachana = args.getString(EXTRA_VACHANA);
        title = args.getString(EXTRA_SHEERSHIKE);
        isVachana = args.getBoolean(EXTRA_IS_VACHANA);
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
            getView().findViewById(R.id.detail_tools).setVisibility(isVachana ? View.VISIBLE : View.GONE);
            if(isVachana) {

                getView().findViewById(R.id.detail_tools_copy).setOnClickListener(onClickListener);
                getView().findViewById(R.id.detail_tools_share).setOnClickListener(onClickListener);

                AutoCompleteTextView searchView = (AutoCompleteTextView) getView().findViewById(R.id.detail_search_word);
                String[] cursorMapping = new String[]{DatabaseHelper.COLUMN_WORD, DatabaseHelper.COLUMN_MEANING};
                int[] views = new int[]{R.id.detail_search_entry_word, R.id.detail_search_entry_meaning};
                SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.detail_word_search_entry, null, cursorMapping, views);
                cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                    @Override
                    public Cursor runQuery(CharSequence charSequence) {
                        return DatabaseHelper.queryMeanings(getActivity(), charSequence.toString());
                    }
                });
                searchView.setAdapter(cursorAdapter);
            }
            ((TextView)getView().findViewById(R.id.detail_vachana)).setTextIsSelectable(isVachana);
            if(!TextUtils.isEmpty(title)) {
                getDialog().setTitle(title);
            }
            setStyle(DialogFragment.STYLE_NORMAL, 0);
            getDialog().setOnKeyListener(onKeyListener);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((TextView)getView().findViewById(R.id.detail_vachana)).setText(vachana);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String data = vachana+"\n-\n"+title;
            switch (view.getId()){
            case R.id.detail_tools_copy:
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(title, data);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), R.string.vachana_copied, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.detail_tools_share:
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_TEXT, data);
                    PackageManager pm = getActivity().getPackageManager();
                    if(pm != null){
                        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
                        if(infos == null){
                            Toast.makeText(getActivity(), R.string.warning_no_app, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(Intent.createChooser(intent, getString(R.string.select_app)));
                    }
                    break;
            }
        }
    };

    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if(keyEvent.getAction() == KeyEvent.KEYCODE_BACK && mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
                return  true;
            }
            return false;
        }
    };

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
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if(menuItem.getItemId() == 100001) {
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

                Path selectionDestination = new Path();
                RectF selectionBounds = new RectF();
                Rect outputBounds = new Rect();
                mTextView.getLayout().getSelectionPath(min, max, selectionDestination);
                selectionDestination.computeBounds(selectionBounds, true /* this param is ignored */);
                selectionBounds.roundOut(outputBounds);
                if(meanings != null && meanings.size() > 0) {
                    showMeanings(meanings, outputBounds.left, outputBounds.bottom);
                }else{
                    Toast.makeText(getActivity(),R.string.word_warning, Toast.LENGTH_LONG).show();
                }
                actionMode.finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }

    private void showMeanings(List<String> meanings, int x, int y){
        StringBuffer buffer = new StringBuffer();
        for(String value : meanings){
            buffer.append(value);
            buffer.append("\n\n");
        }
        int value = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getActivity().getResources().getDisplayMetrics());
        int color = getActivity().getResources().getColor(R.color.color_list_bg);
        mPopupWindow = new PopupWindow(getActivity());
        mPopupWindow.setWidth(value);
        mPopupWindow.setHeight(value);
        ScrollView sv = new ScrollView(getActivity());
        TextView tv = new TextView(getActivity());
        tv.setTextSize(18);
        tv.setPadding(20,20,20,20);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        tv.setText(buffer.toString());
        sv.setBackgroundColor(color);
        sv.addView(tv);
        mPopupWindow.setContentView(sv);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(getView(), 0, x, y);
    }
}
