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

public class MatchTest extends Parameterized {
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void init(int expected, int score) {
        Match match = new Match(score);
        int result = match.score();
        assertThat(result, withContext(message("score", score), equalTo(expected)));
    }
    
    private List<Object[]> parametersForInit() {
        return paramsList(
            params(12, 12),
            params(23, 23));
    }
}
