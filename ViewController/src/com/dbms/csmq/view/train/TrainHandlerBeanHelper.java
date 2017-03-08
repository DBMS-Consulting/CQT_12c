package com.dbms.csmq.view.train;

public class TrainHandlerBeanHelper {
    public TrainHandlerBeanHelper() {
        super();
    }

    private String selectedTrainStopOutcome = null;

    public void setSelectedTrainStopOutcome(String selectedTrainStopOutcome) {
        this.selectedTrainStopOutcome = selectedTrainStopOutcome;
    }

    public String getSelectedTrainStopOutcome() {
        return selectedTrainStopOutcome;
    }
}
