package com.dbms.csmq.view;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.backing.NMQ.NMQUtils;
import com.dbms.csmq.view.backing.NMQ.NMQWizardBean;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.context.AdfFacesContext;

public class RenderingRulesBean { 
    
  
    // ******************************** RENDERED *******************************************
    
    // SEARCH
    private boolean wizardSearchRenderQueryType = true;   
    private boolean wizardSearchRenderScope = true;
    private boolean wizardSearchRenderSMQStatus = true;
    private boolean wizardSearchRenderCriticalEvent = true;
    private boolean wizardSearchRenderGroup = true;
    private boolean wizardSearchRenderSMQSelectItem = true;
    private boolean wizardSearchRenderMaintainDesingees = false;

    // DETAILS
    private boolean wizardDetailsRenderNMQName = true;
    private boolean wizardDetailsRenderDictionary = true;
    private boolean wizardDetailsRenderReleaseGroup = true;
    private boolean wizardDetailsRenderNMQStatus = true;
    private boolean wizardDetailsRenderNMQAlgorithm = true;
    private boolean wizardDetailsRenderNMQProduct = true;
    private boolean wizardDetailsRenderCriticalEvent = true;
    private boolean wizardDetailsRenderNMQLevel = true;
    private boolean wizardDetailsRenderNMQScope = true;
    private boolean wizardDetailsRenderNMQGroup = true;
    private boolean wizardDetailsRenderNMQCode = true;
    private boolean wizardDetailsRenderQueryType = true;
    private boolean wizardDetailsRenderSave = true;
    private boolean wizardDetailsRenderIsApproved = true;
    private boolean wizardDetailsRenderState = true;
    private boolean wizardDetailsRenderExtension = true;
    private boolean wizardDetailsRenderActivationInfo = false;
    private boolean wizardDetailsRenderNMQSelItems = false;
    private boolean wizardDetailsRenderSMQSelItems = false;
    private boolean wizardDetailsRenderMedDRASelItems = false;
    private boolean wizardDetailsShowSMQInSelectList = false;
    private boolean wizardDetailsShowDesigneeList = true;
    private boolean wizardDetailsRenderDesignee = true;
    private boolean wizardDetailsRenderInitalCreateInfo = true;
    
    // RELATIONS
    private boolean wizardRelationsRenderSourceTree = true;
    private boolean wizardRelationsRenderDelete = true;
    private boolean wizardRelationsRenderSave = true;
    private boolean wizardRelationsRenderScope = true;
    
    // INF NOTES
    private boolean wizardInfNotesRenderSave = true;
    private boolean wizardInfNotesRenderTrainStop = true;
    
    // CONFIRM
    private boolean wizardConfirmRenderReactivate = true;
    private boolean wizardConfirmRenderRetire = true;
    private boolean wizardConfirmRenderSubmitToMQM = true;
    private boolean wizardConfirmRenderDelete = true;
    private boolean wizardConfirmRenderInitalCreateInfo = true;
    
    // MENU
    private boolean renderMenu = true;
    
    // ADD RELATIONS SEARCH
    private boolean addRelationsSearchSelectItemMedDRA= false;
    private boolean addRelationsSearchSelectItemSMQ = false;
    private boolean addRelationsSearchSelectItemCustom = false;
    
        
    //************************************ DISABLED *******************************************  
    
    // SEARCH
    private boolean wizardSearchDisableDictionary = false;
    private boolean wizardSearchDisableReleaseStatus = false;
    private boolean wizardSearchDisableQueryType = false;
    private boolean wizardSearchDisableLevel = false;
    private boolean wizardSearchDisableExtension = false;
      
    
    // DETAILS
    private boolean wizardDetailsDisableNMQName = false;
    private boolean wizardDetailsDisableDictionary = false;
    private boolean wizardDetailsDisableReleaseGroup = false;
    private boolean wizardDetailsDisableNMQStatus = false;
    private boolean wizardDetailsDisableNMQAlgorithm = false;
    private boolean wizardDetailsDisableNMQProduct = false;
    private boolean wizardDetailsDisableCriticalEvent = false;
    private boolean wizardDetailsDisableNMQLevel = false;
    private boolean wizardDetailsDisableNMQScope = false;
    private boolean wizardDetailsDisableScope = false;
    private boolean wizardDetailsDisableNMQGroup = false;
    private boolean wizardDetailsDisableNMQCode = false;
    private boolean wizardDetailsDisableSave = false;
    private boolean wizardDetailsDisableExtention = false;
    private boolean wizardDetialsDisableCurrentUserDesigneeSelectItem = false;
    private boolean wizardDetailsDisplayContentCode = false;
    private boolean wizardDetailsDisableNMQDesignee = false;
    
    /*
     * @author MTW
     * 06/20/2014
     * @fsds NMAT-UC01.02 & NMAT-UC11.02
     */
    private boolean wizardDetailsDisableDesignee = false;
    // INF NOTES
    private boolean wizardInfNotesDisableEdit = false;
    
    // RELATIONS
    private boolean wizardRelationsDisableDelete = false;
    private boolean wizardRelationsDisableSave = false;
    
    // CONFIRM
    private boolean wizardConfirmDisableReactivate = false;
    private boolean wizardConfirmDisableRetire = false;
    private boolean wizardConfirmDisableSubmitToMQM = false;
    private boolean wizardConfirmDisableDelete = false;
    private boolean wizardConfirmDisableDemoteToDraft = false;
    private boolean wizardConfirmDisableReviewed = false;
    private boolean wizardConfirmDisableApproved = false;
    private boolean wizardConfirmDisablePublished = false;
    private boolean wizardShowPublished = false;
    
    // ************************************ REQUIRED *******************************************
    
    // **** REQUIRED ****
    private boolean wizardDetailsRequireNMQAlgorithm = false;
    private boolean wizardDetailsRequireNMQGroup = false;
    private boolean wizardDetailsRequireLevel = false;
    private boolean wizardDetailsRequireCriticalEvent = false;
    /* @author MTW
    *  06/12/2014
    *  @fsds NMAT-UC01.01 & NMAT-UC11.01
    */
    private boolean wizardDetailsRequireDesignee = false;
    
    
    //STYLES
    private String wizardRelationsBackgroundFieldColor;
    
    
    // **** LABELS ****
    private String wizardInformativeNotesLabelPrefix;
    private String wizardConfirmLabelPrefix;
    private String wizardDetailsTermName;
    private String wizardSearchQueryType;
    
    // @TODO ARE THESE NEEDED NOW?
    private boolean wizardSearchNMQScopeParam = false;
    private boolean wizardSearchSMQScopeParam = false;
    
    
    // SELECT ITEM FILTERS
    private boolean SMQspecific = false;
    private boolean NMQspecific = false;
    private boolean MEDspecific = false;
    
    // MISC
    private String wizardDetailsCancelButtonText = "Cancel";
    private String wizardDetailsCancelButtonHoverText = "";

 
    // ROLES            
    private boolean USER_MQM;
    private boolean USER_REQ;
    private boolean USER_ADMIN;
    private boolean USER_USER;
    private boolean USER_LOGGED_IN;
    /*
     * @author MTW
     * 06/20/2014
     * @fsds NMAT-UC01.01 & NMAT-UC11.01 & NMAT-UC01.02 & NMAT-UC11.02
     */
    private boolean USER_DESIGNEE;
                        
