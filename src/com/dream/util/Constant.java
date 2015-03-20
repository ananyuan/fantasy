package com.dream.util;


public class Constant {
    /**
     * 定位当前位置返回request code值
     */
    public static final int REQUEST_CODE_LOCAL = 1;
    /**
     * 编辑修改返回request code值
     */
    public static final int REQUEST_CODE_EDIT = 2;
    
    /**
     * 照相机照相request code值
     */
    public static final int REQUEST_CODE_TAKE_PICTURE = 0;
	
	/**
	 * 从相册获取图片 request code值
	 */
    public static final int REQUEST_CODE_GET_PICTURE = 5;
    
    
	/**
	 * 从相册获取图片 request code值
	 */
    public static final int REQUEST_CODE_GET_PICTURE_TODETAIL = 6;
    
	/**
	 * 从照片墙 转到发布的页面 request code值
	 */
    public static final int REQUEST_CODE_PUBLISH = 7;
    
    
	/**
	 * 获取图片返回结果串
	 */
    public static final String RESULT_GET_PICTURE = "result_get_pic";
    
    
    /**
     * 编辑返回页面“request code” key
     */
    public static final String RESULT_CODE = "resultcode";
    /**
     * 编辑返回页面标题 key
     */
    public static final String TITLE = "title";
    /**
     * 编辑返回页面编辑框默认显示 key
     */
    public static final String MIMECONTENT = "mimecontent";
    /**
     * 编辑返回页面编辑可输入文字数目 key
     */
    public static final String EDIT_COUNT_NUM = "edit_number";
    /**
     * 编辑返回页面返回文字 key
     */
    public static final String EDIT_RESULT = "edit_result";
    /**
     * 当前位置返回 key
     */
    public static final String LOCAL_RESULT = "local_result";
    /**
     * 清除当前位置返回 key
     */
    public static final String LOCAL_IF_CLEAR = "local_clear";
    
    
    /**
     * 从发布返回图片墙
     */
    public static final String BACK_FROM_PUBLISH = "back_from_publish";
    
    
    /**
     * 新消息提醒方式
     */
    public static final String[] MESSAGE_REMIND = new String[] { "提醒 ", "智能 ", "屏蔽 "};
    
    /**
     * http 请求成功返回status值
     */
    public static final int HTTP_RESPONSE_OK = 1;
    /**
     * http 请求成功失败status值
     */
    public static final int HTTP_RESPONSE_FAIL = 0;
    
    /**
     * 连接超时时间 单位：ms
     */
    public static final int CONNECT_TIME_OUT = 25000;
    
    /**
     * 读取超时时间 单位：ms
     */
    public static final int READ_TIME_OUT = 25000;
    
    /**
     * 读取数据长度 单位：byte
     */
    public static final int READ_DATA_LENGTH = 1024;
    /**
     * 读取数据长度 单位：byte
     */
    public static final int SEND_MESSAGE_DELAY = 40;
    
    
    /**
     * 图片的宽度
     */
    public static final int CUT_IMG_WIDTH = 800;
    
    /**
     * 获取地址信息
     */
    public static final int SILENT_GET_LOCATION = 10;
    
    /**
     * 从服务端下载到的数据 ， 不需要上传到服务端
     */
    public static final int DYNAMIC_ITEM_SERVER = 0;
    
    /**
     * 本地数据还未上传
     */
    public static final int DYNAMIC_ITEM_LOCAL = 1;
    
    /**
     * 已经上传到服务端， 不需要从服务端获取
     */
    public static final int DYNAMIC_ITEM_UPLOADED = 2;
    
    /**
     * 微信APPID
     */
    public static final String APP_ID_WE_CHAT = "wxf708cd74513a88c8"; 
}
