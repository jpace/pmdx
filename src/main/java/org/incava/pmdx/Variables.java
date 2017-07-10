package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.Token;
import org.incava.ijdk.collect.Array;

/**
 * A list of variables.
 */
public class Variables extends Array<ASTVariableDeclarator> {
    public static final long serialVersionUID = 1L;
    
    public Variables(List<ASTVariableDeclarator> vds) {
        super(vds);
    }    

    public Array<Token> getNames() {
        Array<Token> names = new Array<Token>();
        for (ASTVariableDeclarator vd : this) {
            names.add(new Variable(vd).getName());
        }
        return names;
    }
}
