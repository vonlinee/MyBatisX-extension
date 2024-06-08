package com.baomidou.mybatisx.plugin.actions;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mybatisx.extension.agent.AgentException;
import com.baomidou.mybatisx.agent.Handler;
import com.baomidou.mybatisx.agent.JavaFileHandler;
import com.baomidou.mybatisx.agent.XmlFileHandler;
import com.baomidou.mybatisx.util.Notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileHotSwapAction extends AnAction {

    /**
     * 只会存在一个非核心线程，60秒回收，
     */
    private static final ExecutorService ACTION_THREAD_POOL = new ThreadPoolExecutor(0,
            1,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            r -> new Thread(r, "action服务端线程"));

    /**
     * 文件处理器集合
     */
    private static final List<Handler> fileHandlerList = new ArrayList<>();

    static {
        fileHandlerList.add(new XmlFileHandler());
        fileHandlerList.add(new JavaFileHandler());
    }

    public FileHotSwapAction(@Nullable String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        if (psiFile == null) {
            return;
        }
        final String fileName = psiFile.getName();
        for (Handler handler : fileHandlerList) {
            if (handler.isSupport(fileName)) {
                ACTION_THREAD_POOL.execute(() -> {
                    try {
                        handler.execute(e);
                    } catch (AgentException ex) {
                        Notifications.notify(ex.getMessage(), NotificationType.ERROR);
                    }
                });
                break;
            }
        }
    }

}
