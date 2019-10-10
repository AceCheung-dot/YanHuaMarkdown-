package bob.eve.walle.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public final class PasswordResetActivity extends MyActivity {

    @BindView(R.id.et_password_reset_password1)
    EditText mPasswordView1;
    @BindView(R.id.et_password_reset_password2)
    EditText mPasswordView2;
    @BindView(R.id.btn_password_reset_commit)
    Button mCommitView;
    String phoneNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password_reset;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_password_reset_title;
    }

    @Override
    protected void initView() {
        Intent intent=getIntent();
        this.phoneNo=intent.getStringExtra("phoneNo");

        new InputTextHelper.Builder(this)
                .setMain(mCommitView)
                .addView(mPasswordView1)
                .addView(mPasswordView2)
                .build();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_password_reset_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_password_reset_commit:
                if (!mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString())) {
                    toast("两次密码不一致或者未输入手机号");
                } else {
                    // 重置密码
                    User u=MyUser.getMe();
                    u.setUpassword(mPasswordView1.getText().toString());
                    u.setUspassword(phoneNo);
                }
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
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/UserServlet?mode=e&"+u.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectuser?mode=a&uid=";
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
                    //showResponse(response.toString());
                    //sendRequestWithkHttpURLConnection3(MyUser.getMe().getUid());
                    PasswordResetActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(PasswordResetActivity.this,LoginActivity.class);
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
