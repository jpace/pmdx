package org.incava.pmdx;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.ast.JavaCharStream;
import net.sourceforge.pmd.lang.ast.TokenMgrError;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.JavaParser;
import net.sourceforge.pmd.lang.java.ast.ParseException;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.assertEqual;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;

public class SimpleNodeUtilTest extends Parameterized {
    private static ASTCompilationUnit cu;

    @BeforeClass
    public static void setup() throws Exception {
        tr.Ace.setVerbose(true);
        
        cu = compile("package abc;\nclass C1 {}");
        tr.Ace.log("cu", cu);

        Token tk = SimpleNodeUtil.getFirstToken(cu);

        while (tk != null) {
            tr.Ace.log("tk", tk);
            tr.Ace.log("tk.kind", tk.kind);

            tk = tk.next;
        }
    }

    private static ASTCompilationUnit compile(String str) throws Exception {
        Reader reader = new StringReader(str);
        JavaCharStream jcs = new JavaCharStream(reader);
        JavaParser parser = new JavaParser(jcs);

        // 1.6:
        parser.setJdkVersion(6);

        return parser.CompilationUnit();
    }
    
    @Test
    public void getFirstToken() {
        Token tk = SimpleNodeUtil.getFirstToken(cu);

        assertThat(tk.toString(), equalTo("package"));
        assertThat(tk.kind, equalTo(43));
    }
    
    @Test
    public void getLastToken() {
        Token tk = SimpleNodeUtil.getLastToken(cu);

        // a compilation unit has two EOFs?

        assertThat(tk.toString(), equalTo(""));
        assertThat(tk.kind, equalTo(0));
    }
    
    @Test
    public void hasChildren() {
        boolean result = SimpleNodeUtil.hasChildren(cu);
        assertThat(result, equalTo(true));
    }
    
    @Test
    public void getParent() {
        AbstractJavaNode parent = SimpleNodeUtil.getParent(cu);
        assertThat(parent, nullValue());
    }
    
    @Test
    public void getChildren() {
        List<Object> children = SimpleNodeUtil.getChildren(cu);

        assertThat(children, hasSize(3));
        assertThat(children.get(0), instanceOf(ASTPackageDeclaration.class));
        assertThat(children.get(1), instanceOf(ASTTypeDeclaration.class));
        assertThat(children.get(2), instanceOf(Token.class));        
    }
    
    @Test
    public void getChildrenNodes() {
        List<Object> children = SimpleNodeUtil.getChildren(cu, true, false);

        assertThat(children, hasSize(2));
        assertThat(children.get(0), instanceOf(ASTPackageDeclaration.class));
        assertThat(children.get(1), instanceOf(ASTTypeDeclaration.class));
    }
    
    @Test
    public void getChildrenTokens() {
        List<Object> children = SimpleNodeUtil.getChildren(cu, false, true);

        assertThat(children, hasSize(1));
        assertThat(children.get(0), instanceOf(Token.class));        
    }
    
    @Test
    public void getChildrenNodesTokens() {
        List<Object> children = SimpleNodeUtil.getChildren(cu, true, true);

        assertThat(children, hasSize(3));
        assertThat(children.get(0), instanceOf(ASTPackageDeclaration.class));
        assertThat(children.get(1), instanceOf(ASTTypeDeclaration.class));
        assertThat(children.get(2), instanceOf(Token.class));        
    }
    
    @Test
    public void getChildrenNeither() {
        List<Object> children = SimpleNodeUtil.getChildren(cu, false, false);
        assertThat(children, empty());
    }    
    
    @Test
    public void findChildNoArgs() {
        AbstractJavaNode child = SimpleNodeUtil.findChild(cu);
        assertThat(child, instanceOf(ASTPackageDeclaration.class));
    }    
    
    @Test
    public void findChildOfClass() {
        AbstractJavaNode pkg = SimpleNodeUtil.findChild(cu, ASTPackageDeclaration.class);
        assertThat(pkg, instanceOf(ASTPackageDeclaration.class));

        AbstractJavaNode type = SimpleNodeUtil.findChild(cu, ASTTypeDeclaration.class);
        assertThat(type, instanceOf(ASTTypeDeclaration.class));        
    }
    
    @Test
    public void findChildOfClassOfCount() {
        AbstractJavaNode pkg0 = SimpleNodeUtil.findChild(cu, ASTPackageDeclaration.class, 0);
        assertThat(pkg0, instanceOf(ASTPackageDeclaration.class));

        AbstractJavaNode pkg1 = SimpleNodeUtil.findChild(cu, ASTPackageDeclaration.class, 1);
        assertThat(pkg1, nullValue());
    }
    
    @Test
    public void getChildTokens() {
        List<Token> tokens = SimpleNodeUtil.getChildTokens(cu);

        assertThat(tokens, hasSize(8));
        assertThat(tokens.get(0), hasToString("package"));
        assertThat(tokens.get(1), hasToString("abc"));
        assertThat(tokens.get(2), hasToString(";"));
        assertThat(tokens.get(3), hasToString("class"));
        assertThat(tokens.get(4), hasToString("C1"));
        assertThat(tokens.get(5), hasToString("{"));
        assertThat(tokens.get(6), hasToString("}"));
        assertThat(tokens.get(7), hasToString(""));

        // the second EOF is not included
    }
}
