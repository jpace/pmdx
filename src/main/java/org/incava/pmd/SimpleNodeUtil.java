package org.incava.pmdx;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;

import static org.incava.ijdk.util.IUtil.*;

/**
 * Miscellaneous functions for Node.
 */
public class SimpleNodeUtil {
    /**
     * Returns the token images for the node.
     */
    public static String toString(Node node) {
        Token tk = getFirstToken(node);
        Token last = getLastToken(node);
        StringBuilder sb = new StringBuilder(tk.image);
        while (tk != last) {
            tk = tk.next;
            sb.append(tk.image);
        }
        return sb.toString();
    }

    public static Token getFirstToken(Node node) {
        return node.jjtGetFirstToken();
    }

    public static Token getLastToken(Node node) {
        return node.jjtGetLastToken();
    }

    /**
     * Returns whether the node has any children.
     */
    public static boolean hasChildren(Node node) {
        return node.jjtGetNumChildren() > 0;
    }

    /**
     * Returns the parent node.
     */
    public static Node getParent(Node node) {
        return (Node)node.jjtGetParent();
    }

    /**
     * Returns a list of children, both nodes and tokens.
     */
    public static List<Object> getChildren(Node node) {
        return getChildren(node, true, true);
    }

    /**
     * Returns a list of children, optionally nodes and tokens.
     */
    public static List<Object> getChildren(Node node, boolean getNodes, boolean getTokens) {
        List<Object> list = new ArrayList<Object>();
        
        Token t = new Token();
        t.next = getFirstToken(node);
        
        int nChildren = node.jjtGetNumChildren();
        for (int ord = 0; ord < nChildren; ++ord) {
            Node n = (Node)node.jjtGetChild(ord);
            while (true) {
                t = t.next;
                if (t == getFirstToken(n)) {
                    break;
                }
                if (getTokens) {
                    list.add(t);
                }
            }
            if (getNodes) {
                list.add(n);
            }
            t = getLastToken(n);
        }

        while (t != getLastToken(node)) {
            t = t.next;
            if (getTokens) {
                list.add(t);
            }
        }

        return list;
    }

    public static Node findChild(Node parent) {
        return findChild(parent, null);
    }

    public static <NodeType extends Node> NodeType findChild(Node parent, Class<NodeType> childType) {
        return findChild(parent, childType, 0);
    }

    @SuppressWarnings("unchecked")
    public static <NodeType extends Node> NodeType findChild(Node parent, Class<NodeType> childType, int index) {
        if (index < 0 || isNull(parent)) {
            return null;
        }

        int nChildren = parent.jjtGetNumChildren();
        if (index >= nChildren) {
            return null;
        }

        int nFound = -1;
        for (int idx = 0; idx < nChildren; ++idx) {
            Node child = getChildOfType(parent, childType, idx);
            if (isNotNull(child) && ++nFound == index) {
                return (NodeType)child;
            }
        }
        return null;
    }

    /**
     * Returns the node if the class of the child at the given index matches the
     * given class type. If the given one is null, the child will match.
     */
    @SuppressWarnings("unchecked")
    private static <NodeType extends Node> NodeType getChildOfType(Node parent, Class<NodeType> childType, int index) {
        Node child = (Node)parent.jjtGetChild(index);
        return isNull(childType) || child.getClass().equals(childType) ? (NodeType)child : null;
    }

    /**
     * Returns a list of child tokens, non-hierarchically.
     */
    public static List<Token> getChildTokens(Node node) {
        List<Token> children = new ArrayList<Token>();
        
        Token tk = getFirstToken(node);
        Token lastTk = getLastToken(node);
        
        while (tk != null) {
            children.add(tk);
            if (tk == lastTk) { // yes, ==, not equals
                break;
            }
            tk = tk.next;
        }

        return children;
    }

