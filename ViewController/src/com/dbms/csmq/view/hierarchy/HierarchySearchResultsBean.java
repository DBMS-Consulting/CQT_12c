package com.dbms.csmq.view.hierarchy;


import com.dbms.csmq.CSMQBean;
import com.dbms.csmq.UserBean;
import com.dbms.util.Utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;


public class HierarchySearchResultsBean {

    private TreeModel treemodel;
    private Enumeration rows;
    private List nodes;
    
    public static final int PARAM_LENGTH = 7;
    public static final int VAL_PREDICT_GROUP_ID = 0;
    public static final int VAL_DICT_CONTENT_ID = 1;   
    public static final int VAL_TERMSCP = 2;
    public static final int VAL_TERMCAT = 3;
    public static final int VAL_TERMLVL = 4;
    public static final int VAL_TERMWEIG = 5;
    public static final int VAL_DML = 6;
    UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");


    public HierarchySearchResultsBean() {
        System.out.println("START: NewPTListBean");
        System.out.println("END: NewPTListBean");   
        }
    
    
    public void init () {
        nodes = new ArrayList();
        createTree(); 
        treemodel = new ChildPropertyTreeModel(nodes, "children") {
                public boolean isContainer() {
                    return false;
                }
            }; 
        }
    
    

    private void createTree() {

        CSMQBean.logger.info(userBean.getCaller() + " <<< CREATING RESULTS LIST LIST  >>>");
        
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("HierarchySourceTermSearchVO1Iterator");

        rows = dciterb.getRowSetIterator().enumerateRowsInRange();

        while (rows.hasMoreElements()) {
            Row row = (Row)rows.nextElement();
            GenericTreeNode termNode = new GenericTreeNode();
            
            termNode.setTerm(Utils.getAsString(row, "Mqterm"));
            CSMQBean.logger.debug(userBean.getCaller() + " ADDING CHILD: " + termNode.getTerm());
            
            termNode.setLevelName(resolveTermLevel(Utils.getAsString(row,"LevelNm")));
            termNode.setQueryLevel(Utils.getAsString(row,"LevelNm"));
            //termNode.setLevelName(Utils.getAsString(row, "LevelNm"));
            termNode.setTermScope(Utils.getAsNumber(row,"Mqscp"));
            
            termNode.setHasScope(Utils.getAsBoolean(row, "Mqscp"));
                
            termNode.setDictContentId(Utils.getAsString(row, "ContentId"));
            termNode.setDictContentCode(Utils.getAsString(row, "Mqcode"));
            // generate a temp prikey to use as a key for inserts in the hierarchy assessor
            termNode.setPrikey("123." + termNode.getDictContentId() + "." + termNode.getDictContentCode() + ".123");
                
            //  TODO: UNCOMMENT CSMQBean.logger.info(userBean.getCaller() + " [PT LIST] ADDING: " + termNode);
            nodes.add(termNode);
            }
        }


    public void setTreemodel(TreeModel treemodel) {
        this.treemodel = treemodel;
    }

    public TreeModel getTreemodel() {
        return treemodel;
    }

    private String resolveTermLevel (String level) {

        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(level);
        if (matcher.find())
            return (matcher.group());
        return level;
    }

   
}
