package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.AdjustSalary;

import java.util.List;
import java.util.Map;

public interface AdjustSalaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdjustSalary record);

    int insertSelective(AdjustSalary record);

    AdjustSalary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdjustSalary record);

    int updateByPrimaryKey(AdjustSalary record);

    List<Map<String,Object>> getAsByPage(@Param("page") Integer page,
                                          @Param("size") Integer size,
                                          @Param("name") String name);

    Long getAsTotal(@Param("name") String name);
}
