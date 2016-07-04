package com.dbms.csmq.view.diff;

import oracle.adf.view.rich.component.rich.data.RichTreeTable;

public class DiffUIBean {
    private RichTreeTable cntrlDiffResults;

    public DiffUIBean() {
        super();
    }

    public void setCntrlDiffResults(RichTreeTable cntrlDiffResults) {
        this.cntrlDiffResults = cntrlDiffResults;
    }

    public RichTreeTable getCntrlDiffResults() {
        return cntrlDiffResults;
    }
}
