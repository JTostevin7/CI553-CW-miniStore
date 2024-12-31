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
 * View of the model 
 */
public class CashierView extends JFrame implements Observer
{
  private static final int H = 420;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels
  
  private static final String CHECK  = "Check";
  private static final String BUY    = "Buy";
  private static final String BOUGHT = "Bought/Pay";

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( CHECK );
  private final JButton     theBtBuy   = new JButton( BUY );
  private final JButton     theBtBought= new JButton( BOUGHT );
  
  private final JButton theBtDarkMode = new JButton("Toggle Dark Mode");//adds dark mode toggle button
  private boolean isDarkMode = false; //tracks dark mode state
  
  private final JButton theBtDiscount = new JButton("Discount"); // adding in discount button
  
  private final JButton theBtUndo = new JButton("Undo"); //adding in undo button

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private CashierController cont       = null;
  
  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-coordinate of position of window on screen 
   * @param y     y-coordinate of position of window on screen  
   */
          
  public CashierView(  RootPaneContainer rpc,  MiddleFactory mf, int x, int y  )
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
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
    pageTitle.setText( "Thank You for Shopping at MiniStrore" );                        
    cp.add( pageTitle );  
    
    theBtCheck.setBounds( 16, 25+60*0, 80, 40 );    // Check Button
    theBtCheck.addActionListener(                   // Call back code
      e -> cont.doCheck( theInput.getText() ) );
    cp.add( theBtCheck );                           //  Add to canvas

    theBtBuy.setBounds( 16, 25+60*1, 80, 40 );      // Buy button 
    theBtBuy.addActionListener(                     // Call back code
      e -> cont.doBuy() );
    cp.add( theBtBuy );                             //  Add to canvas

    theBtBought.setBounds( 16, 25+60*3, 80, 40 );   // Bought Button
    theBtBought.addActionListener(                  // Call back code
      e -> cont.doBought() );
    cp.add( theBtBought );                          //  Add to canvas

    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 110, 50, 270, 40 );         // Input Area
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas
    
    theBtDarkMode.setBounds(16,25 + 60*5, 150, 40);    //dark mode area
    theBtDarkMode.addActionListener(e -> {		//calls back code
    	isDarkMode = !isDarkMode; //toggles the state
    	setTheme(isDarkMode);
    });
    cp.add(theBtDarkMode); //add canvas
    
    theBtDiscount.setBounds(16, 25+ 60*4, 90, 40); //Discount Button
    theBtDiscount.addActionListener(e -> cont.applyDiscount()); //call back code
    cp.add(theBtDiscount); //add to canvas
    
    theBtUndo.setBounds(16, 25 + 60*2, 80, 40);
    theBtUndo.addActionListener( e-> cont.doUndo()); //call back code
    cp.add(theBtUndo);

    theSP.setBounds( 110, 100, 270, 160 );          // Scrolling pane
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea
    rootWindow.setVisible( true );                  // Make visible
    theInput.requestFocus();                        // Focus is here
  }

  /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CashierController c )
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
  @Override
  public void update( Observable modelC, Object arg )
  {
    CashierModel model  = (CashierModel) modelC;
    String      message = (String) arg;
    theAction.setText( message );
    Basket basket = model.getBasket();
    if ( basket == null )
      theOutput.setText( "Customers order" );
    else
      theOutput.setText( basket.getDetails() );
    
    theInput.requestFocus();               // Focus is here
  }

}
