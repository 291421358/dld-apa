package com.laola.apa.mapper;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface SelectDao {
    List<Map<String,Object>> selectList(String sql);
}
