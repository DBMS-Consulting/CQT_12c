/** 
 *  Superclass for all hierarchical classes 
 *
 * @author Tom Struzik
 * @version 1.0 Sep 23, 2011
 */

package com.dbms.csmq.view.hierarchy;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;


public abstract class Hierarchy {
    
    protected String defaultMedDRAGroupName = CSMQBean.getProperty("DEFAULT_MEDDRA_RELEASE_GROUP");
    protected String defaultDraftGroupName = CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP");
    protected String defaultReleaseGroupName = CSMQBean.getProperty("DEFAULT_PUBLISH_RELEASE_GROUP");
    protected UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
    protected CSMQBean applicationBean = (CSMQBean)ADFContext.getCurrent().getApplicationScope().get("CSMQBean");
    protected int nodesFetched;
    protected GenericTreeNode root;
    
    // DEFAULT ATTRIBUTES
   
    public static final String DIRECT_SMQ_NMQ_TERMLVL = "0";
    public static final String DIRECT_SMQ_NMQ_TERMSCP = "2";
    public static final String DIRECT_SMQ_NMQ_TERMCAT = "S";
    public static final String DIRECT_SMQ_NMQ_TERMWEIG = "0";

    public static final String DIRECT_SOC_TERMLVL = "1";
    public static final String DIRECT_SOC_TERMSCP = "2";
    public static final String DIRECT_SOC_TERMCAT = "S";
    public static final String DIRECT_SOC_TERMWEIG = "0";

    public static final String DIRECT_HLGT_TERMLVL = "2";
    public static final String DIRECT_HLGT_TERMSCP = "2";
    public static final String DIRECT_HLGT_TERMCAT = "S";
    public static final String DIRECT_HLGT_TERMWEIG = "0";

    public static final String DIRECT_HLT_TERMLVL = "3";
    public static final String DIRECT_HLT_TERMSCP = "2";
    public static final String DIRECT_HLT_TERMCAT = "S";
    public static final String DIRECT_HLT_TERMWEIG = "0";

    public static final String DIRECT_PT_TERMLVL = "4";
    public static final String DIRECT_PT_TERMSCP = "2";
    public static final String DIRECT_PT_TERMCAT = "A";
    public static final String DIRECT_PT_TERMWEIG = "0";
  
    // DERIVED ATTRIBUTES

    public static final String DERIVED_HLGT_TERMLVL = "2";
    public static final String DERIVED_HLGT_TERMCAT = "S";

    public static final String DERIVED_HLT_TERMLVL = "3";
    public static final String DERIVED_HLT_TERMCAT = "S";

    public static final String DERIVED_PT_TERMLVL = "4";
    public static final String DERIVED_PT_TERMCAT = "A";

