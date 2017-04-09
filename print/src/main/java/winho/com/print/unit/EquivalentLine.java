package winho.com.print.unit;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by xuyating on 2017/4/10.
 */

public class EquivalentLine {

    TYPE type;
    int value = 0;
    int displayIndex = 0;
    int[][] lines;


    public EquivalentLine(TYPE type, int[][] line) {
        this.type = type;
        this.lines = line;
    }


    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public String getValueWithUnit() {
        if (value == 0) {
            return "";
        } else {
            return "" + value;
        }
    }

    public void setValue(int value) {
        this.value = value;
    }


    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }

    public int getPoint1() {
        if (lines.length > displayIndex) {
            return lines[displayIndex][0];
        } else {
            return 0;
        }
    }

    public int getPoint2() {
        if (lines.length > displayIndex) {
            return lines[displayIndex][1];
        } else {
            return 0;
        }
    }

    public Boolean containLine(int[][] line) {
        if (lines != null && Arrays.asList(lines).containsAll(Arrays.asList(line))) {
            return true;
        } else {
            return false;
        }

    }

    public Boolean isLineIntersects(HashMap<Integer, Point> prePoints) {
        Utils utils = new Utils();
        Boolean isLineIntersects =false;

        for (int i = 0; i < lines.length; i++) {
            if (lines.length > i + 1) {
                isLineIntersects = utils.doIntersect(prePoints.get(lines[i][0]), prePoints.get(lines[i][1]), prePoints.get(lines[i + 1][0]), prePoints.get(lines[i + 1][1]));
            }
        }
        return isLineIntersects;
    }

    public enum TYPE {
        L, W, H
    }

}
