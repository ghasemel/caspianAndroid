package ir.caspiansoftware.caspianandroidapp.Report;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.core.content.FileProvider;

import java.io.File;

import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.R;

public class ReportFragment extends CaspianFragment implements IFragmentCallback {
    private static final String TAG = "ReportFragment";
    private static final String EXTRA_FILE_NAME = ReportActivity.EXTRA_FILE_NAME;

    private WebView mWebView;


    public static ReportFragment newInstance(String fileName) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FILE_NAME, fileName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void mapViews(View parentView) {
        Log.d(TAG, "mapViews(): start...");
        mWebView = parentView.findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        Log.d(TAG, "mapViews(): end");
    }

    @Override
    protected void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews()");

        if (getActivity() == null || getContext() == null) return;

        Intent intent = getActivity().getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_FILE_NAME)) return;

        String htmlFilePath = intent.getStringExtra(EXTRA_FILE_NAME);
        if (htmlFilePath == null) return;

        File htmlFile = new File(htmlFilePath);
        if (!htmlFile.exists()) return;

        Uri htmlUri = FileProvider.getUriForFile(
                getContext(),
                getContext().getPackageName() + ".provider",
                htmlFile
        );

        if (htmlUri == null) return;

        // Grant temporary read permission to the WebView
        getContext().grantUriPermission(
                "com.android.chrome",
                htmlUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        );

        // Load the file into the WebView
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setBuiltInZoomControls(true); // Enable pinch-to-zoom
        mWebView.getSettings().setDisplayZoomControls(true); // Hide zoom controls
        mWebView.getSettings().setUseWideViewPort(true); // Load full-size webpage
        mWebView.getSettings().setLoadWithOverviewMode(false); // Prevent shrinking
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // Show scrollbars
        mWebView.loadUrl(htmlUri.toString());

        printWebView(mWebView, getContext());
    }


    @Override
    protected int getLayoutId() {
        return R.layout.report_view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionTypes formActionTypes) {
        Log.d(TAG, "onMyActivityCallback(): start...");
    }

    public void printWebView(WebView webView, Context context) {
        if (webView == null || context == null) return;

        // Get the print manager
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);

        // Create a print adapter from the WebView
        String jobName = context.getString(R.string.app_name) + " Document";
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("default", "default", 203, 203))
                .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)
                .build();

        printManager.print(
                jobName,
                webView.createPrintDocumentAdapter(jobName),
                attributes
        );
    }
}
