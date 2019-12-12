package com.example.jigsawpuzzle;

import java.util.Random;

/* 用来保存块数据的类 */
public class Board {
    private int[][] array = null;
    private int row = 0;
    private int col = 0;

    //四个方向
    private int[][] dir = {
        {0, 1},     //下
        {1, 0},     //右
        {0, -1},    //上
        {-1, 0}     //左
    };

    /* 为图片块标号 */
    private void createIntegerArray(int row, int col) {
        array = new int[row][col];
        int idx = 0;
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++)
                array[i][j] = idx++;
    }

    /* 移动块的位置 */
    private Point move(int srcX, int srcY, int xOffset, int yOffset) {
        int x = srcX + xOffset;
        int y = srcY + yOffset;
        //错误返回(-1, -1)
        if(x < 0 || y < 0 || x >= col || y >= row)
            return new Point(-1, -1);

        int temp = array[y][x];
        array[y][x] = array[srcY][srcX];
        array[srcY][srcX] = temp;

        return new Point(x, y);
    }

    /* 得到下一个可以移动的位置 */
    private Point getNextPoint(Point src) {
        Random rd = new Random();
        //产生0~3的随机数
        int idx = rd.nextInt(4);
        int xOffset = dir[idx][0];
        int yOffset = dir[idx][1];
        Point newPoint = move(src.getX(), src.getY(), xOffset, yOffset);
        //移动没出错
        if(newPoint.getX() != -1 && newPoint.getY() != -1) {
            return newPoint;
        }
        //出错递归重算
        return getNextPoint(src);
    }

    /* 生成拼图数据 */
    public int[][] createRandomBoard(int row, int col) {
        if(row < 2 || col < 2)
            throw new IllegalArgumentException("行和列都不能小于2");
        //初始化
        this.row = row;
        this.col = col;

        //为图片快标号
        createIntegerArray(row, col);

        //最开始可移动的位置为右下角
        Point tempPoint = new Point(col - 1,row - 1);
        Random rd = new Random();
        //产生 100~200 的随机数，使开始的图片随机移动 100~200 次
        int num = rd.nextInt(101) + 100;
        for(int i = 0; i < num; i++)
            tempPoint = getNextPoint(tempPoint);

        return array;
    }

    /* 判断是否拼图成功 */
    public boolean isSuccess(int[][] arr) {
        int idx = 0;
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                if(arr[i][j] != idx) {
                    idx = 0;
                    return false;
                }
                idx++;
            }
        }
        return true;
    }
}
