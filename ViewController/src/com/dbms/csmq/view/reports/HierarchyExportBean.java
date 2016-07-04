package com.dbms.csmq.view.reports;
//


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.backing.NMQ.NMQUtils;
import com.dbms.csmq.view.backing.NMQ.NMQWizardBean;

import com.dbms.csmq.view.backing.impact.ImpactAnalysisBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisUIBean;
import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.hierarchy.TermHierarchyBean;
import com.dbms.util.Utils;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;


import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;

import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;

public class HierarchyExportBean {
    
    private CSMQBean cSMQBean;
    private UserBean userBean;    
    private boolean showReportFooter = false;  //SHOW_FOOTER
    private boolean showLastPageFooter = false;  //SHOW_LAST_PAGE_FOOTER
    private String defaultExportReportName; 
    private String defaultExportChangedReportName; 
    private String existingIAReportName;
    private String futureIAReportName;
    private String reportTitle; //REPORT_TITLE
    private Map parameters;
    private NMQWizardBean nMQWizardBean;
    //private ImpactAnalysisBean impactAnalysisBean;
    private String reportFormat = "XLS";
    private RichCommandToolbarButton generateReport;
 
    private RichSelectOneChoice cntrlFormatList;
    private RichSelectOneChoice cntrlExistingFutureList;
    private RichSelectBooleanCheckbox cntrlPerformImpact;
    private String generatedReport;
    private String excelFileName;
    //private String existingFuture = "FUTURE";
    private RichPopup cntrlExportHierarchyPopup;
    private String defaultMedDRAGroupName;
    private String defaultDraftGroupName;
    private String defaultReleaseGroupName;
    private RichSelectOneChoice cntrlSort;
    //private String sortKey;
    
    private String version;
    private String lastUpdate;

