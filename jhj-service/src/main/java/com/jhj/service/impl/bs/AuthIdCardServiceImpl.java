package com.jhj.service.impl.bs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.bs.AuthIdcardMapper;
import com.jhj.po.model.bs.AuthIdcard;
import com.jhj.service.bs.AuthIdCardService;
import com.jhj.vo.AuthIdCardSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class AuthIdCardServiceImpl implements AuthIdCardService {

	@Autowired
	private AuthIdcardMapper authIdcardMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return authIdcardMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AuthIdcard record) {
		return authIdcardMapper.insert(record);
	}

	@Override
	public int insertSelective(AuthIdcard record) {
		return authIdcardMapper.insertSelective(record);
	}

	@Override
	public AuthIdcard selectByPrimaryKey(Long id) {
		return authIdcardMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AuthIdcard record) {
		return authIdcardMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AuthIdcard record) {
		return authIdcardMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public List<AuthIdcard> selectBySearchVo(AuthIdCardSearchVo searchVo) {
		return authIdcardMapper.selectBySearchVo(searchVo);
	}

	@Override
	public PageInfo selectByListPage(AuthIdCardSearchVo searchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<AuthIdcard> list = authIdcardMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public AuthIdcard initAuthIdcard() {
		AuthIdcard record = new AuthIdcard();
		record.setId(0L);
		record.setName("");
		record.setIdCard("");
		record.setContent("");
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		
		return record;
	}

}
