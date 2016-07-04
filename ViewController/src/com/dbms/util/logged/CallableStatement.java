package com.dbms.util.logged;

import com.dbms.csmq.CSMQBean;

import java.io.InputStream;
import java.io.Reader;

import java.math.BigDecimal;

import java.net.URL;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;


public class CallableStatement     {
    String name;
    java.sql.CallableStatement stmnt;
    
    public CallableStatement(java.sql.CallableStatement stmnt, String name) {
        CSMQBean.logger.info(">>> CALLING: " + name);
        this.stmnt = stmnt;
        this.name = name;
    }
    
    
    public void setString (String parameterName, String x) throws SQLException {
        CSMQBean.logger.info("  " + parameterName + "=" + x);
        stmnt.setString(parameterName, x);
        }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

  
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        stmnt.registerOutParameter( parameterIndex,  sqlType) ;
    }

    
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        stmnt.registerOutParameter( parameterIndex,  sqlType,  scale) ;
    }

    
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        stmnt.registerOutParameter( parameterIndex,  sqlType,  typeName) ;
    }

    
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        stmnt.registerOutParameter( parameterName,  sqlType) ;
    }

    
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        stmnt. registerOutParameter( parameterName,  sqlType,  scale) ;
    }

    
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        stmnt.registerOutParameter( parameterName,  sqlType,  typeName) ;
    }

    
    public boolean wasNull() throws SQLException {
        return stmnt.wasNull();
    }

    
    public String getString(int parameterIndex) throws SQLException {
        return stmnt.getString(parameterIndex);
    }

    
    public String getString(String parameterName) throws SQLException {
        return stmnt.getString(parameterName);
    }

    
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return stmnt.getBoolean(parameterIndex);
    }

    
    public boolean getBoolean(String parameterName) throws SQLException {
        return stmnt.getBoolean(parameterName);
    }

    
    public byte getByte(int parameterIndex) throws SQLException {
        return stmnt.getByte(parameterIndex);
    }

    
    public byte getByte(String parameterName) throws SQLException {
        return stmnt.getByte(parameterName);
    }

    
    public short getShort(int parameterIndex) throws SQLException {
        return stmnt.getShort(parameterIndex);
    }

    
    public short getShort(String parameterName) throws SQLException {
        return stmnt.getShort(parameterName);
    }

    
    public int getInt(int parameterIndex) throws SQLException {
        return stmnt.getInt(parameterIndex);
    }

    
    public int getInt(String parameterName) throws SQLException {
        return stmnt.getInt(parameterName);
    }

    
    public long getLong(int parameterIndex) throws SQLException {
        return stmnt.getLong(parameterIndex);
    }

    
    public long getLong(String parameterName) throws SQLException {
        return stmnt.getLong(parameterName);
    }

    
    public float getFloat(int parameterIndex) throws SQLException {
        return stmnt.getFloat(parameterIndex);
    }

    
    public float getFloat(String parameterName) throws SQLException {
        return stmnt.getFloat(parameterName);
    }

    
    public double getDouble(int parameterIndex) throws SQLException {
        return stmnt.getDouble(parameterIndex);
    }

    
    public double getDouble(String parameterName) throws SQLException {
        return stmnt.getDouble(parameterName);
    }

    
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return stmnt.getBigDecimal(parameterIndex, scale);
    }

    
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return stmnt. getBigDecimal(parameterIndex);
    }

    
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return stmnt.getBigDecimal(parameterName);
    }

    
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return stmnt.getBytes(parameterIndex);
    }

    
    public byte[] getBytes(String parameterName) throws SQLException {
        return stmnt.getBytes(parameterName);
    }

    
    public Date getDate(int parameterIndex) throws SQLException {
        return stmnt.getDate(parameterIndex);
    }

    
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return stmnt.getDate(parameterIndex, cal);
    }

    
    public Date getDate(String parameterName) throws SQLException {
        return stmnt.getDate(parameterName);
    }

    
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return stmnt.getDate(parameterName, cal);
    }

    
    public Time getTime(int parameterIndex) throws SQLException {
        return stmnt.getTime(parameterIndex);
    }

    
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return stmnt.getTime(parameterIndex, cal);
    }

    
    public Time getTime(String parameterName) throws SQLException {
        return stmnt.getTime(parameterName);
    }

    
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return stmnt.getTime(parameterName, cal);
    }

    
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return stmnt.getTimestamp(parameterIndex);
    }

    
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return stmnt.getTimestamp(parameterIndex, cal);
    }

    
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return stmnt.getTimestamp(parameterName);
    }

    
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return stmnt.getTimestamp(parameterName, cal);
    }

    
    public Object getObject(int parameterIndex) throws SQLException {
        return stmnt.getObject(parameterIndex);
    }

    
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return stmnt.getObject(parameterIndex, map);
    }

    
    public Object getObject(String parameterName) throws SQLException {
        return stmnt.getObject(parameterName);
    }

    
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return stmnt.getObject(parameterName,map);
    }

    
    public Ref getRef(int parameterIndex) throws SQLException {
        return stmnt.getRef(parameterIndex);
    }

    
    public Ref getRef(String parameterName) throws SQLException {
        return stmnt.getRef(parameterName);
    }

    
    public Blob getBlob(int parameterIndex) throws SQLException {
        return stmnt.getBlob(parameterIndex);
    }

    
    public Blob getBlob(String parameterName) throws SQLException {
        return stmnt.getBlob(parameterName);
    }

    
    public Clob getClob(int parameterIndex) throws SQLException {
        return stmnt.getClob(parameterIndex);
    }

    
    public Clob getClob(String parameterName) throws SQLException {
        return stmnt.getClob(parameterName);
    }

    
    public Array getArray(int parameterIndex) throws SQLException {
        return stmnt.getArray(parameterIndex);
    }

    
    public Array getArray(String parameterName) throws SQLException {
        return stmnt.getArray(parameterName);
    }

    
    public URL getURL(int parameterIndex) throws SQLException {
        return stmnt.getURL(parameterIndex);
    }

    
    public URL getURL(String parameterName) throws SQLException {
        return stmnt.getURL(parameterName);
    }

    
    public void setURL(String parameterName, URL val) throws SQLException {
        stmnt.setURL(parameterName, val) ;
    }

    
    public void setNull(String parameterName, int sqlType) throws SQLException {
        stmnt.setNull(parameterName, sqlType) ;
    }

    
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
            stmnt.setNull( parameterName,  sqlType,  typeName)  ;     }

    
    public void setBoolean(String parameterName, boolean x) throws SQLException {
            stmnt.setBoolean( parameterName,  x) ;     }

    
    public void setByte(String parameterName, byte x) throws SQLException {
            stmnt.setByte( parameterName,  x) ;     }

    
    public void setShort(String parameterName, short x) throws SQLException {
            stmnt.setShort( parameterName,  x) ;     }

    
    public void setInt(String parameterName, int x) throws SQLException {
            stmnt.setInt( parameterName,  x) ;     }

    
    public void setLong(String parameterName, long x) throws SQLException {
            stmnt.setLong( parameterName,  x) ;     }

    
    public void setFloat(String parameterName, float x) throws SQLException {
            stmnt.setFloat( parameterName,  x) ;     }

    
    public void setDouble(String parameterName, double x) throws SQLException {
            stmnt.setDouble( parameterName,  x) ;     }

    
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
            stmnt.setBigDecimal( parameterName,  x) ;     }

    
    public void setBytes(String parameterName, byte[] x) throws SQLException {
            stmnt.setBytes( parameterName,  x) ;     }

    
    public void setDate(String parameterName, Date x) throws SQLException {
            stmnt.setDate( parameterName,  x) ;     }

    
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
            stmnt.setDate( parameterName,  x,  cal) ;     }

    
    public void setTime(String parameterName, Time x) throws SQLException {
            stmnt.setTime( parameterName,  x) ;     }

    
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
            stmnt.setTime( parameterName,  x,  cal) ;     }

    
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
            stmnt.setTimestamp( parameterName,  x) ;     }

    
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
            stmnt.setTimestamp( parameterName,  x,  cal) ;     }

    
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
            stmnt.setAsciiStream( parameterName,  x,  length) ;     }

    
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
            stmnt.setAsciiStream( parameterName,  x,  length) ;     }

    
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
            stmnt.setAsciiStream( parameterName,  x) ;     }

    
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
            stmnt.setBinaryStream( parameterName,  x,  length) ;     }

    
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
            stmnt.setBinaryStream( parameterName,  x,  length) ;     }

    
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
            stmnt.setBinaryStream( parameterName,  x) ;     }

    
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
            stmnt.setObject( parameterName,  x,  targetSqlType,  scale) ;     }

    
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
            stmnt.setObject( parameterName,  x,  targetSqlType) ;     }

    
    public void setObject(String parameterName, Object x) throws SQLException {
            stmnt.setObject( parameterName,  x) ;     }

    
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
            stmnt.setCharacterStream( parameterName,  reader,  length) ;     }

    
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
            stmnt.setCharacterStream( parameterName,  reader,  length) ;     }

    
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
            stmnt.setCharacterStream( parameterName,  reader) ;     }

    
    public RowId getRowId(int parameterIndex) throws SQLException {
        return stmnt.getRowId( parameterIndex);     }

    
    public RowId getRowId(String parameterName) throws SQLException {
        return stmnt.getRowId( parameterName);     }

    
    public void setRowId(String parameterName, RowId x) throws SQLException {
            setRowId( parameterName,  x);     }

    
    public void setNString(String parameterName, String value) throws SQLException {
           setNString( parameterName,  value) ;     }

    
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
            stmnt.setNCharacterStream( parameterName,  value,  length) ;     }

    
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
            stmnt.setNCharacterStream( parameterName,  value) ;     }

    
    public void setNClob(String parameterName, NClob value) throws SQLException {
            stmnt.setNClob( parameterName,  value) ;     }

    
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
            stmnt.setNClob( parameterName,  reader,  length) ;     }

    
    public void setNClob(String parameterName, Reader reader) throws SQLException {
            stmnt.setNClob( parameterName,  reader) ;     }

    
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
            stmnt.setClob( parameterName,  reader,  length) ;     }

    
    public void setClob(String parameterName, Clob x) throws SQLException {
            stmnt.setClob( parameterName,  x) ;     }

    
    public void setClob(String parameterName, Reader reader) throws SQLException {
            stmnt.setClob( parameterName,  reader) ;     }

    
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
            stmnt.setBlob( parameterName,  inputStream,  length) ;     }

    
    public void setBlob(String parameterName, Blob x) throws SQLException {
            stmnt.setBlob( parameterName,  x) ;     }

    
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
            stmnt.setBlob(parameterName, inputStream) ;     }

    
    public NClob getNClob(int parameterIndex) throws SQLException {
        return getNClob(parameterIndex) ;     }

    
    public NClob getNClob(String parameterName) throws SQLException {
        return getNClob(parameterName);     }

    
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
            stmnt.setSQLXML( parameterName,  xmlObject) ;     }

    
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return
            stmnt.getSQLXML( parameterIndex) ;     }

    
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        return 
            stmnt.getSQLXML( parameterName) ;     }

    
    public String getNString(int parameterIndex) throws SQLException {
        return stmnt.getNString( parameterIndex);     }

    
    public String getNString(String parameterName) throws SQLException {
        return stmnt.getNString( parameterName) ;     }

    
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return stmnt.getNCharacterStream( parameterIndex) ;     }

    
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        return stmnt.getNCharacterStream( parameterName) ;     }

    
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        return stmnt.getCharacterStream( parameterIndex);     }

    
    public Reader getCharacterStream(String parameterName) throws SQLException {
        return stmnt.getCharacterStream( parameterName);     }

    
    public ResultSet executeQuery() throws SQLException {
        return stmnt.executeQuery();     }

    
    public int executeUpdate() throws SQLException {
        return stmnt.executeUpdate();     }

    
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
            stmnt.setNull( parameterIndex,  sqlType) ;     }

    
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
            stmnt.setNull( parameterIndex,  sqlType,  typeName)  ;     }

    
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
            stmnt.setBoolean( parameterIndex,  x) ;     }

    
    public void setByte(int parameterIndex, byte x) throws SQLException {
            stmnt.setByte( parameterIndex,  x) ;     }

    
    public void setShort(int parameterIndex, short x) throws SQLException {
            stmnt.setShort( parameterIndex,  x) ;     }

    
    public void setInt(int parameterIndex, int x) throws SQLException {
            stmnt.setInt( parameterIndex,  x) ;     }

    
    public void setLong(int parameterIndex, long x) throws SQLException {
            stmnt.setLong( parameterIndex,  x) ;     }

    
    public void setFloat(int parameterIndex, float x) throws SQLException {
            stmnt.setFloat( parameterIndex,  x) ;     }

    
    public void setDouble(int parameterIndex, double x) throws SQLException {
            stmnt.setDouble( parameterIndex,  x) ;     }

    
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
            stmnt.setBigDecimal( parameterIndex,  x) ;     }

    
    public void setString(int parameterIndex, String x) throws SQLException {
            stmnt.setString( parameterIndex,  x) ;     }

    
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
            stmnt.setBytes( parameterIndex,  x) ;     }

    
    public void setDate(int parameterIndex, Date x) throws SQLException {
            stmnt.setDate( parameterIndex,  x) ;     }

    
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
            stmnt.setDate( parameterIndex,  x,  cal) ;     }

    
    public void setTime(int parameterIndex, Time x) throws SQLException {
            stmnt.setTime( parameterIndex,  x) ;     }

    
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
            stmnt.setTime( parameterIndex,  x,  cal) ;     }



 

    
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
            stmnt.setAsciiStream( parameterIndex,  x,  length) ;     }

    
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
            stmnt.setAsciiStream( parameterIndex,  x,  length) ;     }

    
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
            stmnt.setAsciiStream( parameterIndex,  x) ;     }

    
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
            stmnt.setUnicodeStream( parameterIndex,  x,  length) ;     }

    
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
            stmnt.setBinaryStream( parameterIndex,  x,  length) ;     }

    
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
            stmnt.setBinaryStream( parameterIndex,  x,  length) ;     }

    
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
            stmnt.setBinaryStream( parameterIndex,  x) ;     }

    
    public void clearParameters() throws SQLException {
            stmnt.clearParameters() ;     }

    
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
            stmnt.setObject( parameterIndex,  x,  targetSqlType) ;     }

    
    public void setObject(int parameterIndex, Object x) throws SQLException {
            stmnt.setObject( parameterIndex,  x) ;     }

    
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
            stmnt.setObject( parameterIndex,  x,  targetSqlType,  scaleOrLength) ;     }

    
    public boolean execute() throws SQLException {
        return stmnt.execute() ;
               }

    
    public void addBatch() throws SQLException {
            stmnt.addBatch() ;     }

    
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
            stmnt.setCharacterStream( parameterIndex,  reader, length) ;     }

    
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
            stmnt.setCharacterStream( parameterIndex,  reader,  length) ;     }

    
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
            stmnt.setCharacterStream( parameterIndex,  reader) ;     }

    
    public void setRef(int parameterIndex, Ref x) throws SQLException {
            stmnt.setRef( parameterIndex,  x) ;     }

    
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
            stmnt.setBlob( parameterIndex,  x) ;     }

    
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
            stmnt.setBlob( parameterIndex,  inputStream,  length) ;     }

    
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
            stmnt.setBlob( parameterIndex,  inputStream) ;     }

    
    public void setClob(int parameterIndex, Clob x) throws SQLException {
            stmnt.setClob( parameterIndex,  x) ;     }

    
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
            stmnt.setClob( parameterIndex,  reader,  length) ;     }

    
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
            stmnt.setClob( parameterIndex,  reader) ;     }

    
    public void setArray(int parameterIndex, Array x) throws SQLException {
            stmnt.setArray( parameterIndex,  x) ;     }

    
    public ResultSetMetaData getMetaData() throws SQLException {
        return stmnt.getMetaData() ;
              }

    
    public void setURL(int parameterIndex, URL x) throws SQLException {
            stmnt.setURL( parameterIndex,  x)  ;     }

    
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return stmnt.getParameterMetaData() ;
             }

    
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
            stmnt.setRowId( parameterIndex,  x) ;     }

    
    public void setNString(int parameterIndex, String value) throws SQLException {
            stmnt.setNString( parameterIndex,  value) ;     }

    
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
            stmnt.setNCharacterStream( parameterIndex,  value,  length) ;     }

    
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
            stmnt.setNCharacterStream( parameterIndex,  value) ;     }

    
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
            stmnt.setNClob( parameterIndex,  value) ;     }

    
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
            stmnt.setNClob( parameterIndex,  reader,  length) ;     }

    
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
            stmnt.setNClob( parameterIndex,  reader) ;     }

    
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
            stmnt.setSQLXML( parameterIndex,  xmlObject) ;     }

    
    public ResultSet executeQuery(String sql) throws SQLException {
        return stmnt.executeQuery(sql);
                }

    
    public int executeUpdate(String sql) throws SQLException {
        return stmnt.executeUpdate( sql);
               }

    
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return stmnt.executeUpdate( sql,  autoGeneratedKeys);
       }

    
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return stmnt.executeUpdate( sql,columnIndexes);
               }

    
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return stmnt.executeUpdate( sql, columnNames);
              }

    
    public void close() throws SQLException {
            stmnt.close() ;     }

    
    public int getMaxFieldSize() throws SQLException {
        return stmnt.getMaxFieldSize();
               }

    
    public void setMaxFieldSize(int max) throws SQLException {
            stmnt.setMaxFieldSize( max);     }

    
    public int getMaxRows() throws SQLException {
        return 
            stmnt.getMaxRows() ;     }

    
    public void setMaxRows(int max) throws SQLException {
            stmnt.setMaxRows( max)  ;     }

    
    public void setEscapeProcessing(boolean enable) throws SQLException {
            stmnt.setEscapeProcessing( enable) ;     }

    
    public int getQueryTimeout() throws SQLException {
        return stmnt.getQueryTimeout();
              }

    
    public void setQueryTimeout(int seconds) throws SQLException {
            stmnt.setQueryTimeout( seconds) ;     }

    
    public void cancel() throws SQLException {
            stmnt.cancel() ;     }

    
    public SQLWarning getWarnings() throws SQLException {
        return stmnt.getWarnings();
              }

    
    public void clearWarnings() throws SQLException {
            stmnt.clearWarnings() ;     }

    
    public void setCursorName(String name) throws SQLException {
            stmnt.setCursorName( name) ;     }

    
    public boolean execute(String sql) throws SQLException {
        return stmnt.execute( sql);
               }

    
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return stmnt.execute( sql,  autoGeneratedKeys);
                }

    
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return stmnt.execute( sql,  columnIndexes);
                }

    
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return stmnt.execute( sql, columnNames);
}

    
    public ResultSet getResultSet() throws SQLException {
        return stmnt.getResultSet();
               }

    
    public int getUpdateCount() throws SQLException {
        return stmnt.getUpdateCount();
                }

    
    public boolean getMoreResults() throws SQLException {
        return stmnt.getMoreResults();
                }

    
    public boolean getMoreResults(int current) throws SQLException {
        return stmnt.getMoreResults( current);
            }

    
    public void setFetchDirection(int direction) throws SQLException {
            stmnt.setFetchDirection( direction) ;     }

    
    public int getFetchDirection() throws SQLException {
        return stmnt.getFetchDirection();
              }

    
    public void setFetchSize(int rows) throws SQLException {
            stmnt.setFetchSize( rows) ;     }

    
    public int getFetchSize() throws SQLException {
        return stmnt. getFetchSize();
            }

    
    public int getResultSetConcurrency() throws SQLException {
        return stmnt.getResultSetConcurrency();
              }

    
    public int getResultSetType() throws SQLException {
        return stmnt.getResultSetType();
             }

    
    public void addBatch(String sql) throws SQLException {
            stmnt.addBatch( sql) ;     }

    
    public void clearBatch() throws SQLException {
            stmnt.clearBatch() ;     }

    
    public int[] executeBatch() throws SQLException {
        return stmnt.executeBatch() ;
            }

    
    public Connection getConnection() throws SQLException {
        return stmnt.getConnection();
               }

    
    public ResultSet getGeneratedKeys() throws SQLException {
        return stmnt.getGeneratedKeys();
               }

    
    public int getResultSetHoldability() throws SQLException {
        return stmnt.getResultSetHoldability();
           }

    
    public boolean isClosed() throws SQLException {
        return stmnt.isClosed();
            }

    
    public void setPoolable(boolean poolable) throws SQLException {
            stmnt.setPoolable(poolable) ;}

    
    public boolean isPoolable() throws SQLException {
        return stmnt.isPoolable();
           }

    
    public Object unwrap(Class<Object> iface) throws SQLException {
        return stmnt.unwrap(iface);
              }

    
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return stmnt.isWrapperFor(iface);
             }

    
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        stmnt.setTimestamp( parameterIndex,  x);
    }

    
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        stmnt.setTimestamp( parameterIndex,  x, cal);
    }
    
  
    
    
}
