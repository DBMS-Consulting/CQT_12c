package com.dbms.csmq.model.impact;

import com.dbms.csmq.model.impact.common.ImpactModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oracle.jbo.ViewCriteria;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewObjectImpl;

import org.apache.log4j.Logger;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sat Jul 30 10:25:12 IST 2016
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ImpactModuleImpl extends ApplicationModuleImpl implements ImpactModule {
    /**
     * This is the default constructor (do not remove).
     */
    public ImpactModuleImpl() {
    }

    public static final Logger logger = Logger.getLogger(ImpactModuleImpl.class);

    /**
     * Container's getter for MedDRAImpactVO1.
     * @return MedDRAImpactVO1
     */
    public ViewObjectImpl getMedDRAImpactVO1() {
        return (ViewObjectImpl) findViewObject("MedDRAImpactVO1");
    }

    /**
     * Container's getter for NewPTsVO1.
     * @return NewPTsVO1
     */
    public ViewObjectImpl getNewPTsVO1() {
        return (ViewObjectImpl) findViewObject("NewPTsVO1");
    }

    /**
     * Container's getter for DraftImpactVO1.
     * @return DraftImpactVO1
     */
    public ViewObjectImpl getDraftImpactVO1() {
        return (ViewObjectImpl) findViewObject("DraftImpactVO1");
    }

    /**
     * Container's getter for HistoricalImpactVO1.
     * @return HistoricalImpactVO1
     */
    public ViewObjectImpl getHistoricalImpactVO1() {
        return (ViewObjectImpl) findViewObject("HistoricalImpactVO1");
    }

    /**
     * Container's getter for CurrentImpactVO1.
     * @return CurrentImpactVO1
     */
    public ViewObjectImpl getCurrentImpactVO1() {
        return (ViewObjectImpl) findViewObject("CurrentImpactVO1");
    }

    /**
     * Container's getter for SOCsWithNewPTsVO1.
     * @return SOCsWithNewPTsVO1
     */
    public SOCsWithNewPTsVOImpl getSOCsWithNewPTsVO1() {
        return (SOCsWithNewPTsVOImpl) findViewObject("SOCsWithNewPTsVO1");
    }

    /**
     * Container's getter for LastMVUpdateViewObj1.
     * @return LastMVUpdateViewObj1
     */
    public ViewObjectImpl getLastMVUpdateViewObj1() {
        return (ViewObjectImpl) findViewObject("LastMVUpdateViewObj1");
    }

    /**
     * Container's getter for ImpactSearchListVO1.
     * @return ImpactSearchListVO1
     */
    public ImpactSearchListVOImpl getImpactSearchListVO1() {
        return (ImpactSearchListVOImpl) findViewObject("ImpactSearchListVO1");
    }

    /**
     * Container's getter for ImpactSearchListVO_NMQ_Y.
     * @return ImpactSearchListVO_NMQ_Y
     */
    public ImpactSearchListVOImpl getImpactSearchListVO_NMQ_Y() {
        return (ImpactSearchListVOImpl) findViewObject("ImpactSearchListVO_NMQ_Y");
    }

    /**
     * Container's getter for ImpactSearchListVO_MQ_Y.
     * @return ImpactSearchListVO_MQ_Y
     */
    public ImpactSearchListVOImpl getImpactSearchListVO_MQ_Y() {
        return (ImpactSearchListVOImpl) findViewObject("ImpactSearchListVO_MQ_Y");
    }

    /**
     * Container's getter for ImpactSearchListVO_MQ_N.
     * @return ImpactSearchListVO_MQ_N
     */
    public ImpactSearchListVOImpl getImpactSearchListVO_MQ_N() {
        return (ImpactSearchListVOImpl) findViewObject("ImpactSearchListVO_MQ_N");
    }

    /**
     * Container's getter for ImpactSearchListVO_NMQ_N.
     * @return ImpactSearchListVO_NMQ_N
     */
    public ImpactSearchListVOImpl getImpactSearchListVO_NMQ_N() {
        return (ImpactSearchListVOImpl) findViewObject("ImpactSearchListVO_NMQ_N");
    }

    /**
     * Container's getter for ImpactSearchListVO_CMQ_Y.
     * @return ImpactSearchListVO_CMQ_Y
     */
    public ImpactSearchListVOImpl getImpactSearchListVO_CMQ_Y() {
        return (ImpactSearchListVOImpl) findViewObject("ImpactSearchListVO_CMQ_Y");
    }

    /**
     * Container's getter for ImpactSearchListVO_CMQ_N.
     * @return ImpactSearchListVO_CMQ_N
     */
    public ImpactSearchListVOImpl getImpactSearchListVO_CMQ_N() {
        return (ImpactSearchListVOImpl) findViewObject("ImpactSearchListVO_CMQ_N");
    }

    /**
     * Container's getter for PreviousVerImpactSearchListVO_CMQ_N.
     * @return PreviousVerImpactSearchListVO_CMQ_N
     */
    public ViewObjectImpl getPreviousVerImpactSearchListVO_CMQ_N() {
        return (ViewObjectImpl) findViewObject("PreviousVerImpactSearchListVO_CMQ_N");
    }

    /**
     * Container's getter for PreviousVerImpactSearchListVO_CMQ_Y.
     * @return PreviousVerImpactSearchListVO_CMQ_Y
     */
    public ViewObjectImpl getPreviousVerImpactSearchListVO_CMQ_Y() {
        return (ViewObjectImpl) findViewObject("PreviousVerImpactSearchListVO_CMQ_Y");
    }

    /**
     * Container's getter for PreviousVerImpactSearchListVO_MQ_N.
     * @return PreviousVerImpactSearchListVO_MQ_N
     */
    public ViewObjectImpl getPreviousVerImpactSearchListVO_MQ_N() {
        return (ViewObjectImpl) findViewObject("PreviousVerImpactSearchListVO_MQ_N");
    }

    /**
     * Container's getter for PreviousVerImpactSearchListVO_MQ_Y.
     * @return PreviousVerImpactSearchListVO_MQ_Y
     */
    public ViewObjectImpl getPreviousVerImpactSearchListVO_MQ_Y() {
        return (ViewObjectImpl) findViewObject("PreviousVerImpactSearchListVO_MQ_Y");
    }

    /**
     * Container's getter for PreviousVerImpactSearchListVO_NMQ_N.
     * @return PreviousVerImpactSearchListVO_NMQ_N
     */
    public ViewObjectImpl getPreviousVerImpactSearchListVO_NMQ_N() {
        return (ViewObjectImpl) findViewObject("PreviousVerImpactSearchListVO_NMQ_N");
    }

    /**
     * Container's getter for PreviousVerImpactSearchListVO_NMQ_Y.
     * @return PreviousVerImpactSearchListVO_NMQ_Y
     */
    public ViewObjectImpl getPreviousVerImpactSearchListVO_NMQ_Y() {
        return (ViewObjectImpl) findViewObject("PreviousVerImpactSearchListVO_NMQ_Y");
    }

    /**
     * Container's getter for PreviousVerImpactSearchListVO1.
     * @return PreviousVerImpactSearchListVO1
     */
    public ViewObjectImpl getPreviousVerImpactSearchListVO1() {
        return (ViewObjectImpl) findViewObject("PreviousVerImpactSearchListVO1");
    }

    public void onPreviousVerImpactSearch(String searchLevelStr, String searchTermStr, String searchCodeStr) {
        System.out.println(" Starts executing onPreviousVerImpactSearch() searchLevelStr=" + searchLevelStr +
                           ";; searchTermStr=" + searchTermStr + ":; searchCodeStr=" + searchCodeStr);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_CMQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_CMQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_MQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_MQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_NMQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_NMQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr);
        System.out.println(" End of execution onPreviousVerImpactSearch() -------------------");
    }
    
    public void onPreviousVerImpactSearch(String searchLevelStr, String searchTermStr, String searchCodeStr, String status, String state, String searchProduct) {
        System.out.println(" Starts executing onPreviousVerImpactSearch() searchLevelStr=" + searchLevelStr +
                           ";; searchTermStr=" + searchTermStr + ":; searchCodeStr=" + searchCodeStr);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_CMQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_CMQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_MQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_MQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_NMQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceutePrevImpactSearchVC(getPreviousVerImpactSearchListVO_NMQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        System.out.println(" End of execution onPreviousVerImpactSearch() -------------------");
    }
    
    public void onCurrentVerImpactSearch(String searchLevelStr, String searchTermStr, String searchCodeStr, String status, String state, String searchProduct) {
        System.out.println(" Starts executing onPreviousVerImpactSearch() searchLevelStr=" + searchLevelStr +
                           ";; searchTermStr=" + searchTermStr + ":; searchCodeStr=" + searchCodeStr);
        exceuteCurrImpactSearchVC(getImpactSearchListVO_CMQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceuteCurrImpactSearchVC(getImpactSearchListVO_CMQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceuteCurrImpactSearchVC(getImpactSearchListVO_MQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceuteCurrImpactSearchVC(getImpactSearchListVO_MQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceuteCurrImpactSearchVC(getImpactSearchListVO_NMQ_N(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        exceuteCurrImpactSearchVC(getImpactSearchListVO_NMQ_Y(), searchLevelStr, searchTermStr,
                                  searchCodeStr, status, state, searchProduct);
        System.out.println(" End of execution onPreviousVerImpactSearch() -------------------");
    }
    
    public void ImpactAssesmentClearSelectMQ(){
        getImpactSearchListVO_CMQ_N().executeEmptyRowSet();
        getImpactSearchListVO_CMQ_Y().executeEmptyRowSet();
        getImpactSearchListVO_MQ_N().executeEmptyRowSet();
        getImpactSearchListVO_MQ_Y().executeEmptyRowSet();
        getImpactSearchListVO_NMQ_N().executeEmptyRowSet();
        getImpactSearchListVO_NMQ_Y().executeEmptyRowSet();
    }
    
    public void previousImpactVersionClearSelectMQ(){
        getPreviousVerImpactSearchListVO_CMQ_N().executeEmptyRowSet();
        getPreviousVerImpactSearchListVO_CMQ_Y().executeEmptyRowSet();
        getPreviousVerImpactSearchListVO_MQ_N().executeEmptyRowSet();
        getPreviousVerImpactSearchListVO_MQ_Y().executeEmptyRowSet();
        getPreviousVerImpactSearchListVO_NMQ_N().executeEmptyRowSet();
        getPreviousVerImpactSearchListVO_NMQ_Y().executeEmptyRowSet();
    }
    
    public void executeImpactSearchListVO_CMQ_N(){
        getImpactSearchListVO_CMQ_N().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getImpactSearchListVO_CMQ_N(), null, null,null, "%", "%", null);
    }
    
    public void executeImpactSearchListVO_CMQ_Y(){
        getImpactSearchListVO_CMQ_Y().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getImpactSearchListVO_CMQ_Y(),  null, null,null, "%", "%", null);
    }
    
    public void executeImpactSearchListVO_MQ_N(){
        getImpactSearchListVO_MQ_N().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getImpactSearchListVO_MQ_N(),  null, null,null, "%", "%", null);
    }
    
    public void executeImpactSearchListVO_MQ_Y(){
        getImpactSearchListVO_MQ_Y().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getImpactSearchListVO_MQ_Y(),  null, null,null, "%", "%", null);
    }
    
    public void executeImpactSearchListVO_NMQ_N(){
        getImpactSearchListVO_NMQ_N().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getImpactSearchListVO_NMQ_N(),  null, null,null, "%", "%", null);
    }
    
    public void executeImpactSearchListVO_NMQ_Y(){
        getImpactSearchListVO_NMQ_Y().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getImpactSearchListVO_NMQ_Y(),  null, null,null, "%", "%", null);
    }
    
    public void executePreviousImpactSearchListVO_CMQ_N(){
        getPreviousVerImpactSearchListVO_CMQ_N().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getPreviousVerImpactSearchListVO_CMQ_N(), null, null,null, "%", "%", null);
    }
    
    public void executePreviousImpactSearchListVO_CMQ_Y(){
        getPreviousVerImpactSearchListVO_CMQ_Y().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getPreviousVerImpactSearchListVO_CMQ_Y(),  null, null,null, "%", "%", null);
    }
    
    public void executePreviousImpactSearchListVO_MQ_N(){
        getPreviousVerImpactSearchListVO_MQ_N().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getPreviousVerImpactSearchListVO_MQ_N(),  null, null,null, "%", "%", null);
    }
    
    public void executePreviousImpactSearchListVO_MQ_Y(){
        getPreviousVerImpactSearchListVO_MQ_Y().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getPreviousVerImpactSearchListVO_MQ_Y(),  null, null,null, "%", "%", null);
    }
    
    public void executePreviousImpactSearchListVO_NMQ_N(){
        getPreviousVerImpactSearchListVO_NMQ_N().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getPreviousVerImpactSearchListVO_NMQ_N(),  null, null,null, "%", "%", null);
    }
    
    public void executePreviousImpactSearchListVO_NMQ_Y(){
        getPreviousVerImpactSearchListVO_NMQ_Y().executeEmptyRowSet();
        exceuteCurrImpactSearchVC(getPreviousVerImpactSearchListVO_NMQ_Y(),  null, null,null, "%", "%", null);
    }
    
    private void exceutePrevImpactSearchVC(ViewObjectImpl previousVerImpactSearchListVO, String searchLevelStr,
                                           String searchTermStr, String searchCodeStr) {
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindLevelNumber", searchLevelStr);
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindTerm", searchTermStr);
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindCode", searchCodeStr);
        ViewCriteria vc = previousVerImpactSearchListVO.getViewCriteria("PreviousVerImpactSearchListVOCriteria");
        previousVerImpactSearchListVO.applyViewCriteria(vc);
        previousVerImpactSearchListVO.executeQuery();
        System.out.println("exceutePrevImpactSearchVC() QUERY --> " + previousVerImpactSearchListVO.getQuery());
    }
    
    private void exceutePrevImpactSearchVC(ViewObjectImpl previousVerImpactSearchListVO, String searchLevelStr,
                                           String searchTermStr, String searchCodeStr, String status, String state, String searchProduct) {
        boolean isProductSearched = false;
        String productList = "";
        if((searchProduct != null) && (!"[]".equalsIgnoreCase(searchProduct))){
        int length = searchProduct.length();
        String productString = searchProduct.substring(1, length-1);
        List<String> list = Arrays.asList(productString.split("\\s*,\\s*"));
        int count = 0;
        int listCount = list.size();
        for(String item : list){
            count++;
            isProductSearched = true;
            if(listCount != count){
                productList = productList.concat("VALUE_3 LIKE '%"+item+"%' ").concat("OR ");   
            }else{
                productList = productList.concat("VALUE_3 LIKE '%"+item+"%'");   
            }
        }
        }
        previousVerImpactSearchListVO.setWhereClause(null);
        previousVerImpactSearchListVO.setWhereClauseParams(null);
        if(isProductSearched){
        previousVerImpactSearchListVO.setWhereClause(productList);
        }
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindLevelNumber", searchLevelStr);
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindTerm", searchTermStr);
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindCode", searchCodeStr);
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindStatus", status);
        previousVerImpactSearchListVO.setNamedWhereClauseParam("bindState", state);
        ViewCriteria vc = previousVerImpactSearchListVO.getViewCriteria("PreviousVerImpactSearchListVOCriteria");
        previousVerImpactSearchListVO.applyViewCriteria(vc);
        previousVerImpactSearchListVO.executeQuery();
        System.out.println("exceutePrevImpactSearchVC() QUERY --> " + previousVerImpactSearchListVO.getQuery());
    }
    
    private void exceuteCurrImpactSearchVC(ViewObjectImpl currentVerImpactSearchListVO, String searchLevelStr,
                                           String searchTermStr, String searchCodeStr, String status, String state, String searchProduct) {
        boolean isProductSearched = false;
        String productList = "";
        if((searchProduct != null) && (!"[]".equalsIgnoreCase(searchProduct))){
        int length = searchProduct.length();
        String productString = searchProduct.substring(1, length-1);
        List<String> list = Arrays.asList(productString.split("\\s*,\\s*"));
        int count = 0;
        int listCount = list.size();
        for(String item : list){
            count++;
            isProductSearched = true;
            if(listCount != count){
                productList = productList.concat("VALUE_3 LIKE '%"+item+"%' ").concat("OR ");   
            }else{
                productList = productList.concat("VALUE_3 LIKE '%"+item+"%'");   
            }
        }
        }
        currentVerImpactSearchListVO.setWhereClause(null);
        currentVerImpactSearchListVO.setWhereClauseParams(null);
        if(isProductSearched){
        currentVerImpactSearchListVO.setWhereClause(productList);
        }
        //currentVerImpactSearchListVO.setNamedWhereClauseParam("bindLevelNumber", searchLevelStr);
        currentVerImpactSearchListVO.setNamedWhereClauseParam("bindTerm", searchTermStr);
        currentVerImpactSearchListVO.setNamedWhereClauseParam("bindCode", searchCodeStr);
        currentVerImpactSearchListVO.setNamedWhereClauseParam("bindStatus", status);
        currentVerImpactSearchListVO.setNamedWhereClauseParam("bindState", state);
        ViewCriteria vc = currentVerImpactSearchListVO.getViewCriteria("CurrentVerImpactSearchListVOCriteria");
        currentVerImpactSearchListVO.applyViewCriteria(vc);
        currentVerImpactSearchListVO.executeQuery();
        System.out.println("exceutePrevImpactSearchVC() QUERY --> " + currentVerImpactSearchListVO.getQuery());
    }
    
    public void loadPrevVersionCurrentNFurteMQDetails(Long dictContentId, Boolean showImpactedOnly){
        System.out.println("loadPrevVersionCurrentNFurteMQDetails() dictContentId --> " + dictContentId +";; showImpactedOnly --> "+showImpactedOnly);
        // Load CURRENT MQs based on dictContentCode 
        loadPrevVersionCurrentMQDetails(dictContentId, showImpactedOnly);
        // Load FUTURE MQs based on dictContentCode 
        loadPrevVersionFurteMQDetails(dictContentId, showImpactedOnly);
    }
    
    public void loadPrevVersionCurrentMQDetails(Long dictContentId, Boolean showImpactedOnly){
        System.out.println("loadPrevVersionCurrentMQDetails() dictContentId --> " + dictContentId +";; showImpactedOnly --> "+showImpactedOnly);
        // Load CURRENT MQs based on dictContentCode 
        ViewObjectImpl previousVerCurrentImpactVO = getPreviousVerCurrentImpactVO1();
        previousVerCurrentImpactVO.setNamedWhereClauseParam("bindDictContentId", dictContentId);
        // -1 mean show all
        previousVerCurrentImpactVO.setNamedWhereClauseParam("bindImpactDisplayProperty", showImpactedOnly != null && showImpactedOnly ? "Impact_NMQ_0" : "-1");
        previousVerCurrentImpactVO.executeQuery();
        System.out.println("loadPrevVersionCurrentMQDetails() CURRENT QUERY --> " + previousVerCurrentImpactVO.getQuery());
    }
    
    public void loadPrevVersionFurteMQDetails(Long dictContentId, Boolean showImpactedOnly){
        System.out.println("loadPrevVersionFurteMQDetails() dictContentId --> " + dictContentId +";; showImpactedOnly --> "+showImpactedOnly);
        // Load FUTURE MQs based on dictContentCode 
        ViewObjectImpl previousVerFutureImpactVO1 = getPreviousVerFutureImpactVO1();
        previousVerFutureImpactVO1.setNamedWhereClauseParam("bindDictContentId", dictContentId);
        // -1 mean show all
        previousVerFutureImpactVO1.setNamedWhereClauseParam("bindImpactDisplayProperty", showImpactedOnly != null && showImpactedOnly ? "Impact_NMQ_0" : "-1");
        previousVerFutureImpactVO1.executeQuery();
        System.out.println("loadPrevVersionFurteMQDetails() previousVerFutureImpactVO1.getEstimatedRowCount()--> " + previousVerFutureImpactVO1.getEstimatedRowCount());
        System.out.println("loadPrevVersionFurteMQDetails() FUTURE QUERY --> " + previousVerFutureImpactVO1.getQuery());        
    }    

    /**
     * Container's getter for PreviousVerCurrentImpactVO1.
     * @return PreviousVerCurrentImpactVO1
     */
    public ViewObjectImpl getPreviousVerCurrentImpactVO1() {
        return (ViewObjectImpl) findViewObject("PreviousVerCurrentImpactVO1");
    }

    /**
     * Container's getter for PreviousVerFutureImpactVO1.
     * @return PreviousVerFutureImpactVO1
     */
    public ViewObjectImpl getPreviousVerFutureImpactVO1() {
        return (ViewObjectImpl) findViewObject("PreviousVerFutureImpactVO1");
    }
}

