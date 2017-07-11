package org.incava.pmdx;

import java.util.*;

/**
 * The score between two nodes, of how similar they are.
 */
public class Match {
    private final int score;
    
    public Match(int score) {
        this.score = score;
    }

    public int score() {
        return score;
    }

    public String toString() {
        return String.valueOf(score);
    }
}
