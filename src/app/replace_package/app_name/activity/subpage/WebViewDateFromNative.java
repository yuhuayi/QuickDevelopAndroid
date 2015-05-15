package app.zhengbang.teme.activity.subpage;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import app.zhengbang.teme.activity.base.BaseSubFragment;
import com.util.StringUtils;
import com.zhengbang.TeMe.R;

import java.io.IOException;
import java.io.InputStream;

public class WebViewDateFromNative extends BaseSubFragment {

    private WebView mWebView;
    private TextView title_tv;
    private TextView title_right_tv;
    private View ic_back;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View settingLayout = inflater.inflate(R.layout.web_view_data_from_native, container, false);
        return settingLayout;
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
            default:
                break;
        }
    }

    @Override
    protected void setListener() {
        ic_back.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (url.contains("tel")) {
                    Intent localIntent1 = new Intent("android.intent.action.DIAL", uri);
                    startActivity(localIntent1);
                } else if (url.contains("mailto")) {
                    Intent localIntent2 = new Intent("android.intent.action.SENDTO", uri);
                    startActivity(localIntent2);
                } else if (url.contains("http")) {
                    Intent localIntent3 = new Intent("android.intent.action.VIEW", uri);
                    startActivity(localIntent3);
                }

                return true;
            }
        });
        int rId = getArguments().getInt("rId");
        String titleText = getArguments().getString("title");
        if (!StringUtils.isEmpty(titleText)){
            title_tv.setText(titleText);
        }
        mWebView.loadDataWithBaseURL(null, getContent(rId), "text/html", "utf-8", null);
    }


    private String getContent(int id) {
        InputStream localInputStream = getResources().openRawResource(id);
        StringBuilder localStringBuilder1 = new StringBuilder();
        byte[] arrayOfByte = new byte[4096];
        try {
            while (true) {
                int i = localInputStream.read(arrayOfByte);
                if (i == -1)
                    return localStringBuilder1.toString();
                localStringBuilder1.append(new String(arrayOfByte, 0, i));
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
            return "";
        }
    }
}
