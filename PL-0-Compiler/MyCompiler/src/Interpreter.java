import java.util.Scanner;

public class Interpreter {
  /*
    *    ����ľ�����ʽ��
    *    FLA
    *    ���У�F�δ���α������
    *    L�δ�����ò���˵����Ĳ��ֵ
    *    A�δ���λ��������Ե�ַ��
    *    ��һ��˵����
    *    INT��Ϊ�����õĹ��̣����������̣�������ջS�п�������������ʱA��Ϊ�������ݵ�Ԫ���������������������ݣ���L�κ�Ϊ0��
    *    CAL�����ù��̣���ʱA��Ϊ�����ù��̵Ĺ����壨������֮ǰһ��ָ���Ŀ�����������ڵ�ַ��
    *    LIT���������͵�����ջS��ջ������ʱA��Ϊ����ֵ��
    *    LOD���������͵�����ջS��ջ������ʱA��Ϊ��������˵�����е����λ�á�
    *    STO��������ջS��ջ����������ĳ��������Ԫ�У�A��Ϊ��������˵�����е����λ�á�
    *    JMP��������ת�ƣ���ʱA��Ϊת���ַ��Ŀ����򣩡�
    *    JPC������ת�ƣ�������ջS��ջ���Ĳ���ֵΪ�٣�0��ʱ����ת��A����ָĿ������ַ������˳��ִ�С�
    *    OPR����ϵ���������㣬A��ָ���������㣬����A=2�����������㡰������A��12�����ϵ���㡰>���ȵȡ��������ȡ������ջS��ջ������ջ����
    *
    *    OPR 0 0	���̵��ý�����,���ص��õ㲢��ջ
    *    OPR 0 1	ջ��Ԫ��ȡ��
    *    OPR 0 2	��ջ����ջ����ӣ�������ջԪ�أ����ֵ��ջ
    *    OPR 0 3	��ջ����ȥջ����������ջԪ�أ����ֵ��ջ
    *    OPR 0 4	��ջ������ջ����������ջԪ�أ����ֵ��ջ
    *    OPR 0 5	��ջ������ջ����������ջԪ�أ����ֵ��ջ
    *    OPR 0 6	ջ��Ԫ�ص���ż�жϣ����ֵ��ջ��
    *    OPR 0 7
    *    OPR 0 8	��ջ����ջ���Ƿ���ȣ�������ջԪ�أ����ֵ��ջ
    *    OPR 0 9	��ջ����ջ���Ƿ񲻵ȣ�������ջԪ�أ����ֵ��ջ
    *    OPR 0 10	��ջ���Ƿ�С��ջ����������ջԪ�أ����ֵ��ջ
    *    OPR 0 11	��ջ���Ƿ���ڵ���ջ����������ջԪ�أ����ֵ��ջ
    *    OPR 0 12	��ջ���Ƿ����ջ����������ջԪ�أ����ֵ��ջ
    *    OPR 0 13	��ջ���Ƿ�С�ڵ���ջ����������ջԪ�أ����ֵ��ջ
    *    OPR 0 14	ջ��ֵ�������Ļ,�������һ���ո�
    *    OPR 0 15	��Ļ�������
    *    OPR 0 16	�������ж���һ����������ջ��
    *
    private int LIT = 0;           //LIT 0 ��a ȡ����a��������ջջ��
    private int OPR = 1;        //OPR 0 ��a ִ�����㣬a��ʾִ��ĳ�����㣬�����Ǻ�������������ע��
    private int LOD = 2;        //LOD L ��a ȡ��������Ե�ַΪa�����ΪL���ŵ�����ջ��ջ��
    private int STO = 3;        //STO L ��a ������ջջ�������ݴ����������Ե�ַΪa����β�ΪL��
    private int CAL = 4;        //CAL L ��a ���ù��̣�ת��ָ�����ڵ�ַΪa����β�ΪL��
    private int INT = 5;         //INT 0 ��a ����ջջ��ָ������a
    private int JMP = 6;       //JMP 0 ��a������ת�Ƶ���ַΪa��ָ��
    private int JPC = 7;        //JPC 0 ��a ����ת��ָ�ת�Ƶ���ַΪa��ָ��
    private int RED = 8;       //RED L ��a �����ݲ������������Ե�ַΪa����β�ΪL��
    private int WRT = 9;      //WRT 0 ��0 ��ջ���������

������Ľṹ
�����洢�����洢��CODE���������P�Ĵ���
            ���ݴ洢��STACK��ջ��������̬�������ݿռ�
�ĸ��Ĵ�����
һ��ָ��Ĵ���I:��ŵ�ǰҪִ�еĴ���
һ��ջ��ָʾ���Ĵ���T��ָ������ջSTACK��ջ��
һ������ַ�Ĵ���B����ŵ�ǰ���й��̵���������STACK�е���ʼ��ַ
һ�������ַ�Ĵ���P�������һ��Ҫִ�е�ָ���ַ
�ü����û�й������õļĴ������������㶼Ҫ������ջSTACK��ջ��������Ԫ֮����У�����������ȡ��ԭ����������������������ջ��
���¼��
 RA
 SL
 DL

RA�����ص�ַ
SL������ù���ֱ�����Ļ��¼�׵�ַ
DL�������ߵĻ��¼�׵�ַ��
���̷��ؿ��Կ�����ִ��һ�������OPR����
ע�⣺��β�Ϊ���ò���붨���εĲ�ֵ

    */
    private Pcode IP;    //ָ��Ĵ���I����ŵ�ǰҪִ�еĴ���
    private int T;      //ջ��ָʾ��T
    private int B;      //��ַ�Ĵ���B����ŵ�ǰ���й��̵���������STACK�е���ʼ��ַ
    private int P;      //�����ַ�Ĵ����������һ��Ҫִ�е�ָ��ĵ�ַ
    private int stackSize=1000;
    private int[] dataStack=new int[stackSize];     //���ݴ洢��STACK����ʼֵΪ1000
    private TotalPcode code;            //�洢��CODE���������P�Ĵ���

