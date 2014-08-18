package kr.kdev.dg1s.biowiki.ui.info.viewer.utils;

import java.util.ArrayList;

public class PlantInfoHolder {

    public final String plantName;
    public final String imageURI;
    public final ArrayList<ArrayList<String>> plantDescArray;

    public PlantInfoHolder(String name, String image, ArrayList<ArrayList<String>> descriptionView) {
        this.plantName = name;
        this.imageURI = image;
        this.plantDescArray = descriptionView;
    }
}
