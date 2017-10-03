
/* This program prints all primes less than 1000
   using a technique called "The sieve of Eratosthenes". */

program Primes;

const Limit = 1000;

var prime : array [2..Limit] of Boolean;
    i	  : integer;

procedure FindPrimes;
var i1 : integer;
    I2 : Integer;
begin
   i1 := 2;
   while i1 <= Limit do
   begin
      i2 := 2*i1;
      while i2 <= Limit do
      begin
	 prime[i2] := false;
	 i2 := i2+i1
      end;
      i1 := i1 + 1
   end
end; {FindPrimes}

procedure P4 (x	: integer);
begin
   if x < 1000 then write(' ');
   if x <  100 then write(' ');
   if x <   10 then write(' ');
   write(x);
end; {P4}

procedure PrintPrimes;
var i	     : integer;
    NPrinted : integer;
begin
   i := 2;  NPrinted := 0;
   while i <= Limit do
   begin
      if prime[i] then
      begin
	 if (NPrinted > 0) and (NPrinted mod 10 = 0) then write(eol);
	 P4(i);  NPrinted := NPrinted + 1;
      end;
      i := i + 1;
   end;
   write(eol)
end; {PrintPrimes}

begin {main program}
   i := 2;
   while i <= Limit do begin prime[i] := true;  i := i+1 end;

   /* Find and print the primes: */
   FindPrimes;  PrintPrimes;
end. {main program}
