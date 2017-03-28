package winho.com.print;

import android.graphics.Canvas;
import android.graphics.Paint;

import winho.com.print.unit.PrintModel;

/**
 * Created by xuyating on 2017/3/23.
 */

public abstract class PrintUnit {

    public PrintModel printModel;

    //這個元件正在被放大
    abstract void onScaleSize(float x, float y);

    //這個元件正在被編輯
    abstract Boolean isOnEdit(float x, float y);

    //檢查 x,y 是不是屬於這個元件
    abstract Boolean isOnChosen(float x, float y);

    //正在被點擊叉叉
    abstract Boolean isOnClickDelete(float x, float y);

    //正在被點擊圓圈
    abstract Boolean isOnClickCircle(float x, float y);

    //設定狀態
    abstract void setStatus(int status);

    //拿畫筆
    abstract Paint getPaint();
    //直接畫
    abstract void drawUnit(Canvas canvas);


}
