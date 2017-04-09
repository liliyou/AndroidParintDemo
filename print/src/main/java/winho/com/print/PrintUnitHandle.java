package winho.com.print;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;

import winho.com.print.space.PrintUnit;
import winho.com.print.space.Rectangle;
import winho.com.print.unit.PrintModel;

/**
 * Created by xuyating on 2017/3/28.
 */

public class PrintUnitHandle {


    public static PrintUnit newPaintUnit(View parentView, Resources resources,
                                         PrintModel printModel, float x, float y, Bitmap iconDelete) {
        switch (printModel) {
            case Rectangle:
                Point point1 = new Point();
                point1.x = (int) x;
                point1.y = (int) y;
                return new Rectangle(resources, parentView, point1, iconDelete);

            default:
                Point point2 = new Point();
                point2.x = (int) x;
                point2.y = (int) y;
                return new Rectangle(resources, parentView, point2, iconDelete);
        }
    }


//    public static PrintUnit getOnProcessUnit(float x, float y, ArrayList<PrintUnit> printUnitArrayList) {
//        for (int i = 0; i < printUnitArrayList.size(); i++) {
//            if (printUnitArrayList.get(i).isOnEdit(x, y)) {
//                return printUnitArrayList.get(i);
//            }
//        }
//        return null;
//    }


}
