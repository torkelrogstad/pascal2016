program GCD;
/* A program to compute the {greatest common} of two numbers,
   i.e., the biggest number by which the two original
   numbers can be divided without a remainder. */

const v1 = 1071; v2 = 462;

var res: integer;

function GCD (m: integer; n: integer): integer;
begin
   if n = 0 then
      GCD := m
   else
      GCD := GCD(n, m mod n)
end; { GCD }

begin
   res := GCD(v1,v2);
   write('G', 'C', 'D', '(', v1, ',', v2, ')', '=', res, eol);
end.
