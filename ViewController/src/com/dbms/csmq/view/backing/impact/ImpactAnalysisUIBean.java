package com.dbms.csmq.view.backing.impact;

import oracle.adf.view.rich.component.rich.RichMenu;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichToolbar;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.component.rich.nav.RichTrain;



public class ImpactAnalysisUIBean {
    //private RichTable cntrlImpactSearchResults;
    
    private RichSelectOneChoice ctrlImpact;
   
    private RichSelectManyChoice cntlSOCList;
    private RichSelectBooleanCheckbox cntrlShowImpactedFilterExisting;
    private RichSelectBooleanCheckbox cntrlShowPrimaryFilterExisting;
    private RichSelectOneChoice cntrlShowSortFilterExisting;
    private RichSelectBooleanCheckbox cntrlShowImpactedFilterFuture;
    private RichSelectBooleanCheckbox cntrlShowPrimaryFilterFuture;
    private RichSelectOneChoice cntrlShowSortFilterFuture;
    private RichMenu cntrlExistingMenu;
    private RichMenu cntrlFutureMenu;
    private RichCommandToolbarButton cntrlExportButton;
    private RichCommandToolbarButton cntrlExportDisplayedButtonLeft;
    private RichCommandToolbarButton cntrlExportDisplayedButtonRight;
    private RichToolbar cntrlImpactLeftToolbar;
    private RichToolbar cntrlImpactRightToolbar;
    private RichCommandToolbarButton cntrlRefreshButton;
    private RichTrain cntrlTrain;
    private RichPopup newPreferedTermsPopup;
    private RichPopup impactSearchPopUp;
    private RichToolbar promotionToolBar;
    
    private RichTable cntrlSearchResultsTBL_NMQ_Y;
    private RichTable cntrlSearchResultsTBL_MQ_Y;
    private RichTable cntrlSearchResultsTBL_NMQ_N;
    private RichTable cntrlSearchResultsTBL_MQ_N;
    private RichTable cntrlSearchResultsTBL_CMQ_Y;
    private RichTable cntrlSearchResultsTBL_CMQ_N;
    
    public ImpactAnalysisUIBean() {
    }

//    public void setCntrlImpactSearchResults(RichTable cntrlImpactSearchResults) {
//        this.cntrlImpactSearchResults = cntrlImpactSearchResults;
//    }
//
//    public RichTable getCntrlImpactSearchResults() {
//        return cntrlImpactSearchResults;
//    }
    
    
    
    public void setCntrlShowImpactedFilterExisting(RichSelectBooleanCheckbox cntrlShowImpactedFilterExisting) {
        this.cntrlShowImpactedFilterExisting = cntrlShowImpactedFilterExisting;
    }

    public RichSelectBooleanCheckbox getCntrlShowImpactedFilterExisting() {
        return cntrlShowImpactedFilterExisting;
    }

    public void setCntrlShowPrimaryFilterExisting(RichSelectBooleanCheckbox cntrlShowPrimaryFilterExisting) {
        this.cntrlShowPrimaryFilterExisting = cntrlShowPrimaryFilterExisting;
    }

    public RichSelectBooleanCheckbox getCntrlShowPrimaryFilterExisting() {
        return cntrlShowPrimaryFilterExisting;
    }

    public void setCntrlShowSortFilterExisting(RichSelectOneChoice cntrlShowSortFilterExisting) {
        this.cntrlShowSortFilterExisting = cntrlShowSortFilterExisting;
    }

    public RichSelectOneChoice getCntrlShowSortFilterExisting() {
        return cntrlShowSortFilterExisting;
    }

    public void setCntrlShowImpactedFilterFuture(RichSelectBooleanCheckbox cntrlShowImpactedFilterFuture) {
        this.cntrlShowImpactedFilterFuture = cntrlShowImpactedFilterFuture;
    }

    public RichSelectBooleanCheckbox getCntrlShowImpactedFilterFuture() {
        return cntrlShowImpactedFilterFuture;
    }

    public void setCntrlShowPrimaryFilterFuture(RichSelectBooleanCheckbox cntrlShowPrimaryFilterFuture) {
        this.cntrlShowPrimaryFilterFuture = cntrlShowPrimaryFilterFuture;
    }

    public RichSelectBooleanCheckbox getCntrlShowPrimaryFilterFuture() {
        return cntrlShowPrimaryFilterFuture;
    }

    public void setCntrlShowSortFilterFuture(RichSelectOneChoice cntrlShowSortFilterFuture) {
        this.cntrlShowSortFilterFuture = cntrlShowSortFilterFuture;
    }

    public RichSelectOneChoice getCntrlShowSortFilterFuture() {
        return cntrlShowSortFilterFuture;
    }

    public void setCntrlExistingMenu(RichMenu cntrlExistingMenu) {
        this.cntrlExistingMenu = cntrlExistingMenu;
    }

    public RichMenu getCntrlExistingMenu() {
        return cntrlExistingMenu;
    }

    public void setCntrlFutureMenu(RichMenu cntrlFutureMenu) {
        this.cntrlFutureMenu = cntrlFutureMenu;
    }

    public RichMenu getCntrlFutureMenu() {
        return cntrlFutureMenu;
    }
    
    public void setCntrlExportButton(RichCommandToolbarButton cntrlExportButton) {
        this.cntrlExportButton = cntrlExportButton;
    }

    public RichCommandToolbarButton getCntrlExportButton() {
        return cntrlExportButton;
    }
    
