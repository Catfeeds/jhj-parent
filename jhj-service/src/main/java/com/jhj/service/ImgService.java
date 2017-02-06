package com.jhj.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.jhj.po.model.common.Imgs;
import com.jhj.vo.ImgSearchVo;


public interface ImgService {

	Imgs selectByPrimaryKey(Long id);

	int deleteByPrimaryKey(Long id);

	Long insert(Imgs record);

	Long updateByPrimaryKey(Imgs record);

	int updateByPrimaryKeySelective(Imgs record);

	List<Imgs> selectBySearchVo(ImgSearchVo searchVo);

	Imgs initImg();

	void insertImgs(MultipartFile[] imgs, Long userId, Long linkId, String linkType);

	Map<String, String> multiFileUpLoad(HttpServletRequest request);
	
}
