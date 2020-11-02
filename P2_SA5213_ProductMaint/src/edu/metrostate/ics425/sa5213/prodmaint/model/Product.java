package edu.metrostate.ics425.sa5213.prodmaint.model;

import java.time.LocalDate;

public interface Product {
    public String getProductCode();
    public void setProductCode(String code);
    public String getDescription();
    public void setDescription(String description);
    public Double getPrice();
    public void setPrice(Double price);
    public LocalDate getReleaseDate();
    public void setReleaseDate(LocalDate newreleaseDate);
    public int getYearsReleased();
}
