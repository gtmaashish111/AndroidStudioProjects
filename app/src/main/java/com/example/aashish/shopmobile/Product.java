package com.example.aashish.shopmobile;

public class Product {

    public String barcode;
    public String productName;
    public String productDescription;
    public String productPrice;
    public String productQuantity;
    public String imageUrl;

    public Product()

    {

    }

    public Product(String barcode, String productName,String productDescription,String productPrice, String productQuantity, String imageUrl)
    {
        this.barcode = barcode;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.imageUrl = imageUrl;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }
}
