package com.yo_travel.yo_travelguider.AdapterData;

public class AdapterData1 {

    private String name;
    private String id;
    private String s_date;
    private String e_date;
    private String adults;
    private String child;
    private String total;
    private String map;

    public AdapterData1(String name, String id, String s_date, String e_date, String adults, String child, String total, String map) {

        this.name = name;
        this.id = id;
        this.s_date = s_date;
        this.e_date = e_date;
        this.adults = adults;
        this.child = child;
        this.total = total;
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getE_date() {
        return e_date;
    }

    public void setE_date(String e_date) {
        this.e_date = e_date;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
