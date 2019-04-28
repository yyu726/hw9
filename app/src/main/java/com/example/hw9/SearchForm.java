package com.example.hw9;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchForm extends BaseObservable {
    private String keyword = "";
    private int selectedCategoryPosition = 0;
    private static final int[] category = new int[]{0, 550, 2984, 267, 1145, 58058, 26395, 11233, 1249};
    private boolean conditionNew = false;
    private boolean conditionUsed = false;
    private boolean conditionUnspecified = false;
    private boolean shippingLocal = false;
    private boolean shippingFree = false;
    private boolean nearbySearch = false;
    private String distance = "";
    private boolean hereRadio = true;
    private boolean zipRadio = false;
    private String currentZip = "90008";
    private String customZip = "";


    public SearchForm() {

    }

    @Bindable
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        if (this.keyword != keyword) {
            this.keyword = keyword;
            notifyPropertyChanged(BR.keyword);
        }
    }

    @Bindable
    public int getSelectedCategoryPosition() {
        return selectedCategoryPosition;
    }
    public void setSelectedCategoryPosition(int selectedCategoryPosition) {
        this.selectedCategoryPosition = selectedCategoryPosition;
        notifyPropertyChanged(BR.selectedCategoryPosition);
    }

    @Bindable
    public boolean getConditionNew() {
        return conditionNew;
    }
    public void setConditionNew(boolean conditionNew) {
        if (this.conditionNew != conditionNew) {
            this.conditionNew = conditionNew;
            notifyPropertyChanged(BR.conditionNew);
        }
    }

    @Bindable
    public boolean getConditionUsed() {
        return conditionUsed;
    }
    public void setConditionUsed(boolean conditionUsed) {
        if (this.conditionUsed != conditionUsed) {
            this.conditionUsed = conditionUsed;
            notifyPropertyChanged(BR.conditionUsed);
        }
    }

    @Bindable
    public boolean getConditionUnspecified() {
        return conditionUnspecified;
    }
    public void setConditionUnspecified(boolean conditionUnspecified) {
        if (this.conditionUnspecified != conditionUnspecified) {
            this.conditionUnspecified = conditionUnspecified;
            notifyPropertyChanged(BR.conditionUnspecified);
        }
    }

    @Bindable
    public boolean getShippingLocal() {
        return shippingLocal;
    }
    public void setShippingLocal(boolean shippingLocal) {
        if (this.shippingLocal != shippingLocal) {
            this.shippingLocal = shippingLocal;
            notifyPropertyChanged(BR.shippingLocal);
        }
    }

    @Bindable
    public boolean getShippingFree() {
        return shippingFree;
    }
    public void setShippingFree(boolean shippingFree) {
        if (this.shippingFree != shippingFree) {
            this.shippingFree = shippingFree;
            notifyPropertyChanged(BR.shippingFree);
        }
    }

    @Bindable
    public boolean getNearbySearch() {
        return nearbySearch;
    }
    public void setNearbySearch(boolean nearbySearch) {
        if (this.nearbySearch != nearbySearch) {
            this.nearbySearch = nearbySearch;
            notifyPropertyChanged(BR.nearbySearch);
        }
    }

    @Bindable
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        if (this.distance != distance) {
            this.distance = distance;
            notifyPropertyChanged(BR.distance);
        }
    }

    @Bindable
    public boolean getHereRadio() {
        return hereRadio;
    }
    public void setHereRadio(boolean hereRadio) {
        if (this.hereRadio != hereRadio) {
            this.hereRadio = hereRadio;
            notifyPropertyChanged(BR.hereRadio);
        }
    }

    @Bindable
    public boolean getZipRadio() {
        return zipRadio;
    }
    public void setZipRadio(boolean zipRadio) {
        if (this.zipRadio != zipRadio) {
            this.zipRadio = zipRadio;
            notifyPropertyChanged(BR.zipRadio);
        }
    }


    public String getCurrentZip() {
        return currentZip;
    }
    public void setCurrentZip(String currentZip) {

            this.currentZip = currentZip;

    }

    @Bindable
    public String getCustomZip() {
        return customZip;
    }
    public void setCustomZip(String customZip) {
        if (this.customZip != customZip) {
            this.customZip = customZip;
            notifyPropertyChanged(BR.customZip);
        }
    }

    public String showInstance() {
        String res = "";
        res += "\nkeyword=" + keyword;
        res += "\ncategory=" + category[selectedCategoryPosition];
        res += "\nconditionNew=" + conditionNew;
        res += "\nconditionUsed=" + conditionUsed;
        res += "\nconditionUnspecified=" + conditionUnspecified;
        res += "\nshippingLocal=" + shippingLocal;
        res += "\nshippingFree=" + shippingFree;
        res += "\nnearbySearch=" + nearbySearch;
        res += "\ndistance=" + distance;
        res += "\nhereRadio=" + hereRadio;
        res += "\nzipRadio=" + zipRadio;
        res += "\nzip=" + (hereRadio ? currentZip : customZip);
        return res;
    }
    public String getSearchUrl() {
        String url = "";
        url += "http://yyu726-cs571-hw8.us-west-1.elasticbeanstalk.com/?";
        url += "&type=search";
        url += "&query=";

        try {
            String s = new String(keyword);
            url += "&keywords=" + URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ;
        if (selectedCategoryPosition != 0) {
             url += "&categoryId=" + category[selectedCategoryPosition];
        }

        int filterCount = 0;

        if (nearbySearch) {
            url += "&buyerPostalCode=";
            url += hereRadio ? currentZip : customZip;
            url += "&itemFilter(0).name=MaxDistance&itemFilter(0).value=" + (distance.equals("") ? "10" : distance);
            filterCount++;
        }

        if (shippingFree) {
            url += "&itemFilter(" + filterCount + ").name=FreeShippingOnly";
            url += "&itemFilter(" + filterCount + ").value=true";
            filterCount++;
        }

        if (shippingLocal) {
            url += "&itemFilter(" + filterCount + ").name=LocalPickupOnly";
            url += "&itemFilter(" + filterCount + ").value=true";
            filterCount++;
        }

        url += "&itemFilter(" + filterCount + ").name=HideDuplicateItems";
        url += "&itemFilter(" + filterCount + ").value=true";
        filterCount++;

        if (conditionNew || conditionUsed || conditionUnspecified) {
            int conditionCount = 0;
            url += "&itemFilter(" + filterCount + ").name=Condition";
            if (conditionNew) {
                url += "&itemFilter(" + filterCount + ").value(" + conditionCount + ")=New";
                conditionCount++;
            }
            if (conditionUsed) {
                url += "&itemFilter(" + filterCount + ").value(" + conditionCount + ")=Used";
                conditionCount++;
            }
            if (conditionUnspecified) {
                url += "&itemFilter(" + filterCount + ").value(" + conditionCount + ")=Unspecified";
                conditionCount++;
            }
            filterCount++;
        }
        url += "&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";

        return url;
    }
    public void reset() {
        setKeyword("");
        setSelectedCategoryPosition(0);
/*        setConditionNew(false);
        setConditionUsed(false);
        setConditionUnspecified(false);
        setShippingLocal(false);
        setShippingFree(false);*/
        setNearbySearch(false);
        setDistance("");
        setHereRadio(true);
        setZipRadio(false);
        setCustomZip("");
    }
    public boolean isValid() {
        if (keyword.trim().length() == 0) return false;
        if (nearbySearch && zipRadio && customZip.trim().length() != 5) return false;
        return true;
    }
}
