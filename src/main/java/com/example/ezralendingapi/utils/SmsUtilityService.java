package com.example.ezralendingapi.utils;


import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SmsUtilityService {

    @Value("${sms_account_sid}")
    private String twilioAccountSid;

    @Value("${auth_token}")
    private String twilioAuthToken;

    @Value("${twilio_virtual_phoneNumber}")
    private String twilioPhoneNumber;

    
    public RestResponse sendMessage(String phoneNumber,String message){

        LogHelper.info("Sending Message ....");
        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try{
            LogHelper.info("Sending Message 123 ....");

           String phone =sanitizePhoneNumber(phoneNumber);

           LogHelper.info("WE are sending the message to "+ phone);

            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message.creator(new PhoneNumber(phone), new PhoneNumber(twilioPhoneNumber), message).create();
            resp.message = "Message send successfully";
            status = HttpStatus.OK;

            LogHelper.info("The Message was send...");

        }catch (TwilioException e){
            resp.message =e.getMessage();
            resp.payload =e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;
        }

        return new RestResponse(resp,status);

    }
    /***
     * Sanitizes and  makes sure  that the phone number supplied meets the
     * agreed phone format in Twilio to enable the initiation of an sms
     * @return phoneNumber
     */

    private String sanitizePhoneNumber(String phoneNumber) {

        if(!phoneNumber.startsWith("+") && !phoneNumber.startsWith("0")){
            phoneNumber = "+254"+phoneNumber;
        }else if(phoneNumber.startsWith("0")){
            String modifiedString = phoneNumber.substring(1);
            phoneNumber = "+254"+modifiedString;
        }

        return phoneNumber;
    }
}
