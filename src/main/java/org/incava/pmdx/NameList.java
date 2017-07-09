package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;

/**
 * An ASTNameList
 */
public class NameList extends Node<ASTNameList> {
    public NameList(ASTNameList names) {
        super(names);
    }

    public String getName(int index) {
        ASTName name = findChild(ASTName.class, index);
        return name == null ? null : Node.of(name).toString();
    }

    public ASTName getNameNode(int index) {
        return findChild(ASTName.class, index);
    }    
}
