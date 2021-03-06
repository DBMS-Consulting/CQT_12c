package com.dbms.csmq.model.product;

import oracle.jbo.RowSet;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Jul 11 12:35:59 EDT 2011
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ProductListVORowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
     */
    public enum AttributesEnum {
        ShortVal {
            public Object get(ProductListVORowImpl obj) {
                return obj.getShortVal();
            }

            public void put(ProductListVORowImpl obj, Object value) {
                obj.setShortVal((String)value);
            }
        }
        ,
        LongValue {
            public Object get(ProductListVORowImpl obj) {
                return obj.getLongValue();
            }

            public void put(ProductListVORowImpl obj, Object value) {
                obj.setLongValue((String)value);
            }
        }
        ,
        ViewObj_ProductList1 {
            public Object get(ProductListVORowImpl obj) {
                return obj.getViewObj_ProductList1();
            }

            public void put(ProductListVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static int firstIndex = 0;

        public abstract Object get(ProductListVORowImpl object);

        public abstract void put(ProductListVORowImpl object, Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int SHORTVAL = AttributesEnum.ShortVal.index();
    public static final int LONGVALUE = AttributesEnum.LongValue.index();
    public static final int VIEWOBJ_PRODUCTLIST1 = AttributesEnum.ViewObj_ProductList1.index();

    /**
     * This is the default constructor (do not remove).
     */
    public ProductListVORowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute ShortVal.
     * @return the ShortVal
     */
    public String getShortVal() {
        return (String) getAttributeInternal(SHORTVAL);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute ShortVal.
     * @param value value to set the  ShortVal
     */
    public void setShortVal(String value) {
        setAttributeInternal(SHORTVAL, value);
    }

    /**
     * Gets the attribute value for the calculated attribute LongValue.
     * @return the LongValue
     */
    public String getLongValue() {
        return (String) getAttributeInternal(LONGVALUE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute LongValue.
     * @param value value to set the  LongValue
     */
    public void setLongValue(String value) {
        setAttributeInternal(LONGVALUE, value);
    }


    /**
     * Gets the view accessor <code>RowSet</code> ViewObj_ProductList1.
     */
    public RowSet getViewObj_ProductList1() {
        return (RowSet)getAttributeInternal(VIEWOBJ_PRODUCTLIST1);
    }

    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index, AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
        }
        return super.getAttrInvokeAccessor(index, attrDef);
    }

    /**
     * setAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param value the value to assign to the attribute
     * @param attrDef the attribute

     * @throws Exception
     */
    protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }
}
