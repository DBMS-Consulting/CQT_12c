package com.dbms.csmq.view.backing.NMQ;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.hierarchy.TermHierarchyBean;
import com.dbms.util.ADFUtils;
import com.dbms.util.dml.DMLUtils;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.ViewObject;
import oracle.jbo.server.DBTransaction;


public class NMQWizardBean {
   
    public static final int MAX_PRODUCT_COUNT = 3;
    public static final String INITIAL_STATUS = "PENDING";
    private boolean includeLLTsInExport = false;
    private boolean exportDisplayedOnly = false;

    //SELECTED VALUES
    private String currentFilterDictionaryShortName;
    private String currentPredictGroups;
    private String currentTermName;
    private String currentProduct; //value 3
    private String currentTermLevel = CSMQBean.NMQ_LEVEL_1;
    private String currentMQALGO = CSMQBean.FALSE;
    private String currentMQCRTEV = CSMQBean.CRITICAL_EVENT_NO; //value 4
    private String currentMQGROUP; //value 2
    private String currentScope = CSMQBean.FALSE;
    private String currentStatus = INITIAL_STATUS;
    private String currentMQStatus = CSMQBean.PENDING_ACTIVITY_STATUS;
    private String currentDictContentID;
    private String currentContentCode;
    private String copiedDictContentID;
    private String currentRequestor;
    private String currentReleaseGroup;
    /* @author MTW
        06/12/2014
        NMAT-UC01.02 & NMAT-UC11.02
    */
    //private String currentDesignee;
    //private String currentRequestDate;
    private String currentNarrowScopeOnly = CSMQBean.FALSE;
    private String currentState;
    private String currentReasonForRequest;
    private String currentReasonForApproval;
    private boolean isApproved = false;
    
    private String currentUntilDate;
    private String currentCreateDate;
    private String currentVersion;
    private String currentCreatedBy;
    
    private String currentBaseDictionaryName;
    private String currentBaseDictionaryShortName;
    private String currentFilterDictionaryName;
    private String currentCutOffDate;

    String noteInformativeNoteShortName;
    String descriptionInformativeNoteShortName;
    String sourceInformativeNoteShortName;

    private String currentInfNoteDescription;
    private String currentInfNoteSource;
    private String currentInfNoteNotes;

    //private RichInputText controlMQStatus;

    private String activeDictionary;
    private String activeDictionaryName;
    private String activeDictionaryVersion;

    private List<String> productList = new ArrayList<String>();
    private List<String> mQGroupList = new ArrayList<String>();
    /*
     * @author MTW
     * 07/21/2014
     */
    private List<String> designeeList = new ArrayList<String>();

