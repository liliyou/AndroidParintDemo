package winho.com.print.space;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.HashMap;

import winho.com.print.unit.PrintModel;
import winho.com.print.unit.PrintState;

/**
 * Created by xuyating on 2017/3/23.
 */

public abstract class PrintUnit {

    public PrintModel printModel;

    public Resources resources;

    public PrintState printState = PrintState.NotEdit;

    public final int circleSize = 60;

    public Boolean isWaring = false;

    //這個元件正在被放大
    public abstract void onScaleSize(float x, float y);

    //檢查是不是這個元件該做事

    //點擊元件做事
    public abstract void onActionDown(float x, float y);

    //檢查XY在不在元件內
    public abstract Boolean contains(float x, float y);

    //傳入移動手勢
    public abstract Boolean onMoveProcess(float x, float y);

    //傳入拿起手勢
    public abstract Boolean onActionUpProcess(float x, float y);

    //正在被點擊叉叉
    public abstract Boolean isOnClickDelete(float x, float y);

    //正在被點擊圓圈
    public abstract Boolean isOnClickCircle(float x, float y);

    //正在被點擊圓圈索引
    public abstract int getClickCircleIndex(float x, float y);

    //移動點
    public abstract void movePoint(HashMap<Integer, Point> points, int MovePointIndex, float x, float y);

    //設定狀態
    public abstract void setStatus(int status);

    //拿一班畫筆
    public abstract Paint getPaint();

    //拿填充畫筆
    public abstract Paint getFullPaint();

    //拿外框畫筆
    public abstract Paint getStrokePaint();

    //拿文字畫筆
    public abstract Paint getTextPaint();

    //拿緊告畫筆
    public abstract Paint getWarningPaint();

    //拿圓圈畫筆
    public abstract Paint getCirclePaint();

    //畫圈圈
    public abstract void drawCircle(Canvas canvas);

    //畫叉叉
    public abstract void drawDelete(Canvas canvas);

    //畫叉叉
    public abstract void drawData(Canvas canvas);

    //直接畫
    public abstract void drawUnit(Canvas canvas);

    //設定顏色
    public abstract void setColor(int color);

    //設定粗細
    public abstract void setThick(int thick);

    //設定文字大小
    public abstract void setTextSize(int textSize);

    //設定文字
    public abstract void setText(String text);
}