    public HierarchyExportBean () {
        
        System.out.println("START: HierarchyExportBean");

        userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
        cSMQBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        
        parameters = new HashMap();
        nMQWizardBean = (NMQWizardBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
        
        defaultMedDRAGroupName = CSMQBean.getProperty("DEFAULT_MEDDRA_RELEASE_GROUP");
        defaultDraftGroupName = CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP");
        defaultReleaseGroupName = CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP");
        
        defaultExportReportName = cSMQBean.getProperty("HIERARCHY_EXPORT_TEMPLATE");
        defaultExportChangedReportName = cSMQBean.getProperty("HIERARCHY_EXPORT_CHANGED_TEMPLATE");
        existingIAReportName = cSMQBean.getProperty("HIERARCHY_EXPORT_IA_CURRENT_TEMPLATE");
        futureIAReportName = cSMQBean.getProperty("HIERARCHY_EXPORT_IA_FUTURE_TEMPLATE");   
        
        System.out.println("END: HierarchyExportBean");
        }
    
    public void impactExport(FacesContext facesContext, OutputStream outputStream) {  
        //MedDRAImpactVO1Iterator1
        //DraftImpactVO1Iterator1
        ImpactAnalysisBean impactAnalysisBean = (ImpactAnalysisBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("ImpactAnalysisBean");
        ImpactAnalysisUIBean impactAnalysisUIBean = (ImpactAnalysisUIBean)ADFContext.getCurrent().getRequestScope().get("ImpactAnalysisUIBean");
        doExport(facesContext, outputStream, true, getVisableRowsFromTable(impactAnalysisBean.getMedDRATree()), getVisableRowsFromTable(impactAnalysisBean.getFutureTree()));
        }
    
    public void export(FacesContext facesContext, OutputStream outputStream) {
        //SmallTreeVO1Iterator
        TermHierarchyBean termHierarchyBean = (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
        doExport(facesContext, outputStream, false, getVisableRowsFromTable(termHierarchyBean.getTargetTree()), null);
        }

    public void formatChanged(ValueChangeEvent valueChangeEvent) {
        getGeneratedReport();
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.generateReport);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.generateReport);
    }
    
    public String getGeneratedReport() {
        return generatedReport;
    }

    
    private void doExport (FacesContext facesContext, OutputStream outputStream, boolean isImpact, String currentDisplayedTerms, String futureDisplayedTerms) {
        
        String hasScope = nMQWizardBean.getCurrentScope();
        String ignorePredict = nMQWizardBean.getIgnorePredict();
        
        // GET DICT VERSIONS
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        AttributeBinding attr = (AttributeBinding)bindings.getControlBinding("Version");
        version = (String) attr.getInputValue();
        attr = (AttributeBinding)bindings.getControlBinding("LastUpdate");
        lastUpdate = (String) attr.getInputValue();
        
        String performImpact = isImpact ? CSMQBean.TRUE : CSMQBean.FALSE;
        String allGroups = null;
        
        //String allGroups = this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName + "," + this.defaultReleaseGroupName;
        if (isImpact){
            allGroups = this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName + "," + this.defaultReleaseGroupName;
        } else {
            allGroups = this.defaultDraftGroupName + "," + this.defaultReleaseGroupName;
        }
        if (!nMQWizardBean.isExportDisplayedOnly()) currentDisplayedTerms = "";
        if (!nMQWizardBean.isExportDisplayedOnly()) futureDisplayedTerms = "";
        
        parameters.put("I_DICT_CONTENT_ID", nMQWizardBean.getCurrentDictContentID());
        parameters.put("REPORT_TITLE", reportTitle);
        parameters.put("SHOW_FOOTER", false);
        parameters.put("SHOW_LAST_PAGE_FOOTER", isImpact);
        parameters.put("ACTIVATION_GROUP", allGroups);
        parameters.put("DICTIONARY_VERSION", version);
        parameters.put("DICTIONARY_TIMESTAMP", lastUpdate);
        parameters.put("HAS_SCOPE", hasScope);
        parameters.put("ACTIVITY_STATUS", nMQWizardBean.getCurrentMQStatus());
        parameters.put("INCLUDE_LLTS", nMQWizardBean.getIncludeLLTsInExport() ? CSMQBean.TRUE : CSMQBean.FALSE);
        parameters.put("I_IGNORE_PREDICT", ignorePredict);
        parameters.put("I_RELEASE_STATUS", nMQWizardBean.getCurrentStatus());
        parameters.put("I_CUSTOM_MQNAME", cSMQBean.getCustomMQName());
                
        String [] reportList = null;
        if (isImpact)
            reportList =  new String [] {existingIAReportName, futureIAReportName};
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_EXISTING || nMQWizardBean.getMode() == CSMQBean.MODE_COPY_EXISTING || nMQWizardBean.getMode() == CSMQBean.MODE_INSERT_NEW)
            reportList =  new String [] {defaultExportChangedReportName};
        else     
            reportList =  new String [] {defaultExportReportName};
           
        CSMQBean.logger.info(userBean.getCaller() + " *** RUNNING REPORT ***");
        
        CSMQBean.logger.info(userBean.getCaller() + " user: " +  userBean.getUsername());
        CSMQBean.logger.info(userBean.getCaller() + " I_DICT_CONTENT_ID: " +  nMQWizardBean.getCurrentDictContentID());
        CSMQBean.logger.info(userBean.getCaller() + " REPOR_LIST: " +  reportList);
        CSMQBean.logger.info(userBean.getCaller() + " REPORT_TITLE: " +  reportTitle);
        CSMQBean.logger.info(userBean.getCaller() + " SHOW_FOOTER: " +  (isImpact && reportFormat.equals("PDF")));
        CSMQBean.logger.info(userBean.getCaller() + " SHOW_LAST_PAGE_FOOTER: " +  isImpact);
        CSMQBean.logger.info(userBean.getCaller() + " ACTIVATION_GROUP: " +  allGroups);
        CSMQBean.logger.info(userBean.getCaller() + " DICTIONARY_VERSION: " +  version);
        CSMQBean.logger.info(userBean.getCaller() + " DICTIONARY_TIMESTAMP: " +  lastUpdate);
        CSMQBean.logger.info(userBean.getCaller() + " ACTIVITY_STATUS: " +  nMQWizardBean.getCurrentMQStatus());
        CSMQBean.logger.info(userBean.getCaller() + " INCLUDE_LLTS: " +  nMQWizardBean.getIncludeLLTsInExport());
        CSMQBean.logger.info(userBean.getCaller() + " Mode : " +  nMQWizardBean.getMode());
        CSMQBean.logger.info(userBean.getCaller() + " I_IGNORE_PREDICT: " +  ignorePredict);
        CSMQBean.logger.info(userBean.getCaller() + " ACTIVATION_GROUP: " +  allGroups);
        
        new ReportEngine().exportAsExcelWorkbook(reportList, outputStream, parameters, userBean.getCaller(), performImpact);
    }


    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportTitle() {
        reportTitle = nMQWizardBean.getCurrentTermName();
        return reportTitle;
    }

