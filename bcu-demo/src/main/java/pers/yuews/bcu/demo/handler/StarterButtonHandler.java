package pers.yuews.bcu.demo.handler;

import pers.yuews.bcu.UIDemo;
import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.annotaion.UIComponent;
import pers.yuews.bcu.demo.event.StarterListener;
import pers.yuews.bcu.core.common.handler.BuilderHandler;

import javax.swing.*;

/**
 * @author yuews
 * @create 2024/5/24 11:04
 * @describe 启动执行器接口实现
 */
@UIComponent(text = "开始",listenerEvent = StarterListener.class)
public class StarterButtonHandler extends BuilderHandler {

    @Container
    final JButton startButton = new JButton();

    @Autowired(type = StopButtonHandler.class)
    StopButtonHandler stopButtonHandler;

    @Autowired(type = UIDemo.class)
    UIDemo ui;

    @Override
    protected BuilderHandler addComponent(BuilderHandler successor, UIComponent annotation ){
        startButton.setText(annotation.text());
        startButton.addActionListener(getListener());
        ui.getNavPanel().add(startButton);
        return stopButtonHandler;
    }

}
