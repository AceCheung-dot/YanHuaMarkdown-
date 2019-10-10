package bob.eve.walle.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.widget.CountdownView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bob.eve.walle.R;
import bob.eve.walle.common.MyActivity;
import bob.eve.walle.helper.InputTextHelper;
import bob.eve.walle.pojo.User;
import bob.eve.walle.util.MyUser;
import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends MyActivity {


    @BindView(R.id.et_register_phone)
    EditText mPhoneView;
   // @BindView(R.id.cv_register_countdown)
    //CountdownView mCountdownView;

    @BindView(R.id.et_register_code)
    EditText mCodeView;

    @BindView(R.id.et_register_password1)
    EditText mPasswordView1;
    @BindView(R.id.et_register_password2)
    EditText mPasswordView2;

    @BindView(R.id.btn_register_commit)
    Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_register_title;
    }

    @Override
    protected void initView() {
        new InputTextHelper.Builder(this)
                .setMain(mCommitView)
                .addView(mPhoneView)
                .addView(mCodeView)
                .addView(mPasswordView1)
                .addView(mPasswordView2)
                .build();
    }

    @Override
    protected void initData() {
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finishResult(RESULT_OK);
//            }
//        }, 2000);
    }

    @Override
    protected ImmersionBar statusBarConfig() {
        // 不要把整个布局顶上去
        return super.statusBarConfig().keyboardEnable(true);
    }

    @OnClick({ R.id.btn_register_commit})
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.cv_register_countdown: //获取验证码
//

//
//                break;
            case R.id.btn_register_commit: //提交注册

                if (mPhoneView.getText().toString().length() != 11) {
                    toast(getString(R.string.common_phone_input_error));
                } else if (!mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString())) {
                    toast(getString(R.string.register_password_input_error));
                } else {
                    String username=mPhoneView.getText().toString();
                    String password=mPasswordView1.getText().toString();
                    User user=MyUser.getMe();
                    user.setUstype("phone");
                    user.setUpassword(password);
                    user.setUspassword(username);
                    user.setUnicheng(mCodeView.getText().toString());
                    user.setUjingyan(400);
                    sendRequestWithkHttpURLConnection2(user);

                }
                break;
            default:
                break;
        }
    }
    private void sendRequestWithkHttpURLConnection2(User u){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/UserServlet?mode=a&"+u.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectuser?mode=a&uid=";
                    URL url=new URL(s);
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
                    //showResponse(response.toString());
                    //sendRequestWithkHttpURLConnection3(MyUser.getMe().getUid());
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(i);
                        }
                    });
                } catch (MalformedURLException e) {
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
