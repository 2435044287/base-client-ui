package pers.yuews.bcu.demo.event;

import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.event.listener.BaseListener;
import pers.yuews.bcu.UIDemo;

import java.awt.event.KeyEvent;

/**
 * @author yuews
 * @create 2024/5/24 11:03
 * @describe 清理执行器接口
 */
@Container
public class ClearPageListener extends BaseListener {

    @Autowired(type = UIDemo.class)
    UIDemo ui;

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        //System.out.println("清理成功");
        //final UI ui = EnvironmentContext.getInstance().get(UI.class);
        ui.getNormalMainInterface().setText("");
        ui.getWarnMainInterface().setText("");
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
