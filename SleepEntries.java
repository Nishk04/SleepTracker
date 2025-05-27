

public class SleepEntries {
    private String date; // can make calendar UI to select date
    private int sleepTimeHrs; // 0-23, 0 is 12 AM, 23 is 11 PM
    private int sleepTimeMins; //sleepTime is recorded last time before user slept
    private int wakeTimeHrs; 
    private int wakeTimeMins; 
    private boolean isAMST; // true if wake time is AM, false if PM
    private boolean isAMWT; // true if sleep time is AM, false if PM
    
    private int sleepTimeHour, sleepTimeMin, wakeTimeHour, wakeTimeMin; //raw times in 12-hour format

    public SleepEntries(String d, int sth, int stm, int wth, int wtm, boolean ams, boolean amw) {
    	date = d;
        //Raw times not converted to 24-hour format
        sleepTimeHour = sth;
        sleepTimeMin = stm;
        wakeTimeHour = wth;
        wakeTimeMin = wtm;

        isAMWT = amw;
    	isAMST = ams;
        sleepTimeMins = stm;
    	wakeTimeMins = wtm;

        if(isAMST && sth == 12){
            sleepTimeHrs = 0; // convert 12 AM to 0 hours
        } else if(!isAMST && sth != 12) {
            sleepTimeHrs = sth + 12; // convert PM hours to 24-hour format
        }
        if(isAMWT && wth == 12){
            wakeTimeHrs = 0; // convert 12 AM to 0 hours
        } else if(!isAMWT && wth != 12) {
            wakeTimeHrs = wth + 12; // convert PM hours to 24-hour format
        }   	
    }
    
    public int getSleepDurationMins() { //10:35 PM - 6:40 AM = 8 hrs 5 mins // 485 mins //22-6=16
    	int hrs = 0;
        if(sleepTimeHrs > wakeTimeHrs || (sleepTimeHrs == wakeTimeHrs && sleepTimeMins > wakeTimeMins)) {
            // If sleep time is after wake time, it means the sleep period crossed midnight
            hrs = 24 - sleepTimeHrs + wakeTimeHrs;
        } else {
            hrs = wakeTimeHrs - sleepTimeHrs;
        }
    	int mins = Math.abs(sleepTimeMins - wakeTimeMins);
        return hrs * 60 + mins;
    }

    public String toCSV() {
        // Format: date,sleepTimeHour,sleepTimeMin,wakeTimeHour,wakeTimeMin,isAMST,isAMWT
        return String.format("%s,%02d:%02d,%02d:%02d,%b,%b", date, sleepTimeHour, sleepTimeMin, wakeTimeHour, wakeTimeMin, isAMST, isAMWT);
    }

    
}
