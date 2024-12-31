package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the BackDoor view with updated design and layout.
 */
public class BackDoorView extends JFrame implements Observer {
    private final JLabel pageTitle = new JLabel("Staff Check and Manage Stock");
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextField theInputNo = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane(theOutput);
    private final JLabel availabilityLabel = new JLabel(); // Availability box
    private final JButton theBtClear = new JButton("Clear");
    private final JButton theBtRStock = new JButton("Add");
    private final JButton theBtQuery = new JButton("Query");
    private final JButton theBtDarkMode = new JButton("Toggle Dark Mode");
    private final JButton theBtLowStock = new JButton("Low Stock");
    private final JButton theBtSalesReport = new JButton("Sales Report");

    private boolean isDarkMode = false; // Track dark mode state

    private StockReadWriter theStock = null;
    private BackDoorController cont = null;

    /**
     * Construct the BackDoor view.
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public BackDoorView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        Container cp = rpc.getContentPane();
        cp.setLayout(null);

        // Title
        pageTitle.setBounds(20, 10, 360, 30);
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pageTitle.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        pageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(pageTitle);

        // Input Area
        JLabel productLabel = new JLabel("Product No:");
        productLabel.setBounds(20, 60, 100, 30);
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cp.add(productLabel);

        theInput.setBounds(120, 60, 120, 30);
        theInput.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cp.add(theInput);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(260, 60, 100, 30);
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cp.add(quantityLabel);

        theInputNo.setBounds(340, 60, 60, 30);
        theInputNo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cp.add(theInputNo);

        // Buttons
        theBtQuery.setBounds(20, 110, 120, 30);
        theBtQuery.setBackground(new Color(173, 216, 230));
        theBtQuery.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        cp.add(theBtQuery);

        theBtRStock.setBounds(150, 110, 120, 30);
        theBtRStock.setBackground(new Color(144, 238, 144));
        theBtRStock.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        cp.add(theBtRStock);

        theBtClear.setBounds(280, 110, 120, 30);
        theBtClear.setBackground(new Color(255, 182, 193));
        theBtClear.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        cp.add(theBtClear);

        theBtLowStock.setBounds(20, 160, 150, 30);
        theBtLowStock.setBackground(new Color(240, 230, 140));
        theBtLowStock.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        cp.add(theBtLowStock);

        theBtSalesReport.setBounds(180, 160, 150, 30);
        theBtSalesReport.setBackground(new Color(240, 230, 140));
        theBtSalesReport.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        cp.add(theBtSalesReport);

        theBtDarkMode.setBounds(20, 210, 150, 30);
        theBtDarkMode.setBackground(new Color(240, 230, 140));
        theBtDarkMode.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        cp.add(theBtDarkMode);

        // Output Area
        theSP.setBounds(20, 260, 380, 140);
        theOutput.setEditable(false);
        theOutput.setBorder(BorderFactory.createTitledBorder("Output"));
        theOutput.setFont(new Font("Courier New", Font.PLAIN, 14));
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        // Availability Box
        availabilityLabel.setBounds(20, 410, 380, 30);
        availabilityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        availabilityLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        availabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(availabilityLabel);

        // Set JFrame-specific properties
        if (rpc instanceof JFrame) {
            JFrame frame = (JFrame) rpc;
            frame.setSize(450, 500);
            frame.setVisible(true);
            frame.setLocation(x, y);
        }

        // Add action listeners
        theBtQuery.addActionListener(e -> cont.doQuery(theInput.getText()));
        theBtRStock.addActionListener(e -> cont.doRStock(theInput.getText(), theInputNo.getText()));
        theBtClear.addActionListener(e -> cont.doClear());
        theBtLowStock.addActionListener(e -> cont.checkLowStock());
        theBtSalesReport.addActionListener(e -> cont.generateSalesReport());
        theBtDarkMode.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            setTheme(isDarkMode);
        });

        setTheme(isDarkMode); // Apply initial theme
    }

    /**
     * Set the controller for this view.
     * @param c The controller
     */
    public void setController(BackDoorController c) {
        cont = c;
    }

    /**
     * Change the theme of the view.
     * @param isDarkMode whether dark mode is enabled
     */
    public void setTheme(boolean isDarkMode) {
        if (isDarkMode) {
            getContentPane().setBackground(Color.DARK_GRAY);
            pageTitle.setForeground(Color.WHITE);
            theInput.setBackground(Color.GRAY);
            theInput.setForeground(Color.WHITE);
            theInputNo.setBackground(Color.GRAY);
            theInputNo.setForeground(Color.WHITE);
            theOutput.setBackground(Color.GRAY);
            theOutput.setForeground(Color.WHITE);
            availabilityLabel.setBackground(Color.GRAY);
            availabilityLabel.setForeground(Color.WHITE);
        } else {
            getContentPane().setBackground(Color.LIGHT_GRAY);
            pageTitle.setForeground(Color.BLACK);
            theInput.setBackground(Color.WHITE);
            theInput.setForeground(Color.BLACK);
            theInputNo.setBackground(Color.WHITE);
            theInputNo.setForeground(Color.BLACK);
            theOutput.setBackground(Color.WHITE);
            theOutput.setForeground(Color.BLACK);
            availabilityLabel.setBackground(Color.WHITE);
            availabilityLabel.setForeground(Color.BLACK);
        }
        repaint();
    }

    /**
     * Update the view.
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        BackDoorModel model = (BackDoorModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        availabilityLabel.setText(message); // Display availability
        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }

    /**
     * Display a message in the output area.
     * @param message The message to display
     */
    public void displayMessage(String message) {
        theOutput.setText(message);
    }
}
