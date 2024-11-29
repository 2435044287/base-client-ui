package pers.yuews.bcu.demo.handler;

import pers.yuews.bcu.UIDemo;
import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.annotaion.UIComponent;
import pers.yuews.bcu.demo.event.StopListener;
import pers.yuews.bcu.core.common.handler.BuilderHandler;

import javax.swing.*;

/**
 * @author yuews
 * @create 2024/5/29 14:06
 * @describe
 */
@UIComponent(text = "停止",listenerEvent = StopListener.class)
public class StopButtonHandler extends BuilderHandler {

    @Container
    final JButton stopButton = new JButton();

    @Autowired(type = ClearPageButtonHandler.class)
    ClearPageButtonHandler clearPageButtonHandler;

    @Autowired(type = UIDemo.class)
    UIDemo ui;

    @Override
    protected BuilderHandler addComponent(BuilderHandler successor,UIComponent annotation ){
        stopButton.addActionListener(getListener());
        stopButton.setText(annotation.text());
        ui.getNavPanel().add(stopButton);
        return clearPageButtonHandler;
    }


}
