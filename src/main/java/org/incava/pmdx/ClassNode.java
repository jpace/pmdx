package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * A class or interface.
 */
public class ClassNode extends Node<ASTClassOrInterfaceDeclaration> {
    public ClassNode(ASTClassOrInterfaceDeclaration node) {
        super(node);
    }
    
    public Token getNameToken() {
        return findToken(JavaParserConstants.IDENTIFIER);
    }
    
    public String getName() {
        return getNameToken().image;
    }

    public double getMatchScore(ClassNode other) {
        return getName().equals(other.getName()) ? 1.0 : 0.0;
    }

    public Match match(ClassNode other) {
        return new Match(getName().equals(other.getName()) ? 1.0 : 0.0);
    }
}
