package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Cashier View with enhanced design, borders, colors, and availability box.
 */
public class CashierView extends JFrame implements Observer {
    private final JLabel pageTitle = new JLabel("Cashier System");
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane outputScrollPane = new JScrollPane(theOutput);
    private final JLabel availabilityLabel = new JLabel(); // Box to display item availability
    private final JButton theBtCheck = new JButton("Check");
    private final JButton theBtBuy = new JButton("Buy");
    private final JButton theBtBought = new JButton("Bought/Pay");
    private final JButton theBtUndo = new JButton("Undo");
    private final JButton theBtDiscount = new JButton("Apply Discount");
    private final JButton theBtDarkMode = new JButton("Toggle Dark Mode");

    private boolean isDarkMode = false; // Track dark mode state

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierController cont = null;

    /**
     * Construct the enhanced view.
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter();        // Database access
            theOrder = mf.makeOrderProcessing();        // Process order
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        Container cp = rpc.getContentPane();
        cp.setLayout(null);

        // Title
        pageTitle.setBounds(20, 10, 300, 30); // Align left
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pageTitle.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        cp.add(pageTitle);

        // Input Area
        JLabel productLabel = new JLabel("Product No:");
        productLabel.setBounds(20, 60, 100, 30);
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cp.add(productLabel);

        theInput.setBounds(120, 60, 200, 30);
        theInput.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cp.add(theInput);

        // Buttons
        theBtCheck.setBounds(20, 110, 120, 30);
        theBtCheck.setBackground(new Color(173, 216, 230));
        theBtCheck.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        cp.add(theBtCheck);

        theBtBuy.setBounds(150, 110, 120, 30);
        theBtBuy.setBackground(new Color(173, 216, 230));
        theBtBuy.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        cp.add(theBtBuy);

        theBtBought.setBounds(280, 110, 120, 30);
        theBtBought.setBackground(new Color(173, 216, 230));
        theBtBought.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        cp.add(theBtBought);

        theBtUndo.setBounds(20, 160, 120, 30);
        theBtUndo.setBackground(new Color(255, 182, 193));
        theBtUndo.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        cp.add(theBtUndo);

        theBtDiscount.setBounds(150, 160, 120, 30);
        theBtDiscount.setBackground(new Color(144, 238, 144));
        theBtDiscount.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        cp.add(theBtDiscount);

        theBtDarkMode.setBounds(280, 160, 120, 30);
        theBtDarkMode.setBackground(new Color(240, 230, 140));
        theBtDarkMode.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        cp.add(theBtDarkMode);

        // Output Area
        outputScrollPane.setBounds(20, 210, 380, 100);
        theOutput.setEditable(false);
        theOutput.setBorder(BorderFactory.createTitledBorder("Order Details"));
        theOutput.setFont(new Font("Courier New", Font.PLAIN, 14));
        cp.add(outputScrollPane);

        // Availability Box
        availabilityLabel.setBounds(20, 320, 380, 30);
        availabilityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        availabilityLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        availabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(availabilityLabel);

        // Set JFrame-specific properties
        if (rpc instanceof JFrame) {
            JFrame frame = (JFrame) rpc;
            frame.setSize(450, 400);                  // Adjusted size for added area
            frame.setVisible(true);                   // Make visible
            frame.setLocation(x, y);                  // Position window
        }

        // Add action listeners
        theBtCheck.addActionListener(e -> {
            if (cont != null) {
                cont.doCheck(theInput.getText());
            }
        });
        theBtBuy.addActionListener(e -> cont.doBuy());
        theBtBought.addActionListener(e -> cont.doBought());
        theBtUndo.addActionListener(e -> cont.doUndo());
        theBtDiscount.addActionListener(e -> cont.applyDiscount());
        theBtDarkMode.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            setTheme(isDarkMode);
        });

        setTheme(isDarkMode); // Apply initial theme
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param c   The controller
     */
    public void setController(CashierController c) {
        cont = c;
    }

    /**
     * Changes the theme of the view to a darker scheme
     * @param isDarkMode whether dark mode is enabled
     */
    public void setTheme(boolean isDarkMode) {
        if (isDarkMode) {
            getContentPane().setBackground(Color.DARK_GRAY);
            pageTitle.setForeground(Color.WHITE);
            theInput.setBackground(Color.GRAY);
            theInput.setForeground(Color.WHITE);
            theOutput.setBackground(Color.GRAY);
            theOutput.setForeground(Color.WHITE);
            availabilityLabel.setBackground(Color.GRAY);
            availabilityLabel.setForeground(Color.WHITE);
        } else {
            getContentPane().setBackground(Color.LIGHT_GRAY);
            pageTitle.setForeground(Color.BLACK);
            theInput.setBackground(Color.WHITE);
            theInput.setForeground(Color.BLACK);
            theOutput.setBackground(Color.WHITE);
            theOutput.setForeground(Color.BLACK);
            availabilityLabel.setBackground(Color.WHITE);
            availabilityLabel.setForeground(Color.BLACK);
        }
        repaint();
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        theOutput.setText(model.getBasket() == null ? "" : model.getBasket().getDetails());
        availabilityLabel.setText(message); // Display availability
        theInput.requestFocus();               // Focus is here
    }
}
