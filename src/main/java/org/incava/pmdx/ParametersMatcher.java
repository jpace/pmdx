package org.incava.pmdx;

import java.util.List;
import org.incava.ijdk.collect.Array;
import org.incava.ijdk.lang.Pair;

/**
 * Compares two sets of parameters.
 */
public class ParametersMatcher {
    private final Match match;
    
    public ParametersMatcher(Parameters x, Parameters y) {
        Array<String> xTypes = new Array<String>(x.getTypes());
        Array<String> yTypes = new Array<String>(y.getTypes());

        if (xTypes.equals(yTypes)) {
            match = new Match(100);
        }
        else {
            int numParams = Math.max(xTypes.size(), yTypes.size());            

            // first == exact, second == misordered
            Pair<Integer, Integer> matches = Pair.of(0, 0);

            matches = matchLists(xTypes, yTypes, matches);
            matches = matchLists(yTypes, xTypes, matches);

            int score = (100 * matches.first() / numParams) + (100 * matches.second() / (2 * numParams));
            score = 50 + (score / 2);
            match = new Match(score);
        }
    }

    public Match getMatch() {
        return match;
    }

    protected Pair<Integer, Integer> matchLists(Array<String> xList, Array<String> yList, Pair<Integer, Integer> matches) {
        for (int xIdx = 0; xIdx < xList.size(); ++xIdx) {
            int paramMatch = matchInList(xList, xIdx, yList);
            if (paramMatch == xIdx) {
                matches = Pair.of(matches.first() + 1, matches.second());
            }
            else if (paramMatch >= 0) {
                matches = Pair.of(matches.first(), matches.second() + 1);
            }
        }
        return matches;
    }    

    /**
     * Returns 0 for exact match, +1 for misordered match, -1 for no match.
     */
    protected int matchInList(Array<String> xList, int index, Array<String> yList) {
        String xStr = xList.get(index);
        if (xStr == null) {
            return -1;
        }
        else if (xStr.equals(yList.get(index))) {
            clearFromLists(xList, index, yList, index);
            return index;
        }
        else {
            int yIdx = yList.indexOf(xStr);
            if (yIdx >= 0) {
                clearFromLists(xList, index, yList, yIdx);
                return yIdx;
            }
            else {
                return -1;
            }
        }
    }

    protected void clearFromLists(Array<String> xList, int xIdx, Array<String> yList, int yIdx) {
        xList.set(xIdx, null);
        yList.set(yIdx, null);
    }
}
