package com.example.lab04;

public class Product {

    private int _id;
    private String _productName;
    private double _price;

    public Product(){

    }

    public Product(String productName, double price){
        _productName = productName;
        _price = price;
    }

    public Product(int id, String productName, double price){
        _id = id;
        _productName = productName;
        _price = price;
    }

    public void setID(int id){
        _id = id;
    }

    public int getID(){
        return _id;
    }

    public void setProductName(String productName){
        _productName = productName;
    }

    public String getProductName(){
        return _productName;
    }

    public void setPrice(double price){
        _price = price;
    }

    public double getPrice(){
        return _price;
    }

}
