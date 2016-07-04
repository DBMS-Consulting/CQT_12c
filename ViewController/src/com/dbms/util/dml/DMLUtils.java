package com.dbms.util.dml;


import com.dbms.csmq.CSMQBean;

import com.dbms.util.ADFUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import oracle.adf.model.BindingContext;

import oracle.binding.DataControl;
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
       ApplicationModuleImpl am = null;
       am = ((ApplicationModuleImpl)ADFUtils.getApplicationModuleForDataControl("CQTAppModuleDataControl"));
       if (null == am){
           System.out.println("Failed to get AM Instance from ADFUtils method.");
           BindingContext bc = BindingContext.getCurrent();
           DataControl dc = bc.findDataControl("CQTAppModuleDataControl");
           am = ((ApplicationModuleImpl)dc.getDataProvider());
           System.out.println("Got AM Instance from ADFUtils getDataProvider method.");
       }
       DBTransaction dBTransaction = am.getDBTransaction();
       return dBTransaction;
    }


    public static String getJDBCURL() {

//        DBTransaction transaction = getDBTransaction();
//        PreparedStatement preparedStatement = transaction.createPreparedStatement("commit;", 0);
        Connection conn = null;
        String retVal = "";
        try {
            if (null != conn){
                conn = getConnectionFromDS();
                retVal= conn.getMetaData().getURL();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    // Close the connection
                    conn.close();
                }
                catch (SQLException e) {
                    CSMQBean.logger.error("ERROR", e);
                }
            }
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
    /**Method to get Connection using JDBC DataSource Name
         * @param dsName
         * @return
         * @throws NamingException
         * @throws SQLException
         */
        public static final Connection getConnectionFromDS() throws NamingException, SQLException {
            Connection con = null;
            DataSource datasource = null;

            Context initialContext = new InitialContext();
            if (initialContext == null) {
            }
            datasource = (DataSource) initialContext.lookup("jdbc/TMSDS");
            if (datasource != null) {
                con = datasource.getConnection();
            } else {
                System.out.println("Failed to Find JDBC DataSource.");
            }
            return con;
        }

}
