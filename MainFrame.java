// import javax.swing.*;
// import java.awt.*;

// public class MainFrame extends JFrame {

//     private SleepDataManager manager;
//     private InputPanel inputPanel;
//     private TablePanel tablePanel;
//     private ChartPanel chartPanel;
//     private JTabbedPane tabbedPane;

//     public MainFrame() {
//         super("Sleep Tracker App");
//         manager = new SleepDataManager("data/sleep_log.csv");

//         initUI();

//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setSize(800, 600);
//         setLocationRelativeTo(null); // Center the window
//         setVisible(true);
//     }

//     private void initUI() {
//         // Initialize panels with shared data manager
//         inputPanel = new InputPanel(manager);
//         tablePanel = new TablePanel(manager);
//         chartPanel = new ChartPanel(manager);

//         // Set up tabbed layout
//         tabbedPane = new JTabbedPane();
//         tabbedPane.addTab("Add Sleep Entry", inputPanel);
//         tabbedPane.addTab("View Sleep Log", tablePanel);
//         tabbedPane.addTab("Sleep Chart", chartPanel);

//         add(tabbedPane, BorderLayout.CENTER);
//     }

//     public static void main(String[] args) {
//         // Run app on Event Dispatch Thread
//         SwingUtilities.invokeLater(() -> new MainFrame());
//     }
// }
