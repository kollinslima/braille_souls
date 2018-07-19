package com.braille.souls.brailler.translate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridBrailleAdapter extends BaseAdapter{
    private final Context context;
    private final ArrayList<String> brailleCodes;

    public GridBrailleAdapter(Context context, ArrayList<String> brailleCodes) {
        this.context = context;
        this.brailleCodes = brailleCodes;
    }

    @Override
    public int getCount() {
        return brailleCodes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView outBraille = new TextView(context);
        outBraille.setText(brailleCodes.get(position));
        return outBraille;
    }
}
