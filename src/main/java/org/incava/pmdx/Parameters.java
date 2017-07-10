package org.incava.pmdx;

import java.util.List;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import org.incava.ijdk.collect.Array;

/**
 * A list of parameters.
 */
public class Parameters extends Node<ASTFormalParameters> {
    public static final long serialVersionUID = 1L;
    
    public Parameters(ASTFormalParameters params) {
        super(params);
    }

    public List<ASTFormalParameter> getParameters() {
        return findChildren(ASTFormalParameter.class);
    }
}
