package kr.kdev.dg1s.biowiki.ui.info.common.utils;

import android.content.Context;
import android.util.Log;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kdev.dg1s.biowiki.Constants;
import kr.kdev.dg1s.biowiki.R;

public class PlantInfoFetcher {

    public boolean hasImages = false;
    public ArrayList<String> images;
    public String imageThumbnail;
    public ArrayList<ArrayList<String>> plantDetails;

    public PlantInfoFetcher(String plantName, Context context) {
        if (plantName.equals(Constants.VOID_PLANT)) {
            plantDetails = new ArrayList<ArrayList<String>>();
            ArrayList<String> errorMessage = new ArrayList<String>();
            errorMessage.add(context.getString(R.string.error) + "!");
            errorMessage.add(context.getString(R.string.no_info));
            plantDetails.add(errorMessage);
            return;
        }

        try {
            final Source dictionaryAssets = new Source(context.getResources().getAssets().open("xmls/kingdom.xml"));
            Element element = dictionaryAssets.getFirstElement("name", plantName, false);

            Attributes attributes = element.getAttributes();
            plantDetails = new ArrayList<ArrayList<String>>();
            if (attributes.size() == 0) {
                ArrayList<String> errorMessage = new ArrayList<String>();
                errorMessage.add(context.getString(R.string.error) + "!");
                errorMessage.add(context.getString(R.string.no_info_plant));
                plantDetails.add(errorMessage);
            } else {
                for (Attribute attribute : attributes) {
                    if (attribute.getName().equals("image")) {
                        if (!(attribute.getValue().equals(""))) {
                            hasImages = true;
                            images = new ArrayList<String>();
                            for (String filename : Arrays.asList(attribute.getValue().split(" "))) {
                                images.add(filename);
                            }
                            imageThumbnail = images.get(0);
                        }
                    } else {
                        ArrayList<String> plantInfo = new ArrayList<String>();
                        if (!attribute.getName().equals("name")) {
                            plantInfo.add(convertedTag(attribute.getName()));
                            plantInfo.add(attribute.getValue());
                            plantDetails.add(plantInfo);
                        }
                    }
                }
            }
        } catch (IOException e) {
            plantDetails = new ArrayList<ArrayList<String>>();
            ArrayList<String> errorMessage = new ArrayList<String>();
            errorMessage.add(context.getString(R.string.error) + "!");
            errorMessage.add(context.getString(R.string.error_info));
            plantDetails.add(errorMessage);
            Log.e("FETCHER", "IOEXCEPTION");
        } catch (NullPointerException e) {
            plantDetails = new ArrayList<ArrayList<String>>();
            ArrayList<String> errorMessage = new ArrayList<String>();
            errorMessage.add(context.getString(R.string.error) + "!");
            errorMessage.add(context.getString(R.string.no_info_plant));
            plantDetails.add(errorMessage);
            Log.e("FETCHER", "NULLPOINTER");
        }
    }

    String convertedTag(String tag) {
        if (tag.equals("name")) {
            return "이름";
        } else if (tag.equals("stump")) {
            return "줄기";
        } else if (tag.equals("leaf")) {
            return "잎";
        } else if (tag.equals("flower")) {
            return "꽃";
        } else if (tag.equals("fruit")) {
            return "열매";
        } else if (tag.equals("chromo")) {
            return "핵상";
        } else if (tag.equals("place")) {
            return "서식지";
        } else if (tag.equals("horizon")) {
            return "수평분포";
        } else if (tag.equals("vertical")) {
            return "수직분포";
        } else if (tag.equals("geograph")) {
            return "식생지리";
        } else if (tag.equals("vegetat")) {
            return "식생형";
        } else if (tag.equals("preserve")) {
            return "종보존등급";
        } else if (tag.equals("seed")) {
            return "홀씨";
        } else if (tag.equals("lamella")) {
            return "자루";
        } else if (tag.equals("pileus")) {
            return "갓";
        } else if (tag.equals("breed")) {
            return "번식방법";
        } else if (tag.equals("feature")) {
            return "특징";
        } else {
            return "기타";
        }
    }
}
