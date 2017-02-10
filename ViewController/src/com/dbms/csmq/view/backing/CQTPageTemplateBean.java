package com.dbms.csmq.view.backing;


import com.dbms.util.ADFUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.binding.OperationBinding;


public class CQTPageTemplateBean {
    public CQTPageTemplateBean() {
        super();
    }

    private RichPopup viewCurrentImpactWarningPopup;

    public String displayCurrentViewVersionImpact() {
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("isUpgradePending");
        if(ob == null){
            return null;
        }
        Boolean isUpgradePending = (Boolean)ob.execute();
        System.out.println("RenderingRulesBean.java displayCurrentViewVersionImpact() isUpgradePending=" +
                           isUpgradePending);
        if (isUpgradePending) {
            return "VIEW_CURRENT_VERSION_IMPACT";
        } else {
            if (getViewCurrentImpactWarningPopup() != null) {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                this.getViewCurrentImpactWarningPopup().show(hints);
            } else {
                FacesMessage msg =
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "View Current Version Impact page access",
                                     "The View Version Impact page is only available while MedDRA Versioning data is pending.  Please use the View Previous Version Impact page instead.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            return null;
        }
    }

    public void setViewCurrentImpactWarningPopup(RichPopup viewCurrentImpactWarningPopup) {
        this.viewCurrentImpactWarningPopup = viewCurrentImpactWarningPopup;
    }

    public RichPopup getViewCurrentImpactWarningPopup() {
        return viewCurrentImpactWarningPopup;
    }
}
