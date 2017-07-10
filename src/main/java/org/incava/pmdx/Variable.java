package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * Miscellaneous routines for fields.
 */
public class Variable extends Node<ASTVariableDeclarator> {
    public Variable(ASTVariableDeclarator vd) {
        super(vd);
    }

    public Token getName() {
        ASTVariableDeclaratorId vid = findChild(ASTVariableDeclaratorId.class);
        return Node.of(vid).getFirstToken();
    }    
}
