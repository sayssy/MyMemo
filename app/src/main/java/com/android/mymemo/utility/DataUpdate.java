package com.android.mymemo.utility;

public class DataUpdate {
    private boolean dataUpdated;

    public DataUpdate(boolean dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    public boolean isDataUpdated() {
        if (dataUpdated){
            dataUpdated = false;
            return true;
        }
        return false;
    }

    public void setDataUpdated(boolean dataUpdated) {
        this.dataUpdated = dataUpdated;
    }
}
