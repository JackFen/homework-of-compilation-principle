package LL1Juge;

import java.util.*;

public class Compiler {
    //返回x作为左部时，其对应所有产生式的右部
    public static List<String> find(List<Rule> R,String x){
        List<String> re=new ArrayList<>();
        for (Rule r:R){
            if(r.getX().equals(x)){
                re.add(r.getY());
            }
        }
        return re;
    }
    //判断各个first集或follow集的大小是否变化
    public static boolean ischange(Map<String, Integer> numpre, Map<String, Integer> numaft){
        Set<Map.Entry<String,Integer>> mapEntrySet1 =numpre.entrySet();
        for(Map.Entry<String,Integer> pre:mapEntrySet1){
            String key=pre.getKey();
            Integer value= pre.getValue();
            if(!value.equals(numaft.get(key))){
                return true;
            }
        }
        return false;
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
        //求各个非终结符是否能推出空
        System.out.println("开始计算各个非终结符能否推出空...");
        //各个非终结符的判断数组，顺序与V_N中各个非终结符的顺序一致
        int[] juge = new int[grammer.getV_N().size()];//1：是 0：未定 -1：否
        List<Rule> p = grammer.getP();
        //将能直接推出空的非终结符标记，并且先将产生式中含有终结符的非终结符表示为不能推出空
        for(Rule r:p){
            //对“暂时”不能推出空的非终结符进行判断
            if(juge[grammer.getV_N().indexOf(r.getX())]!=1) {
                String z = r.getY();
                //遍历产生式右部的每一个符号
                for (int i = 0; i < z.length(); i++) {
                    String temp = z.substring(i, i + 1);
                    //是空的话，直接表示为能推出空，结束该产生式的判断
                    if(temp.compareTo("@")==0){
                        juge[grammer.getV_N().indexOf(r.getX())]=1;
                        break;
                    }
                    //如果未被标记为能推出空，且其含有终结符的话，将其标记为不能推出空
                    else if(temp.compareTo("A")<0||temp.compareTo("Z")>0){
                        juge[grammer.getV_N().indexOf(r.getX())]=-1;
                        break;
                    }
                }
            }
        }
        //对上一步不能推出空的非终结符的判断进行纠错
        for (String x: grammer.getV_N()) {
            if(juge[grammer.getV_N().indexOf(x)]==-1){
                //对该非终结符的所有产生式进行查找
                List<String> re=find(grammer.getP(),x);
                //如果产生式中含有右部全部为非终结符的情况，则将该非终结符能否退出空设为未知
                for(String z:re){
                    int count=0;
                    for (int i = 0; i < z.length(); i++) {
                        String temp = z.substring(i, i + 1);
                        if(temp.compareTo("A")>=0&&temp.compareTo("Z")<=0){
                            count++;
                        }
                        if(count==z.length()){
                            juge[grammer.getV_N().indexOf(x)]=0;
                        }
                    }
                }
            }
        }
        //对其所有产生式右部都为非终结符和上一步能否退出空的情况发生改动的非终结符进行判断
        for(Rule r:p){
            if(juge[grammer.getV_N().indexOf(r.getX())]==0){
                String z = r.getY();
                //扫描产生式右部的所有符号，如果出现不能推出空的非终结符，则将该非终结符标记为不能推出空，结束对该产生式的判断
                for (int i = 0; i < z.length(); i++) {
                    String temp = z.substring(i, i + 1);
                    if(juge[grammer.getV_N().indexOf(temp)]==-1){
                        juge[grammer.getV_N().indexOf(r.getX())]=-1;
                        break;
                    }
                    //如果整个产生式右部的非终结符都为可以推出空的非终结符，则将该终结符标记为能推出空
                    if(juge[grammer.getV_N().indexOf(temp)]==1){
                        if(i==z.length()-1){
                            juge[grammer.getV_N().indexOf(r.getX())]=1;
                        }
                    }
                }
            }
        }
        System.out.println("计算完成");
        //System.out.println(Arrays.toString(juge));
        System.out.println("可以推出空的非终结符：");
        for(int i=0;i<juge.length;i++){
            if(juge[i]==1){
                System.out.println(grammer.getV_N().get(i));
            }
        }
        System.out.println("不能推出空的非终结符：");
        for(int i=0;i<juge.length;i++){
            if(juge[i]==-1){
                System.out.println(grammer.getV_N().get(i));
            }
        }
        //求first集合
        //定义终结符集的first集，其集合所含元素就是它本身
        Map<String,Set<String>>firstV_T=new HashMap<>();
        for(String s:grammer.getV_T()){
            Set<String>temp=new HashSet<String>();
            temp.add(s);
            firstV_T.put(s,temp);
        }
        //定义非终结符的first集
        Map<String,Set<String>>firstV_N=new HashMap<>();
        //定义非终结符的first集各个集合的运算前后的大小
        Map<String,Integer>numpre=new HashMap<>();
        Map<String,Integer>numaft=new HashMap<>();
        //初始化非终结符的first集
        for(String s:grammer.getV_N()){
            Set<String>temp=new HashSet<String>();
            firstV_N.put(s,temp);
            numpre.put(s,0);
            numaft.put(s,0);
        }
        //求非终结符的first集
        do{
            //更新各个集合的上一次比较后的大小
            for(String s:grammer.getV_N()){
                numpre.put(s,numaft.get(s));
            }
            for(String s:grammer.getV_N()){
                //获取当前非终结符的所有产生式右部符号串
                List<String> re=find(grammer.getP(),s);
                for(String z:re){
                    //逐个扫描右部的符号
                    for (int i = 0; i < z.length(); i++) {
                        String te = z.substring(i, i + 1);
                        //如果当前符号为非终结符，就将其first集加入到当前产生式左部符号的first集中
                        if(te.compareTo("A")>=0&&te.compareTo("Z")<=0){
                            int j=grammer.getV_N().indexOf(te);
                            firstV_N.get(s).addAll(firstV_N.get(grammer.getV_N().get(j)));
                            //更新集合大小
                            Integer t=firstV_N.get(s).size();
                            numaft.put(s,t);
                            //如果当前符号不能推出空，则结束当前产生式的判断
                            if(juge[j]!=1){
                                break;
                            }
                            //如果当前符号能推出空，且不为右部的最后一个符号，则将当前产生式左部first集的空移除
                            else if(i!=z.length()-1){
                                firstV_N.get(s).remove("@");
                            }
                        }
                        //如果当前符号为终结符，将其加入到当前产生式左部符号的first集中，并结束当前产生式的判断
                        else {
                            firstV_N.get(s).add(te);
                            //更新集合的大小
                            Integer t=firstV_N.get(s).size();
                            numaft.put(s,t);
                            break;
                        }
                    }
                }
            }
        }while (ischange(numpre,numaft));//直到各个集合大小不在变化的时候停止
        //打印信息
        System.out.println("终结符集的frist集为"+firstV_T);
        System.out.println("非终结符集的frist集为"+firstV_N);
        //声明各个产生式右部firsrt集
        Map<String,Set<String>>firstP=new HashMap<>();
        //初始化上述集合
        for(Rule r:p){
            String z = r.getY();
            Set<String>temp=new HashSet<String>();
            firstP.put(z,temp);
        }
        //求各个产生式右部firsrt集
        for(Rule r:p){
            //获取右部
            String z = r.getY();
            //对右部符号串逐个分析
            for (int i = 0; i < z.length(); i++) {
                String temp = z.substring(i, i + 1);
                //如果当前符号为终结符，则将其加入到当前产生式右部的first集中,并结束对该产生式的分析
                if(temp.compareTo("Z")>0||temp.compareTo("A")<0){
                    firstP.get(z).add(temp);
                    break;
                }
                //如果当前符号为非终结符，则将其first集加入到当前产生式右部的first集中
                else {
                    Set<String>te=firstV_N.get(temp);
                    firstP.get(z).addAll(te);
                    //如果当前符号不能推出空，则结束当前产生式的分析
                    if(juge[grammer.getV_N().indexOf(temp)]!=1){
                        break;
                    }
                    //如果当前符号能推出空，且不为右部的最后一个符号，则将右部first集中的空移除
                    else if(i!=z.length()-1){
                        firstP.get(z).remove("@");
                    }
                }
            }
        }
        //输出信息
        System.out.println("各个产生式右部符号串的first集为"+firstP);
        //求各个非终结符的follow集
        //定义非终结符的follow集
        Map<String,Set<String>>follow=new HashMap<>();
        //定义非终结符的follow集各个集合的运算前后的大小
        Map<String,Integer>fnumpre=new HashMap<>();
        Map<String,Integer>fnumaft=new HashMap<>();
        //初始化非终结符的follow集
        for(String s:grammer.getV_N()){
            Set<String>temp=new HashSet<String>();
            follow.put(s,temp);
            fnumpre.put(s,0);
            fnumaft.put(s,0);
        }
        follow.get(grammer.getS()).add("#");
        do{
            //更新各个集合的上一次比较后的大小
            for(String s:grammer.getV_N()){
                fnumpre.put(s,fnumaft.get(s));
            }
            for(Rule r:p){
                //求右部非终结符的follow集
                for(int i=0;i<r.getY().length();i++){
                    String x=r.getY().substring(i,i+1);
                    //判断当前符号是否是非终结符
                    if(x.compareTo("A")>=0&&x.compareTo("Z")<=0){
                        String z=r.getY();
                        //获取当前符号的下一个符号β
                        for(int j=i+1;j<z.length();j++){
                            String temp=z.substring(j,j+1);
                            //如果β是非终结符，则将其first集的非空元素加入到当前符号的follow集中
                            if(temp.compareTo("A")>=0&&temp.compareTo("Z")<=0){
                                follow.get(x).addAll(firstV_N.get(temp));
                                follow.get(x).remove("@");
                                //更新当前符号follow集的大小
                                Integer t=follow.get(x).size();
                                fnumaft.put(x,t);
                                //如果β不能推出空，则停止对当前产生式的分析
                                if(juge[grammer.getV_N().indexOf(temp)]==-1){
                                    break;
                                }
                                //如果β是右部的最后一个符号，且能推出空，则将产生式左部符号的follow集加入到当前符号的follow集中
                                else if(j==z.length()-1) {
                                    follow.get(x).addAll(follow.get(r.getX()));
                                    //更新当前符号follow集的大小
                                    Integer t1=follow.get(x).size();
                                    fnumaft.put(x,t1);
                                }
                            }
                            //如果β是终结符，则将其加入到当前符号的follow集中，并停止对当前产生式的分析
                            else {
                                follow.get(x).add(temp);
                                Integer t=follow.get(x).size();
                                fnumaft.put(x,t);
                                break;
                            }
                        }
                        //如果当前符号是右部的最后一个符号，则将产生式左部符号的follow集加入到当前符号的follow集中
                        if(i==r.getY().length()-1){
                            follow.get(x).addAll(follow.get(r.getX()));
                            Integer t=follow.get(x).size();
                            fnumaft.put(x,t);
                        }
                    }
                }
            }
        }while (ischange(fnumpre,fnumaft));//当各个非终结符的follow集的大小不再变化时停止
        //输出信息
        System.out.println("各个非终结符的follow集为"+follow);
        //求select集
        Map<Rule,Set<String>>select=new HashMap<>();
        for(Rule r:p){
            Set<String>temp=new HashSet<>(firstP.get(r.getY()));
            //如果右部能推出空，则将左部的follow集加入到产生式的select集中
            if (temp.contains("@")){
                temp.remove("@");
                temp.addAll(follow.get(r.getX()));
            }
            select.put(r,temp);
        }
        //输出信息
        System.out.println("select集为"+select);
    }
}
