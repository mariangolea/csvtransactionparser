package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import static java.awt.SystemColor.menu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class CategoriesTreeView extends TreeView<Pair<String, Boolean>> {

    private final CategoriesTreeContextMenu contextMenu;

    public CategoriesTreeView(final TreeItem<Pair<String, Boolean>> root, final UserPreferencesInterface prefs) {
        super(root);
        root.setExpanded(true);
        setShowRoot(false);
        setCellFactory(new CategoriesTreeCellFactory(prefs));
        contextMenu = new CategoriesTreeContextMenu(prefs);
        setContextMenu(contextMenu);
        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                TreeItem<Pair<String, Boolean>> selected = getSelectionModel().getSelectedItem();
                contextMenu.setCategory(selected);
            } 
        });
    }
}
