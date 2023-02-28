package com.codedev.demo;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetsAdapter extends BaseAdapter {
    public static int numOfSets;

    public SetsAdapter(int numOfSets) {
        this.numOfSets = numOfSets;
    }

    @Override
    public int getCount() {
        return numOfSets;
    }

    @Override
    public Object getItem( int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        if (view==null)
        {
            view1= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_item_layout,viewGroup,false);
        }
        else{
            view1=view;
        }
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(viewGroup.getContext(),infoActivity.class);
                intent.putExtra("SETNO",i);
                viewGroup.getContext().startActivity(intent);
            }
        });

        ((TextView) view1.findViewById(R.id.setNo_tv)).setText("Test "+ String.valueOf(i+1));
        return view1;
    }
}
