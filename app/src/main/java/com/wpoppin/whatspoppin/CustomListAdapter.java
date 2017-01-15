package com.wpoppin.whatspoppin;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Post> Items) {
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
            convertView = inflater.inflate(R.layout.list_view, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        //TextView author = (TextView) convertView.findViewById(R.id.author);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        ExpandableTextView description = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        ImageButton save = (ImageButton) convertView.findViewById(R.id.save);
        ImageButton share = (ImageButton) convertView.findViewById(R.id.share);


        // getting movie data for the row
        final Post m = Items.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getImage(), imageLoader);

      //  author.setText("By " + m.getAuthor());
        title.setText(m.getTitle());
        if(m.getPrice() == 0){
            price.setText("Free");
        }

        else if(m.getTicket_link() != null){
            price.setText("$" + String.valueOf(m.getPrice()));
        }

        else {
            price.setText(String.valueOf(m.getPrice()));
        }

        description.setText(m.getDescription());

        String dateString = m.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("  EE'\n'MMM d");
        String newFormat = formatter.format(convertedDate);


        String timeString = m.getTime();
        Date dateObj = new Date();
        try {
             SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
             dateObj = sdf.parse(timeString);

        } catch ( ParseException e) {
            e.printStackTrace();
        }

        final String outputTime = new SimpleDateFormat("h:mm aa").format(dateObj);

        date.setText(newFormat);
        address.setText(m.getAddress() );
        time.setText(outputTime.toString());


        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, outputTime);
                //intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                intent.putExtra(CalendarContract.Events.TITLE, m.getTitle());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, m.getDescription());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, m.getAddress() +", " + m.getCity() + ", " + m.getState());
                intent.putExtra(CalendarContract.Events.RRULE, false);

                v.getContext().startActivity(intent);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = m.getDescription();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "What'sPoppin event: " + m.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

        // + ", " + m.getCity() + ", " + m.getState()

        return convertView;
    }

}