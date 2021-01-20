package ua.antonfedoruk.sweater.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

//it is domain class that contains data from captcha

/*
reCAPTCHA API Response()
************************
The response is a JSON object:
{
  "success": true|false,
  "challenge_ts": timestamp,  // timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
  "hostname": string,         // the hostname of the site where the reCAPTCHA was solved
  "error-codes": [...]        // optional
}
 */

// This simple Java class has a handful of properties and matching getter methods.
// @JsonIgnoreProperties annotation is from the Jackson JSON processing library to indicate
// that any properties not bound in this type should be ignored.
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {
    //To directly bind our data to our custom types, we need to specify the variable name
    // to be exactly the same as the key in the JSON document returned from the API.

    private boolean success;
    @JsonAlias("error-codes")//java don't recognise '-' so this annotation solve this problem
    private Set<String> errorCodes;
}
