package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * Miscellaneous functions for Token.
 */
public class TokenUtil {
    public static void dumpToken(Token st, String prefix) {
        while (st.specialToken != null) {
            st = st.specialToken;
        }
                        
        while (st != null) {
            String s = st.toString().replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r");
            tr.Ace.log(prefix + "    s[" + getLocation(st, st) + "] \"" + s + "\"");
            st = st.next;
        }
    }

    public static String getLocation(Token t) {
        return t.beginLine + ":" + t.beginColumn;
    }

    public static String getLocation(Token t1, Token t2) {
        return "[" + t1.beginLine + ":" + t1.beginColumn + ":" + t2.endLine + ":" + t2.endColumn + "]";
    }
}