    public void setCntrlFormatList(RichSelectOneChoice cntrlFormatList) {
        this.cntrlFormatList = cntrlFormatList;
    }

    public RichSelectOneChoice getCntrlFormatList() {
        return cntrlFormatList;
    }

    public void setCntrlExistingFutureList(RichSelectOneChoice cntrlLeftRightList) {
        this.cntrlExistingFutureList = cntrlLeftRightList;
    }

    public RichSelectOneChoice getCntrlExistingFutureList() {
        return cntrlExistingFutureList;
    }



    public void setCntrlPerformImpact(RichSelectBooleanCheckbox cntrlPerformImpact) {
        this.cntrlPerformImpact = cntrlPerformImpact;
    }

    public RichSelectBooleanCheckbox getCntrlPerformImpact() {
        return cntrlPerformImpact;
    }

    public void cntrlPerformImpactChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void cntrlFormatListChanged(ValueChangeEvent valueChangeEvent) {
        getGeneratedReport();
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.generateReport);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.generateReport);
    }

    public void setCntrlExportHierarchyPopup(RichPopup cntrlExportHierarchyPopup) {
        this.cntrlExportHierarchyPopup = cntrlExportHierarchyPopup;
    }

    public RichPopup getCntrlExportHierarchyPopup() {
        return cntrlExportHierarchyPopup;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public String getReportFormat() {
        return reportFormat;
    }

    public void setGenerateReport(RichCommandToolbarButton generateReport) {
        this.generateReport = generateReport;
    }

    public RichCommandToolbarButton getGenerateReport() {
        return generateReport;
    }

    public void cntrlExportHierarchyPopupCanceled(PopupCanceledEvent popupCanceledEvent) {}

    public void cntrlExportPopupLoaded(PopupFetchEvent popupFetchEvent) {}

    public void showExportPopup(ActionEvent actionEvent) {
        reportTitle = nMQWizardBean.getCurrentTermName();
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        cntrlExportHierarchyPopup.show(hints);
    }
    
    public void refreshSearchList(ActionEvent actionEvent) {
        NMQUtils.refreshImpactMVs();
    }

    public void setCntrlSort(RichSelectOneChoice cntrlSort) {
        this.cntrlSort = cntrlSort;
    }

    public RichSelectOneChoice getCntrlSort() {
        return cntrlSort;
    }

    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    public String getExcelFileName() {
        excelFileName = this.getReportTitle() + ".xls";
        return excelFileName;
    }
    
    
    public String getVisableRowsFromTable (RichTreeTable tree) {
        
        ArrayList tempList = new ArrayList();
        CollectionModel model = (CollectionModel)tree.getValue(); 
        //ArrayList node = (ArrayList) model.getWrappedData();
        ArrayList nodes = (ArrayList) model.getWrappedData();
        GenericTreeNode n = (GenericTreeNode)nodes.get(0);
        System.out.println("CHECKING TREE: " + tree);
        searchTreeNode(n, tempList); 
        return StringUtils.join(tempList.iterator(),',');
    }

    private void searchTreeNode(GenericTreeNode node, ArrayList visibleNodes) {
      
        visibleNodes.add(node.getPrikey());
        
        List<GenericTreeNode> children = node.getChildren();
        if (children != null) {
            for (GenericTreeNode _node : children) 
                searchTreeNode(_node, visibleNodes);
            
        }

    }


}
