package com.dbms.csmq.view.backing.NMQ;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisUIBean;
import com.dbms.csmq.view.backing.impact.ImpactSearchBean;
import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.hierarchy.HierarchyAccessor;
import com.dbms.csmq.view.hierarchy.HierarchySearchResultsBean;
import com.dbms.csmq.view.hierarchy.TermHierarchyBean;
import com.dbms.csmq.view.hierarchy.TermHierarchySourceBean;
import com.dbms.util.ADFUtils;
import com.dbms.util.Utils;

import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DropEvent;
import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;


public class NMQSourceTermSearchBean extends HierarchyAccessor {

    private String paramDictionaryType = "NULL";
    private String paramDictionary = "NULL";
    private String paramLevel = "NULL";
    private String paramReleaseGroup = "NULL";
    private String paramTerm = "NULL";
    private String paramScope = "-1";
    private String paramSort = "TERM";
    private String paramPrimLinkFlag = CSMQBean.TRUE;
    private String paramNarrowScopeOnly = CSMQBean.FALSE;
    
    private boolean historicSearch = false;
    private boolean addRelationsSearch = false;
    private boolean impactSearch = false;
    private boolean multiSearch = false;
    
    boolean showMedDRASelItems = true; // this is the default
    boolean showNMQSelItems = false;
    boolean showSMQSelItems = false;
    
    private String currentDictId;
    

