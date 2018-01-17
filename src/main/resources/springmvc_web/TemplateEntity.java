package com.${org}.${app}.model.entity;


import com.${org}.${app}.model.Base;
import java.util.*;

/**
 * @author Quinn
 * @date 2018/1/15
 * @package com.quinn.app.dao
 */
public class ${entity} extends Base {


        <#list propertyList as plist>
        private ${plist.type} ${plist.name};
        </#list>

        <#list propertyList as plist>
        public ${plist.type} get${plist.name?cap_first }() {
            return ${plist.name};
        }
        public void set${plist.name?cap_first } (${plist.type} ${plist.name}) {
            this.${plist.name} = ${plist.name};
        }
        </#list>


        @Override
        public String toString() {
            return "{" +
        <#list propertyList as plist>
            "\"${plist.name}\":" + "\"" + ${plist.name} + "\"" <#if plist_has_next>+ "," </#if>+
        </#list>
            '}';
        }
}
