package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.ast.ASTName;
import net.sourceforge.pmd.ast.ASTNameList;

/**
 * Miscellaneous routines for throws lists.
 */
public class ThrowsUtil extends SimpleNodeUtil {
    public static List<ASTName> getNames(ASTNameList names) {
        return snatchChildren(names, "net.sourceforge.pmd.ast.ASTName");
    }

    public static String getName(ASTNameList names, int index) {
        ASTName name = (ASTName)findChild(names, "net.sourceforge.pmd.ast.ASTName", index);
        return name == null ? null : toString(name);
    }

    public static ASTName getNameNode(ASTNameList names, int index) {
        return (ASTName)findChild(names, "net.sourceforge.pmd.ast.ASTName", index);
    }
}
