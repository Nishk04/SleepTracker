import java.awt.*;
import javax.swing.*;

class CircularScorePanel extends JPanel {
    private String label;
    private int score;

    public CircularScorePanel(String label, int score) {
        this.label = label;
        this.score = score;
        setOpaque(false);
    }

    private Color getScoreColor(int score) {
        if (score < 30) return new Color(220, 50, 50);       // Red
        else if (score < 50) return new Color(255, 165, 0);   // Orange
        else if (score < 70) return new Color(255, 215, 0);   // Gold
        else if (score < 85) return new Color(60, 179, 113);  // Medium Green
        else return new Color(50, 205, 50);                   // Bright Green
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 10;
        int size = Math.min(getWidth(), getHeight()) - 2 * padding;
        int x = padding;
        int y = padding;

        // Draw background circle
        g2.setColor(new Color(230, 230, 230));
        g2.fillOval(x, y, size, size);

        // Get dynamic color based on score
        Color dynamicColor = getScoreColor(score);

        // Draw score arc
        g2.setColor(dynamicColor);
        g2.setStroke(new BasicStroke(12));
        g2.drawArc(x, y, size, size, 90, -(int) (360 * (score / 100.0)));

        // Center coordinates
        int centerX = x + size / 2;
        int centerY = y + size / 2;

        // Draw score text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String scoreText = score + "%";
        FontMetrics fm = g2.getFontMetrics();
        int scoreTextX = centerX - fm.stringWidth(scoreText) / 2;
        int scoreTextY = centerY + fm.getAscent() / 2 - 4;
        g2.drawString(scoreText, scoreTextX, scoreTextY);

        // Draw label text
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics labelFM = g2.getFontMetrics();
        int labelTextX = centerX - labelFM.stringWidth(label) / 2;
        int labelTextY = centerY + fm.getAscent() + 18;
        g2.drawString(label, labelTextX, labelTextY);
    }
}
