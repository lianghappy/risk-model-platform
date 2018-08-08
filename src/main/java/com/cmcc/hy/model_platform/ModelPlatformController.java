package com.cmcc.hy.model_platform;

import com.cmcc.hy.model_platform.jpmml.ModelCalc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.Executor;


@RestController
@RequestMapping("/model")

public class ModelPlatformController {
    @SuppressWarnings("SpringJavaAutowiringInspection")

    @Autowired
    private ModelCalc modelCalc;

//    @RequestMapping(value="/say{id}",method = RequestMethod.GET)
    @GetMapping(value = "from_stratgy_platform/{id}")
    public double fromStratgy(@PathVariable("id") double id) throws IOException {
        return id;
    }

    @PostMapping(value = "/get_model")
    public double getModel(@RequestBody Model model) throws IOException
    {

        float year =Float.valueOf(model.getYear());
        float month =Float.valueOf(model.getMonth());
        float day = Float.valueOf(model.getDay());
        float hour = Float.valueOf(model.getHour());
        float sdk_version = Float.valueOf(model.getSdk_version());
        float mobile_os = Float.valueOf(model.getMobile_os());
        float ip_region = Float.valueOf(model.getIp_region());
        System.out.println(model.getSdk_version());
        double result=modelCalc.modelCalc(year, month, day, hour, sdk_version, mobile_os, ip_region);
        return result;
    }
}
