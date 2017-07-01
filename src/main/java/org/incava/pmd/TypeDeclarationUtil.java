package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.SimpleNode;
import net.sourceforge.pmd.lang.java.ast.Token;

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
}
