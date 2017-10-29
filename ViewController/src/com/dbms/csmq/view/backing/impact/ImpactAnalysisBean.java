package com.dbms.csmq.view.backing.impact;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.backing.NMQ.NMQSourceTermSearchBean;
import com.dbms.csmq.view.backing.NMQ.NMQUtils;
import com.dbms.csmq.view.backing.NMQ.NMQWizardBean;
import com.dbms.csmq.view.backing.NMQ.NMQWizardSearchBean;
import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.hierarchy.HierarchyAccessor;
import com.dbms.csmq.view.hierarchy.NewPTListBean;
import com.dbms.csmq.view.impact.FutureImpactHierarchyBean;
import com.dbms.csmq.view.impact.MedDRAImpactHierarchyBean;
import com.dbms.csmq.view.impact.PreviousVerCurrentImpactHierarchyBean;
import com.dbms.csmq.view.impact.PreviousVerFutureImpactHierarchyBean;
import com.dbms.util.ADFUtils;
import com.dbms.util.Utils;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichToolbar;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DropEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

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


public class ImpactAnalysisBean extends HierarchyAccessor {
    
    private String showImpact = CSMQBean.TRUE;
    private String mqType = "";//CSMQBean.NMQ;
    FutureImpactHierarchyBean futureImpactHierarchyBean = (FutureImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("FutureImpactHierarchyBean");
    CSMQBean cSMQBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
    UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
    NMQWizardSearchBean nMQWizardSearchBean = (NMQWizardSearchBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardSearchBean");
    NMQWizardBean nMQWizardBean = nMQWizardSearchBean.getNMQWizardBean();
    ImpactAnalysisUIBean impactAnalysisUIBean = null;
    
    private RichTreeTable preferedTermSourceTree;
    private RichTreeTable medDRATree;
    private RichTreeTable futureTree;
    private RichTreeTable hierarchySourceTree;
    private RichSelectManyChoice ctrlProducts;
    private List <String> productList = new ArrayList <String>();
    private RichSelectOneChoice ctrlNMQStatus;
    private String paramReleaseGroup;
    private String parentSOCcontentID;
    private String currentTermName;
    private String activationGroups;
    private String activeDictionaryName;
    private String currentDictId;
    
    // FOR WORKFLOW
    private String currentState = CSMQBean.STATE_PROPOSED;
    private String currentReasonForApproval;
    
    private String currentPredictGroups;
    private String currentContentCode;
    
    int mode;
    
    private String paramFutureShowNonImpacted = CSMQBean.TRUE;
    private String paramFutureSort = "SCOPE";
    private String paramFuturePrimaryOnly = CSMQBean.FALSE;
    private String paramFutureScope = CSMQBean.FULL_NMQ_SMQ;

    private String paramMedDRAShowNonImpacted = CSMQBean.TRUE;
    private String paramMedDRASort = "SCOPE";
    private String paramMedDRAPrimaryOnly = CSMQBean.FALSE;
    private String paramMedDRAScope = CSMQBean.FULL_NMQ_SMQ;
    
    private String allGroups;
    private boolean futureShowImpacted;
    private boolean renderSave = false;
    private RichPopup.PopupHints hints = null;
    //private String socList = "";
    private Object socList;
    private Object newPTDisclosedKeys;
    
    private String searchLevelStr = "%";
    private String searchStateStr = "%";
    private String searchStatusStr = "%";
    private String searchTermStr;
    private boolean showPrevVerCurrentImpactedOnly;
    private boolean showPrevVerFutureImpactedOnly;

    public void setHints(RichPopup.PopupHints hints) {
        this.hints = hints;
    }

    public RichPopup.PopupHints getHints() {
        return hints;
    }

    public void setSearchLevelStr(String searchLevelStr) {
        this.searchLevelStr = searchLevelStr;
    }

    public String getSearchLevelStr() {
        return searchLevelStr;
    }

    public void setSearchTermStr(String searchTermStr) {
        this.searchTermStr = searchTermStr;
    }

    public String getSearchTermStr() {
        return searchTermStr;
    }

    public void setSearchCodeStr(String searchCodeStr) {
        this.searchCodeStr = searchCodeStr;
    }

    public String getSearchCodeStr() {
        return searchCodeStr;
    }
    private String searchCodeStr;

    public ImpactAnalysisBean() {
        
        super();
        System.out.println("START: ImpactAnalysisBean");
        activationGroups = applicationBean.getDefaultDraftReleaseGroup() + "," + applicationBean.getDefaultMedDRAReleaseGroup();
        //NMATDRAFT_AG,NMATMED_AG
        CSMQBean.logger.info(userBean.getCaller() + " @ImpactAnalysisBean");
        CSMQBean.logger.info(userBean.getCaller() + " New PT search groups: " + activationGroups);

        String isViewPreviousFlow =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("isViewPreviousFlow");
        CSMQBean.logger.info(userBean.getCaller() + " isViewPreviousFlow : " + isViewPreviousFlow);

        userBean.setCurrentMenuPath("Y".equals(isViewPreviousFlow) ? "Previous Version Impact › MedDRA Version" :
                                    "Impact Assessment › MedDRA Version");
        userBean.setCurrentMenu("MEDDRA_IMPACT_ASSESSMENT");
        this.currentPredictGroups = cSMQBean.getDefaultMedDRAReleaseGroup();
        getDictionaryInfo();
        allGroups = this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName + "," + this.defaultReleaseGroupName;
        nMQWizardBean.setPerformImpactPriorToExport(true);
        if (nMQWizardBean.getMode() == CSMQBean.MODE_VIEW_VERSION_IMPACT) {
            userBean.setCurrentMenuPath("Y".equals(isViewPreviousFlow) ? "Previous Version Impact › MedDRA Version" :
                                        "View Version Impact › MedDRA Version");
            userBean.setCurrentMenu("VIEW_VERSION_IMPACT");
        }
        hints = new RichPopup.PopupHints();
        System.out.println("END: ImpactAnalysisBean");
        }


    private ImpactAnalysisUIBean getImpactAnalysisUIBean() {
        if (impactAnalysisUIBean == null) impactAnalysisUIBean = (ImpactAnalysisUIBean)ADFContext.getCurrent().getRequestScope().get("ImpactAnalysisUIBean");
        return impactAnalysisUIBean;
        }

    public void getDictionaryInfo() {
        CSMQBean.logger.info(">> Start getDictionaryInfo");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("CurrentDictionaryVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        vo.setNamedWhereClauseParam("shortName", CSMQBean.defaultBaseDictionaryShortName);
        vo.executeQuery();
        Enumeration rows = dciterb.getRowSetIterator().enumerateRowsInRange();
        Row row = (Row)rows.nextElement();
        this.activeDictionaryName = Utils.getAsString(row, "Name");
        CSMQBean.logger.info(">> End getDictionaryInfo");
        }
    

//    public void doSearch(ActionEvent actionEvent) {        
//        // THIS IS NOT USED ANY MORE
//        impactAnalysisUIBean = (ImpactAnalysisUIBean)ADFContext.getCurrent().getRequestScope().get("ImpactAnalysisUIBean");
//        impactAnalysisUIBean.getImpactSearchPopUp().show(hints);
//    
//        String assessmentType = impactAnalysisUIBean.getCtrlImpact().getValue().toString();
//        if (assessmentType.equals("INMQ")){
//            this.showImpact = CSMQBean.TRUE;
//            this.mqType = CSMQBean.NMQ;
//            this.mode = CSMQBean.MODE_UPDATE_EXISTING;
//            }
//        else if (assessmentType.equals("NINMQ")){
//            this.showImpact = CSMQBean.FALSE;
//            this.mqType = CSMQBean.NMQ;
//            this.mode = CSMQBean.MODE_UPDATE_EXISTING;
//            }
//        else if (assessmentType.equals("ISMQ")){
//            this.showImpact = CSMQBean.TRUE;
//            this.mqType = CSMQBean.SMQ;
//            this.mode = CSMQBean.MODE_UPDATE_SMQ;
//            }
//        else if (assessmentType.equals("NISMQ")){
//            this.showImpact = CSMQBean.FALSE;
//            this.mqType = CSMQBean.SMQ;
//            this.mode = CSMQBean.MODE_UPDATE_SMQ;
//            }
//        
//        CSMQBean.logger.info(userBean.getCaller() + " ** PERFORMING SEARCH **");
//        CSMQBean.logger.info(userBean.getCaller() + " showImpact: " + this.showImpact);
//        CSMQBean.logger.info(userBean.getCaller() + " mqType: " + this.mqType);
//        CSMQBean.logger.info(userBean.getCaller() + " killSwitch: " + CSMQBean.KILL_SWITCH_OFF);
//        CSMQBean.logger.info(userBean.getCaller() + " ***********************");
// 
//        BindingContext bc = BindingContext.getCurrent();
//        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
//        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("ImpactSearchListVO1Iterator");
//        ViewObject vo = dciterb.getViewObject();
//        
//        vo.setNamedWhereClauseParam("showImpact", this.showImpact);
//        vo.setNamedWhereClauseParam("mqType", this.mqType);
//        vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_OFF);
//       
//        vo.executeQuery();
//        
//        
//        
//        impactAnalysisUIBean.getImpactSearchPopUp().show(hints);
//        
//
//        
//        
//        AdfFacesContext.getCurrentInstance().addPartialTarget(impactAnalysisUIBean.getCntrlImpactSearchResults());
//        AdfFacesContext.getCurrentInstance().partialUpdateNotify(impactAnalysisUIBean.getCntrlImpactSearchResults());
//        AdfFacesContext.getCurrentInstance().addPartialTarget(impactAnalysisUIBean.getCntrlTrain());
//        AdfFacesContext.getCurrentInstance().partialUpdateNotify(impactAnalysisUIBean.getCntrlTrain());
//        AdfFacesContext.getCurrentInstance().addPartialTarget(impactAnalysisUIBean.getPromotionToolBar());
//        AdfFacesContext.getCurrentInstance().partialUpdateNotify(impactAnalysisUIBean.getPromotionToolBar());
//        //clear the selected row
//        RowKeySet rks = impactAnalysisUIBean.getCntrlImpactSearchResults().getSelectedRowKeys();
//        rks.clear();
//     
//        
//        }


    public String getShowImpact() {
        return showImpact;
    }

    public String getDefaultMedDRAGroupName() {
        return defaultMedDRAGroupName;
    }

    public String getMqType() {
        return mqType;
    }

    public void setShowImpact(String showImpact) {
        this.showImpact = showImpact;
    }

    public void setDefaultMedDRAGroupName(String groupName) {
        this.defaultMedDRAGroupName = groupName;
    }

    public void setMqType(String mQLevel) {
        this.mqType = mQLevel;
    }

    public void rowSelected(SelectionEvent selectionEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE ****");
        String isViewPreviousFlow =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("isViewPreviousFlow");
        CSMQBean.logger.info(userBean.getCaller() + " isViewPreviousFlow =" + isViewPreviousFlow);
        String source = ((RichTable)selectionEvent.getSource()).getId();
        String iterator = null;
        if (source.equalsIgnoreCase("T_CMQ_Y")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_CMQ_Y" : "ImpactSearchListVO_CMQ_Y";
            //            this.showImpact = CSMQBean.TRUE;
            //            this.mqType = CSMQBean.CMQ;
            //            this.mode = CSMQBean.MODE_UPDATE_EXISTING;
        } else if (source.equalsIgnoreCase("T_CMQ_N")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_CMQ_N" : "ImpactSearchListVO_CMQ_N";
            //            this.showImpact = CSMQBean.FALSE;
            //            this.mqType = CSMQBean.CMQ;
            //            this.mode = CSMQBean.MODE_UPDATE_EXISTING;
        } else if (source.equalsIgnoreCase("TBL_MQ_N")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_MQ_N" : "ImpactSearchListVO_MQ_N";
            //            this.showImpact = CSMQBean.FALSE;
            //            this.mqType = CSMQBean.SMQ;
            //            this.mode = CSMQBean.MODE_UPDATE_SMQ;
        } else if (source.equalsIgnoreCase("TBL_MQ_Y")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_MQ_Y" : "ImpactSearchListVO_MQ_Y";
            //            this.showImpact = CSMQBean.TRUE;
            //            this.mqType = CSMQBean.SMQ;
            //            this.mode = CSMQBean.MODE_UPDATE_SMQ;
        } else if (source.equalsIgnoreCase("TBL_NMQ_N")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_NMQ_N" : "ImpactSearchListVO_NMQ_N";
            //            this.showImpact = CSMQBean.FALSE;
            //            this.mqType = CSMQBean.NMQ;
            //            this.mode = CSMQBean.MODE_UPDATE_EXISTING;
        } else if (source.equalsIgnoreCase("TBL_NMQ_Y")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_NMQ_Y" : "ImpactSearchListVO_NMQ_Y";
            //            this.showImpact = CSMQBean.TRUE;
            //            this.mqType = CSMQBean.NMQ;
            //            this.mode = CSMQBean.MODE_UPDATE_EXISTING;
        }


        resolveMethodExpression("#{bindings." + iterator + ".collectionModel.makeCurrent}", null,
                                new Class[] { SelectionEvent.class }, new Object[] { selectionEvent });
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
        String selectedTermState = Utils.getAsString(row, "WorkflowState");
        CSMQBean.logger.info(userBean.getCaller() + " selected Term State: " + selectedTermState);
        //validate the current state of the selected term and allow only for the below states activated or all IA states
        if (null == selectedTermState ||
            (selectedTermState.equalsIgnoreCase(CSMQBean.STATE_ACTIVATED) || selectedTermState.equalsIgnoreCase(CSMQBean.STATE_PENDING_IMPACT_ASSESSMENT) ||
             selectedTermState.equalsIgnoreCase(CSMQBean.IA_STATE_REVIEWED) ||
             selectedTermState.equalsIgnoreCase(CSMQBean.IA_STATE_APPROVED) ||
             selectedTermState.equalsIgnoreCase(CSMQBean.IA_STATE_PUBLISHED))) {

            if (source.equalsIgnoreCase("T_CMQ_Y")) {
                // iterator = "ImpactSearchListVO_CMQ_Y";
                this.showImpact = CSMQBean.TRUE;
                this.mqType = CSMQBean.CMQ;
                this.mode = CSMQBean.MODE_UPDATE_EXISTING;
            } else if (source.equalsIgnoreCase("T_CMQ_N")) {
                //  iterator = "ImpactSearchListVO_CMQ_N";
                this.showImpact = CSMQBean.FALSE;
                this.mqType = CSMQBean.CMQ;
                this.mode = CSMQBean.MODE_UPDATE_EXISTING;
            } else if (source.equalsIgnoreCase("TBL_MQ_N")) {
                //   iterator = "ImpactSearchListVO_MQ_N";
                this.showImpact = CSMQBean.FALSE;
                this.mqType = CSMQBean.SMQ;
                this.mode = CSMQBean.MODE_UPDATE_SMQ;
            } else if (source.equalsIgnoreCase("TBL_MQ_Y")) {
                iterator = "ImpactSearchListVO_MQ_Y";
                this.showImpact = CSMQBean.TRUE;
                this.mqType = CSMQBean.SMQ;
                this.mode = CSMQBean.MODE_UPDATE_SMQ;
            } else if (source.equalsIgnoreCase("TBL_NMQ_N")) {
                //  iterator = "ImpactSearchListVO_NMQ_N";
                this.showImpact = CSMQBean.FALSE;
                this.mqType = CSMQBean.NMQ;
                this.mode = CSMQBean.MODE_UPDATE_EXISTING;
            } else if (source.equalsIgnoreCase("TBL_NMQ_Y")) {
                //   iterator = "ImpactSearchListVO_NMQ_Y";
                this.showImpact = CSMQBean.TRUE;
                this.mqType = CSMQBean.NMQ;
                this.mode = CSMQBean.MODE_UPDATE_EXISTING;
            }


            this.currentDictId = Utils.getAsString(row, "DictContentId");
            this.currentContentCode = Utils.getAsString(row, "DictContentCode");
            this.currentTermName = Utils.getAsString(row, "Term");
            this.activationGroups = CSMQBean.defaultMedDRAReleaseGroup;
            String smqNmq = Utils.getAsString(row, "NmqSmq");
            String queryLevel = Utils.getAsString(row, "DefLevelsShortName");

            nMQWizardBean.setFooterInfo(this.currentTermName);

            String status = Utils.getAsString(row, "Status");

            CSMQBean.logger.info(userBean.getCaller() + " currentDictId: " + currentDictId);
            
            /*
            HashMap result = null;
            if (null == selectedTermState || selectedTermState.equalsIgnoreCase(CSMQBean.STATE_ACTIVATED)) {
                //result = NMQUtils.setDefaultIAState(this.currentDictId, smqNmq, userBean.getCurrentUser(), userBean.getCurrentUserRole());
                DCBindingContainer bc = ADFUtils.getDCBindingContainer();
                OperationBinding ob = bc.getOperationBinding("setDefaultIAState");
                ob.getParamsMap().put("dictContentIDs", this.currentDictId);
                ob.getParamsMap().put("currentUser", userBean.getCurrentUser());
                ob.getParamsMap().put("currentUserRole", userBean.getCurrentUserRole());

                result = (HashMap)ob.execute();
                if (null != result) {
                    String retCode = (String)result.get("RETURN_CODE");
                    if (null != retCode && retCode.equalsIgnoreCase(CSMQBean.SUCCESS)) {
                        this.setCurrentState((String)result.get("STATE"));
                        CSMQBean.logger.info(userBean.getCaller() + " STATE : " + (String)result.get("STATE"));
                    } else {
                        this.setCurrentState(selectedTermState);
                    }
                }

            } else {
                this.setCurrentState(selectedTermState);
            }
            */

            Hashtable result = null;
            if (null == selectedTermState || selectedTermState.equalsIgnoreCase(CSMQBean.STATE_ACTIVATED)) {
                result =
                        NMQUtils.setDefaultIAState(this.currentDictId, smqNmq, userBean.getCurrentUser(), userBean.getCurrentUserRole());
            }
            if (result != null) {
                this.setCurrentState((String)result.get("STATE"));
                CSMQBean.logger.info(userBean.getCaller() + " STATE : " + (String)result.get("STATE"));
            } else {
                this.setCurrentState(selectedTermState);
            }

            CSMQBean.logger.info(userBean.getCaller() + " Current state : " + this.getCurrentState());
            // INF NOTES STUFF
            nMQWizardBean.setCurrentDictContentID(this.currentDictId);
            //nMQWizardBean.setCurrentState(this.getCurrentState());
            //nMQWizardBean.setCurrentStatus(this.getCurrentState());
            nMQWizardBean.setCurrentStatus(status);
            nMQWizardSearchBean.setCurrentStatus(status);
            if (null != queryLevel && !queryLevel.isEmpty()) {
                nMQWizardSearchBean.setParamLevel(queryLevel.substring(queryLevel.length() - 1));
                CSMQBean.logger.info("queryLevel >>>" + nMQWizardSearchBean.getParamLevel());
            }

            String termProduct = Utils.getAsString(row, "Value3");
            nMQWizardBean.setCurrentContentCode(this.currentContentCode);
            // set product and group data
            if (termProduct != null) {
                termProduct = termProduct.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
                Collections.addAll(nMQWizardBean.getProductList(), termProduct.split("%"));
                CSMQBean.logger.info("Term Product list >>>" + nMQWizardBean.getProductList());
            }
            String termGroup = Utils.getAsString(row, "Value2");
            if (termGroup != null) {
                termGroup = termGroup.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
                Collections.addAll(nMQWizardBean.getMQGroupList(), termGroup.split("%"));
                CSMQBean.logger.info("Term Group list >>>" + nMQWizardBean.getMQGroupList());
            }
            nMQWizardBean.setIsNMQ(smqNmq.equalsIgnoreCase("NMQ"));
            nMQWizardBean.setIsSMQ(smqNmq.equalsIgnoreCase("SMQ"));
            nMQWizardSearchBean.initForImpactAnalysis(currentContentCode, currentDictId, activationGroups);
            nMQWizardBean.setCurrentState(this.getCurrentState());

            getImpactAnalysisUIBean().getImpactSearchPopUp().cancel();

            if (medDRATree != null && medDRATree.getDisclosedRowKeys() != null)
                medDRATree.getDisclosedRowKeys().clear(); //to resolve NoRowAvailableException

            if (futureTree != null && futureTree.getDisclosedRowKeys() != null)
                futureTree.getDisclosedRowKeys().clear(); //to resolve NoRowAvailableException

            getImpactAnalysisUIBean().getCntrlExportButton().setDisabled(false);
            getImpactAnalysisUIBean().getCntrlExportDisplayedButtonLeft().setDisabled(false);
            getImpactAnalysisUIBean().getCntrlExportDisplayedButtonRight().setDisabled(false);
            // getImpactAnalysisUIBean().getCntrlRefreshButton().setDisabled(false);
            getImpactAnalysisUIBean().getCntrlExportButton().setDisabled(false);

            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getPromotionToolBar());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getPromotionToolBar());

            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlImpactLeftToolbar());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlImpactLeftToolbar());

            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlImpactRightToolbar());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlImpactRightToolbar());

            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlTrain());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlTrain());
            refreshTrees();
        } else {
            CSMQBean.logger.info(userBean.getCaller() + " selected Term State: " + selectedTermState);
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_WARN, "The MedDRA Query is being updated. Activate the MedDRA Query first, then run impact assessment.",
                                 "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
    }
    
    
    private void clearSearch() {
        try {
            searchLevelStr = "%";
            searchTermStr = null;
            searchCodeStr = null;
            onImpartSearchAction(null);
            //reset the filters
            if (getImpactAnalysisUIBean().getCntrlSearchResultsTBL_CMQ_Y() != null &&
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_CMQ_Y().getDisclosedRowKeys() != null)
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_CMQ_Y().getDisclosedRowKeys().clear();

            if (getImpactAnalysisUIBean().getCntrlSearchResultsTBL_CMQ_N() != null &&
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_CMQ_N().getDisclosedRowKeys() != null)
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_CMQ_N().getDisclosedRowKeys().clear();

            if (getImpactAnalysisUIBean().getCntrlSearchResultsTBL_NMQ_Y() != null &&
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_NMQ_Y().getDisclosedRowKeys() != null)
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_NMQ_Y().getDisclosedRowKeys().clear();

            if (getImpactAnalysisUIBean().getCntrlSearchResultsTBL_MQ_Y() != null &&
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_MQ_Y().getDisclosedRowKeys() != null)
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_MQ_Y().getDisclosedRowKeys().clear();

            if (getImpactAnalysisUIBean().getCntrlSearchResultsTBL_NMQ_N() != null &&
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_NMQ_N().getDisclosedRowKeys() != null)
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_NMQ_N().getDisclosedRowKeys().clear();

            if (getImpactAnalysisUIBean().getCntrlSearchResultsTBL_MQ_N() != null &&
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_MQ_N().getDisclosedRowKeys() != null)
                getImpactAnalysisUIBean().getCntrlSearchResultsTBL_MQ_N().getDisclosedRowKeys().clear();

            getImpactAnalysisUIBean().getCntrlShowImpactedFilterExisting().setValue(false);
            getImpactAnalysisUIBean().getCntrlShowPrimaryFilterExisting().setValue(false);
            getImpactAnalysisUIBean().getCntrlShowSortFilterExisting().setValue("TERM");
            getImpactAnalysisUIBean().getCntrlShowImpactedFilterFuture().setValue(false);
            getImpactAnalysisUIBean().getCntrlShowPrimaryFilterFuture().setValue(false);
            getImpactAnalysisUIBean().getCntrlShowSortFilterFuture().setValue("TERM");

            getImpactAnalysisUIBean().getCntrlShowImpactedFilterExisting().resetValue();
            getImpactAnalysisUIBean().getCntrlShowPrimaryFilterExisting().resetValue();
            getImpactAnalysisUIBean().getCntrlShowSortFilterExisting().resetValue();
            getImpactAnalysisUIBean().getCntrlShowImpactedFilterFuture().resetValue();
            getImpactAnalysisUIBean().getCntrlShowPrimaryFilterFuture().resetValue();
            getImpactAnalysisUIBean().getCntrlShowSortFilterFuture().resetValue();

            this.paramFutureShowNonImpacted = CSMQBean.TRUE;
            this.paramFuturePrimaryOnly = CSMQBean.FALSE;
            this.paramMedDRAShowNonImpacted = CSMQBean.TRUE;
            this.paramMedDRAPrimaryOnly = CSMQBean.FALSE;
            this.paramFutureSort = "TERM";
            this.paramMedDRASort = "SCOPE";

            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlFutureMenu());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlFutureMenu());

            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlExistingMenu());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlExistingMenu());
        } catch (Exception e) {
        }
    }


    public void refreshTrees() {
        String isViewPreviousFlow =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("isViewPreviousFlow");
        if ("Y".equals(isViewPreviousFlow)) {
            setShowPrevVerCurrentImpactedOnly(false);
            setShowPrevVerFutureImpactedOnly(false);
            refreshPreviousVersionTrees();
        } else {
            refreshFutureTree(null);
            refreshMedDRATree();
        }
    }

    public void refreshPreviousVersionTrees() {
        System.out.println("Start exec refreshPreviousVersionTrees() this.currentDictId --> " + this.currentDictId);
        execLoadMQDetailsOP("loadPrevVersionCurrentNFurteMQDetails", new Long(this.currentDictId), false);
        refreshPreviousVersionCurrentTree();
        refreshPreviousVersionFutureTree();
        System.out.println("End of exec refreshPreviousVersionTrees()");
    }
    
    private void execLoadMQDetailsOP(String opName, Long dictContentId, Boolean showImpactedOnly){
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding(opName);
        System.out.println("execLoadMQDetailsOP() opName-->" +opName+";; dictContentId --> " + dictContentId +";; showImpactedOnly --> " + showImpactedOnly);
        ob.getParamsMap().put("dictContentId", dictContentId);
        ob.getParamsMap().put("showImpactedOnly", showImpactedOnly);
        ob.execute();
    }
    
    public void refreshFutureTree () {
        refreshFutureTree(null);
    }
    
    
    public void refreshMedDRATree () {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding medDRAImpactVO1Iterator = (DCIteratorBinding)binding.get("MedDRAImpactVO1Iterator1");
        ViewObject medDRATreeVO = medDRAImpactVO1Iterator.getViewObject();
        
        CSMQBean.logger.info(userBean.getCaller() + " UPDATING: " +  allGroups); 
        medDRATreeVO.setNamedWhereClauseParam("activationGroup", allGroups);
        medDRATreeVO.setNamedWhereClauseParam("dictContentID", this.currentDictId);
        medDRATreeVO.setNamedWhereClauseParam("sortKey", this.paramMedDRASort);
        medDRATreeVO.setNamedWhereClauseParam("showNonImpacted", this.paramMedDRAShowNonImpacted);
        medDRATreeVO.setNamedWhereClauseParam("returnPrimLinkPath", this.paramMedDRAPrimaryOnly);
        medDRATreeVO.setNamedWhereClauseParam("scopeFilter", this.paramMedDRAScope);
            
        medDRATreeVO.setNamedWhereClauseParam("maxLevels", CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        medDRATreeVO.setNamedWhereClauseParam("startLevel", 0);
        
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: " + "MedDRAImpactVO1Iterator1");
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + allGroups);
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " + this.currentDictId);
        CSMQBean.logger.info(userBean.getCaller() + " sortKey: " + this.paramMedDRASort);
        CSMQBean.logger.info(userBean.getCaller() + " showNonImpacted: " + this.paramMedDRAShowNonImpacted);
        CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath: " + this.paramMedDRAPrimaryOnly);
        CSMQBean.logger.info("scopeFilter: " + this.paramMedDRAScope);
        CSMQBean.logger.info(userBean.getCaller() + " maxLevels: " + CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        CSMQBean.logger.info(userBean.getCaller() + " startLevel: " + 0);
        
        medDRATreeVO.executeQuery(); 
        MedDRAImpactHierarchyBean medDRAImpactHierarchyBean = (MedDRAImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("MedDRAImpactHierarchyBean");
        medDRAImpactHierarchyBean.init();
        AdfFacesContext.getCurrentInstance().addPartialTarget(medDRATree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(medDRATree);
        }
    
    
    public void refreshFutureTree (ActionEvent actionEvent) {
   
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding draftImpactVO1Iterator = (DCIteratorBinding)binding.get("DraftImpactVO1Iterator1");
        ViewObject draftTree = draftImpactVO1Iterator.getViewObject();
        //String bothGroups = this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName;
        CSMQBean.logger.info(userBean.getCaller() + " UPDATING: " +  allGroups); 
        draftTree.setNamedWhereClauseParam("activationGroup", allGroups);
        draftTree.setNamedWhereClauseParam("dictContentID", this.currentDictId);
        
            
        draftTree.setNamedWhereClauseParam("activationGroup", allGroups);
        draftTree.setNamedWhereClauseParam("dictContentID", this.currentDictId);
        draftTree.setNamedWhereClauseParam("sortKey", this.paramFutureSort);
        draftTree.setNamedWhereClauseParam("showNonImpacted", this.paramFutureShowNonImpacted);
        draftTree.setNamedWhereClauseParam("returnPrimLinkPath", this.paramFuturePrimaryOnly);
        draftTree.setNamedWhereClauseParam("scopeFilter", this.paramFutureScope);
        draftTree.setNamedWhereClauseParam("maxLevels", CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        draftTree.setNamedWhereClauseParam("startLevel", 0);
        
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: " + "DraftImpactVO1Iterator1");
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + allGroups);
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " + this.currentDictId);
        CSMQBean.logger.info(userBean.getCaller() + " sortKey: " + this.paramFutureSort);
        CSMQBean.logger.info(userBean.getCaller() + " showNonImpacted: " + this.paramFutureShowNonImpacted);
        CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath: " + this.paramFuturePrimaryOnly);
        CSMQBean.logger.info("scopeFilter: " + this.paramFutureScope);
        CSMQBean.logger.info(userBean.getCaller() + " maxLevels: " + CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        CSMQBean.logger.info(userBean.getCaller() + " startLevel: " + 0);
        
        draftTree.executeQuery();   
        FutureImpactHierarchyBean futureImpactHierarchyBean = (FutureImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("FutureImpactHierarchyBean");
        boolean hasScope = false;
        if (nMQWizardBean.getCurrentScope() != null)
            hasScope = nMQWizardBean.getCurrentScope().equals(CSMQBean.HAS_SCOPE);
            
        CSMQBean.logger.info(userBean.getCaller() + " hasScope: " + hasScope);
        futureImpactHierarchyBean.init(hasScope);
        clearKeys(futureTree);
        AdfFacesContext.getCurrentInstance().addPartialTarget(futureTree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(futureTree);
        }
    
    public void refreshPreviousVerFutureTree(ActionEvent actionEvent) {
        //execLoadMQDetailsOP("loadPrevVersionFurteMQDetails", new Long(this.currentDictId), isShowPrevVerFutureImpactedOnly());
        //refreshPreviousVersionFutureTree();
        refreshPreviousVersionFutureTreeNew();
    }
    
    public void refreshPreviousVerCurrentTree(ActionEvent actionEvent) {
        //execLoadMQDetailsOP("loadPrevVersionCurrentMQDetails", new Long(this.currentDictId), isShowPrevVerCurrentImpactedOnly());
        //refreshPreviousVersionCurrentTree();
        refreshPreviousVersionCurrentTreeNew();
    }

    public void refreshPreviousVersionCurrentTree() {
        boolean hasScope = false;
        if (nMQWizardBean.getCurrentScope() != null)
            hasScope = nMQWizardBean.getCurrentScope().equals(CSMQBean.HAS_SCOPE);

        PreviousVerCurrentImpactHierarchyBean previousVerCurrentImpactHierarchyBean =
            (PreviousVerCurrentImpactHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("PreviousVerCurrentImpactHierarchyBean");
        previousVerCurrentImpactHierarchyBean.init(hasScope);
        AdfFacesContext.getCurrentInstance().addPartialTarget(medDRATree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(medDRATree);
    }
    
    public void refreshPreviousVersionCurrentTreeNew() {
        PreviousVerCurrentImpactHierarchyBean previousVerCurrentImpactHierarchyBean =
            (PreviousVerCurrentImpactHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("PreviousVerCurrentImpactHierarchyBean");
        previousVerCurrentImpactHierarchyBean.rebuildTree(isShowPrevVerCurrentImpactedOnly());
        AdfFacesContext.getCurrentInstance().addPartialTarget(medDRATree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(medDRATree);
    }

    public void refreshPreviousVersionFutureTree() {
        PreviousVerFutureImpactHierarchyBean futureImpactHierarchyBean =
            (PreviousVerFutureImpactHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("PreviousVerFutureImpactHierarchyBean");
        boolean hasScope = false;
        if (nMQWizardBean.getCurrentScope() != null)
            hasScope = nMQWizardBean.getCurrentScope().equals(CSMQBean.HAS_SCOPE);

        CSMQBean.logger.info(userBean.getCaller() + " hasScope: " + hasScope);
        futureImpactHierarchyBean.init(hasScope);
        clearKeys(futureTree);
        AdfFacesContext.getCurrentInstance().addPartialTarget(futureTree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(futureTree);
    }
    
    public void refreshPreviousVersionFutureTreeNew() {
        PreviousVerFutureImpactHierarchyBean futureImpactHierarchyBean =
            (PreviousVerFutureImpactHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("PreviousVerFutureImpactHierarchyBean");        
        futureImpactHierarchyBean.rebuildTree(isShowPrevVerFutureImpactedOnly());
        clearKeys(futureTree);
        AdfFacesContext.getCurrentInstance().addPartialTarget(futureTree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(futureTree);
    }   
 
    public void setCurrentDictId(String currentDictId) {
        this.currentDictId = currentDictId;
    }

    public String getCurrentDictId() {
        return currentDictId;
    }

    

    public void setParamReleaseGroup(String paramReleaseGroup) {
        this.paramReleaseGroup = paramReleaseGroup;
    }

    public String getParamReleaseGroup() {
        return paramReleaseGroup;
    }

    
    
    public DnDAction onTreeDrop(DropEvent dropEvent) {
        showStatus(CSMQBean.MQ_MODIFIED);
        RichTreeTable sourceTree = null;
        if (dropEvent.getDragClientId().toString().indexOf("ptList") > 0) { 
            sourceTree = preferedTermSourceTree;
        } else if(dropEvent.getDragClientId().equals("pt1:tt5")){
              NMQSourceTermSearchBean nMQSourceTermSearchBean = 
                  (NMQSourceTermSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
              if (null != nMQSourceTermSearchBean){
                  sourceTree = nMQSourceTermSearchBean.getControlMultiResultsTable();
              }
        } else {
            sourceTree = hierarchySourceTree;
        }
        ///  THIS WILL PROBABLY NEED TO BE FIXED TO USE THE OTHER TREES - TES 1/26/2012
        return processDragAndDropEvent(dropEvent, sourceTree, futureTree, futureImpactHierarchyBean.getTreemodel(), Integer.parseInt(CSMQBean.FULL_NMQ_SMQ));
    }
    
    
    public void onTreeNodeDelete(ActionEvent actionEvent) { 
        showStatus(CSMQBean.MQ_MODIFIED);
        processTreeNodeDelete(futureTree, futureImpactHierarchyBean.getTreemodel());
        }

    
    public void deleteSelected(DialogEvent dialogEvent) {
        processDeleteSelected(dialogEvent, futureTree, futureImpactHierarchyBean.getTreemodel());
        }
    
    
    
    public void SOCChanged(ValueChangeEvent valueChangeEvent) {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        ArrayList list = new ArrayList(Arrays.asList(valueChangeEvent.getNewValue()));
        String retVal = "";//(String)list.get(0);;//
        
        for (Object obj : list) 
            retVal += obj.toString().trim();
        
        retVal = retVal.replace("[", "");
        retVal = retVal.replace("]", "");
        retVal = retVal.replace(',', '^');
        
        socList = retVal;
        }
    
    
    public void refreshNewPTList () {
        
        CSMQBean.logger.info(userBean.getCaller() + " CREATING NEW PT TREE");
        String selectedSocList = getSOCList();
        if (null != selectedSocList && !selectedSocList.isEmpty()){
            BindingContext bc = BindingContext.getCurrent();
            DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
            DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("NewPTsVO1Iterator");
            ViewObject newPTsVO1Iterator = dciterb.getViewObject();
            
            String groups = CSMQBean.defaultMEDSMQReleaseGroup + "^" + CSMQBean.defaultMedDRAReleaseGroup;
            
            newPTsVO1Iterator.setNamedWhereClauseParam("dictionaryName", CSMQBean.defaultBaseDictionaryShortName);
            newPTsVO1Iterator.setNamedWhereClauseParam("releaseGroups", groups);
            newPTsVO1Iterator.setNamedWhereClauseParam("contentIDs", selectedSocList);
            //DEFAULT_MEDSMQ_RELEASE_GROUP
            //DEFAULT_MEDDRA_RELEASE_GROUP
            
            CSMQBean.logger.info(userBean.getCaller() + " NewPTsVO1Iterator");
            CSMQBean.logger.info(userBean.getCaller() + " dictionaryName: " + CSMQBean.defaultBaseDictionaryShortName);
            CSMQBean.logger.info(userBean.getCaller() + " releaseGroups: " + groups);
            CSMQBean.logger.info(userBean.getCaller() + " contentIDs: " + socList);
            
            newPTsVO1Iterator.executeQuery();
            
            if ( preferedTermSourceTree.getDisclosedRowKeys() !=null)
                preferedTermSourceTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
            
            NewPTListBean newPTListBean = (NewPTListBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NewPTListBean");
            newPTListBean.init();
            AdfFacesContext.getCurrentInstance().addPartialTarget(preferedTermSourceTree);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(preferedTermSourceTree);
            CSMQBean.logger.info(userBean.getCaller() + " DONE CREATING NEW  PT TREE");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select atleast one SOC term.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
    }
    
    
    private String getSOCList () {
        String SOCListString = "";
        
        Object selProd = getImpactAnalysisUIBean().getCntlSOCList().getValue();
        if (selProd instanceof java.lang.String) {
            String temp = selProd.toString();
            temp.replace("[", "");
            temp.replace("]", "");
            SOCListString = temp;
            } 
        else {
            List selected = (List)getImpactAnalysisUIBean().getCntlSOCList().getValue();
            if (selected != null) {
                String temp = "";
                for (Object s : selected)
                    temp = temp + s + "^";

                temp.replace("[", "");
                temp.replace("]", "");

                if (temp != null & temp.length() > 0)
                    SOCListString= temp.substring(0, temp.length() - 1);
            }
        }
        return SOCListString;
    }
    
    private RichToolbar cntrlStatusBar;

    private RichImage iconMQChanged;
    private RichImage iconMQSaveError;
    private RichImage iconMQSaved;
    
    
    
    public void showStatus (int code) {
    
        this.iconMQChanged.setVisible(false);
        this.iconMQSaveError.setVisible(false);
        this.iconMQSaved.setVisible(false);
        
        switch (code) {
            case CSMQBean.MQ_SAVED:
                this.iconMQSaved.setVisible(true);
                break;
            case CSMQBean.MQ_SAVE_ERROR:
                this.iconMQSaveError.setVisible(true);
                break;
            case CSMQBean.MQ_MODIFIED:
                this.iconMQChanged.setVisible(true);
                break;
            }
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlStatusBar); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlStatusBar);
        }

    public void updateRelations(ActionEvent actionEvent) {
        
        int result = processUpdateRelations(futureTree, this.currentDictId);
        if (result == 0) showStatus(CSMQBean.MQ_SAVED);
        else showStatus(CSMQBean.MQ_SAVE_ERROR);
        refreshFutureTree(null);
        }

    

    public void setParentSOCcontentID(String parentSOCcontentID) {
        this.parentSOCcontentID = parentSOCcontentID;
    }

    public String getParentSOCcontentID() {
        this.parentSOCcontentID = getImpactAnalysisUIBean().getCntlSOCList().getValue().toString();
        return parentSOCcontentID;
    }

    public void setActivationGroups(String activationGroups) {
        this.activationGroups = activationGroups;
    }

    public String getActivationGroups() {
        return activationGroups;
    }
   


    public void clearMedDRATree () {
        RichTreeTable targetTree = medDRATree;
        // Clear keys
        if (targetTree != null && targetTree.getDisclosedRowKeys()!=null )
            targetTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        
        // REQUERY 
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("MedDRAImpactVO1Iterator1");
        ViewObject vo = dciterb.getViewObject();

        vo.setNamedWhereClauseParam("dictContentID", 0);
        vo.executeQuery();
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(targetTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(targetTree);
        }

   /*  public void showNewPTs(ActionEvent actionEvent) {
        UIComponent source = (UIComponent) actionEvent.getSource();
        refreshNewPTList ("ALL");
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, source)
              .add(RichPopup.PopupHints.HintTypes.HINT_LAUNCH_ID, source)
              .add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, 
                   RichPopup.PopupHints.AlignTypes.ALIGN_BEFORE_START);
        getImpactAnalysisUIBean().getNewPreferedTermsPopup().show(hints);
    } */

   

    public void reviewed(DialogEvent dialogEvent) {        
        Hashtable result = NMQUtils.changeState(this.currentDictId, CSMQBean.IA_STATE_REVIEWED, userBean.getCurrentUser(), userBean.getUserRole(), null, null, cSMQBean.getDefaultDraftReleaseGroup());
        if (result != null) {
            this.currentState = (String)result.get("STATE");
            this.currentReasonForApproval = (String)result.get("REASON");
            nMQWizardBean.setCurrentState(this.getCurrentState());
            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getPromotionToolBar());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getPromotionToolBar());
            }
        }

    public void approve(DialogEvent dialogEvent) {        
        Hashtable result = NMQUtils.changeState(this.currentDictId, CSMQBean.IA_STATE_APPROVED, userBean.getCurrentUser(), userBean.getUserRole(), null, null, cSMQBean.getDefaultDraftReleaseGroup());
        if (result != null) {
            this.currentState = (String)result.get("STATE");
            this.currentReasonForApproval = (String)result.get("REASON");
            nMQWizardBean.setCurrentState(this.getCurrentState());
            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getPromotionToolBar());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getPromotionToolBar());
            
            AdfFacesContext.getCurrentInstance().addPartialTarget(getFutureTree());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getFutureTree());
            }
        }

    public void delete(DialogEvent dialogEvent) {
        if (NMQUtils.delete(this.currentDictId, CSMQBean.defaultDraftReleaseGroup)) {
            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getPromotionToolBar());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getPromotionToolBar());
            }
        }

    public void demoteToPendingIA (DialogEvent dialogEvent)  {
        Hashtable result = NMQUtils.changeState(this.currentDictId, CSMQBean.STATE_PENDING_IMPACT_ASSESSMENT, userBean.getCurrentUser(), userBean.getUserRole(), null, null, cSMQBean.getDefaultDraftReleaseGroup());
        if (result != null) {
            this.currentState = (String)result.get("STATE");
            this.currentReasonForApproval = (String)result.get("REASON");
            nMQWizardBean.setCurrentState(this.getCurrentState());
            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getPromotionToolBar());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getPromotionToolBar());
            }
        }
    
   
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void existingPrimLinkFlagChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals(valueChangeEvent.getOldValue())) return; // it didn't change
        refreshMedDRATree();
    }

    public void futurePrimLinkFlagChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals(valueChangeEvent.getOldValue())) return; // it didn't change
        refreshFutureTree(null);
    }

    public void setParamFutureShowNonImpacted(String paramFutureImpactedOnly) {
        this.paramFutureShowNonImpacted = paramFutureImpactedOnly;
    }

    public String getParamFutureShowNonImpacted() {
        return paramFutureShowNonImpacted;
    }

    public void setParamFutureSort(String paramFutureSort) {
        this.paramFutureSort = paramFutureSort;
    }

    public String getParamFutureSort() {
        return paramFutureSort;
    }

    public void futureParamsChanged(ValueChangeEvent valueChangeEvent) {
        String source = valueChangeEvent.getComponent().getId();
        
        if (source.equals("paramFutureSIO")) {
            paramFutureShowNonImpacted = (Boolean)valueChangeEvent.getNewValue() ? CSMQBean.FALSE : CSMQBean.TRUE;
            }
        if (source.equals("paramFutureSPO")) {
            paramFuturePrimaryOnly = (Boolean)valueChangeEvent.getNewValue() ? CSMQBean.TRUE : CSMQBean.FALSE;
            }
        if (source.equals("paramFutureSort")) {
            paramFutureSort = ((String)valueChangeEvent.getNewValue());
            }
        if (source.equals("paramFutureScope")) {
            paramFutureScope = ((String)valueChangeEvent.getNewValue());
            }
    
        //refreshFutureTree(null); 
        }


    public void existingParamsChanged(ValueChangeEvent valueChangeEvent) {
        String source = valueChangeEvent.getComponent().getId();
        
        if (source.equals("paramMedDRASIO")) {
            paramMedDRAShowNonImpacted = (Boolean)valueChangeEvent.getNewValue() ? CSMQBean.FALSE : CSMQBean.TRUE;
            }
        if (source.equals("paramMedDRASPO")) {
            paramMedDRAPrimaryOnly = (Boolean)valueChangeEvent.getNewValue() ? CSMQBean.TRUE : CSMQBean.FALSE;
            }
        if (source.equals("paramMedDRASort")) {
            paramMedDRASort = ((String)valueChangeEvent.getNewValue());
            }
        if (source.equals("paramMedDRAScope")) {
            paramMedDRAScope = ((String)valueChangeEvent.getNewValue());
            }
    
            refreshMedDRATree(); 
        }
    

    public void setParamFutureScope(String paramFutureScope) {
        this.paramFutureScope = paramFutureScope;
    }

    public String getParamFutureScope() {
        return paramFutureScope;
    }

    public void setParamFuturePrimaryOnly(String paramFuturePrimaryOnly) {
        this.paramFuturePrimaryOnly = paramFuturePrimaryOnly;
    }

    public String getParamFuturePrimaryOnly() {
        return paramFuturePrimaryOnly;
    }

    public void setParamMedDRAShowNonImpacted(String paramMedDRAImpactedOnly) {
        this.paramMedDRAShowNonImpacted = paramMedDRAImpactedOnly;
    }

    public String getParamMedDRAShowNonImpacted() {
        return paramMedDRAShowNonImpacted;
    }

    public void setParamMedDRASort(String paramMedDRASort) {
        this.paramMedDRASort = paramMedDRASort;
    }

    public String getParamMedDRASort() {
        return paramMedDRASort;
    }

    public void setParamMedDRAPrimaryOnly(String paramMedDRAPrimaryOnly) {
        this.paramMedDRAPrimaryOnly = paramMedDRAPrimaryOnly;
    }

    public String getParamMedDRAPrimaryOnly() {
        return paramMedDRAPrimaryOnly;
    }

    public void setParamMedDRAScope(String paramMedDRAScope) {
        this.paramMedDRAScope = paramMedDRAScope;
    }

    public String getParamMedDRAScope() {
        return paramMedDRAScope;
    }

    public void setCurrentTermName(String currentTerm) {
        this.currentTermName = currentTerm;
    }

    public String getCurrentTermName() {
        return currentTermName;
    }

    public void setAllGroups(String allGroups) {
        this.allGroups = allGroups;
    }

    public String getAllGroups() {
        return allGroups;
    }

    public void hierarchyPopUpFetch(PopupFetchEvent popupFetchEvent) {
        NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
        //nMQSourceTermSearchBean.refreshLevelList(applicationBean.getDefaultFilterDictionaryShortName());
       if (paramFutureShowNonImpacted.equals(CSMQBean.FALSE)) {
            paramFutureShowNonImpacted = CSMQBean.TRUE;
            futureShowImpacted = false;
            refreshFutureTree(null);     
            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlFutureMenu());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlFutureMenu());
            }
    }

    public void newPTPopUpFetch(PopupFetchEvent popupFetchEvent) {
       //here
       
       if (paramFutureShowNonImpacted.equals(CSMQBean.FALSE)) {
            paramFutureShowNonImpacted = CSMQBean.TRUE;
            futureShowImpacted = false;
            refreshFutureTree(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlFutureMenu());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlFutureMenu());
            }
    }

    public void setFutureShowImpacted(boolean futureShowImpacted) {
        this.futureShowImpacted = futureShowImpacted;
    }

    public boolean getFutureShowImpacted() {
        return futureShowImpacted;
    }

    public void setRenderSave(boolean renderSave) {
        this.renderSave = renderSave;
    }

    public boolean isRenderSave() {
        this.renderSave = false;
        
        //if (this.mqType == CSMQBean.NMQ && (
        
        
        if (this.currentState != null && (this.currentState.equals(CSMQBean.STATE_PENDING_IMPACT_ASSESSMENT) ||
            this.currentState.equals(CSMQBean.IA_STATE_APPROVED) ||
            this.currentState.equals(CSMQBean.IA_STATE_REVIEWED)
            )) this.renderSave = true;
        return this.renderSave;
    }

    public void nodeChanged(ValueChangeEvent valueChangeEvent) {
    
        
        /* soc3 = scope
         * soc2 = category
         * ot6 = weight
         */
        
        Object o = valueChangeEvent;
        RowKeySet rks = futureTree.getSelectedRowKeys();
        Iterator rksIterator = rks.iterator();
        List key = (List)rksIterator.next();
        
        int rowKey = Integer.parseInt(key.get(1).toString());
        GenericTreeNode node = (GenericTreeNode)futureTree.getRowData(rowKey);
        
        // TEST TO SEE IF IT'S ALREADY BEEN EDITED
        GenericTreeNode oldNode = updates.get(node.getPrikey());
        if (oldNode != null) node = oldNode;
        
        // UPDATE THE NODE (EITHER THE OLD OR THE NEW) WITH THE CHANGED VALUES
        if (valueChangeEvent.getSource().toString().indexOf("soc3") > -1)
            node.setFormattedScope(valueChangeEvent.getNewValue().toString());
        
        if (valueChangeEvent.getSource().toString().indexOf("soc2") > -1)
            node.setTermCategory(valueChangeEvent.getNewValue().toString());
        
        if (valueChangeEvent.getSource().toString().indexOf("ot6") > -1)
            node.setTermWeight(valueChangeEvent.getNewValue().toString());
        
        if (oldNode == null)  // ADD IT SINCE IT'S NOT THERE ALREADY
            updates.put(node.getPrikey(), node);
    }

    public void refreshMedDRATree(ActionEvent actionEvent) {
        refreshMedDRATree();
    }
    
    public String doSearch() {
        // Add event code here...
        return null;
    }

    public void setCntrlStatusBar(RichToolbar cntrlStatusBar) {
        this.cntrlStatusBar = cntrlStatusBar;
    }

    public RichToolbar getCntrlStatusBar() {
        return cntrlStatusBar;
    }

    public void setIconMQChanged(RichImage iconMQChanged) {
        this.iconMQChanged = iconMQChanged;
    }

    public RichImage getIconMQChanged() {
        return iconMQChanged;
    }

    public void setIconMQSaveError(RichImage iconMQSaveError) {
        this.iconMQSaveError = iconMQSaveError;
    }

    public RichImage getIconMQSaveError() {
        return iconMQSaveError;
    }
    
    public void setIconMQSaved(RichImage iconMQSaved) {
        this.iconMQSaved = iconMQSaved;
    }

    public RichImage getIconMQSaved() {
        return iconMQSaved;
    }

    public void updateImpactSearchResults(ActionEvent actionEvent) {
        //this.doSearch(null);
    }

    public void impactSearchResultsPopUp(PopupFetchEvent popupFetchEvent) {
        clearSearch();
    }

    public void refreshNewPTList(ActionEvent actionEvent) {
        refreshNewPTList ();
    }

    public void setSocList(Object socList) {
        this.socList = socList;
    }

    public Object getSocList() {
        return socList;
    }

    public void setNewPTDisclosedKeys(Object newPTDisclosedKeys) {
        this.newPTDisclosedKeys = newPTDisclosedKeys;
    }

    public Object getNewPTDisclosedKeys() {
        return newPTDisclosedKeys;
    }
    
    public void setPreferedTermSourceTree(RichTreeTable sourceTree) {
        this.preferedTermSourceTree = sourceTree;
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
    public void setMedDRATree(RichTreeTable medDRATree) {
        this.medDRATree = medDRATree;
    }

    public RichTreeTable getMedDRATree() {
        return medDRATree;
    }

    public void setFutureTree(RichTreeTable futureTree) {
        this.futureTree = futureTree;
    }

    public RichTreeTable getFutureTree() {
        return futureTree;
    }
    
    public void viewRowSelected(SelectionEvent selectionEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE ****");
        String isViewPreviousFlow =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("isViewPreviousFlow");
        CSMQBean.logger.info(userBean.getCaller() + " isViewPreviousFlow =" + isViewPreviousFlow);
        String source = ((RichTable)selectionEvent.getSource()).getId();
        String iterator = null;
        if (source.equalsIgnoreCase("T_CMQ_Y")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_CMQ_Y" : "ImpactSearchListVO_CMQ_Y";
            this.showImpact = CSMQBean.TRUE;
            this.mqType = CSMQBean.CMQ;
            this.mode = CSMQBean.MODE_BROWSE_SEARCH;
        } else if (source.equalsIgnoreCase("T_CMQ_N")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_CMQ_N" : "ImpactSearchListVO_CMQ_N";
            this.showImpact = CSMQBean.FALSE;
            this.mqType = CSMQBean.CMQ;
            this.mode = CSMQBean.MODE_BROWSE_SEARCH;
        } else if (source.equalsIgnoreCase("TBL_MQ_N")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_MQ_N" : "ImpactSearchListVO_MQ_N";
            this.showImpact = CSMQBean.FALSE;
            this.mqType = CSMQBean.SMQ;
            this.mode = CSMQBean.MODE_BROWSE_SEARCH;
        } else if (source.equalsIgnoreCase("TBL_MQ_Y")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_MQ_Y" : "ImpactSearchListVO_MQ_Y";
            this.showImpact = CSMQBean.TRUE;
            this.mqType = CSMQBean.SMQ;
            this.mode = CSMQBean.MODE_BROWSE_SEARCH;
        } else if (source.equalsIgnoreCase("TBL_NMQ_N")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_NMQ_N" : "ImpactSearchListVO_NMQ_N";
            this.showImpact = CSMQBean.FALSE;
            this.mqType = CSMQBean.NMQ;
            this.mode = CSMQBean.MODE_BROWSE_SEARCH;
        } else if (source.equalsIgnoreCase("TBL_NMQ_Y")) {
            iterator =
                    "Y".equals(isViewPreviousFlow) ? "PreviousVerImpactSearchListVO_NMQ_Y" : "ImpactSearchListVO_NMQ_Y";
            this.showImpact = CSMQBean.TRUE;
            this.mqType = CSMQBean.NMQ;
            this.mode = CSMQBean.MODE_BROWSE_SEARCH;
        }
        resolveMethodExpression("#{bindings." + iterator + ".collectionModel.makeCurrent}", null,
                                new Class[] { SelectionEvent.class }, new Object[] { selectionEvent });
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

        this.currentDictId = Utils.getAsString(row, "DictContentId");
        this.currentContentCode = Utils.getAsString(row, "DictContentCode");
        this.currentTermName = Utils.getAsString(row, "Term");
        this.activationGroups = CSMQBean.defaultMedDRAReleaseGroup;
        this.currentState = Utils.getAsString(row, "WorkflowState");
        String smqNmq = Utils.getAsString(row, "NmqSmq");
        String queryLevel = Utils.getAsString(row, "DefLevelsShortName");

        nMQWizardBean.setFooterInfo(this.currentTermName);

        String status = Utils.getAsString(row, "Status");

        CSMQBean.logger.info(userBean.getCaller() + " currentDictId: " + currentDictId);

        //       Hashtable result = NMQUtils.setDefaultIAState(this.currentDictId, smqNmq, userBean.getCurrentUser(), userBean.getCurrentUserRole());
        //
        //        if (result != null) {
        //           this.setCurrentState((String)result.get("STATE"));
        //            }

        // INF NOTES STUFF
        nMQWizardBean.setCurrentDictContentID(this.currentDictId);
        //nMQWizardBean.setCurrentState(this.getCurrentState());

        nMQWizardSearchBean.setCurrentStatus(status);
        nMQWizardSearchBean.setCurrentDictContentID(this.currentDictId);
        if (null != queryLevel && !queryLevel.isEmpty()) {
            nMQWizardSearchBean.setParamLevel(queryLevel.substring(queryLevel.length() - 1));
            CSMQBean.logger.info("queryLevel >>>" + nMQWizardSearchBean.getParamLevel());
        }
        /*String termProduct = Utils.getAsString(row, "Value3");
            if (termProduct != null) {
                termProduct = termProduct.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
                Collections.addAll(nMQWizardBean.getProductList(), termProduct.split("%"));
                System.out.println("Term Product list >>>"+nMQWizardBean.getProductList());
            }
            String termGroup = Utils.getAsString(row, "Value2");
            if (termGroup != null) {
                termGroup = termGroup.replace(CSMQBean.DEFAULT_DELIMETER_CHAR, '%');
                Collections.addAll(nMQWizardBean.getMQGroupList(), termGroup.split("%"));
                System.out.println("Term Group list >>>"+nMQWizardBean.getMQGroupList());
            }*/
        nMQWizardBean.setIsNMQ(smqNmq.equalsIgnoreCase("NMQ"));
        nMQWizardBean.setIsSMQ(smqNmq.equalsIgnoreCase("SMQ"));
        nMQWizardSearchBean.initForImpactAnalysis(currentContentCode, currentDictId, activationGroups);
        nMQWizardBean.setCurrentState(this.getCurrentState());
        //clearSearch();


        getImpactAnalysisUIBean().getImpactSearchPopUp().cancel();

        if (medDRATree != null && medDRATree.getDisclosedRowKeys() != null)
            medDRATree.getDisclosedRowKeys().clear(); //to resolve NoRowAvailableException

        if (futureTree != null && futureTree.getDisclosedRowKeys() != null)
            futureTree.getDisclosedRowKeys().clear(); //to resolve NoRowAvailableException

        getImpactAnalysisUIBean().getCntrlExportButton().setDisabled(false);
        getImpactAnalysisUIBean().getCntrlExportDisplayedButtonLeft().setDisabled(false);
        getImpactAnalysisUIBean().getCntrlExportDisplayedButtonRight().setDisabled(false);
        getImpactAnalysisUIBean().getCntrlRefreshButton().setDisabled(false);
        getImpactAnalysisUIBean().getCntrlExportButton().setDisabled(false);

        AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getPromotionToolBar());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getPromotionToolBar());

        AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlImpactLeftToolbar());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlImpactLeftToolbar());

        AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlImpactRightToolbar());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlImpactRightToolbar());

