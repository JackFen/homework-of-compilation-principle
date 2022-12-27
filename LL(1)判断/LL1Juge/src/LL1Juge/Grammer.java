package LL1Juge;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Grammer {

    private List<String> V_N;//非终结符集
    private List<String> V_T;//终结符集
    private List<Rule> P;//规则
    private String S;//开始符

    public Grammer() {
    }

    public Grammer(List<String> v_T, List<String> v_N, List<Rule> p) {
        V_T = v_T;
        V_N = v_N;
        P = p;
    }

    @Override
    public String toString() {
        return "Grammer{" +
                "V_N=" + V_N +
                ", V_T=" + V_T +
                ", P=" + P +
                ", S='" + S + '\'' +
                '}';
    }
    //用于检查输入的符号是否在符号集中
    public boolean find(List<String> ls,String x){
        for (String str:ls){
            if(str.equals(x)){
                return true;
            }
        }
        return false;
    }
    public List<String> getV_T() {
        return V_T;
    }

    public void setV_T() {
        List<String> a=new ArrayList<>();
        System.out.println("请输入终结符集，一行一个，其中空用@表示，输入stop结束");
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
        V_T = a;
    }

    public List<String> getV_N() {
        return V_N;
    }

    public void setV_N() {
        List<String> a=new ArrayList<>();
        System.out.println("请输入非终结符集，一行一个，输入stop结束");
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
        V_N = a;
    }

    public List<Rule> getP() {
        return P;
    }

    public void setP() {
        List<Rule> R=new ArrayList<Rule>();
        System.out.println("请输入规则,输入方式：左部 右部，例：S A 输入stop结束");
        Scanner in=new Scanner(System.in); //使用Scanner类定义对象
        String x;
        while(true){
            x=in.nextLine();
            String y[]=x.split(" ");
            if(y[0].equals("stop")){
                break;
            }
            if (!find(V_N,y[0])){
                System.out.println("输入了无效的左部，请重新输入");
                continue;
            }
            String z=y[1];//用于检查产生式右部是否合法
            boolean flag=false;
            for(int i=0;i<z.length();i++){
                String temp=z.substring(i,i+1);
                if(temp.compareTo("Z")<=0&&temp.compareTo("A")>=0){
                    if(!find(V_N,temp)){
                        System.out.println("输入了无效的右部非终结符，"+temp+"请重新输入");
                        flag=true;
                    }
                }
                else{
                    if(!find(V_T,temp)){
                        System.out.println("输入了无效的终结符，"+temp+"请重新输入");
                        flag=true;
                    }
                }
                if(flag){
                    break;
                }
            }
            if(flag){
                continue;
            }
            Rule r=new Rule(y[0],y[1]);
            R.add(r);
        }
        System.out.println("输入完成");
        this.P = R;
    }

    public String getS() {
        return S;
    }

    public void setS() {
        String s;
        System.out.println("请输入开始符");
        Scanner in=new Scanner(System.in);
        s=in.nextLine();
        while (!find(V_N,s)){
            System.out.println("输入了无效的开始符,请重新输入");
            s=in.nextLine();
        }
        S = s;
    }
}
