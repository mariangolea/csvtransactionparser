package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit;

public class EditResult {
    public final String companyIdentifierString;
    public final String companyDisplayName;
    public final String categoryName;
    public final String parentCategory;

    public EditResult(String companyIdentifierString, String companyName, String categoryName, String parentCategory) {
        this.companyDisplayName = companyName;
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
        this.companyIdentifierString = companyIdentifierString;
    }

}
