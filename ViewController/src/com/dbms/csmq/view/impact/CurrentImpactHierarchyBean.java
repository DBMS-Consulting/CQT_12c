package com.dbms.csmq.view.impact;


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

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;


public class CurrentImpactHierarchyBean extends Hierarchy {

    private TreeModel treemodel;
    //private GenericTreeNode root;
    private Enumeration rows;
    private HashMap parentNodesByLevel;
    private boolean editable = false;
    
    //Search terms used for term list box
    //TODO REMOVE 
    //private String dictionaryVersionSearchCriteria = "";
    //private String levelSearchCriteria = "";
    //private String termSearchCriteria = "";
    private RichSelectOneChoice dictionaryVersion;
    private RichSelectOneChoice levelList;
    private RichInputText term;
    private RichTreeTable targetTree;
    private RichTreeTable sourceTree;

    
    public CurrentImpactHierarchyBean() {
        CSMQBean.logger.info ("@ NEW CurrentImpactHierarchyBean()");        
        }
    
    public void init () { 
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
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("CurrentImpactVO1Iterator");

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
        root.setPath(Utils.getAsString(row,"TermPath"));
        root.setTermCategory(Utils.getAsString(row,"Termcat"));
        root.setTermLevel(Utils.getAsString(row, "Termlvl"));
        root.setTermScope(Utils.getAsNumber(row,"Termscp"));
        root.setTermWeight(Utils.getAsString(row,"Termweig"));
        
        if (root.getLevelName().contains (CSMQBean.NMQ)) {
            root.setMqType(CSMQBean.NMQ);
            root.setEditable(true);
            this.editable = true;
            }
        else {
            root.setMqType(CSMQBean.SMQ);
            root.setEditable(false);
            this.editable = false;
            }
        
        //NEW FOR IMPACT
        Object displayAttribute = row.getAttribute("DisplayAttribute");
        if (displayAttribute != null) {
            String code = Utils.getAsString(row,"DisplayAttribute");
            String cssClass = "Impact_" + root.getMqType() + "_" + code;
            root.setStyle(cssClass); // THIS IS USED TO CALL THE CORRECT STYLE
            root.setIcon(code); // SET THE MATCHING ICON - IF IT'S NULL IT WON'T SHOW
            }

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
            termNode.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
            termNode.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
            termNode.setStatus(Utils.getAsString(row,"Status"));
            termNode.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
            termNode.setPath(Utils.getAsString(row,"TermPath"));
            termNode.setTermCategory(Utils.getAsString(row,"Termcat"));
            termNode.setTermLevel(Utils.getAsString(row, "Termlvl"));
            termNode.setTermScope(Utils.getAsNumber(row,"Termscp"));
            termNode.setTermWeight(Utils.getAsString(row,"Termweig"));
            termNode.setFormattedScope(Utils.getAsString(row, "FormattedScope"));
           
            if (termNode.getLevelName().contains (CSMQBean.NMQ)) {
                termNode.setMqType(CSMQBean.NMQ);
                termNode.setEditable(true);
                this.editable = true;
                }
            else {
                termNode.setMqType(CSMQBean.SMQ);
                termNode.setEditable(false);
                this.editable = false;
                }
           
            
            termNode.setMqType(root.getMqType()); // set the query type the same as the parent
            Object displayAttribute = Utils.getAsString(row, "DisplayAttribute");
                        
            
            GenericTreeNode parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());
            parentNode.getChildren().add(termNode);  // add to the parent
            termNode.setParentNode(parentNode);      // set the parent for the child
            if (parentNode.isIsRoot()) termNode.setDeletable(true); //it's a child of the root - it can be deleted
       
            boolean added = true;
            if (displayAttribute != null) {
                String code = Utils.getAsString(row,"DisplayAttribute");
       
                String cssClass = "Impact_" + termNode.getMqType() + "_" + code;
                String description = CSMQBean.getProperty("Impact_" + termNode.getMqType() + "_" + code);
                termNode.setDescription(description);
                
                termNode.setStyle(cssClass); // THIS IS USED TO CALL THE CORRECT STYLE
                termNode.setIcon(code); // SET THE MATCHING ICON - IF IT'S NULL IT WON'T SHOW
                //FILTER OUT THESE CODES
                if (code.equals(CSMQBean.DELETED_MERGED_MOVED_TERM_RELATION)) {
                    CSMQBean.logger.info(userBean.getCaller() + " [CURRENT] IGNORING BY RULE: " + termNode);
                    termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
                    added = false;
                    } 
                }
            //REMOVE LLTs FROM THTE ROOT
            if (parentNode.isIsRoot() && termNode.getLevelName().equals("LLT")) {
                termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
                CSMQBean.logger.info(userBean.getCaller() + " [CURRENT] IGNROING LLT: " + termNode);
                added = false;
                }
                
            if(added)
                CSMQBean.logger.info(userBean.getCaller() + " [CURRENT] ADDING NODE: " + termNode);
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

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
}
