package winho.com.print;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import winho.com.print.unit.PrintModel;

import static android.R.attr.path;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.FILL_AND_STROKE;
import static android.graphics.Paint.Style.STROKE;

/*
* point1 point2
* point3 point4
* */

public class Rectangle extends PrintUnit {


    Boolean onEdit = false;

    HashMap<String, Float> point1 = new HashMap<String, Float>();
    HashMap<String, Float> point2;
    HashMap<String, Float> point3;
    HashMap<String, Float> point4;
    int textSize = 5;
    int thick = 2;
    String text = "";


    public Rectangle(HashMap<String, Float> point1) {

        this.point1 = point1;
        printModel = PrintModel.Rectangle;
    }


    @Override
    void onScaleSize(float x, float y) {
        point2 = new HashMap<String, Float>();
        point3 = new HashMap<String, Float>();
        point4 = new HashMap<String, Float>();
        onMovePoint4(x, y);
    }

    @Override
    Boolean isOnEdit(float x, float y) {
        return onEdit;
    }

    @Override
    Boolean isOnChosen(float x, float y) {
        return null;
    }

    @Override
    Boolean isOnClickDelete(float x, float y) {
        return null;
    }

    @Override
    Boolean isOnClickCircle(float x, float y) {
        return null;
    }

    @Override
    void setStatus(int status) {

    }

    public void onMovePoint1(float x, float y) {


    }

    public void onMovePoint2(float x, float y) {


    }

    public void onMovePoint3(float x, float y) {


    }

    public void onMovePoint4(float x, float y) {
        point2.put("X", x);
        point2.put("Y", point1.get("Y"));
        point4.put("X", x);
        point4.put("Y", y);
        point3.put("X", point1.get("X"));
        point3.put("Y", y);
    }

    @Override
    Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(FILL);
        return paint;
    }

    @Override
    Paint getStorePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20f);
        return paint;
    }

    @Override
    void drawUnit(Canvas canvas) {

        drawMainPart(canvas);
    }


    private void drawMainPart(Canvas canvas) {
        if (point1 != null && point2 != null && point3 != null && point4 != null) {

            Path path = new Path();
            path.moveTo(point1.get("X"), point1.get("Y"));
            path.lineTo(point2.get("X"), point2.get("Y"));
            path.lineTo(point4.get("X"), point4.get("Y"));
            path.lineTo(point3.get("X"), point3.get("Y"));
            path.close();
            canvas.drawPath(path, getPaint());
            canvas.drawPath(path, getStorePaint());
        }

    }
}
