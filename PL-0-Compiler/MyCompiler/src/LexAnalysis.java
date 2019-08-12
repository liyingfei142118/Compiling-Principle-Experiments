/**
 * 词法分析器
 * 功能：识别将源文件中各种字符，将其属性、值和所在的行号存储到文件lex.txt中
 * 实现方式：LexAnalysis lex=newLexAnalysis(filename).bAnalysis(),filename是源文件存储位置
 */

import java.io.*;
public class LexAnalysis {
//Sting []keyWord={"program","begin","end","if","then","else","const","procedure","var","do","while","call","read","write","until"};
    private static  int PROG=1;//program
    private static  int BEG=2;//begin
    private static  int END=3;//end
    private static  int IF=4;//if
    private static  int THEN=5;//then
    private static  int ELS=6;//else
    private static  int CON=7;//const
    private static  int PROC=8;//procdure
    private static  int VAR=9;//var
    private static  int DO=10;//do
    private static  int WHI=11;//while
    private static  int CAL=12;//call
    private static  int REA=13;//read
    private static  int WRI=14;//write
    private static  int ODD=16;//  ODD


    private static  int EQU=17;//"="
    private static  int LES=18;//"<"
    private static  int LESE=19;//"<="
    private static  int LARE=20;//">="
    private static  int LAR=21;//">"
    private static  int NEQE=22;//"<>"


    private static  int ADD=23;//"+"
    private static  int SUB=24;//"-"
    private static  int MUL=25;//"*"
    private static  int DIV=26;//"/"

    private static  int SYM=27;//标识符
    private static  int CONST=28;//常量

    private static  int CEQU=29;//":="

    private static  int COMMA=30;//","
    private static  int SEMIC=31;//";"
    private static  int POI=32;//"."
    private static  int LBR=33;//"("
    private static  int RBR=34;//")"


    private ThreeTable ct=new ThreeTable();
    private WriteValue rv=new WriteValue();
    
    private String[] keyWord=ct.getKeyWord();//关键字
    
    private String[] symTable=ct.getSymTable();//
    private int symLength=symTable.length;
    
    private String[] constTable=ct.getConstTable();//
    private int conLength=constTable.length;

    private char ch=' ';
    private String strToken;
    private  String filename;
    private char[] buffer;
    
    private int searchPtr=0;//搜索索引
    private int line=1;//行
    private boolean Is_error=false;

    public LexAnalysis(String _filename)
    {
        for(int i=0;i<symLength;i++)
        {
            symTable[i]=null;
        }
        for(int j=0;j<conLength;j++)
         {
            constTable[j]=null;
        }
        filename=_filename;
    }

    /*
 读取源文件内容到字符数组buffer中去，包括换行符*/
    public char[] preManage()
    {
        File file=new File(filename);
        BufferedReader bf=null;
        try {
            //   System.out.println("read file test.txt...");
            bf=new BufferedReader(new FileReader(file));
            
            String temp1="",temp2 = "";
            //temp1用于读数据，一行一行的读
            //temp2用于存数据
            while((temp1=bf.readLine())!=null)
            {
                temp2=temp2+temp1+String.valueOf('\n');//将换行符转化为字符串

            }
            buffer=temp2.toCharArray();//将字符串转化为字符数组
            bf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) { 
            e.printStackTrace();
        }
        return buffer;
    }
    
    public char getChar()//得到一个字符
    {
        if(searchPtr<buffer.length)
        {
            ch=buffer[searchPtr];
            searchPtr++;
        }
//	System.out.print(ch);
        return ch;
    }


    public void getBC()//判断是否是空格、换行符
    {
        while( (ch==' '||ch=='	'||ch=='\n')&&(searchPtr<buffer.length))
        {
            if(ch=='\n')//如果是换行符，行加加
            {
                line++;
            }
            getChar();
        }
    }

    public String concat()//将连续字符转化为单词
    {
        strToken=strToken+String.valueOf(ch);
        return strToken;
    }

    public boolean isLetter()//判断是不是字母
    {
        if(Character.isLetter(ch))//character对单个字符操作
        {
            return true;
        }
        return false;
    }

    public boolean isDigit()//判断是不是数字
    {
        if(Character.isDigit(ch))
        {
            return true;
        }
        return false;
    }

    public int reserve()//判断关键字，返回下标
    {
        for(int i=0;i<keyWord.length;i++)
        {
            if(keyWord[i].equals(strToken))
            {
                return i+1;
            }
        }
        return 0;
    }

    public void retract()//指针索引--，ch置空
    {
        searchPtr--;
        ch=' ';
    }

    public int insertId()//插入Id，并返回下标
    {
        for(int i=0;i<symLength;i++)
        {
            if(symTable[i]==null)
            {
                symTable[i]=strToken;
                return i;
            }
        }
        return -1;//表示symTable已经满了
    }

