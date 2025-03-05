package com.common.mapper;

import com.common.entity.CommonEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {
  CommonEntity selectById(Long id);

  List<CommonEntity> selectList();

  int insert(CommonEntity entity);

  int update(CommonEntity entity);

  int deleteById(Long id);
}
