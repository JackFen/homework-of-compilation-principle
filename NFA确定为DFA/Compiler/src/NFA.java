import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//不确定有穷自动机类
public class NFA {
    private List<String> A;//有穷类
    private List<String> B;//字母表
    private List<Fun> f;//转换函数
    private List<String> S; //初态集
    private List<String> Z;//终态集

    public NFA() {

    }

    public NFA(List<String> a, List<String> b, List<Fun> f, List<String> s, List<String> z) {
        A = a;
        B = b;
        this.f = f;
        S = s;
        Z = z;
    }

    private boolean find(List<String> ls, String x){
        for (String str:ls){
            if(str.equals(x)){
                return true;
            }
        }
        return false;
    }
    public List<String> getA() {
        return A;
    }

    public void setA() {
        List<String> a=new ArrayList<>();
        System.out.println("请输入有穷集，一行一个状态，输入stop结束");
        Scanner in=new Scanner(System.in); //使用Scanner类定义对象
        String  x;
        while(true){
            x=in.nextLine();
            if(x.equals("stop")){
                break;
            }
            a.add(x);
        }
        System.out.println("输入完成");
        A = a;
    }

    public List<String> getB() {
        return B;
    }

    public void setB() {
        List<String> b=new ArrayList<>();
        System.out.println("请输入字母表，一行一个字母，输入stop结束");
        Scanner in=new Scanner(System.in); //使用Scanner类定义对象
        String x;
        while(true){
            x=in.nextLine();
            if(x.equals("stop")){
                break;
            }
            b.add(x);
        }
        System.out.println("输入完成");
        B = b;
    }

    public List<Fun> getF() {
        return f;
    }

    public void setF() {
        List<Fun> f=new ArrayList<Fun>();
        System.out.println("请输入转换函数,当前,字母=后继，空弧用@表示，一行一个函数,例:1,a,2，输入stop结束");
        Scanner in=new Scanner(System.in); //使用Scanner类定义对象
        String x;
        while(true){
            x=in.nextLine();
            String y[]=x.split(",");
            if(y[0].equals("stop")){
                break;
            }
            if (!find(A,y[0])||!find(A,y[2])){
                System.out.println("输入了无效的状态");
                continue;
            }
            if (!find(B,y[1])){
                System.out.println("输入了无效的字母");
                continue;
            }
            Fun fun=new Fun(y[0],y[2],y[1]);
            f.add(fun);
        }
        System.out.println("输入完成");
        this.f = f;
    }

    public List<String> getS() {
        return S;
    }

    public void setS() {
        List<String> s =new ArrayList<>();
        System.out.println("请输入初态集，一行一个状态，输入stop结束");
        Scanner in=new Scanner(System.in); //使用Scanner类定义对象
        String  x;
        while(true){
            x=in.nextLine();
            if(x.equals("stop")){
                break;
            }
            if(!find(A,x)){
                System.out.println("输入了无效的状态"+x);
                continue;
            }
            s.add(x);
        }
        System.out.println("输入完成");
        S = s;
    }

    public List<String> getZ() {
        return Z;
    }

    public void setZ() {
        List<String> z =new ArrayList<>();
        System.out.println("请输入终态集，一行一个状态，输入stop结束");
        Scanner in=new Scanner(System.in); //使用Scanner类定义对象
        String  x;
        while(true){
            x=in.nextLine();
            if(x.equals("stop")){
                break;
            }
            if(!find(A,x)){
                System.out.println("输入了无效的状态"+x);
                continue;
            }
            z.add(x);
        }
        System.out.println("输入完成");
        Z = z;
    }

    @Override
    public String toString() {
        return "NFA{" +
                "A=" + A +
                ", B=" + B +
                ", f=" + f +
                ", S='" + S + '\'' +
                ", Z=" + Z +
                '}';
    }
}
