package com.sxdt.house.model;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by agu on 17/1/3.
 */
public class AppConfig {
    private List<Option> cityList;
    private LinkedHashMap<String,List<Web>> citymap;

    public AppConfig() {
    }

    public AppConfig(List<Option> cityList) {
        this.cityList = cityList;
    }

    public AppConfig(List<Option> cityList, LinkedHashMap<String, List<Web>> citymap) {
        this.cityList = cityList;
        this.citymap = citymap;
    }


    public List<Option> getCityList() {
        return cityList;
    }

    public void setCityList(List<Option> cityList) {
        this.cityList = cityList;
    }

    public LinkedHashMap<String, List<Web>> getCitymap() {
        return citymap;
    }

    public void setCitymap(LinkedHashMap<String, List<Web>> citymap) {
        this.citymap = citymap;
    }
}
