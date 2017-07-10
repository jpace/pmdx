package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.StringList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class NameListTest extends Parameterized {
    public NameList getFirst(String str) {
        Ctor ctor = new CtorTest().getFirst(str);
        ASTNameList names = ctor.getThrowsList();        
        return new NameList(names);
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getName(StringList expected, String str) {
        NameList names = getFirst(str);
        for (int idx = 0; idx < expected.size(); ++idx) {
            String name = names.getName(idx);
            assertThat(name, withContext(message("str", str, "idx", idx), equalTo(expected.get(idx))));
        }

        assertThat(names.getName(expected.size()), nullValue());
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params(StringList.of("Ex"),  "public class C { public C() throws Ex { } }"),
            params(StringList.of("Ex", "Fy"),  "public class C { public C() throws Ex, Fy { } }")
                          );
    }

    @Test @Parameters(method="parametersForGetName") @TestCaseName("{method} {index} {params}")
    public void getNameNode(StringList expected, String str) {
        NameList names = getFirst(str);
        for (int idx = 0; idx < expected.size(); ++idx) {
            ASTName name = names.getNameNode(idx);
            assertThat(Node.of(name).toString(), withContext(message("str", str, "idx", idx), equalTo(expected.get(idx))));
        }

        assertThat(names.getName(expected.size()), nullValue());
    }
}
