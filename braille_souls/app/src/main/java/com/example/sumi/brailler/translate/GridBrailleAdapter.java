package com.example.sumi.brailler.translate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridBrailleAdapter extends BaseAdapter{
    private final Context context;
    private final String[] brailleCodes;

    public GridBrailleAdapter(Context context, String[] brailleCodes) {
        this.context = context;
        this.brailleCodes = brailleCodes;
    }

    @Override
    public int getCount() {
        return brailleCodes.length;
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
        outBraille.setText(brailleCodes[position]);
        return outBraille;
    }
}
