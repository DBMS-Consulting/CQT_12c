package com.dbms.csmq.view.impact;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.view.backing.impact.ImpactAnalysisBean;
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
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;
import oracle.jbo.domain.Number;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetTreeImpl;
import org.apache.myfaces.trinidad.model.TreeModel;


public class PreviousVerFutureImpactHierarchyBean extends Hierarchy {

    private TreeModel treemodel;
    private Enumeration rows;
    private HashMap parentNodesByLevel;
    private boolean editable;
    private boolean hasData = false;

    private RichSelectOneChoice dictionaryVersion;
    private RichSelectOneChoice levelList;
    private RichInputText term;
    private int numberOfTermsToBeDeleted;
    private boolean hasScope = false;
    protected GenericTreeNode rootCopy;


    public PreviousVerFutureImpactHierarchyBean() {
        System.out.println("START: PreviousVerFutureImpactHierarchyBean");
        System.out.println("END: PreviousVerFutureImpactHierarchyBean");
    }


    public void init(boolean hasScope) {
        this.hasScope = hasScope;
        parentNodesByLevel = new HashMap();
        if (root != null) {
            root.getChildren().clear();
        }

        createTree();
        List nodes = new ArrayList();
        nodes.add(root);
        rootCopy = root;
        treemodel = new ChildPropertyTreeModel(nodes, "children") {
                public boolean isContainer() {
                    if (getRowData() == null)
                        return false;
                    return ((GenericTreeNode)getRowData()).getChildCount() > 0;
                }
            };
    }

    public void rebuildTree(boolean isShowImpactedOnly) {
        System.out.println("START: PreviousVerFutureImpactHierarchyBean.rebuildTree() isShowImpactedOnly=" +
                           isShowImpactedOnly);
        if (isShowImpactedOnly) {
            /*
            root = copyNode(rootCopy);
            if(rootCopy.getChildren() != null && rootCopy.getChildren().size() > 0){
                int size = rootCopy.getChildren().size();
                List<GenericTreeNode> impactedNodes = null;
                GenericTreeNode childNode = null;
                GenericTreeNode copyNode = null;
                for (int i = 0; i < size; i++) {
                    childNode = (GenericTreeNode) rootCopy.getChildren().get(i);
                    impactedNodes = getImpactedNodes(childNode);
                    if(impactedNodes != null && impactedNodes.size() > 0){
                        copyNode = copyNode(childNode);
                        copyNode.getChildren().addAll(impactedNodes);
                        root.getChildren().add(copyNode);
                    }
                }
            }
            */
            root = copyNode(rootCopy);
            copyNBuildImpactTreeNode(rootCopy, root);
        } else {
            root = rootCopy;
        }
        List nodes = new ArrayList();
        nodes.add(root);
        treemodel = new ChildPropertyTreeModel(nodes, "children") {
                public boolean isContainer() {
                    if (getRowData() == null)
                        return false;
                    return ((GenericTreeNode)getRowData()).getChildCount() > 0;
                }
            };
        System.out.println("END: PreviousVerFutureImpactHierarchyBean.rebuildTree()");
    }

    public GenericTreeNode copyNBuildImpactTreeNode(GenericTreeNode node, GenericTreeNode uiDisplayNode) {
        if (node.getChildren() != null && node.getChildren().size() > 0) {
            int size = node.getChildren().size();
            GenericTreeNode childNode = null;
            GenericTreeNode uiDisplaychildNode = null;
            for (int i = 0; i < size; i++) {
                childNode = (GenericTreeNode)node.getChildren().get(i);
                if (!"0".equalsIgnoreCase(childNode.getIcon())) {
                    uiDisplaychildNode = copyNode(childNode);
                    copyNBuildImpactTreeNode(childNode, uiDisplaychildNode);
                    uiDisplayNode.getChildren().add(uiDisplaychildNode);
                }
            }
        }
        return uiDisplayNode;
    }

    //    private List<GenericTreeNode> getImpactedNodes(GenericTreeNode node){
    //        List<GenericTreeNode> impactedNodes = new ArrayList<GenericTreeNode>();
    //        if(node.getChildren() != null && node.getChildren().size() > 0){
    //            int size = node.getChildren().size();
    //            GenericTreeNode childNode = null;
    //            GenericTreeNode copyNode = null;
    //            for (int i = 0; i < size; i++) {
    //                childNode = (GenericTreeNode) node.getChildren().get(i);
    //                if(!"0".equalsIgnoreCase(childNode.getIcon()) ){
    //                    //copyNode = copyNode(childNode);
    //                    copyNode = copyNBuildImpactTreeNode(childNode);
    //                    impactedNodes.add(copyNode);
    //                }
    //            }
    //        }
    //        return impactedNodes;
    //    }

