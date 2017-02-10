package com.dbms.csmq.view.backing.NMQ;

public class GroupSearchPojo {
    public GroupSearchPojo() {
        super();
    }
    
    private String columnName;
    private String groupValue;
    private String extensionValue;
    private String operator;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setExtensionValue(String extensionValue) {
        this.extensionValue = extensionValue;
    }

    public String getExtensionValue() {
        return extensionValue;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
