package com.dbms.csmq.model.service.common;

import org.eclipse.persistence.sdo.SDODataObject;

public class ViewObjTMSQuerySDOImpl extends SDODataObject implements ViewObjTMSQuerySDO {

   public static final int START_PROPERTY_INDEX = 0;

   public static final int END_PROPERTY_INDEX = START_PROPERTY_INDEX + 19;

   public ViewObjTMSQuerySDOImpl() {}

   public java.lang.String getPrikey() {
      return getString(START_PROPERTY_INDEX + 0);
   }

   public void setPrikey(java.lang.String value) {
      set(START_PROPERTY_INDEX + 0 , value);
   }

   public java.lang.String getParent() {
      return getString(START_PROPERTY_INDEX + 1);
   }

   public void setParent(java.lang.String value) {
      set(START_PROPERTY_INDEX + 1 , value);
   }

   public java.math.BigDecimal getDictContentId() {
      return getBigDecimal(START_PROPERTY_INDEX + 2);
   }

   public void setDictContentId(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 2 , value);
   }

   public java.lang.String getTerm() {
      return getString(START_PROPERTY_INDEX + 3);
   }

   public void setTerm(java.lang.String value) {
      set(START_PROPERTY_INDEX + 3 , value);
   }

   public java.math.BigDecimal getLevel() {
      return getBigDecimal(START_PROPERTY_INDEX + 4);
   }

   public void setLevel(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 4 , value);
   }

   public java.lang.String getLevelName() {
      return getString(START_PROPERTY_INDEX + 5);
   }

   public void setLevelName(java.lang.String value) {
      set(START_PROPERTY_INDEX + 5 , value);
   }

   public java.lang.String getDictShortName() {
      return getString(START_PROPERTY_INDEX + 6);
   }

   public void setDictShortName(java.lang.String value) {
      set(START_PROPERTY_INDEX + 6 , value);
   }

   public java.lang.String getDictContentAltCode() {
      return getString(START_PROPERTY_INDEX + 7);
   }

   public void setDictContentAltCode(java.lang.String value) {
      set(START_PROPERTY_INDEX + 7 , value);
   }

   public java.lang.String getApprovedFlag() {
      return getString(START_PROPERTY_INDEX + 8);
   }

   public void setApprovedFlag(java.lang.String value) {
      set(START_PROPERTY_INDEX + 8 , value);
   }

   public java.lang.String getStatus() {
      return getString(START_PROPERTY_INDEX + 9);
   }

   public void setStatus(java.lang.String value) {
      set(START_PROPERTY_INDEX + 9 , value);
   }

   public java.lang.String getPrimLinkFlag() {
      return getString(START_PROPERTY_INDEX + 10);
   }

   public void setPrimLinkFlag(java.lang.String value) {
      set(START_PROPERTY_INDEX + 10 , value);
   }

   public java.math.BigDecimal getTermlvl() {
      return getBigDecimal(START_PROPERTY_INDEX + 11);
   }

   public void setTermlvl(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 11 , value);
   }

   public java.math.BigDecimal getTermscp() {
      return getBigDecimal(START_PROPERTY_INDEX + 12);
   }

   public void setTermscp(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 12 , value);
   }

   public java.lang.String getTermcat() {
      return getString(START_PROPERTY_INDEX + 13);
   }

   public void setTermcat(java.lang.String value) {
      set(START_PROPERTY_INDEX + 13 , value);
   }

   public java.lang.String getTermweig() {
      return getString(START_PROPERTY_INDEX + 14);
   }

   public void setTermweig(java.lang.String value) {
      set(START_PROPERTY_INDEX + 14 , value);
   }

   public java.math.BigDecimal getPredictGroupId() {
      return getBigDecimal(START_PROPERTY_INDEX + 15);
   }

   public void setPredictGroupId(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 15 , value);
   }

   public java.math.BigDecimal getFormattedScope() {
      return getBigDecimal(START_PROPERTY_INDEX + 16);
   }

   public void setFormattedScope(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 16 , value);
   }

   public java.lang.String getTermPath() {
      return getString(START_PROPERTY_INDEX + 17);
   }

   public void setTermPath(java.lang.String value) {
      set(START_PROPERTY_INDEX + 17 , value);
   }

   public java.math.BigDecimal getSortOrder() {
      return getBigDecimal(START_PROPERTY_INDEX + 18);
   }

   public void setSortOrder(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 18 , value);
   }

   public java.lang.String getChildExists() {
      return getString(START_PROPERTY_INDEX + 19);
   }

   public void setChildExists(java.lang.String value) {
      set(START_PROPERTY_INDEX + 19 , value);
   }


}

