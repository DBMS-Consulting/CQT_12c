package com.dbms.csmq.view.backing.NMQ;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.RenderingRulesBean;
import com.dbms.csmq.view.hierarchy.TermHierarchyBean;
import com.dbms.util.ADFUtils;
import com.dbms.util.Utils;

import java.io.OutputStream;

import java.math.BigDecimal;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichTrain;
import oracle.adf.view.rich.component.rich.output.RichSpacer;
import oracle.adf.view.rich.component.rich.output.RichStatusIndicator;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class NMQWizardSearchBean  {

    private RichSelectOneChoice ctrlReleaseStatus;
    private Integer productRowsSize;
    private Integer groupRowsSize;
    private RichInputDate ctrlStartDate;
    private RichInputDate ctrlEndDate;
    private RichSelectManyChoice ctrlState;
    private RichSelectManyChoice ctrlMQGroups;
    private RichSelectManyChoice ctrlProducts;
    private RichSelectOneChoice ctrlReleaseGroupSearch;
    private RichSelectOneChoice ctrlDictionaryListSearch;
    private RichTable ctrlSearchResults;
    private RichTable ctrlHistoricalResults;
    private RichTable ctrlHistoricalDateListResults;
    private RichInputText ctrlDictionaryVersion;
    private RichInputText ctrlMQAlgorithm;
    private RichSelectManyChoice ctrlProductList;
    private RichInputText ctrlMQName;
    private RichSelectOneChoice ctrlLevelList;
    private RichInputText ctrlMQCode;
    private RichSelectOneChoice ctrlDictionaryTypeSearch;
    private RichSelectOneChoice ctrlDictionary;
    private RichStatusIndicator ctrlStatusIndicator;
    private RichSelectOneChoice ctrlNMQStatus;
    private RichSelectOneChoice ctrlMQScope;
    private RichSelectOneChoice ctrlCriticalEvent;
    private RichSelectOneChoice ctrlQuery;
    private RichPanelBox cntrlSearchPanel;
    private ArrayList<ProductSearchPojo> productSearchRows = new ArrayList<ProductSearchPojo>();
    private ArrayList<SelectItem> columnNameLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> columnValueLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> operatorLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> productValueLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> scopeValueLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> extensionValueLOV = new ArrayList<SelectItem>();
    private ArrayList<GroupSearchPojo> groupSearchRows = new ArrayList<GroupSearchPojo>();
    private ArrayList<SelectItem> groupSearchColumnNameLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> groupValueLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> groupExtensionValueLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> groupOperatorLOV = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> groupColumnNameLOV = new ArrayList<SelectItem>();
    //SEARCH PARAMS
    private String paramDictName = null;
    private String paramStartDate = null;
    private String paramEndDate = null;
    private String paramTerm = null;
    private String paramReleaseStatus = null;
    private String paramActivityStatus = null;
    private String paramState = null;
    private String paramReleaseGroup = CSMQBean.WILDCARD;
    private String paramMQGroupList = null;
    private String paramProductList = null;
    private String paramMQCode = CSMQBean.WILDCARD;
    private String paramMQCriticalEvent = null;
    private String paramUserName = null;
    private String paramUniqueIDsOnly = CSMQBean.TRUE;
    private String paramFilterForUser = null;
    private String paramQueryType = CSMQBean.NMQ_SMQ_SEARCH;
    private String paramNarrowScopeOnly = CSMQBean.FALSE;
    private Integer paramKillSwitch = CSMQBean.KILL_SWITCH_ON;
    private String paramMQScope = CSMQBean.WILDCARD;
    private String paramUserRole = CSMQBean.ROLE_USER;
    private int paramMode = 0;
    private String paramApproved = CSMQBean.WILDCARD;
    private String searchIterator = "";
    private String dictionaryVersion = "CURRENT";
    

    // CURRENT SELECTED DATA
    private String currentDictContentID;
    private String currentReleaseGroup;
    private String currentTermName;
    private String currentMqlevel;
    private String currentMqcode = "%";
    private String currentMqalgo;
    private String currentMqaltcode;
    private String currentDictionary = CSMQBean.getProperty("DEFAULT_FILTER_DICTIONARY_SHORT_NAME");
    private String currentMqstatus;
    private String currentRequestor;
    private oracle.jbo.domain.Date currentDateRequested;
    private String currentCriticalEvent;
    private String currentApprovalFlag;
    private String currentSubType;
    private String currentVersion;
    private String currentMqscp;
    private String currentMqgroups;
    private String currentMqproduct;
    private String currentDictionaryType;
    private String currentStatus;
    private String currentState; 
    private oracle.jbo.domain.Date requestedByDate;
    private String currentReasonForRequest;
    private String currentReasonForApproval;
    private boolean isApproved = false;
    private String currentCutOffDate;
    private String currentUntilDate;
    private String currentCreateDate;
    private String currentCreatedBy;
    private String currentExtension;
    
    
    
    private List <String> mQGroupList = new ArrayList <String>();
    private List <String> productList = new ArrayList <String>();
    
    private ArrayList <SelectItem> releaseGroupSelectItems;
    private ArrayList <SelectItem> getLevelsForQueryType;
    
    public static final String SMQ_LABEL = CSMQBean.SMQ_NAME;
    public static final String NMQ_LABEL = CSMQBean.customMQName;
    public static final String NMQ_SMQ_LABEL = NMQ_LABEL + "/" + SMQ_LABEL;
    
    
    private String searchLabelPrefix = NMQ_SMQ_LABEL;  // this is needed because SMQs are AKA MQs, but NMQs are always NMQs
    private String detailsLabelPrefix = NMQ_SMQ_LABEL;
    
    
    // ** CURRENT INF NOTES
    private String currentInfNoteDescription;
    private String currentInfNoteSource;
    private String currentInfNoteNotes;


    private Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
   
    // BEANS USED
    private NMQWizardBean nMQWizardBean;
    private UserBean userBean;    
    private CSMQBean cSMQBean;
    private TermHierarchyBean termHierarchyBean;
    private RenderingRulesBean renderingRulesBean;
    private RichTrain cntrlTrain;
    private RichSelectOneChoice cntrlApproved;
    private RichColumn cntrlApprovedColumn;
    private NMQWizardUIBean nMQWizardUIBean;
    
    
    //private String clearSearch;

    private boolean IASearch = false;
    private boolean MEDDraSearch = false; 
    private RichPanelGroupLayout cntrlResultsPanel;
    private RichSelectOneChoice cntrlDictionaryVersion;
    private RichPanelGroupLayout cntrlParamPanel;
    private RichSpacer cntrlReleaseGroupSpacer;
    private String paramExtension = CSMQBean.ALL_EXTENSIONS;
    private String paramLevel = CSMQBean.FILTER_LEVEL_ONE;;
    private RichSelectOneChoice controlMQLevel;
    private List<String> modifiedDesigneeList = new ArrayList<String>();
    
    private Date historyInputDate;
    private Date historyDate;
    private boolean historyFlow = false;
    private RichTable productSearchBinding;
    private RichTable groupSearchBinding;
    private RichCommandButton searchBtnBinding;
    private RichPopup productSearchPopup;
    private RichPopup groupSearchPopup;

    public void setCtrlDictionaryTypeSearch(RichSelectOneChoice dictionaryTypeSearch) {
        this.ctrlDictionaryTypeSearch = dictionaryTypeSearch;
    }

    public RichSelectOneChoice getCtrlDictionaryTypeSearch() {
        return ctrlDictionaryTypeSearch;
    }


    public NMQWizardSearchBean() {
        super();
        cSMQBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        nMQWizardBean = (NMQWizardBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
        userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
        termHierarchyBean = (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
         
        nMQWizardUIBean = (NMQWizardUIBean) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{NMQWizardUIBean}", NMQWizardUIBean.class);
           
           
        setUIDefaults();
        }

    BindingContext bc = BindingContext.getCurrent();
    DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();

    private void setUIDefaults () {
        
        
        this.paramUserName = userBean.getCurrentUser();
        // Added user name empty to check to fix adf error - Venkat
        if (userBean.isMQM()|| userBean.isAdmin() || paramUserName.isEmpty()) {
            this.paramFilterForUser = CSMQBean.FALSE;
         }
        else if (userBean.isRequestor()) {
            this.paramFilterForUser = CSMQBean.TRUE;
            this.paramActivityStatus = CSMQBean.ACTIVE_ACTIVITY_STATUS;
            }
        // IT'S A BROWSE USER - SHOW ONLY CURRENT & ACTIVE NMQs
        else {
            this.paramReleaseStatus = CSMQBean.CURRENT_RELEASE_STATUS;
            this.paramActivityStatus = CSMQBean.ACTIVE_ACTIVITY_STATUS;
            this.paramFilterForUser = CSMQBean.TRUE;
            }
       
        // OVERWRITE IF IT'S AN IA SEARCH
        if (IASearch) {
            this.paramReleaseStatus = CSMQBean.BOTH_RELEASE_STATUSES;
            this.paramActivityStatus = CSMQBean.ACTIVE_ACTIVITY_STATUS;
            }
       
       
        //set defaults     
        if (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_EXISTING) {
            this.searchIterator = "SimpleSearch1Iterator";
            //this.searchLabelPrefix = NMQ_LABEL;
            this.detailsLabelPrefix = NMQ_LABEL;
            getLevelsForQueryType = cSMQBean.getLevelsForQueryType("QUERY_TYPE");
             if (ctrlLevelList != null)
                ctrlLevelList.setRendered(false);
            this.paramQueryType = CSMQBean.NMQ_SEARCH;
            }
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_SMQ) {
            this.searchIterator = "SimpleSearch1Iterator";
            //this.searchLabelPrefix = SMQ_LABEL;
            this.detailsLabelPrefix = SMQ_LABEL;
            if (ctrlLevelList != null)
                ctrlLevelList.setRendered(false);
            this.paramQueryType = CSMQBean.SMQ_SEARCH;
            this.paramExtension = SMQ_LABEL;
            }
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_COPY_EXISTING) {
            this.searchIterator = "SimpleSearch1Iterator";
            //this.searchLabelPrefix = NMQ_SMQ_LABEL;
            this.detailsLabelPrefix = NMQ_LABEL;
            getLevelsForQueryType = cSMQBean.getLevelsForQueryType("QUERY_TYPE");
            if (ctrlLevelList != null) {
                ctrlLevelList.setValue(CSMQBean.SMQ_SEARCH);
                }
            this.paramQueryType = CSMQBean.NMQ_SMQ_SEARCH;
            }
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_INSERT_NEW) {
            this.searchIterator = "SimpleSearch1Iterator";
            //this.searchLabelPrefix = NMQ_LABEL;
            this.detailsLabelPrefix = NMQ_LABEL;
            getLevelsForQueryType = cSMQBean.getLevelsForQueryType("NMQ_SQM_SELECT_ITEMS");
            }
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_HISTORIC) { 
            this.searchIterator = "HistoricSearch1Iterator";
            //this.searchLabelPrefix = NMQ_SMQ_LABEL;
            this.detailsLabelPrefix = "";
            getLevelsForQueryType = cSMQBean.getLevelsForQueryType("QUERY_TYPE");
            this.paramQueryType = CSMQBean.NMQ_SMQ_SEARCH;
            if (ctrlLevelList != null) {
                ctrlLevelList.setValue(CSMQBean.NMQ_SMQ_SEARCH);
                }
            }
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_BROWSE_SEARCH) { 
            this.searchIterator = "SimpleSearch1Iterator";
            //this.searchLabelPrefix = NMQ_SMQ_LABEL;
            this.detailsLabelPrefix = "";
            getLevelsForQueryType = cSMQBean.getLevelsForQueryType("NMQ_SQM_SELECT_ITEMS");
            this.paramQueryType = CSMQBean.NMQ_SMQ_SEARCH;
            
            if (ctrlNMQStatus != null)
                ctrlNMQStatus.setValue(CSMQBean.BOTH_RELEASE_STATUSES);
            if (ctrlLevelList != null) {
                ctrlLevelList.setValue(CSMQBean.NMQ_SMQ_SEARCH);
                }
            }
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT) { 
            this.searchIterator = "SimpleSearch1Iterator";
            //this.searchLabelPrefix = NMQ_SMQ_LABEL;
            this.detailsLabelPrefix = "";
            getLevelsForQueryType = cSMQBean.getLevelsForQueryType("NMQ_SQM_SELECT_ITEMS");
            this.paramQueryType = CSMQBean.NMQ_SMQ_SEARCH;
            if (ctrlLevelList != null) {
                ctrlLevelList.setValue(CSMQBean.NMQ_SMQ_SEARCH);
                }
        } else if (nMQWizardBean.getMode() == CSMQBean.MODE_VIEW_VERSION_IMPACT) { 
            this.searchIterator = "SimpleSearch1Iterator";
            //this.searchLabelPrefix = NMQ_SMQ_LABEL;
            this.detailsLabelPrefix = "";
            getLevelsForQueryType = cSMQBean.getLevelsForQueryType("NMQ_SQM_SELECT_ITEMS");
            this.paramQueryType = CSMQBean.NMQ_SMQ_SEARCH;
            if (ctrlLevelList != null) {
                ctrlLevelList.setValue(CSMQBean.NMQ_SMQ_SEARCH);
                }
            }
              
        clearSearch (this.searchIterator);
        releaseGroupSelectItems = cSMQBean.getAGsForDictionary(nMQWizardBean.getCurrentDictionary());
        }
    

    private void clearSearch (String iterator) {
        try {
            BindingContext bc = BindingContext.getCurrent();
            DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
            DCIteratorBinding dciterb = (DCIteratorBinding)binding.get(iterator);
            ViewObject vo = dciterb.getViewObject();
            vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_ON);
            vo.executeQuery();

            ctrlSearchResults.setEmptyText("Selected Term: " + this.currentTermName);
            AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
            
            
           
            //nMQWizardUIBean.getProductListControl().resetValue();
            }
        catch (Exception e) {}
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
        
        if(actionEvent != null){
            RichCommandButton button = (RichCommandButton) actionEvent.getComponent();
            if(button != null && button.getText() != null && 
               button.getText().equalsIgnoreCase("Search")){
                clearProductSearchRows(actionEvent); 
                clearGroupSearchRows(actionEvent);
            }
        }
            // 05-NOV-2013
            // Fix for dupes 
        nMQWizardBean.clearDetails();
        
        // FIX FOR WHEN A USER CANCELS AND COMES BACK IN
        if (searchIterator.length() == 0) setUIDefaults ();
        
        
        nMQWizardBean.getProductList().clear();
        nMQWizardBean.getMQGroupList().clear();
        if (termHierarchyBean != null)
        termHierarchyBean.showStatus(CSMQBean.MQ_INIT);
        nMQWizardBean.setTreeAccessed(false); //reset the tree
        //nMQWizardBean.clearDetails();  UI MOVE?
        
        String activationGroup = getParamReleaseGroup();
        String queryLevel = getParamLevel();
        if (IASearch){
            CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING IA SEARCH **");
            activationGroup = CSMQBean.WILDCARD;
        } else {
            CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING SEARCH **");
        }
        CSMQBean.logger.info(userBean.getCaller() + " searchIterator: " + searchIterator);
        CSMQBean.logger.info(userBean.getCaller() + " startDate: " + getParamStartDate());
        CSMQBean.logger.info(userBean.getCaller() + " endDate: " + getParamEndDate());
        CSMQBean.logger.info(userBean.getCaller() + " term: " + getParamTerm());
        CSMQBean.logger.info(userBean.getCaller() + " activityStatus: " + getParamActivityStatus());
        CSMQBean.logger.info(userBean.getCaller() + " dictShortName: " + getParamDictName());
        CSMQBean.logger.info(userBean.getCaller() + " releaseStatus: " + getParamReleaseStatus());
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + activationGroup);
        CSMQBean.logger.info(userBean.getCaller() + " MQGroup: " + getParamMQGroupList());
        CSMQBean.logger.info(userBean.getCaller() + " product: " + getParamProductList());
        CSMQBean.logger.info(userBean.getCaller() + " MQCode: " + getParamMQCode());
        CSMQBean.logger.info(userBean.getCaller() + " MQCriticalEvent: " + getParamMQCriticalEvent());
        CSMQBean.logger.info(userBean.getCaller() + " uniqueIDsOnly: " + getParamUniqueIDsOnly());
       // CSMQBean.logger.info(userBean.getCaller() + " filterForUser: " +  getParamFilterForUser());
        CSMQBean.logger.info(userBean.getCaller() + " currentUser: " + getParamUserName().toUpperCase());
        CSMQBean.logger.info(userBean.getCaller() + " mqType: " + getParamQueryType());
        CSMQBean.logger.info(userBean.getCaller() + " showNarrowScpOnly: " + getParamNarrowScopeOnly());
        CSMQBean.logger.info(userBean.getCaller() + " MQScope: " + getParamMQScope());
        CSMQBean.logger.info(userBean.getCaller() + " pState: " + getParamState());
        CSMQBean.logger.info(userBean.getCaller() + " pUserRole: " + getParamUserRole());
        CSMQBean.logger.info(userBean.getCaller() + " pMode: " + getParamMode());
        CSMQBean.logger.info(userBean.getCaller() + " pApprove: " + getParamApproved());
        CSMQBean.logger.info(userBean.getCaller() + " psVirtualDictionaryName: " + getDictionaryVersion());
        CSMQBean.logger.info(userBean.getCaller() + " queryLevel: " + queryLevel);
        CSMQBean.logger.info(userBean.getCaller() + " extension: " + getParamExtension());
        CSMQBean.logger.info(userBean.getCaller() + " killSwitch: " + CSMQBean.KILL_SWITCH_OFF);
        
        CSMQBean.logger.info(userBean.getCaller() + " ** DATABASE DEBUGGING INFO **");
        CSMQBean.logger.info(userBean.getCaller() + " pStartDate: " + getParamStartDate());
        CSMQBean.logger.info(userBean.getCaller() + " pEndDate: " + getParamEndDate());
        CSMQBean.logger.info(userBean.getCaller() + " psTerm: " + getParamTerm());
        CSMQBean.logger.info(userBean.getCaller() + " pMQStatus: " + getParamActivityStatus());
        CSMQBean.logger.info(userBean.getCaller() + " psDictionaryName: " + getParamDictName());
        CSMQBean.logger.info(userBean.getCaller() + " pCurrPendStatus: " + getParamReleaseStatus());
        CSMQBean.logger.info(userBean.getCaller() + " pRelGroup: " + activationGroup);
        CSMQBean.logger.info(userBean.getCaller() + " pMQGroup: " + getParamMQGroupList());
        CSMQBean.logger.info(userBean.getCaller() + " pProduct: " + getParamProductList());
        CSMQBean.logger.info(userBean.getCaller() + " pMQCode: " + getParamMQCode());
        CSMQBean.logger.info(userBean.getCaller() + " pMQCrtlEvt: " + getParamMQCriticalEvent());
        CSMQBean.logger.info(userBean.getCaller() + " pUniqueIdOnly: " + getParamUniqueIDsOnly());
        CSMQBean.logger.info(userBean.getCaller() + " pCurrentUser: " + getParamUserName().toUpperCase());
        CSMQBean.logger.info(userBean.getCaller() + " pLevel: " + getParamQueryType());
        CSMQBean.logger.info(userBean.getCaller() + " pNarrowScpOnlyMq: " + getParamNarrowScopeOnly());
        CSMQBean.logger.info(userBean.getCaller() + " pMQSCP: " + getParamMQScope());
        CSMQBean.logger.info(userBean.getCaller() + " pState: " + getParamState());
        CSMQBean.logger.info(userBean.getCaller() + " pUserRole: " + getParamUserRole());
        CSMQBean.logger.info(userBean.getCaller() + " pMode: " + getParamMode());
        CSMQBean.logger.info(userBean.getCaller() + " pApprove: " + getParamApproved());
        CSMQBean.logger.info(userBean.getCaller() + " psVirtualDictionaryName: " + getDictionaryVersion());
        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get(searchIterator);
        ViewObject vo = dciterb.getViewObject();
        vo.setWhereClause(null);
        
        vo.setNamedWhereClauseParam("startDate", getParamStartDate());
        vo.setNamedWhereClauseParam("endDate", getParamEndDate());
        String paramTermVal = getParamTerm();
        if (null != paramTermVal && !paramTermVal.isEmpty()){
           paramTermVal = paramTermVal.replace("'","\''");
        }
        vo.setNamedWhereClauseParam("term", paramTermVal);
        vo.setNamedWhereClauseParam("activityStatus", getParamActivityStatus());
        vo.setNamedWhereClauseParam("dictShortName", getParamDictName());
        vo.setNamedWhereClauseParam("releaseStatus", getParamReleaseStatus());
        vo.setNamedWhereClauseParam("activationGroup", activationGroup);
        vo.setNamedWhereClauseParam("MQGroup", getParamMQGroupList());  // search needs ^ as the delimiter
        vo.setNamedWhereClauseParam("product", getParamProductList());  // search needs ^ as the delimiter
        vo.setNamedWhereClauseParam("MQCode", getParamMQCode());
        vo.setNamedWhereClauseParam("MQCriticalEvent", getParamMQCriticalEvent());
        vo.setNamedWhereClauseParam("uniqueIDsOnly", getParamUniqueIDsOnly());
        //vo.setNamedWhereClauseParam("filterForUser",  getParamFilterForUser());
        vo.setNamedWhereClauseParam("currentUser", getParamUserName().toString());
        vo.setNamedWhereClauseParam("mqType", getParamQueryType());
        vo.setNamedWhereClauseParam("showNarrowScpOnly", getParamNarrowScopeOnly());
        vo.setNamedWhereClauseParam("pState", getParamState());
        vo.setNamedWhereClauseParam("MQScope", getParamMQScope());
        vo.setNamedWhereClauseParam("pUserRole", getParamUserRole());      
        vo.setNamedWhereClauseParam("pMode", getParamMode());
        vo.setNamedWhereClauseParam("pApprove", getParamApproved());
        vo.setNamedWhereClauseParam("psVirtualDictionaryName", getDictionaryVersion());
        vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_OFF);
        
        vo.setNamedWhereClauseParam("queryLevel", queryLevel);
        vo.setNamedWhereClauseParam("extension", getParamExtension());
        //
        if (null != getParamReleaseStatus() && CSMQBean.CURRENT_RELEASE_STATUS.equalsIgnoreCase(getParamReleaseStatus())
            && (null != getParamUserRole() && CSMQBean.ROLE_REQUESTOR.equalsIgnoreCase(getParamUserRole()))
            && (CSMQBean.MODE_UPDATE_EXISTING == getParamMode() || CSMQBean.MODE_UPDATE_SMQ == getParamMode())
            && (nMQWizardBean.isIsNMQ())
        ){
            vo.setNamedWhereClauseParam("filterForUser", CSMQBean.FALSE);
            CSMQBean.logger.info(userBean.getCaller() + " pFilterForUser: " +  CSMQBean.FALSE);
        } else {
            vo.setNamedWhereClauseParam("filterForUser",  getParamFilterForUser());
            CSMQBean.logger.info(userBean.getCaller() + " pFilterForUser: " +  getParamFilterForUser());
        }
        vo.executeQuery();
        CSMQBean.logger.info(userBean.getCaller() + " Simple Search Query:: " + vo.getQuery());
        if (ctrlSearchResults != null) {  // if we are calling this from IA, we won't need this
            CSMQBean.logger.info(userBean.getCaller() + " ctrlSearchResults is not null :: ");
            ctrlSearchResults.setEmptyText("No data to display.");
            AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
            //clear the selected row
            RowKeySet rks= ctrlSearchResults.getSelectedRowKeys();
            rks.clear();
        
            //CLEAR OLD TRESS
            NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
            nMQSourceTermSearchBean.clearTree();
            nMQWizardBean.clearRelations();
            }
        }
    
    public void doHistoricSearch(ActionEvent actionEvent) {        
        //nMQWizardBean.clearDetails();  UI MOVE?
        CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING HISTORIC SEARCH **");
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
        CSMQBean.logger.info(userBean.getCaller() + " mqType: " + getParamQueryType() );
        CSMQBean.logger.info(userBean.getCaller() + " showNarrowScpOnly: " + getParamNarrowScopeOnly());
        CSMQBean.logger.info(userBean.getCaller() + " MQScope: " + getParamMQScope());
        CSMQBean.logger.info(userBean.getCaller() + " pState: " + getParamState());
        CSMQBean.logger.info(userBean.getCaller() + " pUserRole: " + getParamUserRole());
        CSMQBean.logger.info(userBean.getCaller() + " pMode: " + getParamMode());
        CSMQBean.logger.info(userBean.getCaller() + " pApprove: " + getParamApproved());
        CSMQBean.logger.info(userBean.getCaller() + " killSwitch: " + CSMQBean.KILL_SWITCH_OFF);
        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
        
        // set the query type
        
        /* 
         * TES CHANGED 06-AUG-2014
         */
        //nMQWizardBean.setIsNMQ(paramQueryType.indexOf("N") > -1);
        nMQWizardBean.setIsNMQ(this.currentExtension.equalsIgnoreCase("NMQ"));
        nMQWizardBean.setIsSMQ(this.currentExtension.equalsIgnoreCase("SMQ"));
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get(this.searchIterator);
        ViewObject vo = dciterb.getViewObject();
        
        vo.setNamedWhereClauseParam("startDate", getParamStartDate());
        vo.setNamedWhereClauseParam("endDate", getParamEndDate());
        String paramTermVal = getParamTerm();
        if (null != paramTermVal && !paramTermVal.isEmpty()){
           paramTermVal = paramTermVal.replace("'","\''");
        }
        vo.setNamedWhereClauseParam("term", paramTermVal);
        vo.setNamedWhereClauseParam("activityStatus", getParamActivityStatus());
        vo.setNamedWhereClauseParam("dictShortName", getParamDictName());
        vo.setNamedWhereClauseParam("releaseStatus", getParamReleaseStatus());
        vo.setNamedWhereClauseParam("activationGroup", getParamReleaseGroup());
        vo.setNamedWhereClauseParam("MQGroup", getParamMQGroupList());  // search needs ^ as the delimiter
        vo.setNamedWhereClauseParam("product", getParamProductList());  // search needs ^ as the delimiter
        vo.setNamedWhereClauseParam("MQCode", getParamMQCode());
        vo.setNamedWhereClauseParam("MQCriticalEvent", getParamMQCriticalEvent());
        vo.setNamedWhereClauseParam("uniqueIDsOnly", getParamUniqueIDsOnly());
        vo.setNamedWhereClauseParam("filterForUser",  getParamFilterForUser());
        vo.setNamedWhereClauseParam("currentUser", getParamUserName());
        vo.setNamedWhereClauseParam("mqType", getParamQueryType());
        vo.setNamedWhereClauseParam("showNarrowScpOnly", getParamNarrowScopeOnly());
        vo.setNamedWhereClauseParam("pState", getParamState());
        vo.setNamedWhereClauseParam("MQScope", getParamMQScope());
        vo.setNamedWhereClauseParam("pUserRole", getParamUserRole());      
        vo.setNamedWhereClauseParam("pMode", getParamMode());
        vo.setNamedWhereClauseParam("pApprove", getParamApproved());
        vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_OFF);
       
        vo.executeQuery();
        
        if (ctrlHistoricalResults != null) {  // FOR HISTORIC
            ctrlHistoricalDateListResults.setRendered(false);
            ctrlHistoricalResults.setRendered(true);   
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlResultsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlResultsPanel);
            
            ctrlHistoricalResults.setEmptyText("No data to display.");

            //clear the selected row
            RowKeySet rks = ctrlHistoricalDateListResults.getSelectedRowKeys();
            rks.clear();
            rks = ctrlHistoricalResults.getSelectedRowKeys();
            rks.clear();
        
            //CLEAR OLD TRESS
            NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
            nMQSourceTermSearchBean.clearTree();
            nMQWizardBean.clearRelations();
            }
        
        }


    public void initForImpactAnalysis(String mqCode, String dictContentID, String releaseGroups) {
        try {
            // used for impact analysis
            this.IASearch = true;
            this.currentMqcode = mqCode;
            this.paramMQCode = mqCode;
            this.paramQueryType = CSMQBean.SMQ_SEARCH;
            this.currentDictContentID = dictContentID;
            this.paramState = CSMQBean.WILDCARD;
            this.currentDictionary = CSMQBean.defaultFilterDictionaryShortName;
            this.paramDictName = CSMQBean.defaultFilterDictionaryShortName;
            this.paramReleaseGroup = releaseGroups;
            setUIDefaults();

            doSearch(null); // run the search with the params above - these come from the IA search
            // get the results - there should just be one row
            BindingContext bc = BindingContext.getCurrent();
            DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
            DCIteratorBinding dciterb = (DCIteratorBinding)binding.get(searchIterator);

            Enumeration rows = dciterb.getRowSetIterator().enumerateRowsInRange();
            if (rows == null || !rows.hasMoreElements())
                return;

            Row row = (Row)rows.nextElement();


            processSearchResults(row);
            this.getInfNotes();
            //nMQWizardBean.setMode(mode);
            nMQWizardBean.setCurrentTermName(currentTermName);
            nMQWizardBean.setCurrentFilterDictionaryShortName(this.currentDictionary);
            nMQWizardBean.getDictionaryInfo(); // GET BASE DICT INFO FROM FILTER
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
    }
    
    
    public void getInfNotes() {

        try {
            BindingContext bc = BindingContext.getCurrent();
            DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
            DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("InfNotesVO1Iterator");
            ViewObject vo = dciterb.getViewObject();
            
            
            /*
             *  
             
                // CHANGED 14-MAR-2012 for the follwing rules:
                if the current state is not published and not published IA then the release group = draft.  
                if the current state is published then the release group = RELEASE AG 
                ELSE (if the current state is published IA) then the release group = MEDDRA AG.

                String relGroup = cSMQBean.getDefaultPublishReleaseGroup();
                if (this.currentState != null && !(this.currentState.equals(CSMQBean.STATE_PUBLISHED)))
                relGroup = cSMQBean.getDefaultDraftReleaseGroup();
             
             * 
             */
            
            String relGroup = cSMQBean.getDefaultDraftReleaseGroup();
            if (this.currentState != null && this.currentState.equals(CSMQBean.STATE_PUBLISHED)) relGroup = cSMQBean.defaultPublishReleaseGroup;
            if (this.currentState != null && this.currentState.equals(CSMQBean.IA_STATE_PUBLISHED)) relGroup = cSMQBean.defaultMedDRAReleaseGroup;
            
            String tStatus = CSMQBean.CURRENT_IF_PENDING_NULL;
            if (this.currentStatus != null && this.currentStatus.equals(CSMQBean.CURRENT_RELEASE_STATUS)) tStatus = CSMQBean.CURRENT_RELEASE_STATUS;
            
            //override for IA
            if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT || nMQWizardBean.getMode() == CSMQBean.MODE_VIEW_VERSION_IMPACT){
                tStatus = CSMQBean.CURRENT_IF_PENDING_NULL_IA;
            }
            CSMQBean.logger.info(userBean.getCaller() + " ** GETTING INF NOTES **");
            CSMQBean.logger.info(userBean.getCaller() + " dictContentCode: " + this.currentMqcode);
            CSMQBean.logger.info(userBean.getCaller() + " dictShortName: " + this.currentDictionary);
            CSMQBean.logger.info(userBean.getCaller() + " SMQNOTE: " + cSMQBean.getProperty("SMQ_NOTE_INFORMATIVE_NOTE"));
            CSMQBean.logger.info(userBean.getCaller() + " SMQDESC: " + cSMQBean.getProperty("SMQ_DESCRIPTION_INFORMATIVE_NOTE"));
            CSMQBean.logger.info(userBean.getCaller() + " SMQSRC: " + cSMQBean.getProperty("SMQ_SOURCE_INFORMATIVE_NOTE"));
            CSMQBean.logger.info(userBean.getCaller() + " groupname: " + relGroup);
            CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " + this.currentDictContentID);
            CSMQBean.logger.info(userBean.getCaller() + " currentPendingStatus: " + tStatus);

            vo.setNamedWhereClauseParam("dictContentCode", this.currentMqcode);
            vo.setNamedWhereClauseParam("dictShortName", this.currentDictionary);
            vo.setNamedWhereClauseParam("SMQNOTE", cSMQBean.getProperty("SMQ_NOTE_INFORMATIVE_NOTE"));
            vo.setNamedWhereClauseParam("SMQDESC", cSMQBean.getProperty("SMQ_DESCRIPTION_INFORMATIVE_NOTE"));
            vo.setNamedWhereClauseParam("SMQSRC", cSMQBean.getProperty("SMQ_SOURCE_INFORMATIVE_NOTE"));
            vo.setNamedWhereClauseParam("groupname", relGroup);
            vo.setNamedWhereClauseParam("dictContentID", this.currentDictContentID);
            vo.setNamedWhereClauseParam("currentPendingStatus", tStatus);
            
            vo.executeQuery();
            Enumeration rows = dciterb.getRowSetIterator().enumerateRowsInRange();

            if (!rows.hasMoreElements()) {
                CSMQBean.logger.info(userBean.getCaller() + " ! THERE ARE NO INF NOTES !");
                return;
                }
            
            Row row = (Row)rows.nextElement();

            this.currentInfNoteDescription = Utils.getAsString(row, "Mqdesc");
            this.currentInfNoteSource = Utils.getAsString(row, "Mqsrc");
            this.currentInfNoteNotes = Utils.getAsString(row, "Mqnote");

            nMQWizardBean.setCurrentInfNoteDescription(this.currentInfNoteDescription);
            nMQWizardBean.setCurrentInfNoteNotes(this.currentInfNoteNotes);
            nMQWizardBean.setCurrentInfNoteSource(this.currentInfNoteSource);
            } 
        catch (java.util.NoSuchElementException e) {
            CSMQBean.logger.error(e.getMessage(), e);
            }
        }



    public List <String> getDesignees (String dictContentID) {    
           
        CSMQBean.logger.info(userBean.getCaller() + " ** GETTING DESIGNEES **");
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " + dictContentID);
        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("designeeListByMQVO1Iterator");
        
        if (dciterb == null) return null;
        ViewObject vo = dciterb.getViewObject();
        
        vo.setNamedWhereClauseParam("dictContentID", dictContentID);
        vo.executeQuery();
        
        List <String> retVal = new ArrayList <String> ();
         
        RowSetIterator rs = vo.createRowSetIterator(null);
        rs.reset();
        String designee = "";
        
        if (rs.hasNext()) {
            Row row = rs.next();
            if (row.getAttribute("Designee") != null)
            designee = (String)row.getAttribute("Designee");
            System.out.println("Attribute - " + designee);
            
            designee = designee.replaceAll("\\[", "").replaceAll("\\]","");
            retVal = Arrays.asList(designee.split("\\s*,\\s*"));           
            }
              
        rs.closeRowSetIterator();
        return retVal;
        }


    public void setParamDictName(String paramDictName) {
        this.paramDictName = paramDictName;
    }

    public String getParamDictName() {
        if (IASearch) return currentDictionary;
        
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
        paramTerm = cSMQBean.WILDCARD;
        if (ctrlMQName != null && ctrlMQName.getValue() != null && !ctrlMQName.getValue().toString().equalsIgnoreCase("null") && ctrlMQName.getValue().toString().length() > 0)
            paramTerm = ctrlMQName.getValue().toString();
        return paramTerm;
        
    }

    public String getParamReleaseStatus() {
        if (IASearch) return paramReleaseStatus;
        
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
        if (IASearch || MEDDraSearch) return CSMQBean.BOTH_ACTIVITY_STATUSES;

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
                return "%";
            String csvValue = "";
            for (String s : selected)
                csvValue = csvValue + s + CSMQBean.DEFAULT_SEARCH_DELIMETER_CHAR;

            paramState = csvValue.substring(0, csvValue.length() - 1);
            if (paramState.length() == 0) paramState = "%";
            
            return paramState;
        } catch (java.lang.NullPointerException e) {
            // Bury this, fix for 11.1.1.4
        }
        return null;
    }

    public String getParamReleaseGroup() {
        
        if (IASearch) return paramReleaseGroup;
        
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
        if (IASearch) return CSMQBean.WILDCARD;
        
        if (ctrlMQGroups == null) return null;
        Object selProd = ctrlMQGroups.getValue();
        if (selProd == null) return CSMQBean.WILDCARD;
        
        if (selProd instanceof java.lang.String) {
            String temp = selProd.toString();
            temp.replace("[", "");
            temp.replace("]", "");
            this.paramMQGroupList = temp; 
            }
        else {       
            List selected = (List)ctrlMQGroups.getValue();
            if (selected != null) {
                if (selected.size() == 0) return CSMQBean.WILDCARD;
                String temp = "";
                for (Object s : selected)
                        temp = temp + s + CSMQBean.DEFAULT_DELIMETER_CHAR;
                
                temp.replace("[", "");
                temp.replace("]", "");
                
                if (temp != null & temp.length() > 0)            
                    this.paramMQGroupList = temp.substring(0, temp.length() - 1);
                }
        
        }
        return paramMQGroupList.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, CSMQBean.DEFAULT_SEARCH_DELIMETER_CHAR);
    }
            
            
    public String getParamProductList() {
        if (IASearch) return CSMQBean.WILDCARD;
        
        if (ctrlProducts == null) return null;
        Object selProd = ctrlProducts.getValue();
        if (selProd == null) return CSMQBean.WILDCARD;
        
        if (selProd instanceof java.lang.String) {
            String temp = selProd.toString();
            temp.replace("[", "");
            temp.replace("]", "");
            this.paramProductList = temp; 
            }
        else {       
            List selected = (List)ctrlProducts.getValue();
            if (selected != null) {
                if (selected.size() == 0) return CSMQBean.WILDCARD;
                String temp = "";
                for (Object s : selected)
                        temp = temp + s + CSMQBean.DEFAULT_DELIMETER_CHAR;
                
                temp.replace("[", "");
                temp.replace("]", "");
                
                if (temp != null & temp.length() > 0)            
                    this.paramProductList = temp.substring(0, temp.length() - 1);
                }
        
        }
        return paramProductList.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, CSMQBean.DEFAULT_SEARCH_DELIMETER_CHAR);
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

    public void setCurrentDateRequested(oracle.jbo.domain.Date currentDateRequested) {
        this.currentDateRequested = currentDateRequested;
    }

    public oracle.jbo.domain.Date getCurrentDateRequested() {
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
        nMQWizardBean.setTreeAccessed(false);  //reset this to recreate the tree when the page loads
        nMQWizardBean.clearDetails(); // hopefully this works
        resolveMethodExpression("#{bindings.SimpleSearch1.collectionModel.makeCurrent}", null, new Class[] { SelectionEvent.class }, new Object[] { selectionEvent });
        RichTable object = (RichTable)selectionEvent.getSource();
        Row row = null;
        for (Object facesRowKey : object.getSelectedRowKeys()) {
            object.setRowKey(facesRowKey);
            Object o = object.getRowData();
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)o;
            if (rowData == null) return;
            row = rowData.getRow();
            }

        if (row == null) return;

        processSearchResults(row);
        //Copy mode - reset the level to NMQ levels
        if (nMQWizardBean.getMode() == cSMQBean.MODE_COPY_EXISTING){
            nMQWizardBean.setIsNMQ(Boolean.TRUE);
            if (null!= currentMqlevel && currentMqlevel.equalsIgnoreCase(cSMQBean.SMQ_LEVEL_1)){
                nMQWizardBean.setCurrentTermLevel(cSMQBean.NMQ_LEVEL_1);
            } else if (null!= currentMqlevel && currentMqlevel.equalsIgnoreCase(cSMQBean.SMQ_LEVEL_2)){
                nMQWizardBean.setCurrentTermLevel(cSMQBean.NMQ_LEVEL_2);
            } else if (null!= currentMqlevel && currentMqlevel.equalsIgnoreCase(cSMQBean.SMQ_LEVEL_3)){
                nMQWizardBean.setCurrentTermLevel(cSMQBean.NMQ_LEVEL_3);
            } else if (null!= currentMqlevel && currentMqlevel.equalsIgnoreCase(cSMQBean.SMQ_LEVEL_4)){
                nMQWizardBean.setCurrentTermLevel(cSMQBean.NMQ_LEVEL_4);
            } else if (null!= currentMqlevel && currentMqlevel.equalsIgnoreCase(cSMQBean.SMQ_LEVEL_5)){
                nMQWizardBean.setCurrentTermLevel(cSMQBean.NMQ_LEVEL_5);
            }
            if (nMQWizardBean.isIsNMQ() && userBean.isRequestor() && !(userBean.isMQM()|| userBean.isAdmin())){
                nMQWizardBean.setCurrentState(CSMQBean.STATE_PROPOSED);
            } else {
                nMQWizardBean.setCurrentState(CSMQBean.STATE_DRAFT);
            }
            nMQWizardBean.setCurrentPredictGroups(cSMQBean.getDefaultDraftReleaseGroup());
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlTrain);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlTrain);
        // get the notes.
        getInfNotes();
        
        // update the hierarchy
        //nMQWizardBean.updateRelations();  //??  NOT NEEDED ??
        // ??? AdfFacesContext.getCurrentInstance().addPartialTarget(termHierarchyBean.getTargetTree());
        // ???? AdfFacesContext.getCurrentInstance().partialUpdateNotify(termHierarchyBean.getTargetTree());
        
        //clearSearch("SimpleSearch1Iterator");
        setHistoryDate(null);
        historyFlow = false;
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
        
        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE COMPLETE ****");
    }

    public void onHistoricSearchTableNodeSelection(SelectionEvent selectionEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " ***** HISTORIC ROW CHANGE START ****");
        
        resolveMethodExpression("#{bindings.HistoricSearch1.collectionModel.makeCurrent}", null, new Class[] { SelectionEvent.class }, new Object[] { selectionEvent });
        RichTable object = (RichTable)selectionEvent.getSource();
        Row row = null;
        for (Object facesRowKey : object.getSelectedRowKeys()) {
            object.setRowKey(facesRowKey);
            Object o = object.getRowData();
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)o;
            if (rowData == null) return;
            row = rowData.getRow();
            }

        if (row == null) return;

        processSearchResults(row);    

        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HistoricalListViewObj1Iterator");
        ViewObject vo = dciterb.getViewObject();
        vo.setNamedWhereClauseParam("dictContentID", this.currentDictContentID);
        vo.executeQuery();
        
        ctrlHistoricalDateListResults.setRendered(true);
        ctrlHistoricalResults.setRendered(false);   
        AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlResultsPanel);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlResultsPanel);
        CSMQBean.logger.info(userBean.getCaller() + " ***** HISTORIC ROW CHANGE COMPLETE ****");
    }
    
    public void onHistoricDateListTableNodeSelection(SelectionEvent selectionEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " ***** HISTORIC ROW DATE LIST CHANGE START ****");

        resolveMethodExpression("#{bindings.HistoricalListViewObj1.collectionModel.makeCurrent}", null, new Class[] { SelectionEvent.class }, new Object[] { selectionEvent });
        RichTable object = (RichTable)selectionEvent.getSource();
        Row row = null;
        for (Object facesRowKey : object.getSelectedRowKeys()) {
            object.setRowKey(facesRowKey);
            Object o = object.getRowData();
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)o;
            if (rowData == null) return;
            row = rowData.getRow();
            }

        if (row == null) return;

        processSearchResults(row);        

        AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlTrain);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlTrain);
        
        // get the notes.
        getInfNotes();

        //CLEAR OLD TRESS
        NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
        nMQSourceTermSearchBean.clearTree();
        nMQWizardBean.clearRelations();
        
        // update the hierarchy
        
        //nMQWizardBean.updateRelations(); ???
        clearSearch("HistoricalListViewObj1Iterator");
        CSMQBean.logger.info(userBean.getCaller() + " ***** HISTORIC ROW DATE LIST CHANGE COMPLETE ****");
    }
    
    private void processSearchResults (Row row) {
        CSMQBean.logger.info(userBean.getCaller() + " ***** processSearchResults  Mode ==" + nMQWizardBean.getMode());
        currentTermName = Utils.getAsString(row, "Mqterm");
        CSMQBean.logger.info(userBean.getCaller() + " currentTermName:" + currentTermName);
        
        currentMqlevel = Utils.getAsString(row, "Mqlevel");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqlevel:" + currentMqlevel);
        
        currentMqcode = Utils.getAsString(row, "Mqcode");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqcode:" + currentMqcode);
        
        currentMqalgo = Utils.getAsString(row, "Mqalgo");
            
        if (currentMqalgo == null || currentMqalgo.length() < 1)
            currentMqalgo = CSMQBean.DEFAULT_ALGORITHM;
            
        CSMQBean.logger.info(userBean.getCaller() + " currentMqalgo:" + currentMqalgo);

        currentMqaltcode = Utils.getAsString(row, "Mqaltcode");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqaltcode:" + currentMqaltcode);

        currentDictionary = getParamDictName();
        CSMQBean.logger.info(userBean.getCaller() + " currentDictionary:" + currentDictionary);
        
        // TEST 9-MAY
        //currentReleaseGroup = getParamReleaseGroup();
        currentReleaseGroup = Utils.getAsString(row, "Groupname");
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

        currentMqgroups = Utils.getAsString(row, "Mqgroup");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqgroups:" + currentMqgroups);

        currentMqproduct = Utils.getAsString(row, "Mqprodct");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqproduct:" + currentMqproduct);

        currentCriticalEvent = Utils.getAsString(row, "Mqcrtev");
        CSMQBean.logger.info(userBean.getCaller() + " currentCriticalEvent:" + currentCriticalEvent);
        
        currentStatus = Utils.getAsString(row, "CurPendStatus");
        CSMQBean.logger.info(userBean.getCaller() + " currentStatus:" + currentStatus);
        
        currentState = Utils.getAsString(row, "State");
        CSMQBean.logger.info(userBean.getCaller() + " currentState:" + currentState);
        
        requestedByDate = Utils.getAsDate(row, "DueDate");
        CSMQBean.logger.info(userBean.getCaller() + " requestedByDate:" + requestedByDate);
        
        currentDateRequested = Utils.getAsDate(row, "Dates");
        CSMQBean.logger.info(userBean.getCaller() + " currentDateRequested:" + currentDateRequested);
        
        currentReasonForRequest = Utils.getAsString(row, "ReasonForRequest");
        CSMQBean.logger.info(userBean.getCaller() + " currentReasonForRequest:" + currentReasonForRequest);
        
        currentReasonForApproval = Utils.getAsString(row, "ReasonForApproval");
        CSMQBean.logger.info(userBean.getCaller() + " currentReasonForApproval:" + currentReasonForApproval);
        
        isApproved = Utils.getAsBoolean(row, "ApprFlag");
        CSMQBean.logger.info(userBean.getCaller() + " isApproved:" + isApproved);
        
        currentCutOffDate = Utils.getAsString(row, "CutOffDate");
        CSMQBean.logger.info(userBean.getCaller() + " currentCutOffDate:" + currentCutOffDate);
        
        currentUntilDate = Utils.getAsString(row, "NmatUntildt");
        CSMQBean.logger.info(userBean.getCaller() + " currentUntilDate:" + currentUntilDate);

        currentCreateDate = Utils.getAsString(row, "NmatCreatedt");
        CSMQBean.logger.info(userBean.getCaller() + " currentCreateDate:" + currentCreateDate);
        
        currentCreatedBy = Utils.getAsString(row, "Createdby");
        CSMQBean.logger.info(userBean.getCaller() + " Createdby:" + currentCreatedBy);
        
        currentExtension = Utils.getAsString(row, "Extension");
        CSMQBean.logger.info(userBean.getCaller() + " Extension:" + currentExtension);
        
        String designeeStr = Utils.getAsString(row, "Designee");
        CSMQBean.logger.info(userBean.getCaller() + " designeeStr:" + designeeStr);
        List <String> designeeList = new ArrayList <String> ();
        if (null != designeeStr){
            CSMQBean.logger.info(userBean.getCaller() + " designeeStr:" + designeeStr);
            designeeStr = designeeStr.replaceAll("\\[", "").replaceAll("\\]","");
            designeeList = Arrays.asList(designeeStr.split("\\s*,\\s*"));    
        }
        nMQWizardBean.setCurrentTermName(currentTermName);
        nMQWizardBean.setCurrentFilterDictionaryShortName(this.currentDictionary);
        nMQWizardBean.getDictionaryInfo(); // GET BASE DICT INFO FROM FILTER
        
        //UPDATE THE WIZARD WITH THE RESULTS
        nMQWizardBean.setCurrentContentCode(currentMqcode);
        nMQWizardBean.setCurrentDictContentID(currentDictContentID);
        nMQWizardBean.setActiveDictionary(currentDictionary);
        nMQWizardBean.setCurrentMQALGO(currentMqalgo);
        nMQWizardBean.setCurrentMQCRTEV(currentCriticalEvent);
        nMQWizardBean.setCurrentMQGROUP(currentMqgroups);
        //nMQWizardBean.setCurrentPredictGroups(currentReleaseGroup);  //<--test
        nMQWizardBean.setCurrentReleaseGroup(currentReleaseGroup);
        nMQWizardBean.setCurrentProduct(currentMqproduct);
        nMQWizardBean.setCurrentScope(currentMqscp);
        nMQWizardBean.setCurrentMQStatus(currentMqstatus);
        nMQWizardBean.setCurrentTermLevel(currentMqlevel);
        nMQWizardBean.setCurrentStatus(currentStatus);
        nMQWizardBean.setCurrentState(currentState);
        nMQWizardBean.setCurrentDateRequested(currentDateRequested);
        nMQWizardBean.setCurrentRequestedByDate(requestedByDate);
        nMQWizardBean.setCurrentReasonForRequest(currentReasonForRequest);
        nMQWizardBean.setCurrentReasonForApproval(currentReasonForApproval);
        nMQWizardBean.setIsApproved(isApproved);
        nMQWizardBean.setCurrentCutOffDate(currentCutOffDate);
        nMQWizardBean.setCurrentCreateDate(currentCreateDate);
        nMQWizardBean.setCurrentUntilDate(currentUntilDate);
        nMQWizardBean.setCurrentVersion(currentVersion);
        nMQWizardBean.setCurrentCreatedBy(currentCreatedBy);
        nMQWizardBean.setCurrentExtension(currentExtension);
        
        /* 
         * TES 31-JUL-2014
           Added to get activation info
        */
        Hashtable <String, String> activationInfo = NMQUtils.getActivationInfo(currentDictContentID, currentDictionary);
        if (activationInfo != null) {
            nMQWizardBean.setCurrentInitialCreationDate(activationInfo.get("initialCreationDate"));
            nMQWizardBean.setCurrentInitialCreationBy(activationInfo.get("initialCreationBy"));
            nMQWizardBean.setCurrentLastActivationDate(activationInfo.get("lastActivationDate"));
            nMQWizardBean.setCurrentActivationBy(activationInfo.get("activationBy"));
        
            CSMQBean.logger.info(userBean.getCaller() + " initialCreationDate:" + activationInfo.get("initialCreationDate"));
            CSMQBean.logger.info(userBean.getCaller() + " initialCreationBy:" + activationInfo.get("initialCreationBy"));
            CSMQBean.logger.info(userBean.getCaller() + " lastActivationDate:" + activationInfo.get("lastActivationDate"));
            CSMQBean.logger.info(userBean.getCaller() + " activationBy:" + activationInfo.get("activationBy"));
        }
        else {
            CSMQBean.logger.info(userBean.getCaller() + " *** UNABLE TO GET ACTIVATION INFO *** ");
            nMQWizardBean.setCurrentInitialCreationDate("N/A");
            nMQWizardBean.setCurrentInitialCreationBy("N/A");
            nMQWizardBean.setCurrentLastActivationDate("N/A");
            nMQWizardBean.setCurrentActivationBy("N/A");
            }
            
            
            
        
        /*
         * TES 1-AUG-2014
         */
        // No need to load the designee list in case of copy existing.Default set as logged in user name already.
        if (nMQWizardBean.getMode() != cSMQBean.MODE_COPY_EXISTING){
            if (null == designeeList || designeeList.size() == 0){
                designeeList = getDesignees(currentDictContentID);
            }
            nMQWizardBean.setDesigneeList(designeeList);
            //List<String> designee = nMQWizardBean.getDesigneeList();
            CSMQBean.logger.info(userBean.getCaller() + " designee::" + designeeList);
            this.setModifiedDesigneeList(designeeList);
        }
              
        // FIX FOR REGEX CRAZINESS
        if (currentMqproduct != null) {
            currentMqproduct = currentMqproduct.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
            Collections.addAll(nMQWizardBean.getProductList(), currentMqproduct.split("%"));
            }

        if (currentMqgroups != null) {
            currentMqgroups = currentMqgroups.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
            Collections.addAll(nMQWizardBean.getMQGroupList(), currentMqgroups.split("%"));
            }
        
        // set the query type 
        /*
         * TES CHANGED 06-AUG-2014
         */
        
        //nMQWizardBean.setIsNMQ(currentMqlevel.indexOf("N") > -1);
        nMQWizardBean.setIsNMQ(this.currentExtension.equalsIgnoreCase("NMQ"));
        nMQWizardBean.setIsSMQ(this.currentExtension.equalsIgnoreCase("SMQ"));
        
        //update the history
        // todo: add later
        //userBean.addHistory(currentTermName, currentMqcode);
        
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
            this.currentDictionary = valueChangeEvent.getNewValue().toString(); 
            
            if (this.currentDictionary.equals(cSMQBean.getProperty("DEFAULT_BASE_DICTIONARY_SHORT_NAME"))) { // process for MedDRA query
                
                nMQWizardBean.setIsMedDRA(true);  // used for rendering the correct controls
                this.paramQueryType = CSMQBean.WILDCARD;
                
                paramLevel = CSMQBean.BASE_LEVEL_ONE;
                this.MEDDraSearch = true;
                
                ctrlNMQStatus.setRendered(false);
                ctrlState.setRendered(false);
                ctrlMQScope.setRendered(false);
                ctrlCriticalEvent.setRendered(false);
                ctrlMQGroups.setRendered(false);
                
                if (ctrlStartDate != null)
                    ctrlStartDate.setRendered(false);
                
                if (ctrlEndDate != null)
                    ctrlEndDate.setRendered(false);
                
                if (cntrlDictionaryVersion != null)
                    cntrlDictionaryVersion.setRendered(true);
                
                if (ctrlLevelList != null)
                    ctrlLevelList.setRendered(false);
                    
                if (this.ctrlProducts != null)
                    this.ctrlProducts.setRendered(false);
                
                controlMQLevel.setDisabled(false);

                }
            else {
                nMQWizardBean.setIsMedDRA(false);  // used for rendering the correct controls
                this.paramQueryType = CSMQBean.NMQ_SMQ_SEARCH;
                paramLevel = CSMQBean.FILTER_LEVEL_ONE;
                this.MEDDraSearch = false;
                
                renderingRulesBean = (RenderingRulesBean)ADFContext.getCurrent().getRequestScope().get("RenderingRulesBean");
                ctrlNMQStatus.setRendered(true && renderingRulesBean.isWizardSearchRenderSMQStatus());
                ctrlState.setRendered(true);
                ctrlMQScope.setRendered(true && renderingRulesBean.isWizardSearchRenderScope());
                ctrlCriticalEvent.setRendered(true && renderingRulesBean.isWizardSearchRenderCriticalEvent());
                ctrlMQGroups.setRendered(true && renderingRulesBean.isWizardSearchRenderGroup());
     
                if (cntrlDictionaryVersion != null)
                    cntrlDictionaryVersion.setRendered(false);
                
                if (ctrlLevelList != null)
                    ctrlLevelList.setRendered(true);
                
                if (this.ctrlProducts != null)
                    this.ctrlProducts.setRendered(true);
                
                if (nMQWizardBean.isIsNMQ() || nMQWizardBean.isIsSMQ() )
                    controlMQLevel.setDisabled(false);
                else
                    controlMQLevel.setDisabled(true);
                
                }
            
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlSearchPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlSearchPanel);
            
            clearSearchResults();
            AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
            //AdfFacesContext.getCurrentInstance().addPartialTarget(controlMQLevel);
            //AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlMQLevel);
                
            
            } 
        catch (Exception e) {
            e.printStackTrace();
            }
        }
    
    private void clearSearchResults(){
        nMQWizardBean.clearDetails();
        
        // FIX FOR WHEN A USER CANCELS AND COMES BACK IN
        if (searchIterator.length() == 0) setUIDefaults ();
        
        
        nMQWizardBean.getProductList().clear();
        nMQWizardBean.getMQGroupList().clear();
        if (termHierarchyBean != null)
        termHierarchyBean.showStatus(CSMQBean.MQ_INIT);
        nMQWizardBean.setTreeAccessed(false); //reset the tree
        //nMQWizardBean.clearDetails();  UI MOVE?
        
        String activationGroup = getParamReleaseGroup();
        String queryLevel = getParamLevel();
        if (IASearch){
            CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING IA SEARCH **");
            activationGroup = CSMQBean.WILDCARD;
        } else {
            CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING SEARCH **");
        }
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get(searchIterator);
        ViewObject vo = dciterb.getViewObject();
        vo.executeEmptyRowSet();
        if (ctrlSearchResults != null) {  // if we are calling this from IA, we won't need this
            CSMQBean.logger.info(userBean.getCaller() + " ctrlSearchResults is not null :: ");
            ctrlSearchResults.setEmptyText("No data to display.");
            AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
            //clear the selected row
            RowKeySet rks= ctrlSearchResults.getSelectedRowKeys();
            rks.clear();
        
            //CLEAR OLD TRESS
            NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
            nMQSourceTermSearchBean.clearTree();
            nMQWizardBean.clearRelations();
        }
    }

    public void releaseGroupChange(ValueChangeEvent valueChangeEvent) {
        //this.currentReleaseGroup = ctrlReleaseGroupSearch.getValue().toString();
    }


    public void setCtrlDictionary(RichSelectOneChoice controlDictionary) {
        this.ctrlDictionary = controlDictionary;
    }

    public RichSelectOneChoice getCtrlDictionary() {
        return ctrlDictionary;
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


    public void setParamMQCode(String paramMQCode) {
        this.paramMQCode = paramMQCode;
    }

    public String getParamMQCode() {
        if (IASearch) return paramMQCode; // the MQCode is passed in as a param when called from IA
        
        paramMQCode = cSMQBean.WILDCARD;
        if (ctrlMQCode != null && ctrlMQCode.getValue() != null && !ctrlMQCode.getValue().toString().equalsIgnoreCase("null") && ctrlMQCode.getValue().toString().length() > 0)
            paramMQCode = ctrlMQCode.getValue().toString();
        
        return paramMQCode;
    }

    public void setParamMQCriticalEvent(String paramMQCriticalEvent) {
        this.paramMQCriticalEvent = paramMQCriticalEvent;
    }

    public String getParamMQCriticalEvent() {
        if (IASearch) return CSMQBean.WILDCARD;
        
        if (ctrlCriticalEvent == null) return null;
        String temp = String.valueOf(ctrlCriticalEvent.getValue());
        if (temp != null)
            paramMQCriticalEvent = temp.trim();
        return paramMQCriticalEvent;
    }


    public void setParamQueryType(String paramQueryType) {
        this.paramQueryType = paramQueryType;
    }   

    //AMC 7/30/14 using levels instead of paramquertype for SQL query
    public String getParamQueryType() {
        return paramQueryType;
        //nMQWizardBean.getCurrentTermLevel()
       }

    public String getExtensionType(){
        return paramQueryType;
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

    public void setCtrlQuery(RichSelectOneChoice ctrlQuery) {
        this.ctrlQuery = ctrlQuery;
    }

    public RichSelectOneChoice getCtrlQuery() {
        return ctrlQuery;
    }

    public void setReleaseGroupSelectItems(ArrayList<SelectItem> releaseGroupSelectItems) {
        this.releaseGroupSelectItems = releaseGroupSelectItems;
    }

    public ArrayList<SelectItem> getReleaseGroupSelectItems() {
        return releaseGroupSelectItems;
    }

    public void setCntrlSearchPanel(RichPanelBox cntrlSearchPanel) {
        this.cntrlSearchPanel = cntrlSearchPanel;
    }

    public RichPanelBox getCntrlSearchPanel() {
        return cntrlSearchPanel;
    }

    public void releaseStatusChanged(ValueChangeEvent valueChangeEvent) {
        if (ctrlReleaseStatus != null)
            currentMqstatus = ctrlReleaseStatus.getValue().toString();
        
        if (currentMqstatus.equals(CSMQBean.CURRENT_RELEASE_STATUS)) {
            ctrlReleaseGroupSearch.setRendered(false);
            cntrlApprovedColumn.setRendered(true);
            cntrlApproved.setDisabled(false);
            ctrlState.setRendered(false);
            }
        else if (currentMqstatus.equals(CSMQBean.PENDING_RELEASE_STATUS)) {
            ctrlReleaseGroupSearch.setRendered(false);
            cntrlApprovedColumn.setRendered(true);
            ctrlNMQStatus.setValue(CSMQBean.BOTH_RELEASE_STATUSES);
            cntrlApproved.setDisabled(true);
            ctrlState.setRendered(true);
            }
        else {
            ctrlReleaseGroupSearch.setRendered(true);
            cntrlApprovedColumn.setRendered(false);
            ctrlNMQStatus.setValue(CSMQBean.BOTH_RELEASE_STATUSES);
            cntrlApproved.setDisabled(true);
            ctrlState.setRendered(false);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlParamPanel);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlParamPanel);
        AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
        AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlNMQStatus);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlNMQStatus);
        AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlState);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlState);
    }

    public void setSearchLabelPrefix(String searchLabelPrefix) {
        this.searchLabelPrefix = searchLabelPrefix;
    }

    public String getSearchLabelPrefix() {
        if (this.MEDDraSearch) {
            searchLabelPrefix = "";
            }
        else {
             if (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_EXISTING) 
                 this.searchLabelPrefix = NMQ_LABEL;
            else if (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_SMQ) 
                this.searchLabelPrefix = SMQ_LABEL;
            else if (nMQWizardBean.getMode() == CSMQBean.MODE_COPY_EXISTING) 
                this.searchLabelPrefix = NMQ_SMQ_LABEL;
            else if (nMQWizardBean.getMode() == CSMQBean.MODE_INSERT_NEW) 
                this.searchLabelPrefix = NMQ_LABEL;
            else if (nMQWizardBean.getMode() == CSMQBean.MODE_HISTORIC) 
                this.searchLabelPrefix = NMQ_SMQ_LABEL;
            else if (nMQWizardBean.getMode() == CSMQBean.MODE_BROWSE_SEARCH) 
                this.searchLabelPrefix = NMQ_SMQ_LABEL;                
            else if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT || nMQWizardBean.getMode() == CSMQBean.MODE_VIEW_VERSION_IMPACT) 
                this.searchLabelPrefix = NMQ_SMQ_LABEL;
            }
        return searchLabelPrefix;
    }

    public void setDetailsLabelPrefix(String detailsLabelPrefix) {
        this.detailsLabelPrefix = detailsLabelPrefix;
    }

    public String getDetailsLabelPrefix() {
        return detailsLabelPrefix;
    }

    public void setParamKillSwitch(Integer paramKillSwitch) {
        this.paramKillSwitch = paramKillSwitch;
    }

    public Integer getParamKillSwitch() {
        return paramKillSwitch;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setParamNarrowScopeOnly(String paramNarrowScopeOnly) {
        this.paramNarrowScopeOnly = paramNarrowScopeOnly;
    }

    public String getParamNarrowScopeOnly() {
        return paramNarrowScopeOnly;
    }

    public void setGetLevelsForQueryType(ArrayList<SelectItem> getLevelsForQueryType) {
        this.getLevelsForQueryType = getLevelsForQueryType;
    }

    public ArrayList<SelectItem> getGetLevelsForQueryType() {
        return getLevelsForQueryType;
    }

    public void setParamMQScope(String paramMQScope) {
        this.paramMQScope = paramMQScope;
    }

    public String getParamMQScope() {
        if (ctrlMQScope != null && ctrlMQScope.getValue() != null)
            paramMQScope = ctrlMQScope.getValue().toString();
        return paramMQScope;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setRequestedByDate(oracle.jbo.domain.Date requestedByDate) {
        this.requestedByDate = requestedByDate;
    }

    public oracle.jbo.domain.Date getRequestedByDate() {
        return requestedByDate;
    }

    public void setCurrentReasonForRequest(String reasonForRequest) {
        this.currentReasonForRequest = reasonForRequest;
    }

    public String getCurrentReasonForRequest() {
        return currentReasonForRequest;
    }

    public void setParamUserRole(String paramUserRole) {
        this.paramUserRole = paramUserRole;
    }

    public String getParamUserRole() {
        if (userBean.isUserInRole(CSMQBean.ROLE_REQUESTOR)) paramUserRole = CSMQBean.ROLE_REQUESTOR;
        if (userBean.isUserInRole(CSMQBean.ROLE_MQM)) paramUserRole = CSMQBean.ROLE_MQM;
        if (userBean.isUserInRole(CSMQBean.ROLE_ADMIN)) paramUserRole = CSMQBean.ROLE_ADMIN;
        return paramUserRole;
    }

    public void setCurrentReasonForApproval(String currentReasonForApproval) {
        this.currentReasonForApproval = currentReasonForApproval;
    }

    public String getCurrentReasonForApproval() {
        return currentReasonForApproval;
    }

    public void setCntrlTrain(RichTrain cntrlTrain) {
        this.cntrlTrain = cntrlTrain;
    }

    public RichTrain getCntrlTrain() {
        return cntrlTrain;
    }

    public void setParamMode(int paramMode) {
        this.paramMode = paramMode;
    }

    public int getParamMode() {
        this.paramMode = nMQWizardBean.getMode();
        return paramMode;
    }

    public void setParamApproved(String paramApproved) {
        this.paramApproved = paramApproved;
    }

    public String getParamApproved() {
        return paramApproved;
    }

    public void setCntrlApproved(RichSelectOneChoice cntrlApproved) {
        this.cntrlApproved = cntrlApproved;
    }

    public RichSelectOneChoice getCntrlApproved() {
        return cntrlApproved;
    }

    public void setCntrlApprovedColumn(RichColumn cntrlApprovedColumn) {
        this.cntrlApprovedColumn = cntrlApprovedColumn;
    }

    public RichColumn getCntrlApprovedColumn() {
        return cntrlApprovedColumn;
    }

    public void setNMQWizardBean(NMQWizardBean nMQWizardBean) {
        this.nMQWizardBean = nMQWizardBean;
    }

    public NMQWizardBean getNMQWizardBean() {
        return nMQWizardBean;
    }

    public void setCtrlHistoricalResults(RichTable ctrlHistoricalResults) {
        this.ctrlHistoricalResults = ctrlHistoricalResults;
    }

    public RichTable getCtrlHistoricalResults() {
        return ctrlHistoricalResults;
    }

    public void setCtrlHistoricalDateListResults(RichTable ctrlHistoricalDateListResults) {
        this.ctrlHistoricalDateListResults = ctrlHistoricalDateListResults;
    }

    public RichTable getCtrlHistoricalDateListResults() {
        return ctrlHistoricalDateListResults;
    }

    public void setCntrlResultsPanel(RichPanelGroupLayout cntrlResultsPanel) {
        this.cntrlResultsPanel = cntrlResultsPanel;
    }

    public RichPanelGroupLayout getCntrlResultsPanel() {
        return cntrlResultsPanel;
    }

    public void setCntrlDictionaryVersion(RichSelectOneChoice cntrlDictionaryVersion) {
        this.cntrlDictionaryVersion = cntrlDictionaryVersion;
    }

    public RichSelectOneChoice getCntrlDictionaryVersion() {
        return cntrlDictionaryVersion;
    }

    public void setDictionaryVersion(String dictionaryVersion) {
        this.dictionaryVersion = dictionaryVersion;
    }

    public String getDictionaryVersion() {
        if (cntrlDictionaryVersion != null && cntrlDictionaryVersion.getValue() != null)
            this.dictionaryVersion = cntrlDictionaryVersion.getValue().toString();
        return dictionaryVersion;
    }

    public void setCurrentCutOffDate(String currentCutOffDate) {
        this.currentCutOffDate = currentCutOffDate;
    }

    public String getCurrentCutOffDate() {
        return currentCutOffDate;
    }

    public void setCurrentUntilDate(String currentUntilDate) {
        this.currentUntilDate = currentUntilDate;
    }

    public String getCurrentUntilDate() {
        return currentUntilDate;
    }

    public void setCurrentCreateDate(String currentCreateDate) {
        this.currentCreateDate = currentCreateDate;
    }

    public String getCurrentCreateDate() {
        return currentCreateDate;
    }

    public void setCurrentCreatedBy(String currentCreateBy) {
        this.currentCreatedBy = currentCreateBy;
    }

    public String getCurrentCreatedBy() {
        return currentCreatedBy;
    }

    public void setCntrlParamPanel(RichPanelGroupLayout cntrlParamPanel) {
        this.cntrlParamPanel = cntrlParamPanel;
    }

    public RichPanelGroupLayout getCntrlParamPanel() {
        return cntrlParamPanel;
    }

    public void setCntrlReleaseGroupSpacer(RichSpacer cntrlReleaseGroupSpacer) {
        this.cntrlReleaseGroupSpacer = cntrlReleaseGroupSpacer;
    }

    public RichSpacer getCntrlReleaseGroupSpacer() {
        return cntrlReleaseGroupSpacer;
    }


    public void setCurrentExtension(String currentExtension) {
        this.currentExtension = currentExtension;
    }

    public String getCurrentExtension() {
        return currentExtension;
    }

    public void setParamExtension(String paramExtension) {
        this.paramExtension = paramExtension;
    }

    public String getParamExtension() {
        return paramExtension;
    }

    public void setParamLevel(String paramLevel) {
        this.paramLevel = paramLevel;
    }

    public String getParamLevel() {
        return paramLevel;
    }

    public void extensionChanged(ValueChangeEvent valueChangeEvent) {
        controlMQLevel.setDisabled(true);
        
        String newExt = valueChangeEvent.getNewValue().toString();
        boolean ALL = newExt.equals("%");
        nMQWizardBean.setIsNMQ(newExt.equals("NMQ"));
        nMQWizardBean.setIsSMQ(newExt.equals("SMQ"));

        if (MEDDraSearch) {
            paramLevel = CSMQBean.BASE_LEVEL_ONE;
            controlMQLevel.setDisabled(false);
            }
        else if (ALL || nMQWizardBean.isIsNMQ() || nMQWizardBean.isIsSMQ()) {
            paramLevel = CSMQBean.FILTER_LEVEL_ONE;
            controlMQLevel.setDisabled(false);
            }

        AdfFacesContext.getCurrentInstance().addPartialTarget(controlMQLevel);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlMQLevel);
    }

    public void setControlMQLevel(RichSelectOneChoice controlMQLevel) {
        this.controlMQLevel = controlMQLevel;
    }

    public RichSelectOneChoice getControlMQLevel() {
        return controlMQLevel;
    }

    public void setMEDDraSearch(boolean MEDDraSearch) {
        this.MEDDraSearch = MEDDraSearch;
    }

    public boolean isMEDDraSearch() {
        return MEDDraSearch;
    }

    public void setCtrlProducts(RichSelectManyChoice ctrlProducts) {
        this.ctrlProducts = ctrlProducts;
    }

    public RichSelectManyChoice getCtrlProducts() {
        return ctrlProducts;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public List<String> getProductList() {
        return productList;
    }
    public void setModifiedDesigneeList(List<String> modifiedDesigneeList) {
        this.modifiedDesigneeList = modifiedDesigneeList;
    }

    public List<String> getModifiedDesigneeList() {
        return modifiedDesigneeList;
    }
       
    public void modifiedDesigneeValueChange(ValueChangeEvent valueChangeEvent) {
        CSMQBean.logger.info("modifiedDesigneeValue :" + valueChangeEvent.getNewValue());
        setModifiedDesigneeList((List<String>)valueChangeEvent.getNewValue());
    }
    
    public void updateModifiedDesignees(DialogEvent dialogEvent) {
        // Add event code here...
        String designeeListString = "";
        boolean isUpated = false;
        oracle.adf.view.rich.event.DialogEvent.Outcome outcome = dialogEvent.getOutcome();
        if (outcome.ok == oracle.adf.view.rich.event.DialogEvent.Outcome.ok){
            if (this.getModifiedDesigneeList() != null && this.getModifiedDesigneeList().size() > 0) {
               designeeListString = this.getModifiedDesigneeListAsString();
                CSMQBean.logger.info(userBean.getCaller() + "designee updated :" + designeeListString);
                CSMQBean.logger.info(userBean.getCaller() + "CurrentDictContentID :" + getCurrentDictContentID());
               isUpated = NMQUtils.updateMQDesignee(this.getCurrentDictContentID(), designeeListString);
               if (isUpated){
                   // close the pop up dialog
                   nMQWizardBean.setDesigneeList(this.getModifiedDesigneeList());
                   CSMQBean.logger.info(userBean.getCaller() + "designee list updated successfully");
               } else {
                   CSMQBean.logger.info(userBean.getCaller() + "error while udpating designee list ");
               }
            }
        } else {
            CSMQBean.logger.info(userBean.getCaller() + "No designee selected");// keep the previous one as it is.
        }
    }
    
    public java.util.Date convertDomainDateToUtilDate(oracle.jbo.domain.Date domainDate) {
    java.util.Date date = null;
    if (domainDate != null) {
    java.sql.Date sqldate = domainDate.dateValue();
    date = new Date(sqldate.getTime());
    }
    return date;
    }
   
    public String getModifiedDesigneeListAsString() {
        String modifiedDesigneeListAsString = "";
       if (modifiedDesigneeList == null || modifiedDesigneeList.isEmpty()) return "";
        StringBuilder out = new StringBuilder();
        for (Object o : modifiedDesigneeList) {
          out.append(o.toString());
          out.append(",");
            }
        
        modifiedDesigneeListAsString = out.toString();
        int len = modifiedDesigneeListAsString.length();
        //if (len > 0) designeeListAsString = designeeListAsString.substring(len , len-1);
        
        return modifiedDesigneeListAsString;
    }

    public void downloadSearchReport(FacesContext facesContext, OutputStream outputStream) {
            try {
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet worksheet = workbook.createSheet("Search Report");
                HSSFRow excelrow = null;

                int i = 0;
                int colCount = 0;
                excelrow = (HSSFRow) worksheet.createRow((short) i);
                HSSFCell cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Search Criteria");

                i++;
                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Dictionary");
                HSSFCell cellA2 = excelrow.createCell((short) 1);
                cellA2.setCellValue(getParamDictName());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Extenstion");
                cellA2 = excelrow.createCell((short) 1);
                if("%".equals(getParamExtension()))
                    cellA2.setCellValue("All");
                else
                    cellA2.setCellValue(getParamExtension());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Release Status");
                cellA2 = excelrow.createCell((short) 1);
                cellA2.setCellValue(getParamReleaseStatus());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Approved");
                cellA2 = excelrow.createCell((short) 1);
                if("%".equals(getParamApproved()))
                    cellA2.setCellValue("All");
                else
                    cellA2.setCellValue(getParamApproved());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Level");
                cellA2 = excelrow.createCell((short) 1);
                if("%".equals(getParamLevel()))
                    cellA2.setCellValue("All");
                else
                    cellA2.setCellValue(getParamLevel());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Critical Event");
                cellA2 = excelrow.createCell((short) 1);
                if("%".equals(getParamMQCriticalEvent()))
                    cellA2.setCellValue("All");
                else
                    cellA2.setCellValue(getParamMQCriticalEvent());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Product");
                cellA2 = excelrow.createCell((short) 1);
                if("%".equals(getParamProductList())){
                    String clause = (String)ADFUtils.evaluateEL("#{pageFlowScope.productClause}");
                    if(clause != null){
                        cellA2.setCellValue(clause);
                    }
                    else{
                        cellA2.setCellValue("All");
                    }
                }
                else{
                    cellA2.setCellValue(getParamProductList());
                }

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Status");
                cellA2 = excelrow.createCell((short) 1);
                cellA2.setCellValue(getParamActivityStatus());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Group");
                cellA2 = excelrow.createCell((short) 1);
                if("%".equals(getParamMQGroupList())){
                    String clause = (String)ADFUtils.evaluateEL("#{pageFlowScope.groupClause}");
                    if(clause != null){
                        cellA2.setCellValue(clause);
                    }
                    else{
                        cellA2.setCellValue("All");
                    }
                }
                else
                    cellA2.setCellValue(getParamMQGroupList());

                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Scope");
                cellA2 = excelrow.createCell((short) 1);
                if("%".equals(getParamMQScope()))
                    cellA2.setCellValue("All");
                else
                    cellA2.setCellValue(getParamMQScope());

                i++;
                i++;

                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Search Results Table");

                i++;
                i++;

                int k = i;

                BindingContext bc = BindingContext.getCurrent();
                DCBindingContainer binding = (DCBindingContainer) bc.getCurrentBindingsEntry();
                DCIteratorBinding dcIter = (DCIteratorBinding) binding.get("SimpleSearch1Iterator");


                RowSetIterator rs = dcIter.getViewObject().createRowSetIterator(null);

                while (rs.hasNext()) {
                    Row row = rs.next();
                    //print header on first row in excel
                    if (i == k) {
                        excelrow = (HSSFRow) worksheet.createRow((short) i);
//                        short j = 0;
//                        for (String colName : row.getAttributeNames()) {
//                            cellA1 = excelrow.createCell((short) j);
//                            cellA1.setCellValue(colName);
//                            j++;
//
//                        }
                        
                        cellA1 = excelrow.createCell((short) 0);
                        cellA1.setCellValue("NAME");
                        
                        cellA1 = excelrow.createCell((short) 1);
                        cellA1.setCellValue("CODE");
                        
                        cellA1 = excelrow.createCell((short) 2);
                        cellA1.setCellValue("EXTENSION");

                        cellA1 = excelrow.createCell((short) 3);
                        cellA1.setCellValue("LEVEL");

                        cellA1 = excelrow.createCell((short) 4);
                        cellA1.setCellValue("VERSION");

                        cellA1 = excelrow.createCell((short) 5);
                        cellA1.setCellValue("STATUS");

                        cellA1 = excelrow.createCell((short) 6);
                        cellA1.setCellValue("SCOPE");

                        cellA1 = excelrow.createCell((short) 7);
                        cellA1.setCellValue("ALGORITHM");

                        cellA1 = excelrow.createCell((short) 8);
                        cellA1.setCellValue("GROUP");

                        cellA1 = excelrow.createCell((short) 9);
                        cellA1.setCellValue("PRODUCT");

                        cellA1 = excelrow.createCell((short) 10);
                        cellA1.setCellValue("CRITICAL EVENT");

                        cellA1 = excelrow.createCell((short) 11);
                        cellA1.setCellValue("CUR/PEND");

                        cellA1 = excelrow.createCell((short) 12);
                        cellA1.setCellValue("CREATE DATE");

                        cellA1 = excelrow.createCell((short) 13);
                        cellA1.setCellValue("CREATED BY");

                        cellA1 = excelrow.createCell((short) 14);
                        cellA1.setCellValue("STATE");

                        cellA1 = excelrow.createCell((short) 15);
                        cellA1.setCellValue("DESIGNEE");
                        
                        
                    }

                    //print data from second row in excel
                    ++i;
//                    short j = 0;
                    excelrow = worksheet.createRow((short) i);
                    
                    HSSFCell cell = excelrow.createCell(0);
                    cell.setCellValue(row.getAttribute("Mqterm") + "");
                    
                    cell = excelrow.createCell(1);
                    cell.setCellValue(row.getAttribute("Mqcode")+ "");
                    
                    cell = excelrow.createCell(2);
                    cell.setCellValue(row.getAttribute("Extension")+ "");
                    
                    cell = excelrow.createCell(3);
                    cell.setCellValue(row.getAttribute("LevelNm")+ "");
                    
                    cell = excelrow.createCell(4);
                    cell.setCellValue(row.getAttribute("Version")+ "");
                    
                    cell = excelrow.createCell(5);
                    cell.setCellValue(row.getAttribute("Mqstatus")+ "");
                    
                    cell = excelrow.createCell(6);
                    cell.setCellValue(row.getAttribute("Mqscp")+ "");
                    
                    cell = excelrow.createCell(7);
                    cell.setCellValue(row.getAttribute("Mqalgo") + "");
                    
                    cell = excelrow.createCell(8);
                    cell.setCellValue((row.getAttribute("Mqgroup") == null ? "" : row.getAttribute("Mqgroup"))+ "");
                    
                    cell = excelrow.createCell(9);
                    cell.setCellValue((row.getAttribute("Mqprodct") == null ? "" : row.getAttribute("Mqprodct")) + "");
                    
                    cell = excelrow.createCell(10);
                    cell.setCellValue(row.getAttribute("Mqcrtev")+ "");
                    
                    cell = excelrow.createCell(11);
                    cell.setCellValue(row.getAttribute("CurPendStatus")+ "");
                    
                    oracle.jbo.domain.Date date = (oracle.jbo.domain.Date)row.getAttribute("Dates");
                    
                    String dateFormatted = "";
                    if(date != null){
                        java.util.Date utilDate = convertDomainDateToUtilDate(date);
                        dateFormatted = formatter.format(utilDate);
                    }
                    
                    cell = excelrow.createCell(12);
                    cell.setCellValue(dateFormatted);
                    
                    cell = excelrow.createCell(13);
                    cell.setCellValue(row.getAttribute("Createdby")+ "");
                    
                    cell = excelrow.createCell(14);
                    cell.setCellValue((row.getAttribute("State") == null ? "" : row.getAttribute("State"))+ "");
                    
                    cell = excelrow.createCell(15);
                    cell.setCellValue((row.getAttribute("Designee") == null ? "" : row.getAttribute("Designee"))+ "");
                    
                    
//                    for (String colName : row.getAttributeNames()) {
//                        HSSFCell cell = excelrow.createCell(j);
//                        if (row.getAttribute(colName) != null)
//                            cell.setCellValue(row.getAttribute(colName).toString());
//                        j++;
//                    }
//                    colCount = j;
                }
                
                i++;
                i++;
                excelrow = (HSSFRow) worksheet.createRow((short) i);
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Row Count");
                cellA2 = excelrow.createCell((short) 1);
                cellA2.setCellValue(dcIter.getEstimatedRowCount());
                
                worksheet.createFreezePane(0, 1, 0, 1);

                for (int x = 0; x < colCount; x++) {
                    worksheet.autoSizeColumn(x);
                }
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    public void setHistoryDate(Date historyDate) {
        this.historyDate = historyDate;
    }

    public Date getHistoryDate() {
        return historyDate;
    }

    public void onTermHistoryDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        oracle.adf.view.rich.event.DialogEvent.Outcome outcome = dialogEvent.getOutcome();
        if (outcome.ok == oracle.adf.view.rich.event.DialogEvent.Outcome.ok) {
            historyDate = historyInputDate;
            if (getHistoryDate() == null) {
                FacesMessage msg =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "History As of Date", "History As of Date is required ");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                historyFlow = false;
            } else {                
                loadTermDetailsBasedonHistoryDate();
                AdfFacesContext.getCurrentInstance().addPartialTarget(ctrlSearchResults);
                AdfFacesContext.getCurrentInstance().partialUpdateNotify(ctrlSearchResults);
            }
        } else {
            historyFlow = false;
            CSMQBean.logger.info(userBean.getCaller() + "No designee selected"); // keep the previous one as it is.
        }
        historyInputDate = null;
    }

    private void loadTermDetailsBasedonHistoryDate() {
        nMQWizardBean.setTreeAccessed(false);  //reset this to recreate the tree when the page loads
        nMQWizardBean.clearDetails(); // hopefully this works
        System.out.println("Start Exec loadTermDetailsBasedonHistoryDate() ");
        execTermDetailsBasedonHistoryDate(new BigDecimal(getCurrentDictContentID()), getDateStr(getHistoryDate()));
        populateHistoryTermDtls();
        System.out.println("End of Exec loadTermDetailsBasedonHistoryDate() ");
    }

    private void populateHistoryTermDtls() {
        System.out.println("Start Exec populateHistoryTermDtls() ");
        BindingContext bc1 = BindingContext.getCurrent();
        DCBindingContainer binding1 = (DCBindingContainer)bc1.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding1.get("TermHistoricInfoVO1Iterator");
        Row row = dciterb.getCurrentRow();
        
        if(row == null){
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_INFO, "History As of Date", "Term History Data is not available for the selected Date.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        
        historyFlow = true;
        CSMQBean.logger.info(userBean.getCaller() + " ***** processSearchResults  Mode ==" + nMQWizardBean.getMode());
        currentTermName = Utils.getAsString(row, "Term");
        CSMQBean.logger.info(userBean.getCaller() + " currentTermName:" + currentTermName);

        currentMqlevel = Utils.getAsString(row, "LevelName");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqlevel:" + currentMqlevel);

        currentMqcode = Utils.getAsString(row, "DictContentCode");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqcode:" + currentMqcode);

        currentMqalgo = Utils.getAsString(row, "Value1");

        if (currentMqalgo == null || currentMqalgo.length() < 1)
            currentMqalgo = CSMQBean.DEFAULT_ALGORITHM;

        CSMQBean.logger.info(userBean.getCaller() + " currentMqalgo:" + currentMqalgo);

        currentMqaltcode = Utils.getAsString(row, "DictContentAltCode");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqaltcode:" + currentMqaltcode);

        currentDictionary = Utils.getAsString(row, "DictionaryName");
        CSMQBean.logger.info(userBean.getCaller() + " currentDictionary:" + currentDictionary);

        currentReleaseGroup = Utils.getAsString(row, "Value2"); //TODO
        CSMQBean.logger.info(userBean.getCaller() + " currentReleaseGroup:" + currentReleaseGroup);

        currentMqstatus = Utils.getAsString(row, "Status");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqstatus:" + currentMqstatus);

        currentDictContentID = Utils.getAsString(row, "DictContentId");
        CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID:" + currentDictContentID);

        currentApprovalFlag = Utils.getAsString(row, "ApprovedFlag");
        CSMQBean.logger.info(userBean.getCaller() + " currentApprovalFlag:" + currentApprovalFlag);

        currentVersion = Utils.getAsString(row, "DictVersionAsOfDate");
        CSMQBean.logger.info(userBean.getCaller() + " currentVersion:" + currentVersion);

        currentSubType = Utils.getAsString(row, "TermSubtype");
        CSMQBean.logger.info(userBean.getCaller() + " currentSubType:" + currentSubType);

        currentMqscp = Utils.getAsString(row, "Category");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqscp:" + currentMqscp);

        currentMqgroups = Utils.getAsString(row, "MqgroupExpanded");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqgroups:" + currentMqgroups);

        currentMqproduct = Utils.getAsString(row, "Value3");
        CSMQBean.logger.info(userBean.getCaller() + " currentMqproduct:" + currentMqproduct);

        currentCriticalEvent = Utils.getAsString(row, "Value4");
        CSMQBean.logger.info(userBean.getCaller() + " currentCriticalEvent:" + currentCriticalEvent);

        //currentStatus = Utils.getAsString(row, "CurPendStatus");
        CSMQBean.logger.info(userBean.getCaller() + " currentStatus:" + currentStatus);

        currentState = "N/A";
        CSMQBean.logger.info(userBean.getCaller() + " currentState:" + currentState);

        requestedByDate = Utils.getAsDate(row, "FirstTmsEntryTs");
        CSMQBean.logger.info(userBean.getCaller() + " requestedByDate:" + requestedByDate);

        currentDateRequested = Utils.getAsDate(row, "FirstTmsEntryTs");
        CSMQBean.logger.info(userBean.getCaller() + " currentDateRequested:" + currentDateRequested);

        //currentReasonForRequest = Utils.getAsString(row, "ReasonForRequest");
        CSMQBean.logger.info(userBean.getCaller() + " currentReasonForRequest:" + currentReasonForRequest);

        //currentReasonForApproval = Utils.getAsString(row, "ReasonForApproval");
        CSMQBean.logger.info(userBean.getCaller() + " currentReasonForApproval:" + currentReasonForApproval);

        isApproved = Utils.getAsBoolean(row, "ApprovedFlag");
        CSMQBean.logger.info(userBean.getCaller() + " isApproved:" + isApproved);

        currentCutOffDate = Utils.getAsString(row, "NmatEndTs");
        CSMQBean.logger.info(userBean.getCaller() + " currentCutOffDate:" + currentCutOffDate);

        currentUntilDate = Utils.getAsString(row, "NmatEndTs");
        CSMQBean.logger.info(userBean.getCaller() + " currentUntilDate:" + currentUntilDate);

        currentCreateDate = Utils.getAsString(row, "FirstTmsEntryTs");
        CSMQBean.logger.info(userBean.getCaller() + " currentCreateDate:" + currentCreateDate);

        currentCreatedBy = Utils.getAsString(row, "CreatedBy");
        CSMQBean.logger.info(userBean.getCaller() + " Createdby:" + currentCreatedBy);

        currentExtension = Utils.getAsString(row, "LevelExtension");
        CSMQBean.logger.info(userBean.getCaller() + " Extension:" + currentExtension);
        
        /*
        String designeeStr = Utils.getAsString(row, "Value3");
        CSMQBean.logger.info(userBean.getCaller() + " designeeStr:" + designeeStr);
        List<String> designeeList = new ArrayList<String>();
        if (null != designeeStr) {
            CSMQBean.logger.info(userBean.getCaller() + " designeeStr:" + designeeStr);
            designeeStr = designeeStr.replaceAll("\\[", "").replaceAll("\\]", "");
            designeeList = Arrays.asList(designeeStr.split("\\s*,\\s*"));
        }*/
        
        nMQWizardBean.setCurrentTermName(currentTermName);
        nMQWizardBean.setCurrentFilterDictionaryShortName(this.currentDictionary);

        //UPDATE THE WIZARD WITH THE RESULTS
        nMQWizardBean.setCurrentContentCode(currentMqcode);
        nMQWizardBean.setCurrentDictContentID(currentDictContentID);
        nMQWizardBean.setActiveDictionary(currentDictionary);        
        nMQWizardBean.setActiveDictionaryVersion(Utils.getAsString(row, "DictVersionAsOfDate"));
        nMQWizardBean.setCurrentMQALGO(currentMqalgo);
        nMQWizardBean.setCurrentMQCRTEV(currentCriticalEvent);
        nMQWizardBean.setCurrentMQGROUP(currentMqgroups);
        //nMQWizardBean.setCurrentPredictGroups(currentReleaseGroup);  //<--test
        nMQWizardBean.setCurrentProduct(currentMqproduct);
        nMQWizardBean.setCurrentScope(currentMqscp);
        nMQWizardBean.setCurrentMQStatus(currentMqstatus);
        nMQWizardBean.setCurrentTermLevel(currentMqlevel);
        nMQWizardBean.setCurrentStatus(currentStatus);
        nMQWizardBean.setCurrentState(currentState);
        nMQWizardBean.setCurrentDateRequested(currentDateRequested);
        nMQWizardBean.setCurrentRequestedByDate(requestedByDate);
        nMQWizardBean.setCurrentReasonForRequest(currentReasonForRequest);
        nMQWizardBean.setCurrentReasonForApproval(currentReasonForApproval);
        nMQWizardBean.setIsApproved(isApproved);
        nMQWizardBean.setCurrentCutOffDate(currentCutOffDate);
        nMQWizardBean.setCurrentCreateDate(currentCreateDate);
        nMQWizardBean.setCurrentUntilDate(currentUntilDate);
        nMQWizardBean.setCurrentVersion(currentVersion);
        nMQWizardBean.setCurrentCreatedBy(currentCreatedBy);
        nMQWizardBean.setCurrentExtension(currentExtension);
        
        oracle.jbo.domain.Date firstTmsEntryTs = Utils.getAsDate(row, "FirstTmsEntryTs");
        nMQWizardBean.setCurrentInitialCreationDate(firstTmsEntryTs != null ? firstTmsEntryTs.toString() : null);
        nMQWizardBean.setCurrentInitialCreationBy(Utils.getAsString(row, "FirstTmsEntryBy"));
        oracle.jbo.domain.Date lastTmsEntryTs = Utils.getAsDate(row, "LastTmsEntryTs");
        nMQWizardBean.setCurrentLastActivationDate(lastTmsEntryTs != null ? lastTmsEntryTs.toString() : null);
        nMQWizardBean.setCurrentActivationBy(Utils.getAsString(row, "LastTmsEntryBy"));        
        
        // No need to load the designee list in case of copy existing.Default set as logged in user name already.
        /*
        if (nMQWizardBean.getMode() != cSMQBean.MODE_COPY_EXISTING) {
            if (null == designeeList || designeeList.size() == 0) {
                designeeList = getDesignees(currentDictContentID);
            }
            nMQWizardBean.setDesigneeList(designeeList);
            //List<String> designee = nMQWizardBean.getDesigneeList();
            CSMQBean.logger.info(userBean.getCaller() + " designee::" + designeeList);
            this.setModifiedDesigneeList(designeeList);
        }*/
        nMQWizardBean.setDesigneeList(new ArrayList<String>());
        
        String mqProductExpanded = Utils.getAsString(row, "Value3"); //MqproductExpanded
        CSMQBean.logger.info(userBean.getCaller() + " mqProductExpanded:" + mqProductExpanded);
        if (mqProductExpanded != null) {
            mqProductExpanded = mqProductExpanded.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
            Collections.addAll(nMQWizardBean.getProductList(), mqProductExpanded.split("%"));
        }

        if (currentMqgroups != null) {
            currentMqgroups = currentMqgroups.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
            Collections.addAll(nMQWizardBean.getMQGroupList(), currentMqgroups.split("%"));
        }
        nMQWizardBean.setIsNMQ(this.currentExtension.equalsIgnoreCase("NMQ"));
        nMQWizardBean.setIsSMQ(this.currentExtension.equalsIgnoreCase("SMQ"));

        String relGroup = cSMQBean.getDefaultDraftReleaseGroup();
        if (this.currentState != null && this.currentState.equals(CSMQBean.STATE_PUBLISHED))
            relGroup = cSMQBean.defaultPublishReleaseGroup;
        if (this.currentState != null && this.currentState.equals(CSMQBean.IA_STATE_PUBLISHED))
            relGroup = cSMQBean.defaultMedDRAReleaseGroup;

        String tStatus = CSMQBean.CURRENT_IF_PENDING_NULL;
        if (this.currentStatus != null && this.currentStatus.equals(CSMQBean.CURRENT_RELEASE_STATUS))
            tStatus = CSMQBean.CURRENT_RELEASE_STATUS;

        //override for IA
        if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT ||
            nMQWizardBean.getMode() == CSMQBean.MODE_VIEW_VERSION_IMPACT) {
            tStatus = CSMQBean.CURRENT_IF_PENDING_NULL_IA;
        }

        this.currentInfNoteDescription = Utils.getAsString(row, "SmqDesc");
        this.currentInfNoteSource = Utils.getAsString(row, "SmqSrc");
        this.currentInfNoteNotes = Utils.getAsString(row, "SmqNote");

        nMQWizardBean.setCurrentInfNoteDescription(this.currentInfNoteDescription);
        nMQWizardBean.setCurrentInfNoteNotes(this.currentInfNoteNotes);
        nMQWizardBean.setCurrentInfNoteSource(this.currentInfNoteSource);

        System.out.println("End of Exec populateHistoryTermDtls() ");
    }

    private void execTermDetailsBasedonHistoryDate(BigDecimal dictContentId, String effectiveDTStr) {
        System.out.println("Start Exec execTermDetailsBasedonHistoryDate() dictContentId=" + dictContentId +
                           ";; effectiveDTStr=" + effectiveDTStr);
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("loadHistoricTermInformation");
        ob.getParamsMap().put("dictContentId", dictContentId);
        ob.getParamsMap().put("effectiveDTStr", effectiveDTStr);
        ob.execute();
        System.out.println("End of Exec execTermDetailsBasedonHistoryDate() ");
    }

    private String getDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String dateStr = null;
        try {
            if (date == null) {
                date = new Date(System.currentTimeMillis());
            }
            dateStr = sdf.format(date);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        System.out.println(dateStr);
        return dateStr;
    }

    public void setHistoryFlow(boolean historyFlow) {
        this.historyFlow = historyFlow;
    }

    public boolean isHistoryFlow() {
        return historyFlow;
    }

    public void setHistoryInputDate(Date historyInputDate) {
        this.historyInputDate = historyInputDate;
    }

    public Date getHistoryInputDate() {
        return historyInputDate;
    }
    
    public String getHistoryInputDateStr() {
        return getDateStr(getHistoryDate());
    }

    public void setProductSearchRows(ArrayList<ProductSearchPojo> productSearchRows) {
        this.productSearchRows = productSearchRows;
    }

    public ArrayList<ProductSearchPojo> getProductSearchRows() {
        return productSearchRows;
    }
    
    public void addProductSearchRow(ActionEvent e){
        ProductSearchPojo row = new ProductSearchPojo();
        row.setColumnName("Product");
        productSearchRows.add(row);
        ADFUtils.addPartialTarget(productSearchBinding);
    }
    
    public void deleteSearchRow(ActionEvent e){
        ProductSearchPojo row = (ProductSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.productCurrentRow}");
        if(row != null){
            String colName = row.getColumnName();
            if(colName != null && "Scope".equalsIgnoreCase(colName)){
                ADFUtils.setEL("#{pageFlowScope.scopeSelected}", Boolean.FALSE);
            }
            else if(colName != null && "Extension".equalsIgnoreCase(colName)){
                ADFUtils.setEL("#{pageFlowScope.extensionSelected}", Boolean.FALSE);
            }
            removeSearchRow(row);
            ADFUtils.addPartialTarget(productSearchBinding);  
        }   
        else
            ADFUtils.showFacesMessage("Select a row to delete", FacesMessage.SEVERITY_ERROR);
    } 
    
    private void removeSearchRow(ProductSearchPojo row){
        productSearchRows.remove(row);
    }

    public void productSearchSelection(SelectionEvent selectionEvent) {
        //Get table from selectionEvent
        RichTable richTable = (RichTable)selectionEvent.getSource();
        //Cast to the List that populates table
        ProductSearchPojo row = (ProductSearchPojo)richTable.getSelectedRowData();
        //Get the attributes (column) from list
        ADFUtils.setEL("#{pageFlowScope.productCurrentRow}", row);
    }

    public void setProductSearchBinding(RichTable productSearchBinding) {
        this.productSearchBinding = productSearchBinding;
    }

    public RichTable getProductSearchBinding() {
        return productSearchBinding;
    }

    public void setColumnNameLOV(ArrayList<SelectItem> columnNameLOV) {
        this.columnNameLOV = columnNameLOV;
    }

    public ArrayList<SelectItem> getColumnNameLOV() {
        if(columnNameLOV == null || columnNameLOV.size() == 0){
            SelectItem item1 = new SelectItem("Product", "Product");
            SelectItem item2 = new SelectItem("Scope", "Scope");
            SelectItem item3 = new SelectItem("Extension", "Extension");
            columnNameLOV.add(item1);
            columnNameLOV.add(item2);
            columnNameLOV.add(item3);
        }
        return columnNameLOV;
    }

    public void setColumnValueLOV(ArrayList<SelectItem> columnValueLOV) {
        this.columnValueLOV = columnValueLOV;
    }

    public ArrayList<SelectItem> getColumnValueLOV() {
        ProductSearchPojo row = (ProductSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.productCurrentRow}");
        columnValueLOV.clear();
        if(row != null){
            if(row.getColumnName()!=null && row.getColumnName().equals("Product")){
                Row[] rows = ADFUtils.findIterator("ViewObj_ProductList1Iterator").getViewObject().getAllRowsInRange();
                SelectItem item1 = null;
                for(Row extRow : rows){
                    item1 = new SelectItem((String)extRow.getAttribute("ShortVal"), (String)extRow.getAttribute("LongValue"));
                    columnValueLOV.add(item1);
                }
            }
            else if(row.getColumnName()!=null && row.getColumnName().equals("Scope")){
                Row[] rows = ADFUtils.findIterator("ProductScopeVO1Iterator").getViewObject().getAllRowsInRange();
                SelectItem item1 = null;
                for(Row extRow : rows){
                    item1 = new SelectItem((String)extRow.getAttribute("Value"), (String)extRow.getAttribute("Meaning"));
                    columnValueLOV.add(item1);
                }
            }
            else if(row.getColumnName()!=null && row.getColumnName().equals("Extension")){
                Row[] rows = ADFUtils.findIterator("NMQExtentionListVO1Iterator1").getViewObject().getAllRowsInRange();
                SelectItem item1 = null;
                for(Row extRow : rows){
                    item1 = new SelectItem((String)extRow.getAttribute("RefCodelistValueShortVal"), (String)extRow.getAttribute("LongValue"));
                    columnValueLOV.add(item1);
                }
            }
        }
        return columnValueLOV;
    }

    public void setOperatorLOV(ArrayList<SelectItem> operatorLOV) {
        this.operatorLOV = operatorLOV;
    }

    public ArrayList<SelectItem> getOperatorLOV() {
        ProductSearchPojo row = (ProductSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.productCurrentRow}");
        operatorLOV.clear();
        if(row != null){
//            if(row.getColumnName()!= null && !row.getColumnName().equals("Product")){
//                SelectItem item1 = new SelectItem("AND", "AND");
//                operatorLOV.add(item1);
//            }
//            else{
                SelectItem item1 = new SelectItem("AND", "AND");
                SelectItem item2 = new SelectItem("OR", "OR");
                operatorLOV.add(item1);
                operatorLOV.add(item2);
//            }
        }
        else{
            return null;
        }
        return operatorLOV;
    }

    public void productTypeVC(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue()!= valueChangeEvent.getOldValue()){
            if(valueChangeEvent.getNewValue() != null && "Scope".equals(valueChangeEvent.getNewValue())){
                ADFUtils.setEL("#{pageFlowScope.scopeSelected}", Boolean.TRUE);
            }
            else if(valueChangeEvent.getNewValue() != null && "Extension".equals(valueChangeEvent.getNewValue())){
                ADFUtils.setEL("#{pageFlowScope.extensionSelected}", Boolean.TRUE);
            }
            if(valueChangeEvent.getOldValue() != null && "Scope".equals(valueChangeEvent.getOldValue())){
                ADFUtils.setEL("#{pageFlowScope.scopeSelected}", Boolean.FALSE);
            }
            else if(valueChangeEvent.getOldValue() != null && "Extension".equals(valueChangeEvent.getOldValue())){
                ADFUtils.setEL("#{pageFlowScope.extensionSelected}", Boolean.FALSE);
            }
        }
    }

    public void setProductValueLOV(ArrayList<SelectItem> productValueLOV) {
        this.productValueLOV = productValueLOV;
    }

    public ArrayList<SelectItem> getProductValueLOV() {
//        ProductSearchPojo row = (ProductSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.productCurrentRow}");
        if(productValueLOV.size() == 0){
//            if(row.getColumnName()!=null && row.getColumnName().equals("Product")){
                Row[] rows = ADFUtils.findIterator("ViewObj_ProductList1Iterator").getViewObject().getAllRowsInRange();
                for(Row extRow : rows){
                    SelectItem item1 = new SelectItem((String)extRow.getAttribute("ShortVal"), (String)extRow.getAttribute("LongValue"));
                    productValueLOV.add(item1);
                }
            }
//        }
        return productValueLOV;
    }

    public void setScopeValueLOV(ArrayList<SelectItem> scopeValueLOV) {
        this.scopeValueLOV = scopeValueLOV;
    }

    public ArrayList<SelectItem> getScopeValueLOV() {
        ProductSearchPojo row = (ProductSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.productCurrentRow}");
        if(row != null && scopeValueLOV.size() == 0){
            if(row.getColumnName()!=null && row.getColumnName().equals("Scope")){
                RowSetIterator rs =  ADFUtils.findIterator("ProductScopeVO1Iterator").getViewObject().createRowSetIterator(null);
                while(rs.hasNext()){
                    Row extRow = rs.next();
                    SelectItem item1 = new SelectItem((String)extRow.getAttribute("Value"), (String)extRow.getAttribute("Meaning"));
                    scopeValueLOV.add(item1);
                }
            }
        }
        return scopeValueLOV;
    }

    public void setExtensionValueLOV(ArrayList<SelectItem> extensionValueLOV) {
        this.extensionValueLOV = extensionValueLOV;
    }

    public ArrayList<SelectItem> getExtensionValueLOV() {
        ProductSearchPojo row = (ProductSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.productCurrentRow}");
        if(row != null && extensionValueLOV.size() == 0){
            if(row.getColumnName()!=null && row.getColumnName().equals("Extension")){
                RowSetIterator rs =  ADFUtils.findIterator("NMQExtentionListVO1Iterator1").getViewObject().createRowSetIterator(null);
                while(rs.hasNext()){
                    Row extRow = rs.next();
                    SelectItem item1 = new SelectItem((String)extRow.getAttribute("RefCodelistValueShortVal"), (String)extRow.getAttribute("LongValue"));
                    extensionValueLOV.add(item1);
                }
            }
        }
        return extensionValueLOV;
    }
    
    public void addGroupSearchRow(ActionEvent e){
        GroupSearchPojo row = new GroupSearchPojo();
        row.setColumnName("Group");
        getGroupValueLOV();
        groupSearchRows.add(row);
        ADFUtils.addPartialTarget(groupSearchBinding);
    }
    
    public void deleteGroupSearchRow(ActionEvent e){
        GroupSearchPojo row = (GroupSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.groupCurrentRow}");
        if(row != null){
            String colName = row.getColumnName();
            if(colName != null && "Extension".equalsIgnoreCase(colName)){
                ADFUtils.setEL("#{pageFlowScope.groupExtensionSelected}", Boolean.FALSE);
            }
            removeGroupSearchRow(row);
            ADFUtils.addPartialTarget(groupSearchBinding);  
        }   
        else
            ADFUtils.showFacesMessage("Select a row to delete", FacesMessage.SEVERITY_ERROR);
    } 
    
    private void removeGroupSearchRow(GroupSearchPojo row){
        groupSearchRows.remove(row);
    }

    public void groupSearchSelection(SelectionEvent selectionEvent) {
        //Get table from selectionEvent
        RichTable richTable = (RichTable)selectionEvent.getSource();
        //Cast to the List that populates table
        GroupSearchPojo row = (GroupSearchPojo)richTable.getSelectedRowData();
        //Get the attributes (column) from list
        ADFUtils.setEL("#{pageFlowScope.groupCurrentRow}", row);
    }

    public void setGroupSearchBinding(RichTable groupSearchBinding) {
        this.groupSearchBinding = groupSearchBinding;
    }

    public RichTable getGroupSearchBinding() {
        return groupSearchBinding;
    }
    
    public void groupTypeVC(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue()!= valueChangeEvent.getOldValue()){
            if(valueChangeEvent.getNewValue() != null && "Extension".equals(valueChangeEvent.getNewValue())){
                ADFUtils.setEL("#{pageFlowScope.groupExtensionSelected}", Boolean.TRUE);
            }
        }
    }

    public void setGroupSearchRows(ArrayList<GroupSearchPojo> groupSearchRows) {
        this.groupSearchRows = groupSearchRows;
    }

    public ArrayList<GroupSearchPojo> getGroupSearchRows() {
        return groupSearchRows;
    }

    public void setGroupSearchColumnNameLOV(ArrayList<SelectItem> groupSearchColumnNameLOV) {
        this.groupSearchColumnNameLOV = groupSearchColumnNameLOV;
    }

    public ArrayList<SelectItem> getGroupSearchColumnNameLOV() {
        return groupSearchColumnNameLOV;
    }

    public void setGroupValueLOV(ArrayList<SelectItem> groupValueLOV) {
        this.groupValueLOV = groupValueLOV;
    }
    
    public ArrayList<SelectItem> getGroupColumnNameLOV() {
        if(groupColumnNameLOV == null || groupColumnNameLOV.size() ==0){
            SelectItem item1 = new SelectItem("Group", "Group");
            SelectItem item2 = new SelectItem("Extension", "Extension");
            groupColumnNameLOV.add(item1);
            groupColumnNameLOV.add(item2);
        }
        return groupColumnNameLOV;
    }

    public ArrayList<SelectItem> getGroupValueLOV() {
//        GroupSearchPojo row = (GroupSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.groupCurrentRow}");
        if(groupValueLOV.size() == 0){
//            if(row.getColumnName()!=null && row.getColumnName().equals("Group")){
                Row[] rows = ADFUtils.findIterator("MQGroupsVO1Iterator").getViewObject().getAllRowsInRange();
                for(Row extRow : rows){
                    SelectItem item1 = new SelectItem((String)extRow.getAttribute("ShortVal"), (String)extRow.getAttribute("LongValue"));
                    groupValueLOV.add(item1);
                }
//            }
        }
        return groupValueLOV;
    }

    public void setGroupExtensionValueLOV(ArrayList<SelectItem> groupExtensionValueLOV) {
        this.groupExtensionValueLOV = groupExtensionValueLOV;
    }

    public ArrayList<SelectItem> getGroupExtensionValueLOV() {
        GroupSearchPojo row = (GroupSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.groupCurrentRow}");
        if(row != null && groupExtensionValueLOV.size() == 0){
            if(row.getColumnName()!=null && row.getColumnName().equals("Extension")){
                RowSetIterator rs =  ADFUtils.findIterator("NMQExtentionListVO1Iterator1").getViewObject().createRowSetIterator(null);
                while(rs.hasNext()){
                    Row extRow = rs.next();
                    SelectItem item1 = new SelectItem((String)extRow.getAttribute("RefCodelistValueShortVal"), (String)extRow.getAttribute("LongValue"));
                    groupExtensionValueLOV.add(item1);
                }
                Boolean rendered = (Boolean)ADFUtils.evaluateEL("#{RenderingRulesBean.wizardSearchRenderSMQSelectItem}");
                if(rendered != null && rendered){
                    SelectItem item2 = new SelectItem("SMQ", "SMQ");
                    groupExtensionValueLOV.add(item2);
                }
            }
        }
        return groupExtensionValueLOV;
    }

    public void setGroupOperatorLOV(ArrayList<SelectItem> groupOperatorLOV) {
        this.groupOperatorLOV = groupOperatorLOV;
    }

    public ArrayList<SelectItem> getGroupOperatorLOV() {
        GroupSearchPojo row = (GroupSearchPojo)ADFUtils.evaluateEL("#{pageFlowScope.groupCurrentRow}");
        groupOperatorLOV.clear();
        if(row != null){
//            if(row.getColumnName()!= null && !row.getColumnName().equals("Group")){
//                SelectItem item1 = new SelectItem("AND", "AND");
//                groupOperatorLOV.add(item1);
//            }
//            else{
                SelectItem item1 = new SelectItem("AND", "AND");
                SelectItem item2 = new SelectItem("OR", "OR");
                groupOperatorLOV.add(item1);
                groupOperatorLOV.add(item2);
//            }
        }
        else{
            return null;
        }
        return groupOperatorLOV;
    }

    public void onCancelProductPopup(ActionEvent actionEvent) {
        productSearchPopup.hide();
    }

    public void onOkProductPopup(ActionEvent actionEvent) {
        doSearch(actionEvent);
        String whereClause = prepareProductWhereClause();
        ViewObject vo = ADFUtils.findIterator("SimpleSearch1Iterator").getViewObject();
        vo.setWhereClause(whereClause);
        vo.executeQuery();
        productSearchPopup.hide();
//        ADFUtils.closePopup("p3");
//        ADFUtils.addPartialTarget(ctrlSearchResults);
    }
    
    private String prepareProductWhereClause(){
        String whereClause = "";
        String productClause = "";
        String extensionClause = "";
        String scopeClause = "";
        String productClauseForExport = "";
        if(productSearchRows != null && productSearchRows.size() > 0){
            for(ProductSearchPojo row : productSearchRows){
                if(row.getColumnName() != null && "Extension".equalsIgnoreCase(row.getColumnName())){
                    extensionClause = row.getExtensionValue();
                }
                else if(row.getColumnName() != null && "Scope".equalsIgnoreCase(row.getColumnName())){
                    scopeClause = row.getScopeValue();
                }
                else if(row.getColumnName() != null && "Product".equalsIgnoreCase(row.getColumnName())){
                    productClause = productClause + " Mqterm like '%" + row.getProductValue() + "%' OR";
//                                    + (row.getOperator() == null ? "" : row.getOperator());
                    productClauseForExport = productClauseForExport + row.getProductValue() + " OR ";
                }
            }
            if(productClause != null && productClause.endsWith("AND")){
                productClause = productClause.substring(0, productClause.length()-4);
            }
            else if(productClause != null && productClause.endsWith("OR")){
                productClause = productClause.substring(0, productClause.length()-3);
                productClauseForExport = productClauseForExport.substring(0, productClauseForExport.length()-4);
                ADFUtils.setEL("#{pageFlowScope.productClause}", productClauseForExport);
                ADFUtils.setEL("#{pageFlowScope.groupClause}", null);
            }
        }
        if(!"".equals(productClause)){
            whereClause = whereClause + "(" + productClause + ")";
        }
        if(!"".equals(extensionClause)){
            if(!"".equals(whereClause)){
                whereClause = whereClause + " AND Extension = '"+extensionClause+"'";
            }
            else{
                whereClause = whereClause + " Extension = '"+extensionClause+"'";
            }
        }
        if(!"".equals(scopeClause)){
            if(!"".equals(whereClause)){
                whereClause = whereClause + " AND Mqscp = '"+scopeClause+"'";
            }
            else{
                whereClause = whereClause + " Mqscp = '"+scopeClause+"'";
            }
        }
        
        return whereClause;
    }

    public void setProductSearchPopup(RichPopup productSearchPopup) {
        this.productSearchPopup = productSearchPopup;
    }

    public RichPopup getProductSearchPopup() {
        return productSearchPopup;
    }

    public void okGroupPopup(ActionEvent actionEvent) {
        doSearch(actionEvent);
        String whereClause = prepareGroupWhereClause();
        ViewObject vo = ADFUtils.findIterator("SimpleSearch1Iterator").getViewObject();
        vo.setWhereClause(whereClause);
        vo.executeQuery();
//        groupSearchRows.clear();
        groupSearchPopup.hide();
    }

    public void cancelGroupPopup(ActionEvent actionEvent) {
//        groupSearchRows.clear();
        groupSearchPopup.hide();
    }

    public void setGroupSearchPopup(RichPopup groupSearchPopup) {
        this.groupSearchPopup = groupSearchPopup;
    }

    public RichPopup getGroupSearchPopup() {
        return groupSearchPopup;
    }
    
    private String prepareGroupWhereClause(){
        String whereClause = "";
        String groupClause = "";
        String extensionClause = "";
        String groupClauseForExport = "";
        if(groupSearchRows != null && groupSearchRows.size() > 0){
            for(GroupSearchPojo row : groupSearchRows){
                if(row.getColumnName() != null && "Extension".equalsIgnoreCase(row.getColumnName())){
                    extensionClause = row.getExtensionValue();
                }
                else if(row.getColumnName() != null && "Group".equalsIgnoreCase(row.getColumnName())){
                    groupClause = groupClause + " Mqgroup_F like '%" + row.getGroupValue() + "%' OR";
//                                  + (row.getOperator() == null ? "" : row.getOperator());
                     groupClauseForExport = groupClauseForExport + row.getGroupValue() + " OR ";
                }
            }
            if(groupClause != null && groupClause.endsWith("AND")){
                groupClause = groupClause.substring(0, groupClause.length()-4);
            }
            else if(groupClause != null && groupClause.endsWith("OR")){
                groupClause = groupClause.substring(0, groupClause.length()-3);
                groupClauseForExport = groupClauseForExport.substring(0, groupClauseForExport.length()-4);
                ADFUtils.setEL("#{pageFlowScope.groupClause}", groupClauseForExport);
                ADFUtils.setEL("#{pageFlowScope.productClause}", null);
            }
        }
        if(!"".equals(groupClause)){
            whereClause = whereClause + "(" + groupClause + ")";
        }
        if(!"".equals(extensionClause)){
            if(!"".equals(whereClause)){
                whereClause = whereClause + " AND Extension = '"+extensionClause+"'";
            }
            else{
                whereClause = whereClause + " Extension = '"+extensionClause+"'";
            }
        }
        
        return whereClause;
    }

    public void setProductRowsSize(Integer productRowsSize) {
        this.productRowsSize = productRowsSize;
    }

    public Integer getProductRowsSize() {
        productRowsSize  =  productSearchRows.size();
        return productRowsSize;
    }

    public void setGroupColumnNameLOV(ArrayList<SelectItem> groupColumnNameLOV) {
        this.groupColumnNameLOV = groupColumnNameLOV;
    }

    public void setGroupRowsSize(Integer groupRowsSize) {
        this.groupRowsSize = groupRowsSize;
    }

    public Integer getGroupRowsSize() {
        groupRowsSize  =  groupSearchRows.size();
        return groupRowsSize;
    }

    public void clearProductSearchRows(ActionEvent actionEvent) {
        if(productSearchRows != null)
            productSearchRows.clear();
        if(productSearchBinding != null)
        ADFUtils.addPartialTarget(productSearchBinding);
        ADFUtils.setEL("#{pageFlowScope.productClause}", null);
    }
    
    public void clearGroupSearchRows(ActionEvent actionEvent) {
        if(groupSearchRows != null)
            groupSearchRows.clear();
        if(groupSearchBinding != null)
        ADFUtils.addPartialTarget(groupSearchBinding);
        ADFUtils.setEL("#{pageFlowScope.groupClause}", null);
    }

    public void showProductSearchPopup(ActionEvent actionEvent) {
        getProductValueLOV();
        ADFUtils.showPopup(getProductSearchPopup());
    }
}

