package fileHandling;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import connections.PointChecker;
import gui.CircuitCreationSceneUI;
import nodes.*;
import nodes.gates.AndGate;
import nodes.gates.NotGate;
import nodes.gates.OrGate;
import nodes.gates.XorGate;

import java.awt.*;
import java.io.*;
import java.util.*;

public class FileSaver {
    public static BufferedWriter writer;
    public FileSaver() {}

    /**
     * Reads each line from the inFile. The first line is an integer assigned to the gridStepValue.
     * The following lines are all the positional, type, ID, and connection ID of each gate.
     * Each gate has its own line.
     * @param inFile: the file to be opened
     */

    public static void openCircuit(File inFile) throws IOException, CsvException {
        // set the file name
        String circuitName = inFile.getName();

        // read the inFile to a csv reader
        CSVReader reader = new CSVReader(new FileReader(inFile));

        // make arraylist of the items to be rendered
        ArrayList<Placeable> circuitData = new ArrayList<>();

        // PointChecker for the connectionMap
        PointChecker pc = new PointChecker();

        // get the saved gridStepValue from the file
        int gridStep;
        gridStep = Integer.parseInt(reader.readNext()[0]);

        // make a string array of each line
        String[] lineData;
        short maxID = 0;

        // instantiate variables outside loop
        int x;
        int y;
        short id;
        Point pos;
        int currentID = 0;
        Point endPos;

        while ((lineData = reader.readNext()) != null) {
            //debug
            System.out.println("LINE DATA: " +
                    lineData[0] + ' ' +  lineData[1] + ' ' + lineData[2] + ' ' + lineData[3]);

            // get the absolute x and y values of the current node
            x = Integer.parseInt(lineData[0]);
            y = Integer.parseInt(lineData[1]);
            pos = new Point(x * gridStep, y * gridStep);
            id = Short.parseShort(lineData[3]);
            maxID = (short) Math.max(maxID, id);
            int alignment;

            // for each different type of node, make a new gate of that type with the previously defined variables. then add
            // that gate to the ArrayList
            switch (lineData[2]) {
                case "NOT" -> {
                    NotGate notGate = new NotGate(pos, gridStep, id);
                    //notGate.setConnections(lineData[4].equals("null") ? null : circuitData.get(Integer.parseInt(lineData[4])));
                    circuitData.add(notGate);
                    //debug
                    System.out.println("Gate added");

                }
                case "OR" -> {
                    OrGate orGate = new OrGate(pos, gridStep, id);
                    //orGate.setConnections(lineData[4].equals("null") ? null : circuitData.get(Integer.parseInt(lineData[4])));
                    circuitData.add(orGate);
                    System.out.println("Gate added");
                }
                case "XOR" -> {
                    XorGate xorGate = new XorGate(pos, gridStep, id);
                    //xorGate.setConnections(lineData[4].equals("null") ? null : circuitData.get(Integer.parseInt(lineData[4])));
                    circuitData.add(xorGate);
                    System.out.println("Gate added");
                }
                case "AND" -> {
                    AndGate andGate = new AndGate(pos, gridStep, id);
                    //andGate.setConnections(lineData[4].equals("null") ? null : circuitData.get(Integer.parseInt(lineData[4])));
                    circuitData.add(andGate);
                    System.out.println("Gate added");
                }
                case "WIRE" -> {
                    System.out.println(Arrays.toString(lineData));
                    x = Integer.parseInt(lineData[4]);
                    y = Integer.parseInt(lineData[5]);
                    alignment = Integer.parseInt(lineData[6]);
                    endPos = new Point(x * gridStep, y * gridStep);
                    Wire wire = new Wire(pos, endPos, gridStep, id, alignment);
                    circuitData.add(wire);
                    System.out.println("Wire added");
                }
                case "INPUT" -> {
                    boolean value = Boolean.parseBoolean(lineData[5]);
                    InputNode inputNode = new InputNode(pos, gridStep, id, value);
                    circuitData.add(inputNode);
                    System.out.println("InputNode added" + value);
                }
                case "CON" -> {
                    Set<Placeable> value = new HashSet<>();
                    int conX = Integer.parseInt(lineData[0]) * gridStep;
                    int conY = Integer.parseInt(lineData[1]) * gridStep;
                    for (int i = 3; i < lineData.length; i++) {
                        try {
                            currentID = Integer.parseInt(lineData[i]);
                        }
                        catch (NumberFormatException e){
                            System.out.println("Error occurred when attempting to get linData[" + i + "] as an integer");
                        }
                        value.add(circuitData.get(currentID));
                    }
                    pc.addPoint(new Point(conX, conY), value); // TODO: initial connections disappear after first check
                    System.out.println("Connection added");
                }
            }

        }

        System.out.println("File opened:" + reader.getClass().getName());
        new CircuitCreationSceneUI(circuitName, circuitData, gridStep, inFile, maxID, pc);
    }

    /**
     * OVERWRITES the circuit data to the currently opened file.
     * First contains only the gridStepValue.
     * Each succeeding line contains the data from each gate
     *
     * @param circuitData
     * An ArrayList containing all placed objects on screen
     * @param gridStepValue
     * gridStepValue of the scene when the user saved
     * @param inFile
     * File to be written to
     */
    public static void saveCircuit(ArrayList<Placeable> circuitData, int gridStepValue, HashMap<Point, Set<Placeable>> conMap, File inFile) throws IOException{ // TODO: change so it only edits changed values
        if (circuitData.isEmpty()) return;
        writer = new BufferedWriter(new FileWriter(inFile));

        // Write the gridStepValue on the first line
        writer.write(Integer.toString(gridStepValue));
        writer.newLine();

        // Write the data for each node line by line
        for (Placeable placeable : circuitData) {
            if (placeable instanceof Wire wire){
                writer.write(wire.getWireData());
                writer.newLine();
            }
            else if (placeable instanceof InputNode inputNode){
                writer.write(inputNode.getInputNodeData());
                writer.newLine();
            }
            else {
                writer.write(Placeable.getDefaultData(placeable, gridStepValue));
                writer.newLine();
            }
        }

        // Write the data for each connection point
        System.out.println(conMap.size());
        for (Point point : conMap.keySet()) {
            writer.write(point.x / gridStepValue + "," + point.y / gridStepValue + ",CON");

            for (Placeable node : conMap.get(point)) {
                writer.write("," + node.getID());
                System.out.println("ID for " + node.getType() + ": " + node.getID());
            }
            writer.newLine();
        }
        writer.close();
    }
}
