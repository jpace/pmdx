package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;

/**
 * Miscellaneous routines for compilation units.
 */
public class CompilationUnit extends Node<ASTCompilationUnit> {
    public static CompilationUnit of(ASTCompilationUnit acu) {
        return new CompilationUnit(acu);
    }
    
    public CompilationUnit(ASTCompilationUnit cu) {
        super(cu);
    }

    public ASTPackageDeclaration getPackage() {
        return findChild(ASTPackageDeclaration.class);
    }

    public List<ASTImportDeclaration> getImports() {
        return findChildren(ASTImportDeclaration.class);
    }

    public List<ASTTypeDeclaration> getTypeDeclarations() {
        return findChildren(ASTTypeDeclaration.class);
    }
}
