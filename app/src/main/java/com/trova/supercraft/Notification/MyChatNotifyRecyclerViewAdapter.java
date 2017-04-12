package com.trova.supercraft.Notification;

/**
 * Created by Panchakshari on 5/10/2016.
 */

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.trova.supercraft.MyJobsActivity.myGlobals;

public class MyChatNotifyRecyclerViewAdapter extends RecyclerView.Adapter<MyChatNotifyRecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyChatNotifyRecyclerViewAdapter";
    private ArrayList<MyChatNotificationsRowItem> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jobId;
        TextView message;
        TextView dateTime;
        TextView messageCount;
        ImageView profileImage;
        LinearLayout notify;
        int id;
        MyChatNotificationsRowItem notification;

        public DataObjectHolder(View itemView) {
            super(itemView);
            jobId = (TextView) itemView.findViewById(R.id.job_id);
            message = (TextView) itemView.findViewById(R.id.message);
            dateTime = (TextView) itemView.findViewById(R.id.date_time);
            messageCount = (TextView) itemView.findViewById(R.id.message_count);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            notify = (LinearLayout) itemView.findViewById(R.id.notify_layout);

            itemView.setOnClickListener(this);
            notify.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyChatNotifyRecyclerViewAdapter(ArrayList<MyChatNotificationsRowItem> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mynotification_list_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        view.setTag(dataObjectHolder);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.notification = mDataset.get(position);

        String jobId = "Job : #00000" + mDataset.get(position).getJobId();
        holder.jobId.setText(jobId);

        Date dateObj = null;
        String jobTime = null;
        String date = mDataset.get(position).getDate();
        String time = mDataset.get(position).getTime();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            dateObj = sdf.parse(time);
            jobTime = new SimpleDateFormat("hh:mm a").format(dateObj).toUpperCase();
        } catch (final ParseException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        holder.dateTime.setText(date + " " + jobTime);
        holder.message.setText(mDataset.get(position).getMessage());
        holder.messageCount.setText(String.valueOf(mDataset.get(position).getMessageCount()));
        Bitmap bmp = mDataset.get(position).getProfileImage();
        if(bmp != null)
            holder.profileImage.setImageBitmap(mDataset.get(position).getProfileImage());
        holder.id = mDataset.get(position).getId();

        holder.jobId.setTypeface(myGlobals.centuryGothic);
        holder.dateTime.setTypeface(myGlobals.centuryGothic);
        holder.message.setTypeface(myGlobals.centuryGothic);
    }

    public void addItem(MyChatNotificationsRowItem dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void clearDataSet() {
        mDataset.clear();
    }

    public void addDataSet(ArrayList<MyChatNotificationsRowItem> myDataset) {
        mDataset.addAll(myDataset);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
