package com.example.jigsawpuzzle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/* 主视图 */
public class MainView extends View {

    private String pictureName;
    public static MediaPlayer player = null;
    private Context context;
    private Bitmap back;
    private Bitmap[] bitmapTiles;
    private Paint paint;
    private int tileWidth;
    private int tileHeight;
    private int[][] dataTiles;
    private Board tilesBoard;
    private boolean isSuccess;
    private int COL = 3;
    private int ROW = 3;

    private int[][] dir = {
            {-1,0},//左
            {0,-1},//上
            {1,0},//右
            {0,1}//下
    };

    public MainView(Context context) {
        super(context);
        this.context = context;
        //抗锯齿
        paint = new Paint();
        paint.setAntiAlias(true);

        pictureName = MainActivity.pictureName;
        COL = MainActivity.level;
        ROW = MainActivity.level;

        //开始游戏
        letsGo();
    }

    /* 开始游戏的封装 */
    private void letsGo() {
        //初始化数据
        init(pictureName);
        //开始游戏
        startGame();
    }

    /* 初始化 */
    private void init(String pictureName) {
        //载入图像，并将图片切成块
        AssetManager assetManager = context.getAssets();
        try {
            String backMame = pictureName + ".jpg";
            InputStream assetInputStream = assetManager.open(backMame);
            //将输入流解码为位图
            Bitmap bitmap = BitmapFactory.decodeStream(assetInputStream);
            //创建位图并缩放
            back = Bitmap.createScaledBitmap(bitmap, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(),true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        tileWidth = back.getWidth() / COL;
        tileHeight = back.getHeight() / ROW;
        bitmapTiles = new Bitmap[COL * ROW];
        int idx = 0;

        //给老子切
        for(int i = 0; i < ROW; i++)
            for(int j = 0; j < COL; j++)
                bitmapTiles[idx++] = Bitmap.createBitmap(back, j * tileWidth, i * tileHeight, tileWidth, tileHeight);
    }

    /* 开始游戏 */
    private void startGame() {
        tilesBoard = new Board();
        dataTiles = tilesBoard.createRandomBoard(ROW, COL);
        isSuccess = false;
        // 刷新界面
        invalidate();
        if(COL == 5 && ROW == 5)
            musicPlay("levelMax");
    }

    /* 开始绘制 */
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        //在对顶编号的坐标绘制对应编号的图片
        for(int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int idx = dataTiles[i][j];
                if(idx == ROW * COL - 1 && !isSuccess)
                    continue;
                canvas.drawBitmap(bitmapTiles[idx],j * tileWidth,i * tileHeight, paint);
            }
        }
    }

    /* 将屏幕上的点转换成对应拼图块的坐标 */
    private Point posToIndex(int x, int y) {
        return new Point(x / tileWidth, y / tileHeight);
    }

    /* 手指按下事件检测 */
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Point point = posToIndex((int) event.getX(), (int) event.getY());

            for(int i = 0; i < dir.length; i++) {
                //遍历四个运动方向
                int newX = point.getX() + dir[i][0];
                int newY = point.getY() + dir[i][1];

                if(newX >= 0 && newX < COL && newY >= 0 && newY < ROW) {
                    //寻找到空白快
                    if(dataTiles[newY][newX] == COL * ROW - 1) {
                        //交换图片
                        int temp = dataTiles[point.getY()][point.getX()];
                        dataTiles[point.getY()][point.getX()] = dataTiles[newY][newX];
                        dataTiles[newY][newX] = temp;
                        //刷新
                        invalidate();
                        if(tilesBoard.isSuccess(dataTiles)) {
                            isSuccess = true;
                            invalidate();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("拼图成功")
                                    .setCancelable(false)
                                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            letsGo();
                                        }
                                    });
                            if (COL < 5 && ROW < 5) {
                                builder.setMessage("恭喜你拼图成功")
                                        .setNegativeButton("升级继续", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                musicPlay("levelup");
                                                COL++;
                                                ROW++;
                                                letsGo();
                                            }
                                        });
                            }
                            else {
                                builder.setMessage("恭喜通关了")
                                        .setNegativeButton("回到第一关", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                COL = 3;
                                                ROW = 3;
                                                letsGo();
                                            }
                                        });
                            }
                            builder.create().show();
                        }
                    }
                }
            }
        }
        return true;
    }

    /* 播放音乐嗷 */
    private void musicPlay(String music){
        initMediaPlayer(music);
        player.start();
    }

    /* 初始化MediaPlayer */
    private void initMediaPlayer(String music) {
        AssetManager assetManager;
        assetManager = getResources().getAssets();
        player = new MediaPlayer();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(music + ".mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
