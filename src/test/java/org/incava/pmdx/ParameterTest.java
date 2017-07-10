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

public class ParameterTest extends Parameterized {
    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void getName(StringList expected, String str) {
        String clsStr = "class C { C(" + str + ") { } }";
        Parameters ps = new ParametersTest().getFirst(clsStr);
        List<ASTFormalParameter> params = ps.getParameters();

        for (int idx = 0; idx < expected.size(); ++idx) {
            Parameter p = new Parameter(params.get(idx));
            assertThat(p.getName().image, withContext(message("str", str, "idx", idx), equalTo(expected.get(idx))));
        }

        assertThat(params, hasSize(expected.size()));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params(StringList.empty(),      ""),
            params(StringList.of("x"),      "int x"),
            params(StringList.of("x", "y"), "int x, char y")
                          );
    }

    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void getType(String expected, String str) {
        String clsStr = "class C { C(" + str + ") { } }";
        Parameters ps = new ParametersTest().getFirst(clsStr);
        List<ASTFormalParameter> params = ps.getParameters();
        Parameter p = new Parameter(params.get(0));
        assertThat(p.getType(), withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetType() {
        return paramsList(
            params("int",     "int x"),
            params("char",    "char y"),
            params("int[]",   "int[] x"),
            params("char[]" , "char []y"),
            params("char[]" , "char y[]")
                          );
    }
    
}
