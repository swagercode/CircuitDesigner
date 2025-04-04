import com.opencsv.exceptions.CsvException;
import fileHandling.FileSaver;
import gui.MainMenu;

import java.io.File;
import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException, CsvException {
//        new MainMenu(); // TODO: optimize background image array loading
        String path = System.getProperty("user.dir") + "/circuits/amazing3.txt";
        FileSaver.openCircuit(new File(path));


    }


}