/* Generated By:JJTree: Do not edit this line. ASTAnd.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.aws.greengrass.device.configuration.parser;

public
class ASTAnd extends SimpleNode {
  public ASTAnd(int id) {
    super(id);
  }

  public ASTAnd(RuleExpression p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RuleExpressionVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ad1c7216b1f6190a0cc12d71b710b3b2 (do not edit this line) */
