package Minimum;

//转换函数类
public class Fun {
    private String A;//x
    private String B;//y
    private String X;//输入的字母

    public Fun(String a, String b, String x) {
        A = a;
        B = b;
        X = x;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    @Override
    public String toString() {
        return "f("+A+","+X+")="+B;
    }
}
