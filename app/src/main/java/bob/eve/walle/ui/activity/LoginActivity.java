package bob.eve.walle.ui.activity;

import androidx.annotation.Nullable;


import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.image.ImageLoader;
import com.hjq.umeng.Platform;
import com.hjq.umeng.UmengClient;
import com.hjq.umeng.UmengLogin;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import bob.eve.walle.R;
import bob.eve.walle.common.MyActivity;
import bob.eve.walle.helper.InputTextHelper;
import bob.eve.walle.pojo.User;
import bob.eve.walle.util.MyUser;
import butterknife.BindView;
import butterknife.OnClick;


public final class LoginActivity extends MyActivity
        implements UmengLogin.OnLoginListener {

   @BindView(R.id.iv_login_logo)
    ImageView mLogoView;
   @BindView(R.id.et_login_phone)
    EditText mPhoneView;
   @BindView(R.id.et_login_password)
    EditText mPasswordView;

   @BindView(R.id.btn_login_commit)
    Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_login_title;
    }

    @Override
    protected void initView() {
        new InputTextHelper.Builder(this)
                .setMain(mCommitView)
                .addView(mPhoneView)
                .addView(mPasswordView)
                .build();

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivity(RegisterActivity.class);

        startActivityForResult(new Intent(this, RegisterActivity.class), new ActivityCallback() {

            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                toast(String.valueOf(resultCode));
            }
        });
    }

    @Override
    public boolean isSupportSwipeBack() {
        // 不使用侧滑功能
        return false;
    }

    @OnClick({R.id.tv_login_forget, R.id.btn_login_commit, R.id.iv_login_qq, R.id.iv_login_wx, R.id.iv_login_wb,R.id.tb_login_title})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_login_title:
                    startActivity(RegisterActivity.class);
            break;
            case R.id.tv_login_forget:
                startActivity(PasswordForgetActivity.class);

                break;
            case R.id.btn_login_commit:
                if (mPhoneView.getText().toString().length() != 11) {
                    toast(getString(R.string.common_phone_input_error));
                } else {
                    // 处理登录
                    String phoneNo= mPhoneView.getText().toString();
                    String password=mPasswordView.getText().toString();
                    User u=MyUser.getMe();
                    u.setUspassword(phoneNo);
                    u.setUstype("phone");
                    u.setUpassword(password);
                    sendRequestWithkHttpURLConnection2(u);
                    //startActivityFinish(MainActivity.class);
                }
                break;
            case R.id.iv_login_qq:
            case R.id.iv_login_wx:
            case R.id.iv_login_wb:
                Platform platform;
                switch (v.getId()) {
                    case R.id.iv_login_qq:
                        platform = Platform.QQ;
                        break;
                    case R.id.iv_login_wx:
                        platform = Platform.WEIXIN;
                        break;
                    case R.id.iv_login_wb:
                        platform = Platform.SINA;
                        break;
                    default:
                        throw new IllegalStateException("are you ok?");
                }
                UmengClient.login(this, platform, this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 回调友盟第三方登录 SDK 的接口
        UmengClient.onActivityResult(this, requestCode, resultCode, data);
    }

    /**
     * 友盟第三方登录授权回调接口
     * {@link UmengLogin.OnLoginListener}
     */

    /**
     * 授权成功的回调
     *
     * @param platform      平台名称
     * @param data          用户资料返回
     */
    @Override
    public void onSucceed(Platform platform, UmengLogin.LoginData data) {
        // 判断第三方登录的平台
        String type="QQ";
        String zhanghao="";
        User user=MyUser.getMe();
        switch (platform) {
            case QQ: // QQ
                type="QQ";
                break;
            case WEIXIN: // 微信
                type="WEIXIN";
                break;
            case SINA: // 新浪
                type="SINA";
                break;
            default:
                break;
        }

        ImageLoader.loadCircleImage(mLogoView, data.getIcon());

        toast("昵称：" + data.getName() + "\n" + "性别：" + data.getSex());
        toast("id：" + data.getId());
        toast("token：" + data.getToken());
        user.setUid(1);
        user.setUstype(type);
       // user.setUusername("萌新一枚");
        user.setUnicheng(data.getName());
        user.setUsname(data.getToken());
        sendRequestWithkHttpURLConnection3(user);


    }

    /**
     * 授权失败的回调
     *
     * @param platform      平台名称
     * @param t             错误原因
     */
    @Override
    public void onError(Platform platform, Throwable t) {
        toast("第三方登录出错：" + t.getMessage());
    }

    /**
     * 授权取消的回调
     *
     * @param platform      平台名称
     */
    @Override
    public void onCancel(Platform platform) {
        toast("取消第三方登录");
    }  private void sendRequestWithkHttpURLConnection2(final User u){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/UserServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectuser?mode=c&"+u.toString();
                    URL url=new URL(s2);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(8000);
                    InputStream is=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d("info1111",new String(response));
                    showResponse(response.toString());

                } catch (MalformedURLException e) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"未联网或网络环境不好",Toast.LENGTH_LONG).show();
                        }
                    });

                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try {
                            reader.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gs=new Gson();
                //解析泛型
                Type type=new TypeToken<LinkedList<User>>(){}.getType();
                List<User> list=new Gson().fromJson(response,type);
                for(int i=0;i<list.size();i++){
                    User u=list.get(i);
                    MyUser.setMe(u);
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                    });

                }
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(list.size()==0)
                        {
                            Toast.makeText(LoginActivity.this,"用户名或密码不对",Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });

    }
    private void sendRequestWithkHttpURLConnection3(final User u){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/UserServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectuser?mode=d&"+u.toString();
                    URL url=new URL(s2);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(8000);
                    InputStream is=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d("info1111",new String(response));
                    showResponse(response.toString());

                } catch (MalformedURLException e) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"未联网或网络环境不好",Toast.LENGTH_LONG).show();
                        }
                    });

                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try {
                            reader.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
    }

}