package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * Miscellaneous routines for constructors.
 */
public class Ctor extends Function {
    public Ctor(ASTConstructorDeclaration ctor) {
        super(ctor);
    }
    
    public Token getName() {
        return findToken(JavaParserConstants.IDENTIFIER);
    }

    public ASTFormalParameters getParameters() {
        return findChild(ASTFormalParameters.class);
    }
    
    public double getMatchScore(Ctor other) {
        ASTFormalParameters afp = getParameters();
        ASTFormalParameters bfp = other.getParameters();
        
        return ParameterUtil.getMatchScore(afp, bfp);
    }

    public String getFullName() {
        Token nameTk = getName();
        ASTFormalParameters params = getParameters();
        return toFullName(nameTk, params);
    }
}
