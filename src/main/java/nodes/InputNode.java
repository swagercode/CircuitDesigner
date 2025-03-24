package nodes;


import gui.MenuButton;

import java.awt.*;
import java.util.ArrayList;

public class InputNode extends Placeable{
    Point pos;
    Placeable output;
    int gridStepValue;
    private int textureWidth;
    private int textureHeight;
    private short ID;
    private boolean value;
    private ArrayList<Placeable> outputs;

    public InputNode(Point p, int gridStepValue, short ID, boolean inValue) {
        this.gridStepValue = gridStepValue;
        this.pos = p;
        this.ID = ID;
        this.value = inValue;
        outputs  = new ArrayList<>();
    }

    public InputNode() {

    }

    public ArrayList<Point> getOutputPoints(){
        ArrayList<Point> outputPoints = new ArrayList<>();
        //top left corner
        outputPoints.add(pos);
        //top right corner
        outputPoints.add(new Point(pos.x  + gridStepValue, pos.y));
        //bottom left corner
        outputPoints.add(new Point(pos.x, pos.y + gridStepValue));
        //bottom right corner
        outputPoints.add(new Point(pos.x  + gridStepValue, pos.y + gridStepValue));
        return outputPoints;
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

    public String getInputNodeData(){
        StringBuilder output = new StringBuilder(pos.x / gridStepValue + "," + pos.y / gridStepValue + "," + "WIRE" + "," + ID);
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
        textureWidth = gridStepValue;
        textureHeight = gridStepValue;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(FILL_COLOR);
        g2d.fillRect(pos.x, pos.y, textureWidth, textureHeight);

        g2d.setStroke(new BasicStroke(gridStepValue / 5f));
        g2d.setColor(GATE_COLOR);
        g2d.drawRect(pos.x, pos.y, textureWidth, textureHeight);

        g2d.setFont(MenuButton.BUTTON_FONT.deriveFont(Font.PLAIN, (int)(gridStepValue * 0.85)));
        int textOffsetX = gridStepValue / 4;
        int textOffsetY = (int)(gridStepValue * 0.8);

        if(value) {
            g2d.drawString("1", pos.x + textOffsetX, pos.y + textOffsetY);
        }
        else {g2d.drawString("0", pos.x + textOffsetX, pos.y + textOffsetY);}
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
    public String getType() {
        return "INPUT";
    }

    @Override
    public Placeable copy() {
        InputNode copy = new InputNode();
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
