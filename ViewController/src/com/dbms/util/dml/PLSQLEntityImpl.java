package com.dbms.util.dml;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.jbo.JboException;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.TransactionEvent;



public class PLSQLEntityImpl extends EntityImpl {
    public PLSQLEntityImpl() {
        super();
    }
    
    protected void doDML(int operation, TransactionEvent e) {
      // super.doDML(operation, e);
      if (operation == DML_INSERT)
        callInsertProcedure(e);
      else if (operation == DML_UPDATE)
        callUpdateProcedure(e);
      else if (operation == DML_DELETE)
        callDeleteProcedure(e);
    }
    /* Override in a subclass to perform non-default processing */
    protected void callInsertProcedure(TransactionEvent e) {
      super.doDML(DML_INSERT, e);
    }
    /* Override in a subclass to perform non-default processing */  
    protected void callUpdateProcedure(TransactionEvent e) {
      super.doDML(DML_UPDATE, e);
    }
    /* Override in a subclass to perform non-default processing */  
    protected void callDeleteProcedure(TransactionEvent e) {
      super.doDML(DML_DELETE, e);
    }
    
    public void callStoredProcedure(String stmt, Object[] bindVars) {
      PreparedStatement st = null;
      try {
        // 1. Create a JDBC PreparedStatement for 
        st = getDBTransaction().createPreparedStatement("begin "+stmt+";end;",0);
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
              e.printStackTrace();
              }
        }
      }
    }
}
