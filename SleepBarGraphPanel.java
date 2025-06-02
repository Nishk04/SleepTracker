import javax.swing.*;
import java.awt.*;

public class SleepBarGraphPanel extends JPanel {
    private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private final double[] hours = {6.5, 7.2, 8.0, 7.8, 6.9, 7.4, 7.0}; // Example data

    public SleepBarGraphPanel() {
        setPreferredSize(new Dimension(400, 200));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int padding = 60;
        int graphHeight = height - 2 * padding;

        double maxHours = 10.0;
        int barWidth = (width - 2 * padding) / days.length - 15;

        // Draw axes
        g2.setColor(Color.GRAY);
        g2.drawLine(padding, padding, padding, height - padding); // y-axis
        g2.drawLine(padding, height - padding, width - padding, height - padding); // x-axis

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
