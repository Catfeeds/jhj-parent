package com.meijia.utils.vo;

import java.io.Serializable;

/**
*
* App接口返回泛型对象,通用的数据类
*    "status" : "0", //状态码
*    "msg"    : "ok"
*    "data"   : {
*				'c_id': 1,
*               'c_code':
*				'c_vip_mobile':
*				'c_rest_money':
*				'c_vip_name':
*             }
*    }
* @author lnczx
* 2013-07-19
*/
public class AppResultData<T>  implements Serializable {

	/*
	 * 状态码 	说明
	 *  0     成功
	 * 100	服务器错误
 	 * 101	缺失必选参数 (%s)
	 * 102	参数值非法，需为 (%s)，实际为 (%s)
	 */
	public int status;

	/*
	 * 提示信息msg
	 */

	public String msg;

	/*
	 * 返回数据data
	 */

	public T data;

	public AppResultData(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
