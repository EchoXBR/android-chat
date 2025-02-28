package cn.wildfire.chat.app.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
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
import cn.wildfire.chat.kit.utils.DeviceUtils;
import cn.wildfire.chat.kit.utils.FastJsonUtils;
import cn.wildfirechat.chat.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * use {@link SMSLoginActivity} instead
 */
@Deprecated
public class LoginActivity extends WfcBaseActivity {
    @Bind(R.id.loginButton)
    Button loginButton;
    @Bind(R.id.accountEditText)
    EditText accountEditText;
    @Bind(R.id.passwordEditText)
    EditText passwordEditText;

    @Override
    protected int contentLayout() {
        return R.layout.login_activity_account;
    }

    @Override
    protected boolean showHomeMenuItem() {
        return false;
    }

    @OnTextChanged(value = R.id.accountEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputAccount(Editable editable) {
        if (!TextUtils.isEmpty(passwordEditText.getText()) && !TextUtils.isEmpty(editable)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @OnTextChanged(value = R.id.passwordEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputPassword(Editable editable) {
        if (!TextUtils.isEmpty(accountEditText.getText()) && !TextUtils.isEmpty(editable)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }


//    @OnClick(R.id.loginButton)
//    void login() {
//        String account = accountEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
//        String url = "http://" + Config.APP_SERVER_HOST + ":" + Config.APP_SERVER_PORT + "/api/login";
//        Map<String, String> params = new HashMap<>();
//        params.put("name", account);
//        params.put("password", password);
//        try {
//            params.put("clientId", ChatManagerHolder.gChatManager.getClientId());
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(LoginActivity.this, "网络出来问题了。。。", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        MaterialDialog dialog = new MaterialDialog.Builder(this)
//                .content("登录中...")
//                .progress(true, 10)
//                .cancelable(false)
//                .build();
//        dialog.show();
//        OKHttpHelper.post(url, params, new SimpleCallback<LoginResult>() {
//            @Override
//            public void onUiSuccess(LoginResult loginResult) {
//                if (isFinishing()) {
//                    return;
//                }
//                ChatManagerHolder.gChatManager.connect(loginResult.getUserId(), loginResult.getToken());
//                SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
//                sp.edit()
//                        .putString("id", loginResult.getUserId())
//                        .putString("token", loginResult.getToken())
//                        .apply();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                dialog.dismiss();
//                finish();
//            }
//
//            @Override
//            public void onUiFailure(int code, String msg) {
//                if (isFinishing()) {
//                    return;
//                }
//                dialog.dismiss();
//            }
//        });
//    }



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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
