package com.${org}.${app}.service.impl;

import com.${org}.${app}.service.${entity}Service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.${org}.${app}.dao.${entity?cap_first}Dao;
import com.${org}.BaseServiceImpl;

/**
 * @author Quinn
 * @date ${.now?string("yyyy/MM/dd")}
 */
@Service("${entity?cap_first}Service")
public class ${entity}ServiceImpl extends BaseServiceImpl<${entity?cap_first}Dao> implements ${entity}Service {

    @Autowired
    private ${entity}Dao ${entity?cap_first}Dao;
}
