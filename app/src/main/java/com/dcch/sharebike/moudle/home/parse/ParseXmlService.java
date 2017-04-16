package com.dcch.sharebike.moudle.home.parse;

import android.util.Xml;

import com.dcch.sharebike.moudle.home.bean.VersionInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/8/7 0007.
 */
public class ParseXmlService {
    public VersionInfo parseXml(InputStream inputStream) {
        VersionInfo versionInfo = null;
        boolean flag = true;
        try {
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inputStream, "UTF-8");
            int event = pullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        versionInfo = new VersionInfo();
                        break;
                    case XmlPullParser.START_TAG:
                        flag = true;
                        String name = pullParser.getName();
                        if ("VERSIONCODE".equalsIgnoreCase(name) && flag == true) {
                            versionInfo.setVersionCode(Integer.valueOf(pullParser.nextText().trim()));
                        } else if ("FILENAME".equalsIgnoreCase(name) && flag == true) {
                            versionInfo.setFileName(pullParser.nextText().trim());
                        } else if ("DESCRIPTION".equalsIgnoreCase(name) && flag == true) {
                            versionInfo.setDescription(pullParser.nextText().trim());
                        } else if ("LOADURL".equalsIgnoreCase(name) && flag == true) {
                            versionInfo.setLoadUrl(pullParser.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        flag = false;
                        break;
                }
                event = pullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return versionInfo;
    }
}
