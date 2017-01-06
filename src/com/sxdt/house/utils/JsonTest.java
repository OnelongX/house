package com.sxdt.house.utils;

import com.sxdt.house.model.AppConfig;
import com.sxdt.house.model.Option;
import com.sxdt.house.model.Web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by agu on 17/1/3.
 */
public class JsonTest {

    public static void main(String[] args) {


        AppConfig config = new AppConfig();

        List<Option> cityList = new ArrayList<>();

        Option option1 = new Option("nanjin","南京");
        Option option2 = new Option("深圳","深圳");

        cityList.add(option1);
        cityList.add(option2);

        config.setCityList(cityList);



        LinkedHashMap<String,List<Web>> citymap = new LinkedHashMap<String,List<Web>>();

        List<Web> list1 = new ArrayList<Web>();

        Web web1 = new Web("链家","https://m.lianjia.com/nj/loupan/rs$name/","");

        list1.add(web1);


        citymap.put("nanjin",list1);
        citymap.put("shenzhen",list1);

        config.setCitymap(citymap);


        String json = JsonUtils.toJson(config);


        System.out.println(json);
    }
}
