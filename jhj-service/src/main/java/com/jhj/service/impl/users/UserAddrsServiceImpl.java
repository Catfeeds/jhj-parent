package com.jhj.service.impl.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserAddrsServiceImpl implements UserAddrsService {
	@Autowired
	private UserAddrsMapper userAddrsMapper;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private OrgsService orgsService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;	
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return userAddrsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Long insert(UserAddrs record) {
		return userAddrsMapper.insert(record);
	}

	@Override
	public Long insertSelective(UserAddrs record) {
		return userAddrsMapper.insertSelective(record);
	}

	@Override
	public UserAddrs selectByPrimaryKey(Long id) {
		return userAddrsMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public UserAddrs selectByDefaultAddr(Long userId) {
		return userAddrsMapper.selectByDefaultAddr(userId);
	}	

	@Override
	public List<UserAddrs> selectByMobile(String mobile) {
		return userAddrsMapper.selectByMobile(mobile);
	}
	@Override
	public int updataDefaultByMobile(String mobile){
		return userAddrsMapper.updateDefaultByMobile(mobile);
	}

	@Override
	public int updateByPrimaryKey(UserAddrs record) {
		return userAddrsMapper.updateByPrimaryKey(record);
	}



	@Override
	public int updateByPrimaryKey(UserAddrs record, boolean updateOther) {
		if(updateOther) {//取消其他默认地址，修改当前为默认地址
			if(userAddrsMapper.updateByMobile(record.getMobile())>0) {
				return userAddrsMapper.updateByPrimaryKey(record);
			}
			return 0;
		}
		return userAddrsMapper.updateByPrimaryKey(record);
	}


	@Override
	public int updateByPrimaryKeySelective(UserAddrs record) {
		return userAddrsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<UserAddrs> getAll(){
		return userAddrsMapper.selectAll();
	}

	@Override
	public List<UserAddrs> selectByUserId(Long userId) {
		return userAddrsMapper.selectByUserId(userId);
	}

	@Override
	public List<UserAddrs> selectByIds(List<Long> addrIds) {
		return userAddrsMapper.selectByIds(addrIds);
	}

	@Override
	public int updataDefaultByUserId(Long userId) {
		return userAddrsMapper.updateDefaultByUserId(userId);
	}
	
	@Override
	public int updataDefaultById(Long addrId) {
		return userAddrsMapper.updateDefaultById(addrId);
	}

	@Override
	public UserAddrs initUserAddrs() {
		UserAddrs userAddrs = new UserAddrs();
		userAddrs.setId(0L);
		userAddrs.setUserId(0L);
		userAddrs.setMobile(" ");
		userAddrs.setAddr(" ");
		userAddrs.setLongitude(" ");
		userAddrs.setLatitude(" ");
		userAddrs.setPoiType((short)0);
		userAddrs.setName(" ");
		userAddrs.setAddress(" ");
		userAddrs.setCity(" ");
		userAddrs.setUid(" ");
		userAddrs.setPhone(" ");
		userAddrs.setPostCode(" ");
		userAddrs.setUpdateTime(TimeStampUtil.getNow() / 1000);
		userAddrs.setIsDefault((short)1);
		userAddrs.setOrgId(0L);
		return userAddrs;
	}

	@Override
	public UserAddrs selectByUserIdAndAddrId(Long addrId, Long userId) {
		return userAddrsMapper.selectByUserIdAndAddrId(addrId, userId);
	}
	@Override
	public UserAddrs selectUserId(Long userId) {
		return userAddrsMapper.selectUserId(userId);
	}

	@Override
	public UserAddrs selectByNameAndAddr(Long userId, String name, String addr) {
		return 	userAddrsMapper.selectByNameAndAddr(userId, name.trim(), addr.trim());
	}

	@Override
	public UserAddrs selectByUidAndUserId(String uid, Long userId) {
		return userAddrsMapper.selectByUidAndUserId(uid, userId);
	}
	

}
	

