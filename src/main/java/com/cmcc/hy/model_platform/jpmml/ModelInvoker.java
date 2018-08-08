package com.cmcc.hy.model_platform.jpmml;

import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.PMMLUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ModelInvoker {
    @Async
    public Evaluator ModelInvoker(String pmmlFileName) {
        PMML pmml = new PMML();
        InputStream inputStream = null;
        if (pmmlFileName != null) {
            try {
                inputStream = new FileInputStream(pmmlFileName);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                pmml = PMMLUtil.unmarshal(inputStream);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            } finally {
                //关闭输入流
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
        Evaluator evaluator = modelEvaluatorFactory.newModelEvaluator(pmml);
        pmml = null;

        evaluator.verify();
        System.out.println("模型加载成功");
        return evaluator;
    }
}
