package com.cmcc.hy.model_platform.jpmml;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ModelCalc {

    //@Async
    public  double modelCalc(float year,float month,float day,float hour,float sdk_version,
                                  float mobile_os,float ip_region) throws IOException {
        ModelInvoker invoker = new ModelInvoker();
        Evaluator modelEvaluator = invoker.ModelInvoker("/home/qianhuhai/Documents/IdeaProjects/model_platform/GBDT.pmml");
        List<String> featureNames = new ArrayList<String>();
        List<InputField> inputFields = modelEvaluator.getInputFields();
        for (InputField inputField :inputFields) {
            featureNames.add(inputField.getName().toString());
        }
        System.out.println(featureNames);

        double result=predict(modelEvaluator, year, month, day, hour, sdk_version, mobile_os, ip_region);
        return result;
    }


       private double predict(Evaluator evaluator,float year,float month, float day, float hour,
                          float sdk_version, float mobile_os, float ip_region) {
        Map<String, Float> data = new HashMap<String, Float>();
        data.put("year", year);
        data.put("month", month);
        data.put("day", day);
        data.put("hour", hour);
        data.put("sdk_version", sdk_version);
        data.put("mobile_os", mobile_os);
        data.put("ip_region", ip_region);
        List<InputField> inputFields = evaluator.getInputFields();

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<FieldName, FieldValue>();
        for (InputField inputField : inputFields) {
            FieldName inputFieldName = inputField.getName();
            Object rawValue = data.get(inputFieldName.getValue());
            FieldValue inputFieldValue = inputField.prepare(rawValue);
            arguments.put(inputFieldName, inputFieldValue);
        }
        System.out.println(arguments);
        Map<FieldName, ?> results = evaluator.evaluate(arguments);
        List<TargetField> targetFields = evaluator.getTargetFields();

        TargetField targetField = targetFields.get(0);
        FieldName targetFieldName = targetField.getName();

        Object targetFieldValue = results.get(targetFieldName);
        System.out.println("target: " + targetFieldName.getValue() + " value: " + targetFieldValue);
        double primitiveValue = -1;
        if (targetFieldValue instanceof Computable) {
            Computable computable = (Computable) targetFieldValue;
            primitiveValue = (double)computable.getResult();
        }
        System.out.println(year + " " + month
                + " " + day + " " + hour + " " + sdk_version + " "
                + mobile_os +" "+ ip_region + ":" + primitiveValue);
        return primitiveValue;
    }

}
