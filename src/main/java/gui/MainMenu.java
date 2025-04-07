package gui;

import com.opencsv.exceptions.CsvException;
import connections.PointChecker;
import fileHandling.FileSaver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


public class MainMenu extends JFrame implements ActionListener, KeyListener {

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static String circuitName;
    public static BufferedImage titleImage;
    public static BufferedImage iconImage;
    private File outFile;
    private final ArrayList<BufferedImage> backgroundArray;
    private final Timer frameTimer;
    private final JLayeredPane layeredPane;
    private final MenuButton newCircuitButton;
    private final MenuButton openCircuitButton;
    private final MenuButton settingsButton;
    private MenuButton createButton;
    private MenuButton cancelButton;
    private JPanel fieldPanel;
    private JTextField buttonField;


    /**
    This is the main menu for the whole program. It opens the frame asking the user to create a new circuit,
    or open a new one. When a circuit is opened or created, the main menu closes and
    the CircuitCreationSceneUI() is called, taking in the user selected file.
     */
    public MainMenu() throws IOException {

        // Preloads all images into array
        int i = 0;
        backgroundArray = new ArrayList<>();
        while (i <= 199) { // 199
            if (getClass().getResource("/main/resources/backGroundArray/ezgif-frame-" + (i + 1) + ".png") != null) { // TODO: convert to jpeg
                BufferedImage im = ImageIO.read(Objects.requireNonNull(getClass().getResource("/main/resources/backGroundArray/ezgif-frame-" + (i + 1) + ".png")));
                backgroundArray.add(im);
                i++;
            } else {
                System.out.println("Image " + i + " has not been loaded successfully.");
                break;
            }
        }
        System.out.println("Background array has been loaded successfully.");

        // Instantiate and start frame timer
        frameTimer = new Timer(20, this);
        frameTimer.start();


        // Import title image to titleIcon

        try {
            titleImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/main/resources/title.png")));
            titleImage = ImageHandler.resize(titleImage, (int) (titleImage.getWidth() * 2f), (int) (titleImage.getHeight() * 2f));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ImageIcon titleIcon = new ImageIcon(titleImage);


        try {
            iconImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/main/resources/icon.png")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(iconImage);


        // Create main pane
        JPanel mainPain = new JPanel();

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        // Create layout manager for palette panel with 1 column and a 20 pixel gap between buttons
        GridLayout gridLayout = new GridLayout(0, 1, 0, 20);

        // Create top and center panels, put them into the palette panel
        JPanel palettePanel = new JPanel(); // Make a panel to contain the other panels in palette layer
        palettePanel.setOpaque(false);
        palettePanel.setSize(SCREEN_SIZE);
        palettePanel.setMaximumSize(SCREEN_SIZE);
        palettePanel.setLayout(gridLayout);

        // Create top panel
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setBounds(0, 50, 275, 100);
        // Set size
        topPanel.setMinimumSize(new Dimension(SCREEN_SIZE.width / 6, SCREEN_SIZE.height / 6));
        topPanel.setMaximumSize(new Dimension(SCREEN_SIZE.width / 6, SCREEN_SIZE.height / 6));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // Create center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        //Set size
        centerPanel.setMinimumSize(new Dimension(SCREEN_SIZE.width / 6, SCREEN_SIZE.height / 6));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        // Set center panel layout to box layout
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));


        // Add components to top panel
        JLabel title = new JLabel(); // Title Label
        title.setIcon(titleIcon);
        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut((int) SCREEN_SIZE.getHeight() / 4));

        // Create Buttons
        int sizeScale = 3;

        newCircuitButton = new MenuButton();
        newCircuitButton.setText("NEW CIRCUIT");
        newCircuitButton.setSizeScale(sizeScale);
        newCircuitButton.addActionListener(this);

        openCircuitButton = new MenuButton();
        openCircuitButton.setText("OPEN CIRCUIT");
        openCircuitButton.setSizeScale(sizeScale);
        openCircuitButton.addActionListener(this);

        settingsButton = new MenuButton();
        settingsButton.setText("SETTINGS");
        settingsButton.setSizeScale(sizeScale);
        settingsButton.addActionListener(this);

