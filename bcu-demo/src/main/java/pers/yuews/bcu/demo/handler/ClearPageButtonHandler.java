package pers.yuews.bcu.demo.handler;

import pers.yuews.bcu.UIDemo;
import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.annotaion.UIComponent;
import pers.yuews.bcu.demo.event.ClearPageListener;
import pers.yuews.bcu.core.common.handler.BuilderHandler;

import javax.swing.*;

/**
 * @author yuews
 * @create 2024/5/29 14:01
 * @describe
 */
@UIComponent(text = "清理",listenerEvent = ClearPageListener.class)
public class ClearPageButtonHandler extends BuilderHandler {

    @Container
    JButton clearButton=new JButton();

    @Autowired(type = UIDemo.class)
    UIDemo ui;

    @Override
    protected BuilderHandler addComponent(BuilderHandler successor,UIComponent annotation){
        clearButton.setText(annotation.text());
        clearButton.addActionListener(getListener());
        ui.getNavPanel().add(clearButton);
        return null;
    }


}
