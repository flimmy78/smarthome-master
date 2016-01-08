package com.kqt.smarthome.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.accloud.service.ACUserInfo;
import com.kqt.smarthome.R;

public class Util {
    public static boolean isipcF = true;

    public static boolean network = false;

    public static int TIMEOUT_WHAT = 5; // 设置用户信息

    public static int SET_USER = 0x2002; // 设置用户信息
    public static int GET_USER = 0x2003; // 获取用户信息
    public static int SET_NAME = 0x2702; // 设置设备别名
    public static int GET_AP = 0x2703; // 获取用户信息
    public static int SET_AP = 0x2704; // 设置用户信息
    public static int SET_FTP = 0x2006; // 设置FTP信息
    public static int GET_FTP = 0x2007; // 获取FTP信息
    public static int GET_ALARM = 0x2018; // 获取报警信息
    public static int SET_ALARM = 0x2017; // 设置报警信息

    public static int SET_TIME = 0x2015; // 设置时钟信息
    public static int GET_TIME = 0x2016; // 获取时钟信息

    public static int SET_WIFI = 0x2012; // 设置wifi信息
    public static int GET_WIFI = 0x2013; // 获取wifi

    public static Bitmap decodeBitmap(String buff) {
        byte[] buf = null;
        if (buff != null) {
            try {
                buf = buff.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("转回数组-->" + buf);
            Bitmap bitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
            return bitmap;
        }
        return null;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Save Bitmap to a file.保存图片到SD卡。
     *
     * @param bitmap
     * @param file
     * @return error message if the saving is failed. null if the saving is
     * successful.
     * @throws IOException
     */
    public static void saveBitmapToFile(Bitmap bitmap, String _file)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("zjm", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 设置listview高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView,
                                                        int id) {
        // 获取ListView对应的Adapter
        Adapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 商城列表
     *
     * @return
     */
    public static List<Integer> shopGoods() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(R.drawable.product_box);
        list.add(R.drawable.product_infrared);
        list.add(R.drawable.product_ipc_camer);
        list.add(R.drawable.product_three);

        return list;

    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param info
     */
    public static boolean SaveUserInfo(Context context, ACUserInfo info) {

        SharedPreferences preferences = context.getSharedPreferences(
                "userinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", info.getName());
        editor.putLong("userid", info.getUserId());
        return editor.commit();
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public static ACUserInfo GetUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                "userinfo", Activity.MODE_PRIVATE);
        long userid = preferences.getLong("userid", 0);
        String name = preferences.getString("name", "");
        ACUserInfo acUserInfo = new ACUserInfo(userid, name);
        return acUserInfo;

    }

    public static List<String> GetImgList(String path, String type) {
        List<String> imagePathArray = new ArrayList<String>();
        File dir = new File(path);
        File file[] = dir.listFiles();
        if (file != null) {

            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile()) {
                    String icon = file[i].getAbsolutePath();
                    if (icon.endsWith(type))
                        imagePathArray.add(icon);
                    System.out.println(icon);
                }
            }
        }
        return imagePathArray;
    }

    public static String state(int state) {
        String str = "";
        switch (state) {
            case 100:
                str = "在线";
                break;
            case 0:
                str = "正在连接";
                break;
            case 101:
                str = "连接失败";
                break;
            case 9:
                str = "不在线";
                break;
            case 10:
                str = "连接超时";
                break;
            case 11:
                str = "断开连接";
                break;
            case 1:
                str = "账号错误";
                break;
            case 12:
                str = "校验账号";
                break;
            case 5:
                str = "ID不可用";
                break;
            case 4:
                str = "视屏丢失";
                break;
            case 3:
                str = "流ID错误";
                break;

            default:
                break;
        }
        return str;
    }

