package com.${org}.${app}.service.impl;

import com.${org}.${app}.service.${entity}Service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Quinn
 * @date 2018/1/17
 */
@Service("${entity?cap_first}Service")
public class ${entity}ServiceImpl extends BaseServiceImpl implements ${entity}Service {

    @Autowired
    private ${entity}Dao ${entity?cap_first}Dao;
}
