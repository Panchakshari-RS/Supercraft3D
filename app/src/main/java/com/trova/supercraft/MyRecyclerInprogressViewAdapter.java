package com.trova.supercraft;

/**
 * Created by Panchakshari on 5/10/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.logInfo;

public class MyRecyclerInprogressViewAdapter extends RecyclerView.Adapter<MyRecyclerInprogressViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerInprogressViewAdapter";
    private ArrayList<MyJobsRowItem> mDataset;
    private Context mContext;
    private static MyClickListener myClickListener;
    String content ="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic type setting, remaining essentially unchanged.";

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView jobId;
        TextView patientName;
        TextView patientId;
        TextView dateTime;
        TextView content;
        TextView more;
        //TextView status;
        TextView less;
        ImageView image;
        int id;
        MyJobs myJobs;

        public DataObjectHolder(View itemView) {
            super(itemView);
            jobId = (TextView) itemView.findViewById(R.id.job_id);
            patientName = (TextView) itemView.findViewById(R.id.patient_name);
            patientId = (TextView) itemView.findViewById(R.id.patient_id);
            dateTime = (TextView) itemView.findViewById(R.id.job_time);
            content = (TextView) itemView.findViewById(R.id.content);
            more = (TextView) itemView.findViewById(R.id.more);
            //status = (TextView) itemView.findViewById(R.id.curr_status);
            less = (TextView) itemView.findViewById(R.id.less);
            image = (ImageView) itemView.findViewById(R.id.profile_image);

            more.setOnClickListener(this);
            less.setOnClickListener(this);
            ((LinearLayout) itemView.findViewById(R.id.layout_primary)).setOnClickListener(this);
            ((LinearLayout) itemView.findViewById(R.id.layout_primary)).setOnLongClickListener(this);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            return myClickListener.onItemLongClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerInprogressViewAdapter(Context myContext, ArrayList<MyJobsRowItem> myDataset) {
        mContext = myContext;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myjob_list_item, parent, false);

        CardView cardView = (CardView) view.findViewById(R.id.card_view);
        cardView.setCardElevation(0);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        view.setTag(dataObjectHolder);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        String boldText = "JobId :";
        String normalText = " #000000" +  mDataset.get(position).getJobId();
        SpannableStringBuilder str = new SpannableStringBuilder(boldText + normalText);
        str.setSpan(new RelativeSizeSpan(0.75f), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //str.setSpan(new AbsoluteSizeSpan(textSize), 0, boldText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.BLACK), 0, boldText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.jobId.setText(str);
        holder.dateTime.setText(mDataset.get(position).getStrDate());
        holder.patientName.setText(mDataset.get(position).getPatientName());
        //holder.patientName.setText("Panchakshari Ramaiah Sreenivasa Murthy");
        holder.patientId.setText("PAT000000" + mDataset.get(position).getPatientId());
        final String content = mDataset.get(position).getContent();
        holder.content.setText(content);
        //holder.content.setText(content);
        //holder.status.setText(String.valueOf(mDataset.get(position).getStatus()));
        holder.image.setImageResource(R.drawable.patient_image);
        holder.id = mDataset.get(position).getId();
        holder.myJobs = mDataset.get(position).getMyJobs();

        holder.jobId.setTypeface(myGlobals.centuryGothic);
        holder.patientName.setTypeface(myGlobals.centuryGothic);
        holder.patientId.setTypeface(myGlobals.centuryGothic);
        holder.dateTime.setTypeface(myGlobals.centuryGothic);
        holder.content.setTypeface(myGlobals.centuryGothic);
        holder.more.setTypeface(myGlobals.centuryGothic);
        holder.less.setTypeface(myGlobals.centuryGothic);

        holder.content.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = holder.content.getLineCount();
                logInfo("lineCount", lineCount);
                if((content != null) && (!content.equals("")) && (!content.equals("null")) && (content.length() > 0) && (lineCount > 2)) {
                    if (MyInprogressJobsTabFragment.flag[position] == 0) {
                        holder.content.setMaxLines(2);
                        holder.less.setVisibility(View.INVISIBLE);
                        holder.more.setVisibility(View.VISIBLE);
                    } else {
                        holder.content.setMaxLines(Integer.MAX_VALUE);
                        holder.less.setVisibility(View.VISIBLE);
                        holder.more.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.less.setVisibility(View.GONE);
                    holder.more.setVisibility(View.GONE);
                }
            }
        });
    }

    public void addItem(MyJobsRowItem dataObj, int index) {
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

    public void addDataSet(ArrayList<MyJobsRowItem> myDataset) {
        mDataset.addAll(myDataset);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public boolean onItemLongClick(int position, View v);
    }
}
