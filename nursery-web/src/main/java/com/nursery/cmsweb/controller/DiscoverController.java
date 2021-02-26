package com.nursery.cmsweb.controller;

import com.nursery.api.iweb.DiscoverApi;
import com.nursery.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * author:MeiShiQiang
 * Date:2021/2/25 | Time:16:52
 */
@Controller
@RequestMapping("/discover")
public class DiscoverController extends BaseController implements DiscoverApi {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Override
    public ModelAndView visitDiscover(ModelAndView modelAndView) {
        modelAndView.setViewName("discover");
        return modelAndView;
    }
}
