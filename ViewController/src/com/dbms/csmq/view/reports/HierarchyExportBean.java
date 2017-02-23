package com.dbms.csmq.view.reports;
//


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.backing.NMQ.NMQUtils;
import com.dbms.csmq.view.backing.NMQ.NMQWizardBean;
import com.dbms.csmq.view.backing.NMQ.NMQWizardSearchBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisUIBean;
import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.hierarchy.TermHierarchyBean;
import com.dbms.csmq.view.impact.PreviousVerCurrentImpactHierarchyBean;
import com.dbms.csmq.view.impact.PreviousVerFutureImpactHierarchyBean;
import com.dbms.util.ADFUtils;
import com.dbms.util.POIExportUtil;
import com.dbms.util.dml.DMLUtils;

import java.io.IOException;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.server.DBTransaction;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.Datum;
import oracle.sql.STRUCT;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;


public class HierarchyExportBean {

    private CSMQBean cSMQBean;
    private UserBean userBean;
    private boolean showReportFooter = false; //SHOW_FOOTER
    private boolean showLastPageFooter = false; //SHOW_LAST_PAGE_FOOTER
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
    private String mqDtlReportExcelFileName;
    //private String existingFuture = "FUTURE";
    private RichPopup cntrlExportHierarchyPopup;
    private String defaultMedDRAGroupName;
    private String defaultDraftGroupName;
    private String defaultReleaseGroupName;
    private RichSelectOneChoice cntrlSort;
    //private String sortKey;

    private String version;
    private String lastUpdate;
    String sourceDirectory = CSMQBean.getProperty("REPORT_SOURCE");
    private int rowCount = 0;
    private static final String MQ_DTL_REPORT_SQL = "{ ? = call nmat_tms_query_utils.get_tms_hierarchy(?,?,?) }";

