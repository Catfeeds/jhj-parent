package com.jhj.po.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.user.Users;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.jhj.vo.user.UserSearchVo;

public interface UsersMapper {
	int deleteByPrimaryKey(Long id);

	int insert(Users record);

	int insertSelective(Users record);

	int updateByPrimaryKeySelective(Users record);

	int updateByPrimaryKey(Users record);
	
	Users selectByPrimaryKey(Long id);
	
	Users selectByMobile(String mobile);

	List<Users> selectBySearchVo(UserSearchVo searchVo);

	List<Users> selectByListPage(UserSearchVo userSearchVo);
	
	List<Users> selectUsersByOrderMobile();

	List<HashMap> totalByUserIds(List<Long> id);

	ChartMapVo statUserTotal();

	// 市场用户图表统计
	// 按天统计
	List<ChartMapVo> statByDay(ChartSearchVo chartSearchVo);

	// 按月统计
	List<ChartMapVo> statByMonth(ChartSearchVo chartSearchVo);

	// 按季度统计
	List<ChartMapVo> statByQuarter(ChartSearchVo chartSearchVo);

	// 按天统计
	List<ChartMapVo> statOrderCancelByDay(ChartSearchVo chartSearchVo);

	// 按月统计
	List<ChartMapVo> statOrderCancelByMonth(ChartSearchVo chartSearchVo);

	// 按天统计新增用户的id
	List<ChartMapVo> statUserIdsByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> statUserIdsByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> statUserIdsByQuarter(ChartSearchVo chartSearchVo);
	
	List getUserIds(ChartSearchVo chartSearchVo);

	// 统计
	int statTotalUser(ChartSearchVo chartSearchVo);

	// 按季度统计
	List<ChartMapVo> statOrderCancelByQuarter(ChartSearchVo chartSearchVo);

	// 统计某段时间之前用户的个数
	int statTotalUserByStatTime(ChartSearchVo chartSearchVo);

	// 用户余额总用户人数
	List<ChartMapVo> userResyMoneyByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyByQuarter(ChartSearchVo chartSearchVo);

	// 用户余额不足200的人数
	List<ChartMapVo> userResyMoneyLessTwoByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyLessTwoByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyLessTwoByQuarter(ChartSearchVo chartSearchVo);

	// 余额小于1000的人数
	List<ChartMapVo> userResyMoneyLessThousandByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyLessThousandByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyLessThousandByQuarter(ChartSearchVo chartSearchVo);

	// 余额在1000和3000之间的人数
	List<ChartMapVo> userResyMoneyBetweenByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyBetweenByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyBetweenByQuarter(ChartSearchVo chartSearchVo);

	// 余额大于3000的人数
	List<ChartMapVo> userResyMoneyThreeThousandByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyThreeThousandByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> userResyMoneyThreeThousandByQuarter(ChartSearchVo chartSearchVo);

	// 用户有余额的总钱数

	List<ChartMapVo> selectUserAllResyMoneyByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> selectUserAllResyMoneyByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> selectUserAllResyMoneyByQuarter(ChartSearchVo chartSearchVo);

	/*
	 * 统计： 用户余额图表
	 */

	// 处理 某时间下 余额用户数、余额总金额 两列 需要 累加统计的 列

	List<ChartMapVo> getRestUserAndMoneyByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> getRestUserAndMoneyByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> getRestUserAndMoneyByQuarter(ChartSearchVo chartSearchVo);
}