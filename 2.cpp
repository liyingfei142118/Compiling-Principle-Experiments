/*#include<iostream> 
#include<string> 
#include <fstream> 
#include<iomanip> 
#include<stdlib.h>*/
#include"1.2.cpp" 
//using namespace std; 

struct yufa { 
	string SYM; //单词的类别 
	string strToken; //用户所定义的标识符的值 
	int l; //记录换行符的个数，即记录源文件的行数 
}yufa0, yufa1, yufa2[1000];//yufa1 用于保存各个单词，yufa1 用于词法分析过程；yufa0 主要用于语法分析； 

char ch; 
int mm= 0; //结构体数组下标 
int line = 0;//出错位置 //char ch = ' '; 
string key[15] = { "begin", "end", "if", "then", "else", "while", "write",
 "read", "do", "call", "const", "var", "procedure", "program", "odd" };//预设保留字 
void prog(); 
void block(); 
void condecl(); 
void _const(); 
void vardecl(); 
void proc(); 
void body(); 
void statement(); 
void lexp(); 
void exp(); 
void term(); 
void factor(); 

void error0() 
{ 
	cout << "program 标示符出错" << endl; 
	exit(0); //出错即退出 
} 

void error1() 
{ 
	cout << "第" << line << "行缺少分号" << endl; 
} 

void error2() 
{ 
	cout << "第" << line << "行标识符（变量名）出错" << endl; 
} 
void error3() 
{ 	
	cout << "第" << line << "常量未声明（缺少 const）" << endl;
} 
void error4() 
{ 
	cout << "第" << line << "常量未赋值" << endl; 
} 
void error5() 
{ 
 cout << "第" << line << "赋值号出错或者缺少赋值号" << endl; 
} 
void error6() 
{ 
 cout << "第" <<line << "变量未声明（缺少 var）" << endl; 
} 
void error7()
{ 
  cout << "第" << line << "行缺少右括号" << endl;
} 
void error8()
{ 
  cout << "第" << line << "行缺少左括号" << endl; 
} 
void error9() 
{ 
	 cout << "第" << line << "行缺少参数" << endl;
} 
void error10()
{ 
  	cout << "第" << line << "行 procedure 错误" << endl;
} 
void error11()
{ 
 	 cout << "第" << line << "行缺少 end（begin 与 end 不匹配）" << endl; 
}
void error12()
{ 
  	 cout << "第" << line << "行缺少 then（if 与 then 不匹配）" << endl; 
} 
void error13()
{ 
  	 cout << "第" << line << "行缺少 do（while 与 do 不匹配）" << endl; 
} 
void error14()
{ 
   	cout << "第" << line << "行调用过程缺少标识符" << endl; 
} 
void error15()
{ 
   	cout << "第" << line << "行缺少分号" << endl; 
} 
void error16()
{ 
   	cout << "第" << line << "行条件语句出错（缺少界符）" << endl;
} 

void prog()//程序的递归子程序实现<prog> → program <id>；<block> 
{ 
  	line++; 
  	mm++;
  	yufa0.SYM=yufa2[mm].SYM;
  	if (yufa0.SYM == "program")// program 
		{ 
		  mm++; 
		  yufa0.SYM = yufa2[mm].SYM; 
		  if (yufa0.SYM == "biaoshifu") // <id>,即标识符 
		  { 
		  	mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
		  	if (yufa0.SYM == ";")//; 
		  		{ 
				  mm++; 
				  yufa0.SYM = yufa2[mm].SYM;
				  block(); 
		  		} 
			else error1();//缺少；
		  } 
		  else error2();//缺少标识符或者标识符错误 
		 } 
	else 
	error0(); 	  
		  cout << "\n\n--------------------------语法分析结束------------------------\n\n" << endl;
		  
 } 
