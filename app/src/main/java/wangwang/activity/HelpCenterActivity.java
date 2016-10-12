package wangwang.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import chat.tox.antox.R;


/**
 * Created by 24582 on 2016/9/26.
 */
public class HelpCenterActivity extends AppCompatActivity {
    private ListView listView;
    private String[] arr= {"如何从旺旺应用程序发送消息？", "报告问题", "如何添加照片？", "接收不到消息？"
            , "无法接受和发送图片？", "如何知道附近朋友在使用旺旺？", "如何取消提醒？", "如何退出旺旺账号？"
            , "如何清除缓存？", "如何更改个人信息？"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_help);
        listView = (ListView) findViewById(R.id.setting_help_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_setting_help_item, arr);
        listView.setAdapter(arrayAdapter);
    }
}
