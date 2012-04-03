package org.incava.pmdx;

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.pmd.ast.ASTFormalParameters;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.ast.SimpleNode;
import net.sourceforge.pmd.ast.Token;

/**
 * Miscellaneous routines for method declarations. The instance method contains
 * a cache for method lookup.
 */
public class MethodUtil extends FunctionUtil {
    private final Map<ASTMethodDeclaration, MethodMatchCriteria> methodCriterias;
    
    public MethodUtil() {
        methodCriterias = new HashMap<ASTMethodDeclaration, MethodMatchCriteria>();
    }

    public static ASTMethodDeclarator getDeclarator(ASTMethodDeclaration method) {
        return (ASTMethodDeclarator)SimpleNodeUtil.findChild(method, ASTMethodDeclarator.class);
    }
    
    public static Token getName(ASTMethodDeclaration method) {
        ASTMethodDeclarator decl = getDeclarator(method);
        return decl.getFirstToken();
    }

    public static ASTFormalParameters getParameters(ASTMethodDeclaration method) {
        ASTMethodDeclarator decl = getDeclarator(method);
        return (ASTFormalParameters)SimpleNodeUtil.findChild(decl, ASTFormalParameters.class);
    }

    public static String getFullName(ASTMethodDeclaration method) {
        Token nameTk = getName(method);
        ASTFormalParameters params = getParameters(method);
        return toFullName(nameTk, params);
    }

    public static double getMatchScore(ASTMethodDeclaration a, ASTMethodDeclaration b) {
        String fromName = getName(a).image;
        String toName = getName(b).image;

        if (!fromName.equals(toName)) {
            return 0;
        }

        ASTFormalParameters fromParams = getParameters(a);
        ASTFormalParameters toParams = getParameters(b);

        return ParameterUtil.getMatchScore(fromParams, toParams);
    }

    /**
     * This is the same as the static method, but it uses the cache.
     */
    public double fetchMatchScore(ASTMethodDeclaration a, ASTMethodDeclaration b) {
        // caching the criteria (instead of extracting it every time) is around 25% faster.
        MethodMatchCriteria aCriteria = getCriteria(a);
        MethodMatchCriteria bCriteria = getCriteria(b);

        return aCriteria.compare(bCriteria);
    }

    protected MethodMatchCriteria getCriteria(ASTMethodDeclaration method) {
        MethodMatchCriteria crit = methodCriterias.get(method);
        if (crit == null) {
            crit = new MethodMatchCriteria(method);
            methodCriterias.put(method, crit);
        }
        return crit;
    }
}
