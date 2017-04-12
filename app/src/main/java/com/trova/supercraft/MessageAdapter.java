package com.trova.supercraft;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.trova.supercraft.MessageActivity.addMessage;
import static com.trova.supercraft.MessageUtils.launchGalleryViewer;
import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static java.lang.Thread.sleep;

//import static in.trova.android.MessageViewUtils.downloadResource;
//import static in.trova.android.MessageViewUtils.launchGalleryViewer;

/**
 * Created by Panchakshari on 21/2/2017.
 */

public class MessageAdapter extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    public Map<Long,ChatMessages> chatMessages;
    static MediaPlayer mPlayer = null;
    static long messageId = 0;
    static int oneTimeOnly = 0;
    static SeekBar playSeekBar = null;
    static boolean playing = true;
    static ImageView prevPlay, prevPause;
    private String productId;
    private Bitmap bmSender, bmReceiver;

    private static double startTime = 0;
    private static double finalTime = 0;
    private static Handler myHandler = new Handler();
    private static Thread myThread = null;

    private ImageView goneCallout = null, visiCallout = null, goneProfile = null, visiProfile = null;

    public MessageAdapter(Activity activity, Map<Long,ChatMessages> chatMessages, String productId) {
        this.activity=activity;
        this.chatMessages=chatMessages;
        this.productId = productId;
        logInfo("MessageAdapter", "Called ............");
    }

    public void setProfileImages(Bitmap bmSender, Bitmap bmReceiver) {
        this.bmSender = bmSender;
        this.bmReceiver = bmReceiver;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String bytes2Size(long bytes) {
        String[] arr = {"Bytes", "KB", "MB", "GB", "TB"};

        if(bytes == 0)
            return ("0 Bytes");

        logInfo("bytes2Size", String.valueOf(bytes));
        Double d = (Math.floor(Math.log(bytes)) / Math.log(1024));
        int i = d.intValue();

        long val = (Math.round(bytes/Math.pow(1024, i)));

        return (String.valueOf(val) + " " + arr[i]);
    }

    private class ViewHolder {
        protected TextView recvMsg;
        protected TextView recvMsgTime;
        protected TextView sendMsg;
        protected TextView sendMsgTime;
        protected ImageView recvImage;
        protected ImageView sendImage;
        protected TextView recvImgTime;
        protected TextView sendImgTime;
        protected int position;
        protected int curPosition = 0;
        protected long messageId;
        protected Uri thumbUri;
        protected Uri fileUri;
        protected String mimeType;
        protected ProgressBar pbSent;
        protected ProgressBar pbRecv;
        protected ImageView recvCallout;
        protected ImageView sendCallout;
        protected ImageView recvProfile;
        protected ImageView sendProfile;
        protected ProgressBar pbProfile;
        protected ImageView chatDelivery;
        protected RelativeLayout sendMsgLyt;
        protected RelativeLayout sendMsgOutLyt;
        protected RelativeLayout recvMsgLyt;
        protected RelativeLayout recvMsgOutLyt;
        protected RelativeLayout sendImgInnLyt;
        protected RelativeLayout sendImgInLyt;
        //protected RelativeLayout sendImgOutLyt;
        protected ImageView imageDelivery;
        protected RelativeLayout recvImgPenOutLyt;
        protected RelativeLayout recvImgPenInnLyt;
        protected RelativeLayout recvImgContOutLyt;
        protected RelativeLayout recvImgContInLyt;
        //protected RelativeLayout recvImgContInnLyt;
        protected ProgressBar pbImgPenRecv;
        protected TextView recvImgFile;
        protected ImageView recvImageUpload;
        protected RelativeLayout sendAudioOutLyt;
        protected RelativeLayout sendAudioInLyt;
        protected RelativeLayout sendAudioInnLyt;
        protected RelativeLayout sendAudioInnnLyt;
        protected RelativeLayout sendAudioPlayLyt;
        protected TextView sendAudioDur;
        protected ImageView sendAudioImg;
        protected ImageView sentAudioPlay;
        protected ImageView sentAudioPause;
        protected SeekBar sentAudioSeek;
        protected TextView sendAudioTime;
        protected ImageView audioDelivery;
        protected TextView sendAudioSize;
        protected RelativeLayout recvAudioPenOutLyt;
        protected RelativeLayout recvAudioPenInnLyt;
        protected RelativeLayout recvAudioContOutLyt;
        protected RelativeLayout recvAudioContInLyt;
        protected RelativeLayout recvAudioContInnLyt;
        protected RelativeLayout recvAudioContInnnLyt;
        protected RelativeLayout recvAudioPlayLyt;
        protected ProgressBar pbAudioPenRecv;
        protected TextView recvAudioFile;
        protected ImageView recvAudioUpload;
        protected TextView recvAudioDur;
        protected ImageView recvAudioImg;
        protected ImageView recvAudioPlay;
        protected ImageView recvAudioPause;
        protected SeekBar recvAudioSeek;
        protected TextView recvAudioTime;
        protected TextView recvAudioSize;
        protected TextView sentVideoTime;
        protected TextView sentVideoDur;
        protected ImageView videoDelivery;
        //protected RelativeLayout sentVideoDummyLyt;
        protected RelativeLayout sentVideoOutLyt;
        protected RelativeLayout sentVideoInnLyt;
        //protected RelativeLayout sentVideoLyt;
        protected ProgressBar pbSentVideo;
        protected ImageView sentVideoImage;
        protected ImageView sentVideoAlphaImage;
        protected ImageView sentVideoIcon;
        protected RelativeLayout recvVideoPenOutLyt;
        protected RelativeLayout recvVideoPenInnLyt;
        protected RelativeLayout recvVideoContOutLyt;
        protected RelativeLayout recvVideoContInnLyt;
        //protected RelativeLayout recvVideoContLyt;
        protected ImageView recvVideoImage;
        protected ImageView recvVideoImageAlpha;
        protected ImageView recvVideoUpload;
        protected ImageView recvVideoIcon;
        protected TextView recvVideoTime;
        protected TextView recvVideoFile;
        protected TextView recvVideoDur;
        protected ProgressBar pbRecvVideo;
        protected ProgressBar pbRecvVideoPen;
        protected RelativeLayout sentDocOutLyt;
        protected RelativeLayout sentDocInLyt;
        protected RelativeLayout sentDocInnLyt;
        protected RelativeLayout sentDocLyt;
        protected ImageView sentDocImage;
        protected TextView sentDocExt;
        protected TextView sentDocFile;
        protected TextView sentDocTime;
        protected TextView sentDocSize;
        protected ImageView docDelivery;
        protected RelativeLayout recvDocPenOutLyt;
        protected RelativeLayout recvDocPenInnLyt;
        protected RelativeLayout recvDocContOutLyt;
        protected RelativeLayout recvDocContInLyt;
        protected RelativeLayout recvDocContInnLyt;
        protected RelativeLayout recvDocContLyt;
        protected ProgressBar pbRecvDoc;
        protected TextView recvDocFile;
        protected ImageView recvDocUpload;
        protected ImageView recvDocImage;
        protected TextView recvDocExt;
        protected TextView recvDoc;
        protected TextView recvDocSize;
        protected TextView recvDocTime;

        protected boolean isImageDownloading;
        protected boolean isAudioDownloading;
        protected boolean isVideoDownloading;
        protected boolean isDocDownloading;
        protected Thread msgImageThread;
        protected Thread msgAudioThread;
        protected Thread msgVideoThread;
        protected Thread msgDocThread;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        List<Long> list=new ArrayList<>(chatMessages.keySet());
        ChatMessages message = chatMessages.get(list.get(position));
        logInfo("getView", "Called ............" + position);
        int messageStatus = message.getMessageSentOrReceived();

        LayoutInflater inflater = (LayoutInflater)activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_message_view, null);
            holder = new ViewHolder();
            holder.recvMsg = (TextView) convertView.findViewById(R.id.chat_reciver_message);
            holder.recvMsgTime = (TextView) convertView.findViewById(R.id.chat_reciver_time);
            holder.sendMsg = (TextView) convertView.findViewById(R.id.chat_sender_message);
            holder.sendMsgTime = (TextView) convertView.findViewById(R.id.chat_sender_time);
            holder.recvImage = (ImageView) convertView.findViewById(R.id.recv_image);
            holder.sendImage = (ImageView) convertView.findViewById(R.id.send_image);
            holder.recvImgTime = (TextView) convertView.findViewById(R.id.recv_img_time);
            holder.sendImgTime = (TextView) convertView.findViewById(R.id.send_img_time);
            holder.pbRecv = (ProgressBar) convertView.findViewById(R.id.progress_bar_recv);
            holder.pbProfile = (ProgressBar) convertView.findViewById(R.id.progress_bar_profile);
            holder.pbSent = (ProgressBar) convertView.findViewById(R.id.progress_bar_send);
            holder.recvCallout = (ImageView) convertView.findViewById(R.id.recv_callout);
            holder.sendCallout = (ImageView) convertView.findViewById(R.id.send_callout);
            holder.recvProfile = (ImageView) convertView.findViewById(R.id.recv_profile);
            holder.sendProfile = (ImageView) convertView.findViewById(R.id.send_profile);
            holder.sendMsgLyt = (RelativeLayout) convertView.findViewById(R.id.sent_msg_lyt);
            holder.sendMsgOutLyt = (RelativeLayout) convertView.findViewById(R.id.sent_out_msg_lyt);
            holder.recvMsgLyt = (RelativeLayout) convertView.findViewById(R.id.recv_msg_lyt);
            holder.recvMsgOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_out_msg_lyt);
            holder.chatDelivery = (ImageView) convertView.findViewById(R.id.sent_msg);
            holder.sendImgInnLyt = (RelativeLayout) convertView.findViewById(R.id.send_img_out_lyt);
            holder.sendImgInLyt = (RelativeLayout) convertView.findViewById(R.id.send_img_in_lyt);
            //holder.sendImgOutLyt = (RelativeLayout) convertView.findViewById(R.id.send_img_inn_lyt);
            holder.imageDelivery = (ImageView) convertView.findViewById(R.id.sent_img);
            holder.recvImgContInLyt = (RelativeLayout) convertView.findViewById(R.id.recv_image_content_in);
            holder.recvImgContOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_image_content_out);
            //holder.recvImgContInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_image_content_inn);
            holder.recvImgPenInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_image_pending_inn_lyt);
            holder.recvImgPenOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_image_pending_out_lyt);
            holder.pbImgPenRecv = (ProgressBar) convertView.findViewById(R.id.progress_bar_pen_recv);
            holder.recvImgFile = (TextView) convertView.findViewById(R.id.recv_image_filename);
            holder.recvImageUpload = (ImageView) convertView.findViewById(R.id.recv_image_upload);
            holder.sendAudioOutLyt = (RelativeLayout) convertView.findViewById(R.id.send_audio_out_lyt);
            holder.sendAudioInLyt = (RelativeLayout) convertView.findViewById(R.id.send_audio_in_lyt);
            holder.sendAudioInnLyt = (RelativeLayout) convertView.findViewById(R.id.send_audio_inn_lyt);
            holder.sendAudioInnnLyt = (RelativeLayout) convertView.findViewById(R.id.send_audio_innn_lyt);
            holder.sendAudioPlayLyt = (RelativeLayout) convertView.findViewById(R.id.sent_play_layout);
            holder.sendAudioDur = (TextView) convertView.findViewById(R.id.sent_audio_duration);
            holder.sendAudioImg = (ImageView) convertView.findViewById(R.id.sent_audio_img);
            holder.sentAudioPlay = (ImageView) convertView.findViewById(R.id.sent_play);
            holder.sentAudioPause = (ImageView) convertView.findViewById(R.id.sent_pause);
            holder.sentAudioSeek = (SeekBar) convertView.findViewById(R.id.sent_seekbar);
            holder.sendAudioTime = (TextView) convertView.findViewById(R.id.sent_audio_time);
            holder.audioDelivery = (ImageView) convertView.findViewById(R.id.audio_processdone);
            holder.sendAudioSize = (TextView) convertView.findViewById(R.id.sent_audio_size);
            holder.recvAudioPenOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_audio_pending_out);
            holder.recvAudioPenInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_audio_pending_in_lyt);
            holder.recvAudioContOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_audio_content_out);
            holder.recvAudioContInLyt = (RelativeLayout) convertView.findViewById(R.id.recv_audio_content_in);
            holder.recvAudioContInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_audio_content_inn);
            holder.recvAudioContInnnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_audio_content_innn);
            holder.recvAudioPlayLyt = (RelativeLayout) convertView.findViewById(R.id.recv_play_layout);
            holder.pbAudioPenRecv = (ProgressBar) convertView.findViewById(R.id.pb_recv_audio);
            holder.recvAudioFile = (TextView) convertView.findViewById(R.id.recv_audio_filename);
            holder.recvAudioUpload = (ImageView) convertView.findViewById(R.id.recv_audio_upload);
            holder.recvAudioDur = (TextView) convertView.findViewById(R.id.recv_audio_duration);
            holder.recvAudioImg = (ImageView) convertView.findViewById(R.id.recv_audio_img);
            holder.recvAudioPlay = (ImageView) convertView.findViewById(R.id.recv_play);
            holder.recvAudioPause = (ImageView) convertView.findViewById(R.id.recv_pause);
            holder.recvAudioSeek = (SeekBar) convertView.findViewById(R.id.recv_seekbar);
            holder.recvAudioTime = (TextView) convertView.findViewById(R.id.recv_audio_time);
            holder.recvAudioSize = (TextView) convertView.findViewById(R.id.recv_audio_size);
            //holder.sentVideoDummyLyt = (RelativeLayout)convertView.findViewById(R.id.send_video_inn_lyt1);
            holder.sentVideoOutLyt = (RelativeLayout) convertView.findViewById(R.id.send_video_out_lyt);
            holder.sentVideoInnLyt = (RelativeLayout) convertView.findViewById(R.id.send_video_inn_lyt);
            //holder.sentVideoLyt = (RelativeLayout) convertView.findViewById(R.id.send_video_lyt);
            holder.sentVideoImage = (ImageView) convertView.findViewById(R.id.sent_image_video);
            holder.sentVideoAlphaImage = (ImageView) convertView.findViewById(R.id.sent_image_video_alpha);
            holder.sentVideoIcon = (ImageView) convertView.findViewById(R.id.video_icon);
            holder.sentVideoDur = (TextView) convertView.findViewById(R.id.sent_video_duration);
            holder.sentVideoTime = (TextView) convertView.findViewById(R.id.sent_video_time);
            holder.videoDelivery = (ImageView) convertView.findViewById(R.id.video_processdone);
            holder.pbSentVideo = (ProgressBar) convertView.findViewById(R.id.progress_bar_video_sent);
            holder.recvVideoPenOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_video_pending_out_lyt);
            holder.recvVideoPenInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_video_pending_inn_lyt);
            holder.recvVideoContOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_video_content_out_lyt);
            holder.recvVideoContInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_video_content_inn_lyt);
            //holder.recvVideoContLyt = (RelativeLayout) convertView.findViewById(R.id.recv_video_content_lyt);
            holder.pbRecvVideo = (ProgressBar) convertView.findViewById(R.id.progress_bar_video_recv);
            holder.pbRecvVideoPen = (ProgressBar) convertView.findViewById(R.id.pb_recv_video);
            holder.recvVideoImage = (ImageView) convertView.findViewById(R.id.recv_image_video);
            holder.recvVideoImageAlpha = (ImageView) convertView.findViewById(R.id.recv_image_video_alpha);
            holder.recvVideoUpload = (ImageView) convertView.findViewById(R.id.recv_video_upload);
            holder.recvVideoIcon = (ImageView) convertView.findViewById(R.id.recv_video_icon);
            holder.recvVideoFile = (TextView) convertView.findViewById(R.id.recv_video_filename);
            holder.recvVideoTime = (TextView) convertView.findViewById(R.id.recv_video_time);
            holder.recvVideoDur = (TextView) convertView.findViewById(R.id.recv_video_duration);
            holder.sentDocOutLyt = (RelativeLayout) convertView.findViewById(R.id.sent_doc_out_lyt);
            holder.sentDocInLyt = (RelativeLayout) convertView.findViewById(R.id.sent_doc_in_lyt);
            holder.sentDocInnLyt = (RelativeLayout) convertView.findViewById(R.id.sent_doc_inn_lyt);
            holder.sentDocLyt = (RelativeLayout) convertView.findViewById(R.id.sent_doc_lyt);
            holder.sentDocImage = (ImageView) convertView.findViewById(R.id.sent_doc_img);
            holder.sentDocExt = (TextView) convertView.findViewById(R.id.sent_doc_ext);
            holder.sentDocFile = (TextView) convertView.findViewById(R.id.sent_doc_textView);
            holder.sentDocTime = (TextView) convertView.findViewById(R.id.sent_doc_time);
            holder.sentDocSize = (TextView) convertView.findViewById(R.id.sent_doc_size);
            holder.docDelivery = (ImageView) convertView.findViewById(R.id.doc_processdone);

            holder.recvDocPenOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_doc_pending_out_lyt);
            holder.recvDocPenInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_doc_pending_inn_lyt);
            holder.recvDocContOutLyt = (RelativeLayout) convertView.findViewById(R.id.recv_doc_content_out_lyt);
            holder.recvDocContInLyt = (RelativeLayout) convertView.findViewById(R.id.recv_doc_content_in_lyt);
            holder.recvDocContInnLyt = (RelativeLayout) convertView.findViewById(R.id.recv_doc_content_inn_lyt);
            holder.recvDocContLyt = (RelativeLayout) convertView.findViewById(R.id.recv_doc_image_layout);
            holder.pbRecvDoc = (ProgressBar) convertView.findViewById(R.id.pb_recv_doc);
            holder.recvDocFile = (TextView) convertView.findViewById(R.id.recv_doc_filename);
            holder.recvDocUpload = (ImageView) convertView.findViewById(R.id.recv_doc_upload);
            holder.recvDocImage = (ImageView) convertView.findViewById(R.id.recv_doc_img);
            holder.recvDocExt = (TextView) convertView.findViewById(R.id.recv_doc_ext);
            holder.recvDoc = (TextView) convertView.findViewById(R.id.recv_doc_textView);
            holder.recvDocSize = (TextView) convertView.findViewById(R.id.recv_doc_size);
            holder.recvDocTime = (TextView) convertView.findViewById(R.id.recv_doc_time);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sendImage.setOnClickListener(this);
        holder.recvImage.setOnClickListener(this);
        holder.sentVideoImage.setOnClickListener(this);
        holder.recvVideoImage.setOnClickListener(this);
        holder.sentDocInnLyt.setOnClickListener(this);
        holder.recvDocContInnLyt.setOnClickListener(this);
        //holder.sendAudioInnLyt.setOnClickListener(this);
        holder.sentAudioPlay.setOnClickListener(this);
        holder.sentAudioPause.setOnClickListener(this);
        //holder.recvAudioContInnLyt.setOnClickListener(this);
        holder.recvAudioPlay.setOnClickListener(this);
        holder.recvAudioPause.setOnClickListener(this);

        holder.recvImgPenInnLyt.setOnClickListener(this);
        holder.recvAudioPenInnLyt.setOnClickListener(this);
        holder.recvVideoPenInnLyt.setOnClickListener(this);
        holder.recvDocPenInnLyt.setOnClickListener(this);


        holder.position = position;
        holder.messageId = message.getMessageId();

        String thumbFilePath = message.getThumbPath();
        File thumbFile = null;
        if(thumbFilePath != null) {
            logInfo("thumbFilePath", thumbFilePath);
            holder.thumbUri = Uri.parse(thumbFilePath);
            thumbFile = new File(thumbFilePath);
        }

        long fileSize = message.getFileSize();
        String size = null;
        if(fileSize > 0) {
            size = bytes2Size(fileSize);
            logInfo("bytes2Size", size);
        }

        String filePath = message.getFilePath();
        File file = null;
        if(filePath != null) {
            logInfo("filePath", filePath);
            holder.fileUri = Uri.parse(filePath);
            file = new File(filePath);
        }

        holder.mimeType = message.getMimeType();

        Date dateObj = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            dateObj = sdf.parse(message.getTime());
        } catch (final ParseException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        String messageType = null;
        if((message.getMimeType() != null) && (!message.getMimeType().isEmpty()) && (message.getMimeType().length() > 0)) {
            messageType = message.getMimeType();
        }

        goneCallout = null;
        visiCallout = null;
        goneProfile = null;
        visiProfile = null;
        int resId = -1;
        Bitmap bm = null;

        holder.sendMsgTime.setTypeface(myGlobals.centuryGothic);
        holder.sendMsg.setTypeface(myGlobals.centuryGothic);
        holder.sendImgTime.setTypeface(myGlobals.centuryGothic);
        holder.sendAudioTime.setTypeface(myGlobals.centuryGothic);
        holder.sendAudioSize.setTypeface(myGlobals.centuryGothic);
        holder.sendAudioDur.setTypeface(myGlobals.centuryGothic);
        holder.sentVideoTime.setTypeface(myGlobals.centuryGothic);
        holder.sentVideoDur.setTypeface(myGlobals.centuryGothic);
        holder.sentDocTime.setTypeface(myGlobals.centuryGothic);
        holder.sentDocFile.setTypeface(myGlobals.centuryGothic);
        holder.sentDocExt.setTypeface(myGlobals.centuryGothic);
        holder.sentDocSize.setTypeface(myGlobals.centuryGothic);
        holder.recvMsgTime.setTypeface(myGlobals.centuryGothic);
        holder.recvMsg.setTypeface(myGlobals.centuryGothic);
        holder.recvImgTime.setTypeface(myGlobals.centuryGothic);
        holder.recvImgFile.setTypeface(myGlobals.centuryGothic);
        holder.recvAudioTime.setTypeface(myGlobals.centuryGothic);
        holder.recvAudioSize.setTypeface(myGlobals.centuryGothic);
        holder.recvAudioDur.setTypeface(myGlobals.centuryGothic);
        holder.recvAudioFile.setTypeface(myGlobals.centuryGothic);
        holder.recvVideoTime.setTypeface(myGlobals.centuryGothic);
        holder.recvVideoDur.setTypeface(myGlobals.centuryGothic);
        holder.recvVideoFile.setTypeface(myGlobals.centuryGothic);
        holder.recvDocTime.setTypeface(myGlobals.centuryGothic);
        holder.recvDoc.setTypeface(myGlobals.centuryGothic);
        holder.recvDocExt.setTypeface(myGlobals.centuryGothic);
        holder.recvDocSize.setTypeface(myGlobals.centuryGothic);
        holder.recvDocFile.setTypeface(myGlobals.centuryGothic);


        //handleChatContentView(holder);
        if(messageStatus == 1) { // Sent Messages
            goneCallout = holder.recvCallout;
            visiCallout = holder.sendCallout;
            goneProfile = holder.recvProfile;
            visiProfile = holder.sendProfile;
            resId = R.drawable.avatar;
            bm = bmSender;

            if((messageType != null) && (messageType.equals("text"))) {
                setupSentMessage(holder);
                holder.sendMsgOutLyt.setVisibility(View.VISIBLE);
                holder.sendMsgLyt.setVisibility(View.VISIBLE);
                holder.sendMsg.setVisibility(View.VISIBLE);
                holder.chatDelivery.setVisibility(View.VISIBLE);
                holder.sendMsgTime.setVisibility(View.VISIBLE);
                String time = new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase();
                holder.sendMsgTime.setText(time);

                holder.sendMsg.setText(message.getMessage());
                holder.chatDelivery.setImageResource(R.drawable.sent);
                if (message.getMessageDeliveryStatus().equals("true")) {
                    if(message.getReadMessage() == 1) {
                        holder.chatDelivery.setImageResource(R.drawable.seen);
                    } else {
                        holder.chatDelivery.setImageResource(R.drawable.delivered);
                    }
                }

                logInfo("Message", message.getMessage());
                //visiMsg.setMaxWidth(((RelativeLayout)visiMsg.getParent()).getWidth()/3);
                holder.sendMsg.setTag(holder);

            } else if ((messageType != null) && (messageType.contains("image/"))) {
                setupSentImage(holder);
                //holder.sendImgOutLyt.setVisibility(View.VISIBLE);
                holder.sendImgInLyt.setVisibility(View.VISIBLE);
                holder.sendImgInnLyt.setVisibility(View.VISIBLE);
                holder.sendImage.setVisibility(View.VISIBLE);
                holder.sendImgTime.setVisibility(View.VISIBLE);
                holder.pbSent.setVisibility(View.VISIBLE);
                holder.imageDelivery.setVisibility(View.VISIBLE);
                holder.imageDelivery.setImageResource(R.drawable.sent);
                if (message.getMessageDeliveryStatus().equals("true")) {
                    if(message.getReadMessage() == 1) {
                        holder.imageDelivery.setImageResource(R.drawable.seen);
                    } else {
                        holder.imageDelivery.setImageResource(R.drawable.delivered);
                    }
                }
                holder.sendImgTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                holder.sendImage.setTag(holder);
                ProgressBarHelper pb_and_image = new ProgressBarHelper();
                pb_and_image.setImg(holder.sendImage);
                pb_and_image.setPb(holder.pbSent);
                new LoadImageTask().execute(pb_and_image);
            } else if ((messageType != null) && (messageType.contains("audio/"))) {
                setupSentAudio(holder);
                holder.sendAudioOutLyt.setVisibility(View.VISIBLE);
                holder.sendAudioInLyt.setVisibility(View.VISIBLE);
                holder.sendAudioInnLyt.setVisibility(View.VISIBLE);
                holder.sendAudioInnnLyt.setVisibility(View.VISIBLE);
                holder.sendAudioPlayLyt.setVisibility(View.VISIBLE);
                holder.sendAudioDur.setVisibility(View.VISIBLE);
                holder.sendAudioImg.setVisibility(View.VISIBLE);
                holder.sentAudioPlay.setVisibility(View.VISIBLE);
                holder.sentAudioPause.setVisibility(View.GONE);
                holder.sentAudioSeek.setVisibility(View.VISIBLE);
                holder.sendAudioTime.setVisibility(View.VISIBLE);
                holder.audioDelivery.setVisibility(View.VISIBLE);
                holder.sendAudioSize.setVisibility(View.VISIBLE);
                holder.audioDelivery.setImageResource(R.drawable.sent);
                if (message.getMessageDeliveryStatus().equals("true")) {
                    if(message.getReadMessage() == 1) {
                        holder.audioDelivery.setImageResource(R.drawable.seen);
                    } else {
                        holder.audioDelivery.setImageResource(R.drawable.delivered);
                    }
                }
                holder.sendAudioTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                holder.sendAudioSize.setText(size);
                holder.sendAudioDur.setText(message.getDurationTime());
                holder.sentAudioPlay.setTag(holder);
                holder.sentAudioPause.setTag(holder);
                holder.sendAudioInnLyt.setTag(holder);
            } else if ((messageType != null) && (messageType.contains("video/"))) {
                setupSentVideo(holder);
                //holder.sentVideoDummyLyt.setVisibility(View.VISIBLE);
                holder.sentVideoOutLyt.setVisibility(View.VISIBLE);
                holder.sentVideoInnLyt.setVisibility(View.VISIBLE);
                //holder.sentVideoLyt.setVisibility(View.VISIBLE);
                holder.sentVideoImage.setVisibility(View.VISIBLE);
                holder.sentVideoAlphaImage.setVisibility(View.VISIBLE);
                holder.sentVideoIcon.setVisibility(View.VISIBLE);
                holder.sentVideoDur.setVisibility(View.VISIBLE);
                holder.sentVideoTime.setVisibility(View.VISIBLE);
                holder.videoDelivery.setVisibility(View.VISIBLE);
                holder.pbSentVideo.setVisibility(View.VISIBLE);
                holder.videoDelivery.setImageResource(R.drawable.sent);
                if (message.getMessageDeliveryStatus().equals("true")) {
                    if(message.getReadMessage() == 1) {
                        holder.videoDelivery.setImageResource(R.drawable.seen);
                    } else {
                        holder.videoDelivery.setImageResource(R.drawable.delivered);
                    }
                }
                holder.sentVideoTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                holder.sentVideoImage.setTag(holder);
                holder.sentVideoDur.setText(message.getDurationTime());
                ProgressBarHelper pb_and_image = new ProgressBarHelper();
                pb_and_image.setImg(holder.sentVideoImage);
                pb_and_image.setPb(holder.pbSentVideo);
                new LoadImageTask().execute(pb_and_image);
            } else { // other Documents
                setupSentDocs(holder);
                holder.sentDocOutLyt.setVisibility(View.VISIBLE);
                holder.sentDocInLyt.setVisibility(View.VISIBLE);
                holder.sentDocInnLyt.setVisibility(View.VISIBLE);
                holder.sentDocLyt.setVisibility(View.VISIBLE);
                holder.sentDocImage.setVisibility(View.VISIBLE);
                holder.sentDocExt.setVisibility(View.VISIBLE);
                holder.sentDocFile.setVisibility(View.VISIBLE);
                holder.sentDocTime.setVisibility(View.VISIBLE);
                holder.sentDocSize.setVisibility(View.VISIBLE);
                holder.docDelivery.setVisibility(View.VISIBLE);
                holder.docDelivery.setImageResource(R.drawable.sent);
                if (message.getMessageDeliveryStatus().equals("true")) {
                    if(message.getReadMessage() == 1) {
                        holder.docDelivery.setImageResource(R.drawable.seen);
                    } else {
                        holder.docDelivery.setImageResource(R.drawable.delivered);
                    }
                }
                holder.sentDocTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                holder.sentDocFile.setText(message.getFileName());
                holder.sentDocExt.setText(message.getFileExt());
                holder.sentDocSize.setText(size);
                holder.sentDocInnLyt.setTag(holder);
            }
        } else if(messageStatus == 2) { // Received Messages
            resId = R.drawable.avatar;
            bm = bmReceiver;

            String mediaLink = message.getMediaLink();
            if(mediaLink != null)
                mediaLink = mediaLink.substring(mediaLink.lastIndexOf('/')).substring(1);

            goneCallout = holder.sendCallout;
            visiCallout = holder.recvCallout;
            goneProfile = holder.sendProfile;
            visiProfile = holder.recvProfile;
            if ((messageType != null) && (messageType.equals("text"))) {
                setupRecvMessage(holder);
                holder.recvMsgOutLyt.setVisibility(View.VISIBLE);
                holder.recvMsgLyt.setVisibility(View.VISIBLE);
                holder.recvMsgTime.setVisibility(View.VISIBLE);
                holder.recvMsg.setVisibility(View.VISIBLE);
                String time = new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase();
                holder.recvMsgTime.setText(time);
                holder.recvMsg.setText(message.getMessage());

                logInfo("Message", message.getMessage());
                //visiMsg.setMaxWidth(((RelativeLayout)visiMsg.getParent()).getWidth()/3);
                holder.recvMsg.setTag(holder);
            } else if ((messageType != null) && (messageType.contains("image/"))) {
                setupRecvImage(holder);
                if((thumbFile != null)  && ((file != null) && (file.isFile()))) {
                    holder.recvImgContOutLyt.setVisibility(View.VISIBLE);
                    holder.recvImgContInLyt.setVisibility(View.VISIBLE);
                    //holder.recvImgContInnLyt.setVisibility(View.VISIBLE);
                    holder.recvImage.setVisibility(View.VISIBLE);
                    holder.recvImgTime.setVisibility(View.VISIBLE);
                    holder.pbRecv.setVisibility(View.VISIBLE);
                    holder.recvImgTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                    holder.recvImage.setTag(holder);
                    ProgressBarHelper pb_and_image = new ProgressBarHelper();
                    pb_and_image.setImg(holder.recvImage);
                    pb_and_image.setPb(holder.pbRecv);
                    new LoadImageTask().execute(pb_and_image);
                } else {
                    holder.recvImgPenOutLyt.setVisibility(View.VISIBLE);
                    holder.recvImgPenInnLyt.setVisibility(View.VISIBLE);
                    holder.recvImgFile.setVisibility(View.VISIBLE);
                    holder.recvImageUpload.setVisibility(View.VISIBLE);
                    holder.recvImgFile.setText(mediaLink);
                    holder.recvImgPenInnLyt.setTag(holder);
                }
            } else if ((messageType != null) && (messageType.contains("audio/"))) {
                setupRecvAudio(holder);
                if((file != null) && (file.exists())) {
                    holder.recvAudioContOutLyt.setVisibility(View.VISIBLE);
                    holder.recvAudioContInLyt.setVisibility(View.VISIBLE);
                    holder.recvAudioContInnLyt.setVisibility(View.VISIBLE);
                    holder.recvAudioContInnnLyt.setVisibility(View.VISIBLE);
                    holder.recvAudioPlayLyt.setVisibility(View.VISIBLE);
                    holder.recvAudioDur.setVisibility(View.VISIBLE);
                    holder.recvAudioImg.setVisibility(View.VISIBLE);
                    holder.recvAudioPlay.setVisibility(View.VISIBLE);
                    holder.recvAudioPause.setVisibility(View.GONE);
                    holder.recvAudioSeek.setVisibility(View.VISIBLE);
                    holder.recvAudioTime.setVisibility(View.VISIBLE);
                    holder.recvAudioSize.setVisibility(View.VISIBLE);
                    holder.recvAudioTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                    holder.recvAudioSize.setText(size);
                    holder.recvAudioDur.setText(message.getDurationTime());
                } else {
                    holder.recvAudioPenOutLyt.setVisibility(View.VISIBLE);
                    holder.recvAudioPenInnLyt.setVisibility(View.VISIBLE);
                    holder.recvAudioFile.setVisibility(View.VISIBLE);
                    holder.recvAudioUpload.setVisibility(View.VISIBLE);
                    holder.recvAudioFile.setText(mediaLink);
                    holder.recvAudioPenOutLyt.setTag(holder);
                }
                holder.recvAudioPlay.setTag(holder);
                holder.recvAudioPause.setTag(holder);
                holder.recvAudioContInnLyt.setTag(holder);
            } else if ((messageType != null) && (messageType.contains("video/"))) {
                if((thumbFile != null)  && ((file != null) && (file.exists()))) {
                    setupRecvVideo(holder);
                    holder.recvVideoContOutLyt.setVisibility(View.VISIBLE);
                    holder.recvVideoContInnLyt.setVisibility(View.VISIBLE);
                    //holder.recvVideoContLyt.setVisibility(View.VISIBLE);
                    holder.pbRecvVideo.setVisibility(View.VISIBLE);
                    holder.recvVideoImage.setVisibility(View.VISIBLE);
                    holder.recvVideoImageAlpha.setVisibility(View.VISIBLE);
                    holder.recvVideoIcon.setVisibility(View.VISIBLE);
                    holder.recvVideoFile.setVisibility(View.VISIBLE);
                    holder.recvVideoTime.setVisibility(View.VISIBLE);
                    holder.recvVideoDur.setVisibility(View.VISIBLE);
                    holder.recvVideoTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                    holder.recvVideoDur.setText(message.getDurationTime());
                    holder.recvVideoImage.setTag(holder);
                    ProgressBarHelper pb_and_image = new ProgressBarHelper();
                    pb_and_image.setImg(holder.recvVideoImage);
                    pb_and_image.setPb(holder.pbRecvVideo);
                    new LoadImageTask().execute(pb_and_image);
                } else {
                    holder.recvVideoPenOutLyt.setVisibility(View.VISIBLE);
                    holder.recvVideoPenInnLyt.setVisibility(View.VISIBLE);
                    holder.recvVideoFile.setVisibility(View.VISIBLE);
                    holder.recvVideoPenOutLyt.setVisibility(View.VISIBLE);
                    holder.recvVideoUpload.setVisibility(View.VISIBLE);
                    holder.recvVideoFile.setText(mediaLink);
                    holder.recvVideoPenInnLyt.setTag(holder);
                }
            } else { // other Documents
                setupRecvDocs(holder);
                if((file != null) && (file.exists())) {
                    holder.recvDocContOutLyt.setVisibility(View.VISIBLE);
                    holder.recvDocContInLyt.setVisibility(View.VISIBLE);
                    holder.recvDocContInnLyt.setVisibility(View.VISIBLE);
                    holder.recvDocContLyt.setVisibility(View.VISIBLE);
                    holder.pbRecvDoc.setVisibility(View.VISIBLE);
                    holder.recvDocImage.setVisibility(View.VISIBLE);
                    holder.recvDocExt.setVisibility(View.VISIBLE);
                    holder.recvDoc.setVisibility(View.VISIBLE);
                    holder.recvDocSize.setVisibility(View.VISIBLE);
                    holder.recvDocTime.setVisibility(View.VISIBLE);
                    holder.recvDocTime.setText(new SimpleDateFormat("h:mm a").format(dateObj).toUpperCase());
                    holder.recvDoc.setText(message.getFileName());
                    holder.recvDocExt.setText(message.getFileExt());
                    holder.recvDocSize.setText(size);
                    holder.recvDocContInnLyt.setTag(holder);
                } else {
                    holder.recvDocPenOutLyt.setVisibility(View.VISIBLE);
                    holder.recvDocPenInnLyt.setVisibility(View.VISIBLE);
                    holder.recvDocFile.setVisibility(View.VISIBLE);
                    holder.recvDocUpload.setVisibility(View.VISIBLE);
                    holder.recvDocFile.setText(mediaLink);
                    holder.recvDocPenInnLyt.setTag(holder);
                }
            }
        }

        goneCallout.setVisibility(View.GONE);
        visiCallout.setVisibility(View.VISIBLE);

        goneProfile.setVisibility(View.GONE);
        visiProfile.setVisibility(View.VISIBLE);
        visiProfile.setTag(holder);

        ProgressBarHelper pb_and_image = new ProgressBarHelper();
        pb_and_image.setImg(visiProfile);
        pb_and_image.setPb(holder.pbProfile);
        pb_and_image.setResourceId(resId);
        pb_and_image.setBitmap(bm);
        new LoadProfileImage().execute(pb_and_image);

/*
        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(),R.drawable.avatar);
        RoundImage roundedImage = new RoundImage(bm);
        visiProfile.setImageDrawable(roundedImage);
*/

        return convertView;
    }

    @Override
    public void onClick(View v) {
        logInfo("onClick", "Called ..............");
        final ViewHolder holder = (ViewHolder) v.getTag();

        int id = v.getId();
        if((id == R.id.recv_image) || (id == R.id.send_image) || (id == R.id.recv_image_video) ||
                (id == R.id.sent_image_video) || (id == R.id.sent_doc_inn_lyt) || (id == R.id.recv_doc_content_inn_lyt)) {
            logInfo("onClick", "_image " + holder.messageId);
            logInfo("holder.mimeType", holder.mimeType);
            logInfo("holder.fileUri", holder.fileUri.toString());
            launchGalleryViewer(activity, holder.fileUri, holder.mimeType);
        } else if(id == R.id.recv_audio_content_inn) {
            logInfo("onClick", "recv_inner_layout_audio " + holder.messageId);
            playAudio(holder, holder.recvAudioPlay, holder.recvAudioPause, holder.recvAudioSeek);
        } else if(id == R.id.send_audio_inn_lyt) {
            logInfo("onClick", "sent_inner_layout_audio " + holder.messageId);
            playAudio(holder, holder.sentAudioPlay, holder.sentAudioPause, holder.sentAudioSeek);
        } else if(id == R.id.sent_play) {
            resetMediaProperties(holder, holder.sentAudioPlay, holder.sentAudioPause);
            playAudio(holder, holder.sentAudioPlay, holder.sentAudioPause, holder.sentAudioSeek);
        } else if(id == R.id.recv_play) {
            resetMediaProperties(holder, holder.recvAudioPlay, holder.recvAudioPause);
            playAudio(holder, holder.recvAudioPlay, holder.recvAudioPause, holder.recvAudioSeek);
        } else if(id == R.id.sent_pause) {
            stopAudio(holder, holder.sentAudioPlay, holder.sentAudioPause, holder.sentAudioSeek);
        } else if(id == R.id.recv_pause) {
            stopAudio(holder, holder.recvAudioPlay, holder.recvAudioPause, holder.recvAudioSeek);
        } else if(id == R.id.recv_image_pending_inn_lyt) {
            logInfo("onClick", "Pending Resource Called .......... " + holder.messageId);
            if(!holder.isImageDownloading) {
                holder.pbImgPenRecv.setVisibility(View.VISIBLE);
                holder.msgImageThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.isImageDownloading = true;

                        downloadAttachment(holder.messageId);
                        holder.isImageDownloading = false;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Thread(new Runnable() {
                            public void run() {
                                holder.pbImgPenRecv.setVisibility(View.GONE);
                            }
                        }));
                    }
                });
                holder.msgImageThread.start();
            }
        } else if(id == R.id.recv_audio_pending_in_lyt) {
            logInfo("onClick", "Pending Resource Called .......... " + holder.messageId);
            if(!holder.isAudioDownloading) {
                holder.pbAudioPenRecv.setVisibility(View.VISIBLE);
                holder.msgAudioThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.isAudioDownloading = true;
                        downloadAttachment(holder.messageId);
                        holder.isAudioDownloading = false;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Thread(new Runnable() {
                            public void run() {
                                holder.pbAudioPenRecv.setVisibility(View.GONE);
                            }
                        }));
                    }
                });
                holder.msgAudioThread.start();
            }
        } else if(id == R.id.recv_video_pending_inn_lyt) {
            if(!holder.isVideoDownloading) {
                holder.pbRecvVideoPen.setVisibility(View.VISIBLE);
                holder.msgVideoThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.isVideoDownloading = true;
                        downloadAttachment(holder.messageId);
                        holder.isVideoDownloading = false;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Thread(new Runnable() {
                            public void run() {
                                holder.pbRecvVideoPen.setVisibility(View.GONE);
                            }
                        }));
                    }
                });
                holder.msgVideoThread.start();
            }
        } else if(id == R.id.recv_doc_pending_inn_lyt) {
            if(!holder.isDocDownloading) {
                holder.pbRecvDoc.setVisibility(View.VISIBLE);
                holder.msgDocThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.isDocDownloading = true;
                        downloadAttachment(holder.messageId);
                        holder.isDocDownloading = false;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Thread(new Runnable() {
                            public void run() {
                                holder.pbRecvDoc.setVisibility(View.GONE);
                            }
                        }));
                    }
                });
                holder.msgDocThread.start();
            }
        }
    }

    private void downloadAttachment(long messageId) {
        String mimeType = null, filePath = null, mediaLink, fileExt, fileName, thumbFilePath = null;
        String currDate, time, mode, duration;
        long fileSize;

        ChatMessages message = myGlobals.dbHandler.getMessage(messageId, productId);
        mimeType = message.getMimeType();
        mediaLink = message.getMediaLink();
        fileExt = message.getFileExt();
        currDate = message.getDate();
        time = message.getTime();
        mode = message.getMode();
        duration = message.getDurationTime();
        fileSize = message.getFileSize();
        filePath = message.getFilePath();
        logInfo("filePath ", filePath);

        fileName = messageId + "." + fileExt;

        thumbFilePath = myGlobals.trovaApi.downloadAttachment(messageId, mimeType, mediaLink, fileExt);
        if(thumbFilePath != null) {
            logInfo("thumbFilePath", thumbFilePath);
            final ChatMessages chatMessage = new ChatMessages(null, currDate, time, 2, messageId, "false", mode, fileExt, fileName, fileSize, mimeType, thumbFilePath, filePath, duration, 0, mediaLink, 0);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Thread(new Runnable() {
                public void run() {
                    try {
                        addMessage(chatMessage);
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
            }));

            myGlobals.dbHandler.updateMessageLogs(messageId, productId, filePath, thumbFilePath);
        } else {
            logInfo("thumbFilePath", "NULL");
        }
    }

    private void resetMediaProperties(ViewHolder holder, ImageView playAudio, ImageView pauseAudio) {
        if(messageId != holder.messageId) {
            messageId = holder.messageId;
            oneTimeOnly = 0;
            playing = true;
            if(prevPlay != null) {
                prevPlay.setVisibility(View.VISIBLE);
                prevPause.setVisibility(View.GONE);
            }
            prevPlay = playAudio;
            prevPause = pauseAudio;
            holder.curPosition = 0;
            if(mPlayer != null) {
                playSeekBar.setProgress(0);
                mPlayer.seekTo(0);
                mPlayer.stop();
            }
            mPlayer = null;
            playSeekBar = null;
        }

    }

    private void playAudio(final ViewHolder holder, ImageView playAudio, ImageView pauseAudio, SeekBar seekbar) {
        playSeekBar = seekbar;
        if(mPlayer == null)
            logInfo("mPlayer", "NULL");
        else
            logInfo("mPlayer", "TRUE");

        if(mPlayer == null) {
            playing = true;
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mPlayer.setDataSource(activity, holder.fileUri);
                mPlayer.prepare();
                mPlayer.seekTo(holder.curPosition);
                mPlayer.start();
            } catch (IllegalArgumentException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            } catch (SecurityException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            } catch (IOException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        } else {
            mPlayer.seekTo(holder.curPosition);
            mPlayer.start();
        }

        finalTime = mPlayer.getDuration();
        startTime = mPlayer.getCurrentPosition();

        if(oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        if(mPlayer != null) {
            playAudio.setVisibility(View.GONE);
            pauseAudio.setVisibility(View.VISIBLE);
            seekbar.setProgress((int) startTime);
            myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (playing) {
                            sleep(100);
                            myHandler.postDelayed(UpdatePlayTime, 100);
                        }
                        holder.curPosition = mPlayer.getCurrentPosition();
                        mPlayer.stop();
                        mPlayer = null;
                        playSeekBar = null;
                    } catch (InterruptedException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
            });
            myThread.start();
        }
    }

    private Runnable UpdatePlayTime = new Runnable() {
        public void run() {
            if(playing) {
                if (mPlayer != null)
                    startTime = mPlayer.getCurrentPosition();
                if (playSeekBar != null)
                    playSeekBar.setProgress((int) startTime);
            }
        }
    };

    private void stopAudio(final ViewHolder holder, ImageView playAudio, ImageView pauseAudio, SeekBar seekbar) {
        if(mPlayer != null) {
            playing = false;
        }
        playAudio.setVisibility(View.VISIBLE);
        pauseAudio.setVisibility(View.GONE);
    }

    private void setupSentMessage(ViewHolder holder) {
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupRecvMessage(ViewHolder holder) {
        hideSentMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupSentImage(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupRecvImage(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupSentAudio(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupRecvAudio(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupSentVideo(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupRecvVideo(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideSentDocs(holder);
        hideRecvDocs(holder);
    }

    private void setupSentDocs(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideRecvDocs(holder);
    }

    private void setupRecvDocs(ViewHolder holder) {
        hideSentMessage(holder);
        hideRecvMessage(holder);
        hideSentImage(holder);
        hideRecvImage(holder);
        hideSentAudio(holder);
        hideRecvAudio(holder);
        hideSentVideo(holder);
        hideRecvVideo(holder);
        hideSentDocs(holder);
    }

    private void hideSentMessage(ViewHolder holder) {
        holder.sendMsgOutLyt.setVisibility(View.GONE);
        holder.sendMsgLyt.setVisibility(View.GONE);
        holder.sendMsg.setVisibility(View.GONE);
        holder.chatDelivery.setVisibility(View.GONE);
        holder.sendMsgTime.setVisibility(View.GONE);
    }

    private void hideRecvMessage(ViewHolder holder) {
        holder.recvMsgOutLyt.setVisibility(View.GONE);
        holder.recvMsgLyt.setVisibility(View.GONE);
        holder.recvMsg.setVisibility(View.GONE);
        holder.recvMsgTime.setVisibility(View.GONE);
    }

    private void hideSentImage(ViewHolder holder) {
        //holder.sendImgOutLyt.setVisibility(View.GONE);
        holder.sendImgInnLyt.setVisibility(View.GONE);
        holder.sendImgInLyt.setVisibility(View.GONE);
        holder.sendImgTime.setVisibility(View.GONE);
        holder.sendImage.setVisibility(View.GONE);
        holder.imageDelivery.setVisibility(View.GONE);
        holder.pbSent.setVisibility(View.GONE);
    }

    private void hideRecvImage(ViewHolder holder) {
        holder.recvImgContOutLyt.setVisibility(View.GONE);
        holder.recvImgPenOutLyt.setVisibility(View.GONE);
        holder.recvImgContInLyt.setVisibility(View.GONE);
        //holder.recvImgContInnLyt.setVisibility(View.GONE);
        holder.recvImgPenInnLyt.setVisibility(View.GONE);
        holder.pbImgPenRecv.setVisibility(View.GONE);
        holder.recvImgFile.setVisibility(View.GONE);
        holder.recvImageUpload.setVisibility(View.GONE);
        holder.recvImgTime.setVisibility(View.GONE);
    }

    private void hideSentAudio(ViewHolder holder) {
        holder.sendAudioOutLyt.setVisibility(View.GONE);
        holder.sendAudioInLyt.setVisibility(View.GONE);
        holder.sendAudioInnLyt.setVisibility(View.GONE);
        holder.sendAudioInnnLyt.setVisibility(View.GONE);
        holder.sendAudioPlayLyt.setVisibility(View.GONE);
        holder.sendAudioDur.setVisibility(View.GONE);
        holder.sendAudioImg.setVisibility(View.GONE);
        holder.sentAudioPlay.setVisibility(View.GONE);
        holder.sentAudioPause.setVisibility(View.GONE);
        holder.sentAudioSeek.setVisibility(View.GONE);
        holder.sendAudioTime.setVisibility(View.GONE);
        holder.audioDelivery.setVisibility(View.GONE);
        holder.sendAudioSize.setVisibility(View.GONE);
    }

    private void hideRecvAudio(ViewHolder holder) {
        holder.recvAudioContOutLyt.setVisibility(View.GONE);
        holder.recvAudioContInLyt.setVisibility(View.GONE);
        holder.recvAudioContInnLyt.setVisibility(View.GONE);
        holder.recvAudioContInnnLyt.setVisibility(View.GONE);
        holder.recvAudioPenOutLyt.setVisibility(View.GONE);
        holder.recvAudioPenInnLyt.setVisibility(View.GONE);
        holder.recvAudioPlayLyt.setVisibility(View.GONE);
        holder.recvAudioDur.setVisibility(View.GONE);
        holder.recvAudioImg.setVisibility(View.GONE);
        holder.recvAudioPlay.setVisibility(View.GONE);
        holder.recvAudioPause.setVisibility(View.GONE);
        holder.recvAudioSeek.setVisibility(View.GONE);
        holder.recvAudioTime.setVisibility(View.GONE);
        holder.recvAudioSize.setVisibility(View.GONE);
        holder.pbAudioPenRecv.setVisibility(View.GONE);
        holder.recvAudioFile.setVisibility(View.GONE);
        holder.recvAudioUpload.setVisibility(View.GONE);
    }

    private void hideSentVideo(ViewHolder holder) {
        //holder.sentVideoDummyLyt.setVisibility(View.GONE);
        holder.sentVideoOutLyt.setVisibility(View.GONE);
        holder.sentVideoInnLyt.setVisibility(View.GONE);
        //holder.sentVideoLyt.setVisibility(View.GONE);
        holder.sentVideoImage.setVisibility(View.GONE);
        holder.sentVideoAlphaImage.setVisibility(View.GONE);
        holder.sentVideoIcon.setVisibility(View.GONE);
        holder.sentVideoDur.setVisibility(View.GONE);
        holder.sentVideoTime.setVisibility(View.GONE);
        holder.videoDelivery.setVisibility(View.GONE);
        holder.pbSentVideo.setVisibility(View.GONE);
    }

    private void hideRecvVideo(ViewHolder holder) {
        holder.recvVideoPenOutLyt.setVisibility(View.GONE);
        holder.recvVideoPenInnLyt.setVisibility(View.GONE);
        holder.recvVideoContOutLyt.setVisibility(View.GONE);
        holder.recvVideoContInnLyt.setVisibility(View.GONE);
        //holder.recvVideoContLyt.setVisibility(View.GONE);
        holder.pbRecvVideo.setVisibility(View.GONE);
        holder.pbRecvVideoPen.setVisibility(View.GONE);
        holder.recvVideoImage.setVisibility(View.GONE);
        holder.recvVideoImageAlpha.setVisibility(View.GONE);
        holder.recvVideoUpload.setVisibility(View.GONE);
        holder.recvVideoIcon.setVisibility(View.GONE);
        holder.recvVideoFile.setVisibility(View.GONE);
        holder.recvVideoTime.setVisibility(View.GONE);
        holder.recvVideoDur.setVisibility(View.GONE);
    }

    private void hideSentDocs(ViewHolder holder) {
        holder.sentDocOutLyt.setVisibility(View.GONE);
        holder.sentDocInLyt.setVisibility(View.GONE);
        holder.sentDocInnLyt.setVisibility(View.GONE);
        holder.sentDocLyt.setVisibility(View.GONE);
        holder.sentDocImage.setVisibility(View.GONE);
        holder.sentDocExt.setVisibility(View.GONE);
        holder.sentDocFile.setVisibility(View.GONE);
        holder.sentDocTime.setVisibility(View.GONE);
        holder.sentDocSize.setVisibility(View.GONE);
        holder.docDelivery.setVisibility(View.GONE);
    }

    private void hideRecvDocs(ViewHolder holder) {
        holder.recvDocContOutLyt.setVisibility(View.GONE);
        holder.recvDocContInLyt.setVisibility(View.GONE);
        holder.recvDocContInnLyt.setVisibility(View.GONE);
        holder.recvDocContLyt.setVisibility(View.GONE);
        holder.pbRecvDoc.setVisibility(View.GONE);
        holder.recvDocImage.setVisibility(View.GONE);
        holder.recvDocExt.setVisibility(View.GONE);
        holder.recvDoc.setVisibility(View.GONE);
        holder.recvDocSize.setVisibility(View.GONE);
        holder.recvDocTime.setVisibility(View.GONE);
        holder.recvDocPenOutLyt.setVisibility(View.GONE);
        holder.recvDocPenInnLyt.setVisibility(View.GONE);
        holder.recvDocFile.setVisibility(View.GONE);
        holder.recvDocUpload.setVisibility(View.GONE);
    }

    public long getMessageId(ImageView imageView) {
        ViewHolder holder = (ViewHolder) imageView.getTag();

        return holder.messageId;
    }

    public void add(ChatMessages chatMessage) {
        chatMessages.put(chatMessage.getMessageId(), chatMessage);
    }

    public void updateReadMessage(){
        List<Long> msgIdList=new ArrayList<>(chatMessages.keySet());
        for (Long msgId : msgIdList) {
            //logInfo("msgId", String.valueOf(msgId));
            ChatMessages chatMessage = chatMessages.get(msgId);
            if(chatMessage != null) {
                chatMessage.setReadMessage(1);
                chatMessages.put(msgId, chatMessage);
            }
        }
    }

    public void updatemessageId(long msgId){
        ChatMessages chatMessage=chatMessages.get(msgId);
        if(chatMessage != null) {
            chatMessage.setMessageDeliveryStatus("true");
            chatMessages.put(msgId, chatMessage);
        }
    }

    public void clear(){
        chatMessages.clear();
    }

    public Uri getImageUri(ImageView imageView) {
        ViewHolder holder = (ViewHolder) imageView.getTag();

        return holder.thumbUri;
    }

}
