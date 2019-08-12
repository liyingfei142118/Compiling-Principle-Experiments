public class ThreeTable {
//用于记录关键字、标识符、常数
//用于词法分析
    private String[] keyWord={"program","begin","end","if","then","else","const","procedure","var","do","while","call","read","write","repeat","odd"};

    private int symLength=100;
    private String[] symTable=new String[symLength];

    private int conLength=100;
    private String[] constTable=new String[conLength];

    public String[] getKeyWord(){
        return keyWord;
    }
    public String[] getSymTable(){
        return symTable;
    }
    public String[] getConstTable(){
        return constTable;
    }
}