    // MODES            
    private boolean MODE_NEW;
    private boolean MODE_UPDATE;
    private boolean MODE_COPY;
    private boolean MODE_EDIT_SMQ;
    private boolean MODE_BROWSE;
    private boolean MODE_HISTORIC;
    private boolean MODE_IMPACT_ASSESSMENT;
    private boolean MODE_VIEW_VERSION_IMPACT;
    // DICTIONARY
    private boolean DICT_MED;
    
    // QUERY TYPE
    private boolean QUERY_NMQ;
    private boolean QUERY_SMQ;
    private boolean QUERY_CUSTOM;
    
    // STATES
    private boolean STATE_DRAFT;
    private boolean STATE_REQUESTED;
    private boolean STATE_APPROVED;
    private boolean STATE_PUBLISHED;
    private boolean STATE_PROPOSED;
    private boolean STATE_REVIEWED;
    
    private boolean STATE_APPROVED_IA;
    private boolean STATE_PUBLISHED_IA;
    // RELEASE STATUS
    private boolean CURRENT_RELEASE_STATUS;
    private boolean PENDING_RELEASE_STATUS;
    
    // ACTIVITY STATUS
    private boolean ACTIVE_ACTIVITY_STATUS;
    private boolean INACTIVE_ACTIVITY_STATUS;
    private boolean PENDING_ACTIVITY_STATUS;

