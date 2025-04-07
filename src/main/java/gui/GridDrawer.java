package gui;

import connections.PointChecker;
import nodes.gates.Gate;
import nodes.InputNode;
import nodes.Placeable;
import nodes.Wire;

import java.awt.*;
import java.util.*;

public class GridDrawer {

    public GridDrawer(){}

    public static void drawGrid(int gridStepValue, Graphics2D g, int width, int height, int radScale, int strokeScale, Color gridColor){
        int stroke = gridStepValue / strokeScale;
        g.setStroke(new BasicStroke(stroke));
        g.setColor(gridColor);
        // Draw the grid
        for (int i = 0; i <= width; i += gridStepValue) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i <= height; i += gridStepValue) {
            g.drawLine(0, i, width, i);
        }

        // Draws a small circle on each intersection of lines. AKA on every step of the gridStepValue
        int rad = gridStepValue / radScale; // scale the radius by a constant
        // iterate over the X axis
        for (int x = 0; x <= width; x += gridStepValue) {
            // iterate over the Y axis
            for (int y = 0; y <= height; y += gridStepValue) {

                // draw the circles. Ovals are drawn from the corner, so the x and y values need to be offset
                // by the radScale to get the circle centered on the point
                g.fillOval(x - rad / 2, y - rad / 2, rad, rad);
            }
        }
    }

    public static void drawConnectionPoints(Graphics2D g, int gsv, int radScale, Set<Point> connectionPoints, Color connectionColor){
        // call the paint method of each placed node, except for the grabbed node which is painted separately

        int rad = gsv / radScale; // scale the radius by a constant
        g.setColor(connectionColor);
        for (Point point : connectionPoints) {
            if (Objects.isNull(point)) continue;
            g.fillOval(point.x - rad / 2, point.y - rad / 2, rad, rad);
        }
                /* Paint a rectangle around the grabbed node. Also paint it separately so that it doesn't snap to the
                grid til grabbedNode is released.
                 */
    }

    /**
     * Loops through each node and draws it. This does not include the grabbedNode
     * @param nodes ArrayList of all nodes
     * @param g Graphics component
     */
    public static void drawNodes(ArrayList<Placeable> nodes, Placeable grabbedNode,  Graphics2D g){
        for (Placeable node : nodes) {
                node.paint(g);
        }
        if (grabbedNode == null) return;
        grabbedNode.paintSelected(g);
        grabbedNode.paint(g);
    }

    public static void gridUpdate(int lastGSV, int newGSV, ArrayList<Placeable> nodes, Placeable grabbedNode, PointChecker pc){
        // Scales the position of the nodes on screen with the new GSV
        for (Placeable node : nodes) {
            updateNode(node, lastGSV, newGSV);
        }

        if (grabbedNode != null) updateNode(grabbedNode, lastGSV, newGSV);

        HashMap<Point, Set<Placeable>> newConMap = new HashMap<>();
        for (Point key : pc.getConMap().keySet()){
            Set<Placeable> list = pc.getConMap().get(key);
            int newX = (key.x / lastGSV) * newGSV;
            int newY = (key.y / lastGSV) * newGSV;
            Point p = PointChecker.snapToGrid(new Point(newX, newY), newGSV);
            newConMap.put(p, list);
        }
        pc.setConMap(newConMap);
    }

    /**
     * Update the Placeable node's position to align with the grid
     * @param node: Placeable node to be fixed to grid
     * @param lastGSV: last gridStepValue
     * @param newGSV: new gridStepValue
     */
    public static void updateNode(Placeable node, int lastGSV, int newGSV){
        if (node instanceof Gate || node instanceof InputNode) {
            node.setGridStepValue(newGSV);
            int newX = ((node.getPos().x / lastGSV) * newGSV);
            int newY = ((node.getPos().y / lastGSV) * newGSV);
            node.setPos(PointChecker.snapToGrid(new Point(newX, newY), newGSV)); // TODO: might be bad to create objects in a for loop like this??
        }
        else if (node instanceof Wire wire){
            wire.setGridStepValue(newGSV);
            int startX = (wire.getStart().x / lastGSV) * newGSV;
            int startY = (wire.getStart().y / lastGSV) * newGSV;
            int endX = (wire.getEnd().x / lastGSV) * newGSV;
            int endY = (wire.getEnd().y / lastGSV) * newGSV;
            wire.setStart(PointChecker.snapToGrid(new Point(startX, startY), newGSV));
            wire.setEnd(PointChecker.snapToGrid(new Point(endX, endY), newGSV));
            wire.fixMidPoint();
            wire.fixCorners();
        }
    }
}
