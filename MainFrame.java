import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class MainFrame extends JFrame {

    private SleepDataManager manager;
    private InputPanel inputPanel;
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane;

    private JLabel goalReminder;
    private JTextField goalInputField;
    private JButton submitGoalButton;
    private JButton resetGoalButton;
    private JLabel errorLabel; // new error label

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

                int streakCount = manager.getStreakMatchingGoal();
                String streakText = "Sleep Streak: " + streakCount + (streakCount == 1 ? " Day" : " Days") + "!";
                g2d.drawString(streakText, 40, 35);
            }
        };
        streakBar.setPreferredSize(new Dimension(600, 50));
        streakBar.setOpaque(false);

        JPanel goalPanel = new JPanel();
        goalPanel.setLayout(new BoxLayout(goalPanel, BoxLayout.Y_AXIS));
        goalPanel.setOpaque(false);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        inputRow.setOpaque(false);

        goalInputField = new JTextField(8);
        goalInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        submitGoalButton = new JButton("Set Goal");

        goalReminder = new JLabel();
        goalReminder.setFont(new Font("Arial", Font.ITALIC, 18));
        goalReminder.setVisible(false);

        resetGoalButton = new JButton("Reset");
        resetGoalButton.setVisible(false);

        errorLabel = new JLabel("Invalid format! Use HH:mm AM/PM");
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);

        submitGoalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String goalTime = goalInputField.getText().trim();
                if (!goalTime.isEmpty()) {
                    if (!isValidTimeFormat(goalTime)) {
                        errorLabel.setVisible(true);
                        Timer timer = new Timer(2000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                errorLabel.setVisible(false);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                        return;
                    }

                    goalReminder.setText("Goal: sleep by " + goalTime);
                    goalReminder.setVisible(true);
                    resetGoalButton.setVisible(true);
                    goalInputField.setVisible(false);
                    submitGoalButton.setVisible(false);
                    errorLabel.setVisible(false);
                    manager.setGoalString(goalTime);
                    dashboardPanel.repaint();
                }
            }
        });

        resetGoalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goalInputField.setText("");
                goalInputField.setVisible(true);
                submitGoalButton.setVisible(true);
                goalReminder.setVisible(false);
                resetGoalButton.setVisible(false);
                errorLabel.setVisible(false);
                manager.setGoalString(null);
                dashboardPanel.repaint();
            }
        });

        inputRow.add(goalInputField);
        inputRow.add(submitGoalButton);
        inputRow.add(goalReminder);
        inputRow.add(resetGoalButton);

        goalPanel.add(inputRow);
        goalPanel.add(errorLabel);

        topPanel.add(streakBar, BorderLayout.CENTER);
        topPanel.add(goalPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(7, 20, 20, 7));
        centerPanel.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        RoundedPanel avgSleepPanel = new RoundedPanel(20, new Color(240, 250, 255));
        avgSleepPanel.setLayout(new BorderLayout());
        avgSleepPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel avgSleepContent = new JPanel();
        avgSleepContent.setOpaque(false);
        avgSleepContent.setLayout(new BoxLayout(avgSleepContent, BoxLayout.Y_AXIS));

        double avgSleepHours = manager.getAvgSleepForLastSevenDays();
        if (avgSleepHours < 0) {
            avgSleepHours = 0;
        }
        JLabel avgDuration = new JLabel(String.format("Avg Sleep Duration: %.1f hours", avgSleepHours));
        avgDuration.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel evalMsg = new JLabel("You're doing pretty well!");
        evalMsg.setFont(new Font("Arial", Font.PLAIN, 14));
        evalMsg.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        avgSleepContent.add(avgDuration);
        avgSleepContent.add(evalMsg);

        avgSleepPanel.add(avgSleepContent, BorderLayout.CENTER);
        avgSleepPanel.setMaximumSize(new Dimension(400, 120));

        leftPanel.add(avgSleepPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titleWrapper.setOpaque(false);

        JLabel consistencyLabel = new JLabel("Weekly Sleep Log");
        consistencyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleWrapper.add(consistencyLabel);
        leftPanel.add(titleWrapper);

        SleepBarGraphPanel barGraph = new SleepBarGraphPanel();
        barGraph.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel graphWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        graphWrapper.setOpaque(false);
        graphWrapper.add(barGraph);
        leftPanel.add(graphWrapper);

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

        rightsidePanel.add(Box.createHorizontalGlue());
        rightsidePanel.add(sleepScore);
        rightsidePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        rightsidePanel.add(consistencyScore);
        rightsidePanel.add(Box.createHorizontalGlue());

        rightPanel.add(rightsidePanel);

        JTextArea suggestions = new JTextArea("Suggestions:\n- Sleep earlier.\n- Avoid screens before bed.\n- Keep your room dark and cool.");
        suggestions.setFont(new Font("Arial", Font.PLAIN, 16));
        suggestions.setWrapStyleWord(true);
        suggestions.setLineWrap(true);
        suggestions.setEditable(false);
        suggestions.setBackground(new Color(255, 250, 240));
        suggestions.setBorder(BorderFactory.createTitledBorder("Sleep Tips"));

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(suggestions);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private boolean isValidTimeFormat(String time) {
        String pattern = "^(0[1-9]|1[0-2]):[0-5][0-9] (AM|PM)$";
        return Pattern.matches(pattern, time);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
