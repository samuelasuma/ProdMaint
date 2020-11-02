package edu.metrostate.ics425.sa5213.prodmaint.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.metrostate.ics425.prodmaint.data.CatalogException;
import edu.metrostate.ics425.sa5213.prodmaint.db.ProductCatalog;
import edu.metrostate.ics425.sa5213.prodmaint.model.ProductBean;
import edu.metrostate.ics425.sa5213.prodmaint.controller.DateValidator;

/**
 * Servlet implementation class ProductMaintServlet
 */
@WebServlet("/")
public class ProductMaintServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	ProductCatalog catalog = ProductCatalog.getInstance();

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
	 *                          The processRequest method contains all the servlet
	 *                          paths.
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {

		String url = request.getServletPath();

		switch (url) {

		case "/landing":

			landing(request, response);
			break;

		case "/goLanding":

			goLanding(request, response);
			break;
		case "/edit":

			edit(request, response);
			break;

		case "/update":

			update(request, response);
			break;
		case "/delete":

			delete(request, response);
			break;
		case "/deleteAction":

			deleteAction(request, response);
			break;

		case "/add":

			add(request, response);
			break;
		default:
			request.getRequestDispatcher("/defaultError.jsp").forward(request, response);

			// throw new IllegalArgumentException("Unexpected value: " + url);
		}

	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 *                          This method supports all the functionality of the
	 *                          add product page. Performs input validation for non
	 *                          empty fields product code and description. Performs
	 *                          input validation for LocalDate formating with
	 *                          pattern to match. After input validation the method
	 *                          adds form fields to a list then add items from list
	 *                          to catalog. After all check are complete the method
	 *                          then redirects user to product page or user has
	 *                          option to cancel or reset.
	 */

	private void landing(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {

		// Cancel option
		var action = request.getParameter("action");
		if ("Cancel".equals(action)) {
			request.setAttribute("products", catalog.selectAll());
			request.getRequestDispatcher("/landing.jsp").forward(request, response);
		} else if ("Add".equals(action)) {

			// Create list and catalog

			var productCode = request.getParameter("productCode");
			var description = request.getParameter("description");
			var price = request.getParameter("price");
			var releaseDate = request.getParameter("releaseDate");

			// Input Validation

			if (productCode == null || productCode == "" && description == null || description == "") {
				final String inputError = "Product Code and Descritption cannot be left blank";
				request.setAttribute("inputError", inputError);

				request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
			} else {
				if (price == null || price == "" || releaseDate == "") {
					final String inputError = "Price and Release cannot be left blank";
					request.setAttribute("inputError", inputError);

					request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
				} else {
					if (!(isValidDate(releaseDate))) {
						final String inputError = "Date cannot be greater than today";
						request.setAttribute("inputError", inputError);

						request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
					} else {
						// Create product object and add to products list
						ProductBean product = new ProductBean(productCode, description, Double.parseDouble(price),
								LocalDate.parse(releaseDate));

						// Add products to catalog if no errors

						if (catalog.exists(product.getProductCode())) {
							final String errMsg = String.format("Product %s exists", productCode);
							log(errMsg);
							request.setAttribute("errMsg", errMsg);

							request.getRequestDispatcher("/addProduct.jsp").forward(request, response);

						} else {
							catalog.insert(product);
							request.setAttribute("products", catalog.selectAll());

							request.getRequestDispatcher("/landing.jsp").forward(request, response);

						}

					}

				}

			}

		}

		
	}

	/**
	 * @param request
	 * @param responsexf
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 *                          Method to redirect form index to landing page. Loads
	 *                          all items form catalog if any
	 */
	private void goLanding(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {

		request.setAttribute("products", catalog.selectAll());
		request.getRequestDispatcher("/landing.jsp").forward(request, response);
	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * 
	 *                          Method to redirect to add product page from product
	 *                          page
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
	 *                          Method that supports edit link on products in the
	 *                          product page. redirects to editProduct.jsp page
	 */
	private void edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {

		var productCode = request.getParameter("productCode");

		if (productCode != null && catalog.exists(productCode)) {
			var existProduct = catalog.select(productCode);
			request.setAttribute("product", existProduct);
			request.getRequestDispatcher("/editProduct.jsp").forward(request, response);
		} else {

			request.getRequestDispatcher("/productError.jsp").forward(request, response);

		}

	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException Method that supports delete link on products in the
	 *                          product page. redirects to deleteProduct.jsp page
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {

		var productCode = request.getParameter("productCode");

		if (productCode != null && catalog.exists(productCode)) {
			var existProduct = catalog.select(productCode);
			request.setAttribute("product", existProduct);
			request.getRequestDispatcher("/deleteProduct.jsp").forward(request, response);
		} else {

			request.getRequestDispatcher("/productError.jsp").forward(request, response);

		}

	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws CatalogException
	 * 
	 *                          Methods that supports edit page update functionality
	 * 
	 */
	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, CatalogException {

		var url = "/landing.jsp";

		var action = request.getParameter("action");
		if ("Cancel".equals(action)) {
			url = "landing.jsp";
			request.setAttribute("products", catalog.selectAll());
			request.getRequestDispatcher(url).forward(request, response);
		} else if ("Reset".equals(action)) {

			request.setAttribute("description", " ");

		} else if ("update".equals(action)) {

			var productCode = request.getParameter("productCode");
			var description = request.getParameter("description");
			var price = request.getParameter("price");
			var releaseDate = request.getParameter("releaseDate");

			if (productCode != null && description != null && price != null && releaseDate != null
					&& catalog.exists(productCode)) {

				if (price == "" || releaseDate == "") {
					final String inputError = "Price and Release cannot be left blank";
					request.setAttribute("inputError", inputError);

					request.getRequestDispatcher("/editProduct.jsp").forward(request, response);
				}
				ProductBean product = new ProductBean(productCode, description, Double.parseDouble(price),
						LocalDate.parse(releaseDate));
				catalog.update(product);

			} else {
				request.getRequestDispatcher("/productError.jsp").forward(request, response);
			}

		}
		request.setAttribute("products", catalog.selectAll());
		request.getRequestDispatcher(url).forward(request, response);

	}

	/**
	 * @param request
	 * @param response
	 * @throws CatalogException
	 * @throws ServletException
	 * @throws IOException
	 * 
	 *                          Method that supports delete page functionality
	 */
	private void deleteAction(HttpServletRequest request, HttpServletResponse response)
			throws CatalogException, ServletException, IOException {

		var action = request.getParameter("action");
		if ("Cancel".equals(action)) {
			request.setAttribute("products", catalog.selectAll());
			request.getRequestDispatcher("/landing.jsp").forward(request, response);
		} else if ("Confirm Delete".equals(action)) {

			var productCode = request.getParameter("productCode");

			if (productCode != null) {

				catalog.delete(productCode);
				log("Product Deleted: " + productCode);
				request.setAttribute("products", catalog.selectAll());
				request.getRequestDispatcher("/landing.jsp").forward(request, response);
			}

		}

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

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (CatalogException e) {

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

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (CatalogException e) {

			e.printStackTrace();
		}

	}

	public boolean isValidDate(String releaseDate) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US)
				.withResolverStyle(ResolverStyle.STRICT);
		DateValidator validator = new DateValidatorUsingDateTimeFormatter(dateFormatter);
		LocalDate now = LocalDate.now();
		LocalDate userDate = LocalDate.parse(releaseDate);

		if (!validator.isValid(releaseDate) || userDate.isAfter(now)) {
			return false;
		}

		return true;
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
