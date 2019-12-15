package com.example.jigsawpuzzle;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.jigsawpuzzle.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static int screenWidth;     //屏幕像素宽度
    private static int screenHeight;    //屏幕像素高度

    public static String pictureName;
    public static int level = 3;

    public static int PICTURECS_COUNT = 22;


//    面包车前
//              胳膊手
//          头发眼鼻嘴
//              眼 头发脸眼脸 脖子
//              镜 头发头发衣服
//                     衣服 衣服
//                       衣服 衣服
//                         衣服 衣服
//                           衣服 衣服手插兜
//                            大腿    大腿
//                          大腿     大腿
//                        关节      关节
//                     小腿        小腿
//                   鞋子       鞋子
//            地面地面地面地面地面地面地面地面地面
//               今晚去吃烤肉吧！
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //获取屏幕像素
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        //难度选择组
        final RadioButton easy = findViewById(R.id.easy);
        final RadioButton common = findViewById(R.id.common);
        final RadioButton hard = findViewById(R.id.hard);

        Button startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (easy.isChecked()){
                    level = 3;
                }
                if (common.isChecked()){
                    level = 4;
                }
                if (hard.isChecked()){
                    level = 5;
                }
                pictureName = "back" + tabs.getSelectedTabPosition();

                startActivity(new Intent(MainActivity.this, GameStart.class));
            }
        });
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

}