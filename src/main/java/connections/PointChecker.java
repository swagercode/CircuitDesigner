package connections;

import nodes.*;

import java.awt.*;
import java.util.*;

/**
 * PointChecker is a class with the primary purpose of finding connection points between each node. A HashMap stores Points as keys and an ArrayList of Placeables as values.
 */
public class PointChecker {

    HashMap<Point, Set<Placeable>> conMap;

    public PointChecker(HashMap<Point, Set<Placeable>> conMap){
        this.conMap = conMap;
    }

    public PointChecker(){
        this.conMap = new HashMap<>();
    }

    public HashMap<Point, Set<Placeable>> getConMap() {
        return conMap;
    }

    public void setConMap(HashMap<Point, Set<Placeable>> conMap) {
        this.conMap = conMap;
    }

    /**
     * This method removes a node from all ArrayLists it is contained in. If after being removed, a connection list
     * has a length size less than 2, there are no connections. Thus, the entry is removed from the Map.
     * @param node: Placeable to be removed from all connections
     * @param nodes: ArrayList of all Placeables in the currently open file
     */
    public void nodeRemoveAllConnections(Placeable node, ArrayList<Placeable> nodes){
        if (nodes.isEmpty() || Objects.isNull(node)){ // if there are no nodes, it's impossible for there to be any connections.
            conMap.clear(); // clear the map of connections
            return;
        }

        if (!conMap.isEmpty()) { // can't remove connections from an empty map
            ArrayList<Point> remove = new ArrayList<>(); // list of keys to be removed
            for (Map.Entry<Point, Set<Placeable>> entry : conMap.entrySet()) { // iterate through each key value pair
                entry.getValue().remove(node); // remove node from value ArrayList
                if (entry.getValue().size() < 2){ // Point has no connections the associated list has size < 2
                    remove.add(entry.getKey());
                }
            }
            for (Point p : remove){
                conMap.remove(p); // remove entries from the Map
            }
        }
    }

    /**
     * This method loops through all nodes in the currently opened file, and removes Entries from the HashMap if the value
     * has a size < 2. This method should never
     */
    public void removeNonConnections() {
        for (Map.Entry<Point, Set<Placeable>> entry : conMap.entrySet()) {
            if (entry.getValue().size() < 2) {
                conMap.remove(entry.getKey());
                throw new RuntimeException("There should never be a Point with a size < 2 in the HashMap.");
            }
        }
    }

    /**
     * This method checks the connections between a singular input node and every other node on the plane.
     * If there are any connections, add the newNode to ArrayList associated with that Point
     * If no Point key already exists, create a new Entry with the Point and ArrayList containing both the current node
     * and the newNode
     * @param newNode: Placeable node that has just been created or moved
     * @param nodes: ArrayList of all Placeables in the currently open file
     */
    public void pointNodeCheck(Placeable newNode, ArrayList<Placeable> nodes){
        if (nodes.isEmpty() || Objects.isNull(newNode)) { // no connections possible if there are no other nodes
            conMap.clear();
            return;
        }

        for (Point p : newNode.getConnectionPoints()){ // iterate over each connection point on the newNode
            for (Placeable node : nodes){ // iterate over each node in the list of all nodes
                if (node.equals(newNode)) {
                    throw new RuntimeException("Grabbed node should not be in the list of all nodes until dropped. " +
                            "\n    Check that the grabbed node is being removed from the list of all nodes before calling this method.");
                }

                if (landsOnNode(p, node)){ //TODO: if a wire is connected to a node before being moved, move that wire with the node
                    if (newNode instanceof Wire w1 && node instanceof Wire w2){
                        if (!wireToWire(w1, w2, p)){ //TODO: weird edge case where if you draw the end point of a wire onto another wire rather than the start point, it doesnt draw a connection point
                            continue;
                        }
                    }
                    if (conMap.containsKey(p)){
                        conMap.get(p).add(newNode);
                    }
                    else{
                        Set<Placeable> newList = new HashSet<>();
                        newList.add(newNode);
                        newList.add(node);
                        conMap.put(p, newList);
                    }
                }
            }
        }
    }



    // == Algorithm ==
    // new node is dropped/completed: if it's an old node being moved, pass in a boolean true, else pass in false
    // for every possible input/output point on that node
    // check if the input or output point of another node lands on it
    // if the node bounds do not intersect, continue

    // if yes, create a pointNode with the newNode and the current node
    // if the pointNode already exists, add the current node to the pointNode

    // if oldPoints is not null
    // for old points:
    // for each PointNode containing newNode, remove newNode from the list
    // then recheck from newNode new position


    /**
     * iterate over each node and return the node whose bounds the point lands on. If no node exists to fit that requirement,
     * null is returned
     * @param nodes ArrayList of all Placeable nodes
     * @param p given Point
     * @return node whose bounds Point p lands on, otherwise null
     */
    public static Placeable nodesBoundsCheck(ArrayList<Placeable> nodes, Point p){
        if (nodes.isEmpty() || Objects.isNull(p)) return null;
        for (Placeable node : nodes) {
            // Check if the mouse cursor is within the bounds of the node. If so, assign grabbedNode with that node
            if (node.getBounds().contains(p)) {
                return node;
            }
        }
        return null;
    }

    public static boolean landsOnNode(Point p, Placeable node){
        for (Point point : node.getConnectionPoints()){
            if (point.equals(p)){
                return true;
            }
        }
        return false;
    }

    public static boolean wireToWire(Wire w1, Wire w2, Point p){
        if (w2.getEndPoints().contains(p)) return true;
        else {
            for (Point point : w2.getConnectionPoints()) {
                if (w1.getEndPoints().contains(point)) return true;
            }
        }
        return false;
    }

    public static Point randPointNotNode(ArrayList<Placeable> nodes, int gridStepValue){
        int x = (int)(Math.random() * (gridStepValue * 10));
        int y = (int)(Math.random() * (gridStepValue * 10));
        Point p = snapToGrid(new Point(x, y), gridStepValue);
        for (Placeable node : nodes) {
            if (node.getBounds().contains(p)) continue;
            return p;
        }
        throw new RuntimeException("No valid point found. This should never happen. Please report this bug to the developer.");
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
