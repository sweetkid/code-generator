package com.${org}.${app}.service.impl;

import com.${org}.${app}.service.${entity}Service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.quinn.payment.dao.${entity?cap_first}Dao;
import com.quinn.BaseServiceImpl;

/**
 * @author Quinn
 * @date 2018/1/17
 */
@Service("${entity?cap_first}Service")
public class ${entity}ServiceImpl extends BaseServiceImpl<${entity?cap_first}Dao> implements ${entity}Service {

    @Autowired
    private ${entity}Dao ${entity?lower_case}Dao;
}
