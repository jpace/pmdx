package org.incava.pmdx;

import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;

/**
 * A criterion (some criteria) for matching nodes.
 */
public class MethodMatchCriteria {
    private final ASTMethodDeclaration meth;
    private String name = null;
    private ASTFormalParameters params = null;
    
    public MethodMatchCriteria(ASTMethodDeclaration m) {
        meth = m;
    }

    public double compare(MethodMatchCriteria other) {
        String aName = getName();
        String bName = other.getName();

        double score = 0.0;

        if (aName.equals(bName)) {
            ASTFormalParameters afp = getParameters();
            ASTFormalParameters bfp = other.getParameters();

            score = ParameterUtil.getMatchScore(afp, bfp);
        }
        // or else this could eventually find methods renamed, if we compare
        // based on parameters and method contents
            
        return score;
    }

    protected String getName() {
        // lazy evaluation
        if (name == null) {
            name = MethodUtil.getName(meth).image;
        }
        return name;
    }

    protected ASTFormalParameters getParameters() {
        // lazy evaluation
        if (params == null) {
            params = MethodUtil.getParameters(meth);
        }
        return params;
    }
}
