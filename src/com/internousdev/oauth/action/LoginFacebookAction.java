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

import com.internousdev.oauth.dao.LoginOauthDAO;
import com.internousdev.oauth.dto.UsersDTO;
import com.internousdev.oauth.util.Definition;
import com.internousdev.oauth.util.FacebookOauth;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author internous
 *
 */
public class LoginFacebookAction extends ActionSupport
		implements SessionAware, ServletResponseAware, ServletRequestAware {

	static final int NETWORK_NAME = Definition.NETWORK_NAME_FACEBOOK;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private Map<String, Object> session;

	private String errorMsg;

	public String execute() {
		String rtn = ERROR;
		FacebookOauth oauth = new FacebookOauth();
		Map<String, String> userMap = null;
		userMap = oauth.getAccessToken(request, response);

		if (userMap == null) {
			return rtn;
		}

		String uniqueId = userMap.get("id");
		String userName = userMap.get("name");
		LoginOauthDAO dao = new LoginOauthDAO();

		if (dao.select(uniqueId, NETWORK_NAME)) {

			int count = dao.update(uniqueId);
			if (count == 0) {
				return rtn;
			}
			UsersDTO dto = dao.getUsersDTO();
			if (dto.isLoginFlg() == true) {
				errorMsg = "このユーザーは既にログインしています。多重ログインはできません。";
				return rtn;
			}

			session.put("userId", dto.getUserId());
			session.put("loginFlg", dto.isLoginFlg());
			rtn = SUCCESS;
			return rtn;
		}
		boolean result = dao.insert(uniqueId, userName, NETWORK_NAME);
		if (!result) {
			return rtn;
		}
		dao.select(uniqueId, NETWORK_NAME);
		UsersDTO dto = dao.getUsersDTO();

		session.put("userId", dto.getUserId());
		session.put("loginFlg", dto.isLoginFlg());
		rtn = SUCCESS;
		return rtn;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
