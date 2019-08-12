/**
Table是符号表中的每一行
 */
public class Table {

    private int type;           //表示常量、变量或过程
    private int value;          //表示常量或变量的值
    private int level;          //嵌套层次
    private int address;      //相对于所在嵌套过程基地址的地址
    private int size;        
    //表示常量，变量，过程所占的大小，可以用于记录过程的参数个数
    private String name;        //变量、常量或过程名

    public int  getType(){
        return type;
    }

    public int getValue(){
        return value;
    }

    public int getLevel(){
        return level;
    }

    public int getAddress(){
        return address;
    }

    public int getSize(){
        return size;
    }

    public String getName(){
        return name;
    }

    public void setType(int t){
        type=t;
    }

    public void setValue(int v){
        value=v;
    }

    public void setLevel(int L){
        level=L;
    }

    public void setAddress(int a){
        address=a;
    }

    public void setSize(int s){
        size=s;
    }

    public void setName(String s){
        name=s;
    }

}
