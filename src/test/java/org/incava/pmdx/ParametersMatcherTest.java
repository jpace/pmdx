package org.incava.pmdx;

import java.util.List;
import junitparams.naming.TestCaseName;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class ParametersMatcherTest extends Parameterized {
    @Test @junitparams.Parameters @TestCaseName("{method} {index} {params}")
    public void test(int expected, String xs, String ys) {
        Parameters x = new ParametersTest().getFirst("class C { C(" + xs + ") { } }");
        Parameters y = new ParametersTest().getFirst("class C { C(" + ys + ") { } }");
        ParametersMatcher pm = new ParametersMatcher(x, y);
        Match m = pm.getMatch();
        assertThat(m.score(), withContext(message("xs", xs, "ys", ys), equalTo(expected)));
    }
    
    private List<Object[]> parametersForTest() {
        return paramsList(
            params(100, "",                ""),
            params(100, "I x",             "I x"),
            params(100, "C x",             "C x"),
            params(100, "I x",             "I y"),
            
            params(50,  "I x",             ""),
            
            params(100, "I x, C y",        "I x, C y"),
            params(100, "I x, C y",        "I y, C x"),
            params(75,  "I x, C y",        "C x, I y"),
            
            params(100, "I[] x, D y, S z", "I[] x, D y, S z"),
            params(83,  "I[] x, D y, S z", "D y, I[] x, S z"),
            params(74,  "I[] x, D y, S z", "I[] x, S z"),

            // 1 of 3 match, not exact:
            params(58,  "I[] x, D y, S z", "S z"),
            params(58,  "S z",             "I[] x, D y, S z"),            
            params(58,  "I[] x, D y, S z", "D y"),

            // one exact match
            params(66,  "I[] x, D y, S z", "I[] x")
                          );
    }
}
