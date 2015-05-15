package app.zhengbang.teme.service;

import android.app.Activity;
import android.app.Dialog;
import app.zhengbang.teme.AppConstants;
import app.zhengbang.teme.application.TeMeApp;
import app.zhengbang.teme.bean.UserBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.manager.HttpManager;
import com.net.AsyncHttpClient;
import com.net.AsyncHttpClientHelper;
import com.net.AsyncHttpResponseHandler;
import com.net.RequestParams;
import com.util.*;

/**
 * @类名称: LoginService
 * @类描述:
 * @创建人：曹睿翔
 * @创建时间：2014年9月3日 下午5:17:02
 * @备注：
 */
public class LoginService {
    private Activity mContext;
    private boolean showPd_flag = true;
    private LoginSuccess loginSuccess;

    public LoginService(Activity mContext) {
        this.mContext = mContext;
    }


    public void autoUserLogin(LoginSuccess loginSuccess) {
        String username = PreferencesUtils.getString(mContext, AppConstants.LOGIN_ACCOUNT_KEY, "");
        String password = PreferencesUtils.getString(mContext, AppConstants.LOGIN_PSD_KEY, "");
        showPd_flag = false;
        this.loginSuccess = loginSuccess;
        login(username, password);
    }

    public void login(final String username, final String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
        	 if (loginSuccess != null)
                 loginSuccess.onLoginFail();
            return;
        }

        final Dialog loadingDialog = PromptManager.showLoadDataDialog(mContext, "正在登陆，请稍后...");
        final String deviceid = Tools.getDeviceId(mContext);
        if (username != null && username.equals("")) {
            if (showPd_flag)
                PromptManager.showCustomToast(mContext, "手机号不能为空");
            if (loginSuccess != null)
                loginSuccess.onLoginFail();
        } else if (password != null && password.equals("")) {
            if (showPd_flag)
                PromptManager.showCustomToast(mContext, "密码不能为空");
            if (loginSuccess != null)
                loginSuccess.onLoginFail();
        } else {
            if (showPd_flag)
                loadingDialog.show();
                AsyncHttpClient client = AsyncHttpClientHelper.getInstance(mContext);
                RequestParams requestParams = new RequestParams();
                requestParams.put("query", "login");

                JSONObject json = new JSONObject();
                json.put("phone", username);
                json.put("password", password);

                requestParams.put("data", json.toJSONString());
                LogUtil.info("Login-->" + requestParams.toString());
                client.get(mContext, AppConstants.TeMeHost, requestParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(String content) {
                        super.onSuccess(content);
                        try {
                            if (showPd_flag)
                                loadingDialog.dismiss();
                            JSONObject jsonObject = JSONObject.parseObject(content);
                            int key = jsonObject.getIntValue("result");
                            if (key == 200) {
                                PreferencesUtils.putString(mContext, AppConstants.LOGIN_ACCOUNT_KEY, username);
                                PreferencesUtils.putString(mContext, AppConstants.LOGIN_PSD_KEY, password);
                                UserBean data = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), UserBean.class);
                                /**
                                 * 设置到内存
                                 */
                                TeMeApp.getInstance().setCurrentUser(data);
                                if (loginSuccess != null)
                                    loginSuccess.onLoginSuccess();
                            } else {
                                String word = jsonObject.getJSONObject("data").getString("des");
                                if (StringUtils.isEmpty(word)) {
                                    word = "未知异常";
                                }
                                PromptManager.showCustomToast(mContext, word);
                                if (loginSuccess != null)
                                    loginSuccess.onLoginFail();
                            }
                        } catch (Exception e) {
                            L.e(String.valueOf(e.getMessage()));
                            if (loginSuccess != null)
                                loginSuccess.onLoginFail();
                        }
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        super.onFailure(error, content);
                        if (showPd_flag)
                            loadingDialog.dismiss();
                        if (loginSuccess != null)
                            loginSuccess.onLoginFail();
                    }
                });
        }
    }


    public interface LoginSuccess {
        void onLoginSuccess();

        void onLoginFail();
    }
}
