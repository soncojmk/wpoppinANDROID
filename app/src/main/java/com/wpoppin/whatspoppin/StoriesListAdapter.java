package com.wpoppin.whatspoppin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import static com.wpoppin.whatspoppin.CustomListAdapter.fixEncoding;

/**
 * Created by joseph on 1/14/2017.
 */

public class StoriesListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<EventClass> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public StoriesListAdapter(Activity activity, List<EventClass> Items) {
        this.activity = activity;
        this.Items = Items;
    }



    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int location) {
        return Items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.stories_list, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView author = (TextView) convertView.findViewById(R.id.author);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        ExpandableTextView description = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
        ImageButton share = (ImageButton) convertView.findViewById(R.id.share);


        // getting movie data for the row
        final EventClass m = Items.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getImage(), imageLoader);

        author.setText("By " + m.getAuthor());

        final String TITLE = fixEncoding(m.getTitle());
        title.setText(TITLE);

        String s =  fixEncoding(m.getDescription());
        description.setText(s);




        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = m.getBlogShareUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Hey, I think you'd like this What'sPoppin Story");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "What'sPoppin Story: " + TITLE + '\n' + m.getBlogShareUrl());
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

        // + ", " + m.getCity() + ", " + m.getState()

        return convertView;
    }

}