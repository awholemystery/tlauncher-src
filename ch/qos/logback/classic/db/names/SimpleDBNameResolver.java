package ch.qos.logback.classic.db.names;

import ch.qos.logback.core.CoreConstants;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/db/names/SimpleDBNameResolver.class */
public class SimpleDBNameResolver implements DBNameResolver {
    private String tableNamePrefix = CoreConstants.EMPTY_STRING;
    private String tableNameSuffix = CoreConstants.EMPTY_STRING;
    private String columnNamePrefix = CoreConstants.EMPTY_STRING;
    private String columnNameSuffix = CoreConstants.EMPTY_STRING;

    @Override // ch.qos.logback.classic.db.names.DBNameResolver
    public <N extends Enum<?>> String getTableName(N tableName) {
        return this.tableNamePrefix + tableName.name().toLowerCase() + this.tableNameSuffix;
    }

    @Override // ch.qos.logback.classic.db.names.DBNameResolver
    public <N extends Enum<?>> String getColumnName(N columnName) {
        return this.columnNamePrefix + columnName.name().toLowerCase() + this.columnNameSuffix;
    }

    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix != null ? tableNamePrefix : CoreConstants.EMPTY_STRING;
    }

    public void setTableNameSuffix(String tableNameSuffix) {
        this.tableNameSuffix = tableNameSuffix != null ? tableNameSuffix : CoreConstants.EMPTY_STRING;
    }

    public void setColumnNamePrefix(String columnNamePrefix) {
        this.columnNamePrefix = columnNamePrefix != null ? columnNamePrefix : CoreConstants.EMPTY_STRING;
    }

    public void setColumnNameSuffix(String columnNameSuffix) {
        this.columnNameSuffix = columnNameSuffix != null ? columnNameSuffix : CoreConstants.EMPTY_STRING;
    }
}
