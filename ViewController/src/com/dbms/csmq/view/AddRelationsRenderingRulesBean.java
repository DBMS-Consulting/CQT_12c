package com.dbms.csmq.view;

import com.dbms.csmq.view.backing.NMQ.NMQWizardBean;

import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.hierarchy.TermHierarchyBean;

import oracle.adf.view.rich.context.AdfFacesContext;

public class AddRelationsRenderingRulesBean extends RenderingRulesBean {
    
    TermHierarchyBean termHierarchyBean = (TermHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
    private boolean wizardRelationsDisableDetails = false;
    
    
    
    
    
    
    public AddRelationsRenderingRulesBean() {
        super();
    }

    public void setWizardRelationsDisableDetails(boolean wizardRelationsDisableDetails) {
        this.wizardRelationsDisableDetails = wizardRelationsDisableDetails;
    }

    public boolean isWizardRelationsDisableDetails() {
        if (!termHierarchyBean.isHasScope() || !super.isWizardRelationsRenderSave()) wizardRelationsDisableDetails = true;
        return wizardRelationsDisableDetails;
    }
}
