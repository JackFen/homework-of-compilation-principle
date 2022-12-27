package Minimum;

import java.util.*;

public class Compiler {
    //求两集合差集的函数，这里其功能为有穷状态集减去终结符集以求出非终结符集
    public static List<String> subList(List<String> list1, List<String> list2) {
        //空间换时间 降低时间复杂度
        Map<String, String> tempMap = new HashMap<>();
        for (String str : list2) {
            tempMap.put(str, str);
        }
        List<String> resList = new LinkedList<>();
        for (String str : list1) {
            if (!tempMap.containsKey(str)) {
                resList.add(str);
            }
        }
        return resList;
    }
    //这里是最小化算法实现，用于判断一个集合中的各个状态是否等价，若不等价则返回区分后的状态集合，若等价则返回原集合
    public static List<List<String>> minimun(List<String> ls,List<Fun>F,List<String> B){

        List<List<String>>result=new ArrayList<>();
        List<String>y1=new ArrayList<>();//未出界集合
        List<String>y2=new ArrayList<>();//出界集合
        result.add(y2);
        result.add(y1);
        if (ls.size()!=1){
            for(String s:B){
                //先进行a弧转换判断，若判断失败，则用下一个弧判断，直到判断成功或者所有的弧都判断完
                for(String s1:ls){
                    boolean flag2=true;
                    //遍历转换函数表，如果当前状态转换后的下一个状态出界，则将其加入到出界集合
                    for(Fun f:F){
                        if (f.getX().equals(s)&&f.getA().equals(s1)){
                            String y=f.getB();
                            if (!ls.contains(y)){
                                y2.add(s1);
                                flag2=false;
                                break;
                            }
                        }
                    }
                    //如果当前状态转换后的下一个状态未出界，则将其加入到未出界集合
                    if(flag2){
                        y1.add(s1);
                    }
                }
                //判断能否区分两个集合
                if(!y2.isEmpty()){
                    break;
                }
                //若不能区分两个集合，则将两个集合全部清空，再进行判断
                else {
                    y1.clear();
                }
            }
        }
        //若未区分出来，则将未出界集认为是出界集
        if (y1.isEmpty()){
           result.remove(y1);
           result.remove(y2);
           result.add(ls);
           result.add(y1);
        }
        //如果区分出来，返回出界集，未出界集
        //如果未区分出来，则将未出界集认为是出界集，返回出界集，空集
        return result;
    }
    //这是最小化之后，构建最小化确定的有穷自动机函数
    public static DFA minimum2(List<List<String>>A,List<String>B,List<Fun>F,List<String>Z,String S){
        //遍历转换函数集合，将等价的状态全部用其所属集合的第一个状态代替
        for(List ls:A){
            if (ls.size()>1){
                for (int i=1;i<ls.size();i++){
                    for(Fun f:F){
                        if (f.getA().equals(ls.get(i))){
                            f.setA((String) ls.get(0));
                        }
                        if (f.getB().equals(ls.get(i))){
                            f.setB((String) ls.get(0));
                        }
                    }
                }
            }
        }
        //去除重复的转换函数
        List<Fun>f1=new ArrayList<>();
        for(Fun f:F){
            if(f1.isEmpty()){
                f1.add(f);
            }
            boolean flag=false;
            for(Fun f2:f1){
                if(f2.getA().equals(f.getA())&&f2.getB().equals(f.getB())&&f2.getX().equals(f.getX())){
                    flag=true;
                    break;
                }
            }
            if (!flag){
                f1.add(f);
            }
        }
        //求得最小化后的终结符集和初态
        List<String>z=new ArrayList<>();
        for(List ls:A){
            if (ls.contains(S)){
                S= (String) ls.get(0);
            }
            if(Z.containsAll(ls)){
                z.add((String) ls.get(0));
            }
        }
        //求得最小化后的有穷状态集
        List<String>a=new ArrayList<>();
        for(List ls:A){
            a.add((String) ls.get(0));
        }
        //返回最小化后的确定的有穷自动机
        return new DFA(a,B,f1,S,z);
    }

    public static void main(String[] args) {
        //构建一个确定的有穷自动机
        DFA dfa = new DFA();
        dfa.setA();
        dfa.setB();
        dfa.setF();
        dfa.setS();
        dfa.setZ();
        System.out.println("构造出的DFA为" + dfa);
        System.out.println("开始最小化");
        //以下是确定化算法的实现
        //先将状态集合划分为非终态集和终态集
        List<String> s0 = dfa.getZ();
        List<String> s1 = subList(dfa.getA(), s0);
        //定义用于存放最小化后的状态集合
        List<List<String>> result = new ArrayList<>();
        List<List<String>> re;
        //若非终态集中还有不等价的状态集，则对其进行区分
        do {
            re = minimun(s1, dfa.getF(), dfa.getB());
            result.add(re.get(0));
            s1=re.get(1);
        } while (!re.get(1).isEmpty());
        //若终态集中还有不等价的状态集，则对其进行区分
        do{
            re = minimun(s0, dfa.getF(), dfa.getB());
            result.add(re.get(0));
            s0=re.get(1);
        }while (!re.get(1).isEmpty());
        //输出最小化后的各个等价集合
        System.out.println("最小化之后要合并的集合为");
        System.out.println(result);
        //求最小化后的DFA
        System.out.println("用每个集合中的第一个元素代替这个集合，得到的DFA为");
        DFA dfa1=minimum2(result,dfa.getB(),dfa.getF(),dfa.getZ(), dfa.getS());
        System.out.println(dfa1);
    }
}
