package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class CtorTest extends Parameterized {
    public Ctor getFirst(String str) {
        ClassNode cls = new ClassNodeTest().getFirst(str);
        ASTClassOrInterfaceBody body = cls.findChild(ASTClassOrInterfaceBody.class);
        ASTClassOrInterfaceBodyDeclaration decl = Node.of(body).findChild(ASTClassOrInterfaceBodyDeclaration.class);
        ASTConstructorDeclaration ctor = Node.of(decl).findChild(ASTConstructorDeclaration.class);
        
        return new Ctor(ctor);
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getName(String expected, String str) {
        Ctor cn = getFirst(str);
        Token tk = cn.getName();
        assertThat(tk.image, withContext(message("str", str, "tk", tk), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params("C", "class C { C() { } }"),
            params("D", "class D { D() { } }")
                          );
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getFullName(String expected, String str) {
        String clsStr = "class C { " + str + " { } }";
        Ctor cn = getFirst(clsStr);
        String fn = cn.getFullName();
        assertThat(fn, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetFullName() {
        return paramsList(
            params("C()", "C()"),
            params("C(int)", "C(int x)"),
            params("C(char)", "C(char y)"),
            params("C(int, char)", "C(int x, char y)"),
            params("C(int[])", "C(int[] x)")
                          );
    }
}
