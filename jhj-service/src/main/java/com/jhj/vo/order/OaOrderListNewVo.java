package com.jhj.vo.order;

import java.util.Map;

/**
 *
 * @author :hulj
 * @Date : 2015年8月20日下午3:54:22
 * @Description: 
 *
 */
public class OaOrderListNewVo extends OaOrderListVo {

	private Map<String, String> statusNameMap;

	public Map<String, String> getStatusNameMap() {
		return statusNameMap;
	}

	public void setStatusNameMap(Map<String, String> statusNameMap) {
		this.statusNameMap = statusNameMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((statusNameMap == null) ? 0 : statusNameMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OaOrderListNewVo other = (OaOrderListNewVo) obj;
		if (statusNameMap == null) {
			if (other.statusNameMap != null)
				return false;
		} else if (!statusNameMap.equals(other.statusNameMap))
			return false;
		return true;
	}
	
	
	
}
