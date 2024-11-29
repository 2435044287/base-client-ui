package pers.yuews.bcu.demo.event;

import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.enums.MessageTypeEnum;
import pers.yuews.bcu.core.common.event.listener.BaseListener;
import pers.yuews.bcu.UIDemo;
import pers.yuews.bcu.demo.common.ApplicationConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author yuews
 * @create 2024/5/24 11:04
 * @describe 启动执行器接口实现
 */
@Container
public class StarterListener extends BaseListener {

    @Autowired(type = UIDemo.class)
    UIDemo ui;

    @Autowired
    JButton startButton;

    @Autowired(type = ApplicationConfig.class)
    ApplicationConfig applicationConfig;

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.pushMsg("启动成功", MessageTypeEnum.ROUTINE);
        startButton.setEnabled(false);
        //点击开始后 开始监听某个目录的文件 开始跑逻辑了
        start();
    }


    public void start(){
        final String deviceNumber = applicationConfig.getDeviceNumber();



    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
