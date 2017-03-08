package com.dbms.csmq.view.train;

import com.dbms.util.ADFUtils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowContext;
import oracle.adf.controller.TaskFlowTrainModel;
import oracle.adf.controller.TaskFlowTrainStopModel;
import oracle.adf.controller.ViewPortContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.fragment.RichPageTemplate;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandNavigationItem;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.event.DialogEvent;

public class TrainHandlerBean {
    /**
     * Managed bean property reference to a helper bean in view scope
     */
    private TrainHandlerBeanHelper trainHandlerBeanHelper = null;

    public TrainHandlerBean() {
    }

    /**
     * Action method that is referenced from the train stop command
     * action to defer train navigation and allow developers to
     * interact with the user. In this sample a popup dialog is opened
     * for the user to confirm train navigation.
     *
     * The use case for deferred trains top navigation includes manual
     * complex field validation, to check for
     * uncommitted data, e.g.
     * ControllerContext.getInstance().getCurrentViewPort()
     * .isDataDirty() , etc.
     *
     * @param actionEvent
     */
    public void onTrainStopSelect(ActionEvent actionEvent) {
        RichCommandNavigationItem rni = (RichCommandNavigationItem) actionEvent.getSource();
        TaskFlowTrainStopModel selectedTrainStop = (TaskFlowTrainStopModel) rni.getAttributes().get("trainStopNode");
        String outcome = selectedTrainStop.getOutcome();
        trainHandlerBeanHelper.setSelectedTrainStopOutcome(outcome);
       
        UIComponent component = 
            ADFUtils.findComponentInRoot("addDetailsWarning");
        
        if(component != null){
            //add details case
            Boolean isDetailsChanged = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.isDetailsChanged}");
            if(isDetailsChanged != null && isDetailsChanged){
                ADFUtils.setEL("#{pageFlowScope.detailsFromTrain}", Boolean.TRUE);
                ADFUtils.showPopup(component);
            }
            else{
                RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                ActionEvent ae = new ActionEvent(button);
                ae.queue();
            }
        }
        
