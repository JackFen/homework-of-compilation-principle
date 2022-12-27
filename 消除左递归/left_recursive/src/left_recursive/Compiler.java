package left_recursive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler {
    //去掉重复的产生式
    public static void removeDuplicate(Grammer grammer){
        List<String> VN=grammer.getV_N();
        List<Rule> p=grammer.getP();
        Map<String,Integer>count=new HashMap<>();
        for(String s:VN){
            for (int i=0,len=p.size();i<len;i++){
                if(p.get(i).getX().equals(s)){
                    if(!count.containsKey(p.get(i).getY())){
                        count.put(p.get(i).getY(),0);
                    }
                    else {
                        p.remove(i);
                        i--;
                        len--;
                    }
                }
            }
            count.clear();
        }
    }

    //寻找非终结符的对应规则的右部的位置
    public static List<Integer> find(Grammer grammer, String x) {
        List<Integer> pos = new ArrayList<>();
        List<Rule> p = grammer.getP();
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).getX().equals(x)) {
                pos.add(i);
            }
        }
        return pos;
    }

    //替换A_i=A_jr的产生式
    public static boolean solve(Grammer grammer, List<Integer> pos_i, List<Integer> pos_j) {
        List<Rule> rules = grammer.getP();
        boolean flag = false;
        boolean flag1 = false;
        for (Integer i : pos_i) {
            //获取A_i当前的产生式的第一个符号
            String si = rules.get(i).getY().substring(0, 1);
            //查找当前符号是否能够被A_j的产生式替换。
            for (Integer j : pos_j) {
                String sj = rules.get(j).getX();
                //如果能够被替换，则将其改写为A_i->A_j...
                if (si.equals(sj)) {
                    String str = rules.get(j).getY() + rules.get(i).getY().substring(1);
                    rules.add(new Rule(rules.get(i).getX(), str));
                    flag = true;
                    if(rules.get(j).getY().substring(0,1).equals(rules.get(i).getX())){
                        flag1 = true;
                    }
                }
            }
            //去除被替换掉的产生式
            if (flag) {
                rules.remove(i.intValue());
                flag = false;
            }
        }
        return flag1;
    }

    //判断非终结符能否到达
    public  static void juge(Grammer grammer,String x,Map<String,Integer>count){
        List<Integer> pos_i = find(grammer, x);
        for(Integer i:pos_i){
            String str=grammer.getP().get(i).getY();
            for (int j=0;j<str.length();j++){
                String temp=str.substring(j,j+1);
                //如果当前产生式包含该非终结符，则可以到达该非终结符
                if(grammer.getV_N().contains(temp)){
                    count.put(temp, 1);
                }
            }
        }
    }
    //判断是否已经将全部不可到达的非终结符找出，若都已找出，则返回false,否则返回true。
    public static boolean change(List<String>V_N,Map<String,Integer>countpre,Map<String,Integer>countnow){
        for (String s:V_N){
            if(!countpre.get(s).equals(countnow.get(s))){
                return true;
            }
        }
        return false;
    }
    //删除无用非终结符
    public static void deleteV_N(Grammer grammer,String s){
        grammer.getV_N().remove(s);
        List<Rule> p=grammer.getP();
        for (int i=0,len=p.size();i<len;i++) {
            //如果产生式的左部是不可到达的非终结符，则将其删去
            if(p.get(i).getX().equals(s)){
                p.remove(i);
                i--;
                len--;
            }
            //如果产生式的右部含有不可到达的非终结符，则将其删去
            if (p.get(i).getY().contains(s)){
                p.remove(i);
                i--;
                len--;
            }
        }

    }
    //处理左递归,如果存在左递归，则处理完后返回True,否则返回False
    public static Boolean solveLeft(Grammer grammer) {
        List<Rule> p = grammer.getP();
        String S = grammer.getS();
        boolean flag1=false;
        for (int i = 0; i < grammer.getV_N().size(); i++) {
            boolean flag = false;
            for (int j = 0; j < i; j++) {
                //寻找Ai和Aj产生式在规则表里面的位置
                List<Integer> pos_i = find(grammer, grammer.getV_N().get(i));
                List<Integer> pos_j = find(grammer, grammer.getV_N().get(j));
                //若Aj->a...,将形如Ai->Aj...的产生式替换为Ai->a...... 若A_i存在直接左递归，则返回True，否则返回False
                flag = solve(grammer, pos_i, pos_j);
            }
            //消除A_i中的直接左递归
            if (flag) {
                //表示这个文法存在直接左递归
                flag1=true;
                //消除左递归
                //将空弧加入到终结符集中
                if (!grammer.getV_T().contains("@")) {
                    grammer.getV_T().add("@");
                }
                //获取要消除直接左递归的非终结符的产生式的位置
                List<Integer> pos_i1 = find(grammer, grammer.getV_N().get(i));
                //寻找新加入非终结符的表达符号
                char te = p.get(i).getX().charAt(0);
                while (grammer.getV_N().contains(te + "")) {
                    te++;
                    //如果26个英文字母都被占用，则会返回Z。
                    if (te+1 > 'Z') {
                        break;
                    }
                }
                grammer.getV_N().add(te + "");
                String z = null;
                for (Integer i1 : pos_i1) {
                    //寻找存在直接左递归的产生式,并消除该左递归
                    if (p.get(i1).getY().substring(0, 1).equals(p.get(i1).getX())) {
                        String te2 = p.get(i1).getY().substring(1) + te;
                        //获取左递归重复部分的字符串
                        z = p.get(i1).getY().substring(1);
                        p.get(i1).setY(te2);
                    }
                    //将该非终结符的其他产生式都用新加入的非终结符进行改写
                    else {
                        String te3 = p.get(i1).getY() + te;
                        p.get(i1).setY(te3);
                    }
                }
                //将新终结符对应的产生式加入到规则表中
                p.add(new Rule(te + "", z + te));
                p.add(new Rule(te + "", "@"));
                removeDuplicate(grammer);
            }
        }
        //开始去除无用产生式
        if(flag1){
            //初始化判断是否存在无用产生式的判断字典，若对应非终结符的value域为0，则该非终结符为不可到达的。
            Map<String,Integer>countpre=new HashMap<>();
            for(String s:grammer.getV_N()){
                countpre.put(s,0);
                if(s.equals(grammer.getS())){
                    countpre.put(s,1);
                }
            }
            Map<String,Integer>countnow=new HashMap<>(countpre);
            //从开始符号的产生式中包含的非终结符进行判断
            juge(grammer,grammer.getS(),countnow);
            //广度判断是否存在不可到达的非终结符
            do{
                countpre=new HashMap<>(countnow);
                for(Map.Entry<String, Integer> entry : countnow.entrySet()){
                    if(entry.getValue()==1){
                        juge(grammer, entry.getKey(), countnow);
                    }
                }
            } while (change(grammer.getV_N(),countpre,countnow));//直到可到达的非终结符的数量不在变化的时候停止
            //删除无用的产生式
            for(Map.Entry<String, Integer> entry : countnow.entrySet()){
                if(entry.getValue()==0){
                    deleteV_N(grammer,entry.getKey());
                }
            }
        }
        return flag1;
    }

    public static void main(String[] args) {
        //构建一个文法
        Grammer grammer = new Grammer();
        grammer.setV_N();
        grammer.setV_T();
        grammer.setP();
        grammer.setS();
        System.out.println("输入的文法为");
        System.out.println(grammer);
        boolean flag = true;
        //开始处理左递归
        flag = solveLeft(grammer);
        //判断是否存在左递归
        if (!flag) {
            System.out.println("该文法不存在左递归");
        } else {
            System.out.println("修改后的文法为\n"+grammer);
        }
    }
}