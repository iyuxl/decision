package com.shj.controller;

import com.google.common.collect.Lists;
import com.shj.entity.FactResult;
import com.shj.entity.XFact;
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
    public @ResponseBody Object invokeRule(@RequestBody Map map) {
        XFact XFact = new XFact();
        XFact.putAll(map);
        return droolsService.invokeAudit(XFact);
    }

    @RequestMapping("/auditList")
    public @ResponseBody List<FactResult> invokeRuleByList(@RequestBody Map map) {
        XFact XFact = new XFact();
        XFact.putAll(map);
        List<XFact> lists = Lists.newArrayList(XFact);
        return (List<FactResult>) droolsService.invokeAudit(lists);
    }
}
