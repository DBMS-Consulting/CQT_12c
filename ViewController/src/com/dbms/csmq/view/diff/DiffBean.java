package com.dbms.csmq.view.diff;

import com.dbms.csmq.CSMQBean;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;

import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.model.RowKeySet;


public class DiffBean {
    
    private String leftTermDictContentID;
    private String rightTermDictContentID;
    private String leftTermName;
    private String rightTermName;

    public void LeftTermChanged(ValueChangeEvent valueChangeEvent) {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        BindingContext bctx = BindingContext.getCurrent(); 
        BindingContainer bindings = bctx.getCurrentBindingsEntry(); 
        AttributeBinding attributeBinding =  (AttributeBinding) bindings.get("LeftDictContentCode"); 
        leftTermDictContentID = attributeBinding.getInputValue().toString();
        attributeBinding =  (AttributeBinding) bindings.get("LeftTerm"); 
        leftTermName = attributeBinding.getInputValue().toString();
        System.out.println ("leftTermDictContentID: " + leftTermDictContentID);
        System.out.println ("leftTerm: " + leftTermName);
        }

    public void RightTermChanged(ValueChangeEvent valueChangeEvent) {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        BindingContext bctx = BindingContext.getCurrent(); 
        BindingContainer bindings = bctx.getCurrentBindingsEntry(); 
        AttributeBinding attributeBinding =  (AttributeBinding) bindings.get("RightDictContentCode"); 
        rightTermDictContentID = attributeBinding.getInputValue().toString(); 
        attributeBinding =  (AttributeBinding) bindings.get("RightTerm"); 
        rightTermName = attributeBinding.getInputValue().toString();
        System.out.println ("rightTermDictContentID: " + rightTermDictContentID);
        System.out.println ("rightTerm: " + rightTermName);
        }
    
    
    private void doCompare () {
       /* 
        <action IterBinding="MQDiffVO1Iterator" id="ExecuteWithParams" InstanceName="DiffModuleDataControl.MQDiffVO1"
                    DataControl="DiffModuleDataControl" RequiresUpdateModel="true" Action="executeWithParams">
              <NamedData NDName="dictionary" NDValue="x" NDType="java.lang.String"/>
              <NamedData NDName="leftMQ" NDValue="y" NDType="java.lang.String"/>
              <NamedData NDName="rightMQ" NDValue="z" NDType="java.lang.String"/>
            </action>
        */
        
        CSMQBean cSMQBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
        DiffUIBean diffUIBean = (DiffUIBean)ADFContext.getCurrent().getRequestScope().get("DiffUIBean");
        
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("MQDiffVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        
        // EXECUTE BLANK QUERY  
        
        vo.setNamedWhereClauseParam("dictionary", cSMQBean.getDefaultFilterDictionaryShortName());
        vo.setNamedWhereClauseParam("leftMQ", 0);
        vo.setNamedWhereClauseParam("rightMQ", 0);
        vo.executeQuery();
        
        diffUIBean.getCntrlDiffResults().setEmptyText("No data to display.");
        AdfFacesContext.getCurrentInstance().addPartialTarget(diffUIBean.getCntrlDiffResults() );
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(diffUIBean.getCntrlDiffResults() );
        //clear the selected row
        RowKeySet rks= diffUIBean.getCntrlDiffResults() .getSelectedRowKeys();
        rks.clear();
        
        vo.setNamedWhereClauseParam("dictionary", cSMQBean.getDefaultFilterDictionaryShortName());
        vo.setNamedWhereClauseParam("leftMQ", leftTermDictContentID);
        vo.setNamedWhereClauseParam("rightMQ", rightTermDictContentID);
        
        vo.executeQuery();
        
        if (diffUIBean.getCntrlDiffResults() != null) {  
            
            diffUIBean.getCntrlDiffResults().setEmptyText("No data to display.");
            AdfFacesContext.getCurrentInstance().addPartialTarget(diffUIBean.getCntrlDiffResults() );
            AdfFacesContext.getCurrentInstance().partialUpdateNotify(diffUIBean.getCntrlDiffResults() );
            //clear the selected row
            rks= diffUIBean.getCntrlDiffResults() .getSelectedRowKeys();
            rks.clear();
        }
        
        
    }
    
    
    public void setLeftTermDictContentID(String leftTermDictContentID) {
        this.leftTermDictContentID = leftTermDictContentID;
    }

    public String getLeftTermDictContentID() {
        return leftTermDictContentID;
    }

    public void setRightTermDictContentID(String rightTermDictContentID) {
        this.rightTermDictContentID = rightTermDictContentID;
    }

    public String getRightTermDictContentID() {
        return rightTermDictContentID;
    }

    public void setLeftTermName(String leftTermName) {
        this.leftTermName = leftTermName;
    }

    public String getLeftTermName() {
        return leftTermName;
    }

    public void setRightTermName(String rightTermName) {
        this.rightTermName = rightTermName;
    }

    public String getRightTermName() {
        return rightTermName;
    }

    public void doCompare(ActionEvent actionEvent) {
        this.doCompare();
    }
}
