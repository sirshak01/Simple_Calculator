package com.calculator.pro.ui;

import android.widget.TextView;

public class MainUi {

    private TextView display;

    public MainUi(TextView display) {
        this.display = display;
    }

    public void update(String value) {
        display.setText(value == null || value.equals("") ? "0" : value);
    }
}
