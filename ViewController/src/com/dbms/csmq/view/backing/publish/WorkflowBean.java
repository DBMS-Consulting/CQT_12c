package com.dbms.csmq.view.backing.publish;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.backing.NMQ.NMQUtils;

import com.dbms.csmq.view.backing.NMQ.NMQWizardBean;
import com.dbms.util.ADFUtils;
import com.dbms.util.JSFUtils;
import com.dbms.util.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import  java.text.SimpleDateFormat;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyShuttle;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import java.util.Map;
import java.util.HashMap;

import oracle.binding.OperationBinding;

public class WorkflowBean {


    private List list = new ArrayList<javax.faces.model.SelectItem>();
    List selectedTerms;
    private RichSelectManyShuttle sms1;
    private UISelectItems si1;
    private RichCommandButton cb1;
    private UserBean userBean;
    private CSMQBean csmqBean;
    private NMQWizardBean nMQWizardBean;
    private String currentRequestor;
    private String dictContentIDWithError;
    private RichTable cntrlRelationErrors;
    private RichTable cntrlContentErrors;
    private int errorCount = 1;
    private RichCommandToolbarButton cntrlActivateButton;
    private RichInputText controlMQState;
    private RichPopup publishConfirmPopup;
    private RichPopup publishWarningPopup;
    private String runExecuteParams;
    private RichCommandButton cb2;
    private RichButton promoteButton;
    private RichButton approveButton;

    public String init() {
        //userBean.setCurrentMenuPath("Publish");
        //userBean.setCurrentMenu("NON_IMPACT_PUBLISH");
        
        return null;
    }

    public String IA_init() {
        //userBean.setCurrentMenuPath("Impact Analysis Publish");
        //userBean.setCurrentMenu("IMPACT_PUBLISH");
        return null;
    }

    public WorkflowBean() {
        userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
        csmqBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        nMQWizardBean = (NMQWizardBean)ADFContext.getCurrent().getPageFlowScope().get("NMQWizardBean");
        currentRequestor = userBean.getCurrentUser();
        //userBean.setCurrentMenuPath("Confirm");
        //userBean.setCurrentMenu("CONFIRM");
    
        }

