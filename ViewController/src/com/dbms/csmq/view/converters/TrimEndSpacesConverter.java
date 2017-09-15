package com.dbms.csmq.view.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.myfaces.trinidad.context.RequestContext;

public class TrimEndSpacesConverter implements Converter{
    public TrimEndSpacesConverter() {
        super();
    }
    
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) 
      {
            if (string != null) {
                // trim the entered value
                       RequestContext adfContext = RequestContext.getCurrentInstance();
                       adfContext.addPartialTarget(uiComponent);
                 return string.trim();
                   }

                   return string;
      }

      public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object)
      {
            if (object != null) {
                RequestContext adfContext = RequestContext.getCurrentInstance();
                adfContext.addPartialTarget(uiComponent);
                 return object.toString().trim();
                }
                   return null;
      }
}
