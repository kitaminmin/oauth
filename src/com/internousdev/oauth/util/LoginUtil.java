/**
 *
 */
package com.internousdev.oauth.util;

import java.util.Map;

import com.internousdev.oauth.dto.UsersDTO;

/**
 * @author internous
 *
 */
public class LoginUtil {

	/**
	 * ログインの成否を判定するメソッド
	 *
	 * @param dto
	 *            DBから抽出したユーザーDTOのインスタンス
	 * @param session
	 *            セッション
	 * @return errorMsg
	 */
	public String validate(UsersDTO dto, Map<String, Object> session) {
		String errorMsg = null;
		if (session.containsKey("userId")) {
			if ((int) (session.get("userId")) == (dto.getUserId())) {
				errorMsg = "このユーザーは既にログインしています。多重ログインはできません。";
			} else {
				errorMsg = "複数アカウントでの同時ログインはできません。一度ログアウトしてください。";
			}
			return errorMsg;
		}
		if (dto.isLoginFlg() == true) {
			errorMsg = "このユーザーは既にログインしています。多重ログインはできません。";
		}
		return errorMsg;
	}

	/**
	 * ログインの成否を判定するメソッド(オーバーロード)
	 *
	 * @param email
	 *            メールアドレス
	 * @param password
	 *            パスワード
	 * @param dto
	 *            DBから抽出したユーザーDTOのインスタンス
	 * @param session
	 *            セッション
	 * @return errorMsg
	 */
	public String validate(String email, String password, UsersDTO dto, Map<String, Object> session) {
		String errorMsg = null;

		if (!email.equals(dto.getEmail()) || !password.equals(dto.getPassword())) {
			errorMsg = "メールアドレス、またはパスワードが正しくありません。";
			return errorMsg;
		}
		errorMsg = this.validate(dto, session);
		return errorMsg;
	}

}
