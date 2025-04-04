package gui;

import connections.PointChecker;
import fileHandling.FileSaver;
import nodes.*;
import nodes.gates.*;

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
    public final Color BG_COLOR = new Color(252, 250, 249);
    public final Color GRID_COLOR = new Color(190, 190, 190, 255);
    public final Color SIDE_PANEL_COLOR = new Color(222, 219, 217);
    public final Color CONNECTION_COLOR = new Color(243, 211, 189);

    public int gridStepValue;
    public ArrayList<Placeable> nodes;

    private final ArrayList<Point> connectionPoints;

    private final PointChecker pc = new PointChecker();

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

    // Each node is given a unique ID upon creation
    private short ID;


    public CircuitCreationSceneUI(String fileName, ArrayList<Placeable> inNodes, int inGridStep, File in, short inID) {

        ID = inID;

        connectionPoints = new ArrayList<>();


        // assign the inFile if a file is given in the constructor (from a previously saved file)

        inFile = Objects.requireNonNull(in);

        // if a gridStepValue of 0 is given in the constructor, the default of 100 is used
        if (inGridStep != 0) {
            this.gridStepValue = inGridStep;
        } else gridStepValue = 100;

        // inNodes is only given when loading a previously saved file, otherwise it should be null
        // If inNodes is not given, assign the nodes ArrayList with a new, empty ArrayList
        nodes = Objects.requireNonNullElse(inNodes, new ArrayList<>());
        pc.pointNodeCheck(grabbedNode, nodes); // TODO: fix initial check of all nodes at once


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
            int lastGSV = gridStepValue;
            gridStepValue = scaleSlider.getValue();
            GridDrawer.gridUpdate(lastGSV, gridStepValue, nodes, grabbedNode, pc);
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

    private Point spawnPoint = new Point(5 * gridStepValue, 5 * gridStepValue);
    @Override
    public void actionPerformed(ActionEvent e) {
        // Depending on the button pressed, create a new node of that type and add it to the ArrayList of nodes.
        // These are the nodes seen on screen and saved into file storage
        if (e.getSource() == ANDButton || e.getSource() == ORButton || e.getSource() == NOTButton || e.getSource() == XORButton && nodes != null && !nodes.isEmpty()) {
            spawnPoint = PointChecker.randPointNotNode(nodes, gridStepValue);
        }

        if (e.getSource() == ANDButton) {
            nodes.addFirst(new AndGate(spawnPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == ORButton) {
            nodes.addFirst(new OrGate(spawnPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == NOTButton) {
            nodes.addFirst(new NotGate(spawnPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == XORButton) {
            nodes.addFirst(new XorGate(spawnPoint, gridStepValue, ID++));
            repaint();
        } else if (e.getSource() == inputButton) {
            nodes.addFirst(new InputNode(spawnPoint, gridStepValue, ID++, true));
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

    private final int pointScale = 5; // this is the scale of the grid points (GSV / pointScale) TODO: settings
    private final int lineScale = 10; // this is for the grid lines

    private void createUIComponents() {
        // Create the createPanel with custom paintComponent
        createPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                GridDrawer.drawGrid(gridStepValue, g2d, createPanel.getWidth(), createPanel.getHeight(), pointScale, lineScale, GRID_COLOR);
                GridDrawer.drawNodes(nodes, grabbedNode, g2d);
//                GridDrawer.drawFloater(grabbedNode, g2d);
                GridDrawer.drawConnectionPoints(g2d, gridStepValue, pointScale, pc.getConMap().keySet(), CONNECTION_COLOR);
            }
        };


        // Add and initialize the mouse listeners for dragging, dropping, and scrolling
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        createPanel.addMouseListener(clickListener);
        createPanel.addMouseMotionListener(dragListener);
        createPanel.addMouseWheelListener(e -> {
            scaleSlider.setValue(scaleSlider.getValue() + 2 * e.getWheelRotation());

        });
    }

    /** Initialize the grabbedNode as null, a node that is assigned to the node clicked and dragged by the mouse.
    it is re-assigned to null when the mouse is released.
     */
    private Placeable grabbedNode = null;
    private boolean isDragging = false;
    private final ContextMenu menu = new ContextMenu();
    private class ClickListener extends MouseAdapter {

        /**
         * Invoked when left click is pressed
         */
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                // prevPt is the initial position of the mouse when the mouse is clicked
                prevPt = e.getPoint();

                //  == IF IN SELECT MODE ==
                // checks if the mouse is over any of the nodes, and grabs that node if there is one
                if (createPanel.getCursor().getType() == Cursor.DEFAULT_CURSOR){
                    if (grabbedNode != null) {
                        nodes.add(grabbedNode);
                    }
                    grabbedNode = PointChecker.nodesBoundsCheck(nodes, prevPt);
                    isDragging = grabbedNode != null;
                    if (isDragging){
                        nodes.remove(grabbedNode);
                        pc.nodeRemoveAllConnections(grabbedNode, nodes);
                    }

                    repaint();

                }


                // == IF IN WIRE MODE ==
                else if (createPanel.getCursor().getType() == Cursor.CROSSHAIR_CURSOR){
                    grabbedNode = new Wire(PointChecker.snapToGrid(prevPt, gridStepValue), prevPt, gridStepValue, ID++, Wire.ALIGN_TOP);
                    isDragging = true;
                    repaint();
                }


            } // TODO: when grabbedNode is released as another is instantly picked up, the previous node does not snap to the grid

            else if (SwingUtilities.isRightMouseButton(e)){
                grabbedNode = PointChecker.nodesBoundsCheck(nodes, e.getPoint());
                if (grabbedNode != null){
                    nodes.remove(grabbedNode);
                    pc.nodeRemoveAllConnections(grabbedNode, nodes);
                }
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


                // == SELECT MODE ==
                if (createPanel.getCursor().getType() == Cursor.DEFAULT_CURSOR && !Objects.isNull(grabbedNode)){
                    grabbedNode.setPos(PointChecker.snapToGrid(grabbedNode.getPos(), gridStepValue));
                    pc.pointNodeCheck(grabbedNode, nodes);
                    nodes.add(grabbedNode);
                    grabbedNode = null;
                    isDragging = false;
                    repaint();
                }

                // == WIRE MODE ==
                else if (createPanel.getCursor().getType() == Cursor.CROSSHAIR_CURSOR && !Objects.isNull(grabbedNode) && grabbedNode instanceof Wire w){ // n , i , c, 3a, r, g, u TODO: remove
                    w.setEnd(PointChecker.snapToGrid(w.getEnd(), gridStepValue));
                    w.fixMidPoint();
                    if (!w.getEnd().equals(w.getStart())) {
                        pc.pointNodeCheck(grabbedNode, nodes);
                        nodes.add(w);
                    }
                    grabbedNode = null;
                    isDragging = false;
                    repaint();
                }
            }
        }
    }

    private class DragListener extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            Point currentPt = e.getPoint();

            if (isDragging) {
                // == WIRE MODE ==
                if (createPanel.getCursor().getType() == Cursor.CROSSHAIR_CURSOR && grabbedNode instanceof Wire newWire) {
                    // translate the end point by the mouse movement
                    newWire.getEnd().translate(
                            (int) (currentPt.getX() - prevPt.getX()),
                            (int) (currentPt.getY() - prevPt.getY())
                    );
                    repaint();
                }

                // == SELECT MODE ==
                else if (createPanel.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
                    // check if mouse is in bounds of any node currently placed. if it is, sets the position of that node to the nearest valid grid space
                    if (grabbedNode != null) {

                        // DRAGGING WIRE
                        if (grabbedNode instanceof Wire w) {
                            w.getStart().translate(
                                    (int) (currentPt.getX() - prevPt.getX()),
                                    (int) (currentPt.getY() - prevPt.getY())
                            ); // translate the start and end of the wire by the mouse movement
                            w.getEnd().translate(
                                    (int) (currentPt.getX() - prevPt.getX()),
                                    (int) (currentPt.getY() - prevPt.getY())
                            );
                            w.fixMidPoint();
                            w.fixCorners();
                        }

                        // DRAGGING NORMAL NODE
                        else {
                            // translate the temporary point to the mouse position
                            grabbedNode.getPos().translate(
                                    (int) (currentPt.getX() - prevPt.getX()),
                                    (int) (currentPt.getY() - prevPt.getY())
                            );

                        }

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