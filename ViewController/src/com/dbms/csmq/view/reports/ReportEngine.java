package com.dbms.csmq.view.reports;


import com.dbms.csmq.CSMQBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Map;

import javax.naming.InitialContext;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRPropertiesUtil;
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


public class ReportEngine {
    
    String sourceDirectory = CSMQBean.getProperty("REPORT_SOURCE");
    //String sourceDirectory = "C:\\Users\\DileepKumar\\Desktop\\Donna\\NMAT\\JRXMLReport\\";
    String reportFile;
    private final String dbUrlString = "jdbc/TMSDS";
    JRPropertiesUtil jRPropertiesUtil;
    
    public ReportEngine() {
        super();
        net.sf.jasperreports.engine.util.JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
        //LocalJasperReportsContext ctx = new LocalJasperReportsContext(DefaultJasperReportsContext.getInstance());
        //jRPropertiesUtil = JRPropertiesUtil.getInstance(ctx);
        //jRPropertiesUtil.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
    }


    public void exportAsExcelWorkbook (String [] reportNames, OutputStream outputStream, Map parameters, String caller, String performImpact) {
        String [] reportFiles = new String [reportNames.length];
        int i=0;  //report count
        
        parameters.put("REPORT_DIRECTORY", sourceDirectory);
        parameters.put("PERFORM_IMPACT", performImpact);
        CSMQBean.logger.info(caller + "REPORTS: " +  reportNames);
        CSMQBean.logger.info(caller + "REPORT_DIRECTORY: " +  sourceDirectory);
        CSMQBean.logger.info(caller + "PERFORM_IMPACT: " +  performImpact);
        
        ArrayList<JasperPrint> list = new  ArrayList<JasperPrint>();
        //net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporterXLS = new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
        JRXlsExporter exporterXLS = new JRXlsExporter();
        //exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
        
        String impactFlowType = (String) parameters.get("I_FLOW_TYPE");     
        CSMQBean.logger.info(caller + "impactFlowType: " +  impactFlowType);
        if("VIEW_PREV_VER_IMPACT".equals(impactFlowType)){
            exporterXLS.setParameter(JRXlsAbstractExporterParameter.SHEET_NAMES, new String[] {"Previous", "Current"});
        } else {
            exporterXLS.setParameter(JRXlsAbstractExporterParameter.SHEET_NAMES, new String[] {"Existing", "Future"});
        }   
    
        exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE); 
        Connection conn = null;
        try {
            InitialContext initialContext = new InitialContext();
            DataSource ds = (DataSource)initialContext.lookup(dbUrlString);
            //DataSource ds = (DataSource)initialContext.lookup(CSMQBean.getProperty("DATABASE_URL")); // get from your application module configuration
            conn = ds.getConnection();
            
            for (String reportName : reportNames) {
                if("HIERARCHY_EXPORT_IA_FUTURE".equalsIgnoreCase(reportName)){
                    String version = (String)parameters.get("DICTIONARY_VERSION");
                    Double doubleValue = new Double(version);
                    doubleValue = doubleValue + 1;
                    parameters.put("DICTIONARY_VERSION", (new Integer(doubleValue.intValue())).toString().concat(".0"));
                System.out.println();
                }
                
                reportFiles[i] = sourceDirectory + reportName + ".jrxml";
                CSMQBean.logger.info(caller + "RUNNING REPORT: " +  reportFiles[i]);
                InputStream is = new FileInputStream(new File(reportFiles[i]));
                JasperDesign jasperDesign = JRXmlLoader.load(is);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                
                list.add(jasperPrint);
                is.close();
            }
           
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
            exporterXLS.exportReport();
            //conn.close();
            outputStream.close();
            }
        catch (Exception e) {
            CSMQBean.logger.info(caller + "ERROR RUNNING REPORT: " +  reportFiles[i]);
            e.printStackTrace();
        } finally {
                if (null != conn){
                    try {
                        conn.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.info(caller + "error while closing connection...");     
                    }
                }
        }
      }
    
    
    public void printReport (String reportName, String reportFormat, OutputStream outputStream, Map parameters, String caller, String performImpact) {
        
        reportFile = sourceDirectory + reportName + ".jrxml";
        parameters.put("REPORT_DIRECTORY", sourceDirectory);
        parameters.put("PERFORM_IMPACT", performImpact);
        
        parameters.put("DICTIONARY_VERSION", performImpact);
        parameters.put("DICTIONARY_TIMESTAMP", performImpact);
        
        CSMQBean.logger.info(caller + "reportSourceFile: " +  reportFile);
        CSMQBean.logger.info(caller + "REPORT_DIRECTORY: " +  sourceDirectory);
        CSMQBean.logger.info(caller + "PERFORM_IMPACT: " +  performImpact);
        Connection conn = null;
        try {
            InitialContext initialContext = new InitialContext();
            DataSource ds = (DataSource)initialContext.lookup(dbUrlString);
            //DataSource ds = (DataSource)initialContext.lookup(CSMQBean.getProperty("DATABASE_URL")); // get from your application module configuration
            conn = ds.getConnection();
            InputStream is = new FileInputStream(new File(reportFile));
            JasperDesign jasperDesign = JRXmlLoader.load(is);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            
            if (reportFormat.equals("PDF")) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                }
            else if (reportFormat.equals("XLS")) {
                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
                exporterXLS.exportReport();
                }
            
            is.close();
            //conn.close();
            outputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != conn){
                    try {
                        conn.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.info(caller + "error while closing connection...");     
                    }
                }
            }
    }
    
    public void exportMQDtlsAsExcelWorkbook(String[] reportNames, OutputStream outputStream, Map parameters,
                                            String caller) {
        CSMQBean.logger.info(caller + "Start exec exportMQDtlsAsExcelWorkbook() ");
        String[] reportFiles = new String[reportNames.length];
        int i = 0; //report count

        parameters.put("REPORT_DIRECTORY", sourceDirectory);
        CSMQBean.logger.info(caller + "REPORTS: " + reportNames);
        CSMQBean.logger.info(caller + "REPORT_DIRECTORY: " + sourceDirectory);

        ArrayList<JasperPrint> list = new ArrayList<JasperPrint>();
        JRXlsExporter exporterXLS = new JRXlsExporter();
        exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
        Connection conn = null;
        try {
            InitialContext initialContext = new InitialContext();
            DataSource ds = (DataSource)initialContext.lookup(dbUrlString);
            //DataSource ds = (DataSource)initialContext.lookup(CSMQBean.getProperty("DATABASE_URL")); // get from your application module configuration
            conn = ds.getConnection();

            for (String reportName : reportNames) {
                reportFiles[i] = sourceDirectory + reportName + ".jrxml";
                CSMQBean.logger.info(caller + "RUNNING REPORT: " + reportFiles[i]);
                InputStream is = new FileInputStream(new File(reportFiles[i]));
                JasperDesign jasperDesign = JRXmlLoader.load(is);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                list.add(jasperPrint);
                is.close();
            }

            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
            exporterXLS.exportReport();
            //conn.close();
            outputStream.close();
        } catch (Exception e) {
            CSMQBean.logger.info(caller + "ERROR RUNNING REPORT: " + reportFiles[i]);
            e.printStackTrace();
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException sqe) {
                    CSMQBean.logger.info(caller + "error while closing connection...");
                }
            }
        }
        CSMQBean.logger.info(caller + "End of exec exportMQDtlsAsExcelWorkbook() ");
    }
    
}
