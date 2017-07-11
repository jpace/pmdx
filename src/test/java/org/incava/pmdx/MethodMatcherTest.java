package org.incava.pmdx;

import java.util.List;
import junitparams.naming.TestCaseName;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class MethodMatcherTest extends Parameterized {
    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void test(int expected, String xs, String ys) {
        Method        x  = new MethodTest().getFirst("public class C { " + xs + " { } }");
        Method        y  = new MethodTest().getFirst("public class C { " + ys + " { } }");
        MethodMatcher mm = new MethodMatcher(x, y);
        Match         m  = mm.getMatch();        
        assertThat(m.score(), withContext(message("xs", xs, "ys", ys), equalTo(expected)));
    }
    
    private List<Object[]> parametersForTest() {
        return paramsList(
            params(100, "void a()", "void a()"),
            params(0,   "void a()", "void b()"),
            // the return type doesn't matter:
            params(100, "void a()", "int a()"),
            params(100, "void a()", "void a(int i)")
                          );
    }    
}
