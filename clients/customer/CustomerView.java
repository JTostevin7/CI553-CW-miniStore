package clients.customer;

import catalogue.Basket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view with enhanced design and features.
 */
public class CustomerView extends JFrame implements Observer {
    private final JLabel pageTitle = new JLabel("Search Products");
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane outputScrollPane = new JScrollPane(theOutput);
    private final JLabel availabilityLabel = new JLabel(); // Availability box
    private final JButton theBtCheck = new JButton("Check");
    private final JButton theBtClear = new JButton("Clear");
    private final JButton theBtSearchByName = new JButton("Search by Name");
    private final JButton theBtDarkMode = new JButton("Toggle Dark Mode");

    private boolean isDarkMode = false; // Track dark mode state

    private Picture thePicture = new Picture(80, 80);
    private StockReader theStock = null;
    private CustomerController cont = null;

    /**
     * Construct the enhanced CustomerView.
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public CustomerView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReader(); // Database access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        Container cp = rpc.getContentPane();
        cp.setLayout(null);

        // Title
        pageTitle.setBounds(20, 10, 300, 30);
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
        theBtCheck.setBounds(20, 110, 150, 30);
        theBtCheck.setBackground(new Color(173, 216, 230));
        theBtCheck.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        cp.add(theBtCheck);

        theBtClear.setBounds(180, 110, 150, 30);
        theBtClear.setBackground(new Color(173, 216, 230));
        theBtClear.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        cp.add(theBtClear);

        theBtSearchByName.setBounds(180, 160, 150, 30);
        theBtSearchByName.setBackground(new Color(144, 238, 144));
        theBtSearchByName.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        cp.add(theBtSearchByName);

        theBtDarkMode.setBounds(20, 160, 150, 30);
        theBtDarkMode.setBackground(new Color(240, 230, 140));
        theBtDarkMode.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        cp.add(theBtDarkMode);

        // Output Area
        outputScrollPane.setBounds(20, 210, 380, 100);
        theOutput.setEditable(false);
        theOutput.setBorder(BorderFactory.createTitledBorder("Order Details"));
        theOutput.setFont(new Font("Courier New", Font.PLAIN, 14));
        cp.add(outputScrollPane);

        // Picture Area
        thePicture.setBounds(350, 60, 80, 80);
        cp.add(thePicture);

        // Availability Box
        availabilityLabel.setBounds(20, 320, 380, 30);
        availabilityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        availabilityLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        availabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(availabilityLabel);

        // Set JFrame-specific properties
        if (rpc instanceof JFrame) {
            JFrame frame = (JFrame) rpc;
            frame.setSize(450, 400);
            frame.setVisible(true);
            frame.setLocation(x, y);
        }

        // Add action listeners
        theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText()));
        theBtClear.addActionListener(e -> cont.doClear());
        theBtSearchByName.addActionListener(e -> cont.doSearchByName(theInput.getText()));
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
    public void setController(CustomerController c) {
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
     * Update the view.
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        theOutput.setText(model.getBasket() == null ? "" : model.getBasket().getDetails());
        availabilityLabel.setText(message);
        ImageIcon image = model.getPicture();
        if (image == null) {
            thePicture.clear();
        } else {
            thePicture.set(image);
        }
        theInput.requestFocus();
    }
}
