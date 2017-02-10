package com.dbms.csmq.view.backing.NMQ;

public class ProductSearchPojo {
    public ProductSearchPojo() {
        super();
    }
    
    private String columnName;
    private String productValue;
    private String scopeValue;
    private String extensionValue;
    private String operator;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setProductValue(String productValue) {
        this.productValue = productValue;
    }

    public String getProductValue() {
        return productValue;
    }

    public void setScopeValue(String scopeValue) {
        this.scopeValue = scopeValue;
    }

    public String getScopeValue() {
        return scopeValue;
    }

    public void setExtensionValue(String extensionValue) {
        this.extensionValue = extensionValue;
    }

    public String getExtensionValue() {
        return extensionValue;
    }
}
