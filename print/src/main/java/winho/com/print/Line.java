//package winho.com.print;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import winho.com.print.unit.PrintModel;
//
//public class Line extends PrintUnit {
//
//
//    Boolean onEdit = false;
//
//    HashMap<String, Integer> point1 = new HashMap<String, Integer>();
//    HashMap<String, Integer> point2 = new HashMap<String, Integer>();
//
//    int textSize = 5;
//    int thick = 2;
//    String text = "";
//
//
//    public Line(HashMap<String, Integer> point1, HashMap<String, Integer> point2) {
//        this.point1 = point1;
//        this.point2 = point2;
//        printModel = PrintModel.Line;
//    }
//
//
//    @Override
//    void onScaleSize(float x, float y) {
//
//    }
//
//    @Override
//    Boolean isOnEdit(float x, float y) {
//        return onEdit;
//    }
//
//    @Override
//    Boolean isOnChosen(float x, float y) {
//        return null;
//    }
//
//    @Override
//    Boolean isOnClickDelete(float x, float y) {
//        return null;
//    }
//
//    @Override
//    Boolean isOnClickCircle(float x, float y) {
//        return null;
//    }
//}
