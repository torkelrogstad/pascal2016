program OperatorTest;


procedure TestUnaryBoolean;

   procedure Test (x: boolean);
   begin
      write('n', 'o', 't', ' ', x, ' ', '=', ' ', not x, eol);
   end; { Test }

begin
   Test(false);  Test(true);
end; { TestUnaryBoolean }


procedure TestBinaryBoolean;

   procedure Test (x: boolean;  y: boolean);
   begin
      write(x, ' ', 'a', 'n', 'd', ' ', y, ' ', '=', ' ', x and y, eol);
      write(x, ' ', 'o', 'r', ' ', y, ' ', '=', ' ', x or y, eol);
   end; { Test }

begin
   Test(false, false);  Test(false, true);
   Test(true, false);  Test(true, true);
end; { TestBinaryBoolean }


procedure TestUnaryNumeric;

   procedure Test (x: integer);
   begin
      write('-', ' ', x, ' ', '=', ' ', -x, eol);
      write('+', ' ', x, ' ', '=', ' ', +x, eol);
   end; { Test }

begin
   Test(17);  Test(-11);  Test(0);
end; { TestUnaryNumeric }


procedure TestBinaryNumeric;

   procedure Test (x: integer;  y: integer);
   begin
      write(x, ' ', '+', ' ', y, ' ', '=', ' ', x + y, eol);
      write(x, ' ', '-', ' ', y, ' ', '=', ' ', x - y, eol);
      write(x, ' ', '*', ' ', y, ' ', '=', ' ', x * y, eol);
      if y <> 0 then begin
	 write(x, ' ', 'd', 'i', 'v', ' ', y, ' ', '=', ' ', x div y, eol);
	 write(x, ' ', 'm', 'o', 'd', ' ', y, ' ', '=', ' ', x mod y, eol);
      end
   end; { Test }

begin
   Test(17, 17);  Test(17, -11);  Test(17, 0);
   Test(-11, 17);  Test(-11, -11);  Test(17, 0);
   Test(0, 17);  Test(0, -11);  Test(0, 0);
end; { TestBinaryNumeric }


begin
   TestUnaryBoolean;  TestUnaryNumeric;
   TestBinaryBoolean;  TestBinaryNumeric;
end.
