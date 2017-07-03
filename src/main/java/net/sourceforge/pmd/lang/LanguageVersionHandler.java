/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang;

import java.io.Writer;

/**
 * Interface for obtaining the classes necessary for checking source files of a
 * specific language.
 *
 * @author pieter_van_raemdonck - Application Engineers NV/SA - www.ae.be
 */
public interface LanguageVersionHandler {

    /**
     * Get the default ParserOptions.
     *
     * @return ParserOptions
     */
    ParserOptions getDefaultParserOptions();

    /**
     * Get the Parser.
     *
     * @return Parser
     */
    Parser getParser(ParserOptions parserOptions);

    /**
     * Get the SymbolFacade.
     *
     * @return VisitorStarter
     */
    VisitorStarter getSymbolFacade();

    /**
     * Get the SymbolFacade.
     *
     * @param classLoader
     *            A ClassLoader to use for resolving Types.
     * @return VisitorStarter
     */
    VisitorStarter getSymbolFacade(ClassLoader classLoader);

    /**
     * Get the Metrics Framework visitor.
     *
     * @return VisitorStarter
     */
    VisitorStarter getMetricsVisitorFacade();


    /**
     * Get the DumpFacade.
     *
     * @param writer
     *            The writer to dump to.
     * @return VisitorStarter
     */
    VisitorStarter getDumpFacade(Writer writer, String prefix, boolean recurse);
}
