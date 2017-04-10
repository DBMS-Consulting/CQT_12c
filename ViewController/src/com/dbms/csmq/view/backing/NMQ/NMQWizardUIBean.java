package com.dbms.csmq.view.backing.NMQ;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisBean;
import com.dbms.csmq.view.hierarchy.TermHierarchyBean;

import com.dbms.util.ADFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichToolbar;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichTrain;
import oracle.adf.view.rich.component.rich.nav.RichTrainButtonBar;
import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.domain.Date;


public class NMQWizardUIBean {


    // DETAILS CONTROLS
    
    private RichInputText controlRequestorID;
    private RichInputText controlCreateDate;
    
    private RichSelectOneChoice controlMQLevel;
    
    private RichSelectOneChoice controlReleaseGroup;
    private RichInputText controlMQState;
    private RichInputText controlMQCode;
    private RichInputText controlDictionaryVersion;
    private RichInputText controlMqAlgorithm;
    private RichSelectOneChoice controlMQScope;
  

    // INF NOTES CONTROLS
    private RichInputText controlInfNoteMQDescription;
    private RichInputText controlInfNoteMQSource;
    private RichInputText controlInfNoteMQNotes;

    // Status controls
    private RichInputText ctrlActiveDictionary;
    private RichInputText controlMQStatus;

    private boolean standardProduct = true;

    private RichInputText controlMQName;
    private RichInputText controlNMQCode;
    private RichOutputFormatted controlWizardStatus;
    private CSMQBean cSMQBean;
    private UserBean userBean;
    private RichInputText controlCurrentTermName;
    private RichInputText controlRequestor;
    private RichTrain cntrlTrain;
    private RichTrainButtonBar cntrlTrainButtons;

    private RichInputText cntrlReasonForRequest;
    private oracle.jbo.domain.Date currentDateRequested;
    private oracle.jbo.domain.Date currentRequestedByDate;
    private RichInputDate cntrlDateRequested;
    private RichCommandButton cntrlApproved;
    private RichCommandButton cntrlReviewed;
    private RichPanelGroupLayout cntrlConfirmDetailsPanel;
    private RichToolbar cntrlConfirmToolbar;
    private RichSelectOneChoice cntrlScope;

    private RichSelectBooleanCheckbox cntrlIncludeLLTsInExport;
    private RichSelectBooleanCheckbox cntrlExportDisplayedOnly;

    private NMQWizardBean nMQWizardBean;
    private TermHierarchyBean termHierarchyBean;
    private UISelectItems cntrlProductSelectItems;


    public NMQWizardUIBean() {
        nMQWizardBean = (NMQWizardBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
        termHierarchyBean = (TermHierarchyBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
        userBean = nMQWizardBean.getUserBean();
        cSMQBean = nMQWizardBean.getCSMQBean();
    }


    public void setFooterInfo(String termName) {
        nMQWizardBean.setCurrentTermName(termName);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.controlCurrentTermName);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.controlCurrentTermName);
    }


   
    

    /*
     * @author MTW
     * 07/21/2014
     * 
     */
    public void designeeValueChange(ValueChangeEvent valueChangeEvent) {
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.TRUE);
        nMQWizardBean.setDesigneeList((List<String>)valueChangeEvent.getNewValue());
        
    }
    
    public void setControlMQStatus(RichInputText controlMQStatus) {
        this.controlMQStatus = controlMQStatus;
    }

    public RichInputText getControlMQStatus() {
        return controlMQStatus;
    }

    public void setCurrentDictContentID(String currentDictContentID) {
        nMQWizardBean.setCurrentDictContentID(currentDictContentID);
    }

    public String getCurrentDictContentID() {
        return nMQWizardBean.getCurrentDictContentID();
    }

    /*
     * @author MTW
     * 07/02/2014
     */    
