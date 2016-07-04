package com.dbms.csmq.view.backing.search;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.view.hierarchy.GenericTreeNode;
import com.dbms.csmq.view.hierarchy.Hierarchy;

import com.dbms.util.Utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;


//import javax.faces.event.ActionEvent;


public class BrowseAndSearchHierarchyBean extends Hierarchy {

    private TreeModel treemodel;
    private GenericTreeNode root;
    private Enumeration rows;
    private HashMap parentNodesByLevel;
    
    
    //Search terms used for term list box
    private String dictionaryVersionSearchCriteria = "";
    private String levelSearchCriteria = "";
    private String termSearchCriteria = "";
    private RichSelectOneChoice dictionaryVersion;
    private RichSelectOneChoice levelList;
    private RichInputText term;
    private RichTreeTable targetTree;
    private RichTreeTable sourceTree;
    private String currentDictionary;
    //private int numberOfTermsToBeDeleted;
    private boolean editable = false;
   /*  public static final int PARAM_LENGTH = 7;
    public static final int VAL_PREDICT_GROUP_ID = 0;
    public static final int VAL_DICT_CONTENT_ID = 1;   
    public static final int VAL_TERMSCP = 2;
    public static final int VAL_TERMCAT = 3;
    public static final int VAL_TERMLVL = 4;
    public static final int VAL_TERMWEIG = 5;
    public static final int VAL_DML = 6; */
    
    BrowseAndSearchBean browseAndSearchBean;

    public BrowseAndSearchHierarchyBean() {
        super();
        }
    
    
    public void init () {
        parentNodesByLevel = new HashMap();
        browseAndSearchBean = (BrowseAndSearchBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("BrowseAndSearchBean");

        CSMQBean.logger.info(userBean.getCaller() + " CREATING BROWSE AND SEARCH TREE");
        CSMQBean.logger.info(userBean.getCaller() + " Iterator: HierarchyViewObj1Iterator1");
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HierarchyViewObj1Iterator1");
        ViewObject smallTreeVO = dciterb.getViewObject();
        
        smallTreeVO.setNamedWhereClauseParam("activationGroup", browseAndSearchBean.getCurrentReleaseGroup());
        smallTreeVO.setNamedWhereClauseParam("dictContentID", browseAndSearchBean.getCurrentDictContentID());
        smallTreeVO.setNamedWhereClauseParam("asOfDate", browseAndSearchBean.getHistoricalAsOfDate());
        smallTreeVO.setNamedWhereClauseParam("returnPrimLinkPath", browseAndSearchBean.getDisplaySecondaryPath());

        CSMQBean.logger.info(userBean.getCaller() + " activationGroup: "  + browseAndSearchBean.getCurrentReleaseGroup());
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " + browseAndSearchBean.getCurrentDictContentID());
        CSMQBean.logger.info(userBean.getCaller() + " asOfDate: " + browseAndSearchBean.getHistoricalAsOfDate());
        CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath: " + browseAndSearchBean.getDisplaySecondaryPath());
        
        smallTreeVO.executeQuery();

        this.currentDictionary = browseAndSearchBean.getCurrentDictionary();
        
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
        
        this.editable = false;  // THIS BROWSE ONLY
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HierarchyViewObj1Iterator1");

        rows = dciterb.getRowSetIterator().enumerateRowsInRange();
        Row row = (Row)rows.nextElement();

        root = new GenericTreeNode();
        root.setIsRoot(true);
        root.setTerm(Utils.getAsString(row,"Term"));
        root.setPrikey(Utils.getAsString(row,"Prikey"));
        root.setParent(Utils.getAsString(row,"Parent"));
        root.setLevelName(Utils.getAsString(row,"LevelName"));
        root.setLevel(Utils.getAsNumber(row,"Level"));
        root.setDictShortName(Utils.getAsString(row,"DictShortName"));
        root.setDictContentId(Utils.getAsString(row,"DictContentId"));
        root.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
        root.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
        root.setStatus(Utils.getAsString(row,"Status"));
        root.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
        root.setTermCategory(Utils.getAsString(row,"Termcat"));
        root.setTermLevel(Utils.getAsString(row, "Termlvl"));
        root.setTermScope(Utils.getAsNumber(row,"Termscp"));
        root.setTermWeight(Utils.getAsString(row,"Termweig"));
        root.setEndTS(Utils.getAsString(row,"CEndTs"));


        root.setPrimaryLink(Utils.getAsBoolean(row,"PrimLinkFlag"));

        CSMQBean.logger.info(userBean.getCaller() + " [BROWSE AND SEARCH] ADDING NODE: " + root);

        populateTreeNodes(root);
        //clean up the hashmap
        parentNodesByLevel = null;
    }

    private GenericTreeNode populateTreeNodes(GenericTreeNode node) {

        if (parentNodesByLevel == null) return null;
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
            termNode.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
            termNode.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
            termNode.setStatus(Utils.getAsString(row,"Status"));
            termNode.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
            termNode.setTermCategory(Utils.getAsString(row,"Termcat"));
            termNode.setTermLevel(Utils.getAsString (row, "Termlvl"));
            termNode.setTermScope(Utils.getAsNumber(row,"Termscp"));
            termNode.setTermWeight(Utils.getAsString(row,"Termweig"));
            termNode.setFormattedScope(Utils.getAsString(row,"FormattedScope"));
            termNode.setEndTS(Utils.getAsString(row, "CEndTs"));
            termNode.setPrimaryLink(Utils.getAsBoolean(row, "PrimLinkFlag"));
            
            if (!termNode.isPrimaryLink()) termNode.setStyle("SECONDARY_LINK");
            
            
            GenericTreeNode parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());
            parentNode.getChildren().add(termNode);  // add to the parent
            termNode.setParentNode(parentNode);      // set the parent for the child
            if (parentNode.isIsRoot()) termNode.setDeletable(true); //it's a child of the root - it can be deleted
            
            boolean added = true;
            //REMOVE LLTs FROM THTE ROOT
            if (parentNode.isIsRoot() && termNode.getLevelName().equals("LLT")) {
                termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
                CSMQBean.logger.info(userBean.getCaller() + " [BROWSE AND SEARCH] IGNORING LLT: " + termNode);
                added = false;
                }
            if (added)
                CSMQBean.logger.info(userBean.getCaller() + " [BROWSE AND SEARCH] ADDING NODE: " + termNode);
            
            setDerivedRelations(termNode);
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

    public void setTargetTree(RichTreeTable targetTree) {
        this.targetTree = targetTree;
    }

    public RichTreeTable getTargetTree() {
        return targetTree;
    }

    public void setSourceTree(RichTreeTable sourceTree) {
        this.sourceTree = sourceTree;
    }

    public RichTreeTable getSourceTree() {
        return sourceTree;
    }

    public void setCurrentDictionary(String currentDictionary) {
        this.currentDictionary = currentDictionary;
    }

    public String getCurrentDictionary() {
        return currentDictionary;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
}
