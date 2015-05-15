package com.view.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 * 
 * @author 农民伯伯
 * @see http://www.cnblogs.com/over140/archive/2013/03/07/2947721.html
 * 
 */
@SuppressLint({ "SetJavaScriptEnabled", "ClickableViewAccessibility" })
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

	private ProgressBar progressbar;
	private Context mContext;
	private String lastErrorURL = "";
	@SuppressWarnings("unused")
	private String call, service, action, taskID, args;
	private int sameErrorCount = 0;
	@SuppressWarnings("unused")
	private boolean isOver = false;
	public Handler handler = new Handler(mContext.getMainLooper());

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
		this.clearView();
		addView(progressbar);
		WebSettings settings = this.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setAppCacheEnabled(true);
		this.setVerticalScrollBarEnabled(false);
		this.setOnTouchListener(new OnTouchListener() {
			// 增加为了让webview内部的文本编辑框能输入
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

		this.getSettings().setLightTouchEnabled(true);
		this.setWebViewClient(new WebViewClient() {
			public void onLoadResource(WebView view, String url) {
				System.out.println("Load resource=" + url);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				try {
					view.loadUrl(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// Log.WriteErrorLog("webkit error:" + errorCode + "\n" +
				// description + "\n" + "failingurl=" + failingUrl);
			}

			public void onPageFinished(WebView view, String url) {
				isOver = true;
				super.onPageFinished(view, url);
				// addNewView(WebBrowserView.this);
				// WebBrowserView.this.OnWebBrowserDocumentCompletedEventHandler(null,
				// null);
				// this.requestFocus(View.FOCUS_UP | View.FOCUS_DOWN);
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				isOver = false;
				super.onPageStarted(view, url, favicon);
			}
		});

		// 设置浏览器相应js的alert function和confirm function
		this.setWebChromeClient(new WebChromeClient());
		this.addJavascriptInterface(new Object() {

			@SuppressWarnings("unused")
			public void JavascriptHandler(String _call, String _service, String _action, String _taskID, String _args) {
				ProgressWebView.this.call = _call;
				ProgressWebView.this.service = _service;
				ProgressWebView.this.action = _action;
				ProgressWebView.this.taskID = _taskID;
				ProgressWebView.this.args = _args;
				handler.post(new Runnable() {

					public void run() {
						ProgressWebView.this.JavascriptHandler();
					}
				});
			}
		}, "CallCode");
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {

		public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			// builder.setTitle(R.string.title);
			builder.setMessage(message);
			builder.setPositiveButton("OK", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					result.confirm();
				}
			});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}

		public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			// builder.setTitle("");
			builder.setMessage(message);
			builder.setPositiveButton("OK", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					result.confirm();
				}
			});
			builder.setNeutralButton("Cancel", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					result.cancel();
				}
			});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}

		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			// 测试代码，有可能导致死循环,连续5次出同样的错才停止刷新
			// && !message.equals 修改原因 ：小胖地图点击刷新
			if (message.contains("Uncaught") && !message.equals("Uncaught TypeError: Cannot read property 'style' of null")) {
				String url = ProgressWebView.this.getUrl();
				if (lastErrorURL != null && lastErrorURL.equals(url)) {
					if (sameErrorCount < 5) {
						ProgressWebView.this.reload();
						sameErrorCount++;
					} else {
						// Toast.makeText(ExternalCommandMgr.Instance().getMainActivity(),
						// message + "\n" + url , Toast.LENGTH_SHORT).show();
					}
				} else {
					ProgressWebView.this.reload();
					sameErrorCount = 0;
				}
				lastErrorURL = url;
			}
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	private void JavascriptHandler() {

		try {
			// _invokeParas = new InvokeParas(service, action, taskID, args);
		} catch (Exception e) {
		}
		// 异步调用
		new Thread() {
			public void run() {
				try {
					// _callBackResult = _extService.runCommand(_invokeParas);
				} catch (Exception _err) {
					// MyLog.Instance().WriteErrorLog(_err);
					// _callBackResult = new InvokeResult(_invokeParas, _err);
				}
			}
		}.start();

		// if (_callBackResult == null)
		// {
		// _httpContext.Response.WriteResult(null);
		// return;
		// }
		// _httpContext.Response.ContentType = _callBackResult.GetContentType();
		// _httpContext.Response.WriteResult(_callBackResult.GetJsonResult());
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
