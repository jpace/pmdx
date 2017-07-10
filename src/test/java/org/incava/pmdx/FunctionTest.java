package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
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
import static org.incava.pmdx.CompilationUnitTest.compile;

public class FunctionTest extends Parameterized {
    public Function getFirst(String str) {
        ClassNode cls = new ClassNodeTest().getFirst(str);
        
        ASTClassOrInterfaceBody body = cls.findChild(ASTClassOrInterfaceBody.class);
        ASTClassOrInterfaceBodyDeclaration decl = Node.of(body).findChild(ASTClassOrInterfaceBodyDeclaration.class);
        ASTConstructorDeclaration ctor = Node.of(decl).findChild(ASTConstructorDeclaration.class);
        
        return new Function(ctor);
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getThrows(boolean expected, String str) {
        Function cn = getFirst(str);
        Token tk = cn.getThrows();
        assertThat(tk != null, withContext(message("str", str, "tk", tk), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetThrows() {
        return paramsList(
            params(false, "public class C { public C() { } }"),
            params(true,  "public class C { public C() throws Ex { } }")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getThrowsList(boolean expected, String str) {
        Function cn = getFirst(str);
        ASTNameList names = cn.getThrowsList();
        assertThat(names != null, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetThrowsList() {
        return paramsList(
            params(false,  "public class C { public C() { } }"),
            params(true,   "public class C { public C() throws Ex { } }")
                          );
    }
}
