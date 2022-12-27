import java.util.*;


public class Compiler {

    //检查C中是否存在尚未被标记的子集T
    public static boolean check(List<T> C){
        for (T t:C){
            if (!t.flag){
                return true;
            }
        }
        return false;
    }
    //查找C中未被标记的T并返回第一个未被标记的T
    public static List<String> find2(List<T> C){
        for (T t:C){
            if (!t.flag){
                t.flag=true;
                return t.set;
            }
        }
        return null;
    }
    //查找U是否在C中，如果是，返回其编号，否则返回空
    public static String find(List<T> C,List<String> x){
       for (T t:C){
           if(t.set.containsAll(x)&&t.set.size()==x.size()){
               return t.No;
           }
       }
        return null;
    }
    //空闭包运算
    public static List<String> clouse(List<Fun> f, List<String> A){
        List<String> y = new ArrayList<>(A);
        for(String a:A){
            for(Fun fun:f){
                if(fun.getA().equals(a)&&fun.getX().equals("@")){
                    boolean flag=true;
                    for(String x:y){
                        if(x.equals(fun.getB())){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        y.add(fun.getB());
                    }
                }
            }
        }
        return y;
    }
    //a弧转换
    public static List<String> move(List<Fun> f,List<String> A,String X){
        List<String> y = new ArrayList<>();
        for(String a:A){
            for(Fun fun:f){
                if(fun.getA().equals(a)&&fun.getX().equals(X)){
                    boolean flag=true;
                    for(String x:y){
                        if(x.equals(fun.getB())){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        y.add(fun.getB());
                    }
                }
            }
        }
        return y;
    }
    public static void main(String[] args) {
        NFA nfa=new NFA();
        nfa.setA();
        nfa.setB();
        nfa.setF();
        nfa.setS();
        nfa.setZ();
        //以下全部为子集法的演示代码
        List<String> K=clouse(nfa.getF(),nfa.getS());
        List<T> C=new ArrayList<>();
        //确定化后dfa所需参数
        List<Fun>f=new ArrayList<>();
        List<String> A=new ArrayList<>();
        List<String> B=new ArrayList<>(nfa.getB());
        String S="";
        List<String> Z=new ArrayList<>();
        //将T0加入到C中，但不标记
        T t=new T(false,"0",nfa.getS());
        C.add(t);
        Integer countpre=0,x=0;
        String countnow;
        //开始子集法
        while (check(C)){
            K=find2(C);//标记T
            for (String a: nfa.getB()){
                //a弧转换
                List<String> tmp=move(nfa.getF(),K,a);
                //空闭包
                List<String>K_new=clouse(nfa.getF(),tmp);
                if (K_new.size()!=0){
                    //如果U不在C中，将U作为未被标记的子集加入到C中，
                    if (find(C,K_new)==null){
                        x++;
                        countnow =x.toString();
                        T t1=new T(false,countnow,K_new);
                        C.add(t1);
                    }
                    else {
                        countnow=find(C,K_new);
                    }
                    //构造转换函数
                    Fun fun=new Fun(countpre.toString(),countnow,a);
                    f.add(fun);
                }
            }
            countpre++;
        }
        //构造有穷状态集
        for (T t1:C) {
            A.add(t1.No);
            //寻找终态集
            for (String str:t1.set){
                if (nfa.getZ().contains(str)){
                    Z.add(t1.No);
                }
            }
            //寻找唯一初态
            if(t1.set.containsAll(nfa.getS())&&t1.set.size()==nfa.getS().size()){
                S= t1.No;
            }
        }
        //确定化的有穷自动机
        DFA dfa=new DFA(A,B,f,S,Z);
        System.out.println(dfa);
    }
}
