package com.kqt.smarthome.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.kqt.smarthome.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ����ʱ��ѡ��ؼ� ʹ�÷����� private EditText inputDate;//��Ҫ���õ�����ʱ���ı��༭�� private String
 * initDateTime="2012��9��3�� 14:44",//��ʼ����ʱ��ֵ �ڵ���¼���ʹ�ã�
 * inputDate.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) { DateTimePickDialogUtil
 *           dateTimePicKDialog=new
 *           DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 *           dateTimePicKDialog.dateTimePicKDialog(inputDate);
 * 
 *           } });
 * 
 * @author
 */
public class DateTimePickDialogUtil implements OnDateChangedListener,
		OnTimeChangedListener {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Activity activity;

	/**
	 * ����ʱ�䵯��ѡ����캯��
	 * 
	 * @param activity
	 *            �����õĸ�activity
	 * @param initDateTime
	 *            ��ʼ����ʱ��ֵ����Ϊ�������ڵı��������ʱ���ʼֵ
	 */
	public DateTimePickDialogUtil(Activity activity, String initDateTime) {
		this.activity = activity;
		this.initDateTime = initDateTime;

	}

	public void init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "年"
					+ calendar.get(Calendar.MONTH) + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
					+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ calendar.get(Calendar.MINUTE);
		}

		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	/**
	 * ��������ʱ��ѡ��򷽷�
	 * 
	 * @param inputDate
	 *            :Ϊ��Ҫ���õ�����ʱ���ı��༭��
	 * @return
	 */
	public AlertDialog dateTimePicKDialog( final TextView inputDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		init(datePicker, timePicker);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(this);

		ad = new AlertDialog.Builder(activity)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText(dateTime+":00");
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText("设置时间");
					}
				}).show();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// �������ʵ��
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		dateTime = sdf.format(calendar.getTime());
		ad.setTitle(dateTime);
	}

	/**
	 * ʵ�ֽ���ʼ����ʱ��2012��07��02�� 16:45 ��ֳ��� �� �� ʱ �� ��,����ֵ��calendar
	 * 
	 * @param initDateTime
	 *            ��ʼ����ʱ��ֵ �ַ���
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, "日", "index", "front"); // 日期
		String time = spliteString(initDateTime, "日", "index", "back"); // 时间

		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

		String hourStr = spliteString(time, ":", "index", "front"); // 时
		String minuteStr = spliteString(time, ":", "index", "back"); // 分

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, currentHour,
				currentMinute);
		return calendar;
	}

	/**
	 * ��ȡ�Ӵ�
	 * 
	 * @param srcStr
	 *            Դ��
	 * @param pattern
	 *            ƥ��ģʽ
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // ȡ���ַ��һ�γ��ֵ�λ��
		} else {
			loc = srcStr.lastIndexOf(pattern); // ���һ��ƥ�䴮��λ��
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // ��ȡ�Ӵ�
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // ��ȡ�Ӵ�
		}
		return result;
	}

}