    public void setPcode(TotalPcode code){    //��Pcode�еĴ���������ʼ��code
        this.code=code;
    }
    public TotalPcode getCode(){
        return code;
    }
    public void interpreter(){
        P=0;
        B=0;
        T=0;
        IP=code.getPcodeArray()[P];//�õ�һ��ָ��
        //F�δ���α������
  	  //L�δ�����ò���˵����Ĳ��ֵ
      //  A�δ���λ��������Ե�ַ��

        do{
            IP=code.getPcodeArray()[P];
            P++;
            switch(IP.getF()){
                case 0:  //LIT 0 ��a ȡ����a��������ջջ��
                    dataStack[T]=IP.getA();
                    T++;
                    break;
                case 1: //OPR 0 ��a ִ�����㣬a��ʾִ��ĳ�����㣬�����Ǻ�������������ע��
                    switch(IP.getA()){
                        case 0:                 //opr,0,0���̵��ý�����,���ص��õ㲢��ջ
                            T=B;
                            P=dataStack[B+2];
                            B=dataStack[B];
                            break;
                        case 1:                 //opr 0,1ȡ��ָ��
                            dataStack[T-1]=-dataStack[T-1];
                            break;
                        case 2:                 //opr 0 ,2��ӣ���ԭ��������Ԫ����ȥ�����������ջ��
                            dataStack[T-2]=dataStack[T-1]+dataStack[T-2];
                            T--;
                            break;
                        case 3:               //opr 0 ,3�������ԭ��������Ԫ����ȥ�����������ջ��
                            dataStack[T-2]=dataStack[T-2]-dataStack[T-1];
                            T--;
                            break;
                        case 4:              //opr 0 ,4��ˣ���ԭ��������Ԫ����ȥ�����������ջ��
                            dataStack[T-2]=dataStack[T-1]*dataStack[T-2];
                            T--;
                            break;
                        case 5:              //opr 0 ,5�������ԭ��������Ԫ����ȥ�����������ջ��
                            dataStack[T-2]=dataStack[T-2]/dataStack[T-1];
                            T--;
                            break;
                        case 6://ջ��Ԫ�ص���ż�жϣ����ֵ��ջ��
                            dataStack[T-1]=dataStack[T-1]%2;//(����Ϊ1)
                            break;
                        case 7:
                            break;
                        case 8://��ջ����ջ���Ƿ���ȣ�������ջԪ�أ����ֵ��ջ
                            if (dataStack[T - 1] == dataStack[T-2]){//�˴���ʹǰ�淢��ĳЩ������ΪJava�����ͺ�boolean���ǲ����Եȼ۵�
                                dataStack[T-2]=1;
                                T--;
                                break;
                            }
                            dataStack[T-2]=0;
                            T--;
                            break;
                        case 9://��ջ����ջ���Ƿ񲻵ȣ�������ջԪ�أ����ֵ��ջ
                            if (dataStack[T - 1] != dataStack[T-2]){//�˴���ʹǰ�淢��ĳЩ������ΪJava�����ͺ�boolean���ǲ����Եȼ۵�
                                dataStack[T-2]=1;
                                T--;
                                break;
                            }
                            dataStack[T-2]=0;
                            T--;
                            break;
                        case 10://��ջ���Ƿ�С��ջ����������ջԪ�أ����ֵ��ջ
                            if(dataStack[T-2]<dataStack[T-1]){
                                dataStack[T-2]=1;
                                T--;
                                break;
                            }
                            dataStack[T-2]=0;
                            T--;
                            break;
                        case 11://��ջ���Ƿ���ڵ���ջ����������ջԪ�أ����ֵ��ջ
                            if(dataStack[T-2]>=dataStack[T-1]){
                                dataStack[T-2]=1;
                                T--;
                                break;
                            }
                            dataStack[T-2]=0;
                            T--;
                            break;
                        case 12://��ջ���Ƿ����ջ����������ջԪ�أ����ֵ��ջ
                            if(dataStack[T-2]>dataStack[T-1]){
                                dataStack[T-2]=1;
                                T--;
                                break;
                            }
                            dataStack[T-2]=0;
                            T--;
                            break;
                        case 13://��ջ���Ƿ�С�ڵ���ջ����������ջԪ�أ����ֵ��ջ
                            if(dataStack[T-2]<=dataStack[T-1]){
                                dataStack[T-2]=1;
                                T--;
                                break;
                            }
                            dataStack[T-2]=0;
                            T--;
                            break;
                       /* case 14://ջ��ֵ�������Ļ,�������һ���ո�
                            System.out.print(dataStack[T-1]);
                            System.out.print("  ");     //���ڹ۲죬�����һ���ո�
                            break;*/
                        case 14://��Ļ�������
                            System.out.println();
                            break;
                        /*case 16://�������ж���һ����������ջ��
                            Scanner s=new Scanner(System.in);
                            dataStack[T]=s.nextInt();//�˴�����
                            T++;
                            break;*/
                    }
                    break;
                case 2:         //LOD L ��a ȡ��������Ե�ַΪa�����ΪL���ŵ�����ջ��ջ��
                    dataStack[T]=dataStack[IP.getA()+getBase(B,IP.getL())];
                    T++;
                    break;
                case 3://STO L ��a ������ջջ�������ݴ����������Ե�ַΪa����β�ΪL��
                    dataStack[IP.getA()+getBase(B,IP.getL())]=dataStack[T-1];
                    T--;
                    break;
                case 4: //CAL L ��a ���ù��̣�ת��ָ�����ڵ�ַΪa����β�ΪL��
                   /* private int T;      //ջ��ָʾ��T��ָ������ջSTACK��ջ
                    private int B;      //��ַ�Ĵ���B����ŵ�ǰ���й��̵���������STACK�е���ʼ��ַ
                    private int P;      //�����ַ�Ĵ����������һ��Ҫִ�е�ָ��ĵ�ַ*/
                    dataStack[T]=B;
                    dataStack[T+1]=getBase(B,IP.getL());
                    dataStack[T+2]=P;
                    B=T;
                    P=IP.getA();
                    break;
                case 5:  //INT 0 ��a ����ջջ��ָ������a
                    T=T+IP.getA();
                    break;
                case 6://JMP 0 ��a������ת�Ƶ���ַΪa��ָ��
                    P=IP.getA();
                    break;
                case 7: //JPC 0 ��a ����ת��ָ�ת�Ƶ���ַΪa��ָ��
                    if(dataStack[T-1]==0)
                    {
                        P=IP.getA();
                    }
                    break;
                case 8://�������ж���һ����������ջ��
                    Scanner s=new Scanner(System.in);
                    dataStack[T]=s.nextInt();//�˴�����
                    T++;
                    break;
                case 9://ջ��ֵ�������Ļ,�������һ���ո�
                    System.out.print(dataStack[T-1]);
                    System.out.print("  ");     //���ڹ۲죬�����һ���ո�
                    break;
            }
        }while(P!=0);

    }
    private int getBase(int nowBp,int lev){
        int oldBp=nowBp;      //SL
        while(lev>0){
            oldBp=dataStack[oldBp+1];
            lev--;
        }
        return oldBp;
    }


}