    public static final String DERIVED_LLT_TERMLVL = "5";

    
    
    
    
    
    protected void setDerivedRelations(GenericTreeNode genericTreeNode) {
        
        GenericTreeNode parent = genericTreeNode.getParentNode();
        if (parent == null || parent.isIsRoot()) return;  // we only do this for grandchildren or greater
        
        if (parent.getLevelName().equals(CSMQBean.SOC)) {
            if (genericTreeNode.getLevelName().equals(CSMQBean.HLGT)) {
                genericTreeNode.setTermLevel(DERIVED_HLGT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(DERIVED_HLGT_TERMCAT);
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            if (genericTreeNode.getLevelName().equals(CSMQBean.HLT)) {
                genericTreeNode.setTermLevel(DERIVED_HLT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(DERIVED_HLT_TERMCAT);
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            if (genericTreeNode.getLevelName().equals(CSMQBean.PT)) {
                genericTreeNode.setTermLevel(DERIVED_PT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(DERIVED_PT_TERMCAT);
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            if (genericTreeNode.getLevelName().equals(CSMQBean.LLT)) {
                genericTreeNode.setTermLevel(DERIVED_LLT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(parent.getTermCategory());
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            }
        if (parent.getLevelName().equals(CSMQBean.HLGT)) {
            if (genericTreeNode.getLevelName().equals(CSMQBean.HLT)) {
                genericTreeNode.setTermLevel(DERIVED_HLT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(DERIVED_HLT_TERMCAT);
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            if (genericTreeNode.getLevelName().equals(CSMQBean.PT)) {
                genericTreeNode.setTermLevel(DERIVED_PT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(DERIVED_PT_TERMCAT);
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            if (genericTreeNode.getLevelName().equals(CSMQBean.LLT)) {
                genericTreeNode.setTermLevel(DERIVED_LLT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(parent.getTermCategory());
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            }
        if (parent.getLevelName().equals(CSMQBean.HLT)) {
            if (genericTreeNode.getLevelName().equals(CSMQBean.PT)) {
                genericTreeNode.setTermLevel(DERIVED_PT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(DERIVED_PT_TERMCAT);
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            if (genericTreeNode.getLevelName().equals(CSMQBean.LLT)) {
                genericTreeNode.setTermLevel(DERIVED_LLT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(parent.getTermCategory());
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            }
        if (parent.getLevelName().equals(CSMQBean.PT)) {
            if (genericTreeNode.getLevelName().equals(CSMQBean.LLT)) {
                genericTreeNode.setTermLevel(DERIVED_LLT_TERMLVL);
                genericTreeNode.setFormattedScope(parent.getFormattedScope());
                genericTreeNode.setTermCategory(parent.getTermCategory());
                genericTreeNode.setTermWeight(parent.getTermWeight());
                }
            }
        }
        
        protected void setDefaultRelations(GenericTreeNode genericTreeNode) {

           // DIRECT RELATIONS
           //if (genericTreeNode.getLevelName().equals(CSMQBean.NMQ) || genericTreeNode.getLevelName().equals(CSMQBean.SMQ)) {
           if (genericTreeNode.getLevelName().indexOf(CSMQBean.SMQ) > -1) {
               genericTreeNode.setTermLevel(DIRECT_SMQ_NMQ_TERMLVL);
               genericTreeNode.setFormattedScope(DIRECT_SMQ_NMQ_TERMSCP);
               genericTreeNode.setTermCategory(DIRECT_SMQ_NMQ_TERMCAT);
               genericTreeNode.setTermWeight(DIRECT_SMQ_NMQ_TERMWEIG);
           }
           if (genericTreeNode.getLevelName().equals(CSMQBean.SOC)) {
               genericTreeNode.setTermLevel(DIRECT_SOC_TERMLVL);
               genericTreeNode.setFormattedScope(DIRECT_SOC_TERMSCP);
               genericTreeNode.setTermCategory(DIRECT_SOC_TERMCAT);
               genericTreeNode.setTermWeight(DIRECT_SOC_TERMWEIG);
           }
           if (genericTreeNode.getLevelName().equals(CSMQBean.HLGT)) {
               genericTreeNode.setTermLevel(DIRECT_HLGT_TERMLVL);
               genericTreeNode.setFormattedScope(DIRECT_HLGT_TERMSCP);
               genericTreeNode.setTermCategory(DIRECT_HLGT_TERMCAT);
               genericTreeNode.setTermWeight(DIRECT_HLGT_TERMWEIG);
           }
           if (genericTreeNode.getLevelName().equals(CSMQBean.HLT)) {
               genericTreeNode.setTermLevel(DIRECT_HLT_TERMLVL);
               genericTreeNode.setFormattedScope(DIRECT_HLT_TERMSCP);
               genericTreeNode.setTermCategory(DIRECT_HLT_TERMCAT);
               genericTreeNode.setTermWeight(DIRECT_HLT_TERMWEIG);
           }
           if (genericTreeNode.getLevelName().equals(CSMQBean.PT)) {
               genericTreeNode.setTermLevel(DIRECT_PT_TERMLVL);
               genericTreeNode.setFormattedScope(DIRECT_PT_TERMSCP);
               genericTreeNode.setTermCategory(DIRECT_PT_TERMCAT);
               genericTreeNode.setTermWeight(DIRECT_PT_TERMWEIG);
           }
        }
        
    public void setDefaultDraftGroupName(String defaultDraftGroupName) {
        this.defaultDraftGroupName = defaultDraftGroupName;
        }

    public String getDefaultDraftGroupName() {
        return defaultDraftGroupName;
        }
    
    protected void clearKeys (RichTreeTable tree) {
        if (tree != null && tree.getDisclosedRowKeys()!=null )
            tree.getDisclosedRowKeys().clear();//to resolve NoRowAvailableException
        }


    public void setRoot(GenericTreeNode rootNode) {
        this.root = rootNode;
    }

    public GenericTreeNode getRoot() {
        return root;
    }
    
    public GenericTreeNode copyNode(GenericTreeNode node){
        GenericTreeNode copyNode = new GenericTreeNode();
        copyNode.setApprovedFlag(node.getApprovedFlag());
        copyNode.setDeletable(node.isDeletable());
        copyNode.setDescription(node.getDescription());
        copyNode.setDictContentAltCode(node.getDictContentAltCode());
        copyNode.setDictContentCode(node.getDictContentCode());
        copyNode.setDictContentId(node.getDictContentId());
        copyNode.setDictShortName(node.getDictShortName());
        copyNode.setEditable(node.isEditable());
        //copyNode.setEndTS(node.getEndTS());
        copyNode.setFormattedScope(node.getFormattedScope());
        copyNode.setHasScope(node.isHasScope());
        copyNode.setIcon(node.getIcon());
        copyNode.setIsDirectRelation(node.isIsDirectRelation());
        copyNode.setIsExpanded(node.isIsExpanded());
        copyNode.setIsRoot(node.isIsRoot());
        copyNode.setLevel(node.getLevel());
        copyNode.setLevelName(node.getLevelName());
        copyNode.setMarkedForDeletion(node.isMarkedForDeletion());
        copyNode.setMqType(node.getMqType());
        copyNode.setNodeSelected(node.isNodeSelected());
        copyNode.setParent(node.getParent());
        copyNode.setParentNode(node.getParentNode());
        copyNode.setPath(node.getPath());
        copyNode.setPredictGroupId(node.getPredictGroupId());
        copyNode.setPrikey(node.getPrikey());
        copyNode.setPrimaryLink(node.isPrimaryLink());
        copyNode.setQueryLevel(node.getQueryLevel());
        copyNode.setRendered(node.isRendered());
        copyNode.setScopeName(node.getScopeName());
        copyNode.setShowHasChildrenButton(node.isShowHasChildrenButton());
        copyNode.setSortField(node.getSortField());
        copyNode.setStatus(node.getStatus());
        copyNode.setStyle(node.getStyle());
        copyNode.setTerm(node.getTerm());
        copyNode.setTermCategory(node.getTermCategory());
        copyNode.setTermLevel(node.getTermLevel());
        copyNode.setTermScope(node.getTermScope());
        copyNode.setTermWeight(node.getTermWeight());
        return copyNode;
    }
    


    
}
