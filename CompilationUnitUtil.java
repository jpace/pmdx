package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTImportDeclaration;
import net.sourceforge.pmd.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.ast.ASTTypeDeclaration;

/**
 * Miscellaneous routines for compilation units.
 */
public class CompilationUnitUtil {
    public static ASTPackageDeclaration getPackage(ASTCompilationUnit cu) {
        return SimpleNodeUtil.findChild(cu, ASTPackageDeclaration.class);
    }

    public static List<ASTImportDeclaration> getImports(ASTCompilationUnit cu) {
        return SimpleNodeUtil.snatchChildren(cu, "net.sourceforge.pmd.ast.ASTImportDeclaration");
    }

    public static List<ASTTypeDeclaration> getTypeDeclarations(ASTCompilationUnit cu) {
        return SimpleNodeUtil.snatchChildren(cu, "net.sourceforge.pmd.ast.ASTTypeDeclaration");
    }
}
