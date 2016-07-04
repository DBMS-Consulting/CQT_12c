package com.dbms.csmq.view.backing.impact;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.impact.CurrentImpactHierarchyBean;
import com.dbms.csmq.view.impact.HistoricalImpactHierarchyBean;
import com.dbms.util.Utils;
import com.dbms.util.dml.DMLUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import java.util.Iterator;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.datatransfer.DataFlavor;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DropEvent;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetTreeImpl;


public class HistoricalImpactAnalysisBean {
    
    private String showImpact;
    private String defaultMedDRAGroupName = CSMQBean.getProperty("DEFAULT_MEDDRA_RELEASE_GROUP");
    private String defaultDraftGroupName = CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP");
    private String mqType;
    
    HistoricalImpactHierarchyBean historicalImpactHierarchyBean = (HistoricalImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("HistoricalImpactHierarchyBean");
    CurrentImpactHierarchyBean currentImpactHierarchyBean = (CurrentImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("CurrentImpactHierarchyBean");
    UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");   
    //FutureImpactHierarchyBean futureImpactHierarchyBean = (FutureImpactHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("FutureImpactHierarchyBean");
    
    private DCBindingContainer binding;
    private String currentDictId;
    private RichPopup searchPopUp;
    private String paramReleaseGroup;
    private String asOfDate = CSMQBean.getProperty("END_TS_CONSTANT");
    
    private RichSelectOneChoice ctrlImpact;
    private RichTreeTable historicalTree;
    private RichTreeTable futureTree;
    private RichTreeTable sourceTree;

    public static final int PARAM_LENGTH = 7;
    public static final int VAL_PREDICT_GROUP_ID = 0;
    public static final int VAL_DICT_CONTENT_ID = 1;   
    public static final int VAL_TERMSCP = 2;
    public static final int VAL_TERMCAT = 3;
    public static final int VAL_TERMLVL = 4;
    public static final int VAL_TERMWEIG = 5;
    public static final int VAL_DML = 6;


    public HistoricalImpactAnalysisBean() {
        super();
        BindingContext bc = BindingContext.getCurrent();
        binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        }

    public void setCtrlImpact(RichSelectOneChoice ctrlImpact) {
        this.ctrlImpact = ctrlImpact;
    }

    public RichSelectOneChoice getCtrlImpact() {
        return ctrlImpact;
    }

    public void ctrlImpactChanged(ValueChangeEvent valueChangeEvent) {
        
        String assessmentType = ctrlImpact.getValue().toString();
        if (assessmentType.equals("INMQ")){
            this.showImpact = "Y";
            this.mqType = "NMQ";
            }
        else if (assessmentType.equals("NINMQ")){
            this.showImpact = "N";
            this.mqType = "NMQ";
            }
        else if (assessmentType.equals("ISMQ")){
            this.showImpact = "Y";
            this.mqType = "SMQ";
            }
        else if (assessmentType.equals("NISMQ")){
            this.showImpact = "N";
            this.mqType = "SMQ";
            }
        }

    public String getShowImpact() {
        return showImpact;
    }

    public String getDefaultMedDRAGroupName() {
        return defaultMedDRAGroupName;
    }

    public String getMqType() {
        return mqType;
    }

    public void setShowImpact(String showImpact) {
        this.showImpact = showImpact;
    }

    public void setDefaultMedDRAGroupName(String groupName) {
        this.defaultMedDRAGroupName = groupName;
    }

    public void setMqType(String mQLevel) {
        this.mqType = mQLevel;
    }

    public void rowSelected(SelectionEvent selectionEvent) {
        CSMQBean.logger.info(userBean.getCaller() + " ***** ROW CHANGE ****");
        
        resolveMethodExpression("#{bindings.ImpactSearchListVO1.collectionModel.makeCurrent}", null, new Class[] { SelectionEvent.class }, new Object[] {selectionEvent});
        RichTable object = (RichTable)selectionEvent.getSource();
        Row row = null;
        for (Object facesRowKey : object.getSelectedRowKeys()) {
            object.setRowKey(facesRowKey);
            Object o = object.getRowData();
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)o;
            row = rowData.getRow();
            }
        
        if (row == null) return;

        this.currentDictId = Utils.getAsString(row, "DictContentId");
        CSMQBean.logger.info(userBean.getCaller() + " currentDictId: " + currentDictId);
        
        this.searchPopUp.cancel();
        
        if (this.historicalTree != null && this.historicalTree.getDisclosedRowKeys()!=null )
            this.historicalTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
                
        if (this.futureTree != null && this.futureTree.getDisclosedRowKeys()!=null )
            this.futureTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        
        refreshTrees();
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.historicalTree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.historicalTree);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(futureTree);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(futureTree);
        
        }
    
