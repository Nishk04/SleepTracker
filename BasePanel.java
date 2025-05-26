import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 * BasePanel provides a common structure for all panels in the Sleep Tracker app.
 * It contains a shared SleepDataManager reference and an abstract refresh method
 * that child panels should override to update their UI when data changes.
 */
public abstract class BasePanel extends JPanel {

    protected SleepDataManager manager;

    public BasePanel(SleepDataManager manager) {
        this.manager = manager;
        initializeUI();
    }

    /**
     * Initializes UI components.
     * Child classes can override this method if they need custom layout/setup.
     */
    protected void initializeUI() {
        // Default is empty. Child classes can override if needed.
    }

    /**
     * Refreshes the panel's contents when data changes.
     * All extending panels must implement this method.
     */
    public abstract void refresh();
}

