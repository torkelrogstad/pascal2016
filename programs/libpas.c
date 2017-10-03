#include <stdio.h>

void write_bool (int c)
{
  if (c) printf("T");
  else printf("F");
}

void write_char (int c)
{
  printf("%c", c);
}

void write_int (int n)
{
  printf("%d", n);
}
