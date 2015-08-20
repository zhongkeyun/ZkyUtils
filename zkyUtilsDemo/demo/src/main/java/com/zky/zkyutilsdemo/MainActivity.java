package com.zky.zkyutilsdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.task.Priority;
import com.lidroid.xutils.task.PriorityAsyncTask;
import com.zky.zkyutils.http.VolleyRequestSender;
import com.zky.zkyutils.utils.Constants;
import com.zky.zkyutils.utils.LogUtils;
import com.zky.zkyutils.utils.ToastUtils;
import com.zky.zkyutilsdemo.backgroundTask.HandlerListener;
import com.zky.zkyutilsdemo.backgroundTask.NumberCalcHandler;
import com.zky.zkyutilsdemo.http.BindCIDRequest;
import com.zky.zkyutilsdemo.http.CheckVersionResponse;
import com.zky.zkyutilsdemo.http.VersionRequest;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private NumberCalcHandler calcHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_check_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VersionRequest versionRequest = new VersionRequest(new Response.Listener<CheckVersionResponse>() {
                    @Override
                    public void onResponse(CheckVersionResponse response) {
                        Toast.makeText(getApplication(), response.update == 1 ? "需要升级" : "最新版本", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                versionRequest.version = "1.0.0";
                VolleyRequestSender.getInstance(MyApplication.instance).send(versionRequest);
            }
        });

        findViewById(R.id.bt_bindCID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BindCIDRequest bindCIDRequest = new BindCIDRequest(new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {
                        Toast.makeText(getApplication(), "绑定失败", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                bindCIDRequest.client_id = "2342342424";
                bindCIDRequest.token = "24234dsfdsifdfsdf";

                VolleyRequestSender.getInstance(MyApplication.instance).send(bindCIDRequest);
            }
        });


        findViewById(R.id.bt_begin_calc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandlerListener listener = new HandlerListener() {
                    @Override
                    public void onStart() {
                        ToastUtils.showText(getApplicationContext(), "On start");
                        LogUtils.d(Constants.TASK_TAG, "On start");

                    }

                    @Override
                    public void onCancel(String result) {
                        ToastUtils.showText(getApplicationContext(), "On cancel result:" + result);
                        LogUtils.d(Constants.TASK_TAG, "On cancel");

                    }

                    @Override
                    public void onProgress(int i) {
                        ToastUtils.showText(getApplicationContext(), "current ：" + i);
                        LogUtils.d(Constants.TASK_TAG, "current ：" + i);
                    }

                    @Override
                    public void onFinish(String result) {
                        ToastUtils.showText(getApplicationContext(), "finish ：" + result);
                        LogUtils.d(Constants.TASK_TAG, "finish ：" + result);
                    }
                };

                calcHandler = new NumberCalcHandler(listener);
                calcHandler.execute(1, 10, 1 * 1000);

                new NumberCalcHandler(listener).execute(11, 20, 2 * 1000);
                new NumberCalcHandler(listener).execute(21, 30, 2 * 1000);
                new NumberCalcHandler(listener).execute(31, 40, 2 * 1000);
                new NumberCalcHandler(listener).execute(41, 50, 2 * 1000);
                new NumberCalcHandler(listener).execute(51, 60, 2 * 1000);
                new NumberCalcHandler(listener).execute(61, 70, 2 * 1000);
                new NumberCalcHandler(listener).execute(71, 80, 2 * 1000);
                new NumberCalcHandler(listener).execute(81, 90, 2 * 1000);
                new NumberCalcHandler(listener).execute(91, 100, 2 * 1000);
                new NumberCalcHandler(listener).execute(101, 110, 2 * 1000);
                new NumberCalcHandler(listener).execute(111, 120, 2 * 1000);
                new NumberCalcHandler(listener).execute(121, 130, 2 * 1000);
                NumberCalcHandler priorityUpHandler = new NumberCalcHandler(listener);
                priorityUpHandler.setPriority(Priority.UI_TOP);
                priorityUpHandler.execute(131, 140, 2 * 1000);
            }
        });

        findViewById(R.id.bt_cancel_cala).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calcHandler != null)
                    calcHandler.cancel();
            }
        });
    }

    private void downLoadFile() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download("url", "path", true, true, new RequestCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<File> result) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {
            }
        });
    }

    private void uploadFile() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", "value");
        params.addBodyParameter("file", new File("path"));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "uploadUrl",
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
