package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.StringList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;
import static org.incava.pmdx.CompilationUnitTest.compile;

public class TokenUtilTest extends Parameterized {
    public Function getFirst(String str) {
        return new FunctionTest().getFirst(str); 
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getLocationOne(String expected, String str) {
        Function cn = getFirst(str);
        Token tk = cn.getFirstToken();
        assertThat(TokenUtil.getLocation(tk), withContext(message("str", str, "tk", tk), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetLocationOne() {
        return paramsList(
            params("1:25",  "public class C { public C() { } }"),
            params("1:26",  "public class C1 { public C() { } }")
                          );
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getLocationTwo(String expected, String str) {
        Function cn = getFirst(str);
        Token t1 = cn.getFirstToken();
        Token t2 = cn.getLastToken();
        assertThat(TokenUtil.getLocation(t1, t2), withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetLocationTwo() {
        return paramsList(
            params("[1:25:2:1]", "public class C { public C() {\n} }"),
            params("[1:26:3:5]", "public class C1 { public C() {\n\n    } }")
                          );
    }
}
