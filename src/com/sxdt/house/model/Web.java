package com.sxdt.house.model;

/**
 * Created by agu on 17/1/3.
 */
public class Web {


    private String name;

    private String path;
    private String path2;

    public Web() {
    }

    public Web(String name, String path,String path2) {
        this.name = name;
        this.path = path;
        this.path2 = path2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath2() {
        return path2;
    }

    public Web setPath2(String path2) {
        this.path2 = path2;
        return this;
    }
}
