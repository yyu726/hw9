package com.example.hw9;

public class ItemSimilar {
    private String Image;
    private String title;
    private String Shipping;
    private String DaysLeft;
    private String Price;
    private String Onclick;


    public ItemSimilar() {
    }

    public ItemSimilar(String image, String title, String shipping, String daysLeft, String price, String onclick) {
        Image = image;
        this.title = title;
        Shipping = shipping;
        DaysLeft = daysLeft;
        Price = price;
        Onclick = onclick;
    }

    public String getImage() {
        return Image;
    }

    public String getShipping() {
        if(Shipping.equals("Free Shipping")){
            return "Free Shipping";
        }

        try{
            if(Double.parseDouble(Shipping)<=0.1){
                return "Free Shipping";
            }
        }catch(Exception e){
        }
        if(Shipping.charAt(0)=='$'){
            Shipping = Shipping.substring(1);
            try{
                if(Double.parseDouble(Shipping)<=0.1){
                    Shipping =  "Free Shipping";
                }
            }catch(Exception e){
            }

        }
        Shipping = "$"+Shipping;
        return Shipping;
    }

    public String getDaysLeft() {
        int start = DaysLeft.indexOf("P");
        int end = DaysLeft.indexOf("D");
        String day = DaysLeft.substring(start+1,end);
        if(Integer.parseInt(day)<=1){
            return day + " Day Left";
        }else{
            return day + " Days Left";
        }
    }

    public String getPrice() {
        return "$"+Price;
    }

    public String getOnclick() {
        return Onclick;
    }

    public String getTitle() {

        if (title.length() > 50) {
            title = title.substring(0, 50);
            int endOfTitle = title.lastIndexOf(" ");
            if (endOfTitle != -1) {
                title = title.substring(0, endOfTitle);
                title += "...";
            }

        }
        return title.trim();
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setShipping(String shipping) {
        Shipping = shipping;
    }

    public void setDaysLeft(String daysLeft) {
        DaysLeft = daysLeft;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setOnclick(String onclick) {
        Onclick = onclick;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