    public HierarchyExportBean() {

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

    public void impactPOIExport(FacesContext facesContext, OutputStream outputStream) {
        try {
            NMQWizardBean nMQWizardBean =
                (NMQWizardBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");

            HSSFWorkbook workbook = new HSSFWorkbook();

            PreviousVerCurrentImpactHierarchyBean currentImpactHierarchyBean =
                (PreviousVerCurrentImpactHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("PreviousVerCurrentImpactHierarchyBean");
            createWorksheet(workbook, currentImpactHierarchyBean.getRootCopy(), "Current",
                            nMQWizardBean.getIncludeLLTsInExport());

            PreviousVerFutureImpactHierarchyBean futuretImpactHierarchyBean =
                (PreviousVerFutureImpactHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("PreviousVerFutureImpactHierarchyBean");
            createWorksheet(workbook, futuretImpactHierarchyBean.getRootCopy(), "Future",
                            nMQWizardBean.getIncludeLLTsInExport());

            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createWorksheet(HSSFWorkbook workbook, GenericTreeNode root, String worksheetName,
                                 boolean includeLLTsInExport) {
        try {
            HSSFSheet currentWorksheet = workbook.createSheet(worksheetName);
            rowCount = 0;
            POIExportUtil.addImageRow(currentWorksheet, rowCount++);
            POIExportUtil.addImageRow(currentWorksheet, rowCount++);
            POIExportUtil.addImageRow(currentWorksheet, rowCount++);
            POIExportUtil.addImageRow(currentWorksheet, rowCount++);
            POIExportUtil.addImageRow(currentWorksheet, rowCount++);
            currentWorksheet.addMergedRegion(new CellRangeAddress(0, rowCount, 0, 5));
            String logoPath = sourceDirectory + "/app_logo.png";
            POIExportUtil.writeImageTOExcel(currentWorksheet, POIExportUtil.loadResourceAsStream(logoPath));

            POIExportUtil.addEmptyRow(currentWorksheet, rowCount++);
            POIExportUtil.addEmptyRow(currentWorksheet, rowCount++);
            POIExportUtil.addEmptyRow(currentWorksheet, rowCount++);
            POIExportUtil.addHeaderTextRow(currentWorksheet, rowCount++, nMQWizardBean.getCurrentTermName(), 6);

            // GET DICT VERSIONS
            try {
                BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
                AttributeBinding attr = (AttributeBinding)bindings.getControlBinding("Version");
                version = (String)attr.getInputValue();
            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }

            POIExportUtil.addEmptyRow(currentWorksheet, rowCount++);
            POIExportUtil.addFormRow(currentWorksheet, rowCount++, "MedDRA Dictionary Version:", version, 3, 2);
            POIExportUtil.addFormRow(currentWorksheet, rowCount++, "Status:",
                                     "A".equals(nMQWizardBean.getCurrentMQStatus()) ? "Active" : "Inactive", 3, 2);
            POIExportUtil.addFormRow(currentWorksheet, rowCount++, "Scope (Yes/No):",
                                     "Y".equals(nMQWizardBean.getCurrentScope()) ? "Yes" : "No", 3, 2);
            POIExportUtil.addFormRow(currentWorksheet, rowCount++, "Report Date/Time:",
                                     new SimpleDateFormat("dd-MMM-yyyy h:mm a z").format(new Date(System.currentTimeMillis())),
                                     3, 2);

            POIExportUtil.addEmptyRow(currentWorksheet, rowCount++);
            String[] headerArr = { "Term", "Code", "Level", "Category", "Weight", "Scope" };
            int[] colSpan = { 3, 1, 1, 1, 1, 1 };
            POIExportUtil.addHierarchyTableHeaderRow(currentWorksheet, rowCount++, headerArr, colSpan);

            Map<String, HSSFCellStyle> versionImpactCellStylesMap =
                POIExportUtil.formVersionImpactCellStyles(currentWorksheet);

            generateImpactedHierarchyRow(currentWorksheet, root, colSpan, new Integer("0"), includeLLTsInExport,
                                         versionImpactCellStylesMap);
            currentWorksheet.createFreezePane(0, 1, 0, 1);

            for (int x = 0; x < 8; x++) {
                currentWorksheet.autoSizeColumn(x);
            }
        } catch (IOException e) {
        }
    }

    private void generateImpactedHierarchyRow(HSSFSheet worksheet, GenericTreeNode node, int[] colSpan, Integer depth,
                                              boolean includeLLTsInExport,
                                              Map<String, HSSFCellStyle> versionImpactCellStylesMap) {
        CSMQBean.logger.info(userBean.getCaller() + "Start of exec generateImpactedHierarchyRow() ");
        if (!includeLLTsInExport && node.getLevelName() != null && node.getLevelName().contains("LLT")) {
            return;
        }
        String[] valArr = new String[6];
        valArr[0] = node.getTerm();
        valArr[1] = node.getDictContentCode();
        valArr[2] = node.getLevelName();
        valArr[3] = node.getTermCategory();
        valArr[4] = node.getTermWeight();
        valArr[5] = node.getScopeName();
        CSMQBean.logger.info(userBean.getCaller() + "generateImpactedHierarchyRow() Term=" + valArr[0]);
        CSMQBean.logger.info(userBean.getCaller() + "generateImpactedHierarchyRow() rowCount=" + rowCount);
        CSMQBean.logger.info(userBean.getCaller() + "generateImpactedHierarchyRow() depth=" + depth);
        POIExportUtil.addImpactedHierarchyTableValueRow(worksheet, rowCount++, valArr, colSpan, depth,
                                                        versionImpactCellStylesMap.get(node.getStyle()));
        if (node.getChildren() != null && node.getChildren().size() > 0) {
            GenericTreeNode cnode = null;
            depth++;
            for (int i = 0; i < node.getChildren().size(); i++) {
                cnode = (GenericTreeNode)node.getChildren().get(i);
                generateImpactedHierarchyRow(worksheet, cnode, colSpan, depth, includeLLTsInExport,
                                             versionImpactCellStylesMap);
            }
        }
        CSMQBean.logger.info(userBean.getCaller() + "End of exec generateImpactedHierarchyRow() ");
    }


    public void impactExport(FacesContext facesContext, OutputStream outputStream) {
        //MedDRAImpactVO1Iterator1
        //DraftImpactVO1Iterator1
        ImpactAnalysisBean impactAnalysisBean =
            (ImpactAnalysisBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("ImpactAnalysisBean");
        ImpactAnalysisUIBean impactAnalysisUIBean =
            (ImpactAnalysisUIBean)ADFContext.getCurrent().getRequestScope().get("ImpactAnalysisUIBean");
        doExport(facesContext, outputStream, true, getVisableRowsFromTable(impactAnalysisBean.getMedDRATree()),
                 getVisableRowsFromTable(impactAnalysisBean.getFutureTree()));
    }

    public void exportRelations(FacesContext facesContext, OutputStream outputStream) {
        NMQWizardSearchBean nMQWizardSearchBean =
            (NMQWizardSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardSearchBean");
        System.out.println("exportRelations() nMQWizardSearchBean.isHistoryFlow() =" +
                           nMQWizardSearchBean.isHistoryFlow());
        if (nMQWizardSearchBean.isHistoryFlow()) {
            exportHistoryTermRelationsPOI(facesContext, outputStream);
        } else {
            exportRelationJasperReport(facesContext, outputStream);
        }
    }

    public void exportRelationJasperReport(FacesContext facesContext, OutputStream outputStream) {
        //SmallTreeVO1Iterator
        TermHierarchyBean termHierarchyBean =
            (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
        doExport(facesContext, outputStream, false, getVisableRowsFromTable(termHierarchyBean.getTargetTree()), null);
    }

    public void exportHistoryTermRelationsPOI(FacesContext facesContext, OutputStream outputStream) {
        CSMQBean.logger.info(userBean.getCaller() + "Start exec exportHistoryTermRelationsPOI() ");
        CSMQBean.logger.info(userBean.getCaller() + " user: " + userBean.getUsername());
        CSMQBean.logger.info(userBean.getCaller() + " I_DICT_CONTENT_ID: " + nMQWizardBean.getCurrentDictContentID());
        CSMQBean.logger.info(userBean.getCaller() + " CurrentStatus: " + nMQWizardBean.getCurrentStatus());
        CSMQBean.logger.info(userBean.getCaller() + " getCurrentReleaseGroup: " +
                             nMQWizardBean.getCurrentReleaseGroup());
        loadMQDetailedDetailedTermHierarchyInformation(new BigDecimal(nMQWizardBean.getCurrentDictContentID()),
                                                       nMQWizardBean.getCurrentStatus(),
                                                       nMQWizardBean.getCurrentReleaseGroup());
        try {
            TermHierarchyBean termHierarchyBean =
                (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
            GenericTreeNode root = termHierarchyBean.getRoot();

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Existing");
            rowCount = 0;
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            worksheet.addMergedRegion(new CellRangeAddress(0, rowCount, 0, 5));
            String logoPath = sourceDirectory + "/app_logo.png";
            POIExportUtil.writeImageTOExcel(worksheet, POIExportUtil.loadResourceAsStream(logoPath));

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addHeaderTextRow(worksheet, rowCount++, nMQWizardBean.getCurrentTermName(), 6);

            // GET DICT VERSIONS
            try {
                BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
                AttributeBinding attr = (AttributeBinding)bindings.getControlBinding("Version");
                version = (String)attr.getInputValue();
            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            NMQWizardSearchBean nMQWizardSearchBean =
                (NMQWizardSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardSearchBean");
            if (nMQWizardSearchBean != null) {
                POIExportUtil.addSimpleTextRow(worksheet, rowCount++,
                                               "Historic View as of  " + nMQWizardSearchBean.getHistoryInputDateStr(),
                                               5);
            }
            POIExportUtil.addFormRow(worksheet, rowCount++, "MedDRA Dictionary Version:",
                                     nMQWizardBean.getActiveDictionaryVersion(), 2, 2);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Status:",
                                     "A".equals(nMQWizardBean.getCurrentMQStatus()) ? "Active" : "Inactive", 2, 2);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Scope (Yes/No):",
                                     "Y".equals(nMQWizardBean.getCurrentScope()) ? "Yes" : "No", 2, 2);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Report Date/Time:",
                                     new SimpleDateFormat("dd-MMM-yyyy h:mm a z").format(new Date(System.currentTimeMillis())),
                                     2, 2);

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            String[] headerArr = { "Term", "Code", "Level", "Category", "Weight", "Scope" };
            int[] colSpan = { 3, 1, 1, 1, 1, 1 };
            POIExportUtil.addHierarchyTableHeaderRow(worksheet, rowCount++, headerArr, colSpan);

            boolean scopeFlag = true;
            if ("No".equalsIgnoreCase(nMQWizardBean.getCurrentScope()) ||
                "N".equalsIgnoreCase(nMQWizardBean.getCurrentScope())) {
                scopeFlag = false;
            }

            generateHierarchyRow(worksheet, root, colSpan, new Integer("0"), scopeFlag);

            worksheet.createFreezePane(0, 1, 0, 1);

            for (int x = 0; x < 8; x++) {
                worksheet.autoSizeColumn(x);
            }
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CSMQBean.logger.info(userBean.getCaller() + "End of exec exportHistoryTermRelationsPOI() ");
    }

    public void formatChanged(ValueChangeEvent valueChangeEvent) {
        getGeneratedReport();
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.generateReport);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.generateReport);
    }

    public String getGeneratedReport() {
        return generatedReport;
    }


    private void doExport(FacesContext facesContext, OutputStream outputStream, boolean isImpact,
                          String currentDisplayedTerms, String futureDisplayedTerms) {

        String hasScope = nMQWizardBean.getCurrentScope();
        String ignorePredict = nMQWizardBean.getIgnorePredict();

        // GET DICT VERSIONS
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        AttributeBinding attr = (AttributeBinding)bindings.getControlBinding("Version");
        version = (String)attr.getInputValue();
        attr = (AttributeBinding)bindings.getControlBinding("LastUpdate");
        lastUpdate = (String)attr.getInputValue();

        String performImpact = isImpact ? CSMQBean.TRUE : CSMQBean.FALSE;
        String allGroups = null;

        //String allGroups = this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName + "," + this.defaultReleaseGroupName;
        if (isImpact) {
            allGroups =
                    this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName + "," + this.defaultReleaseGroupName;
        } else {
            allGroups = this.defaultDraftGroupName + "," + this.defaultReleaseGroupName;
        }
        if (!nMQWizardBean.isExportDisplayedOnly())
            currentDisplayedTerms = "";
        if (!nMQWizardBean.isExportDisplayedOnly())
            futureDisplayedTerms = "";

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

        String[] reportList = null;
        if (isImpact)
            reportList = new String[] { existingIAReportName, futureIAReportName };
        else if (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_EXISTING ||
                 nMQWizardBean.getMode() == CSMQBean.MODE_COPY_EXISTING ||
                 nMQWizardBean.getMode() == CSMQBean.MODE_INSERT_NEW)
            reportList = new String[] { defaultExportChangedReportName };
        else
            reportList = new String[] { defaultExportReportName };

        String isViewPreviousFlow =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("isViewPreviousFlow");
        parameters.put("I_FLOW_TYPE",
                       "Y".equals(isViewPreviousFlow) ? "VIEW_PREV_VER_IMPACT" : "VIEW_CURR_VER_IMPACT");

        CSMQBean.logger.info(userBean.getCaller() + " *** RUNNING REPORT ***");

        CSMQBean.logger.info(userBean.getCaller() + " user: " + userBean.getUsername());
        CSMQBean.logger.info(userBean.getCaller() + " I_DICT_CONTENT_ID: " + nMQWizardBean.getCurrentDictContentID());
        CSMQBean.logger.info(userBean.getCaller() + " REPOR_LIST: " + reportList);
        CSMQBean.logger.info(userBean.getCaller() + " REPORT_TITLE: " + reportTitle);
        CSMQBean.logger.info(userBean.getCaller() + " SHOW_FOOTER: " + (isImpact && reportFormat.equals("PDF")));
        CSMQBean.logger.info(userBean.getCaller() + " SHOW_LAST_PAGE_FOOTER: " + isImpact);
        CSMQBean.logger.info(userBean.getCaller() + " ACTIVATION_GROUP: " + allGroups);
        CSMQBean.logger.info(userBean.getCaller() + " DICTIONARY_VERSION: " + version);
        CSMQBean.logger.info(userBean.getCaller() + " DICTIONARY_TIMESTAMP: " + lastUpdate);
        CSMQBean.logger.info(userBean.getCaller() + " ACTIVITY_STATUS: " + nMQWizardBean.getCurrentMQStatus());
        CSMQBean.logger.info(userBean.getCaller() + " INCLUDE_LLTS: " + nMQWizardBean.getIncludeLLTsInExport());
        CSMQBean.logger.info(userBean.getCaller() + " Mode : " + nMQWizardBean.getMode());
        CSMQBean.logger.info(userBean.getCaller() + " I_IGNORE_PREDICT: " + ignorePredict);
        CSMQBean.logger.info(userBean.getCaller() + " ACTIVATION_GROUP: " + allGroups);

        new ReportEngine().exportAsExcelWorkbook(reportList, outputStream, parameters, userBean.getCaller(),
                                                 performImpact);
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

    public void cntrlExportHierarchyPopupCanceled(PopupCanceledEvent popupCanceledEvent) {
    }

    public void cntrlExportPopupLoaded(PopupFetchEvent popupFetchEvent) {
    }

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
        excelFileName = this.getReportTitle() + "_Export.xls";
        return excelFileName;
    }


    public String getVisableRowsFromTable(RichTreeTable tree) {

        ArrayList tempList = new ArrayList();
        CollectionModel model = (CollectionModel)tree.getValue();
        //ArrayList node = (ArrayList) model.getWrappedData();
        ArrayList nodes = (ArrayList)model.getWrappedData();
        GenericTreeNode n = (GenericTreeNode)nodes.get(0);
        System.out.println("CHECKING TREE: " + tree);
        searchTreeNode(n, tempList);
        return StringUtils.join(tempList.iterator(), ',');
    }

    private void searchTreeNode(GenericTreeNode node, ArrayList visibleNodes) {

        visibleNodes.add(node.getPrikey());

        List<GenericTreeNode> children = node.getChildren();
        if (children != null) {
            for (GenericTreeNode _node : children)
                searchTreeNode(_node, visibleNodes);

        }

    }

    private void loadMQDetailedDetailedTermHierarchyInformation(BigDecimal dictContentId, String tMSRecordStatus,
                                                                String activationGroup) {
        System.out.println("Start Exec loadMQDetailedDetailedTermHierarchyInformation() dictContentId=" +
                           dictContentId);
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("loadMQDetailedDetailedTermHierarchyInformation");
        ob.getParamsMap().put("dictContentId", dictContentId);
        ob.getParamsMap().put("tMSRecordStatus", tMSRecordStatus);
        ob.getParamsMap().put("activationGroup", activationGroup);
        ob.execute();
        System.out.println("End of Exec loadMQDetailedDetailedTermHierarchyInformation() ");
    }

    public void downloadMQDetailedReport(FacesContext facesContext, OutputStream outputStream) {
        CSMQBean.logger.info(userBean.getCaller() + "Start exec downloadMQDetailedReport() ");
        CSMQBean.logger.info(userBean.getCaller() + " user: " + userBean.getUsername());
        CSMQBean.logger.info(userBean.getCaller() + " I_DICT_CONTENT_ID: " + nMQWizardBean.getCurrentDictContentID());
        CSMQBean.logger.info(userBean.getCaller() + " CurrentStatus: " + nMQWizardBean.getCurrentStatus());
        CSMQBean.logger.info(userBean.getCaller() + " ActiveDictionary: " + nMQWizardBean.getActiveDictionary());
        NMQWizardSearchBean nMQWizardSearchBean =
            (NMQWizardSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardSearchBean");
        System.out.println("TermHierarchyBean.java createTree() nMQWizardSearchBean.isHistoryFlow() =" +
                           nMQWizardSearchBean.isHistoryFlow());
        if (nMQWizardSearchBean.isHistoryFlow()) {
            generateHistoryMQDetailedReport(outputStream);
        } else {
            generateMQDetailedReport(outputStream);
        }
    }

    public void generateMQDetailedReport(OutputStream outputStream) {
        CSMQBean.logger.info(userBean.getCaller() + "Start exec downloadMQDetailedReport() ");
        CSMQBean.logger.info(userBean.getCaller() + " user: " + userBean.getUsername());
        CSMQBean.logger.info(userBean.getCaller() + " I_DICT_CONTENT_ID: " + nMQWizardBean.getCurrentDictContentID());
        CSMQBean.logger.info(userBean.getCaller() + " CurrentStatus: " + nMQWizardBean.getCurrentStatus());
        CSMQBean.logger.info(userBean.getCaller() + " getCurrentReleaseGroup: " +
                             nMQWizardBean.getCurrentReleaseGroup());
        loadMQDetailedDetailedTermHierarchyInformation(new BigDecimal(nMQWizardBean.getCurrentDictContentID()),
                                                       nMQWizardBean.getCurrentStatus(),
                                                       nMQWizardBean.getCurrentReleaseGroup());
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("MQ Detailed Report");
            rowCount = 0;
            BindingContext bc = BindingContext.getCurrent();
            DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
            DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("TermDetailsVO1Iterator");
            Row termDtlRow = dciterb.getCurrentRow();
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            worksheet.addMergedRegion(new CellRangeAddress(0, rowCount, 0, 5));
            String logoPath = sourceDirectory + "/app_logo.png";
            POIExportUtil.writeImageTOExcel(worksheet, POIExportUtil.loadResourceAsStream(logoPath));

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addHeaderTextRow(worksheet, rowCount++, nMQWizardBean.getCurrentTermName(), 6);

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Filter Dictionary:",
                                     (String)termDtlRow.getAttribute("DictionaryName"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Release Group:",
                                     (String)termDtlRow.getAttribute("GroupName"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Term:", (String)termDtlRow.getAttribute("Term"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Product List:", (String)termDtlRow.getAttribute("Value3"),
                                     3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Product List (Expanded):",
                                     (String)termDtlRow.getAttribute("MqproductExpanded"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Designee: ", (String)termDtlRow.getAttribute("Designee"),
                                     3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Level:", (String)termDtlRow.getAttribute("LevelName"), 3,
                                     3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Code:",
                                     (String)termDtlRow.getAttribute("DictContentCode"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Original Algorithm:",
                                     (String)termDtlRow.getAttribute("Value1"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Level Extension:",
                                     (String)termDtlRow.getAttribute("LevelExtension"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Critical Event:",
                                     (String)termDtlRow.getAttribute("Value4"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Query Group:", (String)termDtlRow.getAttribute("Value2"),
                                     3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Query Group (Expanded):",
                                     (String)termDtlRow.getAttribute("MqgroupExpanded"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Status:", (String)termDtlRow.getAttribute("Status"), 3,
                                     3);
            String dmlStatement = (String)termDtlRow.getAttribute("DmlStatement");
            if (dmlStatement == null) {
                POIExportUtil.addFormRow(worksheet, rowCount++, "Release Status:", "CURRENT", 3, 3);
            } else {
                POIExportUtil.addFormRow(worksheet, rowCount++, "Release Status:", "PENDING", 3, 3);
            }
            POIExportUtil.addFormRow(worksheet, rowCount++, "State:", (String)termDtlRow.getAttribute("State"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Scope Analysis:",
                                     (String)termDtlRow.getAttribute("Category"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Initial Creation By:",
                                     (String)termDtlRow.getAttribute("FirstActivationBy"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Initial Creation Date:",
                                     termDtlRow.getAttribute("FirstActivationTs") != null ?
                                     termDtlRow.getAttribute("FirstActivationTs").toString() : "", 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Last Creation By:",
                                     (String)termDtlRow.getAttribute("LastActivationBy"), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Last Creation Date:",
                                     termDtlRow.getAttribute("LastActivationTs") != null ?
                                     termDtlRow.getAttribute("LastActivationTs").toString() : "", 3, 3);

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "Description:", 4);
            POIExportUtil.addSimpleDescTextRow(worksheet, rowCount++, (String)termDtlRow.getAttribute("SmqDesc"), 10);
            rowCount++;

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "Source:", 4);
            POIExportUtil.addSimpleDescTextRow(worksheet, rowCount++, (String)termDtlRow.getAttribute("SmqSrc"), 10);
            rowCount++;

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "Note:", 4);
            POIExportUtil.addSimpleDescTextRow(worksheet, rowCount++, (String)termDtlRow.getAttribute("SmqNote"), 10);
            rowCount++;

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "RELATION HIERARCHY:", 4);
            String[] headerArr = { "Term", "Code", "Level", "Category", "Weight", "Scope" };
            int[] colSpan = { 5, 1, 1, 1, 1, 1 };
            POIExportUtil.addHierarchyTableHeaderRow(worksheet, rowCount++, headerArr, colSpan);
            /*
            DCIteratorBinding dciterb1 = (DCIteratorBinding)binding.get("TermHierarchyDetailsVO1Iterator");
            Enumeration rows = dciterb1.getRowSetIterator().enumerateRowsInRange();

            if (rows != null && rows.hasMoreElements()) {
                Map<oracle.jbo.domain.Number, String> levelNameMap = new HashMap<oracle.jbo.domain.Number, String>();
                Row termHierarchyDtlRow = (Row)rows.nextElement();
                String[] valArr = new String[6];
                valArr[0] = (String)termHierarchyDtlRow.getAttribute("RootTerm");
                valArr[1] = (String)termHierarchyDtlRow.getAttribute("RootDictContentCode");
                valArr[2] = (String)termHierarchyDtlRow.getAttribute("RootLevelName");
                valArr[3] = "N/A";
                valArr[4] = "N/A";
                valArr[5] = "Full " + (String)termHierarchyDtlRow.getAttribute("RootLevelExtension") + "/SMQ";
                POIExportUtil.addHierarchyTableValueRow(worksheet, rowCount++, valArr, colSpan, 0);

                do {
                    valArr = new String[6];
                    valArr[0] = (String)termHierarchyDtlRow.getAttribute("DtlsTerm");
                    valArr[1] = (String)termHierarchyDtlRow.getAttribute("DtlsDictContentCode");
                    valArr[2] = (String)termHierarchyDtlRow.getAttribute("DtlsLevelName");
                    valArr[3] = (String)termHierarchyDtlRow.getAttribute("RelRelationCategory");
                    valArr[4] = (String)termHierarchyDtlRow.getAttribute("RelRelationWeight");
                    valArr[5] =
                            getLevelName(levelNameMap, (String)termHierarchyDtlRow.getAttribute("RelRelationScope"),
                                         (oracle.jbo.domain.Number)termHierarchyDtlRow.getAttribute("DtlsDictContentId"),
                                         (oracle.jbo.domain.Number)termHierarchyDtlRow.getAttribute("MstrDictContentId"));
                    oracle.jbo.domain.Number relDepthFromRoot =
                        (oracle.jbo.domain.Number)termHierarchyDtlRow.getAttribute("RelDepthFromRoot");
                    POIExportUtil.addHierarchyTableValueRow(worksheet, rowCount++, valArr, colSpan,
                                                            relDepthFromRoot.intValue() + 1);
                    termHierarchyDtlRow = (Row)rows.nextElement();
                } while (rows.hasMoreElements());

            }
            */
            String scope = (String)termDtlRow.getAttribute("Category");
            boolean scopeFlag = true;
            if ("No".equalsIgnoreCase(scope) || "N".equalsIgnoreCase(scope)) {
                scopeFlag = false;
            }
            CSMQBean.logger.info(userBean.getCaller() + "generateHistoryMQDetailedReport() scopeFlag=" + scopeFlag);

            generateMQDtlReportTermHierarchyDetails(worksheet, new Integer(nMQWizardBean.getCurrentDictContentID()),
                                                    nMQWizardBean.getCurrentStatus(),
                                                    nMQWizardBean.getCurrentReleaseGroup(), scopeFlag);

            worksheet.createFreezePane(0, 1, 0, 1);

            for (int x = 0; x < 8; x++) {
                worksheet.autoSizeColumn(x);
            }
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CSMQBean.logger.info(userBean.getCaller() + "End of exec downloadMQDetailedReport() ");
    }

    private void generateMQDtlReportTermHierarchyDetails(HSSFSheet worksheet, Integer dictContentId,
                                                         String tMSRecordStatus, String activationGroup,
                                                         boolean scopeFlag) {
        Datum[] elements = null;
        int[] colSpan = { 5, 1, 1, 1, 1, 1 };
        CSMQBean.logger.info(userBean.getCaller() + "Start exec generateMQDtlReportTermHierarchyDetails() ");
        CSMQBean.logger.info(userBean.getCaller() + " dictContentId: " + dictContentId);
        CSMQBean.logger.info(userBean.getCaller() + " tMSRecordStatus: " + tMSRecordStatus);
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + activationGroup);
        CSMQBean.logger.info(userBean.getCaller() + " scopeFlag: " + scopeFlag);

        OracleCallableStatement cstmt = null;
        try {
            DBTransaction dBTransaction = DMLUtils.getDBTransaction();
            cstmt =
                    (OracleCallableStatement)dBTransaction.createCallableStatement(MQ_DTL_REPORT_SQL, DBTransaction.DEFAULT);
            cstmt.registerOutParameter(1, OracleTypes.ARRAY, "NMAT_DICT_HIERARCHY_QRY_OBJT");
            cstmt.setString(2, tMSRecordStatus);
            cstmt.setInt(3, dictContentId.intValue());
            cstmt.setString(4, activationGroup);
            cstmt.execute();
            ARRAY array_to_pass = cstmt.getARRAY(1);
            elements = array_to_pass.getOracleArray();;
//            oracle.sql.ARRAY array_to_pass = ((weblogic.jdbc.wrapper.Array)(cstmt).getObject(1)).unwrap(ARRAY.class);
//            elements = array_to_pass.getOracleArray();

            if (elements != null && elements.length > 0) {
                Map<java.math.BigDecimal, String> levelNameMap = new HashMap<java.math.BigDecimal, String>();
                Object[] element = ((STRUCT)elements[0]).getAttributes();
                String[] valArr = new String[6];
                valArr[0] = (String)element[6]; //root_term
                valArr[1] = (String)element[5]; //root_dict_content_code
                valArr[2] = (String)element[2]; //root_level_name
                valArr[3] = scopeFlag ? "N/A" : "";
                valArr[4] = scopeFlag ? "N/A" : "";
                valArr[5] = scopeFlag ? "Full " + (String)element[3] + "/SMQ" : "";
                POIExportUtil.addHierarchyTableValueRow(worksheet, rowCount++, valArr, colSpan, 0);

                for (int i = 0; i < elements.length; i++) {
                    element = ((STRUCT)elements[i]).getAttributes();
                    valArr = new String[6];
                    valArr[0] = (String)element[34]; //dtls_term
                    valArr[1] = (String)element[33]; //dtls_dict_content_code
                    valArr[2] = (String)element[29]; //dtls_level_name
                    valArr[3] = scopeFlag ? (String)element[46] : ""; //rel_relation_category
                    valArr[4] = scopeFlag ? (String)element[47] : ""; //rel_relation_weight
                    valArr[5] =
                            scopeFlag ? getLevelName(levelNameMap, (String)element[45], (java.math.BigDecimal)element[32],
                                                     (java.math.BigDecimal)element[18]) : "";
                    java.math.BigDecimal relDepthFromRoot = (java.math.BigDecimal)element[54]; // rel_depth_from_root
                    POIExportUtil.addHierarchyTableValueRow(worksheet, rowCount++, valArr, colSpan,
                                                            relDepthFromRoot.intValue() + 1);
                }

            }

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            CSMQBean.logger.error("error while generateMQDtlReportTermHierarchyDetails()..." + e);
        } finally {
            if (null != cstmt) {
                try {
                    cstmt.close();
                } catch (SQLException sqe) {
                    CSMQBean.logger.error("error while closing callable statment...");
                }
            }
        }
        CSMQBean.logger.info(userBean.getCaller() + " End of generateMQDtlReportTermHierarchyDetails() ");
    }

    public void generateHistoryMQDetailedReport(OutputStream outputStream) {
        CSMQBean.logger.info(userBean.getCaller() + "Start exec generateHistoryMQDetailedReport() ");
        CSMQBean.logger.info(userBean.getCaller() + " user: " + userBean.getUsername());
        CSMQBean.logger.info(userBean.getCaller() + " I_DICT_CONTENT_ID: " + nMQWizardBean.getCurrentDictContentID());
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("MQ Detailed Report");
            rowCount = 0;
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            POIExportUtil.addImageRow(worksheet, rowCount++);
            worksheet.addMergedRegion(new CellRangeAddress(0, rowCount, 0, 5));
            String logoPath = sourceDirectory + "/app_logo.png";
            POIExportUtil.writeImageTOExcel(worksheet, POIExportUtil.loadResourceAsStream(logoPath));

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            NMQWizardSearchBean nMQWizardSearchBean =
                (NMQWizardSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardSearchBean");
            if (nMQWizardSearchBean != null) {
                POIExportUtil.addSimpleTextRow(worksheet, rowCount++,
                                               "Historic View as of  " + nMQWizardSearchBean.getHistoryInputDateStr(),
                                               5);
            }

            POIExportUtil.addFormRow(worksheet, rowCount++, "Filter Dictionary:", nMQWizardBean.getCurrentDictionary(),
                                     3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Release Group:", "", 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Term:", nMQWizardBean.getCurrentTermName(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Product List:", nMQWizardBean.getCurrentProduct(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Product List (Expanded):",
                                     nMQWizardBean.getCurrentProduct(), 3, 3);
            //            POIExportUtil.addFormRow(worksheet, rowCount++, "Designee: ", nMQWizardBean.getDesigneeListAsString(), 3,
            //                                     3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Level:", nMQWizardBean.getCurrentTermLevel(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Code:", nMQWizardBean.getCurrentContentCode(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Original Algorithm:", nMQWizardBean.getCurrentMQALGO(), 3,
                                     3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Level Extension:", nMQWizardBean.getCurrentExtension(), 3,
                                     3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Critical Event:", nMQWizardBean.getCurrentMQCRTEV(), 3,
                                     3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Query Group:", nMQWizardBean.getCurrentMQGROUP(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Query Group (Expanded):",
                                     nMQWizardBean.getCurrentMQGROUP(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Status:", nMQWizardBean.getCurrentMQStatus(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Release Status:", nMQWizardBean.getCurrentStatus(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "State:", nMQWizardBean.getCurrentState(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Scope Analysis:", nMQWizardBean.getCurrentScope(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Initial Creation By:",
                                     nMQWizardBean.getCurrentInitialCreationBy(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Initial Creation Date:",
                                     nMQWizardBean.getCurrentInitialCreationDate(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Last Creation By:",
                                     nMQWizardBean.getCurrentActivationBy(), 3, 3);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Last Creation Date:",
                                     nMQWizardBean.getCurrentLastActivationDate(), 3, 3);

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "Description:", 4);
            POIExportUtil.addSimpleDescTextRow(worksheet, rowCount++, nMQWizardBean.getCurrentInfNoteDescription(),
                                               10);
            rowCount++;

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "Source:", 4);
            POIExportUtil.addSimpleDescTextRow(worksheet, rowCount++, nMQWizardBean.getCurrentInfNoteSource(), 10);
            rowCount++;

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "Note:", 4);
            POIExportUtil.addSimpleDescTextRow(worksheet, rowCount++, nMQWizardBean.getCurrentInfNoteNotes(), 10);
            rowCount++;

            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addDescHeaderTextRow(worksheet, rowCount++, "RELATION HIERARCHY:", 4);
            String[] headerArr = { "Term", "Code", "Level", "Category", "Weight", "Scope" };
            int[] colSpan = { 5, 1, 1, 1, 1, 1 };
            POIExportUtil.addHierarchyTableHeaderRow(worksheet, rowCount++, headerArr, colSpan);

            boolean scopeFlag = true;
            if ("No".equalsIgnoreCase(nMQWizardBean.getCurrentScope()) ||
                "N".equalsIgnoreCase(nMQWizardBean.getCurrentScope())) {
                scopeFlag = false;
            }
            CSMQBean.logger.info(userBean.getCaller() + "generateHistoryMQDetailedReport() scopeFlag=" + scopeFlag);

            TermHierarchyBean termHierarchyBean =
                (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
            GenericTreeNode root = termHierarchyBean.getRoot();
            generateHierarchyRow(worksheet, root, colSpan, new Integer("0"), scopeFlag);

            worksheet.createFreezePane(0, 1, 0, 1);

            for (int x = 0; x < 8; x++) {
                worksheet.autoSizeColumn(x);
            }
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CSMQBean.logger.info(userBean.getCaller() + "End of exec generateHistoryMQDetailedReport() ");
    }

    private void generateHierarchyRow(HSSFSheet worksheet, GenericTreeNode node, int[] colSpan, Integer depth) {
        CSMQBean.logger.info(userBean.getCaller() + "Start of exec generateHierarchyRow() ");
        String[] valArr = new String[6];
        valArr[0] = node.getTerm();
        valArr[1] = node.getDictContentCode();
        valArr[2] = node.getLevelName();
        valArr[3] = node.getTermCategory();
        valArr[4] = node.getTermWeight();
        valArr[5] = node.getScopeName();
        CSMQBean.logger.info(userBean.getCaller() + "generateHierarchyRow() Term=" + valArr[0]);
        CSMQBean.logger.info(userBean.getCaller() + "generateHierarchyRow() rowCount=" + rowCount);
        CSMQBean.logger.info(userBean.getCaller() + "generateHierarchyRow() depth=" + depth);
        POIExportUtil.addHierarchyTableValueRow(worksheet, rowCount++, valArr, colSpan, depth);
        if (node.getChildren() != null && node.getChildren().size() > 0) {
            GenericTreeNode cnode = null;
            depth++;
            for (int i = 0; i < node.getChildren().size(); i++) {
                cnode = (GenericTreeNode)node.getChildren().get(i);
                generateHierarchyRow(worksheet, cnode, colSpan, depth);
            }
        }
        CSMQBean.logger.info(userBean.getCaller() + "End of exec generateHierarchyRow() ");
    }

    private void generateHierarchyRow(HSSFSheet worksheet, GenericTreeNode node, int[] colSpan, Integer depth,
                                      boolean scopeFlag) {
        CSMQBean.logger.info(userBean.getCaller() + "Start of exec generateHierarchyRow() ");
        String[] valArr = new String[6];
        valArr[0] = node.getTerm();
        valArr[1] = node.getDictContentCode();
        valArr[2] = node.getLevelName();
        valArr[3] = scopeFlag ? node.getTermCategory() : "";
        valArr[4] = scopeFlag ? node.getTermWeight() : "";
        valArr[5] = scopeFlag ? node.getScopeName() : "";
        CSMQBean.logger.info(userBean.getCaller() + "generateHierarchyRow() Term=" + valArr[0]);
        CSMQBean.logger.info(userBean.getCaller() + "generateHierarchyRow() rowCount=" + rowCount);
        CSMQBean.logger.info(userBean.getCaller() + "generateHierarchyRow() depth=" + depth);
        POIExportUtil.addHierarchyTableValueRow(worksheet, rowCount++, valArr, colSpan, depth);
        if (node.getChildren() != null && node.getChildren().size() > 0) {
            GenericTreeNode cnode = null;
            depth++;
            for (int i = 0; i < node.getChildren().size(); i++) {
                cnode = (GenericTreeNode)node.getChildren().get(i);
                generateHierarchyRow(worksheet, cnode, colSpan, depth, scopeFlag);
            }
        }
        CSMQBean.logger.info(userBean.getCaller() + "End of exec generateHierarchyRow() ");
    }

    private String getLevelName(Map<java.math.BigDecimal, String> levelNameMap, String dtlLevelName,
                                java.math.BigDecimal dtlDictContentId, java.math.BigDecimal masterDictContentId) {
        String levelName = dtlLevelName;
        if ("0".equals(dtlLevelName)) {
            levelName = levelNameMap.get(masterDictContentId);
        } else {
            if (dtlLevelName.contains("BROAD")) {
                levelName = "Broad";
            } else if (dtlLevelName.contains("NARROW")) {
                levelName = "Narrow";
            }
            levelNameMap.put(dtlDictContentId, levelName);
        }
        return levelName;
    }


    public void setMqDtlReportExcelFileName(String mqDtlReportExcelFileName) {
        this.mqDtlReportExcelFileName = mqDtlReportExcelFileName;
    }

    public String getMqDtlReportExcelFileName() {
        mqDtlReportExcelFileName = this.getReportTitle() + "_MQ_Detailed_Report.xls";
        return mqDtlReportExcelFileName;
    }
}
