/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java;

import java.io.Writer;

import net.sourceforge.pmd.lang.AbstractLanguageVersionHandler;
import net.sourceforge.pmd.lang.VisitorStarter;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.DumpFacade;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.lang.java.symboltable.SymbolFacade;
import net.sourceforge.pmd.lang.java.typeresolution.TypeResolutionFacade;

/**
 * Implementation of LanguageVersionHandler for the Java AST. It uses anonymous
 * classes as adapters of the visitors to the VisitorStarter interface.
 *
 * @author pieter_van_raemdonck - Application Engineers NV/SA - www.ae.be
 */
public abstract class AbstractJavaHandler extends AbstractLanguageVersionHandler {

    @Override
    public VisitorStarter getSymbolFacade() {
        return new VisitorStarter() {
            public void start(Node rootNode) {
                new SymbolFacade().initializeWith(null, (ASTCompilationUnit) rootNode);
            }
        };
    }

    @Override
    public VisitorStarter getSymbolFacade(final ClassLoader classLoader) {
        return new VisitorStarter() {
            public void start(Node rootNode) {
                new SymbolFacade().initializeWith(classLoader, (ASTCompilationUnit) rootNode);
            }
        };
    }

    @Override
    public VisitorStarter getTypeResolutionFacade(final ClassLoader classLoader) {
        return new VisitorStarter() {
            public void start(Node rootNode) {
                new TypeResolutionFacade().initializeWith(classLoader, (ASTCompilationUnit) rootNode);
            }
        };
    }

    @Override
    public VisitorStarter getDumpFacade(final Writer writer, final String prefix, final boolean recurse) {
        return new VisitorStarter() {
            public void start(Node rootNode) {
                new DumpFacade().initializeWith(writer, prefix, recurse, (JavaNode) rootNode);
            }
        };
    }
}
