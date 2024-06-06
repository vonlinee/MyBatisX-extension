package com.baomidou.plugin.idea.mybatisx.component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MyDataNode {
//    private String name;
//    private String capital;
//    private Date declared;
//    private Integer area;
//
//    private List<MyDataNode> children;
//
//    public MyDataNode(String name, String capital, Date declared, Integer area, List<MyDataNode> children) {
//        this.name = name;
//        this.capital = capital;
//        this.declared = declared;
//        this.area = area;
//        this.children = children;
//
//        if (this.children == null) {
//            this.children = Collections.emptyList();
//        }
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getCapital() {
//        return capital;
//    }
//
//    public Date getDeclared() {
//        return declared;
//    }
//
//    public Integer getArea() {
//        return area;
//    }
//
//    public List<MyDataNode> getChildren() {
//        return children;
//    }
//
//    public String toString() {
//        return name;
//    }

    private String name;
    private String population;
    private String area;
    private String GDP;
    private String monsoonClimate;
    private List<MyDataNode> children;

    public MyDataNode(String name, String population, String area, String GDP, String monsoonClimate, List<MyDataNode> children) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.GDP = GDP;
        this.monsoonClimate = monsoonClimate;
        this.children = children;

        //判断是否为空，如果为空返回空list
        if (this.children == null) {
            this.children = Collections.emptyList();
        }
    }

    public String getName() {
        return name;
    }

    public String getPopulation() {
        return population;
    }

    public String getArea() {
        return area;
    }

    public String getGDP() {
        return GDP;
    }

    public String getMonsoonClimate() {
        return monsoonClimate;
    }

    public List<MyDataNode> getChildren() {
        return children;
    }

    public String toString() {
        return name;
    }
}
