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

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyChatNotifyRecyclerViewAdapter";
    private ArrayList<MyNotificationsRowItem> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jobId;
        TextView action;
        TextView dateTime;
        TextView notifyCount;
        ImageView profileImage;
        LinearLayout notify;
        int id;
        MyNotificationsRowItem notification;

        public DataObjectHolder(View itemView) {
            super(itemView);
            jobId = (TextView) itemView.findViewById(R.id.job_id);
            action = (TextView) itemView.findViewById(R.id.message);
            dateTime = (TextView) itemView.findViewById(R.id.date_time);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            notify = (LinearLayout) itemView.findViewById(R.id.notify_layout);
            notifyCount = (TextView)itemView.findViewById(R.id.message_count);
            //((TextView)itemView.findViewById(R.id.message_count)).setVisibility(View.GONE);

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

    public MyRecyclerViewAdapter(ArrayList<MyNotificationsRowItem> myDataset) {
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

        //String jobId = "Job : #00000" + mDataset.get(position).getJobId();
        String jobId = mDataset.get(position).getJobId();
        holder.jobId.setText(jobId);

        Date dateObj = null;
        String jobTime = null;
        String date = mDataset.get(position).getDate();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            dateObj = sdf.parse(date);
            jobTime = new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(dateObj).toUpperCase();
        } catch (final ParseException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }


        holder.dateTime.setText(jobTime);
        int action = mDataset.get(position).getAction();
        String actionStr = null;
        switch(action) {
            case 1 :
                actionStr = "Created New Job : #00000" + jobId;
                break;
            case 2 :
            //case 3 :
                actionStr = "Changed priority for Job : #00000" + jobId;
                break;
            case 3 :
                actionStr = "Approved Job : #00000" + jobId;
                break;
            case 4 :
                actionStr = "New DICOM Available for Job : #00000" + jobId;
                break;
            case 5 :
                actionStr = "New 3D Model Available for Job : #00000" + jobId;
                break;
            case 6 :
                break;
            case 7 :
                actionStr = "Accepted Your Job : #00000" + jobId;
                break;
            case 8 :
                break;
        }
        holder.action.setText(actionStr);
        holder.notifyCount.setVisibility(View.VISIBLE);
        int count = mDataset.get(position).getCount();
        if(count > 1) {
            holder.notifyCount.setText(String.valueOf(count));
        } else {
            holder.notifyCount.setVisibility(View.GONE);
        }
        Bitmap bmp = mDataset.get(position).getProfileImage();
        if(bmp != null)
            holder.profileImage.setImageBitmap(bmp);
        holder.id = mDataset.get(position).getId();

        holder.jobId.setTypeface(myGlobals.centuryGothic);
        holder.dateTime.setTypeface(myGlobals.centuryGothic);
        holder.action.setTypeface(myGlobals.centuryGothic);

    }

    public void addItem(MyNotificationsRowItem dataObj, int index) {
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

    public void addDataSet(ArrayList<MyNotificationsRowItem> myDataset) {
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
