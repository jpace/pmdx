package org.incava.pmdx;

import java.util.Iterator;
import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.lang.StringExt;

/**
 * Miscellaneous routines for functions (ctors and methods).
 */
public class FunctionUtil extends SimpleNodeUtil {
    /**
     * Returns the throws token, or null if none.
     */
    public static Token getThrows(AbstractJavaNode function) {
        Token tk = getFirstToken(function);
        while (true) {
            if (tk.kind == JavaParserConstants.THROWS) {
                return tk;
            }
            else if (tk == getLastToken(function)) {
                break;
            }
            else {
                tk = tk.next;
            }
        }
        return null;
    }

    /**
     * Returns the throws list, or null if none.
     */
    public static ASTNameList getThrowsList(AbstractJavaNode function) {
        List<Object> children = getChildren(function);
        Iterator<Object> it = children.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Token && ((Token)obj).kind == JavaParserConstants.THROWS && it.hasNext()) {
                return (ASTNameList)it.next();
            }
        }
        return null;
    }

    protected static String toFullName(Token tk, ASTFormalParameters params) {
        List<String> types = ParameterUtil.getParameterTypes(params);
        String       args  = StringExt.join(types, ", ");
        return tk.image + "(" + args + ")";
    }
}
