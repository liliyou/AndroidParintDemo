package winho.com.print.unit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by xuyating on 2017/3/23.
 */

public class Utils {

    //讀取檔案為 Bitmap
    public Bitmap getLocalBitmap(String path){
        return BitmapFactory.decodeFile(path, getBitmapOptions());
    }
    //把圖片設定為小圖片
    private BitmapFactory.Options getBitmapOptions(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 2;
        return options;
    }
    //給兩點畫文字
    public void drawText(Canvas canvas,Paint paint, HashMap<String, Float> point1, HashMap<String, Float> point2, String strText) {

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
