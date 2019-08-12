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
 
bool Number(char ch)//�ж����� 
{
    if(ch >='0' && ch <= '9') return true;
    return false;
}
 
bool Case(char ch)//�ж���ĸ 
{
    if((ch >='a' && ch <='z') || (ch >= 'A'&& ch <='Z'))
        return true;
    return false;
}
 
bool Caculation(char ch)//�ж������ 
{
    if(ch == '*'||ch == '+' || ch == '-' || ch == '/' || ch == '>' || ch == '<' || ch == '=' || ch == '#' || ch == ':')
        return true;
    return false;
}
 
bool Band(char ch)//�жϷֽ�� 
{
    if(ch == '(' || ch == ')'|| ch == ','|| ch ==';' || ch == '.')
        return true;
    return false;
}
 
int StayWord(char *str) //�жϹؼ��� 
{
     int i;
     for(i=0;i<13;++i)
	 {
        if(!strcmp(str,stayWord[i])) 
			break;
     }
     return i;
}
 
void GetInputFile(char *fileName,char *str,int *str3)//��ȡ�ļ��е����� 
{
    char ch;
    int k=0,s=1,t=0;
    FILE *f;
    f = fopen(fileName,"r");//���ļ� 
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
 
void Is_Calulation(char *str,int n,int key)//�ж��Ƿ�������� 
{
    int len = strlen(str);
    int i;
    for(i=0;i<len;++i)
    {
        if(str[i] == '+') 
			cout<<"+  �����/�Ӻ�"<<endl;
        else if(str[i] == '-') 
			cout<<"-  �����/����"<<endl;
        else if(str[i] == '*') 
			cout<<"*  �����/�˺�"<<endl;
        else if(str[i] == '/') 
			cout<<"/  �����/����"<<endl;
        else if(str[i] == '=') 
			cout<<"=  �����/�Ⱥ�"<<endl;
        else if(str[i] == ':')
			{
            	if(i+1<len && str[i+1] == '=')
				{
               		 cout<<":=  �����/��ֵ��"<<endl;
                		i++;
           		 }
            	else
                	{
                	cout<<str[i]<<"�Ƿ��ַ�����"<<endl;
					cout<<"��"<<n<<" ��"<<" ��"<<key<<" ��"<<endl;
									}
        }
        else if(str[i] == '#') 
			cout<<"#  �����/�����ں�"<<endl;
        else if(str[i] == '>')
			{
            	if(i+1<len && str[i+1] == '=')
				{
                	cout<<">=  �߼������/���ڵ��ں�"<<endl;
                	i++;
           		 }
            	else
                	cout<<">  �߼������/���ں�"<<endl;
        	}
        else if(str[i] == '<')
		{
            if(i+1<len && str[i+1]=='=')
			{
                cout<<"<=  �߼������/С�ڵ��ں�"<<endl;
                i++;
            }
            else
                cout<<"<  �߼������/С�ں�"<<endl;
        }
    }
}
 
void Is_Band(char *str)//�ж��Ƿ��Ƿֽ�� 
{
    int len,i;
    len = strlen(str);
for(i=0;i<len;++i)
    switch(str[i])
	{
 
       case '(': 
	   			cout<<"(  ���/������"<<endl;
				break;
       case ')': 
	   			cout<<")  ���/������"<<endl; 
				break;
       case ',': 
	   			cout<<",  ���/����"<<endl; 
				break;
       case ';': 
	   			cout<<";  ���/�ֺ�"<<endl; 
				break;
       case '.': 
	   			cout<<".  ���/������"<<endl; 
				break;
       default : 
	   			break;
    }
}
void Analysis(char *InputFileName,char *str)
{
	int str3[100000];
    GetInputFile(InputFileName,str,str3);//�õ��ļ������ݣ������ļ�������ȫ������һ���ַ����� 
    int len = strlen(str);//�õ����� 
    int i,j,len1,len2,k,t,re;
    char str1[100],str2[100];
    i=0;//����ַ�����λ�� 
   int key=0;
   
   
    while(i<len)
    {
    	if(str3[i]!=str3[i-1])
    		key=0;
    	//�õ�һ���ַ������ַ� 
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
            if(Case(str1[k]))//�ж�����ĸ�Ƿ�����ĸ 
            { 
                t=0;key++;
                while((!Caculation(str1[k]))&&(!Band(str1[k]))&&k<len1)
                    str2[t++] = str1[k++];//��������ĸ���� 
                str2[t]='\0';
                re = StayWord(str2);//�ж��Ƿ��ǹؼ��� 
                if(re < 13)
                {
                   // cout<< stayWord[re]<<"  �ؼ���/ "<<enumStayWord[re]<<endl;
                    cout<<stayWord[re]<<"  �ؼ���"<<endl;
                    strcpy(str2,"");
                    t=0;
                }
                else
                {
                    cout<<str2<<"  ��ʶ��"<<endl;
                    strcpy(str2,"");
                    t=0;
                }
            }
            else if(Number(str1[k]))//�ж��Ƿ������� 
            {	key++;
                while(Number(str1[k])&&k<len1)
                    str2[t++] = str1[k++];
                str2[t]='\0';
                cout<<str2<<"  ����"<<endl;
                strcpy(str2,"");
                t=0;
            }
            else if(Caculation(str1[k]))//�ж��Ƿ�������� 
			{
			key++;
                while(Caculation(str1[k])&&k<len1)
                    str2[t++] = str1[k++];
                str2[t] = '\0';
                Is_Calulation(str2,str3[i-1],key);
                strcpy(str2,"");
                t=0;
            }
            else if(Band(str1[k]))//�ж��Ƿ��Ƿֽ�� 
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
               	cout<<str1<<"�Ƿ��ַ�����"<<endl;
				cout<<"��"<<str3[i-1]<<" ��"<<" ��"<<key<<" ��"<<endl;; 
				k++;
            }
        }
        strcpy(str2,"");
    }
    fclose(stdout);
    
}


