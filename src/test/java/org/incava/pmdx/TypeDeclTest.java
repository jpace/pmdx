package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;
import static org.incava.pmdx.CompilationUnitTest.compile;

public class TypeDeclTest extends Parameterized {
    public TypeDecl getFirst(String str) {
        ASTCompilationUnit acu = compile(str);
        CompilationUnit cu = new CompilationUnit(acu);
        List<ASTTypeDeclaration> decls = cu.getTypeDeclarations();
        return new TypeDecl(decls.get(0));
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getName(String expected, String str) {
        TypeDecl td = getFirst(str);
        Token name = td.getName();
        assertThat(name.image, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params("C",  "class C {}"),
            params("D",  "class D {}")
                          );
    }
}