    NMQWizardBean nMQWizardBean = (NMQWizardBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
    UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
    String CSMQName = CSMQBean.customMQName;
    
     public RenderingRulesBean() {
        
        // ROLES
        USER_MQM = userBean.isUserInRole("MQM"); 
        USER_REQ = userBean.isUserInRole("Requestor"); 
        USER_ADMIN = userBean.isUserInRole("Admin");
        USER_USER = userBean.isUserInRole("User");
        USER_LOGGED_IN = userBean.isLoggedIn();
        /*
         * @author MTW
         * 06/20/2014
         * @fsds NMAT-UC01.01 & NMAT-UC11.01 & NMAT-UC01.02 & NMAT-UC11.02
         */
        USER_DESIGNEE = userBean.isUserInRole("Designee");
        
        // MODES
        MODE_NEW = (nMQWizardBean.getMode() == CSMQBean.MODE_INSERT_NEW);
        MODE_UPDATE = (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_EXISTING);
        MODE_COPY = (nMQWizardBean.getMode() == CSMQBean.MODE_COPY_EXISTING);
        MODE_EDIT_SMQ = (nMQWizardBean.getMode() == CSMQBean.MODE_UPDATE_SMQ);
        MODE_BROWSE = (nMQWizardBean.getMode() == CSMQBean.MODE_BROWSE_SEARCH);
        MODE_HISTORIC = (nMQWizardBean.getMode() == CSMQBean.MODE_HISTORIC);
        MODE_IMPACT_ASSESSMENT = (nMQWizardBean.getMode() == CSMQBean.MODE_IMPACT_ASSESSMENT);
        MODE_VIEW_VERSION_IMPACT = (nMQWizardBean.getMode() == CSMQBean.MODE_VIEW_VERSION_IMPACT);   
        // DICTIONARY
        DICT_MED = nMQWizardBean.isIsMedDRA();
        MEDspecific = DICT_MED;  
        
        // QUERY TYPES
        QUERY_NMQ = nMQWizardBean.isIsNMQ(); 
        QUERY_SMQ = nMQWizardBean.isIsSMQ(); 
        QUERY_CUSTOM = !(QUERY_NMQ || QUERY_SMQ);
        
        // STATES
        if (nMQWizardBean.getCurrentState() != null) {
            STATE_APPROVED = (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_APPROVED));
            STATE_PUBLISHED = (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_PUBLISHED));
            STATE_PROPOSED = (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_PROPOSED));
            STATE_DRAFT = (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_DRAFT));
            STATE_REQUESTED = (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_REQUESTED));
            STATE_REVIEWED = (nMQWizardBean.getCurrentState().equals(CSMQBean.STATE_REVIEWED));
            STATE_APPROVED_IA = (nMQWizardBean.getCurrentState().equals(CSMQBean.IA_STATE_APPROVED));
            STATE_PUBLISHED_IA = (nMQWizardBean.getCurrentState().equals(CSMQBean.IA_STATE_PUBLISHED));
            }

        // RELEASE STATUS
        if (nMQWizardBean.getCurrentStatus() != null) {
            CURRENT_RELEASE_STATUS = (nMQWizardBean.getCurrentStatus().equals(CSMQBean.CURRENT_RELEASE_STATUS));
            PENDING_RELEASE_STATUS = (nMQWizardBean.getCurrentStatus().equals(CSMQBean.PENDING_RELEASE_STATUS));
            }
        
        // ACTIVITY STATUS
        String tempActivityStatus = nMQWizardBean.getCurrentMQStatus();
        
        if (tempActivityStatus != null) {
            ACTIVE_ACTIVITY_STATUS = (tempActivityStatus.equals(CSMQBean.ACTIVE_ACTIVITY_STATUS));
            INACTIVE_ACTIVITY_STATUS = (tempActivityStatus.equals(CSMQBean.INACTIVE_ACTIVITY_STATUS));
            PENDING_ACTIVITY_STATUS = (tempActivityStatus.equals(CSMQBean.PENDING_ACTIVITY_STATUS));
            }
        }

    //  *******************  RENDERED *****************

    public void setWizardDetailsRenderNMQName(boolean renderNMQName) {
        this.wizardDetailsRenderNMQName = renderNMQName;
    }

    public boolean isWizardDetailsRenderNMQName() {
        return wizardDetailsRenderNMQName;
    }

    public void setWizardDetailsRenderDictionary(boolean renderDictionary) {
        this.wizardDetailsRenderDictionary = renderDictionary;
    }

    public boolean isWizardDetailsRenderDictionary() {
        return wizardDetailsRenderDictionary;
    }

    public void setWizardDetailsRenderReleaseGroup(boolean renderReleaseGroup) {
        this.wizardDetailsRenderReleaseGroup = renderReleaseGroup;
    }

    public boolean isWizardDetailsRenderReleaseGroup() {
        if (CURRENT_RELEASE_STATUS || MODE_HISTORIC) wizardDetailsRenderReleaseGroup= false;
        return wizardDetailsRenderReleaseGroup;
    }

    public void setWizardDetailsRenderNMQStatus(boolean renderNMQStatus) {
        this.wizardDetailsRenderNMQStatus = renderNMQStatus;
    }

    public boolean isWizardDetailsRenderNMQStatus() {
        if (MODE_EDIT_SMQ || MODE_IMPACT_ASSESSMENT) wizardDetailsRenderNMQStatus = false;
        return wizardDetailsRenderNMQStatus;
    }

    public void setWizardDetailsRenderNMQAlgorithm(boolean renderNMQAlgorithm) {
        this.wizardDetailsRenderNMQAlgorithm = renderNMQAlgorithm;
    }

    public boolean isWizardDetailsRenderNMQAlgorithm() {
        if (DICT_MED) wizardDetailsRenderNMQAlgorithm = false;
        return wizardDetailsRenderNMQAlgorithm;
    }

    public void setWizardDetailsRenderNMQProduct(boolean renderNMQProduct) {
        this.wizardDetailsRenderNMQProduct = renderNMQProduct;
    }

    public boolean isWizardDetailsRenderNMQProduct() {
        if (MODE_EDIT_SMQ || QUERY_SMQ) wizardDetailsRenderNMQProduct = false;
        return wizardDetailsRenderNMQProduct;
    }

    public void setWizardDetailsRenderCriticalEvent(boolean renderCriticalEvent) {
        this.wizardDetailsRenderCriticalEvent = renderCriticalEvent;
    }

    public boolean isWizardDetailsRenderCriticalEvent() {
        if (DICT_MED) wizardDetailsRenderCriticalEvent = false;
        return wizardDetailsRenderCriticalEvent;
    }

    public void setWizardDetailsRenderNMQLevel(boolean renderNMQLevel) {
        this.wizardDetailsRenderNMQLevel = renderNMQLevel;
    }

    public boolean isWizardDetailsRenderNMQLevel() {
        return wizardDetailsRenderNMQLevel;
    }

    public void setWizardDetailsRenderNMQScope(boolean renderNMQScope) {
        this.wizardDetailsRenderNMQScope = renderNMQScope;
    }

    public boolean isWizardDetailsRenderNMQScope() {
         if (DICT_MED) wizardDetailsRenderNMQScope = false;
        return wizardDetailsRenderNMQScope;
    }

    public void setWizardDetailsRenderNMQGroup(boolean renderNMQGroup) {
        this.wizardDetailsRenderNMQGroup = renderNMQGroup;
    }

    public boolean isWizardDetailsRenderNMQGroup() {
        if (DICT_MED) wizardDetailsRenderNMQGroup = false;
        return wizardDetailsRenderNMQGroup;
    }

    public void setWizardDetailsRenderNMQCode(boolean renderNMQCode) {
        this.wizardDetailsRenderNMQCode = renderNMQCode;
    }

    public boolean isWizardDetailsRenderNMQCode() {
        return wizardDetailsRenderNMQCode;
    }

     public void setWizardDetailsRenderQueryType(boolean renderQueryType) {
        this.wizardDetailsRenderQueryType = renderQueryType;
    }

    public boolean isWizardDetailsRenderQueryType() {
        if (MODE_COPY) this.wizardDetailsRenderQueryType = true;
        return wizardDetailsRenderQueryType;
    }
    
    public void setWizardDetailsRenderIsApproved(boolean wizardDetailsRenderIsApproved) {
        this.wizardDetailsRenderIsApproved = wizardDetailsRenderIsApproved;
    }

    public boolean isWizardDetailsRenderIsApproved() {
        if (!MODE_BROWSE) wizardDetailsRenderIsApproved = false;
        return wizardDetailsRenderIsApproved;
    }
    
    public void setWizardInfNotesRenderTrainStop(boolean wizardInfNotesRenderTrainStop) {
        this.wizardInfNotesRenderTrainStop = wizardInfNotesRenderTrainStop;
    }

    public boolean isWizardInfNotesRenderTrainStop() {
        if (MODE_BROWSE && DICT_MED) wizardInfNotesRenderTrainStop = false;
        return wizardInfNotesRenderTrainStop;
    }
    
     public void setWizardDetailsRenderState(boolean wizardDetailsRenderState) {
        this.wizardDetailsRenderState = wizardDetailsRenderState;
    }

    public boolean isWizardDetailsRenderState() {
        if (MODE_EDIT_SMQ || MODE_BROWSE) wizardDetailsRenderState = false;
        return wizardDetailsRenderState;
    }
    
    public void setWizardDetailsRenderExtension(boolean wizardDetailsRenderExtension) {
        this.wizardDetailsRenderExtension = wizardDetailsRenderExtension;
    }

    public boolean getWizardDetailsRenderExtension() {
        if (MODE_IMPACT_ASSESSMENT || DICT_MED || MODE_VIEW_VERSION_IMPACT) wizardDetailsRenderExtension = false;
        return wizardDetailsRenderExtension;
    }
    
    /*
     * @author MTW
     * 06/20/2014
     * @fsds NMAT-UC01.02 & NMAT-UC11.02
     */
    public void setWizardDetailsRenderDesignee(boolean wizardDetailsRenderDesignee) {
        this.wizardDetailsRenderDesignee = wizardDetailsRenderDesignee;
    }

    public boolean isWizardDetailsRenderDesignee() {
        return wizardDetailsRenderDesignee;
    }

    
    //  ***********************   DISABLED   *******************
    
    public void setWizardDetailsDisableNMQName(boolean disableNMQName) {
        this.wizardDetailsDisableNMQName = disableNMQName;
    }

    public boolean isWizardDetailsDisableNMQName() {
        if (MODE_EDIT_SMQ || MODE_IMPACT_ASSESSMENT || MODE_BROWSE || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED ))) wizardDetailsDisableNMQName = true;
        return wizardDetailsDisableNMQName;
    }

    public void setWizardDetailsDisableDictionary(boolean disableDictionary) {
        this.wizardDetailsDisableDictionary = disableDictionary;
    }

    public boolean isWizardDetailsDisableDictionary() {
        return wizardDetailsDisableDictionary;
    }

    public void setWizardDetailsDisableReleaseGroup(boolean disableReleaseGroup) {
        this.wizardDetailsDisableReleaseGroup = disableReleaseGroup;
    }

    public boolean isWizardDetailsDisableReleaseGroup() {
        if (MODE_NEW || MODE_UPDATE || MODE_BROWSE || MODE_EDIT_SMQ || MODE_IMPACT_ASSESSMENT || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED))) this.wizardDetailsDisableReleaseGroup = true;
        return wizardDetailsDisableReleaseGroup;
    }

    public void setWizardDetailsDisableNMQStatus(boolean disableNMQStatus) {
        this.wizardDetailsDisableNMQStatus = disableNMQStatus;
    }

    public boolean isWizardDetailsDisableNMQStatus() {
        return wizardDetailsDisableNMQStatus;
    }

    public void setWizardDetailsDisableNMQAlgorithm(boolean disableNMQAlgorithm) {
        this.wizardDetailsDisableNMQAlgorithm = disableNMQAlgorithm;
    }

    public boolean isWizardDetailsDisableNMQAlgorithm() {
         if (MODE_BROWSE || MODE_IMPACT_ASSESSMENT || MODE_EDIT_SMQ || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED)) 
             || (MODE_IMPACT_ASSESSMENT && (STATE_APPROVED_IA || STATE_PUBLISHED_IA)) 
             || (QUERY_CUSTOM && USER_REQ)) this.wizardDetailsDisableNMQAlgorithm = true;
        return wizardDetailsDisableNMQAlgorithm;
    }

    public void setWizardDetailsDisableNMQProduct(boolean disableNMQProduct) {
        this.wizardDetailsDisableNMQProduct = disableNMQProduct;
    }

    public boolean isWizardDetailsDisableNMQProduct() {
        if (MODE_BROWSE || MODE_IMPACT_ASSESSMENT || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED))) 
            this.wizardDetailsDisableNMQProduct = true;
        return wizardDetailsDisableNMQProduct;
    }

    public void setWizardDetailsDisableCriticalEvent(boolean disableCriticalEvent) {
        this.wizardDetailsDisableCriticalEvent = disableCriticalEvent;
    }

    public boolean isWizardDetailsDisableCriticalEvent() {
        if (USER_REQ || USER_USER || MODE_BROWSE || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED)) 
            || (MODE_IMPACT_ASSESSMENT && (STATE_APPROVED_IA || STATE_PUBLISHED_IA))) this.wizardDetailsDisableCriticalEvent = true;
        return wizardDetailsDisableCriticalEvent;
    }

    public void setWizardDetailsDisableNMQLevel(boolean disableNMQLevel) {
        this.wizardDetailsDisableNMQLevel = disableNMQLevel;
    }

    public boolean isWizardDetailsDisableNMQLevel() {
        //if (!QUERY_NMQ ||  MODE_EDIT_SMQ ||  MODE_BROWSE  ||  MODE_IMPACT_ASSESSMENT || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED))) this.wizardDetailsDisableNMQLevel = true;
        if (!(QUERY_NMQ && (MODE_NEW || MODE_COPY))) this.wizardDetailsDisableNMQLevel = true;
        return wizardDetailsDisableNMQLevel;
    }

    public void setWizardDetailsDisableNMQScope(boolean disableNMQScope) {
        this.wizardDetailsDisableNMQScope = disableNMQScope;
    }

    /*
     * @author MTW
     * 06/18/2014
     * @fsds NMAT-UC01.01 & NMAT-UC11.01
     * Added USER_REQ
     * @TODO Add USER_DESIGNEE
     */
    public boolean isWizardDetailsDisableNMQScope() {
        if (USER_DESIGNEE || USER_REQ || USER_USER || MODE_BROWSE || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED))
            || (MODE_IMPACT_ASSESSMENT && (STATE_APPROVED_IA || STATE_PUBLISHED_IA))) this.wizardDetailsDisableNMQScope = true;
        return wizardDetailsDisableNMQScope;
    }

    public void setWizardDetailsDisableNMQGroup(boolean disableNMQGroup) {
        this.wizardDetailsDisableNMQGroup = disableNMQGroup;
    }

    /*
     * @author MTW
     * 06/18/2014
     * @fsds NMAT-UC01.01 & NMAT-UC11.01 & NMAT-UC01.02 & NMAT-UC11.02
     * Removed USER_REQ (Requestor)
     */
    public boolean isWizardDetailsDisableNMQGroup() {
        if (USER_USER || MODE_BROWSE || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED))
            || (MODE_IMPACT_ASSESSMENT && (STATE_APPROVED_IA || STATE_PUBLISHED_IA))) this.wizardDetailsDisableNMQGroup = true;

        return wizardDetailsDisableNMQGroup;
    }

    public void setWizardDetailsDisableNMQCode(boolean disableNMQCode) {
        this.wizardDetailsDisableNMQCode = disableNMQCode;
    }

    public boolean isWizardDetailsDisableNMQCode() {
        return wizardDetailsDisableNMQCode;
    }

    public void setWizardSearchDisableDictionary(boolean wizardSearchDisableDictionary) {
        this.wizardSearchDisableDictionary = wizardSearchDisableDictionary;
    }

    public boolean isWizardSearchDisableDictionary() {
        if (MODE_UPDATE || MODE_COPY || MODE_EDIT_SMQ || MODE_IMPACT_ASSESSMENT) this.wizardSearchDisableDictionary = true;
        return wizardSearchDisableDictionary;
    }

    public void setWizardSearchDisableQueryType(boolean wizardSearchDisableQueryType) {
        this.wizardSearchDisableQueryType = wizardSearchDisableQueryType;
    }

    public boolean isWizardSearchDisableQueryType() {
        if (MODE_UPDATE || MODE_EDIT_SMQ || MODE_IMPACT_ASSESSMENT) this.wizardSearchDisableQueryType = true;
        return wizardSearchDisableQueryType;
    }

    public void setWizardSearchNMQScopeParam(boolean wizardSearchNMQScopeParam) {
        this.wizardSearchNMQScopeParam = wizardSearchNMQScopeParam;
    }

    public boolean isWizardSearchNMQScopeParam() {
        return wizardSearchNMQScopeParam;
    }

    public void setWizardSearchSMQScopeParam(boolean wizardSearchSMQScopeParam) {
        this.wizardSearchSMQScopeParam = wizardSearchSMQScopeParam;
    }

    public boolean isWizardSearchSMQScopeParam() {
        return wizardSearchSMQScopeParam;
    }

    public void setWizardSearchRenderQueryType(boolean wizardSearchRenderQueryType) {
        this.wizardSearchRenderQueryType = wizardSearchRenderQueryType;
    }

    /* Author: AMC
     * Removed MODE_UPDATE from the parameters to get the Update module to display the Extension field 
     * NMAT-UC03.03 
     * NMAT-UC12.03
     */
    public boolean isWizardSearchRenderQueryType() {
        return true;
       // if (MODE_EDIT_SMQ || MODE_IMPACT_ASSESSMENT) this.wizardSearchRenderQueryType = false;
        //return wizardSearchRenderQueryType;
    }

    public void setSMQspecific(boolean SMQspecific) {
        this.SMQspecific = SMQspecific;
    }

    public boolean isSMQspecific() {  // this is used to determine which 
        if (MODE_EDIT_SMQ || MODE_IMPACT_ASSESSMENT || MODE_BROWSE) SMQspecific = true;
        return SMQspecific;
    }

    public void setNMQspecific(boolean NMQspecific) {
        this.NMQspecific = NMQspecific;
    }

    public boolean isNMQspecific() {
        if (MODE_NEW || MODE_UPDATE || MODE_COPY || MODE_BROWSE) NMQspecific = true;
        return NMQspecific;
    }

    public void setWizardInfNotesDisableEdit(boolean wizardInfNotesDisableSave) {
        this.wizardInfNotesDisableEdit = wizardInfNotesDisableSave;
    }

    public boolean isWizardInfNotesDisableEdit() {
        if (MODE_EDIT_SMQ || MODE_BROWSE || STATE_APPROVED || STATE_PUBLISHED 
            || (MODE_IMPACT_ASSESSMENT && (QUERY_SMQ || STATE_APPROVED_IA || STATE_PUBLISHED_IA)) ) wizardInfNotesDisableEdit = true;
        return wizardInfNotesDisableEdit;
    }

    public void setWizardRelationsRenderSourceTree(boolean wizardRelationsRenderSourceTree) {
        this.wizardRelationsRenderSourceTree = wizardRelationsRenderSourceTree;
    }

    public boolean isWizardRelationsRenderSourceTree() {
        if (MODE_EDIT_SMQ || MODE_BROWSE || MODE_HISTORIC || MODE_IMPACT_ASSESSMENT) wizardRelationsRenderSourceTree = false;
        return wizardRelationsRenderSourceTree;
    }

    public void setWizardRelationsRenderDelete(boolean wizardRelationsDisableDelete) {
        this.wizardRelationsRenderDelete = wizardRelationsDisableDelete;
    }

    public boolean isWizardRelationsRenderDelete() {
        if (MODE_EDIT_SMQ || MODE_BROWSE || MODE_HISTORIC) wizardRelationsRenderDelete = false;
        return wizardRelationsRenderDelete;
    }

    public void setWizardInfNotesRenderSave(boolean wizardInfNotesRenderSave) {
        this.wizardInfNotesRenderSave = wizardInfNotesRenderSave;
    }

    public boolean isWizardInfNotesRenderSave() {
        if (MODE_EDIT_SMQ || MODE_BROWSE) wizardInfNotesRenderSave = false;
        return wizardInfNotesRenderSave;
    }

    public void setWizardDetailsRenderSave(boolean wizardDetailsRenderSave) {
        this.wizardDetailsRenderSave = wizardDetailsRenderSave;
    }

    public boolean isWizardDetailsRenderSave() {
        if (MODE_BROWSE) wizardDetailsRenderSave = false;
        return wizardDetailsRenderSave;
    }

    public void setWizardSearchRenderScope(boolean wizardSearchRenderScope) {
        this.wizardSearchRenderScope = wizardSearchRenderScope;
    }

    public boolean isWizardSearchRenderScope() {
        return wizardSearchRenderScope;
    }

    public void setWizardSearchRenderSMQStatus(boolean wizardSearchRenderSMQStatus) {
        this.wizardSearchRenderSMQStatus = wizardSearchRenderSMQStatus;
    }

    public boolean isWizardSearchRenderSMQStatus() {
        return wizardSearchRenderSMQStatus;
    }

    public void setWizardSearchRenderCriticalEvent(boolean wizardSearchRenderCriticalEvent) {
        this.wizardSearchRenderCriticalEvent = wizardSearchRenderCriticalEvent;
    }

    public boolean isWizardSearchRenderCriticalEvent() {
        return wizardSearchRenderCriticalEvent;
    }

    public void setWizardSearchRenderGroup(boolean wizardSearchRenderGroup) {
        this.wizardSearchRenderGroup = wizardSearchRenderGroup;
    }

    public boolean isWizardSearchRenderGroup() {
        return wizardSearchRenderGroup;
    }
    
    public void setWizardRelationsRenderSave(boolean wizardRelationsRenderSave) {
        this.wizardRelationsRenderSave = wizardRelationsRenderSave;
    }

    public boolean isWizardRelationsRenderSave() {
        if (MODE_EDIT_SMQ || MODE_BROWSE || MODE_HISTORIC) wizardRelationsRenderSave = false;
        return wizardRelationsRenderSave;
    }

    public void setWizardConfirmRenderReactivate(boolean wizardConfirmRenderReactivate) {
        this.wizardConfirmRenderReactivate = wizardConfirmRenderReactivate;
    }

    public boolean isWizardConfirmRenderReactivate() {
        if (MODE_EDIT_SMQ) wizardConfirmRenderReactivate = false;
        return wizardConfirmRenderReactivate;
    }

    public void setWizardConfirmRenderRetire(boolean wizardConfirmRenderRetire) {
        this.wizardConfirmRenderRetire = wizardConfirmRenderRetire;
    }

    public boolean isWizardConfirmRenderRetire() {
        if (MODE_EDIT_SMQ) wizardConfirmRenderRetire = false;
        return wizardConfirmRenderRetire;
    }

    public void setWizardConfirmRenderSubmitToMQM(boolean wizardConfirmRenderSubmitToMQM) {
        this.wizardConfirmRenderSubmitToMQM = wizardConfirmRenderSubmitToMQM;
    }

    public boolean isWizardConfirmRenderSubmitToMQM() {
        if (USER_MQM) wizardConfirmRenderSubmitToMQM = false;
        return wizardConfirmRenderSubmitToMQM;
    }

    public void setWizardConfirmRenderDelete(boolean wizardConfirmRenderDelete) {
        this.wizardConfirmRenderDelete = wizardConfirmRenderDelete;
    }

    public boolean isWizardConfirmRenderDelete() {
        if (MODE_NEW || MODE_COPY || MODE_EDIT_SMQ || MODE_BROWSE) wizardConfirmRenderDelete = false;
        return wizardConfirmRenderDelete;
    }

    public void setWizardDetailsCancelButtonText(String wizardDetailsCancelButtonText) {
        this.wizardDetailsCancelButtonText = wizardDetailsCancelButtonText;
    }

    public String getWizardDetailsCancelButtonText() {
       if (MODE_NEW || MODE_COPY || MODE_UPDATE || MODE_EDIT_SMQ) 
            wizardDetailsCancelButtonText = "Cancel/Exit";
        return wizardDetailsCancelButtonText;
    }

    public void setWizardDetailsCancelButtonHoverText(String wizardDetailsCancelButtonHoverText) {
        this.wizardDetailsCancelButtonHoverText = wizardDetailsCancelButtonHoverText;
    }

    public String getWizardDetailsCancelButtonHoverText() {
        if (MODE_NEW || MODE_COPY || MODE_UPDATE || MODE_EDIT_SMQ) wizardDetailsCancelButtonHoverText = "Save your work before Cancel/Exit.  Only work that has been Saved will be available.";
        return wizardDetailsCancelButtonHoverText;
    }

    public void setRenderMenu(boolean renderMenu) {
        this.renderMenu = renderMenu;
    }

    public boolean isRenderMenu() {
        if (MODE_BROWSE) renderMenu = false;
        return renderMenu;
    }

    public void setWizardConfirmDisableReactivate(boolean wizardConfirmDisableReactivate) {
        this.wizardConfirmDisableReactivate = wizardConfirmDisableReactivate;
    }

    public boolean isWizardConfirmDisableReactivate() {
        if (!(USER_MQM || USER_ADMIN) || !INACTIVE_ACTIVITY_STATUS) wizardConfirmDisableReactivate = true;
        return wizardConfirmDisableReactivate;
    }

    public void setWizardConfirmDisableRetire(boolean wizardConfirmDisableRetire) {
        this.wizardConfirmDisableRetire = wizardConfirmDisableRetire;
    }

    public boolean isWizardConfirmDisableRetire() {
        if (!(USER_MQM || USER_ADMIN) || MODE_EDIT_SMQ || PENDING_RELEASE_STATUS || INACTIVE_ACTIVITY_STATUS) wizardConfirmDisableRetire = true;
        return wizardConfirmDisableRetire;
    }

    public void setWizardConfirmDisableSubmitToMQM(boolean wizardConfirmDisableSubmitToMQM) {
        this.wizardConfirmDisableSubmitToMQM = wizardConfirmDisableSubmitToMQM;
    }

    public boolean isWizardConfirmDisableSubmitToMQM() {
        if(!(USER_REQ && STATE_PROPOSED)) wizardConfirmDisableSubmitToMQM = true;
        return wizardConfirmDisableSubmitToMQM;
    }

    public void setWizardConfirmDisableDelete(boolean wizardConfirmDisableDelete) {
        this.wizardConfirmDisableDelete = wizardConfirmDisableDelete;
    }

    public boolean isWizardConfirmDisableDelete() {
    if (CURRENT_RELEASE_STATUS || (!QUERY_CUSTOM && USER_REQ && !STATE_PROPOSED)) wizardConfirmDisableDelete = true;
            return wizardConfirmDisableDelete;
    }

    public void setWizardConfirmDisableDemoteToDraft(boolean wizardConfirmDisableDemoteToDraft) {
        this.wizardConfirmDisableDemoteToDraft = wizardConfirmDisableDemoteToDraft;
    }

    public boolean isWizardConfirmDisableDemoteToDraft() {
        
        if ((STATE_DRAFT || STATE_REQUESTED || STATE_PROPOSED || CURRENT_RELEASE_STATUS) || 
            (USER_REQ && (!QUERY_CUSTOM || !STATE_PUBLISHED))) wizardConfirmDisableDemoteToDraft = true;
        //if (!USER_MQM || STATE_DRAFT || STATE_REQUESTED || STATE_PROPOSED || CURRENT_RELEASE_STATUS) wizardConfirmDisableDemoteToDraft = true;
        return wizardConfirmDisableDemoteToDraft;
    }

    public void setMEDspecific(boolean MEDspecific) {
        this.MEDspecific = MEDspecific;
    }

    public boolean isMEDspecific() {
        return MEDspecific;
    }

    public void setWizardDetailsDisableSave(boolean wizardDetailsDisableSave) {
        this.wizardDetailsDisableSave = wizardDetailsDisableSave;
    }

    public boolean isWizardDetailsDisableSave() {
        if (STATE_APPROVED || STATE_PUBLISHED || STATE_APPROVED_IA || STATE_PUBLISHED_IA) wizardDetailsDisableSave = true;                         
        return wizardDetailsDisableSave;
    }
    
    public void setWizardDetailsDisableExtention(boolean wizardDetailsDisableExtention) {
        this.wizardDetailsDisableExtention = wizardDetailsDisableExtention;
    }

    public boolean isWizardDetailsDisableExtention() {
        if ((MODE_UPDATE && !(USER_MQM || USER_ADMIN)) || (MODE_EDIT_SMQ || MODE_BROWSE || STATE_APPROVED || STATE_PUBLISHED 
                || (MODE_IMPACT_ASSESSMENT && (QUERY_SMQ || STATE_APPROVED_IA  || STATE_PUBLISHED_IA)))) wizardDetailsDisableExtention = true;
        return wizardDetailsDisableExtention;
    }

    public void setWizardRelationsDisableDelete(boolean wizardRelationsDisableDelete) {
        this.wizardRelationsDisableDelete = wizardRelationsDisableDelete;
    }

    public boolean isWizardRelationsDisableDelete() {
        if (STATE_APPROVED || STATE_PUBLISHED || STATE_APPROVED_IA  || STATE_PUBLISHED_IA) wizardRelationsDisableDelete = true;
        return wizardRelationsDisableDelete;
    }

    public void setWizardRelationsDisableSave(boolean wizardRelationsDisableSave) {
        this.wizardRelationsDisableSave = wizardRelationsDisableSave;
    }

    public boolean isWizardRelationsDisableSave() {
        if (STATE_APPROVED || STATE_PUBLISHED || STATE_APPROVED_IA  || STATE_PUBLISHED_IA) wizardRelationsDisableSave = true;
        return wizardRelationsDisableSave;
    }


    public void setWizardConfirmDisableReviewed(boolean wizardConfirmDisableReviewed) {
        this.wizardConfirmDisableReviewed = wizardConfirmDisableReviewed;
    }

    public boolean isWizardConfirmDisableReviewed() {
        if (!(STATE_DRAFT  && (USER_MQM || USER_ADMIN))) wizardConfirmDisableReviewed = true;
        return wizardConfirmDisableReviewed;
    }

    public void setWizardConfirmDisableApproved(boolean wizardConfirmDisableApproved) {
        this.wizardConfirmDisableApproved = wizardConfirmDisableApproved;
    }

    public boolean isWizardConfirmDisableApproved() {
        if (!(STATE_REVIEWED && (USER_MQM || USER_ADMIN))) wizardConfirmDisableApproved = true;
        return wizardConfirmDisableApproved;
    }
    
    /*
     * @author MTW
     * 06/20/2014
     * @fsds NMAT-UC01.01 & NMAT-UC11.01 & NMAT-UC01.02 & NMAT-UC11.02
     */
    public void setWizardDetailsDisableDesignee(boolean wizardDetailsDisableDesignee) {
        this.wizardDetailsDisableDesignee = wizardDetailsDisableDesignee;
    }

    public boolean isWizardDetailsDisableDesignee() {
        if (MODE_BROWSE || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED)) 
           || (MODE_IMPACT_ASSESSMENT && (STATE_APPROVED_IA  || STATE_PUBLISHED_IA))) this.wizardDetailsDisableDesignee = true;
        return wizardDetailsDisableDesignee;
    }
    


    ///  ******** REQUIRED *****


    public void setWizardDetailsRequireNMQAlgorithm(boolean wizardDetailsRequireNMQAlgorithm) {
        this.wizardDetailsRequireNMQAlgorithm = wizardDetailsRequireNMQAlgorithm;
    }

    public boolean isWizardDetailsRequireNMQAlgorithm() {
        if (MODE_COPY || MODE_EDIT_SMQ || MODE_NEW || MODE_UPDATE || MODE_IMPACT_ASSESSMENT)
            wizardDetailsRequireNMQAlgorithm = true;
        return wizardDetailsRequireNMQAlgorithm;
    }

    public void setWizardDetailsRequireNMQGroup(boolean wizardDetailsRequireNMQGroup) {
        this.wizardDetailsRequireNMQGroup = wizardDetailsRequireNMQGroup;
    }

    public boolean isWizardDetailsRequireNMQGroup() {
        if (MODE_COPY || MODE_EDIT_SMQ || MODE_NEW || MODE_UPDATE || MODE_IMPACT_ASSESSMENT)
            wizardDetailsRequireNMQGroup = true;
        return wizardDetailsRequireNMQGroup;
    }

    public void setWizardDetailsRequireLevel(boolean wizardDetailsRequireLevel) {
        this.wizardDetailsRequireLevel = wizardDetailsRequireLevel;
    }

    public boolean isWizardDetailsRequireLevel() {
        if (MODE_COPY || MODE_EDIT_SMQ || MODE_NEW || MODE_UPDATE || MODE_IMPACT_ASSESSMENT)
            wizardDetailsRequireLevel = true;
        return wizardDetailsRequireLevel;
    }

    public void setWizardDetailsRequireCriticalEvent(boolean wizardDetailsRequireCriticalEvent) {
        this.wizardDetailsRequireCriticalEvent = wizardDetailsRequireCriticalEvent;
    }

    public boolean isWizardDetailsRequireCriticalEvent() {
        if (MODE_COPY || MODE_EDIT_SMQ || MODE_NEW || MODE_UPDATE || MODE_IMPACT_ASSESSMENT)
            wizardDetailsRequireCriticalEvent = true;
        return wizardDetailsRequireCriticalEvent;
    }

   /* @author MTW
    * 06/12/2014
    * NMAT-UC01.02 & NMAT-UC11.02
    */
    public void setWizardDetailsRequireDesignee(boolean wizardDetailsRequireDesignee) {
        this.wizardDetailsRequireDesignee = wizardDetailsRequireDesignee;
    }
    
    public boolean isWizardDetailsRequireDesignee() {
        if (!QUERY_NMQ && (MODE_COPY || MODE_EDIT_SMQ || MODE_NEW || MODE_UPDATE || MODE_IMPACT_ASSESSMENT))
            wizardDetailsRequireDesignee = true;
        return wizardDetailsRequireDesignee;
    }

    public void setWizardInformativeNotesLabelPrefix(String wizardInformativeNotesLabel) {
        this.wizardInformativeNotesLabelPrefix = wizardInformativeNotesLabel;
    }

    public String getWizardInformativeNotesLabelPrefix() {
        wizardInformativeNotesLabelPrefix = CSMQBean.customMQName;
        if (MODE_BROWSE) wizardInformativeNotesLabelPrefix = CSMQBean.customMQName + "/" + CSMQBean.SMQ_NAME;
        return wizardInformativeNotesLabelPrefix;
    }

    public void setWizardConfirmLabelPrefix(String wizardConfirmLabel) {
        this.wizardConfirmLabelPrefix = wizardConfirmLabel;
    }

    public String getWizardConfirmLabelPrefix() {
        wizardConfirmLabelPrefix = CSMQBean.customMQName;
        if (MODE_EDIT_SMQ) wizardConfirmLabelPrefix = CSMQBean.SMQ_NAME;
        return wizardConfirmLabelPrefix;
        
    }

    public void setWizardDetailsTermName(String wizardDetailsTermName) {
        this.wizardDetailsTermName = wizardDetailsTermName;
    }

    public String getWizardDetailsTermName() {       
        wizardDetailsTermName = CSMQBean.customMQName + " Name";
        if (MODE_BROWSE) wizardDetailsTermName = "term";
        return wizardDetailsTermName;
    }

    public void setWizardSearchQueryType(String wizardSearchQueryType) {
        this.wizardSearchQueryType = wizardSearchQueryType;
    }

    public String getWizardSearchQueryType() {
        wizardSearchQueryType = "Level";
        if (MODE_COPY || MODE_EDIT_SMQ) wizardSearchQueryType = "Query Type";
        return wizardSearchQueryType;
    }

    public void setWizardRelationsRenderScope(boolean wizardRelationsRenderScope) {
        this.wizardRelationsRenderScope = wizardRelationsRenderScope;
    }

    public boolean isWizardRelationsRenderScope() {
        if (DICT_MED) wizardRelationsRenderScope = false;        
        return wizardRelationsRenderScope;
    }


    public void setWizardRelationsBackgroundFieldColor(String wizardRelationsBackgroundFieldColor) {
        this.wizardRelationsBackgroundFieldColor = wizardRelationsBackgroundFieldColor;
    }

    public String getWizardRelationsBackgroundFieldColor() {
        wizardRelationsBackgroundFieldColor = "background-color:transparent;";
      
        if ((nMQWizardBean.getCurrentScope() == null) || (!nMQWizardBean.getCurrentScope().equals(CSMQBean.HAS_SCOPE)))
            wizardRelationsBackgroundFieldColor = "font-style:italic; background-color:Silver;";
        
        return wizardRelationsBackgroundFieldColor;
    }
    

    public void setWizardSearchDisableReleaseStatus(boolean wizardSearchDisableReleaseStatus) {
        this.wizardSearchDisableReleaseStatus = wizardSearchDisableReleaseStatus;
    }

    public boolean isWizardSearchDisableReleaseStatus() {
        if (!USER_LOGGED_IN) wizardSearchDisableReleaseStatus = true;
        return wizardSearchDisableReleaseStatus;
    }

    public void setAddRelationsSearchSelectItemMedDRA(boolean addRelationsSearchSelectItemNMQ) {
        this.addRelationsSearchSelectItemMedDRA = addRelationsSearchSelectItemNMQ;
    }

    public boolean isAddRelationsSearchSelectItemMedDRA() {
        if (DICT_MED) addRelationsSearchSelectItemMedDRA = true;
        return addRelationsSearchSelectItemMedDRA;
    }

    public void setAddRelationsSearchSelectItemSMQ(boolean addRelationsSearchSelectItemSMQ) {
        this.addRelationsSearchSelectItemSMQ = addRelationsSearchSelectItemSMQ;
    }

    public boolean isAddRelationsSearchSelectItemSMQ() {
        if (!DICT_MED && QUERY_NMQ) addRelationsSearchSelectItemSMQ = true;
        return addRelationsSearchSelectItemSMQ;
    }

    public void setAddRelationsSearchSelectItemCustom(boolean addRelationsSearchSelectItemCustom) {
        this.addRelationsSearchSelectItemCustom = addRelationsSearchSelectItemCustom;
    }

    public boolean isAddRelationsSearchSelectItemCustom() {
        if (!DICT_MED && QUERY_CUSTOM) addRelationsSearchSelectItemCustom = true;
        return addRelationsSearchSelectItemCustom;
    }

    public void setWizardDetailsRenderActivationInfo(boolean wizardDetailsRenderActivationInfo) {
        this.wizardDetailsRenderActivationInfo = wizardDetailsRenderActivationInfo;
    }

    public boolean isWizardDetailsRenderActivationInfo() {
        if (MODE_NEW || MODE_UPDATE || MODE_BROWSE || MODE_COPY) wizardDetailsRenderActivationInfo = true;
        
        return wizardDetailsRenderActivationInfo;
    }

    public void setWizardDetailsRenderNMQSelItems(boolean wizardDetailsRenderNMQSelItems) {
        this.wizardDetailsRenderNMQSelItems = wizardDetailsRenderNMQSelItems;
    }

    public boolean isWizardDetailsRenderNMQSelItems() {
        if (QUERY_NMQ || QUERY_CUSTOM ) wizardDetailsRenderNMQSelItems = true;
        return wizardDetailsRenderNMQSelItems;
    }

    public void setWizardDetailsRenderSMQSelItems(boolean wizardDetailsRenderSMQSelItems) {
        this.wizardDetailsRenderSMQSelItems = wizardDetailsRenderSMQSelItems;
    }

    public boolean isWizardDetailsRenderSMQSelItems() {
        if (QUERY_SMQ || MODE_COPY) wizardDetailsRenderSMQSelItems = true;
        return wizardDetailsRenderSMQSelItems;
    }

    public void setWizardDetailsRenderMedDRASelItems(boolean wizardDetailsRenderMedDRASelItems) {
        this.wizardDetailsRenderMedDRASelItems = wizardDetailsRenderMedDRASelItems;
    }

    public boolean isWizardDetailsRenderMedDRASelItems() {
        if (DICT_MED) wizardDetailsRenderMedDRASelItems = true;
        return wizardDetailsRenderMedDRASelItems;
    }

    public void setWizardSearchDisableLevel(boolean wizardSearchDisableLevel) {
        this.wizardSearchDisableLevel = wizardSearchDisableLevel;
    }

    public boolean isWizardSearchDisableLevel() {
        if (!DICT_MED || QUERY_CUSTOM) wizardSearchDisableLevel = true;
        return wizardSearchDisableLevel;
    }

    public void setWizardConfirmDisablePublished(boolean wizardConfirmPublished) {
        this.wizardConfirmDisablePublished = wizardConfirmPublished;
    }

    public boolean isWizardConfirmDisablePublished() { 
        if (!(QUERY_CUSTOM && STATE_DRAFT) || STATE_PUBLISHED ) {
            wizardConfirmDisablePublished = true;
        }
        return wizardConfirmDisablePublished;
    }

    public void setWizardShowPublished(boolean wizardShowPublished) {
        this.wizardShowPublished = wizardShowPublished;
    }

    public boolean isWizardShowPublished() {
        NMQWizardBean nMQWizardBean = (NMQWizardBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
        String hasRelations = NMQUtils.checkMqHasRelations(nMQWizardBean.getCurrentDictContentID());
        if("NO_RELATION".equalsIgnoreCase(hasRelations)){
            wizardShowPublished = true;
        }else{
            wizardShowPublished = false; 
        }
        return wizardShowPublished;
    }
    //    public boolean isWizardShowPublished() {
//            NMQWizardBean nMQWizardBean = (NMQWizardBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
//            String hasRelations = NMQUtils.checkMqHasRelations(nMQWizardBean.getCurrentDictContentID());
//        if ("NO_RELATION".equalsIgnoreCase(hasRelations)) {
//            return 
//        }
//        return wizardConfirmDisablePublished;
//    }

    public void setWizardDetailsShowSMQInSelectList(boolean wizardDetailsShowSMQInSelectList) {
        this.wizardDetailsShowSMQInSelectList = wizardDetailsShowSMQInSelectList;
    }

    public boolean isWizardDetailsShowSMQInSelectList() {
        if (MODE_BROWSE || MODE_EDIT_SMQ || MODE_VIEW_VERSION_IMPACT) wizardDetailsShowSMQInSelectList = true;        
        return wizardDetailsShowSMQInSelectList;
    }

    public void setWizardDetailsShowDesigneeList(boolean wizardDetailsShowDesigneeList) {
        this.wizardDetailsShowDesigneeList = wizardDetailsShowDesigneeList;
    }

    public boolean isWizardDetailsShowDesigneeList() {
        if (QUERY_SMQ && !MODE_COPY) wizardDetailsShowDesigneeList = false;
        return wizardDetailsShowDesigneeList;
    }

    public void setWizardSearchRenderSMQSelectItem(boolean wizardSearchRenderSMQSelectItem) {
        this.wizardSearchRenderSMQSelectItem = wizardSearchRenderSMQSelectItem;
    }

    public boolean isWizardSearchRenderSMQSelectItem() {
        if (!(MODE_BROWSE || MODE_COPY || MODE_EDIT_SMQ)) wizardSearchRenderSMQSelectItem = false;
        return wizardSearchRenderSMQSelectItem;
    }

    public void setWizardDetialsDisableCurrentUserDesigneeSelectItem(boolean wizardDetialsDisableCurrentUserDesigneeSelectItem) {
        this.wizardDetialsDisableCurrentUserDesigneeSelectItem = wizardDetialsDisableCurrentUserDesigneeSelectItem;
    }

    public boolean isWizardDetialsDisableCurrentUserDesigneeSelectItem() {
        if (!(MODE_NEW || MODE_COPY)) wizardDetialsDisableCurrentUserDesigneeSelectItem = false;
        return wizardDetialsDisableCurrentUserDesigneeSelectItem;
    }

    public void setWizardDetailsRenderInitalCreateInfo(boolean wizardDetailsRenderInitalCreateInfo) {
        this.wizardDetailsRenderInitalCreateInfo = wizardDetailsRenderInitalCreateInfo;
    }

    public boolean isWizardDetailsRenderInitalCreateInfo() {
        if (MODE_NEW || MODE_COPY) wizardDetailsRenderInitalCreateInfo = false;
        return wizardDetailsRenderInitalCreateInfo;
    }

    public void setWizardConfirmRenderInitalCreateInfo(boolean wizardConfirmRenderInitalCreateInfo) {
        this.wizardConfirmRenderInitalCreateInfo = wizardConfirmRenderInitalCreateInfo;
    }

    public boolean isWizardConfirmRenderInitalCreateInfo() {
        if (MODE_NEW || MODE_COPY) wizardConfirmRenderInitalCreateInfo = false;
        return wizardConfirmRenderInitalCreateInfo;
    }

    public void setWizardSearchDisableExtension(boolean wizardSearchDisableExtension) {
        this.wizardSearchDisableExtension = wizardSearchDisableExtension;
    }

    public boolean isWizardSearchDisableExtension() {
        if (MODE_EDIT_SMQ) wizardSearchDisableExtension = true;
        return wizardSearchDisableExtension;
    }

    public void setWizardDetailsDisplayContentCode(boolean wizardDetailsDisplayContentCode) {
        this.wizardDetailsDisplayContentCode = wizardDetailsDisplayContentCode;
    }

    public boolean isWizardDetailsDisplayContentCode() {
        if (MODE_UPDATE || MODE_EDIT_SMQ || MODE_BROWSE || MODE_IMPACT_ASSESSMENT) wizardDetailsDisplayContentCode = true;
        return wizardDetailsDisplayContentCode;
    }

    public void setWizardSearchRenderMaintainDesingees(boolean wizardSearchRenderMaintainDesingees) {
        this.wizardSearchRenderMaintainDesingees = wizardSearchRenderMaintainDesingees;
    }

    public boolean isWizardSearchRenderMaintainDesingees() {
        if (MODE_UPDATE){
            wizardSearchRenderMaintainDesingees = true;
        }
        return wizardSearchRenderMaintainDesingees;
    }

    public void setWizardDetailsDisableNMQDesignee(boolean disableNMQDesignee) {
        this.wizardDetailsDisableNMQDesignee = disableNMQDesignee;
    }

    public boolean isWizardDetailsDisableNMQDesignee() {
        if (MODE_BROWSE || (MODE_IMPACT_ASSESSMENT && (QUERY_SMQ || STATE_APPROVED_IA || STATE_PUBLISHED_IA)) || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED))) 
            this.wizardDetailsDisableNMQDesignee = true;
        return wizardDetailsDisableNMQDesignee;
    }

    public void setWizardDetailsDisableScope(boolean wizardDetailsDisableScope) {
        this.wizardDetailsDisableScope = wizardDetailsDisableScope;
    }

    public boolean isWizardDetailsDisableScope() {
        if (USER_DESIGNEE || USER_USER || MODE_BROWSE || (MODE_UPDATE && (STATE_APPROVED || STATE_PUBLISHED))
            || (MODE_IMPACT_ASSESSMENT && (STATE_APPROVED_IA || STATE_PUBLISHED_IA))) this.wizardDetailsDisableScope = true;
        return wizardDetailsDisableScope;
    }
}
