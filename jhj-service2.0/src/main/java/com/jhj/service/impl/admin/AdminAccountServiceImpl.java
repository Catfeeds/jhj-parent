package com.jhj.service.impl.admin;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.service.admin.AdminAccountService;
import com.jhj.service.admin.AdminOrganizationService;
import com.jhj.service.admin.AdminRoleService;
import com.jhj.vo.admin.AccountSearchVo;
import com.jhj.vo.admin.AdminAccountVo;
import com.jhj.po.dao.admin.AdminAccountMapper;
import com.jhj.po.dao.admin.AdminRoleMapper;
import com.jhj.po.model.admin.AdminAccount;
import com.jhj.po.model.admin.AdminRole;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;


@Service
public class AdminAccountServiceImpl implements AdminAccountService {

	@Autowired
	private AdminAccountMapper adminAccountMapper;

	@Autowired
	protected AdminRoleService adminRoleService;

	@Autowired
	protected AdminOrganizationService adminOrganizationService;

	@Autowired
	public AdminRoleMapper adminRoleMapper;


	@Override
	public AdminAccount selectByPrimaryKey(Long id) {
		return adminAccountMapper.selectByPrimaryKey(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo listPage(AccountSearchVo accountSearchVo, int pageNo, int pageSize) {
		String name = accountSearchVo.getName();
		String username = accountSearchVo.getUsername();

		HashMap<String,String> conditions = new HashMap<String,String>();

		if(name!=null && !name.isEmpty()){
			conditions.put("name", name);
		}
		if(username!=null && !username.isEmpty()){
			conditions.put("username", username);
		}

		 PageHelper.startPage(pageNo, pageSize);
         List<AdminAccount> list = adminAccountMapper.selectByListPage(conditions);
         if(list!=null && list.size()>0){
        	 List<AdminAccountVo> adminAccountViewList = this.getAdminAccountViewList(list);
        	 for(int i = 0; i < list.size(); i++) {
        	 if (adminAccountViewList.get(i) != null) {
        		 list.set(i, adminAccountViewList.get(i));
        	 }
         	}
         }
        PageInfo result = new PageInfo(list);
        return result;
    }

	@Override
	public AdminAccount selectByUsername(String username){
		return adminAccountMapper.selectByUsername(username);
	}

	@Override
	public Long insert(AdminAccount record) throws NoSuchAlgorithmException {
		record.setRegisterTime(DateUtil.getNowOfDate());
		return adminAccountMapper.insert(record);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AdminAccount login(String username, String password) throws NoSuchAlgorithmException{

		HashMap<String,String> conditions = new HashMap<String,String>();

		conditions.put("username", username);
		conditions.put("password", StringUtil.md5(password.trim()));
		AdminAccount po = adminAccountMapper.selectByUsernameAndPassword(conditions);
		return po;
	}

	@Override
	public AdminAccount initAccount()  {
		AdminAccount account = new AdminAccount();
		account.setId(0L);
		account.setVersion(0L);
		account.setEnable((short) 1);
		account.setName("");
		account.setEmail("");
		account.setUsername("");
		account.setPassword("");
		account.setRegisterTime(DateUtil.getNowOfDate());
		account.setRoleId(1L);
		account.setOrganizationId(0L);
		account.setImUsername("");
		account.setNickname("");
		return account;
	}

	@Override
	public AdminAccount updateBind(Long id, Long roleId, Long organizationId,String imUsername) {
		AdminAccount adminAccount = adminAccountMapper.selectByPrimaryKey(id);
		adminAccount.setRoleId(roleId);
		adminAccount.setOrganizationId(organizationId);
		adminAccount.setImUsername(imUsername);
		adminAccountMapper.updateByPrimaryKeySelective(adminAccount);
		return adminAccount;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return adminAccountMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(AdminAccount record) {
		return adminAccountMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminAccount record) {
		return adminAccountMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AdminAccount record) {
		return adminAccountMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<AdminAccount> selectByListPage(HashMap conditions) {
		return adminAccountMapper.selectByListPage(conditions);
	}

	@Override
	public AdminAccount selectByUsernameAndPassword(HashMap conditions) {
		return adminAccountMapper.selectByUsernameAndPassword(conditions);
	}

	@Override
	public List<AdminAccount> selectByRoleId(Long roleId) {
		return adminAccountMapper.selectByRoleId(roleId);
	}

	@Override
	public List<AdminAccountVo> getAdminAccountViewList(List<AdminAccount> list) {
		 List<Long> roleIds = new ArrayList<Long>();
	     AdminAccount item = null;
	     //将AdminAccount中的roleId放到List集合中
	     for (int i = 0 ; i < list.size(); i ++) {
	     	item = list.get(i);
	     	roleIds.add(item.getRoleId());
	     }
	     //根据roleIds查询出对应的AdminRole
	     List<AdminRole> roleList = adminRoleMapper.selectByRoleIds(roleIds);
	     List<AdminAccountVo> result = new ArrayList<AdminAccountVo>();
	     Long roleId = 0L;
	     //AdminAccount中的roleId和AdminRole中的Id进行比较，相同则为roleName赋值
	     for (int i = 0 ; i < list.size(); i ++) {
	     	item = list.get(i);
	     	roleId = item.getRoleId();

	     	AdminAccountVo vo = new AdminAccountVo();
	     	BeanUtils.copyProperties(item, vo);

	     	String roleName = "";
	     	AdminRole adminRole = null;
	     	for(int n = 0; n < roleList.size(); n++) {
	     		adminRole = roleList.get(n);
	     		if (adminRole.getId().equals(roleId)) {
	     			roleName = adminRole.getName();
	     			break;
	     		}
	     	}
	     	vo.setRoleName(roleName);;
	     	result.add(vo);
	     }
	     return result;
	}

	@Override
	public List<AdminAccount> selectByIds(List<Long> ids) {
		return adminAccountMapper.selectByIds(ids);
	}

	@Override
	public int updateByPrimaryKeySelectives(AdminAccount record) {
		
		return adminAccountMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<AdminAccount> selectByAll() {
		
		return adminAccountMapper.selectByAll();
	}

}
