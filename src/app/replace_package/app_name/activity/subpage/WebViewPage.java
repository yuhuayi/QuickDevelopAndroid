package app.zhengbang.teme.activity.subpage;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import app.zhengbang.teme.activity.base.BaseSubFragment;
import app.zhengbang.teme.activity.mainpage.publish.PublishProductPage;
import app.zhengbang.teme.activity.subpage.myself.PersonSpaceEditDataPage;
import com.event.EventMessage;
import com.util.LogUtil;
import com.util.PromptManager;
import com.util.StringUtils;
import com.zhengbang.TeMe.R;

public class WebViewPage extends BaseSubFragment {

    public static final String SEARCH_HISTORY = "WebViewPage";
    private WebView mWebView;
    private String type;
    private boolean main;
    private TextView title_tv;
    private TextView title_right_tv;
    private View ic_back;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View contactsLayout = inflater.inflate(R.layout.web_view_page,container, false);
        return contactsLayout;
    }

    @Override
    protected void findViewById(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        ic_back = (View) view.findViewById(R.id.title_back_img);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_right_tv = (TextView) view.findViewById(R.id.title_right_tv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                popBackStack();
                break;
            case R.id.title_right_tv:
                PromptManager.showCustomToast(mActivity,"还需要考虑需求");
//                Bundle bundle = new Bundle();
//                mActivity.changeSubFragment(this, mActivity.fragment_content_id,
//                        PublishProductPage.class.getName(),
//                        null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void setListener() {
        ic_back.setOnClickListener(this);
        title_right_tv.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void processLogic() {
        try {
            if (mWebView != null) {
                mWebView.setWebViewClient(new NewsClient());
                String url = getArguments().getString("url");
                String titleText = getArguments().getString("title");
                type = getArguments().getString("type");

                if (!StringUtils.isEmpty(titleText)) {
                    title_tv.setText(titleText);
                }
                if (StringUtils.isEmpty(url)) {
                    url = "http://www.baidu.com";
                }
                mWebView.loadUrl(url);
                LogUtil.info("url" + url);
                LogUtil.info("title" + titleText);
                if (getPhoneAndroidSDK() >= 14) {// 4.0 需打开硬件加速
                    mActivity.getWindow().setFlags(0x1000000, 0x1000000);
                }
                WebSettings mWebSettings = mWebView.getSettings();
                mWebSettings.setJavaScriptEnabled(true);
                mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                mWebSettings.setPluginState(PluginState.ON);
//                mWebSettings.setPluginsEnabled(true);
                mWebSettings.setAllowFileAccess(true);
                mWebSettings.setLoadWithOverviewMode(true);
                mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
                mWebSettings.setDisplayZoomControls(true);
                mWebSettings.setBuiltInZoomControls(true);
                mWebSettings.setUseWideViewPort(true);
                mWebSettings.setSupportZoom(true);
                mWebView.setWebChromeClient(new MyWebChromeClient());
                mWebView.setWebViewClient(new MyWebViewClient());
                mWebView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                            case MotionEvent.ACTION_UP:
                                if (!v.hasFocus()) {
                                    v.requestFocus();
                                }
                                break;
                        }
                        return false;
                    }
                });
            } else {

            }
        } catch (Exception e) {
            LogUtil.info(String.valueOf(e.getMessage()));
        }
    }

    public static int getPhoneAndroidSDK() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;

    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    @Override
    public void onEventMainThread(EventMessage event) {

    }

    /**
     * 开通三方：openPayment
     * <p/>
     * 投资接口：invest
     * <p/>
     * 充值接口：recharge
     * <p/>
     * 绑定银行卡：bindBankCard
     * <p/>
     * 提现接口：cash
     */
    class NewsClient extends WebViewClient {

        private AlertDialog alertDialog;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (url != null && url.contains("bankcard/addCard")) {
                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Bundle bundle = new Bundle();
                    }
                }, 200);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {

            return true;
        }
    }

    private void htmlGoBack() {
        try {
            String currentGoBackUrl = mWebView.getUrl();
            if (mWebView != null) {
                if (currentGoBackUrl != null) {
                    mWebView.loadData("", "text/html; charset=UTF-8", null);
                    popBackStack();
                } else {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.info(String.valueOf(e.getMessage()));
        }
    }

    public class MyWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onPause() {// 继承自Activity
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {// 继承自Activity
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
