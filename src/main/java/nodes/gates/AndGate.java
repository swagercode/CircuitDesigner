package nodes.gates;

import nodes.Placeable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class AndGate extends Gate {
    Point pos;
    Placeable connection;
    int gridStepValue;
    private int textureWidth;
    private int textureHeight;
    private short ID;
    private boolean value;
    private Point bottomInputPoint;
    private Point outputPoint;
    private Set<Point> inputPoints;

    public AndGate(Point p, int gridStepValue, short id) {
        this.gridStepValue = gridStepValue;
        this.pos = p;
        this.ID = id;
        outputPoint = new Point(pos.x + 3 * gridStepValue, pos.y + gridStepValue);
        bottomInputPoint = new Point(pos.x, pos.y + gridStepValue * 2);
        inputPoints = Set.of(pos, bottomInputPoint);
    }

    public AndGate() {
    }

    @Override
    public ArrayList<Point> getConnectionPoints() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(pos);
        points.add(bottomInputPoint);
        points.add(outputPoint);
        return points;
    }


    @Override
    public void setID(short id) {
        this.ID = id;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getValue() {
        return value;
    }

    /**
     * paints the AND gate with the Graphics arc. This is the second simplest of all the gate designs
     */
    @Override
    public void paint(Graphics g) {

        int overhangY = Math.round(gridStepValue / 3f);
        textureWidth = gridStepValue * 3;
        textureHeight = gridStepValue * 2;

        ((Graphics2D) g).setStroke(new BasicStroke(gridStepValue / 10f));






        // Debugging


        g.setColor(FILL_COLOR);
        // Draw the head of the AND gate
        ((Graphics2D) g).setStroke(new BasicStroke(gridStepValue / 5f));
        g.fillArc(pos.x, pos.y - overhangY, gridStepValue * 2, 2 * overhangY + gridStepValue * 2, 90, -180); // TODO: allow rotation by changing the angle. Can be rotated by top toolbar or by right click menu
        g.setColor(GATE_COLOR);
        g.drawArc(pos.x, pos.y - overhangY, gridStepValue * 2, 2 * overhangY + gridStepValue * 2, 90, -180); // TODO: allow rotation by changing the angle. Can be rotated by top toolbar or by right click menu
        g.drawLine(pos.x + gridStepValue, pos.y - overhangY, pos.x + gridStepValue, pos.y + overhangY + gridStepValue * 2);

        // Draw the two input lines
        ((Graphics2D) g).setStroke(new BasicStroke(gridStepValue / 10f));
        g.drawLine(pos.x, pos.y, pos.x + gridStepValue, pos.y);
        g.drawLine(pos.x, pos.y + 2 * gridStepValue, pos.x + gridStepValue, pos.y + 2 * gridStepValue);

        // Draw the output line
        g.drawLine(pos.x + 2 * gridStepValue, pos.y + gridStepValue, pos.x + 3 * gridStepValue, pos.y + gridStepValue );




    }


    /**
     * paints a box around the node when run. Used for when the node is selected with the mouse
     */
    @Override
    public void paintSelected(Graphics g) {
        g.setColor(SELECTED_COLOR);
        ((Graphics2D) g).setStroke(new BasicStroke(gridStepValue / 15f));
        g.drawRect(getPos().x - gridStepValue / 5, getPos().y - gridStepValue / 5, getCornerPos().x - getPos().x + gridStepValue / 5 * 2, getCornerPos().y - getPos().y  + gridStepValue / 5 * 2);
    }

    @Override
    public Point getPos() {
        return pos;
    }

    @Override
    public Point getCornerPos() {
        // gets the corner of the whole texture
        int cornerX = pos.x + textureWidth;
        int cornerY = pos.y + textureHeight;
        return new Point(cornerX, cornerY);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pos.x, pos.y, textureWidth, textureHeight);
    }

    @Override
    public void setPos(Point newPos) {
        this.pos = newPos;
        bottomInputPoint = new Point(pos.x, pos.y + gridStepValue * 2);
        outputPoint = new Point(pos.x + 3 * gridStepValue, pos.y + gridStepValue);
    }

    @Override
    public void setOutput(Placeable placeable) {
        connection = placeable;
    }

    @Override
    public void setGridStepValue(int gridStepValue) {
        this.gridStepValue = gridStepValue;
    }

    @Override
    public int getGridStepValue() {
        return gridStepValue;
    }

    @Override
    public String getType() {
        return "AND";
    }

    @Override
    public Placeable copy() {
        // Create a shallow copy or deep copy of the AndGate instance
        AndGate copy = new AndGate();
        copy.setID(this.ID);
        copy.setValue(this.value);
        copy.setPos(new Point(this.pos)); // Deep copy the position
        copy.setGridStepValue(this.gridStepValue);
        copy.setOutput(this.connection);
        // Connection is not cloned to avoid recursive copying.
        return copy;

    }

    @Override
    public Placeable getOutput(){
        return connection;
    }


    @Override
    public Point getBottomInputPoint() {
        return bottomInputPoint;
    }

    @Override
    public Point getOutputPoint() {
        return outputPoint;
    }

    @Override
    public Set<Point> getInputPoints() {
        return inputPoints;
    }
}
