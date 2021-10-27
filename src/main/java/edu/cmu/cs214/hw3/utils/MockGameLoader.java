//package edu.cmu.cs214.hw3.utils;
//
//import java.io.File;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * This is a mock GameLoader for the purpose of testing.Without the
// * implementation of a GUI, sequences of functions/operations should be called
// * to mock the whole process of the Santorini game instead. For the simplicity, I
// * use the same pattern from HW1 to load a series of mock actions to do this. Also,
// * following the writeup of HW2, these "loader" functions are not tested in this homework.
// */
//
//public class MockGameLoader {
//
//    private final File file;
//    private String[] names = new String[2];
//    private List<RoundAction> setupSteps = new ArrayList<>();
//    private List<RoundAction> rounds = new ArrayList<>();
//
//    public MockGameLoader(File file) {
//        this.file = file;
//    }
//
//    private WorkerType getType(String type) { return WorkerType.valueOf(type); }
//
//    private int[] getCellPos(String pos) {
//        int[] cellPos = new int[2];
//
//        if(pos.contains("[")) {
//            String cellPosStr = pos.substring(pos.indexOf("[") + 1, pos.indexOf("]"));
//            if(cellPosStr.contains(",")) {
//                cellPos[0] = Integer.parseInt(cellPosStr.split(",")[0]);
//                cellPos[1] = Integer.parseInt(cellPosStr.split(",")[1]);
//            }
//        }
//        return cellPos;
//    }
//
//    // Load the mock rounds from the file
//    public List<RoundAction> loadMockRoundsFromFile() throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        try {
//            String line;
//            String[] parts;
//            while ((line = reader.readLine()) != null) {
//                if (line.contains("-")) {
//                    parts = line.split("-");
//                    RoundAction roundAction = new RoundAction();
//                    rounds.add(roundAction);
//                }
//            }
//            return rounds;
//        } finally {
//            reader.close();
//        }
//    }
//
//    // Load the mock setup actions from file
//    public List<RoundAction> loadMockSetupFromFile() throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        try {
//            String line;
//            String[] parts;
//            while ((line = reader.readLine()) != null) {
//                if (line.contains(":")) {
//                    parts = line.split(":");
//                    setupSteps.add(roundAction);
//                }
//            }
//            return setupSteps;
//        }finally {
//            reader.close();
//        }
//    }
//
//    // Load the mock players names from file
//    public String[] loadMockPlayerNamesFromFile() {
//        for(RoundAction setup: setupSteps) {
//            if (names[0] == null) {
//                names[0]= setup.getName();
//            } else if (names[1] == null && !setup.getName().equals(names[0])) {
//                names[1]= setup.getName();
//            }
//        }
//        return names;
//    }
//}
