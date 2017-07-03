/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang;

import java.io.Writer;

/**
 * This is a generic implementation of the LanguageVersionHandler interface.
 *
 * @see LanguageVersionHandler
 */
public abstract class AbstractLanguageVersionHandler implements LanguageVersionHandler {

    @Override
    public ParserOptions getDefaultParserOptions() {
        return new ParserOptions();
    }

    @Override
    public VisitorStarter getSymbolFacade() {
        return VisitorStarter.DUMMY;
    }

    @Override
    public VisitorStarter getSymbolFacade(ClassLoader classLoader) {
        return VisitorStarter.DUMMY;
    }

    @Override
    public VisitorStarter getMetricsVisitorFacade() {
        return VisitorStarter.DUMMY;
    }

    @Override
    public VisitorStarter getDumpFacade(final Writer writer, final String prefix, final boolean recurse) {
        return VisitorStarter.DUMMY;
    }
}
