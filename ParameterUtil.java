package org.incava.pmdx;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pmd.ast.ASTFormalParameter;
import net.sourceforge.pmd.ast.ASTFormalParameters;
import net.sourceforge.pmd.ast.ASTType;
import net.sourceforge.pmd.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.ast.Token;

/**
 * Miscellaneous routines for parameters.
 */
public class ParameterUtil extends SimpleNodeUtil {
    public static List<ASTFormalParameter> getParameters(ASTFormalParameters params) {
        return snatchChildren(params, "net.sourceforge.pmd.ast.ASTFormalParameter");
    }

    public static List<Token> getParameterNames(ASTFormalParameters params) {
        List<ASTFormalParameter> fps = getParameters(params);
        List<Token> names = new ArrayList<Token>();
        
        for (ASTFormalParameter fp : fps) {
            Token name = getParameterName(fp);
            names.add(name);
        }

        return names;
    }

    public static ASTFormalParameter getParameter(ASTFormalParameters params, int index) {
        return (ASTFormalParameter)findChild(params, "net.sourceforge.pmd.ast.ASTFormalParameter", index);
    }

    public static Token getParameterName(ASTFormalParameters params, int index) {
        ASTFormalParameter param = getParameter(params, index);
        return getParameterName(param);
    }

    public static String getParameterType(ASTFormalParameters params, int index) {
        ASTFormalParameter param = getParameter(params, index);
        return getParameterType(param);
    }

    public static List<String> getParameterTypes(ASTFormalParameters params) {
        List<String> types = new ArrayList<String>();
        int  nParams = params.jjtGetNumChildren();
        for (int i = 0; i < nParams; ++i) {
            ASTFormalParameter param = (ASTFormalParameter)params.jjtGetChild(i);
            String             type  = new Parameter(param).getType();
            types.add(type);
        }
        return types;
    }

    public static List<Parameter> getParameterList(ASTFormalParameters params) {
        List<Parameter> paramList = new ArrayList<Parameter>();
        int nParams = params.jjtGetNumChildren();
        
        for (int i = 0; i < nParams; ++i) {
            ASTFormalParameter param  = getParameter(params, i);
            paramList.add(new Parameter(param));
        }

        return paramList;
    }

    public static Token getParameterName(ASTFormalParameter param) {
        if (param == null) {
            return null;
        }
        else {
            ASTVariableDeclaratorId vid = (ASTVariableDeclaratorId)param.jjtGetChild(1);
            return vid.getFirstToken();
        }
    }

    public static String getParameterType(ASTFormalParameter param) {
        if (param == null) {
            return null;
        }
        else {
            // type is the first child, but we also have to look for the
            // variable ID including brackets, for arrays
            StringBuffer typeBuf = new StringBuffer();
            ASTType      type    = (ASTType)SimpleNodeUtil.findChild(param, "net.sourceforge.pmd.ast.ASTType");
            Token        ttk     = type.getFirstToken();
        
            while (true) {
                typeBuf.append(ttk.image);
                if (ttk == type.getLastToken()) {
                    break;
                }
                else {
                    ttk = ttk.next;
                }
            }
            
            ASTVariableDeclaratorId vid = (ASTVariableDeclaratorId)SimpleNodeUtil.findChild(param, "net.sourceforge.pmd.ast.ASTVariableDeclaratorId");
            
            Token vtk = vid.getFirstToken();
            while (vtk != vid.getLastToken()) {
                vtk = vtk.next;
                typeBuf.append(vtk.image);
            }

            return typeBuf.toString();
        }
    }