        else{
            component = 
                ADFUtils.findComponentInRoot("infoNotesWarning");
            if(component != null){
                //info notes case
                Boolean isNotesChanged = (Boolean)ADFUtils.evaluateEL("#{pageFlowScope.isNotesChanged}");
                if(isNotesChanged != null && isNotesChanged){
                    ADFUtils.setEL("#{pageFlowScope.notesFromTrain}", Boolean.TRUE);
                    ADFUtils.showPopup(component);
                }
                else{
                    RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                    ActionEvent ae = new ActionEvent(button);
                    ae.queue();
                }
            }
            else{
                component = 
                    ADFUtils.findComponentInRoot("addRelationsWarning");
                if(component != null){
                    Object relationIcon = ADFUtils.evaluateEL("#{pageFlowScope.TermHierarchyBean.iconMQChanged}");
                    if(relationIcon == null){
                        RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                        ActionEvent ae = new ActionEvent(button);
                        ae.queue();
                    }
                    else{
                        RichImage img = (RichImage)relationIcon;
                        if(img.isVisible()){
                            ADFUtils.setEL("#{pageFlowScope.relationsFromTrain}", Boolean.TRUE);
                            ADFUtils.showPopup(component);
                        }
                        else{
                            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                            ActionEvent ae = new ActionEvent(button);
                            ae.queue();
                        }
                    }
                }
                else{
                    RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                    ActionEvent ae = new ActionEvent(button);
                    ae.queue();
                }
            }
        }
    }

    public String onClickOKButton(){
        return trainHandlerBeanHelper.getSelectedTrainStopOutcome();
    }

    /**
     * Managed bean property reference to a managed bean in view scope
     * @param trainHandlerBeanHelper Managed bean inserted as a managed
     * property reference
     */
    public void setTrainHandlerBeanHelper(TrainHandlerBeanHelper trainHandlerBeanHelper) {
        this.trainHandlerBeanHelper = trainHandlerBeanHelper;
    }

    public TrainHandlerBeanHelper getTrainHandlerBeanHelper() {
        return trainHandlerBeanHelper;
    }

    public void navigateNextStop() {
        ControllerContext controllerContext = ControllerContext.getInstance();
        ViewPortContext currentViewPortCtx = controllerContext.getCurrentViewPort();
        TaskFlowContext taskFlowCtx = currentViewPortCtx.getTaskFlowContext();
        TaskFlowTrainModel taskFlowTrainModel = taskFlowCtx.getTaskFlowTrainModel();
        TaskFlowTrainStopModel currentStop = taskFlowTrainModel.getCurrentStop();
        TaskFlowTrainStopModel nextStop = taskFlowTrainModel.getNextStop(currentStop);
        trainHandlerBeanHelper.setSelectedTrainStopOutcome(nextStop.getOutcome());
    }
    
    public void navigatePrevStop() {
        ControllerContext controllerContext = ControllerContext.getInstance();
        ViewPortContext currentViewPortCtx = controllerContext.getCurrentViewPort();
        TaskFlowContext taskFlowCtx = currentViewPortCtx.getTaskFlowContext();
        TaskFlowTrainModel taskFlowTrainModel = taskFlowCtx.getTaskFlowTrainModel();
        TaskFlowTrainStopModel currentStop = taskFlowTrainModel.getCurrentStop();
        TaskFlowTrainStopModel prevStop = taskFlowTrainModel.getPreviousStop(currentStop);
        trainHandlerBeanHelper.setSelectedTrainStopOutcome(prevStop.getOutcome());
    }

    public void onTrainButtonBarAction(ActionEvent actionEvent) {
        UIComponent component = ADFUtils.findComponentInRoot("addDetailsWarning");

        if (component != null) {
            //add details case
            Boolean isDetailsChanged = (Boolean) ADFUtils.evaluateEL("#{pageFlowScope.isDetailsChanged}");
            if (isDetailsChanged != null && isDetailsChanged) {
                ADFUtils.setEL("#{pageFlowScope.detailsFromTrain}", Boolean.TRUE);
                ADFUtils.showPopup(component);
            } else {
                RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                ActionEvent ae = new ActionEvent(button);
                ae.queue();
            }
        }

        else {
            component = ADFUtils.findComponentInRoot("infoNotesWarning");
            if (component != null) {
                //info notes case
                Boolean isNotesChanged = (Boolean) ADFUtils.evaluateEL("#{pageFlowScope.isNotesChanged}");
                if (isNotesChanged != null && isNotesChanged) {
                    ADFUtils.setEL("#{pageFlowScope.notesFromTrain}", Boolean.TRUE);
                    ADFUtils.showPopup(component);
                } else {
                    RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                    ActionEvent ae = new ActionEvent(button);
                    ae.queue();
                }
            } else {
                component = ADFUtils.findComponentInRoot("addRelationsWarning");
                if (component != null) {
                    Object relationIcon = ADFUtils.evaluateEL("#{pageFlowScope.TermHierarchyBean.iconMQChanged}");
                    if (relationIcon == null) {
                        RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                        ActionEvent ae = new ActionEvent(button);
                        ae.queue();
                    } else {
                        RichImage img = (RichImage) relationIcon;
                        if (img.isVisible()) {
                            ADFUtils.setEL("#{pageFlowScope.relationsFromTrain}", Boolean.TRUE);
                            ADFUtils.showPopup(component);
                        } else {
                            RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                            ActionEvent ae = new ActionEvent(button);
                            ae.queue();
                        }
                    }
                } else {
                    RichButton button = (RichButton) ADFUtils.findComponentInRoot("hiddenButton");
                    ActionEvent ae = new ActionEvent(button);
                    ae.queue();
                }
            }
        }
    }

    public void onTrainButtonBarBack(ActionEvent actionEvent) {
        navigatePrevStop();
        onTrainButtonBarAction(actionEvent);
    }

    public void onTrainButtonBarNext(ActionEvent actionEvent) {
        navigateNextStop();
        onTrainButtonBarAction(actionEvent);
    }
}
