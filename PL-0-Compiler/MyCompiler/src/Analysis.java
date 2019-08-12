/*
 �ؼ���const��Ӧ�ı�����con
 ��������Ӧ�ı�����const
 ����������ű�����ַ��1
 * ������¼���ű�����ַ��1
 * ���̵�¼���ű����Ƚ�level��1�����������address��0��������ɺ��ٽ�level��һ���ٽ�address�ָ���
 */
import java.io.*;
public class Analysis {
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
    private static  int ODD=16;//  oddl      ��keyWord��ÿ���ֵ��������ȵ�

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
   // private static  int POI=32;//"."
    private static  int LBR=33;//"("
    private static  int RBR=34;//")"

    LexAnalysis lex;
    private boolean Is_error=false;
    private int rvLength=1000;
    private WriteValue[] rv=new WriteValue[rvLength];
    private int Ptr=0;       //ThreeValue�ĵ�����

    private SymbolTable Table=new SymbolTable();//���ű�
    private TotalPcode  Pcode=new TotalPcode();//���Ŀ�����


    private int level=0;                //������Ϊ��0��
    private int address=0;             //������������������Ϊ0
    
    public Analysis(String filename){
        for(int i=0;i<rvLength;i++){
            rv[i]=new WriteValue();
            rv[i].setId(-2);
            rv[i].setValue("-2");
        }
        lex=new LexAnalysis(filename);
    }