void block()//程序体的递归子程序实现,<block> → [<condecl>][<vardecl>][<proc>]<body>
{ 
	if (yufa0.SYM == "const") 
		condecl();
	if (yufa0.SYM == "var") 
		vardecl(); 
	if (yufa0.SYM == "procedure") 
		proc(); 
	body();
} 
			
 void condecl()//常量说明的递归子程序实现,<condecl> → const <const>{,<const>};
 { 
 	line++; 
 	if (yufa0.SYM == "const") // const 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		_const();//<const> 
		while (yufa0.SYM == ",")//,逗号
		{ 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM;
			 _const();
		 }//<const>
		if (yufa0.SYM == ";")//; 
		{ 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM;
		 } 
		else error1();//缺少； 
		} 
	else error3();//缺少常量定义 const 
}
void _const()//常量,<const> → <id>:=<integer> 
{ 
	if (yufa0.SYM == "biaoshifu") // <id>
	 { 
	 	mm++; 
		yufa0.SYM = yufa2[mm].SYM;
		if (yufa0.SYM == "fuzhi") 
			{ 
				mm++; 
				yufa0.SYM = yufa2[mm].SYM; 
				if (yufa0.SYM == "digit")
				  { 
					mm++; 
					yufa0.SYM = yufa2[mm].SYM;
				  } 
				else error4();//常量未赋值 
			}
		else error5();//缺少赋值号，或者赋值号错误 
	 } 
	else error2(); //缺少标识符 
} 
void vardecl()//变量说明的递归子程序实现,<vardecl> → var <id>{,<id>}; 
{ 
	line++; 
	if (yufa0.SYM == "var") 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		if (yufa0.SYM == "biaoshifu") //<id> 
		 { 
		   mm++; 
		   yufa0.SYM = yufa2[mm].SYM; 
		   while (yufa0.SYM == ",")//,
			{ 
				mm++; 
				yufa0.SYM = yufa2[mm].SYM;//下一单词 
				if (yufa0.SYM == "biaoshifu") //<id> 
					{ 
					  mm++; 
					  yufa0.SYM = yufa2[mm].SYM; 
					} 
				else 
				error2();//缺少标识符即缺少变量
			} 
		if (yufa0.SYM == ";") 
		 { 
		 	mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
		 }//下一单词 
		 else error2(); 
		 } 
	} 
  else error6();//变量未声明 
} 
void proc()//<proc> → procedure <id>（<id>{,<id>}）;<block>{;<proc>}
{ 
	line++; 
	if (yufa0.SYM == "procedure") //procedure
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		if (yufa0.SYM == "biaoshifu") // <id> 
			{ 
			  mm++; 
			  yufa0.SYM = yufa2[mm].SYM;
			  if (yufa0.SYM == "(") //( 
			  { 
				  mm++; 
				  yufa0.SYM = yufa2[mm].SYM; 
				  if (yufa0.SYM == "biaoshifu")// <id> 
				 { 
					  mm++; 
					  yufa0.SYM = yufa2[mm].SYM; 
					  while (yufa0.SYM == ",")// ,
					 { 
						  mm++; 
						  yufa0.SYM = yufa2[mm].SYM; 
						  if (yufa0.SYM == "biaoshifu") 
						  { 
						     mm++; 
							 yufa0.SYM = yufa2[mm].SYM;
						  } 
						else 
							error2(); 
					  } 
				  } 
				if (yufa0.SYM == ")")// ) 
				{ 
					  mm++; 
					  yufa0.SYM = yufa2[mm].SYM; 
					  if (yufa0.SYM == ";") //; 
					  { 
						 mm++; 
						 yufa0.SYM = yufa2[mm].SYM;//下一单词---------->> 
						 block(); 
						 while (yufa0.SYM == ";") 
						 { 
						   mm++; 
						   yufa0.SYM = yufa2[mm].SYM; 
						   proc(); 
						  } 
					  }
					else error1();// 缺少; 
				} 
				 else error7();//括号不匹配 （右括号少）
				}
				else error8();//左括号缺少
			}
			else error9();//缺少参数
		} 
		else error10();//procedure 错误 
}
							    