    private void createTree() {

        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("PreviousVerFutureImpactVO1Iterator");

        rows = dciterb.getRowSetIterator().enumerateRowsInRange();

        if (rows != null && !rows.hasMoreElements()) {
            return;
        }

        Row row = (Row)rows.nextElement();
        root = new GenericTreeNode();
        root.setIsRoot(true);
        root.setTerm(Utils.getAsString(row, "RootTerm"));
        root.setPrikey(Utils.getAsString(row, "RootDictContentId"));
        root.setParent(null);
        String rootLevelName = Utils.getAsString(row, "RootLevelName");
        root.setLevelName(rootLevelName);
        root.setQueryLevel(rootLevelName);
        Number level = null;
        String levelNumberStr = null;
        try {
            levelNumberStr = rootLevelName.substring(rootLevelName.length() - 1);
            level = new Number(levelNumberStr);
        } catch (Exception e) {
        }
        root.setLevel(level);
        root.setDictShortName(Utils.getAsString(row, "RootDictName"));
        root.setDictContentId(Utils.getAsString(row, "RootDictContentId"));
        root.setDictContentCode(Utils.getAsString(row, "RootDictContentCode"));
        root.setApprovedFlag("A");
        root.setDictContentAltCode(Utils.getAsString(row, "RootDictContentCode"));
        root.setStatus("A"); //TODO
        root.setPredictGroupId(null);
        root.setPath("0");
        root.setTermCategory(""); //TODO
        root.setTermLevel(levelNumberStr);
        root.setTermScope(null);
        root.setTermWeight(""); //TODO
        root.setPath(""); //TODO
        root.setFormattedScope(""); //TODO
        root.setHasScope(hasScope);

        if (root.getLevelName().contains(CSMQBean.NMQ)) {
            root.setMqType(CSMQBean.NMQ);
            root.setEditable(true);
            this.editable = true;
        } else if (root.getLevelName().contains(CSMQBean.CMQ)) {
            root.setMqType(CSMQBean.CMQ);
            root.setEditable(true);
            this.editable = true;
        } else {
            root.setMqType(CSMQBean.SMQ);
            root.setEditable(false);
            this.editable = false;
        }
        root.setScopeName("Full " + root.getMqType() + "/SMQ");

        //NEW FOR IMPACT
        String displayAttribute = Utils.getAsString(row, "RootNmatDisplayProperty");
        if (displayAttribute != null) {
            String code =
                displayAttribute.indexOf("_") > 0 ? displayAttribute.substring(displayAttribute.lastIndexOf("_") + 1) :
                "0";
            root.setDescription(displayAttribute);
            root.setStyle(displayAttribute); // THIS IS USED TO CALL THE CORRECT STYLE
            root.setIcon(code); // SET THE MATCHING ICON - IF IT'S NULL IT WON'T SHOW
        }

        root.setShowHasChildrenButton(false); //don't show it for the root
        parentNodesByLevel.put(root.getDictContentId(), root);
        CSMQBean.logger.info(userBean.getCaller() + " ADDING ROOT: " + root.toString() + ";EDITABLE=" + this.editable);

        populateTreeNode(row);
        while (rows.hasMoreElements()) {
            row = (Row)rows.nextElement();
            populateTreeNode(row);
        }
        hasData = true;
        hasScope = false;
        //clean up the hashmap
        parentNodesByLevel = null;

    }

