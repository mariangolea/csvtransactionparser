package com.mariangolea.fintracker.banks.csvparser.impl.ui.categorized.table;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParser;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionTableView extends TableView<TableViewData> {

    private final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedModel = FXCollections.observableHashMap();
    private final ObservableList<TableViewData> data = FXCollections.observableArrayList();
    private final UserPreferencesInterface prefs;
    private final Calendar cal = Calendar.getInstance(AbstractBankParser.ROMANIAN_LOCALE);
    private final DateFormat format = new SimpleDateFormat("MMMM-yyyy", AbstractBankParser.ROMANIAN_LOCALE);

    public TransactionTableView(final UserPreferencesInterface prefs) {
        this.prefs = Objects.requireNonNull(prefs);
    }

    protected void constructColumns(Map<String, Collection<String>> categoriesMap, final Set<YearSlot> timeSlots) {
        getColumns().clear();

        TableColumn<TableViewData, String> topMostCategory = new TableColumn("");
        topMostCategory.setCellValueFactory(new PropertyValueFactory<>("topMostCategoryString"));
        getColumns().add(topMostCategory);
        TableColumn<TableViewData, String> subCategory = new TableColumn("");
        subCategory.setCellValueFactory(new PropertyValueFactory<>("subCategoryString"));
        getColumns().add(subCategory);

        final List<YearSlot> indexed = new ArrayList<>(timeSlots);
        for (YearSlot timeSlot : indexed) {
            TableColumn<TableViewData, SimpleObjectProperty<List<String>>> col = new TableColumn<>(timeSlot.toString(cal, format));
            col.setCellFactory(callback -> {
                return new AmountStringPropertyValueFactory(indexed.indexOf(timeSlot));
            });
            col.setCellValueFactory(
                    new PropertyValueFactory("amountStrings"));
            getColumns().add(col);
        }
    }

    public void resetView(Map<YearSlot, Collection<BankTransactionGroupInterface>> incomming) {
        data.clear();

        Collection<String> topMostCategories = prefs.getTopMostCategories();
        Map<String, Collection<String>> categoriesMap = FXCollections.observableHashMap();
        topMostCategories.forEach(topMostCategory -> {
            categoriesMap.put(topMostCategory, prefs.getSubCategories(topMostCategory));
        });

        slottedModel.clear();
        slottedModel.putAll(incomming);

        //prepare view for new columns.
        constructColumns(categoriesMap, slottedModel.keySet());

        categoriesMap.entrySet().forEach(topMostCategoryAssociation -> {
            data.add(new TableViewData(topMostCategoryAssociation.getKey(), true, slottedModel, prefs));
            topMostCategoryAssociation.getValue().forEach(subCategory -> {
                data.add(new TableViewData(subCategory, false, slottedModel, prefs));
            });
        });

        setItems(data);
    }

    public static class AmountStringPropertyValueFactory extends TableCell<TableViewData, SimpleObjectProperty<List<String>>> {

        private final int col;

        public AmountStringPropertyValueFactory(int column) {
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
