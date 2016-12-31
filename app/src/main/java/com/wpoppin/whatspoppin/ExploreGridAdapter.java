package com.wpoppin.whatspoppin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by joseph on 12/29/2016.
 *
 * This adapter feeds the Explore gridView with images that allow the user
 * to select an event category
 */

public class ExploreGridAdapter extends BaseAdapter {

    String [] result;
    Context context;
    int [] imageId;

    private static LayoutInflater inflater=null;
    public ExploreGridAdapter(Explore ex, String[] prgmNameList, int[] prgmImages) {

        result = prgmNameList;
        context = ex;
        imageId = prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        return result.length;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.explore_grid_layout, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);

        /*rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Toast.makeText(context, "You Clicked "+ result[position], Toast.LENGTH_LONG).show();
            }
        }); */

        return rowView;
    }

}