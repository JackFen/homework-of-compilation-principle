package LL1Juge;

public class Rule {
    private String X;
    private String Y;

    public Rule(String x, String y) {
        X = x;
        Y = y;
    }

    @Override
    public String toString() {
        return "Rule{" +X+"->"+Y+"}";
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
}
