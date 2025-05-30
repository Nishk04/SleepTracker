import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class MainFrame extends JFrame {

    private SleepDataManager manager;
    private InputPanel inputPanel;
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        super("Sleep Tracker App");
        manager = new SleepDataManager("SleepLog");
        initUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        inputPanel = new InputPanel(manager);
        dashboardPanel = createDashboardPanel();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Add Sleep Entry", inputPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // ================================================== TOP STREAK BAR ==============================================================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel streakBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(100, 180, 255));
                g.fillRoundRect(20, 10, getWidth() - 160, 40, 20, 20);
                g.setColor(Color.BLACK);
                Graphics2D g2d = (Graphics2D) g;
                Font boldFont = new Font("Arial", Font.BOLD, 20);
                g2d.setFont(boldFont);
                g2d.drawString("Sleep Streak: 4 Days!", 40, 35); // TODO: Replace with actual streak data
            }
        };

        streakBar.setPreferredSize(new Dimension(600, 50));
        streakBar.setOpaque(false);

        JLabel goalReminder = new JLabel("Goal: Sleep by 10:30 PM", SwingConstants.CENTER); // TODO: Replace with actual goal
        goalReminder.setFont(new Font("Arial", Font.ITALIC, 18));
        goalReminder.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 20));

        topPanel.add(streakBar, BorderLayout.CENTER);
        topPanel.add(goalReminder, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH); //adds the top panel to the main panel

        // ============================================ MAIN CENTER AREA ==================================================================================
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(7, 20, 20, 7));
        centerPanel.setBackground(Color.WHITE);

        // LEFT SIDE (Avg Sleep & Consistency) ============================================================================================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); //
        leftPanel.setBackground(Color.BLACK);

        RoundedPanel avgSleepPanel = new RoundedPanel(20, new Color(240, 250, 255));
        avgSleepPanel.setLayout(new BoxLayout(avgSleepPanel, BoxLayout.Y_AXIS));
        avgSleepPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 20));

        JLabel avgDuration = new JLabel("Avg Sleep Duration: 7.5 hrs"); // TODO: Replace with actual data
        avgDuration.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel evalMsg = new JLabel("You're doing pretty well!"); // TODO: Replace with actual evaluation or just a bank of phrases
        evalMsg.setFont(new Font("Arial", Font.PLAIN, 14));

        avgSleepPanel.add(avgDuration);
        avgSleepPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        avgSleepPanel.add(evalMsg);
        avgSleepPanel.setMaximumSize(new Dimension(400, 100));
        leftPanel.add(avgSleepPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // RIGHT SIDE (Score + Suggestions) =========================================
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        JPanel rightsidePanel = new JPanel();
        rightsidePanel.setLayout(new BoxLayout(rightsidePanel, BoxLayout.X_AXIS));
        rightsidePanel.setBackground(Color.WHITE);

        CircularScorePanel sleepScore = new CircularScorePanel("Sleep Score", 85, new Color(50, 200, 100));
        CircularScorePanel consistencyScore = new CircularScorePanel("Consistency", 50, Color.ORANGE);

        Dimension circleSize = new Dimension(160, 160);
        sleepScore.setPreferredSize(circleSize);
        consistencyScore.setPreferredSize(circleSize);

        sleepScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        consistencyScore.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightsidePanel.add(Box.createHorizontalGlue()); // pushes everything to center
        rightsidePanel.add(sleepScore);
        rightsidePanel.add(Box.createRigidArea(new Dimension(20, 0))); // space between
        rightsidePanel.add(consistencyScore);
        rightsidePanel.add(Box.createHorizontalGlue());

        rightPanel.add(rightsidePanel);

        JTextArea suggestions = new JTextArea("Suggestions:\n- Sleep earlier.\n- Avoid screens before bed.\n- Keep your room dark and cool.");
        suggestions.setFont(new Font("Arial", Font.PLAIN, 13));
        suggestions.setWrapStyleWord(true);
        suggestions.setLineWrap(true);
        suggestions.setEditable(false);
        suggestions.setBackground(new Color(255, 250, 240));
        suggestions.setBorder(BorderFactory.createTitledBorder("Sleep Tips"));

        rightPanel.add(suggestions);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
