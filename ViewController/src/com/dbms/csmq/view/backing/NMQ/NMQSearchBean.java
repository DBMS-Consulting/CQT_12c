package com.dbms.csmq.view.backing.NMQ;


import com.dbms.csmq.CSMQBean;

import com.dbms.csmq.UserBean;
import com.dbms.util.Utils;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.Application;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;


public class NMQSearchBean {
    private RichSelectManyCheckbox releaseStatus;
    private RichSelectManyCheckbox activityStatus;
    private RichInputText term;
    private RichInputDate startDate;
    private RichInputDate endDate;
    private RichSelectManyChoice state;
    private RichSelectManyChoice MQGroups;
    private RichSelectOneChoice releaseGroupSearch;
    private RichSelectOneChoice dictionaryListSearch;
    private RichTable searchResults;

    //
    private RichInputText mqName;
    private RichSelectOneChoice levelList;
    private RichInputText mqCode;
    private String statusValue;


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


    private Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
    private RichInputText dictionaryVersion;
    private RichInputText mqAlgorithm;
    private RichSelectManyChoice productList;


    // ** CURRENT SELECTED DATA
    private String currentDictContentID;
    private String currentReleaseGroup;
    private ArrayList currentProductList;
    private String currentTermName;
    private String currentMqlevel;
    private String currentMqcode;
    private String currentMqalgo;
    private String currentMqaltcode;
    private String currentDictionary;
    private String currentMqstatus;
    private String currentRequestor;
    private String currentDateRequested;
    private String currentCriticalEvent;
    private String currentApprovalFlag;
    private String currentSubType;
    private String currentVersion;
    private String currentMqscp;
    private String currentMqgroups;
    private String currentMqproduct;


    UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
    
    public static final int MAX_PRODUCT_COUNT = 3;
    public static final String BOTH_ACTIVITY_STATUSES = "B";
    public static final String BOTH_RELEASE_STATUSES = "BOTH";
    private UISelectItems productListSelectItems;
    private boolean createHierarchy = false;
    private boolean createList = false;
    private RichPanelFormLayout detailsPanel;
    private RichSelectOneChoice dictionaryTypeSearch;


    public void setAction(NMQSearchBean.Action action) {
        this.action = action;
    }

    public NMQSearchBean.Action getAction() {
        return action;
    }

    public void setDictionaryTypeSearch(RichSelectOneChoice dictionaryTypeSearch) {
        this.dictionaryTypeSearch = dictionaryTypeSearch;
    }

    public RichSelectOneChoice getDictionaryTypeSearch() {
        return dictionaryTypeSearch;
    }





    public enum Action {    
        VIEW,               
        INSERT,             
        COPY,               
        EDIT;
        }                       
                            
    private Action action;  


