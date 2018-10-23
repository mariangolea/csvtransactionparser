package com.mariangolea.fintracker.banks.csvparser.ui.transactions;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.TransactionCategorizer;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Collection;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TransactionTableView extends TableView<BankTransactionGroupInterface> {

    private Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedModel = FXCollections.observableHashMap();
    protected final Collection<BankTransaction> model;

    public TransactionTableView(final Collection<BankTransaction> model) {
        this.model = model;
    }

    protected final void constructView() {
        TableColumn firstNameCol = new TableColumn("First Name");
        TableColumn lastNameCol = new TableColumn("Last Name");
        TableColumn emailCol = new TableColumn("Email");

        getColumns().addAll(firstNameCol, lastNameCol, emailCol);
    }

    protected void constructColumns(Collection<String> columnHeaders) {
        getColumns().clear();
        columnHeaders.forEach(header -> {
            getColumns().add(new TableColumn<>(header));
        });
    }

    public void resetView() {
        UserPreferences prefs =  UserPreferencesHandler.INSTANCE.getPreferences();
        Collection<String> topMostCategories = prefs.getTopMostCategories();
        
        TransactionCategorizer categorizer = new TransactionCategorizer(model, prefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> incomming = categorizer.categorize();
        //prepare view for new columns.
        constructColumns(topMostCategories);
        
        slottedModel.clear();
        slottedModel.putAll(categorizer.categorize());

    }

}
