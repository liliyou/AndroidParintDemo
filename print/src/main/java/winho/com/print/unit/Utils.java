package winho.com.print.unit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by xuyating on 2017/3/23.
 */

public class Utils {

    //讀取檔案為 Bitmap
    public Bitmap getLocalBitmap(String path) {
        return BitmapFactory.decodeFile(path, getBitmapOptions());
    }

    //把圖片設定為小圖片
    private BitmapFactory.Options getBitmapOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 2;
        return options;
    }

    // 判斷是不是在線上
    public boolean onTouchLine2(View parentView, HashMap<String, Float> point1, HashMap<String, Float> point2, HashMap<String, Float> pointCheck) {


        Path path = new Path();

        path.moveTo(point1.get("X") - 20, point1.get("Y") - 20);
        path.lineTo(point1.get("X") + 20, point1.get("Y") + 20);
        path.lineTo(point2.get("X") + 20, point2.get("Y") + 20);
        path.lineTo(point2.get("X") - 20, point2.get("Y") - 20);
        path.close();

        Region region = new Region();
        region.setPath(path, new Region(0, 0, parentView.getWidth(), parentView.getHeight()));
        float x = pointCheck.get("X");
        float y = pointCheck.get("Y");

        return region.contains((int) x, (int) y);
    }

    // 判斷是不是在直線上
    public boolean onTouchLine(HashMap<String, Float> point1, HashMap<String, Float> point2, HashMap<String, Float> pointCheck) {

        float x = point1.get("X");
        float y = point1.get("Y");
        float x2 = point2.get("X");
        float y2 = point2.get("Y");
        float e1 = pointCheck.get("X");
        float e2 = pointCheck.get("Y");

        if (x > x2) {
            if (e1 > x || e1 < x2) {
                Log.i("Ｘ不對", "");
                return false;
            }
        } else {
            if (e1 > x2 || e1 < x) {
                Log.i("Ｘ不對", "");
                return false;
            }
        }
        if (y > y2) {
            if (e2 > y || e2 < y2) {
                Log.i("Ｙ不對", "");
                return false;
            }
        } else {
            if (e2 > y2 || e2 < y) {
                Log.i("Ｙ不對", "");
                return false;
            }
        }


        double m = (double) (y - y2) / (double) (x - x2);
        if (!Double.isInfinite(m)) {
            Log.e("除了垂直線", "四捨五入");
            m = ((int) (m * 1000)) / 1000;//四捨五入三位數
        }

        Log.i("算出的斜率", "" + m);
        double bI = y - (m * x);// 常數=-50
        Log.i("算出的常數", "" + bI);

        //x
        int e1Arrund1 = (int) e1+ 60; //
        int e1Arrund2 = (int) e1- 60; //
        //y
        int e2Arrund1 = (int) e2+ 60; //
        int e2Arrund2 = (int) e2- 60; //

        Log.e("tag", "");
        //垂直線
        if (Double.isInfinite(m)) {
            Log.e("垂直線", "");
            for (int j = e2Arrund2; j < e2Arrund1; j++) {
                if (j == y) {
                    return true;
                }
            }
        } else {
            // 判斷是不是在斜率上
            // 點擊的上下左右
            for (int i = e1Arrund2; i < e1Arrund1; i++) {
                long NewY = (long) ((i * m) + bI);// 現在x上的線上的y點
                Log.i("NewY", "" + NewY);
                if ((e2Arrund2 <= NewY) && (e2Arrund1 >= NewY)) {
                    return true;
                }
            }
        }
        Log.e("奇怪", "");

        return false;
    }

    //給兩點畫文字
    public void drawText(Canvas canvas, Paint paint, HashMap<String, Float> point1, HashMap<String, Float> point2, String strText) {

        //避免文字比線還長處理
        Rect bounds = new Rect();
        paint.getTextBounds(strText, 0, strText.length(), bounds);
        paint.setTextAlign(Paint.Align.CENTER);
        int a1 = (int) (point1.get("X") - point2.get("X"));
        int b1 = (int) (point1.get("Y") - point2.get("Y"));
        int c1 = (int) Math.sqrt(a1 * a1 + b1 * b1);
        int d1 = bounds.width() - c1 + 10;
        if (d1 > 0) {
            int sizeOneText = (int) bounds.width()
                    / strText.length();// 一個字的寬
            float numTF = c1 / sizeOneText;
            int numT = (int) Math.floor(numTF);// 可以顯示幾個字
            if ((numT - 4) > 0) {
                strText = strText.substring(0, numT - 4)
                        + "...";
            } else {
                strText = "...";
            }
        }


        Path path = new Path();
        if (point1.get("X") < point2.get("X")) {
            //路徑畫文字
            path.moveTo(point1.get("X"), point1.get("Y"));
            path.lineTo(point2.get("X"), point2.get("Y"));
            canvas.drawTextOnPath(strText,
                    path, -8, -bounds.height(),
                    paint);
        } else {
            //路徑畫文字
            path.moveTo(point2.get("X"), point2.get("Y"));
            path.lineTo(point1.get("X"), point1.get("Y"));
            canvas.drawTextOnPath(strText,
                    path, -8, -bounds.height(),
                    paint);
        }
    }

}
