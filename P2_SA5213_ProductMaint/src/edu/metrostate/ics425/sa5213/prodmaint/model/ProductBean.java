package edu.metrostate.ics425.sa5213.prodmaint.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import edu.metrostate.ics425.prodmaint.model.Product;



public class ProductBean implements Serializable, Product {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String prouductCode, description;
	private Double price;
	private LocalDate releaseDate;
	
	

	public ProductBean() {
		super();
		
	}

	public ProductBean(String prouductCode, String description, Double price, LocalDate releaseDate) {
		
		this.prouductCode = prouductCode;
		this.description = description;
		this.price = price;
		this.releaseDate = releaseDate;
	}

	

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public Double getPrice() {

		return price;
	}

	@Override
	public String getProductCode() {

		return prouductCode;
	}

	@Override
	public LocalDate getReleaseDate() {

		return releaseDate;
	}

	@Override
	public int getYearsReleased() {
		LocalDate current = LocalDate.now();

		if (releaseDate == null) {
			return -2;
		} else if (releaseDate.isAfter(current)) {
			return -1;
		} else {
			Period difference = Period.between(releaseDate, current);
			int years = difference.getYears();
			return years;
		}
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public void setProductCode(String productCode) {
		this.prouductCode = productCode;
	}

	@Override
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prouductCode == null) ? 0 : prouductCode.toLowerCase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ProductBean))
			return false;
		ProductBean other = (ProductBean) obj;
		if (prouductCode == null) {
			if (other.prouductCode != null)
				return false;
		} else if (!prouductCode.equalsIgnoreCase(other.prouductCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProductBean [getDescription()=" + getDescription() + ", getPrice()=" + getPrice()
				+ ", getProductCode()=" + getProductCode() + ", getReleaseDate()=" + getReleaseDate()
				+ ", getYearsReleased()=" + getYearsReleased() + ", hashCode()=" + hashCode() + "]";
	}

}
