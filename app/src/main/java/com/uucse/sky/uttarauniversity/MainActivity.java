package com.uucse.sky.uttarauniversity;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.textclassifier.TextSelection;
import android.view.textclassifier.TextSelection.Request;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends AppCompatActivity {
     String currentUrl="https://www.uttarauniversity.edu.bd/";
AdvancedWebView webView;
 SwipeRefreshLayout swipeRefreshLayout;
 ProgressBar progressBar;
 TextView progressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.progressBar);
        progressTextView=findViewById(R.id.progressTextView);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_dark,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                Toast.makeText(MainActivity.this,"Loading....please wait",Toast.LENGTH_SHORT).show();
                Load(currentUrl);
            }
        });
 Load(currentUrl);
    }

public void Load(String url)
{

    try {

        swipeRefreshLayout.setRefreshing(true);
        webView = (AdvancedWebView) findViewById(R.id.webviewid);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setThirdPartyCookiesEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {

                progressBar.setProgress(progress);
                progressTextView.setText(progress+"%");

            }


        });
        webView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                currentUrl = url;
                return super.shouldOverrideUrlLoading(view, url);

            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Toast.makeText(MainActivity.this, "Error! " + description, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                Toast.makeText(MainActivity.this,"Loading....please wait",Toast.LENGTH_SHORT).show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.splash).setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                findViewById(R.id.swipe).setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);




            }

        });

       webView.setDownloadListener(new DownloadListener() {

            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_QUICK_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

               /* DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                String fileName=URLUtil.guessFileName(url,contentDisposition,mimetype);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
                DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);*/
            }
        });

        webView.loadUrl(url);


    } catch (Exception e) {
        Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_SHORT).show();
    }



}
    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack())
        {
            Toast.makeText(MainActivity.this,"Loading....please wait",Toast.LENGTH_SHORT).show();
            webView.goBack();
        }
        else
        {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Exit!");
            alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);
            alertDialog.setMessage("Are you sure you want to exit?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    Toast.makeText(MainActivity.this,"Exit Successfully!",Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
alertDialog.setCancelable(false);
alertDialog.show();
        }
    }
}
