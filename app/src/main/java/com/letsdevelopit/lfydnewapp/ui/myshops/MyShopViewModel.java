package com.letsdevelopit.lfydnewapp.ui.myshops;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyShopViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyShopViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}