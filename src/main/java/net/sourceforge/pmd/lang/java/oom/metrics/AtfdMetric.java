/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.oom.metrics;

import java.util.List;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodOrConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.AccessNode;
import net.sourceforge.pmd.lang.java.ast.QualifiedName;
import net.sourceforge.pmd.lang.java.oom.AbstractMetric;
import net.sourceforge.pmd.lang.java.oom.api.ClassMetric;
import net.sourceforge.pmd.lang.java.oom.api.MetricVersion;
import net.sourceforge.pmd.lang.java.oom.api.OperationMetric;
import net.sourceforge.pmd.lang.java.oom.signature.OperationSigMask;
import net.sourceforge.pmd.lang.java.oom.signature.OperationSignature.Role;
import net.sourceforge.pmd.lang.java.oom.signature.Signature.Visibility;

/**
 * Access to Foreign Data. Quantifies the number of foreign fields accessed directly or via accessors.
 *
 * @author Clément Fournier
 */
public final class AtfdMetric extends AbstractMetric implements ClassMetric, OperationMetric {

    @Override
    public boolean supports(AccessNode node) {
        return node instanceof ASTClassOrInterfaceDeclaration
            || node instanceof ASTMethodOrConstructorDeclaration && !node.isAbstract();
    }

    @Override // TODO:cf
    public double computeFor(ASTMethodOrConstructorDeclaration node, MetricVersion version) {

        OperationSigMask targetOps = new OperationSigMask();
        targetOps.restrictVisibilitiesTo(Visibility.PUBLIC);
        targetOps.restrictRolesTo(Role.GETTER_OR_SETTER);

        List<QualifiedName> callQNames = findAllCalls(node);
        int foreignCalls = 0;
        for (QualifiedName name : callQNames) {
            if (getTopLevelPackageStats().hasMatchingSig(name, targetOps)) {
                foreignCalls++;
            }
        }

        return foreignCalls / callQNames.size();
    }

    @Override
    public double computeFor(ASTClassOrInterfaceDeclaration node, MetricVersion version) {
        // TODO:cf
        return 0;
    }

}
