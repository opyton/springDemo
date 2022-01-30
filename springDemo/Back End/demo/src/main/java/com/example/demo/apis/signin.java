package com.example.demo.apis;

import com.example.demo.obj.User;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class signin {
    /**
     *
     * @param loginInfo JSONSTRING with required user field i.e. {"user","1"}
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody String loginInfo){
        JSONObject obj = new JSONObject(loginInfo);
        User user = new User(obj);
        if(!user.verifyUser(user.getId())){
            JSONObject invalid = new JSONObject();
            invalid.put("Failure","Invalid ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalid.toString());
        }

        JSONObject valid = new JSONObject();
        valid.put("Success","Authenticated User");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(valid.toString());
    }

}
