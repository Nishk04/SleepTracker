import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class SleepDataManager {
    private ArrayList<SleepEntries> entries;
    private File dataFile;
    
    public SleepDataManager(String filePath) {
        entries = new ArrayList<SleepEntries>();
        dataFile = new File(filePath);
    }
}
