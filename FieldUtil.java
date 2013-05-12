package org.incava.pmdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sourceforge.pmd.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.ast.ASTType;
import net.sourceforge.pmd.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.ast.Token;
import org.incava.ijdk.lang.StringExt;
import org.incava.ijdk.util.CollectionExt;

/**
 * Miscellaneous routines for fields.
 */
public class FieldUtil extends SimpleNodeUtil {
    public static Token getName(ASTVariableDeclarator vd) {
        ASTVariableDeclaratorId vid = findChild(vd, ASTVariableDeclaratorId.class);
        return vid.getFirstToken();
    }

    public static List<ASTVariableDeclarator> getVariableDeclarators(ASTFieldDeclaration fld) {
        return snatchChildren(fld, ASTVariableDeclarator.class);
    }

    /**
     * Returns a string in the form "a, b, c", for the variables declared in
     * this field.
     */
    public static String getNames(ASTFieldDeclaration fld) {
        return StringExt.join(getNameList(fld), ", ");
    }

    /**
     * Returns a list of strings of the names of the variables declared in this
     * field.
     */
    public static List<String> getNameList(ASTFieldDeclaration fld) {
        List<String> names = new ArrayList<String>();
        for (ASTVariableDeclarator avd : getVariableDeclarators(fld)) {
            names.add(VariableUtil.getName(avd).image);
        }
        return names;
    }

    public static double getMatchScore(ASTFieldDeclaration afd, ASTFieldDeclaration bfd) {
        // a field can have more than one name.

        List<String> aNames = getNameList(afd);
        List<String> bNames = getNameList(bfd);

        Set<String> inBoth = CollectionExt.intersection(getNameList(afd), getNameList(bfd));

        int     matched = inBoth.size();
        int     count   = Math.max(aNames.size(), bNames.size());
        double  score   = 0.5 * matched / count;

        ASTType aType   = findChild(afd, ASTType.class);
        ASTType bType   = findChild(bfd, ASTType.class);

        if (toString(aType).equals(toString(bType))) {
            score += 0.5;
        }
        
        return score;
    }
}
