package com.${org}.${app}.dao;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.${org}.BaseDao;

/**
 * @author Quinn
 * @date ${.now?string("yyyy/MM/dd")}
 */
@Repository
public interface ${entity}Dao extends BaseDao {
        List<${entity}> listByEntity(${entity} entity);

}
