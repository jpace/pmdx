package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * Miscellaneous routines for classes.
 */
public class ClassUtil extends SimpleNodeUtil {
    public static Token getName(ASTClassOrInterfaceDeclaration coid) {
        return findToken(coid, JavaParserConstants.IDENTIFIER);
    }

    public static double getMatchScore(ASTClassOrInterfaceDeclaration a, ASTClassOrInterfaceDeclaration b) {
        return getName(a).image.equals(getName(b).image) ? 1.0 : 0.0;
    }
}
