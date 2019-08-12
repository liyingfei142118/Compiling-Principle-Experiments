//SymTable���ű�ÿһ�еľ��庬���Table

public class SymbolTable {
    private int row_Max=10000;           //����
    //private int value_Max=100000;        //����������ֵ
    //private int level_Max=3;                 //����Ƕ�ײ��
    //private int address_Max=10000;       //����ַ��

    private int myconst=1;                  //����������1��ʾ
    private int var=2;                      //����������2��ʾ
    private int proc=3;             //����������3��ʾ

    //Table�Ƿ��ű��е�ÿһ��
    //tablePtrָ����ű����Ѿ�����ֵ���һ�����һ��
    //length��ʾ���ű��������˶��������ݣ�ʵ���Ͽ�����tablePtr����ʾ
    private Table[] table=new Table[row_Max];          //rowMax��


    private int tablePtr=0;
    private int length=0;


    public void setTablePtr(int tablePtr) {
        this.tablePtr = tablePtr;
    }
    //��ʼ����ȫ��Ϊ0
    public SymbolTable(){
        for(int i=0;i<row_Max;i++){
            table[i]=new Table();
            table[i].setAddress(0);
            table[i].setLevel(0);
            table[i].setSize(0);
            table[i].setType(0);
            table[i].setValue(0);
            table[i].setName(null);
        }
    }

    public int getVar() {
        return var;
    }

    public int getMyconst() {
        return myconst;
    }

    public int getProc() {
        return proc;
    }

    public int getLength(){
        return length;
    }

    //��ȡ���ű��е�i��
    public Table getRow(int i){
        return table[i];
    }
    /*
      *��¼���������ű�
      * ������
      * name��������
      * level�����ڲ��
      * value��ֵ
      * address����������ڲ�λ���ַ�ĵ�ַ
    */
    public void enterConst(String name,int level,int value,int address){
        table[tablePtr].setName(name);
        table[tablePtr].setLevel(level);
        table[tablePtr].setValue(value);
        table[tablePtr].setAddress(address);
        table[tablePtr].setType(myconst);
        table[tablePtr].setSize(4);
        tablePtr++;
        length++;
    }



    /*
     *    ��¼���������ű�
     *  ����ͬ��
     *  ˵�������ڵ�¼���ű���������ڱ�������������������������е��ã���PL/0��֧�ֱ�������ʱ��ֵ�����Բ��������value
    */
    public void enterVar(String name,int level,int address){
        table[tablePtr].setName(name);
        table[tablePtr].setLevel(level);
        table[tablePtr].setAddress(address);
        table[tablePtr].setType(var);
        table[tablePtr].setSize(0);
        tablePtr++;
        length++;
    }
    //��¼���̽����ű�����ͬ��
    public void enterProc(String name,int level,int address){
        table[tablePtr].setName(name);
        table[tablePtr].setLevel(level);
        table[tablePtr].setAddress(address);
        table[tablePtr].setType(proc);
        table[tablePtr].setSize(0);
        tablePtr++;
        length++;
    }

    //��lev��֮ǰ������lev�㣬����Ϊname�ı�����������������Ƿ񱻶��壬
   
    public boolean Is_PreTable(String name,int lev){
        for(int i=0;i<length;i++){
            if(table[i].getName().equals(name)&&table[i].getLevel()<=lev){
                return true;
            }
        }
        return false;
    }   
 /* //��lev��֮ǰ����Ϊname�ı�����������������Ƿ񱻶��壬
  *    public boolean Is_PreTable(String name,int lev){
        for(int i=0;i<length;i++){
            if(table[i].getName().equals(name)&&table[i].getLevel()<lev){
                return true;
            }
        }
        return false;
    }   
  */
    //��lev��������������̿��У����̿��name�� Pname���ڼ�⣩
    //lev��֮ǰ ����Ϊname�ı�����������������Ƿ񱻶���
    public boolean Is_Table(String name,int lev,String Pname){
    	int key=0;
        for(int i=0;i<length;i++){
        	if(table[i].getName()==Pname) {
        	lev--;//����ڱ����������û���ҵ� lev-- �Ӷ������ҵ�������������̿�
        	break;
        	}
            if(table[i].getName().equals(name)&&table[i].getLevel()==lev){
                //��������������ҵ�
            	key=1;
            }
        }
       
        if(key==0)//����ڱ����������û���ҵ� ȥ�Ͳ��� ����Is_PreTable����
        if(Is_PreTable(name,lev)) {
        	key=1;//�ҵ� key=1
        } 	 
        if(key==1) {
        	return true;
        }
        else
        {
        	return false;
        }
    }
    //��lev�㣬����Ϊname�ı�����������������Ƿ񱻶��壬
    //��������������������ʱ���øú���
    public boolean Is_NowTable(String name,int lev){
        for(int i=0;i<length;i++){
            if(table[i].getName().equals(name)&&table[i].getLevel()==lev){
                return true;
            }
        }
        return false;
    }


    //���ط��ű�������Ϊname���е��к�
    public int  getNameRow(String name){
        for(int i=length-1;i>=0;i--){
            if(table[i].getName().equals(name)){
                return i;
            }
        }
        return -1;          //����-1��ʾ�����ڸ�����
    }
    public int getTablePtr() {
        return tablePtr;
    }

    public Table[] getAllTable(){
        return table;
    }

    //���ұ���Ĺ����ڷ��ű��е�λ��
    public int getLevelPorc(int level){
        for(int i=length-1;i>=0;i--){
            if(table[i].getType()==proc){
                return i;
            }
        }
        return -1;
    }

}
