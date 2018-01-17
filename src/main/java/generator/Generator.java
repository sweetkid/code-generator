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

    private final static String org = "quinn";
    private final static String app = "app";
    private static String table_name = "tb_payment";
    /**
     * 需要去掉的前缀
     */
    private final static String sub_prefix_table = "tb_";

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
    private static Map<String, String> propertyMap = new HashMap<String, String>();
    /**
     * 文件存放路径
     **/
    private static Map<String, String> fileDirMap = new HashMap<String, String>();

    static {
        propertyMap.put("varchar", "String");
        propertyMap.put("text", "String");
        propertyMap.put("datetime", "Date");
        propertyMap.put("bigint", "Long");
        propertyMap.put("int", "Integer");
        propertyMap.put("decimal", "BigDecimal");
        propertyMap.put("tinyint", "Integer");
        propertyMap.put("date", "Date");
        propertyMap.put("double", "Double");
        propertyMap.put("float", "Float");
        propertyMap.put("longtext", "String");
        propertyMap.put("tinytext", "String");
        propertyMap.put("varbinary", "String");
        propertyMap.put("time", "Date");
        propertyMap.put("timestamp", "Date");
        propertyMap.put("json", "String");
        propertyMap.put("mediumtext", "String");

        fileDirMap.put("TemplateDao.java", "D:\\generator\\dao\\");
        fileDirMap.put("TemplateDao.xml", "D:\\generator\\mapping\\");
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

        String sql = "DESCRIBE " + tableName;//SQL语句
        PreparedStatement statement = DBHandler.getStatement(sql);
        ResultSet ret1 = statement.executeQuery();//执行语句，得到结果集

        System.out.println(tableName + "----------start");

        List<Property> propertyList = new ArrayList<Property>();
        while (ret1.next()) {
            String columnName = ret1.getString(1).toLowerCase();//自动转小写
            columnName = g.getJavaPropertyName(columnName, Generator.name_rule_20);

            String type = ret1.getString(2);//获取数据类型
            if (type.indexOf("(") > 0) {
                type = type.substring(0, type.indexOf("("));
            }
            if (CommonUtil.isNullStr(propertyMap.get(type))) {
                throw new RuntimeException("jdbc type is null " + type);
            }
            type = propertyMap.get(type);

            Property property = new Property();
            property.setName(columnName);
            property.setType(type);
            propertyList.add(property);
            paramMap.put("propertyList", propertyList);
            readTemplateToCode(paramMap);

        }
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
        g.generatorEntityByDB();
//        g.generatorEntityByTable("tb_payment");
    }


}
