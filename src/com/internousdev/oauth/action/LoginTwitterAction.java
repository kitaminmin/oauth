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

import com.internousdev.oauth.util.Definition;
import com.internousdev.oauth.util.TwitterOauth;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author internous
 *
 */

public class LoginTwitterAction extends ActionSupport implements ServletResponseAware, ServletRequestAware, SessionAware {

	static final int NETWORK_NAME = Definition.NETWORK_NAME_TWITTER;

	public Map<String, Object> session;

	private HttpServletResponse response;

	private HttpServletRequest request;

	private String errorMsg;

	public String execute() {

		String rtn = ERROR;
		TwitterOauth twitterOauth = new TwitterOauth();
		String[] userData = new String[2];
		userData = twitterOauth.getAccessToken(request, response);

		if (userData == null) {
			return rtn;
		}
 String uniqueId = userData[0];
 String userName = userData[1];
 LoginOauthDAO dao = new LoginOauthDAO();

if (dao.select(uniqueId, NETWORK_NAME)) {
        int count = dao.update(uniqueId);
        if(count == 0) {
               return rtn;
        }

        UsersDTO dto = dao.getUsersDTO();
         if(dto.isLoginFlg() == true) {
               errorMsg = "このユーザーは既にログインしています。";
               return rtn;
}

          session.put("userId", dto.getUserId());
session.put("loginFlg", dto.isLoginFlg());
          rtn = SUCCESS;
return rtn;
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


public void setServletResponse(HttpServletResponse response) {
this.response = response;
}

public void setServletRequest(HttpServletRequest request) {
this.request = request;
}
public void setSession(Map<String, Object> session) {
this.session = session;
}

public Map<String, Object> getSession() {
return session;
}

public String getErrorMsg() {
return errorMsg;
}

public void setErrorMsg(String errorMsg) {
this.errorMsg = errorMsg;
}
}
}
