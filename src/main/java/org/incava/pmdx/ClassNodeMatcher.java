package org.incava.pmdx;

import java.util.List;
import org.incava.ijdk.collect.StringList;

/**
 * Compares two fields.
 */
public class ClassNodeMatcher {
    private final Match match;
    
    public ClassNodeMatcher(ClassNode x, ClassNode y) {
        match = new Match(x.getName().equals(y.getName()) ? 100 : 0);
    }

    public Match getMatch() {
        return match;
    }
}
