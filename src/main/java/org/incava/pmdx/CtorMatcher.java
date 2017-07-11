package org.incava.pmdx;

import java.util.*;

/**
 * Compares two ctors.
 */
public class CtorMatcher {
    private final Match match;
    
    public CtorMatcher(Ctor x, Ctor y) {
        Parameters xp = new Parameters(x.getParameters());
        Parameters yp = new Parameters(y.getParameters());
        match = new ParametersMatcher(xp, yp).getMatch();
    }

    public Match getMatch() {
        return match;
    }
}
