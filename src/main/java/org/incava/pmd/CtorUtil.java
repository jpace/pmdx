package org.incava.pmdx;

import net.sourceforge.pmd.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.ast.ASTFormalParameters;
import net.sourceforge.pmd.ast.JavaParserConstants;
import net.sourceforge.pmd.ast.Token;

/**
 * Miscellaneous routines for constructors.
 */
public class CtorUtil extends FunctionUtil {
    public static Token getName(ASTConstructorDeclaration ctor) {
        return SimpleNodeUtil.findToken(ctor, JavaParserConstants.IDENTIFIER);
   }

    public static ASTFormalParameters getParameters(ASTConstructorDeclaration ctor) {
        return findChild(ctor, ASTFormalParameters.class);
    }
    
    public static double getMatchScore(ASTConstructorDeclaration a, ASTConstructorDeclaration b) {
        ASTFormalParameters afp = getParameters(a);
        ASTFormalParameters bfp = getParameters(b);
        
        return ParameterUtil.getMatchScore(afp, bfp);
    }

    public static String getFullName(ASTConstructorDeclaration ctor) {
        Token nameTk = getName(ctor);
        ASTFormalParameters params = getParameters(ctor);
        return toFullName(nameTk, params);
    }
}
