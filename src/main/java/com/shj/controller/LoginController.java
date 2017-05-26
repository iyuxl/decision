package com.shj.controller;

import com.shj.entity.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaoyaolan on 2017/5/3.
 */
@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResultVo login(String user, byte[] password) {
        ResultVo rv = new ResultVo();
        rv.setFlag(true);
        logger.info(rv.toString());
        return rv;
    }
}
