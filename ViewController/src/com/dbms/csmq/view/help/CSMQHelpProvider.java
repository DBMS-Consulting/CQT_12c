package com.dbms.csmq.view.help;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.help.ResourceBundleHelpProvider;

public class CSMQHelpProvider extends ResourceBundleHelpProvider {
    public CSMQHelpProvider() {
        super();
    }


    protected String getExternalUrl(FacesContext context,
                                    UIComponent component, String topicId) {
       /*  RichPopup popup = new RichPopup();
        RichPopup.PopupHints ph = new RichPopup.PopupHints();
        popup.show(ph);
        if (true) return null; */
        if (topicId == null)
            return null;
        if (topicId.contains("TOPICID_ALL") ||
            topicId.contains("TOPICID_DEFN_URL") ||
            topicId.contains("TOPICID_INSTR_URL") ||
            topicId.contains("TOPICID_URL"))
            return "http://www.google.com";
        else
            return null;
    }


}
