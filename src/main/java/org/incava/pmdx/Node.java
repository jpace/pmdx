package org.incava.pmdx;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pmd.lang.ast.GenericToken;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.collect.IntegerList;

/**
 * Miscellaneous functions for AbstractJavaNode.
 */
public class Node<ASTNode extends AbstractJavaNode> {
    public static <ASTNode extends AbstractJavaNode> Node<ASTNode> of(ASTNode pn) {
        return new Node<ASTNode>(pn);
    }
    
    private final ASTNode node;

    public Node(ASTNode node) {
        this.node = node;
    }
    
    /**
     * Returns the token images for the node.
     */
    public String toString() {
        Token tk = getFirstToken();
        Token last = getLastToken();
        StringBuilder sb = new StringBuilder(tk.image);
        while (tk != last) {
            tk = tk.next;
            sb.append(tk.image);
        }
        return sb.toString();
    }

    public Token getFirstToken() {
        return (Token)node.jjtGetFirstToken();
    }

    public Token getLastToken() {
        return (Token)node.jjtGetLastToken();
    }

    /**
     * Returns whether the node has any children.
     */
    public boolean hasChildren() {
        return node.jjtGetNumChildren() > 0;
    }

    /**
     * Returns the parent node.
     */
    public AbstractJavaNode getParent() {
        return (AbstractJavaNode)node.jjtGetParent();
    }

    /**
     * Returns a list of children, both nodes and tokens.
     */
    public List<Object> getChildren() {
        return getChildren(true, true);
    }

    /**
     * Returns a list of children, optionally nodes and tokens.
     */
    public List<Object> getChildren(boolean getNodes, boolean getTokens) {
        List<Object> list = new ArrayList<Object>();
        
        Token t = new Token();
        t.next = getFirstToken();
        
        int nChildren = node.jjtGetNumChildren();
        for (int ord = 0; ord < nChildren; ++ord) {
            AbstractJavaNode n = (AbstractJavaNode)node.jjtGetChild(ord);
            Node<AbstractJavaNode> nn = Node.of(n);
            while (true) {
                t = t.next;
                if (t == nn.getFirstToken()) {
                    break;
                }
                if (getTokens) {
                    list.add(t);
                }
            }
            if (getNodes) {
                list.add(n);
            }
            t = nn.getLastToken();
        }

        while (t != getLastToken()) {
            t = t.next;
            if (getTokens) {
                list.add(t);
            }
        }

        return list;
    }

    /**
     * Returns the first child node of the parent.
     */
    public AbstractJavaNode findChild() {
        return findChild(null);
    }

    /**
     * Returns the first child node of the parent, with the given type.
     */
    public <NodeType extends AbstractJavaNode> NodeType findChild(Class<NodeType> childType) {
        return findChild(childType, 0);
    }

    /**
     * Returns the nth child node of the parent, with the given type.
     */
    @SuppressWarnings("unchecked")
    public <NodeType extends AbstractJavaNode> NodeType findChild(Class<NodeType> childType, int nth) {
        if (nth < 0 || node == null) {
            return null;
        }

        int nChildren = node.jjtGetNumChildren();
        if (nth >= nChildren) {
            return null;
        }

        int nFound = -1;
        for (int idx = 0; idx < nChildren; ++idx) {
            AbstractJavaNode child = getChildOfType(childType, idx);
            if (child != null && ++nFound == nth) {
                return (NodeType)child;
            }
        }
        return null;
    }

