package com.mariangolea.fintracker.banks.pdfparser.api;

/**
 * Default transaction categories.
 * <br> Used by the application to automatically associate similar transactions from
 * different banks.
 *
 * @author mariangolea@gmail.com
 */
public enum DefaultTransactionCategories {
    INCOME_SALARY("Salary income"),
    INCOME_OTHER("Other income"),
    UTILITIES("Utilities"),
    FOOD("Food"),
    CLOTHES("Clothes"),
    CAR("Car payments"),
    VACATION("Vacation"),
    EDUCATION("Education"),
    INSURANCE("Insurance"),
    OTHER("Other not specified");
    
    private final String desc;

    private DefaultTransactionCategories(final String desc) {
        this.desc = desc;
    }
    
}
