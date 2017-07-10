package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.collect.StringList;

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