    public void readLex(){
        String filename="lex.txt";
        File file=new File(filename);
        BufferedReader ints=null;
        String tempLex,temp[];
        try{
            ints=new BufferedReader(new FileReader(file));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        try{
            int i=0;
            while((tempLex=ints.readLine())!=null) {
                temp = tempLex.split(" ");
                rv[i].setId(Integer.parseInt(temp[0], 10));
                rv[i].setValue(temp[1]);
                rv[i].setLine(Integer.parseInt(temp[2]));
                i++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
//<prog> �� program <id>��<block>
    public void prog()
    {
        if(rv[Ptr].getId()==PROG)
        {
            Ptr++;
            if(rv[Ptr].getId()!=SYM)
            {
                Is_error=true;
                Display_Error(1,"");
            }
            else
            {
                Ptr++;
                if(rv[Ptr].getId()!=SEMIC)
                {
                    Is_error=true;
                    Display_Error(0,"");
                    return;
                }
                else
                {
                    Ptr++;
                    block();
                }
            }
        }
        else 
        {
            Is_error = true;
            Display_Error(2,"");
            return;
        }
    }
//<block> �� [<condecl>][<vardecl>][<proc>]<body>
    public void block()
    {
        int addr0=address;      //��¼����֮ǰ�����������Ա�ָ�ʱ����
        int tx0=Table.getTablePtr();       //��¼�������ֵĳ�ʼλ��
        int  location=0;
        if(tx0>0)
        {
            location=Table.getLevelPorc(level);//�õ��ڷ��ű�ĵڼ���
            tx0=tx0- Table.getRow(location).getSize();   
            //��¼����������Ŀ�ʼλ��
        }
        if(tx0==0)
        {
            address=3;      //ÿһ���ʼλ�õ������ռ�������ž�̬��SL����̬��DL���ͷ��ص�ַRA
        }
        else
        {
            //ÿһ���ʼλ�õ������ռ�������ž�̬��SL����̬��DL���ͷ��ص�ַRA
            //�����ŷ��βεĸ���
            address=3+Table.getAllTable()[location].getSize();
        }
        //�ݴ浱ǰPcode.codePtr��ֵ����jmp,0,0��codePtr�е�λ�ã�����һ�����
        int tempCodePtr= Pcode.getCodePtr();
        Pcode.gen(Pcode.getJMP(),0,0);

      //�˴�û��terPtr++
        if(rv[Ptr].getId()==CON)
        {
            condecl();
        } 
        if(rv[Ptr].getId()==VAR)
        {
            vardecl();
        } if(rv[Ptr].getId()==PROC)
        {
            proc();
            level--;
        }
        /*����������ɣ�������䴦���֣�֮ǰ���ɵ�jmp��0��0Ӧ����ת�����λ��*/
        //����jmp��0��0����ת��ַ
        if(tx0>0)
        {
            for(int i=0;i<Table.getAllTable()[location].getSize();i++)
            {
                Pcode.gen(Pcode.getSTO(),0,Table.getAllTable()[location].getSize()+3-1-i);
            }
        }
        Pcode.getPcodeArray()[tempCodePtr].setA(Pcode.getCodePtr());
        
        Pcode.gen(Pcode.getINT(),0,address);        //���ɷ����ڴ�Ĵ���
        if(tx0==0)
        {
           // Table.getRow(tx0).setValue(Pcode.getCodePtr());     //���������ڷ��ű��е�ֵ��Ϊ������ִ����俪ʼ��λ��
        }
        else
        {
            Table.getRow(location).setValue(Pcode.getCodePtr()-1-Table.getAllTable()[location].getSize());     //���������ڷ��ű��е�ֵ��Ϊ������ִ����俪ʼ��λ��

        }

        body();
        Pcode.gen(Pcode.getOPR(),0,0);       //�����˳����̵Ĵ��룬������������ֱ���˳�����

        address=addr0;      //�ֳ���������ָ����ֵ
        Table.setTablePtr(tx0);

    }
//<condecl> �� const <const>{,<const>};
    public void condecl(){          //const��level��������ģ���PPT42
        if(rv[Ptr].getId()==CON)
        {
            Ptr++;
            myconst();
            while(rv[Ptr].getId()==COMMA)
            {
                Ptr++;
                myconst();
            }
            if(rv[Ptr].getId()!=SEMIC)
            {
                Is_error=true;
                Display_Error(0,"");
                return;
            }
            else
            {
                Ptr++;
            }
        }
        else
        {
            Is_error=true;
            Display_Error(-1,"");
            return;
        }
    }
//<const> �� <id>:=<integer>
    public void myconst()
    {
        String name;
        int value;
        if(rv[Ptr].getId()==SYM)
        {
            name=rv[Ptr].getValue();
            Ptr++;
            if(rv[Ptr].getId()==CEQU)
            {
                Ptr++;
                if(rv[Ptr].getId()==CONST)
                {
                    value=Integer.parseInt(rv[Ptr].getValue());
                    if(Table.Is_NowTable(name, level))
                    {
                        Is_error=true;
                        Display_Error(15,name);
                    }
                    Table.enterConst(name,level,value,address);
                    Ptr++;
                }
            }
            else
            {
                Is_error=true;
                Display_Error(3,"");
                return;
            }
        }
        else
        {
            Is_error=true;
            Display_Error(1,"");
            return;
        }
    }
//<vardecl> �� var <id>{,<id>};
    public void vardecl()
    {
        String name;
        if(rv[Ptr].getId()==VAR)
        {
            Ptr++;
            if(rv[Ptr].getId()==SYM)
            {
                name=rv[Ptr].getValue();
                if(Table.Is_NowTable(name, level))
                {
                    Is_error=true;
                    Display_Error(15,name);
                }
                Table.enterVar(name,level,address);
                address+=1;
                Ptr++;
                while(rv[Ptr].getId()==COMMA)
                {
                    Ptr++;
                    if(rv[Ptr].getId()==SYM)
                    {
                        name=rv[Ptr].getValue();
                        if(Table.Is_NowTable(name, level))
                        {
                            Is_error=true;
                            Display_Error(15,name);
                        }
                        Table.enterVar(name,level,address);
                        address+=1;     //��ַ��1��¼���ű�
                        Ptr++;
                    }
                    else
                    {
                        Is_error=true;
                        Display_Error(1,"");
                        return;
                    }
                }
                if(rv[Ptr].getId()!=SEMIC)
                {
                    Is_error=true;
                    Display_Error(0,"");
                    return;
                }
                else
                {
                    Ptr++;
                }
            }
            else 
            {
                Is_error=true;
                Display_Error(1,"");
                return;
            }

        }
        else
        {
            Is_error=true;
            Display_Error(-1,"");
            return;
        }
    }
//<proc> �� procedure <id>��[<id>{,<id>}]��;<block>{;<proc>}
    public void proc()
    {
        if(rv[Ptr].getId()==PROC)
        {
            Ptr++;
            //<id>;
            int count=0;//������¼proc���βεĸ���
            int location;// ��¼��proc�ڷ��ű��е�λ��
            if(rv[Ptr].getId()==SYM)
            {
                String name=rv[Ptr].getValue();//�õ�<id>
                if(Table.Is_NowTable(name, level))//����Ƿ��Ѿ����ڷ��ű�
                {
                    Is_error=true;
                    Display_Error(15,name);
                }
                location=Table.getTablePtr();//�õ����ű�����
                Table.enterProc(rv[Ptr].getValue(),level,address);//enterProc
                level++;                //levelֵ��һ����Ϊ�������ж�����ڸ��µ�proc�����
                Ptr++;
                if(rv[Ptr].getId()==LBR)
                {
                    Ptr++;
                    //�õ�<id>;
                    if(rv[Ptr].getId()==SYM)
                    {
                        Table.enterVar(rv[Ptr].getValue(),level,3+count) ; //enterVar
                        //3+count+1Ϊ�β��ڴ洢�ռ��е�λ��
                        count++;
                        Table.getAllTable()[location].setSize(count);
                        //�ñ������ڷ��ű��е�size���¼�βεĸ���
                        Ptr++;
                        while(rv[Ptr].getId()==COMMA)
                        {
                            Ptr++;
                            if(rv[Ptr].getId()==SYM)
                            {
                                Table.enterVar(rv[Ptr].getValue(),level,3+count) ;     
                                //3+count+1Ϊ�β��ڴ洢�ռ��е�λ��
                                count++;
                                Table.getAllTable()[location].setSize(count);        
                                //�ñ������ڷ��ű��е�size���¼�βεĸ���
                                Ptr++;
                            }
                            else
                            {
                                Is_error=true;
                                Display_Error(1,"");
                                return;
                            }
                        }
                    }
                    if(rv[Ptr].getId()==RBR)
                    {
                        Ptr++;
                        if(rv[Ptr].getId()!=SEMIC)
                        {
                            Is_error=true;
                            Display_Error(0,"");
                            return;
                        }
                        else
                        {
                            Ptr++;
                            block();
                            while(rv[Ptr].getId()==SEMIC)
                            {
                                Ptr++;
                                proc();
                            }
                        }
                    }
                    else
                    {
                        Is_error=true;
                        Display_Error(5,"");
                        return;
                    }

                }
                else
                {
                    Is_error=true;
                    Display_Error(4,"");
                    return;
                }
            }
            else
            {
                Is_error=true;
                Display_Error(1,"");
                return;
            }

        }
        else
        {
            Is_error=true;
            Display_Error(-1,"");
            return;
        }
    }
    //<body> �� begin <statement>{;<statement>}end
    public void body()
    {
        if(rv[Ptr].getId()==BEG)
        {
        	Ptr++;
            statement();
            while(rv[Ptr].getId()==SEMIC)
            {
                Ptr++;
                statement();
            }
            if(rv[Ptr].getId()==END)
            {
                Ptr++;
            }
            else
            {
                Is_error=true;
                Display_Error(7,"");
                return;
            }
        }
        else
        {
            Is_error=true;
            Display_Error(6,"");
            return;
        }
    }
/*<statement> �� <id> := <exp>               
|if <lexp> then <statement>[else <statement>]
               |while <lexp> do <statement>
               |call <id>��[<exp>{,<exp>}]��
               |<body>
               |read (<id>{��<id>})
               |write (<exp>{,<exp>})*/
    public void statement()
    {
//if <lexp> then <statement>[else <statement>]
        if(rv[Ptr].getId()==IF)
        {
            int cx1;
            Ptr++;
            lexp();
            if(rv[Ptr].getId()==THEN)
            {
                cx1=Pcode.getCodePtr();             
                //��cx1��¼jpc ��0��0������������һ����������Ŀ����룩��Pcode�еĵ�ַ������һ�����
                Pcode.gen(Pcode.getJPC(),0,0);  
                //��������ת��ָ�������boolֵΪ0ʱ��ת����ת��Ŀ�ĵ�ַ��ʱ��Ϊ0
                Ptr++;
                statement();
                
                int cx2=Pcode.getCodePtr(); 
                //��cx2��¼boolֵΪ1��ִ��statement()����Ŀ������λ��
                
                Pcode.gen(Pcode.getJMP(),0,0);
                Pcode.getPcodeArray()[cx1].setA(Pcode.getCodePtr());       
                //��ַ�����jpc��0��0�е�A����Ϊcx2=Pcode.getCodePtr()
               // Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());
                //JMP��������ת�ƣ���ʱA��Ϊת���ַ��Ŀ�����Ҳ��cx2
                if(rv[Ptr].getId()==ELS)
                {
                    Ptr++;
                    statement();
                    Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());
                  //��ַ�����JMP��0��0�е�A����Ϊִ��statement()����Ŀ������λ��
                }
            }
            else
            {
                Is_error=true;
                Display_Error(8,"");
                return;
            }
        }
//while <lexp> do <statement>
        else if(rv[Ptr].getId()==WHI)
        {
            int cx1=Pcode.getCodePtr();     //�����������ʽ��Pcode�еĵ�ַ
            Ptr++;
            lexp();
            if(rv[Ptr].getId()==DO)
            {
                int cx2=Pcode.getCodePtr();    
                //����������תָ��ĵ�ַ���ڻ���ʱʹ�ã�������������������ת
                //JPC������ת�ƣ�������ջS��ջ���Ĳ���ֵΪ�٣�0��ʱ����ת��A����ָĿ������ַ��
                //����˳��ִ�С�
                Pcode.gen(Pcode.getJPC(),0,0);//Ϊ�� ����
                Ptr++;
                statement();
                
                Pcode.gen(Pcode.getJMP(),0,cx1); // ѭ��ִ�� 
                //���DO������������Ҫ��ת���������ʽ��������Ƿ�������������Ƿ����ѭ��
                Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());       
                //��������ת��ָ��//������
            }
            else
            {
                Is_error=true;
                Display_Error(9,"");
                return;
            }
        }
//call <id>��[<exp>{,<exp>}]��
        else if(rv[Ptr].getId()==CAL)
        {
            Ptr++;
            //id();
            int count=0;//�������鴫��Ĳ������趨�Ĳ����Ƿ����
            Table tempRow;
            if(rv[Ptr].getId()==SYM)
            {
            	//����Ƿ��Ǳ�����Ȼ�����ʶ�������Ƿ���proc
                if(Table.Is_PreTable(rv[Ptr].getValue(),level))
                {     
                     tempRow=Table.getRow(Table.getNameRow(rv[Ptr].getValue())); 
                    if(tempRow.getType()!=Table.getProc()) 
                    {
                        Is_error=true;
                        Display_Error(11,"");
                        return;
                    }
                }      
                else
                {           //cal δ��������Ĵ���
                    Is_error=true;
                    Display_Error(10,"");
                    return;
                }
                Ptr++;
                if(rv[Ptr].getId()==LBR)
                {
                    Ptr++;
                    if(rv[Ptr].getId()==RBR)//�޲���
                    {
                        Ptr++;
                        Pcode.gen(Pcode.getCAL(),level-tempRow.getLevel(),tempRow.getValue());       
                    }
                    else//�в���
                    {
                        exp();
                        count++;
                        while(rv[Ptr].getId()==COMMA)
                        {
                            Ptr++;
                            exp();
                            count++;
                        }
                        if(count!=tempRow.getSize())
                        {
                            Is_error=true;
                            Display_Error(8,tempRow.getName());
                            return;
                        }
                        Pcode.gen(Pcode.getCAL(),level-tempRow.getLevel(),tempRow.getValue());        //���ù����еı����ֳ��ɽ��ͳ�����ɣ�����ֻ����Ŀ�����,+3����ϸ˵��
                        if(rv[Ptr].getId()==RBR)
                        {
                            Ptr++;
                        }
                        else
                        {
                            Is_error=true;
                            Display_Error(5,"");
                            return;
                        }
                    }
                }
                else
                {
                    Is_error=true;
                    Display_Error(4,"");
                    return;
                }
            }
            else
            {
                Is_error=true;
                Display_Error(1,"");
                return;
            }

        }
//read (<id>{��<id>})
        else if(rv[Ptr].getId()==REA)
        {
            Ptr++;
            if(rv[Ptr].getId()==LBR)
            {
                Ptr++;
                //<id>
                if(rv[Ptr].getId()==SYM)
                {
                    if(!Table.Is_PreTable((rv[Ptr].getValue()),level))
                    {     
                    	//�����ж��ڷ��ű����ڱ���򱾲�֮ǰ�Ƿ��д˱���
                        Is_error=true;
                        Display_Error(10,"");
                        return;

                    }//if�ж��ڷ��ű����Ƿ��д˱���
                    else
                    {           //stoδ��������Ĵ���
                        Table tempTable=Table.getRow(Table.getNameRow(rv[Ptr].getValue()));
                        if(tempTable.getType()==Table.getVar())
                        {       //�ñ�ʶ���Ƿ�Ϊ��������
                            Pcode.gen(Pcode.getRED(),0,0);         //RED�������ж���һ����������ջ��  
                            Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress()); 
                            //STO L ��a ������ջջ�������ݴ����������Ե�ַΪa����β�ΪL��
                        }//if��ʶ���Ƿ�Ϊ��������
                        else
                        {       //sto���Ͳ�һ�µĴ���
                            Is_error=true;
                            Display_Error(12,"");
                            return;
                        }
                    }
                    Ptr++;
                    while(rv[Ptr].getId()==COMMA)
                    {
                        Ptr++;
                        if(rv[Ptr].getId()==SYM){
                            if(!Table.Is_PreTable((rv[Ptr].getValue()),level))
                            {      //�����ж��ڷ��ű����Ƿ��д˱���
                                Is_error=true;
                                Display_Error(10,"");
                                return;

                            }//if�ж��ڷ��ű����Ƿ��д˱���
                            else
                            {          
                                Table tempTable=Table.getRow(Table.getNameRow(rv[Ptr].getValue()));
                                if(tempTable.getType()==Table.getVar())
                                {       //�ñ�ʶ���Ƿ�Ϊ��������
                                    Pcode.gen(Pcode.getRED(),0,0);         //RED 0,0	�������ж���һ����������ջ��  
                                    Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress());  //STO L ��a ������ջջ�������ݴ����������Ե�ַΪa����β�ΪL��
                                }//if��ʶ���Ƿ�Ϊ��������
                                else
                                {       //sto���Ͳ�һ�µĴ���
                                    Is_error=true;
                                    Display_Error(12,"");
                                    return;
                                }
                            }
                            Ptr++;
                        }
                        else
                        {
                            Is_error=true;
                            Display_Error(1,"");
                            return;
                        }
                    }
                    if(rv[Ptr].getId()==RBR)
                    {
                        Ptr++;
                    }
                    else
                    {
                        Is_error=true;
                        Display_Error(25,"");
                    }
                }
                else
                {
                    Is_error=true;
                    Display_Error(26,"");
                }
            }
            else
            {
                Is_error=true;
                Display_Error(4,"");
                return;
            }
        }
//write (<exp>{,<exp>})
        else if(rv[Ptr].getId()==WRI)
        {
            Ptr++;
            if(rv[Ptr].getId()==LBR)
            {
                Ptr++;
                exp();
                Pcode.gen(Pcode.getWRT(),0,0);         //���ջ����ֵ����Ļ
                while(rv[Ptr].getId()==COMMA)
                {
                    Ptr++;
                    exp();
                    Pcode.gen(Pcode.getWRT(),0,0);         //���ջ����ֵ����Ļ
                }

                Pcode.gen(Pcode.getOPR(),0,14);         //�������
                if(rv[Ptr].getId()==RBR)
                {
                    Ptr++;
                }
                else
                {
                    Is_error=true;
                    Display_Error(5,"");
                    return;
                }
            }
            else
            {
                Is_error=true;
                Display_Error(4,"");
                return;
            }
        }
//body()
        else if(rv[Ptr].getId()==BEG)
        {       
        	//body������Ŀ�����
            body();
        }
//<id> := <exp>         
        else if(rv[Ptr].getId()==SYM)
        {      //��ֵ���
            String name=rv[Ptr].getValue();
            Ptr++;
            if(rv[Ptr].getId()==CEQU)
            {
                Ptr++;
                exp();
                
                if(!Table.Is_PreTable(name,level))
                {        //����ʶ���Ƿ��ڷ��ű��д���
                    Is_error=true;
                    Display_Error(15,name);
                    return;
                }//if�ж��ڷ��ű����Ƿ��д˱���
                else
                {           //stoδ��������Ĵ���
                    Table tempTable=Table.getRow(Table.getNameRow(name));
                    if(tempTable.getType()==Table.getVar())
                    {           //����ʶ���Ƿ�Ϊ��������
                        Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress());  //STO L ��a ������ջջ�������ݴ������
                    }////����ʶ���Ƿ�Ϊ��������
                    else
                    {       //���Ͳ�һ�µĴ���
                        Is_error=true;
                        Display_Error(13,name);
                        return;
                    }
                }
            }
            else
            {
                Is_error=true;
                Display_Error(3,"");
                return;
            }
        }
        else
        {
            Is_error=true;
            Display_Error(1,"");
            return;
        }
    }
