package com.jhj.po.dao.user;

import java.util.List;

import com.jhj.po.model.user.UserLogined;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;

public interface UserLoginedMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLogined record);

    int insertSelective(UserLogined record);

    UserLogined selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLogined record);

    int updateByPrimaryKey(UserLogined record);

    //当月活跃总人数
	List<ChartMapVo> selectUserLoginTotalByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> selectUserLoginTotalByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> selectUserLoginTotalByQuarter(ChartSearchVo chartSearchVo);

	
	//截止到当前的总人数
	List<ChartMapVo> selectUserAllLoginTotalByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> selectUserAllLoginTotalByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> selectUserAllLoginTotalByQuarter(ChartSearchVo chartSearchVo);
	
	//有过登录记录的用户 （无重复的集合）
	List<String>  selectDistinctAll();
	
	
}