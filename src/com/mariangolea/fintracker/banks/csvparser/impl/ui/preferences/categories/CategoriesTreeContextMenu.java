package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories.CategoriesTree;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.util.Pair;

public class CategoriesTreeContextMenu extends ContextMenu {

    private MenuItem delete;
    private MenuItem addChild;
    private TreeItem<Pair<String, Boolean>> category;
    private final UserPreferencesInterface prefs;

    public CategoriesTreeContextMenu(final UserPreferencesInterface prefs) {
        this.prefs= Objects.requireNonNull(prefs);
        createItems();
    }

    public void setCategory(final TreeItem<Pair<String, Boolean>> category) {
        this.category = category;
        boolean operationsDisallowed = category == null || category.getValue().getKey().equals(CategoriesTree.ROOT);
        addChild.setDisable(operationsDisallowed);
        delete.setDisable(operationsDisallowed);
    }

    protected final void createItems() {
        addChild = new MenuItem("Add Child");
        addChild.setOnAction(item -> addChild());

        delete = new MenuItem("Delete");
        delete.setOnAction(item -> deleteCategory());

        getItems().add(addChild);
        getItems().add(delete);
    }

    protected void deleteCategory() {
        category.getParent().getChildren().addAll(category.getChildren());
        category.getParent().getChildren().remove(category);
        
        final String categoryName = category.getValue().getKey();
        Collection<String> subCategories = prefs.getSubCategories(categoryName);
        final String parent = prefs.getParent(categoryName);
        prefs.removeSubCategory(parent, categoryName);
        prefs.appendDefinition(parent, subCategories);
    }

    protected void addChild() {
        TextInputDialog dialog = new TextInputDialog();
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            category.getChildren().add(new TreeItem<>(new Pair<>(name, Boolean.FALSE)));
        });
    }
}
