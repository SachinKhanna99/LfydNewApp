package com.letsdevelopit.lfydnewapp.ui.qrscanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRScannerViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public QRScannerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is qr fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
