package fileHandling;

import javax.swing.*;

public class FileChooser {
    public String filePath = null;
    public JFrame frame;
    public boolean open = true;
    public FileChooser() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                frame = new JFrame("Open File");

                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    System.out.println("User selected: " + chooser.getSelectedFile().getAbsolutePath());
                    filePath = chooser.getSelectedFile().getAbsolutePath();
                    frame.dispose();
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);


                while (true){
                    System.out.println("waiting for selection...");
                    if (!open) {
                        frame.setVisible(false);
                        frame.dispose();
                        break;
                    }
                }

    }

    public String getFilePath(){
        return filePath;
    }

}
