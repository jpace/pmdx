package org.incava.pmdx;

import java.util.*;

/**
 * The score between two nodes, of how similar they are.
 */
public class Match {
    private final double score;
    
    public Match(double score) {
        this.score = score;
    }

    public double score() {
        return score;
    }
}
