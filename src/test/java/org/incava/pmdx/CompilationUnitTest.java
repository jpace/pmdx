package org.incava.pmdx;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.ast.JavaCharStream;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.JavaParser;
import org.incava.attest.Parameterized;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class CompilationUnitTest extends Parameterized {
    public static ASTCompilationUnit compile(String str) {
        try {
            Reader reader = new StringReader(str);
            JavaCharStream jcs = new JavaCharStream(reader);
            JavaParser parser = new JavaParser(jcs);

            // 1.6:
            parser.setJdkVersion(6);

            return parser.CompilationUnit();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getPackage(boolean expected, String str) {
        ASTCompilationUnit acu = compile(str);
        CompilationUnit cu = new CompilationUnit(acu);
        ASTPackageDeclaration result = cu.getPackage();
        assertThat(result != null, withContext(message("str", str), equalTo(expected)));
    }
    
    private List<Object[]> parametersForGetPackage() {
        return paramsList(
            params(true,  "package abc;\nclass C {}\nclass D {}"),
            params(false, "class C {}\nclass D {}")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getImports(int expected, String str) {
        ASTCompilationUnit acu = compile(str);
        CompilationUnit cu = new CompilationUnit(acu);
        List<ASTImportDeclaration> result = cu.getImports();
        assertThat(result, withContext(message("str", str), hasSize(expected)));
    }
    
    private List<Object[]> parametersForGetImports() {
        return paramsList(
            params(0, ""),
            params(1, "import a.B;"),
            params(2, "import a.B;\nimport c.D;"),
            params(1, "import a.B;\n class C {}")
                          );
    }

    @Test @Parameters @TestCaseName("{method} {index} {params}")
    public void getTypeDeclarations(int expected, String str) {
        ASTCompilationUnit acu = compile(str);
        CompilationUnit cu = new CompilationUnit(acu);
        List<ASTTypeDeclaration> result = cu.getTypeDeclarations();
        assertThat(result, withContext(message("str", str), hasSize(expected)));
    }
    
    private List<Object[]> parametersForGetTypeDeclarations() {
        return paramsList(
            params(0, ""),
            params(1, "class C {}"),
            params(2, "class C {}\nclass D {}")
                          );
    }
}
