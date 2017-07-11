package org.incava.pmdx;

import java.util.List;
import junitparams.naming.TestCaseName;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class CtorMatcherTest extends Parameterized {
    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void test(int expected, String xs, String ys) {
        Ctor x = new CtorTest().getFirst("public class C { " + xs + " { } }");
        Ctor y = new CtorTest().getFirst("public class C { " + ys + " { } }");
        CtorMatcher matcher = new CtorMatcher(x, y);
        Match m = matcher.getMatch();        
        assertThat(m.score(), withContext(message("xs", xs, "ys", ys), equalTo(expected)));
    }
    
    private List<Object[]> parametersForTest() {
        return paramsList(
            params(100, "C()", "C()"),
            params(50,  "C()", "C(int x)"),
            params(50,  "C(int x)", "C()")
                          );
    }    
}
