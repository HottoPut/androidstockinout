package com.example.puttaporn.stockmillimed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DataItemLotAdapter extends ArrayAdapter<GetDataItemLotInfo> {

    Context context;
    List<GetDataItemLotInfo> objects;

    public DataItemLotAdapter(@NonNull Context context, int resource, @NonNull List<GetDataItemLotInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.testbg_layout, parent, false);
        TextView textView = rowView.findViewById(R.id.tv_sptest);

        textView.setText(objects.get(position).batch_no);
        return rowView;
    }

    @Nullable
    @Override
    public GetDataItemLotInfo getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.testbg_layout, parent, false);
        TextView textView = rowView.findViewById(R.id.tv_sptest);

        textView.setText(objects.get(position).batch_no);
        return rowView;
    }
}

