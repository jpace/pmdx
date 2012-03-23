package org.incava.pmdx;

import java.util.*;
import net.sourceforge.pmd.ast.*;


/**
 * Miscellaneous routines for constructors.
 */
public class CtorUtil extends FunctionUtil {
    public static Token getName(ASTConstructorDeclaration ctor) {
        return SimpleNodeUtil.findToken(ctor, JavaParserConstants.IDENTIFIER);
   }

    public static ASTFormalParameters getParameters(ASTConstructorDeclaration ctor) {
        return (ASTFormalParameters)SimpleNodeUtil.findChild(ctor, ASTFormalParameters.class);
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
