package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

// this code belongs to https://stackoverflow.com/users/1917206/alex-orzechowski

public class ImageHandler {
    public ImageHandler(){}

    public static BufferedImage resize(BufferedImage image, int newWidth, int newHeight) {
        Image tmp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage drawnImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = drawnImg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return drawnImg;
    }


}

