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
* point1 point3
* point2 point4
* */

public class Rectangle extends PrintUnit {


    Boolean onEdit = false;

    HashMap<Integer, HashMap<String, Float>> points = new HashMap<Integer, HashMap<String, Float>>();
    HashMap<String, Float> deletePosition = new HashMap<String, Float>();
    int textSize = 5;
    int thick = 2;
    int circleSize = 50;
    int nowChangePointIndex = 0;
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
        point2.put("X", points.get(1).get("X"));
        point2.put("Y", y);
        points.put(2, point2);
        HashMap<String, Float> point3 = new HashMap<String, Float>();
        point3.put("X", x);
        point3.put("Y", points.get(1).get("X"));
        points.put(3, point3);
        HashMap<String, Float> point4 = new HashMap<String, Float>();
        point4.put("X", x);
        point4.put("Y", y);
        points.put(4, point4);

    }

    @Override
    Boolean onDuty(float x, float y) {

        Boolean onClickThis = true;
        //如果早就是編輯模式
        if (onEdit) {
            //點擊叉叉？
            if (isOnClickDelete(x, y)) {
                //刪除這個元件
            }

            //點擊點
            if (isOnClickCircle(x, y)) {

            }

        } else {
            //如果還沒有載入完畢
            if ((!points.containsKey(3)) || (!points.get(3).containsKey("Y"))) {
                onClickThis = false;
                onEdit = false;
                return onClickThis;
            }

            //如果不在匡裡
            if (!(points.get(1).get("X") < x && points.get(4).get("X") > x &&
                    points.get(1).get("Y") < y && points.get(4).get("Y") > y)) {
                onClickThis = false;
                onEdit = false;
                return onClickThis;
            }

            //轉換為編輯模式
            if ((!onEdit) && onClickThis) {
                onEdit = true;
            }
        }


        return onClickThis;
    }

    @Override
    Boolean onMoveProcess(float x, float y) {
        if (onEdit && nowChangePointIndex == 0) {

        }
        if (nowChangePointIndex != 0) {
            movePoint(nowChangePointIndex, x, y);
        }


        return null;
    }

    @Override
    Boolean onActionUpProcess(float x, float y) {
        return null;
    }

    @Override
    Boolean isOnClickDelete(float x, float y) {
        Boolean onClick = false;
//        if (onEdit && deletePosition.containsKey("X") && deletePosition.containsKey("Y")) {
//
//        }
//        if (deletePosition.get("X") < x && deletePosition.get("X") + iconDelete.getWidth() > x) {
//
//        }
//        if (deletePosition.get("Y") < y && deletePosition.get("Y") + iconDelete.getHeight() > y) {
//            onClick = true;
//        }
        return onClick;
    }

    @Override
    Boolean isOnClickCircle(float x, float y) {
        nowChangePointIndex = 0;
        Boolean onClick = false;
        if (onEdit && points.size() == 4) {
            for (int i = 1; i < 5; ++i) {
                float point_X = points.get(i).get("X");
                float point_Y = points.get(i).get("Y");

                //上下左右判定
                Boolean isX_L = ((point_X - (circleSize / 2)) < x);
                Boolean isX_R = ((point_X + (circleSize / 2)) > x);
                Boolean isY_T = ((point_Y - (circleSize / 2)) < y);
                Boolean isY_B = ((point_Y + (circleSize / 2)) > y);

                if (isX_L && isX_R && isY_T && isY_B) {
                    onClick = true;
                    nowChangePointIndex = i;
                    break;
                }

            }
        }
//        Log.e("目前點擊的點", "" + nowChangePointIndex);

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
