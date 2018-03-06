package com.vachanasaahitya.vachanas.ui;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

import org.w3c.dom.Text;

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

    private PopupWindow mPopupWindow = null;

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
            getDialog().setOnKeyListener(onKeyListener);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((TextView)getView().findViewById(R.id.detail_vachana)).setText(vachana);
        }
    }

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
                showMeanings(meanings, outputBounds.left, outputBounds.bottom);
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
        mPopupWindow.setElevation(10f);
        mPopupWindow.setHeight(value);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(color));
        ScrollView sv = new ScrollView(getActivity());
        TextView tv = new TextView(getActivity());
        tv.setTextSize(18);
        tv.setBackgroundColor(color);
        tv.setPadding(20,20,20,20);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        tv.setText(buffer.toString());
        sv.addView(tv, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPopupWindow.setContentView(sv);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(getView(), 0, x, y);
    }
}
