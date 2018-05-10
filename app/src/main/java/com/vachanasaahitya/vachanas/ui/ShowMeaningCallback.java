package com.vachanasaahitya.vachanas.ui;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.vachanasaahitya.vachanas.R;
import com.vachanasaahitya.vachanas.db.DatabaseHelper;

import java.util.List;

public class ShowMeaningCallback implements ActionMode.Callback{

    private TextView mTextView = null;

    public ShowMeaningCallback(TextView textView){
        this.mTextView = textView;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        menu.add(0, 100001, 0, mTextView.getContext().getString(R.string.meanings));
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
            List<String> meanings = DatabaseHelper.findMeaning(mTextView.getContext(), selectedText.toString());

            Path selectionDestination = new Path();
            RectF selectionBounds = new RectF();
            Rect outputBounds = new Rect();
            mTextView.getLayout().getSelectionPath(min, max, selectionDestination);
            selectionDestination.computeBounds(selectionBounds, true /* this param is ignored */);
            selectionBounds.roundOut(outputBounds);
            if(meanings != null && meanings.size() > 0) {
                showMeanings(meanings, outputBounds.left, outputBounds.bottom);
            }else{
                Toast.makeText(mTextView.getContext(),R.string.word_warning, Toast.LENGTH_LONG).show();
            }
            actionMode.finish();
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }

    private void showMeanings(List<String> meanings, int x, int y){
        StringBuffer buffer = new StringBuffer();
        for(String value : meanings){
            buffer.append(value);
            buffer.append("\n\n");
        }
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, mTextView.getContext().getResources().getDisplayMetrics());
        int color = mTextView.getContext().getResources().getColor(R.color.color_list_bg);
        ScrollView sv = new ScrollView(mTextView.getContext());
        TextView tv = new TextView(mTextView.getContext());
        tv.setTextSize(18);
        tv.setPadding(20,20,20,20);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        tv.setText(buffer.toString());
        sv.setBackgroundColor(color);
        sv.addView(tv);

        PopupWindow popupWindow = new PopupWindow(mTextView.getContext());
        popupWindow.setWidth(value);
        popupWindow.setHeight(value);
        popupWindow.setContentView(sv);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(mTextView, 0, x, y);
    }
}