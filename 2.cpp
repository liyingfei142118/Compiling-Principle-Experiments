/*#include<iostream> 
#include<string> 
#include <fstream> 
#include<iomanip> 
#include<stdlib.h>*/
#include"1.2.cpp" 
//using namespace std; 

struct yufa { 
	string SYM; //���ʵ���� 
	string strToken; //�û�������ı�ʶ����ֵ 
	int l; //��¼���з��ĸ���������¼Դ�ļ������� 
}yufa0, yufa1, yufa2[1000];//yufa1 ���ڱ���������ʣ�yufa1 ���ڴʷ��������̣�yufa0 ��Ҫ�����﷨������ 

char ch; 
int mm= 0; //�ṹ�������±� 
int line = 0;//����λ�� //char ch = ' '; 
string key[15] = { "begin", "end", "if", "then", "else", "while", "write",
 "read", "do", "call", "const", "var", "procedure", "program", "odd" };//Ԥ�豣���� 
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
	cout << "program ��ʾ������" << endl; 
	exit(0); //�����˳� 
} 

void error1() 
{ 
	cout << "��" << line << "��ȱ�ٷֺ�" << endl; 
} 

void error2() 
{ 
	cout << "��" << line << "�б�ʶ����������������" << endl; 
} 
void error3() 
{ 	
	cout << "��" << line << "����δ������ȱ�� const��" << endl;
} 
void error4() 
{ 
	cout << "��" << line << "����δ��ֵ" << endl; 
} 
void error5() 
{ 
 cout << "��" << line << "��ֵ�ų������ȱ�ٸ�ֵ��" << endl; 
} 
void error6() 
{ 
 cout << "��" <<line << "����δ������ȱ�� var��" << endl; 
} 
void error7()
{ 
  cout << "��" << line << "��ȱ��������" << endl;
} 
void error8()
{ 
  cout << "��" << line << "��ȱ��������" << endl; 
} 
void error9() 
{ 
	 cout << "��" << line << "��ȱ�ٲ���" << endl;
} 
void error10()
{ 
  	cout << "��" << line << "�� procedure ����" << endl;
} 
void error11()
{ 
 	 cout << "��" << line << "��ȱ�� end��begin �� end ��ƥ�䣩" << endl; 
}
void error12()
{ 
  	 cout << "��" << line << "��ȱ�� then��if �� then ��ƥ�䣩" << endl; 
} 
void error13()
{ 
  	 cout << "��" << line << "��ȱ�� do��while �� do ��ƥ�䣩" << endl; 
} 
void error14()
{ 
   	cout << "��" << line << "�е��ù���ȱ�ٱ�ʶ��" << endl; 
} 
void error15()
{ 
   	cout << "��" << line << "��ȱ�ٷֺ�" << endl; 
} 
void error16()
{ 
   	cout << "��" << line << "������������ȱ�ٽ����" << endl;
} 

void prog()//����ĵݹ��ӳ���ʵ��<prog> �� program <id>��<block> 
{ 
  	line++; 
  	mm++;
  	yufa0.SYM=yufa2[mm].SYM;
  	if (yufa0.SYM == "program")// program 
		{ 
		  mm++; 
		  yufa0.SYM = yufa2[mm].SYM; 
		  if (yufa0.SYM == "biaoshifu") // <id>,����ʶ�� 
		  { 
		  	mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
		  	if (yufa0.SYM == ";")//; 
		  		{ 
				  mm++; 
				  yufa0.SYM = yufa2[mm].SYM;
				  block(); 
		  		} 
			else error1();//ȱ�٣�
		  } 
		  else error2();//ȱ�ٱ�ʶ�����߱�ʶ������ 
		 } 
	else 
	error0(); 	  
		  cout << "\n\n--------------------------�﷨��������------------------------\n\n" << endl;
		  
 } 
