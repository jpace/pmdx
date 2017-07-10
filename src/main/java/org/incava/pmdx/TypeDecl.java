package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * Miscellaneous routines for type declarations.
 */
public class TypeDecl extends Node<ASTTypeDeclaration> {
    public TypeDecl(ASTTypeDeclaration td) {
        super(td);
    }
    
    public Token getName() {
        ASTClassOrInterfaceDeclaration cidecl = findChild(ASTClassOrInterfaceDeclaration.class);
        return cidecl == null ? null : Node.of(cidecl).getFirstToken().next;
    }

    public ASTClassOrInterfaceDeclaration getType() {
        return findChild(ASTClassOrInterfaceDeclaration.class);
    }
}
