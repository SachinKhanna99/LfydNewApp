package com.letsdevelopit.lfydnewapp.ui.dashboard;

public class ModelFavShopListClass {



    private String imagefav;
    private String shopname;
    private String discount;
    private String address,cashback,suid,Book;

    double lan1 , lon1 , lan2, lon2;

    String Type;



    public ModelFavShopListClass(String imagefav, String shopname, String address, String discount, String cashback, double la , double lon, double la2 , double lon2 ,  String a, String type, String book) {

        this.imagefav = imagefav;
        this.shopname = shopname;
        this.address=address;
        this.discount = discount;
        this.cashback=cashback;
        this.lan1 = la;
        this.lon1 = lon;
        this.lan2 = la2;
        this.lon2 = lon2;
        this.suid = a;
        this.Type = type;
        this.Book = book;
    }

    public double getLon1() {
        return lon1;
    }

    public double getLon2() {
        return lon2;
    }

    public double getLan2() {
        return lan2;
    }

    public double getLan1() {
        return lan1;
    }

    public String getBook() {
        return Book;
    }

    public String getImagefav() {
        return imagefav;
    }


    public String getType() {
        return Type;
    }

    public String getSuid() {
        return suid;
    }



    public String getAddress() {
        return address;
    }

    public String getCashback() {
        return cashback;
    }

    public String getDiscount() {
        return discount;
    }


    public String getShopname() {
        return shopname;
    }


}

