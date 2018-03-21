package generator;

import jdbc.DBHandler;
import util.CommonUtil;
import util.FreemarkerUtil;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Quinn
 * @date 2018/1/15
 * @package generator
 */
public class Generator {

    private final static String org = "qiaorong";
    private final static String app = "cs";
    private static String table_name = "ti_user_inf";
    /**
     * 需要去掉的前缀
     */
    private final static String sub_prefix_table = "ti_";

    /**
     * 首字母大写
     **/
    private final static String name_rule_10 = "10";
    /**
     * 首字母小写写
     **/
    private final static String name_rule_20 = "20";

    /**
     * 数据库类型 对应 java 类型
     **/
    private static Map<String, String> javaTypeMap = new HashMap<String, String>();
    private static Map<String, String> jdbcTypeMap = new HashMap<String, String>();
    /**
     * 文件存放路径
     **/
    private static Map<String, String> fileDirMap = new HashMap<String, String>();

    static {
        javaTypeMap.put("varchar", "String");
        javaTypeMap.put("text", "String");
        javaTypeMap.put("datetime", "Date");
        javaTypeMap.put("bigint", "Long");
        javaTypeMap.put("int", "Integer");
        javaTypeMap.put("decimal", "BigDecimal");
        javaTypeMap.put("tinyint", "Integer");
        javaTypeMap.put("date", "Date");
        javaTypeMap.put("double", "Double");
        javaTypeMap.put("float", "Float");
        javaTypeMap.put("longtext", "String");
        javaTypeMap.put("tinytext", "String");
        javaTypeMap.put("varbinary", "String");
        javaTypeMap.put("time", "Date");
        javaTypeMap.put("timestamp", "Date");
        javaTypeMap.put("json", "String");
        javaTypeMap.put("mediumtext", "String");


        jdbcTypeMap.put("varchar", "VARCHAR");
        jdbcTypeMap.put("text", "LONGVARCHAR");
        jdbcTypeMap.put("datetime", "TIMESTAMP");
        jdbcTypeMap.put("bigint", "BIGINT");
        jdbcTypeMap.put("int", "INTEGER");
        jdbcTypeMap.put("decimal", "DECIMAL");
        jdbcTypeMap.put("tinyint", "TINYINT");
        jdbcTypeMap.put("date", "DATE");
        jdbcTypeMap.put("double", "DOUBLE");
        jdbcTypeMap.put("float", "REAL");
        jdbcTypeMap.put("longtext", "LONGVARCHAR");
        jdbcTypeMap.put("tinytext", "VARCHAR");
        jdbcTypeMap.put("varbinary", "LONGVARBINARY");
        jdbcTypeMap.put("time", "TIME");
        jdbcTypeMap.put("timestamp", "TIMESTAMP");
        jdbcTypeMap.put("json", "VARCHAR");
        jdbcTypeMap.put("mediumtext", "LONGVARCHAR");





        fileDirMap.put("TemplateDao.java", "D:\\generator\\dao\\");
        fileDirMap.put("TemplateDao.xml", "D:\\generator\\mapping\\");
        fileDirMap.put("TemplateDao_Ext.xml", "D:\\generator\\mapping\\");
        fileDirMap.put("TemplateBo.java", "D:\\generator\\bo\\");
        fileDirMap.put("TemplateEntity.java", "D:\\generator\\entity\\");
        fileDirMap.put("TemplateService.java", "D:\\generator\\service\\");
        fileDirMap.put("TemplateServiceImpl.java", "D:\\generator\\service\\impl\\");

    }


    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    private String firstUpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 获取java名
     *
     * @param name
     * @param rule
     * @return
     */
    private String getJavaPropertyName(String name, String rule) {
        if (CommonUtil.isNullStr(name)) {
            throw new RuntimeException("name is null");
        }
        String[] nameArr = name.split("_");

        if (rule.equals(name_rule_10)) {
            String result = "";
            for (int i = 0; i < nameArr.length; i++) {
                result += firstUpperCase(nameArr[i]);
            }
            return result;
        }
        if (rule.equals(name_rule_20)) {
            String result = nameArr[0];
            for (int i = 1; i < nameArr.length; i++) {
                result += firstUpperCase(nameArr[i]);
            }
            return result;

        }
        throw new RuntimeException("name is error");
    }

