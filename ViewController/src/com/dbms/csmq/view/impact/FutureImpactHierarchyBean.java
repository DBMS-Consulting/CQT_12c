package com.dbms.csmq.view.impact;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisUIBean;
import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.hierarchy.Hierarchy;

import com.dbms.util.Utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.layout.RichToolbar;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;

import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetTreeImpl;
import org.apache.myfaces.trinidad.model.TreeModel;


public class FutureImpactHierarchyBean extends Hierarchy {

    private TreeModel treemodel;
    private Enumeration rows;
    private HashMap parentNodesByLevel;
    private boolean editable;
    
    private RichSelectOneChoice dictionaryVersion;
    private RichSelectOneChoice levelList;
    private RichInputText term;
    private int numberOfTermsToBeDeleted;
    private boolean hasScope = false;
    
    
    public FutureImpactHierarchyBean() {
        System.out.println("START: FutureImpactHierarchyBean");
        System.out.println("END: FutureImpactHierarchyBean");  
        }

    
    public void init (boolean hasScope) {
        this.hasScope = hasScope;
        parentNodesByLevel = new HashMap();
        createTree();
        List nodes = new ArrayList();
        nodes.add(root);
        treemodel = new ChildPropertyTreeModel(nodes, "children") {
                public boolean isContainer() {
                    if (getRowData() == null) return false;
                    return ((GenericTreeNode)getRowData()).getChildCount() > 0;
                }
            }; 
        }

    private void createTree() {
      
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("DraftImpactVO1Iterator1");

        rows = dciterb.getRowSetIterator().enumerateRowsInRange();
 
        Row row = (Row)rows.nextElement();

        root = new GenericTreeNode();
        root.setIsRoot(true);
        root.setTerm(Utils.getAsString(row,"Term"));
        root.setPrikey(Utils.getAsString(row,"Prikey"));
        root.setParent(Utils.getAsString(row,"Parent"));
        root.setLevelName(Utils.getAsString(row,"LevelName"));
        root.setQueryLevel(Utils.getAsString(row,"LevelName"));
        root.setLevel(Utils.getAsNumber(row,"Level"));
        root.setDictShortName(Utils.getAsString(row,"DictShortName"));
        root.setDictContentId(Utils.getAsString(row,"DictContentId"));
        root.setDictContentCode(Utils.getAsString(row,"DictContentCode"));
        root.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
        root.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
        root.setStatus(Utils.getAsString(row,"Status"));
        root.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
        root.setPath(Utils.getAsString(row,"TermPath"));
        root.setTermCategory(Utils.getAsString(row,"Termcat"));
        root.setTermLevel(Utils.getAsString(row, "Termlvl"));
        root.setTermScope(Utils.getAsNumber(row,"Termscp"));
        root.setTermWeight(Utils.getAsString(row,"Termweig"));
        root.setPath(Utils.getAsString(row,"TermPath"));
        root.setFormattedScope(Utils.getAsString(row,"FormattedScope"));
        root.setHasScope(hasScope);
        
        if (root.getLevelName().contains (CSMQBean.NMQ)) {
            root.setMqType(CSMQBean.NMQ);
            root.setEditable(true);
            this.editable = true;
        } else if (root.getLevelName().contains (CSMQBean.CMQ)){
            root.setMqType(CSMQBean.CMQ);
            root.setEditable(true);
            this.editable = true;
        }
        else {
            root.setMqType(CSMQBean.SMQ);
            root.setEditable(false);
            this.editable = false;
            }
        
        //NEW FOR IMPACT
        Object displayAttribute = Utils.getAsString(row, "DisplayAttribute");
        if (displayAttribute != null) {
            String code = Utils.getAsString(row,"DisplayAttribute");
            String description = CSMQBean.getProperty("Impact_" + root.getMqType() + "_" + code);
            root.setDescription(description);
            String cssClass = "Impact_" + root.getMqType() + "_" + code;
            root.setStyle(cssClass); // THIS IS USED TO CALL THE CORRECT STYLE
            root.setIcon(code); // SET THE MATCHING ICON - IF IT'S NULL IT WON'T SHOW
            }

        root.setShowHasChildrenButton(false); //don't show it for the root
        
        CSMQBean.logger.info(userBean.getCaller() + " ADDING ROOT: " + root.toString() + ";EDITABLE=" + this.editable);

        populateTreeNodes(root);
        //clean up the hashmap
        parentNodesByLevel = null;

    }