//    public void setProductListControl(RichInputComboboxListOfValues productListControl) {
//        this.productListControl = productListControl;
//    }
//
//    public RichInputComboboxListOfValues getProductListControl() {
//        return productListControl;
//    }
    
   

    public void setControlRequestorID(RichInputText controlRequestorID) {
        this.controlRequestorID = controlRequestorID;
    }

    public RichInputText getControlRequestorID() {
        return controlRequestorID;
    }

    public void setControlCreateDate(RichInputText controlCreateDate) {
        this.controlCreateDate = controlCreateDate;
    }

    public RichInputText getControlCreateDate() {
        return controlCreateDate;
    }

   

    public void setControlMQLevel(RichSelectOneChoice controlMQLevel) {
        this.controlMQLevel = controlMQLevel;
    }

    public RichSelectOneChoice getControlMQLevel() {
        return controlMQLevel;
    }

   


    public void setControlReleaseGroup(RichSelectOneChoice controlReleaseGroup) {
        this.controlReleaseGroup = controlReleaseGroup;
    }

    public RichSelectOneChoice getControlReleaseGroup() {
        return controlReleaseGroup;
    }

    public void setControlMQState(RichInputText controlMQState) {
        this.controlMQState = controlMQState;
    }

    public RichInputText getControlMQState() {
        return controlMQState;
    }

    public void setControlMQCode(RichInputText controlMQCode) {
        this.controlMQCode = controlMQCode;
    }

    public RichInputText getControlMQCode() {
        return controlMQCode;
    }

    public void setControlDictionaryVersion(RichInputText controlDictionaryVersion) {
        this.controlDictionaryVersion = controlDictionaryVersion;
    }

    public RichInputText getControlDictionaryVersion() {
        return controlDictionaryVersion;
    }

    public void setControlMqAlgorithm(RichInputText controlMqAlgorithm) {
        this.controlMqAlgorithm = controlMqAlgorithm;
    }

    public RichInputText getControlMqAlgorithm() {
        return controlMqAlgorithm;
    }
    
    
   

    public void setControlInfNoteMQDescription(RichInputText controlInfNoteMQDescription) {
        this.controlInfNoteMQDescription = controlInfNoteMQDescription;
    }

    public RichInputText getControlInfNoteMQDescription() {
        return controlInfNoteMQDescription;
    }


    public void setControlInfNoteMQNotes(RichInputText controlInfNoteMQNotes) {
        this.controlInfNoteMQNotes = controlInfNoteMQNotes;
    }

    public RichInputText getControlInfNoteMQNotes() {
        return controlInfNoteMQNotes;
    }

    public void setControlMQScope(RichSelectOneChoice controlMQScope) {
        this.controlMQScope = controlMQScope;
    }

    public RichSelectOneChoice getControlMQScope() {
        return controlMQScope;
    }
    
    public String yesWarningDetails() {
        Boolean fromTrain = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.detailsFromTrain}");
        if(fromTrain != null && fromTrain){
            saveDetails();
            nMQWizardBean.queueDetailAction();
            return null;
        }
        else{
            saveDetails();
            return "CANCEL";
        }
    }


    public boolean saveDetails() {
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.FALSE);
        ADFUtils.setEL("#{pageFlowScope.detailsFromTrain}", Boolean.FALSE);
        setCurrentDetailValuesFromUI();
        this.controlMQLevel.setDisabled(true);
        boolean retVal = nMQWizardBean.saveDetails();
        // update effected controls
        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(cntrlTrainButtons);
        adfFacesContext.partialUpdateNotify(cntrlTrainButtons);
        adfFacesContext.addPartialTarget(cntrlTrain);
        adfFacesContext.partialUpdateNotify(cntrlTrain);
        adfFacesContext.addPartialTarget(controlMQName);
        adfFacesContext.partialUpdateNotify(controlMQName);
        adfFacesContext.addPartialTarget(controlNMQCode);
        adfFacesContext.partialUpdateNotify(controlNMQCode);
        adfFacesContext.addPartialTarget(controlMQLevel);
        adfFacesContext.partialUpdateNotify(controlMQLevel);
        adfFacesContext.addPartialTarget(controlMQStatus);
        adfFacesContext.partialUpdateNotify(controlMQStatus);

        return retVal;
    }


    private void setCurrentDetailValuesFromUI() {

        nMQWizardBean.setCurrentFilterDictionaryShortName(String.valueOf(ctrlActiveDictionary.getValue()));
        nMQWizardBean.setCurrentPredictGroups(String.valueOf(controlReleaseGroup.getValue()));
        nMQWizardBean.setCurrentTermName(String.valueOf(controlMQName.getValue()));
        nMQWizardBean.setCurrentTermLevel(String.valueOf(controlMQLevel.getValue()));
        nMQWizardBean.setCurrentMQALGO(String.valueOf(controlMqAlgorithm.getValue()));
        nMQWizardBean.setCurrentMQCRTEV(String.valueOf(nMQWizardBean.getControlCriticalEvent().getValue()));
        nMQWizardBean.setCurrentScope(String.valueOf(controlMQScope.getValue()));


        Object selProd = nMQWizardBean.getProductListControl().getValue();
        if (selProd instanceof java.lang.String) {
            String temp = selProd.toString();
            temp.replace("[", "");
            temp.replace("]", "");
            nMQWizardBean.setCurrentProduct(temp);
        } else {

            List selected = (List)nMQWizardBean.getProductListControl().getValue();
            if (selected != null) {
                String temp = "";
                for (Object s : selected)
                    temp = temp + s + ",";

                temp.replace("[", "");
                temp.replace("]", "");

                if (temp != null & temp.length() > 0)
                    nMQWizardBean.setCurrentProduct(temp.substring(0, temp.length() - 1));
            }
        }

        List selected = (List)nMQWizardBean.getControlMQGroup().getValue();
        if (selected != null) {
            String temp = "";
            for (Object s : selected)
                temp = temp + s + CSMQBean.DEFAULT_DELIMETER_CHAR;

            temp.replace("[", "");
            temp.replace("]", "");
            if (temp != null & temp.length() > 0)
                nMQWizardBean.setCurrentMQGROUP(temp.substring(0, temp.length() - 1));
        }

    }

    public void setCurrentInfNoteslValuesFromUI(ActionEvent actionEvent) {
        nMQWizardBean.setCurrentInfNoteDescription(String.valueOf(controlInfNoteMQDescription.getValue()));
        nMQWizardBean.setCurrentInfNoteSource(String.valueOf(controlInfNoteMQSource.getValue()));
        nMQWizardBean.setCurrentInfNoteNotes(String.valueOf(controlInfNoteMQNotes.getValue()));
    }


    public void getDictionaryInfo() {

        nMQWizardBean.getDictionaryInfo();

        AdfFacesContext.getCurrentInstance().addPartialTarget(this.controlCurrentTermName);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.controlCurrentTermName);
    }


    public void clearRelations() {
        nMQWizardBean.clearRelations();

    }


    public String updateRelations() {
        
    System.out.println("************* REFRESHING RELATIONS ****************");
        
        nMQWizardBean.updateRelations();

        

        boolean scope = false;
        if (nMQWizardBean.getCurrentScope() != null)
            scope = nMQWizardBean.getCurrentScope().equals(CSMQBean.HAS_SCOPE);
        termHierarchyBean.init(scope);
        if (termHierarchyBean.getTargetTree() != null) {
            AdfFacesContext.getCurrentInstance().addPartialTarget(termHierarchyBean.getTargetTree());
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(termHierarchyBean.getTargetTree());
        }
        return null;
    }


    public void MQDescriptionChanged(ValueChangeEvent valueChangeEvent) {
        controlInfNoteMQDescription.setChanged(true);
    }

    public void dictionaryChanaged(ValueChangeEvent valueChangeEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " dictionaryChanaged:" + valueChangeEvent);
    }

    public void releaseGroupChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() == null)
            return;
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.TRUE);
        CSMQBean.logger.info(userBean.getCaller() + " releaseGroupChanged:" + valueChangeEvent);
        nMQWizardBean.setCurrentPredictGroups(String.valueOf(controlReleaseGroup.getValue()));
    }

    public void nmqGroupChanged(ValueChangeEvent valueChangeEvent) {
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.TRUE);
        CSMQBean.logger.info(userBean.getCaller() + " nmqGroupChanged:" + valueChangeEvent);
        nMQWizardBean.setCurrentMQGROUP(String.valueOf(nMQWizardBean.getControlMQGroup().getValue()));
    }

    public void setControlMQName(RichInputText controlMQName) {
        this.controlMQName = controlMQName;
    }

    public RichInputText getControlMQName() {
        return controlMQName;
    }

    public void setControlInfNoteMQSource(RichInputText controlInfNoteMQSource) {
        this.controlInfNoteMQSource = controlInfNoteMQSource;
    }

    public RichInputText getControlInfNoteMQSource() {
        return controlInfNoteMQSource;
    }


    public void setControlNMQCode(RichInputText controlNMQCode) {
        this.controlNMQCode = controlNMQCode;
    }

    public RichInputText getControlNMQCode() {
        return controlNMQCode;
    }


    public void setControlWizardStatus(RichOutputFormatted controlWizardStatus) {
        this.controlWizardStatus = controlWizardStatus;
    }

    public RichOutputFormatted getControlWizardStatus() {
        return controlWizardStatus;
    }


    public void saveDetailsEvent(ActionEvent actionEvent) {
        this.saveDetails();
    }

    public void setCtrlActiveDictionary(RichInputText controlCurrentBaseDictionary) {
        this.ctrlActiveDictionary = controlCurrentBaseDictionary;
    }

    public RichInputText getCtrlActiveDictionary() {
        return ctrlActiveDictionary;
    }

    public void setControlCurrentTermName(RichInputText controlCurrentTermName) {
        this.controlCurrentTermName = controlCurrentTermName;
    }

    public RichInputText getControlCurrentTermName() {
        return controlCurrentTermName;
    }


    public void setControlRequestor(RichInputText controlRequestor) {
        this.controlRequestor = controlRequestor;
    }

    public RichInputText getControlRequestor() {
        return controlRequestor;
    }

    public void setCntrlTrain(RichTrain cntrlTrain) {
        this.cntrlTrain = cntrlTrain;
    }

    public RichTrain getCntrlTrain() {
        return cntrlTrain;
    }


    public void setCntrlTrainButtons(RichTrainButtonBar cntrlTrainButtons) {
        this.cntrlTrainButtons = cntrlTrainButtons;
    }

    public RichTrainButtonBar getCntrlTrainButtons() {
        return cntrlTrainButtons;
    }

    public void reactivate(DialogEvent dialogEvent) {
        if (NMQUtils.activate(nMQWizardBean.getCurrentDictContentID(), nMQWizardBean.getCurrentContentCode(),
                              userBean.getUserRole(), userBean.getCurrentUser())) {
            nMQWizardBean.setCurrentMQStatus(CSMQBean.ACTIVE_ACTIVITY_STATUS);
            nMQWizardBean.setCurrentStatus(CSMQBean.PENDING_RELEASE_STATUS);
            nMQWizardBean.setCurrentState(CSMQBean.STATE_DRAFT);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmToolbar);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmToolbar);
        }
    }

    public void retire(DialogEvent dialogEvent) {
        if (NMQUtils.retire(nMQWizardBean.getCurrentDictContentID(), nMQWizardBean.getCurrentContentCode(),
                            userBean.getUserRole(), userBean.getCurrentUser())) {
            nMQWizardBean.setCurrentMQStatus(CSMQBean.INACTIVE_ACTIVITY_STATUS);
            nMQWizardBean.setCurrentStatus(CSMQBean.PENDING_RELEASE_STATUS);
            nMQWizardBean.setCurrentState(CSMQBean.STATE_DRAFT);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmToolbar);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmToolbar);
        }
    }

    public void delete(DialogEvent dialogEvent) {
        if (NMQUtils.delete(nMQWizardBean.getCurrentDictContentID(), nMQWizardBean.getCurrentPredictGroups())) {
            nMQWizardBean.setActionDelete(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmToolbar);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmToolbar);
        }
    }

    public void demoteToDraft(DialogEvent dialogEvent) {
        Hashtable result =
            NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_DRAFT, userBean.getCurrentUser(),
                                 userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null,
                                 cSMQBean.getDefaultDraftReleaseGroup());
        if (result != null) {
            nMQWizardBean.setCurrentPredictGroups(cSMQBean.getDefaultDraftReleaseGroup());
            nMQWizardBean.setCurrentState((String)result.get("STATE"));
            nMQWizardBean.setCurrentReasonForApproval((String)result.get("REASON"));
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmToolbar);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmToolbar);
        }
    }

    public void confirmReviewed(DialogEvent dialogEvent) {
        Hashtable result =
            NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_REVIEWED, userBean.getCurrentUser(),
                                 userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null,
                                 cSMQBean.getDefaultDraftReleaseGroup());
        if (result != null) {
            nMQWizardBean.setCurrentPredictGroups(cSMQBean.getDefaultDraftReleaseGroup());
            nMQWizardBean.setCurrentState((String)result.get("STATE"));
            nMQWizardBean.setCurrentReasonForApproval((String)result.get("REASON"));
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmToolbar);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmToolbar);
        }
    }

    public void approve(DialogEvent dialogEvent) {
        Hashtable result =
            NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_APPROVED, userBean.getCurrentUser(),
                                 userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(), null,
                                 cSMQBean.getDefaultDraftReleaseGroup());
        if (result != null) {
            nMQWizardBean.setCurrentPredictGroups(cSMQBean.getDefaultDraftReleaseGroup());
            nMQWizardBean.setCurrentState((String)result.get("STATE"));
            nMQWizardBean.setCurrentReasonForApproval((String)result.get("REASON"));
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmToolbar);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmToolbar);
        }
    }

    public void promoteToRequested(DialogEvent dialogEvent) {
        if (nMQWizardBean.getCurrentReasonForRequest() == null ||
            nMQWizardBean.getCurrentReasonForRequest().length() < 1) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to submit to MQM", "A request reason is required.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        Hashtable result =
            NMQUtils.changeState(nMQWizardBean.getCurrentDictContentID(), CSMQBean.STATE_REQUESTED, userBean.getCurrentUser(),
                                 userBean.getUserRole(), nMQWizardBean.getCurrentRequestedByDate(),
                                 nMQWizardBean.getCurrentReasonForRequest(), cSMQBean.getDefaultDraftReleaseGroup());
        if (result != null) {
            nMQWizardBean.setCurrentPredictGroups(cSMQBean.getDefaultDraftReleaseGroup());
            nMQWizardBean.setCurrentState((String)result.get("STATE"));
            nMQWizardBean.setCurrentReasonForApproval((String)result.get("REASON"));
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmDetailsPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlConfirmToolbar);
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlConfirmToolbar);
        }
    }


    

    public void setCntrlReasonForRequest(RichInputText cntrlReasonForRequest) {
        this.cntrlReasonForRequest = cntrlReasonForRequest;
    }

    public RichInputText getCntrlReasonForRequest() {
        return cntrlReasonForRequest;
    }

     public void setCurrentDateRequested(oracle.jbo.domain.Date currentDateRequested) {
        this.currentDateRequested = currentDateRequested;
    }

    public oracle.jbo.domain.Date getCurrentDateRequested() {
        return currentDateRequested;
    } 

    public void setCntrlDateRequested(RichInputDate cntrlDateRequested) {
        this.cntrlDateRequested = cntrlDateRequested;
    }

    public RichInputDate getCntrlDateRequested() {
        return cntrlDateRequested;
    }


     public void setCurrentRequestedByDate(oracle.jbo.domain.Date currentRequestedByDate) {
        this.currentRequestedByDate = currentRequestedByDate;
    }

    public oracle.jbo.domain.Date getCurrentRequestedByDate() {
        return currentRequestedByDate;
    } 

    public void setCntrlApproved(RichCommandButton cntrlApproved) {
        this.cntrlApproved = cntrlApproved;
    }

    public RichCommandButton getCntrlApproved() {
        return cntrlApproved;
    }

    public void setCntrlReviewed(RichCommandButton cntrlReviewed) {
        this.cntrlReviewed = cntrlReviewed;
    }

    public RichCommandButton getCntrlReviewed() {
        return cntrlReviewed;
    }


    public void setCntrlConfirmDetailsPanel(RichPanelGroupLayout cntrlConfirmDetailsPanel) {
        this.cntrlConfirmDetailsPanel = cntrlConfirmDetailsPanel;
    }

    public RichPanelGroupLayout getCntrlConfirmDetailsPanel() {
        return cntrlConfirmDetailsPanel;
    }

    public void setCntrlConfirmToolbar(RichToolbar cntrlConfirmToolbar) {
        this.cntrlConfirmToolbar = cntrlConfirmToolbar;
    }

    public RichToolbar getCntrlConfirmToolbar() {
        return cntrlConfirmToolbar;
    }

    public void hierarchyScopeChanged(ValueChangeEvent valueChangeEvent) {
        updateRelations();
    }

    public void setCntrlScope(RichSelectOneChoice cntrlScope) {
        this.cntrlScope = cntrlScope;
    }

    public RichSelectOneChoice getCntrlScope() {
        return cntrlScope;
    }


    public void sortChanged(ValueChangeEvent valueChangeEvent) {
        nMQWizardBean.setHierarchyParamSort(valueChangeEvent.getNewValue().toString());
        updateRelations();
    }

    public void showSecondaryPathChanged(ValueChangeEvent valueChangeEvent) {
        nMQWizardBean.setParamPrimLinkFlag((Boolean)valueChangeEvent.getNewValue() ? CSMQBean.TRUE : CSMQBean.FALSE);
        updateRelations();
    }

    public void scopeChanged(ValueChangeEvent valueChangeEvent) {
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.TRUE);
        String newVal = valueChangeEvent.getNewValue().toString();
        //nMQWizardBean.setHierarchyParamScope(newVal);
        nMQWizardBean.setCurrentScope(newVal.toString());
        if (nMQWizardBean.getMode() != CSMQBean.MODE_IMPACT_ASSESSMENT){
            termHierarchyBean.setHasScope(newVal.equals(CSMQBean.HAS_SCOPE));
            updateRelations();
        } else {
            CSMQBean.logger.info(userBean.getCaller() + " Scope Changed:" + newVal);
            CSMQBean.logger.info("*** REFRESHING IA RELATIONS Begin***");
            ImpactAnalysisBean impactAnalysisBean = (ImpactAnalysisBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("ImpactAnalysisBean");
            impactAnalysisBean.refreshFutureTree();
            CSMQBean.logger.info("*** REFRESHING IA RELATIONS End***");
        }
    }

    public void maxLevelsChanged(ValueChangeEvent valueChangeEvent) {
        nMQWizardBean.setMaxLevels(Integer.parseInt(valueChangeEvent.getNewValue().toString()));
        updateRelations();
    }


    public void refreshHierarchy(ActionEvent actionEvent) {
        updateRelations();
    }

    public void setNMQWizardBean(NMQWizardBean nMQWizardBean) {
        this.nMQWizardBean = nMQWizardBean;
    }

    public NMQWizardBean getNMQWizardBean() {
        return nMQWizardBean;
    }

    public void addRelationsPageLoad(PhaseEvent phaseEvent) {
        if (!nMQWizardBean.isTreeAccessed()) {
            updateRelations();
            nMQWizardBean.setTreeAccessed(true);
        }
        //if (getCntrlIncludeLLTsInExport() != null)
       //     this.getCntrlIncludeLLTsInExport().setValue(false);    
        nMQWizardBean.setIncludeLLTsInExport(false);
        
        
        //if(phaseEvent.getPhaseId().toString().equalsIgnoreCase("RENDER_RESPONSE 6"))
        
    }
    
    
    public void setCntrlIncludeLLTsInExport(RichSelectBooleanCheckbox cntrlIncludeLLTsInExport) {
        this.cntrlIncludeLLTsInExport = cntrlIncludeLLTsInExport;
    }

    public RichSelectBooleanCheckbox getCntrlIncludeLLTsInExport() {
        return cntrlIncludeLLTsInExport;
    }

    public void setCntrlExportDisplayedOnly(RichSelectBooleanCheckbox cntrlExportDisplayedOnly) {
        this.cntrlExportDisplayedOnly = cntrlExportDisplayedOnly;
    }

    public RichSelectBooleanCheckbox getCntrlExportDisplayedOnly() {
        return cntrlExportDisplayedOnly;
    }

    public void addRelationsPageBeforeLoad(PhaseEvent phaseEvent) {
        nMQWizardBean.getDictionaryInfo();// Add event code here...
    }

    public void requestedByDateChanged(ValueChangeEvent valueChangeEvent) {
        Date reqDate = (Date) valueChangeEvent.getNewValue();
        nMQWizardBean.setCurrentDateRequested(reqDate);
        // Add event code here...
    }

    public void extensionChanged(ValueChangeEvent valueChangeEvent) {
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.TRUE);
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        String newExt = valueChangeEvent.getNewValue().toString();
        nMQWizardBean.setIsNMQ(newExt.equals("NMQ"));
        nMQWizardBean.setIsSMQ(newExt.equals("SMQ"));
   
   
        nMQWizardBean.setCurrentMQType(newExt);       
        //nMQWizardBean.loadProductSelectList();
        
        if (nMQWizardBean.getCurrentExtension().equals(CSMQBean.NMQ) || nMQWizardBean.getCurrentExtension().equals(CSMQBean.SMQ_NAME))
            controlMQLevel.setReadOnly(false);
        else
            controlMQLevel.setReadOnly(true);
        
        
     //   if (!nMQWizardBean.isIsNMQ() && (userBean.isMQM() || userBean.isRequestor())) nMQWizardBean.setCurrentState(CSMQBean.STATE_DRAFT);
      //  if (nMQWizardBean.isIsNMQ()  && userBean.isRequestor()) nMQWizardBean.setCurrentState(CSMQBean.STATE_PROPOSED);
        
        if (nMQWizardBean.isIsNMQ() && userBean.isRequestor() && !(userBean.isMQM() || userBean.isAdmin())){
            nMQWizardBean.setCurrentState(CSMQBean.STATE_PROPOSED);
        } else {
            nMQWizardBean.setCurrentState(CSMQBean.STATE_DRAFT);
        }
        if (nMQWizardBean.getCurrentExtension().equals(CSMQBean.SMQ_NAME)){
            nMQWizardBean.setRenderProduct(Boolean.FALSE);
            nMQWizardBean.getProductList().clear();
            nMQWizardBean.getProductListControl().resetValue();
        } else {
            nMQWizardBean.setRenderProduct(Boolean.TRUE);
        }
		
        // FIX: 20141121-2
        if (nMQWizardBean.isIsNMQ()) {
            nMQWizardBean.getProductList().clear();
            nMQWizardBean.getProductList().add(CSMQBean.DEFAULT_PRODUCT);
        }
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(controlMQState);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlMQState);

        AdfFacesContext.getCurrentInstance().addPartialTarget(nMQWizardBean.getControlDesignee());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(nMQWizardBean.getControlDesignee());
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(nMQWizardBean.getProductListControl());
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(nMQWizardBean.getProductListControl());
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(controlMQLevel);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlMQLevel);
        
        if (userBean.isUserInRole("Requestor")
            && !(nMQWizardBean.getCurrentExtension().equals(CSMQBean.NMQ) || nMQWizardBean.getCurrentExtension().equals(CSMQBean.SMQ_NAME))){
            controlMqAlgorithm.setReadOnly(true);
        } else {
            controlMqAlgorithm.setReadOnly(false);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(controlMqAlgorithm);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(controlMqAlgorithm);
    }

    public void addDetailsPageLoad(PhaseEvent phaseEvent) {
        nMQWizardBean.loadProductSelectList();
    }

    public void setCntrlProductSelectItems(UISelectItems cntrlProductSelectItems) {
        this.cntrlProductSelectItems = cntrlProductSelectItems;
    }

    public UISelectItems getCntrlProductSelectItems() {
        return cntrlProductSelectItems;
    }

    public void detailsChanged(ValueChangeEvent valueChangeEvent) {
        ADFUtils.setEL("#{pageFlowScope.isDetailsChanged}", Boolean.TRUE);
    }
}
