/*
SymbolTable+Table�����ɷ��ű�ĵ�¼����������Table��SymbolTable�е�һ��
*/
import java.util.Scanner;


public class MyCompiler {
    public static void main(String [] args) {
        String filename;
        System.out.println("�����ļ���:");
        
        Scanner s=new Scanner(System.in);
        filename=s.next();
        
        Analysis p=new Analysis(filename);//�﷨ ������� ����Ŀ����� ���ű�
        if(!p.GramAnalysis())
        {
            System.out.println("����ɹ�");
        }
        
        String cc;
        System.out.println("�������ѡ��:");
        System.out.println("1.��ʼ���� " + "2.Ŀ�����PCode  " + "3.���ű�SymbolTable");
        cc=s.next();
       
        while(true)
        {
           if(cc.equals("1")) 
           {
               System.out.println("��ʼ����");
               p.interpreter();
           }
           else if(cc.equals("2"))
           {
               System.out.println("Ŀ�����PCode:");
               p.showPcodeInStack();
           }
           else if(cc.equals("3"))
           {
               System.out.println("���ű�SymbolTable:");
               p.showtable();
           }
           else
           {
        	   break;
           }
           System.out.println();
           System.out.println("�������ѡ��:");
           System.out.println("1.��ʼ���� " + "2.Ŀ�����PCode  " + "3.���ű�SymbolTable");
           cc=s.next();
        }
    }


}
