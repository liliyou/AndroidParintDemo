package winho.com.print;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import winho.com.print.unit.PrintModel;
import winho.com.print.unit.Utils;

/**
 * Created by xuyating on 2017/3/23.
 */

public class Board extends View {


    private Context mContext;
    private View parentView;
    //現在將要繪圖
    PrintModel nowWantToPrintModel = PrintModel.Rectangle;
    //載入圖片位置
    private String filePath;
    //圖片
    private Bitmap bitmapPhoto;
    //現在使用元件
    private PrintUnit printUnit;
    //所有繪畫元件
    private ArrayList<PrintUnit> printUnitArrayList = new ArrayList<PrintUnit>();


    public Board(Context context, View pParentView, String filePath) {
        super(context);
        this.mContext = context;
        this.parentView = pParentView;
        this.filePath = filePath;
        setBoardBg();

        printUnit = PrintUnitHandle.newPaintUnit(0,0, nowWantToPrintModel);
        printUnitArrayList.add(printUnit);

    }

    private void setBoardBg() {
        parentView.post(new Runnable() {
            @Override
            public void run() {
                //取得拍照圖片
                bitmapPhoto = ImageHandle.getFitSampleBitmap(filePath, parentView.getWidth(), parentView.getHeight());
                if (bitmapPhoto != null) {
                    Log.e("", "");
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint vPaint = new Paint();
        vPaint.setColor(Color.WHITE);
        vPaint.setStyle(Paint.Style.FILL);
        if (bitmapPhoto != null) {
            //畫出背景
            canvas.drawRect(0, 0, parentView.getWidth(), parentView.getHeight(),
                    vPaint);
            //畫出拍照圖片
            canvas.drawBitmap(bitmapPhoto, 0, 0, vPaint);

            //畫出元件
            if (printUnit != null) {
                printUnit.drawUnit(canvas);
            }

            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //當值元件要做事

                //選擇畫面上的元件

                //建立一個元件
//                printUnit = PrintUnitHandle.newPaintUnit(event.getX(), event.getY(), nowWantToPrintModel);
//                printUnitArrayList.add(printUnit);

//                Log.e("","x:"+event.getX()+"y:"+event.getY());
                return true;

            case MotionEvent.ACTION_MOVE:
//                Log.i("TOP", "ACTION_MOVE");
                printUnit.onScaleSize(event.getX(), event.getY());
//                invalidate();
                return true;
            case MotionEvent.ACTION_UP:

//                Log.i("","x:"+event.getX()+"y:"+event.getY());
//                printUnit.setStatus(1);
                return true;
        }

        return false;
    }


    // 取得現在要畫的物件

    // 給物件手勢響應事件

    // 圖片輸出畫面

    // 圖片存擋


}
