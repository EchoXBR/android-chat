package cn.wildfire.chat.app.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.login.model.LoginResult;
import cn.wildfire.chat.app.main.MainActivity;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.net.HttpMethods;
import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.net.SimpleCallback;
import cn.wildfire.chat.kit.net.base.StatusResult;
import cn.wildfire.chat.kit.utils.DeviceUtils;
import cn.wildfire.chat.kit.utils.FastJsonUtils;
import cn.wildfirechat.chat.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SMSLoginActivity extends WfcBaseActivity {
    @Bind(R.id.loginButton)
    Button loginButton;
    @Bind(R.id.phoneNumberEditText)
    EditText phoneNumberEditText;
    @Bind(R.id.authCodeEditText)
    EditText authCodeEditText;
    @Bind(R.id.requestAuthCodeButton)
    Button requestAuthCodeButton;

    private String phoneNumber;

    @Override
    protected int contentLayout() {
        return R.layout.login_activity_sms;
    }

    @OnTextChanged(value = R.id.phoneNumberEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputPhoneNumber(Editable editable) {
        String phone = editable.toString().trim();
        if (phone.length() == 11) {
            requestAuthCodeButton.setEnabled(true);
        } else {
            requestAuthCodeButton.setEnabled(false);
            loginButton.setEnabled(false);
        }
    }

    @OnTextChanged(value = R.id.authCodeEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputAuthCode(Editable editable) {
        if (editable.toString().length() > 2) {
            loginButton.setEnabled(true);
        }
    }

    @Override
    protected boolean showHomeMenuItem() {
        return false;
    }

//    @OnClick(R.id.loginButton)
    void login() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String authCode = authCodeEditText.getText().toString().trim();

        String url = "http://" + Config.APP_SERVER_HOST + ":" + Config.APP_SERVER_PORT + "/pttapp/login";
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("code", authCode);
        try {
            params.put("clientId", ChatManagerHolder.gChatManager.getClientId());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SMSLoginActivity.this, "网络出来问题了。。。", Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("登录中...")
                .progress(true, 100)
                .cancelable(false)
                .build();
        dialog.show();
        OKHttpHelper.post(url, params, new SimpleCallback<LoginResult>() {
            @Override
            public void onUiSuccess(LoginResult loginResult) {
                if (isFinishing()) {
                    return;
                }
                dialog.dismiss();
//                ChatManagerHolder.gChatManager.connect(loginResult.getUserId(), loginResult.getToken());
//                SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
//                sp.edit()
//                        .putString("id", loginResult.getUserId())
//                        .putString("token", loginResult.getToken())
//                        .apply();
                Intent intent = new Intent(SMSLoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onUiFailure(int code, String msg) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(SMSLoginActivity.this, "登录失败：" + code + " " + msg, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }





    //登陆
    @OnClick(R.id.loginButton)
    void loginb() {


        String imei = DeviceUtils.getIMEI(getApplicationContext());

        Map<String, String> params = new HashMap<>();
        params.put("imei", imei.substring(0,imei.length()-1));
        params.put("token", "speedata");
        String clientId = "";
        try {
            clientId = ChatManagerHolder.gChatManager.getClientId();
            params.put("clientId", clientId);

        } catch (Exception e) {
            e.printStackTrace();

            return;
        }

        String finalClientId = clientId;

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("登录中...")
                .progress(true, 10)
                .cancelable(false)
                .build();
        dialog.show();
        String sendData = FastJsonUtils.toJSONString(params);
        HttpMethods.getInstance().login(sendData, new Observer<LoginResult>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(LoginResult loginResult) {
                if (loginResult != null && loginResult.getResult()!=null) {
                    LoginResult.ResultBean result = loginResult.getResult();

                    ChatManagerHolder.gChatManager.connect(result.getDeviceId(), result.getToken());


                    SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                    sp.edit()
                            .putString("id", result.getDeviceId())
                            .putString("token",  result.getToken())
                            .apply();
                    Intent intent = new Intent(SMSLoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                    //                    EventBus.getDefault().post(new MsgEvent(Constant.EVENT_LOGIN, "状态：已登陆"));
                }
                loginResult.getResult();

            }

            @Override
            public void onError(Throwable e) {
                //                EventBus.getDefault().post(new MsgEvent(Constant.EVENT_ERROR, e.getMessage()));
                dialog.dismiss();
                Toast.makeText(SMSLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private Handler handler = new Handler();

    @OnClick(R.id.requestAuthCodeButton)
    void requestAuthCode() {
        requestAuthCodeButton.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    requestAuthCodeButton.setEnabled(true);
                }
            }
        }, 60 * 1000);

        Toast.makeText(this, "请求验证码...", Toast.LENGTH_SHORT).show();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String url = "http://" + Config.APP_SERVER_HOST + ":" + Config.APP_SERVER_PORT + "/send_code";
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        OKHttpHelper.post(url, params, new SimpleCallback<StatusResult>() {
            @Override
            public void onUiSuccess(StatusResult statusResult) {
                if (statusResult.getCode() == 0) {
                    Toast.makeText(SMSLoginActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SMSLoginActivity.this, "发送验证码失败: " + statusResult.getCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUiFailure(int code, String msg) {
                Toast.makeText(SMSLoginActivity.this, "发送验证码失败: " + msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
