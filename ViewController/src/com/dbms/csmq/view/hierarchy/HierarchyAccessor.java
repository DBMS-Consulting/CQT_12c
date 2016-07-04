/**
 *  Superclass used by classes that access hierarchies.
 *
 * @author Tom Struzik
 * @version 1.0 Sep 23, 2011
 */

package com.dbms.csmq.view.hierarchy;


import com.dbms.csmq.CSMQBean;
import com.dbms.util.dml.DMLUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.datatransfer.DataFlavor;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DropEvent;

import oracle.jbo.server.DBTransaction;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.model.ModelUtils;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetTreeImpl;
import org.apache.myfaces.trinidad.model.TreeModel;


public abstract class HierarchyAccessor extends Hierarchy {

       
    // UPDATE ARRAY VALUES
    public static final int PARAM_LENGTH = 7;
    public static final int VAL_PREDICT_GROUP_ID = 0;
    public static final int VAL_DICT_CONTENT_ID = 1;   
    public static final int VAL_TERMSCP = 2;
    public static final int VAL_TERMCAT = 3;
    public static final int VAL_TERMLVL = 4;
    public static final int VAL_TERMWEIG = 5;
    public static final int VAL_DML = 6;
     
    protected Hashtable <String, GenericTreeNode> inserts = new Hashtable <String, GenericTreeNode> ();  
    protected Hashtable <String, GenericTreeNode> deletes = new Hashtable <String, GenericTreeNode> ();
    protected Hashtable <String, GenericTreeNode> updates = new Hashtable <String, GenericTreeNode> (); 
     
    public HierarchyAccessor () {
        super();
    }
      

    protected boolean legalMove(GenericTreeNode source, GenericTreeNode target) {

        // THESE CAN GO TO ANY LEVEL
        if (source.getQueryLevel().equals("HLGT"))       return true;
        if (source.getQueryLevel().equals("HLT"))        return true;
        if (source.getQueryLevel().equals("LLT"))        return true;
        if (source.getQueryLevel().equals("PT"))         return true;
        if (source.getQueryLevel().equals("PPGRP"))      return true;
        if (source.getQueryLevel().equals("SOC"))        return true;
        if (source.getQueryLevel().equals("VT"))         return true;

        // THESE MUST FOLLOW THESE RULES
        if (target.getQueryLevel().equals("MQ1") && source.getQueryLevel().equals("MQ2")) return true;
        if (target.getQueryLevel().equals("MQ2") && source.getQueryLevel().equals("MQ3")) return true;
        if (target.getQueryLevel().equals("MQ3") && source.getQueryLevel().equals("MQ4")) return true;
        if (target.getQueryLevel().equals("MQ4") && source.getQueryLevel().equals("MQ5")) return true;

        if (target.getQueryLevel().equals("NMQ1") && source.getQueryLevel().equals("NMQ2")) return true;
        if (target.getQueryLevel().equals("NMQ2") && source.getQueryLevel().equals("NMQ3")) return true;
        if (target.getQueryLevel().equals("NMQ3") && source.getQueryLevel().equals("NMQ4")) return true;
        if (target.getQueryLevel().equals("NMQ4") && source.getQueryLevel().equals("NMQ5")) return true;

        if (target.getQueryLevel().equals("NMQ1") && source.getQueryLevel().equals("MQ1")) return true;
        if (target.getQueryLevel().equals("NMQ1") && source.getQueryLevel().equals("MQ2")) return true;
        if (target.getQueryLevel().equals("NMQ1") && source.getQueryLevel().equals("MQ3")) return true;
        if (target.getQueryLevel().equals("NMQ1") && source.getQueryLevel().equals("MQ4")) return true;
        if (target.getQueryLevel().equals("NMQ1") && source.getQueryLevel().equals("MQ5")) return true;

        if (target.getQueryLevel().equals("NMQ2") && source.getQueryLevel().equals("MQ1")) return true;
        if (target.getQueryLevel().equals("NMQ2") && source.getQueryLevel().equals("MQ2")) return true;
        if (target.getQueryLevel().equals("NMQ2") && source.getQueryLevel().equals("MQ3")) return true;
        if (target.getQueryLevel().equals("NMQ2") && source.getQueryLevel().equals("MQ4")) return true;
        if (target.getQueryLevel().equals("NMQ2") && source.getQueryLevel().equals("MQ5")) return true;

        if (target.getQueryLevel().equals("NMQ3") && source.getQueryLevel().equals("MQ1")) return true;
        if (target.getQueryLevel().equals("NMQ3") && source.getQueryLevel().equals("MQ2")) return true;
        if (target.getQueryLevel().equals("NMQ3") && source.getQueryLevel().equals("MQ3")) return true;
        if (target.getQueryLevel().equals("NMQ3") && source.getQueryLevel().equals("MQ4")) return true;
        if (target.getQueryLevel().equals("NMQ3") && source.getQueryLevel().equals("MQ5")) return true;

        if (target.getQueryLevel().equals("NMQ4") && source.getQueryLevel().equals("MQ1")) return true;
        if (target.getQueryLevel().equals("NMQ4") && source.getQueryLevel().equals("MQ2")) return true;
        if (target.getQueryLevel().equals("NMQ4") && source.getQueryLevel().equals("MQ3")) return true;
        if (target.getQueryLevel().equals("NMQ4") && source.getQueryLevel().equals("MQ4")) return true;
        if (target.getQueryLevel().equals("NMQ4") && source.getQueryLevel().equals("MQ5")) return true;

        if (target.getQueryLevel().equals("NMQ5") && source.getQueryLevel().equals("MQ1")) return true;
        if (target.getQueryLevel().equals("NMQ5") && source.getQueryLevel().equals("MQ2")) return true;
        if (target.getQueryLevel().equals("NMQ5") && source.getQueryLevel().equals("MQ3")) return true;
        if (target.getQueryLevel().equals("NMQ5") && source.getQueryLevel().equals("MQ4")) return true;
        if (target.getQueryLevel().equals("NMQ5") && source.getQueryLevel().equals("MQ5")) return true;
        return false;
        }

