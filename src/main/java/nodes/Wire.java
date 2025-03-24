package nodes;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Set;

public class Wire extends Placeable{
    private Point start;
    private Point end;
    private Point midPoint;
    private short ID;
    private boolean value;
    private int gridStepValue;
    private int alignment;
    private final Color WIRE_COLOR = new Color(76, 76, 76);
    private ArrayList<Placeable> outputs;

    public static final int ALIGN_TOP = 0; // This means the midPoint will take the Y coordinate of end point and the X coordinate of the start point
    public static final int ALIGN_BOTTOM = 1; // This means the midPoint will take the X coordinate of end point and the Y coordinate of the start point

    /**
     * A wire connects two nodes. One end of the wire must be connected to an input, and the other must be connected to
     * an output.
     *
     * Starting a wire at the input or output of a node, or anywhere on the path of another wire will create a output point there.
     *
     * A wire will choose an intermediate point as the vertex to draw before changing directions 90 degrees.
     * This point is decided by the direction of the wire after an initial change in the end Point.
     *
     *
     * @param start the beginning Point of the wire. This point is defined on first LEFT CLICK down
     * @param end the end Point of the wire. This point is defined on mouse LEFT CLICK release
     * @param gridStepValue the current gridStepValue. This tells the wire how to paint itself to the grid
     */
    public Wire(Point start, Point end, int gridStepValue, short ID, int alignment){
        this.start = start;
        this.end = end;
        this.gridStepValue = gridStepValue;
        this.ID = ID;
        this.alignment = alignment;
        this.outputs = new ArrayList<>();

        fixMidPoint();

    }

    public Wire() {

    }

    public void setOutputs(ArrayList<Placeable> connections){
        this.outputs = connections;
    }

    public ArrayList<Placeable> getOutputs(){
        return this.outputs;
    }

    public void addOutput(Placeable output){
        this.outputs.add(output);
        removeDuplicateConnections();
    }

    private void removeDuplicateConnections(){
        Set<Placeable> set = new java.util.HashSet<>(this.outputs);
        outputs.clear();
        outputs.addAll(set);
    }

    public void setStart(Point start){
        this.start = start;
    }

    public void setEnd(Point end){
        this.end = end;
    }

    public Point getStart(){
        return this.start;
    }

    public Point getMidPoint(){return this.midPoint;}


    public Point getEnd(){
        return this.end;
    }

    


    public ArrayList<Point> getEndPoints(){
        ArrayList<Point> points = new ArrayList<>();
        points.add(start);
        points.add(end);
        return points;
    }

    public void fixMidPoint(){
        if (alignment == ALIGN_TOP){
            int x = start.x;
            int y = end.y;
            midPoint = new Point(x, y);
        }
        else if (alignment == ALIGN_BOTTOM){
            int x = end.x;
            int y = start.y;
            midPoint = new Point(x, y);
        }
    }

    public void setAlignment(int alignment){
        this.alignment = alignment;
    }

    public int getAlignment(){
        return alignment;
    }

    /**
     * outputs the data of this wire in CSV format.
     * @return startX, startY, WIRE, ID, endX, endY, alignment, connectionID(0..n)
     */
    public String getWireData(){

        return start.x / gridStepValue + "," + start.y / gridStepValue + "," + "WIRE" + "," + ID + ","
                + end.x / gridStepValue + ',' + end.y / gridStepValue + ',' + alignment;
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
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(GATE_COLOR);
        g2d.setStroke(new BasicStroke(gridStepValue / 10f));
        g2d.drawLine(start.x, start.y, midPoint.x, midPoint.y);
        g2d.drawLine(midPoint.x, midPoint.y, end.x, end.y);
    }

    @Override
    public void paintSelected(Graphics g) {

    }

    @Override
    public Point getPos() {
        return start;
    }

    @Override
    public Point getCornerPos() {
        return end;
    }

    public void setCornerPos(Point point) {
        end = point;
        fixMidPoint();
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public void setPos(Point point) {
        this.start = point;
    }

    @Override
    public void setOutput(Placeable placeable) {
    }

    @Override
    public Placeable getOutput() {
        return null;
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
        return "WIRE";
    }

    @Override
    public Placeable copy() {
        Wire copy = new Wire();
        copy.setID(ID);
        copy.setValue(value);
        copy.setStart(new Point(start));
        copy.setEnd(new Point(end));
        copy.setGridStepValue(gridStepValue);
        copy.setAlignment(alignment);
        copy.setOutputs(outputs);
        return copy;
    }
}
