package clients.backDoor;

import catalogue.Product;
import java.util.List;
import middle.StockException;


/**
 * The BackDoor Controller
 */

public class BackDoorController
{
  private BackDoorModel model = null;
  private BackDoorView  view  = null;
  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public BackDoorController( BackDoorModel model, BackDoorView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Query interaction from view
   * @param pn The product number to be checked
   */
  public void doQuery( String pn )
  {
    model.doQuery(pn);
  }
  
  /**
   * Low stock interaction from View
   */
  public void checkLowStock() {
	  try {
		  List<Product> lowStockItems = model.getLowStockItems(5);
		  StringBuilder result = new StringBuilder("Low Stock Products:\n");
		  for(Product product: lowStockItems) {
			  result.append(String.format("%s : %5.2f (%d)\n",
					  product.getDescription(), product.getPrice(), product.getQuantity()));
		  }
		  view.displayMessage(result.toString());
	  } catch (StockException e) {
		  view.displayMessage("Error checking low stock items: " + e.getMessage());
	  }
  }
  
  /**
   * RStock interaction from view
   * @param pn       The product number to be re-stocked
   * @param quantity The quantity to be re-stocked
   */
  public void doRStock( String pn, String quantity )
  {
    model.doRStock(pn, quantity);
  }

  /**
   * Clear interaction from view
   */
  public void doClear()
  {
    model.doClear();
  }

  
}

