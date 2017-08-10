#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netdb.h>

int compare(const void * a,const void* b) {
  return (rand() % 3)-1;
}


int main() {
  char str [3];
  int digits [5];
  int target;

  int sfd = socket(AF_INET,SOCK_STREAM,0);
  uint8_t port = (uint8_t)7777;
  struct sockaddr a = {gethostbyname("localhost")->h_addr,7777};
  connect(sfd,&a,sizeof(a));
  scanf("%d %d %d %d %d %d",&digits[0],&digits[1],&digits[2],&digits[3],&digits[4],&target);
  srand(time(NULL));
  qsort(digits,5,sizeof(int),compare);
  char ops[] = {'+','*'};
  for(int i = 0;i<5;i++) {
    printf("%d",digits[i]);
    if(i<4) printf("%c",ops[rand() % 2]);
  }
  printf("\n");


}


