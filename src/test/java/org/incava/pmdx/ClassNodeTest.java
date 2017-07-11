package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.incava.attest.Parameterized;
import org.incava.ijdk.collect.Array;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class ClassNodeTest extends Parameterized {
    public ClassNode getFirst(String str) {
        TypeDecl td = new TypeDeclTest().getFirst(str);
        return new ClassNode(td.getType());
    }
    
    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getName(String expected, String str) {
        ClassNode cn = getFirst(str);
        String name = cn.getName();
        assertThat(name, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetName() {
        return paramsList(
            params("C",  "class C {}"),
            params("D",  "class D {}")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getMatch(int expected, String xStr, String yStr) {
        ClassNode x = getFirst(xStr);
        ClassNode y = getFirst(yStr);

        Match match = x.match(y);

        assertThat(match.score(), withContext(message("xStr", xStr, "yStr", yStr), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetMatch() {
        return paramsList(
            params(100, "class C {}", "class C {}"),
            params(0,   "class C {}", "class D {}")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getField(boolean expected, String str, int idx) {
        ClassNode cn = getFirst(str);
        Field result = cn.getField(idx);
        assertThat(result != null, withContext(message("result", result, "str", str, "idx", idx), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetField() {
        return paramsList(
            params(true,  "class C { int i; }", 0),
            params(false, "class C { }", 0),
            params(false, "class C { int i; }", 1),
            
            params(true,  "class C { int i; char j; }", 0),
            params(true,  "class C { int i; char j; }", 1),
            params(true,  "class C { int i; C() { } char j; }", 1),
            params(false, "class C { C() { } }", 0),
            params(false, "class C { int i; char j; }", 2)
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getFields(int expected, String str) {
        ClassNode cn = getFirst(str);
        Array<Field> result = cn.getFields();
        assertThat(result, withContext(message("result", result, "str", str), hasSize(expected)));
    }
    
    private List<Object[]> parametersForGetFields() {
        return paramsList(
            params(1, "class C { int i; }"),
            params(0, "class C { }"),
            params(2, "class C { int i; char j; }"),
            params(2, "class C { int i; C() { } char j; }"),
            params(0, "class C { C() { } }")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getMethod(boolean expected, String str, int idx) {
        ClassNode cn = getFirst(str);
        Method result = cn.getMethod(idx);
        assertThat(result != null, withContext(message("result", result, "str", str, "idx", idx), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetMethod() {
        return paramsList(
            params(false, "class C { int i; }", 0),
            params(false, "class C { }", 0),
            params(true,  "class C { void a() { } }", 0),
            params(false, "class C { void a() { } }", 1),
            params(true,  "class C { int i; void a() { } }", 0),
            params(false, "class C { int i; void a() { } }", 1),
            params(true,  "class C { void a() { } char j; }", 0),
            params(false, "class C { void a() { } char j; }", 1),
            params(true,  "class C { void a() { } void b() { }; }", 0),
            params(true,  "class C { void a() { } void b() { }; }", 1),
            params(false, "class C { void a() { } void b() { }; }", 2)
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getMethods(int expected, String str) {
        ClassNode cn = getFirst(str);
        Array<Method> result = cn.getMethods();
        assertThat(result, withContext(message("result", result, "str", str), hasSize(expected)));
    }
    
    private List<Object[]> parametersForGetMethods() {
        return paramsList(
            params(0, "class C { int i; }"),
            params(0, "class C { }"),
            params(1, "class C { void a() { } }"),
            params(1, "class C { int i; void a() { } }"),
            params(1, "class C { void a() { } char j; }"),
            params(2, "class C { void a() { } void b() { }; }")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getCtors(int expected, String str) {
        ClassNode cn = getFirst(str);
        Array<Ctor> result = cn.getCtors();
        assertThat(result, withContext(message("result", result, "str", str), hasSize(expected)));
    }
    
    private List<Object[]> parametersForGetCtors() {
        return paramsList(
            params(1, "class C { C() { } }"),
            params(0, "class C { }"),
            params(1, "class C { C() { } void a() { } }"),
            params(2, "class C { C() { } C(int i) { } }")
                          );
    }
}
