package org.incava.pmdx;

import java.util.Iterator;
import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.lang.StringExt;

/**
 * Miscellaneous routines for functions (ctors and methods).
 */
public class Function extends Node<AbstractJavaNode> {
    public Function(AbstractJavaNode node) {
        super(node);
    }

    /**
     * Returns the throws token, or null if none.
     */
    public Token getThrows() {
        Token tk = getFirstToken();
        while (true) {
            tr.Ace.cyan("tk", tk);
            if (tk.kind == JavaParserConstants.THROWS) {
                return tk;
            }
            else if (tk == getLastToken()) {
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
    public ASTNameList getThrowsList() {
        List<Object> children = getChildren();
        Iterator<Object> it = children.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Token && ((Token)obj).kind == JavaParserConstants.THROWS && it.hasNext()) {
                return (ASTNameList)it.next();
            }
        }
        return null;
    }

    protected String toFullName(Token tk, ASTFormalParameters params) {
        List<String> types = ParameterUtil.getParameterTypes(params);
        String       args  = StringExt.join(types, ", ");
        return tk.image + "(" + args + ")";
    }
}
