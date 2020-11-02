package edu.metrostate.ics425.sa5213.prodmaint.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.time.LocalDate;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import edu.metrostate.ics425.prodmaint.data.CatalogException;
import edu.metrostate.ics425.prodmaint.model.Product;
import edu.metrostate.ics425.sa5213.prodmaint.model.ProductBean;

public class ProductCatalog {

	private static ProductCatalog catalog;
	private DataSource ds;
	static {
		catalog = new ProductCatalog();
		Logger.getAnonymousLogger().info("connection pool created");
	}

	private ProductCatalog() {
		// Obtain our environment naming context
		Context initCtx;
		try {
			initCtx = new InitialContext();

			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			// Look up our data source
			ds = (DataSource) envCtx.lookup("jdbc/prodmaint");
			Logger.getAnonymousLogger().info("lookup successful");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns the instance of the product catalog
	 * 
	 * @return an instance of the product catalog
	 */
	public static ProductCatalog getInstance() {
		return catalog;
	}
	
	/**
	 * Returns a list of all Products from the Product Catalog
	 * 
	 * @return a list of products
	 * @throws CatalogException if there is a problem accessing the catalog
	 */
	public Product select(String productCode) throws CatalogException {
		String SELECT_SQL = "SELECT ProductCode, ProductDescription, ProductPrice, ProductReleaseDate FROM Product WHERE ProductCode = ?;";
		Product product = null;

		try (var conn = ds.getConnection();
				var pstmt = prepPreparedStatement(SELECT_SQL, conn, productCode);
				var rs = pstmt.executeQuery();) {
			
		
			while (rs.next()) {
				
				product = new ProductBean(rs.getString("ProductCode"));
				product.setDescription(rs.getString("ProductDescription"));
				product.setPrice(rs.getDouble("ProductPrice"));
				product.setReleaseDate(rs.getDate("ProductReleaseDate").toLocalDate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CatalogException(e);
		}

		return product;
	}
	
	/**
	 * 
	 * Deletes product from database 
	 * 
	 * @param productCode
	 * @return delete product
	 * @throws CatalogException
	 */
	public Product delete(String productCode) throws CatalogException {
		String DELETE_SQL = "DELETE FROM Product WHERE ProductCode = ?";
		Product product = null;

		try (var conn = ds.getConnection();
				var pstmt = prepPreparedStatement(DELETE_SQL, conn, productCode);
				 ) {
			pstmt.executeUpdate();
		
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CatalogException(e);
		}

		return product;
	}

	/**
	 * 
	 * PreparedStatement Helper Method
	 * 
	 * @param SELECT_SQL
	 * @param conn
	 * @param productCode
	 * @return prepared statement helper method
	 * @throws SQLException
	 */
	private PreparedStatement prepPreparedStatement(String SELECT_SQL, Connection conn, String productCode) throws SQLException {
		var pstmt =  conn.prepareStatement(SELECT_SQL);
		pstmt.setString(1, productCode);
		return pstmt;
	}

	/**
	 * Returns a list of all Products from the Product Catalog
	 * 
	 * @return a list of products
	 * @throws CatalogException if there is a problem accessing the catalog
	 */
	public List<Product> selectAll() throws CatalogException {
		String SELECT_SQL = "SELECT ProductCode, ProductDescription, ProductPrice, ProductReleaseDate FROM Product;";
		List<Product> products = new LinkedList<>();

		try (var conn = ds.getConnection();
				var stmt = conn.createStatement();
				var rs = stmt.executeQuery(SELECT_SQL);) {
			while (rs.next()) {
				ProductBean pb = new ProductBean(rs.getString("ProductCode"));
				pb.setDescription(rs.getString("ProductDescription"));
				pb.setPrice(rs.getDouble("ProductPrice"));
				pb.setReleaseDate(rs.getDate("ProductReleaseDate").toLocalDate());
				products.add(pb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CatalogException(e);
		}

		return products;
	}
	
	
	
	
   
    /**
     * Check's whether product exists in database
     * 
     * @param productCode
     * @return returns boolean product if exists
     * @throws CatalogException
     */
    public boolean exists(String productCode) throws CatalogException{
    	if (this.select(productCode) != null) {
    		return true;
			
		}
        return false;
    }

	
	/**
	 * Inserts new product in database
	 * 
	 * @param product
	 * @return boolean if product inserted into database
	 * @throws CatalogException
	 */
	public boolean insert(Product product) throws CatalogException {
		final String INSERT_SQL = "INSERT INTO Product (ProductCode, ProductDescription, ProductPrice, ProductReleaseDate) VALUES (?, ?, ?, ?)";

		try (var conn = ds.getConnection();
				var pstmt = prepPreparedStatement(INSERT_SQL, conn, product.getProductCode(), product.getDescription(), product.getPrice(), product.getReleaseDate());
				 ) {
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CatalogException(e);
		}
		return false;
      
	}
	
	/**
	 * Updated the product in database
	 * 
	 * @param product
	 * @return boolean if product updated
	 * @throws CatalogException
	 */
	public boolean update(Product product) throws CatalogException {
		final String UPDATE_SQL = "UPDATE Product SET  ProductDescription = ?, ProductPrice = ?, ProductReleaseDate = ? WHERE ProductCode = ?";

		try (var conn = ds.getConnection();
				var pstmt = prepPreparedStatement(UPDATE_SQL, conn, product.getDescription(), product.getPrice(), product.getReleaseDate(), product.getProductCode());
				 ) {
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CatalogException(e);
		}
		return false;
      
	}
	
	/**
	 * PreparedStatement Helper Method
	 * 
	 * @param INSERT_SQL
	 * @param conn
	 * @param description
	 * @param price
	 * @param releaseDate
	 * @param productCode
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	private PreparedStatement prepPreparedStatement(String INSERT_SQL, Connection conn,  String description,
			Double price, LocalDate releaseDate, String productCode) throws SQLException {
		var pstmt =  conn.prepareStatement(INSERT_SQL);
		
	
		pstmt.setString(1, description);
		pstmt.setDouble(2, price);
		pstmt.setObject(3, releaseDate);
		pstmt.setString(4, productCode);
		
		return pstmt;
	}

	/**
	 * PreparedStatement Helper Method
	 *
	 * @param INSERT_SQL
	 * @param conn
	 * @param productCode
	 * @param description
	 * @param price
	 * @param releaseDate
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	private PreparedStatement prepPreparedStatement(String INSERT_SQL, Connection conn, String productCode, String description,
			Double price, LocalDate releaseDate) throws SQLException {
		var pstmt =  conn.prepareStatement(INSERT_SQL);
		
		pstmt.setString(1, productCode);
		pstmt.setString(2, description);
		pstmt.setDouble(3, price);
		pstmt.setObject(4, releaseDate);
		return pstmt;
	}

}
