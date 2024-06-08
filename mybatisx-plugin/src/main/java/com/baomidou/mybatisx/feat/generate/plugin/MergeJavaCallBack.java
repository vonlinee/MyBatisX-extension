package com.baomidou.mybatisx.feat.generate.plugin;

import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;

public class MergeJavaCallBack extends DefaultShellCallback {

    public MergeJavaCallBack(boolean overwrite) {
        super(overwrite);
    }

    @Override
    public boolean isMergeSupported() {
        return true;
    }

    @Override
    public String mergeJavaFile(String newFileSource, File existingFile, String[] javadocTags, String fileEncoding) throws ShellException {
        return newFileSource;
    }
}