    @SuppressWarnings("unchecked")
    public static <NodeType extends Node> List<NodeType> findChildren(Node parent, Class<NodeType> childType) {
        List<NodeType> list = new ArrayList<NodeType>();
        int nChildren = parent == null ? 0 : parent.jjtGetNumChildren();
        for (int i = 0; i < nChildren; ++i) {
            Node child = (Node)parent.jjtGetChild(i);
            if (childType == null || child.getClass().equals(childType)) {
                list.add((NodeType)child);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <NodeType extends Node> List<NodeType> findChildren(Node parent) {
        return findChildren(parent, null);
    }

    /**
     * @todo remove -- this doesn't seem to be used.
     */
    public static List<Node> findDescendants(Node parent, Class<?> childType) {
        List<Node> kids = new ArrayList<Node>();
        int nChildren = parent == null ? 0 : parent.jjtGetNumChildren();
        for (int i = 0; i < nChildren; ++i) {
            Node child = (Node)parent.jjtGetChild(i);
            if (childType == null || child.getClass().equals(childType)) {
                kids.add(child);
            }
            kids.addAll(findDescendants(child, childType));
        }

        return kids;
    }

    /**
     * Returns the tokens for a node.
     */
    public static List<Token> getTokens(Node node) {
        List<Token> tokens = new ArrayList<Token>();
        Token tk = new Token();
        tk.next = getFirstToken(node);

        if (tk != null) {
            tokens.add(tk);
            do {
                tk = tk.next;
                tokens.add(tk);
            } while (tk != getLastToken(node));
        }
        return tokens;
    }

    public static Token findToken(Node node, int tokenType) {
        List<Object> childTokens = getChildren(node, false, true);
        for (Object obj : childTokens) {
            Token tk = (Token)obj;
            if (tk.kind == tokenType) {
                return tk;
            }
        }
        return null;
    }

    /**
     * Returns whether the node has a matching token, occurring prior to any
     * non-tokens (i.e., before any child nodes).
     */
    public static boolean hasLeadingToken(Node node, int tokenType) {
        return getLeadingToken(node, tokenType) != null;
    }

    /**
     * Returns the matching token, occurring prior to any non-tokens (i.e.,
     * before any child nodes).
     */
    public static Token getLeadingToken(Node node, int tokenType) {
        if (node.jjtGetNumChildren() == 0) {
            return null;
        }

        Node n = (Node)node.jjtGetChild(0);

        Token t = new Token();
        t.next = getFirstToken(node);
            
        while (true) {
            t = t.next;
            if (t == getFirstToken(n)) {
                break;
            }
            else if (t.kind == tokenType) {
                return t;
            }
        }

        return null;
    }

    /**
     * Returns the tokens preceding the first child of the node.
     */
    public static List<Token> getLeadingTokens(Node node) {
        List<Token> list = new ArrayList<Token>();
        
        if (node.jjtGetNumChildren() == 0) {
            return list;
        }

        Node n = (Node)node.jjtGetChild(0);

        Token t = new Token();
        t.next = getFirstToken(node);
            
        while (true) {
            t = t.next;
            if (t == getFirstToken(n)) {
                break;
            }
            else {
                list.add(t);
            }
        }

        return list;
    }

    public static void print(Node node) {
        print(node, "");
    }

    public static void print(Node node, String prefix) {
        Token first = getFirstToken(node);
        Token last  = getLastToken(node);
        tr.Ace.log(prefix + node.toString() + ":" + TokenUtil.getLocation(first, last));
    }

    public static void dump(Node node) {
        dump(node, "", false);
    }

    public static void dump(Node node, String prefix) {
        dump(node, prefix, false);
    }

    public static void dump(Node node, String prefix, boolean showWhitespace) {
        print(node, prefix);

        List<Object> children = getChildren(node);
        for (Object obj : children) {
            dumpObject(obj, prefix, showWhitespace);
        }
    }  

    public static void dumpObject(Object obj, String prefix, boolean showWhitespace) {
        if (obj instanceof Token) {
            Token tk = (Token)obj;                
            if (showWhitespace && tk.specialToken != null) {
                TokenUtil.dumpToken(tk.specialToken, prefix);
            }                
            tr.Ace.log(prefix + "    \"" + tk + "\" " + TokenUtil.getLocation(tk, tk) + " (" + tk.kind + ")");
        }
        else {
            Node sn = (Node)obj;
            dump(sn, prefix + "    ", showWhitespace);
        }
    }

    /**
     * Returns a numeric "level" for the node. Zero is public or abstract, one
     * is protected, two is package, and three is private.
     */
    public static int getLevel(Node node) {
        List<Token> tokens = getLeadingTokens(node);
        for (Token t : tokens) {
            switch (t.kind) {
                case JavaParserConstants.PUBLIC:
                    // fallthrough
                case JavaParserConstants.ABSTRACT:
                    return 0;
                case JavaParserConstants.PROTECTED:
                    return 1;
                case JavaParserConstants.PRIVATE:
                    return 3;
            }
        }

        // AKA "package"
        return 2;
    }

    private static String getClassName(Class<?> cls) {
        return cls == null ? null : cls.getName();
    }
}
