package bob.eve.walle.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import bob.eve.walle.R;
import bob.eve.walle.mvp.copy.CopyContract;
import bob.eve.walle.pojo.BordingContent;
import bob.eve.walle.pojo.DingWei;
import bob.eve.walle.util.MyUser;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_tittle;
    EditText et_content;
    Button bt_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        et_tittle=(EditText) findViewById(R.id.et_adt_title);
        et_content=(EditText)findViewById(R.id.et_adt_content);
        bt_save=(Button)findViewById(R.id.btn_adt_save);
        bt_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_adt_save)
        {
            String titile=this.et_tittle.getText().toString();
            String content=this.et_content.getText().toString();
            int user= MyUser.getMe().getUid();
            BordingContent bc=new BordingContent();
            bc.setBcontent(user+"_"+titile+"_"+user+"_"+content);
            bc.setBctime(new Timestamp(System.currentTimeMillis()));
            bc.setBcid(user);
            bc.setBusing(1);
            bc.setBctouid(0);
            bc.setBcfromuid(MyUser.getMe().getUid());
            sendRequestWithkHttpURLForDingWei(bc);


        }
    }
    private void sendRequestWithkHttpURLForDingWei(final BordingContent bc){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/BordingContentServlet?mode=a&"+bc.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectuser";
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
                    AddTaskActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(AddTaskActivity.this,MainActivity.class);
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
