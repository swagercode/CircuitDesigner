package nodes;

import java.awt.*;
import java.util.Objects;
import java.util.Set;

public class NotGate extends Gate { // TODO: fix all
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

    public NotGate(Point p, int gridStepValue, short ID) {
        this.gridStepValue = gridStepValue;
        this.pos = p;
        this.ID = ID;
        bottomInputPoint = new Point(pos.x, pos.y + gridStepValue * 2);
        outputPoint = new Point(pos.x + 3 * gridStepValue, pos.y + gridStepValue);
        inputPoints = Set.of(pos, bottomInputPoint);
    }

    public NotGate() {

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

    @Override
    public void paint(Graphics g) {
        /**
         * paints the AND gate TODO: fix the drawing
         */
        int overhangY = Math.round(gridStepValue / 3f);
        textureWidth = gridStepValue * 3;
        textureHeight = gridStepValue * 2;

        // Draw the head of the NOT gate
        ((Graphics2D) g).setStroke(new BasicStroke(gridStepValue / 5f));

        int[] xPoints = {pos.x + gridStepValue, pos.x + gridStepValue, pos.x + gridStepValue * 2};
        int[] yPoints = {pos.y, pos.y + textureHeight, pos.y + gridStepValue};
        g.setColor(FILL_COLOR);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(GATE_COLOR);
        g.drawPolygon(xPoints, yPoints, 3);


        // Draw the two input lines
        ((Graphics2D) g).setStroke(new BasicStroke(gridStepValue / 10f));
        g.drawLine(pos.x, pos.y, pos.x + gridStepValue, pos.y);
        g.drawLine(pos.x, pos.y + 2 * gridStepValue, pos.x + gridStepValue, pos.y + 2 * gridStepValue);

        // Draw the output line
        g.drawLine(pos.x + 2 * gridStepValue, pos.y + gridStepValue, pos.x + 3 * gridStepValue, pos.y + gridStepValue );





    }

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
        bottomInputPoint = new Point(pos.x + gridStepValue, pos.y + gridStepValue * 2);
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
        return "NOT";
    }

    @Override
    public Placeable copy() {
        NotGate copy = new NotGate();
        copy.setID(this.ID);
        copy.setValue(this.value);
        copy.setPos(new Point(this.pos));
        copy.setGridStepValue(this.gridStepValue);
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
