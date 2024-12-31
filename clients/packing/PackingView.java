package clients.packing;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Packing view with updated design and layout.
 */
public class PackingView extends JFrame implements Observer {
    private final JLabel pageTitle = new JLabel("Packing Bought Order");
    private final JLabel theAction = new JLabel();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane(theOutput);
    private final JLabel availabilityLabel = new JLabel(); // Availability box
    private final JButton theBtPack = new JButton("Packed");
    private final JButton theBtDarkMode = new JButton("Toggle Dark Mode"); // Dark mode toggle button

    private boolean isDarkMode = false; // Track dark mode state

    private OrderProcessing theOrder = null;

    private PackingController cont = null;

    /**
     * Construct the Packing view.
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public PackingView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theOrder = mf.makeOrderProcessing(); // Process order
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

        // Packed Button
        theBtPack.setBounds(20, 60, 150, 30);
        theBtPack.setBackground(new Color(144, 238, 144));
        theBtPack.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        theBtPack.addActionListener(e -> cont.doPacked());
        cp.add(theBtPack);

        // Dark Mode Toggle Button
        theBtDarkMode.setBounds(200, 60, 150, 30);
        theBtDarkMode.setBackground(new Color(240, 230, 140));
        theBtDarkMode.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        theBtDarkMode.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            setTheme(isDarkMode);
        });
        cp.add(theBtDarkMode);

        // Output Area
        theSP.setBounds(20, 110, 360, 200);
        theOutput.setEditable(false);
        theOutput.setBorder(BorderFactory.createTitledBorder("Order Details"));
        theOutput.setFont(new Font("Courier New", Font.PLAIN, 14));
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        // Availability Box
        availabilityLabel.setBounds(20, 320, 360, 30);
        availabilityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        availabilityLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        availabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(availabilityLabel);

        // Set JFrame-specific properties
        if (rpc instanceof JFrame) {
            JFrame frame = (JFrame) rpc;
            frame.setSize(420, 400);
            frame.setVisible(true);
            frame.setLocation(x, y);
        }

        setTheme(isDarkMode); // Apply initial theme
    }

    /**
     * Set the controller for this view.
     * @param c The controller
     */
    public void setController(PackingController c) {
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
            theAction.setForeground(Color.LIGHT_GRAY);
            theOutput.setBackground(Color.GRAY);
            theOutput.setForeground(Color.WHITE);
            availabilityLabel.setBackground(Color.GRAY);
            availabilityLabel.setForeground(Color.WHITE);
        } else {
            getContentPane().setBackground(Color.LIGHT_GRAY);
            pageTitle.setForeground(Color.BLACK);
            theAction.setForeground(Color.BLACK);
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
        PackingModel model = (PackingModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        availabilityLabel.setText(message); // Display availability

        Basket basket = model.getBasket();
        if (basket != null) {
            theOutput.setText(basket.getDetails());
        } else {
            theOutput.setText("");
        }
    }
}
