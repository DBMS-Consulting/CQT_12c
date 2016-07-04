package com.dbms.csmq.view.backing.search;

import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.hierarchy.NewPTListBean;
import com.dbms.csmq.view.hierarchy.TermHierarchySourceBean;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;

public class PreferedTermSearchBean {
    private RichSelectOneChoice cntlSOCList;
    private String parentSOCcontentID;
    CSMQBean applicationBean;
    private RichTreeTable ctrlResultTree;
    private String activationGroups;
    UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
    

    public PreferedTermSearchBean() {
        super();
        applicationBean = (CSMQBean)ADFContext.getCurrent().getSessionScope().get("CSMQBean");
        activationGroups = applicationBean.getDefaultDraftReleaseGroup() + "," + applicationBean.getDefaultMedDRAReleaseGroup();
    }

    public void setCntlSOCList(RichSelectOneChoice cntlSOCList) {
        this.cntlSOCList = cntlSOCList;
    }

    public RichSelectOneChoice getCntlSOCList() {
        return cntlSOCList;
    }

    public void setParentSOCcontentID(String parentSOCcontentID) {
        this.parentSOCcontentID = parentSOCcontentID;
    }

    public String getParentSOCcontentID() {
        
        this.parentSOCcontentID = cntlSOCList.getValue().toString();
        return parentSOCcontentID;
    }

    public void SOCChanged(ValueChangeEvent valueChangeEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " CREATING NEW PT TREE");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("NewPTsVO1Iterator");
        ViewObject newPTsVO1Iterator = dciterb.getViewObject();
        
   
        
        newPTsVO1Iterator.setNamedWhereClauseParam("dictContentID", getParentSOCcontentID());
        newPTsVO1Iterator.setNamedWhereClauseParam("activationGroups", activationGroups);        
        newPTsVO1Iterator.executeQuery();
        
        this.ctrlResultTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.ctrlResultTree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.ctrlResultTree);
        
        NewPTListBean newPTListBean = (NewPTListBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NewPTListBean");
        newPTListBean.init();

        CSMQBean.logger.info(userBean.getCaller() + " DONE CREATING NEW  PT TREE");
    }

    public void setCtrlResultTree(RichTreeTable ctrlResultTree) {
        this.ctrlResultTree = ctrlResultTree;
    }

    public RichTreeTable getCtrlResultTree() {
        return ctrlResultTree;
    }

    public void setActivationGroups(String activationGroups) {
        this.activationGroups = activationGroups;
    }

    public String getActivationGroups() {
        return activationGroups;
    }

    public void SOCChanged(AttributeChangeEvent attributeChangeEvent) {
        // Add event code here...
    }

    public void resultRowChanged(SelectionEvent selectionEvent) {
        
        }

    

}



