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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/* 主视图 */
public class MainView extends View {

    private String pictureName;             //图片名
    public static MediaPlayer player = null;
    private Context context;
    private Bitmap back;                    //位图
    private Bitmap[] bitmapTiles;           //位图数组
    private Paint paint;
    private int tileWidth;                  //图片块宽度
    private int tileHeight;                 //图片块高度
    private int[][] dataTiles;              //图片块序号
    private Board tilesBoard;               //对图片块的运算
    private boolean isSuccess;              //是否完成游戏
    private int COL = 3;
    private int ROW = 3;

    private int yPadding;                   //图片初始纵坐标偏移量

    private int[][] dir = {
            {-1,0},//左
            {0,-1},//上
            {1,0},//右
            {0,1}//下
    };

//          卧槽     ²³³³³³³    6666666    厉害了23333
//      ²³³³³³³³³³³     2333     6666         ²³³   666
//          太流弊了！！卧槽     ²³³³³³³    6666666
//          厉害了23333     ²³³³³³³³³³³     2333     6666
//                  ²³卧槽     ²³³³³³³    6666666    厉害了23333
//      ²³³³³³³³³³³     6666     2333

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
//            back = Bitmap.createScaledBitmap(bitmap, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(),true);

            // 获得图片的宽高
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) MainActivity.getScreenWidth()) / width;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);

            //创建位图并缩放
            back = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
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
        if(MainActivity.getScreenHeight() > back.getHeight())
            yPadding = (MainActivity.getScreenHeight() - back.getHeight()) / 2;
        else
            yPadding = 0;

        //在对顶编号的坐标绘制对应编号的图片
        for(int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int idx = dataTiles[i][j];
                if(idx == ROW * COL - 1 && !isSuccess)
                    continue;
                canvas.drawBitmap(bitmapTiles[idx],j * tileWidth,yPadding + i * tileHeight, paint);
            }
        }
    }

    /* 将屏幕上的点转换成对应拼图块的坐标 */
    private Point posToIndex(int x, int y) {
        return new Point(x / tileWidth, (y - yPadding)/ tileHeight);
    }

    /* 点击坐标在图片上 */
    private boolean inPicture(int y){
        if((y >= yPadding) && (y <= (MainActivity.getScreenHeight() - yPadding))){
            return true;
        }
        else {
            return false;
        }
    }

    /* 手指按下事件检测 */
    public boolean onTouchEvent(MotionEvent event) {
        if(!isSuccess) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (inPicture((int) event.getY())) {
                    Point point = posToIndex((int) event.getX(), (int) event.getY());

                    for (int i = 0; i < dir.length; i++) {
                        //遍历四个运动方向
                        int newX = point.getX() + dir[i][0];
                        int newY = point.getY() + dir[i][1];

                        if (newX >= 0 && newX < COL && newY >= 0 && newY < ROW) {
                            //寻找到空白快
                            if (dataTiles[newY][newX] == COL * ROW - 1) {
                                //交换图片
                                int temp = dataTiles[point.getY()][point.getX()];
                                dataTiles[point.getY()][point.getX()] = dataTiles[newY][newX];
                                dataTiles[newY][newX] = temp;
                                //刷新
                                invalidate();
                                if (tilesBoard.isSuccess(dataTiles)) {
                                    isSuccess = true;
                                    invalidate();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("拼图成功")
                                            .setCancelable(false)
                                            .setPositiveButton("让我康康！", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                letsGo();
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
                                    } else {
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
