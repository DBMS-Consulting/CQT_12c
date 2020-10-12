package com.dbms.csmq.view.reports;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.model.report.MedraQueryReportRVOImpl;
import com.dbms.csmq.view.backing.NMQ.NMQUtils;
import com.dbms.util.POIExportUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.naming.InitialContext;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichGoButton;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;

import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;


public class ReportBean {

    private final String dbUrlString = "jdbc/TMSDS";
    private RichSelectOneChoice cntrlReportList;
    private RichSelectOneChoice cntrlFormatList;
    //private RichSelectManyChoice cntrlStateList;
    //private RichSelectOneChoice cntrlReleaseGroupList;
    //private RichSelectOneChoice cntrlRequestorList;
    private RichInputDate cntrlStartDate;
    private RichInputDate cntrlEndDate;

    private String reportName;
    private String reportFormat = "PDF";
    private String paramRequestor = "NULL";
    private String paramReleaseGroup = "NULL";
    private Date paramStartDate = null;
    private Date paramEndDate = null;
    private oracle.jbo.domain.Date paramLastActivationDate = null;

    private String paramProposed = CSMQBean.TRUE;
    private String paramRequested = CSMQBean.TRUE;
    private String paramDraft = CSMQBean.TRUE;
    private String paramPending = CSMQBean.TRUE;
    private String paramReviewed = CSMQBean.TRUE;
    private String paramApproved = CSMQBean.TRUE;
    private String paramPublished = CSMQBean.TRUE;
    private String paramActivated = CSMQBean.TRUE;
    private String paramRetired = CSMQBean.TRUE;
    private String paramReActivated = CSMQBean.TRUE;

    public static final String GROUP_DELIMETER = ",";
    private RichGoButton cntrlOpenReport;

    private String reportURL;
    //private RichSelectBooleanCheckbox cntrlProposed;
    //private RichSelectBooleanCheckbox cntrlRequested;
    //private RichSelectBooleanCheckbox cntrlDraft;
    //private RichSelectBooleanCheckbox cntrlReviewed;
    //private RichSelectBooleanCheckbox cntrlPublished;
    //private RichSelectBooleanCheckbox cntrlPending;
    //private RichSelectBooleanCheckbox cntrlApproved;
    //private RichSelectBooleanCheckbox cntrlActivated;
    //private RichSelectBooleanCheckbox cntrlRetired;

    /**
     */
    private RichSelectBooleanCheckbox cntrlReactivated;

    private ArrayList<SelectItem> releaseGroupSelectItems;
    private CSMQBean cSMQBean;
    private UserBean userBean;
    private String generatedReport;
    private RichCommandButton generateReportButton;
    private String version;

    public ReportBean() {
        cSMQBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        releaseGroupSelectItems = cSMQBean.getAGsForDictionary(null);
        userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
        userBean.setCurrentMenuPath("Reports");
        userBean.setCurrentMenu("REPORTS");
    }

