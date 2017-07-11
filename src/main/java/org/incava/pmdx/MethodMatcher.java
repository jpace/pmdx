package org.incava.pmdx;

import java.util.*;

/**
 * Compares two methods.
 */
public class MethodMatcher {
    private final Match match;
    
    public MethodMatcher(Method x, Method y) {
        String xn = x.getName().image;
        String yn = y.getName().image;

        if (xn.equals(yn)) {
            Parameters xp = new Parameters(x.getParameters());
            Parameters yp = new Parameters(y.getParameters());
            match = new ParametersMatcher(xp, yp).getMatch();
        }
        else {
            match = new Match(0);
        }
    }

    public Match getMatch() {
        return match;
    }
}
