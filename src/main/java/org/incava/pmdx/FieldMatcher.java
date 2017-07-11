package org.incava.pmdx;

import java.util.List;
import org.incava.ijdk.collect.StringList;

/**
 * Compares two fields.
 */
public class FieldMatcher {
    private final Match match;
    
    public FieldMatcher(Field x, Field y) {
        // a field can have more than one name.
        StringList aNames = x.getNameList();
        StringList bNames = y.getNameList();

        List<String> inBoth = aNames.intersection(bNames);
        
        int matched = inBoth.size();
        int count   = Math.max(aNames.size(), bNames.size());
        int score   = 50 * matched / count;

        if (x.getType().toString().equals(y.getType().toString())) {
            score += 50;
        }
        
        match = new Match(score);
    }

    public Match getMatch() {
        return match;
    }
}
