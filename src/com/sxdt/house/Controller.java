package com.sxdt.house;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sxdt.house.model.*;
import com.sxdt.house.utils.HttpUtil;
import com.sxdt.house.utils.JsonUtils;
import com.sxdt.house.utils.StringUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.apache.commons.io.FileUtils;

import java.io.File;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller implements Initializable {

    @FXML
    private ComboBox cityList;

    @FXML
    private TextField house;
    @FXML
    private TextArea log;

    private AppConfig appConfig;
    private String citySelect = "nanjing";


    protected void log(final String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                log.appendText(msg);
                log.appendText("\n");
            }
        });
    }

    @FXML
    private void handleOkButtonAction(ActionEvent event) {
        final String houseStr = house.getText().trim();
        if (houseStr.isEmpty()) {
            log("楼盘不能为空，请输入（如香悦澜山）");
            return;
        }
        log.clear();
        log(">>>>>>任务开始....\n");
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<Web> webList = appConfig.getCitymap().get(citySelect);
                if (webList != null && webList.size() > 0) {
                    for (Web web : webList) {
                        switch (web.getName()) {
                            case "链家":
                                lianjia(houseStr, web);
                                break;
                            case "网易房产":
                                wangyi(houseStr, web);
                                break;
                            case "搜狐焦点":
                                souhu(houseStr, web);
                                break;
                            case "腾讯房产":
                                tx(houseStr, web);
                                break;
                            case "Q房网":
                                qfang(houseStr, web);
                                break;
                            case "房多多":
                                fangdd(houseStr, web);
                                break;
                            case "平安好房产":
                                pinganfang(houseStr, web);
                                break;
                            case "365淘房":
                                taofang(houseStr, web);
                                break;
                            case "安居客":
                                anjuke(houseStr, web);
                                break;
                            case "新浪乐居":
                                leju(houseStr, web);
                                break;
                            case "房天下":
                                fang(houseStr, web);
                                break;

                        }
                    }
                    log("\n任务结束<<<<<<<<<<");
                }

                return null;
            }
        };
        new Thread(task).start();
    }

    private void fang(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);

            List<Map<String, String>> list = JsonUtils.deserialize(body, new TypeReference<List<Map<String, String>>>() {
            });

            if (list != null && list.size() > 0) {

                for (Map<String, String> map : list) {

                    if (map.get("projname").indexOf(houseStr) != -1) {

                        String dUrl = map.get("loupanurl");
                        if (dUrl.startsWith("//")) {
                            dUrl = "http:" + dUrl;
                        }

                        System.out.println(web.getName() + "=>" + dUrl);
                        log(web.getName() + "\t\t=>\t" +dUrl);
                        success = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }

        /*
        boolean success = false;
        String url;
            url = StringUtil.renderString(web.getPath(), houseStr);

        try {
            String body = HttpUtil.get(url);

            int listIndex = body.indexOf("<ul id=\"xfContentList\">");

            if (listIndex != -1)
            {
                int start = body.indexOf("<li class=\"",listIndex);
                while (start != -1) {
                    int end = body.indexOf("</li>", start + 4);
                    String content;
                    if (end <= -1) {
                        content = body.substring(start);
                    } else {
                        content = body.substring(start, end);
                    }
                    //System.out.println(content);
                    if (content.indexOf(houseStr) != -1) {
                        //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                        content = content.replaceAll("\r","").replaceAll("\t","").replaceAll("\n","");
                        Pattern pattern = Pattern.compile("<a.+?href=\"(.+?)\"");
                        Matcher m = pattern.matcher(content);
                        if (m.find()) {
                            //String dUrl = url.substring(0,url.indexOf("/",10)) + m.group(1);
                            String dUrl = m.group(1).trim();
                            if(dUrl.startsWith("//")){
                                dUrl = "http:"+dUrl;
                            }
                            System.out.println(web.getName() + "=>" + dUrl);
                            log(web.getName() + "\t\t=>\t" + dUrl);
                            success = true;
                            return;
                        }
                        break;
                    }
                    start = body.indexOf("<a data-id=\"", end+10);
                }
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
        */
    }

    private void leju(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);
            int listIndex = body.indexOf("<ul class=\"y_subList01 pd30 y_subList01_bdr\" id=\"house_list\">");
            if (listIndex != -1) {
                int start = body.indexOf("<li>", listIndex);
                while (start != -1) {
                    int end = body.indexOf("</li>", start + 4);
                    String content;
                    if (end <= -1) {
                        content = body.substring(start);
                    } else {
                        content = body.substring(start, end);
                    }
                    //System.out.println(content);
                    if (content.indexOf(houseStr) != -1) {
                        //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                        Pattern pattern = Pattern.compile("<a.+?href=\"(.+?)\"");
                        Matcher m = pattern.matcher(content);
                        if (m.find()) {
                            String dUrl = url.substring(0, url.indexOf("/", 10)) + m.group(1);
                            System.out.println(web.getName() + "=>" + dUrl);
                            log(web.getName() + "\t\t=>\t" + dUrl);
                            success = true;
                            return;
                        }
                        break;
                    }
                    start = body.indexOf("<a data-id=\"", end + 10);
                }
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }

    }

    /*安居客*/
    private void anjuke(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);
            int listIndex = body.indexOf("<div id=\"content\">");
            if (listIndex != -1) {
                int start = body.indexOf("<a data-id=\"");
                while (start != -1) {
                    int end = body.indexOf("<a data-id=\"", start + 10);
                    String content;
                    if (end <= -1) {
                        content = body.substring(start);
                    } else {
                        content = body.substring(start, end);
                    }
                    //System.out.println(content);
                    if (content.indexOf(houseStr) != -1) {
                        //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                        Pattern pattern = Pattern.compile("<a.+?href=\"(http.+?)\"");
                        Matcher m = pattern.matcher(content);
                        if (m.find()) {
                            //String dUrl = url.substring(0,url.indexOf("/",10)) + m.group(1);
                            System.out.println(web.getName() + "=>" + m.group(1));
                            log(web.getName() + "\t\t=>\t" + m.group(1));
                            success = true;
                            return;
                        }
                        break;
                    }
                    start = body.indexOf("<a data-id=\"", end + 10);
                }
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }

    }

    /*365淘房 m*/
    private void taofang(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);
            int listIndex = body.indexOf("<ul class=\"sellList bg_white botl loadinglist\"");
            while (listIndex != -1) {
                int end = body.indexOf("</li>", listIndex + 10);
                String content;
                if (end <= -1) {
                    content = body.substring(listIndex);
                } else {
                    content = body.substring(listIndex, end);
                }
                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("<a.+?href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        String dUrl = url.substring(0, url.indexOf("/", 10)) + m.group(1);
                        System.out.println(web.getName() + "=>" + dUrl);
                        log(web.getName() + "\t\t=>\t" + dUrl);
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    break;
                }
                listIndex = end;//body.indexOf("<li class=\"el_list_item\">",end);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    /*365淘房*/
    private void taofang2(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath2(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);

            int listIndex = body.indexOf("<div class=\"mc_list clearfix\">");
            while (listIndex != -1) {
                int end = -1;//body.indexOf("<div class="mc_list clearfix">",listIndex+10);
                String content;
                if (end <= -1) {
                    content = body.substring(listIndex);
                } else {
                    content = body.substring(listIndex, end);
                }
                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("<a.+?href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        // String dUrl = url.substring(0,url.indexOf("/",10)) + m.group(1);
                        System.out.println(web.getName() + "=>" + m.group(1));
                        log(web.getName() + "\t\t=>\t" + m.group(1));
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    break;
                }
                listIndex = end;//body.indexOf("<li class=\"el_list_item\">",end);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    /*平安好房*/
    private void pinganfang(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            PAVo paVo = JsonUtils.deserialize(body, PAVo.class);
            if (paVo != null && !paVo.hasError && paVo.success && paVo.data != null && paVo.data.size() > 0) {
                for(PAItemVo paItemVo : paVo.data) {
                    if (paItemVo.sName.indexOf(houseStr) != -1) {
                        System.out.println(web.getName() + "=>" + paItemVo.sUrl);
                        log(web.getName() + "\t=>\t" + paItemVo.sUrl);
                        success = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t=>\t");
        }
    }

    /*房多多*/
    private void fangdd(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);

            int listIndex = body.indexOf("<ul class=\"list-holder list-lp\">");

            if (listIndex != -1) {
                int end = body.indexOf("</ul>", listIndex + 10);
                String content;
                if (end <= -1) {
                    content = body.substring(listIndex);
                } else {
                    content = body.substring(listIndex, end);
                }
                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("<a.+?href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        String dUrl = url.substring(0, url.indexOf("/", 10)) + m.group(1);
                        System.out.println(web.getName() + "=>" + dUrl);
                        log(web.getName() + "\t\t=>\t" + dUrl);
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    //break;
                }
                //listIndex = end;//body.indexOf("<li class=\"el_list_item\">",end);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    /*Q房*/
    private void qfang2(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);
            boolean hasContent = body.indexOf("没有找到相应的房源") == -1;
            if (!hasContent) {
                log(web.getName() + "\t\t=>\t");
                return;
            }
            int listIndex = body.indexOf("<div class=\"newhouse-list");
            while (listIndex != -1) {
                int end = body.indexOf("<div class=\"newhouse-list", listIndex + 10);
                String content;
                if (end <= -1) {
                    content = body.substring(listIndex);
                } else {
                    content = body.substring(listIndex, end);
                }
                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("<a.+?href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        String dUrl = url.substring(0, url.indexOf("/", 10)) + m.group(1);
                        if (dUrl.indexOf("?") != -1) {
                            dUrl = dUrl.substring(0, dUrl.indexOf("?"));
                        }
                        System.out.println(web.getName() + "=>" + dUrl);
                        log(web.getName() + "\t\t=>\t" + dUrl);
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    break;
                }
                listIndex = end;//body.indexOf("<li class=\"el_list_item\">",end);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    private void qfang(String houseStr, Web web) {

        if (web.getPath().indexOf("m.qfang.com") != -1) {
            qfang2(houseStr, web);
            return;
        }

        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //
            //System.out.println(body);
            boolean hasContent = body.indexOf("抱歉，小Q真的尽力了，可是依然没找到符合您要求的房源") == -1;
            if (!hasContent) {
                log(web.getName() + "\t\t=>\t");
                return;
            }
            int listIndex = body.indexOf("<div class=\"newhouse-list-content\">");
            while (listIndex != -1) {
                int end = body.indexOf("<div class=\"newhouse-list-content\">", listIndex + 10);
                String content;
                if (end <= -1) {
                    content = body.substring(listIndex);
                } else {
                    content = body.substring(listIndex, end);
                }
                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("<a.+?href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        String dUrl = url.substring(0, url.indexOf("/", 10)) + m.group(1);

                        System.out.println(web.getName() + "=>" + dUrl);
                        log(web.getName() + "\t\t=>\t" + dUrl);
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    break;
                }
                listIndex = end;//body.indexOf("<li class=\"el_list_item\">",end);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    /*腾讯房产*/
    private void tx(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);

            int listIndex = body.indexOf("<div class=\"item\">");
            while (listIndex != -1) {
                int end = body.indexOf("<div class=\"item\">", listIndex + 10);
                String content;
                if (end <= -1) {
                    content = body.substring(listIndex);
                } else {
                    content = body.substring(listIndex, end);
                }
                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("<a href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        //String dUrl = url.substring(0,url.indexOf("/",10)) + m.group(1);
                        System.out.println(web.getName() + "=>" + m.group(1));
                        log(web.getName() + "\t\t=>\t" + m.group(1));
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    break;
                }
                listIndex = end;//body.indexOf("<li class=\"el_list_item\">",end);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    /*搜狐焦点*/
    private void souhu(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);
            boolean hasContent = body.indexOf("暂无符合条件的楼盘或户型") == -1;
            if (!hasContent) {
                log(web.getName() + "\t\t=>\t");
                return;
            }
            int contentStart = body.indexOf("<div class=\"loupan-list result-list\">");
            if (contentStart != -1) {
                int contentEnd = body.indexOf("</section>", contentStart);
                if (contentEnd == -1) {
                    log(web.getName() + "\t\t=>\t");
                    return;
                }
                body = body.substring(contentStart, contentEnd);
            } else {
                log(web.getName() + "\t\t=>\t");
                return;
            }


            int listIndex = body.indexOf("<div class=\"loupan-list-item result-item\"");
            while (listIndex != -1) {
                int end = body.indexOf("<div class=\"loupan-list-item result-item\"", listIndex + 20);

                String content;
                if (end <= -1) {
                    content = body.substring(listIndex);
                } else {
                    content = body.substring(listIndex, end);
                }

                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("<a href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        String dUrl = url.substring(0, url.indexOf("/", 10)) + m.group(1);
                        System.out.println(web.getName() + "=>" + dUrl);
                        log(web.getName() + "\t\t=>\t" + dUrl);
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    break;
                }

                listIndex = end;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    /* 链家 */
    private void lianjia(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);
            int contentIndex = body.indexOf("<div class=\"mod_cont\">");
            boolean hasContent = body.indexOf("没有找到相关楼盘") == -1;
            if (contentIndex != -1 && hasContent) {
                String pre = url.substring(url.indexOf("/", 10), url.lastIndexOf("/"));
                int start = body.indexOf(pre, contentIndex);
                if (start != -1) {
                    int end = body.indexOf("\"", start);
                    String dUrl = url.substring(0, url.indexOf("/", 10)) + body.substring(start, end);
                    System.out.println(web.getName() + "=>" + dUrl);
                    log(web.getName() + "\t\t\t=>\t" + dUrl);
                    success = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t\t=>\t");
        }
    }

    /*网易*/
    private void wangyi(String houseStr, Web web) {
        boolean success = false;
        String url = StringUtil.renderString(web.getPath(), houseStr);
        try {
            String body = HttpUtil.get(url);
            //System.out.println(body);

            int listIndex = body.indexOf("<li class=\"el_list_item\">");
            while (listIndex != -1) {
                int end = body.indexOf("</li>", listIndex);
                String content = body.substring(listIndex, end);
                if (content.indexOf(houseStr) != -1) {
                    //Pattern p = Pattern.compile("<(\\w+)([^<>]*)>([^<>]+)</\\1>");
                    Pattern pattern = Pattern.compile("href=\"(.+?)\"");
                    Matcher m = pattern.matcher(content);
                    while (m.find()) {
                        String dUrl = url.substring(0, url.indexOf("/", 10)) + m.group(1);
                        System.out.println(web.getName() + "=>" + dUrl);
                        log(web.getName() + "\t\t=>\t" + dUrl);
                        success = true;
                        break;
                    }
                    //System.out.println(content);
                    break;
                }
                listIndex = body.indexOf("<li class=\"el_list_item\">", end);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            log(web.getName() + "\t\t=>\t");
        }
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        //退出
        //updateConfig(appConfig);
        System.exit(0);

    }

    /**
     * 初始化
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appConfig = this.readConfig();

        cityList.getItems().addAll(appConfig.getCityList());
        log.setEditable(false);

        //cityList.getSelectionModel().selectedItemProperty()
        cityList.valueProperty().addListener(new ChangeListener<Option>() {
            @Override
            public void changed(ObservableValue observable, Option oldValue, Option newValue) {
                //Option select = (Option) cityList.getSelectionModel().getSelectedItem();
                citySelect = newValue.value;

                //System.out.println("切换数据为:" + newValue.text);


            }
        });

        cityList.getSelectionModel().select(0);

        //new Thread(new MyRunnable()).start();
        //System.out.println(dbList.getSelectionModel().getSelectedItem().toString());

    }

    /**
     * 读取配置
     *
     * @return
     */
    protected AppConfig readConfig() {
        String basepath = System.getProperty("user.dir");
        File file = new File(basepath + "/config/config.json");
        if (file.exists()) {
            try {
                String json = FileUtils.readFileToString(file, "utf-8");
                AppConfig appConfig = JsonUtils.toObject(json, AppConfig.class);
                return appConfig;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
