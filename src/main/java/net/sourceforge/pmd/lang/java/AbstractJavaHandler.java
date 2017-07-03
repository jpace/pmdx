/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java;

import java.io.Writer;

import net.sourceforge.pmd.lang.AbstractLanguageVersionHandler;
import net.sourceforge.pmd.lang.VisitorStarter;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.JavaNode;

/**
 * Implementation of LanguageVersionHandler for the Java AST. It uses anonymous
 * classes as adapters of the visitors to the VisitorStarter interface.
 *
 * @author pieter_van_raemdonck - Application Engineers NV/SA - www.ae.be
 */
public abstract class AbstractJavaHandler extends AbstractLanguageVersionHandler {

    @Override
    public VisitorStarter getSymbolFacade() {
        return null;
    }

    @Override
    public VisitorStarter getSymbolFacade(final ClassLoader classLoader) {
        return null;
    }

    @Override
    public VisitorStarter getTypeResolutionFacade(final ClassLoader classLoader) {
        return null;
    }

    @Override
    public VisitorStarter getDumpFacade(final Writer writer, final String prefix, final boolean recurse) {
        return null;
    }
}
