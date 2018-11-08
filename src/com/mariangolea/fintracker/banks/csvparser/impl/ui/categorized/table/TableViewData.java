package com.mariangolea.fintracker.banks.csvparser.impl.ui.categorized.table;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Container for a row of data in a table which will be presented as follows:
 * <pre>
 *                          june    may
 * topMostCat1              20      100
 *              subCat1     15        3
 *              subcat2      5       97
 * topMostCat2              100       9
 *              subCat3      40       1
 *              subCat4      60       8
 * </pre>
 *
 *
 */
public class TableViewData {

    public final SimpleObjectProperty<String> topMostCategoryString;
    public final SimpleObjectProperty<String> subCategoryString;
    public final SimpleObjectProperty<List<String>> amountStrings;
    private final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedModel;
    private final UserPreferencesInterface prefs;

    public TableViewData(final String category, boolean isTopMostCategory, final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedModel, final UserPreferencesInterface prefs) {
        this.prefs = Objects.requireNonNull(prefs);
        this.slottedModel = Objects.requireNonNull(slottedModel);
        this.topMostCategoryString = new SimpleObjectProperty<>(isTopMostCategory ? Objects.requireNonNull(category) : "");
        this.subCategoryString = new SimpleObjectProperty<>(isTopMostCategory ? "" : category);
        this.amountStrings = new SimpleObjectProperty<>();
        final List<String> amounts = new ArrayList<>();
        Objects.requireNonNull(slottedModel).keySet().forEach(timeSlot -> {
            amounts.add(getAmountStringFor(category, timeSlot));
            Collection<String> subCategories = prefs.getSubCategories(category);
            subCategories.forEach(subCategory ->{
                amounts.add(getAmountStringFor(subCategory, timeSlot));
            });
        });
        amountStrings.set(amounts);
    }

    public String getTopMostCategoryString() {
        return topMostCategoryString.get();
    }

    public String getSubCategoryString() {
        return subCategoryString.get();
    }

    public String getAmountString(int column) {
        return amountStrings.get().get(column);
    }

    public SimpleObjectProperty<List<String>> getAmountStrings() {
        return amountStrings;
    }

    protected String getAmountStringFor(final String category, final YearSlot slot) {
        String result = "";
        Collection<BankTransactionGroupInterface> groups = slottedModel.get(slot);
        for (BankTransactionGroupInterface group : groups) {
            BankTransactionGroupInterface match = findSubGroup(group, category);
            if (match != null) {
                return match.getTotalAmount().toPlainString();
            }
        }
        return result;
    }

    protected BankTransactionGroupInterface findSubGroup(BankTransactionGroupInterface target, String category) {
        if (category.equals(target.getCategoryName())) {
            return target;
        }
        for (BankTransactionGroupInterface subGroup : target.getContainedGroups()) {
            if (category.equals(subGroup.getCategoryName())) {
                return subGroup;
            }
        }

        return null;
    }
}
