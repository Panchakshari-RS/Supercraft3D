package com.trova.supercraft;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.downloadProfileImage;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.TrovaMessageCallback.fetchJobFileVersions;


/**
 * Created by Trova on 11/2/2016.
 */
public class MyNewJobsTabFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static Context context;
    private static Activity activity;
    private static MyRecyclerNewViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView mRecyclerView = null;
    private boolean done = false;
    public static int[] flag = null, selected = null;
    private static MySwipeRefreshLayout mySwipeRefreshLayout;
    private static String jobId = "";
    private static Handler handler = null;
    private static boolean success = false;
    private static ProgressDialog progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logInfo("MyNewJobsTabFragment", "0 .........");
        View contentview=inflater.inflate(R.layout.jobs_card_view, container, false);
        context = this.getContext();
        activity = this.getActivity();
        logInfo("MyNewJobsTabFragment", "1 .........");

        mySwipeRefreshLayout = (MySwipeRefreshLayout) contentview.findViewById(R.id.my_swipe_refresh_layout);
        logInfo("MyNewJobsTabFragment", "1.1 .........");
        mySwipeRefreshLayout.setOnRefreshListener (new MySwipeRefreshLayoutListener ());
        logInfo("MyNewJobsTabFragment", "1.1 .........");

        mRecyclerView = (RecyclerView) contentview.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        logInfo("MyNewJobsTabFragment", "2 .........");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerNewViewAdapter(context, getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        logInfo("MyNewJobsTabFragment", "3 .........");
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
        logInfo("MyNewJobsTabFragment", "4 .........");

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        logInfo("MyNewJobsTabFragment", "5 .........");

        ((MyRecyclerNewViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerNewViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                logInfo("MyNewJobsTabFragment onItemClick", position);
                MyRecyclerNewViewAdapter.DataObjectHolder vh = (MyRecyclerNewViewAdapter.DataObjectHolder) mRecyclerView.findViewHolderForAdapterPosition(position);

                if (flag[position] == 1)
                    flag[position] = 0;
                else
                    flag[position] = 1;

                switch (v.getId()) {
                    case R.id.more:
                        if (vh != null) {
                            vh.content.setMaxLines(Integer.MAX_VALUE);
                            vh.more.setVisibility(View.INVISIBLE);
                            vh.less.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.less:
                        if (vh != null) {
                            vh.content.setMaxLines(2);
                            vh.more.setVisibility(View.VISIBLE);
                            vh.less.setVisibility(View.INVISIBLE);
                        }
                        break;
                    default :
                        logInfo("setOnItemClickListener", "onItemClick");
                        if(myGlobals.checked) {
                            onItemLongClick(position, v);
                        } else {
                            myGlobals.isCompletedJob = true;
                            launchDetailsActivity(vh.myJobs);
                        }
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                MyRecyclerNewViewAdapter.DataObjectHolder vh = (MyRecyclerNewViewAdapter.DataObjectHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
                if (flag[position] == 1)
                    flag[position] = 0;
                else
                    flag[position] = 1;


                switch (v.getId()) {
                    case R.id.more:
                    case R.id.less:
                        break;
                    default :
                        logInfo("setOnItemClickListener", "onItemLongClick");
                        LinearLayout ll = (LinearLayout)v.getParent();
                        if(ll == null)
                            logInfo("CardView ", "NULL");
                        else
                            logInfo("CardView ", "Not NULL");

                        if (selected[position] == 1) {
                            selected[position] = 0;
                            ((TextView) v.findViewById(R.id.checked)).setVisibility(View.GONE);
                            ll.setBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            selected[position] = 1;
                            ((TextView) v.findViewById(R.id.checked)).setVisibility(View.VISIBLE);
                            ll.setBackgroundColor(getResources().getColor(R.color.background));
                            FontManager.markAsIconContainer(v.findViewById(R.id.checked), myGlobals.materialIconFont);
                        }

                        myGlobals.checked = false;
                        myGlobals.checkedTabId = -1;
                        for(int i = 0; (i < selected.length); i++) {
                            if(selected[i] == 1) {
                                myGlobals.checked = true;
                                myGlobals.checkedTabId = 0;
                                break;
                            }
                        }

                        if(myGlobals.checked)
                            MyJobsActivity.enableMenuItems();
                        else
                            MyJobsActivity.disableMenuItems();

                        break;
                }

                return true;
            }
        });

        return contentview;
    }

    public static void resetViewer() {
        int count = selected.length;
        logInfo("MyNewJobsTabFragment", "resetViewer");
        for(int pos = 0; (pos < count); pos++) {
            MyRecyclerNewViewAdapter.DataObjectHolder vh = (MyRecyclerNewViewAdapter.DataObjectHolder) mRecyclerView.findViewHolderForAdapterPosition(pos);
            if(vh != null) {
                final View v = vh.itemView;
                if (v != null) {
                    LinearLayout ll = (LinearLayout) v.findViewById(R.id.lin);
                    if (selected[pos] == 1) {
                        selected[pos] = 0;
                        ((TextView) v.findViewById(R.id.checked)).setVisibility(View.GONE);
                        ll.setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                }
            }
        }
    }

    public static void updateJobStatus(final String mode) {
        int count = selected.length;

        for(int pos = 0; (pos < count); pos++) {
            MyRecyclerNewViewAdapter.DataObjectHolder vh = (MyRecyclerNewViewAdapter.DataObjectHolder) mRecyclerView.findViewHolderForAdapterPosition(pos);
            if(vh != null) {
                final View v = vh.itemView;
                if (v != null) {
                    LinearLayout ll = (LinearLayout) v.findViewById(R.id.lin);
                    if (selected[pos] == 1) {
                        selected[pos] = 0;
                        if(pos == 0)
                            jobId = jobId + vh.myJobs.getJobId();
                        else
                            jobId = jobId + "," + vh.myJobs.getJobId();
                        ((TextView) v.findViewById(R.id.checked)).setVisibility(View.GONE);
                        ll.setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                }
            }
        }

        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Updating Jobs...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateJobs(mode, jobId);
            }
        }, 10);

    }

    private static void updateJobs(final String mode, final String jobId) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(success) {
                    //update db and refresh
                    String[] jobList = jobId.split(",");
                    for(int i = 0; (i < jobList.length); i++) {
                        String id = jobList[i];
                        myGlobals.dbHandler.deleteJob(id);
                    }
                    myGlobals.checked = false;
                    myGlobals.checkedTabId = -1;
                    MyJobsActivity.disableMenuItems();

                    updateDataSet();
                }
                progressBar.dismiss();
            }
        };

        new Thread() {
            public void run() {
                int status = 0;
                String keyValuePairs = "mode=" + mode + "&isFromMobile=" + true + "&jobid=" + jobId;
                String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "changejobStatus", keyValuePairs, "POST", null);
                try {
                    if ((response != null) && (!response.isEmpty())) {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        status = jsonObject.getInt("status");
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                if (status == 1)
                    success = true;
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    public static void updateDataSet() {
        mAdapter.clearDataSet();
        ArrayList results = getDataSet();
        mAdapter.addDataSet(results);
        mAdapter.notifyDataSetChanged();
    }

    public static void launchDetailsActivity(final MyJobs myJobs) {
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Loading Job...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        myGlobals.stlFileVersionsList = null;
        success = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFileVersions(myJobs);
            }
        }, 10);

    }

    private static void loadFileVersions(final MyJobs myJobs) {
        myGlobals.currJobActive = myJobs;
        myGlobals.jobId = myJobs.getJobId();
        myGlobals.patientId = myJobs.getPatientId();
        myGlobals.patientName = myJobs.getPatientName();
        String hospitalId = myJobs.getHospitalId();
        HospitalInfo hospitalInfo = myGlobals.dbHandler.getHospital(hospitalId);
        if(hospitalInfo != null) {
            myGlobals.hospitalName = hospitalInfo.getHospitalName();
            myGlobals.cityName = hospitalInfo.getHospitalCity();
        }

        switch(myGlobals.loggedinUserType) {
            case 1 :
                downloadProfileImage(myGlobals.currJobActive.getEngineerEmail());
                downloadProfileImage(myGlobals.currJobActive.getTechEmail());
                break;
            case 2 :
                downloadProfileImage(myGlobals.currJobActive.getEngineerEmail());
                downloadProfileImage(myGlobals.currJobActive.getDoctorEmail());
                break;
            case 3 :
                downloadProfileImage(myGlobals.currJobActive.getDoctorEmail());
                downloadProfileImage(myGlobals.currJobActive.getTechEmail());
                break;
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(success) {
                    //update db and refresh
                    Intent intent = new Intent(context, MyJobDetailsActivity.class);
                    if(intent != null) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
                progressBar.dismiss();
            }
        };

        new Thread() {
            public void run() {
                String jobId = myJobs.getJobId();
                fetchJobFileVersions(jobId);

                myGlobals.stlFileVersionsList = myGlobals.dbHandler.getStlFileVersions(jobId);
                success = true;
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(done) {
            done = false;
        }
        logInfo("MyNewJobsTabFragment", "onResume Called ............");
    }

    private static ArrayList<MyJobsRowItem> getDataSet() {
        ArrayList results = new ArrayList<MyJobsRowItem>();

        List<MyJobs> myJobsList = myGlobals.dbHandler.getMyJobsByStatus(0, "DESC");
        int count = 0;
        if((myJobsList != null) && (myJobsList.size() > 0)) {
            count = myJobsList.size();
            flag = new int[myJobsList.size()];
            selected = new int[myJobsList.size()];

            String dicomNotes = null;
            for(int index = 0; (index < myJobsList.size()); index++) {
                MyJobs myJobs = myJobsList.get(index);
                Date dateObj = null;
                String jobTime = null;
                String date = myJobs.getCreatedDate();
                date = date.replaceAll("T", " ");
                date = date.replaceAll(".000Z", "");
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    dateObj = sdf.parse(date);
                    jobTime = new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(dateObj).toUpperCase();
                } catch (final ParseException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
                dicomNotes = myJobs.getDicomNotes();
                if((dicomNotes != null) && (!dicomNotes.equals("")) && (!dicomNotes.equals("null")) && (dicomNotes.length() > 0)) {
                    dicomNotes = dicomNotes.replace("<br />", "\n");
                    dicomNotes = dicomNotes.replace("<br/>", "\n");
                    dicomNotes = dicomNotes.replace("<br >", "\n");
                    dicomNotes = dicomNotes.replace("<br>", "\n");
                } else {
                    dicomNotes = "";
                }

                MyJobsRowItem item = new MyJobsRowItem(myJobs.getJobId(), myJobs.getStatus(), myJobs.getPatientName(), myJobs.getPatientId(), dicomNotes, jobTime, myJobs.getId(), myJobs.getPriority(), myJobs);
                results.add(index, item);
            }
        }

        MyJobsActivity.updateTabTitle(0, count);

        return results;
    }

    @Override
    public void onClick(View v) {

    }

    private class MySwipeRefreshLayoutListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh () {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateComplaintStatus();
                }
            }, 10);
        }
    }

    private static void updateComplaintStatus() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        mAdapter.notifyDataSetChanged();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        };

        new Thread() {
            public void run() {
                try {
                    //myGlobals.getJobsStatus();
                    mAdapter.clearDataSet();
                    mAdapter.addDataSet(getDataSet());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

}
