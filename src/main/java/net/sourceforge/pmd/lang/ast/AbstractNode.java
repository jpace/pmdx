/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractNode implements Node {

    protected Node parent;
    protected Node[] children;
    protected int childIndex;
    protected int id;

    private String image;
    protected int beginLine = -1;
    protected int endLine;
    protected int beginColumn = -1;
    protected int endColumn;
    private Object userData;
    protected GenericToken firstToken;
    protected GenericToken lastToken;

    public AbstractNode(int id) {
        this.id = id;
    }

    public AbstractNode(int id, int theBeginLine, int theEndLine, int theBeginColumn, int theEndColumn) {
        this(id);

        beginLine = theBeginLine;
        endLine = theEndLine;
        beginColumn = theBeginColumn;
        endColumn = theEndColumn;
    }

    public boolean isSingleLine() {
        return beginLine == endLine;
    }

    @Override
    public void jjtOpen() {
        // to be overridden by subclasses
    }

    @Override
    public void jjtClose() {
        // to be overridden by subclasses
    }

    @Override
    public void jjtSetParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public Node jjtGetParent() {
        return parent;
    }

    @Override
    public void jjtAddChild(Node child, int index) {
        if (children == null) {
            children = new Node[index + 1];
        } else if (index >= children.length) {
            Node[] newChildren = new Node[index + 1];
            System.arraycopy(children, 0, newChildren, 0, children.length);
            children = newChildren;
        }
        children[index] = child;
        child.jjtSetChildIndex(index);
    }

    @Override
    public void jjtSetChildIndex(int index) {
        childIndex = index;
    }

    @Override
    public int jjtGetChildIndex() {
        return childIndex;
    }

    @Override
    public Node jjtGetChild(int index) {
        return children[index];
    }

    @Override
    public int jjtGetNumChildren() {
        return children == null ? 0 : children.length;
    }

    @Override
    public int jjtGetId() {
        return id;
    }

    /**
     * Subclasses should implement this method to return a name usable with
     * XPathRule for evaluating Element Names.
     */
    @Override
    public abstract String toString();

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean hasImageEqualTo(String image) {
        return this.getImage() != null && this.getImage().equals(image);
    }

    @Override
    public int getBeginLine() {
        return beginLine;
    }

    public void testingOnlySetBeginLine(int i) {
        this.beginLine = i;
    }

    @Override
    public int getBeginColumn() {
        if (beginColumn != -1) {
            return beginColumn;
        } else {
            if (children != null && children.length > 0) {
                return children[0].getBeginColumn();
            } else {
                throw new RuntimeException("Unable to determine beginning line of Node.");
            }
        }
    }

    public void testingOnlySetBeginColumn(int i) {
        this.beginColumn = i;
    }

    @Override
    public int getEndLine() {
        return endLine;
    }

    public void testingOnlySetEndLine(int i) {
        this.endLine = i;
    }

    @Override
    public int getEndColumn() {
        return endColumn;
    }

    public void testingOnlySetEndColumn(int i) {
        this.endColumn = i;
    }

    /**
     * Returns the n-th parent or null if there are not <code>n</code> ancestors
     *
     * @param n
     *            how many ancestors to iterate over.
     * @return the n-th parent or null.
     * @throws IllegalArgumentException
     *             if <code>n</code> is not positive.
     */
    @Override
    public Node getNthParent(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        Node result = this.jjtGetParent();
        for (int i = 1; i < n; i++) {
            if (result == null) {
                return null;
            }
            result = result.jjtGetParent();
        }
        return result;
    }

    /**
     * Traverses up the tree to find the first parent instance of type
     * parentType
     *
     * @param parentType
     *            class which you want to find.
     * @return Node of type parentType. Returns null if none found.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFirstParentOfType(Class<T> parentType) {
        Node parentNode = jjtGetParent();
        while (parentNode != null && parentNode.getClass() != parentType) {
            parentNode = parentNode.jjtGetParent();
        }
        return (T) parentNode;
    }

    /**
     * Traverses up the tree to find all of the parent instances of type
     * parentType
     *
     * @param parentType
     *            classes which you want to find.
     * @return List of parentType instances found.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getParentsOfType(Class<T> parentType) {
        List<T> parents = new ArrayList<>();
        Node parentNode = jjtGetParent();
        while (parentNode != null) {
            if (parentNode.getClass() == parentType) {
                parents.add((T) parentNode);
            }
            parentNode = parentNode.jjtGetParent();
        }
        return parents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> findDescendantsOfType(Class<T> targetType) {
        List<T> list = new ArrayList<>();
        findDescendantsOfType(this, targetType, list, true);
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void findDescendantsOfType(Class<T> targetType, List<T> results, boolean crossBoundaries) {
        findDescendantsOfType(this, targetType, results, crossBoundaries);
    }

    @SuppressWarnings("unchecked")
    private static <T> void findDescendantsOfType(Node node, Class<T> targetType, List<T> results,
            boolean crossFindBoundaries) {

        if (!crossFindBoundaries && node.isFindBoundary()) {
            return;
        }

        int n = node.jjtGetNumChildren();
        for (int i = 0; i < n; i++) {
            Node child = node.jjtGetChild(i);
            if (child.getClass() == targetType) {
                results.add((T) child);
            }

            findDescendantsOfType(child, targetType, results, crossFindBoundaries);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findChildrenOfType(Class<T> targetType) {
        List<T> list = new ArrayList<>();
        int n = jjtGetNumChildren();
        for (int i = 0; i < n; i++) {
            Node child = jjtGetChild(i);
            if (child.getClass() == targetType) {
                list.add((T) child);
            }
        }
        return list;
    }

    @Override
    public boolean isFindBoundary() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getFirstDescendantOfType(Class<T> descendantType) {
        return getFirstDescendantOfType(descendantType, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFirstChildOfType(Class<T> childType) {
        int n = jjtGetNumChildren();
        for (int i = 0; i < n; i++) {
            Node child = jjtGetChild(i);
            if (child.getClass() == childType) {
                return (T) child;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFirstDescendantOfType(Class<T> descendantType, Node node) {
        int n = node.jjtGetNumChildren();
        for (int i = 0; i < n; i++) {
            Node n1 = node.jjtGetChild(i);
            if (n1.getClass() == descendantType) {
                return (T) n1;
            }
            T n2 = getFirstDescendantOfType(descendantType, n1);
            if (n2 != null) {
                return n2;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> boolean hasDescendantOfType(Class<T> type) {
        return getFirstDescendantOfType(type) != null;
    }

    /**
     *
     * @param types
     * @return boolean
     */
    public final boolean hasDecendantOfAnyType(Class<?>... types) {
        for (Class<?> type : types) {
            if (hasDescendantOfType(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Node> findChildNodesWithXPath(String xpathString) {
        return new ArrayList<Node>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDescendantMatchingXPath(String xpathString) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getUserData() {
        return userData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public GenericToken jjtGetFirstToken() {
        return firstToken;
    }

    public void jjtSetFirstToken(GenericToken token) {
        this.firstToken = token;
    }

    public GenericToken jjtGetLastToken() {
        return lastToken;
    }

    public void jjtSetLastToken(GenericToken token) {
        this.lastToken = token;
    }
}
