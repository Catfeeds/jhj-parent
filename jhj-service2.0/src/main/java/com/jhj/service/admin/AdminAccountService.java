package com.jhj.service.admin;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.vo.admin.AccountSearchVo;
import com.jhj.vo.admin.AdminAccountVo;
import com.jhj.po.model.admin.AdminAccount;


public interface AdminAccountService {

		AdminAccount selectByPrimaryKey(Long id);

		Long insert(AdminAccount record) throws NoSuchAlgorithmException;

		AdminAccount selectByUsername(String username);

		AdminAccount login(String username, String password)
				throws NoSuchAlgorithmException;

		PageInfo listPage(AccountSearchVo accountSearchVo, int pageNo, int pageSize);

		AdminAccount initAccount();

		public AdminAccount updateBind(Long id, Long roleId, Long organizationId,String imUsername);

		int deleteByPrimaryKey(Long id);


		int insertSelective(AdminAccount record);


		int updateByPrimaryKeySelective(AdminAccount record);

	    int updateByPrimaryKey(AdminAccount record);

		List<AdminAccount> selectByListPage(HashMap conditions);


		AdminAccount selectByUsernameAndPassword(HashMap conditions);

		List<AdminAccount> selectByRoleId(Long roleId);

		List<AdminAccountVo> getAdminAccountViewList(List<AdminAccount> list);

		List<AdminAccount> selectByIds(List<Long> ids);

		int updateByPrimaryKeySelectives(AdminAccount record);

		List<AdminAccount> selectByAll();
		
		


}