void body()//<body> → begin <statement>{;<statement>}end 
{ 
	if (yufa0.SYM == "begin")
	{ 
			line++; 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
			statement(); 
			// while ((yufa0.SYM == "biaoshi") || (yufa0.SYM == "if") || (yufa0.SYM == "while") || 
			// (yufa0.SYM == "call") || (yufa0.SYM == "read") || (yufa0.SYM == "write") || (yufa0.SYM == "begin")) 
			// error1();//语句未到最后一条，缺少";" 
			while(yufa0.SYM == ";")
			{ 
				mm++; 
				yufa0.SYM = yufa2[mm].SYM; 
				statement(); 
			} 
			if (yufa0.SYM == "end") 
			{ 
				 line ++; 
				 mm++; 
				 yufa0.SYM = yufa2[mm].SYM; 
			} 
			else error11();//缺少 end 与 begin 匹配
	} 
	else error11();//缺少 begin 
} 
//<statement> → <id> := <exp> 
//语句 
// |if <lexp> then <statement>[else <statement>] 
// |while <lexp> do <statement> 
// |call <id>[（<exp>{,<exp>}）]
// |<body> 
// |read (<id>{，<id>}) 
// |write (<exp>{,<exp>}) 
void statement() 
{ 
	if (yufa0.SYM == "biaoshifu") //<statement> → <id> := <exp> 
	{ 
		line++; 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		if (yufa0.SYM == "fuzhi") 
		{ 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
			exp(); 
		} 
		else error5();//赋值号错误 
	} 
	else if (yufa0.SYM == "if") //if <lexp> then <statement>[else <statement>] 
	{ 
		line++; 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		lexp();
		if (yufa0.SYM == "then") 
		{ 
			mm++; 
		 	yufa0.SYM = yufa2[mm].SYM; 
		 	statement(); 
			if (yufa0.SYM == "else")
			{ 
				mm++; 
				yufa0.SYM = yufa2[mm].SYM; 
				statement(); 
			} 
		} 
		else 
		error12();//缺少 then 
	} 
	else if (yufa0.SYM == "while")//while <lexp> do <statement>
	{ 
			line++; 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
			lexp();
			if (yufa0.SYM == "do") 
			{ 
				mm++; 
				yufa0.SYM = yufa2[mm].SYM; 
				statement();
			} 
			else error13();//缺少 do 不匹配（while 与 do 不匹配） 
	} 
	else if (yufa0.SYM == "call")//call <id>[（<exp>{,<exp>}）] 
	{ 
			line++; 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
			if (yufa0.SYM == "biaoshifu") //
			 { 
				 mm++; 
			 	yufa0.SYM = yufa2[mm].SYM;
				// exp(); 
				if (yufa0.SYM == "(") // 
				{ 
					mm++; 
					yufa0.SYM = yufa2[mm].SYM;
					exp();
					while(yufa0.SYM == ",")
					{ 
						mm++;
						yufa0.SYM = yufa2[mm].SYM; 
						exp(); 
					}
					if (yufa0.SYM == ")") 
					{ 
						mm++; 
						yufa0.SYM = yufa2[mm].SYM; 
					} 
					else error7();//缺少右括号 
				} // 此处不会报错，[（<exp>{,<exp>}）]因为表示可有可无的
 			}
			else error14();//调用过程缺少标识符 
	} 
	else if (yufa0.SYM == "read")//read (<id>{，<id>})
	{ 
		 	line++; 
		 	mm++; 
		 	yufa0.SYM = yufa2[mm].SYM; 
			if (yufa0.SYM == "(") 
			{ 
				mm++; 
				yufa0.SYM = yufa2[mm].SYM; 
				if (yufa0.SYM == "biaoshifu") 
				{ 
					mm++; 
					yufa0.SYM = yufa2[mm].SYM; 
					while (yufa0.SYM == ",") 
					{ 
						mm++; 
						yufa0.SYM = yufa2[mm].SYM;
						
						if (yufa0.SYM == "biaoshifu") 
						{ 
							mm++; 
							yufa0.SYM = yufa2[mm].SYM; 
						}
						else error2();//缺少标示符 
					 }
					if (yufa0.SYM == ")") 
					{ 
						mm++; 
						yufa0.SYM = yufa2[mm].SYM; 
					} 
					else error7();//缺少右括号 
				 } 
					else error9();//read 缺少参数 
				}
			else error8();//缺少左括号 
	} 
	else if (yufa0.SYM == "write")//write (<exp>{,<exp>}) 
	{ 
			line++; 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
			if (yufa0.SYM == "(") 
			{ 
				mm++; 
				yufa0.SYM = yufa2[mm].SYM; 
				exp(); 
				while (yufa0.SYM == ",") 
				{ 
					mm++; 
					yufa0.SYM = yufa2[mm].SYM; 
					exp(); 
				}
				if (yufa0.SYM == ")") 
				{ 
					mm++; 
					yufa0.SYM = yufa2[mm].SYM; 
				}
				else error7();//缺少右括号 
			} 
			else error8();//缺少左括号 
	} 
	else body();//<body> 
} 
//<lexp> → <exp> <lop> <exp>|odd <exp> 
//<exp> → [+|-]<term>{<aop><term>} 
//<term> → <factor>{<mop><factor>} 
//<factor>→<id>|<integer>|(<exp>) 
//<lop> → =|<>|<|<=|>|>= //<aop> → +|- 
//<mop> → *|/ 
//<id> → l{l|d} （注：l 表示字母） 
//<integer> → d{d} 
void lexp()//<lexp> → <exp> <lop> <exp>|odd <exp> 
{ 
	if (yufa0.SYM == "odd") 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		exp(); 
	} 
	else 
	{ 
		exp(); 
		if ((yufa0.SYM == "ge") || (yufa0.SYM == "g") || (yufa0.SYM == "l") || (yufa0.SYM == "ne") || (yufa0.SYM == "l") || (yufa0.SYM == "=")) 
		{ 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
			exp(); 
			} 
		else 
			error15();//条件语句出错，缺少界符 
	} 
	//odd 是否存在？
	} 
