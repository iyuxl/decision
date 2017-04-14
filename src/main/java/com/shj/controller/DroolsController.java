package com.shj.controller;

import com.google.common.collect.Lists;
import com.shj.entity.FactResult;
import com.shj.entity.LHS;
import com.shj.service.DroolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyaolan on 2017/4/7.
 */
@Controller
public class DroolsController {
    @Autowired
    private DroolsService droolsService;

    @RequestMapping("/audit")
    public @ResponseBody FactResult invokeRule(@RequestBody Map map) {
        LHS lhs = new LHS();
        lhs.putAll(map);
        return droolsService.invokeAudit(lhs);
    }

    @RequestMapping("/auditList")
    public @ResponseBody List<FactResult> invokeRuleByList(@RequestBody Map map) {
        LHS lhs = new LHS();
        lhs.putAll(map);
        List<LHS> lists = Lists.newArrayList(lhs);
        return (List<FactResult>) droolsService.invokeAudit(lists);
    }
}