    public void printReport(FacesContext facesContext, OutputStream outputStream) {

        reportName = cntrlReportList.getValue().toString();
        reportFormat = cntrlFormatList.getValue().toString();
        String sourceDirectory = CSMQBean.getProperty("REPORT_SOURCE");
        String reportFile = sourceDirectory + reportName + ".jrxml";
        boolean medraQueryReport = false;
        if (cntrlStartDate.getValue() != null) {
            paramStartDate = (Date)cntrlStartDate.getValue();
        }
        if (cntrlEndDate.getValue() != null) {
            paramEndDate = (Date)cntrlEndDate.getValue();
        }
        java.sql.Date lastActDate = null;

        // Code to get the last activation date from function if start date and end dates are null from UI
        if (!reportName.equalsIgnoreCase("CURRENT_MEDDRA_QUERIES")) {
            medraQueryReport = true;
            if (null != paramEndDate) {
                long milliInADay = 1000 * 60 * 60 * 24;
                lastActDate =
                        NMQUtils.getLastActivationDate(null, new java.sql.Date(paramEndDate.getTime() + milliInADay));
            } else {
                lastActDate = NMQUtils.getLastActivationDate(null, null);
            }
            if (null == paramEndDate && null != lastActDate) {
            paramEndDate = new java.util.Date(lastActDate.getTime());
            }
            if (null == paramStartDate && null != lastActDate) {
                paramStartDate = new java.util.Date(lastActDate.getTime());
            }
        }

        Map parameters = new HashMap();
        parameters.put("ReportTitle", reportName);
        parameters.put("reportFormat", reportFormat);
        parameters.put("REPORT_DIRECTORY", sourceDirectory);
        parameters.put("release_group", paramReleaseGroup);
        parameters.put("requestor", paramRequestor);
        parameters.put("trans_start_date", paramStartDate);
        parameters.put("trans_end_date", paramEndDate);
        parameters.put("proposed_yn", paramProposed);
        parameters.put("requested_yn", paramRequested);
        parameters.put("draft_yn", paramDraft);
        parameters.put("pending_yn", paramPending);
        parameters.put("reviewed_yn", paramReviewed);
        parameters.put("approved_yn", paramApproved);
        parameters.put("published_yn", paramPublished);
        parameters.put("activated_yn", paramActivated);
        parameters.put("reactivated_yn", paramReActivated);
        parameters.put("retired_yn", paramRetired);
        //parameters.put("DICTIONARY_VERSION", version);
        //parameters.put("LAST_ACTIVATON_DATE", this.getParamLastActivationDate());


        CSMQBean.logger.info(userBean.getCaller() + " *** RUNNING REPORT ***");
        CSMQBean.logger.info(userBean.getCaller() + " user: " + userBean.getUsername());
        CSMQBean.logger.info(userBean.getCaller() + " ReportTitle: " + reportName);
        CSMQBean.logger.info(userBean.getCaller() + " reportSourceFile: " + reportFile);
        CSMQBean.logger.info(userBean.getCaller() + " generatedReport: " + reportName);
        CSMQBean.logger.info(userBean.getCaller() + " REPORT_DIRECTORY: " + sourceDirectory);
        CSMQBean.logger.info(userBean.getCaller() + " release_group: " + paramReleaseGroup);
        CSMQBean.logger.info(userBean.getCaller() + " requestor: " + paramRequestor);
        CSMQBean.logger.info(userBean.getCaller() + " trans_start_date: " + paramStartDate);
        CSMQBean.logger.info(userBean.getCaller() + " trans_end_date: " + paramEndDate);
        CSMQBean.logger.info(userBean.getCaller() + " proposed_yn: " + paramProposed);
        CSMQBean.logger.info(userBean.getCaller() + " requested_yn: " + paramRequested);
        CSMQBean.logger.info(userBean.getCaller() + " draft_yn: " + paramDraft);
        CSMQBean.logger.info(userBean.getCaller() + " pending_yn: " + paramPending);
        CSMQBean.logger.info(userBean.getCaller() + " reviewed_yn: " + paramReviewed);
        CSMQBean.logger.info(userBean.getCaller() + " approved_yn: " + paramApproved);
        CSMQBean.logger.info(userBean.getCaller() + " published_yn: " + paramPublished);
        CSMQBean.logger.info(userBean.getCaller() + " activated_yn: " + paramActivated);
        CSMQBean.logger.info(userBean.getCaller() + " reactivated_yn: " + paramReActivated);
        CSMQBean.logger.info(userBean.getCaller() + " retired_yn: " + paramRetired);
        //CSMQBean.logger.info(userBean.getCaller() + " LAST_ACTIVATON_DATE: " +  this.getParamLastActivationDate());
       
        
            if (reportFormat.equals("PDF")) {
                    Connection conn = null;
                    try {
                        InitialContext initialContext = new InitialContext();
                        //DataSource ds = (DataSource)initialContext.lookup(dbUrlString); // get from your application module configuration
                        DataSource ds = (DataSource)initialContext.lookup(CSMQBean.getProperty("DATABASE_URL")); // get from your application module configuration
                        conn = ds.getConnection();
                        InputStream is = new FileInputStream(new File(reportFile));
                        JasperDesign jasperDesign = JRXmlLoader.load(is);
                        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                
                is.close();
                //conn.close();
                outputStream.close();
                
                } catch (Exception e) {
                e.printStackTrace();
                } finally {
                if (null != conn) {
                    try {
                        conn.close();
                    } catch (SQLException sqe) {
                        CSMQBean.logger.info("error while closing connection...");
                    }
                }
                }
            } else if (reportFormat.equals("XLS")) {
                
//                JRXlsExporter exporterXLS = new JRXlsExporter();
//                                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
//                                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
//                                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
//                                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
//                                exporterXLS.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
//                                                         Boolean.TRUE);
//                                exporterXLS.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
//                                                         Boolean.TRUE);
//
//            try {
//                exporterXLS.exportReport();
//            } catch (JRException e) {
//            }
            if(medraQueryReport){
                DCBindingContainer bindings = this.getDCBindingContainer();
                DCIteratorBinding itrBinding = bindings.findIteratorBinding("MedraQueryReportIterator");
                ViewObject vo = itrBinding.getViewObject();
                MedraQueryReportRVOImpl medraQueryReportRVOImpl = (MedraQueryReportRVOImpl)vo;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                medraQueryReportRVOImpl.setbindStartDate(sdf.format(paramStartDate));
                medraQueryReportRVOImpl.setbindEndDate(sdf.format(paramEndDate));
                medraQueryReportRVOImpl.executeQuery();
                try {
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet worksheet = workbook.createSheet("MEDDRA QUERY NMQ REPORT");
                    HSSFRow excelrow = null;

                    int i = 0;
                    POIExportUtil.addImageRow(worksheet, i++);
                    POIExportUtil.addImageRow(worksheet, i++);
                    POIExportUtil.addImageRow(worksheet, i++);
                    POIExportUtil.addImageRow(worksheet, i++);
                    POIExportUtil.addImageRow(worksheet, i++);
                    worksheet.addMergedRegion(new CellRangeAddress(0, i, 0, 5));
                    String logoPath = sourceDirectory + "/app_logo.png";
                    POIExportUtil.writeImageTOExcel(worksheet, POIExportUtil.loadResourceAsStream(logoPath));
                    //POIExportUtil pOIExportUtil = new POIExportUtil();
                    //POIExportUtil.writeImageTOExcel(worksheet, pOIExportUtil.getImageInpStream());
                    int colCount = 0;
                    excelrow = (HSSFRow) worksheet.createRow((short) i);
                    HSSFCell cellA1 = excelrow.createCell((short) 0);
                    
                    excelrow = (HSSFRow) worksheet.createRow((short) i);
                    HSSFCell cellA2 = excelrow.createCell((short) 1);

                    i++;

                    POIExportUtil.addHeaderTextRow(worksheet, i++, "MedDRA Query Report", 6);
                    
                    POIExportUtil.addFormRow(worksheet, i++, "Between "+new SimpleDateFormat("dd-MMM-yyyy").format(paramStartDate).toString()+" and "+new SimpleDateFormat("dd-MMM-yyyy").format(paramEndDate).toString(),null,5, 1);

                    i++;
                    int k = i;


                    RowSetIterator rs = medraQueryReportRVOImpl.createRowSetIterator(null);
                    List<String> extensionList = new ArrayList<String>();
                    List<String> extension = null;
                    while (rs.hasNext()) {
                        Row row = rs.next();
                        //print header on first row in excel
                        if (i == k) {
                            CellStyle styleNew = worksheet.getWorkbook().createCellStyle();
                            Font font = worksheet.getWorkbook().createFont();
                            font.setFontHeightInPoints((short)10);
                            font.setFontName("Arial");
                            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                            font.setColor(new HSSFColor.BLACK().getIndex());
                            styleNew.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
                            //styleNew.setFillPattern(CellStyle.FINE_DOTS);
                            styleNew.setFont(font);
                            excelrow = (HSSFRow) worksheet.createRow((short) i);
                            
                            cellA1 = excelrow.createCell((short) 0);
                            excelrow.createCell((short) 1);
                            worksheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
                            cellA1.setCellValue("NMQ Code");
                            cellA1.setCellStyle(styleNew);
                            
                            cellA1 = excelrow.createCell((short) 2);
                            excelrow.createCell((short) 3);
                            excelrow.createCell((short) 4);
                            excelrow.createCell((short) 5);
                            excelrow.createCell((short) 6);
                            worksheet.addMergedRegion(new CellRangeAddress(i, i, 2, 6));
                            cellA1.setCellValue("NMQ Name");
                            cellA1.setCellStyle(styleNew);
                            
                            
                            cellA1 = excelrow.createCell((short) 7);
                            excelrow.createCell((short) 8);
                            worksheet.addMergedRegion(new CellRangeAddress(i, i, 7, 8));
                            cellA1.setCellValue("Published Date");
                            cellA1.setCellStyle(styleNew);
                            
                        }

                        //print data from second row in excel
                        i++;
                        
                        CellStyle styleNew = worksheet.getWorkbook().createCellStyle();
                        Font font = worksheet.getWorkbook().createFont();
                        font.setFontHeightInPoints((short)10);
                        font.setFontName("Arial");
                        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                        font.setColor(new HSSFColor.BLACK().getIndex());
                        styleNew.setFont(font);
                        //excelrow = (HSSFRow) worksheet.createRow((short) i);   
                        
                        if((!extensionList.contains(row.getAttribute("ApprovalReason"))) || (!extension.contains(row.getAttribute("Extension")))){
                        if(!extensionList.contains(row.getAttribute("ApprovalReason"))){   
                        excelrow = worksheet.createRow((short) i);
                        cellA1 = excelrow.createCell((short) 0);
                        cellA1.setCellValue(row.getAttribute("ApprovalReason") + "");
                        cellA1.setCellStyle(styleNew);
                        extensionList.add(row.getAttribute("ApprovalReason") + "");
                        extension = new ArrayList<String>();
                        i++;
                        }
                        if(!extension.contains(row.getAttribute("Extension"))){
                            excelrow = worksheet.createRow((short) i);
                            cellA1 = excelrow.createCell((short) 1);
                            cellA1.setCellValue(row.getAttribute("Extension") + "");
                            cellA1.setCellStyle(styleNew);
                            extension.add(row.getAttribute("Extension") +  "");
                            i++;   
                        }
                        }

                        
                        
                        excelrow = worksheet.createRow((short) i);
                        
                        HSSFCell cell = excelrow.createCell(0);
                        excelrow.createCell(1);
                        worksheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
                        cell.setCellValue(row.getAttribute("DictContentCode") + "");
                        
                        cell = excelrow.createCell(2);
                        excelrow.createCell(3);
                        excelrow.createCell(4);
                        excelrow.createCell(5);
                        excelrow.createCell(6);
                        worksheet.addMergedRegion(new CellRangeAddress(i, i, 2, 6));
                        cell.setCellValue(row.getAttribute("Term")+ "");
                        
                        cell = excelrow.createCell(7);
                        excelrow.createCell(8);
                        worksheet.addMergedRegion(new CellRangeAddress(i, i, 7, 8));
                        oracle.jbo.domain.Date date = (oracle.jbo.domain.Date)row.getAttribute("TransactionDate");
                        cell.setCellValue(new SimpleDateFormat("dd-MMM-yyyy").format(date.dateValue()).toString()+ "");                    
                    }
                    
                    i++;
                    i++;
                //                excelrow = (HSSFRow) worksheet.createRow((short) i);
                //                cellA1 = excelrow.createCell((short) 0);
                //                cellA1.setCellValue("Row Count");
                //                cellA2 = excelrow.createCell((short) 1);
                //                cellA2.setCellValue(itrBinding.getEstimatedRowCount());
                    
                    worksheet.createFreezePane(0, 1, 0, 1);

                    for (int x = 0; x < colCount; x++) {
                        worksheet.autoSizeColumn(x);
                    }
                    workbook.write(outputStream);
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }else{
            DCBindingContainer bindings = this.getDCBindingContainer();
            DCIteratorBinding itrBinding = bindings.findIteratorBinding("GenerateCurrentMedraQuery1Iterator");
            ViewObject vo = itrBinding.getViewObject();
        
            try {
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet worksheet = workbook.createSheet("Search Report");
                HSSFRow excelrow = null;

                int i = 0;
                POIExportUtil.addImageRow(worksheet, i++);
                POIExportUtil.addImageRow(worksheet, i++);
                POIExportUtil.addImageRow(worksheet, i++);
                POIExportUtil.addImageRow(worksheet, i++);
                POIExportUtil.addImageRow(worksheet, i++);
                worksheet.addMergedRegion(new CellRangeAddress(0, i, 0, 5));
                String logoPath = sourceDirectory + "/app_logo.png";
                POIExportUtil.writeImageTOExcel(worksheet, POIExportUtil.loadResourceAsStream(logoPath));
                //POIExportUtil pOIExportUtil = new POIExportUtil();
                //POIExportUtil.writeImageTOExcel(worksheet, pOIExportUtil.getImageInpStream());
                int colCount = 0;
                excelrow = (HSSFRow) worksheet.createRow((short) i);
                HSSFCell cellA1 = excelrow.createCell((short) 0);
                
                excelrow = (HSSFRow) worksheet.createRow((short) i);
                HSSFCell cellA2 = excelrow.createCell((short) 1);

                i++;

                POIExportUtil.addHeaderTextRow(worksheet, i++, "Current MedDRA Queries", 6);
                
                POIExportUtil.addFormRow(worksheet, i++, "Report Date/Time:",new SimpleDateFormat("dd-MMM-yyyy h:mm a z").format(new Date(System.currentTimeMillis())),3, 2);
                
                POIExportUtil.addFormRow(worksheet, i++, "Total:",new Long(itrBinding.getEstimatedRowCount()).toString(),3, 2);

                i++;
                int k = i;


                RowSetIterator rs = vo.createRowSetIterator(null);

                while (rs.hasNext()) {
                    Row row = rs.next();
                    //print header on first row in excel
                    if (i == k) {
                        CellStyle styleNew = worksheet.getWorkbook().createCellStyle();
                        Font font = worksheet.getWorkbook().createFont();
                        font.setFontHeightInPoints((short)10);
                        font.setFontName("Arial");
                        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                        font.setColor(new HSSFColor.BLACK().getIndex());
                        styleNew.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
                        styleNew.setFillPattern(CellStyle.FINE_DOTS);
                        styleNew.setFont(font);
                        excelrow = (HSSFRow) worksheet.createRow((short) i);
                        
                        cellA1 = excelrow.createCell((short) 0);
                        cellA1.setCellValue("Code");
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 1);
                        excelrow.createCell((short) 2);
                        excelrow.createCell((short) 3);
                        excelrow.createCell((short) 4);
                        excelrow.createCell((short) 5);
                        worksheet.addMergedRegion(new CellRangeAddress(i, i, 1, 5));
                        cellA1.setCellValue("Name");
                        cellA1.setCellStyle(styleNew);
                        
                        
                        cellA1 = excelrow.createCell((short) 6);
                        cellA1.setCellValue("Extension");
                        cellA1.setCellStyle(styleNew);

                        cellA1 = excelrow.createCell((short) 7);
                        cellA1.setCellValue("Product");
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 8);
                        cellA1.setCellValue("Level");
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 9);
                        cellA1.setCellValue("Scope");
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 10);
                        excelrow.createCell((short) 11);
                        cellA1.setCellValue("Dictionary Version");
                        worksheet.addMergedRegion(new CellRangeAddress(i, i, 10, 11));
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 12);
                        cellA1.setCellValue("Status");
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 13);
                        cellA1.setCellValue("Algorithm");
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 14);
                        cellA1.setCellValue("Critical Event");
                        cellA1.setCellStyle(styleNew);
                        
