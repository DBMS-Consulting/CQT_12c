package com.dbms.csmq.model.designee;

import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Jun 02 00:31:10 IST 2020
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class MqDesigneeListRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        DictContentId {
            protected Object get(MqDesigneeListRowImpl obj) {
                return obj.getDictContentId();
            }

            protected void put(MqDesigneeListRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        DictContentCode {
            protected Object get(MqDesigneeListRowImpl obj) {
                return obj.getDictContentCode();
            }

            protected void put(MqDesigneeListRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        TermUpper {
            protected Object get(MqDesigneeListRowImpl obj) {
                return obj.getTermUpper();
            }

            protected void put(MqDesigneeListRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        Designee {
            protected Object get(MqDesigneeListRowImpl obj) {
                return obj.getDesignee();
            }

            protected void put(MqDesigneeListRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;


        protected int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        protected static final int firstIndex() {
            return firstIndex;
        }

        protected static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        protected static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }


        protected abstract Object get(MqDesigneeListRowImpl object);

        protected abstract void put(MqDesigneeListRowImpl object, Object value);
    }


    public static final int DICTCONTENTID = AttributesEnum.DictContentId.index();
    public static final int DICTCONTENTCODE = AttributesEnum.DictContentCode.index();
    public static final int TERMUPPER = AttributesEnum.TermUpper.index();
    public static final int DESIGNEE = AttributesEnum.Designee.index();

    /**
     * This is the default constructor (do not remove).
     */
    public MqDesigneeListRowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute DictContentId.
     * @return the DictContentId
     */
    public Number getDictContentId() {
        return (Number) getAttributeInternal(DICTCONTENTID);
    }

    /**
     * Gets the attribute value for the calculated attribute DictContentCode.
     * @return the DictContentCode
     */
    public String getDictContentCode() {
        return (String) getAttributeInternal(DICTCONTENTCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute TermUpper.
     * @return the TermUpper
     */
    public String getTermUpper() {
        return (String) getAttributeInternal(TERMUPPER);
    }

    /**
     * Gets the attribute value for the calculated attribute Designee.
     * @return the Designee
     */
    public String getDesignee() {
        return (String) getAttributeInternal(DESIGNEE);
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
