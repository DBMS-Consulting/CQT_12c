package com.dbms.csmq.view.hierarchy;


import com.dbms.csmq.CSMQBean;

import java.util.ArrayList;
import java.util.List;

import oracle.jbo.domain.Number;


public class GenericTreeNode {

    public GenericTreeNode() {
        this.children = new ArrayList();
    }

    public GenericTreeNode(String term) {
        this.term = term;
        this.children = new ArrayList();
    }


    private List children;
    private boolean nodeSelected;
    private GenericTreeNode parentNode;
   
    private String term;            
    private String prikey;          
    private String parent;          
    private String levelName; 
    private String queryLevel;
    private Number level;           
    private String dictShortName;   
    private String dictContentId;
    private String dictContentCode;
    private String dictContentAltCode;
    private String approvedFlag;
    private String status;
    private Number predictGroupId;
    private String endTS;
    
    //TERMLVL, TERMSCP, TERMCAT, TERMWEIG
    
    private String termLevel; 
    private Number termScope;
    private String termCategory; 
    private String termWeight;

    private String formattedScope;
    private String scopeName;
    
    private boolean markedForDeletion;
    private boolean deletable = false;
    private boolean editable = false;
    private boolean isRoot = false;
    private boolean hasScope = false;
    private String style;
    private String mqType;
    private boolean rendered = true;
    private String description;
    private String icon;
    private String path;
    private boolean primaryLink = false;
    private String sortField;  // used for straight lists that are non-hierarchical
    // These are used when the tree is in 'lazy' mode
    private boolean isExpanded = false;
    //private boolean hasChildren = false;
    private boolean showHasChildrenButton = false;
    private boolean isDirectRelation = false;  
    private boolean isDirectRelationOrRoot = false; //added to fix EL bug
    
    
    public int getChildCount() {
        if (children == null)
            return 0;
        else
            return children.size();
    }


    public void setChildren(List children) {
        this.children = children;
    }

    public List getChildren() {
         return children;
    }

    public void setNodeSelected(boolean nodeSelected) {
        this.nodeSelected = nodeSelected;
    }

    public boolean isNodeSelected() {
        return nodeSelected;
    }


    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setPrikey(String prikey) {
        this.prikey = prikey;
    }

    public String getPrikey() {
        return prikey;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }
   
    public void setQueryLevel(String queryLevel) {
        this.queryLevel = queryLevel;
    }
    //AMC 7/18/14
    public String getQueryLevel() {
        return queryLevel;
    }

    public void setLevel(Number level) {
        this.level = level;
    }

    public Number getLevel() {
        return level;
    }

    public void setDictShortName(String dictShortName) {
        this.dictShortName = dictShortName;
    }

    public String getDictShortName() {
        return dictShortName;
    }

    public void setDictContentId(String dictContentId) {
        this.dictContentId = dictContentId;
    }

    public String getDictContentId() {
        return dictContentId;
    }
    
    public String toString () {
        String retVal = "   TERM=" +  this.term
        + ";LEVEL=" + this.level
        + ";LEVEL_NAME=" + this.levelName
        + ";PRIKEY=" + this.prikey
        + ";SCOPE=" + this.termScope
        + ";FORMATTED_SCOPE=" + this.formattedScope
        + ";SHOW_HAS_CHILDREN=" + this.showHasChildrenButton
        + ";DIRECT_RELATION=" + this.isDirectRelation
        + ";DIRECT_RELATION_OR_ROOT=" + this.isDirectRelationOrRoot
        + ";STYLE=" + this.style;
        return retVal;
    }

    public void setDictContentAltCode(String dictContentAltCode) {
        this.dictContentAltCode = dictContentAltCode;
    }

    public String getDictContentAltCode() {
        return dictContentAltCode;
    }

    public void setApprovedFlag(String approvedFlag) {
        this.approvedFlag = approvedFlag;
    }

    public String getApprovedFlag() {
        return approvedFlag;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setParentNode(GenericTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public GenericTreeNode getParentNode() {
        return parentNode;
    }

    public void setPredictGroupId(Number predictGroupId) {
        this.predictGroupId = predictGroupId;
    }

    public Number getPredictGroupId() {
        return predictGroupId;
    }

    public void setTermLevel(String termLevel) {
        this.termLevel = termLevel;
    }

    public String getTermLevel() {
        return termLevel;
    }

    public void setTermScope(Number termScope) {
        this.termScope = termScope;
    }

    public Number getTermScope() {
        return termScope;
    }

    public void setTermCategory(String termCategory) {
        this.termCategory = termCategory;
    }

    public String getTermCategory() {
        return termCategory;
    }

    public void setTermWeight(String termWeight) {
        this.termWeight = termWeight;
    }

    public String getTermWeight() {
        if (hasScope && (termWeight == null || termWeight.isEmpty()))
            termWeight = "0";
        return termWeight;
    }

    public void setFormattedScope(String formattedScope) {
        this.formattedScope = formattedScope;
    }

    public String getFormattedScope() {
        return formattedScope;
    }

    public String getScopeName() {
        if (formattedScope == null) return null;
        if(scopeName == null){
            if (formattedScope.equals("0")) scopeName = "Full " + CSMQBean.customMQName + "/SMQ";
            if (formattedScope.equals("1")) scopeName = "Broad";
            if (formattedScope.equals("2")) scopeName = "Narrow";
            if (formattedScope.equals("3")) scopeName = "Child/Narrow";
        }        
        return scopeName;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean isIsRoot() {
        return isRoot;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    public void setMqType(String mqType) {
        this.mqType = mqType;
    }

    public String getMqType() {
        return mqType;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setEndTS(String endTS) {
        this.endTS = endTS;
    }

    public String getEndTS() {
        if (this.endTS.equals("15AUG3501")) return "Current";
        return endTS;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPrimaryLink(boolean primaryLink) {
        this.primaryLink = primaryLink;
    }

    public boolean isPrimaryLink() {
        return primaryLink;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortField() {
        return sortField;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public boolean isIsExpanded() {
        return isExpanded;
    }

    /*
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }
    */

    public void setHasScope(boolean hasScope) {
        this.hasScope = hasScope;
    }

    public boolean isHasScope() {
        return hasScope;
    }

    public void setShowHasChildrenButton(boolean showHasChildrenButton) {
        this.showHasChildrenButton = showHasChildrenButton;
    }

    public boolean isShowHasChildrenButton() {
        return showHasChildrenButton;
    }

    public void setDictContentCode(String dictContentCode) {
        this.dictContentCode = dictContentCode;
    }

    public String getDictContentCode() {
        return dictContentCode;
    }

    public void setIsDirectRelation(boolean isDirectRelation) {
        this.isDirectRelation = isDirectRelation;
    }

    public boolean isIsDirectRelation() {
        return isDirectRelation;
    }
    
    public boolean equals(Object aThat) {
        if ( this == aThat ) return true;
        if ( !(aThat instanceof GenericTreeNode) ) return false;
        GenericTreeNode that = (GenericTreeNode)aThat;
        return (this.prikey == that.prikey);
    }


    public void setIsDirectRelationOrRoot(boolean isDirectRelationOrRoot) {
        this.isDirectRelationOrRoot = isDirectRelationOrRoot;
    }

    public boolean isIsDirectRelationOrRoot() {
        isDirectRelationOrRoot = (isDirectRelation || isRoot);
        return isDirectRelationOrRoot;
    }


    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }
}
