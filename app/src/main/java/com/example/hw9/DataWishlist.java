package com.example.hw9;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class DataWishlist extends BaseObservable {
    private int countNum = 0;
    private double subtotalNum = 0.0;


    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    public int getCountNum() {
        return countNum;
    }

    public void addCountNum() {
        countNum++;
    }

    public void subtractCountNum() {
        countNum--;
    }
    public void setSubtotalNum(int subtotalNum) {
        this.subtotalNum = subtotalNum;
    }
    public void addSubtotalNum(double num) {
        subtotalNum += num;
    }
    public void subtractSubtotalNum(double num) {
        subtotalNum -= num;
        if (subtotalNum < 0) {
            subtotalNum = 0;
        }
    }

    @Bindable
    public String getCount() {
        return "Wishlist total(" + countNum + " items):";
    }

    @Bindable
    public String getSubtotal() {
        return "$" + String.format("%.2f", subtotalNum);
    }


}
