/**
 *
 */
package com.internousdev.oauth.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.internousdev.oauth.util.FacebookOauth;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author internous
 *
 */

// Facebookでログインする為のクラス
public class GoFacebookAction extends ActionSupport implements ServletResponseAware, ServletRequestAware {

	private HttpServletRequest request;
	private HttpServletResponse response;

	public String execute() {
		System.out.println("action");
		FacebookOauth oauth = new FacebookOauth();
		oauth.getRequestToken(request, response);
		return SUCCESS;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
}
