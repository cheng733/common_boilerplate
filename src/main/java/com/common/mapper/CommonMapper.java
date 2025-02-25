package com.common.mapper;

import com.common.entity.CommonEntity;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommonMapper {
    CommonEntity selectById(Long id);
    List<CommonEntity> selectList();
    int insert(CommonEntity entity);
    int update(CommonEntity entity);
    int deleteById(Long id);
}