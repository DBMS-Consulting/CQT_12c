package com.dbms.csmq.view.backing.impact;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.view.hierarchy.HierarchyAccessor;
import com.dbms.csmq.view.hierarchy.NewPTListBean;
import com.dbms.csmq.view.impact.CurrentImpactHierarchyBean;
import com.dbms.csmq.view.impact.HistoricalImpactHierarchyBean;

import com.dbms.util.Utils;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichStatusIndicator;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DropEvent;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;


public class ImpactSearchBean extends HierarchyAccessor {

    private RichSelectOneChoice ctrlReleaseStatus;
    private RichInputDate ctrlStartDate;
    private RichInputDate ctrlEndDate;
    private RichSelectManyChoice ctrlState;
    private RichSelectManyChoice ctrlMQGroups;
    private RichSelectOneChoice ctrlReleaseGroupSearch;
    private RichSelectOneChoice ctrlDictionaryListSearch;
    private RichTable ctrlSearchResults;
    private RichInputText ctrlDictionaryVersion;
    private RichInputText ctrlMQAlgorithm;
    private RichSelectManyChoice ctrlProductList;
    private RichInputText ctrlMQName;
    private RichSelectOneChoice ctrlLevelList;
    private RichInputText ctrlMQCode;
    private RichSelectOneChoice ctrlDictionaryTypeSearch;
    private RichSelectOneChoice ctrlDictionary;
    private RichStatusIndicator ctrlStatusIndicator;
    private RichSelectOneChoice ctrlQueryType;
    private RichSelectOneChoice ctrlNMQStatus;
    private RichSelectOneChoice ctrlMQScope;



    //SEARCH PARAMS
    private String paramDictName;
    private String paramStartDate;
    private String paramEndDate;
    private String paramTerm;
    private String paramReleaseStatus;
    private String paramActivityStatus;
    private String paramState;
    private String paramReleaseGroup;
    private String paramMQGroupList;
    private String paramMQCode;
    private String paramMQCriticalEvent;
    private String paramUserName;
    private String paramUniqueIDsOnly = CSMQBean.TRUE;
    private String paramFilterForUser;
    private String paramQueryType = CSMQBean.SMQ_SEARCH;
    private Integer paramKillSwitch = CSMQBean.KILL_SWITCH_ON;
    

    // ** CURRENT SELECTED DATA
    private String currentDictContentID = "999999";
    private String currentReleaseGroup;
    private String currentTermName;
    private String currentMqlevel;
    private String currentMqcode = "%";
    private String currentMqalgo;
    private String currentMqaltcode;
    private String currentDictionary;
    private String currentMqstatus;
    private String currentRequestor;
    private Date currentDateRequested;
    private String currentCriticalEvent;
    private String currentApprovalFlag;
    private String currentSubType;
    private String currentVersion;
    private String currentMqscp;
    private String currentMqgroups;
    private String currentMqproduct;
    private String currentDictionaryType;
   
    private List <String> mQGroupList = new ArrayList <String>();
    

    // ** CURRENT INF NOTES
    private String currentInfNoteDescription;
    private String currentInfNoteSource;
    private String currentInfNoteNotes;


    private Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
    private RichSelectOneChoice ctrlCriticalEvent;
    private String showNonImpacted = CSMQBean.TRUE;
    private boolean editable = false;
    private String mqType;
    private String activationGroups; 
    
    ///  **********************   FOR HISTORICAL IMPACT
    private RichSelectOneChoice ctrlEntryTS;
    private String historicalAsOfDate = CSMQBean.getProperty("END_TS_CONSTANT");
    private RichSelectOneChoice ctrlEndTS;
    private String selectedHistoricalDate;
    private RichTreeTable historicalTree;
    private RichTreeTable currentTree;
    private RichSelectOneChoice ctrlImpact;
    
    private RichSelectOneChoice cntlSOCList;
    private String parentSOCcontentID;
   
    private RichTreeTable preferedTermSourceTree;
    private RichTreeTable hierarchySourceTree;
    private String showImpact;

    private boolean showReactivateButton = false;
    private String asOfDate = CSMQBean.getProperty("END_TS_CONSTANT");
    private RichSelectOneChoice cntrlShowImpacted;
    CurrentImpactHierarchyBean currentImpactHierarchyBean;


    public void setCtrlDictionaryTypeSearch(RichSelectOneChoice dictionaryTypeSearch) {
        this.ctrlDictionaryTypeSearch = dictionaryTypeSearch;
    }

    public RichSelectOneChoice getCtrlDictionaryTypeSearch() {
        return ctrlDictionaryTypeSearch;
    }


