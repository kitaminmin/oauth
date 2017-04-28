/**
 *
 */
package com.internousdev.oauth.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONValue;

import com.opensymphony.xwork2.ActionSupport;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
/**
 * @author internous
 *
 */

public class FacebookOauth extends ActionSupport {

	private static final String APP_ID = "122240998320207";

	private static final String APP_SECRET = "79ef1c909642df86e25061e75e0f2079";

	private static final String CALLBACK_PATH = "/LoginFacebookAction";

	/**
	 * ユーザー情報を取得するメソッド
	 *
	 * @param request
	 *            リクエスト
	 * @param response
	 *            レスポンス
	 */
	public void getRequestToken(HttpServletRequest request, HttpServletResponse response) {
		Facebook facebook = new FacebookFactory().getInstance();
		request.getSession().setAttribute("facebook", facebook);
		facebook.setOAuthAppId(APP_ID, APP_SECRET);
		String accessTokenString = APP_ID + "|" + APP_SECRET;
		AccessToken at = new AccessToken(accessTokenString);
		facebook.setOAuthAccessToken(at);
		StringBuffer callbackURL = request.getRequestURL();
		int index = callbackURL.lastIndexOf("/");
		callbackURL.replace(index, callbackURL.length(), "").append(CALLBACK_PATH);
		try {
			response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackURL.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * アクセストークンの取得メソッド
	 *
	 * @param request
	 *            リクエスト
	 * @param response
	 *            レスポスト
	 * @return userMap ユーザー情報
	 */
	public Map<String, String> getAccessToken(HttpServletRequest request, HttpServletResponse response) {

		final String callbackURL = request.getRequestURL().toString();
		final String code = request.getParameter("code");

		if (code == null) {
			try {
				response.sendRedirect(request.getContextPath() + "/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String accessTokenURL = null;
		try {
			accessTokenURL = "https://graph.facebook.com/oauth/access_token?client_id=" + APP_ID + "&redirect_uri="
					+ URLEncoder.encode(callbackURL, "UTF-8") + "&client_secret=" + APP_SECRET + "&code="
					+ URLEncoder.encode(code, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String accessTokenResult = null;
		try {
			accessTokenResult = httpRequest(new URL(accessTokenURL));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		String accessToken = null;

		/*
		 * =====================================================================
		 * ===================
		 * 返ってきたaccessTokenResultからaccess_tokenを取り出すために、Stringを操作して切り出す
		 */
		String[] pairs = accessTokenResult.split("&");
		String[] kv = {};
		for (String pair : pairs) {
			kv = pair.split(",");
		}
		if (kv.length != 3) { // splitした結果が目当ての形と違った場合、例外を投げる
			throw new RuntimeException("Unexpected auth response");
		}

		String[] kv2 = kv[0].split("\"");
		if (kv2.length != 4) {
			throw new RuntimeException("Unexpected auth response");
		}
		if (kv2[1].equals("access_token")) {
			accessToken = kv2[3];
		}
		// ========================================================================================

		String apiURL = null;
		try {
			apiURL = "https://graph.facebook.com/me?access_token=" + URLEncoder.encode(accessToken, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String apiResult = null;
		try {
			apiResult = httpRequest(new URL(apiURL));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Map<String, String> userMap = (Map<String, String>) JSONValue.parse(apiResult);
		return userMap;
	}

	/**
	 * リクエスト用メソッド
	 *
	 * @param url
	 *            URL
	 * @return String
	 */
	private String httpRequest(URL url) {
		HttpURLConnection conn = null;
		String response = "";
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;

			while ((line = reader.readLine()) != null) {
				response += line;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return response;
	}

}