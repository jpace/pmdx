package org.incava.pmdx;

import java.util.List;
import junitparams.naming.TestCaseName;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class FieldMatcherTest extends Parameterized {
    @Test @junitparams.Parameters @TestCaseName("{field} {index} {params}")
    public void test(int expected, String xs, String ys) {
        Field        x  = new FieldTest().getFirst("public class C { " + xs + "; }");
        Field        y  = new FieldTest().getFirst("public class C { " + ys + "; }");
        FieldMatcher mm = new FieldMatcher(x, y);
        Match         m  = mm.getMatch();        
        assertThat(m.score(), withContext(message("xs", xs, "ys", ys), equalTo(expected)));
    }
    
    private List<Object[]> parametersForTest() {
        return paramsList(
            params(100, "int f",    "int f"),
            params(75,  "int f",    "int f, g"),
            params(75,  "int f, g", "int f"),
            params(66,  "int f",    "int f, g, h"),
            params(83,  "int f, g", "int f, g, h"),
            params(50,  "int f",    "char f"),
            params(25,  "int f",    "char f, g"),
            params(0,   "int f",    "char g")
                          );
    }
}
