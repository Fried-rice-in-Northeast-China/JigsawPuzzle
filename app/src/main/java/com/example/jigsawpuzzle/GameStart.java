package com.example.jigsawpuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;

public class GameStart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //系统默认状态
        requestWindowFeature(1);
        //隐藏顶部导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new MainView(this){
            /* 实现退出方法 */
            @Override
            protected void quit(){
                finish();
            }
        });
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
