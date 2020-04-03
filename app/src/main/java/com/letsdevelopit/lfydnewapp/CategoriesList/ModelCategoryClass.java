package com.letsdevelopit.lfydnewapp.CategoriesList;

public class ModelCategoryClass {



    private String URL;
    private String Name;



    public ModelCategoryClass(String name, String url) {

        this.URL = url;
        this.Name = name;


    }

    public String getName() {
        return Name;
    }

    public String getURL() {
        return URL;
    }

}