    public int insertConst()//插入Const，并返回下标
    {
        for(int i=0;i<conLength;i++)
        {
            if(constTable[i]==null)
            {
                constTable[i]=strToken;
                return i;
            }
        }
        return -1;//constTable已经满了
    }

    public void Display_Error()//展示错误
    {
       // System.out.println();
        System.out.print("Error_line： "+line);
        System.out.println();


    }

/*
 字符识别函数
通过读取buffer数组中的单个字符进行识别源程序中的各个元素，每次识别一个元素
 识别出的字符的属性：RV.getId()是属性，RV.getValue()是值，RV.getLine()是所在行号
*/
    
    public WriteValue analysis()
    {
        int code,value;
        strToken="";
        
        getChar();//得到一个字符ch（全局变量），指针索引已经searchPtr=0
        getBC();//经过此步，如果ch=='\n'，则肯定是到达了文件末尾，得到下一个字符ch
        
        if(ch=='\n')
        {
            rv.setId(-1);
            rv.setValue("-1");
            rv.setLine(line);
            return rv;
        }
        
        if(isLetter())//得到以字符开头的单词
        {
            while((isLetter()||isDigit()))
            {
                concat();//连接
                getChar();//searchPtr++，得到字符ch
            }
            retract();//searchPtr--，ch=‘ ’
            
            code=reserve();//与关键字数组比较，得到下标
            
            if(code==0)
            {
                value=insertId();
                rv.setId(SYM);
                rv.setValue(symTable[value]);
                rv.setLine(line);
                return rv;
            }
            else 
            {
                rv.setId(code);
                rv.setValue("-");
                rv.setLine(line);
                return rv;
            }
        }else if(isDigit()){
            while(isDigit()){
                concat();
                getChar();
            }
            retract();
            
            value=insertConst();
            
            rv.setId(CONST);
            rv.setValue(constTable[value]);
            rv.setLine(line);
            return rv;
        }else if(ch=='='){
            rv.setId(EQU);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch=='+'){
            rv.setId(ADD);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch=='-'){
            rv.setId(SUB);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        } else if(ch=='*'){
            rv.setId(MUL);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch=='/'){
            rv.setId(DIV);
            rv.setValue("/");
            rv.setLine(line);
            return rv;
        }else if(ch=='<'){
            getChar();
            if(ch=='='){
                rv.setId(LESE);
                rv.setValue("-");
                rv.setLine(line);
                return rv;
            }else if(ch=='>'){
                rv.setId(NEQE);
                rv.setValue("-");
                rv.setLine(line);
                return rv;
            }else{
                retract();
                rv.setId(LES);
                rv.setValue("-");
                rv.setLine(line);
                return rv;
            }
        }else if(ch=='>'){
            getChar();
            if(ch=='='){
                rv.setId(LARE);
                rv.setValue("-");
                rv.setLine(line);
                return rv;
            }else{
                retract();
                rv.setId(LAR);
                rv.setValue("-");
                rv.setLine(line);
                return rv;
            }
        }else if(ch==','){
            rv.setId(COMMA);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch==';'){
            rv.setId(SEMIC);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch=='.'){
            rv.setId(POI);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch=='('){
            rv.setId(LBR);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch==')'){
            rv.setId(RBR);
            rv.setValue("-");
            rv.setLine(line);
            return rv;
        }else if(ch==':'){
        	char ch1=ch;
            getChar();
            if(ch=='='){
                rv.setId(CEQU);
                rv.setValue("-");
                rv.setLine(line);
                return rv;
            }else{
                System.out.println();
                System.out.print(String.valueOf(ch1)+ch);
                retract();
            }
        }
        else 
        {
        System.out.println();
        System.out.print(ch);
        }
        Is_error=true;
        return rv;

    }


/*
  循环识别出所有字符并输出到文件lex.txt中
 */
    public void bAnalysis(){
        preManage();//读取源文件内容到字符数组buffer中去，包括换行符
        WriteValue temp;
        
        String str="lex.txt";
        OutputStream myout=null;
        File file=new File(str);
        try{
            myout=new FileOutputStream(file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        while(searchPtr<buffer.length&&Is_error==false)
        { 
        	temp=analysis();//返回Writevalue对象rv
            String tempId=String.valueOf(temp.getId());
            String tempLine=String.valueOf(temp.getLine());
            
            byte[] bid=tempId.getBytes();
            byte[] bname=temp.getValue().getBytes();
            byte[] bline=tempLine.getBytes();
            
            try{
                myout.write(bid);
                myout.write(' ');
                myout.write(bname);
                myout.write(' ');
                myout.write(bline);
                myout.write('\n');
            }catch(IOException e){
                e.printStackTrace();
            }
            //出错提示 继续找
            if(Is_error==true)
            {
                Display_Error();
                Is_error=false;
            }
        }//while
        
        try{
            myout.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        if(Is_error==true){
            Display_Error();
        }
    }
}


