//        AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getCntrlTrain());
//        AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getCntrlTrain());
        AdfFacesContext.getCurrentInstance().addPartialTarget(getImpactAnalysisUIBean().getPromotionToolBar());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(getImpactAnalysisUIBean().getPromotionToolBar());
        //clear the selected row
        //RowKeySet rks = impactAnalysisUIBean.getCntrlImpactSearchResults().getSelectedRowKeys();
        //rks.clear();


        refreshTrees();
    }
    /*
    private HashMap changeState(String dictContentIDs, String state, String currentUser, String currentUserRole,
                                oracle.jbo.domain.Date dueDate, String comment, String activationGroup) {
        HashMap retVal;
        FacesMessage msg;
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("changeState");
        ob.getParamsMap().put("dictContentIDs", dictContentIDs);
        ob.getParamsMap().put("state", state);
        ob.getParamsMap().put("currentUser", currentUser);
        ob.getParamsMap().put("currentUserRole", currentUserRole);
        ob.getParamsMap().put("dueDate", dueDate);
        ob.getParamsMap().put("comment", comment);
        ob.getParamsMap().put("activationGroup", activationGroup);

        retVal = (HashMap)ob.execute();
        if (null != retVal) {
            String retCode = (String)retVal.get("RETURN_CODE");
            if (null != retCode && retCode.equalsIgnoreCase(CSMQBean.SUCCESS)) {
                msg =
new FacesMessage(FacesMessage.SEVERITY_INFO, "MedDRA Query State Changed Successfully to " + state, null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                String messageText = "";
                if (retCode.equalsIgnoreCase(CSMQBean.INVALID_PROMOTION_ERROR)) {
                    messageText = CSMQBean.getProperty("INVALID_PROMOTION_ERROR");
                } else if (retCode.equalsIgnoreCase(CSMQBean.INVALID_PROMOTION_SEQUENCE_ERROR)) {
                    messageText = CSMQBean.getProperty("INVALID_PROMOTION_SEQUENCE_ERROR");
                } else if (retCode.equalsIgnoreCase(CSMQBean.PROMOTION_DEPENDENCY_ERROR)) {
                    messageText = CSMQBean.getProperty("PROMOTION_DEPENDENCY_ERROR");
                } else if (retCode.equalsIgnoreCase(CSMQBean.INVALID_STATE_CHANGE_ERROR)) {
                    messageText = CSMQBean.getProperty("INVALID_STATE_CHANGE_ERROR");
                } else if (retCode.equalsIgnoreCase(CSMQBean.MUST_BE_NMQ_OR_SMQ_ERROR)) {
                    messageText = CSMQBean.getProperty("MUST_BE_NMQ_OR_SMQ_ERROR");
                } else if (retCode.equalsIgnoreCase(CSMQBean.GENERIC_ACTIVATION_ERROR)) {
                    messageText = CSMQBean.getProperty("GENERIC_ACTIVATION_ERROR");
                } else if (retCode.equalsIgnoreCase(CSMQBean.DATABASE_CONFIGURATION_ERROR)) {
                    messageText = CSMQBean.getProperty("DATABASE_CONFIGURATION_ERROR");
                } else if (retCode.equalsIgnoreCase(CSMQBean.INVALID_STATE_CHANGE_FROM_PENDING_TO_DRAFT_ERROR)) {
                    messageText = CSMQBean.getProperty("INVALID_STATE_CHANGE_FROM_PENDING_TO_DRAFT_ERROR");
                } else { // it's something else
                    messageText = "Error occurred during state change to " + state;
                }
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
        }
        return retVal;
    }*/

    public void onImpartSearchAction(ActionEvent actionEvent) {
        System.out.println("Start Exec onImpartSearchAction() ");
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("onPreviousVerImpactSearch1");
        ob.getParamsMap().put("searchLevelStr", getSearchLevelStr());
        ob.getParamsMap().put("searchTermStr", getSearchTermStr());
        ob.getParamsMap().put("searchCodeStr", getSearchCodeStr());
        ob.getParamsMap().put("status", getSearchStatusStr());
        ob.getParamsMap().put("state", getSearchStateStr());
        ob.getParamsMap().put("searchProduct", getProductList());
        ob.execute();
        System.out.println("End of Exec onImpartSearchAction() ");
    }
    
    public void onCurrImpartSearchAction(ActionEvent actionEvent) {
        System.out.println("Start Exec onImpartSearchAction() ");
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("onCurrentVerImpactSearch");
        ob.getParamsMap().put("searchLevelStr", getSearchLevelStr());
        ob.getParamsMap().put("searchTermStr", getSearchTermStr());
        ob.getParamsMap().put("searchCodeStr", getSearchCodeStr());
        ob.getParamsMap().put("status", getSearchStatusStr());
        ob.getParamsMap().put("state", getSearchStateStr());
        ob.getParamsMap().put("searchProduct", getProductList());
        ob.execute();
        System.out.println("End of Exec onImpartSearchAction() ");
    }
    
    public void onImpactAssesmentSearchAction(ActionEvent actionEvent) {
        System.out.println("Start Exec onImpartSearchAction() ");
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("onCurrentVerImpactSearch");
        ob.getParamsMap().put("searchLevelStr", getSearchLevelStr());
        ob.getParamsMap().put("searchTermStr", getSearchTermStr());
        ob.getParamsMap().put("searchCodeStr", getSearchCodeStr());
        ob.getParamsMap().put("status", getSearchStatusStr());
        ob.getParamsMap().put("state", getSearchStateStr());
        ob.getParamsMap().put("searchProduct", getProductList());
        ob.execute();
        System.out.println("End of Exec onImpartSearchAction() ");
    }

    public void setShowPrevVerCurrentImpactedOnly(boolean showPrevVerCurrentImpactedOnly) {
        this.showPrevVerCurrentImpactedOnly = showPrevVerCurrentImpactedOnly;
    }

    public boolean isShowPrevVerCurrentImpactedOnly() {
        return showPrevVerCurrentImpactedOnly;
    }

    public void setShowPrevVerFutureImpactedOnly(boolean showPrevVerFutureImpactedOnly) {
        this.showPrevVerFutureImpactedOnly = showPrevVerFutureImpactedOnly;
    }

    public boolean isShowPrevVerFutureImpactedOnly() {
        return showPrevVerFutureImpactedOnly;
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

    public void setCtrlNMQStatus(RichSelectOneChoice ctrlNMQStatus) {
        this.ctrlNMQStatus = ctrlNMQStatus;
    }

    public RichSelectOneChoice getCtrlNMQStatus() {
        return ctrlNMQStatus;
    }

    public void setSearchStateStr(String searchStateStr) {
        this.searchStateStr = searchStateStr;
    }

    public String getSearchStateStr() {
        return searchStateStr;
    }

    public void setSearchStatusStr(String searchStatusStr) {
        this.searchStatusStr = searchStatusStr;
    }

    public String getSearchStatusStr() {
        return searchStatusStr;
    }
    
    public void downloadSearchReport(FacesContext facesContext, OutputStream outputStream) {
            try {
                HSSFWorkbook workbook = new HSSFWorkbook();
                workbook = createWorkBookTab("Impacted CMQs","Impacted CMQs Search Results","PreviousVerImpactSearchListVO_CMQ_YIterator", workbook);
                workbook = createWorkBookTab("Impacted NMQs","Impacted NMQs Search Results","PreviousVerImpactSearchListVO_NMQ_YIterator", workbook);
                workbook = createWorkBookTab("Impacted SMQs","Impacted SMQs Search Results","PreviousVerImpactSearchListVO_MQ_YIterator", workbook);
                workbook = createWorkBookTab("Non-impacted CMQs","Non-impacted CMQs Search Results","PreviousVerImpactSearchListVO_CMQ_NIterator", workbook);
                workbook = createWorkBookTab("Non-impacted NMQs","Non-impacted NMQs Search Results","PreviousVerImpactSearchListVO_NMQ_NIterator", workbook);
                workbook = createWorkBookTab("Non-impacted SMQs","Non-impacted SMQs Search Results","PreviousVerImpactSearchListVO_MQ_NIterator", workbook);
                
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    public void downloadSearchReport1(FacesContext facesContext, OutputStream outputStream) {
            try {
                HSSFWorkbook workbook = new HSSFWorkbook();
                workbook = createWorkBookTab("Impacted CMQs","Impacted CMQs Search Results","ImpactSearchListVO_CMQ_YIterator", workbook);
                workbook = createWorkBookTab("Impacted NMQs","Impacted NMQs Search Results","ImpactSearchListVO_NMQ_YIterator", workbook);
                workbook = createWorkBookTab("Impacted SMQs","Impacted SMQs Search Results","ImpactSearchListVO_MQ_YIterator", workbook);
                workbook = createWorkBookTab("Non-impacted CMQs","Non-impacted CMQs Search Results","ImpactSearchListVO_CMQ_NIterator", workbook);
                workbook = createWorkBookTab("Non-impacted NMQs","Non-impacted NMQs Search Results","ImpactSearchListVO_NMQ_NIterator", workbook);
                workbook = createWorkBookTab("Non-impacted SMQs","Non-impacted SMQs Search Results","ImpactSearchListVO_MQ_NIterator", workbook);
                
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public void downloadSearchReport2(FacesContext facesContext, OutputStream outputStream) {
            try {
                HSSFWorkbook workbook = new HSSFWorkbook();
                workbook = createWorkBookTab("Impacted CMQs","Impacted CMQs Search Results","ImpactSearchListVO_CMQ_YIterator", workbook);
                workbook = createWorkBookTab("Impacted NMQs","Impacted NMQs Search Results","ImpactSearchListVO_NMQ_YIterator", workbook);
                workbook = createWorkBookTab("Impacted SMQs","Impacted SMQs Search Results","ImpactSearchListVO_MQ_YIterator", workbook);
                workbook = createWorkBookTab("Non-impacted CMQs","Non-impacted CMQs Search Results","ImpactSearchListVO_CMQ_NIterator", workbook);
                workbook = createWorkBookTab("Non-impacted NMQs","Non-impacted NMQs Search Results","ImpactSearchListVO_NMQ_NIterator", workbook);
                workbook = createWorkBookTab("Non-impacted SMQs","Non-impacted SMQs Search Results","ImpactSearchListVO_MQ_NIterator", workbook);
                
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    private HSSFWorkbook createWorkBookTab(String sheetName, String tableName, String iteratorName, HSSFWorkbook workbook){

        HSSFSheet worksheet = workbook.createSheet(sheetName);
        HSSFRow excelrow = null;

        int i = 0;
        int colCount = 0;

        excelrow = (HSSFRow) worksheet.createRow((short) i);
        HSSFCell cellA1 = excelrow.createCell((short) 0);
        cellA1.setCellValue(tableName);

        i++;
        i++;

        int k = i;

        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer) bc.getCurrentBindingsEntry();
        DCIteratorBinding dcIter = (DCIteratorBinding) binding.get(iteratorName);


        RowSetIterator rs = dcIter.getViewObject().createRowSetIterator(null);

        while (rs.hasNext()) {
            Row row = rs.next();
            //print header on first row in excel
            if (i == k) {
                excelrow = (HSSFRow) worksheet.createRow((short) i);
                
                cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("Term");
                
                cellA1 = excelrow.createCell((short) 1);
                cellA1.setCellValue("MQ Code");
                
                cellA1 = excelrow.createCell((short) 2);
                cellA1.setCellValue("Status");                     
                colCount = 3;
            }

            ++i;

            excelrow = worksheet.createRow((short) i);
            
            HSSFCell cell = excelrow.createCell(0);
            cell.setCellValue(row.getAttribute("Term") + "");
            
            cell = excelrow.createCell(1);
            cell.setCellValue(row.getAttribute("DictContentCode")+ "");
            
            cell = excelrow.createCell(2);
            cell.setCellValue(row.getAttribute("Status")+ "");

        }
        
        i++;
        i++;
        excelrow = (HSSFRow) worksheet.createRow((short) i);
        cellA1 = excelrow.createCell((short) 0);
        cellA1.setCellValue("Row Count");
        HSSFCell cellA2 = excelrow.createCell((short) 1);
        cellA2.setCellValue(dcIter.getEstimatedRowCount());
        
        //worksheet.createFreezePane(0, 1, 0, 1);

        for (int x = 0; x < colCount; x++) {
            worksheet.autoSizeColumn(x);
        }
        if(colCount == 0){
            worksheet.autoSizeColumn(0);
        }
        
        return workbook;
    }
}
