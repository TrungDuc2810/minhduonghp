package com.example.TTTN.payload;

public class RevenueDto {
    private String label;
    private String revenue;
    private String profit;

    public RevenueDto(String label, String profit, String revenue) {
        this.label = label;
        this.revenue = revenue;
        this.profit = profit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
