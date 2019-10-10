package bob.eve.walle.fitpopwindow;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bob.eve.walle.R;
import bob.eve.walle.pojo.Xingji;
import bob.eve.walle.ui.activity.CommentMainActivity;
import bob.eve.walle.ui.activity.UpdateActivity;
import bob.eve.walle.util.MyUser;
import bob.eve.walle.widget.StarBarView;


/**
 * Created by DongJr on 2017/2/27.
 */

public class FitPopupUtil implements View.OnClickListener {

    private View contentView;

    private Activity context;

 //   private TextView reason1;
   // private TextView reason2;
   // private TextView reason3;
    private StarBarView starBarView;

    int mid;
//
  private TextView btnCommit;
//
   // private boolean reason1Selected;
 //   private boolean reason2Selected;
  //  private boolean reason3Selected;

    private FitPopupWindow mPopupWindow;

    private OnCommitClickListener listener;
    int marker;

    public FitPopupUtil(Activity context) {

        this.context = context;


        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.layout_popupwindow,null);
 //      reason1 = (TextView) contentView.findViewById(R.id.tv_reason1);
     //   reason2 = (TextView) contentView.findViewById(R.id.tv_reason2);
   //   reason3 = (TextView) contentView.findViewById(R.id.tv_reason3);
      btnCommit = (TextView) contentView.findViewById(R.id.btn_commit);
        starBarView=(StarBarView)contentView.findViewById(R.id.sbv_starbar_pinglin);
        starBarView.setOnClickListener(this);

  //      reason1.setOnClickListener(this);
     //   reason2.setOnClickListener(this);
      //  reason3.setOnClickListener(this);


    }
    public FitPopupUtil(Activity context,int mid) {

        this.context = context;


        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.layout_popupwindow,null);
      //  reason1 = (TextView) contentView.findViewById(R.id.tv_reason1);
      //  reason2 = (TextView) contentView.findViewById(R.id.tv_reason2);
    //    reason3 = (TextView) contentView.findViewById(R.id.tv_reason3);
       btnCommit = (TextView) contentView.findViewById(R.id.btn_commit);
        starBarView=(StarBarView)contentView.findViewById(R.id.sbv_starbar_pinglin);
        starBarView.setOnClickListener(this);
        this.mid=mid;

    //    reason1.setOnClickListener(this);
    //    reason2.setOnClickListener(this);
     //   reason3.setOnClickListener(this);


    }

    public void setOnClickListener(OnCommitClickListener listener) {
        this.listener = listener;
    }

    /**
     * 弹出自适应位置的popupwindow
     *
     * @param anchorView 目标view
     */
    public View showPopup(View anchorView) {
        if (mPopupWindow == null) {
            mPopupWindow = new FitPopupWindow(context,
                    ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(20),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        mPopupWindow.setView(contentView, anchorView);
        mPopupWindow.show();
        return contentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_reason1:
//                reason1Selected = !reason1Selected;
//                reason1.setSelected(reason1Selected);
//                break;
//            case R.id.tv_reason2:
//                reason2Selected = !reason2Selected;
//                reason2.setSelected(reason2Selected);
//                break;
//            case R.id.tv_reason3:
//                reason3Selected = !reason3Selected;
//                reason3.setSelected(reason3Selected);
//                break;
            case R.id.sbv_starbar_pinglin:
               float pingfen=starBarView.getStarRating();
                Xingji xingji=new Xingji();
                xingji.setXuser(MyUser.getMe().getUid());
                xingji.setXfenzhi(new Float(pingfen).intValue());
                xingji.setXmarker(mid);
                sendRequestWithkHttpURLConnectionwithXingJi(xingji);

//                break;
//            case R.id.btn_commit:
//                if (listener != null) {
//                   listener.onClick(getReason());
//                }
//                mPopupWindow.dismiss();
                break;
        }




 btnCommit.setOnClickListener(null);
            btnCommit.setText("确定");
    }



    public String getReason() {
//        String content1 = reason1Selected ? reason1.getText().toString() + "," : "";
//        String content2 = reason2Selected ? reason2.getText().toString() + "," : "";
//       String content3 = reason3Selected ? reason3.getText().toString() + "," : "";
//
//        String s = content1 + content2 + content3;
//        return s.substring(0, s.length() - 1);
        return "";

    }

    public interface OnCommitClickListener {
        void onClick(String reason);
    }
    private void sendRequestWithkHttpURLConnectionwithXingJi(final Xingji x){//修改用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever +":8080/AndroidSever/FenZhiServlet?mode=a&"+x.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectxingji";
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
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"谢谢您的评分Toast.",Toast.LENGTH_LONG).show();
                    }
                });




            }

        }).start();
    }

}
