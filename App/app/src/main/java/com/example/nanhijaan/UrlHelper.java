package com.example.nanhijaan;

public class UrlHelper {
    /*params:
    language = {hindi, english, punjabi, telugu}
    id = */

    public static final String SIHAPI_URL = "http://sihnith.herokuapp.com/sihapi/getDataLang/?publish_status=1&language=";
    public static final String SIHAPI_DISEASE_URL = "http://sihnith.herokuapp.com/sihapi/getResource/?publish_status=1&id=";
    public static final String SIHAPI_POST_PARENTS_URL = "http://sihnith.herokuapp.com/sihapi/feedback/";
    public static final String SIHAPI_POST_CHAT = "http://sihnith.herokuapp.com/sihapi/search/";
    public static final String SIHAPI_GET_CHAT_RESPONSE = "http://sihnith.herokuapp.com/sihapi/search/?query=";


}
