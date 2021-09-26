package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.Action;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MockGameLoader {

    private final File file;

    private WorkerType getType(String type) { return WorkerType.valueOf(type); }

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

    public MockGameLoader(File file) {
        this.file = file;
    }

    public List<Action> loadMockRoundsFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            List<Action> steps = new ArrayList<>();
            String line;
            String[] parts;
            while ((line = reader.readLine()) != null) {
                if (line.contains("-")) {
                    parts = line.split("-");
                    Action action = new Action(getType(parts[0]), getCellPos(parts[1]), getCellPos(parts[2]));
                    steps.add(action);
                }
            }
            return steps;
        } finally {
            reader.close();
        }
    }

    public List<Action> loadMockSetupFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            List<Action> setup = new ArrayList<>();
            String line;
            String[] parts;
            while ((line = reader.readLine()) != null) {
                if (line.contains(":")) {
                    parts = line.split(":");
                    Action action = new Action(parts[0], getType(parts[1]), getCellPos(parts[2]));
                    setup.add(action);
                }
            }
            return setup;
        }finally {
            reader.close();
        }
    }
}
