package nodes.gates;

import nodes.Placeable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public abstract class Gate extends Placeable {

    public abstract Point getBottomInputPoint();

    public abstract Point getOutputPoint();

    public abstract Set<Point> getInputPoints();

    public static ArrayList<Point> getConnectionPoints(int inputs, Gate gate){
        ArrayList<Point> connectionPoints = new ArrayList<>();
        connectionPoints.addAll(gate.getInputPoints());
        connectionPoints.add(gate.getOutputPoint());
        return connectionPoints;
    }
}
