/*
 关键字const对应的变量是con
 而常数对应的变量是const
 常量登入符号表：将地址加1
 * 变量登录符号表：将地址加1
 * 过程登录符号表：首先将level加1，传入参数将address置0，过程完成后再将level减一，再将address恢复，
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
    private static  int ODD=16;//  oddl      和keyWord中每个字的序号是相等的

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
   // private static  int POI=32;//"."
    private static  int LBR=33;//"("
    private static  int RBR=34;//")"

    LexAnalysis lex;
    private boolean Is_error=false;
    private int rvLength=1000;
    private WriteValue[] rv=new WriteValue[rvLength];
    private int Ptr=0;       //ThreeValue的迭代器

    private SymbolTable Table=new SymbolTable();//符号表
    private TotalPcode  Pcode=new TotalPcode();//存放目标代码


    private int level=0;                //主程序为第0层
    private int address=0;             //主程序或变量的声明是为0
    
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
//<prog> → program <id>；<block>
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
//<block> → [<condecl>][<vardecl>][<proc>]<body>
    public void block()
    {
        int addr0=address;      //记录本层之前的数据量，以便恢复时返回
        int tx0=Table.getTablePtr();       //记录本层名字的初始位置
        int  location=0;
        if(tx0>0)
        {
            location=Table.getLevelPorc(level);//得到在符号表的第几行
            tx0=tx0- Table.getRow(location).getSize();   
            //记录本本层变量的开始位置
        }
        if(tx0==0)
        {
            address=3;      //每一层最开始位置的三个空间用来存放静态连SL、动态连DL、和返回地址RA
        }
        else
        {
            //每一层最开始位置的三个空间用来存放静态连SL、动态连DL、和返回地址RA
            //紧接着放形参的个数
            address=3+Table.getAllTable()[location].getSize();
        }
        //暂存当前Pcode.codePtr的值，即jmp,0,0在codePtr中的位置，用来一会回填
        int tempCodePtr= Pcode.getCodePtr();
        Pcode.gen(Pcode.getJMP(),0,0);

      //此处没有terPtr++
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
        /*声明部分完成，进入语句处理部分，之前生成的jmp，0，0应当跳转到这个位置*/
        //回填jmp，0，0的跳转地址
        if(tx0>0)
        {
            for(int i=0;i<Table.getAllTable()[location].getSize();i++)
            {
                Pcode.gen(Pcode.getSTO(),0,Table.getAllTable()[location].getSize()+3-1-i);
            }
        }
        Pcode.getPcodeArray()[tempCodePtr].setA(Pcode.getCodePtr());
        
        Pcode.gen(Pcode.getINT(),0,address);        //生成分配内存的代码
        if(tx0==0)
        {
           // Table.getRow(tx0).setValue(Pcode.getCodePtr());     //将本过程在符号表中的值设为本过程执行语句开始的位置
        }
        else
        {
            Table.getRow(location).setValue(Pcode.getCodePtr()-1-Table.getAllTable()[location].getSize());     //将本过程在符号表中的值设为本过程执行语句开始的位置

        }

        body();
        Pcode.gen(Pcode.getOPR(),0,0);       //生成退出过程的代码，若是主程序，则直接退出程序

        address=addr0;      //分程序结束，恢复相关值
        Table.setTablePtr(tx0);

    }
