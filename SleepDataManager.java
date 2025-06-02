import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    //FUTURE: may want a way to filter entries by date or other criteria - might have to be in another class though
    
    // Add a new entry to the list
    public void addEntry(SleepEntries entry) {
        entries.add(entry);
        saveToFile(); // Save after adding a new entry
    }

    // Delete an entry by index (FUTURE: implement thru date?)
    public void deleteEntry(int index) {
        entries.remove(index);
        saveToFile(); // Save after deleting an entry
    }

    public double getAvgSleepForLastSevenDays() {
        if (entries.isEmpty() || entries.size() < 7) {
            return entries.get(entries.size() - 1).getTotalSleepHours();
        }
        
        double totalSleep = 0.0;
        int count = 0;
        for (int i = entries.size() - 1; i >= 0 && count < 7; i--) {
            totalSleep += entries.get(i).getTotalSleepHours();
            count++;
        }
        
        return totalSleep / Math.min(count, 7);
    }


    // Gets all entries
    public ArrayList<SleepEntries> getEntries() {
        return entries;
    }

    // Save all entries to the file
    public void saveToFile() { //Overwrites the entire file with current entries in the list 
        try {
            PrintWriter writer = new PrintWriter(dataFile);
            for (SleepEntries entry : entries) {
                writer.println(entry.toCSV());
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred with writing entries to the CSV.");
            e.printStackTrace();
        }
    }

    // Load entries from the file
    public void loadFromFile() {
        try {
            Scanner scanner = new Scanner(dataFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String date = parts[0];
                    String[] sleepTimeParts = parts[1].split(":");
                    String[] wakeTimeParts = parts[2].split(":");
                    boolean isAMST = Boolean.parseBoolean(parts[3]);
                    boolean isAMWT = Boolean.parseBoolean(parts[4]);
                    
                    int sleepTimeHour = Integer.parseInt(sleepTimeParts[0]);
                    int sleepTimeMin = Integer.parseInt(sleepTimeParts[1]);
                    int wakeTimeHour = Integer.parseInt(wakeTimeParts[0]);
                    int wakeTimeMin = Integer.parseInt(wakeTimeParts[1]);
                    
                    SleepEntries entry = new SleepEntries(date, sleepTimeHour, sleepTimeMin, wakeTimeHour, wakeTimeMin, isAMST, isAMWT);
                    entries.add(entry);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Starting with an empty list.");
        } catch (Exception e) {
            System.out.println("An error occurred while loading entries from the CSV.");
            e.printStackTrace();
        }
    }

    // Clear all entries (used maybe for a reset button)
    public void clearAllEntries() {
        entries.clear();
        saveToFile(); // Save after clearing all entries
        System.out.println("All entries cleared.");
    }
}
