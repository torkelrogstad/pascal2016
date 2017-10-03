package parser;

public abstract class PascalDecl extends PascalSyntax {
  String name, progProcFuncName;
  int declLevel = 0, declOffset = 0;
  public types.Type type;
  TypeDecl typeRef;
  String label;

  PascalDecl(String id , int lNum) {
    super(lNum);
    name = id;
  }


  //this triggers if something was tried assigned to an unassignable object
  //in all assignable objects there are polymorphic methods that trigger instead
  void checkWhetherAssignable(PascalSyntax where) {
    where.error("You cannot assign to a " + this.getClass().getSimpleName());
  }

  void prettyPrint() {

  }

  void checkWhetherFunction(PascalSyntax where) {
    return ;
  }

  void checkWhetherProcedure(PascalSyntax where) {
    return ;
  }

  //this triggers if a variable is set to something without value
  //in all assignable objects there are polymorphic methods that trigger instead
  void checkWhetherValue(PascalSyntax where) {
    where.error(this.getClass().getSimpleName());
  }
}
