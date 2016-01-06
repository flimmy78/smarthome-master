package com.kqt.smarthome.util;

import java.util.Comparator;

import com.kqt.smarthome.entity.wifi;

/**
 * wifi信号强度排序
 * 
 * @author Administrator
 *
 */
public class SortComparator implements Comparator {

	@Override
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		wifi a = (wifi) lhs;
		wifi b = (wifi) rhs;
		return a.getDbm0() - b.getDbm0();
	}

}