        // Add components to center panel
        centerPanel.add(newCircuitButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(openCircuitButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(settingsButton);


        // Add components to palette panel
        palettePanel.add(topPanel, BorderLayout.NORTH);
        palettePanel.add(centerPanel, BorderLayout.CENTER);


        // Create background panel and animate background
        // initialize frame counter
        // Draw next frame of the background each timer tick
        // increment the frame counter by 1 and once it reaches the end frame, restart
        JPanel backgroundPanel = getJPanel();


        // Add components to layered pane
        layeredPane.add(palettePanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.setPreferredSize(new Dimension((int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight()));
        layeredPane.setMaximumSize(new Dimension((int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight()));
        layeredPane.setMinimumSize(new Dimension((int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight()));
        layeredPane.setLayout(null);

        mainPain.add(layeredPane);


        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setContentPane(mainPain);
        this.setVisible(true);
        this.setTitle("Circuit Designer - Day Eleven!");
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);


    }

    private JPanel getJPanel() {
        JPanel backgroundPanel = new JPanel() {

            // initialize frame counter
            private short i = 0;

            @Override
            protected void paintComponent(Graphics g) { // Draw next frame of the background each timer tick
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(backgroundArray.get(i), 0, 0, null);

                // increment the frame counter by 1 and once it reaches the end frame, restart
                i++;
                if (i == backgroundArray.size() - 1) {
                    i = 0;
                }
            }
        };
        backgroundPanel.setOpaque(false);
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setBounds(0, 0, (int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight());
        return backgroundPanel;
    }

    /**
     * Opens the menu for creating a new circuit
     */
    public void newCircuitButtonPressed() {
        // Gray out the other menu buttons when menu pops up
        newCircuitButton.setEnabled(false);
        openCircuitButton.setEnabled(false);
        settingsButton.setEnabled(false);


        // Create the panel to hold the text field and label
        fieldPanel = new JPanel();
        fieldPanel.setOpaque(true);
        fieldPanel.setLayout(new FlowLayout());
        fieldPanel.setBackground(Color.LIGHT_GRAY);
        fieldPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        fieldPanel.setBounds(new Rectangle(SCREEN_SIZE.width / 4, SCREEN_SIZE.height / 4, SCREEN_SIZE.width / 2, SCREEN_SIZE.height / 5));

        // Create label to tell the user to type a file name
        JLabel fieldLabel = new JLabel();
        fieldLabel.setText("Enter Circuit Name Here: ");
        fieldLabel.setFont(MenuButton.BUTTON_FONT);
        fieldLabel.setOpaque(false);
        fieldLabel.setBackground(Color.GRAY);
        fieldLabel.setBounds(new Rectangle(100, 20));


        // Create text field for file name
        buttonField = new JTextField("", 20);
        buttonField.addKeyListener(this);
        buttonField.setFont(MenuButton.BUTTON_FONT);
        buttonField.setOpaque(true);
        buttonField.setBackground(Color.LIGHT_GRAY);
        buttonField.setEditable(true);


        // Create save and cancel buttons
        createButton = new MenuButton();
        createButton.setText("CREATE");
        createButton.addActionListener(this);
        createButton.setSizeScale(3f);

        cancelButton = new MenuButton();
        cancelButton.setText("CANCEL");
        cancelButton.setSizeScale(3f);
        cancelButton.addActionListener(this);


        // Add buttons and label to the pop-up
        fieldPanel.add(fieldLabel);
        fieldPanel.add(buttonField);
        fieldPanel.add(createButton);
        fieldPanel.add(cancelButton);

        layeredPane.add(fieldPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.moveToFront(fieldPanel);
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Opens the file chooser for a previously created or imported circuit when called
      */
    public void openCircuitButtonPressed() throws IOException, CsvException {
        // Gray out the other menu buttons when menu pops up
        newCircuitButton.setEnabled(false);
        openCircuitButton.setEnabled(false);
        settingsButton.setEnabled(false);

        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        // Open a file chooser and assign the outFile with the selected file
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/circuits/"));
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            circuitName = chooser.getSelectedFile().getAbsolutePath();
            outFile = chooser.getSelectedFile();
        }
        // if canceled, return everything to normal
        else if (option == JFileChooser.CANCEL_OPTION) {
            newCircuitButton.setEnabled(true);
            openCircuitButton.setEnabled(true);
            settingsButton.setEnabled(true);
            return;
        }

        FileSaver.openCircuit(outFile);
        dispose();
        frameTimer.stop();
        }

    /**
     * Creates the new circuit with the input file name
     */
    public void createCircuit() throws IOException {

        // Initialize a String to the users file directory, put it in the circuits folder,
        // concatenate it with the button field text
        String path = System.getProperty("user.dir") + "/circuits/" + buttonField.getText().replaceAll(" ", "") + ".txt";

        // Assign outFile to the file found in that filepath
        outFile = new File(path);

        // Change the scene
        dispose();
        frameTimer.stop();
        CircuitCreationSceneUI newScene = new CircuitCreationSceneUI(buttonField.getText(), null, 100, outFile, (short) 0, new PointChecker());
        newScene.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Tick the background on each tick of the timer
        if(e.getSource() == frameTimer){
            repaint();
        }

        // Open circuit creation menu on "NEW CIRCUIT" button press
        else if(e.getSource() == newCircuitButton){

            newCircuitButtonPressed();

        }

        // Create the circuit on "CREATE" or ENTER pressed. this switches to the next scene as seen in createCircuit()
        else if (e.getSource() == createButton)
        {
            try {
                createCircuit();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Open the file chooser to open a previous project
        else if (e.getSource() == openCircuitButton)
        {
            try {
                openCircuitButtonPressed();
            } catch (CsvException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Cancel button closes the circuit creation menu
        else if (e.getSource() == cancelButton)
        {
            // remove the text field
            layeredPane.remove(fieldPanel);

            // Re-enable all the menu buttons
            newCircuitButton.setEnabled(true);
            openCircuitButton.setEnabled(true);
            settingsButton.setEnabled(true);

            // Repaint and revalidate
            layeredPane.repaint();
            layeredPane.revalidate();
        }

    }

    /**
     * not yet used
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * On ENTER pressed (new circuit menu must be open) calls createCircuit(), closing the main menu and opening the CircuitCreationSceneUI
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Creates the circuit in createCircuit() method if enter is pressed during file creation menu
        if((e.getKeyCode() == KeyEvent.VK_ENTER) && (buttonField != null))
            try {
                createCircuit();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    } // Empty

} // End of class



