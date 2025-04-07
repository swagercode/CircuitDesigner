package nodes.gates;


import gui.MenuButton;
import nodes.Placeable;

import java.awt.*;
import java.util.ArrayList;

public class LightNode extends Placeable {
    Point pos;
    Placeable output;
    int gridStepValue;
    private int textureWidth;
    private int textureHeight;
    private short ID;
    private boolean value;
    private ArrayList<Placeable> outputs;

    public LightNode(Point p, int gridStepValue, short ID) {
        this.gridStepValue = gridStepValue;
        this.pos = p;
        this.ID = ID;
        outputs  = new ArrayList<>();
    }

    public LightNode() {

    }

    public void addOutput(Placeable placeable){
        outputs.add(placeable);
    }

    public ArrayList<Placeable> getOutputs(){
        return outputs;
    }


    public void setValue(boolean inValue) {
        value = inValue;
    }

    public boolean getValue() {
        return value;
    }

    public String getOutputNodeData(){
        StringBuilder output = new StringBuilder(pos.x / gridStepValue + "," + pos.y / gridStepValue + "," + "LIGHT" + "," + ID);
        for (Placeable p : outputs){
            output.append(",").append(p.getID());
        }
        return output.toString();
    }

    @Override
    public void setID(short id) {
        ID = id;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(gridStepValue / 5f));
        g2d.setColor(GATE_COLOR);
        g2d.drawRect(pos.x, pos.y, textureWidth, textureHeight);
        g2d.setColor(FILL_COLOR);
        g2d.fillRect(pos.x, pos.y, textureWidth, textureHeight);
    }

    @Override
    public void paintSelected(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(SELECTED_COLOR);
        g2d.setStroke(new BasicStroke(gridStepValue / 15f));
        g2d.drawRect(pos.x, pos.y, textureWidth, textureHeight);
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
    public ArrayList<Point> getConnectionPoints() {
        ArrayList<Point> points = new ArrayList<>();
        //top left corner
        points.add(pos);
        //top right corner
        points.add(new Point(pos.x  + gridStepValue, pos.y));
        //bottom left corner
        points.add(new Point(pos.x, pos.y + gridStepValue));
        //bottom right corner
        points.add(new Point(pos.x  + gridStepValue, pos.y + gridStepValue));
        return points;
    }

    @Override
    public String getType() {
        return "INPUT";
    }

    @Override
    public Placeable copy() {
        LightNode copy = new LightNode();
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
}