                        cellA1 = excelrow.createCell((short) 15);
                        excelrow.createCell((short) 16);
                        excelrow.createCell((short) 17);
                        excelrow.createCell((short) 18);
                        excelrow.createCell((short) 19);
                        excelrow.createCell((short) 20);
                        excelrow.createCell((short) 21);
                        worksheet.addMergedRegion(new CellRangeAddress(i, i, 15, 21));
                        cellA1.setCellValue("MG Group");
                        cellA1.setCellStyle(styleNew);
                        
                    }

                    //print data from second row in excel
                    ++i;
    //                    short j = 0;
                    excelrow = worksheet.createRow((short) i);
                    
                    HSSFCell cell = excelrow.createCell(0);
                    cell.setCellValue(row.getAttribute("Mqcd") + "");
                    
                    cell = excelrow.createCell(1);
                    excelrow.createCell(2);
                    excelrow.createCell(3);
                    excelrow.createCell(4);
                    excelrow.createCell(5);
                    worksheet.addMergedRegion(new CellRangeAddress(i, i, 1, 5));
                    cell.setCellValue(row.getAttribute("Mqname")+ "");
                    
                    cell = excelrow.createCell(6);
                    cell.setCellValue(row.getAttribute("Extension")+ "");
                    
                    cell = excelrow.createCell(7);
                    cell.setCellValue(row.getAttribute("Mqprodct")+ "");
                    
                    cell = excelrow.createCell(8);
                    cell.setCellValue(row.getAttribute("Mqlevel") + "");
                    
                    cell = excelrow.createCell(9);
                    cell.setCellValue(row.getAttribute("Mqscp")+ "");
                    
                    cell = excelrow.createCell(10);
                    excelrow.createCell(11);
                    worksheet.addMergedRegion(new CellRangeAddress(i, i, 10, 11));
                    cell.setCellValue(row.getAttribute("Dictver")+ "");
                    
                    cell = excelrow.createCell(12);
                    cell.setCellValue(row.getAttribute("Status")+ "");
                    
                    cell = excelrow.createCell(13);
                    cell.setCellValue(row.getAttribute("Mqalgo")+ "");
                    
                    cell = excelrow.createCell(14);
                    cell.setCellValue(row.getAttribute("Mqcrtev")+ "");
                    
                    cell = excelrow.createCell(15);
                    excelrow.createCell(16);
                    excelrow.createCell(17);
                    excelrow.createCell(18);
                    excelrow.createCell(19);
                    excelrow.createCell(20);
                    excelrow.createCell(21);
                    worksheet.addMergedRegion(new CellRangeAddress(i, i, 15, 21));
                    cell.setCellValue(row.getAttribute("Mqgroup")+ "");
                    
                }
                
                i++;
                i++;
