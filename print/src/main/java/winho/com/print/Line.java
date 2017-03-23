package winho.com.print;

import java.util.ArrayList;
import java.util.HashMap;

public class Line {
    ArrayList<HashMap<String, Integer>> point = new ArrayList<HashMap<String, Integer>>();
    ArrayList<HashMap<String, Integer>> type = new ArrayList<HashMap<String, Integer>>();

    public Line(ArrayList<HashMap<String, Integer>> point) {
        this.point = point;
    }

    public ArrayList<HashMap<String, Integer>> getType() {
        return type;
    }
}