    public void setCntrlExportDisplayedButtonLeft(RichCommandToolbarButton cntrlExportDisplayedButton) {
        this.cntrlExportDisplayedButtonLeft = cntrlExportDisplayedButton;
    }

    public RichCommandToolbarButton getCntrlExportDisplayedButtonLeft() {
        return cntrlExportDisplayedButtonLeft;
    }
    public void setCntrlImpactLeftToolbar(RichToolbar impactLeftToolbar) {
        this.cntrlImpactLeftToolbar = impactLeftToolbar;
    }

    public RichToolbar getCntrlImpactLeftToolbar() {
        return cntrlImpactLeftToolbar;
    }

    public void setCntrlImpactRightToolbar(RichToolbar impactRightToolbar) {
        this.cntrlImpactRightToolbar = impactRightToolbar;
    }

    public RichToolbar getCntrlImpactRightToolbar() {
        return cntrlImpactRightToolbar;
    }

    public void setCntrlExportDisplayedButtonRight(RichCommandToolbarButton cntrlExportDisplayedButtonRight) {
        this.cntrlExportDisplayedButtonRight = cntrlExportDisplayedButtonRight;
    }

    public RichCommandToolbarButton getCntrlExportDisplayedButtonRight() {
        return cntrlExportDisplayedButtonRight;
    }

    public void setCntrlRefreshButton(RichCommandToolbarButton refreshButton) {
        this.cntrlRefreshButton = refreshButton;
    }

    public RichCommandToolbarButton getCntrlRefreshButton() {
        return cntrlRefreshButton;
    }
    
    public void setCntrlTrain(RichTrain cntrlTrain) {
        this.cntrlTrain = cntrlTrain;
    }

    public RichTrain getCntrlTrain() {
        return cntrlTrain;
    }
    
    public void setPromotionToolBar(RichToolbar promotionToolBar) {
        this.promotionToolBar = promotionToolBar;
    }

    public RichToolbar getPromotionToolBar() {
        return promotionToolBar;
    }
    
    

    public void setCntlSOCList(RichSelectManyChoice cntlSOCList) {
        this.cntlSOCList = cntlSOCList;
    }

    public RichSelectManyChoice getCntlSOCList() {
        return cntlSOCList;
    }
    
    public void setImpactSearchPopUp(RichPopup searchPopUp) {
        this.impactSearchPopUp = searchPopUp;
    }

    public RichPopup getImpactSearchPopUp() {
        return impactSearchPopUp;
    }
    
   
    
    public void setCtrlImpact(RichSelectOneChoice ctrlImpact) {
        this.ctrlImpact = ctrlImpact;
    }

    public RichSelectOneChoice getCtrlImpact() {
        return ctrlImpact;
    }
    
    public void setNewPreferedTermsPopup(RichPopup newPreferedTermsPopup) {
        this.newPreferedTermsPopup = newPreferedTermsPopup;
    }

    public RichPopup getNewPreferedTermsPopup() {
        return newPreferedTermsPopup;
    }
    public void setCntrlSearchResultsTBL_NMQ_Y(RichTable cntrlSearchResultsTBL_NMQ_Y) {
        this.cntrlSearchResultsTBL_NMQ_Y = cntrlSearchResultsTBL_NMQ_Y;
    }

    public RichTable getCntrlSearchResultsTBL_NMQ_Y() {
        return cntrlSearchResultsTBL_NMQ_Y;
    }

    public void setCntrlSearchResultsTBL_MQ_Y(RichTable cntrlSearchResultsTBL_MQ_Y) {
        this.cntrlSearchResultsTBL_MQ_Y = cntrlSearchResultsTBL_MQ_Y;
    }

    public RichTable getCntrlSearchResultsTBL_MQ_Y() {
        return cntrlSearchResultsTBL_MQ_Y;
    }

    public void setCntrlSearchResultsTBL_NMQ_N(RichTable cntrlSearchResultsTBL_NMQ_N) {
        this.cntrlSearchResultsTBL_NMQ_N = cntrlSearchResultsTBL_NMQ_N;
    }

    public RichTable getCntrlSearchResultsTBL_NMQ_N() {
        return cntrlSearchResultsTBL_NMQ_N;
    }

    public void setCntrlSearchResultsTBL_MQ_N(RichTable cntrlSearchResultsTBL_MQ_N) {
        this.cntrlSearchResultsTBL_MQ_N = cntrlSearchResultsTBL_MQ_N;
    }

    public RichTable getCntrlSearchResultsTBL_MQ_N() {
        return cntrlSearchResultsTBL_MQ_N;
    }

    public void setCntrlSearchResultsTBL_CMQ_Y(RichTable cntrlSearchResultsTBL_CMQ_Y) {
        this.cntrlSearchResultsTBL_CMQ_Y = cntrlSearchResultsTBL_CMQ_Y;
    }

    public RichTable getCntrlSearchResultsTBL_CMQ_Y() {
        return cntrlSearchResultsTBL_CMQ_Y;
    }

    public void setCntrlSearchResultsTBL_CMQ_N(RichTable cntrlSearchResultsTBL_CMQ_N) {
        this.cntrlSearchResultsTBL_CMQ_N = cntrlSearchResultsTBL_CMQ_N;
    }

    public RichTable getCntrlSearchResultsTBL_CMQ_N() {
        return cntrlSearchResultsTBL_CMQ_N;
    }

}
