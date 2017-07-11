package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.collect.StringList;

/**
 * Miscellaneous routines for fields.
 */
public class Field extends Node<ASTFieldDeclaration> {
    public Field(ASTFieldDeclaration fld) {
        super(fld);
    }

    public List<ASTVariableDeclarator> getVariableDeclarators() {
        return findChildren(ASTVariableDeclarator.class);
    }

    /**
     * Returns a string in the form "a, b, c", for the variables declared in this field.
     */
    public String getNames() {
        return getNameList().join(", ");
    }

    /**
     * Returns a list of strings of the names of the variables declared in this field.
     */
    public StringList getNameList() {
        StringList names = StringList.empty();
        for (ASTVariableDeclarator avd : getVariableDeclarators()) {
            names.add(new Variable(avd).getName().image);
        }
        return names;
    }

    /**
     * Returns the type of the field.
     */
    public Node<ASTType> getType() {
        return Node.of(findChild(ASTType.class));
    }
}
