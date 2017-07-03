package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * Miscellaneous routines for Items.
 */
public class ItemUtil extends SimpleNodeUtil {
    protected static final int[] ACCESSES = new int[] {
        JavaParserConstants.PUBLIC,
        JavaParserConstants.PROTECTED,
        JavaParserConstants.PRIVATE
    };

    /**
     * Returns the access type, as a string. "package" is the default.
     */
    public static String getAccessString(AbstractJavaNode node) {
        Token tk = getAccess(node);
        return tk == null ? "package" : tk.image;
    }

    /**
     * Returns the access type, as a token.
     */
    public static Token getAccess(AbstractJavaNode node) {
        for (int ai = 0; ai < ACCESSES.length; ++ai) {
            int   acc = ACCESSES[ai];
            Token tk  = getLeadingToken(node, acc);
            if (tk != null) {
                return tk;
            }
        }
        return null;
    }
}