    /**
     * 读取模板文件
     *
     * @param file
     * @return
     */
    public static String readFileToString(File file) {
        String encoding = "UTF-8";
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将字符流写成文件
     *
     * @param str
     * @return
     */
    public void writeStringToFile(String str, String fileName) {
        try {
            FileOutputStream o = new FileOutputStream(fileName);
            o.write(str.getBytes("UTF-8"));
            o.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
        }

    }

    /**
     * 读取模板文件生成代码
     *
     * @param paramMap
     */
    public void readTemplateToCode(Map<String, Object> paramMap) {
        File file = new File(ClassLoader.getSystemResource("springmvc_web").getPath());
        for (File f : file.listFiles()) {
            if(f.getName().equals("TemplateDao_Ext.xml")){
//                System.out.println("TemplateDao_Ext.xml 暂时不生成");
//                continue;
            }
            String fileStr = readFileToString(f);
            String rr = FreemarkerUtil.getString(fileStr, paramMap);

            String dir = fileDirMap.get(f.getName());
            String entity = getJavaPropertyName(table_name.replace(sub_prefix_table, ""), Generator.name_rule_10);
            String name = f.getName().replace("Template", entity).replace("Entity", "");
            writeStringToFile(rr, dir + name);

        }

    }


    /**
     * 生成文件 - 表
     *
     * @param tableName
     * @throws SQLException
     */
    public void generatorEntityByTable(String tableName) throws SQLException {
        Generator g = new Generator();
        table_name = tableName;
        String entityName = g.getJavaPropertyName(tableName.replace(Generator.sub_prefix_table, ""), Generator.name_rule_10);

        Map<String, Object> paramMap = new HashMap();
        paramMap.put("org", org);
        paramMap.put("app", app);
        paramMap.put("entity", entityName);
        paramMap.put("tableName", tableName);

        String sql = "DESCRIBE " + tableName;//SQL语句
        PreparedStatement statement = DBHandler.getStatement(sql);
        ResultSet ret1 = statement.executeQuery();//执行语句，得到结果集

        System.out.println(tableName + "----------start");

        List<Property> propertyList = new ArrayList<Property>();
        while (ret1.next()) {
            String columnName = ret1.getString(1);
            columnName = columnName.toLowerCase();//自动转小写
            String propertyName = g.getJavaPropertyName(columnName, Generator.name_rule_20);

            String columnType = ret1.getString(2);//获取数据类型

            if (columnType.indexOf("(") > 0) {
                columnType = columnType.substring(0, columnType.indexOf("("));
            }

            if (CommonUtil.isNullStr(javaTypeMap.get(columnType))) {
                throw new RuntimeException("java type is null " + columnType);
            }
            if (CommonUtil.isNullStr(jdbcTypeMap.get(columnType))) {
                throw new RuntimeException("jdbc type is null " + columnType);
            }
            Property property = new Property();
            property.setColumnName(columnName);
            property.setColumnType(columnType);
            property.setJdbcType(jdbcTypeMap.get(columnType));
            property.setPropertyName(propertyName);
            property.setPropertyType(javaTypeMap.get(columnType));
            propertyList.add(property);
            paramMap.put("propertyList", propertyList);

        }
        readTemplateToCode(paramMap);
        ret1.close();
    }

    /**
     * 生成文件 - 数据库
     *
     * @throws SQLException
     */
    public void generatorEntityByDB() throws SQLException {
        String sql = "show tables";//SQL语句
        PreparedStatement statement = DBHandler.getStatement(sql);
        try {
            ResultSet ret = statement.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String tableName = ret.getString(1);
                Generator g = new Generator();
                g.generatorEntityByTable(tableName);
            }//显示数据
            ret.close();
            statement.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws SQLException {
        Generator g = new Generator();
//        g.generatorEntityByDB();
        g.generatorEntityByTable("cs_company_user_relation");
    }


}
