package com.laola.apa.mapper;

import com.laola.apa.entity.RegentPlace;
import com.laola.apa.utils.MyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegentPlaceMapper extends MyMapper<RegentPlace> {

    Integer getCopies(int projectId);

}