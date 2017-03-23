package winho.com.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xuyating on 2017/3/23.
 */

public class Board extends View {

    private Canvas canvas   = null;
    private Bitmap bitmap   = null;

    // 現在手指位置
    float eventX2;
    float eventY2;

    //現在使用元件
    PrintUnit printUnit;

    // 是不是這在移動
    int isMove = 0;

    // 設定畫面大小
    Canvas vBitmapCanvas = new Canvas();

    public Board(Context context,String settingPath) {
        super(context);
    }

    // 設定背景

    // 設定背景照片

    // 給上次存擋的資料

    // 設定現在要畫的模組

    public enum PrintModel {
        Line,
        ArrowLine,
        Rectangle,
        Icon
    }

    // 取得現在要畫的物件

    // 給物件手勢響應事件

    // 圖片輸出畫面

    // 圖片存擋


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 重繪, 再一次執行 onDraw 程序
        invalidate();
    }
}