void block()//������ĵݹ��ӳ���ʵ��,<block> �� [<condecl>][<vardecl>][<proc>]<body>
{ 
	if (yufa0.SYM == "const") 
		condecl();
	if (yufa0.SYM == "var") 
		vardecl(); 
	if (yufa0.SYM == "procedure") 
		proc(); 
	body();
} 
			
 void condecl()//����˵���ĵݹ��ӳ���ʵ��,<condecl> �� const <const>{,<const>};
 { 
 	line++; 
 	if (yufa0.SYM == "const") // const 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		_const();//<const> 
		while (yufa0.SYM == ",")//,����
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
		else error1();//ȱ�٣� 
		} 
	else error3();//ȱ�ٳ������� const 
}
void _const()//����,<const> �� <id>:=<integer> 
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
				else error4();//����δ��ֵ 
			}
		else error5();//ȱ�ٸ�ֵ�ţ����߸�ֵ�Ŵ��� 
	 } 
	else error2(); //ȱ�ٱ�ʶ�� 
} 
void vardecl()//����˵���ĵݹ��ӳ���ʵ��,<vardecl> �� var <id>{,<id>}; 
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
				yufa0.SYM = yufa2[mm].SYM;//��һ���� 
				if (yufa0.SYM == "biaoshifu") //<id> 
					{ 
					  mm++; 
					  yufa0.SYM = yufa2[mm].SYM; 
					} 
				else 
				error2();//ȱ�ٱ�ʶ����ȱ�ٱ���
			} 
		if (yufa0.SYM == ";") 
		 { 
		 	mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
		 }//��һ���� 
		 else error2(); 
		 } 
	} 
  else error6();//����δ���� 
} 
void proc()//<proc> �� procedure <id>��<id>{,<id>}��;<block>{;<proc>}
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
						 yufa0.SYM = yufa2[mm].SYM;//��һ����---------->> 
						 block(); 
						 while (yufa0.SYM == ";") 
						 { 
						   mm++; 
						   yufa0.SYM = yufa2[mm].SYM; 
						   proc(); 
						  } 
					  }
					else error1();// ȱ��; 
				} 
				 else error7();//���Ų�ƥ�� ���������٣�
				}
				else error8();//������ȱ��
			}
			else error9();//ȱ�ٲ���
		} 
		else error10();//procedure ���� 
}
							    
void body()//<body> �� begin <statement>{;<statement>}end 
{ 
	if (yufa0.SYM == "begin")
	{ 
			line++; 
			mm++; 
			yufa0.SYM = yufa2[mm].SYM; 
			statement(); 
			// while ((yufa0.SYM == "biaoshi") || (yufa0.SYM == "if") || (yufa0.SYM == "while") || 
			// (yufa0.SYM == "call") || (yufa0.SYM == "read") || (yufa0.SYM == "write") || (yufa0.SYM == "begin")) 
			// error1();//���δ�����һ����ȱ��";" 
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
			else error11();//ȱ�� end �� begin ƥ��
	} 
	else error11();//ȱ�� begin 
} 
//<statement> �� <id> := <exp> 
//��� 
// |if <lexp> then <statement>[else <statement>] 
// |while <lexp> do <statement> 
// |call <id>[��<exp>{,<exp>}��]
// |<body> 
// |read (<id>{��<id>}) 
// |write (<exp>{,<exp>}) 
void statement() 
{ 
	if (yufa0.SYM == "biaoshifu") //<statement> �� <id> := <exp> 
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
		else error5();//��ֵ�Ŵ��� 
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
		error12();//ȱ�� then 
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
			else error13();//ȱ�� do ��ƥ�䣨while �� do ��ƥ�䣩 
	} 
	else if (yufa0.SYM == "call")//call <id>[��<exp>{,<exp>}��] 
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
					else error7();//ȱ�������� 
				} // �˴����ᱨ��[��<exp>{,<exp>}��]��Ϊ��ʾ���п��޵�
 			}
			else error14();//���ù���ȱ�ٱ�ʶ�� 
	} 
	else if (yufa0.SYM == "read")//read (<id>{��<id>})
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
						else error2();//ȱ�ٱ�ʾ�� 
					 }
					if (yufa0.SYM == ")") 
					{ 
						mm++; 
						yufa0.SYM = yufa2[mm].SYM; 
					} 
					else error7();//ȱ�������� 
				 } 
					else error9();//read ȱ�ٲ��� 
				}
			else error8();//ȱ�������� 
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
				else error7();//ȱ�������� 
			} 
			else error8();//ȱ�������� 
	} 
	else body();//<body> 
} 
//<lexp> �� <exp> <lop> <exp>|odd <exp> 
//<exp> �� [+|-]<term>{<aop><term>} 
//<term> �� <factor>{<mop><factor>} 
//<factor>��<id>|<integer>|(<exp>) 
//<lop> �� =|<>|<|<=|>|>= //<aop> �� +|- 
//<mop> �� *|/ 
//<id> �� l{l|d} ��ע��l ��ʾ��ĸ�� 
//<integer> �� d{d} 
void lexp()//<lexp> �� <exp> <lop> <exp>|odd <exp> 
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
			error15();//����������ȱ�ٽ�� 
	} 
	//odd �Ƿ���ڣ�
	} 
