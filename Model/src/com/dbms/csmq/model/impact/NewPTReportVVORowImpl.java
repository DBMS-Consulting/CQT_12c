package com.dbms.csmq.model.impact;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Jun 25 02:36:35 IST 2019
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class NewPTReportVVORowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        Product {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getProduct();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        MqCode {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getMqCode();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        MqName {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getMqName();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        MqContentId {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getMqContentId();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        PrimaryHltCode {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getPrimaryHltCode();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        PrimaryHltTerm {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getPrimaryHltTerm();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        PrimaryHltContentId {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getPrimaryHltContentId();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        NewPtCode {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getNewPtCode();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        NewPtTerm {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getNewPtTerm();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        NewPtContentId {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getNewPtContentId();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        FormerPtCode {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getFormerPtCode();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        FormerPtTerm {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getFormerPtTerm();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        FormerPtContentId {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getFormerPtContentId();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        RelatedLltCode {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getRelatedLltCode();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        RelatedLltTerm {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getRelatedLltTerm();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        RelatedLltContentId {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getRelatedLltContentId();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        HltPtLltLoadTs {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getHltPtLltLoadTs();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        ReportType {
            protected Object get(NewPTReportVVORowImpl obj) {
                return obj.getReportType();
            }

            protected void put(NewPTReportVVORowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        protected abstract Object get(NewPTReportVVORowImpl object);

        protected abstract void put(NewPTReportVVORowImpl object, Object value);

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
    }


    public static final int PRODUCT = AttributesEnum.Product.index();
    public static final int MQCODE = AttributesEnum.MqCode.index();
    public static final int MQNAME = AttributesEnum.MqName.index();
    public static final int MQCONTENTID = AttributesEnum.MqContentId.index();
    public static final int PRIMARYHLTCODE = AttributesEnum.PrimaryHltCode.index();
    public static final int PRIMARYHLTTERM = AttributesEnum.PrimaryHltTerm.index();
    public static final int PRIMARYHLTCONTENTID = AttributesEnum.PrimaryHltContentId.index();
    public static final int NEWPTCODE = AttributesEnum.NewPtCode.index();
    public static final int NEWPTTERM = AttributesEnum.NewPtTerm.index();
    public static final int NEWPTCONTENTID = AttributesEnum.NewPtContentId.index();
    public static final int FORMERPTCODE = AttributesEnum.FormerPtCode.index();
    public static final int FORMERPTTERM = AttributesEnum.FormerPtTerm.index();
    public static final int FORMERPTCONTENTID = AttributesEnum.FormerPtContentId.index();
    public static final int RELATEDLLTCODE = AttributesEnum.RelatedLltCode.index();
    public static final int RELATEDLLTTERM = AttributesEnum.RelatedLltTerm.index();
    public static final int RELATEDLLTCONTENTID = AttributesEnum.RelatedLltContentId.index();
    public static final int HLTPTLLTLOADTS = AttributesEnum.HltPtLltLoadTs.index();
    public static final int REPORTTYPE = AttributesEnum.ReportType.index();

    /**
     * This is the default constructor (do not remove).
     */
    public NewPTReportVVORowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute Product.
     * @return the Product
     */
    public String getProduct() {
        return (String) getAttributeInternal(PRODUCT);
    }

    /**
     * Gets the attribute value for the calculated attribute MqCode.
     * @return the MqCode
     */
    public String getMqCode() {
        return (String) getAttributeInternal(MQCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute MqName.
     * @return the MqName
     */
    public String getMqName() {
        return (String) getAttributeInternal(MQNAME);
    }

    /**
     * Gets the attribute value for the calculated attribute MqContentId.
     * @return the MqContentId
     */
    public Number getMqContentId() {
        return (Number) getAttributeInternal(MQCONTENTID);
    }

    /**
     * Gets the attribute value for the calculated attribute PrimaryHltCode.
     * @return the PrimaryHltCode
     */
    public String getPrimaryHltCode() {
        return (String) getAttributeInternal(PRIMARYHLTCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute PrimaryHltTerm.
     * @return the PrimaryHltTerm
     */
    public String getPrimaryHltTerm() {
        return (String) getAttributeInternal(PRIMARYHLTTERM);
    }

    /**
     * Gets the attribute value for the calculated attribute PrimaryHltContentId.
     * @return the PrimaryHltContentId
     */
    public Number getPrimaryHltContentId() {
        return (Number) getAttributeInternal(PRIMARYHLTCONTENTID);
    }

    /**
     * Gets the attribute value for the calculated attribute NewPtCode.
     * @return the NewPtCode
     */
    public String getNewPtCode() {
        return (String) getAttributeInternal(NEWPTCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute NewPtTerm.
     * @return the NewPtTerm
     */
    public String getNewPtTerm() {
        return (String) getAttributeInternal(NEWPTTERM);
    }

    /**
     * Gets the attribute value for the calculated attribute NewPtContentId.
     * @return the NewPtContentId
     */
    public Number getNewPtContentId() {
        return (Number) getAttributeInternal(NEWPTCONTENTID);
    }

    /**
     * Gets the attribute value for the calculated attribute FormerPtCode.
     * @return the FormerPtCode
     */
    public String getFormerPtCode() {
        return (String) getAttributeInternal(FORMERPTCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute FormerPtTerm.
     * @return the FormerPtTerm
     */
    public String getFormerPtTerm() {
        return (String) getAttributeInternal(FORMERPTTERM);
    }

    /**
     * Gets the attribute value for the calculated attribute FormerPtContentId.
     * @return the FormerPtContentId
     */
    public Number getFormerPtContentId() {
        return (Number) getAttributeInternal(FORMERPTCONTENTID);
    }

    /**
     * Gets the attribute value for the calculated attribute RelatedLltCode.
     * @return the RelatedLltCode
     */
    public String getRelatedLltCode() {
        return (String) getAttributeInternal(RELATEDLLTCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute RelatedLltTerm.
     * @return the RelatedLltTerm
     */
    public String getRelatedLltTerm() {
        return (String) getAttributeInternal(RELATEDLLTTERM);
    }

    /**
     * Gets the attribute value for the calculated attribute RelatedLltContentId.
     * @return the RelatedLltContentId
     */
    public Number getRelatedLltContentId() {
        return (Number) getAttributeInternal(RELATEDLLTCONTENTID);
    }

    /**
     * Gets the attribute value for the calculated attribute HltPtLltLoadTs.
     * @return the HltPtLltLoadTs
     */
    public Date getHltPtLltLoadTs() {
        return (Date) getAttributeInternal(HLTPTLLTLOADTS);
    }

    /**
     * Gets the attribute value for the calculated attribute ReportType.
     * @return the ReportType
     */
    public String getReportType() {
        return (String) getAttributeInternal(REPORTTYPE);
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