    protected Object resolveMethodExpression(String expression, Class returnType, Class[] argTypes, Object[] argValues) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        MethodExpression methodExpression = elFactory.createMethodExpression(elContext, expression, returnType, argTypes);
        Object retVal = methodExpression.invoke(elContext, argValues);
        return retVal;
    }

    public DnDAction processDragAndDropEvent (DropEvent dropEvent, RichTreeTable sourceTree, RichTreeTable targetTree, TreeModel targetTreeModel, int rootTermScope) {
        
        
        CSMQBean.logger.info(userBean.getCaller() + " -- DRAG AND DROP INITIATED --");
        
        /*
        if (targetTree != null && targetTree.getDisclosedRowKeys()!=null )
               targetTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        */
           
        clearKeys(sourceTree);
        clearKeys(targetTree);
        
        //TARGET
        //Object targetRowKey = dropEvent.getDropSite();
        //CHANGE TO FORCE THE ROOT NODE TO BE TARGET
        //targetTree.setRowKey(targetRowKey);
        //int targetIndex = targetTree.getRowIndex()
        
        ArrayList targetRootKey = new ArrayList();
        targetRootKey.add(0);
        targetTree.setRowKey(targetRootKey);
        int targetIndex = 0;
        
     
        
        GenericTreeNode targetNode = (GenericTreeNode)targetTree.getRowData(targetIndex);
        CSMQBean.logger.info(userBean.getCaller() + " TARGET:" + targetNode);

        DataFlavor<RowKeySet> df = DataFlavor.getDataFlavor(RowKeySet.class);
        RowKeySet droppedValue = dropEvent.getTransferable().getData(df);
        Object[] keys = droppedValue.toArray();
        CSMQBean.logger.info(userBean.getCaller() + " DROPPING ["+keys.length+"] TERMS");
        
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
           
            // set the scope to 0
            sourceNode.setTermScope(new oracle.jbo.domain.Number(rootTermScope));
            
            CSMQBean.logger.info(userBean.getCaller() + " ADDING NODE:" + sourceNode);

            if (!targetNode.isEditable()) {
                String msgTxt = targetNode.getTerm() + " is not editable";
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgTxt, null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                }         
            else if (!legalMove(sourceNode, targetNode)) {
                String msgTxt = "An " + sourceNode.getQueryLevel() + " cannot be the child of an " + targetNode.getQueryLevel();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgTxt, null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                }           
            else {
                sourceNode.setStyle("Impact_NMQ_1070"); // SET IT TO MQM ADDED STYLE
                //set defaults when term level is Meddra Level
                boolean isFormattedScopeNull = (null == sourceNode.getFormattedScope());
                if (null != sourceNode.getLevelName() && isMeddDRATermLevel(sourceNode.getLevelName())){
                    setDefaultRelations(sourceNode);
                    // reset formatted scope if null
                    if (isFormattedScopeNull){
                        sourceNode.setFormattedScope(null);
                    }
                }
                targetNode.getChildren().add(sourceNode);
                RowKeySet rks = new RowKeySetTreeImpl(true);
                rks.setCollectionModel(targetTreeModel);
                targetTree.setDisclosedRowKeys(rks);
                AdfFacesContext.getCurrentInstance().addPartialTarget(targetTree); 
                AdfFacesContext.getCurrentInstance().partialUpdateNotify(targetTree);
                }
        
            inserts.put(sourceNode.getPrikey(), sourceNode);
        
        
        }
        clearKeys(sourceTree);
        clearKeys(targetTree);
        CSMQBean.logger.info(userBean.getCaller() + " -- DRAG AND DROP COMPLETE --");
        
        
        RowKeySet disclosedRowKeys = new RowKeySetTreeImpl(true); 
        disclosedRowKeys.setCollectionModel(ModelUtils.toTreeModel(targetTree.getValue()));
        targetTree.setDisclosedRowKeys(disclosedRowKeys);
        
        
        
        return DnDAction.NONE;
    }
 
    public void processTreeNodeDelete(RichTreeTable tree, TreeModel treeModel) { 
    
        int rowIndexTreeTable = tree.getRowIndex();
     
        GenericTreeNode node = (GenericTreeNode)tree.getRowData(rowIndexTreeTable);
        node.getParentNode().getChildren().remove(node);
       
        RowKeySet rks = new RowKeySetTreeImpl(true);
        rks.setCollectionModel(treeModel);
        tree.setDisclosedRowKeys(rks);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(tree);

        CSMQBean.logger.info(userBean.getCaller() + " ** removed targetNode:" + node);
        }


    public void processDeleteSelected(DialogEvent dialogEvent, RichTreeTable tree, TreeModel treeModel) {
        int numberOfTermsToBeDeleted = 0;
        List immediateChildren = ((GenericTreeNode)tree.getRowData(0)).getChildren();
        ArrayList <GenericTreeNode> nodesToBeDeleted = new ArrayList <GenericTreeNode> ();
        
        Iterator iterator = immediateChildren.iterator();

        CSMQBean.logger.info(userBean.getCaller() + " ** CREATING LIST FOR DELETE **");
                
        while(iterator.hasNext()) {
            GenericTreeNode genericTreeNode = (GenericTreeNode)iterator.next();
            if (genericTreeNode.isMarkedForDeletion()) {
            CSMQBean.logger.info(userBean.getCaller() + " ADDING TO DELETE LIST: " + genericTreeNode.toString());
                nodesToBeDeleted.add(genericTreeNode);
                numberOfTermsToBeDeleted++;
                }  
            }
        
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.ok)) {
            CSMQBean.logger.info(userBean.getCaller() + " DELETING");
            for (GenericTreeNode gtn : nodesToBeDeleted) { 
                    gtn.getParentNode().getChildren().remove(gtn);  // GET THE PARRENT AND REMOVE ITSELF
                     //  18-JUL-2012  - ADD IT TO THE DELETES ARRAY
                    deletes.put(gtn.getPrikey(), gtn);
                    }
               
                RowKeySet rks = new RowKeySetTreeImpl(true);
                rks.setCollectionModel(treeModel);
                tree.setDisclosedRowKeys(rks);
                AdfFacesContext.getCurrentInstance().addPartialTarget(tree); 
                AdfFacesContext.getCurrentInstance().partialUpdateNotify(tree); 
                }
        else {
            CSMQBean.logger.info(userBean.getCaller() + " NOT DELETING");
            }
            
        }



    private Object [][] getChildrenForUpdate (RichTreeTable tree) {
        
        List immediateChildren = ((GenericTreeNode)tree.getRowData(0)).getChildren();
        int arrayLength = immediateChildren.size();
        Object [][] retVal = new Object[arrayLength][PARAM_LENGTH];
        
        Iterator iterator = immediateChildren.iterator();
        int rowCount = 0;

        CSMQBean.logger.info(userBean.getCaller() + " ** CREATING NODE ARRAY FOR UPDATE **");
            
        while(iterator.hasNext()) {
            GenericTreeNode genericTreeNode = (GenericTreeNode)iterator.next();
                retVal[rowCount][VAL_PREDICT_GROUP_ID] = genericTreeNode.getPredictGroupId();
                retVal[rowCount][VAL_DICT_CONTENT_ID] = genericTreeNode.getDictContentId();
                retVal[rowCount][VAL_TERMSCP] = genericTreeNode.getFormattedScope();
                retVal[rowCount][VAL_TERMCAT] = genericTreeNode.getTermCategory();
                retVal[rowCount][VAL_TERMLVL] = genericTreeNode.getTermLevel();
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

    
        private Object [][] getChildrenForUpdate () {
            
            Hashtable [] changes = {inserts, updates, deletes};
            String [] DML = {"I", "U", "D"};
                             
            int arrayLength = inserts.size() + updates.size() + deletes.size();
            Object [][] retVal = new Object[arrayLength][PARAM_LENGTH];
            
            int rowCount = 0;
            CSMQBean.logger.info(userBean.getCaller() + " ** CREATING NODE ARRAY FOR UPDATE **");
                
            for (int i=0; i<changes.length; i++) {
  
                Enumeration keys = changes[i].keys();
                while(keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                   
                    GenericTreeNode genericTreeNode = (GenericTreeNode)changes[i].remove(key);
                    retVal[rowCount][VAL_PREDICT_GROUP_ID] = genericTreeNode.getPredictGroupId();
                    retVal[rowCount][VAL_DICT_CONTENT_ID] = genericTreeNode.getDictContentId();
                    retVal[rowCount][VAL_TERMSCP] = genericTreeNode.getFormattedScope();
                    retVal[rowCount][VAL_TERMCAT] = genericTreeNode.getTermCategory();
                    retVal[rowCount][VAL_TERMLVL] = genericTreeNode.getTermLevel();
                    retVal[rowCount][VAL_TERMWEIG] = genericTreeNode.getTermWeight();
                    retVal[rowCount][VAL_DML] = DML[i];
    
                    CSMQBean.logger.info(userBean.getCaller() + " ROW:" + rowCount);
                    CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_PREDICT_GROUP_ID + ":" + retVal[rowCount][VAL_PREDICT_GROUP_ID]);
                    CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_DICT_CONTENT_ID + ":" + retVal[rowCount][VAL_DICT_CONTENT_ID]);
                    CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMSCP + ":" + retVal[rowCount][VAL_TERMSCP]);
                    CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMCAT + ":" + retVal[rowCount][VAL_TERMCAT]);
                    CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMLVL + ":" + retVal[rowCount][VAL_TERMLVL]);
                    CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_TERMWEIG + ":" + retVal[rowCount][VAL_TERMWEIG]);
                    CSMQBean.logger.info(userBean.getCaller() + "  " + VAL_DML + ":" + retVal[rowCount][VAL_DML]);
                    rowCount++;      
                    }
                }
            
            return retVal;
            }
        
        

    public int processUpdateRelations (RichTreeTable tree, String dictContentID) {
        
        /*
        hierarchy_pkg.relate_children_to_parent(
            i_dict_id,      <-- num
            i_grp,          <-- string
            i_created_by,   <-- string
            v_objectArray   <-- object array
            o_returnVal     <-- string, output param
         );
        */
        
        String currentDictContentID = dictContentID;
        String currentPredictGroups = this.defaultDraftGroupName;
        String currentUser = userBean.getUsername();
        Object [][] relations = this.getChildrenForUpdate();
        
        String sql = "{call hierarchy_pkg.relate_children_to_parent (?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        String newDictContentCode = "";
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "MQ updated successfully.", null); 
            Connection con = null;
            CallableStatement cstmt = null;
        try {
            cstmt = dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT);
            con = cstmt.getConnection();
            ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "RELATION_CHILD_ARRAY", con);
            ARRAY array_to_pass = new ARRAY( descriptor, cstmt.getConnection(), relations);

            CSMQBean.logger.info(userBean.getCaller() + " ** SAVING RELATIONS");
            CSMQBean.logger.info(userBean.getCaller() + " currentDictContentID: " + currentDictContentID);
            CSMQBean.logger.info(userBean.getCaller() + " currentPredictGroups: " + currentPredictGroups);
            CSMQBean.logger.info(userBean.getCaller() + " currentUser: " + currentUser);
            CSMQBean.logger.info(userBean.getCaller() + " array_to_pass: " + array_to_pass);
        
        
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
            
            // REMOVE ALL THE PENDING CHANGES - JUST TO BE SAFE
            inserts.clear();
            updates.clear();
            deletes.clear();
            
            } 
        catch (SQLException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The following error occurred: " + e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            e.printStackTrace();
                return -1;
            }
            finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.info("error while closing callable statment...");     
                    }
                }
            }
              
        return 0;
        }
        private boolean isMeddDRATermLevel(String levelName){
            boolean returnVal = false;
            if (null != levelName && levelName.equals(CSMQBean.SOC) 
               || levelName.equals(CSMQBean.HLGT)
               || levelName.equals(CSMQBean.HLT)
               || levelName.equals(CSMQBean.PT)
               || levelName.equals(CSMQBean.LLT)) {
               returnVal = true;     
            }
           return returnVal;
        }
}
