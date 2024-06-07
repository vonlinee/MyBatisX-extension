package com.baomidou.plugin.idea.mybatisx.jpa.operate.manager;

import com.baomidou.plugin.idea.mybatisx.jpa.common.SyntaxAppender;
import com.baomidou.plugin.idea.mybatisx.jpa.common.SyntaxAppenderFactory;
import com.baomidou.plugin.idea.mybatisx.jpa.common.appender.AreaSequence;
import com.baomidou.plugin.idea.mybatisx.jpa.common.appender.CustomAreaAppender;
import com.baomidou.plugin.idea.mybatisx.jpa.component.TypeDescriptor;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * The type Statement block.
 */
@Setter
@Getter
public class StatementBlock {

    /**
     * 标签名称
     * -- SETTER --
     * Sets tag name.
     *
     * @param tagName the tag name
     */
    private String tagName;
    /**
     * 结果集区域
     * -- SETTER --
     * Sets result appender factory.
     *
     * @param resultAppenderFactory the result appender factory
     */
    private SyntaxAppenderFactory resultAppenderFactory;
    /**
     * 条件区域
     * -- SETTER --
     * Sets condition appender factory.
     *
     * @param conditionAppenderFactory the condition appender factory
     */
    private SyntaxAppenderFactory conditionAppenderFactory;
    /**
     * 排序区域
     * -- SETTER --
     * Sets sort appender factory.
     *
     * @param sortAppenderFactory the sort appender factory
     */
    private SyntaxAppenderFactory sortAppenderFactory;
    /**
     * 返回值类型
     */
    private TypeDescriptor returnDescriptor;

    /**
     * Gets syntax appender factory by str.
     *
     * @param text the text
     * @return the syntax appender factory by str
     */
    public SyntaxAppenderFactory getSyntaxAppenderFactoryByStr(String text) {
        if (existCurrentArea(getResultAppenderFactory(), text)) {
            return getResultAppenderFactory();
        }
        if (existCurrentArea(getConditionAppenderFactory(), text)) {
            return getConditionAppenderFactory();
        }
        if (existCurrentArea(getSortAppenderFactory(), text)) {
            return getSortAppenderFactory();
        }
        return getResultAppenderFactory();
    }

    private boolean existCurrentArea(SyntaxAppenderFactory appenderFactory, String text) {
        if (appenderFactory == null) {
            return false;
        }
        return text.equals(appenderFactory.getTipText());
    }

    /**
     * Sets return wrapper.
     *
     * @param typeDescriptor the type descriptor
     */
    public void setReturnWrapper(TypeDescriptor typeDescriptor) {
        this.returnDescriptor = typeDescriptor;
    }

    /**
     * Find priority linked list.
     *
     * @param stringLengthComparator the string length comparator
     * @param splitStr               the split str
     * @return the linked list
     */
    public LinkedList<SyntaxAppender> findPriority(Comparator<SyntaxAppender> stringLengthComparator, String splitStr) {
        if (StringUtils.isNotBlank(splitStr) && !(getTagName().startsWith(splitStr) || splitStr.startsWith(getTagName()))) {
            return new LinkedList<>();
        }
        String replaceStr = splitStr;
        LinkedList<SyntaxAppender> syntaxAppenderList = new LinkedList<>();
        AreaSequence currentArea = AreaSequence.RESULT;
        // 找到一个合适的前缀
        while (!replaceStr.isEmpty()) {
            PriorityQueue<SyntaxAppender> priorityQueue = new PriorityQueue<>(stringLengthComparator);
            if (currentArea.getSequence() <= AreaSequence.RESULT.getSequence()) {
                resultAppenderFactory.findPriority(priorityQueue, syntaxAppenderList, replaceStr);
            }
            if (conditionAppenderFactory != null && currentArea.getSequence() <= AreaSequence.CONDITION.getSequence()) {
                conditionAppenderFactory.findPriority(priorityQueue, syntaxAppenderList, replaceStr);
            }
            if (sortAppenderFactory != null && currentArea.getSequence() <= AreaSequence.SORT.getSequence()) {
                sortAppenderFactory.findPriority(priorityQueue, syntaxAppenderList, replaceStr);
            }
            SyntaxAppender priorityAppender = priorityQueue.peek();
            if (priorityAppender == null) {
                break;
            }
            syntaxAppenderList.add(priorityAppender);
            if (priorityAppender.getAreaSequence() == AreaSequence.AREA) {
                currentArea = ((CustomAreaAppender) priorityAppender).getChildAreaSequence();
            } else {
                currentArea = priorityAppender.getAreaSequence();
            }
            replaceStr = replaceStr.substring(priorityAppender.getText().length());
        }
        return syntaxAppenderList;
    }
}
