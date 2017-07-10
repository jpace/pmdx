package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.Token;

public class Method extends Function {
    public Method(ASTMethodDeclaration md) {
        super(md);
    }
    
    public ASTMethodDeclarator getDeclarator() {
        return findChild(ASTMethodDeclarator.class);
    }
    
    public Token getName() {
        ASTMethodDeclarator decl = getDeclarator();
        return Node.of(decl).getFirstToken();
    }

    public ASTFormalParameters getParameters() {
        ASTMethodDeclarator decl = getDeclarator();
        return Node.of(decl).findChild(ASTFormalParameters.class);
    }

    public String getFullName() {
        Token nameTk = getName();
        ASTFormalParameters params = getParameters();
        return toFullName(nameTk, params);
    }    
}
