/* Test program 'easter'
                ========
   Computes Easter Sunday for the years 2010-2020. 
*/

program Easter;

var y: integer;
   
procedure easter (y: integer);
var
   a: integer;  b: integer;  c: integer;  d: integer;  e: integer;
   f: integer;  g: integer;  h: integer;  i: integer;  k: integer;
   l: integer;  m: integer;

   month: integer;  /* The date of Easter Sunday */
   day: integer;

   ix: integer;
begin
   a := y mod 19;
   b := y div 100;
   c := y mod 100;
   d := b div 4;
   e := b mod 4;
   f := (b+8) div 25;
   g := (b-f+1) div 3;
   h := (19*a+b-d-g+15) mod 30;
   i := c div 4;
   k := c mod 4;
   l := (32+2*e+2*i-h-k) mod 7;
   m := (a+11*h+22*l) div 451;

   month := (h+l-7*m+114) div 31;
   day := (h+l-7*m+114) mod 31 + 1;

   if month = 3 then
      write(day, ' ', 'M', 'a', 'r', 'c', 'h', ' ', y, eol)
   else
      write(day, ' ', 'A', 'p', 'r', 'i', 'l', ' ', y, eol);
end; {easter}

begin
   y := 2010;
   while y <= 2020 do
   begin
      easter(y);  y := y + 1;
   end
end.
