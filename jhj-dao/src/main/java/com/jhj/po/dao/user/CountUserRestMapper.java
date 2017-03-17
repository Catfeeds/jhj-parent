package com.jhj.po.dao.user;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.user.CountUserRest;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;

public interface CountUserRestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CountUserRest record);

    int insertSelective(CountUserRest record);

    CountUserRest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CountUserRest record);

    int updateByPrimaryKey(CountUserRest record);
    
    int insertList(List<Map<String, Object>> list);
    
    List<ChartMapVo> countUserNumber(ChartSearchVo chartSearchVo);
}