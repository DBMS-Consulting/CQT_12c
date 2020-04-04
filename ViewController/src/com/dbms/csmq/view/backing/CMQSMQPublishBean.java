package com.dbms.csmq.view.backing;

import com.dbms.csmq.view.backing.NMQ.NMQUtils;

import com.dbms.csmq.view.backing.NMQ.NMQWizardBean;
import com.dbms.csmq.view.backing.publish.WorkflowBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.context.AdfFacesContext;

public class CMQSMQPublishBean {
    private RichPopup publishConfirmPopup;
    private RichPopup publishWarningPopup;
    private RichPopup approveConfirmPopup;
    private RichPopup approveWarningPopup;

    public CMQSMQPublishBean() {
        super();
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

    public void publishAction(ActionEvent actionEvent) {
        WorkflowBean workflowBean = (WorkflowBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("WorkflowBean");
        String terms = "";
        if(workflowBean.getList() != null){
        for (int i = 0; i < workflowBean.getList().size(); i++)
            terms += workflowBean.getList().get(i) + ",";
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select at least one term.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return; 
        }
        if (terms.length() < 1) {    
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select at least one term.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
            }
        boolean hasRelationsFlag = true;
        for (int i = 0; i < workflowBean.getList().size(); i++){
           // System.out.println("----workflowBean.getList().get(i).toString()----");
            String hasRelations = NMQUtils.checkMqHasRelations(workflowBean.getList().get(i).toString());
            //String hasRelations = NMQUtils.checkMqHasRelations("913717791");
             // String hasRelations = "NO_RELATION";
             if("NO_RELATION".equalsIgnoreCase(hasRelations)){
                 hasRelationsFlag = false;
                 break;
             }
        }
        
        if(!hasRelationsFlag){
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            this.getPublishWarningPopup().show(hints);
        }else{
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            this.getPublishConfirmPopup().show(hints);
        }
    }
    
    public void cmqPublishAction(ActionEvent actionEvent) {
          NMQWizardBean nMQWizardBean = (NMQWizardBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
          String hasRelations = NMQUtils.checkMqHasRelations(nMQWizardBean.getCurrentDictContentID());
       // String hasRelations = "NO_RELATION";
      if("NO_RELATION".equalsIgnoreCase(hasRelations)){
          RichPopup.PopupHints hints = new RichPopup.PopupHints();
          this.getPublishWarningPopup().show(hints);
      }else{
          RichPopup.PopupHints hints = new RichPopup.PopupHints();
          this.getPublishConfirmPopup().show(hints);
      }
    }

    public void approveAction(ActionEvent actionEvent) {
        NMQWizardBean nMQWizardBean = (NMQWizardBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQWizardBean");
        String hasRelations = NMQUtils.checkMqHasRelations(nMQWizardBean.getCurrentDictContentID());
        // String hasRelations = "NO_RELATION";
        if("NO_RELATION".equalsIgnoreCase(hasRelations)){
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        this.getApproveWarningPopup().show(hints);
        }else{
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        this.getApproveConfirmPopup().show(hints);
        }
    }

    public void setApproveConfirmPopup(RichPopup approveConfirmPopup) {
        this.approveConfirmPopup = approveConfirmPopup;
    }

    public RichPopup getApproveConfirmPopup() {
        return approveConfirmPopup;
    }

    public void setApproveWarningPopup(RichPopup approveWarningPopup) {
        this.approveWarningPopup = approveWarningPopup;
    }

    public RichPopup getApproveWarningPopup() {
        return approveWarningPopup;
    }
}
