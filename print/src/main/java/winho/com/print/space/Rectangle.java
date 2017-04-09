package winho.com.print.space;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import winho.com.print.R;
import winho.com.print.unit.EquivalentLine;
import winho.com.print.unit.PrintModel;
import winho.com.print.unit.PrintState;
import winho.com.print.unit.Utils;

import static android.graphics.Paint.Style.FILL;
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
    int color = R.color.alcolor5;

    String text = "";

    HashMap<Integer, Point> points = new HashMap<Integer, Point>();
    HashMap<Integer, HashMap<String, Float>> pointsSpace = new HashMap<Integer, HashMap<String, Float>>();
    HashMap<String, Float> deletePosition = new HashMap<String, Float>();
    Path tPath = new Path();
    Region tRegion = new Region();


    EquivalentLine equivalentLine_L = new EquivalentLine(EquivalentLine.TYPE.L, new int[][]{{1, 3}, {2, 4}});
    EquivalentLine equivalentLine_W = new EquivalentLine(EquivalentLine.TYPE.W, new int[][]{{1, 2}, {3, 4}});


    public Rectangle(Resources resources, View parentView, Point point1, Bitmap iconDelete) {
        this.parentView = parentView;
        this.resources = resources;
        this.iconDelete = iconDelete;
        points.put(1, point1);
        printModel = PrintModel.Rectangle;
    }

    @Override
    public void onScaleSize(float x, float y) {

        Point point2 = new Point();
        point2.x = points.get(1).x;
        point2.y = (int) y;
        points.put(2, point2);


        Point point3 = new Point();
        point3.x = (int) x;
        point3.y = points.get(1).y;
        points.put(3, point3);

        Point point4 = new Point();
        point4.x = (int) x;
        point4.y = (int) y;
        points.put(4, point4);

    }

    @Override
    public void onActionDown(float x, float y) {


        if (printState == PrintState.NotEdit) {
            //框裡轉換為編輯模式
            printState = PrintState.Edit;

            //如果還沒有載入完畢
            if ((!points.containsKey(3)) || points.get(3) == null) {
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
                        space.put("X", points.get(i).x - x);
                        space.put("Y", points.get(i).y - y);
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
    public Boolean contains(float x, float y) {

        Boolean inUnit = false;

        if (tRegion.isEmpty()) {

        } else {
            if (tRegion.contains((int) x, (int) y)) {
                inUnit = true;
            }
            if(isOnClickDelete((int) x, (int) y)){
                inUnit = true;
            }
            if(isOnClickCircle((int) x, (int) y)){
                inUnit = true;
            }
        }
        return inUnit;
    }


    @Override
    public Boolean onMoveProcess(float x, float y) {

        if ((printState == PrintState.onMoveCircle) && (nowChangePointIndex != 0) && (points.size() == 4)) {

            HashMap<Integer, Point> prePoints = (HashMap<Integer, Point>) points.clone();
            Utils utils = new Utils();
            //先確定下一步沒被禁止
            movePoint(prePoints, nowChangePointIndex, x, y);

            isWaring = isWrongOperation(prePoints);
            if (!isWaring) {
                //可以移動
                movePoint(points, nowChangePointIndex, x, y);
            }
        }
        if ((printState == PrintState.onMovePrintUnit) && contains(x, y)) {

            if (pointsSpace.size() == 4) {
                //初始化 space
                for (int i = 1; i < 5; ++i) {
                    Point point = new Point();
                    point.x = (int) (x + pointsSpace.get(i).get("X"));
                    point.y = (int) (y + pointsSpace.get(i).get("Y"));

                    points.put(i, point);
                }
            }

        }
        return null;
    }

    @Override
    public Boolean onActionUpProcess(float x, float y) {
        return null;
    }

    @Override
    public Boolean isOnClickDelete(float x, float y) {
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

//        Log.e("點擊叉叉？", "" + onClick);
        return onClick;
    }

    @Override
    public Boolean isOnClickCircle(float x, float y) {

        Boolean onClick = (getClickCircleIndex(x, y) != 0);

//        Log.e("點擊圈圈？", "" + onClick);
        return onClick;
    }


    @Override
    public int getClickCircleIndex(float x, float y) {
        int Index = 0;

        if ((printState != PrintState.NotEdit) && points.size() == 4) {
            for (int i = 1; i < 5; ++i) {
                float point_X = points.get(i).x;
                float point_Y = points.get(i).y;

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
    public void setStatus(int status) {

    }


    public void movePoint(HashMap<Integer, Point> points, int MovePointIndex, float x, float y) {

        Point point = new Point();
        point.x = (int) x;
        point.y = (int) y;
        points.put(MovePointIndex, point);

    }

    @Override
    public Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(resources.getColor(color));
        paint.setStyle(FILL);
        return paint;
    }

    @Override
    public Paint getFullPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(resources.getColor(color));
        paint.setAlpha(60);
        paint.setStyle(FILL);
        return paint;
    }

    @Override
    public Paint getStrokePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(STROKE);
        paint.setColor(resources.getColor(color));
        paint.setStrokeWidth(20f);
        return paint;
    }


    @Override
    public Paint getWarningPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(STROKE);
        if (isWaring) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.TRANSPARENT);
        }
        paint.setStrokeWidth(20f);
        return paint;
    }

    @Override
    public Paint getTextPaint() {
        Paint paint = new Paint();
        paint.setTextSize(thick * 6);// 設定字體大小
        paint.setColor(Color.BLUE);
        return paint;
    }

    @Override
    public Paint getCirclePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(resources.getColor(R.color.draw_circle));
        return paint;
    }

    @Override
    public void drawUnit(Canvas canvas) {

        drawMainPart(canvas);
        drawCircle(canvas);
        drawDelete(canvas);
        drawData(canvas);

    }


    private void drawMainPart(Canvas canvas) {
        if (points.containsKey(4) && points.get(4) != null) {

            tPath = getPath();
            tRegion = getRegion(tPath);
            canvas.drawPath(tPath, getFullPaint());
            canvas.drawPath(tPath, getStrokePaint());
            canvas.drawPath(tPath, getWarningPaint());
        }

    }

    private Path getPath() {
        Path path = new Path();
        path.moveTo(points.get(1).x, points.get(1).y);
        path.lineTo(points.get(2).x, points.get(2).y);
        path.lineTo(points.get(4).x, points.get(4).y);
        path.lineTo(points.get(3).x, points.get(3).y);
        path.close();
        return path;
    }

    private Region getRegion(Path path) {
        Region region = new Region();
        region.setPath(path, new Region(0, 0, parentView.getWidth(), parentView.getHeight()));

        return region;
    }

    @Override
    public void drawCircle(Canvas canvas) {
        if ((printState != PrintState.NotEdit) && points.containsKey(3) && points.get(3) != null) {
            canvas.drawCircle(points.get(1).x, points.get(1).y, circleSize, getCirclePaint());
            canvas.drawCircle(points.get(2).x, points.get(2).y, circleSize, getCirclePaint());
            canvas.drawCircle(points.get(3).x, points.get(3).y, circleSize, getCirclePaint());
            canvas.drawCircle(points.get(4).x, points.get(4).y, circleSize, getCirclePaint());
        }
    }

    @Override
    public void drawDelete(Canvas canvas) {

        if ((printState != PrintState.NotEdit) && points.containsKey(4) && points.get(4) != null) {
            int index = 3;

            if ((points.get(index).x + iconDelete.getHeight() + circleSize + 20) > parentView.getWidth() || points.get(index).y - (circleSize * 3) - iconDelete.getHeight() < 0) {
                //叉叉畫裡面
                deletePosition.put("X", (float) points.get(index).x - (circleSize * 3));
                deletePosition.put("Y", (float) points.get(index).y + circleSize);

            } else {
                //叉叉畫外面
                deletePosition.put("X", (float) points.get(index).x + circleSize);
                deletePosition.put("Y", (float) points.get(index).y - (circleSize * 3));
            }

            canvas.drawBitmap(iconDelete, deletePosition.get("X"), deletePosition.get("Y"), getPaint());
        }
    }

    @Override
    public void drawData(Canvas canvas) {
        Utils utils = new Utils();

        utils.drawText(canvas, getTextPaint(), points.get(equivalentLine_L.getPoint1()), points.get(equivalentLine_L.getPoint2()), equivalentLine_L.getValueWithUnit());
        utils.drawText(canvas, getTextPaint(), points.get(equivalentLine_W.getPoint1()), points.get(equivalentLine_W.getPoint2()), equivalentLine_W.getValueWithUnit());


    }


    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void setThick(int thick) {
        this.thick = thick;
    }

    @Override
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }


    public Boolean isWrongOperation(HashMap<Integer, Point> prePoints) {
        Utils utils = new Utils();
        Boolean isLineIntersects;
        if (equivalentLine_L.isLineIntersects(prePoints) || equivalentLine_W.isLineIntersects(prePoints)) {
            //警告
            isLineIntersects = true;

        } else {
            isLineIntersects = false;
        }
        return isLineIntersects;
    }
}
