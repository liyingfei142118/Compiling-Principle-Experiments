public class Pcode {
    private  int F;//F段代表伪操作码
    private int L;// L段代表调用层与说明层的层差值
    private int A;//A段代表位移量（相对地址）

    public Pcode(int f,int l,int a){
        F=f;
        L=l;
        A=a;
    }
    public void setF(int f){
        F=f;
    }
    public void setL(int l){
        L=l;
    }
    public void setA(int a){
        A=a;
    }
    public int getF(){
        return F;
    }
    public int getL(){
        return L;
    }
    public int getA(){
        return A;
    }


}