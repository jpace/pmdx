package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.StringList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class FieldTest extends Parameterized {
    public Field getFirst(String str) {
        ClassNode cls = new ClassNodeTest().getFirst(str);
        return cls.getField(0);
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getNames(String expected, String str) {
        Field f = getFirst("class C { " + str + "; }");
        String result = f.getNames();
        assertThat(result, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetNames() {
        return paramsList(
            params("x",    "int x"),
            params("x, y", "int x, y"),
            params("x, y", "int x,    y")
                          );
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getNameList(List<String> expected, String str) {
        Field f = getFirst("class C { " + str + "; }");
        List<String> result = f.getNameList();
        assertThat(result, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetNameList() {
        return paramsList(
            params(StringList.of("x"),      "int x"),
            params(StringList.of("x", "y"), "int x, y"),
            params(StringList.of("x", "y"), "int x,    y")
                          );
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getType(String expected, String str) {
        Field x = getFirst("class C { " + str + "; }");
        Node<ASTType> type = x.getType();
        assertThat(type, withContext(message("str", str), hasToString(expected)));
    }
    
    private List<Object[]> parametersForGetType() {
        return paramsList(
            params("int",    "int x"),
            params("char",   "char x")
                          );
    }    
}
