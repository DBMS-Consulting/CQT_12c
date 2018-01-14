package com.dbms.util.dml;


import com.dbms.csmq.CSMQBean;

import com.dbms.util.ADFUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.adf.model.BindingContext;

import oracle.binding.DataControl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.JboException;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;


public class DMLUtils {
    public DMLUtils() {
        super();
    }

    /***
     * This method returns the current instance of the session DBTransaction object.
     * The method below is implemented from a Singleton Object for utilitarian purposes.
     */
    public static synchronized DBTransaction getDBTransaction() {
        DBTransaction dBTransaction = null;
        try {
            BindingContext bc = BindingContext.getCurrent();
            DataControl dc = bc.findDataControl("NMQModuleDataControl");
            if (dc != null) {
                ADFUtils.setEL("#{sessionScope.bcContext}", dc);
            }
            dc = (DataControl) ADFUtils.evaluateEL("#{sessionScope.bcContext}");
            if(dc != null){
            ApplicationModuleImpl am = ((ApplicationModuleImpl) dc.getDataProvider());
            dBTransaction = am.getDBTransaction();
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return dBTransaction;
    }


    public static String getJDBCURL() {

        DBTransaction transaction = getDBTransaction();
        PreparedStatement preparedStatement = transaction.createPreparedStatement("commit;", 0);
        Connection conn;
        String retVal = "";
        try {
            conn = preparedStatement.getConnection();
            retVal= conn.getMetaData().getURL();
            }
        catch (SQLException e) {
            e.printStackTrace();
            }
        return retVal;
        
        }

    public static synchronized void callStoredProcedure(String stmt, Object[] bindVars) {
        PreparedStatement st = null;
        try {
            // 1. Create a JDBC PreparedStatement for


            st = getDBTransaction().createPreparedStatement("begin " + stmt + ";end;", 0);
            if (bindVars != null) {
                // 2. Loop over values for the bind variables passed in, if any
                for (int z = 0; z < bindVars.length; z++) {
                    // 3. Set the value of each bind variable in the statement
                    st.setObject(z + 1, bindVars[z]);
                }
            }
            // 4. Execute the statement
            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new JboException(e);
        }
        finally {
            if (st != null) {
                try {
                    // 5. Close the statement
                    st.close();
                }
                catch (SQLException e) {
                    CSMQBean.logger.error("ERROR", e);
                }
            }
        }
    }

}
