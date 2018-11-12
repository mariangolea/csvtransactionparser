package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories.CategoriesTree;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.PickResult;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

public class CategoriesTreeView extends TreeView<String> {

    public CategoriesTreeView(final TreeItem<String> root) {
        super(root);
        root.setExpanded(true);
        setShowRoot(false);
        setCellFactory(new CellFactory());
    }

    protected class CellFactory implements Callback<TreeView<String>, TreeCell<String>> {

        @Override
        public TreeCell<String> call(final TreeView<String> param) {
            TreeCell<String> treeCell = new TreeCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        setText(item);
                        setGraphic(getTreeItem().getGraphic());
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };

            treeCell.setOnDragDetected(event -> {
                treeCell.setStyle("-fx-background-color: transparent;");
            });

            treeCell.setOnDragEntered(event -> {
            });
            
            treeCell.setOnDragOver(event -> {
            });
            
            treeCell.setOnDragOver(event -> {
            });
            
            treeCell.setOnDragDropped(event -> {
                event.acceptTransferModes(TransferMode.MOVE);
                CategoriesTree source = (CategoriesTree)event.getGestureSource();
                PickResult result = event.getPickResult();
                Object accepting = event.getAcceptingObject();
                int i= 0;
            });
            return treeCell;
        }

    }
}
