package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;
import static org.incava.pmdx.CompilationUnitTest.compile;

public class ClassNodeTest extends Parameterized {
    public ClassNode getFirst(String str) {
        ASTCompilationUnit acu = compile(str);
        CompilationUnit cu = new CompilationUnit(acu);
        List<ASTTypeDeclaration> decls = cu.getTypeDeclarations();
        
        Node<ASTTypeDeclaration> typeDecl = Node.of(decls.get(0));
        ASTClassOrInterfaceDeclaration coid = typeDecl.findChild(ASTClassOrInterfaceDeclaration.class);
        return new ClassNode(coid);
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getName(String expected, String str) {
        ClassNode cn = getFirst(str);
        String name = cn.getName();
        assertThat(name, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params("C",  "class C {}"),
            params("D",  "class D {}")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getMatch(double expected, String xStr, String yStr) {
        ClassNode x = getFirst(xStr);
        ClassNode y = getFirst(yStr);

        Match match = x.match(y);

        assertThat(match.score(), withContext(message("xStr", xStr, "yStr", yStr), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetMatch() {
        return paramsList(
            params(1.0, "class C {}", "class C {}"),
            params(0.0, "class C {}", "class D {}")
                          );
    }    
}