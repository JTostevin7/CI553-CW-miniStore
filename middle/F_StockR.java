package middle;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the 
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Product;


import debug.DEBUG;
import remote.RemoteStockR_I;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dbAccess.DBAccess;
import dbAccess.DBAccessFactory;
import middle.StockException;
import catalogue.Product;


/**
 * Setup connection to the middle tier
 */

public class F_StockR implements StockReader
{
  private RemoteStockR_I aR_StockR   = null;
  private String         theStockURL = null;
  private final DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();


  public F_StockR( String url )
  {
    DEBUG.trace("F_StockR: %s", url );
    theStockURL = url;
  }
  
  private void connect() throws StockException
  {
    try                                             // Setup
    {                                               //  connection
      aR_StockR =                                   //  Connect to
        (RemoteStockR_I) Naming.lookup(theStockURL);// Stub returned
    }
    catch ( Exception e )                           // Failure to
    {                                               //  attach to the
      aR_StockR = null;
      throw new StockException( "Com: " + 
                                e.getMessage()  );  //  object
      
    }
  }

  /**
   * Checks if the product exits in the stock list
   * @return true if exists otherwise false
   */

  public synchronized boolean exists( String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:exists()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.exists( number );
    } catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }
  

  /**
   * Returns details about the product in the stock list
   * @return StockNumber, Description, Price, Quantity
   */

  public synchronized Product getDetails( String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:getDetails()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.getDetails( number );
    } catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }
  
  
  public synchronized ImageIcon getImage( String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:getImage()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.getImage( number );
    }
    catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }
  
  @Override
  public List<Product> getLowStockItems(int threshold) throws StockException {
      List<Product> lowStockItems = new ArrayList<>();
      try (Connection conn = DriverManager.getConnection(
              dbDriver.urlOfDatabase(),
              dbDriver.username(),
              dbDriver.password());
           PreparedStatement stmt = conn.prepareStatement(
              "SELECT p.productNo, p.description, p.price, s.stockLevel " +
              "FROM ProductTable p " +
              "JOIN StockTable s ON p.productNo = s.productNo " +
              "WHERE s.stockLevel < ?")) {

          stmt.setInt(1, threshold); // Set the threshold
          ResultSet rs = stmt.executeQuery();

          while (rs.next()) {
              Product product = new Product(
                      rs.getString("productNo"),    // Product number
                      rs.getString("description"), // Description
                      rs.getDouble("price"),       // Price
                      rs.getInt("stockLevel")      // Stock level
              );
              lowStockItems.add(product);
          }
      } catch (SQLException e) {
          throw new StockException("Error fetching low stock items: " + e.getMessage());
      }
      return lowStockItems;
  }



  @Override
  public List<Product> searchByName(String name) throws StockException {
      List<Product> matchingProducts = new ArrayList<>();
      DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
      try (Connection conn = DriverManager.getConnection(
              dbDriver.urlOfDatabase(),
              dbDriver.username(),
              dbDriver.password());
           PreparedStatement stmt = conn.prepareStatement(
              "SELECT p.productNo, p.description, p.price, s.stockLevel " +
              "FROM ProductTable p " +
              "JOIN StockTable s ON p.productNo = s.productNo " +
              "WHERE LOWER(p.description) LIKE ?")) {

          stmt.setString(1, "%" + name.toLowerCase() + "%");
          ResultSet rs = stmt.executeQuery();

          while (rs.next()) {
              Product product = new Product(
                  rs.getString("productNo"),
                  rs.getString("description"),
                  rs.getDouble("price"),
                  rs.getInt("stockLevel")
              );
              matchingProducts.add(product);
          }
      } catch (SQLException e) {
          throw new StockException("Error searching by name: " + e.getMessage());
      }
      return matchingProducts;
  }
}