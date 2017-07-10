package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.Array;
import org.incava.ijdk.collect.StringList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class VariablesTest extends Parameterized {
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getNames(StringList expected, String str) {
        Field field = new FieldTest().getFirst(str);
        List<ASTVariableDeclarator> vds = field.getVariableDeclarators();
        Variables vars = new Variables(vds);
        
        for (int idx = 0; idx < expected.size(); ++idx) {
            Variable var = new Variable(vars.get(idx));
            assertThat(var.getName().image, withContext(message("str", str, "idx", idx), equalTo(expected.get(idx))));
        }

        assertThat(vars, hasSize(expected.size()));
    }
    
    private List<Object[]> parametersForGetNames() {
        return paramsList(
            params(StringList.of("x"),       "public class C { int x; }"),
            params(StringList.of("x", "y"),  "public class C { char x, y; }")
                          );
    }
}