//<lexp> �� <exp> <lop> <exp>|odd <exp>
    public void lexp()
    {
        if(rv[Ptr].getId()==ODD)
        {
            Ptr++;
            exp();
            Pcode.gen(Pcode.getOPR(),0,6);  //OPR 0 6	ջ��Ԫ�ص���ż�жϣ����ֵ��ջ��
        }
        else
        {
            exp();
            int compare=lop();        //����ֵ��������Ŀ����룬����
            exp();
            
            if(compare==EQU)
            {
                Pcode.gen(Pcode.getOPR(),0,8);      //OPR 0 8	��ջ����ջ���Ƿ���ȣ�������ջԪ�أ����ֵ��ջ
            }
            else if(compare==NEQE)
            {
                Pcode.gen(Pcode.getOPR(),0,9);      //OPR 0 9	��ջ����ջ���Ƿ񲻵ȣ�������ջԪ�أ����ֵ��ջ
            }
            else if(compare==LES)
            {
                Pcode.gen(Pcode.getOPR(),0,10);     //OPR 0 10	��ջ���Ƿ�С��ջ����������ջԪ�أ����ֵ��ջ
            }
            else if(compare==LESE)
            {
                Pcode.gen(Pcode.getOPR(),0,13);     // OPR 0 13	��ջ���Ƿ�С�ڵ���ջ����������ջԪ�أ����ֵ��ջ
            }
            else if(compare==LAR)
            {
                Pcode.gen(Pcode.getOPR(),0,12);     //OPR 0 12	��ջ���Ƿ����ջ����������ջԪ�أ����ֵ��ջ
            }
            else if(compare==LARE)
            {
                Pcode.gen(Pcode.getOPR(),0,11);     //OPR 0 11	��ջ���Ƿ���ڵ���ջ����������ջԪ�أ����ֵ��ջ
            }
        }
    }
