package com.dbms.csmq.view.backing.NMQ;


import com.dbms.csmq.CSMQBean;
import com.dbms.util.dml.DMLUtils;

//import java.sql.CallableStatement;
import com.dbms.util.logged.CallableStatement;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Hashtable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.jbo.server.DBTransaction;

import oracle.jdbc.OracleTypes;


public class NMQUtils {
    public NMQUtils() {
        super();
    }
    
    
    public static boolean delete (String dictContentID, String predictGroupName) {

       /*  
       PROCEDURE cleanup_predict
           (pPredictConID             IN tms.tms_predict_contents.predict_content_id%TYPE,
           pPredictGroupName           IN tms.tms_predict_groups.name%TYPE,
           pDelRelatOrConfirmPageInd  IN VARCHAR2,
           pNotesDeleteInd            IN VARCHAR2)

        pDelRelatOrConfirmPageInd: pass ‘C’ for Confirm page, ‘R’ for Relation Page
        
         */
        boolean retVal = false;
        
        CSMQBean.logger.info("DELETING: " + dictContentID + " FROM : " + predictGroupName); 
        String sql = "{call NMAT_UI_pkg.cleanup_predict(?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        FacesMessage msg;
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "NMAT_UI_pkg.cleanup_predict");

        try {
            cstmt.setString("pPredictConID", dictContentID);
            cstmt.setString("pPredictGroupName", predictGroupName);
            cstmt.setString("pDelRelatOrConfirmPageInd", "C");  // the UI only ever calls this from the confirm page
            cstmt.setString("pNotesDeleteInd", CSMQBean.TRUE);
            
            cstmt.executeUpdate();
            cstmt.close();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "MedDRA Query Deleted Successfully", null);
            retVal = true;
            } 
        catch (SQLException e) {
            CSMQBean.logger.error(e.getMessage()); 
            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Delete MedDRA Query", e.getMessage());
            retVal = false;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        FacesContext.getCurrentInstance().addMessage(null, msg); 
        return retVal;
        } 
    
    
    public static boolean activate (String dictContentID, String mQCode, String userRole, String userStatus) {
        
       /*  
      NMAT_UI_PKG.reactivate_retire_nmq (
                    pContent_code     IN tms_dict_contents.dict_content_code%TYPE,
                    pActivation_Group IN tms.tms_predict_groups.name%TYPE,
                    pStatus           IN tms_dict_contents.status%TYPE,
                    pUserRole         IN VARCHAR2,
                    pUserName         IN nmq_track_state.created_user%TYPE)

         */
        CSMQBean.logger.info("ACTIVATING: " + mQCode);
        CSMQBean.logger.info(CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"));
        String sql = "{call NMAT_UI_pkg.reactivate_retire_nmq(?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        FacesMessage msg;
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "NMAT_UI_pkg.reactivate_retire_nmq");
        boolean retVal = false;
        try {
            cstmt.setString("pContent_ID", dictContentID);
            cstmt.setString("pContent_code", mQCode);
            cstmt.setString("pActivation_Group", CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"));
            cstmt.setString("pStatus", CSMQBean.ACTIVE_ACTIVITY_STATUS);
            cstmt.setString("pUserRole", userRole);
            cstmt.setString("pUserName", userStatus);
            cstmt.executeUpdate();
            cstmt.close();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "MedDRA Query Ractivated Successfully", null);
            retVal = true;
            } 
        catch (SQLException e) {
                if (e.getMessage().indexOf(CSMQBean.INVALID_PROMOTION_ERROR) > -1) {  
                    String messageText = CSMQBean.getProperty("ACTIVATE_DURING_IMPACT_ANALYSYS");
                    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
                    }
                else {
                    CSMQBean.logger.error(e.getMessage());
                    e.printStackTrace();
                    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Activate MedDRA Query", e.getMessage());
                    }
                retVal = false;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return retVal;
        }   
    
    public static boolean retire (String dictContentID, String mQCode, String userRole, String userStatus) {
        
       /*  
       NMAT_UI_PKG.reactivate_retire_nmq (
                    pContent_code     IN tms_dict_contents.dict_content_code%TYPE,
                    pActivation_Group IN tms.tms_predict_groups.name%TYPE,
                    pStatus           IN tms_dict_contents.status%TYPE,
                    pUserRole         IN VARCHAR2,
                    pUserName         IN nmq_track_state.created_user%TYPE)

         */
        CSMQBean.logger.info(" *** RETIRING: " + mQCode);
        CSMQBean.logger.info(CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"));
        String sql = "{call NMAT_UI_pkg.reactivate_retire_nmq(?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        FacesMessage msg;
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "NMAT_UI_pkg.reactivate_retire_nmq");
        boolean retVal = false;
        try {
            cstmt.setString("pContent_ID", dictContentID);
            cstmt.setString("pContent_code", mQCode);
            cstmt.setString("pActivation_Group", CSMQBean.getProperty("DEFAULT_DRAFT_RELEASE_GROUP"));
            cstmt.setString("pStatus", CSMQBean.INACTIVE_ACTIVITY_STATUS);
            cstmt.setString("pUserRole", userRole);
            cstmt.setString("pUserName", userStatus);
            cstmt.executeUpdate();
            cstmt.close();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "MedDRA Query Retired Successfully", null);
            retVal = true;
            } 
        catch (SQLException e) {
            if (e.getMessage().indexOf(CSMQBean.INVALID_PROMOTION_ERROR) > -1) {  
                String messageText = CSMQBean.getProperty("DELETE_DURING_IMPACT_ANALYSYS");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
                }
            else {
                CSMQBean.logger.error(e.getMessage());
                e.printStackTrace();
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retire " + CSMQBean.customMQName, e.getMessage());
                }
            retVal = false;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return retVal;
        }
    
    
    public static void setCurrentFilterDictionary (String shortName)   {
           String sql = "{call smq_codelist_pkg.set_database_customizations (?)}";
           DBTransaction dBTransaction = DMLUtils.getDBTransaction();
           CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "smq_codelist_pkg.set_database_customizations");
           try {
               cstmt.setString(1, shortName);
               cstmt.executeUpdate();
               cstmt.close();
                } 
           catch ( Exception e) {
               CSMQBean.logger.error(e.getMessage());
               e.printStackTrace();
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
       }
    
    
    public static Hashtable changeState (String dictContentIDs, String state, String currentUser, String currentUserRole, oracle.jbo.domain.Date dueDate, String comment, String activationGroup) {
        return  changeState (dictContentIDs, state, currentUser, currentUserRole, dueDate, comment, activationGroup, true); 
        }
    
    
    public static Hashtable changeState (String dictContentIDs, String state, String currentUser, String currentUserRole, oracle.jbo.domain.Date dueDate, String comment, String activationGroup, boolean showMessage) {
        
       /*
        PROCEDURE workflow_pkg.change_state_of_nmq (
          i_dict_content_id IN tms_predict_contents.predict_content_id%TYPE,
          i_new_state       IN nmq_track_state.state%TYPE,
          i_user_id         IN nmq_track_state.created_user%TYPE,
          i_due_date        IN nmq_track_state.due_date_ts%TYPE,
          i_comment         IN nmq_track_state.description%TYPE,
          i_group_name      IN tms.tms_predict_groups.name%TYPE)
          o_new_state        OUT nmq_track_state.state%TYPE,
          o_approval_reason  OUT nmq_track_state.description%TYPE);
          
        */
        
        CSMQBean.logger.info("*** CHANGING STATE ***");
        CSMQBean.logger.info("i_dict_content_id: " + dictContentIDs);
        CSMQBean.logger.info("i_new_state: " + state); 
        CSMQBean.logger.info("i_user_id: " + currentUser);
        CSMQBean.logger.info("i_due_date: " + dueDate);
        CSMQBean.logger.info("i_comment: " + comment);
        CSMQBean.logger.info("i_group_name: " + activationGroup);
        
        
        String sql = "{call workflow_pkg.change_state_of_nmq(?,?,?,?,?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        FacesMessage msg;
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "workflow_pkg.change_state_of_nmq");
        String messageText;
        Hashtable retVal = new Hashtable();  // array to return the new state and message
        
        try {
            cstmt.setString("i_dict_content_id", dictContentIDs);
            cstmt.setString("i_new_state", state);
            cstmt.setString("i_user_id", currentUser);
            cstmt.setString("i_role_name", currentUserRole); 
            
            if (dueDate == null) 
                cstmt.setNull("i_due_date", Types.DATE);
            else
                cstmt.setDate("i_due_date",  dueDate.dateValue());
            
            cstmt.setString("i_comment", comment);
            cstmt.setString("i_group_name", activationGroup);
            cstmt.setNull("i_transaction_date", Types.DATE);
            
            cstmt.setString("o_new_state", "");
            cstmt.registerOutParameter("o_new_state", Types.NVARCHAR);
            
            cstmt.setString("o_approval_reason", "");
            cstmt.registerOutParameter("o_approval_reason", Types.NVARCHAR);
            
            cstmt.executeUpdate();
            
            String tempState = cstmt.getString("o_new_state");
            String tempReason = cstmt.getString("o_approval_reason");
            
            if (tempState != null)
                retVal.put("STATE", tempState);
            if (tempReason != null)
            retVal.put("REASON",tempReason);

            cstmt.close();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "MedDRA Query State Changed Successfully to " + state, null);
            } 
        catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage().indexOf(CSMQBean.INVALID_PROMOTION_ERROR) > -1) {  
                messageText = CSMQBean.getProperty("INVALID_PROMOTION_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.INVALID_PROMOTION_SEQUENCE_ERROR) > -1) {
                messageText = CSMQBean.getProperty("INVALID_PROMOTION_SEQUENCE_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.PROMOTION_DEPENDENCY_ERROR) > -1) {
                messageText = CSMQBean.getProperty("PROMOTION_DEPENDENCY_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.INVALID_STATE_CHANGE_ERROR) > -1) {
                messageText =  e.getMessage(); //CSMQBean.getProperty("INVALID_STATE_CHANGE_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.MUST_BE_NMQ_OR_SMQ_ERROR) > -1) {
                messageText = CSMQBean.getProperty("MUST_BE_NMQ_OR_SMQ_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.GENERIC_ACTIVATION_ERROR) > -1) {
                messageText = CSMQBean.getProperty("GENERIC_ACTIVATION_ERROR") + "DETAILS: " + e.getMessage();
                }
            else if (e.getMessage().indexOf(CSMQBean.DATABASE_CONFIGURATION_ERROR) > -1) {
                messageText = CSMQBean.getProperty("DATABASE_CONFIGURATION_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.INVALID_STATE_CHANGE_FROM_PENDING_TO_DRAFT_ERROR) > -1) {
                messageText = CSMQBean.getProperty("INVALID_STATE_CHANGE_FROM_PENDING_TO_DRAFT_ERROR");
                }    
            
            else { // it's something else
                messageText = "The following error occurred: " +  e.getMessage() ;
                e.printStackTrace();
                }
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        if (showMessage) FacesContext.getCurrentInstance().addMessage(null, msg);
        return retVal;
    }
    
    
    public static Hashtable changeStateFromDraftToPublish (String dictContentIDs, String state, String currentUser, String currentUserRole, oracle.jbo.domain.Date dueDate, String comment, String activationGroup) {
        
       /*
       PROCEDURE workflow_pkg.change_state_draft_to_publish (
                 i_dict_content_id IN tms_predict_contents.predict_content_id%TYPE,
                 i_new_state       IN nmq_track_state.state%TYPE,
                 i_user_id         IN nmq_track_state.created_user%TYPE,
                 i_role_name        IN user_role_privs.granted_role%TYPE,
                 i_due_date        IN nmq_track_state.due_date_ts%TYPE,
                 i_comment         IN nmq_track_state.description%TYPE,
                 i_group_name      IN tms.tms_predict_groups.name%TYPE,
                 o_new_state        OUT nmq_track_state.state%TYPE,
                 o_approval_reason  OUT nmq_track_state.description%TYPE)

          
        */
        
        state = "Published";
        
        CSMQBean.logger.info("*** CHANGING STATE FROM DRAFT TO PUBLISH ***");
        CSMQBean.logger.info("i_dict_content_id: " + dictContentIDs);
        CSMQBean.logger.info("i_new_state: " + state); 
        CSMQBean.logger.info("i_user_id: " + currentUser);
        CSMQBean.logger.info("i_due_date: " + dueDate);
        CSMQBean.logger.info("i_comment: " + comment);
        CSMQBean.logger.info("i_group_name: " + activationGroup);
        
        
        String sql = "{call workflow_pkg.change_state_draft_to_publish(?,?,?,?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        FacesMessage msg;
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "workflow_pkg.change_state_draft_to_publish");
        String messageText;
        Hashtable retVal = new Hashtable();  // array to return the new state and message
        
        try {
            cstmt.setString("i_dict_content_id", dictContentIDs);
            cstmt.setString("i_new_state", state);
            cstmt.setString("i_user_id", currentUser);
            cstmt.setString("i_role_name", currentUserRole); 
            
            if (dueDate == null) 
                cstmt.setNull("i_due_date", Types.DATE);
            else
                cstmt.setDate("i_due_date",  dueDate.dateValue());
            
            cstmt.setString("i_comment", comment);
            cstmt.setString("i_group_name", activationGroup);
            //cstmt.setNull("i_transaction_date", Types.DATE);
            
            cstmt.setString("o_new_state", "");
            cstmt.registerOutParameter("o_new_state", Types.NVARCHAR);
            
            cstmt.setString("o_approval_reason", "");
            cstmt.registerOutParameter("o_approval_reason", Types.NVARCHAR);
            
            cstmt.executeUpdate();
            
            String tempState = cstmt.getString("o_new_state");
            String tempReason = cstmt.getString("o_approval_reason");
            
            if (tempState != null)
                retVal.put("STATE", tempState);
            if (tempReason != null)
            retVal.put("REASON",tempReason);

            cstmt.close();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "MedDRA Query State Changed Successfully to " + state, null);
            } 
        catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage().indexOf(CSMQBean.INVALID_PROMOTION_ERROR) > -1) {  
                messageText = CSMQBean.getProperty("INVALID_PROMOTION_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.INVALID_PROMOTION_SEQUENCE_ERROR) > -1) {
                messageText = CSMQBean.getProperty("INVALID_PROMOTION_SEQUENCE_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.PROMOTION_DEPENDENCY_ERROR) > -1) {
                messageText = CSMQBean.getProperty("PROMOTION_DEPENDENCY_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.INVALID_STATE_CHANGE_ERROR) > -1) {
                messageText =  e.getMessage(); //CSMQBean.getProperty("INVALID_STATE_CHANGE_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.MUST_BE_NMQ_OR_SMQ_ERROR) > -1) {
                messageText = CSMQBean.getProperty("MUST_BE_NMQ_OR_SMQ_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.GENERIC_ACTIVATION_ERROR) > -1) {
                messageText = CSMQBean.getProperty("GENERIC_ACTIVATION_ERROR") + "DETAILS: " + e.getMessage();
                }
            else if (e.getMessage().indexOf(CSMQBean.DATABASE_CONFIGURATION_ERROR) > -1) {
                messageText = CSMQBean.getProperty("DATABASE_CONFIGURATION_ERROR");
                }
            else if (e.getMessage().indexOf(CSMQBean.INVALID_STATE_CHANGE_FROM_PENDING_TO_DRAFT_ERROR) > -1) {
                messageText = CSMQBean.getProperty("INVALID_STATE_CHANGE_FROM_PENDING_TO_DRAFT_ERROR");
                }    
            
            else { // it's something else
                messageText = "The following error occurred: " +  e.getMessage() ;
                e.printStackTrace();
                }
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return retVal;
    }
    
    
    public static Hashtable setDefaultIAState (String dictContentIDs, String nmqSmq, String currentUser, String currentUserRole) {
        
       /*
       PROCEDURE default_state_to_pending_ia (
         i_dict_content_id  IN VARCHAR2,
         i_nmq_smq          IN VARCHAR2,
         i_new_state        IN nmq_track_state.state%TYPE := g_state_pending,
         i_user_id          IN nmq_track_state.created_user%TYPE,
         i_role_name        IN user_role_privs.granted_role%TYPE,
         i_due_date         IN nmq_track_state.due_date_ts%TYPE := NULL,
         i_comment          IN nmq_track_state.description%TYPE := NULL,
         i_group_name       IN tms.tms_predict_groups.NAME%TYPE := NULL,
         i_transaction_date IN nmq_track_state.creation_ts%TYPE := NULL,
         o_new_state        OUT nmq_track_state.state%TYPE,
         o_approval_reason  OUT nmq_track_state.description%TYPE);


       PROCEDURE change_state_of_nmq (
         i_dict_content_id  IN VARCHAR2,
         i_new_state        IN nmq_track_state.state%TYPE,
         i_user_id          IN nmq_track_state.created_user%TYPE,
         i_role_name        IN user_role_privs.granted_role%TYPE,
         i_due_date         IN nmq_track_state.due_date_ts%TYPE,
         i_comment          IN nmq_track_state.description%TYPE,
         i_group_name       IN tms.tms_predict_groups.NAME%TYPE,
         i_transaction_date IN nmq_track_state.creation_ts%TYPE := NULL,
         o_new_state        OUT nmq_track_state.state%TYPE,
         o_approval_reason  OUT nmq_track_state.description%TYPE) IS
        */
        
        CSMQBean.logger.info("*** CHANGING STATE ***");
        CSMQBean.logger.info("i_dict_content_id: " + dictContentIDs);
       // CSMQBean.logger.info("i_nmq_smq: " + nmqSmq); 
        CSMQBean.logger.info("i_user_id: " + currentUser);
        CSMQBean.logger.info("i_role_name: " + currentUserRole);
        CSMQBean.logger.info("i_new_state: " + CSMQBean.STATE_PENDING_IMPACT_ASSESSMENT);
        
       // String sql = "{call workflow_pkg.default_state_to_pending_ia(?,?,?,?,?,?,?,?,?,?,?)}";
       String sql = "{call workflow_pkg.change_state_of_nmq(?,?,?,?,?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        FacesMessage msg;
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "workflow_pkg.change_state_of_nmq");
        String messageText;
        Hashtable retVal = new Hashtable();  // array to return the new state and message
        
        try {
            cstmt.setString("i_dict_content_id", dictContentIDs);
           // cstmt.setString("i_nmq_smq", nmqSmq);
            cstmt.setString("i_user_id", currentUser);
            cstmt.setString("i_role_name", currentUserRole);
            cstmt.setString("i_new_state", CSMQBean.STATE_PENDING_IMPACT_ASSESSMENT);
           // cstmt.setNull("i_new_state", Types.VARCHAR);
            cstmt.setNull("i_due_date", Types.DATE);
            cstmt.setString("i_comment", "Updated via NMAT UI DML");
            cstmt.setNull("i_group_name", Types.VARCHAR);
            cstmt.setNull("i_transaction_date", Types.DATE);
            
            cstmt.setString("o_new_state", "");
            cstmt.registerOutParameter("o_new_state", Types.VARCHAR);
            
            cstmt.setString("o_approval_reason", "");
            cstmt.registerOutParameter("o_approval_reason", Types.VARCHAR);
            
            cstmt.executeUpdate();
            
            String tempState = cstmt.getString("o_new_state");
            String tempReason = cstmt.getString("o_approval_reason");
            
            if (tempState != null)
                retVal.put("STATE", tempState);
            if (tempReason != null)
            retVal.put("REASON",tempReason);
            cstmt.close();
            return retVal;
            }  
        catch (SQLException e) {
            CSMQBean.logger.error(null, e);
            return null;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        
        }
    
    
    /*
     * @author MTW
     * 06/30/2014
     * @fsds NMAT-UC01.02 & NMAT-UC11.02
     * add String currentDesignee
     */
    public static Hashtable saveDetails (String currentFilterDictionaryShortName, String currentPredictGroups, String tempName, 
                                         String currentProduct, String currentTermLevel, String currentScope, String currentMQALGO, 
                                         String currentMQCRTEV, String currentMQGROUP, String currentContentCode, String updateParam, 
                                         String currentRequestor, String currentDictContentID, String userRole, String action, String currentPendingStatus, String currentDesignee) {
        
       // CALL PROC TO SAVE
        /*
        PROCEDURE nmat_ui_pkg.load_content (
            psmq_dct_nm         IN tms.tms_def_dictionaries.short_name%TYPE,
            psmq_grp_nm         IN tms.tms_predict_groups.name%TYPE,
            pmq_name            IN tms.tms_predict_contents.term%TYPE,
            pmq_product         IN tms.tms_predict_contents.value_3%TYPE,
            pmq_level           IN tms.tms_def_levels.name%TYPE,
            pmq_scp             IN tms.tms_predict_contents.category%TYPE,
            pmq_algo            IN tms.tms_predict_contents.value_1%TYPE,
            pmq_crtev           IN tms.tms_predict_contents.value_4%TYPE,
            pmq_group           IN tms.tms_predict_contents.value_2%TYPE,
            pmqcd               IN tms.tms_predict_contents.dict_content_code%TYPE,
            pstatus             IN tms.tms_predict_contents.status%TYPE, --I, U
            pUserName           IN tms.tms_predict_contents.created_by%TYPE,
            pContentID          IN tms.tms_predict_contents.predict_content_id%TYPE,
            pUserRole           IN  VARCHAR2,
            pCurrPendStatus     IN  VARCHAR2,
            o_dict_content_id   OUT tms_predict_contents.predict_content_id%TYPE,
            o_dict_content_code OUT tms_predict_contents.dict_content_code%TYPE,
            o_Create_date       OUT tms_predict_contents.dict_content_entry_ts%TYPE
            )
        */

        String sql = "{call nmat_ui_pkg.load_content(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        String newDictContentID = "";
        String newDictContentCode = "";
        oracle.jbo.domain.Date currentDateRequested = null;
        Hashtable retVal = new Hashtable();  // array to return the new state and message
        
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "nmat_ui_pkg.load_content");

        try {
            cstmt.setString("psmq_dct_nm", currentFilterDictionaryShortName);
            cstmt.setString("psmq_grp_nm", currentPredictGroups);
            cstmt.setString("pmq_name", tempName);
            cstmt.setString("pmq_product", currentProduct);
            cstmt.setString("pmq_level", currentTermLevel);
            cstmt.setString("pmq_scp", currentScope);
            cstmt.setString("pmq_algo", currentMQALGO);
            cstmt.setString("pmq_crtev", currentMQCRTEV);
            cstmt.setString("pmq_group", currentMQGROUP);
            cstmt.setString("pmqcd", currentContentCode);
            cstmt.setString("pstatus", updateParam);
            cstmt.setString("pUserName", currentRequestor);
            cstmt.setString("pContentID", currentDictContentID);
            cstmt.setString("pUserRole", userRole);
            cstmt.setString("pCurrPendStatus", currentPendingStatus);
            cstmt.setString("o_dict_content_id", "");
            cstmt.registerOutParameter("o_dict_content_id", Types.NVARCHAR);
            cstmt.setString("o_dict_content_code", "");
            cstmt.registerOutParameter("o_dict_content_code", Types.NVARCHAR);
            cstmt.setString("o_Create_date", "");
            cstmt.registerOutParameter("o_Create_date", Types.NVARCHAR);
            
            /*
             * @author MTW
             * 07/22/2014
             */
            //saveDesignee(currentDictContentID, currentDesignee);
            /*
             * TES TEMP CHANGE TO CALL FUNCTION BELOW
             * 
             */
            
            
            cstmt.executeUpdate();
            newDictContentID = cstmt.getString("o_dict_content_id");
            newDictContentCode = cstmt.getString("o_dict_content_code");
            String tempDate = cstmt.getString("o_Create_date");
  
            updateMQDesignee(newDictContentID, currentDesignee);
  
            //DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
            String dateFormat = "dd-MMM-yy";
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            java.util.Date date = null;
            //java.util.Date date = formatter.parse(tempDate);
            //20141121-2  fix -Venkat
            try{
                date = formatter.parse(tempDate);
            } catch (java.text.ParseException e){
                dateFormat = "dd-MM-yy";
                formatter = new SimpleDateFormat(dateFormat);
                date = formatter.parse(tempDate);
            }
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            currentDateRequested = new oracle.jbo.domain.Date(sqlDate);
            cstmt.close();
            
            if (newDictContentID != null) retVal.put("NEW_DICT_CONTENT_ID", newDictContentID);
            if (newDictContentCode != null) retVal.put("NEW_DICT_CONTENT_CODE", newDictContentCode);
            if (currentDateRequested != null) retVal.put("CURRENT_DATE_REQUESTED", currentDateRequested);
            } 
        catch (SQLException e) {
            String messageText;
                CSMQBean.logger.error("error in save Details..."+e.getMessage()); 
            if (e.getMessage().indexOf(CSMQBean.NAME_IN_USE_ERROR) > -1 || e.getMessage().indexOf(CSMQBean.NAME_IN_USE_ERROR_NEW) > -1) {  // it's a name already in use error
                //messageText = "The name: " + tempName + " is already in use.  Please use another name";
                messageText = "This MQ already exists.  Please modify the MQ name or use the MQ, which already exists by this name."; 
            }
            else if (e.getMessage().indexOf(CSMQBean.INVALID_STATE_CHANGE_ERROR) > -1) {  // it's a name already in use error
                messageText = tempName + " is Pending Impact Assessment and must be deleted in Impact Assessment to Update the Current NMQ.";
                }
            else { // it's something else
                messageText = "The following error occurred.  " + tempName + " was not " + action + " successfully.\n" + e.getMessage() ;
                e.printStackTrace();
                }
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
            }
        catch (ParseException e) {
            CSMQBean.logger.error(null,e);
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        return retVal;
        }
    
    private static void saveDesignee(String currentDictContentID, String currentDesignee) {
        /*
         * TODO: IMPLEMENT THIS IN DATABASE INSTEAD OF CALLING SQL BELOW
         * 
         */
        /*
         PROCEDURE NMAT_UI_PKG.s_insert_update_designee (
            pPredictConID      IN tms.tms_predict_contents.predict_content_id%TYPE,
            pDesignee          IN track_designee.designee%TYPE
        )
        */
        
        String sql = "{call NMAT_UI_PKG.s_insert_update_designee(?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        String newDictContentID = "";
        String newDictContentCode = "";
        Hashtable retVal = new Hashtable();  // array to return the new state and message
        
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "NMAT_UI_PKG.s_insert_update_designee");

        try {
            cstmt.setString("pPredictConID", currentDictContentID);
            cstmt.setString("pDesignee", currentDesignee);
            
            cstmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
//        catch (ParseException e) {
//            CSMQBean.logger.error(null,e);
//        }
    }
    
    public static Hashtable saveIADetails (String tempName, String currentProduct, String currentTermLevel, String currentScope, String currentMQALGO, 
                                         String currentMQCRTEV, String currentMQGROUP, String currentContentCode, String updateParam, 
                                         String currentRequestor, String currentDictContentID, String userRole, String action, String currentDesignee) {
        
       // CALL PROC TO SAVE
        /*
       PROCEDURE nmat_ui_pkg.load_content_IA
         (pmq_name            IN tms.tms_predict_contents.term%TYPE,
          pmq_product         IN tms.tms_predict_contents.value_3%TYPE,
          pmq_level           IN tms.tms_def_levels.name%TYPE,
          pmq_scp             IN tms.tms_predict_contents.category%TYPE,
          pmq_algo            IN tms.tms_predict_contents.value_1%TYPE,
          pmq_crtev           IN tms.tms_predict_contents.value_4%TYPE,
          pmq_group           IN tms.tms_predict_contents.value_2%TYPE,
          pmqcd               IN tms.tms_predict_contents.dict_content_code%TYPE,
          pstatus             IN tms.tms_predict_contents.status%TYPE, --I, U
          pUserName           IN tms.tms_predict_contents.created_by%TYPE,
          pContentID          IN tms.tms_predict_contents.predict_content_id%TYPE,
          pUserRole           IN  VARCHAR2, 
          o_dict_content_id   OUT tms_predict_contents.predict_content_id%TYPE,
          o_dict_content_code OUT tms_predict_contents.dict_content_code%TYPE,
          o_Create_date       OUT tms_predict_contents.dict_content_entry_ts%TYPE
         ) ;
        */

        String sql = "{call nmat_ui_pkg.load_content_IA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        String newDictContentID = "";
        String newDictContentCode = "";
        oracle.jbo.domain.Date currentDateRequested = null;
        Hashtable retVal = new Hashtable();  // array to return the new state and message
        
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "nmat_ui_pkg.load_content_IA");

        try {
            cstmt.setString("pmq_name", tempName);
            cstmt.setString("pmq_product", currentProduct);
            cstmt.setString("pmq_level", currentTermLevel);
            cstmt.setString("pmq_scp", currentScope);
            cstmt.setString("pmq_algo", currentMQALGO);
            cstmt.setString("pmq_crtev", currentMQCRTEV);
            cstmt.setString("pmq_group", currentMQGROUP);
            cstmt.setString("pmqcd", currentContentCode);
            cstmt.setString("pstatus", updateParam);
            cstmt.setString("pUserName", currentRequestor);
            cstmt.setString("pContentID", currentDictContentID);
            cstmt.setString("pUserRole", userRole);
            cstmt.setString("o_dict_content_id", "");
            cstmt.registerOutParameter("o_dict_content_id", Types.NVARCHAR);
            cstmt.setString("o_dict_content_code", "");
            cstmt.registerOutParameter("o_dict_content_code", Types.NVARCHAR);
            cstmt.setString("o_Create_date", "");
            cstmt.registerOutParameter("o_Create_date", Types.NVARCHAR);
            
            cstmt.executeUpdate();
            newDictContentID = cstmt.getString("o_dict_content_id");
            newDictContentCode = cstmt.getString("o_dict_content_code");
            String tempDate = cstmt.getString("o_Create_date");
            
            updateMQDesignee(newDictContentID, currentDesignee);
            //DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
            String dateFormat = "dd-MMM-yy";
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            java.util.Date date = null;
            //java.util.Date date = formatter.parse(tempDate);
            //20141121-2  fix -Venkat
            try{
                date = formatter.parse(tempDate);
            } catch (java.text.ParseException e){
                dateFormat = "dd-MM-yy";
                formatter = new SimpleDateFormat(dateFormat);
                date = formatter.parse(tempDate);
            }
            
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            currentDateRequested = new oracle.jbo.domain.Date(sqlDate);
            cstmt.close();
            
            if (newDictContentID != null) retVal.put("NEW_DICT_CONTENT_ID", newDictContentID);
            if (newDictContentCode != null) retVal.put("NEW_DICT_CONTENT_CODE", newDictContentCode);
            if (currentDateRequested != null) retVal.put("CURRENT_DATE_REQUESTED", currentDateRequested);
            } 
        catch (SQLException e) {
            String messageText;
            CSMQBean.logger.error("error in save Details..."+e.getMessage()); 
            if (e.getMessage().indexOf(CSMQBean.NAME_IN_USE_ERROR) > -1 || e.getMessage().indexOf(CSMQBean.NAME_IN_USE_ERROR_NEW) > -1) {  // it's a name already in use error
            //messageText = "The name: " + tempName + " is already in use.  Please use another name";
            messageText = "This MQ already exists.  Please modify the MQ name or use the MQ, which already exists by this name."; 
            }
            else { // it's something else
                messageText = "The following error occurred.  " + tempName + " was not " + action + " successfully.\n" + e.getMessage() ;
                e.printStackTrace();
                }
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
            }
        catch (ParseException e) {
            CSMQBean.logger.error(null,e);
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        return retVal;
        }
    
    
    public static int saveInfNotes (String noteName, String content, String currentFilterDictionaryShortName, String currentPredictGroups, String currentDictContentID, String currentTermLevel, String userName, String userRole, String action, String extension) {
        /*
        PROCEDURE nmat_ui_pkg.load_notes
             (psmq_dct_nm        IN tms.tms_def_dictionaries.short_name%TYPE,
              psmq_grp_nm        IN tms.tms_predict_groups.name%TYPE,
              pContent_Id        IN tms.tms_predict_contents.predict_content_id%TYPE,
              pmq_level          IN tms.tms_def_levels.name%TYPE,
              pnote_nm           IN tms.tms_def_details.short_name%TYPE, --SMQDESC
              pnote_type         IN tms.tms_def_dict_info_dets.info_note_type%TYPE,--C=Content; R=Relations; D=Dictionary
              pnote_val          IN tms.tms_dict_info_clobs.def_detail_value%TYPE,
              pUserRole          IN VARCHAR2,
              pUserName          IN nmq_track_state.created_user%TYPE,
              pTermType          IN VARCHAR2
              ) ;


        */
        CSMQBean.logger.info("SAVING NOTE: " + noteName);

        String sql = "{call nmat_ui_pkg.load_notes (?,?,?,?,?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        String newDictContentCode = "";
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "nmat_ui_pkg.load_notes");
        try {
            cstmt.setString("psmq_dct_nm", currentFilterDictionaryShortName);
            cstmt.setString("psmq_grp_nm", currentPredictGroups);
            cstmt.setString("pContent_Id", currentDictContentID);
            cstmt.setString("pmq_level", currentTermLevel);
            cstmt.setString("pnote_nm", noteName);
            cstmt.setString("pnote_type", "C");
            cstmt.setString("pnote_val", content);
            cstmt.setString("pUserRole", userRole);
            cstmt.setString("pUserName", userName);
            cstmt.setString("pTermType", extension);
            cstmt.executeUpdate();
            cstmt.close();
            } 
        catch (SQLException e) {
            String messageText = "An error occurred.  " + noteName + " was not " + action + " successfully.";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
            return 1;
        } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        return 0;
    }
    
    
    public static int saveIAInfNotes (String noteName, String content, String currentDictContentID, String currentTermLevel, String userName, String userRole, String action, String extension) {
        /*
        PROCEDURE load_notes_IA
                 (pContent_Id        IN tms.tms_predict_contents.predict_content_id%TYPE,
                  pmq_level          IN tms.tms_def_levels.name%TYPE,
                  pnote_nm           IN tms.tms_def_details.short_name%TYPE, --SMQDESC SMQALG
                  pnote_type         IN tms.tms_def_dict_info_dets.info_note_type%TYPE,
                  pnote_val          IN tms.tms_dict_info_clobs.def_detail_value%TYPE,
                  pUserRole          IN VARCHAR2,
                  pUserName          IN nmq_track_state.created_user%TYPE)
        */
        CSMQBean.logger.info("SAVING IA NOTE: " + noteName);

        String sql = "{call nmat_ui_pkg.load_notes_IA (?,?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        String newDictContentCode = "";
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "nmat_ui_pkg.load_notes_IA");
        try {
            cstmt.setString("pContent_Id", currentDictContentID);
            cstmt.setString("pmq_level", currentTermLevel);
            cstmt.setString("pnote_nm", noteName);
            cstmt.setString("pnote_type", "C");
            cstmt.setString("pnote_val", content);
            cstmt.setString("pUserRole", userRole);
            cstmt.setString("pUserName", userName);
            cstmt.executeUpdate();
            cstmt.close();
            } 
        catch (SQLException e) {
            String messageText = "An error occurred.  " + noteName + " was not " + action + " successfully.";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
            return 1;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        return 0;
    }
    

    
    
    public static void activateGroup (String groupName)   {
           /*
             PROCEDURE nmat_ui_pkg.nmat_activation_chkmode
                (pGrpName IN tms.tms_predict_groups.Name%TYPE)
            */
           String messageText;
           FacesMessage msg;
           String sql = "{call nmat_ui_pkg.nmat_transid_activation (?)}";
           DBTransaction dBTransaction = DMLUtils.getDBTransaction();
           CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "nmat_ui_pkg.nmat_transid_activation");
           try {
                cstmt.setString(1, groupName);
                cstmt.executeUpdate();
                cstmt.close();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Activation group " + groupName + " successfully activated", null);
                } 
           catch (SQLException e) {
            if (e.getMessage().indexOf(CSMQBean.CHECK_MODE_ERRORS) > -1) {  // it's a name already in use error
                messageText = "Errors occurred during the pre-activation validation and need to be resolved before " + groupName + " can be activated.";
                }
            else { // it's something else
                messageText = "The following error occurred: " +  e.getMessage() ;
                e.printStackTrace();
                }
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        FacesContext.getCurrentInstance().addMessage(null, msg);
       }
    
    public static boolean activateGroupInCheckMode (String groupName)   {
            /*
             PROCEDURE nmat_ui_pkg.nmat_transid_activation 
            (pGrpName IN tms.tms_predict_groups.name%TYPE)
            */
           boolean retVal = false;
           String messageText;
           FacesMessage msg;
           String sql = "{call nmat_ui_pkg.nmat_activation_chkmode (?)}";
           DBTransaction dBTransaction = DMLUtils.getDBTransaction();
           CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "nmat_ui_pkg.nmat_activation_chkmode");
           try {
                cstmt.setString(1, groupName);
                cstmt.executeUpdate();
                cstmt.close();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Activation group " + groupName + " successfully validated in checkmode.", null);
                retVal = true;
                } 
            catch (SQLException e) {
                if (e.getMessage().indexOf(CSMQBean.ACTIVATION_ERRORS) > -1) {  
                    messageText = "The errors found during CHECK mode activation are displayed on the screen. Demote the NMQs in error to Draft to resolve, Promote and Validate again.";
                    }
                else if (e.getMessage().indexOf(CSMQBean.RECORD_LOCKED_ERROR) > -1) {  
                    messageText = CSMQBean.getProperty("RECORD_LOCKED_ERROR");
                    }
                else { // it's something else
                    messageText = "The following error occurred: " +  e.getMessage() ;
                    e.printStackTrace();
                    }
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            retVal = false;
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return retVal;
        }
    

    public static void refreshImpactMVs ()   {
           /*
           PROCEDURE impact_pkg.submit_refresh_mviews(
             i_filter_dict_name IN tms_def_dictionaries.short_name%TYPE) 
            */
           String messageText = "";
           FacesMessage msg;
           //String sql = "{call impact_pkg.submit_refresh_mviews (?)}";
           //String sql = "DECLARE v_job number; begin dbms_job.submit (job => v_job,next_date => SYSDATE,what => 'BEGIN smq_codelist_pkg.define_globals; ' || 'dbms_mview.refresh(''nmq_smq_impact_contents''); END;',INTERVAL => NULL);exception WHEN others THEN raise; end;";
           String sql = "BEGIN impact_pkg.submit_refresh_mviews; EXCEPTION WHEN OTHERS THEN RAISE; END;";
               
            
            
           DBTransaction dBTransaction = DMLUtils.getDBTransaction();
           CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "impact_pkg.submit_refresh_mviews");
           CSMQBean.logger.info ("*** SUBMITTING JOB TO REFRESH VIEWS ***");
           try {
                //cstmt.setString(1, CSMQBean.defaultFilterDictionaryShortName);
                cstmt.executeUpdate();
                cstmt.close();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Impacted list refreshed for dictionary: " + CSMQBean.defaultFilterDictionaryShortName + " successfully activated", null);
                } 
           catch (SQLException e) {
            if (e.getMessage().indexOf(CSMQBean.CHECK_MODE_ERRORS) > -1) {  // it's a name already in use error
                messageText = "The following error(s) occurred during the the refresh of the impacted list for dictionary: " + CSMQBean.defaultFilterDictionaryShortName + "\n" + e.getMessage();
                }
          
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        FacesContext.getCurrentInstance().addMessage(null, msg);
       }
    
    
    
    public static Hashtable <String, String> getActivationInfo (String dictContentID, String dictionaryID) {
        
       /*
        PROCEDURE nmat_ui_pkg.s_get_Activation_Info_By_MQ (
            pDictionaryName         IN      tms.tms_def_dictionaries.short_name%TYPE,
            pdictContentID          IN      tms.tms_dict_contents.dict_content_id%TYPE,
            pInitialCreationDate    OUT     DATE,
            pInitialCreationBy      OUT     VARCHAR2,
            pLastActivationDate     OUT     DATE,
            pActivationBy           OUT     VARCHAR2 
            )
        */
        
        CSMQBean.logger.info("*** GETTING ACTIVATION INFO ***");
        CSMQBean.logger.info("pDictionaryId: " + dictionaryID);
        CSMQBean.logger.info("pdictContentID: " + dictContentID); 
          
        String sql = "{call NMAT_UI_PKG.s_get_Activation_Info_By_MQ(?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        FacesMessage msg;
        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "NMAT_UI_PKG.s_get_Activation_Info_By_MQ");
        String messageText;
        Hashtable <String, String>retVal = new Hashtable<String, String>();  // array to return the new state and message
        
        try {
            cstmt.setString(1, dictionaryID);
            cstmt.setString(2, dictContentID);

            cstmt.registerOutParameter(3, oracle.jdbc.OracleTypes.DATE);
            cstmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CHAR);
            cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.DATE);
            cstmt.registerOutParameter(6, oracle.jdbc.OracleTypes.CHAR);
            
            cstmt.executeUpdate();
            
            String initialCreationDate = cstmt.getString(3) != null ? cstmt.getString(3) : "";
            String initialCreationBy = cstmt.getString(4) != null ? cstmt.getString(4) : "";
            String lastActivationDate = cstmt.getString(5) != null ? cstmt.getString(5) : "";
            String activationBy = cstmt.getString(6) != null ? cstmt.getString(6) : "";
            
            retVal.put("initialCreationDate", initialCreationDate);
            retVal.put("initialCreationBy", initialCreationBy);
            retVal.put("lastActivationDate", lastActivationDate);
            retVal.put("activationBy",activationBy);

            cstmt.close();
            return retVal;
            } 
        catch (SQLException e) {
                //messageText = "The following error occurred: " +  e.getMessage() ;
                e.printStackTrace();
                } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
        
            //msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, "");
            //FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
            } 
        
    public static boolean updateMQDesignee (String dictContentID, String designee) {
        /*
        PROCEDURE nmat_ui_pkg.s_insert_update_designee
                   (pPredictConID             IN tms.tms_predict_contents.predict_content_id%TYPE,
                   pDesignee           IN track_designee.designee%TYPE)
        */
        
        String sql = "{call nmat_ui_pkg.s_insert_update_designee(?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();

        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "nmat_ui_pkg.s_insert_update_designee");
        
        try {
            cstmt.setString(1, dictContentID);
            cstmt.setString(2, designee);
            cstmt.executeUpdate(); 
            return true;
            }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
    }
    public static Date getLastActivationDate(String dictContentID, Date transEndDate) {
        /*
        PROCEDURE nmat_ui_pkg.s_insert_update_designee
                   (pPredictConID             IN tms.tms_predict_contents.predict_content_id%TYPE,
                   pDesignee           IN track_designee.designee%TYPE)
        */
        Date lastActivationDate = null;
        String sql = "{? = call workflow_pkg.last_activation_date(?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();

        CallableStatement cstmt = new CallableStatement (dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT), "workflow_pkg.last_activation_date");
        
        try {
            cstmt.registerOutParameter(1, Types.DATE);
            cstmt.setString(2, dictContentID);
            cstmt.setDate(3, transEndDate);
            cstmt.execute();
            lastActivationDate = (Date)cstmt.getDate(1);
            return lastActivationDate;
            }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
                if (null != cstmt){
                    try {
                        cstmt.close();
                    } catch (SQLException sqe){
                        CSMQBean.logger.error("error while closing callable statment...");     
                    }
                }
            }
    }
}
