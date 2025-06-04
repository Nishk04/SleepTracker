import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class SleepDataManager {
    private ArrayList<SleepEntries> entries;
    private File dataFile;
    private String goalString;

    public SleepDataManager(String filePath) {
        entries = new ArrayList<SleepEntries>();
        dataFile = new File(filePath);
        goalString = null;
        loadFromFile(); // Load any existing entries
    }

    public void addEntry(SleepEntries entry) {
        entries.add(entry);
        saveToFile();
    }

    public void deleteEntry(int index) {
        if (index >= 0 && index < entries.size()) {
            entries.remove(index);
            saveToFile();
        }
    }

    public double getAvgSleepForLastSevenDays() {
        if (entries == null || entries.isEmpty()) {
            return 0.0;
        } else if (entries.size() < 7) {
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

    public ArrayList<SleepEntries> getEntries() {
        return entries;
    }

    public void saveToFile() {
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

    public void clearAllEntries() {
        entries.clear();
        saveToFile();
        System.out.println("All entries cleared.");
    }

    public void setGoalString(String goal) {
        this.goalString = goal;
    }

    public String getGoalString() {
        return this.goalString;
    }

    // New method: calculates streak of entries meeting the goal
    public int getStreakMatchingGoal() {
        if (goalString == null || goalString.isEmpty()) {
            return 0;
        }

        // Convert goal string (e.g. "11:00 PM") into LocalTime
        LocalTime goalTime = parseGoalTo24Hour(goalString);
        if (goalTime == null) return 0;

        int streak = 0;

        for (int i = entries.size() - 1; i >= 0; i--) {
            SleepEntries entry = entries.get(i);

            int hour = entry.getSleepTimeHour();
            if (!entry.isAMST() && hour != 12) hour += 12;
            if (entry.isAMST() && hour == 12) hour = 0;

            LocalTime entrySleep = LocalTime.of(hour, entry.getSleepTimeMin());
            if (entrySleep.isBefore(goalTime) || entrySleep.equals(goalTime)) {
                streak++;
            } else {
                break; // streak broken
            }
        }

        return streak;
    }

    private LocalTime parseGoalTo24Hour(String goal) {
        try {
            String[] parts = goal.split(" ");
            String[] timeParts = parts[0].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            boolean isPM = parts[1].equalsIgnoreCase("PM");

            if (isPM && hour != 12) hour += 12;
            if (!isPM && hour == 12) hour = 0;

            return LocalTime.of(hour, min);
        } catch (Exception e) {
            System.out.println("Invalid goal format.");
            return null;
        }
    }
}
