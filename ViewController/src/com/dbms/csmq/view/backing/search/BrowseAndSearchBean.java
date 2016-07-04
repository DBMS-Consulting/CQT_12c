package com.dbms.csmq.view.backing.search;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;

import com.dbms.util.Utils;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichStatusIndicator;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;


public class BrowseAndSearchBean {

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
    private String paramUniqueIDsOnly;
    private String paramFilterForUser;    
    private String paramQueryType = "%";
        
    //FOR HIERARCHY
    private String displaySecondaryPath = CSMQBean.TRUE;
        

    // ** CURRENT SELECTED DATA
    private String currentDictContentID;
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


    public static final int MAX_PRODUCT_COUNT = 3;


    private ResourceBundle resourceBundle;
    private Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
    private BrowseAndSearchHierarchyBean browseAndSearchHierarchyBean;
    private CSMQBean applicationBean;
    private UserBean userBean;
    private RichSelectOneChoice ctrlCriticalEvent;

    
    private RichSelectOneChoice ctrlEntryTS;
    private String historicalAsOfDate = CSMQBean.getProperty("END_TS_CONSTANT");
    private RichSelectOneChoice ctrlEndTS;
    private String selectedHistoricalDate;
    private RichTreeTable resultsTree;
    private RichSelectBooleanCheckbox ctrlShowSecondaryPath;
    private String userRole;
    

    public void setCtrlDictionaryTypeSearch(RichSelectOneChoice dictionaryTypeSearch) {
        this.ctrlDictionaryTypeSearch = dictionaryTypeSearch;
    }

    public RichSelectOneChoice getCtrlDictionaryTypeSearch() {
        return ctrlDictionaryTypeSearch;
    }


    public BrowseAndSearchBean() {
        super();
        resourceBundle = BundleFactory.getBundle(CSMQBean.RESOURCE_BUNDLE_NAME);
        browseAndSearchHierarchyBean = (BrowseAndSearchHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("BrowseAndSearchHierarchyBean");
        applicationBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
        userBean.setCurrentMenuPath("Browse & Search");
        userBean.setCurrentMenu("dialog:SEARCH");
        setUserProperties ();
        }

    BindingContext bc = BindingContext.getCurrent();
    DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();

    private void setUserProperties () {
        if (userBean.isMQM() || userBean.isAdmin()) {
            this.paramFilterForUser = CSMQBean.FALSE;
            return;
            }
        if (userBean.isRequestor()) {
            this.paramFilterForUser = CSMQBean.TRUE;
            this.paramUserName = userBean.getCurrentUser();
            this.paramActivityStatus = CSMQBean.ACTIVE_ACTIVITY_STATUS;
            return;
            }
        // IT'S A BROWSE USER - SHOW ONLY CURRENT & ACTIVE NMQs
        this.paramReleaseStatus = CSMQBean.CURRENT_RELEASE_STATUS;
        this.paramActivityStatus = CSMQBean.ACTIVE_ACTIVITY_STATUS;
        this.paramFilterForUser = CSMQBean.TRUE;
        this.paramUserName = null;
    }



    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
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
        CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING SEARCH **");
        CSMQBean.logger.info(userBean.getCaller() + " paramDictName:" + getParamDictName());
        CSMQBean.logger.info(userBean.getCaller() + " paramStartDate:" + getParamStartDate());
        CSMQBean.logger.info(userBean.getCaller() + " paramEndDate:" + getParamEndDate());
        CSMQBean.logger.info(userBean.getCaller() + " paramTerm:" + getParamTerm());
        CSMQBean.logger.info(userBean.getCaller() + " paramReleaseStatus:" + getParamReleaseStatus());
        CSMQBean.logger.info(userBean.getCaller() + " paramActivityStatus:" + getParamActivityStatus());
        CSMQBean.logger.info(userBean.getCaller() + " paramState:" + getParamState());
        CSMQBean.logger.info(userBean.getCaller() + " paramMQGroupList:" + getParamMQGroupList());
        CSMQBean.logger.info(userBean.getCaller() + " paramReleaseGroup:" + getParamReleaseGroup());
        CSMQBean.logger.info(userBean.getCaller() + " paramMQCode:" + getParamMQCode());
        CSMQBean.logger.info(userBean.getCaller() + " paramMQCriticalEvent:" + getParamMQCriticalEvent());
        CSMQBean.logger.info(userBean.getCaller() + " paramUserName:" +  getParamUserName());
        CSMQBean.logger.info(userBean.getCaller() + " paramUniqueIDsOnly:" + getParamUniqueIDsOnly());
        CSMQBean.logger.info(userBean.getCaller() + " paramFilterForUser:" + getParamFilterForUser());
        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
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

    /*
    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        if (statusValue == null)
            return null;
        return statusValue.trim();
    }
*/


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

    public Object resolveMethodExpression(String expression, Class returnType, Class[] argTypes, Object[] argValues) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        MethodExpression methodExpression = elFactory.createMethodExpression(elContext, expression, returnType, argTypes);
        return methodExpression.invoke(elContext, argValues);
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
        
        currentMqalgo = Utils.getAsString(row, "Mqalgo");
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

        CSMQBean.logger.info(userBean.getCaller() + " DONE");
        
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler nh = context.getApplication().getNavigationHandler();
        nh.handleNavigation(context, "", "searchDetail");
        UIComponent comp = selectionEvent.getComponent();
        oracle.adf.view.rich.util.ResetUtils.reset(comp);
        browseAndSearchHierarchyBean.init();
        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE COMPLETE ****");
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
        if (type.equals(CSMQBean.SMQ))
            this.paramMQCode = "2%";
        if (type.equals(CSMQBean.NMQ))
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

//    public void setCtrlDetailsPane(RichShowDetailItem ctrlDetailsPane) {
//        this.ctrlDetailsPane = ctrlDetailsPane;
//    }
//
//    public RichShowDetailItem getCtrlDetailsPane() {
//        return ctrlDetailsPane;
//    }

    public void setCtrlEntryTS(RichSelectOneChoice ctrlEntryTS) {
        this.ctrlEntryTS = ctrlEntryTS;
    }

    public RichSelectOneChoice getCtrlEntryTS() {
        return ctrlEntryTS;
    }

    public void historicalDateChanged(ValueChangeEvent valueChangeEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " CHANGING HISTORICAL DATE");
        this.historicalAsOfDate = ctrlEndTS.getValue().toString();
        browseAndSearchHierarchyBean.init();        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.resultsTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.resultsTree);
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

