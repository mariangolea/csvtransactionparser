package com.mariangolea.fintracker.banks.csvparser.ui.transactions;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.math.BigDecimal;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class FilterableTreeView extends TreeView<BankTransactionGroupInterface> {

    public FilterableTreeView(FilterableTreeItem root) {
        super(root);
        root.setExpanded(true);
        setShowRoot(false);
    }

    @Override
    public FilterableTreeItem getTreeItem(int row) {
        return (FilterableTreeItem) super.getTreeItem(row);
    }

    public FilterableTreeItem getFilterableRoot() {
        return (FilterableTreeItem) getRoot();
    }

    public static class FilterableTreeItem extends TreeItem<BankTransactionGroupInterface> {

        private final ObservableList<FilterableTreeItem> sourceList = FXCollections.observableArrayList();
        private final FilteredList<FilterableTreeItem> filteredChildren = new FilteredList<>(sourceList);
        private final ObjectProperty<Predicate<BankTransactionGroupInterface>> predicate = new SimpleObjectProperty<>();

        public FilterableTreeItem(BankTransactionGroupInterface value) {
            super(value);
            filteredChildren.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                Predicate<TreeItem<BankTransactionGroupInterface>> p = child -> {
                    if (child instanceof FilterableTreeItem) {
                        ((FilterableTreeItem) child).predicateProperty().set(predicate.get());
                    }
                    if (predicate.get() == null || !child.getChildren().isEmpty()) {
                        return true;
                    }
                    return predicate.get().test(child.getValue());
                };
                return p;
            }, predicate));

            filteredChildren.addListener((ListChangeListener<FilterableTreeItem>) c -> {
                while (c.next()) {
                    getChildren().removeAll(c.getRemoved());
                    getChildren().addAll(c.getAddedSubList());
                }
            });
        }

        public BigDecimal getTotalAmount() {
            if (sourceList == null || sourceList.isEmpty()) {
                return getValue() == null ? BigDecimal.ZERO : getValue().getTotalAmount();
            } else {
                BigDecimal amount = BigDecimal.ZERO;
                sourceList.forEach((item) -> {
                    BankTransactionGroupInterface value = item.getValue();
                    if (value != null) {
                        amount.add(item.getValue().getTotalAmount());
                    }
                });

                return amount;
            }
        }

        public ObservableList<FilterableTreeItem> getFilteredChildren() {
            return sourceList;
        }

        public ObjectProperty<Predicate<BankTransactionGroupInterface>> predicateProperty() {
            return predicate;
        }
    }

}
