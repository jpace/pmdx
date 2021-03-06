package org.incava.pmdx;

import java.util.List;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.StringList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class ParametersTest extends Parameterized {
    public Parameters getFirst(String str) {
        Ctor ctor = new CtorTest().getFirst(str);
        return new Parameters(ctor.findChild(ASTFormalParameters.class));
    }
    
    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void getName(int expected, String str) {
        String clsStr = "class C { " + str + " { } }";
        Parameters ps = getFirst(clsStr);
        List<ASTFormalParameter> params = ps.getParameters();
        assertThat(params, hasSize(expected));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params(0, "C()"),
            params(1, "C(int x)"),
            params(2, "C(int x, char y)")
                          );
    }
    
    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void getTypes(StringList expected, String str) {
        String clsStr = "class C { " + str + " { } }";
        Parameters ps = getFirst(clsStr);
        List<String> params = ps.getTypes();
        assertThat(params, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetTypes() {
        return paramsList(
            params(StringList.empty(), "C()"),
            params(StringList.of("int"), "C(int x)"),
            params(StringList.of("int", "char"), "C(int x, char y)")
                          );
    }
}
