import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class SleepBarGraphPanel extends JPanel {
    private SleepDataManager manager;
    private static final String[] WEEK_DAYS = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public SleepBarGraphPanel(SleepDataManager manager) {
        this.manager = manager;
        setPreferredSize(new Dimension(450, 300));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<SleepEntries> entries = manager.getEntries();
        if (entries == null || entries.isEmpty()) return;

        // Step 1: Find the latest date in the data
        LocalDate latestDate = entries.stream()
                .map(e -> {
                    try {
                        return LocalDate.parse(e.getDate(), INPUT_FORMAT);
                    } catch (Exception ex) {
                        return null;
                    }
                })
                .filter(d -> d != null)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        // Step 2: Use that to find the current Sunday-to-Saturday range
        LocalDate sunday = latestDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate saturday = latestDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        // Step 3: Collect sleep data for the week
        double[] hours = new double[7];
        for (SleepEntries e : entries) {
            try {
                LocalDate entryDate = LocalDate.parse(e.getDate(), INPUT_FORMAT);
                if (!entryDate.isBefore(sunday) && !entryDate.isAfter(saturday)) {
                    int index = entryDate.getDayOfWeek().getValue() % 7; // Sunday = 0
                    hours[index] = e.getTotalSleepHours();
                }
            } catch (Exception ignored) {}
        }

        // Step 4: Draw the graph
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth(), height = getHeight(), padding = 30;
        int graphHeight = height - padding * 2;
        double maxHours = 10.0;
        int barWidth = (width - 2 * padding) / 7 - 10;

        g2.setColor(Color.GRAY);
        g2.drawLine(padding, padding, padding, height - padding);
        g2.drawLine(padding, height - padding, width - padding, height - padding);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i <= maxHours; i++) {
            int y = height - padding - (int) ((i / maxHours) * graphHeight);
            g2.drawString(i + "h", padding - 25, y + 5);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(padding, y, width - padding, y);
            g2.setColor(Color.BLACK);
        }

        for (int i = 0; i < 7; i++) {
            int x = padding + i * (barWidth + 10) + 5;
            int barHeight = (int) ((hours[i] / maxHours) * graphHeight);
            int y = height - padding - barHeight;

            g2.setColor(new Color(100, 180, 255));
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(Color.BLACK);
            int labelX = x + (barWidth / 2) - g2.getFontMetrics().stringWidth(WEEK_DAYS[i]) / 2;
            g2.drawString(WEEK_DAYS[i], labelX, height - padding + 15);
        }
    }
}

// import javax.swing.*;
// import java.awt.*;
// import java.time.DayOfWeek;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.time.temporal.TemporalAdjusters;
// import java.util.List;

// public class SleepBarGraphPanel extends JPanel {
//     private SleepDataManager manager;
//     private static final String[] WEEK_DAYS = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
//     private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

//     public SleepBarGraphPanel(SleepDataManager manager) {
//         this.manager = manager;
//         setPreferredSize(new Dimension(450, 300));
//         setBackground(Color.WHITE);
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         List<SleepEntries> entries = manager.getEntries();
//         if (entries == null) return;

//         LocalDate today = LocalDate.now();
//         LocalDate sunday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
//         LocalDate saturday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

//         double[] hours = new double[7];
//         for (SleepEntries e : entries) {
//             try {
//                 LocalDate entryDate = LocalDate.parse(e.getDate(), INPUT_FORMAT);
//                 if (!entryDate.isBefore(sunday) && !entryDate.isAfter(saturday)) {
//                     int index = entryDate.getDayOfWeek().getValue() % 7; // Sunday = 0
//                     hours[index] = e.getTotalSleepHours();
//                 }
//             } catch (Exception ignored) {}
//         }

//         Graphics2D g2 = (Graphics2D) g;
//         int width = getWidth(), height = getHeight(), padding = 30;
//         int graphHeight = height - padding * 2;
//         double maxHours = 10.0;
//         int barWidth = (width - 2 * padding) / 7 - 10;

//         g2.setColor(Color.GRAY);
//         g2.drawLine(padding, padding, padding, height - padding);
//         g2.drawLine(padding, height - padding, width - padding, height - padding);

//         g2.setFont(new Font("Arial", Font.BOLD, 12));
//         for (int i = 0; i <= maxHours; i++) {
//             int y = height - padding - (int) ((i / maxHours) * graphHeight);
//             g2.drawString(i + "h", padding - 25, y + 5);
//             g2.setColor(Color.LIGHT_GRAY);
//             g2.drawLine(padding, y, width - padding, y);
//             g2.setColor(Color.BLACK);
//         }

//         for (int i = 0; i < 7; i++) {
//             int x = padding + i * (barWidth + 10) + 5;
//             int barHeight = (int) ((hours[i] / maxHours) * graphHeight);
//             int y = height - padding - barHeight;

//             g2.setColor(new Color(100, 180, 255));
//             g2.fillRect(x, y, barWidth, barHeight);
//             g2.setColor(Color.BLACK);
//             int labelX = x + (barWidth / 2) - g2.getFontMetrics().stringWidth(WEEK_DAYS[i]) / 2;
//             g2.drawString(WEEK_DAYS[i], labelX, height - padding + 15);
//         }
//     }
// }
