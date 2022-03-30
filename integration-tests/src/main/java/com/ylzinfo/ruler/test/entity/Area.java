package com.ylzinfo.ruler.test.entity;

import java.util.List;

public class Area {
    private String areaCode;
    private List<Country> countries;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
