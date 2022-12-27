package com.jack;

public class priority {
    private String X;
    private String Y;
    private String pri="=";

    public priority() {
    }

    public priority(String x, String y, int type) {
        X = x;
        Y = y;
        if(type==0){
            pri="=";
        }
        else if(type==1){
            pri="<";
        }
        else {
            pri=">";
        }
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    @Override
    public String toString() {
        return "priority{" +X+pri+Y+"}";
    }
}
