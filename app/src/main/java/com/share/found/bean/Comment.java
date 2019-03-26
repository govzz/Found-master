package com.share.found.bean;


import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {


	private User user;
	private String content;
	private String travelId;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTravelId() {
		return travelId;
	}

	public void setTravelId(String travelId) {
		this.travelId = travelId;
	}
}
