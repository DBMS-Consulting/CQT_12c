package com.dbms.csmq.view.hierarchy;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.view.backing.NMQ.NMQSourceTermSearchBean;

import com.dbms.csmq.view.backing.impact.ImpactAnalysisBean;
import com.dbms.util.Utils;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.event.ActionEvent;

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
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetTreeImpl;
import org.apache.myfaces.trinidad.model.TreeModel;


//import javax.faces.event.ActionEvent;


public class TermHierarchySourceBean extends Hierarchy implements Serializable {

    @SuppressWarnings("compatibility:4183707243586059543")
    private static final long serialVersionUID = 3177839331058851172L;

    private transient TreeModel treemodel;
    //private GenericTreeNode root;
    private transient Enumeration rows;
    private HashMap parentNodesByLevel;
    
    //Search terms used for term list box
    private String dictionaryVersionSearchCriteria = "";
    private String levelSearchCriteria = "";
    private String termSearchCriteria = "";
    private transient RichSelectOneChoice dictionaryVersion;
    private transient RichSelectOneChoice levelList;
    private transient RichInputText term;
    private transient RichTreeTable targetTree;
    private transient RichTreeTable sourceTree;
    private boolean hasScope = false;
    


    public TermHierarchySourceBean() {
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
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SourceTreeVO1Iterator");

        rows = dciterb.getRowSetIterator().enumerateRowsInRange();
        if (rows == null || !rows.hasMoreElements()) return;
        
        Row row = (Row)rows.nextElement();

        root = new GenericTreeNode();
        root.setIsRoot(true);
        root.setTerm(Utils.getAsString(row,"Term"));
        root.setPrikey(Utils.getAsString(row,"Prikey"));
        ///// ?????????????????????????????????????????????????????????????????????????????
        root.setParent(Utils.getAsString(row,"Parent"));
        //root.setLevelName(Utils.getAsString(row,"LevelName"));
        root.setLevelName(resolveTermLevel(Utils.getAsString(row,"LevelName")));
        root.setQueryLevel(Utils.getAsString(row,"LevelName"));
        root.setLevel(Utils.getAsNumber(row,"Level"));
        root.setDictShortName(Utils.getAsString(row,"DictShortName"));
        root.setDictContentId(Utils.getAsString(row,"DictContentId"));
        root.setDictContentCode(Utils.getAsString(row,"DictContentCode"));
        root.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
        root.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
        root.setStatus(Utils.getAsString(row,"Status"));
        root.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
        root.setTermCategory(Utils.getAsString(row,"Termcat"));
        root.setTermLevel(Utils.getAsString(row,"Termlvl"));
        root.setTermScope(Utils.getAsNumber(row,"Termscp"));
        root.setTermWeight(Utils.getAsString(row,"Termweig"));
        root.setFormattedScope(Utils.getAsString(row,"FormattedScope"));  
        root.setPrimaryLink(Utils.getAsBoolean(row,"PrimLinkFlag"));
        CSMQBean.logger.info(userBean.getCaller() + " ADDING ROOT: " + root);
        
        populateTreeNodes(root);
        //clean up the hashmap
        parentNodesByLevel = null;

    }

