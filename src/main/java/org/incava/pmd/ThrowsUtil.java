package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.ast.ASTName;
import net.sourceforge.pmd.ast.ASTNameList;

/**
 * Miscellaneous routines for throws lists.
 */
public class ThrowsUtil extends SimpleNodeUtil {
    public static List<ASTName> getNames(ASTNameList names) {
        return findChildren(names, ASTName.class);
    }

    public static String getName(ASTNameList names, int index) {
        ASTName name = findChild(names, ASTName.class, index);
        return name == null ? null : toString(name);
    }

    public static ASTName getNameNode(ASTNameList names, int index) {
        return findChild(names, ASTName.class, index);
    }
}
