package backend;

import nodes.Gate;
import nodes.InputNode;
import nodes.Placeable;
import nodes.Wire;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.awt.geom.Line2D;

public class PointChecker {
    private static final Log log = LogFactory.getLog(PointChecker.class);

    public static Point check(Placeable p, Placeable p2) {
        if (p instanceof Wire w && p2 instanceof Wire w2) {
            return wireToWire(w, w2);
        }

        else if (p instanceof Wire w && p2 instanceof Gate g) {
            return wireToGate(w, g);
        }
        else if (p instanceof Gate g && p2 instanceof Wire w){
            return wireToGate(w, g);
        }

        else if (p instanceof InputNode i && p2 instanceof Wire w){
            return inputNodeToWire(i, w);
        }
        else if (p instanceof Wire w && p2 instanceof InputNode i){
            return inputNodeToWire(i, w);
        }

        else if (p instanceof InputNode i && p2 instanceof Gate g){
            return inputNodeToGate(i, g);
        }
        else if (p instanceof Gate g && p2 instanceof InputNode i){
            return inputNodeToGate(i, g);
        }

        else if (p instanceof Gate g1 && p2 instanceof Gate g2){
            return gateToGate(g1, g2);
        }



        return null;
    }



    public static Point gateToGate(Gate g1, Gate g2) {
        System.out.println("gateToGate()");
        int gsv = g1.getGridStepValue();
        for (Point p : g1.getInputPoints()){
            if (withinRange(p, g2.getOutputPoint(), gsv)){
                return p;
            }
        }
        for (Point p : g2.getInputPoints()){
            if(withinRange(p, g1.getOutputPoint(), gsv)){
                return p;
            }
        }
        return null;
    }

    public static Point inputNodeToGate(InputNode i, Gate g) {
        System.out.println("inputNodeToGate()");
        for (Point p : i.getOutputPoints()){
            for (Point p2 : g.getInputPoints()){
                if (withinRange(p, p2, g.getGridStepValue())){
                    return p;
                }
            }
        }
        return null;
    }

    private static Point inputNodeToWire(InputNode i, Wire w) {
        System.out.println("inputNodeToWire()");
        for (Point p : i.getOutputPoints()){
            if (landsOnWireEnd(p, w)){
                return p;
            }
        }
        return null;
    }

    private static Point wireToGate(Wire w, Gate g) {
        System.out.println("wireToGate()");
        for (Point p : g.getInputPoints()){
            if (landsOnWire(p, w)){
                return p;
            }
        }
        if (landsOnWire(g.getOutputPoint(), w)){
            return g.getOutputPoint();
        }
        return null;
    }

    private static Point wireToWire(Wire w1, Wire w2) {
        System.out.println("wireToWire()");
        // case for endpoint of wire w1 lands on w2
        for (Point p : w1.getEndPoints()) {
            if (landsOnWire(p, w2)){
                return p;
            }
        }
        for (Point p : w2.getEndPoints()) {
            if (landsOnWire(p, w1)){
                return p;
            }
        }
        return null;
    }





    private static boolean landsOnWireEnd(Point p, Wire w) {
        System.out.println("landsOnWireEnd()");
        for (Point end : w.getEndPoints()) {
            if (withinRange(p, end, w.getGridStepValue())) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param p1 Point to be tested
     * @param p2 Another Point to be tested
     * @param gridStepValue current GSV
     * @return whether the distance between the two points is within an acceptable range (GSV / 3)
     */
    private static boolean withinRange(Point p1, Point p2, int gridStepValue){
        System.out.println("withinRange()");
        double acceptableRange = gridStepValue / 3d;
        double dist = Point.distance(p1.x, p1.y, p2.x, p2.y);
        System.out.println("Testing withinRange: p1=" + p1 + ", p2=" + p2 + ", dist=" + dist + ", acceptable=" + acceptableRange);
        return dist <= acceptableRange;
    }

    /**
     * @return if the point lands on the wire
     */
    private static boolean landsOnWire(Point point, Wire w) {
        double acceptableRange = w.getGridStepValue() / 3d;
        Line2D line = new Line2D.Double(w.getStart(), w.getMidPoint()); // start to mid line
        Line2D line2 = new Line2D.Double(w.getMidPoint(), w.getEnd());  // mid to end line
        System.out.println("Testing landsOnWire: " + line.ptSegDist(point) + " mid-end: " + line2.ptSegDist(point));
        return line.ptSegDist(point) <= acceptableRange || line2.ptSegDist(point) <= acceptableRange;
    }
}
