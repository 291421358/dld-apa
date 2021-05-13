package com.laola.apa.server.impl.PortDataDeal;

import com.google.gson.Gson;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.utils.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service("p92")
public class P92 implements PortDataDealService<String,Object> {

    private static final Logger logger = LoggerFactory.getLogger(PortDataDealService.class);


    /**
     * 读取ad
     * @param data
     * @return
     */
    @Override
    public String deal(Object... data) {

        String hexStr = String.valueOf(data[0]);
        logger.info("GET AD DATA" + hexStr);

        Map<String, String> ADMap = new HashMap<>(11);
        assert hexStr != null;
        if (!hexStr.substring(6, 8).equals("00")) {
            ADMap.put("tem",
                    String.valueOf((
                            new BigInteger(hexStr.substring(4, 6), 16).intValue() * 256
                                    + new BigInteger(hexStr.substring(6, 8), 16).intValue()) / 1351
                    )
            );
        }
        //取前72位数
        hexStr = hexStr.substring(64);
        for (int i = 0; i < hexStr.length() / 4; i++) {
            ADMap.put("ad" + String.valueOf(i + 1),
                    String.valueOf(
                            new BigInteger(hexStr.substring(i * 4, i * 4 + 2), 16).intValue() * 256
                                    + new BigInteger(hexStr.substring(i * 4 + 2, i * 4 + 4), 16).intValue()
                    )
            );
        }
//
        ADMap.put("id", "readAD");
        Gson gson = new Gson();
        try {
            WebSocket.getClients().get("user1").sendMessageTo(gson.toJson(ADMap),"user1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "200";
    }
}
