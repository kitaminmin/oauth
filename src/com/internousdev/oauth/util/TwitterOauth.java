/**
 *
 */
package com.internousdev.oauth.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * @author MIYUKI HARADA
 *
 */
public class TwitterOauth {

	private boolean result = false;

	private final String CONSUMER_KEY = "fmzse98t0Vkqimgxd1ubrEZYj";
	private final String CONSUMER_SECRET = "H2jd0kpeHpHJprLa4fRMnEFLLyXJ7Y0stj3bzXyMiqgBWUyXKl";
	private static TwitterFactory twitterFactory = new TwitterFactory();

	public boolean getRequestToken(HttpServletRequest request, HttpServletResponse response){

		Twitter twitter = twitterFactory.getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

		try{
			RequestToken reqToken = twitter.getOAuthRequestToken();
			HttpSession session = request.getSession();
			session.setAttribute("RequestToken", reqToken);
			session.setAttribute("Twitter", twitter);
			String url = reqToken.getAuthorizationURL();
			response.sendRedirect(url);
		}catch(TwitterException | IOException e){
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * トークンを取得する為のメソッド
	 * @param request リクエスト
	 * @param response レスポンス
	 * @return userData ユーザー情報
	 */
	public String[] getAccessToken(HttpServletRequest request, HttpServletResponse response){

		HttpSession session = request.getSession();
		Twitter twitter = (Twitter) session.getAttribute("Twitter");
		String verifier = request.getParameter("oauth_verifier");

		if(verifier == null){
			try{
				response.sendRedirect(request.getContextPath()+ "/login");
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}

		AccessToken accessToken = null;

		try{
			if(session.getAttribute("RequestToken") != null){
				accessToken = twitter.getOAuthAccessToken( (RequestToken) session.getAttribute("RequestToken"), verifier);
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

		if(accessToken != null){
			session.setAttribute("AccessToken", accessToken.getToken());
			session.setAttribute("AccessTokenSecret", accessToken.getTokenSecret());
		}

		User user = null;

		try{
			user = twitter.verifyCredentials();
		}catch(TwitterException e){
			e.printStackTrace();
		}

		String[] userData = new String[2];
		userData[0] = user.getScreenName();
		userData[1] = user.getName();

		return userData;
	}

}
