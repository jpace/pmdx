package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.StringList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class MethodTest extends Parameterized {
    public Method getFirst(String str) {
        ClassNode cls = new ClassNodeTest().getFirst(str);
        ASTClassOrInterfaceBody body = cls.findChild(ASTClassOrInterfaceBody.class);
        ASTClassOrInterfaceBodyDeclaration decl = Node.of(body).findChild(ASTClassOrInterfaceBodyDeclaration.class);
        ASTMethodDeclaration md = Node.of(decl).findChild(ASTMethodDeclaration.class);
        
        return new Method(md);
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getName(String expected, String str) {
        String clsStr = "public class C { " + str + " { } }";
        Method cn = getFirst(clsStr);
        Token tk = cn.getName();
        assertThat(tk.image, withContext(message("str", str, "tk", tk), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params("a", "void a()"),
            params("b", "void b()")
                          );
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getParameters(boolean expected, String str) {
        String clsStr = "public class C { " + str + " { } }";
        Method m = getFirst(clsStr);
        ASTFormalParameters fp = m.getParameters();
        assertThat(fp != null, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetParameters() {
        return paramsList(
            params(true, "void a()"),
            params(true, "void a(int i)")
                          );
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getFullName(String expected, String str) {
        String clsStr = "public class C { " + str + " { } }";
        Method m = getFirst(clsStr);
        String fn = m.getFullName();
        assertThat(fn, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetFullName() {
        return paramsList(
            params("a()",          "void a()"),
            params("b()",          "void b()"),
            params("a(int)",       "void a(int x)"),
            params("a(char)",      "void a(char y)"),
            params("a(int, char)", "void a(int x, char y)"),
            params("a(int[])",     "void a(int[] x)")
                          );
    }
}
