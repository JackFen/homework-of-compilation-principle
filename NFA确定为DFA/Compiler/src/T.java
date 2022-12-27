import java.util.List;

//确定化所构造的子集类
public class T {
    public boolean flag=false;
    public String No;
    public List<String> set;

    public T(boolean flag, String no, List<String> set) {
        this.flag = flag;
        No = no;
        this.set = set;
    }

    public T() {
    }
}
