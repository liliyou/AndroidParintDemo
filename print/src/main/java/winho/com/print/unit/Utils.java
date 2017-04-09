package winho.com.print.unit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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

    // point to line
    public Path getLineFromPoint(Point point, Point point2) {
        Path path = new Path();
        path.moveTo(point.x - 60, point.y - 60);
        path.lineTo(point.x + 60, point.y + 60);
        path.lineTo(point2.x + 60, point2.y + 60);
        path.lineTo(point2.x - 60, point2.y - 60);
        path.close();
        return path;
    }

    // Given three colinear points p, q, r, the function checks if point q lies on line segment 'pr'
    public boolean onSegment(Point p, Point q, Point r) {
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y))
            return true;
        return false;
    }


    /**
     * To find orientation of ordered triplet (p, q, r).
     * The function returns following values
     *
     * @param p
     * @param q
     * @param r
     * @return 0 --> p, q and r are colinear, 1 --> 顺时针方向, 2 --> 逆时钟方向
     */
    int orientation(Point p, Point q, Point r) {
        // See http://www.geeksforgeeks.org/orientation-3-ordered-points/  for details of below formula.
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;  // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }


    //檢查兩線相交
    // The main function that returns true if line segment 'p1q1' and 'p2q2' intersect.
    public boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {

        // Find the four orientations needed for general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }
        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }


    // 判斷是不是在直線上
//    public boolean onTouchLine(HashMap<String, Float> point1, HashMap<String, Float> point2, HashMap<String, Float> pointCheck) {
//        //沒有未完全正確
//        Boolean onTouch = false;
//        float x = point1.get("X");
//        float y = point1.get("Y");
//        float x2 = point2.get("X");
//        float y2 = point2.get("Y");
//        float e1 = pointCheck.get("X");
//        float e2 = pointCheck.get("Y");
//        //x
//        int e1Arrund1 = (int) e1 + 60; //
//        int e1Arrund2 = (int) e1 - 60; //
//        //y
//        int e2Arrund1 = (int) e2 + 60; //
//        int e2Arrund2 = (int) e2 - 60; //
//
//
//        //最簡單的檢查
//        if (!checkRangeSimple(x, y, x2, y2, e1, e2)) {
//            Log.e("最簡單的檢查", "錯誤");
//        } else {
//            //檢查是否為特殊斜率
//            //斜率無限大
//            if (Double.isInfinite((double) (y - y2) / (double) (x - x2))) {
//                Log.e("垂直線", "");
//                for (int j = e2Arrund2; j < e2Arrund1; j++) {
//                    if (j == y) {
//                        onTouch = true;
//                    }
//                }
//            } else {
//                double m = (double) (y - y2) / (double) (x - x2);
//                m = ((int) (m * 1000)) / 1000;//四捨五入三位數
//                Log.i("算出的斜率", "" + m);
//                double bI = y - (m * x);// 常數=-50
//                Log.i("算出的常數", "" + bI);
//
//                // 判斷是不是在斜率上
//                // 點擊的上下左右
//                for (int i = e1Arrund2; i < e1Arrund1; i++) {
//                    long NewY = (long) ((i * m) + bI);// 現在x上的線上的y點
//                    Log.i("NewY", "" + NewY);
//                    if ((e2Arrund2 <= NewY) && (e2Arrund1 >= NewY)) {
//                        onTouch = true;
//                    }
//                }
//
//            }
//        }
//        return onTouch;
//    }
//
//    private Boolean checkRangeSimple(float x, float y, float x2, float y2, float e1, float e2) {
//        if (x > x2) {
//            if (e1 > x || e1 < x2) {
//                Log.i("Ｘ不對", "");
//                return false;
//            }
//        } else {
//            if (e1 > x2 || e1 < x) {
//                Log.i("Ｘ不對", "");
//                return false;
//            }
//        }
//        if (y > y2) {
//            if (e2 > y || e2 < y2) {
//                Log.i("Ｙ不對", "");
//                return false;
//            }
//        } else {
//            if (e2 > y2 || e2 < y) {
//                Log.i("Ｙ不對", "");
//                return false;
//            }
//        }
//        return true;
//
//    }

    //給兩點畫文字
    public void drawText(Canvas canvas, Paint paint, Point point1, Point point2, String strText) {

        //避免文字比線還長處理
        Rect bounds = new Rect();
        paint.getTextBounds(strText, 0, strText.length(), bounds);
        paint.setTextAlign(Paint.Align.CENTER);
        int a1 = (int) (point1.x - point2.x);
        int b1 = (int) (point1.y - point2.y);
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
        if (point1.x < point2.x) {
            //路徑畫文字
            path.moveTo(point1.x, point1.y);
            path.lineTo(point2.x, point2.y);
            canvas.drawTextOnPath(strText,
                    path, -8, -bounds.height(),
                    paint);
        } else {
            //路徑畫文字
            path.moveTo(point2.x, point2.y);
            path.lineTo(point1.x, point1.y);
            canvas.drawTextOnPath(strText,
                    path, -8, -bounds.height(),
                    paint);
        }
    }

}