    public ImpactSearchBean() {
        super();
        currentImpactHierarchyBean = (CurrentImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("CurrentImpactHierarchyBean");
        activationGroups = applicationBean.getDefaultDraftReleaseGroup() + "," + applicationBean.getDefaultMedDRAReleaseGroup(); 
        userBean.setCurrentMenuPath("Impact Assessment › Historical " + CSMQBean.customMQName);
        userBean.setCurrentMenu("HISTORICAL_IMPACT_ASSESSMENT");
        setUIDefaults();
        }

    /* BindingContext bc = BindingContext.getCurrent();
    DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry(); */
    
        private void setUIDefaults () {
        if (userBean.isMQM() || userBean.isAdmin()) {
            this.paramFilterForUser = CSMQBean.FALSE;
            }
        else if (userBean.isRequestor()) {
            this.paramFilterForUser = CSMQBean.TRUE;
            this.paramUserName = userBean.getCurrentUser();
            this.paramActivityStatus = CSMQBean.ACTIVE_ACTIVITY_STATUS;
            }
        // IT'S A BROWSE USER - SHOW ONLY CURRENT & ACTIVE NMQs
        else {
            this.paramReleaseStatus = CSMQBean.CURRENT_RELEASE_STATUS;
            this.paramActivityStatus = CSMQBean.ACTIVE_ACTIVITY_STATUS;
            this.paramFilterForUser = CSMQBean.TRUE;
            this.paramUserName = null;
            }

        }
    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }


    public void setCtrlImpact(RichSelectOneChoice ctrlImpact) {
        this.ctrlImpact = ctrlImpact;
    }

    public RichSelectOneChoice getCtrlImpact() {
        return ctrlImpact;
    }

    public void ctrlImpactChanged(ValueChangeEvent valueChangeEvent) {
        
        String assessmentType = ctrlImpact.getValue().toString();
        if (assessmentType.equals("INMQ")){
            this.showImpact = "Y";
            this.mqType = CSMQBean.NMQ;
            }
        else if (assessmentType.equals("NINMQ")){
            this.showImpact = "N";
            this.mqType = CSMQBean.NMQ;
            }
        else if (assessmentType.equals("ISMQ")){
            this.showImpact = "Y";
            this.mqType = CSMQBean.SMQ;
            }
        else if (assessmentType.equals("NISMQ")){
            this.showImpact = "N";
            this.mqType = CSMQBean.SMQ;
            }
        }

