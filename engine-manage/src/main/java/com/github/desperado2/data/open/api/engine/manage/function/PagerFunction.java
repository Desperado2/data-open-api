package com.github.desperado2.data.open.api.engine.manage.function;//package com.github.desperado2.open.data.platform.api.manage.extend.function;
//
//import com.github.alenfive.rocketapi.extend.ApiInfoContent;
//import com.github.alenfive.rocketapi.extend.IApiPager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 分页封装函数
// * @Deprecated  已将方法迁移到 @Link DbFunciton.db.pager(Long total,List list)
// */
//@Component
//@Deprecated
//public class PagerFunction implements IFunction{
//
//    @Autowired
//    private IApiPager pager;
//
//    @Autowired
//    private ApiInfoContent apiInfoContent;
//
//    @Override
//    public String getVarName() {
//        return "Pager";
//    }
//
//    public Object build(Long total, List<Map<String,Object>> list){
//        return pager.buildPager(total,list,apiInfoContent.getApiInfo(),apiInfoContent.getApiParams());
//    }
//}
