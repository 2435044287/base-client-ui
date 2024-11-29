package pers.yuews.bcu.core.common.event.listener;

import pers.yuews.bcu.core.common.annotaion.Container;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author yuews
 * @create 2024/5/29 16:02
 * @describe
 */
@Container
public class DefaultListener extends BaseListener{
    @Override
    public void actionPerformed(ActionEvent e) {
        //ActionListener 按钮被点击的事件
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //KeyListener 键盘按下，然后释放
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //KeyListener 键被按下
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //KeyListener 键被弹起
    }
}
