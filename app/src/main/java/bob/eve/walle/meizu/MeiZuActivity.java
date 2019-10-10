package bob.eve.walle.meizu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import bob.eve.walle.group.GroupRecyclerView;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.zjun.widget.TimeRuleView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import bob.eve.walle.Adapter.MarkdownItemAdapter;
import bob.eve.walle.Adapter.TimeLineAdapter;
import bob.eve.walle.R;

import bob.eve.walle.bean.DateComparator;
import bob.eve.walle.bean.DateText;

import bob.eve.walle.pojo.Markdown;

import bob.eve.walle.util.MyUser;


public class MeiZuActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener, View.OnScrollChangeListener {
    private RecyclerView recyclerView;
    private List<DateText> mDatas;
    private TimeLineAdapter mTimeLineAdapter;

    TextView mTextMonthDay;

    TextView mTextYear;
    int time=0;

    TextView mTextLunar;

    TextView mTextCurrentDay;
    TextView tvTime;
    private TimeRuleView trvTime;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    int temp_year=2019;
    int temp_month=8;
    int temp_day=11;
   // GroupRecyclerView mRecyclerView;


    public static void show(Context context) {
        context.startActivity(new Intent(context, MeiZuActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mei_zu;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);

        mTextCurrentDay = findViewById(R.id.tv_current_day);
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);

        mTextMonthDay.setOnClickListener(v ->
        {
            if (!mCalendarLayout.isExpand()) {
                mCalendarLayout.expand();
                return;
            }
            mCalendarView.showYearSelectLayout(mYear);
            mTextLunar.setVisibility(View.GONE);
            mTextYear.setVisibility(View.GONE);
            mTextMonthDay.setText(String.valueOf(mYear));

        });
        findViewById(R.id.fl_current).setOnClickListener(v->{
          //  mCalendarLayout.setModeOnlyMonthView();
            mCalendarView.scrollToCurrent();
        });

        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        initData();
        sendRequestWithkHttpURLConnection2(0);
        initTimeLine();

    }
    void initTimeLine(){
       // tvTime = findViewById(R.id.tv_time);
        trvTime = findViewById(R.id.trv_time);

        trvTime.setOnTimeChangedListener(new TimeRuleView.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(int newTimeValue) {
                try {
                    Date date=new SimpleDateFormat("hh:mm:ss").parse(TimeRuleView.formatTimeHHmmss(newTimeValue));
                    time=newTimeValue;
                    getPosition(temp_year,temp_month,temp_day,newTimeValue/3600,((newTimeValue%3600)/60)/30,1);
                    System.out.println(newTimeValue/3600);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);


//        mRecyclerView = (GroupRecyclerView) findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
//        mRecyclerView.setAdapter(new ArticleAdapter(this));
//        mRecyclerView.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        if(mCalendarLayout.isExpand()){
            mCalendarLayout.shrink();
        }
        trvTime.setCurrentTime(0);
        temp_day= calendar.getDay();
        temp_month= calendar.getMonth();
        temp_year= calendar.getYear();
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        getPosition(calendar.getYear(), calendar.getMonth(), calendar.getDay(),0,0,0);
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }
void getPosition(int year,int month,int day,int hour,int minute,int flag){
        List<DateText>dt=mDatas;
    ArrayList<DateText>dtl2=new ArrayList<>();
    java.util.Calendar cal= java.util.Calendar.getInstance();
    System.out.println("y"+year+"m"+month+"d"+day+"h"+hour);
    Date date1=null;
    Date date3=null;
    if(flag==0||time==0) {
        try {
            date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(year+"-"+month+"-"+day+" "+0+":"+0+":"+0);
            date3=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(year+"-"+month+"-"+(day+1)+" "+0+":"+0+":"+0);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    else {
        try {
            date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(year+"-"+month+"-"+day+" "+(hour)+":"+(minute)*30+":"+0);
            date3=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(year+"-"+month+"-"+(day)+" "+(hour)+":"+(minute+1)*30+":"+0);

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        cal.set(year, month-1 , day, hour, 0, 0);
//        date1= cal.getTime();
//        cal.set(year,month-1,day,hour+1,0,0);
//        date3=cal.getTime();
    }



        for(int i=0;i<mDatas.size();i++){
            DateText dt1=mDatas.get(i);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            System.out.println(date1.toLocaleString());
            System.out.println(date3.toLocaleString());
            Date date2= null;
            try {
                date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt1.getDate());
                Log.d("date1",new String(""+date1.getTime()));
                Log.d("date2",new String(""+date2.getTime()));
                System.out.println(dt1.getDate());

                if(date2.getTime()<=date3.getTime()&&date2.getTime()>=date1.getTime()){
                 dtl2.add(dt1);
               }

                initData1(dtl2);
            } catch (ParseException e) {
                e.printStackTrace();
            }




        }
}
    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }
    private void initData1(ArrayList<DateText> dt) {



        initView1(dt);

    }
    private void initView1(ArrayList<DateText> dt) {
        recyclerView=(RecyclerView) findViewById(R.id.timeline_recyclerview);
        init(dt);

    }
    private void init(ArrayList<DateText> dt) {
        // 将数据按照时间排序
        DateComparator comparator = new DateComparator();
        Collections.sort(dt, comparator);
        mTimeLineAdapter=new TimeLineAdapter(this, dt);
        GroupRecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(this, GroupRecyclerView.VERTICAL,false);
        recyclerView.setAdapter(mTimeLineAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnScrollChangeListener(this);
    }
    private void sendRequestWithkHttpURLConnection2(final int id){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/MarkdownServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectmarkdown";
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
            public void run() {
                Gson gs=new Gson();
                Markdown m;
                //解析泛型
                Type type=new TypeToken<LinkedList<Markdown>>(){}.getType();
                List<Markdown> list=new Gson().fromJson(response,type);
                ArrayList<DateText>dtl=new ArrayList<DateText>();
                for(int i=0;i<list.size();i++){
                    m=list.get(i);


                    StringTokenizer st=new StringTokenizer(m.getMt1(),"<>");
                    String key ;
                    String value=null;
                    for(;st.hasMoreTokens();)
                    {
                        if(st.countTokens()>=2) {
                            key = st.nextToken();
                            System.out.println(key);
                             value = st.nextToken();

                           // items.add(new Item(key, "" + value, k));
                        }

                    }
                  java.util.Date date=new java.util.Date(m.getMtime().getTime());
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    DateText dt=new DateText(sdf.format(date),m.getMtitle(),value,m.getMplace(),m.getMicon(),m.getMid());
                    dtl.add(dt);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           mDatas=dtl;
                        }
                    });
                }


            }
        });

    }


    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        RecyclerView view=(RecyclerView)v;



    }
}

