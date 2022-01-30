package com.example.demo.apis;

import com.example.demo.obj.User;
import com.example.demo.obj.WorkShift;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class workshifts {
    @GetMapping("/workshifts")
    public ResponseEntity<String> getWorkShifts(@RequestParam String userid) {
        try {
            Integer.parseInt(userid);
        } catch (NumberFormatException invalidInt) {
            JSONObject invalid = new JSONObject();
            invalid.put("Failure", "Invalid ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalid.toString());
        }

        final List<WorkShift> list = WorkShift.getWorkShifts();
        final int id = Integer.parseInt(userid);
        final List<WorkShift> workShift = list.stream().filter((c) -> c.getUserId() == id)
                .sorted((a, b) -> a.getUniqueId() - b.getUniqueId())
                .collect(Collectors.toList());
        JSONArray data = new JSONArray();
        for (WorkShift w : workShift) {
            JSONObject obj = new JSONObject();
            obj.put("UniqueId", w.getUniqueId());
            obj.put("Status", w.getStatus());
            obj.put("UserId", w.getUserId());
            data.put(obj);
        }

        JSONObject valid = new JSONObject();
        valid.put("Data", data);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(valid.toString());
    }

    /**
     *
     * @param punch JSONSTRING with required fields shown i.e. {"userId","1"; punchType,"in/out}
     * @return
     */
    @PostMapping("/workshifts")
    public ResponseEntity<String> postPunch(@RequestBody String punch){
        JSONObject obj = new JSONObject(punch);
        if(!hasRequiredPunchField(obj)){
            JSONObject invalid = new JSONObject();
            invalid.put("Failure","Invalid Post Body");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalid.toString());
        }
        final List<WorkShift> list = WorkShift.getWorkShifts();
        final int id = Integer.parseInt(obj.getString("userId"));
        final List<WorkShift> workShift = list.stream().filter((c) -> c.getUserId() == id)
                .sorted((a, b) -> a.getUniqueId() - b.getUniqueId())
                .collect(Collectors.toList());
        WorkShift last = workShift.get(workShift.size()-1);
        if(!last.getStatus().equals(obj.getString("punchType"))){
            WorkShift w = new WorkShift(last.getUniqueId()+1, id, obj.getString("punchType"));
            WorkShift.submitPunch(w);
            JSONObject valid = new JSONObject();
            valid.put("Success","Your punch has been entered");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(valid.toString());
        }
        JSONObject invalid = new JSONObject();
        invalid.put("Failure","There was an issue with your request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalid.toString());
    }

    private boolean hasRequiredPunchField(JSONObject obj) {
        if(obj.get("userId")!=null && obj.get("punchType")!=null)
            return true;
        return false;
    }
}
