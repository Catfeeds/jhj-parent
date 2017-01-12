package com.jhj.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.meijia.utils.ImgServerUtil;
import com.meijia.utils.TimeStampUtil;
import com.jhj.service.ImgService;
import com.jhj.vo.ImgSearchVo;
import com.jhj.common.Constants;
import com.jhj.po.dao.common.ImgsMapper;
import com.jhj.po.model.common.Imgs;

@Service
public class ImgServiceImpl implements ImgService {

	@Autowired
	private ImgsMapper imgsMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return imgsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Long insert(Imgs record) {
		return imgsMapper.insert(record);
	}

	@Override
	public Long updateByPrimaryKey(Imgs record) {
		return imgsMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Imgs record) {
		return imgsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Imgs selectByPrimaryKey(Long id) {
		return imgsMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Imgs> selectBySearchVo(ImgSearchVo searchVo) {
		return imgsMapper.selectBySearchVo(searchVo);
	}

	@Override
	public Imgs initImg() {
		Imgs record = new Imgs();
		record.setImgId(0L);
		record.setUserId(0L);
		record.setLinkId(0L);
		record.setLinkType("");
		record.setImgUrl("");
		record.setAddTime(TimeStampUtil.getNowSecond());

		return record;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertImgs(MultipartFile[] imgs, Long userId, Long linkId, String linkType) {
		// 处理图片上传的信息.
		if (imgs != null && imgs.length > 0) {

			// 先删除指定的图片，再新增图片。
			ImgSearchVo searchVo = new ImgSearchVo();
			searchVo.setLinkId(linkId);
			searchVo.setLinkType(linkType);
			List<Imgs> imgList = selectBySearchVo(searchVo);
			if (!imgList.isEmpty()) {
				for (Imgs item : imgList) {
					deleteByPrimaryKey(item.getImgId());
				}
			}

			for (int i = 0; i < imgs.length; i++) {
				String url = Constants.IMG_SERVER_HOST + "/upload/";
				String fileName = imgs[i].getOriginalFilename();
				String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
				fileType = fileType.toLowerCase();
				String sendResult = null;
				try {
					sendResult = ImgServerUtil.sendPostBytes(url, imgs[i].getBytes(), fileType);
				} catch (IOException e) {
					e.printStackTrace();
				}

				ObjectMapper mapper = new ObjectMapper();

				HashMap<String, Object> o = null;
				try {
					o = mapper.readValue(sendResult, HashMap.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				HashMap<String, String> info = (HashMap<String, String>) o.get("info");

				String imgUrl = Constants.IMG_SERVER_HOST + "/" + info.get("md5").toString();
				Imgs img = initImg();
				img.setLinkId(linkId);
				img.setUserId(userId);
				img.setLinkType(linkType);
				img.setImgUrl(imgUrl);
				insert(img);

			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> multiFileUpLoad(HttpServletRequest request) {
		
		Map<String, String> returnMap = new HashMap<String,String>();
		// 处理图片上传
		// 创建一个通用的多部分解析器.
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			// 判断 request 是否有文件上传,即多部分请求...
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) (request);
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {

				CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile(iter.next());
				// MultipartFile file = multiRequest.getFile(iter.next());

				// 能够得到 表单字段的 属性名，进行对对应的设置
				String fieldName = file.getFileItem().getFieldName();
				
				String imgUrl = "";
				
				if (file != null && !file.isEmpty()) {
					String url = Constants.IMG_SERVER_HOST + "/upload/";
					String fileName = file.getOriginalFilename();
					String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
					fileType = fileType.toLowerCase();
					String sendResult = ImgServerUtil.sendPostBytes(url, file.getBytes(), fileType);
					
					ObjectMapper mapper = new ObjectMapper();

					HashMap<String, Object> o = new HashMap<String, Object>();
					try {
						o = mapper.readValue(sendResult, HashMap.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					HashMap<String, String> info = (HashMap<String, String>) o.get("info");

					imgUrl = Constants.IMG_SERVER_HOST + "/" + info.get("md5").toString();

				}
				returnMap.put(fieldName, imgUrl);
			}
		}

		return returnMap;
	}


}
