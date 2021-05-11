package com.laola.apa.server.impl.PortDataDeal;

import com.google.gson.Gson;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.utils.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("p93")
public class P93 implements PortDataDealService<String,String> {
    private static final Logger logger = LoggerFactory.getLogger(P93.class);
    /**
     * 读取光准
     * @param data
     * @return
     */
    @Override
    public String deal(String... data) {
        String hexStr = data[0];
        logger.info("GET OPTICAL ALIGNMENT DATA" + hexStr);
        Map<String, Object> lightQuasiMap = new HashMap<>(11);
        assert hexStr != null;
        List<String> LightQuasiStr = new ArrayList<>();
        for (int i = 0; i < hexStr.length() / 2; i++) {
            BigInteger bigInteger = new BigInteger(hexStr.substring(i * 2, i * 2 + 2), 16);
            if (i % 5 != 0 || (Integer.parseInt(String.valueOf(bigInteger)) > 250)) {
                LightQuasiStr.add(String.valueOf(bigInteger));
            }
        }
        lightQuasiMap.put("msg", LightQuasiStr);
        lightQuasiMap.put("id", "readLightQuasi");
        Gson gson = new Gson();
        try {
            WebSocket.getClients().get("user1").sendMessageTo(gson.toJson(lightQuasiMap), "user1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "200";
    }
}