    /**
     * Returns a list of child tokens, non-hierarchically.
     */
    public List<Token> getChildTokens() {
        List<Token> children = new ArrayList<Token>();
        
        Token tk = getFirstToken();
        Token lastTk = getLastToken();
        
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
    public <NodeType extends AbstractJavaNode> List<NodeType> findChildren(Class<NodeType> childType) {
        List<NodeType> list = new ArrayList<NodeType>();
        int nChildren = node == null ? 0 : node.jjtGetNumChildren();
        for (int i = 0; i < nChildren; ++i) {
            AbstractJavaNode child = (AbstractJavaNode)node.jjtGetChild(i);
            if (childType == null || child.getClass().equals(childType)) {
                list.add((NodeType)child);
            }
        }
        return list;
    }

    // apparently unused (by DiffJ)
    // @SuppressWarnings("unchecked")
    // public static <NodeType extends ASTNode> List<NodeType> findChildren(ASTNode parent) {
    //     return findChildren(parent, null);
    // }

    // /**
    //  * Returns the tokens for a node.
    //  */
    // public static List<Token> getTokens(ASTNode node) {
    //     List<Token> tokens = new ArrayList<Token>();
    //     Token tk = new Token();
    //     tk.next = getFirstToken(node);

    //     if (tk != null) {
    //         tokens.add(tk);
    //         do {
    //             tk = tk.next;
    //             tokens.add(tk);
    //         } while (tk != getLastToken(node));
    //     }
    //     return tokens;
    // }

    public Token findToken(int tokenType) {
        List<Object> childTokens = getChildren(false, true);
        for (Object obj : childTokens) {
            Token tk = (Token)obj;
            if (tk.kind == tokenType) {
                return tk;
            }
        }
        return null;
    }

    /**
     * Returns whether the node has a matching token, occurring prior to any non-tokens (i.e.,
     * before any child nodes).
     */
    public boolean hasLeadingToken(int tokenType) {
        return getLeadingToken(tokenType) != null;
    }

    /**
     * Returns the token of the given type, occurring prior to any non-tokens (i.e., before any
     * child nodes).
     */
    public Token getLeadingToken(int tokenType) {
        if (node.jjtGetNumChildren() == 0) {
            return null;
        }

        AbstractJavaNode n = (AbstractJavaNode)node.jjtGetChild(0);

        Token t = new Token();
        t.next = getFirstToken();
        
        while (true) {
            t = t.next;
            Node<AbstractJavaNode> nn = Node.of(n);
            if (t == nn.getFirstToken()) {
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
    public List<Token> getLeadingTokens() {
        List<Token> list = new ArrayList<Token>();
        
        if (node.jjtGetNumChildren() == 0) {
            return list;
        }

        AbstractJavaNode n = (AbstractJavaNode)node.jjtGetChild(0);

        Token t = new Token();
        t.next = getFirstToken();
            
        while (true) {
            t = t.next;
            Node<AbstractJavaNode> nn = Node.of(n);
            if (t == nn.getFirstToken()) {
                break;
            }
            else {
                list.add(t);
            }
        }

        return list;
    }

    public void print() {
        print("");
    }

    public void print(String prefix) {
        Token first = getFirstToken();
        Token last  = getLastToken();
        tr.Ace.log(prefix + node.toString() + ":" + TokenUtil.getLocation(first, last));
    }

    public void dump() {
        dump("", false);
    }

    public void dump(String prefix) {
        dump(prefix, false);
    }

    public void dump(String prefix, boolean showWhitespace) {
        print(prefix);

        List<Object> children = getChildren();
        for (Object obj : children) {
            dumpObject(obj, prefix, showWhitespace);
        }
    }  

    public void dumpObject(Object obj, String prefix, boolean showWhitespace) {
        if (obj instanceof Token) {
            Token tk = (Token)obj;                
            if (showWhitespace && tk.specialToken != null) {
                TokenUtil.dumpToken(tk.specialToken, prefix);
            }                
            tr.Ace.log(prefix + "    \"" + tk + "\" " + TokenUtil.getLocation(tk, tk) + " (" + tk.kind + ")");
        }
        else {
            AbstractJavaNode sn = (AbstractJavaNode)obj;
            Node<AbstractJavaNode> nn = new Node<AbstractJavaNode>(sn);
            nn.dump(prefix + "    ", showWhitespace);
        }
    }

    /**
     * Returns a numeric "level" for the node. Zero is public or abstract, one is protected, two is
     * package, and three is private.
     */
    public int getLevel() {
        List<Token> tokens = getLeadingTokens();
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

    protected static final IntegerList ACCESSES = new IntegerList(JavaParserConstants.PUBLIC,
                                                                  JavaParserConstants.PROTECTED,
                                                                  JavaParserConstants.PRIVATE);
    
    /**
     * Returns the access type, as a token.
     */
    public Token getAccess() {
        for (Integer access : ACCESSES) {
            Token tk  = getLeadingToken(access);
            if (tk != null) {
                return tk;
            }
        }
        return null;
    }    

    private String getClassName(Class<?> cls) {
        return cls == null ? null : cls.getName();
    }

    /**
     * Returns the node if the class of the child at the given index matches the given class type.
     * If the given one is null, the child will match.
     */
    @SuppressWarnings("unchecked")
    private <NodeType extends AbstractJavaNode> NodeType getChildOfType(Class<NodeType> childType, int index) {
        AbstractJavaNode child = (AbstractJavaNode)node.jjtGetChild(index);
        return childType == null || child.getClass().equals(childType) ? (NodeType)child : null;
    }

    protected ASTNode astNode() {
        return node;
    }
}
