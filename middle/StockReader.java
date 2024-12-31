package middle;

import catalogue.Product;
import catalogue.SalesSummary;
import javax.swing.*;
import java.util.List;

/**
  * Interface for read access to the stock list.
  * @author  Mike Smith University of Brighton
  * @version 2.0
  */

public interface StockReader
{

 /**
   * Checks if the product exits in the stock list
   * @param pNum Product nymber
   * @return true if exists otherwise false
   * @throws StockException if issue
   */
  boolean exists(String pNum) throws StockException;
         
  /**
   * Returns details about the product in the stock list
   * @param pNum Product nymber
   * @return StockNumber, Description, Price, Quantity
   * @throws StockException if issue
   */
  
  Product getDetails(String pNum) throws StockException;
  
  /**
   * Returns a list of all low stock items.
   * @Param threshold threhold of stock to go under
   * @return list of items under specified amount
   * @throws StockException if issue
   */
  List<Product> getLowStockItems(int threshold) throws StockException;
  
  
  /**
   * Returns an image of the product in the stock list
   * @param pNum Product nymber
   * @return Image
   * @throws StockException if issue
   */
  
  ImageIcon getImage(String pNum) throws StockException;
  /**
   * Returns list of names of products if it matches
   * @param name of the product
   * @return productname, price, quantity
   * @throws StockException if issue
   */
  List<Product> searchByName(String name) throws StockException;
  
  /**
   * returns list of all sales
   * @return Sales Summary
   * @throws StockException
   */
  
  List<SalesSummary> getSalesSummary() throws StockException;
 
}


