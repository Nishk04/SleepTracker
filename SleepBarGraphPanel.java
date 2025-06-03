import javax.swing.*;
import java.awt.*;

public class SleepBarGraphPanel extends JPanel {
    private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private final double[] hours = {6.5, 7.2, 8.0, 7.8, 6.9, 7.4, 7.0}; // Example data

    public SleepBarGraphPanel() {
        setPreferredSize(new Dimension(450, 300));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int padding = 30;
        int graphHeight = height - padding*2;

        double maxHours = 10.0;
        int barWidth = (width - 2 * padding) / days.length - 15;

        // Y-axis
        g2.setColor(Color.GRAY);
        g2.drawLine(padding, padding, padding, height - padding); // y-axis
        g2.drawLine(padding, height - padding, width - padding, height - padding); // x-axis

        // Add y-axis labels (hours)
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i <= maxHours; i++) {
            int y = height - padding - (int) ((i / maxHours) * graphHeight);
            g2.drawString(i + "h", padding - 30, y + 5);
            g2.setColor(new Color(220, 220, 220)); // Light grid lines
            g2.drawLine(padding, y, width - padding, y);
            g2.setColor(Color.BLACK);
        }

        // Draw bars
        for (int i = 0; i < days.length; i++) {
            int x = padding + i * (barWidth + 10) + 10;
            int barHeight = (int) ((hours[i] / maxHours) * graphHeight);
            int y = height - padding - barHeight;

            g2.setColor(new Color(100, 180, 255));
            g2.fillRect(x, y, barWidth, barHeight);

            g2.setColor(Color.BLACK);
            g2.drawString(days[i], x + barWidth / 4, height - padding + 15);
        }
    }

}
