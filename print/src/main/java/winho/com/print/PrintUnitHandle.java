package winho.com.print;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import winho.com.print.unit.PrintModel;

import static winho.com.print.unit.PrintModel.Rectangle;

/**
 * Created by xuyating on 2017/3/28.
 */

public class PrintUnitHandle {

    public static PrintUnit newPaintUnit(View parentView, Resources resources,
                                         PrintModel printModel, float x, float y, Bitmap iconDelete) {
        switch (printModel) {
            case Rectangle:
                HashMap<String, Float> point1 = new HashMap<String, Float>();
                point1.put("X", x);
                point1.put("Y", y);
                return new Rectangle(resources, parentView, point1, iconDelete);

            default:
                HashMap<String, Float> point2 = new HashMap<String, Float>();
                point2.put("X", x);
                point2.put("Y", y);
                return new Rectangle(resources, parentView, point2, iconDelete);
        }
    }


    public static PrintUnit getOnProcessUnit(float x, float y, ArrayList<PrintUnit> printUnitArrayList) {
        for (int i = 0; i < printUnitArrayList.size(); i++) {
            if (printUnitArrayList.get(i).isOnEdit(x, y)) {
                return printUnitArrayList.get(i);
            }
        }
        return null;
    }



}
