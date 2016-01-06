package com.kqt.smarthome.util;

import android.content.Context;
import android.widget.Toast;

public class Ttoast {
	public static Toast toast;;

	public static void show(Context context, String str) {
		if (toast == null) {
			toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		} else
			toast.setText(str);

		toast.show();

	}

	public static void CheckErrorCode(Context context, int code) {
		switch (code) {
		case 3501:
			Ttoast.show(context, "帐号不存在");
			break;
		case 3502:
			Ttoast.show(context, "账号存在");
			break;
		case 3503:
			Ttoast.show(context, "帐号不合法");
			break;
		case 3504:
			Ttoast.show(context, "密码错误");
			break;
		case 3505:
			Ttoast.show(context, "验证码错误");
			break;
		case 3506:
			Ttoast.show(context, "验证码已失效");
			break;
		case 3507:
			Ttoast.show(context, "邮箱不合法");
			break;
		case 3508:
			Ttoast.show(context, "手机不合法");
			break;

		default:
			Ttoast.show(context, code + "");

			break;
		}
	}
}
