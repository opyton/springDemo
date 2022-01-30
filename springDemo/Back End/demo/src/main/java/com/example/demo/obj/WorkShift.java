package com.example.demo.obj;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;


public class WorkShift {
    private int uniqueId, userId;
    private String status;

    public WorkShift() {
    }

    public WorkShift(int uniqueId, int userId, String status) {
        this.uniqueId = uniqueId;
        this.userId = userId;
        this.status = status;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public static void submitPunch(WorkShift w) {
        final Resource resource = new ClassPathResource("database/Shifts.txt");
        File file = null;
        try {
            file = resource.getFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(w.getUniqueId() + "," + w.getUserId() + "," + w.getStatus());
            bw.newLine();
            bw.close();
            fw.close();
            System.out.println("MADE IT: " + w.getUniqueId());
            try {
                BufferedReader br1 = null;
                br1 = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br1.readLine()) != null)
                    System.out.println("ST: " + st);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static ArrayList<WorkShift> getWorkShifts() {
        final ArrayList<WorkShift> list = new ArrayList<>();
        final Resource resource = new ClassPathResource("database/Shifts.txt");
        final BufferedReader br;
        try {
            File file = null;
            file = resource.getFile();
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                String[] delimitedValues = st.split(",");
                int uniqueId = 0;
                int userId = 0;
                String status = "";
                for (int value = 0; value < delimitedValues.length; value++) {
                    //in case there are extra values
                    switch (value) {
                        case 0:
                            try {
                                uniqueId = Integer.parseInt(delimitedValues[value].trim());
                            } catch (NumberFormatException num) {
                            }
                            break;
                        case 1:
                            try {
                                userId = Integer.parseInt(delimitedValues[value].trim());
                            } catch (NumberFormatException num) {
                            }
                            break;
                        case 2:
                            status = delimitedValues[value].trim();
                            break;
                        default:
                            break;
                    }
                }
                WorkShift workShift = new WorkShift(uniqueId, userId, status);
                list.add(workShift);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
