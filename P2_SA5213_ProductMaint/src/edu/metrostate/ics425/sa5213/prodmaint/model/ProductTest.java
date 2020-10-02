package edu.metrostate.ics425.sa5213.prodmaint.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import edu.metrostate.ics425.prodmaint.data.CatalogException;
import edu.metrostate.ics425.prodmaint.data.ProductCatalog;



class ProductTest {
	ProductBean product = new ProductBean();
	ProductBean product1 = new ProductBean();
	ProductBean product2 = new ProductBean();
	ProductBean catalogProduct = new ProductBean("A100", "Speaker", 23.0, LocalDate.parse("2000-03-02"));
	ProductCatalog<ProductBean> catalog;

	/**
	 * Test that objects are instances of Class
	 */
	@Test
	public void testInstantiation() {

		assertTrue(product instanceof ProductBean);
		assertTrue(product1 instanceof ProductBean);
		assertTrue(product2 instanceof ProductBean);
		assertTrue(catalogProduct instanceof ProductBean);
	

	}

	/**
	 * Tests setter and getter methods
	 */
	@Test
	public void test() {
		product.setProductCode("A");
		product.setDescription("Welcome to Jamrock");
		product.setPrice(22.00);

		assertEquals(product.getProductCode(), "A");
		assertEquals(product.getDescription(), "Welcome to Jamrock");
		assertEquals(product.getPrice(), 22.00);

	}

	/**
	 * Tests the years released method for actions 1: Number of whole years to
	 * current date 2: -1 if release date is in the future. 3: -2 if release date is
	 * not set (is null).
	 */
	@Test
	public void yearsReleasedTest() {
		product.setReleaseDate(LocalDate.parse("2005-09-12"));
		assertEquals(product.getYearsReleased(), 15);

		product1.setReleaseDate(LocalDate.parse("2021-09-12"));
		assertEquals(product1.getYearsReleased(), -1);

		assertEquals(product2.getYearsReleased(), -2);

	}
	
	
	/**
	 * 
	 * Tests three methods of the Product Catalog Class. 
	 * Methods tested are the insert, select and delete.
	 * @throws CatalogException
	 */
	@Test 
	public void catalogTest() throws CatalogException {
		catalog = new ProductCatalog<ProductBean>();
		catalog.insert(catalogProduct);
		
		assertEquals(true, catalog.exists(catalogProduct.getProductCode()));
		assertEquals(catalogProduct, catalog.select(catalogProduct.getProductCode()));
		
		catalog.delete(catalogProduct.getProductCode());
		assertEquals(false, catalog.exists(catalogProduct.getProductCode()));
		
		
	
		
	}

}
