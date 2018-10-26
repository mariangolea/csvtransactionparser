package com.mariangolea.fintracker.banks.csvparser.ui.categorized.table;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.util.Collection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TableViewData {
        public final SimpleObjectProperty<YearSlot> timeSlot;
        public final ObservableList<SimpleObjectProperty<BankTransactionGroupInterface>> data;
        public final int columns;

    public TableViewData(final YearSlot timeSlot, final Collection<BankTransactionGroupInterface> groups) {
        this.timeSlot = new SimpleObjectProperty<>(timeSlot);
        this.data = FXCollections.observableArrayList();
        groups.forEach((group) -> {
            data.add(new SimpleObjectProperty<>(group));
            });
        columns = groups.size();
    }
        
        
        
}
