package com.mariangolea.fintracker.banks.csvparser.impl.ui.categorized.table;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParser;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;

public class TableViewData {

    public final SimpleObjectProperty<String> timeSlotString;
    public final SimpleObjectProperty<List<String>> amountStrings;
    public final int columns;
    private final Calendar cal = Calendar.getInstance(AbstractBankParser.ROMANIAN_LOCALE);
    private final DateFormat format = new SimpleDateFormat("MMMM-yyyy", AbstractBankParser.ROMANIAN_LOCALE);

    public TableViewData(final YearSlot timeSlot, final Collection<BankTransactionGroupInterface> groups) {
        this.timeSlotString = new SimpleObjectProperty<>(timeSlot.toString(cal, format));
        this.amountStrings = new SimpleObjectProperty<>();
        final List<String> amounts = new ArrayList<>();
        groups.forEach(group -> {
            amounts.add(group.getTotalAmount() + "");
            });
        columns = groups.size();
        amountStrings.set(amounts);
    }

    public String getTimeSlotString() {
        return timeSlotString.get();
    }

    public String getAmountString(int column) {
        return amountStrings.get().get(column);
    }

    public SimpleObjectProperty<List<String>> getAmountStrings() {
        return amountStrings;
    }
}