    public static int[] getMatch(List<Parameter> fromParameters, int fromIdx, List<Parameter> toParameters) {
        int typeMatch = -1;
        int nameMatch = -1;
        
        Parameter fromParam = fromParameters.get(fromIdx);

        for (int toIdx = 0; toIdx < toParameters.size(); ++toIdx) {
            Parameter toParam = toParameters.get(toIdx);

            if (toParam != null) {
                if (fromParam.getType().equals(toParam.getType())) {
                    typeMatch = toIdx;
                }

                if (fromParam.getName().equals(toParam.getName())) {
                    nameMatch = toIdx;
                }

                if (typeMatch == toIdx && nameMatch == toIdx) {
                    fromParameters.set(fromIdx, null);
                    toParameters.set(toIdx, null);
                    return new int[] { typeMatch, nameMatch };
                }
            }
        }

        int bestMatch = typeMatch;
        if (bestMatch < 0) {
            bestMatch = nameMatch;
        }
        
        if (bestMatch >= 0) {
            // make sure there isn't an exact match for this somewhere else in
            // fromParameters
            Parameter toParam = toParameters.get(bestMatch);

            int fromMatch = getExactMatch(fromParameters, toParam);

            if (fromMatch >= 0) {
                return new int[] { -1, -1 };
            }
            else {
                fromParameters.set(fromIdx, null);
                toParameters.set(bestMatch, null);
                return new int[] { typeMatch, nameMatch };
            }
        }
        else {
            return new int[] { -1, -1 };
        }
    }

    public static double getMatchScore(ASTFormalParameters a, ASTFormalParameters b) {
        double score;
        
        if (a.jjtGetNumChildren() == 0 && b.jjtGetNumChildren() == 0) {
            score = 1.0;
        }
        else {
            // (int[], double, String) <=> (int[], double, String) ==> 100% (3 of 3)
            // (int[], double, String) <=> (double, int[], String) ==> 80% (3 of 3 - 10% * misordered)
            // (int[], double)         <=> (double, int[], String) ==> 46% (2 of 3 - 10% * misordered)
            // (int[], double, String) <=> (String) ==> 33% (1 of 3 params)
            // (int[], double) <=> (String) ==> 0 (0 of 3)

            List<String> aParamTypes = getParameterTypes(a);
            List<String> bParamTypes = getParameterTypes(b);

            int aSize = aParamTypes.size();
            int bSize = bParamTypes.size();

            int exactMatches = 0;
            int misorderedMatches = 0;
            
            for (int ai = 0; ai < aSize; ++ai) {
                int paramMatch = getListMatch(aParamTypes, ai, bParamTypes);
                if (paramMatch == ai) {
                    ++exactMatches;
                }
                else if (paramMatch >= 0) {
                    ++misorderedMatches;
                }
            }

            for (int bi = 0; bi < bSize; ++bi) {
                int paramMatch = getListMatch(bParamTypes, bi, aParamTypes);
                if (paramMatch == bi) {
                    ++exactMatches;
                }
                else if (paramMatch >= 0) {
                    ++misorderedMatches;
                }
            }

            int numParams = Math.max(aSize, bSize);
            double match = (double)exactMatches / numParams;
            match += (double)misorderedMatches / (2 * numParams);

            score = 0.5 + (match / 2.0);
        }
        
        return score;
    }

    /**
     * Returns 0 for exact match, +1 for misordered match, -1 for no match.
     */
    protected static int getListMatch(List<String> aList, int aIndex, List<String> bList) {
        int    aSize = aList.size();
        int    bSize = bList.size();
        String aStr  = aIndex < aSize ? aList.get(aIndex) : null;
        String bStr  = aIndex < bSize ? bList.get(aIndex) : null;
        
        if (aStr == null) {
            return -1;
        }
        if (aStr.equals(bStr)) {
            aList.set(aIndex, null);
            bList.set(aIndex, null);
            return aIndex;
        }
        else {
            for (int bi = 0; bi < bSize; ++bi) {
                bStr = bList.get(bi);
                if (aStr.equals(bStr)) {
                    aList.set(aIndex, null);
                    bList.set(bi, null);
                    return bi;
                }
            }
            return -1;
        }
    }

    protected static int getExactMatch(List<Parameter> parameters, Parameter other) {
        int idx = 0;
        for (Parameter param : parameters) {
            if (param != null && param.getType().equals(other.getType()) && param.getName().equals(other.getName())) {
                return idx;
            }
            else {
                ++idx;
            }
        }
        return -1;
    }

}