    public void setResultsTree(RichTreeTable resultsTree) {
        this.resultsTree = resultsTree;
    }

    public RichTreeTable getResultsTree() {
        return resultsTree;
    }

    public void cancel(ActionEvent actionEvent) {
        UIComponent comp = actionEvent.getComponent();
        oracle.adf.view.rich.util.ResetUtils.reset(comp);
    }

    public void showPrimaryPathChanged(ValueChangeEvent valueChangeEvent) {
        Boolean val = (Boolean)this.ctrlShowSecondaryPath.getValue();
        if (val) this.displaySecondaryPath = CSMQBean.FALSE;
        else this.displaySecondaryPath = CSMQBean.TRUE;
       
        browseAndSearchHierarchyBean.init();
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.resultsTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.resultsTree);
        }

    public void setCtrlShowSecondaryPath(RichSelectBooleanCheckbox ctrlShowSecondaryPath) {
        this.ctrlShowSecondaryPath = ctrlShowSecondaryPath;
    }

    public RichSelectBooleanCheckbox getCtrlShowSecondaryPath() {
        return ctrlShowSecondaryPath;
    }

    public void setParamUserName(String paramUserName) {
        this.paramUserName = paramUserName;
    }

    public String getParamUserName() {
                
        return paramUserName;
    }

    public void setParamUniqueIDsOnly(String uniqueIDsOnly) {
        this.paramUniqueIDsOnly = uniqueIDsOnly;
    }

    public String getParamUniqueIDsOnly() {
        return paramUniqueIDsOnly;
    }

    public void setParamFilterForUser(String filterForUser) {
        this.paramFilterForUser = filterForUser;
    }

    public String getParamFilterForUser() {
        return paramFilterForUser;
    }

    public void setDisplaySecondaryPath(String returnPrimLinkPath) {
        this.displaySecondaryPath = returnPrimLinkPath;
    }

    public String getDisplaySecondaryPath() {
        return displaySecondaryPath;
    }

    public void setParamQueryType(String paramQueryType) {
        this.paramQueryType = paramQueryType;
    }

    public String getParamQueryType() {
        return paramQueryType;
    }
}