void exp()//<exp> �� [+|-]<term>{<aop><term>} 
{ 
	while((yufa0.SYM == "+") || (yufa0.SYM == "-")) 
 	{ 
	 	 mm++; 
		 yufa0.SYM = yufa2[mm].SYM; 
		 term();//<term> 
	} 
 	term(); 
	while ((yufa0.SYM == "+") || (yufa0.SYM == "-"))//<aop> �� +|- 
	{ 
		mm++; 
		yufa0.SYM = yufa2[mm].SYM; 
		term();
  	} 
} 
void term()//<term> �� <factor>{<mop><factor>} 
{ 
  	factor(); 
  	while ((yufa0.SYM == "*") || (yufa0.SYM == "/"))//<mop> �� *|/ 
  	{ 
		  mm++; 
		  yufa0.SYM = yufa2[mm].SYM; 
		  factor();//factor() 
	} 
} 
void factor()//<factor>��<id>|<integer>|(<exp>) 
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
		else error7(); //ȱ�������� 
	} 
} 
string Concat(char ch,string strToken) 
{ 
	strToken += ch; 
	return strToken; 
} 
int IsLetter(char c ) //�ж���ĸ 
{ 
	if (((c <= 'z')&&(c >= 'a')) || ((c <= 'Z')&&(c >= 'A'))) 
		return 1; 
	else 
		return 0;
} 
int IsDigit(char c) //�ж�����
{ 
	if ((c >= '0')&&(c <= '9')) 
		return 1; 
	else 
		return 0; 
} 
int IsKey(string StrToken) //�жϱ����� 
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
	//�˵��հ��ַ� 
	// ch = file.get(); 
   if (IsLetter(ch))//��ͷ����ĸ�õ����� 
   { 
   		while (IsLetter(ch) || IsDigit(ch)) 
   		{ 
		   StrToken = Concat(ch,StrToken); 
		   ch = file.get(); 
		} 
   		if (IsKey(StrToken)) //�õ������ж��Ƿ��Ǳ����ֺͱ�ʶ�� 
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
  else if (IsDigit(ch)) //��ͷ������ 
   {  
   		while(IsDigit(ch)) //�õ����ִ� 
		{ 
			StrToken = Concat(ch,StrToken); 
			ch = file.get(); 
		} 
   		if(IsLetter(ch)) //���ֺ������ĸ������ʷ� 
   		{ 
   			StrToken="";
   			while(IsLetter(ch)||IsDigit(ch))
    		{ 
				StrToken = Concat(ch,StrToken); 
				ch = file.get(); 
			} 
			cout <<"�ʷ�����(���ֺ������ĸ) "<< StrToken<< endl; 
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

		while(IsDigit(ch)) //�õ����ִ� 
		{ 
			StrToken = Concat(ch,StrToken); 
			ch = file.get(); 
		} 
   		if(IsLetter(ch)) //���ֺ������ĸ������ʷ� 
   		{ 
   			StrToken="";
   			while(IsLetter(ch)||IsDigit(ch))
    		{ 
				StrToken = Concat(ch,StrToken); 
				ch = file.get(); 
			} 
			cout <<"�ʷ�����(���ֺ������ĸ) "<< StrToken<< endl; 
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
				 cout << ":" << "�ʷ����󣨣�����û��=��" << endl; 
			 
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
	cout << "������Դ�ļ���:"; 
	cin.getline(filename, 30); 
	file.open(filename, ios::in); 
	if (!file) 
	{ 
		cout << "�ļ���ʧ��!\n\n"; 
		exit(0);
	}
	
	yufa2[nn].l = 0; //���з��ĸ���0
	cout << "\n--------------------------�ʷ�����------------------------" << endl;
	int sss=BTO(filename); 
	if(sss==0)
	{

	ch=file.get();
	while (!file.eof()) 
	{ 
		
	  while(ch == ' ' || ch == '\t'||ch == '\n') //�˵��հ��ַ�
	  	{
			  
	  		if (ch == '\n') //����++
	  		{ 
	  			ll ++; 
			}
			ch=file.get(); 
	  	} 
	  	
	 	 getsym(file); 
	 	 
		 nn++;//�����±�++ 
	 	 yufa2[nn].SYM = yufa1.SYM; 
	 	 yufa2[nn].strToken = yufa1.strToken; 
	  	 yufa2[nn].l = ll; 

	  //	cout << "(" << setw(2) << nn << ") "; 
	  //	cout << setw(13) << yufa2[nn].SYM << setw(9) << yufa2[nn].strToken << " "; 
	  //	cout << setw(13) << yufa1.SYM << setw(9) << yufa1.strToken << "\n"; 
	} 
	  
	  // nn++; 
	  // yufa2[nn].SYM = "the_end";//��� 
	  // yufa2[nn].strToken = "the_end"; 
	  file.close(); 
	 
	 cout << "\n\n\n--------------------------�﷨����------------------------" << endl; 
	 // yufa0.SYM = yufa2[mm].SYM; 
	  // yufa0.SYM = yufa1.SYM; 
	prog(); 
}

	  return 0; 
}