    private int mode;
    private String wizardStatus;
    private boolean standardProduct = true;
    private boolean saved = false;
    private String currentDictionary;
    private boolean isMedDRA = false;
    private boolean isNMQ = false;
    private boolean isSMQ = false;
    private String updateParam;
    private String currentUser = ADFContext.getCurrent().getSecurityContext().getUserName();
    private String hierarchyParamScope = "-1";
    private String hierarchyParamSort = "scope";
    private String paramPrimLinkFlag = CSMQBean.FALSE;
    private int maxLevels = Integer.parseInt(CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
    
    BindingContext bc = BindingContext.getCurrent();
    DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();


    private CSMQBean cSMQBean;
    private UserBean userBean;
    private oracle.jbo.domain.Date currentDateRequested;
    private oracle.jbo.domain.Date currentRequestedByDate;
    private boolean performImpactPriorToExport = false;
    private String currentMQType;
    private boolean treeAccessed = false;
    private boolean dictionaryInfoFetched = false;

    private RichSelectManyChoice controlMQGroup;
    private RichSelectManyChoice productListControl;
    private RichSelectManyChoice controlDesignee;
    private RichSelectOneChoice controlCriticalEvent;
    
    private String ignorePredict;

    /*
     * TES 31-JUL-2014
     * FOR ACTIVATION INFO
     */
    private String currentInitialCreationDate;
    private String currentInitialCreationBy;
    private String currentLastActivationDate;
    private String currentActivationBy;
    
    private String currentExtension;
    //private UISelectItem cntrlProductListSelectItems;
    private ArrayList<SelectItem> productSelectItems;
    private String currentTermLevelNumber;
    private String designeeListAsString;
    private boolean actionDelete = false;
    private String productListAsString;
	private boolean renderProduct = false;
    private RichPopup addDetailsWarningPopup;
    private RichPopup infoNotesWarningPopup;
    private RichPopup addRelationsWarningPopup;
    private final String dbUrlString = "jdbc/TMSDS";

    public NMQWizardBean() {           
        
            /**
             *  @author TES
             *  @fsds NMAT-UC01.01
             */
        
        System.out.println("NMQWizardBean");
        
        productSelectItems = new ArrayList<SelectItem> ();
        
        cSMQBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
        currentRequestor = userBean.getCurrentUser();

        // LOAD DEFAULTS FROM APP BEAN
        this.currentBaseDictionaryShortName = cSMQBean.getDefaultBaseDictionaryShortName();
        this.currentFilterDictionaryShortName = cSMQBean.getDefaultFilterDictionaryShortName();
        

        this.noteInformativeNoteShortName = cSMQBean.getProperty("SMQ_NOTE_INFORMATIVE_NOTE");
        this.descriptionInformativeNoteShortName = cSMQBean.getProperty("SMQ_DESCRIPTION_INFORMATIVE_NOTE");
        this.sourceInformativeNoteShortName = cSMQBean.getProperty("SMQ_SOURCE_INFORMATIVE_NOTE");
        
        this.currentMQType = cSMQBean.getCustomMQName();
        if (!dictionaryInfoFetched)
            getDictionaryInfo();

        }


    public String setModeUpdateExisting() {
        this.currentPredictGroups = cSMQBean.getDefaultDraftReleaseGroup();
        
        /*
         * 
         * 20141117-2   Details Page for Create, Update, or Create by Copy      
         * For a CMQ created by a requestor OR MQM, the first state must be DRAFT, not proposed.  
         * Only for NMQs created by Requestors should the state be Proposed.
         * 
         */
        //if (userBean.isRequestor()) this.currentState = CSMQBean.STATE_PROPOSED;
        //if (userBean.isMQM()) this.currentState = CSMQBean.STATE_DRAFT;
        //  CHANGED 24-MAR-2105 for all CMQs:  if (!isNMQ && (userBean.isMQM() || userBean.isRequestor())) this.currentState = CSMQBean.STATE_DRAFT;   
       // if (!isNMQ) this.currentState = CSMQBean.STATE_DRAFT;
       // if (isNMQ && userBean.isRequestor()) this.currentState = CSMQBean.STATE_PROPOSED;
        if (isNMQ && userBean.isRequestor() && !(userBean.isMQM() || userBean.isAdmin())){
            this.setCurrentState(CSMQBean.STATE_PROPOSED);
        } else {
            this.setCurrentState(CSMQBean.STATE_DRAFT);
        }
        userBean.setCurrentMenuPath("Update");
        userBean.setCurrentMenu("UPDATE_NMQ");
        this.mode = CSMQBean.MODE_UPDATE_EXISTING;
        this.updateParam = CSMQBean.DML_UPDATE;
        CSMQBean.logger.info(userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        return null;
    }

    public String setModeCopyExisting() {
        this.currentPredictGroups = cSMQBean.getDefaultDraftReleaseGroup();
        
        
        /*
         * 
         * 20141117-2   Details Page for Create, Update, or Create by Copy      
         * For a CMQ created by a requestor OR MQM, the first state must be DRAFT, not proposed.  
         * Only for NMQs created by Requestors should the state be Proposed.
         * 
         */
        //if (userBean.isRequestor()) this.currentState = CSMQBean.STATE_PROPOSED;
        //if (userBean.isMQM()) this.currentState = CSMQBean.STATE_DRAFT;

        //  CHANGED 24-MAR-2105 for all CMQs:  if (!isNMQ && (userBean.isMQM() || userBean.isRequestor())) this.currentState = CSMQBean.STATE_DRAFT;   
        //if (!isNMQ) this.currentState = CSMQBean.STATE_DRAFT;
        //if (isNMQ && userBean.isRequestor()) this.currentState = CSMQBean.STATE_PROPOSED;
        if (isNMQ && userBean.isRequestor() && !(userBean.isMQM()|| userBean.isAdmin())){
            this.setCurrentState(CSMQBean.STATE_PROPOSED);
        } else {
            this.setCurrentState(CSMQBean.STATE_DRAFT);
        }
        if (isNMQ){
            productList.clear();
            productList.add(CSMQBean.DEFAULT_PRODUCT);  // add the default product only if it is new or copy
        }
        userBean.setCurrentMenuPath("Copy");
        userBean.setCurrentMenu("COPY_NMQ");
        this.mode = CSMQBean.MODE_COPY_EXISTING;
        this.updateParam = CSMQBean.DML_INSERT;
        CSMQBean.logger.info(userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        String designee = userBean.getCurrentUser().toUpperCase();
        CSMQBean.logger.info("ADDING DESIGNEE: " + designee);
        designeeList.add(designee);
        return null;
    }


    public String setModeInsertNew() {
        this.currentPredictGroups = cSMQBean.getDefaultDraftReleaseGroup();
        /* @author MTW
        * 06/17/2014
        * @fsds NMAT-UC01.01 & NMAT-UC11.01
        * added !isNMQ || and isNMQ &&
        */
        
        /*
         * 20141117-2	Details Page for Create, Update, or Create by Copy	
         * For a CMQ created by a requestor OR MQM, the first state must be DRAFT, not proposed.  
         * Only for NMQs created by Requestors should the state be Proposed.
         * 
         */
        //  CHANGED 24-MAR-2105 for all CMQs:  if (!isNMQ && (userBean.isMQM() || userBean.isRequestor())) this.currentState = CSMQBean.STATE_DRAFT;   
       // if (!isNMQ) this.currentState = CSMQBean.STATE_DRAFT;
       // if (isNMQ && userBean.isRequestor()) this.currentState = CSMQBean.STATE_PROPOSED;
        if (isNMQ && userBean.isRequestor() && !(userBean.isMQM() || userBean.isAdmin())){
            this.setCurrentState(CSMQBean.STATE_PROPOSED);
        } else {
            this.setCurrentState(CSMQBean.STATE_DRAFT);
        }

        
        
        //if (userBean.isMQM()) this.currentState = CSMQBean.STATE_DRAFT;
        /* @author MTW
        * 06/18/2014
        * @fsds NMAT-UC01.01 & NMAT-UC11.01
        * Added if (isNMQ)
        */
        if (isNMQ) productList.add(CSMQBean.DEFAULT_PRODUCT);  // add the default product only if it is new or copy NMQ
        
        userBean.setCurrentMenuPath("Create");
        userBean.setCurrentMenu("CREATE_NEW_NMQ");
        this.mode = CSMQBean.MODE_INSERT_NEW;
        this.updateParam = CSMQBean.DML_INSERT;
        //productList.add(CSMQBean.DEFAULT_PRODUCT);
        CSMQBean.logger.info(userBean.getCaller() + userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        setCurrentExtension(CSMQBean.customMQName);
        String designee = userBean.getCurrentUser().toUpperCase();
        CSMQBean.logger.info("ADDING DESIGNEE: " + designee);
        designeeList.add(designee);
        this.setRenderProduct(Boolean.TRUE);
        return null;
    }

    public String setModeUpdateSMQ() {
       // NMQWizardSearchBean nMQWizardSearchBean = (NMQWizardSearchBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardSearchBean");
       // nMQWizardSearchBean.setParamExtension("SMQ");
        this.currentPredictGroups = cSMQBean.getDefaultDraftReleaseGroup();
        userBean.setCurrentMenuPath("Update");
        userBean.setCurrentMenu("UPDATE_SMQ");
        this.mode = CSMQBean.MODE_UPDATE_SMQ;
        this.updateParam = CSMQBean.DML_UPDATE;
        CSMQBean.logger.info(userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        return null;
    }

    public String setModeBrowseSearch() {
        userBean.setCurrentMenuPath("Browse & Search");
        userBean.setCurrentMenu("BROWSE_SEARCH");
        this.mode = CSMQBean.MODE_BROWSE_SEARCH;
        this.updateParam = CSMQBean.DML_NONE;
        CSMQBean.logger.info(userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        return null;
        }
    
    
    public String setModeHistoric() {
        this.currentPredictGroups = cSMQBean.getDefaultDraftReleaseGroup();
        userBean.setCurrentMenuPath("Historic");
        userBean.setCurrentMenu("HISTORIC");
        this.mode = CSMQBean.MODE_HISTORIC;
        this.updateParam = CSMQBean.DML_NONE;
        CSMQBean.logger.info(userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        return null;
        }
    
    public String setModeImpactAssessment() {
        this.currentPredictGroups = cSMQBean.getDefaultMedDRAReleaseGroup();
        userBean.setCurrentMenuPath("Impact Assessment");
        userBean.setCurrentMenu("MEDDRA_IMPACT_ASSESSMENT");
        this.mode = CSMQBean.MODE_IMPACT_ASSESSMENT;
        this.updateParam = CSMQBean.DML_UPDATE;
        CSMQBean.logger.info(userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        
        return null;
        }
    
   
    
    private void setDefaultDictionary () {
        currentDictionary = cSMQBean.getDefaultBaseDictionaryShortName();
        if (this.mode == CSMQBean.MODE_UPDATE_EXISTING || this.mode == CSMQBean.MODE_COPY_EXISTING || this.mode == CSMQBean.MODE_UPDATE_SMQ || this.mode == CSMQBean.MODE_BROWSE_SEARCH)
            currentDictionary = cSMQBean.getDefaultFilterDictionaryShortName();
    }
   
    
    public void loadProductSelectList () {
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        if (binding == null) return;
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("ViewObj_ProductList1Iterator");
        ViewObject vo = dciterb.getViewObject();
        
        vo.executeQuery();
        
//        RowSetIterator rs = vo.createRowSetIterator(null);
//        rs.reset();
//        String designee = "";
//        SelectItem selectItem;
//        while (rs.hasNext()) {
//            Row row = rs.next();
//            String name = (String)row.getAttribute("LongValue");
//            String val = (String)row.getAttribute("ShortVal");
//            selectItem = new SelectItem(name, val, null, false, false, true);
//                productSelectItems.add(selectItem);
//            //if (!getCurrentMQType().equals("NMQ") && name.equals(CSMQBean.DEFAULT_PRODUCT)) break; // don't add the default UNLESS it's an NMQ
////            if (!getCurrentExtension().equals("NMQ") && name.equals(CSMQBean.DEFAULT_PRODUCT)) break; // don't add the default UNLESS it's an NMQ
////            productSelectItems.add(selectItem);
//            }
//        
//        rs.closeRowSetIterator();
        
        
        }

    public void setCurrentDictContentID(String currentDictContentID) {
        this.currentDictContentID = currentDictContentID;
    }

    public String getCurrentDictContentID() {
        return currentDictContentID;
    }

    
   

   

    public void setCurrentFilterDictionaryShortName(String currentDictionary) {
        this.currentFilterDictionaryShortName = currentDictionary;
    }

    public String getCurrentFilterDictionaryShortName() {
        return currentFilterDictionaryShortName;
    }

    public void setCurrentPredictGroups(String currentPredictGroups) {
        if (currentPredictGroups == null)
            cSMQBean.getDefaultDraftReleaseGroup();
        else
            this.currentPredictGroups = currentPredictGroups;
        }

    public String getCurrentPredictGroups() {
        return currentPredictGroups;
    }

    public void setCurrentTermName(String currentTermName) {
        this.currentTermName = currentTermName;
    }

    public String getCurrentTermName() {
        return currentTermName;
    }

    public void setCurrentProduct(String currentProduct) {
        this.currentProduct = currentProduct;
    }

    public String getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentTermLevel(String currentTermLevel) {
        this.currentTermLevel = currentTermLevel;
    }

    public String getCurrentTermLevel() {
        return currentTermLevel;
    }

    public void setCurrentMQALGO(String currentMQALGO) {
        this.currentMQALGO = currentMQALGO;
    }

    public String getCurrentMQALGO() {
        return currentMQALGO;
    }

    public void setCurrentMQCRTEV(String currentMQCRTEV) {
        this.currentMQCRTEV = currentMQCRTEV;
    }

    public String getCurrentMQCRTEV() {
        return currentMQCRTEV;
    }

    public void setCurrentMQGROUP(String currentMQGROUP) {
        this.currentMQGROUP = currentMQGROUP;
    }

    public String getCurrentMQGROUP() {
        return currentMQGROUP;

    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;     
        }

    public String getCurrentStatus() {
        return currentStatus;
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

    public boolean saveDetails() {
        return saveDetails(false);
        }
    
    public boolean saveDetails (boolean silent) {

        //if (this.mode == CSMQBean.MODE_INSERT_NEW && saved) return true;  //it's already been saved - do this to avoid the "name in use" error
        
        
        
        
        String tempName = currentTermName;
        
        if (mode != CSMQBean.MODE_UPDATE_SMQ) {
            // strip out the old product and NMQ label
            int firstBraket =  currentTermName.indexOf("[");
            if (firstBraket > 0)
                tempName = currentTermName.substring(0,firstBraket);  
            }
        
        
        // If it's a copy, get rid of the old extension
        if ( this.mode == CSMQBean.MODE_COPY_EXISTING) {
            int firstParen = tempName.indexOf("(");
            if (firstParen > 0)
                tempName = tempName.substring(0, firstParen -1);
            }
            tempName = tempName.trim();  // get rid of the spaces - these cause a problem

        
        // IF IT'S NEW or UPDATE APPEND THE PRODUCT AND NMQ TO THE NAME
        if (this.mode == CSMQBean.MODE_INSERT_NEW || this.mode == CSMQBean.MODE_COPY_EXISTING  || this.mode == CSMQBean.MODE_UPDATE_EXISTING )
            tempName += " [" + currentProduct + "] ("+this.currentExtension+")";
        
        //if (this.mode == CSMQBean.MODE_COPY_EXISTING)
        //    tempName += "_COPY";
      
        // For a CMQ created by a requestor OR MQM, the first state must be DRAFT, not proposed.  
        // Only for NMQs created by Requestors should the state be Proposed.
        if (this.currentExtension.equals("CMQ")) {
            currentState = CSMQBean.STATE_DRAFT;
        } else if (this.currentExtension.equals("NMQ")){
            if (userBean.isRequestor() && !(userBean.isMQM() || userBean.isAdmin())){
                currentState = CSMQBean.STATE_PROPOSED;
            } else {
                currentState = CSMQBean.STATE_DRAFT;
            }
        }
        String action = (mode == CSMQBean.MODE_INSERT_NEW && !isSaved()) ?  "Inserted" : "Updated";
       
        Hashtable results = null;
        CSMQBean.logger.info(userBean.getCaller() + " ***** SAVING NMQ DETAILS *****");
        CSMQBean.logger.info(userBean.getCaller() + " mode: " + this.mode);
        CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID: " + this.currentDictContentID);
        CSMQBean.logger.info(userBean.getCaller() + " copiedDictContentID: " + this.copiedDictContentID);
        CSMQBean.logger.info(userBean.getCaller() + " currentDictionary: " + this.currentFilterDictionaryShortName);
        CSMQBean.logger.info(userBean.getCaller() + " currentPredictGroups: " + this.currentPredictGroups);
        CSMQBean.logger.info(userBean.getCaller() + " newTermName: " + tempName);
        CSMQBean.logger.info(userBean.getCaller() + " currentTermLevel: " + this.currentTermLevel);
        CSMQBean.logger.info(userBean.getCaller() + " currentMQALGO: " + this.currentMQALGO);
        CSMQBean.logger.info(userBean.getCaller() + " currentMQCRTEV: " + this.currentMQCRTEV);
        CSMQBean.logger.info(userBean.getCaller() + " currentScope: " + this.currentScope);
        CSMQBean.logger.info(userBean.getCaller() + " currentMQGROUP: " + this.currentMQGROUP);
        CSMQBean.logger.info(userBean.getCaller() + " currentProduct: " + this.currentProduct);
        CSMQBean.logger.info(userBean.getCaller() + " currentContentCode: " + this.currentContentCode);
        CSMQBean.logger.info(userBean.getCaller() + " getUpdateParam: " + this.getUpdateParam());
        CSMQBean.logger.info(userBean.getCaller() + " currentRequestor: " + this.currentRequestor);
        CSMQBean.logger.info(userBean.getCaller() + " userRole: " + userBean.getUserRole());
        CSMQBean.logger.info(userBean.getCaller() + " action: " + action);
        CSMQBean.logger.info(userBean.getCaller() + " currentStatus: " + this.currentStatus);
        /*
         * @author MTW
         * 06/30/2014
         * @fsds NMAT-UC01.02 & NMAT-UC11.02
         */
        String designeeListString = "";
        if (designeeList != null) {
            designeeListString = designeeList.toString();
            CSMQBean.logger.info(userBean.getCaller() + " currentDesignee: " + designeeListString);
        }
        if (mode == CSMQBean.MODE_IMPACT_ASSESSMENT) {
            CSMQBean.logger.info(userBean.getCaller() + " CALLING: saveIADetails");
            results = NMQUtils.saveIADetails(tempName, currentProduct, currentTermLevel, currentScope, currentMQALGO, currentMQCRTEV, currentMQGROUP, currentContentCode, this.getUpdateParam(), currentRequestor, currentDictContentID, userBean.getUserRole(), action, designeeListString); 
            }
        /*
         * @author MTW
         * 06/30/2014
         * @fsds NMAT-UC01.02 & NMAT-UC11.02
         * add currentDesignee
         */
        else {
            CSMQBean.logger.info(userBean.getCaller() + " CALLING: saveDetails");
            results = NMQUtils.saveDetails(currentFilterDictionaryShortName, currentPredictGroups, tempName, currentProduct, currentTermLevel, currentScope, currentMQALGO, currentMQCRTEV, currentMQGROUP, currentContentCode, this.getUpdateParam(), currentRequestor, currentDictContentID, userBean.getUserRole(), action, currentStatus, designeeListString); 
            }
       
       if (results == null) return false; // it failed
       this.currentStatus = INITIAL_STATUS;
       //if it's new or a copy, update the content code & ID with the new values & change the mode from insert to update & save the old code
       if ((this.mode == CSMQBean.MODE_INSERT_NEW || this.mode == CSMQBean.MODE_COPY_EXISTING) && !isSaved()) {
           this.currentContentCode = (String)results.get("NEW_DICT_CONTENT_CODE"); //
           this.copiedDictContentID = this.getCurrentDictContentID();
           this.currentDictContentID = (String)results.get("NEW_DICT_CONTENT_ID"); //newDictContentID;
           this.currentDateRequested = (oracle.jbo.domain.Date)results.get("CURRENT_DATE_REQUESTED");
           // if it's new, copy all the relations, too
           if (this.mode == CSMQBean.MODE_COPY_EXISTING && this.saved == false) {
               CSMQBean.logger.info(userBean.getCaller() + " currentContentCode:" + currentContentCode);
               CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID:" + currentDictContentID);
               CSMQBean.logger.info(userBean.getCaller() + " copiedDictContentID:" + copiedDictContentID);
               copyAllRelations();
               // also copy the inf notes, homes
               saveAllInfNotes();
            }
        }
       
       this.saved = true;
       
                 
        // set the state
       // if (userBean.isRequestor()) this.currentState = CSMQBean.STATE_PROPOSED;
       // if (userBean.isMQM()) this.currentState = CSMQBean.STATE_DRAFT;
        
        
        
        // lock down the level 
        //this.controlMQLevel.setDisabled(true);  <-- moved to UIBean
        
        // alert the user that everything saved ok
        if (!silent) {  //called from relations when we want to make sure the NMQ is saved before adding relations
            String messageText = tempName + " " + action + " successfully.";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, messageText, null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        
        currentTermName = tempName;
               
        //UPDATE THE RELATIONS TREE WITH THE NEW DICT ID
        if (mode != CSMQBean.MODE_IMPACT_ASSESSMENT) // we don't need to refresh the tree when making impact changes since it's only for SMQs 
            updateRelations();
        
         // now make it editable
        //commented below code to fix - 20141121-2 -Venkat
         //if (this.mode == CSMQBean.MODE_COPY_EXISTING)
          //  this.mode = CSMQBean.MODE_UPDATE_EXISTING;
        
        return true;
        }


    private void copyAllRelations() {

        CSMQBean.logger.info(userBean.getCaller() + " >>> COPYING RELATIONS");
        CSMQBean.logger.info(userBean.getCaller() + " copiedDictContentID:" + copiedDictContentID);
        CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID:" + currentDictContentID);
        CSMQBean.logger.info(userBean.getCaller() + " currentPredictGroups:" + currentPredictGroups);
        CSMQBean.logger.info(userBean.getCaller() + " currentUser:" + currentUser);


        /*
        PROCEDURE copy_all_relations (
          i_old_parent_id IN tms_dict_contents.dict_content_id%TYPE,
          i_new_parent_id IN tms_dict_contents.dict_content_id%TYPE,
          i_dest_group_name IN tms.tms_predict_groups.name%TYPE,
          i_src_group_name  IN tms.tms_predict_groups.name%TYPE := NULL,
          i_as_of_date    IN tms_dict_contents.end_ts%TYPE := SYSDATE);

        */

        String sql = "{call dict_pkg.copy_all_relations(?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        CallableStatement cstmt = dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT);

        try {
            cstmt.setInt(1, Integer.parseInt(copiedDictContentID));
            cstmt.setInt(2, Integer.parseInt(currentDictContentID));
            cstmt.setString(3, currentPredictGroups);
            cstmt.setString(4, currentPredictGroups);
            cstmt.setString(5, this.currentUser);
            cstmt.executeUpdate();
            cstmt.close();
            } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
     public void clearRelations() {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SmallTreeVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        vo.setNamedWhereClauseParam("dictContentID", CSMQBean.HIERARCHY_KILL_SWITCH);
        vo.executeQuery();
        TermHierarchyBean termHierarchyBean = (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
        termHierarchyBean.init(false);
        return;
        }
    
    
    
    public String updateRelations() {
        
        System.out.println("***updateRelations***");
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SmallTreeVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        
        String tGroup = null;
        
        ignorePredict = currentStatus.equals("CURRENT") ? CSMQBean.TRUE : CSMQBean.FALSE;
        
        // CHANGED 20-JUL-2012 to limit to current only in the following modes:
        if (this.mode == CSMQBean.MODE_COPY_EXISTING)
            ignorePredict = CSMQBean.TRUE;
        
        if (this.saved == true) ignorePredict = CSMQBean.FALSE; // if it has been saved we need predict records.
        
        if (this.currentStatus.equals(CSMQBean.PENDING_RELEASE_STATUS)) {
            if (null != this.currentPredictGroups){
                tGroup = this.getCurrentPredictGroups() + "," + cSMQBean.getDefaultPublishReleaseGroup();    
            } else {
                tGroup = cSMQBean.getDefaultDraftReleaseGroup() + "," + cSMQBean.getDefaultPublishReleaseGroup();
            }
            
            ignorePredict = CSMQBean.FALSE;
        }
        CSMQBean.logger.info(userBean.getCaller() + " ** UPDATING RELATIONS **");
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: " + "SmallTreeVO1Iterator");
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " + this.currentDictContentID);
        CSMQBean.logger.info(userBean.getCaller() + " maxLevels: " + maxLevels);
        CSMQBean.logger.info(userBean.getCaller() + " scopeFilter: " + this.getHierarchyParamScope());
        CSMQBean.logger.info(userBean.getCaller() + " sortKey: " + this.getHierarchyParamSort());
        CSMQBean.logger.info(userBean.getCaller() + " asOfDate: " + this.getCurrentUntilDate());
        CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath:" + getParamPrimLinkFlag());
        CSMQBean.logger.info(userBean.getCaller() + " ignorePredict:" + ignorePredict);
        CSMQBean.logger.info(userBean.getCaller() + " activationGroup:" + tGroup);
        
        vo.setNamedWhereClauseParam("dictContentID", this.currentDictContentID);
        vo.setNamedWhereClauseParam("maxLevels", maxLevels);
        vo.setNamedWhereClauseParam("scopeFilter", this.getHierarchyParamScope());
        vo.setNamedWhereClauseParam("returnPrimLinkPath", getParamPrimLinkFlag());
        vo.setNamedWhereClauseParam("ignorePredict", ignorePredict);
        vo.setNamedWhereClauseParam("sortKey", this.getHierarchyParamSort());
        if(ignorePredict.equalsIgnoreCase(CSMQBean.FALSE)){
            vo.setNamedWhereClauseParam("activationGroup", tGroup);
        }
        // CHANGED 26-SEP-2012
        // CHANGED 23-APR-2014 to add UPDATE and COPY MODES
        CSMQBean.logger.info(userBean.getCaller() + " dictVersion:" + this.currentVersion);
        if (this.mode == CSMQBean.MODE_HISTORIC || this.mode == CSMQBean.MODE_UPDATE_EXISTING || this.mode == CSMQBean.MODE_COPY_EXISTING || this.mode == CSMQBean.MODE_INSERT_NEW)
            vo.setNamedWhereClauseParam("dictVersion", this.currentVersion);
        //~
        
        if (this.getCurrentUntilDate() != null)
            vo.setNamedWhereClauseParam("asOfDate", this.getCurrentUntilDate());
    
        vo.executeQuery();
        
        
        return null;
    }


    public void setCurrentContentCode(String currentContentCode) {
        this.currentContentCode = currentContentCode;
    }

    public String getCurrentContentCode() {
        return currentContentCode;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentScope(String currentScope) {
        this.currentScope = currentScope;
    }

    public String getCurrentScope() {
        return currentScope;
    }

   
    public void getDictionaryInfo() {

//            String sql = "SELECT dict.short_name, dict.name, dict.description, s.def_detail_value VERSION FROM tms.tms_def_details    d, tms.tms_dict_info_hdrs h,                tms.tms_dict_info_strs s,                tms.tms_def_dictionaries dict           WHERE d.short_name               = '~DICTVER'             and dict.short_name = '" +this.currentFilterDictionaryShortName+ "'             AND h.dict_content_relation_id = dict.def_dictionary_id             AND d.def_detail_id            = h.def_detail_id AND h.dict_info_hdr_id = s.dict_info_hdr_id AND h.end_ts = TO_DATE(3000000,'J') AND s.end_ts = TO_DATE(3000000,'J')";
//            PreparedStatement stmt = null;
//            ResultSet rs = null;
//            DBTransaction dBTransaction = DMLUtils.getDBTransaction();
//            if(dBTransaction != null){
//            CallableStatement cstmt = dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT);
//                
//            System.out.println("***** : " + sql);
//            try {
//                rs = cstmt.executeQuery();
//                rs.next();
//                this.activeDictionaryName =  rs.getString("Name");// Utils.getAsString(row, "Name");
//                this.activeDictionaryVersion = rs.getString("Version");//Utils.getAsString(row, "Version");
//                
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            }
//            dictionaryInfoFetched = true;
        
        
            String sql = "SELECT dict.short_name, dict.name, dict.description, s.def_detail_value VERSION FROM tms.tms_def_details    d, tms.tms_dict_info_hdrs h,                tms.tms_dict_info_strs s,                tms.tms_def_dictionaries dict           WHERE d.short_name               = '~DICTVER'             and dict.short_name = '" +this.currentFilterDictionaryShortName+ "'             AND h.dict_content_relation_id = dict.def_dictionary_id             AND d.def_detail_id            = h.def_detail_id AND h.dict_info_hdr_id = s.dict_info_hdr_id AND h.end_ts = TO_DATE(3000000,'J') AND s.end_ts = TO_DATE(3000000,'J')";
            Connection conn = getConnection();
            PreparedStatement stmt = null;
            ResultSet rs = null;
            if(conn != null){
                
            System.out.println("***** : " + sql);
            try {
                stmt = conn.prepareCall(sql);
                rs = stmt.executeQuery();
                rs.next();
                this.activeDictionaryName =  rs.getString("Name");// Utils.getAsString(row, "Name");
                this.activeDictionaryVersion = rs.getString("Version");//Utils.getAsString(row, "Version");
                
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException sqle) {
                    // TODO: Add catch code
                    sqle.printStackTrace();
                }
            }
            }
            dictionaryInfoFetched = true;

            }

    
        
    public String saveAllInfNotes() {
        ADFUtils.setEL("#{pageFlowScope.isNotesChanged}", Boolean.FALSE);
        ADFUtils.setEL("#{pageFlowScope.notesFromTrain}", Boolean.FALSE);
        String action = (mode == CSMQBean.MODE_INSERT_NEW) ? "Inserted" : "Updated";
        int errorCount = 0;
        
        if (this.mode == CSMQBean.MODE_IMPACT_ASSESSMENT) {
            errorCount += NMQUtils.saveIAInfNotes(this.noteInformativeNoteShortName, this.currentInfNoteNotes, currentDictContentID, currentTermLevel, userBean.getCurrentUser(), userBean.getCurrentUserRole(), action, currentExtension);
             try {Thread.sleep(1000);} catch (InterruptedException e) {}
            errorCount += NMQUtils.saveIAInfNotes(this.descriptionInformativeNoteShortName, this.currentInfNoteDescription,  currentDictContentID, currentTermLevel, userBean.getCurrentUser(), userBean.getCurrentUserRole(), action, currentExtension);
             try {Thread.sleep(1000);} catch (InterruptedException e) {}
            errorCount += NMQUtils.saveIAInfNotes(this.sourceInformativeNoteShortName, this.currentInfNoteSource, currentDictContentID, currentTermLevel, userBean.getCurrentUser(), userBean.getCurrentUserRole(), action, currentExtension);
         }
        else {
            errorCount += NMQUtils.saveInfNotes(this.noteInformativeNoteShortName, this.currentInfNoteNotes, currentFilterDictionaryShortName, currentPredictGroups, currentDictContentID, currentTermLevel, userBean.getCurrentUser(), userBean.getCurrentUserRole(), action, currentExtension);
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            errorCount += NMQUtils.saveInfNotes(this.descriptionInformativeNoteShortName, this.currentInfNoteDescription, currentFilterDictionaryShortName, currentPredictGroups, currentDictContentID, currentTermLevel, userBean.getCurrentUser(), userBean.getCurrentUserRole(), action, currentExtension);
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            errorCount += NMQUtils.saveInfNotes(this.sourceInformativeNoteShortName, this.currentInfNoteSource, currentFilterDictionaryShortName, currentPredictGroups, currentDictContentID, currentTermLevel, userBean.getCurrentUser(), userBean.getCurrentUserRole(), action, currentExtension);
            }
        
        if (errorCount == 0) { // IF IT'S >0 THEN ONE FAILED
            
            String messageText = "All Informative Notes were " + action + " successfully.";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, messageText, null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
    }


    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
   

    public void setWizardStatus(String wizardStatus) {
        this.wizardStatus = wizardStatus;
    }
   

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public List<String> getProductList() {
        return productList;
    }
    
    /*
     * @author MTW
     * 07/21/2014
     */
    public void setDesigneeList(List<String> designeeList) {
        this.designeeList = designeeList;
    }

    public List<String> getDesigneeList() {
        return designeeList;
    }

    public void setMQGroupList(List<String> mQGroupList) {
        this.mQGroupList = mQGroupList;
    }

    public List<String> getMQGroupList() {
        return mQGroupList;
    }

    public String getCopiedDictContentID() {
        return copiedDictContentID;
    }

    public void setCurrentBaseDictionaryName(String currentBaseDictionaryName) {
        this.currentBaseDictionaryName = currentBaseDictionaryName;
    }

    public String getCurrentBaseDictionaryName() {
        return currentBaseDictionaryName;
    }

    public void setCurrentBaseDictionaryShortName(String currentBaseDictionaryShortName) {
        this.currentBaseDictionaryShortName = currentBaseDictionaryShortName;
    }

    public String getCurrentBaseDictionaryShortName() {
        return currentBaseDictionaryShortName;
    }

    public void setCurrentFilterDictionaryName(String currentFilterDictionaryName) {
        this.currentFilterDictionaryName = currentFilterDictionaryName;
    }

    public String getCurrentFilterDictionaryName() {
        return currentFilterDictionaryName;
    }

    public void setActiveDictionary(String activeDictionary) {
        this.activeDictionary = activeDictionary;
    }

    public String getActiveDictionary() {
        return activeDictionary;
    }

    public void setActiveDictionaryName(String activeDictionaryName) {
        this.activeDictionaryName = activeDictionaryName;
    }

    public String getActiveDictionaryName() {
        return activeDictionaryName;
    }

    public void setCurrentRequestor(String currentRequestor) {
        this.currentRequestor = currentRequestor;
    }

    public String getCurrentRequestor() {
        if (this.mode == CSMQBean.MODE_BROWSE_SEARCH || this.mode == CSMQBean.MODE_HISTORIC) return currentCreatedBy;
        return currentRequestor;
    }

    /* @author MTW
     * 06/12/2014
     * NMAT-UC01.02 & NMAT-UC11.02
    
    public void setCurrentDesignee(String currentDesignee) {
        this.currentDesignee = currentDesignee;   
    }
    
    public String getCurrentDesignee() {
        return currentDesignee;
    }
    */
    public void setUpdateParam(String updateParam) {
        this.updateParam = updateParam;
    }

    public String getUpdateParam() {
        //if (this.mode == CSMQBean.MODE_INSERT_NEW) updateParam = CSMQBean.DML_INSERT;
        if (this.mode == CSMQBean.MODE_UPDATE_EXISTING) updateParam = CSMQBean.DML_UPDATE;
        if (this.mode == CSMQBean.MODE_IMPACT_ASSESSMENT) updateParam = CSMQBean.DML_UPDATE;
        //fix - 20141121-2 -Venkat Instead of updating the mode to MODE_UPDATE_EXISTING, updated the updateParam to DML_UPDATE
        if (this.mode == CSMQBean.MODE_COPY_EXISTING || this.mode == CSMQBean.MODE_INSERT_NEW){
            if (this.isSaved()){
                updateParam = CSMQBean.DML_UPDATE;
            } else {
                updateParam = CSMQBean.DML_INSERT;
            }
        }
        return updateParam;
    }


    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean getSaved() {
        return saved;
    }

      

    public void setCurrentDictionary(String currentDictionary) {
        this.currentDictionary = currentDictionary;
    }

    public String getCurrentDictionary() {
        return currentDictionary;
    }

    public void setCurrentMQStatus(String currentMQStatus) {
        this.currentMQStatus = currentMQStatus;
    }

    public String getCurrentMQStatus() {
        return currentMQStatus;
    }
    

    public void setCurrentNarrowScopeOnly(String currentNarrowScopeOnly) {
        this.currentNarrowScopeOnly = currentNarrowScopeOnly;
    }

    public String getCurrentNarrowScopeOnly() {
        return currentNarrowScopeOnly;
    }

    public void setCurrentReasonForRequest(String currentReasonForRequest) {
        this.currentReasonForRequest = currentReasonForRequest;
    }

    public String getCurrentReasonForRequest() {
         return currentReasonForRequest;
    }

    public void setCurrentDateRequested(oracle.jbo.domain.Date currentDateRequested) {
        this.currentDateRequested = currentDateRequested;
    }

    public oracle.jbo.domain.Date getCurrentDateRequested() {
        return currentDateRequested;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentRequestedByDate(oracle.jbo.domain.Date currentRequestedByDate) {
        this.currentRequestedByDate = currentRequestedByDate;
    }

    public oracle.jbo.domain.Date getCurrentRequestedByDate() {
        return currentRequestedByDate;
    }

    public void setCurrentReasonForApproval(String currentReasonForApproval) {
        this.currentReasonForApproval = currentReasonForApproval;
    }

    public String getCurrentReasonForApproval() {
        return currentReasonForApproval;
    }

    public void setIsMedDRA(boolean isMedDRA) {
        this.isMedDRA = isMedDRA;
    }

    public boolean isIsMedDRA() {
        return isMedDRA;
    }

    public void setIsNMQ(boolean isNMQ) {
        this.isNMQ = isNMQ;
    }

    public boolean isIsNMQ() {
        return isNMQ;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public boolean isIsApproved() {
        if (this.mode == CSMQBean.MODE_BROWSE_SEARCH && this.currentStatus.equals(CSMQBean.PENDING_RELEASE_STATUS))
            isApproved = false;
            
        return isApproved;
    }

    public void setHierarchyParamScope(String hierarchyParamScope) {
        this.hierarchyParamScope = hierarchyParamScope;
    }

    public String getHierarchyParamScope() {
        return hierarchyParamScope;
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
        if (currentUntilDate == null) currentUntilDate = CSMQBean.DEFAULT_END_DATE;
        return currentUntilDate;
    }

    public void setCurrentCreateDate(String currentCreateDate) {
        this.currentCreateDate = currentCreateDate;
    }

    public String getCurrentCreateDate() {
        return currentCreateDate;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void sortChanged(ValueChangeEvent valueChangeEvent) {
        this.hierarchyParamSort = valueChangeEvent.getNewValue().toString();
        //updateRelations();
    }

    public void showSecondaryPathChanged(ValueChangeEvent valueChangeEvent) {
        this.paramPrimLinkFlag =  (Boolean)valueChangeEvent.getNewValue() ? CSMQBean.TRUE : CSMQBean.FALSE;
        //updateRelations();
    }

    public void scopeChanged(ValueChangeEvent valueChangeEvent) {
        this.hierarchyParamScope = valueChangeEvent.getNewValue().toString();
        //updateRelations();
    }
    
    public void maxLevelsChanged(ValueChangeEvent valueChangeEvent) {
        this.maxLevels = Integer.parseInt(valueChangeEvent.getNewValue().toString());
        updateRelations();
    }

    public void setHierarchyParamSort(String hierarchyParamSort) {
        this.hierarchyParamSort = hierarchyParamSort;
    }

    public String getHierarchyParamSort() {
        return hierarchyParamSort;
    }

    public void setParamPrimLinkFlag(String paramPrimLinkFlag) {
        this.paramPrimLinkFlag = paramPrimLinkFlag;
    }

    public String getParamPrimLinkFlag() {
        return paramPrimLinkFlag;
    }

    public void setCurrentCreatedBy(String currentCreatedBy) {
        this.currentCreatedBy = currentCreatedBy;
    }

    public String getCurrentCreatedBy() {
        return currentCreatedBy;
    }

    public void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    public int getMaxLevels() {
        return maxLevels;
    }
   
    public void setPerformImpactPriorToExport(boolean performImpactPriorToExport) {
        this.performImpactPriorToExport = performImpactPriorToExport;
    }

    public boolean isPerformImpactPriorToExport() {
        return performImpactPriorToExport;
    }

    public void setCurrentMQType(String currentMQType) {
        this.currentMQType = currentMQType;
    }

    public String getCurrentMQType() {
        if (this.mode == CSMQBean.MODE_COPY_EXISTING) this.currentMQType = CSMQBean.NMQ;
        return currentMQType;
    }
    
    
    
    
    public void setFooterInfo(String termName) {
        currentTermName = termName;
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.controlCurrentTermName);
        //AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.controlCurrentTermName);
    }


    public void setTreeAccessed(boolean treeAccessed) {
        this.treeAccessed = treeAccessed;
    }

    public boolean isTreeAccessed() {
        return treeAccessed;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }
    
    public void setIncludeLLTsInExport(boolean includeLLTsInExport) {
        //this.includeLLTsInExport = includeLLTsInExport;
    }

    public boolean getIncludeLLTsInExport() {
        return includeLLTsInExport;
    }

    
    public void includeLLTsInExportChanged(ValueChangeEvent valueChangeEvent) {
        includeLLTsInExport = (Boolean)valueChangeEvent.getNewValue();
    }

    public void exportDisplayedOnlyChanged(ValueChangeEvent valueChangeEvent) {
        exportDisplayedOnly = (Boolean)valueChangeEvent.getNewValue();
    }
    
    
    public void setExportDisplayedOnly(boolean showOnlyVisibleInExport) {
        this.exportDisplayedOnly = showOnlyVisibleInExport;
    }

    public boolean isExportDisplayedOnly() {
        return exportDisplayedOnly;
    }


    public void setActiveDictionaryVersion(String activeDictionaryVersion) {
        this.activeDictionaryVersion = activeDictionaryVersion;
    }

    public String getActiveDictionaryVersion() {
        return activeDictionaryVersion;
    }

    public void setCSMQBean(CSMQBean cSMQBean) {
        this.cSMQBean = cSMQBean;
    }

    public CSMQBean getCSMQBean() {
        return cSMQBean;
    }

    public void setIgnorePredict(String ignorePredict) {
        this.ignorePredict = ignorePredict;
    }

    public String getIgnorePredict() {
        return ignorePredict;
    }
    
    public void setCurrentInitialCreationDate(String currentInitialCreationDate) {
        this.currentInitialCreationDate = currentInitialCreationDate;
    }

    public String getCurrentInitialCreationDate() {
        return currentInitialCreationDate;
    }

    public void setCurrentInitialCreationBy(String currentInitialCreationBy) {
        this.currentInitialCreationBy = currentInitialCreationBy;
    }

    public String getCurrentInitialCreationBy() {
        return currentInitialCreationBy;
    }

    public void setCurrentLastActivationDate(String currentLastActivationDate) {
        this.currentLastActivationDate = currentLastActivationDate;
    }

    public String getCurrentLastActivationDate() {
        return currentLastActivationDate;
    }

    public void setCurrentActivationBy(String currentActivationBy) {
        this.currentActivationBy = currentActivationBy;
    }

    public String getCurrentActivationBy() {
        return currentActivationBy;
    }

    void updateDesigneeList(String string) {
    }

    public void setCurrentExtension(String currentExtension) {
        this.currentExtension = currentExtension;
    }

    public String getCurrentExtension() {
        return currentExtension;
    }

    public void setIsSMQ(boolean isSMQ) {
        this.isSMQ = isSMQ;
    }

    public boolean isIsSMQ() {
        return isSMQ;
    }

    /*
    public void setCntrlProductListSelectItems(UISelectItem cntrlProductListSelectItems) {
        this.cntrlProductListSelectItems = cntrlProductListSelectItems;
    }

    public UISelectItem getCntrlProductListSelectItems() {
        return cntrlProductListSelectItems;
    }
*/

    public void setProductSelectItems(ArrayList<SelectItem> productSelectItems) {
        this.productSelectItems = productSelectItems;
    }

    public ArrayList<SelectItem> getProductSelectItems() {
        return productSelectItems;
    }

    public void detailsPageLoad(PhaseEvent phaseEvent) {
        loadProductSelectList ();
    }

    public void setCurrentTermLevelNumber(String currentTermLevelNumber) {
        this.currentTermLevelNumber = currentTermLevelNumber;
    }

    public String getCurrentTermLevelNumber() {
        Pattern intsOnly = Pattern.compile("\\d+");
        Matcher makeMatch = intsOnly.matcher(currentTermLevel); 
        makeMatch.find();
        currentTermLevelNumber = makeMatch.group();
        return currentTermLevelNumber;
    }

    public void setDesigneeListAsString(String designeeListAsString) {
        this.designeeListAsString = designeeListAsString;
    }

    public String getDesigneeListAsString() {
       if (designeeList == null || designeeList.isEmpty()) return "";
        StringBuilder out = new StringBuilder();
        for (Object o : designeeList) {
          out.append(o.toString());
          out.append(",");
            }
        
        designeeListAsString = out.toString();
        int len = designeeListAsString.length();
        if (len > 0) designeeListAsString = designeeListAsString.substring(0 , len-1);
        
        return designeeListAsString;
    }
    
    
    public void setControlMQGroup(RichSelectManyChoice controlMQGroup) {
        this.controlMQGroup = controlMQGroup;
    }

    public RichSelectManyChoice getControlMQGroup() {
        return controlMQGroup;
    }
    
    
    public void setControlDesignee(RichSelectManyChoice controlDesignee) {
        this.controlDesignee = controlDesignee;
    }

    public RichSelectManyChoice getControlDesignee() {
        return controlDesignee;
    }
    
    
    public void clearDetails() {
        if (controlMQGroup != null) {
            try {
                this.getMQGroupList().clear();
                controlMQGroup.resetValue();
            } catch (java.lang.UnsupportedOperationException e) {
                e.printStackTrace();
            }
        }
        if (productListControl != null) {
            try {
                this.getProductList().clear();
                productListControl.resetValue();
            } catch (java.lang.UnsupportedOperationException e) {
                e.printStackTrace();
            }
        }
        
        if (controlDesignee != null) {
            try {
                this.getDesigneeList().clear();
                controlDesignee.resetValue();
            } catch (java.lang.UnsupportedOperationException e) {
                e.printStackTrace();
            }
        }

        this.setCurrentContentCode(null);
        
        
        // clear out the conf page too
        this.setCurrentReasonForRequest(null);
        this.setCurrentReasonForApproval(null);
        this.setCurrentRequestedByDate(null);
        this.setCurrentRequestedByDate(null);
        // and inf notes
        this.setCurrentInfNoteDescription(null);
        this.setCurrentInfNoteSource(null);
        this.setCurrentInfNoteNotes(null);
        this.saved = false;
        this.actionDelete = false;
        this.setCurrentInitialCreationDate(null);
        this.setCurrentInitialCreationBy(null);
        this.setCurrentLastActivationDate(null);
        this.setCurrentActivationBy(null);

    }

    public void productListValueChange(ValueChangeEvent valueChangeEvent) {
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.TRUE);
        ArrayList newList = new ArrayList(Arrays.asList(valueChangeEvent.getNewValue()));
        ArrayList oldList = new ArrayList(Arrays.asList(valueChangeEvent.getOldValue()));

        String newString = "";
        String oldString = "";
        ArrayList<String> outList = new ArrayList<String>();

        if (oldList != null) {
            for (int i = 0; i < oldList.size(); i++) {
                int l = oldList.size() - 1;
                oldString = oldList.get(l).toString();
            }
        }

        if (newList != null) {
            for (int i = 0; i < newList.size(); i++) {
                if(newList.size() > 0){
                int index = newList.size() - 1;
                if(newList.get(index) != null)
                newString = newList.get(index).toString();
                }
            }

            if (newString != null) {
                newString = newString.replaceAll("[\\[\\]]", "");
                StringTokenizer st = new StringTokenizer(newString, ","); // this needs to be a comma
                int nto = st.countTokens();
                for (int j = 0; j < nto; j++) {
                    String token = st.nextToken();
                    String temp = token.trim();

                    if (!temp.equals(CSMQBean.DEFAULT_PRODUCT))
                        outList.add(temp);

                }
            }
        }

        // CLEAR THE LIST
        this.getProductList().clear();
        productListControl.resetValue();

        standardProduct =
                !((newString.indexOf(CSMQBean.DEFAULT_PRODUCT) == -1) || 
                  ((newString.indexOf(CSMQBean.DEFAULT_PRODUCT) >-1 &&
                  newString.length() > CSMQBean.DEFAULT_PRODUCT.length()))); // if it's not there or if it is and there is also a product, then it's not standard

        if (!standardProduct) { // it's product specific - don't add STANDARD
            this.getProductList().addAll(outList);
        } else {
            if (this.isIsNMQ()) this.getProductList().add(CSMQBean.DEFAULT_PRODUCT); // it's standard - no products
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(productListControl);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(productListControl);

        AdfFacesContext.getCurrentInstance().addPartialTarget(controlCriticalEvent);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlCriticalEvent);
    }
    
    
    public void setProductListControl(RichSelectManyChoice productListControl) {
        this.productListControl = productListControl;
    }

    public RichSelectManyChoice getProductListControl() {
        return productListControl;
    }
    
    
    public void setControlCriticalEvent(RichSelectOneChoice controlCriticalEvent) {
        this.controlCriticalEvent = controlCriticalEvent;
    }

    public RichSelectOneChoice getControlCriticalEvent() {
        return controlCriticalEvent;
    }
    public boolean isSaved(){
        return this.saved;
    }

    public void setActionDelete(boolean actionDelete) {
        this.actionDelete = actionDelete;
    }

    public boolean isActionDelete() {
        return actionDelete;
    }
    
    public String setModeViewVersionImpact() {
        String isViewPreviousFlow = (String) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("isViewPreviousFlow");
        this.currentPredictGroups = cSMQBean.getDefaultMedDRAReleaseGroup();
        if ("Y".equals(isViewPreviousFlow)) {
            userBean.setCurrentMenuPath("Previous Version Impact");
        } else {
            userBean.setCurrentMenuPath("View Version Impact");
        }
        userBean.setCurrentMenu("VIEW_VERSION_IMPACT");
        this.mode = CSMQBean.MODE_VIEW_VERSION_IMPACT;
        this.updateParam = CSMQBean.DML_NONE;
        CSMQBean.logger.info(userBean.getCaller() + " SETTING MODE: " + this.mode);
        setDefaultDictionary ();
        
        return null;
        }

    public void setProductListAsString(String productListAsString) {
        this.productListAsString = productListAsString;
    }

    public String getProductListAsString() {
        if (productList == null || productList.isEmpty()) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            for (Object o : productList) {
               out.append(o.toString());
               out.append(",");
            }
            productListAsString = out.toString();
        }
        int len = productListAsString.length();
        if (len > 0){
            productListAsString = productListAsString.substring(0, len-1);
        }
        return productListAsString;
    }
	public void setRenderProduct(boolean renderProduct) {
        this.renderProduct = renderProduct;
    }

    public boolean isRenderProduct() {
        return renderProduct;
    }

    public void setCurrentReleaseGroup(String currentReleaseGroup) {
        this.currentReleaseGroup = currentReleaseGroup;
    }

    public String getCurrentReleaseGroup() {
        return currentReleaseGroup;
    }

    public String showWarningPopup() {
        Boolean isDetailsChanged = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.isDetailsChanged}");
        if(isDetailsChanged != null && isDetailsChanged){
            ADFUtils.showPopup(getAddDetailsWarningPopup());
            return null;
        }
        return "CANCEL";
    }

    public void setAddDetailsWarningPopup(RichPopup addDetailsWarningPopup) {
        this.addDetailsWarningPopup = addDetailsWarningPopup;
    }

    public RichPopup getAddDetailsWarningPopup() {
        return addDetailsWarningPopup;
    }

    public String showInfoNotesWarningPopup() {
        Boolean isNotesChanged = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.isNotesChanged}");
        if(isNotesChanged != null && isNotesChanged){
            ADFUtils.showPopup(getInfoNotesWarningPopup());
            return null;
        }
        return "CANCEL";
    }

    public void setInfoNotesWarningPopup(RichPopup infoNotesWarningPopup) {
        this.infoNotesWarningPopup = infoNotesWarningPopup;
    }

    public RichPopup getInfoNotesWarningPopup() {
        return infoNotesWarningPopup;
    }

    public String showRelationsWarningPopup() {
        Object relationIcon = ADFUtils.evaluateEL("#{pageFlowScope.TermHierarchyBean.iconMQChanged}");
        if(relationIcon == null){
            return "CANCEL";
        }
        else{
            RichImage img = (RichImage)relationIcon;
            if(img.isVisible()){
                ADFUtils.showPopup(getAddRelationsWarningPopup());
                return null;
            }
            else{
                return "CANCEL";
            }
        }
    }

    public void notesChanged(ValueChangeEvent valueChangeEvent) {
        ADFUtils.setEL("#{pageFlowScope.isNotesChanged}", Boolean.TRUE);
    }

    public String yesWarning() {
        Boolean fromTrain = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.notesFromTrain}");
        if(fromTrain != null && fromTrain){
            saveAllInfNotes();
            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
            ActionEvent ae = new ActionEvent(button);
            ae.queue();
            getInfoNotesWarningPopup().hide();
            return null;
        }
        else{
            saveAllInfNotes();
            return "CANCEL";
        }
    }

    public void setAddRelationsWarningPopup(RichPopup addRelationsWarningPopup) {
        this.addRelationsWarningPopup = addRelationsWarningPopup;
    }

    public RichPopup getAddRelationsWarningPopup() {
        return addRelationsWarningPopup;
    }

    public String onYesWarningAction() {
        Boolean fromTrain = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.relationsFromTrain}");
        if(fromTrain != null && fromTrain){
            ADFUtils.setEL("#{pageFlowScope.relationsFromTrain}", Boolean.FALSE);
            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
            ActionEvent ae = new ActionEvent(button);
            ae.queue();
            getAddRelationsWarningPopup().hide();
            return null;
        }
        else{
            return "CANCEL";
        }
    }

    public String noWarning() {
        Boolean fromTrain = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.notesFromTrain}");
        if(fromTrain != null && fromTrain){
            ADFUtils.setEL("#{pageFlowScope.notesFromTrain}", Boolean.FALSE);
            ADFUtils.setEL("#{pageFlowScope.isNotesChanged}", Boolean.FALSE);
            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
            ActionEvent ae = new ActionEvent(button);
            ae.queue();
            getInfoNotesWarningPopup().hide();
            return null;
        }
        else{
            return "CANCEL";
        }
    }

    public String onNoRelationsWarning() {
        Boolean fromTrain = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.relationsFromTrain}");
        if(fromTrain != null && fromTrain){
            ADFUtils.setEL("#{pageFlowScope.relationsFromTrain}", Boolean.FALSE);
            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
            ActionEvent ae = new ActionEvent(button);
            ae.queue();
            getAddRelationsWarningPopup().hide();
            return null;
        }
        else{
            return "CANCEL";
        }
    }
    
    public void queueDetailAction(){
        RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
        ActionEvent ae = new ActionEvent(button);
        ae.queue();
        getAddDetailsWarningPopup().hide();
    }
    
    public String yesWarningDetails() {
        Boolean fromTrain = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.detailsFromTrain}");
        if(fromTrain != null && fromTrain){
            saveDetails();
            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
            ActionEvent ae = new ActionEvent(button);
            ae.queue();
            getAddDetailsWarningPopup().hide();
            return null;
        }
        else{
            saveDetails();
            return "CANCEL";
        }
    }

    public String noWarningDetails() {
        Boolean fromTrain = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.detailsFromTrain}");
        if(fromTrain != null && fromTrain){
            ADFUtils.setEL("#{pageFlowScope.detailsFromTrain}", Boolean.FALSE);
            ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.FALSE);
            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
            ActionEvent ae = new ActionEvent(button);
            ae.queue();
            getAddDetailsWarningPopup().hide();
            return null;
        }
        else{
            return "CANCEL";
        }
    }
    
    private Connection getConnection(){
        Connection dbConnection = null;
        if (null != dbConnection){
            return dbConnection;
        }else {
            try {
                InitialContext initialContext = new InitialContext();
                DataSource ds = (DataSource)initialContext.lookup(dbUrlString);
                dbConnection = ds.getConnection();
            } catch (NamingException e) {
                        e.printStackTrace();
            } catch (SQLException e) {
                        e.printStackTrace();
            }
        }
        return dbConnection;
    }
}
