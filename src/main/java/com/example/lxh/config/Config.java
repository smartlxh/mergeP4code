package com.example.lxh.config;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    public List<String> config(){
        List modalityList = new ArrayList<String>();
        File file = new File("./src/main/resources/config.json");
        if(file.exists()){
            logger.info("config.json exists");
            try{
                String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                // config  the returned value must be a List
                Configuration conf = Configuration.defaultConfiguration();
                Configuration conf2 = conf.addOptions(Option.ALWAYS_RETURN_LIST);

                ReadContext  ctx = JsonPath.using(conf2).parse(content);
                ArrayList<Map<String,String> > modalitys = ctx.read("$.modality.[*]");
                for(int i=0;i<modalitys.size();i++){
                    //logger.info(modalitys.get(i).get("name").toString());
                    modalityList.add(modalitys.get(i).get("name").toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            logger.info("config.json not exists"+file);
        }
        return modalityList;

    }

    public static void main(String args[]){
        Config config = new Config();
        config.config();
    }
}
