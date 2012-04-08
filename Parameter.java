package org.incava.pmdx;

import net.sourceforge.pmd.ast.ASTFormalParameter;

/**
 * Wraps an ASTFormalParameter
 */
public class Parameter {
    private final ASTFormalParameter param;

    public Parameter(ASTFormalParameter param) {
        this.param = param;
    }

    public ASTFormalParameter getParameter() {
        return param;
    }

    public String getName() {
        return ParameterUtil.getParameterName(param).image;
    }

    public String getType() {
        return ParameterUtil.getParameterType(param);
    }

    public String toString() {
        return param.toString();
    }
}
