package winho.com.print;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import winho.com.print.unit.PrintModel;

/**
 * Created by xuyating on 2017/3/23.
 */

public abstract class PrintUnit {

    public PrintModel printModel;

    public Resources resources;

    //這個元件正在被放大
    abstract void onScaleSize(float x, float y);

    //檢查是不是這個元件該做事
    abstract Boolean onDuty(float x, float y);

    //傳入移動手勢
    abstract Boolean onMoveProcess(float x, float y);

    //傳入拿起手勢
    abstract Boolean onActionUpProcess(float x, float y);

    //正在被點擊叉叉
    abstract Boolean isOnClickDelete(float x, float y);

    //正在被點擊圓圈
    abstract Boolean isOnClickCircle(float x, float y);

    //移動點
    abstract void movePoint(int MovePointIndex, float x, float y);

    //設定狀態
    abstract void setStatus(int status);

    //拿畫筆
    abstract Paint getPaint();

    //拿畫筆
    abstract Paint getStrokePaint();

    //拿圓圈畫筆
    abstract Paint getCirclePaint();

    //畫圈圈
    abstract void drawCircle(Canvas canvas);

    //畫叉叉
    abstract void drawDelete(Canvas canvas);

    //直接畫
    abstract void drawUnit(Canvas canvas);


}
