package pers.yuews.bcu.demo.event;

import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.context.EnvironmentContext;
import pers.yuews.bcu.core.common.event.listener.BaseListener;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * @author yuews
 * @create 2024/5/24 11:04
 * @describe 停止执行器接口实现
 */
@Container
public class StopListener extends BaseListener {

    @Autowired
    JButton startButton;

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        startButton.setEnabled(true);
        System.out.println("停止了");
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
