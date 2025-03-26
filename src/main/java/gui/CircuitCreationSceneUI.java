package gui;

import connections.PointChecker;
import fileHandling.FileSaver;
import nodes.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class CircuitCreationSceneUI extends JFrame implements ActionListener {
    public final Color BG_COLOR = new Color(244, 244, 249);
    public final Color GRID_COLOR = new Color(199, 199, 199, 255);
    public final Color SIDE_PANEL_COLOR = new Color(88, 111, 124);
    public final Color CONNECTION_COLOR = new Color(100, 127, 213);

    public int gridStepValue;
    public ArrayList<Placeable> nodes;

    private final ArrayList<Point> connectionPoints;

    private Point prevPt;

    private BufferedImage iconImage;

    private JButton ANDButton;
    private JButton ORButton;
    private JButton NOTButton;
    private JButton XORButton;
    private JPanel mainPane;
    private JPanel sidePanel;
    private JPanel createPanel;
    private JButton inputButton;
    private JButton lightButton;
    private JButton segmentButton;
    private JSlider scaleSlider;
    private JButton saveAndExitButton;
    private JButton wireButton;
    private JButton panButton;
    private JPanel topPanel;
    private JButton selectButton;

    private final File inFile;

    private int radius;

    // Each node is given a unique ID upon creation
    private short ID;


    public CircuitCreationSceneUI(String fileName, ArrayList<Placeable> inNodes, int inGridStep, File in, short inID) {

        ID = inID;

        connectionPoints = new ArrayList<>();


        // assign the inFile if a file is given in the constructor (from a previously saved file)

        inFile = Objects.requireNonNull(in);

        // TODO: remove (debugging)
        System.out.println(inFile.getAbsolutePath());

        // if a gridStepValue of 0 is given in the constructor, the default of 100 is used
        if (inGridStep != 0) {
            this.gridStepValue = inGridStep;
        } else gridStepValue = 100;

        // inNodes is only given when loading a previously saved file, otherwise it should be null
        // If inNodes is not given, assign the nodes ArrayList with a new, empty ArrayList
        nodes = Objects.requireNonNullElse(inNodes, new ArrayList<>());
        checkConnections();
        System.out.println(connectionPoints);


        // Set up labels and buttons
        {

            ANDButton.setFont(MenuButton.BUTTON_FONT);
            ANDButton.setOpaque(true);
            ANDButton.setBackground(Color.LIGHT_GRAY);
            ANDButton.addActionListener(this);
            ANDButton.setFocusable(false);


            ORButton.setFont(MenuButton.BUTTON_FONT);
            ORButton.setOpaque(true);
            ORButton.setBackground(Color.LIGHT_GRAY);
            ORButton.addActionListener(this);
            ORButton.setFocusable(false);

            NOTButton.setFont(MenuButton.BUTTON_FONT);
            NOTButton.setOpaque(true);
            NOTButton.setBackground(Color.LIGHT_GRAY);
            NOTButton.addActionListener(this);
            NOTButton.setFocusable(false);

            XORButton.setFont(MenuButton.BUTTON_FONT);
            XORButton.setOpaque(true);
            XORButton.setBackground(Color.LIGHT_GRAY);
            XORButton.addActionListener(this);
            XORButton.setFocusable(false);

            inputButton.setFont(MenuButton.BUTTON_FONT);
            inputButton.setOpaque(true);
            inputButton.setBackground(Color.LIGHT_GRAY);
            inputButton.addActionListener(this);
            inputButton.setFocusable(false);

            lightButton.setFont(MenuButton.BUTTON_FONT);
            lightButton.setOpaque(true);
            lightButton.setBackground(Color.LIGHT_GRAY);
            lightButton.addActionListener(this);
            lightButton.setFocusable(false);

            segmentButton.setFont(MenuButton.BUTTON_FONT);
            segmentButton.setOpaque(true);
            segmentButton.setBackground(Color.LIGHT_GRAY);
            segmentButton.addActionListener(this);
            segmentButton.setFocusable(false);

            saveAndExitButton.setFont(MenuButton.BUTTON_FONT);
            saveAndExitButton.setOpaque(true);
            saveAndExitButton.setBackground(Color.LIGHT_GRAY);
            saveAndExitButton.addActionListener(this);
            saveAndExitButton.setFocusable(false);

            wireButton.setFont(MenuButton.BUTTON_FONT);
            wireButton.setOpaque(true);
            wireButton.setBackground(Color.LIGHT_GRAY);
            wireButton.addActionListener(this);
            wireButton.setFocusable(false);

            panButton.setFont(MenuButton.BUTTON_FONT);
            panButton.setOpaque(true);
            panButton.setBackground(Color.LIGHT_GRAY);
            panButton.addActionListener(this);
            panButton.setFocusable(false);

            selectButton.setFont(MenuButton.BUTTON_FONT);
            selectButton.setOpaque(true);
            selectButton.setBackground(Color.LIGHT_GRAY);
            selectButton.addActionListener(this);
            selectButton.setFocusable(false);
        }

        // The scale slider is used for changing the gridStepValue. As the gridStepValue is changed, the entire
        // grid and all components scale with it.
        // sets the minimum gridStepValue to be 10 pixels
        scaleSlider.setMinimum(10);
        scaleSlider.setValue(gridStepValue);
        //set the custom UI
        scaleSlider.setUI(new BasicSliderUI(scaleSlider) {
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height);
            }

            public void paintThumb(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
                g2.setColor(Color.GRAY);
                g2.drawRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }
        });
        scaleSlider.addChangeListener(e -> {
            // store the current gridStepValue for scaling the node positions later on
            int lastGSV = gridStepValue;

            // set the gridStepValue to the slider position. This is 10 at minimum and 100 at maximum
            gridStepValue = scaleSlider.getValue();

            // Scales the position of the nodes on screen with the new GSV
            for (Placeable node : nodes) {
                if (node instanceof Gate || node instanceof InputNode) {
                    node.setGridStepValue(gridStepValue);
                    int newX = (int) ((node.getPos().getX() / lastGSV) * gridStepValue);
                    int newY = (int) ((node.getPos().getY() / lastGSV) * gridStepValue);
                    node.setPos(Placeable.snapToGrid(new Point(newX, newY), gridStepValue)); // TODO: might be bad to create objects in a for loop like this??
                }
                else if (node instanceof Wire wire){
                    wire.setGridStepValue(gridStepValue);
                    int startX = (int) ((wire.getPos().getX() / lastGSV) * gridStepValue);
                    int startY = (int) ((wire.getPos().getY() / lastGSV) * gridStepValue);
                    int endX = (int) ((wire.getCornerPos().getX() / lastGSV) * gridStepValue);
                    int endY = (int) ((wire.getCornerPos().getY() / lastGSV) * gridStepValue);
                    wire.setPos(Placeable.snapToGrid(new Point(startX, startY), gridStepValue));
                    wire.setCornerPos(Placeable.snapToGrid(new Point(endX, endY), gridStepValue));
                }

            }

            for (Point point : connectionPoints) {
                int newX = (int) ((point.getX() / lastGSV) * gridStepValue);
                int newY = (int) ((point.getY() / lastGSV) * gridStepValue);
                point.setLocation(new Point(newX, newY));
            }
            repaint();
        }); // Scale slider initialization end

        // Set the background of the top panel
        topPanel.setBackground(BG_COLOR);

        // Set the background color of the side panel
        sidePanel.setBackground(SIDE_PANEL_COLOR);

        // Set the background color of the circuit creation panel
        createPanel.setBackground(BG_COLOR);

        // Try to get the icon image
        try {
            iconImage = (ImageIO.read(Objects.requireNonNull(getClass().getResource("/main/resources/icon.png"))));
        } catch (IOException ex) {
            System.out.println("Failed to get the icon image.");
            ex.printStackTrace();
        }

        if (fileName.contains(".")){
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        // Set up and start the frame
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setTitle("Circuit Designer - Day Eleven! Currently Designing: " + fileName);
        this.setIconImage(iconImage);
        this.setSize(MainMenu.SCREEN_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setContentPane(mainPane);
        this.setVisible(true);
    }

    public void menuOrClosePopup(){
        JFrame popup = new JFrame();

        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(BG_COLOR);
        iconPanel.setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setBackground(BG_COLOR);
        questionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JLabel question = new JLabel("Go back to Main Menu?");
        question.setFont(MenuButton.BUTTON_FONT);
        question.setOpaque(true);
        question.setBackground(BG_COLOR);
        question.setHorizontalAlignment(JLabel.CENTER);
        iconPanel.add(question, BorderLayout.CENTER);


        MenuButton yesButton = new MenuButton();
        yesButton.setText("Yes");
        yesButton.addActionListener(e -> {
            try {
                FileSaver.saveCircuit(nodes, connectionPoints, gridStepValue, inFile);
                new MainMenu();
                this.dispose();
                popup.dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        questionPanel.add(yesButton);

        MenuButton noButton = new MenuButton();
        noButton.setText("No");
        noButton.addActionListener(e -> {
            try {
                FileSaver.saveCircuit(nodes, connectionPoints, gridStepValue, inFile);
                System.exit(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        questionPanel.add(noButton);

        popup.setLayout(new BorderLayout());
        popup.add(iconPanel, BorderLayout.NORTH);
        popup.add(questionPanel, BorderLayout.CENTER);
        popup.setTitle("Circuit Designer - Day Eleven!");
        popup.setIconImage(MainMenu.iconImage);
        popup.setBackground(SIDE_PANEL_COLOR);
        popup.pack();
        popup.setResizable(false);
        popup.setVisible(true);
        popup.setLocationRelativeTo(null);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }




    @Override
    public void actionPerformed(ActionEvent e) {

        Point defaultPoint = new Point(5 * gridStepValue, 5 * gridStepValue);
        // Depending on the button pressed, create a new node of that type and add it to the ArrayList of nodes.
        // These are the nodes seen on screen and saved into file storage
        if (e.getSource() == ANDButton) {
            nodes.addFirst(new AndGate(defaultPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == ORButton) {
            nodes.addFirst(new OrGate(defaultPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == NOTButton) {
            nodes.addFirst(new NotGate(defaultPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == XORButton) {
            nodes.addFirst(new XorGate(defaultPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == inputButton) {
            nodes.addFirst(new InputNode(defaultPoint, gridStepValue, ID++, true));
            repaint();
        } else if (e.getSource() == lightButton) {
            //TODO
        } else if (e.getSource() == segmentButton) {
            //TODO
        }

        // These top panel buttons decide what mode of creation the program is in, drawing wires, panning (NOT YET IMPLEMENTED)
        // or select (normal mode). Depending on the mode, the cursor is changed for the createPanel and certain
        // functions are disabled or enabled, such as in wire drawing mode, where moving nodes around is disabled,
        // and drawing wires is enabled.
        // In order for other functions to know what mode the program is in, they get the cursor type using:
        // createPanel.getCursor().getType();
        else if (e.getSource() == wireButton) {
            createPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            grabbedNode = null;
            isDragging = false;
            repaint();
        }
        else if (e.getSource() == panButton) {
            createPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            grabbedNode = null;
            isDragging = false;
            repaint();
        }
        else if (e.getSource() == selectButton) {
            createPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        /* When the Save and Exit button is pressed, the saveCircuit() function is called which saves all the nodes data,
        the gridStepValue, into the inFile. inFile should have
        */
        else if (e.getSource() == saveAndExitButton) {
            menuOrClosePopup();
        }

    }


    private final int pointScale = 5;
    private void createUIComponents() {
        // Create the createPanel with custom paintComponent
        createPanel = new JPanel() {


            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                GridDrawer.drawGrid(gridStepValue, g2d, createPanel.getWidth(), createPanel.getHeight(), pointScale, GRID_COLOR);
                GridDrawer.drawNodes(nodes, g2d, grabbedNode);
                GridDrawer.drawFloaters(newWire, grabbedNode, g2d);
                GridDrawer.drawConnectionPoints(g2d, gridStepValue, pointScale, connectionPoints, CONNECTION_COLOR);
            }
        };// End createPanel creation


        // Add and initialize the mouse listeners for dragging and dropping
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        createPanel.addMouseListener(clickListener);
        createPanel.addMouseMotionListener(dragListener);
    }
    int wireCount = 0;


    /**
     * clear all the connection points off the screen, then check for new connection points and add them to the ArrayList
     */
    public void checkConnections(){ //TODO
        connectionPoints.clear();

        // check each node in nodes for connections
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                System.out.println("Checking pair: " + nodes.get(i).getType() + " -> " + nodes.get(j).getType());
                Point result = PointChecker.check(nodes.get(i), nodes.get(j));
                if (result == null) {
                    System.out.println("No connection found between " + nodes.get(i).getType() + " and " + nodes.get(j).getType());
                } else {
                    System.out.println("Found connection point: " + result);
                    connectionPoints.add(result);
                }

            }
        }
        System.out.println("DEBUGGING CONNECTIONS: " + connectionPoints);
    }

    /** Initialize the grabbedNode as null, a node that is assigned to the node clicked and dragged by the mouse.
    it is re-assigned to null when the mouse is released.
     */
    private Placeable grabbedNode = null;
    private Wire newWire = null;
    private boolean isDragging = false;

    private class ClickListener extends MouseAdapter {

        /**
         * Invoked when left click is pressed
         */
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                // prevPt is the initial position of the mouse when the mouse is clicked
                prevPt = e.getPoint();

                // IF IN SELECT MODE
                // iterate over each node in the nodes ArrayList
                if (createPanel.getCursor().getType() == Cursor.DEFAULT_CURSOR){
                    for (Placeable node : nodes) {
                        if (node instanceof Gate || node instanceof InputNode) {

                            // Check if the mouse cursor is within the bounds of the node. If so, assign grabbedNode with that node
                            if (node.getBounds().contains(prevPt)) {
                                grabbedNode = node;
                                isDragging = true;
                                repaint();
                                break;
                            }
                            else {
                                grabbedNode = null;
                                isDragging = false;
                                repaint();
                            }
                        }
                    }
                }

                // IF IN WIRE MODE
                else if (createPanel.getCursor().getType() == Cursor.CROSSHAIR_CURSOR){
                    newWire = new Wire(Placeable.snapToGrid(prevPt, gridStepValue), prevPt, gridStepValue, ID++, Wire.ALIGN_TOP);
                    isDragging = true;
                    repaint();
                }


            } // TODO: when grabbedNode is released as another is instantly picked up, the previous node does not snap to the grid

            else if (SwingUtilities.isRightMouseButton(e)){
                ContextMenu menu = new ContextMenu();
                menu.show(createPanel, e.getX(), e.getY());

            }
        }
        /**
         * Execute the mouse is released
         * @param e the event to be processed
         */
        public void mouseReleased(MouseEvent e) {

            /*
            Invoke on left mouse released
             */

            if (e.getButton() == MouseEvent.BUTTON1) {


                if (createPanel.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
                    // check if grabbedNode is already null, if it is, break out of the function call
                    if (grabbedNode == null) return;
                    isDragging = false;
                    // snap the grabbedNode to the grid once it's done being dragged
                    grabbedNode.setPos(Placeable.snapToGrid(grabbedNode.getPos(), gridStepValue));

                    checkConnections();

                    // repaint to reflect the changes
                    repaint();
                }
                else if (createPanel.getCursor().getType() == Cursor.CROSSHAIR_CURSOR){ // n , i , c, 3a, r, g, u TODO: remove
                    newWire.setEnd(Placeable.snapToGrid(newWire.getEnd(), gridStepValue));
                    newWire.fixMidPoint();
                    nodes.add(newWire);
                    newWire = null;
                    isDragging = false;
                    checkConnections();
                    repaint();
                }
            }
        }
    }

    private class DragListener extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            Point currentPt = e.getPoint();

            if (isDragging) {
                if (createPanel.getCursor().getType() == Cursor.CROSSHAIR_CURSOR) {

                    if (newWire != null) {
                        Point p = newWire.getEnd();
                        p.translate(
                                (int) (currentPt.getX() - prevPt.getX()),
                                (int) (currentPt.getY() - prevPt.getY())
                        );
                        newWire.setEnd(p);
                        repaint();
                    }

                } else if (createPanel.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
                    // check if mouse is in bounds of any node currently placed. if it is, sets the position of that node to the nearest valid grid space
                    if (grabbedNode != null) {
                        // get the current position of the placeable node
                        Point p = grabbedNode.getPos();

                        // translate the temporary point to the mouse position
                        p.translate(
                                (int) (currentPt.getX() - prevPt.getX()),
                                (int) (currentPt.getY() - prevPt.getY())
                        );


                        // sets the position to the nearest grid spot
                        grabbedNode.setPos(p);
                    }

                    // the current point becomes the previous point, yielding a smooth change in value
                    prevPt = currentPt;
                }
            }

            // repaint to reflect changes
            repaint();
        }
    }

    private class ContextMenu extends JPopupMenu{

        JMenuItem deleteItem;
        JMenuItem duplicateItem;
        JMenuItem switchValue;

        public ContextMenu(){
            deleteItem = new JMenuItem("Delete");
            deleteItem.addActionListener(e -> {
                nodes.remove(grabbedNode);
                grabbedNode = null;
                isDragging = false;
                createPanel.repaint();
            });

            duplicateItem = new JMenuItem("Duplicate");
            duplicateItem.addActionListener(e -> {
                Placeable newNode = grabbedNode.copy();
                Point newPos = new Point(gridStepValue * 5, gridStepValue * 5);
                newNode.setPos(newPos);
                nodes.add(newNode);
                createPanel.repaint();
            });

            switchValue = new JMenuItem("Switch Value");
            switchValue.addActionListener(e -> {
               if (grabbedNode instanceof InputNode inNode){
                   inNode.setValue(!inNode.getValue());
                   createPanel.repaint();
               }
            });

            this.add(deleteItem);
            this.add(duplicateItem);
            this.add(switchValue);
        }

    }
}