package gui;

import nodes.Placeable;
import nodes.Wire;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class GridDrawer {
    public GridDrawer(){}

    public static void drawGrid(int gridStepValue, Graphics2D g, int width, int height, int radScale, Color gridColor){
        g.setStroke(new BasicStroke(2));
        g.setColor(gridColor);
        // Draw the grid
        for (int i = 0; i <= width; i += gridStepValue) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i <= height; i += gridStepValue) {
            g.drawLine(0, i, width, i);
        }

        // Draws a small circle on each intersection of lines. AKA on every step of the gridStepValue
        int dynRad = gridStepValue / radScale; // dynamic radius
        // iterate over the X axis
        for (int x = 0; x <= width; x += gridStepValue) {
            // iterate over the Y axis
            for (int y = 0; y <= height; y += gridStepValue) {

                // draw the circles. Ovals are drawn from the corner, so the x and y values need to be offset
                // by the radScale to get the circle centered on the point
                g.fillOval(x - dynRad / 2, y - dynRad / 2, dynRad, dynRad);
            }
        }

    }

    public static void drawConnectionPoints(Graphics2D g, int gsv, int radScale, ArrayList<Point> connectionPoints, Color connectionColor){
        // call the paint method of each placed node, except for the grabbed node which is painted separately

        int dynRad = gsv / radScale; // dynamic radScale
        g.setColor(connectionColor);
        for (Point point : connectionPoints) {
            if (Objects.isNull(point)) continue;
            g.fillOval(point.x - dynRad / 2, point.y - dynRad / 2, dynRad, dynRad);
        }


                /* Paint a rectangle around the grabbed node. Also paint it separately so that it doesn't snap to the
                grid til grabbedNode is released.
                 */

    }

    public static void drawNodes(ArrayList<Placeable> nodes, Graphics2D g, Placeable grabbedNode){
        for (Placeable node : nodes) {
            if (!node.equals(grabbedNode)) {
                node.paint(g);
            }
        }
    }

    public static void drawFloaters(Wire newWire, Placeable grabbedNode, Graphics2D g){
        if (!Objects.isNull(grabbedNode)) {
            grabbedNode.paint(g);
            grabbedNode.paintSelected(g);
        }

        if (!Objects.isNull(newWire)){
            newWire.paint(g);
        }
    }
}