void exp()//<exp> → [+|-]<term>{<aop><term>} 
{ 
	while((yufa0.SYM == "+") || (yufa0.SYM == "-")) 
 	{ 
	 	 mm++; 
		 yufa0.SYM = yufa2[mm].SYM; 
		 term();//<term> 
	} 
 	term(); 
	while ((yufa0.SYM == "+") || (yufa0.SYM == "-"))//<aop> → +|- 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		term();
  	} 
} 
void term()//<term> → <factor>{<mop><factor>} 
{ 
  	factor(); 
  	while ((yufa0.SYM == "*") || (yufa0.SYM == "/"))//<mop> → *|/ 
  	{ 
		  mm++; 
		  yufa0.SYM = yufa2[mm].SYM; 
		  factor();//factor() 
	} 
} 
void factor()//<factor>→<id>|<integer>|(<exp>) 
{ 
	if (yufa0.SYM == "biaoshifu") 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
	} 
	else if (yufa0.SYM == "digit") 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
	}
	else if (yufa0.SYM == "(") 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		exp(); 
		if (yufa0.SYM == ")") 
		{ 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
		}
		else error7(); //缺少右括号 
	} 
} 
string Concat(char ch,string strToken) 
{ 
	strToken += ch; 
	return strToken; 
} 
int IsLetter(char c ) //判断字母 
{ 
	if (((c <= 'z')&&(c >= 'a')) || ((c <= 'Z')&&(c >= 'A'))) 
		return 1; 
	else 
		return 0;
} 
int IsDigit(char c) //判断数字
{ 
	if ((c >= '0')&&(c <= '9')) 
		return 1; 
	else 
		return 0; 
} 
int IsKey(string StrToken) //判断保留字 
{ 
	int i; 
	for (i = 0; i<15; i++) 
  	{ 
	   if (key[i].compare(StrToken) == 0) 
	   	return 1; 
	} 
	return 0; 
} 

