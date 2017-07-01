/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTClassOrInterfaceBodyDeclaration.java */

package net.sourceforge.pmd.lang.java.ast;

public class ASTClassOrInterfaceBodyDeclaration extends AbstractJavaNode {

    public ASTClassOrInterfaceBodyDeclaration(int id) {
        super(id);
    }

    public ASTClassOrInterfaceBodyDeclaration(JavaParser p, int id) {
        super(p, id);
    }

    @Override
    public boolean isFindBoundary() {
        return isAnonymousInnerClass();
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public boolean isAnonymousInnerClass() {
        return jjtGetParent().jjtGetParent() instanceof ASTAllocationExpression;
    }

    public boolean isEnumChild() {
        return jjtGetParent().jjtGetParent() instanceof ASTEnumConstant;
    }
}
