package com.dbms.csmq.view.backing.NMQ;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

public class NMQSourceTermSearchUIBean {
    
    
    
    private RichSelectOneChoice controlReleaseGroup;
    private RichInputText controlTerm;
    private RichTable controlResultsTable;
    private RichSelectOneChoice controlDictionary;
    private RichSelectOneChoice cntrlSortList;
    private RichSelectOneChoice cntrlScope;
    private RichPopup searchPopUp;
    private RichSelectBooleanCheckbox ctrlNewTermsOnly;
    private RichSelectBooleanCheckbox ctrlReturnPrimLinkPath;
    private RichSelectBooleanCheckbox cntrlNarrowScope;
    
    private RichPanelGroupLayout cntrlResultsPanel;
    
   

    

    public void setControlReleaseGroup(RichSelectOneChoice controlReleaseGroup) {
        this.controlReleaseGroup = controlReleaseGroup;
    }

    public RichSelectOneChoice getControlReleaseGroup() {
        return controlReleaseGroup;
    }

    public void setControlTerm(RichInputText controlTerm) {
        this.controlTerm = controlTerm;
    }

    public RichInputText getControlTerm() {
        return controlTerm;
    }

    public void setControlResultsTable(RichTable controlResultsTable) {
        this.controlResultsTable = controlResultsTable;
    }

    public RichTable getControlResultsTable() {
        return controlResultsTable;
    }
    
    public void setControlDictionary(RichSelectOneChoice controlDictionary) {
        this.controlDictionary = controlDictionary;
    }

    public RichSelectOneChoice getControlDictionary() {
        return controlDictionary;
    }
    
   
    public void setCntrlSortList(RichSelectOneChoice cntrlSortList) {
        this.cntrlSortList = cntrlSortList;
    }

    public RichSelectOneChoice getCntrlSortList() {
        return cntrlSortList;
    }

    public void setCntrlScope(RichSelectOneChoice cntrlScope) {
        this.cntrlScope = cntrlScope;
    }

    public RichSelectOneChoice getCntrlScope() {
        return cntrlScope;
    }
   
    public NMQSourceTermSearchUIBean() {
        super();
    }
    public void setCntrlNarrowScope(RichSelectBooleanCheckbox cntrlNarrowScope) {
        this.cntrlNarrowScope = cntrlNarrowScope;
    }

    public RichSelectBooleanCheckbox getCntrlNarrowScope() {
        return cntrlNarrowScope;
    }
    
    public void setCntrlResultsPanel(RichPanelGroupLayout cntrlResultsPanel) {
        this.cntrlResultsPanel = cntrlResultsPanel;
    }

    public RichPanelGroupLayout getCntrlResultsPanel() {
        return cntrlResultsPanel;
    }
    
    public void setSearchPopUp(RichPopup searchPopUp) {
        this.searchPopUp = searchPopUp;
    }

    public RichPopup getSearchPopUp() {
        return searchPopUp;
    }
    public void setCtrlNewTermsOnly(RichSelectBooleanCheckbox ctrlNewTermsOnly) {
        this.ctrlNewTermsOnly = ctrlNewTermsOnly;
    }

    public RichSelectBooleanCheckbox getCtrlNewTermsOnly() {
        return ctrlNewTermsOnly;
    }
    
    public void setCtrlReturnPrimLinkPath(RichSelectBooleanCheckbox ctrlreturnPrimLinkPath) {
        this.ctrlReturnPrimLinkPath = ctrlreturnPrimLinkPath;
    }

    public RichSelectBooleanCheckbox getCtrlReturnPrimLinkPath() {
        return ctrlReturnPrimLinkPath;
    }
    
}
