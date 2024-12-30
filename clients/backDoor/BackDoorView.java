package clients.backDoor;

import middle.MiddleFactory;

import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class BackDoorView extends JFrame implements Observer
{
  private static final String RESTOCK  = "Add";
  private static final String CLEAR    = "Clear";
  private static final String QUERY    = "Query";
 
  private static final int H = 420;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextField  theInputNo = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtClear = new JButton( CLEAR );
  private final JButton     theBtRStock = new JButton( RESTOCK );
  private final JButton     theBtQuery = new JButton( QUERY );
  
  private final JButton theBtDarkMode = new JButton("Toggle Dark Mode"); //adds dark mode toggle button
  private boolean isDarkMode = false; //tracks dark mode state
  
  private final JButton theBtLowStock = new JButton("Low Stock"); //adds button to see what items are low stock
  
  private StockReadWriter theStock     = null;
  private BackDoorController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public BackDoorView(  RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock = mf.makeStockReadWriter();          // Database access
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
    pageTitle.setText( "Staff check and manage stock" );                        
    cp.add( pageTitle );
    
    theBtQuery.setBounds( 16, 25+60*0, 80, 40 );    // Buy button 
    theBtQuery.addActionListener(                   // Call back code
      e -> cont.doQuery( theInput.getText() ) );
    cp.add( theBtQuery );                           //  Add to canvas

    theBtRStock.setBounds( 16, 25+60*1, 80, 40 );   // Check Button
    theBtRStock.addActionListener(                  // Call back code
      e -> cont.doRStock( theInput.getText(),
                          theInputNo.getText() ) );
    cp.add( theBtRStock );                          //  Add to canvas

    theBtClear.setBounds( 16, 25+60*2, 80, 40 );    // Buy button 
    theBtClear.addActionListener(                   // Call back code
      e -> cont.doClear() );
    cp.add( theBtClear );                           //  Add to canvas

 
    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 110, 50, 120, 40 );         // Input Area
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas
    
    theInputNo.setBounds( 260, 50, 120, 40 );       // Input Area
    theInputNo.setText("0");                        // 0
    cp.add( theInputNo );                           //  Add to canvas
    
    theBtDarkMode.setBounds(16,25 + 60*5, 150, 40);    //dark mode area
    theBtDarkMode.addActionListener(e -> {		//calls change theme code
    	isDarkMode = !isDarkMode; //toggles the state
    	setTheme(isDarkMode);
    });
    cp.add(theBtDarkMode); //add canvas
    
    theBtLowStock.setBounds(16,25+60*4, 150, 40); // button area
    theBtLowStock.addActionListener(e -> cont.checkLowStock());//calls back code
    cp.add(theBtLowStock);

    theSP.setBounds( 110, 100, 270, 160 );          // Scrolling pane
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea
    rootWindow.setVisible( true );                  // Make visible
    theInput.requestFocus();                        // Focus is here
  }
  
  public void setController( BackDoorController c )
  {
    cont = c;
  }
  
  public void displayMessage(String message) {
	  theOutput.setText(message);
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
   * Update the view, called by notifyObservers(theAction) in model,
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )  
  {
    BackDoorModel model  = (BackDoorModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();
  }

}