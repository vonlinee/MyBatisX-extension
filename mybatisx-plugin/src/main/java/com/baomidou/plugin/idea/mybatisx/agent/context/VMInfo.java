package com.baomidou.plugin.idea.mybatisx.agent.context;

import com.sun.tools.attach.VirtualMachine;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VMInfo {

    private VirtualMachine virtualMachine;

    private String ip;

    private int port;

    private String pid;

    private String processName;
}
