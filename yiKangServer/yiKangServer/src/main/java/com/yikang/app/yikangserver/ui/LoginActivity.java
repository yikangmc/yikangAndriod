package com.yikang.app.yikangserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.yikang.app.yikangserver.R;
import com.yikang.app.yikangserver.api.Api;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.application.AppConfig;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.receiver.UserInfoAlteredReceiver;
import com.yikang.app.yikangserver.utils.CachUtils;
import com.yikang.app.yikangserver.utils.UIHelper;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements Handler.Callback,
        OnClickListener, PlatformActionListener {
    private static final String TAG = "LoginActivity";
    /**
     * 标识是否进入过主页面
     */
    public static final String START_MAIN = "start_main";
    private EditText edtUserId, edtPassw;
    private Button btnLogin;
    private TextView bt_third_party_login,bt_third_party_login_wechat_sina,bt_third_party_login_wechat;

    private TextView tvRegister, tvFindPassw;
    private String accessTicket;
    private int threePartNum;//第三方登录的来源  0:qq  1:新浪微博   2：微信
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR= 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private PlatformDb platDB;
    private HashMap<String, Object> res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        initContent();
        initTitleBar(getString(R.string.login_title));
        if (AppContext.get(AppConfig.PRE_APP_FIRST_USE, true)) {
           // toRegister();
            AppContext.set(AppConfig.PRE_APP_FIRST_USE, false);
        }
    }

    @Override
    protected void findViews() {
        edtUserId = (EditText) findViewById(R.id.edt_login_userId);
        edtPassw = (EditText) findViewById(R.id.edt_login_passw);
        btnLogin = (Button) findViewById(R.id.bt_login_login);
        bt_third_party_login = (TextView) findViewById(R.id.bt_third_party_login);
        bt_third_party_login_wechat_sina = (TextView) findViewById(R.id.bt_third_party_login_wechat_sina);
        bt_third_party_login_wechat = (TextView) findViewById(R.id.bt_third_party_login_wechat);
        tvRegister = (TextView) findViewById(R.id.tv_login_register);
        tvFindPassw = (TextView) findViewById(R.id.tv_login_forget);

        edtUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    edtPassw.setText("");
                }
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void getData() {
    }

    @Override
    protected void initViewContent() {
        btnLogin.setOnClickListener(this);
        bt_third_party_login.setOnClickListener(this);
        bt_third_party_login_wechat_sina.setOnClickListener(this);
        bt_third_party_login_wechat.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvFindPassw.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppConfig config = AppConfig.getAppConfig(this);
        String userName = config.getProperty("login.userName");
        String passw = config.getProperty("login.password");
        boolean autoLogin = AppContext.get("autoLogin", false);
        if (!TextUtils.isEmpty(userName) && !userName.equals(edtUserId.getText())) {

            if(userName.trim().length()==11) {
                edtUserId.setText(userName);
                edtPassw.setText(passw);
            }
        }
        if (autoLogin) {
            login(userName, passw);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_login_login:
                String userId = edtUserId.getText().toString();
                String passw = edtPassw.getText().toString();
                if (TextUtils.isEmpty(userId) //|| userId.length() != 11
                        || TextUtils.isEmpty(passw)) {
                    AppContext.showToast(getString(R.string.login_passw_error_tip));
                    return;
                }
                login(userId, passw);
                break;
            case R.id.tv_login_register:
                toRegister();// 注册完直接登录，不会回到这个页面
                break;
            case R.id.tv_login_forget:
                findPassw();
                break;
            case R.id.bt_third_party_login://qq
                authorize(new QZone(this),0);
                break;
            case R.id.bt_third_party_login_wechat_sina://新浪
                authorize(new SinaWeibo(this),1);
                break;
            case R.id.bt_third_party_login_wechat://微信
                authorize(new Wechat(this),2);
                break;
            default:
                break;
        }
    }

    private void authorize(Platform plat,int i) {
        threePartNum=i;
        String userId = null;
        plat.removeAccount();
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.showUser(null);

    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        this.res=res;
        platDB = platform.getDb();//获取数平台数据DB
        //通过DB获取各种数据
        platDB.getToken();
        platDB.getExpiresTime();//过期时间
        platDB.getUserGender();
        platDB.getUserIcon();
        platDB.getUserId();
        platDB.getUserName();
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
        }

    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void logins(String plat, String gender, final String userId, HashMap<String, Object> userInfo, int userSource) {
       /* Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);*/
        showWaitingUI();
        Api.registerNewFromThreePart(plat, gender, userId, userInfo, userSource,"14",new ResponseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
               loginThreePart(userId);
            hideWaitingUI();
            }

            @Override
            public void onFailure(String status, String message) {
                AppContext.showToast(message);
                //loginThreePart(userId);
                hideWaitingUI();
            }
        });
    }

    /**
     * 登录
     */
    private void loginThreePart(final String userName) {
        showWaitingUI();
        Api.loginThreePart(userName, new LoginHandler(userName));

    }

    /**登录回调*/
    private class LoginHandler implements ResponseCallback<String> {
        private String userName;


        private LoginHandler(String userName) {
            this.userName = userName;
        }


        @Override
        public void onSuccess(String ticket) {
            hideWaitingUI();
            finish();
            AppContext.getAppContext().updateAccessTicket(ticket);
            AppConfig appConfig = AppConfig.getAppConfig(getApplicationContext());
            appConfig.setProperty("login.userName", userName);
            getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));

            // 判断是否进入过主页面
            boolean isStartMained = CachUtils.getBoolean(LoginActivity.this,
                    LoginActivity.START_MAIN);
            if (isStartMained) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                // 进入主页面
               /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
            } else {
                // 如果没有进入，就进入引导页面
                Intent intent = new Intent(LoginActivity.this,
                        GuideActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }


        }

        @Override
        public void onFailure(String status, String message) {
            hideWaitingUI();
            //AppContext.showToast(status+message);
            if(status.equals("000001")) {
                showWaitingUI();
                logins(platDB.getUserName(), platDB.getUserGender(), platDB.getUserId(), res, threePartNum);
            }
        }
    }
    /**
     * 找回密码
     */
    private void findPassw() {
        UIHelper.showFindPasswordPage(this);
    }

    /**
     * 跳到注册页面
     */
    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void toMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 登录
     */
    private void login(final String userName, final String passw) {
        showWaitingUI();
        Api.login(userName, passw, new ResponseCallback<String>() {

            @Override
            public void onSuccess(String data) {
                hideWaitingUI();
                accessTicket = data;

                AppContext.getAppContext().updateAccessTicket(accessTicket);
                AppConfig appConfig = AppConfig.getAppConfig(getApplicationContext());
                appConfig.setProperty("login.userName", userName);
                appConfig.setProperty("login.password", passw);
                getApplicationContext().sendBroadcast(new Intent(UserInfoAlteredReceiver.ACTION_USER_INFO_ALTERED));

                // 判断是否进入过主页面
                boolean isStartMained = CachUtils.getBoolean(LoginActivity.this,
                        START_MAIN);
                if (isStartMained) {
                    // 进入主页面
                    //toMainPage();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    // 如果没有进入，就进入引导页面
                    Intent intent = new Intent(LoginActivity.this,
                            GuideActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            }

            @Override
            public void onFailure(String status, String message) {
                hideWaitingUI();

                AppContext.showToast(LoginActivity.this, message);
            }
        });
    }


    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch(msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
            }
            break;
          /*  case MSG_LOGIN: {

                String text = getString(R.string.logining, msg.obj);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                System.out.println("---------------哈"+msg.obj);

//				Builder builder = new Builder(this);
//				builder.setTitle(R.string.if_register_needed);
//				builder.setMessage(R.string.after_auth);
//				builder.setPositiveButton(R.string.ok, null);
//				builder.create().show();
            }
            break;*/
            case MSG_AUTH_CANCEL: {
                // Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();

            }
            break;
            case MSG_AUTH_ERROR: {
                //Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();

            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                loginThreePart(platDB.getUserId());

                // 判断是否进入过主页面

               /* boolean isStartMained = CachUtils.getBoolean(LoginActivity.this,
                        START_MAIN);
                if (isStartMained) {
                    // 进入主页面
                    toMainPage();
                } else {
                    // 如果没有进入，就进入引导页面
                    Intent intent = new Intent(LoginActivity.this,
                            GuideActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }*/
            }
            break;
        }
        return false;
    }
}
