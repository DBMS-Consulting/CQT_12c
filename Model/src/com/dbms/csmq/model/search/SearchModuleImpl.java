package com.dbms.csmq.model.search;


import com.dbms.csmq.model.search.common.SearchModule;

import java.math.BigDecimal;

import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewObjectImpl;


// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Aug 15 15:14:23 IST 2016
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class SearchModuleImpl extends ApplicationModuleImpl implements SearchModule {
    /**
     * This is the default constructor (do not remove).
     */
    public SearchModuleImpl() {
    }

    /**
     * Container's getter for SimpleSearch1.
     * @return SimpleSearch1
     */
    public ViewObjectImpl getSimpleSearch1() {
        return (ViewObjectImpl)findViewObject("SimpleSearch1");
    }

    /**
     * Container's getter for HierarchySourceTermSearchVO1.
     * @return HierarchySourceTermSearchVO1
     */
    public ViewObjectImpl getHierarchySourceTermSearchVO1() {
        return (ViewObjectImpl)findViewObject("HierarchySourceTermSearchVO1");
    }

    /**
     * Container's getter for HierarchyViewObj1.
     * @return HierarchyViewObj1
     */
    public ViewObjectImpl getHierarchyViewObj1() {
        return (ViewObjectImpl)findViewObject("HierarchyViewObj1");
    }

    /**
     * Container's getter for EntryTSListVO1.
     * @return EntryTSListVO1
     */
    public EntryTSListVOImpl getEntryTSListVO1() {
        return (EntryTSListVOImpl)findViewObject("EntryTSListVO1");
    }

    /**
     * Container's getter for HistoricSearch1.
     * @return HistoricSearch1
     */
    public ViewObjectImpl getHistoricSearch1() {
        return (ViewObjectImpl)findViewObject("HistoricSearch1");
    }

    /**
     * Container's getter for HistoricalListViewObj1.
     * @return HistoricalListViewObj1
     */
    public ViewObjectImpl getHistoricalListViewObj1() {
        return (ViewObjectImpl)findViewObject("HistoricalListViewObj1");
    }

    /**
     * Container's getter for HierarchySourceTermSearchFixVO1.
     * @return HierarchySourceTermSearchFixVO1
     */
    public HierarchySourceTermSearchFixVOImpl getHierarchySourceTermSearchFixVO1() {
        return (HierarchySourceTermSearchFixVOImpl)findViewObject("HierarchySourceTermSearchFixVO1");
    }

    /**
     * Container's getter for TermHistoricInfoVO1.
     * @return TermHistoricInfoVO1
     */
    public ViewObjectImpl getTermHistoricInfoVO1() {
        return (ViewObjectImpl)findViewObject("TermHistoricInfoVO1");
    }
    
    public void loadHistoricTermInformation(BigDecimal dictContentId, String effectiveDTStr){
        System.out.println("loadHistoricTermInformation() dictContentId --> " + dictContentId +";; effectiveDTStr --> "+effectiveDTStr);
        ViewObjectImpl termHistoricInfoVO = getTermHistoricInfoVO1();
        termHistoricInfoVO.setNamedWhereClauseParam("bindContentID", dictContentId);
        termHistoricInfoVO.setNamedWhereClauseParam("bindEffectiveDT", effectiveDTStr);
        termHistoricInfoVO.executeQuery();
        System.out.println("loadHistoricTermInformation() termHistoricInfoVO.getEstimatedRowCount()--> " + termHistoricInfoVO.getEstimatedRowCount());
        System.out.println("loadHistoricTermInformation() QUERY --> " + termHistoricInfoVO.getQuery());        
    }

    /**
     * Container's getter for TermHistoryHierarchyVO1.
     * @return TermHistoryHierarchyVO1
     */
    public ViewObjectImpl getTermHistoryHierarchyVO1() {
        return (ViewObjectImpl)findViewObject("TermHistoryHierarchyVO1");
    }
    
    public void loadHistoricTermHierarchyInformation(BigDecimal dictContentId, String effectiveDTStr){
        System.out.println("loadHistoricTermHierarchyInformation() dictContentId --> " + dictContentId +";; effectiveDTStr --> "+effectiveDTStr);
        ViewObjectImpl termHistoryHierarchyVO1 = getTermHistoryHierarchyVO1();
        termHistoryHierarchyVO1.setNamedWhereClauseParam("bindContentID", dictContentId);
        termHistoryHierarchyVO1.setNamedWhereClauseParam("bindEffectiveDT", effectiveDTStr);
        termHistoryHierarchyVO1.executeQuery();
        System.out.println("loadHistoricTermHierarchyInformation() termHistoricInfoVO.getEstimatedRowCount()--> " + termHistoryHierarchyVO1.getEstimatedRowCount());
        System.out.println("loadHistoricTermHierarchyInformation() QUERY --> " + termHistoryHierarchyVO1.getQuery());        
    }

    /**
     * Container's getter for TermDetailsVO1.
     * @return TermDetailsVO1
     */
    public ViewObjectImpl getTermDetailsVO1() {
        return (ViewObjectImpl)findViewObject("TermDetailsVO1");
    }

    /**
     * Container's getter for TermHierarchyDetailsVO1.
     * @return TermHierarchyDetailsVO1
     */
    public ViewObjectImpl getTermHierarchyDetailsVO1() {
        return (ViewObjectImpl)findViewObject("TermHierarchyDetailsVO1");
    }
    
    public void loadMQDetailedDetailedTermHierarchyInformation(BigDecimal dictContentId, String tMSRecordStatus, String activationGroup) {
        System.out.println("loadMQDetailedDetailedTermHierarchyInformation() dictContentId --> " + dictContentId);
        System.out.println("tMSRecordStatus --> " + tMSRecordStatus + ";;; activationGroup --> " + activationGroup);
        ViewObjectImpl termDetailsVO = getTermDetailsVO1();
        termDetailsVO.setNamedWhereClauseParam("bindDictContentId", dictContentId);
        termDetailsVO.setNamedWhereClauseParam("bindTMSRecordStatus", tMSRecordStatus);
        termDetailsVO.setNamedWhereClauseParam("bindActivationGroup", activationGroup);
        termDetailsVO.executeQuery();

//        ViewObjectImpl termHierarchyDetailsVO = getTermHierarchyDetailsVO1();
//        termHierarchyDetailsVO.setNamedWhereClauseParam("bindDictContentId", dictContentId);
//        termHierarchyDetailsVO.setNamedWhereClauseParam("bindTMSRecordStatus", tMSRecordStatus);
//        termHierarchyDetailsVO.setNamedWhereClauseParam("bindActivationGroup", activationGroup);
//        termHierarchyDetailsVO.executeQuery();
        System.out.println("End of loadMQDetailedDetailedTermHierarchyInformation()  ");
    }
}
