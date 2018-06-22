package com.sf.arch.udata.privilege.pojo;

import javax.persistence.*;

/**
 * 日志信息表
 * @author Eddy Xiang
 */
@Entity
@Table(name = "log")
public class LogDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name="uid")
	private String uid;

	@Column(name="uname")
	private String uname;

	@Column(name="description")
	private String description;

	@Column(name="url")
	private String url;

	@Column(name="method")
	private String method;

	@Column(name="params")
	private String params;

	@Column(name="type")
	private Integer type;   	/**日志类型(0操作日志;1异常日志)*/

	@Column(name="ip")
	private String ip;    	/**请求IP*/

	@Column(name="detail")
	private String detail;  	/**异常描述*/

	@Column(name="ctime")
	private int ctime;  	/**请求日期*/

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getCtime() {
		return ctime;
	}

	public void setCtime(int ctime) {
		this.ctime = ctime;
	}
}
