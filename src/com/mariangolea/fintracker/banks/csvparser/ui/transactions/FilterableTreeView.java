package com.mariangolea.fintracker.banks.csvparser.ui.transactions;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import javafx.scene.control.TreeView;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;

public class FilterableTreeView extends TreeView<BankTransactionGroupInterface> {

    public FilterableTreeView(FilterableTreeItem root) {
        super(root);
        root.setExpanded(true);
        setShowRoot(false);
    }

    @Override
    public FilterableTreeItem<BankTransactionGroupInterface> getTreeItem(int row) {
        return (FilterableTreeItem<BankTransactionGroupInterface>) super.getTreeItem(row);
    }

    public FilterableTreeItem<BankTransactionGroupInterface> getFilterableRoot() {
        return (FilterableTreeItem) getRoot();
    }
}
