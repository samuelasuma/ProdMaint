package edu.metrostate.ics425.sa5213.prodmaint.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.metrostate.ics425.prodmaint.data.CatalogException;
import edu.metrostate.ics425.prodmaint.data.ProductCatalog;

import edu.metrostate.ics425.sa5213.prodmaint.model.ProductBean;

/**
 * Servlet implementation class ProductMaintServlet
 */
@WebServlet("/")
public class ProductMaintServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	ProductCatalog<ProductBean> catalog;

	/**
	 * Default constructor.
	 */
	public ProductMaintServlet() {
		super();

	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 * The processRequest method contains all the servlet paths.
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {

		String url = request.getServletPath();
		switch (url) {

		case "/landing": {

			landing(request, response);
		}
		
		case "/goLanding": {

			goLanding(request, response);
		}
		case "/edit": {

			edit(request, response);
		}
		case "/update": {

			update(request, response);
		}
		case "/delete": {

			delete(request, response);
		}
		case "/deleteAction": {

			deleteAction(request, response);
		}

		case "/add": {

			add(request, response);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + url);
		}

	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 * This method supports all the functionality of the add product page. 
	 * Performs input validation for non empty fields product code and description. 
	 * Performs input validation for LocalDate formating with pattern to match.
	 * After input validation the method adds form fields to a list then add items from list to catalog.
	 * After all check are complete the method then redirects user to product page or user has option to cancel or reset.
	 */
	private void landing(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {
		
		//Cancel option
		var action = request.getParameter("action");
		if ("Cancel".equals(action)) {
			request.getRequestDispatcher("/landing.jsp").forward(request, response);
		}
		
		//Create list and catalog
		ArrayList<ProductBean> products;
		catalog = new ProductCatalog<ProductBean>();
	if (request.getServletContext().getAttribute("products") != null && request.getServletContext().getAttribute("products") instanceof ArrayList) {
		 products = (ArrayList<ProductBean>) getServletContext().getAttribute("products");
	}else {
		 products = new ArrayList<>();
	}
		
		var productCode = request.getParameter("productCode");
		var description = request.getParameter("description");
		var price = request.getParameter("price");
		var releaseDate = request.getParameter("releaseDate");
		
		// Input Validation
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US)
			    .withResolverStyle(ResolverStyle.STRICT);
			DateValidator validator = new DateValidatorUsingDateTimeFormatter(dateFormatter);
			LocalDate now = LocalDate.now();
			LocalDate userDate = LocalDate.parse(releaseDate);
		
		if (!(validator.isValid(releaseDate)) || userDate.isAfter(now)) {
			final String dateError = "Please enter a valid date formatted date as such yyyy-mm-dd";
			request.setAttribute("dateError", dateError);
			
			request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
		}
		
		if (Double.parseDouble(price) < 0 ) {
			final String priceError = "Price cannot be negative";
			request.setAttribute("priceError", priceError);
			
			request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
		}
		
		
		if (productCode == null || productCode == "" && description == null || description == "") {
			final String inputError = "Product Code and Descritption cannot be left blank";
			request.setAttribute("inputError", inputError);
			
			request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
		} else {
			if (price == null || price == "" || releaseDate == "") {
				price ="0.0";
				releaseDate = "0001-01-01";
			}
			
			//Create product object and add to products list
			ProductBean product = new ProductBean(productCode, description, Double.parseDouble(price),
					LocalDate.parse(releaseDate));
			products.add(product);
		}
		
		//Add products to catalog if no errors
		for (ProductBean productBean : products) {
		
			if (catalog.exists(productCode)) {
				final String errMsg = String.format("Product exists: %s", productCode);
				log(errMsg);
				request.setAttribute("errMsg", errMsg);
				
				request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
				

			} else {
				catalog.insert(productBean);
				
			}
		}
		
		

		request.getServletContext().setAttribute("products", catalog.selectAll());

		request.getRequestDispatcher("/landing.jsp").forward(request, response);

		request.getServletContext().setAttribute("products", products);
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 * Method to redirect form index to landing page. 
	 * Loads all items form catalog if any
	 */
	private void goLanding(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CatalogException {
		
		
		

		request.getRequestDispatcher("/landing.jsp").forward(request, response);
		request.getServletContext().setAttribute("products", catalog.selectAll());
	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * 
	 * Method to redirect to add product page from product page
	 */
	private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 * Method that supports edit link on products in the product page. 
	 * redirects to editProduct.jsp page
	 */
	private void edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {
		
		

		var productCode = request.getParameter("productCode");
		System.out.println(productCode);

		ProductBean existProduct = catalog.exists(productCode) ? existProduct = catalog.select(productCode) : null;

		request.setAttribute("product", existProduct);

		request.getRequestDispatcher("/editProduct.jsp").forward(request, response);
	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * Method that supports delete link on products in the product page. 
	 * redirects to deleteProduct.jsp page
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {
		
		

		var productCode = request.getParameter("productCode");
		System.out.println(productCode);

		ProductBean existProduct = catalog.exists(productCode) ? existProduct = catalog.select(productCode) : null;

		request.setAttribute("product", existProduct);

		request.getRequestDispatcher("/deleteProduct.jsp").forward(request, response);
	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 * Methods that supports edit page update functionality 
	 * 
	 */
	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {
		
		
		var action = request.getParameter("action");
		if ("Cancel".equals(action)) {
			request.getRequestDispatcher("/landing.jsp").forward(request, response);
		}else if ("Reset".equals(action)) {
			
			request.setAttribute("description", " ");
			
		}else if ("update".equals(action)) {
			request.getRequestDispatcher("/underconstruction.jsp").forward(request, response);
		}

		var productCode = request.getParameter("productCode");
		var description = request.getParameter("description");
		var price = request.getParameter("price");
		var releaseDate = request.getParameter("releaseDate");
		if (productCode != null && description != null && price != null && releaseDate != null) {
			ProductBean product = new ProductBean(productCode, description, Double.parseDouble(price),
					LocalDate.parse(releaseDate));
			catalog.update(product);
		}
		request.setAttribute("product", catalog.selectAll());

		request.getRequestDispatcher("/landing.jsp").forward(request, response);

	}

	/**
	 * @param request
	 * @param response
	 * @throws CatalogException
	 * @throws ServletException
	 * @throws IOException
	 * 
	 * Method that supports delete page functionality 
	 */
	private void deleteAction(HttpServletRequest request, HttpServletResponse response)
			throws CatalogException, ServletException, IOException {
		
		
		
		var action = request.getParameter("action");
		if ("Cancel".equals(action)) {
			request.getRequestDispatcher("/landing.jsp").forward(request, response);
		}else if ("Delete".equals(action)) {
			request.getRequestDispatcher("/underconstruction.jsp").forward(request, response);
		}
		var productCode = request.getParameter("productCode");
		var description = request.getParameter("description");
		var price = request.getParameter("price");
		var releaseDate = request.getParameter("releaseDate");
		if (productCode != null && description != null && price != null && releaseDate != null) {

			catalog.delete(productCode);
		}
		request.setAttribute("product", catalog.selectAll());

		request.getRequestDispatcher("/landing.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			processRequest(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CatalogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CatalogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/**
	 * @author https://www.baeldung.com/java-string-valid-date
	 *
	 */
	public class DateValidatorUsingDateTimeFormatter implements DateValidator {
	    private DateTimeFormatter dateFormatter;
	    
	    public DateValidatorUsingDateTimeFormatter(DateTimeFormatter dateFormatter) {
	        this.dateFormatter = dateFormatter;
	    }
	 
	    @Override
	    public boolean isValid(String dateStr) {
	        try {
	            this.dateFormatter.parse(dateStr);
	        } catch (DateTimeParseException e) {
	            return false;
	        }
	        return true;
	    }
	}
	
	
	
	
	
}
