package com.laola.apa.server;

import gnu.io.SerialPort;

import java.util.List;
import java.util.Map;

public interface GetProjectResult {
    // 处理并存入项目结果
    void dealProjectResult(String string, SerialPort serialUtil);
}
