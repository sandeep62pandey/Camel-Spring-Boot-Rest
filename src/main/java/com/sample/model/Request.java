package com.sample.model;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class Request {

    @Pattern(regexp = "^2[0-9]{3}-[0-9]{2}$")
    private String year;

    @Valid
    private List<SalesDetails> salesDetails;

    @Valid
    private CustomerDetails customerDetails;


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<SalesDetails> getSalesDetails() {
        return salesDetails;
    }

    public void setSalesDetails(List<SalesDetails> salesDetails) {
        this.salesDetails = salesDetails;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    @Override
    public String toString() {
        return "Request{" +
                "year='" + year + '\'' +
                ", salesDetails=" + salesDetails +
                ", customerDetails=" + customerDetails +
                '}';
    }

    public static class SalesDetails{

        @DecimalMin(value ="0")
        @DecimalMax(value = "999999999.99")
        private double amount;

        @Valid
        private boolean paid;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        @Override
        public String toString() {
            return "SalesDetails{" +
                    "amount=" + amount +
                    ", paid=" + paid +
                    '}';
        }
    }

    public static   class CustomerDetails{

        @Pattern(regexp = "^[0-9a-zA-Z]{1,90}$")
        private String customerReference;

        public String getCustomerReference() {
            return customerReference;
        }

        public void setCustomerReference(String customerReference) {
            this.customerReference = customerReference;
        }

        @Override
        public String toString() {
            return "CustomerDetails{" +
                    "customerReference='" + customerReference + '\'' +
                    '}';
        }
    }
}