void getsym(fstream &file) 
{ 
	 string StrToken = "";  
	// if (ch == ' ' || ch == '\t' || ch == '\n') 
	//滤掉空白字符 
	// ch = file.get(); 
   if (IsLetter(ch))//开头是字母得到单词 
   { 
   		while (IsLetter(ch) || IsDigit(ch)) 
   		{ 
		   StrToken = Concat(ch,StrToken); 
		   ch = file.get(); 
		} 
   		if (IsKey(StrToken)) //得到单词判断是否是保留字和标识符 
   		{ 
   			int i;
    		for (i = 0; i<15; i++) 
   			{ 
		   		if (key[i].compare(StrToken) == 0) 
		   		yufa1.SYM = key[i]; 
			} 
			yufa1.strToken = StrToken; 
		} 
   		else 
   		{ 
   			yufa1.SYM = "biaoshifu"; 
			yufa1.strToken = StrToken; 
   		}
   } 
  else if (IsDigit(ch)) //开头是数字 
   {  
   		while(IsDigit(ch)) //得到数字串 
		{ 
			StrToken = Concat(ch,StrToken); 
			ch = file.get(); 
		} 
   		if(IsLetter(ch)) //数字后面跟字母，错误词法 
   		{ 
   			StrToken="";
   			while(IsLetter(ch)||IsDigit(ch))
    		{ 
				StrToken = Concat(ch,StrToken); 
				ch = file.get(); 
			} 
			cout <<"词法错误！(数字后面跟字母) "<< StrToken<< endl; 
		} 
		else 
		{
		
			yufa1.SYM = "digit";
			yufa1.strToken = StrToken;
		}
		 
	} 
	else 
	{

	switch (ch) 
	{ 
	 case'+':
	 	{
	 		yufa1.SYM = "+";
			yufa1.strToken = "+";
			ch = file.get();
			break;
		} 
	 case'-':
		 {
		 yufa1.SYM = "-";
		 yufa1.strToken = "-";
	//	StrToken = Concat(ch,StrToken); 
		ch = file.get(); 
	//	 ch = file.get();
	/*if(!IsDigit(ch))
	{

		while(IsDigit(ch)) //得到数字串 
		{ 
			StrToken = Concat(ch,StrToken); 
			ch = file.get(); 
		} 
   		if(IsLetter(ch)) //数字后面跟字母，错误词法 
   		{ 
   			StrToken="";
   			while(IsLetter(ch)||IsDigit(ch))
    		{ 
				StrToken = Concat(ch,StrToken); 
				ch = file.get(); 
			} 
			cout <<"词法错误！(数字后面跟字母) "<< StrToken<< endl; 
		} 
		else 
		{
		
			yufa1.SYM = "digit";
			yufa1.strToken = StrToken;
		}
	}
	else
	{
		yufa1.SYM = "-";
		yufa1.strToken = "-";
	}*/
		 break;
		 } 
	 case'*':
		 {
		 yufa1.SYM = "*";
		 yufa1.strToken = "*";
	 	ch = file.get();
		 break;
	 	} 
	 case'/':
		 {
		 yufa1.SYM = "/";
		 yufa1.strToken = "/";
	 	ch = file.get();
	 	break;
		 } 
	 case'(':
	 	{
		 	yufa1.SYM = "(";
		 	yufa1.strToken = "(";
		 	ch = file.get();
		 	break;
		 } 
	 case')':
	 	{
	 		yufa1.SYM = ")";
	 		yufa1.strToken = ")";
	 		ch = file.get();
	 		break;
		 } 
	 case'=':
		 {
	 		yufa1.SYM = "=";
	 		yufa1.strToken = "=";
	 		ch = file.get();
	 		break;
	 	} 
	 case';':
		 {
		 	yufa1.SYM = ";";
		 	yufa1.strToken = ";";
	 		ch = file.get();
	 		break;
	 	} 
	 case',':
	 	{
	 		yufa1.SYM = ",";
	 		yufa1.strToken = ",";
	 		ch = file.get();
	 		break;
	 	} 
	 case':': 
	 	{ 
	 		ch = file.get(); 
	 		if (ch == '=') 
			{ 
				yufa1.SYM = "fuzhi"; 
				yufa1.strToken = ":=";
				ch=file.get(); 
			} 
	 		else 
				 cout << ":" << "词法错误（：后面没有=）" << endl; 
			 
			break; 
		} 
	case'>': 
		{ 
	 	ch = file.get(); 
		if (ch == '=') 
		{ 
			yufa1.SYM = "ge"; 
			yufa1.strToken = ">="; 
			ch=file.get();
		} 
	 	else 
		{ 
			yufa1.SYM = "g"; 
			yufa1.strToken = ">"; 
		} 
		
		break; 
		} 
	 case'<': 
	 	{ 
		 	ch = file.get(); 
		 	if (ch == '=') 
	 		{ 
	 			yufa1.SYM = "le"; 
	 			yufa1.strToken = "<=";
			 	ch=file.get();
	   		} 
	 		else if (ch == '>') 
		 	{ 
		 		yufa1.SYM = "ne"; 
				yufa1.strToken = "<>";
				ch=file.get();
		 	} 
	 		else 
		 	{ 
		 		yufa1.SYM = "l"; 
				yufa1.strToken = "<";
		 	}  
			break; 
		}
} 
}
} 

