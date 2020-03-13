package edu.tjrac.swant.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static void  regex(){
        String rule="<p.*>(.*)</p>";
        String content="<p>aaaa833131231<2></p><p>dasdasd</p><div class=\"media-wrap image-wrap\"><img class=\"media-wrap image-wrap\" src=\"https://file-xz.veimg.cn/lms/uploads/2019/12/1575967229.png?x-oss-process=image/resize,m_lfit,w_300/format,png\"/></div><p></p>";
        Pattern pattern = Pattern.compile(rule);
        while (content != null) {
            Matcher m = pattern.matcher(content);
            if (m.find()) {
                System.out.println(m.group(1));
            }
        }
    }
}