    private NMQWizardBean nMQWizardBean;
    TermHierarchySourceBean termHierarchySourceBean = (TermHierarchySourceBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchySourceBean");
    TermHierarchyBean termHierarchyBean = (TermHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
    NMQSourceTermSearchUIBean nMQSourceTermSearchUIBean = null;
    private boolean hasScope = false;
    private String paramExtension;
    private RichSelectOneChoice controlDictionaryType;
    private RichSelectOneChoice controlLevel;

    private RichTreeTable multiHierarchySourceTree;
    private RichTreeTable controlMultiResultsTable;
    private boolean meddraSearch = true;
    private boolean showHierarchyChildTree = false;

    public void doSearch(ActionEvent actionEvent) {

        
        
        
        paramScope = "-1";
        if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT){
            CSMQBean.logger.info(userBean.getCaller() + " *** PRFORMING IA Hierarchy SEARCH ***");
        } else {
            CSMQBean.logger.info(userBean.getCaller() + " *** PRFORMING SEARCH ***");
        }
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: HierarchySourceTermSearchVO1Iterator");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HierarchySourceTermSearchVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        String activationGroups = CSMQBean.WILDCARD;
//        if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT && isMeddraSearch()){
//            activationGroups = CSMQBean.defaultMedDRAReleaseGroup + "^" + CSMQBean.defaultDraftReleaseGroup;
//        }
        String paramTermVal = getParamTerm();
        if (null != paramTermVal && !paramTermVal.isEmpty()){
           paramTermVal = paramTermVal.replace("'","\''");
        }
        vo.setNamedWhereClauseParam("startDate", "");
        vo.setNamedWhereClauseParam("endDate", "");
        vo.setNamedWhereClauseParam("term", paramTermVal);
        vo.setNamedWhereClauseParam("activityStatus", "ALL");
        vo.setNamedWhereClauseParam("dictShortName", getParamDictionary());
        vo.setNamedWhereClauseParam("releaseStatus", CSMQBean.BOTH_ACTIVITY_STATUSES);
        vo.setNamedWhereClauseParam("activationGroup", activationGroups);
        vo.setNamedWhereClauseParam("MQGroup", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("product", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("MQCode", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("MQCriticalEvent", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("uniqueIDsOnly", CSMQBean.TRUE);
        vo.setNamedWhereClauseParam("filterForUser",  CSMQBean.FALSE);
        vo.setNamedWhereClauseParam("currentUser", nMQWizardBean.getCurrentUser());
        vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_OFF);
        vo.setNamedWhereClauseParam("showNarrowScpOnly", getParamNarrowScopeOnly());
        vo.setNamedWhereClauseParam("MQScope", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("pState", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("pUserRole", "MQM");
        vo.setNamedWhereClauseParam("levelName", getParamLevel());
        CSMQBean.logger.info(userBean.getCaller() + " startDate: " + "");
        CSMQBean.logger.info(userBean.getCaller() + " endDate: " + "");
        CSMQBean.logger.info(userBean.getCaller() + " term: " + paramTermVal);
        CSMQBean.logger.info(userBean.getCaller() + " activityStatus: " + "ALL");
        CSMQBean.logger.info(userBean.getCaller() + " dictShortName: " + getParamDictionary());
        CSMQBean.logger.info(userBean.getCaller() + " releaseStatus: " + CSMQBean.BOTH_ACTIVITY_STATUSES);
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + activationGroups);
        CSMQBean.logger.info(userBean.getCaller() + " MQGroup: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " product: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " MQCode: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " MQCriticalEvent: " + "%");
        CSMQBean.logger.info(userBean.getCaller() + " uniqueIDsOnly: " + CSMQBean.TRUE);
        CSMQBean.logger.info(userBean.getCaller() + " filterForUser: " +  CSMQBean.FALSE);
        CSMQBean.logger.info(userBean.getCaller() + " currentUser: " + nMQWizardBean.getCurrentUser());
        CSMQBean.logger.info(userBean.getCaller() + " killSwitch: " + CSMQBean.KILL_SWITCH_OFF);
        CSMQBean.logger.info(userBean.getCaller() + " showNarrowScpOnly: " + getParamNarrowScopeOnly());
        CSMQBean.logger.info(userBean.getCaller() + " MQScope: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " pState: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " pUserRole: " + "MQM");
        CSMQBean.logger.info(userBean.getCaller() + " levelName: " + getParamLevel());
        
        if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT){
            vo.setNamedWhereClauseParam("pMode", CSMQBean.MODE_VIEW_VERSION_IMPACT);
            CSMQBean.logger.info(userBean.getCaller() + " IA hierarchy search query: " + vo.getQuery());
        }
        vo.executeQuery();
        
        nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(nMQSourceTermSearchUIBean.getControlResultsTable());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(nMQSourceTermSearchUIBean.getControlResultsTable());

        
    }
    
    
    
    public void doMultiSearch(ActionEvent actionEvent) {
        
        paramScope = "-1";
         
        CSMQBean.logger.info(userBean.getCaller() + " *** PRFORMING Hierarchy SEARCH ***");
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: HierarchySourceTermSearchVO1Iterator");
        CSMQBean.logger.info(userBean.getCaller() + " Mode:: " + getCurrentMode());
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HierarchySourceTermSearchVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        
        vo.setNamedWhereClauseParam("startDate", "");
        vo.setNamedWhereClauseParam("endDate", "");
        String paramTermVal = getParamTerm();
        if (null != paramTermVal && !paramTermVal.isEmpty()){
           paramTermVal = paramTermVal.replace("'","\''");
        }
        String activationGroup = CSMQBean.WILDCARD;
        vo.setNamedWhereClauseParam("term", paramTermVal);
        vo.setNamedWhereClauseParam("activityStatus", "ALL");
        vo.setNamedWhereClauseParam("dictShortName", getParamDictionary());
        vo.setNamedWhereClauseParam("releaseStatus", CSMQBean.BOTH_ACTIVITY_STATUSES);
        vo.setNamedWhereClauseParam("activationGroup",CSMQBean.WILDCARD );
        vo.setNamedWhereClauseParam("MQGroup", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("product", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("MQCode", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("MQCriticalEvent", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("uniqueIDsOnly", CSMQBean.TRUE);
        vo.setNamedWhereClauseParam("filterForUser",  CSMQBean.FALSE);
        vo.setNamedWhereClauseParam("currentUser", nMQWizardBean.getCurrentUser());
        vo.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_OFF);
        vo.setNamedWhereClauseParam("showNarrowScpOnly", getParamNarrowScopeOnly());
        vo.setNamedWhereClauseParam("MQScope", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("pState", CSMQBean.WILDCARD);
        vo.setNamedWhereClauseParam("pUserRole", userBean.getUserRole());
        vo.setNamedWhereClauseParam("levelName", getParamLevel());
        
        
        CSMQBean.logger.info(userBean.getCaller() + " startDate: " + "");
        CSMQBean.logger.info(userBean.getCaller() + " endDate: " + "");
        CSMQBean.logger.info(userBean.getCaller() + " term: " + paramTermVal);
        CSMQBean.logger.info(userBean.getCaller() + " activityStatus: " + "ALL");
        CSMQBean.logger.info(userBean.getCaller() + " dictShortName: " + getParamDictionary());
        CSMQBean.logger.info(userBean.getCaller() + " releaseStatus: " + CSMQBean.BOTH_ACTIVITY_STATUSES);
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " MQGroup: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " product: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " MQCode: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " MQCriticalEvent: " + "%");
        CSMQBean.logger.info(userBean.getCaller() + " uniqueIDsOnly: " + CSMQBean.TRUE);
        CSMQBean.logger.info(userBean.getCaller() + " filterForUser: " +  CSMQBean.FALSE);
        CSMQBean.logger.info(userBean.getCaller() + " currentUser: " + nMQWizardBean.getCurrentUser());
        CSMQBean.logger.info(userBean.getCaller() + " killSwitch: " + CSMQBean.KILL_SWITCH_OFF);
        CSMQBean.logger.info(userBean.getCaller() + " showNarrowScpOnly: " + getParamNarrowScopeOnly());
        CSMQBean.logger.info(userBean.getCaller() + " MQScope: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " pState: " + CSMQBean.WILDCARD);
        CSMQBean.logger.info(userBean.getCaller() + " pUserRole: " + userBean.getUserRole());
        CSMQBean.logger.info(userBean.getCaller() + " levelName: " + getParamLevel());
        
        if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT){
            vo.setNamedWhereClauseParam("pMode", CSMQBean.MODE_VIEW_VERSION_IMPACT);
            this.impactSearch = true;
        }
        CSMQBean.logger.info(userBean.getCaller() + " IA hierarchy search query: " + vo.getQuery());
//        else {
//            if (null != nMQWizardBean.getCurrentPredictGroups()){
//                activationGroup = nMQWizardBean.getCurrentPredictGroups() + "," + CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP");    
//            } else {
//                activationGroup = CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP") + "," + CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP");
//            }
//        }
//        vo.setNamedWhereClauseParam("activationGroup", activationGroup);
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + activationGroup);
        vo.executeQuery();
        
        
        HierarchySearchResultsBean hierarchySearchResultsBean = (HierarchySearchResultsBean)ADFContext.getCurrent().getPageFlowScope().get("HierarchySearchResultsBean");
        hierarchySearchResultsBean.init();
        this.setShowHierarchyChildTree(false);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(controlMultiResultsTable);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlMultiResultsTable);

        
    }
    
    
    
    
    public NMQSourceTermSearchBean () {
        nMQWizardBean = (NMQWizardBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
        }


    // PARAMS

    public String getParamDictionaryType() {
       /*  try {
            nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
            this.paramDictionaryType = nMQSourceTermSearchUIBean.getControlDictionaryType().getValue().toString();
            } 
        catch (Exception e) {} */
       this.paramDictionaryType = getControlDictionaryType().getValue().toString();
        return paramDictionaryType;
    }

    public String getParamDictionary() {
       
       try {
            
            if (getParamDictionaryType().equals("BASE")) this.paramDictionary = nMQWizardBean.getCurrentBaseDictionaryShortName();
            else this.paramDictionary = nMQWizardBean.getCurrentFilterDictionaryShortName();
  
            //this.paramDictionary = this.controlDictionary.getValue().toString();
        } catch (Exception e) {}
        return paramDictionary;
    }

    public String getParamLevel() {
        try {
            nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
            this.paramLevel = getControlLevel().getValue().toString();
        } catch (Exception e) {}
        return paramLevel;
    }

    public String getParamReleaseGroup() {
        try {
            nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
            if (nMQSourceTermSearchUIBean.getControlReleaseGroup() != null) {
                this.paramReleaseGroup = nMQSourceTermSearchUIBean.getControlReleaseGroup().getValue().toString();
                }
            else {  // this is an overload to use the bean from the impact screen
                CSMQBean applicationBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
                this.paramReleaseGroup = applicationBean.getDefaultDraftReleaseGroup();
            }
            
        } catch (Exception e) {}
        return paramReleaseGroup;
    }

    public String getParamTerm() {
        paramTerm = CSMQBean.WILDCARD;
        nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
        if (nMQSourceTermSearchUIBean != null && nMQSourceTermSearchUIBean.getControlTerm() != null && nMQSourceTermSearchUIBean.getControlTerm().getValue() != null && !nMQSourceTermSearchUIBean.getControlTerm().getValue().toString().equalsIgnoreCase("null") && nMQSourceTermSearchUIBean.getControlTerm().getValue().toString().length() > 0)
            paramTerm = nMQSourceTermSearchUIBean.getControlTerm().getValue().toString();
        return paramTerm;
    }


    public void rowChanged(SelectionEvent selectionEvent) {
        
        // SEE WHICH OF THE 3 SOURCES IT IS
        if (selectionEvent.getSource().toString().indexOf("hResults") > 0) this.historicSearch = true;
        else if (selectionEvent.getSource().toString().indexOf("rResults") > 0) this.addRelationsSearch = true;
        else if (selectionEvent.getSource().toString().indexOf("t5") > 0) this.multiSearch = true;
        else this.impactSearch = true;

        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE ****");
        
        RichTable sourceTable = (RichTable)selectionEvent.getSource();
        RowKeySet selectedEmps = sourceTable.getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding empIter = bindings.findIteratorBinding("HierarchySourceTermSearchVO1Iterator");
        RowSetIterator empRSIter = empIter.getRowSetIterator();
        
        while (selectedEmpIter.hasNext()) {
            Key key = (Key)((List)selectedEmpIter.next()).get(0);
            Row currentRow = empRSIter.getRow(key); // current row
            System.out.println(currentRow.getAttribute("ContentId"));
            
            this.currentDictId = Utils.getAsString(currentRow, "ContentId");
            this.hasScope = Utils.getAsBoolean(currentRow, "Mqscp");

            CSMQBean.logger.info(userBean.getCaller() + " paramReleaseGroup: " + paramReleaseGroup);
            CSMQBean.logger.info(userBean.getCaller() + " currentDictId: " + currentDictId);
            }
        
            sourceTable.getSelectedRowKeys().clear();  // clear the selection - if there is only one, then the user can't select it & fire the event
        //if (!impactSearch) { //close it if its not an impact search - this MAY need to be changed for the other 2 types
            //sourceTable.getSelectedRowKeys().clear();  // clear the selection - if there is only one, then the user can't select it & fire the event
       //     if (nMQSourceTermSearchUIBean != null && nMQSourceTermSearchUIBean.getSearchPopUp() != null)
        //        nMQSourceTermSearchUIBean.getSearchPopUp().cancel();
      //      }

        refreshTree();
        
        }
 
    public void multiSelectRowChanged(SelectionEvent selectionEvent) {
        
            // RichTreeTable sourceTable = (RichTreeTable)selectionEvent.getSource();
            RichTreeTable sourceTable = controlMultiResultsTable;
            //clearKeys (targetTree);
            
            GenericTreeNode newRootNode = null;

            //RichTreeTable tree = targetTree;
            RowKeySet droppedValue = sourceTable.getSelectedRowKeys();

            Object[] keys = droppedValue.toArray();
            
            RowKeySet selectedRowKeys = sourceTable.getSelectedRowKeys();
              
              //Store original rowKey
              Object oldRowKey = sourceTable.getRowKey();
              try{ 

                if (selectedRowKeys != null) {
                  Iterator iter = selectedRowKeys.iterator();
                  if (iter != null && iter.hasNext()) {
                  Object rowKey = iter.next();
                  sourceTable.setRowKey(rowKey); //stamp row
                  GenericTreeNode rowData =
                    (GenericTreeNode)sourceTable.getRowData();
                  
                      this.currentDictId = rowData.getDictContentId();
                      this.hasScope = rowData.isHasScope();
                  
                  
                  System.out.println(rowData);
                  }
                }

              }finally{

                //Restore the original rowKey
                sourceTable.setRowKey(oldRowKey);

              }
              
        if (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT){
            this.impactSearch = true;
        } else {
            this.multiSearch = true;
        }

        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE ****");
        
        //RichTreeTable sourceTable = (RichTreeTable)selectionEvent.getSource();
        //RowKeySet selectedEmps = sourceTable.getSelectedRowKeys();
        //Iterator selectedEmpIter = selectedEmps.iterator();
        //DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        //DCIteratorBinding empIter = bindings.findIteratorBinding("HierarchySourceTermSearchVO1Iterator");
        //RowSetIterator empRSIter = empIter.getRowSetIterator();
        
        //while (selectedEmpIter.hasNext()) {
            //Key key = (Key)((List)selectedEmpIter.next()).get(0);
            //Row currentRow = empRSIter.getRow(key); // current row
           // System.out.println(currentRow.getAttribute("ContentId"));
            
           //GenericTreeNode newRootNode = null;



            //RowKeySet rks = sourceTable.getSelectedRowKeys();
            //Iterator rksIterator = rks.iterator();
            //List firstSet = (List)rks.iterator().next();

             
                 
        //}
 
            CSMQBean.logger.info(userBean.getCaller() + " paramReleaseGroup: " + paramReleaseGroup);
            CSMQBean.logger.info(userBean.getCaller() + " currentDictId: " + currentDictId);
            
        
            //sourceTable.getSelectedRowKeys().clear();  // clear the selection - if there is only one, then the user can't select it & fire the event
        //if (!impactSearch) { //close it if its not an impact search - this MAY need to be changed for the other 2 types
            //sourceTable.getSelectedRowKeys().clear();  // clear the selection - if there is only one, then the user can't select it & fire the event
       //     if (nMQSourceTermSearchUIBean != null && nMQSourceTermSearchUIBean.getSearchPopUp() != null)
        //        nMQSourceTermSearchUIBean.getSearchPopUp().cancel();
      //      }

        refreshTree();
        
        }
    
 
 

    public void refreshTree () {
        CSMQBean.logger.info(userBean.getCaller() + " *** CREATING SOURCE TREE ***");
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: SourceTreeVO1Iterator");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SourceTreeVO1Iterator");
        ViewObject sourceTreeVO = dciterb.getViewObject();
        
        sourceTreeVO.setNamedWhereClauseParam("dictShortName", this.paramDictionary);
        sourceTreeVO.setNamedWhereClauseParam("dictContentID", this.currentDictId);
        sourceTreeVO.setNamedWhereClauseParam("scopeFilter", this.getParamScope());
        sourceTreeVO.setNamedWhereClauseParam("sortKey", this.getParamSort());          
        sourceTreeVO.setNamedWhereClauseParam("returnPrimLinkPath", getParamPrimLinkFlag());
        sourceTreeVO.setNamedWhereClauseParam("maxLevels", CSMQBean.getProperty("HIERARCHY_INITIAL_FETCH"));
        sourceTreeVO.setNamedWhereClauseParam("narrowScopeOnly", getParamNarrowScopeOnly());
        
        String ignorePredict = getParamDictionaryType().equals("BASE") ? CSMQBean.TRUE : CSMQBean.FALSE; 
        
        String allGroups = null;
            ImpactAnalysisBean impactAnalysisBean =  null;
        if (impactSearch){
            impactAnalysisBean = (ImpactAnalysisBean)ADFContext.getCurrent().getPageFlowScope().get("ImpactAnalysisBean");
            ignorePredict = CSMQBean.FALSE;
            allGroups = impactAnalysisBean.getAllGroups();
        } else {
            if (multiSearch && null != ignorePredict && CSMQBean.FALSE.equalsIgnoreCase(ignorePredict)){
                allGroups = nMQWizardBean.getCurrentPredictGroups() + "," + CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP");
            }
        }
        sourceTreeVO.setNamedWhereClauseParam("ignorePredict", ignorePredict);
        sourceTreeVO.setNamedWhereClauseParam("activationGroup", allGroups);
        CSMQBean.logger.info(userBean.getCaller() + " dictShortName:" + this.paramDictionary);
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID:" + this.currentDictId);
        CSMQBean.logger.info(userBean.getCaller() + " scopeFilter:" + this.getParamScope());
        CSMQBean.logger.info(userBean.getCaller() + " sortKey:" + this.getParamSort());          
        CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath:" + getParamPrimLinkFlag());
        CSMQBean.logger.info(userBean.getCaller() + " maxLevels:" + CSMQBean.getProperty("HIERARCHY_INITIAL_FETCH"));
        CSMQBean.logger.info(userBean.getCaller() + " narrowScopeOnly:" + getParamNarrowScopeOnly());
        CSMQBean.logger.info(userBean.getCaller() + " ignorePredict:" + ignorePredict);
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup:" + allGroups);
        
        sourceTreeVO.executeQuery();
        
        termHierarchySourceBean.init(hasScope);
        
        if (impactSearch) {
            impactAnalysisBean = (ImpactAnalysisBean)ADFContext.getCurrent().getPageFlowScope().get("ImpactAnalysisBean");
            AdfFacesContext.getCurrentInstance().addPartialTarget(impactAnalysisBean.getHierarchySourceTree());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(impactAnalysisBean.getHierarchySourceTree());
            }
         else if (historicSearch) {
            ImpactSearchBean impactSearchBean = (ImpactSearchBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("ImpactSearchBean");
            AdfFacesContext.getCurrentInstance().addPartialTarget(impactSearchBean.getHierarchySourceTree());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(impactSearchBean.getHierarchySourceTree()); 
            }
        else if (multiSearch) {
            AdfFacesContext.getCurrentInstance().addPartialTarget(multiHierarchySourceTree);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(multiHierarchySourceTree);
            }
        else {
            TermHierarchyBean termHierarchyBean = (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
            AdfFacesContext.getCurrentInstance().addPartialTarget(termHierarchyBean.getSourceTree());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(termHierarchyBean.getSourceTree()); 
            }

        }
    
    
     public void clearTree () {
        CSMQBean.logger.info(userBean.getCaller() + " CREATING SOURCE TREE");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SourceTreeVO1Iterator");
        if (dciterb == null) return;  // theres no data here anyway
        ViewObject smallTreeVO = dciterb.getViewObject();
        
        smallTreeVO.setNamedWhereClauseParam("dictContentID", CSMQBean.HIERARCHY_KILL_SWITCH);       
        smallTreeVO.executeQuery();
        
        termHierarchySourceBean.init(false);
        }
    
    
    private void clearSearch (SelectionEvent selectionEvent) {
        try {
            BindingContext bc = BindingContext.getCurrent();
            DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
            DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HierarchySourceTermSearchVO1Iterator");
            ViewObject vo = dciterb.getViewObject();
   
            vo.executeEmptyRowSet();
            nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
            
            
            
            resolveMethodExpression("bindings.HierarchySourceTermSearch.collectionModel.makeCurrent}",null, new Class[] { SelectionEvent.class },new Object[] { selectionEvent });
            
            
            AdfFacesContext.getCurrentInstance().addPartialTarget(nMQSourceTermSearchUIBean.getControlResultsTable());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(nMQSourceTermSearchUIBean.getControlResultsTable());
        }
        catch (Exception e) {
            }
    }

   
    public void updateRelations(ActionEvent actionEvent) {
        if (nMQWizardBean.saveDetails(true)) { //save the NMQ first, so that the new relations show up
            int result = super.processUpdateRelations(termHierarchyBean.getTargetTree(), nMQWizardBean.getCurrentDictContentID());
            if (result == 0) termHierarchyBean.showStatus(CSMQBean.MQ_SAVED);
            else termHierarchyBean.showStatus(CSMQBean.MQ_SAVE_ERROR);
            termHierarchyBean.refresh();
            }
        }

    public DnDAction onTreeDrop(DropEvent dropEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " onTreeDrop...");
        termHierarchyBean.showStatus(CSMQBean.MQ_MODIFIED);
        return processDragAndDropEvent(dropEvent, termHierarchyBean.getSourceTree(), termHierarchyBean.getTargetTree(), termHierarchyBean.getTreemodel(), Integer.parseInt(getParamScope()));
    }

    public void deleteSelected(DialogEvent dialogEvent) {
        termHierarchyBean.showStatus(CSMQBean.MQ_MODIFIED);
        super.processDeleteSelected(dialogEvent, termHierarchyBean.getTargetTree(), termHierarchyBean.getTreemodel());
    }

    public void dictionaryTypeChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent == null) return;
        String newDictionary = valueChangeEvent.getNewValue().toString();
        
        if (newDictionary.equals("BASE")) {
            this.paramDictionary = nMQWizardBean.getCurrentBaseDictionaryShortName();
            // commented as it effecting the display of scope in add details page
            //nMQWizardBean.setIsMedDRA(true);
            controlLevel.setValue("SOC");
            this.showMedDRASelItems = true;
            this.showNMQSelItems = false;
            this.showSMQSelItems = false;
            this.setMeddraSearch(true);
            }
        else {
            this.paramDictionary = nMQWizardBean.getCurrentFilterDictionaryShortName();
                // commented as it effecting the display of scope in add details page
            //nMQWizardBean.setIsMedDRA(false);
            this.setMeddraSearch(false);
            if (nMQWizardBean.isIsNMQ()) {
                this.showMedDRASelItems = false;
                this.showNMQSelItems = true;
                this.showSMQSelItems = true;
                controlLevel.setValue("MQ1");
                }
            else {
                this.showMedDRASelItems = false;
                this.showNMQSelItems = false;
                this.showSMQSelItems = true;
                controlLevel.setValue("MQ1");
                }
 
            }
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(getControlLevel());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(getControlLevel());
        
        //refreshLevelList(paramDictionary);
    }
    
//    public void refreshLevelList (String dictionary) {
//        nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
//        this.paramDictionaryType = getControlDictionaryType().getValue().toString();
//               
//        BindingContext bc = BindingContext.getCurrent();
//        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
//        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("LevelsByDictionaryVO1Iterator");
//        ViewObject vo = dciterb.getViewObject();
//        
//        vo.setNamedWhereClauseParam("dictShortName", dictionary);
//        vo.executeQuery();
//        
//        AdfFacesContext.getCurrentInstance().addPartialTarget(getControlLevel());
//        AdfFacesContext.getCurrentInstance().partialUpdateNotify(getControlLevel());
//    }
    

    public void dialogClosed(PopupCanceledEvent popupCanceledEvent) {
        clearResults();
        nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
        if (nMQSourceTermSearchUIBean.getCntrlScope() == null || getControlDictionaryType().getValue() == null) return;
        if (getControlDictionaryType().getValue().toString().equals("BASE")) 
            nMQSourceTermSearchUIBean.getCntrlScope().setDisabled(true);
        else
            nMQSourceTermSearchUIBean.getCntrlScope().setDisabled(false);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(nMQSourceTermSearchUIBean.getCntrlScope());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(nMQSourceTermSearchUIBean.getCntrlScope());
        }


    private void clearResults () {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HierarchySourceTermSearchVO1Iterator");
        ViewObject search = dciterb.getViewObject();
 
        // CLEAR THE RESULTS
        search.setNamedWhereClauseParam("killSwitch", CSMQBean.KILL_SWITCH_ON);
        search.executeQuery();
        
        nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");    
        nMQSourceTermSearchUIBean.getControlResultsTable().clearLocalCache();
 
        AdfFacesContext.getCurrentInstance().addPartialTarget(nMQSourceTermSearchUIBean.getControlResultsTable());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(nMQSourceTermSearchUIBean.getControlResultsTable());
    }

    public void showSecondaryPathChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals(valueChangeEvent.getOldValue())) return; // it didn't change
        refreshTree();
    }

    public void setParamScope(String paramScope) {
        this.paramScope = paramScope;
    }

    public String getParamScope() {
        nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
         if (nMQSourceTermSearchUIBean != null && nMQSourceTermSearchUIBean.getCntrlScope() != null)
            this.paramScope = nMQSourceTermSearchUIBean.getCntrlScope().getValue().toString();
        return paramScope;
    }

    public void setParamSort(String sort) {
        this.paramSort = sort;
    }

    public String getParamSort() {
        nMQSourceTermSearchUIBean = (NMQSourceTermSearchUIBean)ADFContext.getCurrent().getRequestScope().get("NMQSourceTermSearchUIBean");
        if (nMQSourceTermSearchUIBean != null && nMQSourceTermSearchUIBean.getCntrlSortList() != null)
            this.paramSort =  nMQSourceTermSearchUIBean.getCntrlSortList().getValue().toString();
        return paramSort;
    }

    public void refresh(ActionEvent actionEvent) {
        refreshTree();
    }

    public void sortChanged(ValueChangeEvent valueChangeEvent) {
        this.paramSort = valueChangeEvent.getNewValue().toString();
    }

    public void scopeChanged(ValueChangeEvent valueChangeEvent) {
        this.paramScope = valueChangeEvent.getNewValue().toString();
    }

    public void setParamNarrowScopeOnly(String paramNarrowScopeOnly) {
        this.paramNarrowScopeOnly = paramNarrowScopeOnly;
    }

    public String getParamNarrowScopeOnly() {
        if (nMQSourceTermSearchUIBean != null && nMQSourceTermSearchUIBean.getCntrlNarrowScope() != null && nMQSourceTermSearchUIBean.getCntrlNarrowScope().getValue() != null)
            this.paramNarrowScopeOnly =  nMQSourceTermSearchUIBean.getCntrlNarrowScope().getValue().toString().equals("true") ? CSMQBean.TRUE : CSMQBean.FALSE;
        return paramNarrowScopeOnly;
    }

    public void setParamPrimLinkFlag(String returnPrimLinkFlag) {
        this.paramPrimLinkFlag = returnPrimLinkFlag;
    }

    public String getParamPrimLinkFlag() {
        if (nMQSourceTermSearchUIBean != null && nMQSourceTermSearchUIBean.getCtrlReturnPrimLinkPath() != null)
        this.paramPrimLinkFlag = nMQSourceTermSearchUIBean.getCtrlReturnPrimLinkPath().getValue().toString().equals("true") ? CSMQBean.TRUE : CSMQBean.FALSE;
        return paramPrimLinkFlag;
    }


    
    
    public void nodeChanged(ValueChangeEvent valueChangeEvent) {
        
        
        /* soc3 = scope
         * soc2 = category
         * ot6 = weight
         */
        TermHierarchyBean termHierarchyBean = (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
        
        Object o = valueChangeEvent;
        RowKeySet rks = termHierarchyBean.getTargetTree().getSelectedRowKeys();
        Iterator rksIterator = rks.iterator();
        List key = (List)rksIterator.next();
        
        int rowKey = Integer.parseInt(key.get(1).toString());
        GenericTreeNode node = (GenericTreeNode)termHierarchyBean.getTargetTree().getRowData(rowKey);
        
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
        
        termHierarchyBean.showStatus(CSMQBean.MQ_MODIFIED);
    }


    public void searchPopupLoad(PopupFetchEvent popupFetchEvent) {
        
       // init level list
        
        //refreshLevelList(nMQWizardBean.getCurrentFilterDictionaryShortName());
        
        //dictionaryTypeChanged(null);
    }

    public void setParamExtension(String paramExtension) {
        this.paramExtension = paramExtension;
    }

    public String getParamExtension() {
        return paramExtension;
    }
    
    public void setControlDictionaryType(RichSelectOneChoice controlDictionaryType) {
        this.controlDictionaryType = controlDictionaryType;
    }

    public RichSelectOneChoice getControlDictionaryType() {
        return controlDictionaryType;
    }
    
    public void setControlLevel(RichSelectOneChoice controlLevel) {
        this.controlLevel = controlLevel;
    }

    public RichSelectOneChoice getControlLevel() {
        return controlLevel;
    }


    public void setShowMedDRASelItems(boolean showMedDRASelItems) {
        this.showMedDRASelItems = showMedDRASelItems;
    }

    public boolean isShowMedDRASelItems() {
        return showMedDRASelItems;
    }

    public void setShowNMQSelItems(boolean showNMQSelItems) {
        this.showNMQSelItems = showNMQSelItems;
    }

    public boolean isShowNMQSelItems() {
        return showNMQSelItems;
    }

    public void setShowSMQSelItems(boolean showSMQSelItems) {
        this.showSMQSelItems = showSMQSelItems;
    }

    public boolean isShowSMQSelItems() {
        return showSMQSelItems;
    }
    
    
    public DnDAction onMultiTreeDrop(DropEvent dropEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " onMultiTreeDrop...");
        RichTreeTable source = multiHierarchySourceTree;
        if (dropEvent.getDragClientId().equals("pt1:t5")) source = controlMultiResultsTable;
 
        termHierarchyBean.showStatus(CSMQBean.MQ_MODIFIED);
        ///  THIS WILL PROBABLY NEED TO BE FIXED TO USE THE OTHER TREES - TES 1/26/2012
        return processDragAndDropEvent(dropEvent, source, termHierarchyBean.getTargetTree(), termHierarchyBean.getTreemodel(), Integer.parseInt(getParamScope()));
        }


    

    public void setMultiHierarchySourceTree(RichTreeTable multiHierarchySourceTree) {
        this.multiHierarchySourceTree = multiHierarchySourceTree;
    }

    public RichTreeTable getMultiHierarchySourceTree() {
        return multiHierarchySourceTree;
    }

    public void setControlMultiResultsTable(RichTreeTable controlMultiResultsTable) {
        this.controlMultiResultsTable = controlMultiResultsTable;
    }

    public RichTreeTable getControlMultiResultsTable() {
        return controlMultiResultsTable;
    }

    public void loadHierachy(ActionEvent actionEvent) {
        this.setShowHierarchyChildTree(true);
        multiSelectRowChanged(null);
    }

    public void setMeddraSearch(boolean meddraSearch) {
        this.meddraSearch = meddraSearch;
    }

    public boolean isMeddraSearch() {
        return meddraSearch;
    }
    public int getCurrentMode(){
        return nMQWizardBean.getMode();
    }

    public void setShowHierarchyChildTree(boolean showHierarchyChildTree) {
        this.showHierarchyChildTree = showHierarchyChildTree;
    }

    public boolean isShowHierarchyChildTree() {
        return showHierarchyChildTree;
    }
}

