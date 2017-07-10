package org.incava.pmdx;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.Token;

/**
 * A parameter.
 */
public class Parameter extends Node<ASTFormalParameter> {
    public Parameter(ASTFormalParameter fp) {
        super(fp);
    }

    public Token getName() {
        ASTVariableDeclaratorId vid = findChild(ASTVariableDeclaratorId.class);
        return Node.of(vid).getFirstToken();
    }

    public String getType() {
        // type is the first child, but we also have to look for the variable ID including brackets,
        // for arrays
        StringBuilder sb   = new StringBuilder();
        ASTType       type = findChild(ASTType.class);
        Node<ASTType> tn   = Node.of(type);
        Token         ttk  = tn.getFirstToken();
        
        while (true) {
            sb.append(ttk.image);
            if (ttk == tn.getLastToken()) {
                break;
            }
            else {
                ttk = ttk.next;
            }
        }

        Node<ASTVariableDeclaratorId> vid = Node.of(findChild(ASTVariableDeclaratorId.class));
            
        Token vtk = vid.getFirstToken();
        while (vtk != vid.getLastToken()) {
            vtk = vtk.next;
            sb.append(vtk.image);
        }

        return sb.toString();
    }
}
