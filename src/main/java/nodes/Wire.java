package nodes;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class Wire extends Placeable{
    private Point start;
    private Point mid;
    private Point end;
    private Point topLeft;
    private Point bottomRight;
    private short ID;
    private boolean value;
    private int gridStepValue;
    private int alignment;
    private final Color WIRE_COLOR = new Color(76, 76, 76);
    private ArrayList<Placeable> outputs;
    private int scalar;

    public static final int ALIGN_TOP = 0; // This means the mid will take the Y coordinate of end point and the X coordinate of the start point
    public static final int ALIGN_BOTTOM = 1; // This means the mid will take the X coordinate of end point and the Y coordinate of the start point

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
        this.scalar = gridStepValue / 3;

        topLeft = new Point(Math.min(start.x, end.x), Math.min(start.y, end.y));
        bottomRight = new Point(Math.max(start.x, end.x), Math.max(start.y, end.y));

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
        fixMidPoint();
        fixCorners();
    }

    public Point getStart(){
        return this.start;
    }

    public Point getMid(){return this.mid;}


    public Point getEnd(){
        return this.end;
    }

    public void fixCorners(){
        topLeft.x = Math.min(start.x, end.x);
        topLeft.y = Math.min(start.y, end.y);
        bottomRight.x = Math.max(start.x, end.x);
        bottomRight.y = Math.max(start.y, end.y);
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
            mid = new Point(x, y);
        }
        else if (alignment == ALIGN_BOTTOM){
            int x = end.x;
            int y = start.y;
            mid = new Point(x, y);
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
        g2d.drawLine(start.x, start.y, mid.x, mid.y);
        g2d.drawLine(mid.x, mid.y, end.x, end.y);
    }

    @Override
    public void paintSelected(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(SELECTED_COLOR);
        g2d.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
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
        return new Rectangle(topLeft.x - scalar, topLeft.y - scalar, bottomRight.x - topLeft.x + 2 * scalar, bottomRight.y - topLeft.y + 2 * scalar);
    }

    @Override
    public void setPos(Point point) {
        int distX = point.x - this.start.x;
        int distY = point.y - this.start.y;
        this.start.translate(distX, distY);
        this.end.translate(distX, distY);
        fixMidPoint();
        fixCorners();
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
        scalar = gridStepValue / 3;
    }

    @Override
    public int getGridStepValue() {
        return gridStepValue;
    }


    /**
     * This method returns the ArrayList of connection Points for the wire. A wire is different from other nodes in that
     * it's connectionPoints are not static. Thus, they must be defined
     * @return
     */
    @Override
    public ArrayList<Point> getConnectionPoints() {
        ArrayList<Point> points = new ArrayList<>();
        if (start.x == mid.x){
            points.addAll(xSame(start, mid));
        }
        else if (start.y == mid.y){
            points.addAll(ySame(start, mid));
        }
        if (mid.x == end.x){
            points.addAll(xSame(mid,end));
        }
        else if (mid.y == end.y){
            points.addAll(ySame(mid,end));
        }
        return points;
    }

    /**
     * Returns the list of all grid Points on a line, assuming the Y coordinate of the line is constant.
     * Return all Points such the X coordinate % gsv == 0, and the X coordinate is > start or < end
     * @param start: Point at the beginning of the line
     * @param end: Point at the end of the line
     * @return: ArrayList containing all grid Points on the line
     */
    private ArrayList<Point> ySame(Point start, Point end){
        ArrayList<Point> points = new ArrayList<>();
        int n = (end.x - start.x) / this.gridStepValue; // the number of grid Points on a line
        /*
        It is possible for a line to have an end point further to the left than the start point.
        If that is the case, the distance between the two X-coordinates will be negative.
         */
        int sign = (int) Math.signum(n); // -1 if the distance is negative, 1 if it is positive
        /*
        Iterate over the range of the number of grid Points in the line.
        Starting at 0, the first grid Point will have an X-coordinate defined by the start of that line, plus the index
        of that Point in the range, multiplied with the gsv, multiplied with the sign. If the sign is negative, that
        means the Point iterates one grid square to the left and vice versa.

        The Y-coordinate will always be a constant here.
         */
        for (int i = 0; i <= n; i++){
            points.add(new Point(start.x + (i * this.gridStepValue * sign), end.y));
        }
        return points;
    }

    /**
     * Returns the list of all grid Points on a line, assuming the X coordinate of the line is constant.
     * Return all Points such the X coordinate % gsv == 0, and the Y coordinate is > start or < end
     * @param start: Point at the beginning of the line
     * @param end: Point at the end of the line
     * @return: ArrayList containing all grid Points on the line
     */
    private ArrayList<Point> xSame(Point start, Point end){
        ArrayList<Point> points = new ArrayList<>();
        int n = (end.y - start.y) / this.gridStepValue; // the number of grid Points on a line
        /*
        It is possible for a line to have an end point higher than the start point.
        If that is the case, the distance between the two Y-coordinates will be negative.
         */
        int sign = (int) Math.signum(n); // -1 if the distance is negative, 1 if it is positive
        /*
        Iterate over the range of the number of grid Points in the line.
        Starting at 0, the first grid Point will have a Y-coordinate defined by the start of that line, plus the index
        of that Point in the range, multiplied with the gsv, multiplied with the sign. If the sign is negative, that
        means the Point iterates one grid square higher and vice versa.

        The X-coordinate will always be a constant here.
         */
        for (int i = 0; i <= n; i++){
            points.add(new Point(end.x, start.y + (i * this.gridStepValue * sign)));
        }
        return points;
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
        copy.setPos(copy.getPos());
        return copy;
    }
}
