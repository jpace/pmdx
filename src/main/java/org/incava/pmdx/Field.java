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
    public static Token getName(ASTVariableDeclarator vd) {
        ASTVariableDeclaratorId vid = Node.of(vd).findChild(ASTVariableDeclaratorId.class);
        return Node.of(vid).getFirstToken();
    }

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
            names.add(VariableUtil.getName(avd).image);
        }
        return names;
    }

    /**
     * Returns the type of the field.
     */
    public Node<ASTType> getType() {
        return Node.of(findChild(ASTType.class));
    }

    public double getMatchScore(ASTFieldDeclaration other) {
        return match(new Field(other)).score();
    }

    public Match match(Field other) {
        // a field can have more than one name.

        StringList aNames = getNameList();
        StringList bNames = other.getNameList();

        List<String> inBoth = aNames.intersection(bNames);
        
        int     matched = inBoth.size();
        int     count   = Math.max(aNames.size(), bNames.size());
        double  score   = 0.5 * matched / count;

        if (getType().toString().equals(other.getType().toString())) {
            score += 0.5;
        }
        
        return new Match(score);
    }
}
