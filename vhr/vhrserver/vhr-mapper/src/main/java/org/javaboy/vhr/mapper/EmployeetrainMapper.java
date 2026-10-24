package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Employeetrain;

import java.util.List;
import java.util.Map;

public interface EmployeetrainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employeetrain record);

    int insertSelective(Employeetrain record);

    Employeetrain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employeetrain record);

    int updateByPrimaryKey(Employeetrain record);

    List<Map<String,Object>> getTrainByPage(@Param("page") Integer page,
                                            @Param("size") Integer size,
                                            @Param("name") String name);

    Long getTrainTotal(@Param("name") String name);
}