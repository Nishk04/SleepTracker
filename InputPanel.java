import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;

public class InputPanel extends BasePanel {

    private JTextField dateField;
    private JTextField sleepHourField, sleepMinField;
    private JRadioButton sleepAM, sleepPM;

    private JTextField wakeHourField, wakeMinField;
    private JRadioButton wakeAM, wakePM;

    private JButton submitButton;
    private JLabel statusLabel;

    public InputPanel(SleepDataManager manager) {
        super(manager);
    }

    @Override
    protected void initializeUI() {
        Font bigFont = new Font("SansSerif", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(200, 60);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Date
        dateField = new JTextField("DD-MM-YYYY", 10);
        dateField.setFont(bigFont);
        dateField.setPreferredSize(fieldSize);
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(bigFont);
        gbc.gridx = 0; 
        gbc.gridy = row;
        add(dateLabel, gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        // Sleep Time
        sleepHourField = new JTextField(2); sleepHourField.setFont(bigFont);
        sleepMinField = new JTextField(2); sleepMinField.setFont(bigFont);
        sleepHourField.setPreferredSize(new Dimension(50, 30));
        sleepMinField.setPreferredSize(new Dimension(50, 30));

        sleepAM = new JRadioButton("AM"); sleepAM.setFont(bigFont);
        sleepPM = new JRadioButton("PM"); sleepPM.setFont(bigFont);
        ButtonGroup sleepGroup = new ButtonGroup();
        sleepGroup.add(sleepAM); 
        sleepGroup.add(sleepPM);
        sleepPM.setSelected(true);

        JPanel sleepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        sleepPanel.add(sleepHourField);
        sleepPanel.add(new JLabel(":"));
        sleepPanel.add(sleepMinField);
        sleepPanel.add(sleepAM);
        sleepPanel.add(sleepPM);

        JLabel sleepLabel = new JLabel("Sleep Time:");
        sleepLabel.setFont(bigFont);
        row++;
        gbc.gridx = 0; 
        gbc.gridy = row;
        add(sleepLabel, gbc);
        gbc.gridx = 1;
        add(sleepPanel, gbc);

        // Wake Time
        wakeHourField = new JTextField(2); wakeHourField.setFont(bigFont);
        wakeMinField = new JTextField(2); wakeMinField.setFont(bigFont);
        wakeHourField.setPreferredSize(new Dimension(50, 30));
        wakeMinField.setPreferredSize(new Dimension(50, 30));

        wakeAM = new JRadioButton("AM"); wakeAM.setFont(bigFont);
        wakePM = new JRadioButton("PM"); wakePM.setFont(bigFont);
        ButtonGroup wakeGroup = new ButtonGroup();
        wakeGroup.add(wakeAM); wakeGroup.add(wakePM);
        wakeAM.setSelected(true);

        JPanel wakePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        wakePanel.add(wakeHourField);
        wakePanel.add(new JLabel(":"));
        wakePanel.add(wakeMinField);
        wakePanel.add(wakeAM);
        wakePanel.add(wakePM);

        JLabel wakeLabel = new JLabel("Wake Time:");
        wakeLabel.setFont(bigFont);
        row++;
        gbc.gridx = 0; 
        gbc.gridy = row;
        add(wakeLabel, gbc);
        gbc.gridx = 1;
        add(wakePanel, gbc);

        // Submit button
        submitButton = new JButton("Add Entry");
        submitButton.setFont(bigFont);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        add(submitButton, gbc);

        // Status Label - padding is added thru createCompoundBorder()
        statusLabel = new JLabel("Waiting for input...");
        statusLabel.setFont(bigFont);
        Border line = BorderFactory.createLineBorder(Color.BLACK, 1, true);
        Border padding = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(line, padding));
        gbc.gridx = 1;
        add(statusLabel, gbc);

        // Action
        submitButton.addActionListener(this::handleSubmit);
    }

    private void handleSubmit(ActionEvent e) {
        try {
            String date = dateField.getText().trim();

            int sth = Integer.parseInt(sleepHourField.getText().trim());
            int stm = Integer.parseInt(sleepMinField.getText().trim());
            boolean isAMST = sleepAM.isSelected();

            int wth = Integer.parseInt(wakeHourField.getText().trim());
            int wtm = Integer.parseInt(wakeMinField.getText().trim());
            boolean isAMWT = wakeAM.isSelected();

            if (sth < 1 || sth > 12 || stm < 0 || stm >= 60 || wth < 1 || wth > 12 || wtm < 0 || wtm >= 60) {
                throw new NumberFormatException();
            }

            SleepEntries entry = new SleepEntries(date, sth, stm, wth, wtm, isAMST, isAMWT);
            // manager.addEntry(entry);
            statusLabel.setForeground(Color.green);
            statusLabel.setText("Entry added successfully!");
            
            new javax.swing.Timer(3000, evt -> {  
                ((javax.swing.Timer) evt.getSource()).stop();
                refresh(); // This will now run after 'x' seconds of showing submitted
            }).start();

        } catch (NumberFormatException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("⚠️ Invalid time format. Use whole numbers.");
        } catch (Exception ex) {
            statusLabel.setText("⚠️ Error: " + ex.getMessage());
        }
    }

    @Override 
    public void refresh() {
        dateField.setText("DD-MM-YYYY");
        sleepHourField.setText("");
        sleepMinField.setText("");
        wakeHourField.setText("");
        wakeMinField.setText("");
        sleepPM.setSelected(true);
        wakeAM.setSelected(true);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText("Waiting for input...");
    }
}
