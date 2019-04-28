package com.example.hw9;

import org.json.JSONObject;

public class Item {

    private String id;
    private String title;
    private String image;
    private String condition;
    private String price;
    private String shipping;
    private String zip;
    private JSONObject obj;

    public Item() {

    }

    public Item(String title, String image) {
        setTitle(title);
        this.image = image;
    }

    public Item(String id, String title, String image, String condition, String price, String shipping, String zip) {
        this.id = id;
        setTitle(title);
        this.image = image;
        this.condition = condition;
        this.price = price;
        this.shipping = shipping;
        this.zip = zip;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
/*        if (title.length() > 60) {
            title = title.substring(0, 60);
            int lastIndex = title.lastIndexOf(" ");
            if (lastIndex != -1) {
                title = title.substring(0, lastIndex);
                title += "...";
            }
        }*/
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
