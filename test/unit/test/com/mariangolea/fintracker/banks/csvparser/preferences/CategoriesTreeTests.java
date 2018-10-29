package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import com.mariangolea.fintracker.banks.csvparser.preferences.CategoriesTree;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class CategoriesTreeTests {

    @Test
    public void testConstructor() {
        CategoriesTree root = new CategoriesTree();
        assertEquals(CategoriesTree.ROOT, root.categoryName);
        assertNull(root.getParentCategory());

        CategoriesTree child = new CategoriesTree("child");
        assertEquals("child", child.categoryName);
        assertNull(root.getParentCategory());
    }

    @Test
    public void testGetCategory() {
        CategoriesTree root = new CategoriesTree();
        root.addSubCategories(Arrays.asList("one", "two"));

        CategoriesTree sub = root.getCategory("one");
        assertNotNull(sub);

        sub = root.getCategory("two");
        assertNotNull(sub);

        sub = root.getCategory("three");
        assertNull(sub);
    }

    @Test
    public void testGetParentCategory() {
        CategoriesTree root = new CategoriesTree();
        root.addSubCategories(Arrays.asList("one", "two"));
        CategoriesTree sub = root.getCategory("one");
        assertEquals(root, sub.getParentCategory());
    }

    @Test
    public void testGetAllSubCategoryNames() {
        CategoriesTree root = new CategoriesTree();
        root.addSubCategories(Arrays.asList("one", "two"));
        Collection<String> sub = root.getAllSubCategoryNames();
        assertEquals(Arrays.asList("one", "two"), sub);

        Collection<String> node = root.getNodeSubCategoryNames();
        assertEquals(sub, node);

        CategoriesTree child = root.getCategory("one");
        child.addSubCategories(Arrays.asList("three"));

        sub = root.getAllSubCategoryNames();
        assertEquals(Arrays.asList("one", "three", "two"), sub);
    }
}
