package nodes;

import gui.CircuitCreationSceneUI;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Placeable{
    /**
     * nodes.Placeable is the structure for the Node class
     * @param g
     */

    public final Color GATE_COLOR = new Color(76, 76, 76);
    public final Color FILL_COLOR = new Color(227, 227, 227);
    public final Color SELECTED_COLOR = new Color(196, 248, 253);

    public abstract void setID(short id);

    public abstract int getID();

    public abstract void setValue(boolean value);

    public abstract boolean getValue();

    public abstract void paint(Graphics g);

    public abstract void paintSelected(Graphics g);

    public abstract Point getPos();

    public abstract Point getCornerPos();

    public abstract Rectangle getBounds();

    public abstract void setPos(Point point);

    public abstract void setOutput(Placeable placeable);

    public abstract Placeable getOutput();

    public abstract void setGridStepValue(int gridStepValue);

    public abstract int getGridStepValue();

    /**
     * return the type of the node
     */
    public abstract String getType();

    public abstract Placeable copy();

    /**
     * Return the data of the node in a CSV format
     * Example: 7,5,XOR,0,null:
     *          x,y,TYPE,ID,CONNECTION_ID
     * @param p
     * @param gridStepValue
     */
    public static String getDefaultData(Placeable p, int gridStepValue){
            return p.getPos().x / gridStepValue + "," + p.getPos().y / gridStepValue + "," + p.getType() + "," + p.getID() + "," + (p.getOutput() == null ? null : p.getOutput().getID());
    }

    public static Point snapToGrid(Point pos, int gridStepValue) {

            int newX;
            if ((pos.x % gridStepValue < (int)(gridStepValue / 2f))) { // round down to the nearest valid X position for values 14 or lower
                newX = pos.x - pos.x % gridStepValue;
            } else { // round up to the nearest valid X position for values 15 or greater
                newX = pos.x + (gridStepValue - pos.x % gridStepValue);
            }
            int newY;
            if ((pos.y % gridStepValue < (int)(gridStepValue / 2f))) {
                newY = pos.y - pos.y % gridStepValue;
            } else {
                newY = pos.y + (gridStepValue - pos.y % gridStepValue);
            }

            return new Point(newX, newY);
    }


}







