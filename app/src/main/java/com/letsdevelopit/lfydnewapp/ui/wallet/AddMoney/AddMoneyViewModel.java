package com.letsdevelopit.lfydnewapp.ui.wallet.AddMoney;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddMoneyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddMoneyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}