    private void populateTreeNode(Row row) {
        //store node and level
        GenericTreeNode termNode = new GenericTreeNode();
        termNode.setTerm(Utils.getAsString(row, "DtlsTerm"));
        termNode.setPrikey(Utils.getAsString(row, "DtlsDictContentId"));
        termNode.setParent(Utils.getAsString(row, "MstrDictContentId"));
        termNode.setLevelName(Utils.getAsString(row, "DtlsLevelName"));
        //termNode.setLevel(Utils.getAsNumber(row, "Level"));
        termNode.setDictShortName(Utils.getAsString(row, "DtlsDictName"));
        termNode.setDictContentId(Utils.getAsString(row, "DtlsDictContentId"));
        termNode.setDictContentCode(Utils.getAsString(row, "DtlsDictContentCode"));
        termNode.setApprovedFlag("A");
        termNode.setDictContentAltCode(Utils.getAsString(row, "DtlsDictContentCode"));
        termNode.setStatus(Utils.getAsString(row, "RelRelationScope")); //TODO
        //termNode.setPredictGroupId(Utils.getAsNumber(row, "PredictGroupId"));
        termNode.setPath(Utils.getAsString(row, "RelDepthFromRoot"));
        termNode.setTermCategory(Utils.getAsString(row, "RelRelationCategory"));
        termNode.setTermLevel(Utils.getAsString(row, "DtlsLevelName"));
        termNode.setTermScope(Utils.getAsNumber(row, "RelRelationScope"));
        termNode.setTermWeight(Utils.getAsString(row, "RelRelationWeight"));
        termNode.setFormattedScope(Utils.getAsString(row, "RelRelationScope"));
        termNode.setHasScope(this.hasScope);
        termNode.setEditable(this.editable); // 4.APR.2014
        termNode.setFormattedScope(Utils.getAsString(row, "RelRelationScope"));

        GenericTreeNode parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());

