package bob.eve.walle.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bob.eve.walle.R;
import bob.eve.walle.pojo.QunLiao;

public class QunLiaoMainActivity extends AppCompatActivity {
    private EditText text;
    private Button bt;
    private RecyclerView recy;
    public List list;
    private lianjie lj;
    public Handler hand = new Handler() {//用于在子线程更新UI，收到信息和连接服务器成功时用到
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "连接服务器成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    recy.setAdapter(new adapter1(list));//设置适配器
                    recy.scrollToPosition(list.size() - 1);//将屏幕移动到RecyclerView的底部
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qun_liao_main);
        this.setTitle("群聊");
        list = new ArrayList<xx>();
        text = findViewById(R.id.text);
        bt = findViewById(R.id.bt);
        recy = findViewById(R.id.recy);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        recy.setLayoutManager(lin);
        lj = new lianjie(this);
        bt.setOnClickListener(new View.OnClickListener() {//给发送按钮设置监听事件
            @Override
            public void onClick(View v) {
                String s = text.getText().toString();
                if (s == null || s.equals("")) {
                    Toast.makeText(getApplicationContext(), "发送消息不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    list.add(new xx(s, R.mipmap.gaara, false));
                    //new一个xx类，第一个参数的信息的内容，第二个参数是头像的图片id，第三个参数表示左右
                    //true为左边，false为右
                    lj.fa(new QunLiao(1,s,9).toString());//发送消息
                    recy.setAdapter(new adapter1(list));//再把list添加到适配器
                    text.setText(null);
                    recy.scrollToPosition(list.size() - 1);//将屏幕移动到RecyclerView的底部
                }
            }
        });
    }
}