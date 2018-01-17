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
    private final static String entity = "User";
    private final static String table = "tb_user";
    /** 需要去掉的前缀 */
    private final static String sub_prefix_table = "tb_";

    /** 首字母大写 **/
    private final static  String name_rule_10 = "10";
    /** 首字母小写写 **/
    private final static  String name_rule_20 = "20";

    private static  Map<String,String> propertyMap = new HashMap<String, String>();

    static {
        propertyMap.put("varchar","String");
        propertyMap.put("text","String");
        propertyMap.put("datetime","Date");
        propertyMap.put("bigint","Long");
        propertyMap.put("int","Integer");
        propertyMap.put("decimal","BigDecimal");
        propertyMap.put("tinyint","Integer");
        propertyMap.put("date","Date");
        propertyMap.put("double","Double");
        propertyMap.put("float","Float");
        propertyMap.put("longtext","String");
        propertyMap.put("tinytext","String");
        propertyMap.put("varbinary","String");
        propertyMap.put("time","Date");
        propertyMap.put("timestamp","Date");
    }



    /**
     * 首字母大写
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
     * @param name
     * @param rule
     * @return
     */
    private String getJavaPropertyName(String name,String rule) {
        if(CommonUtil.isNullStr(name)){
            throw  new RuntimeException("name is null");
        }
        String[] nameArr = name.split("_");

        if(rule.equals(name_rule_10)){
            String result = "";
            for(int i = 0;i<nameArr.length;i++){
                result += firstUpperCase(nameArr[i]);
            }
            return result;
        }
        if(rule.equals(name_rule_20)){
            String result = nameArr[0];
            for(int i = 1;i<nameArr.length;i++){
                result += firstUpperCase(nameArr[i]);
            }
            return result;

        }
        throw new RuntimeException("name is error");
    }

    /**
     * 读取模板文件
     * @param file
     * @return
     */
    public static String readFileToString(File file) {
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void readTemplateToCode(Map<String, Object> paramMap) {
        File file = new File(ClassLoader.getSystemResource("springmvc_web").getPath());
        for (File f : file.listFiles()) {
            String fileStr = readFileToString(f);
            String rr = FreemarkerUtil.getString(fileStr, paramMap);

        }

    }


    public static void main(String[] args) {
        File file = new File(ClassLoader.getSystemResource("springmvc_web").getPath());
        for (File f : file.listFiles()) {
            System.out.println(f.getName());
            String r = readFileToString(f);
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("org", org);
            paramMap.put("app", app);
            paramMap.put("entity", entity.replace(sub_prefix_table, ""));

            List<Property> propertyList = new ArrayList<Property>();
            Property p1 = new Property();
            p1.setName("name1");
            p1.setType("String");
            Property p2 = new Property();
            p2.setName("name2");
            p2.setType("String");
            propertyList.add(p1);
            propertyList.add(p2);

            paramMap.put("propertyList", propertyList);
            String rr = FreemarkerUtil.getString(r, paramMap);
//            System.out.println(rr);

        }
    }

    /**
     * 生成单表文件
     * @param tableName
     * @throws SQLException
     */
    public void generatorEntityByTable(String tableName) throws SQLException {
        Generator g = new Generator();
        String entityName = g.getJavaPropertyName(tableName.replace(Generator.sub_prefix_table,""),Generator.name_rule_10);

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
            String columnName = ret1.getString(1);
            columnName = g.getJavaPropertyName(columnName,Generator.name_rule_20);

            String type = ret1.getString(2);
            if (type.indexOf("(") > 0) {
                type = type.substring(0, type.indexOf("("));
            }
            Property property = new Property();
            property.setName(columnName);
            property.setType(type);
            propertyList.add(property);
            paramMap.put("propertyList", propertyList);

        }
        ret1.close();
    }

    /**
     * 生成数据库对应java文件
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


    public static void main2(String[] args) throws SQLException {
        Generator g = new Generator();
        g.generatorEntityByDB();
    }




}
