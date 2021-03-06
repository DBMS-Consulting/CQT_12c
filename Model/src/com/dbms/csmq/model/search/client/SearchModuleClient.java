package com.dbms.csmq.model.search.client;

import com.dbms.csmq.model.search.common.SearchModule;

import java.math.BigDecimal;

import oracle.jbo.client.remote.ApplicationModuleImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Aug 15 15:18:22 IST 2016
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class SearchModuleClient extends ApplicationModuleImpl implements SearchModule {
    /**
     * This is the default constructor (do not remove).
     */
    public SearchModuleClient() {
    }


    public void loadMQDetailedDetailedTermHierarchyInformation(BigDecimal dictContentId) {
        Object _ret =
            this.riInvokeExportedMethod(this,"loadMQDetailedDetailedTermHierarchyInformation",new String [] {"java.math.BigDecimal"},new Object[] {dictContentId});
        return;
    }

    public void loadHistoricTermHierarchyInformation(BigDecimal dictContentId, String effectiveDTStr) {
        Object _ret =
            this.riInvokeExportedMethod(this,"loadHistoricTermHierarchyInformation",new String [] {"java.math.BigDecimal","java.lang.String"},new Object[] {dictContentId, effectiveDTStr});
        return;
    }

    public void loadHistoricTermInformation(BigDecimal dictContentId, String effectiveDTStr) {
        Object _ret =
            this.riInvokeExportedMethod(this,"loadHistoricTermInformation",new String [] {"java.math.BigDecimal","java.lang.String"},new Object[] {dictContentId, effectiveDTStr});
        return;
    }

    public void loadMQDetailedDetailedTermHierarchyInformation(BigDecimal dictContentId, String tMSRecordStatus,
                                                               String activationGroup) {
        Object _ret =
            this.riInvokeExportedMethod(this,"loadMQDetailedDetailedTermHierarchyInformation",new String [] {"java.math.BigDecimal","java.lang.String","java.lang.String"},new Object[] {dictContentId, tMSRecordStatus, activationGroup});
        return;
    }
}