//                excelrow = (HSSFRow) worksheet.createRow((short) i);
//                cellA1 = excelrow.createCell((short) 0);
//                cellA1.setCellValue("Row Count");
//                cellA2 = excelrow.createCell((short) 1);
//                cellA2.setCellValue(itrBinding.getEstimatedRowCount());
                
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
            }
           

    }
    
    public DCBindingContainer getDCBindingContainer(){
        DCBindingContainer dcBindingContainer = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        return dcBindingContainer;
    }

    public void setCntrlReportList(RichSelectOneChoice cntrlReportList) {
        this.cntrlReportList = cntrlReportList;
    }

    public RichSelectOneChoice getCntrlReportList() {
        return cntrlReportList;
    }

    public void setCntrlFormatList(RichSelectOneChoice cntrlFormatList) {
        this.cntrlFormatList = cntrlFormatList;
    }

    public RichSelectOneChoice getCntrlFormatList() {
        return cntrlFormatList;
    }
    //
    //
    //    public void setCntrlStateList(RichSelectManyChoice cntrlStateList) {
    //        this.cntrlStateList = cntrlStateList;
    //    }
    //
    //    public RichSelectManyChoice getCntrlStateList() {
    //        return cntrlStateList;
    //    }
    //
    //    public void setCntrlReleaseGroupList(RichSelectOneChoice cntrlReleaseGroupList) {
    //        this.cntrlReleaseGroupList = cntrlReleaseGroupList;
    //    }
    //
    //    public RichSelectOneChoice getCntrlReleaseGroupList() {
    //        return cntrlReleaseGroupList;
    //    }
    //
    //    public void setCntrlRequestorList(RichSelectOneChoice cntrlRequestorList) {
    //        this.cntrlRequestorList = cntrlRequestorList;
    //    }
    //
    //    public RichSelectOneChoice getCntrlRequestorList() {
    //        return cntrlRequestorList;
    //    }

    public void setCntrlStartDate(RichInputDate startDate) {
        this.cntrlStartDate = startDate;
    }

    public RichInputDate getCntrlStartDate() {
        return cntrlStartDate;
    }

    public void setCntrlEndDate(RichInputDate endDate) {
        this.cntrlEndDate = endDate;
    }

    public RichInputDate getCntrlEndDate() {
        return cntrlEndDate;
    }

    public void setParamRequestor(String paramRequestor) {
        this.paramRequestor = paramRequestor;
    }

    public String getParamRequestor() {
        return paramRequestor;
    }

    public void setParamReleaseGroup(String paramReleaseGroup) {
        this.paramReleaseGroup = paramReleaseGroup;
    }

    public String getParamReleaseGroup() {
        return paramReleaseGroup;
    }

    public void setParamStartDate(Date paramStartDate) {
        this.paramStartDate = paramStartDate;
    }

    public Date getParamStartDate() {
        return paramStartDate;
    }

    public void setParamEndDate(Date paramEndDate) {
        this.paramEndDate = paramEndDate;
    }

    public Date getParamEndDate() {
        return paramEndDate;
    }

    public void setCntrlOpenReport(RichGoButton cntrlOpenReport) {
        this.cntrlOpenReport = cntrlOpenReport;
    }

    public RichGoButton getCntrlOpenReport() {
        return cntrlOpenReport;
    }

    public void setReportURL(String reportURL) {
        this.reportURL = reportURL;
    }

    public String getReportURL() {
        return reportURL;
    }


    //    public void setCntrlProposed(RichSelectBooleanCheckbox cntrlProposed) {
    //        this.cntrlProposed = cntrlProposed;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlProposed() {
    //        return cntrlProposed;
    //    }
    //
    //    public void setCntrlRequested(RichSelectBooleanCheckbox cntrlRequested) {
    //        this.cntrlRequested = cntrlRequested;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlRequested() {
    //        return cntrlRequested;
    //    }
    //
    //    public void setCntrlDraft(RichSelectBooleanCheckbox cntrlDraft) {
    //        this.cntrlDraft = cntrlDraft;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlDraft() {
    //        return cntrlDraft;
    //    }
    //
    //    public void setCntrlReviewed(RichSelectBooleanCheckbox cntrlReviewed) {
    //        this.cntrlReviewed = cntrlReviewed;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlReviewed() {
    //        return cntrlReviewed;
    //    }
    //
    //    public void setCntrlPublished(RichSelectBooleanCheckbox cntrlPublished) {
    //        this.cntrlPublished = cntrlPublished;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlPublished() {
    //        return cntrlPublished;
    //    }
    //
    //    public void setCntrlPending(RichSelectBooleanCheckbox cntrlPending) {
    //        this.cntrlPending = cntrlPending;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlPending() {
    //        return cntrlPending;
    //    }
    //
    //    public void setCntrlApproved(RichSelectBooleanCheckbox cntrlApproved) {
    //        this.cntrlApproved = cntrlApproved;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlApproved() {
    //        return cntrlApproved;
    //    }
    //
    //    public void setCntrlActivated(RichSelectBooleanCheckbox cntrlActivated) {
    //        this.cntrlActivated = cntrlActivated;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlActivated() {
    //        return cntrlActivated;
    //    }

    public void setReleaseGroupSelectItems(ArrayList<SelectItem> releaseGroupSelectItems) {
        this.releaseGroupSelectItems = releaseGroupSelectItems;
    }

    public ArrayList<SelectItem> getReleaseGroupSelectItems() {
        return releaseGroupSelectItems;
    }
    //
    //    public void setCntrlRetired(RichSelectBooleanCheckbox cntrlRetired) {
    //        this.cntrlRetired = cntrlRetired;
    //    }
    //
    //    public RichSelectBooleanCheckbox getCntrlRetired() {
    //        return cntrlRetired;
    //    }

    public void setCntrlReactivated(RichSelectBooleanCheckbox cntrlReactivated) {
        this.cntrlReactivated = cntrlReactivated;
    }

    public RichSelectBooleanCheckbox getCntrlReactivated() {
        return cntrlReactivated;
    }

    public void setParamLastActivationDate(oracle.jbo.domain.Date paramLastActivationDate) {
        this.paramLastActivationDate = paramLastActivationDate;
    }

    public java.util.Date getParamLastActivationDate() {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("ViewObjLatestActivationState1Iterator");

        Enumeration rows = dciterb.getRowSetIterator().enumerateRowsInRange();
        Row row = (Row)rows.nextElement();
        Object tVal = row.getAttribute("LastActivationDate");
        if (tVal == null)
            return null;
        oracle.jbo.domain.Date paramLastActivationDate = (oracle.jbo.domain.Date)tVal;
        return paramLastActivationDate.dateValue();
    }

    public void setGeneratedReport(String generatedReport) {
        this.generatedReport = generatedReport;
    }

    public String getGeneratedReport() {
        reportName = cntrlReportList.getValue().toString();
        reportFormat = cntrlFormatList.getValue().toString();
        generatedReport = reportName + "." + reportFormat;
        return generatedReport;
    }


    public void setGenerateReportButton(RichCommandButton generateReport) {
        this.generateReportButton = generateReport;
    }

    public RichCommandButton getGenerateReportButton() {
        return generateReportButton;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public String getReportFormat() {
        return reportFormat;
    }

    public void reportNameChanged(ValueChangeEvent valueChangeEvent) {
        String selectedReport = valueChangeEvent.getNewValue().toString();
        if (null != selectedReport && selectedReport.equalsIgnoreCase("CURRENT_MEDDRA_QUERIES")) {
            this.cntrlStartDate.setVisible(false);
            this.cntrlEndDate.setVisible(false);
            this.cntrlFormatList.setDisabled(true);
            this.cntrlEndDate.resetValue();
            this.cntrlStartDate.resetValue();
            this.paramStartDate = null;
            this.paramEndDate = null;
            this.cntrlFormatList.setValue("XLS");
        } else {
            this.cntrlStartDate.setVisible(true);
            this.cntrlEndDate.setVisible(true);
            this.cntrlFormatList.setDisabled(false);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.cntrlStartDate);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.cntrlStartDate);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.cntrlEndDate);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.cntrlEndDate);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.cntrlFormatList);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.cntrlFormatList);
    }

    public void validateReportDates(ValueChangeEvent evt) {
        if (cntrlStartDate.getValue() != null) {
            paramStartDate = (Date)cntrlStartDate.getValue();
        }
        if (cntrlEndDate.getValue() != null) {
            paramEndDate = (Date)cntrlEndDate.getValue();
        }
        if (null != paramStartDate && null != paramEndDate && paramStartDate.after(paramEndDate)) {
            CSMQBean.logger.info(userBean.getCaller() + " paramStartDate: " + paramStartDate);
            CSMQBean.logger.info(userBean.getCaller() + " paramEndDate: " + paramEndDate);
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "The End date must be later than Start date.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
    }

    public void exportGroups(FacesContext facesContext, OutputStream outputStream) {
        try {


            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Group Lists");

            HSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short)10);
            font.setFontName("Arial");
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setItalic(false);

            writeImageTOExcel(worksheet, getImageInpStream());

            int rowCount = 0;
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            //            POIExportUtil.addSimpleTextRow(worksheet, rowCount++,
            //                                           "Report Date :" + new SimpleDateFormat("dd-MM-yy").format(new Date(System.currentTimeMillis())),
            //                                           2);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Report Date:",
                                     new SimpleDateFormat("dd-MM-yy").format(new Date(System.currentTimeMillis())), 1,
                                     1);

            DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("MQGroupsVO1Iterator");
            HSSFRow excelrow = null;

            // Get all the rows of a iterator
            oracle.jbo.Row[] rows = dcIteratorBindings.getAllRowsInRange();
            int i = 0;


            for (oracle.jbo.Row row : rows) {

                //print header on first row in excel
                if (i == 0) {
                    excelrow = (HSSFRow)worksheet.createRow((short)rowCount++);

                    CellStyle style = workbook.createCellStyle();
                    style.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                    style.setFillPattern(CellStyle.DIAMONDS);
                    style.setFont(font);
                    //                    excelrow.setRowStyle((HSSFCellStyle)style);
                    HSSFCell cellA1 = excelrow.createCell((short)0);
                    cellA1.setCellValue("Group Lists");
                    cellA1.setCellStyle(style);

                    i = 3;
                }

                //print data from second row in excel
                excelrow = worksheet.createRow((short)rowCount++);
                HSSFCell cell = excelrow.createCell((short)0);
                if (row.getAttribute("LongValue") != null)
                    cell.setCellValue(row.getAttribute("LongValue").toString());
                else
                    cell.setCellValue("");
            }

            worksheet.createFreezePane(0, 1, 0, 1);
            worksheet.autoSizeColumn(0);
            worksheet.autoSizeColumn(1);
            workbook.write(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportProducts(FacesContext facesContext, OutputStream outputStream) {
        try {


            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Products List");

            HSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short)10);
            font.setFontName("Arial");
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setItalic(false);


            writeImageTOExcel(worksheet, getImageInpStream());

            int rowCount = 0;
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            POIExportUtil.addEmptyRow(worksheet, rowCount++);
            //            POIExportUtil.addSimpleTextRow(worksheet, rowCount++,
            //                                           "Report Date :" + new SimpleDateFormat("dd-MM-yy").format(new Date(System.currentTimeMillis())),
            //                                           2);
            POIExportUtil.addFormRow(worksheet, rowCount++, "Report Date:",
                                     new SimpleDateFormat("dd-MM-yy").format(new Date(System.currentTimeMillis())), 1,
                                     1);

            DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("ViewObj_ProductList1Iterator");
            HSSFRow excelrow = null;

            // Get all the rows of a iterator
            oracle.jbo.Row[] rows = dcIteratorBindings.getAllRowsInRange();
            int i = 0;


            for (oracle.jbo.Row row : rows) {

                //print header on first row in excel
                if (i == 0) {
                    excelrow = (HSSFRow)worksheet.createRow((short)rowCount++);
                    HSSFCell cellA1 = excelrow.createCell((short)0);
                    cellA1.setCellValue("Products");
                    CellStyle style = workbook.createCellStyle();
                    style.setFont(font);
                    style.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                    style.setFillPattern(CellStyle.FINE_DOTS);
                    cellA1.setCellStyle(style);
                    i = 3;
                }

                //print data from second row in excel
                excelrow = worksheet.createRow((short)rowCount++);
                HSSFCell cell = excelrow.createCell((short)0);
                if (row.getAttribute("LongValue") != null)
                    cell.setCellValue(row.getAttribute("LongValue").toString());
                else
                    cell.setCellValue("");
            }


            worksheet.createFreezePane(0, 1, 0, 1);
            worksheet.autoSizeColumn(0);
            worksheet.autoSizeColumn(1);
            workbook.write(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public InputStream getImageInpStream() {
//        String sourceDirectory = CSMQBean.getProperty("REPORT_SOURCE");
//        InputStream inputStreamOfExcel = 
// loadResourceAsStream(sourceDirectory + "/app_logo.png");
////                                loadResourceAsStream("E:\\CQT\\branches\\CQT_Enhancements\\ViewController\\public_html\\image\\app_logo.png");
//        return inputStreamOfExcel;
        
        InputStream inputStreamOfExcel = this.getClass().getClassLoader().getResourceAsStream("crs.png");
            //ExcelExportUtils.loadResourceAsStream("C:\\Users\\DileepKumar\\Desktop\\Donna\\ECRS\\trunk\\ECRSViewController\\public_html\\images\\crs.png");
        return inputStreamOfExcel;
    }

    /**
     * @param sheet
     * @throws IOException
     */
    public static void writeImageTOExcel(Sheet sheet, InputStream imageInputStream) throws IOException {
        byte[] bytes = IOUtils.toByteArray(imageInputStream);
        int pictureIdx = sheet.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        imageInputStream.close();
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short)0, 0, (short)1, 3);
        anchor.setAnchorType(2);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        //        pict.resize();
    }


    /**
     * This method to upload the file from local.
     * @param resourceName
     * @return
     */
    public static InputStream loadResourceAsStream(final String resourceName) {
        //_logger.info("Start of CRSReportsBean:loadResourceAsStream()");
        InputStream input = null;
        try {
            input = new FileInputStream(resourceName);
        } catch (FileNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "File not found in the provided location"));
            //  _logger.log(_logger.ERROR, "Exception occured in ManageDealsBean loadResourceAsStream() method", e);
            e.printStackTrace();
            //  _logger.severe("Exception message ManageDealsBean loadResourceAsStream()-->"+e.getMessage());
        }
        // _logger.info("End of CRSReportsBean:loadResourceAsStream()");
        return input;
    }
}