    private void loadPromoteToPublished () { 

        //BindingContext bc = BindingContext.getCurrent();
        //DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        
        DCBindingContainer binding = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("ViewObjTermsByState1Iterator");
        ViewObject vo = dciterb.getViewObject();
        vo.setNamedWhereClauseParam("state", CSMQBean.STATE_APPROVED);
        vo.setNamedWhereClauseParam("activationGroup", csmqBean.getDefaultPublishReleaseGroup());      
 
        CSMQBean.logger.info(userBean.getCaller() + " ** REQUERY **");
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: ViewObjTermsByState1Iterator");
        CSMQBean.logger.info(userBean.getCaller() + " state: " + CSMQBean.STATE_APPROVED);
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + csmqBean.getDefaultPublishReleaseGroup());
        vo.executeQuery();
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(sms1);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(sms1);
        
        }



    public List getSelectedTerms() {
        if (selectedTerms == null) {
            selectedTerms = new ArrayList<javax.faces.model.SelectItem>();
        }
        return selectedTerms;
    }
    
    /**
     * @param sheet
     * @throws IOException
     */
    public static void writeImageTOExcel(Sheet sheet,InputStream imageInputStream)throws IOException{
        byte[] bytes = IOUtils.toByteArray(imageInputStream);
        int pictureIdx = sheet.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        imageInputStream.close();
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = new HSSFClientAnchor(0,0,0,0,(short)0,0,(short)1,3);
        anchor.setAnchorType(2);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
    //        pict.resize();
    }

    public void setSelectedTerms(List selectedItems) {
        this.selectedTerms = selectedItems;
    }

    public void activate(DialogEvent actionEvent) {
        NMQUtils.activateGroup(CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"));
    }

    public void demoteToDraft(DialogEvent actionEvent) {
        changeState(CSMQBean.STATE_DRAFT, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"));
    }
    
    public void demoteToPendingImpactAssessment (DialogEvent actionEvent) {
        changeState(CSMQBean.STATE_PENDING_IMPACT_ASSESSMENT, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"));
    }
    
    public void activateInCheckMode(ActionEvent actionEvent) {  
        if (NMQUtils.activateGroupInCheckMode(CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"))) {
            this.cntrlActivateButton.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlActivateButton);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlActivateButton);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlRelationErrors);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlRelationErrors);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlContentErrors);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlContentErrors);
            }
        }

    public void promoteToPublished(DialogEvent actionEvent) {
        changeState(CSMQBean.STATE_PUBLISHED, CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"));
        AdfFacesContext.getCurrentInstance().addPartialTarget(sms1);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(sms1);
        }


    public void promoteSingleMQToPublished(DialogEvent actionEvent)  {
        Hashtable h = new Hashtable();
        h = NMQUtils.changeStateFromDraftToPublish(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_REQUESTED, userBean.getCurrentUser(), userBean.getUserRole(), null, "Publish MedDRA Query", CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"));           
        System.out.println("Changing state : " + h);
        if (h.get("STATE").equals("Published")) {
            nMQWizardBean.setCurrentState(CSMQBean.STATE_PUBLISHED);
//            AdfFacesContext.getCurrentInstance().addPartialTarget(cb1);
//            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cb1);
//            AdfFacesContext.getCurrentInstance().addPartialTarget(cb2);
//            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cb2);
              AdfFacesContext.getCurrentInstance().addPartialTarget(this.promoteButton);
              AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.promoteButton);
            AdfFacesContext.getCurrentInstance().addPartialTarget(controlMQState);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlMQState);
            
            }
            
         /*   
        if (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_PROPOSED )){
                System.out.println("*** CHANGING STATE FROM STATE_PROPOSED TO:");
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_REQUESTED, userBean.getCurrentUser(), userBean.getUserRole(), null, "Publish MedDRA Query", CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                System.out.println("...STATE_PROPOSED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_DRAFT, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                System.out.println("...STATE_DRAFT: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_REVIEWED, userBean.getCurrentUser(), userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                System.out.println("...STATE_REVIEWED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_APPROVED, userBean.getCurrentUser(), userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                System.out.println("...STATE_APPROVED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_PUBLISHED, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"), true);
                System.out.println("...STATE_PUBLISHED: " + h);
                }
        
            else if (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_REQUESTED)){
                System.out.println("*** CHANGING STATE FROM STATE_REQUESTED TO:");
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_DRAFT, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                System.out.println("...STATE_DRAFT: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_REVIEWED, userBean.getCurrentUser(), userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                System.out.println("...STATE_REVIEWED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_APPROVED, userBean.getCurrentUser(), userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                System.out.println("...STATE_APPROVED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_PUBLISHED, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"), true);
                System.out.println("...STATE_PUBLISHED: " + h);
                }
            
            else if (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_DRAFT)){
                System.out.println("*** CHANGING STATE FROM STATE_DRAFT TO:");
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_REVIEWED, userBean.getCurrentUser(), userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                    System.out.println("...STATE_REVIEWED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_APPROVED, userBean.getCurrentUser(), userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                    System.out.println("...STATE_APPROVED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_PUBLISHED, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"), true);
                    System.out.println("...STATE_PUBLISHED: " + h);
                }    
           
            else if (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_REVIEWED)){
                System.out.println("*** CHANGING STATE FROM STATE_REVIEWED TO:");
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_APPROVED, userBean.getCurrentUser(), userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"), false);
                    System.out.println("...STATE_APPROVED: " + h);
                try {Thread.sleep(5000);} catch (InterruptedException e) {}
                
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_PUBLISHED, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"), true);
                    System.out.println("...STATE_PUBLISHED: " + h);
                }     
            
            else if (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_APPROVED)){
                System.out.println("*** CHANGING STATE FROM STATE_APPROVED TO:");
                h = NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_PUBLISHED, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP"), true);
                    System.out.println("...STATE_PUBLISHED: " + h);
                }    
            */
            
            }

    public void promoteToPublishedIA(DialogEvent actionEvent) {
        changeState(CSMQBean.IA_STATE_PUBLISHED, CSMQBean.getProperty("DEFAULT_MEDDRA_RELEASE_GROUP"));
        AdfFacesContext.getCurrentInstance().addPartialTarget(sms1);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(sms1);
        }


    private void changeState(String state, String activationGroup) {
        String terms = "";
        for (int i = 0; i < list.size(); i++)
            terms += list.get(i) + ",";
        
        if (terms.length() < 1) {    
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select at least one term.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
            }    
        
        terms = terms.substring(0, terms.length() - 1);
        NMQUtils.changeState(terms, state, userBean.getCurrentUser(), userBean.getUserRole(), null, null, activationGroup);
    }

    public void setSms1(RichSelectManyShuttle sms1) {
        this.sms1 = sms1;
    }

    public RichSelectManyShuttle getSms1() {
        return sms1;
    }

    public void setSi1(UISelectItems si1) {
        this.si1 = si1;
    }

    public UISelectItems getSi1() {
        return si1;
    }

    public void setCb1(RichCommandButton cb1) {
        this.cb1 = cb1;
    }

    public RichCommandButton getCb1() {
        return cb1;
    }

    public void termListChanged(ValueChangeEvent valueChangeEvent) {
        list = (ArrayList)valueChangeEvent.getNewValue();
    }

    public void setDictContentIDWithError(String dictContentIDWithError) {
        this.dictContentIDWithError = dictContentIDWithError;
    }

    public String getDictContentIDWithError() {
        return dictContentIDWithError;
    }

    public void setCntrlRelationErrors(RichTable cntrlRelationErrors) {
        this.cntrlRelationErrors = cntrlRelationErrors;
    }

    public RichTable getCntrlRelationErrors() {
        return cntrlRelationErrors;
    }

    public void contentsErrorRowChanged(SelectionEvent selectionEvent) {
        resolveMethodExpression("#{bindings.ViewObjActivationErrors_Contents1.collectionModel.makeCurrent}", null,
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

        dictContentIDWithError = Utils.getAsString(row, "PredictContentId");
        AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlRelationErrors);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlRelationErrors);
        }


    public Object resolveMethodExpression(String expression, Class returnType, Class[] argTypes, Object[] argValues) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        MethodExpression methodExpression =
            elFactory.createMethodExpression(elContext, expression, returnType, argTypes);
        return methodExpression.invoke(elContext, argValues);
    }

    public void demoteSelected(DialogEvent dialogEvent) {
        String terms = "";
        
        RowKeySet rksSelectedRows = this.cntrlContentErrors.getSelectedRowKeys();
        Iterator itrSelectedRows = rksSelectedRows.iterator();
     
        // Get the data control that is bound to the table - e.g. OpenSupportItemsIterator
        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("ViewObjActivationErrors_Contents1Iterator");
        RowSetIterator rsiSelectedRows = dcIteratorBindings.getRowSetIterator();

        while (itrSelectedRows.hasNext()) {
            Key key = (Key)((List)itrSelectedRows.next()).get(0);
            Row myRow = rsiSelectedRows.getRow(key);
            String pci = myRow.getAttribute("PredictContentId").toString();
            terms+=pci + ",";
            }

        terms = terms.substring(0, terms.length()-1);
        NMQUtils.changeState(terms, CSMQBean.STATE_DRAFT, userBean.getCurrentUser(), userBean.getUserRole(), null, null, CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP")); 
        }


    public void setCntrlContentErrors(RichTable cntrlContentErrors) {
        this.cntrlContentErrors = cntrlContentErrors;
        }

    public RichTable getCntrlContentErrors() {
        if (cntrlContentErrors != null) errorCount = cntrlContentErrors.getRowCount();
        return cntrlContentErrors;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getErrorCount() {
        return errorCount;
    }


    public void setCntrlActivateButton(RichCommandToolbarButton cntrlActivateButton) {
        this.cntrlActivateButton = cntrlActivateButton;
    }

    public RichCommandToolbarButton getCntrlActivateButton() {
        return cntrlActivateButton;
    }

    public String test() {
        loadPromoteToPublished ();
        return null;
    }

    public void setControlMQState(RichInputText controlMQState) {
        this.controlMQState = controlMQState;
    }

    public RichInputText getControlMQState() {
        return controlMQState;
    }
    
    public InputStream getImageInpStream() {
        String sourceDirectory = CSMQBean.getProperty("REPORT_SOURCE");
        InputStream inputStreamOfExcel = 
            loadResourceAsStream(sourceDirectory + "/app_logo.png");
//                 this.getClass().getClassLoader().getResourceAsStream("app_logo.png");
//                loadResourceAsStream("E:\\CQT\\branches\\CQT_Enhancements\\ViewController\\public_html\\image\\app_logo.png");
        return inputStreamOfExcel;
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
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          null,
                                                                          "File not found in the provided location"));
            //  _logger.log(_logger.ERROR, "Exception occured in ManageDealsBean loadResourceAsStream() method", e);
            e.printStackTrace();
            //  _logger.severe("Exception message ManageDealsBean loadResourceAsStream()-->"+e.getMessage());
        }
        // _logger.info("End of CRSReportsBean:loadResourceAsStream()");
        return input;
    }
    
    
    public void downloadReport(FacesContext facesContext, OutputStream outputStream) {
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet("Published IA MQ Report");

        DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("PublishedIAMQVO1Iterator");
        HSSFRow excelrow = null;

        // Get all the rows of a iterator
        oracle.jbo.Row[] rows = dcIteratorBindings.getAllRowsInRange();
        int i = 0;

        try {
            writeImageTOExcel(worksheet, getImageInpStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        HSSFFont font= workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setItalic(false);

        excelrow = (HSSFRow) worksheet.createRow((short) 0);
        HSSFCell cellTitle = excelrow.createCell((short) 1);
        cellTitle.setCellValue("Published IA MQ Report");
        CellStyle style1 = workbook.createCellStyle();
        style1.setFont(font);
        cellTitle.setCellStyle(style1);
        
        excelrow = (HSSFRow) worksheet.createRow((short) 1);
        HSSFCell cellReportRun = excelrow.createCell((short) 1);
        cellReportRun.setCellValue("Report Date/Time: "+new SimpleDateFormat("dd-MMM-yyyy h:mm a z").format(new Date(System.currentTimeMillis())));
        
        for (oracle.jbo.Row row : rows) {

            //print header on first row in excel
            if (i == 0) {
                excelrow = (HSSFRow) worksheet.createRow((short) 3);
                HSSFCell cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("MQ Code");
                HSSFCell cellA2 = excelrow.createCell((short) 1);
                cellA2.setCellValue("Term Name");
                HSSFCell cellA3 = excelrow.createCell((short) 2);
                cellA3.setCellValue("Level Name");
                
                HSSFCell cellA7 = excelrow.createCell((short) 3);
                cellA7.setCellValue("Created By");
                
                HSSFCell cellA4 = excelrow.createCell((short) 4);
                cellA4.setCellValue("Activation Group");
                
                CellStyle style = workbook.createCellStyle();
                style.setFont(font);
                style.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                style.setFillPattern(CellStyle.FINE_DOTS);
                cellA1.setCellStyle(style);
                cellA2.setCellStyle(style);
                cellA3.setCellStyle(style);
                cellA4.setCellStyle(style);
                cellA7.setCellStyle(style);
                
                i=3;
            }

            //print data from second row in excel
            ++i;
            excelrow = worksheet.createRow((short) i);
            HSSFCell cell = excelrow.createCell((short) 0);
            cell.setCellValue(row.getAttribute("TmsCode") + "" );
            HSSFCell cell1 = excelrow.createCell((short) 1);
            cell1.setCellValue(row.getAttribute("TmsName") + "");
            HSSFCell cell2 = excelrow.createCell((short) 2);
            cell2.setCellValue((row.getAttribute("LevelName") == null ? "" : row.getAttribute("LevelName")) + "");
            
            HSSFCell cell7 = excelrow.createCell((short) 3);
            cell7.setCellValue(row.getAttribute("TmsCreatedBy") + "");
            
            HSSFCell cell3 = excelrow.createCell((short) 4);
            cell3.setCellValue(row.getAttribute("ActivationGroup") + "");
        }

        


        worksheet.createFreezePane(0, 1, 0, 1);
        worksheet.autoSizeColumn(0);
        worksheet.autoSizeColumn(1);
        worksheet.autoSizeColumn(2);
        worksheet.autoSizeColumn(3);
        worksheet.autoSizeColumn(4);
        worksheet.autoSizeColumn(5);
        worksheet.autoSizeColumn(6);
        try{
        workbook.write(outputStream);
        outputStream.flush();

        } catch (Exception e) {
        e.printStackTrace();
        }
    }
      
    public void downloadReport1(FacesContext facesContext, OutputStream outputStream) {
        System.out.println("-------------Inside downloadReport1------------------------------");
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet worksheet = workbook.createSheet("Published IA MQ Report");
//
//        DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
//        DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("PublishedIAMQVO1Iterator");
//        HSSFRow excelrow = null;
//
//        // Get all the rows of a iterator
//        oracle.jbo.Row[] rows = dcIteratorBindings.getAllRowsInRange();
//        int i = 0;
//
//        try {
//            writeImageTOExcel(worksheet, getImageInpStream());
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        
//        HSSFFont font= workbook.createFont();
//        font.setFontHeightInPoints((short)10);
//        font.setFontName("Arial");
//        font.setColor(IndexedColors.BLACK.getIndex());
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        font.setItalic(false);
//
//        excelrow = (HSSFRow) worksheet.createRow((short) 0);
//        HSSFCell cellTitle = excelrow.createCell((short) 1);
//        cellTitle.setCellValue("Published IA MQ Report");
//        CellStyle style1 = workbook.createCellStyle();
//        style1.setFont(font);
//        cellTitle.setCellStyle(style1);
//        
//        excelrow = (HSSFRow) worksheet.createRow((short) 1);
//        HSSFCell cellReportRun = excelrow.createCell((short) 1);
//        cellReportRun.setCellValue("Report Date/Time: "+new SimpleDateFormat("dd-MMM-yyyy h:mm a z").format(new Date(System.currentTimeMillis())));
//        
//        for (oracle.jbo.Row row : rows) {
//
//            //print header on first row in excel
//            if (i == 0) {
//                excelrow = (HSSFRow) worksheet.createRow((short) 3);
//                HSSFCell cellA1 = excelrow.createCell((short) 0);
//                cellA1.setCellValue("MQ Code");
//                HSSFCell cellA2 = excelrow.createCell((short) 1);
//                cellA2.setCellValue("Term Name");
//                HSSFCell cellA3 = excelrow.createCell((short) 2);
//                cellA3.setCellValue("Level Name");
//                
//                HSSFCell cellA7 = excelrow.createCell((short) 3);
//                cellA7.setCellValue("Created By");
//                
//                HSSFCell cellA4 = excelrow.createCell((short) 4);
//                cellA4.setCellValue("Activation Group");
//                
//                CellStyle style = workbook.createCellStyle();
//                style.setFont(font);
//                style.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
//                style.setFillPattern(CellStyle.FINE_DOTS);
//                cellA1.setCellStyle(style);
//                cellA2.setCellStyle(style);
//                cellA3.setCellStyle(style);
//                cellA4.setCellStyle(style);
//                cellA7.setCellStyle(style);
//                
//                i=3;
//            }
//
//            //print data from second row in excel
//            ++i;
//            excelrow = worksheet.createRow((short) i);
//            HSSFCell cell = excelrow.createCell((short) 0);
//            cell.setCellValue(row.getAttribute("TmsCode") + "" );
//            HSSFCell cell1 = excelrow.createCell((short) 1);
//            cell1.setCellValue(row.getAttribute("TmsName") + "");
//            HSSFCell cell2 = excelrow.createCell((short) 2);
//            cell2.setCellValue((row.getAttribute("LevelName") == null ? "" : row.getAttribute("LevelName")) + "");
//            
//            HSSFCell cell7 = excelrow.createCell((short) 3);
//            cell7.setCellValue(row.getAttribute("TmsCreatedBy") + "");
//            
//            HSSFCell cell3 = excelrow.createCell((short) 4);
//            cell3.setCellValue(row.getAttribute("ActivationGroup") + "");
//        }
//
//        
//
//
//        worksheet.createFreezePane(0, 1, 0, 1);
//        worksheet.autoSizeColumn(0);
//        worksheet.autoSizeColumn(1);
//        worksheet.autoSizeColumn(2);
//        worksheet.autoSizeColumn(3);
//        worksheet.autoSizeColumn(4);
//        worksheet.autoSizeColumn(5);
//        worksheet.autoSizeColumn(6);
//        try{
//        workbook.write(outputStream);
//        outputStream.flush();
//
//        } catch (Exception e) {
//        e.printStackTrace();
//        }
    }  

    public void downloadPublishedMQReport(FacesContext facesContext, OutputStream outputStream) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet("Published MQ Report");

        DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("PublishedMQVO1Iterator");
        HSSFRow excelrow = null;

        // Get all the rows of a iterator
        oracle.jbo.Row[] rows = dcIteratorBindings.getAllRowsInRange();
        int i = 0;

        try {
            writeImageTOExcel(worksheet, getImageInpStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        HSSFFont font= workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setItalic(false);
        

        excelrow = (HSSFRow) worksheet.createRow((short) 0);
        HSSFCell cellTitle = excelrow.createCell((short) 1);
        cellTitle.setCellValue("Published MQ Report");
        CellStyle style1 = workbook.createCellStyle();
        style1.setFont(font);
        cellTitle.setCellStyle(style1);
        
        excelrow = (HSSFRow) worksheet.createRow((short) 1);
        HSSFCell cellReportRun = excelrow.createCell((short) 1);
        cellReportRun.setCellValue("Report Date/Time: "+new SimpleDateFormat("dd-MMM-yyyy h:mm a z").format(new Date(System.currentTimeMillis())));

        for (oracle.jbo.Row row : rows) {

            //print header on first row in excel
            if (i == 0) {
                excelrow = (HSSFRow) worksheet.createRow((short) 3);
                HSSFCell cellA1 = excelrow.createCell((short) 0);
                cellA1.setCellValue("MQ Code");
                HSSFCell cellA2 = excelrow.createCell((short) 1);
                cellA2.setCellValue("Term Name");
                HSSFCell cellA3 = excelrow.createCell((short) 2);
                cellA3.setCellValue("Level Name");
                
                HSSFCell cellA7 = excelrow.createCell((short) 3);
                cellA7.setCellValue("Created By");
                
                HSSFCell cellA4 = excelrow.createCell((short) 4);
                cellA4.setCellValue("Activation Group");
                
                CellStyle style = workbook.createCellStyle();
                style.setFont(font);
                style.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                style.setFillPattern(CellStyle.FINE_DOTS);
                cellA1.setCellStyle(style);
                cellA2.setCellStyle(style);
                cellA3.setCellStyle(style);
                cellA4.setCellStyle(style);
                cellA7.setCellStyle(style);
                
                i=3;
            }

            //print data from second row in excel
            ++i;
            excelrow = worksheet.createRow((short) i);
            HSSFCell cell = excelrow.createCell((short) 0);
            cell.setCellValue(row.getAttribute("TmsCode") + "");
            HSSFCell cell1 = excelrow.createCell((short) 1);
            cell1.setCellValue(row.getAttribute("TmsName") + "");
            HSSFCell cell2 = excelrow.createCell((short) 2);
            cell2.setCellValue((row.getAttribute("LevelName") == null ? "" : row.getAttribute("LevelName")) + "");
            
            HSSFCell cell7 = excelrow.createCell((short) 3);
            cell7.setCellValue(row.getAttribute("TmsCreatedBy") + "");
            
            HSSFCell cell3 = excelrow.createCell((short) 4);
            cell3.setCellValue(row.getAttribute("ActivationGroup") + "");
        }

        


        worksheet.createFreezePane(0, 1, 0, 1);
        worksheet.autoSizeColumn(0);
        worksheet.autoSizeColumn(1);
        worksheet.autoSizeColumn(2);
        worksheet.autoSizeColumn(3);
        worksheet.autoSizeColumn(4);
        worksheet.autoSizeColumn(5);
        worksheet.autoSizeColumn(6);
        
        try{
        workbook.write(outputStream);
        outputStream.flush();

        } catch (Exception e) {
        e.printStackTrace();
        }
    }
    
    public void exportPromoteDownload(FacesContext facesContext, OutputStream outputStream) {
        try {


            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Promote");

            DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("ViewObjTermsByState1Iterator");
            HSSFRow excelrow = null;
            
            excelrow = (HSSFRow) worksheet.createRow(0);
            HSSFCell cellA9 = excelrow.createCell(0);
            cellA9.setCellValue("List Of MedDRA Queries");
            
            excelrow = (HSSFRow) worksheet.createRow(2);
            HSSFCell cellA7 = excelrow.createCell(0);
            cellA7.setCellValue("Approved State");
            
            HSSFCell cellA8 = excelrow.createCell(1);
            cellA8.setCellValue("Awaiting Published Confirmation");
            
            
            ViewObject view = dcIteratorBindings.getViewObject();
            RowSetIterator rowIter = (RowSetIterator) view.createRowSetIterator(null); //creating secoundary Iterator
            rowIter.reset();
            Row dataRow;
               
            Map<String, String> allList = new HashMap<String, String>();
            while (rowIter.hasNext()) {
                dataRow = (Row) rowIter.next();       
                allList.put(dataRow.getAttribute("DictNm").toString(), dataRow.getAttribute("Mqterm").toString());
              
            }
           List<String> rightHandList = new ArrayList<String>();
           List selectedItems = this.getSelectedTerms();
            if(selectedItems.size()>0){
                for(int k = 0 ; k<selectedItems.size();k++){
                    String selectItem = (String)selectedItems.get(k);
                    rightHandList.add(allList.get(selectItem));
                    allList.remove(selectItem);
                }

            }
            int i = 3;
            for(String key : allList.keySet()){
                excelrow = (HSSFRow) worksheet.createRow(i);
                HSSFCell cellA3 = excelrow.createCell(0);
                cellA3.setCellValue(allList.get(key));

                i++;
            }
            
            int j = 3;
            for(String item : rightHandList){
                excelrow = worksheet.getRow(j);
                if(excelrow == null){
                    excelrow = worksheet.createRow(i);
                }
                HSSFCell cellA6 = excelrow.createCell(1);
                cellA6.setCellValue(item);
                j++;
                i++;
            }
            //worksheet.createFreezePane(0, 1, 0, 1);
            worksheet.autoSizeColumn(0);
            worksheet.autoSizeColumn(1);
            workbook.write(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void exportDemoteDownload(FacesContext facesContext, OutputStream outputStream) {
        try {


            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Demote");

            DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("ViewObjTermsByState1Iterator");
            HSSFRow excelrow = null;
            
            excelrow = (HSSFRow) worksheet.createRow(0);
            HSSFCell cellA9 = excelrow.createCell(0);
            cellA9.setCellValue("List Of MedDRA Queries");
            
            excelrow = (HSSFRow) worksheet.createRow(2);
            HSSFCell cellA7 = excelrow.createCell(0);
            cellA7.setCellValue("Approved IA State");
            
            HSSFCell cellA8 = excelrow.createCell(1);
            cellA8.setCellValue("Awaiting Published IA Confirmation");
            
            
            ViewObject view = dcIteratorBindings.getViewObject();
            RowSetIterator rowIter = (RowSetIterator) view.createRowSetIterator(null); //creating secoundary Iterator
            rowIter.reset();
            Row dataRow;
               
            Map<String, String> allList = new HashMap<String, String>();
            while (rowIter.hasNext()) {
                dataRow = (Row) rowIter.next();       
                allList.put(dataRow.getAttribute("DictNm").toString(), dataRow.getAttribute("Mqterm").toString());
              
            }
           List<String> rightHandList = new ArrayList<String>();
           List selectedItems = this.getSelectedTerms();
            if(selectedItems.size()>0){
                for(int k = 0 ; k<selectedItems.size();k++){
                    String selectItem = (String)selectedItems.get(k);
                    rightHandList.add(allList.get(selectItem));
                    allList.remove(selectItem);
                }

            }
            int i = 3;
            for(String key : allList.keySet()){
                excelrow = (HSSFRow) worksheet.createRow(i);
                HSSFCell cellA3 = excelrow.createCell(0);
                cellA3.setCellValue(allList.get(key));

                i++;
            }
            
            int j = 3;
            for(String item : rightHandList){
                excelrow = worksheet.getRow(j);
                if(excelrow == null){
                    excelrow = worksheet.createRow(i);
                }
                HSSFCell cellA6 = excelrow.createCell(1);
                cellA6.setCellValue(item);
                j++;
            }
            //worksheet.createFreezePane(0, 1, 0, 1);
            worksheet.autoSizeColumn(0);
            worksheet.autoSizeColumn(1);
            workbook.write(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void setRunExecuteParams(String runExecuteParams) {
//        this.runExecuteParams = runExecuteParams;
//    }

    public String getRunExecuteParams() {
            OperationBinding ob=BindingContext.getCurrent().getCurrentBindingsEntry().getOperationBinding("ExecuteWithParams");
                ob.execute();
                if(ob.getErrors().isEmpty()){
                    // Successfully Executed
                }
        return runExecuteParams;
    }

    public void promoteAction(ActionEvent actionEvent) {
//      NMQWizardBean nMQWizardBean = (NMQWizardBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
//      String hasRelations = NMQUtils.checkMqHasRelations(nMQWizardBean.getCurrentDictContentID());
        String hasRelations = "NO_RELATION";
      if("NO_RELATION".equalsIgnoreCase(hasRelations)){
          RichPopup.PopupHints hints = new RichPopup.PopupHints();
          this.getPublishWarningPopup().show(hints);
      }else{
          RichPopup.PopupHints hints = new RichPopup.PopupHints();
          this.getPublishConfirmPopup().show(hints);
      }
    }

    public void setCb2(RichCommandButton cb2) {
        this.cb2 = cb2;
    }

    public RichCommandButton getCb2() {
        return cb2;
    }

    public void setPromoteButton(RichButton promoteButton) {
        this.promoteButton = promoteButton;
    }

    public RichButton getPromoteButton() {
        return promoteButton;
    }

    public void setPublishConfirmPopup(RichPopup publishConfirmPopup) {
        this.publishConfirmPopup = publishConfirmPopup;
    }

    public RichPopup getPublishConfirmPopup() {
        return publishConfirmPopup;
    }

    public void setPublishWarningPopup(RichPopup publishWarningPopup) {
        this.publishWarningPopup = publishWarningPopup;
    }

    public RichPopup getPublishWarningPopup() {
        return publishWarningPopup;
    }

    public void setList(List list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public void setApproveButton(RichButton approveButton) {
        this.approveButton = approveButton;
    }

    public RichButton getApproveButton() {
        return approveButton;
    }
}
