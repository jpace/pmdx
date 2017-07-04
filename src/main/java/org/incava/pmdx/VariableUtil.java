package org.incava.pmdx;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * Miscellaneous routines for variables (declarators).
 */
public class VariableUtil extends SimpleNodeUtil {
    public static Token getName(ASTVariableDeclarator vd) {
        ASTVariableDeclaratorId vid = findChild(vd, ASTVariableDeclaratorId.class);
        return getFirstToken(vid);
    }

    public static List<Token> getVariableNames(List<ASTVariableDeclarator> vds) {
        List<Token> names = new ArrayList<Token>();
        for (ASTVariableDeclarator vd : vds) {
            names.add(getName(vd));
        }
        return names;
    }
}
