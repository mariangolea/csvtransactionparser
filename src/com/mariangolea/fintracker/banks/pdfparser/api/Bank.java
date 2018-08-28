package com.mariangolea.fintracker.banks.pdfparser.api;

import java.util.Locale;


/**
 * Enum of major banks (Romania for now).
 * <br> Each enum contains links to bank's swift code (needed to identify which
 * bank generated the pdf report), a dedicated parser object, and the locale
 * used to generate the report (impacts the way dates and currency amounts are
 * parsed).
 *
 * @author mariangolea@gmail.com
 */
public enum Bank {
    ING("INGBROBU", Locale.GERMAN),
    BRD("BRDEROBU", Locale.GERMAN),
    BT("BTRLRO22", Locale.GERMAN),
    BCR("RNCBROBU", Locale.GERMAN);

    public final String swiftCode;
    public final Locale locale;

    private Bank(final String swiftCode, final Locale locale) {
        this.swiftCode = swiftCode;
        this.locale = locale;
    }

}