    /**
     * ipc设置
     *
     * @return
     */
    public static List<HashMap<String, String>> SettingData() {

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map0 = new HashMap<String, String>();
        map0.put("name", "修改名称 ");
        map0.put("id", "0");
        map0.put("img", R.drawable.admin_settin + "");
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "用户设置");
        map1.put("img", R.drawable.admin_settin + "");
        map1.put("id", "1");
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("name", "时钟设置");
        map2.put("img", R.drawable.time_setting + "");
        map2.put("id", "2");
        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("name", "报警设置");
        map3.put("id", "3");
        map3.put("img", R.drawable.aler_setting + "");
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("name", "AP设置");
        map4.put("id", "4");
        map4.put("img", R.drawable.ap_seting_img + "");
        HashMap<String, String> map5 = new HashMap<String, String>();
        map5.put("name", "FTP设置");
        map5.put("id", "5");
        map5.put("img", R.drawable.ap_seting_img + "");
        HashMap<String, String> map6 = new HashMap<String, String>();
        map6.put("name", "WIFI设置");
        map6.put("id", "6");
        map6.put("img", R.drawable.wifi_seting_img + "");
        HashMap<String, String> map7 = new HashMap<String, String>();
        map7.put("name", "图像翻转");
        map7.put("id", "7");
        map7.put("img", R.drawable.ap_seting_img + "");
        HashMap<String, String> map8 = new HashMap<String, String>();
        map8.put("name", "设备分享");
        map8.put("id", "8");
        map8.put("img", R.drawable.ap_seting_img + "");

        list.add(map0);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);
        list.add(map7);
        list.add(map8);

        return list;
    }

    /**
     * 电箱设置列表
     *
     * @return
     */
    public static List<HashMap<String, String>> BoxSettingData() {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map0 = new HashMap<String, String>();
        map0.put("name", "设备改名 ");
        map0.put("id", "0");
        map0.put("img", R.drawable.admin_settin + "");
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "设备分享");
        map1.put("img", R.drawable.admin_settin + "");
        map1.put("id", "1");
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("name", "定时管理");
        map2.put("img", R.drawable.time_setting + "");
        map2.put("id", "2");
        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("name", "报警管理");
        map3.put("id", "3");
        map3.put("img", R.drawable.aler_setting + "");
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("name", "情景管理");
        map4.put("id", "4");
        map4.put("img", R.drawable.ap_seting_img + "");
        HashMap<String, String> map5 = new HashMap<String, String>();
        map5.put("name", "电量查询");
        map5.put("id", "5");
        map5.put("img", R.drawable.ap_seting_img + "");

        list.add(map0);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);

        return list;
    }

    /**
     * 根据警报类型获取报警信息
     *
     * @param nType
     * @return
     */
    public static String GetAlarmMsg(int nType) {
        String msg = "";
        switch (nType) {
            case 0x20:
                msg = "移动侦测警报";
                break;
            case 0x21:
                msg = "GPIO警报";
                break;
            case 0x24:
                msg = "声音警报";
                break;
            case 0x25:
                msg = "红外警报";
                break;
            case 0x6:
                msg = "门磁警报";
                break;
            case 0x27:
                msg = "烟感警报";
                break;
            case 0x28:
                msg = "红外对射警报";
                break;
            case 0x29:
                msg = "门铃按下";
                break;

            default:
                msg = "未知报警";
                break;
        }

        return msg;

    }

    /**
     * 获取当前时间
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        return time;

    }

    public static String GetEorrMsg(int eorrid) {
        String msg = "";
        switch (eorrid) {
            case 3811:
                msg = "设备已绑定";
                break;
            case 3504:
                msg = "密码错误";
                break;
            case 3501:
                msg = "账号不存在";
                break;
            case 3503:
                msg = "账号不合法";
                break;
            case 3506:
                msg = "验证码已失效";
                break;
            case 3508:
                msg = "手机不合法";
                break;
            case 3505:
                msg = "验证码错误";
                break;
            case 3817:
                msg = "分享码已过期";
                break;
            case 3815:
                msg = "分享码不存在";
                break;
            case 3816:
                msg = "分享码不合法";
                break;

            default:
                msg = "未知错误" + eorrid;
                break;
        }
        return msg;

    }
}