//<exp> �� [+|-]<term>{<aop><term>}
    public void exp(){
        int tempId=rv[Ptr].getId();
        if(rv[Ptr].getId()==ADD)
        {
            Ptr++;
        }
        else if(rv[Ptr].getId()==SUB)
        {
            Ptr++;
        }
        term();
        if(tempId==SUB)
        {
            Pcode.gen(Pcode.getOPR(),0,1);      //  OPR 0 1	ջ��Ԫ��ȡ��
        }
        while(rv[Ptr].getId()==ADD||rv[Ptr].getId()==SUB)
        {
            tempId=rv[Ptr].getId();
            Ptr++;
            term();
            if(tempId==ADD)
            {
                Pcode.gen(Pcode.getOPR(),0,2);       //OPR 0 2	��ջ����ջ����ӣ�������ջԪ�أ����ֵ��ջ
            }
            else if(tempId==SUB)
            {
                Pcode.gen(Pcode.getOPR(),0,3);      //OPR 0 3	��ջ����ȥջ����������ջԪ�أ����ֵ��ջ
            }
        }
    }
//<term> �� <factor>{<mop><factor>}
    public void term()
    {
        factor();
        while(rv[Ptr].getId()==MUL||rv[Ptr].getId()==DIV){
            int tempId=rv[Ptr].getId();
            Ptr++;
            factor();
            if(tempId==MUL){
                Pcode.gen(Pcode.getOPR(),0,4);       //OPR 0 4	��ջ������ջ����������ջԪ�أ����ֵ��ջ
            }else if(tempId==DIV){
                Pcode.gen(Pcode.getOPR(),0,5);      // OPR 0 5	��ջ������ջ����������ջԪ�أ����ֵ��ջ
            }
        }
    }
