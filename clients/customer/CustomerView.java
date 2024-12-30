package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class CustomerView extends JFrame implements Observer
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "Check";
    public static final String CLEAR  = "Clear";
  }

  private static final int H = 420;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( Name.CHECK );
  private final JButton     theBtClear = new JButton( Name.CLEAR );
  
  private final JButton theBtSearchByName = new JButton("Search by Name"); //adds search by name button
  private final JButton theBtDarkMode = new JButton("Toggle Dark Mode"); //adds dark mode toggle button
  private boolean isDarkMode = false; //tracks dark mode state

  private Picture thePicture = new Picture(80,80);
  private StockReader theStock   = null;
  private CustomerController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public CustomerView( RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );

    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is
    
    pageTitle.setBounds( 110, 0 , 270, 20 );       
    pageTitle.setText( "Search products" );                        
    cp.add( pageTitle );

    theBtCheck.setBounds( 16, 25+60*0, 80, 40 );    // Check button
    theBtCheck.addActionListener(                   // Call back code
      e -> cont.doCheck( theInput.getText() ) );
    cp.add( theBtCheck );                           //  Add to canvas

    theBtClear.setBounds( 16, 25+60*1, 80, 40 );    // Clear button
    theBtClear.addActionListener(                   // Call back code
      e -> cont.doClear() );
    cp.add( theBtClear );                           //  Add to canvas

    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( " " );                       // blank
    cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 110, 50, 270, 40 );         // Product no area
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas
    
    theSP.setBounds( 110, 100, 270, 160 );          // Scrolling pane
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea

    thePicture.setBounds( 16, 25+60*2, 80, 80 );   // Picture area
    cp.add( thePicture );                           //  Add to canvas
    thePicture.clear();
    
    theBtDarkMode.setBounds(16,25 + 60*5, 150, 40);    //dark mode area
    theBtDarkMode.addActionListener(e -> {		//calls back code
    	isDarkMode = !isDarkMode; //toggles the state
    	setTheme(isDarkMode);
    });
    cp.add(theBtDarkMode); //add canvas
    
    
    theBtSearchByName.setBounds(16, 25 + 60 * 4, 150, 40 ); //search by name area
    theBtSearchByName.addActionListener(e -> cont.doSearchByName(theInput.getText())); //call back code
    cp.add(theBtSearchByName); //add to canvas
    
    rootWindow.setVisible( true );                  // Make visible);
    theInput.requestFocus();                        // Focus is here
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
  {
    cont = c;
  }
  
  /**
   * changes the theme of the view to a darker scheme
   * @param isDarkMode
   */
  
  public void setTheme(boolean isDarkMode) {
	  if (isDarkMode) {
	        getContentPane().setBackground(Color.DARK_GRAY);
	        pageTitle.setForeground(Color.WHITE);
	        theAction.setForeground(Color.LIGHT_GRAY);
	        theInput.setBackground(Color.GRAY);
	        theInput.setForeground(Color.WHITE);
	        theOutput.setBackground(Color.GRAY);
	        theOutput.setForeground(Color.WHITE);
	    } else {
	        getContentPane().setBackground(Color.LIGHT_GRAY);
	        pageTitle.setForeground(Color.BLACK);
	        theAction.setForeground(Color.BLACK);
	        theInput.setBackground(Color.WHITE);
	        theInput.setForeground(Color.BLACK);
	        theOutput.setBackground(Color.WHITE);
	        theOutput.setForeground(Color.BLACK);
	    }
	    repaint();
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
   
  public void update( Observable modelC, Object arg )
  {
    CustomerModel model  = (CustomerModel) modelC;
    String        message = (String) arg;
    
    if(message.startsWith("Error")|| message.startsWith("Search term")) {
    	theAction.setText(message);
    	theOutput.setText("");
    } else {
    	theAction.setText("Search results:");
    	theOutput.setText(message);
    }
    theAction.setText( message );
    ImageIcon image = model.getPicture();  // Image of product
    if ( image == null )
    {
      thePicture.clear();                  // Clear picture
    } else {
      thePicture.set( image );             // Display picture
    }
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();               // Focus is here
  }

}