//<condecl> → const <const>{,<const>};
    public void condecl(){          //const的level是无意义的，见PPT42
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
//<const> → <id>:=<integer>
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
//<vardecl> → var <id>{,<id>};
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
                        address+=1;     //地址加1登录符号表
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
//<proc> → procedure <id>（[<id>{,<id>}]）;<block>{;<proc>}
    public void proc()
    {
        if(rv[Ptr].getId()==PROC)
        {
            Ptr++;
            //<id>;
            int count=0;//用来记录proc中形参的个数
            int location;// 记录本proc在符号表中的位置
            if(rv[Ptr].getId()==SYM)
            {
                String name=rv[Ptr].getValue();//得到<id>
                if(Table.Is_NowTable(name, level))//检查是否已经存在符号表
                {
                    Is_error=true;
                    Display_Error(15,name);
                }
                location=Table.getTablePtr();//得到符号表索引
                Table.enterProc(rv[Ptr].getValue(),level,address);//enterProc
                level++;                //level值加一，因为其后的所有定义均在该新的proc中完成
                Ptr++;
                if(rv[Ptr].getId()==LBR)
                {
                    Ptr++;
                    //得到<id>;
                    if(rv[Ptr].getId()==SYM)
                    {
                        Table.enterVar(rv[Ptr].getValue(),level,3+count) ; //enterVar
                        //3+count+1为形参在存储空间中的位置
                        count++;
                        Table.getAllTable()[location].setSize(count);
                        //用本过程在符号表中的size域记录形参的个数
                        Ptr++;
                        while(rv[Ptr].getId()==COMMA)
                        {
                            Ptr++;
                            if(rv[Ptr].getId()==SYM)
                            {
                                Table.enterVar(rv[Ptr].getValue(),level,3+count) ;     
                                //3+count+1为形参在存储空间中的位置
                                count++;
                                Table.getAllTable()[location].setSize(count);        
                                //用本过程在符号表中的size域记录形参的个数
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
    //<body> → begin <statement>{;<statement>}end
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
/*<statement> → <id> := <exp>               
|if <lexp> then <statement>[else <statement>]
               |while <lexp> do <statement>
               |call <id>（[<exp>{,<exp>}]）
               |<body>
               |read (<id>{，<id>})
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
                //用cx1记录jpc ，0，0（就是下面这一条语句产生的目标代码）在Pcode中的地址，用来一会回填
                Pcode.gen(Pcode.getJPC(),0,0);  
                //产生条件转移指令，条件的bool值为0时跳转，跳转的目的地址暂时填为0
                Ptr++;
                statement();
                
                int cx2=Pcode.getCodePtr(); 
                //用cx2记录bool值为1后执行statement()过后目标代码的位置
                
                Pcode.gen(Pcode.getJMP(),0,0);
                Pcode.getPcodeArray()[cx1].setA(Pcode.getCodePtr());       
                //地址回填，将jpc，0，0中的A回填为cx2=Pcode.getCodePtr()
               // Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());
                //JMP：无条件转移，这时A段为转向地址（目标程序）也是cx2
                if(rv[Ptr].getId()==ELS)
                {
                    Ptr++;
                    statement();
                    Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());
                  //地址回填，将JMP，0，0中的A回填为执行statement()过后目标代码的位置
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
            int cx1=Pcode.getCodePtr();     //保存条件表达式在Pcode中的地址
            Ptr++;
            lexp();
            if(rv[Ptr].getId()==DO)
            {
                int cx2=Pcode.getCodePtr();    
                //保存条件跳转指令的地址，在回填时使用，仍是条件不符合是跳转
                //JPC：条件转移，当运行栈S的栈顶的布尔值为假（0）时，则转向A段所指目标程序地址；
                //否则顺序执行。
                Pcode.gen(Pcode.getJPC(),0,0);//为假 跳出
                Ptr++;
                statement();
                
                Pcode.gen(Pcode.getJMP(),0,cx1); // 循环执行 
                //完成DO后的相关语句后，需要跳转至条件表达式处，检查是否符合条件，即是否继续循环
                Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());       
                //回填条件转移指令//跳出口
            }
            else
            {
                Is_error=true;
                Display_Error(9,"");
                return;
            }
        }
//call <id>（[<exp>{,<exp>}]）
        else if(rv[Ptr].getId()==CAL)
        {
            Ptr++;
            //id();
            int count=0;//用来检验传入的参数和设定的参数是否相等
            Table tempRow;
            if(rv[Ptr].getId()==SYM)
            {
            	//检查是否是变量，然后检查标识符类型是否是proc
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
                {           //cal 未定义变量的错误
                    Is_error=true;
                    Display_Error(10,"");
                    return;
                }
                Ptr++;
                if(rv[Ptr].getId()==LBR)
                {
                    Ptr++;
                    if(rv[Ptr].getId()==RBR)//无参数
                    {
                        Ptr++;
                        Pcode.gen(Pcode.getCAL(),level-tempRow.getLevel(),tempRow.getValue());       
                    }
                    else//有参数
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
                        Pcode.gen(Pcode.getCAL(),level-tempRow.getLevel(),tempRow.getValue());        //调用过程中的保存现场由解释程序完成，这里只产生目标代码,+3需详细说明
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
//read (<id>{，<id>})
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
                    	//首先判断在符号表中在本层或本层之前是否有此变量
                        Is_error=true;
                        Display_Error(10,"");
                        return;

                    }//if判断在符号表中是否有此变量
                    else
                    {           //sto未定义变量的错误
                        Table tempTable=Table.getRow(Table.getNameRow(rv[Ptr].getValue()));
                        if(tempTable.getType()==Table.getVar())
                        {       //该标识符是否为变量类型
                            Pcode.gen(Pcode.getRED(),0,0);         //RED从命令行读入一个输入置于栈顶  
                            Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress()); 
                            //STO L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
                        }//if标识符是否为变量类型
                        else
                        {       //sto类型不一致的错误
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
                            {      //首先判断在符号表中是否有此变量
                                Is_error=true;
                                Display_Error(10,"");
                                return;

                            }//if判断在符号表中是否有此变量
                            else
                            {          
                                Table tempTable=Table.getRow(Table.getNameRow(rv[Ptr].getValue()));
                                if(tempTable.getType()==Table.getVar())
                                {       //该标识符是否为变量类型
                                    Pcode.gen(Pcode.getRED(),0,0);         //RED 0,0	从命令行读入一个输入置于栈顶  
                                    Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress());  //STO L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
                                }//if标识符是否为变量类型
                                else
                                {       //sto类型不一致的错误
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
                Pcode.gen(Pcode.getWRT(),0,0);         //输出栈顶的值到屏幕
                while(rv[Ptr].getId()==COMMA)
                {
                    Ptr++;
                    exp();
                    Pcode.gen(Pcode.getWRT(),0,0);         //输出栈顶的值到屏幕
                }

                Pcode.gen(Pcode.getOPR(),0,14);         //输出换行
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
        	//body不生成目标代码
            body();
        }
//<id> := <exp>         
        else if(rv[Ptr].getId()==SYM)
        {      //赋值语句
            String name=rv[Ptr].getValue();
            Ptr++;
            if(rv[Ptr].getId()==CEQU)
            {
                Ptr++;
                exp();
                
                if(!Table.Is_PreTable(name,level))
                {        //检查标识符是否在符号表中存在
                    Is_error=true;
                    Display_Error(15,name);
                    return;
                }//if判断在符号表中是否有此变量
                else
                {           //sto未定义变量的错误
                    Table tempTable=Table.getRow(Table.getNameRow(name));
                    if(tempTable.getType()==Table.getVar())
                    {           //检查标识符是否为变量类型
                        Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress());  //STO L ，a 将数据栈栈顶的内容存入变量
                    }////检查标识符是否为变量类型
                    else
                    {       //类型不一致的错误
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
//<lexp> → <exp> <lop> <exp>|odd <exp>
    public void lexp()
    {
        if(rv[Ptr].getId()==ODD)
        {
            Ptr++;
            exp();
            Pcode.gen(Pcode.getOPR(),0,6);  //OPR 0 6	栈顶元素的奇偶判断，结果值在栈顶
        }
        else
        {
            exp();
            int compare=lop();        //返回值用来产生目标代码，如下
            exp();
            
            if(compare==EQU)
            {
                Pcode.gen(Pcode.getOPR(),0,8);      //OPR 0 8	次栈顶与栈顶是否相等，退两个栈元素，结果值进栈
            }
            else if(compare==NEQE)
            {
                Pcode.gen(Pcode.getOPR(),0,9);      //OPR 0 9	次栈顶与栈顶是否不等，退两个栈元素，结果值进栈
            }
            else if(compare==LES)
            {
                Pcode.gen(Pcode.getOPR(),0,10);     //OPR 0 10	次栈顶是否小于栈顶，退两个栈元素，结果值进栈
            }
            else if(compare==LESE)
            {
                Pcode.gen(Pcode.getOPR(),0,13);     // OPR 0 13	次栈顶是否小于等于栈顶，退两个栈元素，结果值进栈
            }
            else if(compare==LAR)
            {
                Pcode.gen(Pcode.getOPR(),0,12);     //OPR 0 12	次栈顶是否大于栈顶，退两个栈元素，结果值进栈
            }
            else if(compare==LARE)
            {
                Pcode.gen(Pcode.getOPR(),0,11);     //OPR 0 11	次栈顶是否大于等于栈顶，退两个栈元素，结果值进栈
            }
        }
    }
//<exp> → [+|-]<term>{<aop><term>}
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
            Pcode.gen(Pcode.getOPR(),0,1);      //  OPR 0 1	栈顶元素取反
        }
        while(rv[Ptr].getId()==ADD||rv[Ptr].getId()==SUB)
        {
            tempId=rv[Ptr].getId();
            Ptr++;
            term();
            if(tempId==ADD)
            {
                Pcode.gen(Pcode.getOPR(),0,2);       //OPR 0 2	次栈顶与栈顶相加，退两个栈元素，结果值进栈
            }
            else if(tempId==SUB)
            {
                Pcode.gen(Pcode.getOPR(),0,3);      //OPR 0 3	次栈顶减去栈顶，退两个栈元素，结果值进栈
            }
        }
    }
//<term> → <factor>{<mop><factor>}
    public void term()
    {
        factor();
        while(rv[Ptr].getId()==MUL||rv[Ptr].getId()==DIV){
            int tempId=rv[Ptr].getId();
            Ptr++;
            factor();
            if(tempId==MUL){
                Pcode.gen(Pcode.getOPR(),0,4);       //OPR 0 4	次栈顶乘以栈顶，退两个栈元素，结果值进栈
            }else if(tempId==DIV){
                Pcode.gen(Pcode.getOPR(),0,5);      // OPR 0 5	次栈顶除以栈顶，退两个栈元素，结果值进栈
            }
        }
    }
//<factor>→<id>|<integer>|(<exp>)
    public void factor()
    {
        if(rv[Ptr].getId()==CONST)
        {
            Pcode.gen(Pcode.getLIT(),0,Integer.parseInt(rv[Ptr].getValue()));    //是个数字,  LIT 0 a 取常量a放入数据栈栈顶
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
            {     //判断标识符在符号表中是否存在
                Is_error=true;
                Display_Error(10,"");
                return;
            }
            else//存在 变量取值 常数放值
            {           //未定义变量的错误
                Table tempRow= Table.getRow(Table.getNameRow(name));
                if(tempRow.getType()==Table.getVar())
                { //标识符是变量类型
                    Pcode.gen(Pcode.getLOD(),level-tempRow.getLevel(),tempRow.getAddress());   
                    //变量，LOD L  取变量（相对地址为a，层差为L）放到数据栈的栈顶
                }
                else if (tempRow.getType()==Table.getMyconst())
                {//标识符是常数类型
                    Pcode.gen(Pcode.getLIT(),0,tempRow.getValue());        
                    //常量，LIT 0 a 取常量a放入数据栈栈顶
                }
                else
                {       //类型不一致的错误
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
//<lop> → =|<>|<|<=|>|>=
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
        lex.bAnalysis();//词法分析
        readLex();//将lex.txt文件中的数据读入ThreeValue rv[]
        prog();//语法、语义分析，符号表的建立、产生目标代码
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
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("常量定义不是const开头,变量定义不是var 开头");        //常量定义不是const开头,变量定义不是var 开头
                break;
            case 0:
                System.out.print("Error： "+i+" "+"in line " + (rv[Ptr].getLine()-1)+":");
                System.out.println("缺少分号");        //缺少分号
                break;
            case 1:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("标识符不合法");       //标识符不合法
                break;
            case 2:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("程序开始必须是program");       //程序开始第一个字符必须是program
                break;
            case 3:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("赋值没用：=");       //赋值没用：=
                break;
            case 4:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("缺少左括号");       //缺少左括号
                break;
            case 5:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("缺少右括号");       //缺少右括号
                break;
            case 6:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("缺少begin");       //缺少begin
                break;
            case 7:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("缺少end");       //缺少end
                break;
            case 8:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("缺少then");       //缺少then
                break;
            case 9:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("缺少do");       //缺少do
                break;
            case 10:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("call，write，read语句中，不存在标识符"+"'"+rv[Ptr].getValue()+"'");  
                break;
            case 11:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("'"+rv[Ptr].getValue()+"'"+"该标识符不是proc类型"); 
                break;
            case 12:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("'"+rv[Ptr].getValue()+"'"+"read，write语句中，该标识符不是var类型");     
            case 13:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("'"+name+"'"+" 赋值语句中，该标识符不是var类型");      
                break;
            case 14:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("赋值语句中，该标识符不存在"+"'"+name+"'");      
                break;
            case 15:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("该标识符已经存在"+"'"+name+"'");      
                break;
            case 16:
                System.out.print("Error： "+i+" "+"in line " + rv[Ptr].getLine()+":");
                System.out.println("该标识符已经存在 "+"'"+name+"'");      
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
        inter.setPcode(Pcode);//存入CODE存储器
        inter.interpreter();
    }

}

