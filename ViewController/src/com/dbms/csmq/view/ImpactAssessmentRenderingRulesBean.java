package com.dbms.csmq.view;

import com.dbms.csmq.view.impact.FutureImpactHierarchyBean;

import oracle.adf.view.rich.context.AdfFacesContext;

public class ImpactAssessmentRenderingRulesBean extends RenderingRulesBean {
    
    FutureImpactHierarchyBean futureImpactHierarchyBean = (FutureImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("FutureImpactHierarchyBean");
    private boolean wizardRelationsDisableDetails = false;
       
    
    public ImpactAssessmentRenderingRulesBean() {
        super();
    }

    public void setWizardRelationsDisableDetails(boolean wizardRelationsDisableDetails) {
        this.wizardRelationsDisableDetails = wizardRelationsDisableDetails;
    }

    public boolean isWizardRelationsDisableDetails() {
        if (!futureImpactHierarchyBean.isHasScope() || !futureImpactHierarchyBean.isEditable()) wizardRelationsDisableDetails = true;
        return wizardRelationsDisableDetails;
    }
}