    private GenericTreeNode populateTreeNodes(GenericTreeNode node) {

        //store node and level
        if (parentNodesByLevel == null) return null;
        parentNodesByLevel.put(node.getDictContentId(), node);

        if (rows.hasMoreElements()) {
            Row row = (Row)rows.nextElement();
            GenericTreeNode termNode = new GenericTreeNode();
            termNode.setTerm(Utils.getAsString(row,"Term"));
            termNode.setPrikey(Utils.getAsString(row,"Prikey"));
            termNode.setParent(Utils.getAsString(row,"Parent"));
            //termNode.setLevelName(Utils.getAsString(row,"LevelName"));
            termNode.setLevelName(resolveTermLevel(Utils.getAsString(row,"LevelName")));
            termNode.setQueryLevel(Utils.getAsString(row,"LevelName"));
            termNode.setLevel(Utils.getAsNumber(row,"Level"));
            termNode.setDictShortName(Utils.getAsString(row,"DictShortName"));
            termNode.setDictContentId(Utils.getAsString(row,"DictContentId"));
            termNode.setDictContentCode(Utils.getAsString(row,"DictContentCode"));
            termNode.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
            termNode.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
            termNode.setStatus(Utils.getAsString(row,"Status"));
            termNode.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
            termNode.setPath(Utils.getAsString(row,"TermPath"));
            termNode.setPrimaryLink(Utils.getAsBoolean(row,"PrimLinkFlag"));
            
            if (!termNode.isPrimaryLink()) termNode.setStyle("SECONDARY_LINK");
            
            //String hasChildren = Utils.getAsString(row,"ChildExists");
            //boolean showMoreChildren = hasChildren.equals("Y") ? true : false;
            boolean showMoreChildren = Utils.getAsBoolean(row,"ChildExists");
                if (showMoreChildren) {
                    //termNode.setIcon(CSMQBean.getProperty("HAS_CHILDREN_ICON"));
                    termNode.setShowHasChildrenButton(true);
                    }
            
            termNode.setTermCategory(Utils.getAsString(row,"Termcat"));
            termNode.setTermLevel(Utils.getAsString(row,"Termlvl"));
            termNode.setTermScope(Utils.getAsNumber(row,"Termscp"));
            termNode.setTermWeight(Utils.getAsString(row,"Termweig"));
            
            if (!hasScope) {
                termNode.setFormattedScope(null);
                }
            else {
                if (Utils.getAsString(row,"FormattedScope") != null)
                    termNode.setFormattedScope(Utils.getAsString(row,"FormattedScope"));
                }
            
            GenericTreeNode parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());
            parentNode.getChildren().add(termNode);  // add to the parent
            termNode.setParentNode(parentNode);      // set the parent for the child
            
            CSMQBean.logger.info(userBean.getCaller() + " ADDING NODE: " + termNode);
            if (hasScope) setDerivedRelations(termNode); // if the MQ has scope, set the derived relations
            populateTreeNodes(termNode);
            
        }
        return node;
    }

     

    private void showChildren() {
                
        GenericTreeNode newRootNode = null;
        NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
        TermHierarchyBean termHierarchyBean = (TermHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
        
        RichTreeTable tree = termHierarchyBean.getSourceTree();
        RowKeySet droppedValue = tree.getSelectedRowKeys();

        Object[] keys = droppedValue.toArray();
        
        for (int i = 0; i <keys.length; i++) {
            List list = (List)keys[i];
        
            int depth = list.size();
            //int rootKey = Integer.parseInt(list.get(0).toString());
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
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SourceTreeVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        String parentTermScope = "0";
        if (newRootNode != null)
            parentTermScope = newRootNode.getParentNode().getFormattedScope();
        vo.setNamedWhereClauseParam("dictShortName", nMQSourceTermSearchBean.getParamDictionary());
        vo.setNamedWhereClauseParam("dictContentID", newRootNode.getDictContentId());
        vo.setNamedWhereClauseParam("scopeFilter", parentTermScope);
        vo.setNamedWhereClauseParam("sortKey", nMQSourceTermSearchBean.getParamSort());          
        vo.setNamedWhereClauseParam("returnPrimLinkPath", nMQSourceTermSearchBean.getParamPrimLinkFlag());
        vo.setNamedWhereClauseParam("maxLevels", CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        vo.setNamedWhereClauseParam("narrowScopeOnly", nMQSourceTermSearchBean.getParamNarrowScopeOnly());
          
        CSMQBean.logger.info(userBean.getCaller() + " ** REQUERY **");
        CSMQBean.logger.info(userBean.getCaller() + " dictShortName: " +  nMQSourceTermSearchBean.getParamDictionary());
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " +  newRootNode.getDictContentId());
        CSMQBean.logger.info(userBean.getCaller() + " scopeFilter: " +  parentTermScope);
        CSMQBean.logger.info(userBean.getCaller() + " sortKey: " +  nMQSourceTermSearchBean.getParamSort());          
        CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath: " + nMQSourceTermSearchBean.getParamPrimLinkFlag());
        CSMQBean.logger.info(userBean.getCaller() + " maxLevels: " +  CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        CSMQBean.logger.info(userBean.getCaller() + " narrowScopeOnly: " + nMQSourceTermSearchBean.getParamNarrowScopeOnly());
        
        vo.executeQuery();
        
        rows = dciterb.getRowSetIterator().enumerateRowsInRange();
        
        // skip the first row, since it is the parent
        rows.nextElement();
                
        parentNodesByLevel = new HashMap();
        populateTreeNodes(newRootNode);
        
        newRootNode.setIcon(null); // get rid of the icon
        newRootNode.setIsExpanded(true); // prevent it from being called again
        
        RowKeySet rks = new RowKeySetTreeImpl(true);
        rks.setCollectionModel(treemodel);
        tree.setDisclosedRowKeys(rks);

        AdfFacesContext.getCurrentInstance().addPartialTarget(tree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(tree);
    }


    public void setTreemodel(TreeModel treemodel) {
        this.treemodel = treemodel;
    }

    public TreeModel getTreemodel() {
        return treemodel;
    }

    public String getDictionaryVersionSearchCriteria() {
        dictionaryVersionSearchCriteria = String.valueOf(getDictionaryVersion().getValue()).trim();
        return dictionaryVersionSearchCriteria;
        }

    public String getLevelSearchCriteria() {
        levelSearchCriteria = String.valueOf(getLevelList().getValue()).trim();
        return levelSearchCriteria;
    }

    public String getTermSearchCriteria() {
        termSearchCriteria = String.valueOf(getTerm().getValue()).trim();
        return termSearchCriteria;
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


    public void setHasScope(boolean hasScope) {
        this.hasScope = hasScope;
    }

    public boolean isHasScope() {
        return hasScope;
    }
    
    public void expandChildren(ActionEvent actionEvent) {
        showChildren();
    }
    
    public void refresh(ActionEvent actionEvent) {
        refresh (actionEvent, root.getDictContentId());
        }
    
    public void refresh(ActionEvent actionEvent, String rootNode) {
        
        // Clear keys
        if (targetTree != null && targetTree.getDisclosedRowKeys()!=null )
            targetTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        
        if (sourceTree != null && sourceTree.getDisclosedRowKeys()!=null )
            sourceTree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        
        // REQUERY 
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SourceTreeVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
        vo.setNamedWhereClauseParam("dictShortName", nMQSourceTermSearchBean.getParamDictionary());
        vo.setNamedWhereClauseParam("dictContentID", rootNode);
        vo.setNamedWhereClauseParam("scopeFilter", nMQSourceTermSearchBean.getParamScope());
        vo.setNamedWhereClauseParam("sortKey", nMQSourceTermSearchBean.getParamSort());          
        vo.setNamedWhereClauseParam("returnPrimLinkPath", nMQSourceTermSearchBean.getParamPrimLinkFlag());
        vo.setNamedWhereClauseParam("maxLevels", CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        vo.setNamedWhereClauseParam("narrowScopeOnly", nMQSourceTermSearchBean.getParamNarrowScopeOnly());
        
        
        vo.executeQuery();
        init(this.hasScope);
        AdfFacesContext.getCurrentInstance().addPartialTarget(targetTree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(targetTree);
        }
    
    public void clear () {
        refresh (null, "0");
        }
    
    private String resolveTermLevel (String level) {

        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(level);
        if (matcher.find())
            return (matcher.group());
        return level;
    }
    public void expandHierarchyChildren(ActionEvent actionEvent) {
        showChildrenInHierarchySearch();
    }
    private void showChildrenInHierarchySearch() {
              
        GenericTreeNode newRootNode = null;
        NMQSourceTermSearchBean nMQSourceTermSearchBean = (NMQSourceTermSearchBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("NMQSourceTermSearchBean");
       // TermHierarchyBean termHierarchyBean = (TermHierarchyBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("TermHierarchyBean");
          
        RichTreeTable tree = nMQSourceTermSearchBean.getMultiHierarchySourceTree();
        if (nMQSourceTermSearchBean.getCurrentMode() == CSMQBean.MODE_IMPACT_ASSESSMENT){
            ImpactAnalysisBean impAnalysisBean = (ImpactAnalysisBean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("ImpactAnalysisBean");
            if (null != impAnalysisBean){
                tree = impAnalysisBean.getHierarchySourceTree();
            }
        }
        clearKeys(tree);
        RowKeySet droppedValue = tree.getSelectedRowKeys();

        Object[] keys = droppedValue.toArray();
        Object oldRowKey = tree.getRowKey();
        try{
            
        
        for (int i = 0; i <keys.length; i++) {
            List list = (List)keys[i];
        
            int depth = list.size();
            //int rootKey = Integer.parseInt(list.get(0).toString());
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
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("SourceTreeVO1Iterator");
        ViewObject vo = dciterb.getViewObject();
        String parentTermScope = "0";
        if (newRootNode != null)
            parentTermScope = newRootNode.getParentNode().getFormattedScope();
        vo.setNamedWhereClauseParam("dictShortName", nMQSourceTermSearchBean.getParamDictionary());
        vo.setNamedWhereClauseParam("dictContentID", newRootNode.getDictContentId());
        vo.setNamedWhereClauseParam("scopeFilter", parentTermScope);
        vo.setNamedWhereClauseParam("sortKey", nMQSourceTermSearchBean.getParamSort());          
        vo.setNamedWhereClauseParam("returnPrimLinkPath", nMQSourceTermSearchBean.getParamPrimLinkFlag());
        vo.setNamedWhereClauseParam("maxLevels", CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        vo.setNamedWhereClauseParam("narrowScopeOnly", nMQSourceTermSearchBean.getParamNarrowScopeOnly());
        
        CSMQBean.logger.info(userBean.getCaller() + " ** REQUERY **");
        CSMQBean.logger.info(userBean.getCaller() + " **showChildrenInHierarchySearch... REQUERY **");
        CSMQBean.logger.info(userBean.getCaller() + " dictShortName: " +  nMQSourceTermSearchBean.getParamDictionary());
        CSMQBean.logger.info(userBean.getCaller() + " dictContentID: " +  newRootNode.getDictContentId());
        CSMQBean.logger.info(userBean.getCaller() + " scopeFilter: " +  parentTermScope);
        CSMQBean.logger.info(userBean.getCaller() + " sortKey: " +  nMQSourceTermSearchBean.getParamSort());          
        CSMQBean.logger.info(userBean.getCaller() + " returnPrimLinkPath: " + nMQSourceTermSearchBean.getParamPrimLinkFlag());
        CSMQBean.logger.info(userBean.getCaller() + " maxLevels: " +  CSMQBean.getProperty("HIERARCHY_SUBSEQUENT_FETCH"));
        CSMQBean.logger.info(userBean.getCaller() + " narrowScopeOnly: " + nMQSourceTermSearchBean.getParamNarrowScopeOnly());
        
        
        vo.executeQuery();
        
        rows = dciterb.getRowSetIterator().enumerateRowsInRange();
        
        // skip the first row, since it is the parent
        rows.nextElement();
                
        parentNodesByLevel = new HashMap();
        populateTreeNodesInHierarchy(newRootNode);
        
        newRootNode.setIcon(null); // get rid of the icon
        newRootNode.setIsExpanded(true); // prevent it from being called again
        
        RowKeySet rks = new RowKeySetTreeImpl(true);
        rks.setCollectionModel(treemodel);
        tree.setDisclosedRowKeys(rks);
        }finally{
               //Restore the original rowKey
                tree.setRowKey(oldRowKey);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(tree); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(tree);
    }
    private GenericTreeNode populateTreeNodesInHierarchy(GenericTreeNode node) {

        //store node and level
        if (parentNodesByLevel == null) return null;
        parentNodesByLevel.put(node.getDictContentId(), node);

        if (rows.hasMoreElements()) {
            Row row = (Row)rows.nextElement();
            GenericTreeNode termNode = new GenericTreeNode();
            termNode.setTerm(Utils.getAsString(row,"Term"));
            termNode.setPrikey(Utils.getAsString(row,"Prikey"));
            termNode.setParent(Utils.getAsString(row,"Parent"));
            //termNode.setLevelName(Utils.getAsString(row,"LevelName"));
            termNode.setLevelName(resolveTermLevel(Utils.getAsString(row,"LevelName")));
            termNode.setQueryLevel(Utils.getAsString(row,"LevelName"));
            termNode.setLevel(Utils.getAsNumber(row,"Level"));
            termNode.setDictShortName(Utils.getAsString(row,"DictShortName"));
            termNode.setDictContentId(Utils.getAsString(row,"DictContentId"));
            termNode.setDictContentCode(Utils.getAsString(row,"DictContentCode"));
            termNode.setApprovedFlag(Utils.getAsString(row,"ApprovedFlag"));
            termNode.setDictContentAltCode(Utils.getAsString(row,"DictContentAltCode"));
            termNode.setStatus(Utils.getAsString(row,"Status"));
            termNode.setPredictGroupId(Utils.getAsNumber(row,"PredictGroupId"));
            termNode.setPath(Utils.getAsString(row,"TermPath"));
            termNode.setPrimaryLink(Utils.getAsBoolean(row,"PrimLinkFlag"));
            
            if (!termNode.isPrimaryLink()) termNode.setStyle("SECONDARY_LINK");
            
            //String hasChildren = Utils.getAsString(row,"ChildExists");
            //boolean showMoreChildren = hasChildren.equals("Y") ? true : false;
            boolean showMoreChildren = Utils.getAsBoolean(row,"ChildExists");
                if (showMoreChildren) {
                    //termNode.setIcon(CSMQBean.getProperty("HAS_CHILDREN_ICON"));
                    termNode.setShowHasChildrenButton(true);
                    }
            termNode.setTermCategory(Utils.getAsString(row,"Termcat"));
            termNode.setTermLevel(Utils.getAsString(row,"Termlvl"));
            termNode.setTermScope(Utils.getAsNumber(row,"Termscp"));
            termNode.setTermWeight(Utils.getAsString(row,"Termweig"));
            
            if (!hasScope) {
                termNode.setFormattedScope(null);
                }
            else {
                if (Utils.getAsString(row,"FormattedScope") != null)
                    termNode.setFormattedScope(Utils.getAsString(row,"FormattedScope"));
                }
            
            GenericTreeNode parentNode = (GenericTreeNode)parentNodesByLevel.get(termNode.getParent());
            String levelName = termNode.getLevelName();
            if (null != levelName && !levelName.equalsIgnoreCase(CSMQBean.LLT)){
                parentNode.getChildren().add(termNode);  // add to the parent
                termNode.setParentNode(parentNode);      // set the parent for the child
            }
            CSMQBean.logger.info(userBean.getCaller() + " ADDING NODE: " + termNode);
            if (hasScope) setDerivedRelations(termNode); // if the MQ has scope, set the derived relations
            populateTreeNodesInHierarchy(termNode);
        }
        return node;
    }

    
}