//<factor>��<id>|<integer>|(<exp>)
    public void factor()
    {
        if(rv[Ptr].getId()==CONST)
        {
            Pcode.gen(Pcode.getLIT(),0,Integer.parseInt(rv[Ptr].getValue()));    //�Ǹ�����,  LIT 0 a ȡ����a��������ջջ��
            Ptr++;
        }
        else if(rv[Ptr].getId()==LBR)
        {
            Ptr++;
            exp();
            if(rv[Ptr].getId()==RBR)
            {
                Ptr++;
            }
            else
            {
                Is_error=true;
                Display_Error(5,"");
            }
        }
        else if(rv[Ptr].getId()==SYM)
        {
            String name=rv[Ptr].getValue();
            if(!Table.Is_PreTable(name,level))
            {     //�жϱ�ʶ���ڷ��ű����Ƿ����
                Is_error=true;
                Display_Error(10,"");
                return;
            }
            else//���� ����ȡֵ ������ֵ
            {           //δ��������Ĵ���
                Table tempRow= Table.getRow(Table.getNameRow(name));
                if(tempRow.getType()==Table.getVar())
                { //��ʶ���Ǳ�������
                    Pcode.gen(Pcode.getLOD(),level-tempRow.getLevel(),tempRow.getAddress());   
                    //������LOD L  ȡ��������Ե�ַΪa�����ΪL���ŵ�����ջ��ջ��
                }
                else if (tempRow.getType()==Table.getMyconst())
                {//��ʶ���ǳ�������
                    Pcode.gen(Pcode.getLIT(),0,tempRow.getValue());        
                    //������LIT 0 a ȡ����a��������ջջ��
                }
                else
                {       //���Ͳ�һ�µĴ���
                    Is_error=true;
                    Display_Error(12,"");
                    return;
                }
            }
            Ptr++;
        }
        else 
        {
            Is_error=true;
            Display_Error(1,"");
        }
    }
