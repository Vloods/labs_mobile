package ru.tishin.databaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ArrayList<String>> {
    private final int resourceLayout;
    private final Context mContext;

    public ListAdapter(Context context, int resource, ArrayList<ArrayList<String>> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(resourceLayout, null);
        }

        ArrayList<String> p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) view.findViewById(R.id.textView);
            TextView tt2 = (TextView) view.findViewById(R.id.textView2);

            if (tt1 != null) {
                tt1.setText("Фамилия: " + p.get(0) + "\n");
            }

            if (tt2 != null) {
                tt2.setText("Имя: " + p.get(1) + "\n");
            }
        }

        return view;
    }

}
