/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.oom.metrics.cyclo;

import org.apache.commons.lang3.mutable.MutableInt;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTCatchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalExpression;
import net.sourceforge.pmd.lang.java.ast.ASTDoStatement;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchLabel;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.ast.JavaParserVisitorAdapter;

/**
 * Visitor calculating cyclo without counting boolean operators.
 *
 * @author Clément Fournier
 * @see net.sourceforge.pmd.lang.java.oom.metrics.CycloMetric
 */
public class CycloPathUnawareOperationVisitor extends JavaParserVisitorAdapter implements CycloVisitor {

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        int childCount = node.jjtGetNumChildren();
        int lastIndex = childCount - 1;

        for (int n = 0; n < lastIndex; n++) {
            Node childNode = node.jjtGetChild(n);
            if (childNode instanceof ASTSwitchLabel) {
                // default is not considered a decision (same as "else")
                ASTSwitchLabel sl = (ASTSwitchLabel) childNode;
                if (!sl.isDefault()) {
                    childNode = node.jjtGetChild(n + 1);    // check the label is not empty
                    if (childNode instanceof ASTBlockStatement) {
                        ((MutableInt) data).increment();
                    }
                }
            }
        }
        super.visit(node, data);
        return data;
    }

    @Override
    public Object visit(ASTConditionalExpression node, Object data) {
        if (node.isTernary()) {
            ((MutableInt) data).increment();
            super.visit(node, data);
        }
        return data;
    }

    @Override
    public Object visit(ASTWhileStatement node, Object data) {
        ((MutableInt) data).increment();
        super.visit(node, data);
        return data;
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        ((MutableInt) data).increment();
        super.visit(node, data);
        return data;
    }

    @Override
    public Object visit(ASTCatchStatement node, Object data) {
        ((MutableInt) data).increment();
        super.visit(node, data);
        return data;
    }

    @Override
    public Object visit(ASTForStatement node, Object data) {
        ((MutableInt) data).increment();
        super.visit(node, data);
        return data;
    }

    @Override
    public Object visit(ASTDoStatement node, Object data) {
        ((MutableInt) data).increment();
        super.visit(node, data);
        return data;
    }
}
