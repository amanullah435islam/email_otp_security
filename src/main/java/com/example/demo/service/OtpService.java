package com.example.demo.service;


import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;


@Service
public class OtpService {

    private Map<String, String> otpStore = new HashMap<>();

    // generate OTP
    public String generateOtp(String email) {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        //String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpStore.put(email, otp);
        return otp;
    }

 // verify OTP
    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStore.get(email));
    }
}






// previous normal otp code::::::::::

//@Service
//public class OtpService {
//
//    private Map<String, String> otpStorage = new HashMap<>();
//
//    // OTP generate
//    public String generateOtp(String email) {
//        Random random = new Random();
//        String otp = String.valueOf(100000 + random.nextInt(900000));
//
//        otpStorage.put(email, otp);
//        return otp;
//    }
//
//    // OTP verify
//    public boolean verifyOtp(String email, String otp) {
//        if (!otpStorage.containsKey(email)) {
//            return false;
//        }
//
//        return otpStorage.get(email).equals(otp);
//    }
//}