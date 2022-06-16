package com.jia.easyRpc.Service.Register;



import com.jia.easyRpc.Entity.ServerInfo;

import java.util.HashMap;
import java.util.Map;

public class DefauletServerRegister implements ServerRegister {
    private Map<String, ServerInfo> serverInfoMap=new HashMap<>();
    private Integer port;
    private String protocol;
    @Override
    public void register(ServerInfo serverInfo) throws Exception {
        if(serverInfo==null) throw new IllegalArgumentException();
        serverInfoMap.put(serverInfo.getServiceName(),serverInfo);

    }

    @Override
    public ServerInfo getServerInstance(String serverName) throws Exception {
        return serverInfoMap.get(serverName);
    }
}
