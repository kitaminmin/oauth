/**
 *
 */
package com.internousdev.oauth.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.oauth.util.TwitterOauth;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author MIYUKI HARADA
 *
 */
public  class TwitterAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, SessionAware {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Object> session;

	public String execute() {

		String result = ERROR;

		TwitterOauth twitterOauth = new TwitterOauth();

		if (!twitterOauth.getRequestToken(request, response)) {
			return result;
		}
		result = SUCCESS;
		return result;
	}



	/**
	 * @return session
	 */
	public Map<String, Object> getSession() {
		return session;
	}

	/**
	 * @param session
	 *            セットする session
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}


	public void setServletRequest(HttpServletRequest request) {
		this.request = request;


	}

}
