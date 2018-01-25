package com.${org}.${app}.model.entity;


import com.${org}.${app}.Base;
import java.util.*;
import java.math.BigDecimal;

/**
 * @author Quinn
 * @date ${.now?string("yyyy/MM/dd")}
 */
public class ${entity} extends Base {


        <#list propertyList as plist>
        private ${plist.propertyType} ${plist.propertyName};
        </#list>

        <#list propertyList as plist>
        public ${plist.propertyType} get${plist.propertyName?cap_first }() {
            return ${plist.propertyName};
        }
        public void set${plist.propertyName?cap_first } (${plist.propertyType} ${plist.propertyName}) {
            this.${plist.propertyName} = ${plist.propertyName};
        }
        </#list>


        @Override
        public String toString() {
            return "{" +
        <#list propertyList as plist>
            "\"${plist.propertyName}\":" + "\"" + ${plist.propertyName} + "\"" <#if plist_has_next>+ "," </#if>+
        </#list>
            '}';
        }
}
