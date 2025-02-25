package com.common.service.impl;

import com.common.entity.CommonEntity;
import com.common.mapper.CommonMapper;
import com.common.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {
    
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public CommonEntity getById(Long id) {
        return commonMapper.selectById(id);
    }

    @Override
    public List<CommonEntity> list() {
        return commonMapper.selectList();
    }

    @Override
    public boolean save(CommonEntity entity) {
        return commonMapper.insert(entity) > 0;
    }

    @Override
    public boolean update(CommonEntity entity) {
        return commonMapper.update(entity) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return commonMapper.deleteById(id) > 0;
    }
}