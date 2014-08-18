package kr.kdev.dg1s.biowiki.ui.info.viewer.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ArrayUtils {

    public int regenerateRandomIntegerValues (ArrayList<Integer> arrayList, int dataSize, Random random, int min, int max) {arrayList.clear();
        arrayList.add(random.nextInt((max - min) + 1) + min);
        for (int i = 0; i < dataSize; i++) {
            arrayList.add(((random.nextInt((max - min) + 1) + min) * 3 + arrayList.get(i)) / 4);
        }
        return Collections.max(arrayList);
    }

    public float regenerateRandomFloatValues (ArrayList<Float> arrayList, int dataSize, Random random) {
        arrayList.clear();
        arrayList.add(random.nextFloat());
        for (int i = 0; i < dataSize; i++) {
            arrayList.add(((random.nextFloat() * 3 + arrayList.get(i)) / 4));
        }
        return Collections.max(arrayList);
    }

    public String[] genericLabels(int maxValue) {
        String[] labelArray = new String[maxValue + 1];
        for (int i = 0; i < (maxValue + 1); i++) {
            labelArray[i] = String.valueOf(i);
        }
        return labelArray;
    }

    public float[] genericAxisIndex(int dataCount) {
        float[] indexArray = new float[dataCount + 1];
        for (int i = 0; i < (dataCount + 1); i++) {
            indexArray[i] = (float) i / dataCount;
        }
        return indexArray;
    }

    public float[] genericPositions(int dataSize) {
        float[] indexArray = new float[dataSize];
        for (int i = 0; i < dataSize; i++) {
            indexArray[i] = i;
        }
        return indexArray;
    }

    public void migrateValues(ArrayList<Integer> origin, float[] destination) {
        for (int i = 0; i < origin.size(); i++) {
            destination[i] = (origin.get(i) != null ? origin.get(i) : Float.NaN);
        }
    }
}
