package pers.yuews.bcu.core.common.handler;

import pers.yuews.bcu.core.common.annotaion.UIComponent;
import pers.yuews.bcu.core.common.context.EnvironmentContext;
import pers.yuews.bcu.core.common.event.listener.BaseListener;

/**
 * 责任链模式 依次构建所有组件 直到下一个组件为null
 * @author yuews
 * @create 2024/5/29 13:53
 * @describe
 */
public abstract class BuilderHandler {

    protected EnvironmentContext environmentContext= EnvironmentContext.getInstance();

    private String group;

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    /**
     * 添加组件
     * @param successor
     * @return
     */
    protected abstract  BuilderHandler addComponent(BuilderHandler successor, UIComponent annotation) throws InstantiationException, IllegalAccessException;

    public BuilderHandler handle(BuilderHandler successor) throws InstantiationException, IllegalAccessException {
        //本节点处理完之后处理下一个节点
        BuilderHandler nextNode=null;
        if (successor!=null){
            check(successor,successor.getClass().getAnnotation(UIComponent.class));
            nextNode = successor.addComponent(successor,successor.getClass().getAnnotation(UIComponent.class));
        }
        if (nextNode != null) {
            return successor.handle(nextNode);
        }
        return null;
    }

    protected BaseListener getListener(){
        return environmentContext.get(this.getClass().getAnnotation(UIComponent.class).listenerEvent().getName());
    }

    private void check(BuilderHandler successor, UIComponent annotation){
        if (annotation==null){
            throw new RuntimeException(successor.getClass()+ " annotation is null");
        }
    }

}
