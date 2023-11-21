package com.example.android_demo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private static MutableLiveData<String> username;
    private static MutableLiveData<String> age;

    public MainViewModel(){
    }

    public MutableLiveData<String> getUsername() {
        if (username == null) {
            username = new MutableLiveData<String>();
        }
        return username;
    }
    public MutableLiveData<String> getAge() {
        if (age == null) {
            age = new MutableLiveData<String>();
        }
        return age;
    }

}
