package com.dbms.csmq.view.hierarchy;


import com.dbms.csmq.CSMQBean;

import com.dbms.csmq.UserBean;
import com.dbms.util.Utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;


public class TermListBean {

    private TreeModel treemodel;
    private Enumeration rows;


    //Search terms used for term list box
    private String dictionaryVersionSearchCriteria = null;
    private String levelSearchCriteria = null;
    private String termSearchCriteria = null;

    List nodes = new ArrayList();
    private RichSelectOneChoice dictVersionControl;
    private RichSelectOneChoice levelControl;
    private RichInputText termControl;
    UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
    
    public TermListBean() {
        
    }

    private void createList() {


        if (termSearchCriteria == null || termSearchCriteria.equals("null")) return;

        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding dciterb = (DCIteratorBinding)binding.get("TermListVO1Iterator");
        ViewObject termListVO = dciterb.getViewObject();
        UserBean userBean = (UserBean)ADFContext.getCurrent().getSessionScope().get("UserBean");
        CSMQBean.logger.info(userBean.getCaller() + " ** REQUERY **");
        CSMQBean.logger.info(userBean.getCaller() + " termName: " +  termSearchCriteria);
        CSMQBean.logger.info(userBean.getCaller() + " dictVersion: " + dictionaryVersionSearchCriteria);
        CSMQBean.logger.info(userBean.getCaller() + " levelName: " + levelSearchCriteria);
        
        
        termListVO.setNamedWhereClauseParam("termName", termSearchCriteria);
        termListVO.setNamedWhereClauseParam("dictVersion", dictionaryVersionSearchCriteria);
        termListVO.setNamedWhereClauseParam("levelName", levelSearchCriteria);
        termListVO.executeQuery();
        
        
        
        
        Enumeration rows = dciterb.getRowSetIterator().enumerateRowsInRange();


        CSMQBean.logger.info(userBean.getCaller() + " RANGE SIZE:" + dciterb.getRangeSize());

        while (rows.hasMoreElements()) {

            Row row = (Row)rows.nextElement();
            GenericTreeNode root = new GenericTreeNode();
            root.setTerm(Utils.getAsString(row, "Term"));
            root.setPrikey(Utils.getAsString(row, "Prikey"));
            root.setParent(Utils.getAsString(row, "Parent"));
            root.setLevelName(Utils.getAsString(row, "LevelName"));
            root.setLevel(null);
            root.setDictShortName(Utils.getAsString(row, "DictShortName"));
            root.setDictContentId(Utils.getAsString(row, "DictContentId"));
            root.setApprovedFlag(Utils.getAsString(row, "ApprovedFlag"));
            root.setDictContentAltCode(Utils.getAsString(row, "DictContentAltCode"));
            root.setStatus(Utils.getAsString(row, "Status"));

            CSMQBean.logger.info(userBean.getCaller() + " Adding Node:" + root);
            nodes.add(root);
            }
        }



    private boolean setParams () {
        try {
            dictionaryVersionSearchCriteria = String.valueOf(this.dictVersionControl.getValue());
            levelSearchCriteria = String.valueOf(this.levelControl.getValue());
            termSearchCriteria = String.valueOf(this.termControl.getValue());

            CSMQBean.logger.info(userBean.getCaller() + " SETTING PARAMS");
            CSMQBean.logger.info(userBean.getCaller() + " dictionaryVersionSearchCriteria:" + dictionaryVersionSearchCriteria);
            CSMQBean.logger.info(userBean.getCaller() + " levelSearchCriteria:" + levelSearchCriteria);
            CSMQBean.logger.info(userBean.getCaller() + " termSearchCriteria:" + termSearchCriteria); 
            
            
            return true;
            }    
        catch (Exception e) {
            CSMQBean.logger.info (e.getMessage());
            return false;
            }      
        }
    
    private void createModel () {
        CSMQBean.logger.info(userBean.getCaller() + " >> GETTING TermListBean treeModel");
        //if (nMQBean.isCreateList()) {
        CSMQBean.logger.info(userBean.getCaller() + " CREATING LIST");
        if (!setParams()) return;
        createList();
        treemodel = new ChildPropertyTreeModel(nodes, "children") {
                public boolean isContainer() {
                    return false;
                }
            };
        //     nMQBean.setCreateList(false);
        //   }
    
    }


    public void setTreemodel(TreeModel treemodel) {
        this.treemodel = treemodel;
    }

    public TreeModel getTreemodel() {
       if (treemodel == null || !treemodel.isRowAvailable()) createModel();
        return treemodel;
    }


    public void setDictVersionControl(RichSelectOneChoice dictVersionControl) {
        this.dictVersionControl = dictVersionControl;
    }

    public RichSelectOneChoice getDictVersionControl() {
        return dictVersionControl;
    }

    public void setLevelControl(RichSelectOneChoice levelControl) {
        this.levelControl = levelControl;
    }

    public RichSelectOneChoice getLevelControl() {
        return levelControl;
    }

    public void setTermControl(RichInputText termControl) {
        this.termControl = termControl;
    }

    public RichInputText getTermControl() {
        return termControl;
    }
}
