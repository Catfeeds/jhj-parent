package com.jhj.po.dao.common;

import java.util.List;

import com.jhj.po.model.common.Imgs;
import com.jhj.vo.ImgSearchVo;

public interface ImgsMapper {
    int deleteByPrimaryKey(Long imgId);

    Long insert(Imgs record);

    int insertSelective(Imgs record);

    Imgs selectByPrimaryKey(Long imgId);

    int updateByPrimaryKeySelective(Imgs record);

    Long updateByPrimaryKey(Imgs record);

	List<Imgs> selectBySearchVo(ImgSearchVo searchVo);

}