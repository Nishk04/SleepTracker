public class SleepEntries {
    private String date;
    private int sleepTimeHour, sleepTimeMin, wakeTimeHour, wakeTimeMin;
    private boolean isAMST; // sleep time AM
    private boolean isAMWT; // wake time AM

    public SleepEntries(String date, int sleepHour, int sleepMin, int wakeHour, int wakeMin, boolean isAMST, boolean isAMWT) {
        this.date = date;
        this.sleepTimeHour = sleepHour;
        this.sleepTimeMin = sleepMin;
        this.wakeTimeHour = wakeHour;
        this.wakeTimeMin = wakeMin;
        this.isAMST = isAMST;
        this.isAMWT = isAMWT;
    }

    public int getSleepTimeHour() {
        return sleepTimeHour;
    }

    public int getSleepTimeMin() {
        return sleepTimeMin;
    }

    public boolean isAMST() {
        return isAMST;
    }

    public boolean isAMWT() {
        return isAMWT;
    }

    public String getDate() {
        return date;
    }

    public double getTotalSleepHours() {
        return getSleepDurationMins() / 60.0;
    }

    public int getTotalSleepMinsWithoutHours() {
        return getSleepDurationMins() % 60;
    }

    public int getSleepDurationMins() {
        int startHour24 = convertTo24Hour(sleepTimeHour, isAMST);
        int endHour24 = convertTo24Hour(wakeTimeHour, isAMWT);

        int startTotalMins = startHour24 * 60 + sleepTimeMin;
        int endTotalMins = endHour24 * 60 + wakeTimeMin;

        if (endTotalMins < startTotalMins) {
            // crossed midnight
            return (24 * 60 - startTotalMins) + endTotalMins;
        }
        return endTotalMins - startTotalMins;
    }

    private int convertTo24Hour(int hour, boolean isAM) {
        if (hour == 12) {
            return isAM ? 0 : 12;
        }
        return isAM ? hour : hour + 12;
    }

    public String toCSV() {
        return String.format("%s,%02d:%02d,%02d:%02d,%b,%b",
                date, sleepTimeHour, sleepTimeMin, wakeTimeHour, wakeTimeMin, isAMST, isAMWT);
    }
}