        if (parentNode == null) {
            populateTreeNodeFromMaster(row);
            parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());
        }

        if (parentNode != null) {
            parentNode.getChildren().add(termNode); // add to the parent
            termNode.setParentNode(parentNode); // set the parent for the child
            if (parentNode.isIsRoot())
                termNode.setDeletable(true); //it's a child of the root - it can be deleted
            if (root.equals(parentNode))
                termNode.setIsDirectRelation(true); // it's a direct relation

            termNode.setMqType(parentNode.getMqType()); // set the query type the same as the parent
        }

        String displayAttribute = (String)row.getAttribute("GuiNmatDisplayProperty");
        if (displayAttribute != null) {
            String code =
                displayAttribute != null && displayAttribute.indexOf("_") > 0 ? displayAttribute.substring(displayAttribute.lastIndexOf("_") +
                                                                                                           1) : "0";
            termNode.setDescription(displayAttribute);
            termNode.setStyle(displayAttribute); // THIS IS USED TO CALL THE CORRECT STYLE
            termNode.setIcon(code); // SET THE MATCHING ICON - IF IT'S NULL IT WON'T SHOW
            //FILTER OUT THESE CODES
            //            if (code.equals(CSMQBean.DELETED_MERGED_MOVED_TERM_RELATION)) {
            //                CSMQBean.logger.info(userBean.getCaller() + " FUTURE: IGNORING " + termNode);
            //                termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
            //            }
        }
        //REMOVE LLTs FROM THTE ROOT
        if (parentNode != null && parentNode.isIsRoot() && termNode.getLevelName().equals("LLT")) {
            termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
            CSMQBean.logger.info(userBean.getCaller() + " REMOVING LLT " + termNode);
        }

        // FOR 'LAZY' LOADING
        //        boolean showMoreChildren = Utils.getAsBoolean(row, "ChildExists");
        //        if (showMoreChildren)
        //            termNode.setShowHasChildrenButton(true);

        setDerivedRelations(termNode);
        if (termNode.getFormattedScope().contains("BROAD")) {
            termNode.setScopeName("Broad");
        } else if (termNode.getFormattedScope().contains("NARROW")) {
            termNode.setScopeName("Narrow");
        }
        CSMQBean.logger.info(userBean.getCaller() + " FUTURE ADDING NODE: " + termNode);

        if (termNode != null)
            parentNodesByLevel.put(termNode.getDictContentId(), termNode);
    }

    private void populateTreeNodeFromMaster(Row row) {
        //store node and level
        GenericTreeNode termNode = new GenericTreeNode();
        termNode.setTerm(Utils.getAsString(row, "MstrTerm"));

        termNode.setPrikey(Utils.getAsString(row, "MstrDictContentId"));
        termNode.setParent(Utils.getAsString(row, "RootDictContentId"));
        termNode.setLevelName(Utils.getAsString(row, "MstrLevelName"));
        //termNode.setLevel(Utils.getAsNumber(row, "Level"));
        termNode.setDictShortName(Utils.getAsString(row, "MstrDictName"));
        termNode.setDictContentId(Utils.getAsString(row, "MstrDictContentId"));
        termNode.setDictContentCode(Utils.getAsString(row, "MstrDictContentCode"));
        termNode.setApprovedFlag("A");
        termNode.setDictContentAltCode(Utils.getAsString(row, "MstrDictContentCode"));
        termNode.setStatus(Utils.getAsString(row, "RelRelationScope")); //TODO
        //termNode.setPredictGroupId(Utils.getAsNumber(row, "PredictGroupId"));
        termNode.setPath(Utils.getAsString(row, "RelDepthFromRoot"));
        termNode.setTermCategory(Utils.getAsString(row, "RelRelationCategory"));
        termNode.setTermLevel(Utils.getAsString(row, "MstrLevelName"));
        termNode.setTermScope(Utils.getAsNumber(row, "RelRelationScope"));
        termNode.setTermWeight(Utils.getAsString(row, "RelRelationWeight"));
        termNode.setHasScope(this.hasScope);
        termNode.setEditable(this.editable); // 4.APR.2014
        termNode.setFormattedScope(Utils.getAsString(row, "RelRelationScope"));

        GenericTreeNode parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());

        if (parentNode != null) {
            parentNode.getChildren().add(termNode); // add to the parent
            termNode.setParentNode(parentNode); // set the parent for the child
            if (parentNode.isIsRoot())
                termNode.setDeletable(true); //it's a child of the root - it can be deleted
            if (root.equals(parentNode))
                termNode.setIsDirectRelation(true); // it's a direct relation

            termNode.setMqType(parentNode.getMqType()); // set the query type the same as the parent
        }
        String displayAttribute = (String)row.getAttribute("MstrNmatDisplayProperty");
        if (displayAttribute != null) {
            String code =
                displayAttribute != null && displayAttribute.indexOf("_") > 0 ? displayAttribute.substring(displayAttribute.lastIndexOf("_") +
                                                                                                           1) : "0";
            termNode.setDescription(displayAttribute);
            termNode.setStyle(displayAttribute); // THIS IS USED TO CALL THE CORRECT STYLE
            termNode.setIcon(code); // SET THE MATCHING ICON - IF IT'S NULL IT WON'T SHOW
            //FILTER OUT THESE CODES
            //            if (code.equals(CSMQBean.DELETED_MERGED_MOVED_TERM_RELATION)) {
            //                CSMQBean.logger.info(userBean.getCaller() + " FUTURE: IGNORING " + termNode);
            //                termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
            //            }
        }
        //REMOVE LLTs FROM THTE ROOT
        if (parentNode != null && parentNode.isIsRoot() && termNode.getLevelName().equals("LLT")) {
            termNode.getParentNode().getChildren().remove(termNode); //remove it from it's parent
            CSMQBean.logger.info(userBean.getCaller() + " REMOVING LLT " + termNode);
        }

        // FOR 'LAZY' LOADING
        //        boolean showMoreChildren = Utils.getAsBoolean(row, "ChildExists");
        //        if (showMoreChildren)
        //            termNode.setShowHasChildrenButton(true);

        setDerivedRelations(termNode);

        if (termNode.getFormattedScope().contains("BROAD")) {
            termNode.setScopeName("Broad");
        } else if (termNode.getFormattedScope().contains("NARROW")) {
            termNode.setScopeName("Narrow");
        }

        CSMQBean.logger.info(userBean.getCaller() + " CURRENT ADDING NODE: " + termNode);

        if (termNode != null)
            parentNodesByLevel.put(termNode.getDictContentId(), termNode);
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
        CSMQBean.logger.info("SORT LEVEL");
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
        ImpactAnalysisBean impactAnalysisBean =
            (ImpactAnalysisBean)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("ImpactAnalysisBean");
        RichTreeTable targetTree = impactAnalysisBean.getFutureTree();
        // Clear keys

        if (targetTree != null && targetTree.getDisclosedRowKeys() != null)
            targetTree.getDisclosedRowKeys().clear(); //to resolve NoRowAvailableException

        GenericTreeNode newRootNode = null;

        RichTreeTable tree = targetTree;
        RowKeySet droppedValue = targetTree.getSelectedRowKeys();

        Object[] keys = droppedValue.toArray();

        for (int i = 0; i < keys.length; i++) {
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

        if (newRootNode.isIsExpanded())
            return; // don't requery if already done

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

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setRootCopy(GenericTreeNode rootCopy) {
        this.rootCopy = rootCopy;
    }

    public GenericTreeNode getRootCopy() {
        return rootCopy;
    }
}
