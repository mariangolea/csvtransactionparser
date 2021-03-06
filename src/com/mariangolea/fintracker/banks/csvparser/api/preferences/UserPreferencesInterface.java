package com.mariangolea.fintracker.banks.csvparser.api.preferences;

import java.util.Calendar;

public interface UserPreferencesInterface extends CategoriesInterface{

    public enum Timeframe {
        MONTH(Calendar.MONTH),
        YEAR(Calendar.YEAR);

        public final int timeFrame;

        private Timeframe(int timeframe) {
            this.timeFrame = timeframe;
        }
    }


    public String getCSVInputFolder();

    public void setCSVInputFolder(final String csvInputFolder);

    public Timeframe getTransactionGroupingTimeframe();

    public void setTransactionGroupingTimeframe(Timeframe timeframe);
    
    public UserPreferencesInterface deepClone();
}
