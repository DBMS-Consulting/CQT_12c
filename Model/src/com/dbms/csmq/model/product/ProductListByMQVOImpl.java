package com.dbms.csmq.model.product;

import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Sep 20 17:20:23 EDT 2011
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ProductListByMQVOImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public ProductListByMQVOImpl() {
    }

    /**
     * Returns the bind variable value for dictContentID.
     * @return bind variable value for dictContentID
     */
    public String getdictContentID() {
        return (String)getNamedWhereClauseParam("dictContentID");
    }

    /**
     * Sets <code>value</code> for bind variable dictContentID.
     * @param value value to bind as dictContentID
     */
    public void setdictContentID(String value) {
        setNamedWhereClauseParam("dictContentID", value);
    }
}
