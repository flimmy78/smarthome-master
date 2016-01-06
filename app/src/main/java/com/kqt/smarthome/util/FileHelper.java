package com.kqt.smarthome.util;

import android.os.Environment;

public class FileHelper {
	public static String IMAGE_PATH = ""; // 图片路径
	public static String AUDIO_PATH = ""; // 图片路径

	static {

		IMAGE_PATH = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ipccamer/image";
		AUDIO_PATH = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ipccamer/audio";

	}
}
