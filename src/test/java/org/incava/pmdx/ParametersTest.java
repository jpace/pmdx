package org.incava.pmdx;

import java.util.List;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.StringList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;
import static org.incava.pmdx.CompilationUnitTest.compile;

public class ParametersTest extends Parameterized {
    public Parameters getFirst(String str) {
        Ctor ctor = new CtorTest().getFirst(str);
        return new Parameters(ctor.findChild(ASTFormalParameters.class));
    }
    
    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void getName(int expected, String str) {
        Parameters ps = getFirst(str);
        List<ASTFormalParameter> params = ps.getParameters();
        assertThat(params, hasSize(expected));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params(0, "class C { C() { } }"),
            params(1, "class C { C(int x) { } }"),
            params(2, "class C { C(int x, char y) { } }")
                          );
    }
}