    public Object resolveMethodExpression(String expression, Class returnType, Class[] argTypes, Object[] argValues) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        MethodExpression methodExpression = elFactory.createMethodExpression(elContext, expression, returnType, argTypes);
        return methodExpression.invoke(elContext, argValues);
    }
    
    public void refreshTrees () {
        refreshCurrentTree();
        refreshHistoricTree();
        }

    
    public void refreshHistoricTree () {
        CSMQBean.logger.info(userBean.getCaller() + " *** REFRESHING HISTORIC TREE ***");
        DCIteratorBinding medDRAImpactVO1Iterator = (DCIteratorBinding)binding.get("HistoricalImpactVO1Iterator");
        ViewObject medDRATree = medDRAImpactVO1Iterator.getViewObject();
        CSMQBean.logger.info(userBean.getCaller() + " UPDATING: " +  this.defaultMedDRAGroupName);
        medDRATree.setNamedWhereClauseParam("activationGroup", this.defaultMedDRAGroupName);
        medDRATree.setNamedWhereClauseParam("dictContentID", this.currentDictId);
        medDRATree.executeQuery();
        historicalImpactHierarchyBean.init();
        }


    public void refreshCurrentTree () {
        CSMQBean.logger.info(userBean.getCaller() + " *** REFRESHING CURRENT TREE ***");
        DCIteratorBinding draftImpactVO1Iterator = (DCIteratorBinding)binding.get("CurrentImpactVO1Iterator");
        ViewObject draftTree = draftImpactVO1Iterator.getViewObject();
        String bothGroups = this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName;
        CSMQBean.logger.info(userBean.getCaller() + " \nUPDATING: " +  bothGroups); 
        draftTree.setNamedWhereClauseParam("activationGroup", bothGroups);
        draftTree.setNamedWhereClauseParam("dictContentID", this.currentDictId);
        draftTree.executeQuery();   
        currentImpactHierarchyBean.init();
        }




    public void setCurrentDictId(String currentDictId) {
        this.currentDictId = currentDictId;
    }

    public String getCurrentDictId() {
        return currentDictId;
    }

    public void setSearchPopUp(RichPopup searchPopUp) {
        this.searchPopUp = searchPopUp;
    }

    public RichPopup getSearchPopUp() {
        return searchPopUp;
    }

    public void setParamReleaseGroup(String paramReleaseGroup) {
        this.paramReleaseGroup = paramReleaseGroup;
    }

    public String getParamReleaseGroup() {
        return paramReleaseGroup;
    }

    public void setDefaultDraftGroupName(String defaultDraftGroupName) {
        this.defaultDraftGroupName = defaultDraftGroupName;
    }

    public String getDefaultDraftGroupName() {
        return defaultDraftGroupName;
    }

    public void setHistoricalTree(RichTreeTable medDRATree) {
        this.historicalTree = medDRATree;
    }

    public RichTreeTable getHistoricalTree() {
        return historicalTree;
    }


    public void setFutureTree(RichTreeTable futureTree) {
        this.futureTree = futureTree;
    }

    public RichTreeTable getFutureTree() {
        return futureTree;
    }
    
    public DnDAction onTreeDrop(DropEvent dropEvent) {


        CSMQBean.logger.info(userBean.getCaller() + " -- DRAG AND DROP INITIATED --");
        
        if (futureTree != null && futureTree.getDisclosedRowKeys()!=null )
               futureTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
           
        //TARGET
        Object targetRowKey = dropEvent.getDropSite();
        futureTree.setRowKey(targetRowKey);
        int targetIndex = futureTree.getRowIndex();
        GenericTreeNode targetNode = (GenericTreeNode)futureTree.getRowData(targetIndex);
        CSMQBean.logger.info(userBean.getCaller() + " ** targetNode:" + targetNode);

        DataFlavor<RowKeySet> df = DataFlavor.getDataFlavor(RowKeySet.class);
        RowKeySet droppedValue = dropEvent.getTransferable().getData(df);
        Object[] keys = droppedValue.toArray();
        CSMQBean.logger.info(userBean.getCaller() + " -- DROPPING "+keys.length+ "TERMS");
        
        for (int i = 0; i <keys.length; i++) {
            List list = (List)keys[i];
     
            int depth = list.size();
            int rootKey = Integer.parseInt(list.get(0).toString());


            GenericTreeNode sourceNode = null;
            GenericTreeNode root = null;
            GenericTreeNode c1 = null;
            GenericTreeNode c2 = null;
            GenericTreeNode c3 = null;
            GenericTreeNode c4 = null;

            int c1key;
            int c2key;
            int c3key;
            int c4key;

            switch (depth) {

                case 1:
                    root = (GenericTreeNode)sourceTree.getRowData(rootKey);
                    sourceNode = root;
                    break;
                case 2:
                    root = (GenericTreeNode)sourceTree.getRowData(rootKey);
                    c1key = Integer.parseInt(list.get(1).toString());
                    c1 = (GenericTreeNode)root.getChildren().get(c1key);
                    sourceNode = c1;
                    break;
                case 3:
                    root = (GenericTreeNode)sourceTree.getRowData(rootKey);
                    c1key = Integer.parseInt(list.get(1).toString());
                    c1 = (GenericTreeNode)root.getChildren().get(c1key);
                    c2key = Integer.parseInt(list.get(2).toString());
                    c2 = (GenericTreeNode)c1.getChildren().get(c2key);
                    sourceNode = c2;
                    break;
                case 4:
                    root = (GenericTreeNode)sourceTree.getRowData(rootKey);
                    c1key = Integer.parseInt(list.get(1).toString());
                    c1 = (GenericTreeNode)root.getChildren().get(c1key);
                    c2key = Integer.parseInt(list.get(2).toString());
                    c2 = (GenericTreeNode)c1.getChildren().get(c2key);
                    c3key = Integer.parseInt(list.get(3).toString());
                    c3 = (GenericTreeNode)c2.getChildren().get(c3key);
                    sourceNode = c3;
                    break;
                case 5:
                    root = (GenericTreeNode)sourceTree.getRowData(rootKey);
                    c1key = Integer.parseInt(list.get(1).toString());
                    c1 = (GenericTreeNode)root.getChildren().get(c1key);
                    c2key = Integer.parseInt(list.get(2).toString());
                    c2 = (GenericTreeNode)c1.getChildren().get(c2key);
                    c3key = Integer.parseInt(list.get(3).toString());
                    c3 = (GenericTreeNode)c2.getChildren().get(c3key);
                    c4key = Integer.parseInt(list.get(4).toString());
                    c4 = (GenericTreeNode)c3.getChildren().get(c4key);
                    sourceNode = c4;
                    break;
                }

            CSMQBean.logger.info(userBean.getCaller() + " ADDING NODE:" + sourceNode);
            
           
            if (!legalMove(sourceNode, targetNode)) {
                String msgTxt = "An " + sourceNode.getLevelName() + " cannot be the child of an " + targetNode.getLevelName();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgTxt, null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            
            else {
                sourceNode.setStyle("Impact_NMQ_1070"); // SET IT TO MQM ADDED STYLE
                targetNode.getChildren().add(sourceNode);
                RowKeySet rks = new RowKeySetTreeImpl(true);
                
                rks.setCollectionModel(currentImpactHierarchyBean.getTreemodel());
                this.futureTree.setDisclosedRowKeys(rks);
                AdfFacesContext.getCurrentInstance().addPartialTarget(this.futureTree); 
                AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.futureTree);
                }

        //AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance(); 
        //adfFacesContext.addPartialTarget(futureTree); 
        
        }
        return DnDAction.NONE;
    }
    
    private boolean legalMove (GenericTreeNode source, GenericTreeNode target ) {
            
            // THESE CAN GO TO ANY LEVEL
            if (source.getLevelName().equals("HLGT")) return true;
            if (source.getLevelName().equals("HLT")) return true;
            if (source.getLevelName().equals("LLT")) return true;
            if (source.getLevelName().equals("PT")) return true;
            if (source.getLevelName().equals("PPGRP")) return true;
            if (source.getLevelName().equals("SOC")) return true;
            if (source.getLevelName().equals("VT")) return true;
            
            // THESE MUST FOLLOW THESE RULES
            if (target.getLevelName().equals("MQ1") && source.getLevelName().equals("MQ2")) return true;
            if (target.getLevelName().equals("MQ2") && source.getLevelName().equals("MQ3")) return true;
            if (target.getLevelName().equals("MQ3") && source.getLevelName().equals("MQ4")) return true;
            if (target.getLevelName().equals("MQ4") && source.getLevelName().equals("MQ5")) return true;

            if (target.getLevelName().equals("NMQ1") && source.getLevelName().equals("NMQ2")) return true;
            if (target.getLevelName().equals("NMQ2") && source.getLevelName().equals("NMQ3")) return true;
            if (target.getLevelName().equals("NMQ3") && source.getLevelName().equals("NMQ4")) return true;
            if (target.getLevelName().equals("NMQ4") && source.getLevelName().equals("NMQ5")) return true;

            if (target.getLevelName().equals("NMQ1") && source.getLevelName().equals("MQ1")) return true;
            if (target.getLevelName().equals("NMQ1") && source.getLevelName().equals("MQ2")) return true;
            if (target.getLevelName().equals("NMQ1") && source.getLevelName().equals("MQ3")) return true;
            if (target.getLevelName().equals("NMQ1") && source.getLevelName().equals("MQ4")) return true;
            if (target.getLevelName().equals("NMQ1") && source.getLevelName().equals("MQ5")) return true;

            if (target.getLevelName().equals("NMQ2") && source.getLevelName().equals("MQ1")) return true;
            if (target.getLevelName().equals("NMQ2") && source.getLevelName().equals("MQ2")) return true;
            if (target.getLevelName().equals("NMQ2") && source.getLevelName().equals("MQ3")) return true;
            if (target.getLevelName().equals("NMQ2") && source.getLevelName().equals("MQ4")) return true;
            if (target.getLevelName().equals("NMQ2") && source.getLevelName().equals("MQ5")) return true;

            if (target.getLevelName().equals("NMQ3") && source.getLevelName().equals("MQ1")) return true;
            if (target.getLevelName().equals("NMQ3") && source.getLevelName().equals("MQ2")) return true;
            if (target.getLevelName().equals("NMQ3") && source.getLevelName().equals("MQ3")) return true;
            if (target.getLevelName().equals("NMQ3") && source.getLevelName().equals("MQ4")) return true;
            if (target.getLevelName().equals("NMQ3") && source.getLevelName().equals("MQ5")) return true;

            if (target.getLevelName().equals("NMQ4") && source.getLevelName().equals("MQ1")) return true;
            if (target.getLevelName().equals("NMQ4") && source.getLevelName().equals("MQ2")) return true;
            if (target.getLevelName().equals("NMQ4") && source.getLevelName().equals("MQ3")) return true;
            if (target.getLevelName().equals("NMQ4") && source.getLevelName().equals("MQ4")) return true;
            if (target.getLevelName().equals("NMQ4") && source.getLevelName().equals("MQ5")) return true;

            if (target.getLevelName().equals("NMQ5") && source.getLevelName().equals("MQ1")) return true;
            if (target.getLevelName().equals("NMQ5") && source.getLevelName().equals("MQ2")) return true;
            if (target.getLevelName().equals("NMQ5") && source.getLevelName().equals("MQ3")) return true;
            if (target.getLevelName().equals("NMQ5") && source.getLevelName().equals("MQ4")) return true;
            if (target.getLevelName().equals("NMQ5") && source.getLevelName().equals("MQ5")) return true;
            return false;
            }

    public void onTreeNodeDelete(ActionEvent actionEvent) { 
    
        //RowKeySet rks = futureTree.getSelectedRowKeys();
        //Iterator rksIterator = rks.iterator();
        
        int rowIndexTreeTable = futureTree.getRowIndex();
        //Object key = actionEvent.getSource();
        
        GenericTreeNode node = (GenericTreeNode)futureTree.getRowData(rowIndexTreeTable);
       
        node.getParentNode().getChildren().remove(node);
       
        //AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance(); 
        //adfFacesContext.addPartialTarget(futureTree); 
        RowKeySet rks = new RowKeySetTreeImpl(true);
        rks.setCollectionModel(currentImpactHierarchyBean.getTreemodel());
        this.futureTree.setDisclosedRowKeys(rks);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.futureTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(this.futureTree);

        CSMQBean.logger.info(userBean.getCaller() + " ** removed targetNode:" + node);
        }

    private Object [][] getChildrenForUpdate () {
        
        List immediateChildren = ((GenericTreeNode)futureTree.getRowData(0)).getChildren();
        int arrayLength = immediateChildren.size();
        Object [][] retVal = new Object[arrayLength][PARAM_LENGTH];
        
        Iterator iterator = immediateChildren.iterator();
        int rowCount = 0;

        CSMQBean.logger.info(userBean.getCaller() + " ** CREATING NODE ARRAY FOR UPDATE **");
            
        while(iterator.hasNext()) {
            GenericTreeNode genericTreeNode = (GenericTreeNode)iterator.next();
                retVal[rowCount][VAL_PREDICT_GROUP_ID] = genericTreeNode.getPredictGroupId();
                retVal[rowCount][VAL_DICT_CONTENT_ID] = genericTreeNode.getDictContentId();
                retVal[rowCount][VAL_TERMLVL] = genericTreeNode.getTermLevel();
                retVal[rowCount][VAL_TERMSCP] = null; //genericTreeNode.getTermScope();
                retVal[rowCount][VAL_TERMCAT] = genericTreeNode.getTermCategory();
                retVal[rowCount][VAL_TERMWEIG] = genericTreeNode.getTermWeight();
                retVal[rowCount][VAL_DML] = null;

            CSMQBean.logger.info(userBean.getCaller() + " ROW:" + rowCount);
            CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_PREDICT_GROUP_ID + ":" + retVal[rowCount][VAL_PREDICT_GROUP_ID]);
            CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_DICT_CONTENT_ID + ":" + retVal[rowCount][VAL_DICT_CONTENT_ID]);
            CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMSCP + ":" + retVal[rowCount][VAL_TERMSCP]);
            CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMCAT + ":" + retVal[rowCount][VAL_TERMCAT]);
            CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMLVL + ":" + retVal[rowCount][VAL_TERMLVL]);
            CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMWEIG + ":" + retVal[rowCount][VAL_TERMWEIG]);
             
            
            rowCount++;      
            }
        return retVal;
        }

    public void updateRelations (ActionEvent actionEvent) {
        
        /*
        hierarchy_pkg.relate_children_to_parent(
            i_dict_id,      <-- num
            i_grp,          <-- string
            i_created_by,   <-- string
            v_objectArray   <-- object array
            o_returnVal     <-- string, output param
         );
        */
                
        
        
        String currentDictContentID = this.currentDictId;
        String currentPredictGroups = this.getDefaultDraftGroupName();
        String currentUser = userBean.getUsername();
        Object [][] relations = this.getChildrenForUpdate();
        
        String sql = "{call hierarchy_pkg.relate_children_to_parent (?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        String newDictContentCode = "";
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "MQ updated successfully.", null); 
        CallableStatement cstmt = null;
        Connection con = null;
        try {
            cstmt = dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT);
            con = cstmt.getConnection();
            ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "RELATION_CHILD_ARRAY", con);
            ARRAY array_to_pass = new ARRAY( descriptor, cstmt.getConnection(), relations);
        
            cstmt.setString(1, currentDictContentID);
            cstmt.setString(2, currentPredictGroups);
            cstmt.setString(3, currentUser);
            cstmt.setArray( 4, array_to_pass);
    
            cstmt.setString(5, "");
            cstmt.registerOutParameter(5, Types.NVARCHAR);
            cstmt.executeUpdate();
            newDictContentCode = cstmt.getString(5);  
            con.commit();
            //cstmt.close();

            CSMQBean.logger.info(userBean.getCaller() + " ** SAVING RELATIONS");
            CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID: " + currentDictContentID);
            CSMQBean.logger.info(userBean.getCaller() + " currentPredictGroups: " + currentPredictGroups);
            CSMQBean.logger.info(userBean.getCaller() + " currentUser: " + currentUser);
            CSMQBean.logger.info(userBean.getCaller() + " array_to_pass: " + array_to_pass);
            
            } 
        catch (SQLException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The following error occurred: " + e.getMessage(), null);
            e.printStackTrace();
        } finally {
            if (null != cstmt){
                try {
                    cstmt.close();
                } catch (SQLException sqe){
                    CSMQBean.logger.info("error while closing callable statment...");     
                }
            }
        }
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void setSourceTree(RichTreeTable sourceTree) {
        this.sourceTree = sourceTree;
    }

    public RichTreeTable getSourceTree() {
        return sourceTree;
    }

    public void setAsOfDate(String asOfDate) {
        this.asOfDate = asOfDate;
    }

    public String getAsOfDate() {
        return asOfDate;
    }
}