//<lop> �� =|<>|<|<=|>|>=
    public int lop(){
        if(rv[Ptr].getId()==EQU){
            Ptr++;
            return EQU;
        }else if(rv[Ptr].getId()==NEQE){
            Ptr++;
            return NEQE;
        }else if(rv[Ptr].getId()==LES){
            Ptr++;
            return LES;
        }else if(rv[Ptr].getId()==LESE){
            Ptr++;
            return LESE;
        }else if(rv[Ptr].getId()==LAR){
            Ptr++;
            return LAR;
        }else if(rv[Ptr].getId()==LARE){
            Ptr++;
            return LARE;
        }
        return -1;
    }

    public boolean GramAnalysis(){
        lex.bAnalysis();//�ʷ�����
        readLex();//��lex.txt�ļ��е����ݶ���ThreeValue rv[]
        prog();//�﷨��������������ű�Ľ���������Ŀ�����
        return Is_error;
    }

    public void showtable(){
        System.out.println("(i),type,name,level,address,value,size");
        for(int i=0;i<Table.getLength();i++){
            System.out.println(("(")+i+(") ")+Table.getRow(i).getType()+"  "+ Table.getRow(i).getName()+"  "+Table.getRow(i).getLevel()+"  "+Table.getRow(i).getAddress()+"  "+Table.getRow(i).getValue()+
            "  "+Table.getRow(i).getSize());
        }
    }

    public void showPcode(){
        for(int i=0;i<Pcode.getCodePtr();i++)
        {
        	 System.out.print(("(")+i+(") "));
           switch (Pcode.getPcodeArray()[i].getF())
           {
               case 0:
                   System.out.print("LIT  ");
                   break;
               case 1:
                   System.out.print("OPR  ");
                   break;
               case 2:
                   System.out.print("LOD  ");
                   break;
               case 3:
                   System.out.print("STO  ");
                   break;
               case 4:
                   System.out.print("CAL  ");
                   break;
               case 5:
                   System.out.print("INT  ");
                   break;
               case 6:
                   System.out.print("JMP  ");
                   break;
               case 7:
                   System.out.print("JPC  ");
                   break;
               case 8:
                   System.out.print("RED  ");
                   break;
               case 9:
                   System.out.print("WRI  ");
                   break;
           }
            System.out.println(Pcode.getPcodeArray()[i].getL()+"  "+Pcode.getPcodeArray()[i].getA());
        }
    }

    public void showPcodeInStack(){
        Interpreter inter=new Interpreter();
        inter.setPcode(Pcode);
        for(int i=0;i<inter.getCode().getCodePtr();i++)
        {
        	 System.out.print(("(")+i+(") "));
            switch (inter.getCode().getPcodeArray()[i].getF())
            {
                case 0:
                    System.out.print("LIT  ");
                    break;
                case 1:
                    System.out.print("OPR  ");
                    break;
                case 2:
                    System.out.print("LOD  ");
                    break;
                case 3:
                    System.out.print("STO  ");
                    break;
                case 4:
                    System.out.print("CAL  ");
                    break;
                case 5:
                    System.out.print("INT  ");
                    break;
                case 6:
                    System.out.print("JMP  ");
                    break;
                case 7:
                    System.out.print("JPC  ");
                    break;
                case 8:
                    System.out.print("RED  ");
                    break;
                case 9:
                    System.out.print("WRI  ");
                    break;

            }
            System.out.println(inter.getCode().getPcodeArray()[i].getL()+"  "+inter.getCode().getPcodeArray()[i].getA());
        }


    }

    public void Display_Error(int i,String name){
        switch (i){
            case -1:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("�������岻��const��ͷ,�������岻��var ��ͷ");        //�������岻��const��ͷ,�������岻��var ��ͷ
                break;
            case 0:
                System.out.print("Error�� "+i+" "+"in line " + (rv[Ptr].getLine()-1)+":");
                System.out.println("ȱ�ٷֺ�");        //ȱ�ٷֺ�
                break;
            case 1:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("��ʶ�����Ϸ�");       //��ʶ�����Ϸ�
                break;
            case 2:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("����ʼ������program");       //����ʼ��һ���ַ�������program
                break;
            case 3:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("��ֵû�ã�=");       //��ֵû�ã�=
                break;
            case 4:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("ȱ��������");       //ȱ��������
                break;
            case 5:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("ȱ��������");       //ȱ��������
                break;
            case 6:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("ȱ��begin");       //ȱ��begin
                break;
            case 7:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("ȱ��end");       //ȱ��end
                break;
            case 8:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("ȱ��then");       //ȱ��then
                break;
            case 9:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("ȱ��do");       //ȱ��do
                break;
            case 10:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("call��write��read����У������ڱ�ʶ��"+"'"+rv[Ptr].getValue()+"'");  
                break;
            case 11:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("'"+rv[Ptr].getValue()+"'"+"�ñ�ʶ������proc����"); 
                break;
            case 12:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("'"+rv[Ptr].getValue()+"'"+"read��write����У��ñ�ʶ������var����");     
            case 13:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("'"+name+"'"+" ��ֵ����У��ñ�ʶ������var����");      
                break;
            case 14:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("��ֵ����У��ñ�ʶ��������"+"'"+name+"'");      
                break;
            case 15:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("�ñ�ʶ���Ѿ�����"+"'"+name+"'");      
                break;
            case 16:
                System.out.print("Error�� "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("�ñ�ʶ���Ѿ����� "+"'"+name+"'");      
                break;
        }

    }
    public void interpreter()
    {
        if(Is_error)
        {
            return;
        }
        Interpreter inter=new Interpreter();
        inter.setPcode(Pcode);//����CODE�洢��
        inter.interpreter();
    }

}

