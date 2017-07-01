/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.oom.api;

import net.sourceforge.pmd.lang.java.oom.metrics.AtfdMetric;
import net.sourceforge.pmd.lang.java.oom.metrics.CycloMetric;
import net.sourceforge.pmd.lang.java.oom.metrics.LocMetric;
import net.sourceforge.pmd.lang.java.oom.metrics.NcssMetric;

/**
 * Keys identifying operation metrics.
 */
public enum OperationMetricKey implements MetricKey<OperationMetric> {

    /** Access to Foreign Data. */ // TODO:cf add short description here for javadoc hints
    ATFD(new AtfdMetric()),
    /** Cyclomatic complexity. */
    CYCLO(new CycloMetric()),
    /** Non Commenting Source Statements. */
    NCSS(new NcssMetric()),
    /** Lines of Code. */
    LOC(new LocMetric());

    private final OperationMetric calculator;

    OperationMetricKey(OperationMetric m) {
        calculator = m;
    }

    @Override
    public OperationMetric getCalculator() {
        return calculator;
    }
}
