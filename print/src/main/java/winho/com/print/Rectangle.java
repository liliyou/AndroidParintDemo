package winho.com.print;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import winho.com.print.unit.PrintModel;
import winho.com.print.unit.PrintState;
import winho.com.print.unit.Utils;

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

    int nowChangePointIndex = 0;
    Bitmap iconDelete;
    View parentView;


    int textSize = 25;
    int thick = 10;
    String text = "";

    HashMap<Integer, HashMap<String, Float>> points = new HashMap<Integer, HashMap<String, Float>>();
    HashMap<Integer, HashMap<String, Float>> pointsSpace = new HashMap<Integer, HashMap<String, Float>>();
    HashMap<String, Float> deletePosition = new HashMap<String, Float>();
    Path tPath = new Path();
    Region tRegion = new Region();


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
    void onActionDown(float x, float y) {


        if (printState == PrintState.NotEdit) {
            //框裡轉換為編輯模式
            printState = PrintState.Edit;

            //如果還沒有載入完畢
            if ((!points.containsKey(3)) || (!points.get(3).containsKey("Y"))) {
                printState = PrintState.NotEdit;
            }
        } else {
            Boolean hasEdit = false;
            //如果早就是編輯模式

            if (isOnClickDelete(x, y)) {
                printState = PrintState.onDelete;
                hasEdit = true;
            }
            //點擊點得到正在點擊的點
            if (isOnClickCircle(x, y)) {
                nowChangePointIndex = getClickCircleIndex(x, y);
                printState = PrintState.onMoveCircle;
                hasEdit = true;
            }

            //如果在框裡
            if (contains(x, y)) {
                //準備移動初始化
                if (!hasEdit) {
                    //初始化 space
                    for (int i = 1; i < 5; ++i) {
                        HashMap<String, Float> space = new HashMap<String, Float>();
                        space.put("X", points.get(i).get("X") - x);
                        space.put("Y", points.get(i).get("Y") - y);
                        pointsSpace.put(i, space);
                    }
                    printState = PrintState.onMovePrintUnit;
                    hasEdit = true;
                }
            }
            //禁止編輯
            if (!hasEdit) {
                printState = PrintState.NotEdit;
            }

        }
    }

    @Override
    Boolean contains(float x, float y) {

        Boolean inUnit = false;

        if (tRegion.isEmpty()) {

        } else {
            if (tRegion.contains((int) x, (int) y)) {
                inUnit = true;
            }
        }
        return inUnit;
    }


    @Override
    Boolean onMoveProcess(float x, float y) {


        if ((printState == PrintState.onMoveCircle) && (nowChangePointIndex != 0)) {
            movePoint(nowChangePointIndex, x, y);
        }
        if ((printState == PrintState.onMovePrintUnit) && contains(x, y)) {

            if (pointsSpace.size() == 4) {
                //初始化 space
                for (int i = 1; i < 5; ++i) {
                    HashMap<String, Float> point = new HashMap<String, Float>();
                    point.put("X", x + pointsSpace.get(i).get("X"));
                    point.put("Y", y + pointsSpace.get(i).get("Y"));
                    points.put(i, point);
                }
            }

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

        //已經有叉叉
        if ((printState != PrintState.NotEdit) && deletePosition.containsKey("X") && deletePosition.containsKey("Y")) {

            //上下左右判定
            Boolean isX_L = (deletePosition.get("X") < x);
            Boolean isX_R = (deletePosition.get("X") + iconDelete.getWidth() > x);
            Boolean isY_T = (deletePosition.get("Y") < y);
            Boolean isY_B = (deletePosition.get("Y") + iconDelete.getHeight() > y);

            if (isX_L && isX_R && isY_T && isY_B) {
                onClick = true;
            }

        }

        Log.e("點擊叉叉？", "" + onClick);
        return onClick;
    }

    @Override
    Boolean isOnClickCircle(float x, float y) {

        Boolean onClick = (getClickCircleIndex(x, y) != 0);

        Log.e("點擊圈圈？", "" + onClick);
        return onClick;
    }


    @Override
    int getClickCircleIndex(float x, float y) {
        int Index = 0;

        if ((printState != PrintState.NotEdit) && points.size() == 4) {
            for (int i = 1; i < 5; ++i) {
                float point_X = points.get(i).get("X");
                float point_Y = points.get(i).get("Y");

                //上下左右判定
                Boolean isX_L = ((point_X - (circleSize)) < x);
                Boolean isX_R = ((point_X + (circleSize)) > x);
                Boolean isY_T = ((point_Y - (circleSize)) < y);
                Boolean isY_B = ((point_Y + (circleSize)) > y);

                if (isX_L && isX_R && isY_T && isY_B) {
                    Index = i;
                    break;
                }

            }
        }
        return Index;
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
    Paint getTextPaint() {
        Paint paint = new Paint();
        paint.setTextSize(thick * 6);// 設定字體大小
        paint.setColor(Color.BLUE);
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
        drawData(canvas);
    }


    private void drawMainPart(Canvas canvas) {
        if (points.containsKey(4) && points.get(4).containsKey("Y")) {

            tPath = getPath();
            tRegion = getRegion(tPath);
            canvas.drawPath(tPath, getPaint());
            canvas.drawPath(tPath, getStrokePaint());
        }

    }

    private Path getPath() {
        Path path = new Path();
        path.moveTo(points.get(1).get("X"), points.get(1).get("Y"));
        path.lineTo(points.get(2).get("X"), points.get(2).get("Y"));
        path.lineTo(points.get(4).get("X"), points.get(4).get("Y"));
        path.lineTo(points.get(3).get("X"), points.get(3).get("Y"));
        path.close();
        return path;
    }

    private Region getRegion(Path path) {
        Region region = new Region();
        region.setPath(path, new Region(0, 0, parentView.getWidth(), parentView.getHeight()));

        return region;
    }

    @Override
    void drawCircle(Canvas canvas) {
        if ((printState != PrintState.NotEdit) && points.containsKey(3) && points.get(3).containsKey("Y")) {
            canvas.drawCircle(points.get(1).get("X"), points.get(1).get("Y"), circleSize, getCirclePaint());
            canvas.drawCircle(points.get(2).get("X"), points.get(2).get("Y"), circleSize, getCirclePaint());
            canvas.drawCircle(points.get(3).get("X"), points.get(3).get("Y"), circleSize, getCirclePaint());
            canvas.drawCircle(points.get(4).get("X"), points.get(4).get("Y"), circleSize, getCirclePaint());
        }
    }

    @Override
    void drawDelete(Canvas canvas) {

        if ((printState != PrintState.NotEdit) && points.containsKey(4) && points.get(4).containsKey("Y")) {
            int index = 3;

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

    @Override
    void drawData(Canvas canvas) {

        Utils utils = new Utils();
        utils.drawText(canvas, getTextPaint(), points.get(1), points.get(2), "123");
        
    }

}
