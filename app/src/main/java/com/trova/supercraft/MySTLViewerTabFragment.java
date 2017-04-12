package com.trova.supercraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.cordova.Config;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.logInfo;

/**
 * Created by Panchakshari on 16/3/2017.
 */

public class MySTLViewerTabFragment extends Fragment {
    private static Context context;
    private View contentview = null;
    WebView cwv;
    boolean posted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logInfo("MySTLViewerTabFragment", "0 .........");
        contentview = inflater.inflate(R.layout.stl_viewer_tab, container, false);
        context = this.getContext();

        cwv = (WebView) contentview.findViewById(R.id.cordovaview);

        cwv.getSettings().setPluginState(WebSettings.PluginState.ON);
        cwv.getSettings().setAllowFileAccess(true);
        cwv.getSettings().setAllowContentAccess(true);
        cwv.getSettings().setAllowFileAccessFromFileURLs(true);
        cwv.getSettings().setAllowUniversalAccessFromFileURLs(true);
        cwv.setWebViewClient(new MyWebClient());
        cwv.clearCache(true);
        cwv.clearHistory();
        logInfo("MySTLViewerTabFragment", "onCreate Called .........");

        Config.init(this.getActivity());
        JavaScriptInterface jsInterface = new JavaScriptInterface(this.getContext(), myGlobals.userId);
        cwv.getSettings().setJavaScriptEnabled(true);
        cwv.addJavascriptInterface(jsInterface, "JSInterface");
        cwv.getSettings().setLoadsImagesAutomatically(true);
        cwv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        //cwv.loadUrl("javascript:setSTLFilePath(\""+myGlobals.currJobActive.getStlFilePath()+"\")");
        logInfo("MySTLViewerTabFragment", "onCreate Called .........");

        cwv.loadUrl("file:///android_asset/www/index.html#stlviewcanvas");
        logInfo("MySTLViewerTabFragment", "onCreate Called .........");

        return contentview;
    }

    public class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            logInfo("onLoadResource", "Called .......");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            logInfo("onPageStarted", "Called .......");
            //contentview.findViewById(R.id.cordovaview_main).setVisibility(View.GONE);
            //findViewById(R.id.edit).setVisibility(View.GONE);
            //contentview.findViewById(R.id.cordovaview_splash).setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            logInfo("onPageFinished", "Called .......");

            if(!posted) {
                posted = true;
                //findViewById(R.id.cordovaview_splash).setVisibility(View.GONE);
                //myToolbar.setVisibility(View.VISIBLE);
                //findViewById(R.id.cordovaview_main).setVisibility(View.VISIBLE);
                //findViewById(R.id.edit).setVisibility(View.VISIBLE);
            }
        }
    }

}
