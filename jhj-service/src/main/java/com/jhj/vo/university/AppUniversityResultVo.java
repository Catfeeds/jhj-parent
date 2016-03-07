package com.jhj.vo.university;

/**
 *
 * @author :hulj
 * @Date : 2016年1月18日下午5:42:06
 * @Description: 
 *	
 *	 答题结果VO	
 */
public class AppUniversityResultVo {
	
	//是否通过的标识
	private short resultFlag;
	//答对题目数
	private short rightNum;
	//当前题库需答对题目数
	private short needNum;
	
	public short getResultFlag() {
		return resultFlag;
	}
	public void setResultFlag(short resultFlag) {
		this.resultFlag = resultFlag;
	}
	public short getRightNum() {
		return rightNum;
	}
	public void setRightNum(short rightNum) {
		this.rightNum = rightNum;
	}
	public short getNeedNum() {
		return needNum;
	}
	public void setNeedNum(short needNum) {
		this.needNum = needNum;
	}
	
	
	
}
