#include <stdio.h>
  
#define N 100000
bool p[N+1];
int pp[N+1];
int cnum[N+1]={0};
void jisuan()
{
    int i,j;
    int size1=0;
    for (i=2;i<=N;i++)
    {
        if (!p[i])
        {
            pp[size1++]=i;
            for (j=i<<1;j<N+1;j+=i)
            {
                p[j]=true;
            }
        }
  
    }
  
    for (i=0;i<size1;i++)
    {
        for (j=i+1;j<size1;j++)
        {
            if (pp[i]+pp[j]>N)
            {
                break;
  
            }
            cnum[pp[i]+pp[j]]++;
        }
    }
}
int main()
{
    jisuan();
    int num;
    while (scanf("%d",&num)!=EOF)
    {
        printf("%d\n",cnum[num]);
    }
  
    return 0;
}
