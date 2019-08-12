#include<iostream> 
#include<string.h> 
#include <fstream> 
#include<iomanip>
#include<stdio.h> 
#include<stdlib.h>
using namespace std; 
char stayWord[13][15] = {"const","var","procedure","begin","end","odd","if","then","call","while","do","read","write"};
/*char enumStayWord[13][15] = {"constsym","varsym","proceduresym","beginsym","endsym","oddsym","ifsym","thensym",
                            "callsym","whilesym","dosym","readsym","writesym"};*/
bool Number(char ch);
bool Case(char ch);
bool Caculation(char ch);
bool Band(char ch);
int StayWord(char *str);
void GetInputFile(char *fileName,char *str);
void Is_Calulation(char *str);
void Is_Band(char *str);
void Analysis(char *InputFileName,char *str);
 
int main()
{
    char strr[100000],input[100]="E:3.txt";
    Analysis(input,strr);
    //return 0;
}
 
bool Number(char ch)//判断数字 
{
    if(ch >='0' && ch <= '9') return true;
    return false;
}
 
bool Case(char ch)//判断字母 
{
    if((ch >='a' && ch <='z') || (ch >= 'A'&& ch <='Z'))
        return true;
    return false;
}
 
bool Caculation(char ch)//判断运算符 
{
    if(ch == '*'||ch == '+' || ch == '-' || ch == '/' || ch == '>' || ch == '<' || ch == '=' || ch == '#' || ch == ':')
        return true;
    return false;
}
 
bool Band(char ch)//判断分界符 
{
    if(ch == '(' || ch == ')'|| ch == ','|| ch ==';' || ch == '.')
        return true;
    return false;
}
 
int StayWord(char *str) //判断关键字 
{
     int i;
     for(i=0;i<13;++i)
	 {
        if(!strcmp(str,stayWord[i])) 
			break;
     }
     return i;
}
 
void GetInputFile(char *fileName,char *str,int *str3)//读取文件中的内容 
{
    char ch;
    int k=0,s=1,t=0;
    FILE *f;
    f = fopen(fileName,"r");//打开文件 
    while((ch=fgetc(f))!=EOF)
    {
        if(ch!='\n'&&ch!='\t')
            {    	
            	str[k++] = ch;
            	str3[t]=s;
            	t++;
			}
        else
            {
            	str[k++]=' ';
            	
            	if(ch=='\n')
            		s++;
            		
            	str3[t]=s;
            	t++;
			}
    }
    str[k]='\0';
    str3[t]='\0';
    fclose(f);
}
 
void Is_Calulation(char *str,int n,int key)//判断是否是运算符 
{
    int len = strlen(str);
    int i;
    for(i=0;i<len;++i)
    {
        if(str[i] == '+') 
			cout<<"+  运算符/加号"<<endl;
        else if(str[i] == '-') 
			cout<<"-  运算符/减号"<<endl;
        else if(str[i] == '*') 
			cout<<"*  运算符/乘号"<<endl;
        else if(str[i] == '/') 
			cout<<"/  运算符/除号"<<endl;
        else if(str[i] == '=') 
			cout<<"=  运算符/等号"<<endl;
        else if(str[i] == ':')
			{
            	if(i+1<len && str[i+1] == '=')
				{
               		 cout<<":=  运算符/赋值号"<<endl;
                		i++;
           		 }
            	else
                	{
                	cout<<str[i]<<"非法字符串！"<<endl;
					cout<<"第"<<n<<" 行"<<" 第"<<key<<" 列"<<endl;
									}
        }
        else if(str[i] == '#') 
			cout<<"#  运算符/不等于号"<<endl;
        else if(str[i] == '>')
			{
            	if(i+1<len && str[i+1] == '=')
				{
                	cout<<">=  逻辑运算符/大于等于号"<<endl;
                	i++;
           		 }
            	else
                	cout<<">  逻辑运算符/大于号"<<endl;
        	}
        else if(str[i] == '<')
		{
            if(i+1<len && str[i+1]=='=')
			{
                cout<<"<=  逻辑运算符/小于等于号"<<endl;
                i++;
            }
            else
                cout<<"<  逻辑运算符/小于号"<<endl;
        }
    }
}
 
void Is_Band(char *str)//判断是否是分界符 
{
    int len,i;
    len = strlen(str);
for(i=0;i<len;++i)
    switch(str[i])
	{
 
       case '(': 
	   			cout<<"(  界符/左括号"<<endl;
				break;
       case ')': 
	   			cout<<")  界符/右括号"<<endl; 
				break;
       case ',': 
	   			cout<<",  界符/逗号"<<endl; 
				break;
       case ';': 
	   			cout<<";  界符/分号"<<endl; 
				break;
       case '.': 
	   			cout<<".  界符/结束号"<<endl; 
				break;
       default : 
	   			break;
    }
}
void Analysis(char *InputFileName,char *str)
{
	int str3[100000];
    GetInputFile(InputFileName,str,str3);//得到文件中内容，即将文件的内容全部存在一个字符串中 
    int len = strlen(str);//得到长度 
    int i,j,len1,len2,k,t,re;
    char str1[100],str2[100];
    i=0;//最大字符串的位置 
   int key=0;
   
   
    while(i<len)
    {
    	if(str3[i]!=str3[i-1])
    		key=0;
    	//得到一个字符串或字符 
        j=0;
        while(str[i] == ' '&& i<len) 
			++i;
        while(str[i] != ' '&& i<len) 
			str1[j++] = str[i++];
        str1[j]='\0';
        len1 = strlen(str1);
        
        
        k=0;
        while(k<len1)
        {
            if(Case(str1[k]))//判断首字母是否是字母 
            { 
                t=0;key++;
                while((!Caculation(str1[k]))&&(!Band(str1[k]))&&k<len1)
                    str2[t++] = str1[k++];//将后续字母加入 
                str2[t]='\0';
                re = StayWord(str2);//判断是否是关键字 
                if(re < 13)
                {
                   // cout<< stayWord[re]<<"  关键字/ "<<enumStayWord[re]<<endl;
                    cout<<stayWord[re]<<"  关键字"<<endl;
                    strcpy(str2,"");
                    t=0;
                }
                else
                {
                    cout<<str2<<"  标识符"<<endl;
                    strcpy(str2,"");
                    t=0;
                }
            }
            else if(Number(str1[k]))//判断是否是数字 
            {	key++;
                while(Number(str1[k])&&k<len1)
                    str2[t++] = str1[k++];
                str2[t]='\0';
                cout<<str2<<"  常数"<<endl;
                strcpy(str2,"");
                t=0;
            }
            else if(Caculation(str1[k]))//判断是否是运算符 
			{
			key++;
                while(Caculation(str1[k])&&k<len1)
                    str2[t++] = str1[k++];
                str2[t] = '\0';
                Is_Calulation(str2,str3[i-1],key);
                strcpy(str2,"");
                t=0;
            }
            else if(Band(str1[k]))//判断是否是分界符 
            {
			key++;
                while(Band(str1[k]) && k<len1)
                    str2[t++] = str1[k++];;
                str2[t] = '\0';
                Is_Band(str2);
                strcpy(str2,"");
                t=0;
            }
            else
			{
				key++;
               	cout<<str1<<"非法字符串！"<<endl;
				cout<<"第"<<str3[i-1]<<" 行"<<" 第"<<key<<" 列"<<endl;; 
				k++;
            }
        }
        strcpy(str2,"");
    }
    fclose(stdout);
    
}


