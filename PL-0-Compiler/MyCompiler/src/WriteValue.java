public class WriteValue {
    private int id;//单词（如常量）
    private int line;//在第几行
    private String value;//值


    public void setId(int _id){
        id=_id;
    }
    public void setValue(String _value){
        value=_value;
    }
    public void setLine(int _line){
        line=_line;
    }

    public int getId(){
        return id;
    }
    public int getLine(){
        return line;
    }
    public String getValue(){
        return value;
    }

}
