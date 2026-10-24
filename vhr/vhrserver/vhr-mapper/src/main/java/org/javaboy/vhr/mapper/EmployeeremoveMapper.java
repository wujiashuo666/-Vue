package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Employeeremove;

import java.util.List;
import java.util.Map;

public interface EmployeeremoveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employeeremove record);

    int insertSelective(Employeeremove record);

    Employeeremove selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employeeremove record);

    int updateByPrimaryKey(Employeeremove record);

    List<Map<String,Object>> getMvByPage(@Param("page") Integer page,
                                          @Param("size") Integer size,
                                          @Param("name") String name);

    Long getMvTotal(@Param("name") String name);
}
