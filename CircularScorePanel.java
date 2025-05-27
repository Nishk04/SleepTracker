import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

class CircularScorePanel extends JPanel {
    private String label;
    private int score;
    private Color color;
    private int width = 200; // Default width
    private int height = 200; // Default height

    public CircularScorePanel(String label, int score, Color color, int width, int height) {
        this.label = label;
        this.score = score;
        this.color = color;
        this.width = width;
        this.height = height;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //this.setPreferredSize(new java.awt.Dimension(width, height));
        int size = Math.min(getWidth(), getHeight()) - 10;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background circle
        g2.setColor(new Color(230, 230, 230));
        g2.fillOval(5, 5, size, size);

        // Score ring
        g2.setColor(color);
        g2.setStroke(new BasicStroke(12));
        g2.drawArc(5, 5, size, size, 90, -(int) (360 * (score / 100.0)));

        // Text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String scoreText = score + "%";
        FontMetrics fm = g2.getFontMetrics();
        int x = getWidth() / 2 - fm.stringWidth(scoreText) / 2;
        int y = getHeight() / 2 + fm.getAscent() / 2 - 5;
        g2.drawString(scoreText, x, y);

        // Label
        g2.setFont(new Font("Arial", Font.PLAIN, 15));
        int lx = getWidth() / 2 - g2.getFontMetrics().stringWidth(label) / 2;
        g2.drawString(label, lx, getHeight()/2 + fm.getAscent() + 15);
    }
}