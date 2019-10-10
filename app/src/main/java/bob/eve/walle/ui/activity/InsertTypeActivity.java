package bob.eve.walle.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import bob.eve.walle.Adapter.MarkdownItemAdapter;
import bob.eve.walle.R;
import bob.eve.walle.pojo.Item;
import bob.eve.walle.pojo.State;
import bob.eve.walle.pojo.Ziduan;

public class InsertTypeActivity extends AppCompatActivity implements View.OnClickListener {

    public LinkedList<Item> items=new LinkedList<Item>();
    public LinkedList<Item> items2=new LinkedList<Item>();
    MarkdownItemAdapter adapter=null;

    int id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_type);
        items.add(new Item("id","1",0));
        items.add(new Item("您的用户id","1",1));
        items.add(new Item("名称","1111",2));

        adapter=new MarkdownItemAdapter(InsertTypeActivity.this,R.layout.insert_update_markdown_layout,items);
        adapter.setItems(items);
        ListView lv=(ListView)findViewById(R.id.lv_info);
        lv.setAdapter(adapter);
        Button insert=(Button)findViewById(R.id.add_item);
        insert.setOnClickListener(this);
        Button update=(Button)findViewById(R.id.send_info);
        update.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_item){
            Log.d("info3333","3333");
            String str="字符型";
            RadioButton rb_varchar=(RadioButton)findViewById(R.id.rb_type_varchar);

            RadioButton rb_int=(RadioButton)findViewById(R.id.rb_type_int);
            if(rb_int.isChecked())
            {
                str="整数型";
            }
            RadioButton rb_date=(RadioButton)findViewById(R.id.rb_type_date);
            if(rb_int.isChecked())
            {
                str="日期型";
            }
            RadioButton rb_double=(RadioButton)findViewById(R.id.rb_type_double);
            if(rb_int.isChecked())
            {
                str="浮点型";
            }
            EditText et=(EditText)findViewById(R.id.et_arg);
            Item item=new Item(et.getText().toString(),str,items.size());
            //items=adapter.getItems();
            items.add(item);

            adapter=new MarkdownItemAdapter(InsertTypeActivity.this, R.layout.insert_update_markdown_layout,items);
            adapter.setItems(items);
            ListView lv=(ListView)findViewById(R.id.lv_info);
            lv.setAdapter(adapter);
        }
        else if(v.getId()==R.id.send_info){
            id=1;


            State sta=new State();
            sta.setSid(new Integer(adapter.getItem(0).getValue()));
            sta.setSuser(new Integer(adapter.getItem(1).getValue()));
            sta.setStitle(adapter.getItem(2).getValue());
            System.out.println(sta.toString());
            sendRequestWithkHttpURLConnection(sta);


        }

    }

    private void sendRequestWithkHttpURLConnection(final State state){//新增用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://172.20.10.3:8080/AndroidSever/StateServlet?mode=a&"+state.toString();
                    String s2="http://172.20.10.3:8080/AndroidSever/selectstate";
                    URL url=new URL(s);//构造网页地址
                    connection=(HttpURLConnection)url.openConnection();//连接
                    connection.setRequestMethod("GET");//连接是的方法

                    connection.setConnectTimeout(8000);//设置超时
                    InputStream is=connection.getInputStream();//读取网页内容开始
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    //读取网页内容结束
                    Log.d("info1111",new String(response));

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
                sendRequestWithkHttpURLConnection();
            }
        }).start();//令线程在jvm的就绪队列中等候执行。
    }
    private void sendRequestWithkHttpURLConnection(){//全部查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://172.20.10.3:8080/AndroidSever/MarkdownServlet";
                    String s2="http://172.02.10.3:8080/AndroidSever/selectstate";
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
            public void run() {//注：这个县城用于修改TextView的信息、跳转等凡是跟UI油管的操作
                State s=null;
                Gson gs=new Gson();
                //解析泛型
                //这里的类型（凡是有Markdown的地方）也可以换成自己的。
                Type type=new TypeToken<LinkedList<State>>(){}.getType();
                List<State> list=new Gson().fromJson(response,type);
                for(int i=0;i<list.size();i++){
                    s=list.get(i);

                    System.out.println(s.toString());
                }
                for(int i=3;i<items.size();i++){
                    Ziduan zi=new Ziduan();
                    zi.setZid(1);
                    zi.setZname(adapter.getItem(i).getKey());
                    zi.setZstate(s.getSid());
                    zi.setZt1(""+getType(adapter.getItem(i).getValue()));
                    sendRequestWithkHttpURLConnection2(zi);

                }

            }
        });


    }
    int getType(String s){
        int index=0;
        if(s.compareTo("整数型")==0)
        {
            index=0;
        }
        if(s.compareTo("字符型")==0)
        {
            index=1;
        }
        if(s.compareTo("日期型")==0)
        {
            index=2;
        }
        if(s.compareTo("浮点型")==0)
        {
            index=3;
        }
        return index;
    }
    private void sendRequestWithkHttpURLConnection2(final Ziduan ziduan){//新增用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://192.168.0.103:8080/AndroidSever/ZiduanServlet?mode=a&"+ziduan.toString();
                    String s2="http://192.168.0.103:8080/AndroidSever/selectziduan";
                    URL url=new URL(s);//构造网页地址
                    connection=(HttpURLConnection)url.openConnection();//连接
                    connection.setRequestMethod("GET");//连接是的方法

                    connection.setConnectTimeout(8000);//设置超时
                    InputStream is=connection.getInputStream();//读取网页内容开始
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    //读取网页内容结束
                    Log.d("info1111",new String(response));

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
        }).start();//令线程在jvm的就绪队列中等候执行。
    }

}

