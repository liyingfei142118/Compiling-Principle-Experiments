/**
 * �ʷ�������
 * ���ܣ�ʶ��Դ�ļ��и����ַ����������ԡ�ֵ�����ڵ��кŴ洢���ļ�lex.txt��
 * ʵ�ַ�ʽ��LexAnalysis lex=newLexAnalysis(filename).bAnalysis(),filename��Դ�ļ��洢λ��
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

    private static  int SYM=27;//��ʶ��
    private static  int CONST=28;//����

    private static  int CEQU=29;//":="

    private static  int COMMA=30;//","
    private static  int SEMIC=31;//";"
    private static  int POI=32;//"."
    private static  int LBR=33;//"("
    private static  int RBR=34;//")"


    private ThreeTable ct=new ThreeTable();
    private WriteValue rv=new WriteValue();
    
    private String[] keyWord=ct.getKeyWord();//�ؼ���
    
    private String[] symTable=ct.getSymTable();//
    private int symLength=symTable.length;
    
    private String[] constTable=ct.getConstTable();//
    private int conLength=constTable.length;

    private char ch=' ';
    private String strToken;
    private  String filename;
    private char[] buffer;
    
    private int searchPtr=0;//��������
    private int line=1;//��
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
 ��ȡԴ�ļ����ݵ��ַ�����buffer��ȥ���������з�*/
    public char[] preManage()
    {
        File file=new File(filename);
        BufferedReader bf=null;
        try {
            //   System.out.println("read file test.txt...");
            bf=new BufferedReader(new FileReader(file));
            
            String temp1="",temp2 = "";
            //temp1���ڶ����ݣ�һ��һ�еĶ�
            //temp2���ڴ�����
            while((temp1=bf.readLine())!=null)
            {
                temp2=temp2+temp1+String.valueOf('\n');//�����з�ת��Ϊ�ַ���

            }
            buffer=temp2.toCharArray();//���ַ���ת��Ϊ�ַ�����
            bf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) { 
            e.printStackTrace();
        }
        return buffer;
    }
    
    public char getChar()//�õ�һ���ַ�
    {
        if(searchPtr<buffer.length)
        {
            ch=buffer[searchPtr];
            searchPtr++;
        }
//	System.out.print(ch);
        return ch;
    }


    public void getBC()//�ж��Ƿ��ǿո񡢻��з�
    {
        while( (ch==' '||ch=='	'||ch=='\n')&&(searchPtr<buffer.length))
        {
            if(ch=='\n')//����ǻ��з����мӼ�
            {
                line++;
            }
            getChar();
        }
    }

    public String concat()//�������ַ�ת��Ϊ����
    {
        strToken=strToken+String.valueOf(ch);
        return strToken;
    }

    public boolean isLetter()//�ж��ǲ�����ĸ
    {
        if(Character.isLetter(ch))//character�Ե����ַ�����
        {
            return true;
        }
        return false;
    }

    public boolean isDigit()//�ж��ǲ�������
    {
        if(Character.isDigit(ch))
        {
            return true;
        }
        return false;
    }

    public int reserve()//�жϹؼ��֣������±�
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

    public void retract()//ָ������--��ch�ÿ�
    {
        searchPtr--;
        ch=' ';
    }

    public int insertId()//����Id���������±�
    {
        for(int i=0;i<symLength;i++)
        {
            if(symTable[i]==null)
            {
                symTable[i]=strToken;
                return i;
            }
        }
        return -1;//��ʾsymTable�Ѿ�����
    }

    public int insertConst()//����Const���������±�
    {
        for(int i=0;i<conLength;i++)
        {
            if(constTable[i]==null)
            {
                constTable[i]=strToken;
                return i;
            }
        }
        return -1;//constTable�Ѿ�����
    }

    public void Display_Error()//չʾ����
    {
       // System.out.println();
        System.out.print("Error_line�� "+line);
        System.out.println();


    }

/*
 �ַ�ʶ����
ͨ����ȡbuffer�����еĵ����ַ�����ʶ��Դ�����еĸ���Ԫ�أ�ÿ��ʶ��һ��Ԫ��
 ʶ������ַ������ԣ�RV.getId()�����ԣ�RV.getValue()��ֵ��RV.getLine()�������к�
*/
    
    public WriteValue analysis()
    {
        int code,value;
        strToken="";
        
        getChar();//�õ�һ���ַ�ch��ȫ�ֱ�������ָ�������Ѿ�searchPtr=0
        getBC();//�����˲������ch=='\n'����϶��ǵ������ļ�ĩβ���õ���һ���ַ�ch
        
        if(ch=='\n')
        {
            rv.setId(-1);
            rv.setValue("-1");
            rv.setLine(line);
            return rv;
        }
        
        if(isLetter())//�õ����ַ���ͷ�ĵ���
        {
            while((isLetter()||isDigit()))
            {
                concat();//����
                getChar();//searchPtr++���õ��ַ�ch
            }
            retract();//searchPtr--��ch=�� ��
            
            code=reserve();//��ؼ�������Ƚϣ��õ��±�
            
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
  ѭ��ʶ��������ַ���������ļ�lex.txt��
 */
    public void bAnalysis(){
        preManage();//��ȡԴ�ļ����ݵ��ַ�����buffer��ȥ���������з�
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
        	temp=analysis();//����Writevalue����rv
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
            //������ʾ ������
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


















