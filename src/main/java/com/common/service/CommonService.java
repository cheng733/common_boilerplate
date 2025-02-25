package com.common.service;

import com.common.entity.CommonEntity;
import java.util.List;

public interface CommonService {
    CommonEntity getById(Long id);
    List<CommonEntity> list();
    boolean save(CommonEntity entity);
    boolean update(CommonEntity entity);
    boolean delete(Long id);
}