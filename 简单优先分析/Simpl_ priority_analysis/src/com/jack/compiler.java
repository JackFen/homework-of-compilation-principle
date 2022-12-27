package com.jack;

import java.util.ArrayList;
import java.util.List;

public class compiler {
    //返回x作为左部时，其对应所有产生式的右部
    public static List<String> find(List<Rule> R, String x) {
        List<String> re = new ArrayList<>();
        for (Rule r : R) {
            if (r.getX().equals(x)) {
                re.add(r.getY());
            }
        }
        return re;
    }

    //递归求优先级前者小于后者
    public static void findsub(List<priority> sub, List<String> res, String x, String y, List<Rule> p) {
        for (String s : res) {
            String temp = s.substring(0, 1);
            sub.add(new priority(x, temp, 1));
            //如果推出的产生式第一个字符为非终结符，则在将其加入到priority_sub集后，还需向下递归
            if (temp.compareTo("A") >= 0 && temp.compareTo("Z") <= 0) {
                List<String> result = find(p, temp);
                findsub(sub, result, x, temp, p);
            }
        }
    }

    //递归求优先级前者大于后者
    public static void findplus(List<priority> plus, List<String> xs, List<String> ys, List<Rule> p) {
        for (String y1 : ys) {
            String tempy = y1.substring(0,1);
            for (String x1 : xs) {
                String tempx = x1.substring(x1.length() - 1);
                plus.add(new priority(tempx, tempy, 2));
                //如果A->...BD...中，B推出的产生式最后一个字符为非终结符，则在将其加入到priority_sub集后，还需向下递归
                if (tempx.compareTo("A") >= 0 && tempx.compareTo("Z") <= 0) {
                    List<String> result = find(p, tempx);
                    findplus(plus, result, ys, p);
                }
            }
            //如果A->...BD...中，D推出的产生式第一个符号是非终结符，还需向下递归
            if (y1.compareTo("A") >= 0 && y1.compareTo("Z") <= 0) {
                List<String> result = find(p, tempy);
                findplus(plus, xs, result, p);
            }
        }
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
        System.out.println("开始求各个符号之前的优先级...");
        List<Rule> p = grammer.getP();
        List<priority> equ = new ArrayList<>();//优先级相等
        List<priority> sub = new ArrayList<>();//优先级前者小于后者
        List<priority> plus = new ArrayList<>();//优先级前者大于后者
        //求优先性相等关系
        for (Rule r : p) {
            String right = r.getY();
            for (int i = 0; i < right.length() - 1; i++) {
                //将两个相邻的符号加入到优先级相等的集合中
                String x = right.substring(i, i + 1);
                String y = right.substring(i + 1, i + 2);
                equ.add(new priority(x, y, 0));
            }
        }
        //求优先级前者小于后者关系
        for (Rule r : p) {
            String right = r.getY();
            for (int i = 0; i < right.length() - 1; i++) {
                String x = right.substring(i, i + 1);
                String y = right.substring(i + 1, i + 2);
                //判断A->...BD...中，D是否可以求正闭包
                if (y.compareTo("A") >= 0 && y.compareTo("Z") <= 0) {
                    List<String> res = find(p, y);
                    //将D求得的正闭包加入到优先级前者小于后者的集合中
                    findsub(sub, res, x, y, p);
                }
            }
        }
        //求优先级前者大于后者关系
        for (Rule r : p) {
            String right = r.getY();
            for (int i = 0; i < right.length() - 1; i++) {
                String x = right.substring(i, i + 1);
                String y = right.substring(i + 1, i + 2);
                //判断A->...BD...中，B是否可以求闭包
                if (x.compareTo("A") >= 0 && x.compareTo("Z") <= 0) {
                    List<String> xs = find(p, x);
                    List<String> ys = new ArrayList<>();
                    //判断D是否可以求正闭包
                    if (y.compareTo("A") >= 0 && y.compareTo("Z") <= 0) {
                        ys = find(p, y);
                    } else {
                        ys.add(y);
                    }
                    //将B求得的闭包和D求得的正闭包加入到优先级前者小于后者的集合中
                    findplus(plus, xs, ys, p);
                }
            }
        }
        System.out.println("优先级相等的符号有" + equ);
        System.out.println("优先级前者小于后者有" + sub);
        System.out.println("优先级前者大于后者有" + plus);
    }
}
