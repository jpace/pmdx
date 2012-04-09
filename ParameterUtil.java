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
            String             type  = getParameterType(param);
            types.add(type);
        }
        return types;
    }

    public static Token getParameterName(ASTFormalParameter param) {
        return getParameterNameToken(param);
    }

    public static Token getParameterNameToken(ASTFormalParameter param) {
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

    protected static void clearFromLists(List<ASTFormalParameter> fromParameters, int fromIdx, List<ASTFormalParameter> toParameters, int toIdx) {
        fromParameters.set(fromIdx, null);
        toParameters.set(toIdx, null);
    }

    public static Integer[] getParamMatches(List<ASTFormalParameter> fromFormalParams, int fromIdx, List<ASTFormalParameter> toFormalParams) {
        Integer[] typeAndNameMatch = new Integer[] { -1, -1 };
        ASTFormalParameter fp = fromFormalParams.get(fromIdx);

        for (int toIdx = 0; toIdx < toFormalParams.size(); ++toIdx) {
            ASTFormalParameter tp = toFormalParams.get(toIdx);

            if (tp == null) {
                continue;
            }

            if (areTypesEqual(fp, tp)) {
                typeAndNameMatch[0] = toIdx;
            }

            if (areNamesEqual(fp, tp)) {
                typeAndNameMatch[1] = toIdx;
            }

            if (typeAndNameMatch[0] == toIdx && typeAndNameMatch[1] == toIdx) {
                return typeAndNameMatch;
            }
        }
        return typeAndNameMatch;
    }

    public static Integer[] getMatch(List<ASTFormalParameter> fromFormalParams, int fromIdx, List<ASTFormalParameter> toFormalParams) {
        final Integer[] noMatch = new Integer[] { -1, -1 };

        Integer[] typeAndNameMatch = getParamMatches(fromFormalParams, fromIdx, toFormalParams);
        if (typeAndNameMatch[0] >= 0 && typeAndNameMatch[0] == typeAndNameMatch[1]) {
            clearFromLists(fromFormalParams, fromIdx, toFormalParams, typeAndNameMatch[1]);
            return typeAndNameMatch;
        }

        Integer bestMatch = typeAndNameMatch[0] >= 0 ? typeAndNameMatch[0] : typeAndNameMatch[1];
        
        if (bestMatch < 0) {
            return noMatch;
        }

        // make sure there isn't an exact match for this somewhere else in
        // fromParameters
        ASTFormalParameter to = toFormalParams.get(bestMatch);

        int fromMatch = getExactMatch(fromFormalParams, to);

        if (fromMatch >= 0) {
            return noMatch;
        }
        else {
            clearFromLists(fromFormalParams, fromIdx, toFormalParams, bestMatch);
            return typeAndNameMatch;
        }
    }

    public static double getMatchScore(ASTFormalParameters from, ASTFormalParameters to) {
        if (from.jjtGetNumChildren() == 0 && to.jjtGetNumChildren() == 0) {
            return 1.0;
        }
        
        // (int[], double, String) <=> (int[], double, String) ==> 100% (3 of 3)
        // (int[], double, String) <=> (double, int[], String) ==> 80% (3 of 3 - 10% * misordered)
        // (int[], double)         <=> (double, int[], String) ==> 46% (2 of 3 - 10% * misordered)
        // (int[], double, String) <=> (String) ==> 33% (1 of 3 params)
        // (int[], double) <=> (String) ==> 0 (0 of 3)

        List<String> fromParamTypes = getParameterTypes(from);
        List<String> toParamTypes = getParameterTypes(to);

        int fromSize = fromParamTypes.size();
        int toSize = toParamTypes.size();

        int exactMatches = 0;
        int misorderedMatches = 0;
            
        for (int fromIdx = 0; fromIdx < fromSize; ++fromIdx) {
            int paramMatch = getListMatch(fromParamTypes, fromIdx, toParamTypes);
            if (paramMatch == fromIdx) {
                ++exactMatches;
            }
            else if (paramMatch >= 0) {
                ++misorderedMatches;
            }
        }

        for (int toIdx = 0; toIdx < toSize; ++toIdx) {
            int paramMatch = getListMatch(toParamTypes, toIdx, fromParamTypes);
            if (paramMatch == toIdx) {
                ++exactMatches;
            }
            else if (paramMatch >= 0) {
                ++misorderedMatches;
            }
        }

        int numParams = Math.max(fromSize, toSize);
        double match = (double)exactMatches / numParams;
        match += (double)misorderedMatches / (2 * numParams);

        return 0.5 + (match / 2.0);
    }

    /**
     * Returns 0 for exact match, +1 for misordered match, -1 for no match.
     */
    protected static int getListMatch(List<String> fromList, int fromIndex, List<String> toList) {
        int fromSize = fromList.size();
        int toSize = toList.size();
        String fromStr = fromIndex < fromSize ? fromList.get(fromIndex) : null;
        String toStr = fromIndex < toSize ? toList.get(fromIndex) : null;
        
        if (fromStr == null) {
            return -1;
        }
        
        if (fromStr.equals(toStr)) {
            fromList.set(fromIndex, null);
            toList.set(fromIndex, null);
            return fromIndex;
        }
        
        for (int toIdx = 0; toIdx < toSize; ++toIdx) {
            toStr = toList.get(toIdx);
            if (fromStr.equals(toStr)) {
                fromList.set(fromIndex, null);
                toList.set(toIdx, null);
                return toIdx;
            }
        }
        return -1;
    }

    protected static int getExactMatch(List<ASTFormalParameter> fromParameters, ASTFormalParameter to) {
        int idx = 0;
        for (ASTFormalParameter from : fromParameters) {
            if (areTypesEqual(from, to) && areNamesEqual(from, to)) {
                return idx;
            }
            else {
                ++idx;
            }
        }
        return -1;
    }

    protected static boolean areTypesEqual(ASTFormalParameter from, ASTFormalParameter to) {
        return from != null && getParameterType(from).equals(getParameterType(to));
    }

    protected static boolean areNamesEqual(ASTFormalParameter from, ASTFormalParameter to) {
        return from != null && getParameterName(from).image.equals(getParameterName(to).image);
    }
}
