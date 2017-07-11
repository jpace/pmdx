package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.collect.Array;

/**
 * A class or interface.
 */
public class ClassNode extends Node<ASTClassOrInterfaceDeclaration> {
    public ClassNode(ASTClassOrInterfaceDeclaration node) {
        super(node);
    }
    
    public Token getNameToken() {
        return findToken(JavaParserConstants.IDENTIFIER);
    }
    
    public String getName() {
        return getNameToken().image;
    }

    public double getMatchScore(ClassNode other) {
        return match(other).score();
    }

    public Match match(ClassNode other) {
        return new Match(getName().equals(other.getName()) ? 100 : 0);
    }

    public Field getField(int idx) {
        // inefficient
        return getFields().get(idx);
    }

    public Array<Field> getFields() {
        Array<Field> fields = Array.empty();
        Array<ASTFieldDeclaration> fieldDecls = getAllOfType(ASTFieldDeclaration.class);
        for (ASTFieldDeclaration fd : fieldDecls) {
            fields.add(new Field(fd));
        }
        return fields;
    }

    public Method getMethod(int idx) {
        ASTClassOrInterfaceBody body = findChild(ASTClassOrInterfaceBody.class);
        List<ASTClassOrInterfaceBodyDeclaration> decls = Node.of(body).findChildren(ASTClassOrInterfaceBodyDeclaration.class);
        int matched = -1;
        for (ASTClassOrInterfaceBodyDeclaration decl : decls) {
            ASTMethodDeclaration d = Node.of(decl).findChild(ASTMethodDeclaration.class);
            if (d != null && ++matched == idx) {
                return new Method(d);
            }
        }
        return null;
    }    

    public Array<Method> getMethods() {
        Array<ASTMethodDeclaration> methodDecls = getAllOfType(ASTMethodDeclaration.class);
        Array<Method> methods = Array.empty();
        for (ASTMethodDeclaration md : methodDecls) {
            methods.add(new Method(md));
        }
        return methods;
    }

    public Array<Ctor> getCtors() {
        Array<ASTConstructorDeclaration> ctorDecls = getAllOfType(ASTConstructorDeclaration.class);
        Array<Ctor> ctors = Array.empty();
        for (ASTConstructorDeclaration d : ctorDecls) {
            ctors.add(new Ctor(d));
        }
        return ctors;
    }

    protected <A extends AbstractJavaNode> Array<A> getAllOfType(Class<A> cls) {
        Array<A> matching = Array.empty();
        ASTClassOrInterfaceBody body = findChild(ASTClassOrInterfaceBody.class);
        List<ASTClassOrInterfaceBodyDeclaration> decls = Node.of(body).findChildren(ASTClassOrInterfaceBodyDeclaration.class);
        for (ASTClassOrInterfaceBodyDeclaration decl : decls) {
            A d = Node.of(decl).findChild(cls);
            if (d != null) {
                matching.add(d);
            }
        }
        return matching;
    }    
}
