package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.incava.attest.Parameterized;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;
import static org.incava.pmdx.CompilationUnitTest.compile;

public class MatchTest extends Parameterized {
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void init(double expected, double score) {
        Match match = new Match(score);
        double result = match.score();
        assertThat(result, withContext(message("score", score), equalTo(expected)));
    }
    
    private List<Object[]> parametersForInit() {
        return paramsList(
            params(1.2, 1.2),
            params(2.3, 2.3));
    }
}
