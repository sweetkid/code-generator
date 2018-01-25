package generator;

/**
 * @author Quinn
 * @date 2018/1/17
 * @package generator
 */
public class Property {


    private String columnName;
    private String columnType;
    private String jdbcType;
    private String propertyName;
    private String propertyType;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + propertyName + '\'' +
                ", type='" + propertyType + '\'' +
                '}';
    }
}
