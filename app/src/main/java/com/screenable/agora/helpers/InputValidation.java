package com.screenable.agora.helpers;

public class InputValidation {
//define rules such as:
//    length, case,characters
    public static Boolean length(String input, int length){
        if(input.length()>length){
            return true;
        }else {
            return false;
        }
    }


}
