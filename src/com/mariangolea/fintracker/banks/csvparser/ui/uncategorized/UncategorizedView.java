package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;

public class UncategorizedView extends BorderPane{
    private final ObservableList<BankTransactionGroupInterface> model = FXCollections.observableArrayList();
    private final Label responseLabel = new Label();

    public UncategorizedView() {
        setTop(responseLabel);
        setCenter(createTransactionView());
    }
    
    private ScrollPane createTransactionView() {
        ListView<BankTransactionGroupInterface> listView = new ListView<>(model);
        model.addListener(new UncategorizedTransactionGroupListSelectionListener(listView, responseLabel));
        listView.setCellFactory((ListView<BankTransactionGroupInterface> param) -> {
            return new UncategorizedTransactionGroupCellRenderer(param);
        });
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }
}
