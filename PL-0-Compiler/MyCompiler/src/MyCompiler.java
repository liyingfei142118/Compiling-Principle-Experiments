/*
SymbolTable+Table组合完成符号表的登录操作，其中Table是SymbolTable中的一行
*/
import java.util.Scanner;


public class MyCompiler {
    public static void main(String [] args) {
        String filename;
        System.out.println("输入文件名:");
        
        Scanner s=new Scanner(System.in);
        filename=s.next();
        
        Analysis p=new Analysis(filename);//语法 语义分析 产生目标代码 符号表
        if(!p.GramAnalysis())
        {
            System.out.println("编译成功");
        }
        
        String cc;
        System.out.println("输入你的选择:");
        System.out.println("1.开始运行 " + "2.目标代码PCode  " + "3.符号表SymbolTable");
        cc=s.next();
       
        while(true)
        {
           if(cc.equals("1")) 
           {
               System.out.println("开始运行");
               p.interpreter();
           }
           else if(cc.equals("2"))
           {
               System.out.println("目标代码PCode:");
               p.showPcodeInStack();
           }
           else if(cc.equals("3"))
           {
               System.out.println("符号表SymbolTable:");
               p.showtable();
           }
           else
           {
        	   break;
           }
           System.out.println();
           System.out.println("输入你的选择:");
           System.out.println("1.开始运行 " + "2.目标代码PCode  " + "3.符号表SymbolTable");
           cc=s.next();
        }
    }


}
