package com.dbms.csmq.view.reports;

import com.dbms.csmq.CSMQBean;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;

public class ReportDownloadBean {

    CSMQBean cSMQBean;
    private RichSelectOneChoice cntrlReportList;
    private String fileName = "NULL";
    private ArrayList<String> reports;
    private String reportDirectory;
    
    
    public ReportDownloadBean() {
        super();
        cSMQBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        reportDirectory = cSMQBean.getProperty("DOWNLOAD_DIRECTORY");
        
        //reportDirectory = "C:\\Users\\DileepKumar\\Desktop\\Donna\\reports\\";
        System.out.println("reportDirectory ==> "+ reportDirectory);
        reports = new ArrayList<String>();
        loadReportList();
        if (cntrlReportList != null) cntrlReportList.setValue(reports.get(0));
        }


    public ArrayList<String> getReportList() {
        return reports;
    }


    private void loadReportList () {
        if (reportDirectory.length() == 0)
            return;
        File folder = new File(reportDirectory);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null){
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                reports.add(listOfFiles[i].getName());
            }
        }
        }
        
    }


    public void reportDownloadAction(FacesContext facesContext, OutputStream outputStream) throws FileNotFoundException, IOException {
        
        String fileName =  this.cntrlReportList.getValue().toString();
        File f = new File(reportDirectory + fileName);
        FileInputStream fis = null;
        byte[] b;
        try {
            System.out.println("Opening file ["+ f.length() +"]: " + f.toString());
            fis = new FileInputStream(f);
            
            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        outputStream.flush();
        outputStream.close();
        fis.close();
        
        
    }


    public void setReportDirectory(String reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    public String getReportDirectory() {
        return reportDirectory;
    }

    public void setCntrlReportList(RichSelectOneChoice cntrlReportList) {
        this.cntrlReportList = cntrlReportList;
    }

    public RichSelectOneChoice getCntrlReportList() {
        return cntrlReportList;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void reportNameChanged(ValueChangeEvent valueChangeEvent) {
        this.fileName = valueChangeEvent.getNewValue().toString();
    }
}