    private GenericTreeNode populateTreeNodes(GenericTreeNode node) {

        //store node and level
        if (parentNodesByLevel == null) return null;
        if (node != null)
            parentNodesByLevel.put(node.getDictContentId(), node);

        if (rows.hasMoreElements()) {
            Row row = (Row)rows.nextElement();
            GenericTreeNode termNode = new GenericTreeNode();
            termNode.setTerm(Utils.getAsString(row,"Term"));
            termNode.setPrikey(Utils.getAsString(row,"Prikey"));
            termNode.setParent(Utils.getAsString(row,"Parent"));
            termNode.setLevelName(Utils.getAsString(row,"LevelName"));
            termNode.setLevel(Utils.getAsNumber(row,"Level"));
            termNode.setDictShortName(Utils.getAsString(row,"DictShortName"));
            termNode.setDictContentId(Utils.getAsString(row,"DictContentId"));
            termNode.setDictContentCode(Utils.getAsString(row,"DictContentCode"));
            termNode.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
            termNode.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
            termNode.setStatus(Utils.getAsString(row,"Status"));
            termNode.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
            termNode.setPath(Utils.getAsString(row,"TermPath"));
            termNode.setTermCategory(Utils.getAsString(row,"Termcat"));
            termNode.setTermLevel(Utils.getAsString(row, "Termlvl"));
            termNode.setTermScope(Utils.getAsNumber(row,"Termscp"));
            termNode.setTermWeight(Utils.getAsString(row,"Termweig"));
            termNode.setFormattedScope(Utils.getAsString(row,"FormattedScope"));
            termNode.setMqType(root.getMqType()); // set the query type the same as the parent
            termNode.setHasScope(this.hasScope);
            termNode.setEditable(this.editable); // 4.APR.2014
            Object displayAttribute = row.getAttribute("DisplayAttribute");
                        
            GenericTreeNode parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());
            parentNode.getChildren().add(termNode);  // add to the parent
            termNode.setParentNode(parentNode);      // set the parent for the child
            if (parentNode.isIsRoot()) termNode.setDeletable(true); //it's a child of the root - it can be deleted
            if (root.equals(parentNode)) termNode.setIsDirectRelation(true); // it's a direct relation
            
            if (displayAttribute != null) {
                String code = Utils.getAsString(row,"DisplayAttribute");
                String description = CSMQBean.getProperty("Impact_" + termNode.getMqType() + "_" + code);
                termNode.setDescription(description);
                String cssClass = "Impact_" + termNode.getMqType() + "_" + code;
                termNode.setStyle(cssClass); // THIS IS USED TO CALL THE CORRECT STYLE
                termNode.setIcon(code); // SET THE MATCHING ICON - IF IT'S NULL IT WON'T SHOW
                //FILTER OUT THESE CODES
                if (code.equals(CSMQBean.DELETED_MERGED_MOVED_TERM_RELATION)) {
                    CSMQBean.logger.info(userBean.getCaller() + " FUTURE: IGNORING " + termNode);
                    termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
                    } 
                }
            //REMOVE LLTs FROM THTE ROOT
            if (parentNode.isIsRoot() && termNode.getLevelName().equals("LLT")) {
                termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
                CSMQBean.logger.info(userBean.getCaller() + " REMOVING LLT " + termNode);
                }
               
            // FOR 'LAZY' LOADING          
            boolean showMoreChildren = Utils.getAsBoolean(row,"ChildExists");
            if (showMoreChildren) 
                termNode.setShowHasChildrenButton(true);
               
            
            
               
            setDerivedRelations(termNode);
            CSMQBean.logger.info(userBean.getCaller() + " FUTURE ADDING NODE: " + termNode);
            populateTreeNodes(termNode);
            }
        return node;
        }


    public void setTreemodel(TreeModel treemodel) {
        this.treemodel = treemodel;
    }

    public TreeModel getTreemodel() {
        return treemodel;
    }


    public void setDictionaryVersion(RichSelectOneChoice dictionaryVersion) {
        this.dictionaryVersion = dictionaryVersion;
    }

    public RichSelectOneChoice getDictionaryVersion() {
        return dictionaryVersion;
    }

    public void setLevelList(RichSelectOneChoice levelList) {
        this.levelList = levelList;
    }

    public RichSelectOneChoice getLevelList() {
        return levelList;
    }

    public void setTerm(RichInputText term) {
        this.term = term;
    }

    public RichInputText getTerm() {
        return term;
    }

    public void setNumberOfTermsToBeDeleted(int numberOfTermsToBeDeleted) {
        this.numberOfTermsToBeDeleted = numberOfTermsToBeDeleted;
    }

    public int getNumberOfTermsToBeDeleted() {
        return numberOfTermsToBeDeleted;
    }

    public void sortByLevel(AttributeChangeEvent attributeChangeEvent) {
        CSMQBean.logger.info ("SORT LEVEL");
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
    
    public void expandChildren(ActionEvent actionEvent) {
        showChildren();
        }
    
    private void showChildren() {
        
        ImpactAnalysisUIBean impactAnalysisUIBean = (ImpactAnalysisUIBean)ADFContext.getCurrent().getRequestScope().get("ImpactAnalysisUIBean");
        ImpactAnalysisBean impactAnalysisBean = (ImpactAnalysisBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("ImpactAnalysisBean");
        RichTreeTable targetTree = impactAnalysisBean.getFutureTree();        
       // Clear keys
        
       if (targetTree != null && targetTree.getDisclosedRowKeys()!=null )
           targetTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
       
       GenericTreeNode newRootNode = null;

       RichTreeTable tree = targetTree;
       RowKeySet droppedValue = targetTree.getSelectedRowKeys();

       Object[] keys = droppedValue.toArray();
       
       for (int i = 0; i <keys.length; i++) {
           List list = (List)keys[i];
       
           int depth = list.size();
           int rootKey = Integer.parseInt(list.get(0).toString());
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
                   newRootNode = root;
                   break;
               case 2:
                   c1key = Integer.parseInt(list.get(1).toString());
                   c1 = (GenericTreeNode)root.getChildren().get(c1key);
                   newRootNode = c1;
                   break;
               case 3:
                   c1key = Integer.parseInt(list.get(1).toString());
                   c1 = (GenericTreeNode)root.getChildren().get(c1key);
                   c2key = Integer.parseInt(list.get(2).toString());
                   c2 = (GenericTreeNode)c1.getChildren().get(c2key);
                   newRootNode = c2;
                   break;
               case 4:
                   c1key = Integer.parseInt(list.get(1).toString());
                   c1 = (GenericTreeNode)root.getChildren().get(c1key);
                   c2key = Integer.parseInt(list.get(2).toString());
                   c2 = (GenericTreeNode)c1.getChildren().get(c2key);
                   c3key = Integer.parseInt(list.get(3).toString());
                   c3 = (GenericTreeNode)c2.getChildren().get(c3key);
                   newRootNode = c3;
                   break;
               case 5:
                   c1key = Integer.parseInt(list.get(1).toString());
                   c1 = (GenericTreeNode)root.getChildren().get(c1key);
                   c2key = Integer.parseInt(list.get(2).toString());
                   c2 = (GenericTreeNode)c1.getChildren().get(c2key);
                   c3key = Integer.parseInt(list.get(3).toString());
                   c3 = (GenericTreeNode)c2.getChildren().get(c3key);
                   c4key = Integer.parseInt(list.get(4).toString());
                   c4 = (GenericTreeNode)c3.getChildren().get(c4key);
                   newRootNode = c4;
                   break;
               }

       }
       
       if (newRootNode.isIsExpanded()) return; // don't requery if already done
       
       // REQUERY AND GET THE CHILDREN
       BindingContext bc = BindingContext.getCurrent();
       DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
       DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("DraftImpactVO1Iterator1");
       ViewObject vo = dciterb.getViewObject();
       String parentTermScope = "0";
       if (newRootNode != null)
           parentTermScope = newRootNode.getFormattedScope();
           //parentTermScope = newRootNode.getParentNode().getFormattedScope();
           //  18MAR change -- parentTermScope = newRootNode.getFormattedScope();  // TODO test fix for term scope error
           
       
       //String bothGroups = this.defaultDraftGroupName + "," + this.defaultMedDRAGroupName;
       CSMQBean.logger.info(userBean.getCaller() + " \nUPDATING: " +  impactAnalysisBean.getAllGroups());     
        
       vo.setNamedWhereClauseParam("activationGroup", impactAnalysisBean.getAllGroups());
       vo.setNamedWhereClauseParam("dictContentID", newRootNode.getDictContentId());
       vo.setNamedWhereClauseParam("sortKey", impactAnalysisBean.getParamFutureSort());
       vo.setNamedWhereClauseParam("showNonImpacted", impactAnalysisBean.getParamFutureShowNonImpacted());
       vo.setNamedWhereClauseParam("returnPrimLinkPath", impactAnalysisBean.getParamFuturePrimaryOnly());
       
       
       // 18 MAR Fix
       //vo.setNamedWhereClauseParam("termScopeFilter", impactAnalysisBean.getParamFutureScope());
      
       vo.setNamedWhereClauseParam("scopeFilter", parentTermScope);
       vo.setNamedWhereClauseParam("maxLevels", CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
       vo.setNamedWhereClauseParam("startLevel", newRootNode.getLevel());
        
       
       CSMQBean.logger.info(userBean.getCaller() + " ** REQUERY **");
       CSMQBean.logger.info(userBean.getCaller() + " Iterator: DraftImpactVO1Iterator1");
       CSMQBean.logger.info(userBean.getCaller() + " activationGroup: " + impactAnalysisBean.getAllGroups());
       CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " +  newRootNode.getDictContentId());
       CSMQBean.logger.info(userBean.getCaller() + " maxLevels: " +  CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
       CSMQBean.logger.info(userBean.getCaller() + " sortKey: " +  impactAnalysisBean.getParamFutureSort());
       CSMQBean.logger.info(userBean.getCaller() + " showNonImpacted: " +  impactAnalysisBean.getParamFutureShowNonImpacted());
       CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath: " +  impactAnalysisBean.getParamFuturePrimaryOnly());
       CSMQBean.logger.info(userBean.getCaller() + " scopeFilter: " +  parentTermScope);
       
       CSMQBean.logger.info(userBean.getCaller() + " startLevel: " +  newRootNode.getLevel());
       vo.executeQuery();
       
       rows = dciterb.getRowSetIterator().enumerateRowsInRange();
       
       // skip the first row, since it is the parent
       rows.nextElement();
               
       parentNodesByLevel = new HashMap();
       populateTreeNodes(newRootNode);
       
       //newRootNode.setIcon(null); // get rid of the icon
       newRootNode.setIsExpanded(true); // prevent it from being called again
       newRootNode.setShowHasChildrenButton(false);
       
       RowKeySet rks = new RowKeySetTreeImpl(true);
       rks.setCollectionModel(treemodel);
       tree.setDisclosedRowKeys(rks);

       AdfFacesContext.getCurrentInstance().addPartialTarget(tree); 
       AdfFacesContext.getCurrentInstance().partialUpdateNotify(tree);
    }

    public void setHasScope(boolean hasScope) {
        this.hasScope = hasScope;
    }

    public boolean isHasScope() {
        return hasScope;
    }

    
}
