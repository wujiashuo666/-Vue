package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Employeeec;

import java.util.List;
import java.util.Map;

public interface EmployeeecMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employeeec record);

    int insertSelective(Employeeec record);

    Employeeec selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employeeec record);

    int updateByPrimaryKey(Employeeec record);

    List<Map<String,Object>> getEcByPage(@Param("page") Integer page,
                                          @Param("size") Integer size,
                                          @Param("name") String name);

    Long getEcTotal(@Param("name") String name);
}
