package com.baomidou.mybatisx.plugin.ui.dialog;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class SaveOrUpdateDialog<T> extends DialogBase {

    T target;
    boolean saveOrUpdate;

    public SaveOrUpdateDialog() {
        this(null);
    }

    public SaveOrUpdateDialog(T target) {
        this.target = target;
        this.saveOrUpdate = this.target == null;
        if (target == null) {
            setTitle("Save");
        } else {
            setTitle("Edit");
        }
    }

    @Override
    protected @NotNull Action getOKAction() {
        return new DialogWrapperAction("") {
            @Override
            protected void doAction(ActionEvent e) {
                if (getTarget() == null) {
                    setTarget(createObject());
                }
                fill(getTarget(), saveOrUpdate);
                submit(getTarget(), saveOrUpdate);
                closeByOk();
            }
        };
    }

    protected abstract void fill(T object, boolean saveOrUpdate);

    public final void fill() {
        if (this.target != null) {
            fill(this.target, saveOrUpdate);
        }
    }

    @NotNull
    protected abstract T createObject();

    protected abstract void submit(T target, boolean saveOrUpdate);

    public final T getTarget() {
        return target;
    }

    public final void setTarget(T target) {
        this.target = target;
    }
}
