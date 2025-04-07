package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuButton extends JButton {
    BufferedImage defaultButton; // Initializes the button icons
    BufferedImage rolloverButton;
    public float sizeScale = 3;
    public Font buttonFont;
    public static final Font BUTTON_FONT = new Font("Segoe UI Black", Font.BOLD, 30);

    MenuButton(){
        buttonFont = new Font("Segoe UI Black", Font.BOLD, 30);
        try {
            defaultButton = ImageIO.read(getClass().getResource("/main/resources/menuButton.png"));
            rolloverButton = ImageIO.read(getClass().getResource("/main/resources/menuButtonRollover.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Set icons for the buttons
        ImageIcon defaultButtonIcon = new ImageIcon(defaultButton.getScaledInstance( (int) (defaultButton.getWidth() * sizeScale), (int)(defaultButton.getHeight() * sizeScale), Image.SCALE_SMOOTH));
        ImageIcon defaultButtonRolloverIcon = new ImageIcon(rolloverButton.getScaledInstance( (int) (rolloverButton.getWidth() * sizeScale), (int) (rolloverButton.getHeight() * sizeScale), Image.SCALE_SMOOTH));

        // Set dimensions for the button
        Dimension buttonSize = new Dimension(defaultButtonIcon.getIconWidth(), defaultButtonIcon.getIconHeight());


        // Set button size
        this.setPreferredSize(buttonSize);
        this.setMaximumSize(buttonSize);
        this.setMinimumSize(buttonSize);
        this.setSize(buttonSize);
        this.setBounds(0, 0, defaultButtonIcon.getIconWidth(), defaultButtonIcon.getIconHeight());

        // Set icons
        this.setIcon(defaultButtonIcon);
        this.setRolloverIcon((defaultButtonRolloverIcon));

        // Set text and font
        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.CENTER);
        this.setFont(buttonFont);


        this.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        this.setFocusable(false);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);


    }


    public void setSizeScale(float sizeScale){
        this.sizeScale = sizeScale;
        this.buttonFont = new Font("Segoe UI Black", Font.BOLD, (int) (10  * sizeScale));
    }
}