    public void releaseStatusChanged(ValueChangeEvent valueChangeEvent) {
        if (ctrlReleaseStatus != null)
            currentMqstatus = ctrlReleaseStatus.getValue().toString();
        
        if (currentMqstatus.equals(CSMQBean.CURRENT_RELEASE_STATUS)) {
            ctrlReleaseGroupSearch.setDisabled(true);
            ctrlReleaseGroupSearch.setValue(CSMQBean.WILDCARD);
            }
        else {
            ctrlReleaseGroupSearch.setDisabled(false);
            }
        AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlReleaseGroupSearch);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlReleaseGroupSearch);
        
    }

    public String getSearchDictionaryShortName() {
        try {
            String value = String.valueOf(getCtrlDictionaryListSearch().getValue());
            return value.trim();
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }


    public String getSearchDictionaryType() {
        try {
            String value = String.valueOf(getCtrlDictionaryTypeSearch().getValue());
            return value.trim();
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }


    public void setCtrlDictionaryListSearch(RichSelectOneChoice dictionaryList) {
        this.ctrlDictionaryListSearch = dictionaryList;
    }

    public RichSelectOneChoice getCtrlDictionaryListSearch() {
        return ctrlDictionaryListSearch;
    }

    public void setCtrlReleaseStatus(RichSelectOneChoice status) {
        this.ctrlReleaseStatus = status;
    }

    public RichSelectOneChoice getCtrlReleaseStatus() {
        return ctrlReleaseStatus;
    }

    public void setCtrlStartDate(RichInputDate startDate) {
        this.ctrlStartDate = startDate;
    }

    public RichInputDate getCtrlStartDate() {
        return ctrlStartDate;
    }

    public void setCtrlEndDate(RichInputDate endDate) {
        this.ctrlEndDate = endDate;
    }

    public RichInputDate getCtrlEndDate() {
        return ctrlEndDate;
    }

    public void setCtrlState(RichSelectManyChoice state) {
        this.ctrlState = state;
    }

    public RichSelectManyChoice getCtrlState() {
        return ctrlState;
    }

    public void setCtrlReleaseGroupSearch(RichSelectOneChoice releaseGroup) {
        this.ctrlReleaseGroupSearch = releaseGroup;
    }

    public RichSelectOneChoice getCtrlReleaseGroupSearch() {
        return ctrlReleaseGroupSearch;
    }

    public void doSearch(ActionEvent actionEvent) {
        
        // TODO update for new search params
        
        CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING SEARCH **");
        CSMQBean.logger.info(userBean.getCaller() + " startDate: " + getParamStartDate());
        CSMQBean.logger.info(userBean.getCaller() + " endDate: " + getParamEndDate());
        CSMQBean.logger.info(userBean.getCaller() + " term: " + getParamTerm());
        CSMQBean.logger.info(userBean.getCaller() + " activityStatus: " + getParamActivityStatus());
        CSMQBean.logger.info(userBean.getCaller() + " dictShortName: " + getParamDictName());
        CSMQBean.logger.info(userBean.getCaller() + " releaseStatus: " + getParamReleaseStatus());
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + getParamReleaseGroup());
        CSMQBean.logger.info(userBean.getCaller() + " MQGroup: " + getParamMQGroupList());
        CSMQBean.logger.info(userBean.getCaller() + " MQCode: " + getParamMQCode());
        CSMQBean.logger.info(userBean.getCaller() + " MQCriticalEvent: " + getParamMQCriticalEvent());
        CSMQBean.logger.info(userBean.getCaller() + " uniqueIDsOnly: " + getParamUniqueIDsOnly());
        CSMQBean.logger.info(userBean.getCaller() + " filterForUser: " +  getParamFilterForUser());
        CSMQBean.logger.info(userBean.getCaller() + " currentUser: " + getParamUserName());
        CSMQBean.logger.info(userBean.getCaller() + " mqType: " + getParamQueryType());
        CSMQBean.logger.info(userBean.getCaller() + " killSwitch: " + CSMQBean.KILL_SWITCH_OFF);
        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
        
        ctrlSearchResults.setEmptyText("No data to display.");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SimpleSearch1Iterator");
        ViewObject vo = dciterb.getViewObject();
        
        vo.setNamedWhereClauseParam("startDate", getParamStartDate());
        vo.setNamedWhereClauseParam("endDate", getParamEndDate());
        vo.setNamedWhereClauseParam("term", getParamTerm());
        vo.setNamedWhereClauseParam("activityStatus", getParamActivityStatus());
        vo.setNamedWhereClauseParam("dictShortName", getParamDictName());
        vo.setNamedWhereClauseParam("releaseStatus", getParamReleaseStatus());
        vo.setNamedWhereClauseParam("activationGroup", getParamReleaseGroup());
        vo.setNamedWhereClauseParam("MQGroup", getParamMQGroupList());
        vo.setNamedWhereClauseParam("MQCode", getParamMQCode());
        vo.setNamedWhereClauseParam("MQCriticalEvent", getParamMQCriticalEvent());
        vo.setNamedWhereClauseParam("uniqueIDsOnly", getParamUniqueIDsOnly());
        vo.setNamedWhereClauseParam("filterForUser",  getParamFilterForUser());
        vo.setNamedWhereClauseParam("currentUser", getParamUserName());
        vo.setNamedWhereClauseParam("mqType", getParamQueryType());
        vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_OFF);
       
        vo.executeQuery();
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
        
        
//        CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING SEARCH **");
//        CSMQBean.logger.info(userBean.getCaller() + " paramDictName:" + getParamDictName());
//        CSMQBean.logger.info(userBean.getCaller() + " paramStartDate:" + getParamStartDate());
//        CSMQBean.logger.info(userBean.getCaller() + " paramEndDate:" + getParamEndDate());
//        CSMQBean.logger.info(userBean.getCaller() + " paramTerm:" + getParamTerm());
//        CSMQBean.logger.info(userBean.getCaller() + " paramReleaseStatus:" + getParamReleaseStatus());
//        CSMQBean.logger.info(userBean.getCaller() + " paramActivityStatus:" + getParamActivityStatus());
//        CSMQBean.logger.info(userBean.getCaller() + " paramState:" + getParamState());
//        CSMQBean.logger.info(userBean.getCaller() + " paramMQGroupList:" + getParamMQGroupList());
//        CSMQBean.logger.info(userBean.getCaller() + " paramReleaseGroup:" + getParamReleaseGroup());
//        CSMQBean.logger.info(userBean.getCaller() + " paramMQCode:" + getParamMQCode());
//        CSMQBean.logger.info(userBean.getCaller() + " paramMQCriticalEvent:" + getParamMQCriticalEvent());
//        CSMQBean.logger.info(userBean.getCaller() + " paramQueryType:" + getParamQueryType());
//        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
        
        
    }



    public void setParamDictName(String paramDictName) {
        this.paramDictName = paramDictName;
    }

    public String getParamDictName() {
        try {
            String temp = String.valueOf(ctrlDictionaryListSearch.getValue());
            if (temp != null)
                paramDictName = temp.trim();
            return paramDictName;
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4

        }
        return null;
    }

    public String getParamStartDate() {
        try {
            paramStartDate = formatter.format(ctrlStartDate.getValue());
            return paramStartDate.trim();
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        } catch (java.lang.IllegalArgumentException iae) {
            // Bury this, fix for 11.1.1.4
        }

        return null;
    }

    public String getParamEndDate() {
        try {
            paramEndDate = formatter.format(ctrlEndDate.getValue());
            return paramEndDate.trim();
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4

        } catch (java.lang.IllegalArgumentException iae) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    public String getParamTerm() {
        try {
            String temp = String.valueOf(ctrlMQName.getValue());
            if (temp != null)
                paramTerm = temp.trim();
            return paramTerm;
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4

        }
        return null;
    }

    public String getParamReleaseStatus() {
        try {
            String temp = String.valueOf(ctrlReleaseStatus.getValue());
            if (temp != null)
                paramReleaseStatus = temp.trim();
            return paramReleaseStatus;
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    public String getParamActivityStatus() {
        try {
            String temp = String.valueOf(ctrlNMQStatus.getValue());
            if (temp != null)
                paramActivityStatus = temp.trim();
            return paramActivityStatus;
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;

    }

    public String getParamState() {
        try {
            List<String> selected = (List<String>)ctrlState.getValue();
            if (selected == null)
                return null;
            String csvValue = "";
            for (String s : selected)
                csvValue = csvValue + s + ",";

            paramState = csvValue.substring(0, csvValue.length() - 1);
            return paramState;
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    public String getParamReleaseGroup() {
        try {
            String temp = String.valueOf(ctrlReleaseGroupSearch.getValue());
            if (temp != null)
                paramReleaseGroup = temp.trim();
            return paramReleaseGroup;
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }


    public void setCtrlSearchResults(RichTable searchResults) {
        this.ctrlSearchResults = searchResults;
    }

    public RichTable getCtrlSearchResults() {
        return ctrlSearchResults;
    }


    public void setCtrlMQName(RichInputText mqName) {
        this.ctrlMQName = mqName;
    }

    public RichInputText getCtrlMQName() {
        return ctrlMQName;
    }

    public void setCtrlLevelList(RichSelectOneChoice levelList) {
        this.ctrlLevelList = levelList;
    }

    public RichSelectOneChoice getCtrlLevelList() {
        return ctrlLevelList;
    }

    public void setCtrlMQCode(RichInputText mqCode) {
        this.ctrlMQCode = mqCode;
    }

    public RichInputText getCtrlMQCode() {
        return ctrlMQCode;
    }

    public void setCtrlDictionaryVersion(RichInputText dictionaryVersion) {
        this.ctrlDictionaryVersion = dictionaryVersion;
    }

    public RichInputText getCtrlDictionaryVersion() {
        return ctrlDictionaryVersion;
    }

    public void setCtrlMQAlgorithm(RichInputText mqAlgorithm) {
        this.ctrlMQAlgorithm = mqAlgorithm;
    }

    public RichInputText getCtrlMQAlgorithm() {
        return ctrlMQAlgorithm;
    }

    public void setCtrlProductList(RichSelectManyChoice productList) {
        this.ctrlProductList = productList;
    }

    public RichSelectManyChoice getCtrlProductList() {
        return ctrlProductList;
    }

    public String getParamMQGroupList() {
        if (ctrlMQGroups == null) return null;
        Object selProd = ctrlMQGroups.getValue();
        if (selProd == null) return "%";
        
        if (selProd instanceof java.lang.String) {
            String temp = selProd.toString();
            temp.replace("[", "");
            temp.replace("]", "");
            this.paramMQGroupList = temp; 
            }
        else {       
            List selected = (List)ctrlMQGroups.getValue();
            if (selected != null) {
                if (selected.size() == 0) return "%";
                String temp = "";
                for (Object s : selected)
                        temp = temp + s + ",";
                
                temp.replace("[", "");
                temp.replace("]", "");
                
                if (temp != null & temp.length() > 0)            
                    this.paramMQGroupList = temp.substring(0, temp.length() - 1);
                }
        
        }
        return paramMQGroupList;
    }
    

    public void setCurrentDictContentID(String currentDictContentID) {
        this.currentDictContentID = currentDictContentID;
    }

    public String getCurrentDictContentID() {
        if (currentDictContentID == null)
            return null;
        return currentDictContentID.trim();
    }

    public void setCurrentReleaseGroup(String currentReleaseGroup) {
        this.currentReleaseGroup = currentReleaseGroup;
    }

    public String getCurrentReleaseGroup() {
        if (currentReleaseGroup == null)
            return null;
        return currentReleaseGroup.trim();
    }


    public void setCtrlMQGroups(RichSelectManyChoice MQGroups) {
        this.ctrlMQGroups = MQGroups;
    }

    public RichSelectManyChoice getCtrlMQGroups() {
        return ctrlMQGroups;
    }


    public void setCurrentTermName(String currentTermName) {
        this.currentTermName = currentTermName;
    }

    public String getCurrentTermName() {
        return currentTermName;
    }

    public String editSelectedTerm() {
        CSMQBean.logger.info(userBean.getCaller() + " !! EDIT !!");

        return null;
    }


    public void setCurrentMqlevel(String currentMqlevel) {
        this.currentMqlevel = currentMqlevel;
    }

    public String getCurrentMqlevel() {
        return currentMqlevel;
    }

    public void setCurrentMqcode(String currentMqcode) {
        this.currentMqcode = currentMqcode;
    }

    public String getCurrentMqcode() {
        return currentMqcode;
    }

    public void setCurrentMqalgo(String currentMqalgo) {
        this.currentMqalgo = currentMqalgo;
    }

    public String getCurrentMqalgo() {
        return currentMqalgo;
    }

    public void setCurrentMqaltcode(String currentMqaltcode) {
        this.currentMqaltcode = currentMqaltcode;
    }

    public String getCurrentMqaltcode() {
        return currentMqaltcode;
    }

    public void setCurrentDictionary(String currentDictionary) {
        this.currentDictionary = currentDictionary;
    }

    public String getCurrentDictionary() {
        return currentDictionary;
    }

    public void setCurrentMqstatus(String currentMqstatus) {
        this.currentMqstatus = currentMqstatus;
    }

    public String getCurrentMqstatus() {
        return currentMqstatus;
    }

    public void setCurrentRequestor(String currentRequestor) {
        this.currentRequestor = currentRequestor;
    }

    public String getCurrentRequestor() {
        return currentRequestor;
    }

    public void setCurrentCriticalEvent(String currentCriticalEvent) {
        this.currentCriticalEvent = currentCriticalEvent;
    }

    public String getCurrentCriticalEvent() {
        return currentCriticalEvent;
    }

    public void setCurrentDateRequested(Date currentDateRequested) {
        this.currentDateRequested = currentDateRequested;
    }

    public Date getCurrentDateRequested() {
        return currentDateRequested;
    }


    public void onTableNodeSelection(SelectionEvent selectionEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE START ****");

        resolveMethodExpression("#{bindings.SimpleSearch1.collectionModel.makeCurrent}", null, new Class[] { SelectionEvent.class }, new Object[] { selectionEvent });
        RichTable object = (RichTable)selectionEvent.getSource();
        Row row = null;
        for (Object facesRowKey : object.getSelectedRowKeys()) {
            object.setRowKey(facesRowKey);
            Object o = object.getRowData();
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)o;
            row = rowData.getRow();
        }

        if (row == null)
            return;

        currentTermName = Utils.getAsString(row, "Mqterm");
        CSMQBean.logger.info(userBean.getCaller() + " currentTermName:" + currentTermName);
        
        currentMqlevel = Utils.getAsString(row, "Mqlevel");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqlevel:" + currentMqlevel);
        
        currentMqcode = Utils.getAsString(row, "Mqcode");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqcode:" + currentMqcode);
        
        currentMqalgo = Utils.getAsString(row, "Mqalgo").toString();
        CSMQBean.logger.info(userBean.getCaller() + " currentMqalgo:" + currentMqalgo);

        currentMqaltcode = Utils.getAsString(row, "Mqaltcode");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqaltcode:" + currentMqaltcode);

        currentDictionary = getParamDictName();
        CSMQBean.logger.info(userBean.getCaller() + " currentDictionary:" + currentDictionary);
        
        currentReleaseGroup = getParamReleaseGroup();
        CSMQBean.logger.info(userBean.getCaller() + " currentReleaseGroup:" + currentReleaseGroup);

        currentMqstatus = Utils.getAsString(row, "Mqstatus");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqstatus:" + currentMqstatus);

        currentDictContentID = Utils.getAsString(row, "ContentId");
        CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID:" + currentDictContentID);
        
        currentApprovalFlag = Utils.getAsString(row, "ApprFlag");
        CSMQBean.logger.info(userBean.getCaller() + " currentApprovalFlag:" + currentApprovalFlag);

        currentVersion = Utils.getAsString(row, "Version");
        CSMQBean.logger.info(userBean.getCaller() + " currentVersion:" + currentVersion);

        currentSubType = Utils.getAsString(row, "TermSubtype");
        CSMQBean.logger.info(userBean.getCaller() + " currentSubType:" + currentSubType);

        currentMqscp = Utils.getAsString(row, "Mqscp");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqscp:" + currentMqscp);

        currentMqgroups = Utils.getAsString(row, "MqgroupF");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqgroups:" + currentMqgroups);

        currentMqproduct = Utils.getAsString(row, "Mqprodct");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqproduct:" + currentMqproduct);

        currentCriticalEvent = Utils.getAsString(row, "Mqcrtev");
        CSMQBean.logger.info(userBean.getCaller() + " currentCriticalEvent:" + currentCriticalEvent);
        
        if (this.historicalTree != null && this.historicalTree.getDisclosedRowKeys()!=null )
            this.historicalTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
                
        if (this.currentTree != null && this.currentTree.getDisclosedRowKeys()!=null )
            this.currentTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        
        refreshHistoricalTree();
        refreshCurrentTree();
        clearSearch();
        CSMQBean.logger.info(userBean.getCaller() + " CHANGING PAGE");
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler nh = context.getApplication().getNavigationHandler();
        nh.handleNavigation(context, "", "historicalImpactAssessment");


        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE COMPLETE ****");
    }


    public void refreshCurrentTree () {
        
        //i_dict_content_id => :dictContentID,
        //i_as_of_date_1 => TO_DATE(:olderDate,'DDMONYYYY'),
        //i_as_of_date_2 => TO_DATE(:recentDate,'DDMONYYYY'),

        CSMQBean.logger.info(userBean.getCaller() + " *** REFRESHING CURRENT TREE ***");

        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " +  this.currentDictContentID);

        CSMQBean.logger.info(userBean.getCaller() + " Updating Current Tree...");
        DCIteratorBinding currentImpactVO1Iterator = (DCIteratorBinding)binding.get("CurrentImpactVO1Iterator");
        ViewObject currentTree = currentImpactVO1Iterator.getViewObject();
        currentTree.setNamedWhereClauseParam("olderDate", this.historicalAsOfDate);
        currentTree.setNamedWhereClauseParam("dictContentID", this.currentDictContentID);
        currentTree.executeQuery();   
        CurrentImpactHierarchyBean currentImpactHierarchyBean = (CurrentImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("CurrentImpactHierarchyBean");
        currentImpactHierarchyBean.init();


        CSMQBean.logger.info(userBean.getCaller() + " *** DONE CURRENT TREE ***");        
    }

    public void refreshHistoricalTree () {
        
        //i_dict_content_id => :dictContentID,
        //i_as_of_date_1 => TO_DATE(:olderDate,'DDMONYYYY'),
        //i_as_of_date_2 => TO_DATE(:recentDate,'DDMONYYYY'),


        CSMQBean.logger.info(userBean.getCaller() + " *** REFRESHING HISTORIC TREE ***");

        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " +  this.currentDictContentID);


        CSMQBean.logger.info(userBean.getCaller() + " Updating Historical Tree..."); 
        DCIteratorBinding historicalImpactVO1Iterator = (DCIteratorBinding)binding.get("HistoricalImpactVO1Iterator");
        ViewObject historicTree = historicalImpactVO1Iterator.getViewObject();      
        historicTree.setNamedWhereClauseParam("olderDate", this.historicalAsOfDate);
        historicTree.setNamedWhereClauseParam("dictContentID", this.currentDictContentID);
        historicTree.setNamedWhereClauseParam("showNonImpacted", this.showNonImpacted);
        historicTree.executeQuery();        
        HistoricalImpactHierarchyBean historicalImpactHierarchyBean = (HistoricalImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("HistoricalImpactHierarchyBean");
        historicalImpactHierarchyBean.init();


        CSMQBean.logger.info(userBean.getCaller() + " *** DONE HISTORIC TREE ***");        
    }



    public void setCurrentApprovalFlag(String currentApprovalFlag) {
        this.currentApprovalFlag = currentApprovalFlag;
    }

    public String getCurrentApprovalFlag() {
        return currentApprovalFlag;
    }

    public void setCurrentSubType(String currentSubType) {
        this.currentSubType = currentSubType;
    }

    public String getCurrentSubType() {
        if (currentSubType == null)
            return null;
        if (currentSubType.equals("C"))
            return "CUSTOM";
        return "OTHER";
    }

    public void setCurrentVersion(String currentVerison) {
        this.currentVersion = currentVerison;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentMqscp(String currentMqscp) {
        this.currentMqscp = currentMqscp;
    }

    public String getCurrentMqscp() {
        return currentMqscp;
    }

    public void setCurrentMqgroups(String currentMqgroups) {
        this.currentMqgroups = currentMqgroups;
    }

    public String getCurrentMqgroups() {
        return currentMqgroups;
    }

    public void setCurrentMqproduct(String currentMqproduct) {
        this.currentMqproduct = currentMqproduct;
    }

    public String getCurrentMqproduct() {
        return currentMqproduct;
    }

    public void setCurrentInfNoteDescription(String currentInfNoteDescription) {
        this.currentInfNoteDescription = currentInfNoteDescription;
    }

    public String getCurrentInfNoteDescription() {
        return currentInfNoteDescription;
    }

    public void setCurrentInfNoteSource(String currentInfNoteSource) {
        this.currentInfNoteSource = currentInfNoteSource;
    }

    public String getCurrentInfNoteSource() {
        return currentInfNoteSource;
    }

    public void setCurrentInfNoteNotes(String currentInfNoteNotes) {
        this.currentInfNoteNotes = currentInfNoteNotes;
    }

    public String getCurrentInfNoteNotes() {
        return currentInfNoteNotes;
    }

    public void dictionaryChange(ValueChangeEvent valueChangeEvent) {
        try {
            this.currentDictionary = ctrlDictionaryListSearch.getValue().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseGroupChange(ValueChangeEvent valueChangeEvent) {
        this.currentReleaseGroup = ctrlReleaseGroupSearch.getValue().toString();
        // Add event code here...
    }


    public void setCtrlDictionary(RichSelectOneChoice controlDictionary) {
        this.ctrlDictionary = controlDictionary;
    }

    public RichSelectOneChoice getCtrlDictionary() {
        return ctrlDictionary;
    }

    public void dictionaryTypeChanged(ValueChangeEvent valueChangeEvent) {
        this.currentDictionaryType = ctrlDictionaryTypeSearch.getValue().toString();

    }

    public void setCurrentDictionaryType(String currentDictionaryType) {
        this.currentDictionaryType = currentDictionaryType;
    }

    public String getCurrentDictionaryType() {
        return currentDictionaryType;
    }

    public void setCtrlStatusIndicator(RichStatusIndicator controlStatusIndicator) {
        this.ctrlStatusIndicator = controlStatusIndicator;
    }

    public RichStatusIndicator getCtrlStatusIndicator() {
        return ctrlStatusIndicator;
    }

    public void setCtrlQueryType(RichSelectOneChoice ctrllQueryType) {
        this.ctrlQueryType = ctrllQueryType;
    }

    public RichSelectOneChoice getCtrlQueryType() {
        return ctrlQueryType;
    }

    public void setParamMQCode(String paramMQCode) {
        this.paramMQCode = paramMQCode;
    }

    public String getParamMQCode() {
        if (ctrlMQCode == null) return null;
        String temp = String.valueOf(ctrlMQCode.getValue());
        if (temp != null)
            paramMQCode = temp.trim();
        if (paramMQCode.equals("null") || paramMQCode.length() == 0)
            paramMQCode = "%";
        return paramMQCode;
    }

    public void setParamMQCriticalEvent(String paramMQCriticalEvent) {
        this.paramMQCriticalEvent = paramMQCriticalEvent;
    }

    public String getParamMQCriticalEvent() {
        if (ctrlCriticalEvent == null) return null;
        String temp = String.valueOf(ctrlCriticalEvent.getValue());
        if (temp != null)
            paramMQCriticalEvent = temp.trim();
        return paramMQCriticalEvent;
    }

    public void queryTypeChanged(ValueChangeEvent valueChangeEvent) {
        String type = ctrlQueryType.getValue().toString();
        this.paramMQCode = "%";
        if (type.equals("SMQ"))
            this.paramMQCode = "2%";
        if (type.equals("NMQ"))
            this.paramMQCode = "9%";
    }

    public void setCtrlNMQStatus(RichSelectOneChoice ctrlNMQStatus) {
        this.ctrlNMQStatus = ctrlNMQStatus;
    }

    public RichSelectOneChoice getCtrlNMQStatus() {
        return ctrlNMQStatus;
    }

    public void setCtrlMQScope(RichSelectOneChoice ctrlMQScope) {
        this.ctrlMQScope = ctrlMQScope;
    }

    public RichSelectOneChoice getCtrlMQScope() {
        return ctrlMQScope;
    }

    public void setCtrlCriticalEvent(RichSelectOneChoice ctrlCriticalEvent) {
        this.ctrlCriticalEvent = ctrlCriticalEvent;
    }

    public RichSelectOneChoice getCtrlCriticalEvent() {
        return ctrlCriticalEvent;
    }

    public void setMQGroupList(List<String> mQGroupList) {
        this.mQGroupList = mQGroupList;
    }

    public List<String> getMQGroupList() {
        return mQGroupList;
    }

    public void setCtrlEntryTS(RichSelectOneChoice ctrlEntryTS) {
        this.ctrlEntryTS = ctrlEntryTS;
    }

    public RichSelectOneChoice getCtrlEntryTS() {
        return ctrlEntryTS;
    }

    public void historicalDateChanged(ValueChangeEvent valueChangeEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " CHANGING HISTORICAL DATE");
        this.historicalAsOfDate = ctrlEndTS.getValue().toString();
        //impactSearchHierarchyBean.init();
        this.showReactivateButton = (!this.historicalAsOfDate.equals(CSMQBean.getProperty("END_TS_CONSTANT")));
        
        refreshHistoricalTree();

        AdfFacesContext.getCurrentInstance().addPartialTarget(this.historicalTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.historicalTree);
  
    }

    public void setHistoricalAsOfDate(String historicalAsOfDate) {
        this.historicalAsOfDate = historicalAsOfDate;
    }

    public String getHistoricalAsOfDate() {
        return historicalAsOfDate;
    }

    public void setCtrlEndTS(RichSelectOneChoice ctrlEndTS) {
        this.ctrlEndTS = ctrlEndTS;
    }

    public RichSelectOneChoice getCtrlEndTS() {
        return ctrlEndTS;
    }

    public void setSelectedHistoricalDate(String selectedHistoricalDate) {
        this.selectedHistoricalDate = selectedHistoricalDate;
    }

    public String getSelectedHistoricalDate() {
        return selectedHistoricalDate;
    }


    public void setHistoricalTree(RichTreeTable historicalTree) {
        this.historicalTree = historicalTree;
    }

    public RichTreeTable getHistoricalTree() {
        return historicalTree;
    }

    public void setCurrentTree(RichTreeTable currentTree) {
        this.currentTree = currentTree;
    }

    public RichTreeTable getCurrentTree() {
        return currentTree;
    }


    public void setParamUserName(String paramUserName) {
        this.paramUserName = paramUserName;
    }

    public String getParamUserName() {
        return paramUserName;
    }

    public void setParamUniqueIDsOnly(String paramUniqueIDsOnly) {
        this.paramUniqueIDsOnly = paramUniqueIDsOnly;
    }

    public String getParamUniqueIDsOnly() {
        return paramUniqueIDsOnly;
    }

    public void setParamFilterForUser(String paramFilterForUser) {
        this.paramFilterForUser = paramFilterForUser;
    }

    public String getParamFilterForUser() {
        return paramFilterForUser;
    }

    public void setShowReactivateButton(boolean showReactivateButton) {
        this.showReactivateButton = showReactivateButton;
    }

    public boolean getShowReactivateButton() {
        return showReactivateButton;
    }

    public void setAsOfDate(String asOfDate) {
        this.asOfDate = asOfDate;
    }

    public String getAsOfDate() {
        return asOfDate;
    }

    public void setCntrlShowImpacted(RichSelectOneChoice cntrlShowImpacted) {
        this.cntrlShowImpacted = cntrlShowImpacted;
    }

    public RichSelectOneChoice getCntrlShowImpacted() {
        return cntrlShowImpacted;
    }

    public void cntrlShowImpactedChanged(ValueChangeEvent valueChangeEvent) {
        this.showNonImpacted = this.cntrlShowImpacted.getValue().toString();
        refreshHistoricalTree();

        AdfFacesContext.getCurrentInstance().addPartialTarget(this.historicalTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.historicalTree);
    }

    public void setShowNonImpacted(String showNonImpacted) {
        this.showNonImpacted = showNonImpacted;
    }

    public String getShowNonImpacted() {
        return showNonImpacted;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
    
    public DnDAction onTreeDrop(DropEvent dropEvent) {
   
        RichTreeTable sourceTree;

        if (dropEvent.getDragClientId().toString().indexOf("ptList") > 0) 
            sourceTree = this.preferedTermSourceTree;
        else
            sourceTree = this.hierarchySourceTree;

        CSMQBean.logger.info(userBean.getCaller() + " -- DRAG AND DROP INITIATED --");
        
        return processDragAndDropEvent(dropEvent, sourceTree, currentTree, currentImpactHierarchyBean.getTreemodel(), Integer.parseInt(CSMQBean.FULL_NMQ_SMQ));
        }
  

    private void clearSearch () {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SimpleSearch1Iterator");
        ViewObject vo = dciterb.getViewObject();

        vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_ON);
        vo.executeQuery();
        ctrlSearchResults.setEmptyText("Selected Term: " + this.currentTermName);
        AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
        }

    /* public void onTreeNodeDelete(ActionEvent actionEvent) { 
    
        int rowIndexTreeTable = currentTree.getRowIndex();        
        GenericTreeNode node = (GenericTreeNode)currentTree.getRowData(rowIndexTreeTable);
       
        node.getParentNode().getChildren().remove(node);
        RowKeySet rks = new RowKeySetTreeImpl(true);
        rks.setCollectionModel(currentImpactHierarchyBean.getTreemodel());
        this.currentTree.setDisclosedRowKeys(rks);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.currentTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.currentTree); 
       
        com.dbms.csmq.Application.logger.info("** removed targetNode:" + node);
        } */

    public void SOCChanged(ValueChangeEvent valueChangeEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " CREATING NEW PT TREE");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("NewPTsVO1Iterator");
        ViewObject newPTsVO1Iterator = dciterb.getViewObject();
        
        newPTsVO1Iterator.setNamedWhereClauseParam("dictContentID", getParentSOCcontentID());
        newPTsVO1Iterator.setNamedWhereClauseParam("activationGroups", activationGroups);        
        newPTsVO1Iterator.executeQuery();
        
        this.preferedTermSourceTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.preferedTermSourceTree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.preferedTermSourceTree);
        
        NewPTListBean newPTListBean = (NewPTListBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NewPTListBean");
        newPTListBean.init();

        CSMQBean.logger.info(userBean.getCaller() + " DONE CREATING NEW  PT TREE");
    }
    public void setPreferedTermSourceTree(RichTreeTable preferedTermSourceTree) {
        this.preferedTermSourceTree = preferedTermSourceTree;
    }

    public RichTreeTable getPreferedTermSourceTree() {
        return preferedTermSourceTree;
    }

    public void setHierarchySourceTree(RichTreeTable hierarchySourceTree) {
        this.hierarchySourceTree = hierarchySourceTree;
    }

    public RichTreeTable getHierarchySourceTree() {
        return hierarchySourceTree;
    }

    public void setCntlSOCList(RichSelectOneChoice cntlSOCList) {
        this.cntlSOCList = cntlSOCList;
    }

    public RichSelectOneChoice getCntlSOCList() {
        return cntlSOCList;
    }

    public void setParentSOCcontentID(String parentSOCcontentID) {
        this.parentSOCcontentID = parentSOCcontentID;
    }

    public String getParentSOCcontentID() {
        this.parentSOCcontentID = cntlSOCList.getValue().toString();
        return parentSOCcontentID;
    }

    public void setShowImpact(String showImpact) {
        this.showImpact = showImpact;
    }

    public String getShowImpact() {
        return showImpact;
    }

    public void setMqType(String mqType) {
        this.mqType = mqType;
    }

    public String getMqType() {
        return mqType;
    }

    public void setActivationGroups(String activationGroups) {
        this.activationGroups = activationGroups;
    }

    public String getActivationGroups() {
        return activationGroups;
    }

    public void setParamQueryType(String paramQueryType) {
        this.paramQueryType = paramQueryType;
    }

    public String getParamQueryType() {
        return paramQueryType;
    }

    public void setParamKillSwitch(Integer paramKillSwitch) {
        this.paramKillSwitch = paramKillSwitch;
    }

    public Integer getParamKillSwitch() {
        return paramKillSwitch;
    }
}

