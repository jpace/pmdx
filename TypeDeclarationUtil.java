package org.incava.pmdx;

import java.util.List;
import java.util.Map;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.ast.SimpleNode;
import net.sourceforge.pmd.ast.Token;

/**
 * Miscellaneous routines for type declarations.
 */
public class TypeDeclarationUtil extends SimpleNodeUtil {
    public static Token getName(ASTTypeDeclaration typeDecl) {
        ASTClassOrInterfaceDeclaration cidecl = findChild(typeDecl, ASTClassOrInterfaceDeclaration.class);
        return cidecl == null ? null : cidecl.getFirstToken().next;
    }

    public static ASTClassOrInterfaceDeclaration getType(ASTTypeDeclaration typeDecl) {
        return findChild(typeDecl, ASTClassOrInterfaceDeclaration.class);
    }

    public static ASTTypeDeclaration findTypeDeclaration(String name, List<ASTTypeDeclaration> types) {
        for (ASTTypeDeclaration type : types) {
            Token otherName = getName(type);

            if ((otherName == null && name == null) ||
                (otherName != null && otherName.image.equals(name))) {
                return type;
            }
        }

        return null;
    }

    /**
     * Returns a list of all methods, fields, constructors, and inner classes
     * and interfaces.
     */
    public static List<ASTClassOrInterfaceBodyDeclaration> getDeclarations(ASTTypeDeclaration tdecl) {
        ASTClassOrInterfaceDeclaration cidecl = findChild(tdecl, ASTClassOrInterfaceDeclaration.class);
        return getDeclarations(cidecl);
    }

    /**
     * Returns a list of all methods, fields, constructors, and inner classes
     * and interfaces.
     */
    public static List<ASTClassOrInterfaceBodyDeclaration> getDeclarations(ASTClassOrInterfaceDeclaration coid) {
        ASTClassOrInterfaceBody body = findChild(coid, ASTClassOrInterfaceBody.class);
        return snatchChildren(body, ASTClassOrInterfaceBodyDeclaration.class);
    }

    /**
     * Returns the real declaration, which is a method, field, constructor, or
     * inner class or interface.
     */
    public static SimpleNode getDeclaration(ASTClassOrInterfaceBodyDeclaration bdecl) {
        return hasChildren(bdecl) ? findChild(bdecl) : null;
    }

    /**
     * Returns the real declaration, which is a method, field, constructor, or
     * inner class or interface.
     */
    public static <NodeType extends SimpleNode> NodeType getDeclaration(ASTClassOrInterfaceBodyDeclaration bdecl, Class<NodeType> cls) {
        return hasChildren(bdecl) ? findChild(bdecl, cls) : null;
    }
}
