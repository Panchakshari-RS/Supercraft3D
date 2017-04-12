package com.trova.supercraft;

/**
 * Created by Panchakshari on 23/11/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trova.supercraft.Notification.NotificationActivity;

import static com.trova.supercraft.SuperCraftUtils.logInfo;


public class CustomGrid extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private String[] menuText;
    private int[] images;
    private Bitmap[] bitmaps;
    private boolean useBitmap;
    private static Integer id = 0;

    public CustomGrid(Context context, String[] menuText, int[] images, Bitmap[] bitmaps, boolean useBitmap) {
        this.context = context;
        this.menuText = menuText;
        this.images = images;
        this.bitmaps = bitmaps;
        this.useBitmap = useBitmap;
    }

    @Override
    public int getCount() {
        if(useBitmap)
            return bitmaps.length;

        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        LinearLayout actionLayout;
        ImageView packageImage;
        TextView menu_text;
        int position;
    }

    @Override
    public void onClick(View v) {
        id = ((ViewHolder) v.getTag()).position;

        logInfo("OnClick", "Clicked id " + id + " - " + menuText[id]);

        if(menuText[id].equals("Clear Log")) {
            logInfo("Clicked ", "on Clear Log");
            NotificationActivity.clearLogs();
        } else if(menuText[id].equals("Profile")) {
            logInfo("Clicked ", "on Edit Profile Menus");
            MyJobsActivity.editProfile();
        } else if(menuText[id].equals("Password")) {
            logInfo("Clicked ", "on Change Password Menus");
            MyJobsActivity.changePassword();
        } else {
            switch (id) {
                case 0:
                    MessageActivity.launchDocumentPicker();
                    break;
                case 1:
                    MessageActivity.launchCameraIntent();
                    break;
                case 2:
                    MessageActivity.launchGalleryIntent();
                    break;
                case 3:
                    MessageActivity.launchVideoIntent();
                    break;
                case 4:
                    MessageActivity.launchAudioIntent();
                    break;
            }
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        //View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_layout, null);
            holder.actionLayout = (LinearLayout) convertView.findViewById(R.id.actionLayout);
            holder.menu_text = (TextView) convertView.findViewById(R.id.menu_text);
            holder.menu_text.setText(menuText[position]);
            holder.packageImage = (ImageView) convertView.findViewById(R.id.menu_image);
            //holder.packageImage.setImageResource(images[position]);
            Bitmap bm = null;
            if(useBitmap)
                bm = bitmaps[position];
            else
                bm = BitmapFactory.decodeResource(context.getResources(),images[position]);
            RoundImage roundedImage = new RoundImage(bm);
            holder.packageImage.setImageDrawable(roundedImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        holder.actionLayout.setOnClickListener((View.OnClickListener) this);
        holder.actionLayout.setTag(holder);

        return convertView;
    }

}
