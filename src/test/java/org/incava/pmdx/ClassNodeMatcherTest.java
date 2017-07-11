package org.incava.pmdx;

import java.util.List;
import junitparams.naming.TestCaseName;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class ClassNodeMatcherTest extends Parameterized {
    @Test @junitparams.Parameters @TestCaseName("{field} {index} {params}")
    public void test(int expected, String xs, String ys) {
        ClassNode        x  = new ClassNodeTest().getFirst(xs);
        ClassNode        y  = new ClassNodeTest().getFirst(ys);
        ClassNodeMatcher mm = new ClassNodeMatcher(x, y);
        Match         m  = mm.getMatch();
        assertThat(m.score(), withContext(message("xs", xs, "ys", ys), equalTo(expected)));
    }
    
    private List<Object[]> parametersForTest() {
        return paramsList(
            params(100, "class C {}", "class C {}"),
            params(0,   "class C {}", "class D {}")
                          );
    }
}
