package pers.yuews.bcu.demo.common;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.setting.dialect.Props;
import pers.yuews.bcu.core.common.annotaion.Container;

import java.io.FileInputStream;

/**
 * @author yuews
 * @create 2024/7/1 17:06
 * @describe
 */
@Container
public class ApplicationConfig {

    private static final String APP_NAME = "BCU-DEMO";

    /**
     * 监听文件路径
     */
    private final String listeningFilePath;

    /**
     * 上传到服务器的路径
     */
    private final String uploadFilePath;

    /**
     * 设备号
     */
    private final String deviceNumber;

    public String getListeningFilePath() {
        return listeningFilePath;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    private ApplicationConfig(){
        Props props = new Props();
        props.load(ResourceUtil.getResourceObj("application.properties"));
        this.listeningFilePath=props.getStr("listeningFilePath");
        this.uploadFilePath=props.getStr("uploadFilePath");
        this.deviceNumber=props.getStr("deviceNumber");
    }

}
