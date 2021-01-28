package com.ROIIM.Assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping( "/" )
    public String getCheckoutForm(){

        return "PaymentCheckout";
    }


}
