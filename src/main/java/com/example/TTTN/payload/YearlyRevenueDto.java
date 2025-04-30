package com.example.TTTN.payload;

public class YearlyRevenueDto {
    private int year;
    private String revenue;

    public YearlyRevenueDto(int year, String revenue) {
        this.year = year;
        this.revenue = revenue;
    }

    // Getters & Setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }
}
