package edu.cmu.cs214.hw3.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mockStepsLoader {

    private final String regex = "[\\[\\]]";

    private WorkerType getType(String type) {
        return WorkerType.valueOf(type);
    }

    private int[] getCellPos(String pos) {
        int[] cellPos = new int[2];

        if(pos.contains("[")) {
            String cellPosStr = pos.substring(pos.indexOf("[") + 1, pos.indexOf("]"));
            if(cellPosStr.contains(",")) {
                cellPos[0] = Integer.parseInt(cellPosStr.split(",")[0]);
                cellPos[1] = Integer.parseInt(cellPosStr.split(",")[1]);
            }
        }
        return cellPos;
    }

    public List<Action> loadMockStepsFromFile(File stepFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(stepFile));
        try {
            List<Action> result = new ArrayList<>();
            String line;
            Action action;
            while ((line = reader.readLine()) != null) {
                if (line.contains("-")) {
                    String[] parts = line.split("-");
                    action = new Action(getType(parts[0]), getCellPos(parts[1]), getCellPos(parts[2]));
                    result.add(action);
                }
            }
            return result;
        } finally {
            reader.close();
        }
    }
}
