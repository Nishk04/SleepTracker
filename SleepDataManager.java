import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class SleepDataManager {
    private ArrayList<SleepEntries> entries;
    private File dataFile;
    private String goalString;

    public SleepDataManager(String filePath) {
        entries = new ArrayList<>();
        dataFile = new File(filePath);
        goalString = null;
        loadFromFile();
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

    public ArrayList<SleepEntries> getEntries() {
        return entries;
    }

    public ArrayList<SleepEntries> getLastSevenEntries() {
        ArrayList<SleepEntries> last7 = new ArrayList<>();
        int start = Math.max(0, entries.size() - 7);
        for (int i = start; i < entries.size(); i++) {
            last7.add(entries.get(i));
        }
        return last7;
    }

    public double getAvgSleepForLastSevenDays() {
        ArrayList<SleepEntries> last7 = getLastSevenEntries();
        if (last7.isEmpty()) return 0.0;
        double total = 0;
        for (SleepEntries e : last7) {
            total += e.getTotalSleepHours();
        }
        return total / last7.size();
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(dataFile)) {
            for (SleepEntries entry : entries) {
                writer.println(entry.toCSV());
            }
            System.out.println("Successfully saved data.");
        } catch (IOException e) {
            System.out.println("Error saving file.");
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        if (!dataFile.exists()) return;
        try (Scanner scanner = new Scanner(dataFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String date = parts[0];
                    String[] sleepTime = parts[1].split(":");
                    String[] wakeTime = parts[2].split(":");

                    int sth = Integer.parseInt(sleepTime[0]);
                    int stm = Integer.parseInt(sleepTime[1]);
                    int wth = Integer.parseInt(wakeTime[0]);
                    int wtm = Integer.parseInt(wakeTime[1]);

                    boolean isAMST = Boolean.parseBoolean(parts[3]);
                    boolean isAMWT = Boolean.parseBoolean(parts[4]);

                    SleepEntries entry = new SleepEntries(date, sth, stm, wth, wtm, isAMST, isAMWT);
                    entries.add(entry);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file.");
            e.printStackTrace();
        }
    }

    public void clearAllEntries() {
        entries.clear();
        saveToFile();
    }

    public void setGoalString(String goal) {
        this.goalString = goal;
    }

    public String getGoalString() {
        return goalString;
    }

    public int getStreakMatchingGoal() {
        if (goalString == null || goalString.isEmpty()) return 0;
        LocalTime goalTime = parseGoalTo24Hour(goalString);
        if (goalTime == null) return 0;

        int streak = 0;
        for (int i = entries.size() - 1; i >= 0; i--) {
            SleepEntries e = entries.get(i);
            int hour = e.getSleepTimeHour();
            if (!e.isAMST() && hour != 12) hour += 12;
            if (e.isAMST() && hour == 12) hour = 0;

            LocalTime entryTime = LocalTime.of(hour, e.getSleepTimeMin());
            if (!entryTime.isAfter(goalTime)) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    private LocalTime parseGoalTo24Hour(String goal) {
        try {
            String[] parts = goal.split(" ");
            String[] time = parts[0].split(":");
            int hour = Integer.parseInt(time[0]);
            int min = Integer.parseInt(time[1]);
            boolean isPM = parts[1].equalsIgnoreCase("PM");
            if (isPM && hour != 12) hour += 12;
            if (!isPM && hour == 12) hour = 0;
            return LocalTime.of(hour, min);
        } catch (Exception e) {
            return null;
        }
    }

    // Returns a score out of 100 based on how close the average is to 8 hours
    // If the average deviation is 90 minutes or more, the score is 0.
    // If all sleep times are exactly the same, the score is 100.
    public int calculateSleepScore() {
        double avg = getAvgSleepForLastSevenDays();
        if (avg >= 8) return 100;
        return (int) ((avg / 8.0) * 100);  // e.g., 6.5 hours → 81
    }

    // Returns a score out of 100 based on how consistent the sleep start time is
    public int calculateConsistencyScore() {
        ArrayList<SleepEntries> last7 = getLastSevenEntries();
        if (last7.size() < 2) return 50;

        // Convert each sleep time to minutes since midnight
        ArrayList<Integer> times = new ArrayList<>();
        for (SleepEntries e : last7) {
            int hour = e.getSleepTimeHour();
            int min = e.getSleepTimeMin();
            if (!e.isAMST() && hour != 12) hour += 12;
            if (e.isAMST() && hour == 12) hour = 0;
            int totalMinutes = hour * 60 + min;
            times.add(totalMinutes);
        }

        // Calculate the average deviation from the median time
        times.sort(Integer::compareTo);
        int median = times.get(times.size() / 2);
        double deviation = 0;
        for (int t : times) {
            deviation += Math.abs(t - median);
        }
        deviation /= times.size();

        // Max expected deviation we tolerate is 90 minutes
        double normalized = Math.max(0, 100 - (deviation / 90.0) * 100);
        return (int) normalized;
    }

}
