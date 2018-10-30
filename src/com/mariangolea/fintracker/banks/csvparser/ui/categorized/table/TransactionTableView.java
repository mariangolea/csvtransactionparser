package com.mariangolea.fintracker.banks.csvparser.ui.categorized.table;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionTableView extends TableView<TableViewData> {

    private final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedModel = FXCollections.observableHashMap();
    protected final Collection<BankTransaction> model;
    private final ObservableList<TableViewData> data = FXCollections.observableArrayList();
    private final UserPreferences prefs = UserPreferencesHandler.INSTANCE.getPreferences();

    public TransactionTableView(final Collection<BankTransaction> model) {
        this.model = model;
    }

    protected void constructColumns(final Collection<String> columnHeaders) {
        getColumns().clear();

        TableColumn<TableViewData, String> timeSlot = new TableColumn("");
        timeSlot.setCellValueFactory(new PropertyValueFactory<>("timeSlotString"));
        getColumns().add(timeSlot);
        final List<String> indexed = new ArrayList<>(columnHeaders);
        for (String header : columnHeaders) {
            TableColumn<TableViewData, SimpleObjectProperty<List<String>>> col = new TableColumn<>(header);
            col.setCellFactory(column -> {
                return new PropertyValueFactoryHack(indexed.indexOf(header));
            });
            col.setCellValueFactory(
                    new PropertyValueFactory("amountStrings"));
            getColumns().add(col);
        }
    }

    public void resetView(Map<YearSlot, Collection<BankTransactionGroupInterface>> incomming) {
        data.clear();

        Collection<String> topMostCategories = prefs.getTopMostCategories();
        //prepare view for new columns.
        constructColumns(topMostCategories);

        slottedModel.clear();
        slottedModel.putAll(incomming);

        slottedModel.entrySet().forEach(entry -> {
            data.add(new TableViewData(entry.getKey(), entry.getValue()));
        });

        setItems(data);
    }

    private class PropertyValueFactoryHack extends TableCell<TableViewData, SimpleObjectProperty<List<String>>> {

        private final int col;

        public PropertyValueFactoryHack(int column) {
            col = column;
        }

        @Override
        protected void updateItem(SimpleObjectProperty<List<String>> item, boolean empty) {
            if (item == null || empty) {
                setText(null);
                setStyle("");
            } else {
                // Format date.
                setText(item.get().get(col));
            }
        }

    }
}
