package nodes.gates;

import nodes.Placeable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class OrGate extends Gate {
    Point pos;
    Placeable output;
    int gridStepValue;
    private int textureWidth;
    private int textureHeight;
    private short ID;
    private boolean value;
    private Point bottomInputPoint;
    private Point outputPoint;
    private Set<Point> inputPoints;

    public OrGate(Point p, int gridStepValue, short ID) {
        this.gridStepValue = gridStepValue;
        this.pos = p;
        this.ID = ID;
        bottomInputPoint = new Point(pos.x, pos.y + gridStepValue * 2);
        outputPoint = new Point(pos.x + 3 * gridStepValue, pos.y + gridStepValue);
        inputPoints = Set.of(pos, bottomInputPoint);
    }

    public OrGate() {

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
        Graphics2D g2d = (Graphics2D) g;



        g2d.setStroke(new BasicStroke(gridStepValue / 5f));

        int diameter = Math.round(textureWidth * 1.5f);
        int radius = diameter / 2;
        int x = pos.x - radius;
        int y = pos.y - radius;

//        g.drawOval(x, y, diameter, diameter);
//        g.drawOval(x, y + textureHeight, diameter, diameter);
//        g.drawOval(x - gridStepValue, y + gridStepValue, diameter, diameter);

        // Fill arc this is basically trial and error of putting ovals so that they cover the
        // space and make the gate opaque. It's a weird shape so this may be necessary
        g2d.setColor(FILL_COLOR);

        //center
        int ezTweakX = (int)(getPos().x + gridStepValue * 1.2);
        g2d.fillOval(ezTweakX, getPos().y + gridStepValue / 2, (int)(gridStepValue * .8), gridStepValue);

        // center sides
        int ezTweakW = (int)(gridStepValue * .6);
        g2d.fillOval(ezTweakX, getPos().y + gridStepValue / 3, ezTweakW, diameter / 7);
        g2d.fillOval(ezTweakX, getPos().y + gridStepValue, ezTweakW, diameter / 7);

        // side sides
        int ezTweakH = (int)(gridStepValue / 2);
        ezTweakW = (int)(gridStepValue * .4);
        ezTweakX = (int)(getPos().x + gridStepValue * 1.1);
        int ezTweakY = (int)(getPos().y + gridStepValue * .1);
        int ezTweakY2 = (int)(getPos().y + gridStepValue * 1.3);
        g2d.fillOval(ezTweakX, ezTweakY, ezTweakW, ezTweakH);
        g2d.fillOval(ezTweakX, ezTweakY2, ezTweakW, ezTweakH);
        // Arc filling done


        // Draw the OR gate
        g2d.setColor(GATE_COLOR);
        int startAngle = 27;
        int changeAngle = 36;
        g2d.drawArc(x, y, diameter, diameter, -startAngle, -changeAngle);
        g2d.drawArc(x, y + textureHeight, diameter, diameter, startAngle, changeAngle);
        int bottomAngle = -27;
        g2d.setStroke(new BasicStroke(gridStepValue / 5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g2d.drawArc(x - gridStepValue, y + gridStepValue, diameter, diameter, bottomAngle, -bottomAngle * 2);


        // Draw the two input lines
        g2d.setStroke(new BasicStroke(gridStepValue / 10f));
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
        bottomInputPoint = new Point(pos.x, pos.y + gridStepValue * 2);
        outputPoint = new Point(pos.x + 3 * gridStepValue, pos.y + gridStepValue);
    }

    @Override
    public void setOutput(Placeable placeable) {
        output = placeable;
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
        return "OR";
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
    public Placeable copy() {
        OrGate copy = new OrGate();
        copy.setID(this.ID);
        copy.setValue(this.value);
        copy.setPos(new Point(this.pos));
        copy.setGridStepValue(this.gridStepValue);
        return copy;
    }

    @Override
    public Placeable getOutput(){
        return output;
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
