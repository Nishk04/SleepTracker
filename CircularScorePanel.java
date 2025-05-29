import java.awt.*;
import javax.swing.*;

class CircularScorePanel extends JPanel {
    private String label;
    private int score;
    private Color color;

    public CircularScorePanel(String label, int score, Color color) {
        this.label = label;
        this.score = score;
        this.color = color;
        setOpaque(false);
        //setPreferredSize(new Dimension(width, height));  // Make constructor dimensions useful
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 5;
        int size = Math.min(getWidth(), getHeight()) - 2 * padding;
        int x = padding;
        int y = padding;

        // Draw background circle
        g2.setColor(new Color(230, 230, 230));
        g2.fillOval(x, y, size, size);

        // Draw score ring
        g2.setColor(color);
        g2.setStroke(new BasicStroke(12));
        g2.drawArc(x, y, size, size, 90, -(int) (360 * (score / 100.0)));

        // Draw score text (centered)
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String scoreText = score + "%";
        FontMetrics fm = g2.getFontMetrics();
        int centerX = x + size / 2;
        int centerY = y + size / 2;

        int scoreTextX = centerX - fm.stringWidth(scoreText) / 2;
        int scoreTextY = centerY + fm.getAscent() / 2 - 4;
        g2.drawString(scoreText, scoreTextX, scoreTextY);

        // Draw label text (below score)
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics labelFM = g2.getFontMetrics();
        int labelTextX = centerX - labelFM.stringWidth(label) / 2;
        int labelTextY = centerY + fm.getAscent() + 18;
        g2.drawString(label, labelTextX, labelTextY);
    }
}

// import java.awt.BasicStroke;
// import java.awt.Color;
// import java.awt.Font;
// import java.awt.FontMetrics;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.RenderingHints;
// import java.awt.geom.RoundRectangle2D;
// import javax.swing.JPanel;

// class CircularScorePanel extends JPanel {
//     private String label;
//     private int score;
//     private Color color;
//     private int width = 50; // Default width
//     private int height = 50; // Default height

//     public CircularScorePanel(String label, int score, Color color, int width, int height) {
//         this.label = label;
//         this.score = score;
//         this.color = color;
//         this.width = width;
//         this.height = height;
//         setOpaque(false);
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         Graphics2D g2 = (Graphics2D) g;
//         //this.setPreferredSize(new java.awt.Dimension(width, height));
//         int size = Math.min(getWidth(), getHeight()) - 10;
//         System.out.println(size);
//         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//         // Background circle
//         g2.setColor(new Color(230, 230, 230));
//         g2.fillOval(5, 5, size, size);

//         // Score ring
//         g2.setColor(color);
//         g2.setStroke(new BasicStroke(12));
//         g2.drawArc(5, 5, size, size, 90, -(int) (360 * (score / 100.0)));

//         // Text
//         g2.setColor(Color.BLACK);
//         g2.setFont(new Font("Arial", Font.BOLD, 16));
//         String scoreText = score + "%";
//         FontMetrics fm = g2.getFontMetrics();
//         int x = getWidth() / 2 - fm.stringWidth(scoreText) / 2;
//         int y = getHeight() / 2 + fm.getAscent() / 2 - 5;
//         g2.drawString(scoreText, x, y);

//         // Label
//         g2.setFont(new Font("Arial", Font.PLAIN, 15));
//         int lx = getWidth() / 2 - g2.getFontMetrics().stringWidth(label) / 2;
//         g2.drawString(label, lx, getHeight()/2 + fm.getAscent() + 15);
//     }
// }