package com.dbms.csmq.model.report;

import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri May 04 01:29:53 IST 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class MedraQueryReportRVOImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public MedraQueryReportRVOImpl() {
    }

    /**
     * Returns the bind variable value for bindStartDate.
     * @return bind variable value for bindStartDate
     */
    public String getbindStartDate() {
        return (String) getNamedWhereClauseParam("bindStartDate");
    }

    /**
     * Sets <code>value</code> for bind variable bindStartDate.
     * @param value value to bind as bindStartDate
     */
    public void setbindStartDate(String value) {
        setNamedWhereClauseParam("bindStartDate", value);
    }

    /**
     * Returns the bind variable value for bindEndDate.
     * @return bind variable value for bindEndDate
     */
    public String getbindEndDate() {
        return (String) getNamedWhereClauseParam("bindEndDate");
    }

    /**
     * Sets <code>value</code> for bind variable bindEndDate.
     * @param value value to bind as bindEndDate
     */
    public void setbindEndDate(String value) {
        setNamedWhereClauseParam("bindEndDate", value);
    }
}

