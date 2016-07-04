package com.dbms.util;



import com.dbms.csmq.CSMQBean;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;
import oracle.jbo.domain.Number;

public class Utils {

    public static String replace(String where, String what, String with) {
        return where.replaceAll(what, with);
    }

    public static String getAsString(Row row, String key) {
        String retVal = null;
        Object o = row.getAttribute(key);
        if (o != null)
            retVal = o.toString();
        return retVal;
    }

    public static oracle.jbo.domain.Date getAsDate(Row row, String key) {
        oracle.jbo.domain.Date retVal = null;
        Object o = row.getAttribute(key);
        if (o != null)
            retVal = (oracle.jbo.domain.Date)o;
        return retVal;
    }

    public static Number getAsNumber(Row row, String key) {
        Object o = row.getAttribute(key);
        if (o instanceof oracle.jbo.domain.Number)
            return (Number)o;
        return new Number(); //return 0
    }

    public static boolean getAsBoolean(Row row, String key) {
        Object o = row.getAttribute(key);
        if (o == null)
            return false;
        return o.toString().equals(CSMQBean.TRUE);
    }

    public static boolean getAsBoolean(Row row, String key, String testValue) {
        Object o = row.getAttribute(key);
        if (o == null)
            return false;
        return o.toString().equals(testValue);
    }


    public static boolean isNum(String in) {
        try {
            Integer.parseInt(in);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
        return exp.getValue(elContext);
    }

    public static void refresh(javax.faces.component.UIComponent component) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(component);
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(component);
    }

    public static void changeCookieValue(String cookieName, String pValue) {
        FacesContext vFacesContext = FacesContext.getCurrentInstance();
        ExternalContext vExternalContext = vFacesContext.getExternalContext();
        HttpServletResponse vResponse = (HttpServletResponse)vExternalContext.getResponse();
        Cookie vNewCookie = new Cookie(cookieName, pValue);
        vNewCookie.setMaxAge(-1);
        vResponse.addCookie(vNewCookie);
    }

    public static String getCurrentCookieValue(String cookieName) {
        FacesContext vFacesContext = FacesContext.getCurrentInstance();
        ExternalContext vExternalContext = vFacesContext.getExternalContext();
        Map vRequestCookieMap = vExternalContext.getRequestCookieMap();
        Cookie vMyCookie = (Cookie)vRequestCookieMap.get(cookieName);
        if (vMyCookie != null) {
            return vMyCookie.getValue();
        } else {
            return "no cookie '" + cookieName + "' available";
        }
    }
}
