package com.visitor;

public class Address {

    private  String Street;
    private  String suburbs;
    private  int postalCode;
    private  String city;

    @Override
    public String toString() {
        return "Address{" +
                "Street='" + Street + '\'' +
                ", suburbs='" + suburbs + '\'' +
                ", postalCode=" + postalCode +
                ", city='" + city + '\'' +
                '}';
    }

    public Address(String street, String suburbs, int postalCode, String city) {
        Street = street;
        this.suburbs = suburbs;
        this.postalCode = postalCode;
        this.city = city;
    }
    public  Address(Address address){
        Street = address.Street; ;
        this.suburbs = address.suburbs;
        this.postalCode = address.postalCode;
        this.city = address.city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getSuburbs() {
        return suburbs;
    }

    public void setSuburbs(String suburbs) {
        this.suburbs = suburbs;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