int main() 
{ 
	int nn = 0; 
	//string StrToken = ""; 
	fstream file; 
	//char ch; 
	int ll=0; 
	
	char filename[30]; 
	cout << "请输入源文件名:"; 
	cin.getline(filename, 30); 
	file.open(filename, ios::in); 
	if (!file) 
	{ 
		cout << "文件打开失败!\n\n"; 
		exit(0);
	}
	
	yufa2[nn].l = 0; //换行符的个数0
	cout << "\n--------------------------词法分析------------------------" << endl;
	int sss=BTO(filename); 
	if(sss==0)
	{

	ch=file.get();
	while (!file.eof()) 
	{ 
		
	  while(ch == ' ' || ch == '\t'||ch == '\n') //滤掉空白字符
	  	{
			  
	  		if (ch == '\n') //换行++
	  		{ 
	  			ll ++; 
			}
			ch=file.get(); 
	  	} 
	  	
	 	 getsym(file); 
	 	 
		 nn++;//数组下标++ 
	 	 yufa2[nn].SYM = yufa1.SYM; 
	 	 yufa2[nn].strToken = yufa1.strToken; 
	  	 yufa2[nn].l = ll; 

	  //	cout << "(" << setw(2) << nn << ") "; 
	  //	cout << setw(13) << yufa2[nn].SYM << setw(9) << yufa2[nn].strToken << " "; 
	  //	cout << setw(13) << yufa1.SYM << setw(9) << yufa1.strToken << "\n"; 
	} 
	  
	  // nn++; 
	  // yufa2[nn].SYM = "the_end";//完成 
	  // yufa2[nn].strToken = "the_end"; 
	  file.close(); 
	 
	 cout << "\n\n\n--------------------------语法分析------------------------" << endl; 
	 // yufa0.SYM = yufa2[mm].SYM; 
	  // yufa0.SYM = yufa1.SYM; 
	prog(); 
}

	  return 0; 
}
