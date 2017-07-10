package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.collect.Array;

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
        return match(other).score();
    }

    public Match match(ClassNode other) {
        return new Match(getName().equals(other.getName()) ? 1.0 : 0.0);
    }

    public Field getField(int idx) {
        ASTClassOrInterfaceBody body = findChild(ASTClassOrInterfaceBody.class);
        List<ASTClassOrInterfaceBodyDeclaration> decls = Node.of(body).findChildren(ASTClassOrInterfaceBodyDeclaration.class);
        int matched = -1;
        for (ASTClassOrInterfaceBodyDeclaration decl : decls) {
            ASTFieldDeclaration fd = Node.of(decl).findChild(ASTFieldDeclaration.class);
            if (fd != null && ++matched == idx) {
                return new Field(fd);
            }
        }
        return null;
    }

    public Array<Field> getFields() {
        Array<Field> fields = Array.empty();
        ASTClassOrInterfaceBody body = findChild(ASTClassOrInterfaceBody.class);
        List<ASTClassOrInterfaceBodyDeclaration> decls = Node.of(body).findChildren(ASTClassOrInterfaceBodyDeclaration.class);
        for (ASTClassOrInterfaceBodyDeclaration decl : decls) {
            ASTFieldDeclaration fd = Node.of(decl).findChild(ASTFieldDeclaration.class);
            if (fd != null) {
                fields.add(new Field(fd));
            }
        }
        return fields;
    }
}
