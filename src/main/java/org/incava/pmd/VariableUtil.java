package org.incava.pmdx;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pmd.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.ast.Token;

/**
 * Miscellaneous routines for variables (declarators).
 */
public class VariableUtil extends SimpleNodeUtil {
    public static Token getName(ASTVariableDeclarator vd) {
        ASTVariableDeclaratorId vid = findChild(vd, ASTVariableDeclaratorId.class);
        return vid.getFirstToken();
    }

    public static List<Token> getVariableNames(List<ASTVariableDeclarator> vds) {
        List<Token> names = new ArrayList<Token>();
        for (ASTVariableDeclarator vd : vds) {
            names.add(getName(vd));
        }
        return names;
    }
}
