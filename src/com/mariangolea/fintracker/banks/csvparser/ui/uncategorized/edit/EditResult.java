package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit;

import javafx.util.Pair;

public class EditResult {
    public final Pair<String,String> companyNameDefinition;
    public final Pair<String,String> categoryNameDefinition;

    public EditResult(Pair<String, String> companyNameDefinition, Pair<String, String> categoryNameDefinition) {
        this.companyNameDefinition = companyNameDefinition;
        this.categoryNameDefinition = categoryNameDefinition;
    }
}
