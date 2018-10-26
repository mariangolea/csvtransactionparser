package com.mariangolea.fintracker.banks.csvparser.ui.categorized.table;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.TransactionsCategorizedSlotter;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Collection;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TransactionTableView extends TableView<TableViewData> {

    private final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedModel = FXCollections.observableHashMap();
    protected final Collection<BankTransaction> model;
    private final ObservableList<TableViewData> data = FXCollections.observableArrayList();
    private final UserPreferences prefs =  UserPreferencesHandler.INSTANCE.getPreferences();

    public TransactionTableView(final Collection<BankTransaction> model) {
        this.model = model;
    }

    protected void constructColumns(final Collection<String> columnHeaders) {
        getColumns().clear();
        getColumns().add(new TableColumn(""));
        columnHeaders.forEach(header -> {
            TableColumn col = new TableColumn<>(header);
            getColumns().add(col);
        });
    }

    public void resetView() {
        data.clear();
        
        Collection<String> topMostCategories = prefs.getTopMostCategories();
        TransactionsCategorizedSlotter categorizer = new TransactionsCategorizedSlotter(model, prefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> incomming = categorizer.getUnmodifiableSlottedCategorized();
        //prepare view for new columns.
        constructColumns(topMostCategories);
        
        slottedModel.clear();
        slottedModel.putAll(categorizer.getUnmodifiableSlottedCategorized());

        slottedModel.entrySet().forEach(entry ->{
            data.add(new TableViewData(entry.getKey(), entry.getValue()));
        });
        
        setItems(data);
    }
}
