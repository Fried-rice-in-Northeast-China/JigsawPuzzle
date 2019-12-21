package com.example.jigsawpuzzle;

import android.app.Activity;
import android.os.Bundle;

import android.view.WindowManager;

public class GameStart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //系统默认状态
        requestWindowFeature(1);
        //隐藏顶部导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new MainView(this));
    }

    /* 释放资源 */
    protected void onStop() {
        super.onStop();
        // 关闭，释放资源
        if(MainView.player != null) {
            MainView.player.release();
        }
    }
}
