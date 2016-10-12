package chat.tox.antox.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

import chat.tox.antox.R;
import chat.tox.antox.theme.ThemeManager;
import wangwang.adapter.FragmentAdapter;
import wangwang.entity.SlidingMenuView;


/*
* @主界面
* */
public class Main3Activity extends AppCompatActivity {

    private SlidingMenuView slideMenu1;

    SharedPreferences preferences;
    private Locale locale=null;

    private ViewPager viewPager;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_better;
    private RadioButton rb_setting;
    private RadioButton rb_friends;
    private FragmentAdapter mAdapter;


    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        slideMenu1 = (SlidingMenuView) findViewById(R.id.slideMenu1);


        preferences= PreferenceManager.getDefaultSharedPreferences(this);//设置
        selectLanguage();
        ThemeManager.init(getApplicationContext());//actionBar初始化颜色

        ThemeManager.applyTheme(this, getSupportActionBar());//修改actionBar背景

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        bindViews(); //绑定底部五个按钮按钮
        rb_channel.setChecked(true);

        //提示当前菜单栏状态（open or close）
        slideMenu1.setOnStatusListener(new SlidingMenuView.OnStatusListener() {
            @Override
            public void statusChanged(SlidingMenuView.Status status) {
                if (status == SlidingMenuView.Status.Open) {
                    Toast.makeText(Main3Activity.this, "Open", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Main3Activity.this, "Close", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //用户界面中 打开菜单栏对应的按钮
    public void user_setting (View v){
        slideMenu1.toggle();

    }

    //侧划界面中 设置按钮对应的事件
    public void sliding_setting (View v){
        Intent intent = new Intent(this, SettingsActivity1.class);
        startActivity(intent);
    }


    //修改语言
    private void selectLanguage(){
        String localeString=preferences.getString("locale", "-1");
        locale=getResources().getConfiguration().locale;
        if(localeString.equals("-1")){
            SharedPreferences.Editor editor=preferences.edit();
            String currentLanguage=locale.getLanguage().toLowerCase();
            String currentCountry=locale.getCountry();
            editor.putString("locale",currentLanguage+"_"+currentCountry);
            editor.apply();
        }else {
            if(localeString.contains("_")){
                String language=localeString.substring(0,localeString.indexOf("_"));
                String country=localeString.substring(localeString.indexOf("_")+1,localeString.length());
                locale=new Locale(language,country);
            }else {
                locale=new Locale(localeString);
            }
            Locale.setDefault(locale);
            Configuration config=new Configuration();
            config.locale=locale;
            getApplicationContext().getResources().updateConfiguration(config,getApplicationContext().getApplicationContext().getResources().getDisplayMetrics());
        }
    }


    //添加好友
    public void onClickAdd(View v) {
        Intent intent = new Intent(Main3Activity.this, AddActivity.class);
        startActivity(intent);
    }

    //底部五个按钮 与 对应的Viewpage子项 绑定
    private void bindViews() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_channel = (RadioButton) findViewById(R.id.rb_channel);
        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rb_friends = (RadioButton) findViewById(R.id.rb_friends);
        rb_better = (RadioButton) findViewById(R.id.rb_better);
        rb_setting = (RadioButton) findViewById(R.id.rb_setting);
        rg_tab_bar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_channel:
                        Log.d("ViewPage第1子项","第一个Fragment") ;
                        viewPager.setCurrentItem(PAGE_ONE);
                        break;
                    case R.id.rb_message:
                        viewPager.setCurrentItem(PAGE_TWO);
                        Log.d("ViewPage第2子项","第二个Fragment") ;
                        break;
                    case R.id.rb_friends:
                        viewPager.setCurrentItem(PAGE_THREE);
                        Log.d("ViewPage第3子项","第三个Fragment") ;
                        break;
                    case R.id.rb_better:
                        viewPager.setCurrentItem(PAGE_FOUR);
                        Log.d("ViewPage第4子项","第四个Fragment") ;
                        break;
                    case R.id.rb_setting:
                        viewPager.setCurrentItem(PAGE_FIVE);
                        Log.d("ViewPage第5子项","第五个Fragment") ;
                        break;

                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.view_first_layout);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            //对应的Viewpage子项 与 底部五个按钮 绑定
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (viewPager.getCurrentItem()) {
                        case PAGE_ONE:
                            rb_channel.setChecked(true);
                            break;
                        case PAGE_TWO:
                            rb_message.setChecked(true);
                            break;
                        case PAGE_THREE:
                            rb_friends.setChecked(true);
                            break;
                        case PAGE_FOUR:
                            rb_better.setChecked(true);
                            break;
                        case PAGE_FIVE:
                            rb_setting.setChecked(true);
                            break;
                    }
                }
            }
        });
    }

    //监听物理退出键并弹出AlertDialog提示框
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //return super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //创建退出提示框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            //设置图片
            isExit.setIcon(R.drawable.logo);
            //设置对话框标题
            isExit.setTitle("系统提示");
            //设置对话框提示
            isExit.setMessage("确定退出？");
            //设置两个监听按钮
            isExit.setButton(AlertDialog.BUTTON_POSITIVE, "确定", listener);
            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", listener);
            isExit.show();
        }
        return false;
    }

    //监听对话框里的Button按钮
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_first, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
