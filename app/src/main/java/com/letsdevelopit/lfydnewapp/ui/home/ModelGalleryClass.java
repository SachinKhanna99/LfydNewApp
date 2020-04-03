package com.letsdevelopit.lfydnewapp.ui.home;

public class ModelGalleryClass {



    private String URL;
    private String Name;



    public ModelGalleryClass(String name,String url) {

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