    public NMQSearchBean() {
        super();
        
    }


    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public String getSearchDictionaryShortName() {
        try {
            String value = String.valueOf(getDictionaryListSearch().getValue());
            return value.trim();
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }


    public String getSearchDictionaryType() {
        try {
            String value = String.valueOf(getDictionaryTypeSearch().getValue());
            return value.trim();
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    
    public void setDictionaryListSearch(RichSelectOneChoice dictionaryList) {
        this.dictionaryListSearch = dictionaryList;
    }

    public RichSelectOneChoice getDictionaryListSearch() {
        return dictionaryListSearch;
    }

    public void setReleaseStatus(RichSelectManyCheckbox status) {
        this.releaseStatus = status;
    }

    public RichSelectManyCheckbox getReleaseStatus() {
        return releaseStatus;
    }

    public void setTerm(RichInputText term) {
        this.term = term;
    }

    public RichInputText getTerm() {
        return term;
    }

    public void setStartDate(RichInputDate startDate) {
        this.startDate = startDate;
    }

    public RichInputDate getStartDate() {
        return startDate;
    }

    public void setEndDate(RichInputDate endDate) {
        this.endDate = endDate;
    }

    public RichInputDate getEndDate() {
        return endDate;
    }

    public void setState(RichSelectManyChoice state) {
        this.state = state;
    }

    public RichSelectManyChoice getState() {
        return state;
    }

    public void setReleaseGroupSearch(RichSelectOneChoice releaseGroup) {
        this.releaseGroupSearch = releaseGroup;
    }

    public RichSelectOneChoice getReleaseGroupSearch() {
        return releaseGroupSearch;
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
        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
    }


   
    public void setParamDictName(String paramDictName) {
        this.paramDictName = paramDictName;
    }

    public String getParamDictName() {
        try {
            String temp = String.valueOf(dictionaryListSearch.getValue());
            if (temp != null)
                paramDictName = temp.trim();
            return paramDictName;
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4

        }
        return null;
    }

    public String getParamStartDate() {
        try {
            paramStartDate = formatter.format(startDate.getValue());
            return paramStartDate.trim();
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        catch (java.lang.IllegalArgumentException iae) {
            // Bury this, fix for 11.1.1.4
        }

        return null;
    }

    public String getParamEndDate() {
        try {
            paramEndDate = formatter.format(endDate.getValue());
            return paramEndDate.trim();
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4

        }
        catch (java.lang.IllegalArgumentException iae) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    public String getParamTerm() {
        try {
            String temp = String.valueOf(term.getValue());
            if (temp != null)
                paramTerm = temp.trim();
            return paramTerm;
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4

        }
        return null;
    }

    public String getParamReleaseStatus() {
        try {
            List<String> selected = (List<String>)releaseStatus.getValue();
            if (selected == null)
                return null;
            String csvValue = "";

            if (selected.size() == 2)
                paramReleaseStatus = BOTH_RELEASE_STATUSES;
            else
                paramReleaseStatus = selected.get(0);

            return paramReleaseStatus;
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    public String getParamActivityStatus() {
        try {
            List<String> selected = (List<String>)activityStatus.getValue();
            if (selected == null)
                return null;
            String csvValue = "";

            if (selected.size() == 2)
                paramActivityStatus = BOTH_ACTIVITY_STATUSES;
            else
                paramActivityStatus = selected.get(0);


            return paramActivityStatus;
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;

    }

    public String getParamState() {
        try {
            List<String> selected = (List<String>)state.getValue();
            if (selected == null)
                return null;
            String csvValue = "";
            for (String s : selected)
                csvValue = csvValue + s + ",";

            paramState = csvValue.substring(0, csvValue.length() - 1);
            return paramState;
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    public String getParamReleaseGroup() {
        try {
            String temp = String.valueOf(releaseGroupSearch.getValue());
            if (temp != null)
                paramReleaseGroup = temp.trim();
            return paramReleaseGroup;
        }
        catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }


   

    public void setSearchResults(RichTable searchResults) {
        this.searchResults = searchResults;
    }

    public RichTable getSearchResults() {
        return searchResults;
    }


    private void copyTermAttributes() {
        
    }

    private void clearFields() {
        
    }


    
    public void setMqName(RichInputText mqName) {
        this.mqName = mqName;
    }

    public RichInputText getMqName() {
        return mqName;
    }

    public void setLevelList(RichSelectOneChoice levelList) {
        this.levelList = levelList;
    }

    public RichSelectOneChoice getLevelList() {
        return levelList;
    }

    public void setMqCode(RichInputText mqCode) {
        this.mqCode = mqCode;
    }

    public RichInputText getMqCode() {
        return mqCode;
    }

    public void setDictionaryVersion(RichInputText dictionaryVersion) {
        this.dictionaryVersion = dictionaryVersion;
    }

    public RichInputText getDictionaryVersion() {
        return dictionaryVersion;
    }

    public void setMqAlgorithm(RichInputText mqAlgorithm) {
        this.mqAlgorithm = mqAlgorithm;
    }

    public RichInputText getMqAlgorithm() {
        return mqAlgorithm;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        if (statusValue == null)
            return null;
        return statusValue.trim();
    }

   

   
    public void setProductList(RichSelectManyChoice productList) {
        this.productList = productList;
    }

    public RichSelectManyChoice getProductList() {
        return productList;
    }


    


    public String getParamMQGroupList() {
        BindingContainer bc = this.getBindings();
        JUCtrlListBinding listBinding = (JUCtrlListBinding)bc.get("MQGroupsVO1");
        if (listBinding == null) return "";
        Object[] str = listBinding.getSelectedValues();
        if (str == null || str.length == 0)
            return null;
        String csvValue = "";
        for (int i = 0; i < str.length; i++) {
            csvValue += str[i] + ",";
        }
        paramMQGroupList = csvValue.substring(0, csvValue.length() - 1);
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


    public void setMQGroups(RichSelectManyChoice MQGroups) {
        this.MQGroups = MQGroups;
    }

    public RichSelectManyChoice getMQGroups() {
        return MQGroups;
    }

    public void setActivityStatus(RichSelectManyCheckbox activityStatus) {
        this.activityStatus = activityStatus;
    }

    public RichSelectManyCheckbox getActivityStatus() {
        return activityStatus;
    }


    public void saveHierarchy(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.ok)) {
            CSMQBean.logger.info(userBean.getCaller() + " SAVING HIERARCHY");
            //new PLSQLEntityImpl().callStoredProcedure("devguidepkg.proc_with_three_args(?,?,?)", new Object[]{n,d,v});
        }
    }

    public void setProductListSelectItems(UISelectItems productListSelectItems) {
        this.productListSelectItems = productListSelectItems;
    }

    public UISelectItems getProductListSelectItems() {
        return productListSelectItems;
    }

    public void setCurrentTermName(String currentTermName) {
        this.currentTermName = currentTermName;
    }

    public String getCurrentTermName() {
        return currentTermName;
    }

    public String editSelectedTerm() {
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

    public void setCurrentDateRequested(String currentDateRequested) {
        this.currentDateRequested = currentDateRequested;
    }

    public String getCurrentDateRequested() {
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
        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE ****");
         
        resolveMethodExpression("#{bindings.SimpleSearch1.collectionModel.makeCurrent}", null, new Class[] { SelectionEvent.class },
                                new Object[] { selectionEvent });
        RichTable object = (RichTable)selectionEvent.getSource();
        Row row = null;
        for (Object facesRowKey : object.getSelectedRowKeys()) {
            object.setRowKey(facesRowKey);
            Object o = object.getRowData();
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)o;
            row = rowData.getRow();
        }
        
        if (row == null) return;
        
        currentTermName = Utils.getAsString(row, "Mqterm");
        currentMqlevel = Utils.getAsString(row, "Mqlevel");
        currentMqcode = Utils.getAsString(row, "Mqcode");
        currentMqalgo = Utils.getAsString(row, "Mqalgo");
        currentMqaltcode = Utils.getAsString(row, "Mqaltcode");
        currentDictionary = getParamDictName();
        currentReleaseGroup = getParamReleaseGroup();
        currentMqstatus = Utils.getAsString(row, "Mqstatus");
        currentDictContentID = Utils.getAsString(row, "ContentId");
        currentApprovalFlag = Utils.getAsString(row, "ApprFlag");
        currentVersion = Utils.getAsString(row, "Version");
        currentSubType = Utils.getAsString(row, "TermSubtype");
        currentMqscp = Utils.getAsString(row, "Mqscp");
        currentMqgroups = Utils.getAsString(row, "MqgroupF");
        currentMqproduct = Utils.getAsString(row, "Mqprodct");
        currentCriticalEvent = Utils.getAsString(row, "Mqcrtev");


        CSMQBean.logger.info(userBean.getCaller() + " currentTermName:" + currentTermName);
        CSMQBean.logger.info(userBean.getCaller() + " currentMqlevel:" + currentMqlevel);
        CSMQBean.logger.info(userBean.getCaller() + " currentMqcode:" + currentMqcode);
        CSMQBean.logger.info(userBean.getCaller() + " currentMqalgo:" + currentMqalgo);
        CSMQBean.logger.info(userBean.getCaller() + " currentMqaltcode:" + currentMqaltcode);
        CSMQBean.logger.info(userBean.getCaller() + " currentDictionary:" + currentDictionary);
        CSMQBean.logger.info(userBean.getCaller() + " currentReleaseGroup:" + currentReleaseGroup);
        CSMQBean.logger.info(userBean.getCaller() + " currentMqstatus:" + currentMqstatus);
        CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID:" + currentDictContentID);
        CSMQBean.logger.info(userBean.getCaller() + " currentApprovalFlag:" + currentApprovalFlag);
        CSMQBean.logger.info(userBean.getCaller() + " currentVersion:" + currentVersion);
        CSMQBean.logger.info(userBean.getCaller() + " currentSubType:" + currentSubType);
        CSMQBean.logger.info(userBean.getCaller() + " currentMqscp:" + currentMqscp);
        CSMQBean.logger.info(userBean.getCaller() + " currentCriticalEvent:" + currentCriticalEvent);
        //copyTermAttributes();
  
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
        if (currentSubType == null) return null;
        if (currentSubType.equals("C")) return "CUSTOM";
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

    public void setCreateHierarchy(boolean createHierarchy) {
        this.createHierarchy = createHierarchy;
    }

    public boolean isCreateHierarchy() {
        return createHierarchy;
    }

    public void setDetailsPanel(RichPanelFormLayout detailsPanel) {
        this.detailsPanel = detailsPanel;
    }

    public RichPanelFormLayout getDetailsPanel() {
        return detailsPanel;
    }

    public void showHierarchy(ActionEvent actionEvent) {
        this.createHierarchy = true;
        this.createList = true;
    }

    public void setCreateList(boolean createList) {
        this.createList = createList;
    }

    public boolean isCreateList() {
        return createList;
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
}

