/**
 * 
 */
package com.kqt.smarthome.listenner;

/**
 * @author Administrator
 *
 */
public interface RecorderListener
{
	public void callBack_RecordPlayPos(long userid, int pos);
	public void callBackAudioData(long userID, byte[] pcm, int size);
}
