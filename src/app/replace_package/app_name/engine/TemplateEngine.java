package app.zhengbang.teme.engine;

import android.app.Activity;
import android.os.Bundle;
import app.zhengbang.teme.AppConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.event.EventBus;
import com.event.EventMessage;
import com.net.AsyncHttpClient;
import com.net.AsyncHttpClientHelper;
import com.net.AsyncHttpResponseHandler;
import com.net.RequestParams;
import com.util.ListUtils;
import com.util.LogUtil;
import com.util.PromptManager;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 模板类
 *
 * @author pc
 */
public class TemplateEngine {

    private String TAG = "TemplateEngine";
    private Activity mContext;

    TemplateEngine(Activity context) {
        this.mContext = context;
    }

    private void getDataFromServerByGet(final int requestCode) {
        AsyncHttpClient client = AsyncHttpClientHelper.getInstance(mContext);
        RequestParams requestParams = new RequestParams();
        requestParams.put("m", "User");
        requestParams.put("a", "get_msg");
        LogUtil.info("getServiceData_DealMessageBean" + requestParams.toString());
        client.get(mContext, AppConstants.HOST, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    parseData(content, requestCode);
                } catch (Exception e) {
                    LogUtil.info(e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);

            }
        });
    }

    private void getDataFromServerByPost(final int requestCode) {
        JSONArray order_list = new JSONArray();
        int index = 0;
        JSONObject jsonObject = new JSONObject();
        // jsonObject.put("uid", uid);
        // jsonObject.put("total", total);

        jsonObject.put("order_list", order_list);
        PromptManager.showLoadDataDialog(mContext, "");
        AsyncHttpClient client = AsyncHttpClientHelper.getInstance(mContext);
        try {
            StringEntity entity = new StringEntity(jsonObject.toJSONString(), AppConstants.ENCODE);
            LogUtil.info(TAG + jsonObject.toJSONString());
            client.post(mContext, AppConstants.HOST + "?m=Goods&a=submit_order", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String content) {
                            super.onSuccess(content);
                            parseData(content, requestCode);
                        }

                        @Override
                        public void onFailure(Throwable error, String content) {
                            super.onFailure(error, content);
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void parseData(String content, int requestCode) {
        JSONObject jsonObject = JSON.parseObject(content);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        Bundle bundle = new Bundle();
        if (!ListUtils.isEmpty(new ArrayList<>())) {
            bundle.putSerializable("dealMessages", new ArrayList<>());
            bundle.putBoolean("success", true);
        } else {
            bundle.putSerializable("dealMessages", new ArrayList<>());
            bundle.putBoolean("success", false);
        }
        EventBus.getDefault().post(new EventMessage(requestCode, AppConstants.TemplateEngine, bundle));
    }
}
