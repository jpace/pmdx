package org.incava.pmdx;

import java.util.List;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.attest.Parameterized;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.incava.attest.Assertions.assertEqual;
import static org.incava.attest.Assertions.message;
import static org.incava.attest.ContextMatcher.withContext;
import static org.incava.pmdx.CompilationUnitTest.compile;

public class SimpleNodeUtilTest extends Parameterized {
    private static ASTCompilationUnit compUnit;

    @BeforeClass
    public static void setup() {
        compUnit = compile("package abc;\nclass C1 {}");
    }
    
    @Test
    public void getFirstToken() {
        Token tk = SimpleNodeUtil.getFirstToken(compUnit);

        assertThat(tk.toString(), equalTo("package"));
        assertThat(tk.kind, equalTo(43));
    }
    
    @Test
    public void getLastToken() {
        Token tk = SimpleNodeUtil.getLastToken(compUnit);

        // a compilation unit has two EOFs?

        assertThat(tk.toString(), equalTo(""));
        assertThat(tk.kind, equalTo(0));
    }
    
    @Test
    public void hasChildren() {
        boolean result = SimpleNodeUtil.hasChildren(compUnit);
        assertThat(result, equalTo(true));
    }
    
    @Test
    public void getParent() {
        AbstractJavaNode parent = SimpleNodeUtil.getParent(compUnit);
        assertThat(parent, nullValue());
    }
    
    @Test
    public void getChildren() {
        List<Object> children = SimpleNodeUtil.getChildren(compUnit);

        assertThat(children, hasSize(3));
        assertThat(children.get(0), instanceOf(ASTPackageDeclaration.class));
        assertThat(children.get(1), instanceOf(ASTTypeDeclaration.class));
        assertThat(children.get(2), instanceOf(Token.class));        
    }
    
    @Test
    public void getChildrenNodes() {
        List<Object> children = SimpleNodeUtil.getChildren(compUnit, true, false);

        assertThat(children, hasSize(2));
        assertThat(children.get(0), instanceOf(ASTPackageDeclaration.class));
        assertThat(children.get(1), instanceOf(ASTTypeDeclaration.class));
    }
    
    @Test
    public void getChildrenTokens() {
        List<Object> children = SimpleNodeUtil.getChildren(compUnit, false, true);

        assertThat(children, hasSize(1));
        assertThat(children.get(0), instanceOf(Token.class));        
    }
    
    @Test
    public void getChildrenNodesTokens() {
        List<Object> children = SimpleNodeUtil.getChildren(compUnit, true, true);

        assertThat(children, hasSize(3));
        assertThat(children.get(0), instanceOf(ASTPackageDeclaration.class));
        assertThat(children.get(1), instanceOf(ASTTypeDeclaration.class));
        assertThat(children.get(2), instanceOf(Token.class));        
    }
    
    @Test
    public void getChildrenNeither() {
        List<Object> children = SimpleNodeUtil.getChildren(compUnit, false, false);
        assertThat(children, empty());
    }    
    
    @Test
    public void findChildNoArgs() {
        AbstractJavaNode child = SimpleNodeUtil.findChild(compUnit);
        assertThat(child, instanceOf(ASTPackageDeclaration.class));
    }    
    
    @Test
    public void findChildOfClass() {
        AbstractJavaNode pkg = SimpleNodeUtil.findChild(compUnit, ASTPackageDeclaration.class);
        assertThat(pkg, instanceOf(ASTPackageDeclaration.class));

        AbstractJavaNode type = SimpleNodeUtil.findChild(compUnit, ASTTypeDeclaration.class);
        assertThat(type, instanceOf(ASTTypeDeclaration.class));        
    }
    
    @Test
    public void findChildOfClassOfCount() {
        AbstractJavaNode pkg0 = SimpleNodeUtil.findChild(compUnit, ASTPackageDeclaration.class, 0);
        assertThat(pkg0, instanceOf(ASTPackageDeclaration.class));

        AbstractJavaNode pkg1 = SimpleNodeUtil.findChild(compUnit, ASTPackageDeclaration.class, 1);
        assertThat(pkg1, nullValue());
    }
    
    @Test
    public void getChildTokens() {
        List<Token> tokens = SimpleNodeUtil.getChildTokens(compUnit);

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

    private <NodeType extends AbstractJavaNode> void assertFindChildren(int numExpected, ASTCompilationUnit cu, Class<NodeType> childType) {
        List<NodeType> children = SimpleNodeUtil.findChildren(cu, childType);
        assertThat(children, hasSize(numExpected));
    }

    @Test
    public void findChildren() throws Exception {
        ASTCompilationUnit cu = compile("package abc;\nclass C {}\nclass D {}");
        tr.Ace.log("cu", cu);
        tr.Ace.log("cu.children", SimpleNodeUtil.getChildren(cu, true, true));

        assertFindChildren(1, cu, ASTPackageDeclaration.class);
        assertFindChildren(2, cu, ASTTypeDeclaration.class);
        assertFindChildren(0, cu, ASTImportDeclaration.class);
    }

    @Test
    public void findToken() throws Exception {
        ASTCompilationUnit cu = compile("package abc;\npublic class C {}");
        AbstractJavaNode type = SimpleNodeUtil.findChild(cu, ASTTypeDeclaration.class);

        Token pb = SimpleNodeUtil.findToken(type, JavaParserConstants.PUBLIC);
        assertThat(pb, notNullValue());

        Token abs = SimpleNodeUtil.findToken(type, JavaParserConstants.ABSTRACT);
        assertThat(abs, nullValue());
    }

    @Test
    public void getLeadingToken() throws Exception {
        ASTCompilationUnit cu = compile("package abc;\npublic abstract class C {}");
        AbstractJavaNode type = SimpleNodeUtil.findChild(cu, ASTTypeDeclaration.class);

        Token pb = SimpleNodeUtil.getLeadingToken(type, JavaParserConstants.PUBLIC);
        assertThat(pb, notNullValue());

        Token abs = SimpleNodeUtil.getLeadingToken(type, JavaParserConstants.ABSTRACT);
        assertThat(abs, notNullValue());

        Token st = SimpleNodeUtil.findToken(type, JavaParserConstants.STATIC);
        assertThat(st, nullValue());
    }    
}
