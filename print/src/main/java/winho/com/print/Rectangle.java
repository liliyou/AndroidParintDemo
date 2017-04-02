package winho.com.print;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import winho.com.print.unit.PrintModel;

import static android.R.attr.content;
import static android.R.attr.path;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.FILL_AND_STROKE;
import static android.graphics.Paint.Style.STROKE;

/*
* point1 point2
* point3 point4
* */

public class Rectangle extends PrintUnit {


    Boolean onEdit = true;

    HashMap<Integer, HashMap<String, Float>> points = new HashMap<Integer, HashMap<String, Float>>();
    HashMap<String, Float> deletePosition = new HashMap<String, Float>();
    int textSize = 5;
    int thick = 2;
    int circleSize = 50;
    int nowChangePointIndex = 1;
    String text = "";
    Bitmap iconDelete;
    View parentView;


    public Rectangle(Resources resources, View parentView, HashMap<String, Float> point1, Bitmap iconDelete) {
        this.parentView = parentView;
        this.resources = resources;
        this.iconDelete = iconDelete;
        points.put(1, point1);
        printModel = PrintModel.Rectangle;
    }


    @Override
    void onScaleSize(float x, float y) {

        HashMap<String, Float> point2 = new HashMap<String, Float>();
        point2.put("X", x);
        point2.put("Y", points.get(1).get("Y"));
        points.put(2, point2);
        HashMap<String, Float> point4 = new HashMap<String, Float>();
        point4.put("X", x);
        point4.put("Y", y);
        points.put(4, point4);
        HashMap<String, Float> point3 = new HashMap<String, Float>();
        point3.put("X", points.get(1).get("X"));
        point3.put("Y", y);
        points.put(3, point3);

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
    Boolean onDuty(float x, float y) {

        Boolean onClick = true;

        //如果還沒有載入完畢
        if ((!points.containsKey(3)) || (!points.get(3).containsKey("Y"))) {
            onClick = false;
            return onClick;
        }

        //如果是Ｘ
        if (points.get(1).get("X") > x || points.get(4).get("X") < x) {
            onClick = false;
            return onClick;
        }

        //如果是Ｙ
        if (points.get(1).get("Y") > y || points.get(4).get("Y") < y) {
            onClick = false;
            return onClick;
        }

        return onClick;
    }

    @Override
    Boolean isOnClickDelete(float x, float y) {
        Boolean onClick = false;
        if (onEdit && deletePosition.containsKey("X") && deletePosition.containsKey("Y")) {

        }
        if (deletePosition.get("X") < x && deletePosition.get("X") + iconDelete.getWidth() > x) {

        }
        if (deletePosition.get("Y") < y && deletePosition.get("Y") + iconDelete.getHeight() > y) {
            onClick = true;
        }
        return onClick;
    }

    @Override
    Boolean isOnClickCircle(float x, float y) {

        Boolean onClick = false;
        if (onEdit && points.containsKey(3) && points.get(3).containsKey("Y")) {
            for (int i = 1; i < 5; ++i) {
                if (points.get(i).get("X") < x && points.get(i).get("X") + (circleSize / 2) > x) {
                    if (points.get(i).get("Y") < y && points.get(i).get("Y") + (circleSize / 2) > y) {
                        onClick = true;
                        nowChangePointIndex = i;
                        break;
                    }
                }
            }
        }

        return onClick;
    }

    @Override
    void setStatus(int status) {

    }


    public void movePoint(int MovePointIndex, float x, float y) {

        HashMap<String, Float> point = new HashMap<String, Float>();
        point.put("X", x);
        point.put("Y", y);
        points.put(MovePointIndex, point);
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
    Paint getStrokePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20f);
        return paint;
    }

    @Override
    Paint getCirclePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(resources.getColor(R.color.draw_circle));
        return paint;
    }

    @Override
    void drawUnit(Canvas canvas) {

        drawMainPart(canvas);
        drawCircle(canvas);
        drawDelete(canvas);
    }


    private void drawMainPart(Canvas canvas) {
        if (points.containsKey(3) && points.get(3).containsKey("Y")) {
            Path path = new Path();
            path.moveTo(points.get(1).get("X"), points.get(1).get("Y"));
            path.lineTo(points.get(2).get("X"), points.get(2).get("Y"));
            path.lineTo(points.get(4).get("X"), points.get(4).get("Y"));
            path.lineTo(points.get(3).get("X"), points.get(3).get("Y"));
            path.close();
            canvas.drawPath(path, getPaint());
            canvas.drawPath(path, getStrokePaint());
        }

    }

    @Override
    void drawCircle(Canvas canvas) {
        if (onEdit && points.containsKey(3) && points.get(3).containsKey("Y")) {
            canvas.drawCircle(points.get(1).get("X"), points.get(1).get("Y"), circleSize, getCirclePaint());
            canvas.drawCircle(points.get(2).get("X"), points.get(2).get("Y"), circleSize, getCirclePaint());
            canvas.drawCircle(points.get(3).get("X"), points.get(3).get("Y"), circleSize, getCirclePaint());
            canvas.drawCircle(points.get(4).get("X"), points.get(4).get("Y"), circleSize, getCirclePaint());
        }
    }

    @Override
    void drawDelete(Canvas canvas) {

        if (onEdit && points.containsKey(3) && points.get(3).containsKey("Y")) {
            int index = 2;

            if ((points.get(index).get("X") + iconDelete.getHeight() + circleSize + 20) > parentView.getWidth() || points.get(index).get("Y") - (circleSize * 3) - iconDelete.getHeight() < 0) {
                //叉叉畫裡面
                deletePosition.put("X", points.get(index).get("X") - (circleSize * 3));
                deletePosition.put("Y", points.get(index).get("Y") + circleSize);

            } else {
                //叉叉畫外面
                deletePosition.put("X", points.get(index).get("X") + circleSize);
                deletePosition.put("Y", points.get(index).get("Y") - (circleSize * 3));
            }

            canvas.drawBitmap(iconDelete, deletePosition.get("X"), deletePosition.get("Y"), getPaint());
        }
    }

}
