package com.laola.apa.server.impl;

import com.laola.apa.entity.RegentPlace;
import com.laola.apa.mapper.RegentPlaceMapper;
import com.laola.apa.mapper.SelectDao;
import com.laola.apa.server.ReagentPlaceIntf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ReagentPlaceImpl implements ReagentPlaceIntf {
    private final SelectDao selectDao;
    private final RegentPlaceMapper regentPlaceMapper;
    private Logger logger = Logger.getGlobal();

    @Autowired
    public ReagentPlaceImpl(SelectDao selectDao, RegentPlaceMapper regentPlaceMapper) {
        this.selectDao = selectDao;
        this.regentPlaceMapper = regentPlaceMapper;
    }

    @Override
    public List<Map<String, Object>> getRegentPlace() {
        return selectDao.selectList("select uc.*,rp.*,pp.name from regent_place rp\n" +
                "left join project_param pp on rp.project_param_id = pp.id\n" +
                "left join used_code uc on rp.code = uc.code\n" +
                "where rp.project_param_id is not null\n" +
                "Order by rp.id");
    }

    /**
     * 更新试剂位置
     * @param regentPlace
     * @return
     */
    @Override
    public int updateRegentPlace(RegentPlace regentPlace) {

        return regentPlaceMapper.updateByPrimaryKeySelective(regentPlace);
    }

    @Override
    public List<Map<String, Object>> getBoolScal() {

        return selectDao.selectList("SELECT place,   project_param_id ,id as dateId  FROM (SELECT place,  rp.project_param_id ,dateId,p.id  from regent_place rp\n" +
                "LEFT JOIN project_param pp on pp.id=rp.project_param_id\n" +
                "LEFT JOIN scaling s on s.dateId=pp.factor \n" +
                "LEFT JOIN project p on DATE_FORMAT( p.starttime, '%y年%m月%d日%H时%i分' ) = DATE_FORMAT( s.dateId, '%y年%m月%d日%H时%i分' )  )as ss GROUP BY  place  ORDER BY place\n");
    }

}
