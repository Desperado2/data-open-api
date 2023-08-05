package com.github.desperado2.data.open.api.common.manage.utils;

/**
 * 密码校验
 * @author tu nan
 * @date 2023/4/18
 **/
public class PasswordCheckUtils {
    // 校验的正则
    private static String  regex1 = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,30}";

    public static CheckResult check(String password){
        if(!password.matches(regex1)){
            return new CheckResult(false, "密码中必须包含大小写 字母、数字、特称字符，至少8个字符，最多30个字符");
        }
        if(!validContinuousStr(password)){
            return new CheckResult(false, "您的密码为弱口令密码，密码中不能连续字符出现连续3位或3位以上");
        }
        if(validIncrementalStr(password)){
            return new CheckResult(false, "您的密码为弱口令密码，密码中不能相同字符出现连续3位或3位以上");
        }
        return new CheckResult(true, "");
    }

    private static Boolean validContinuousStr(String password){
        char[] charArray = password.toCharArray();
        boolean flag = true;
        for (int i = 1; i < charArray.length-1; i++) {
            int firstIndex = charArray[i-1];
            int secondIndex = charArray[i];
            int thirdIndex = charArray[i+1];
            if ((thirdIndex - secondIndex == 1) && (secondIndex - firstIndex == 1)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static Boolean validIncrementalStr(String password){
        char[] charArray = password.toLowerCase().trim().toCharArray();
        int count=0 ;
        char lastChar = charArray[0];
        for (int i = 1; i < charArray.length; i++) {
            if(lastChar == charArray[i]){
                count ++;
            }else{
                count = 0;
            }
            lastChar = charArray[i];
            if(count >= 3){
                return true;
            }
        }
        return false;
    }

    public static class CheckResult{
        private Boolean pass;

        private String errorMsg;

        public CheckResult(Boolean pass, String errorMsg) {
            this.pass = pass;
            this.errorMsg = errorMsg;
        }

        public Boolean getPass() {
            return pass;
        }

        public void setPass(Boolean pass) {
            this.pass = pass;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "CheckResult{" +
                    "pass=" + pass +
                    ", errorMsg='" + errorMsg + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) {
        System.out.println(PasswordCheckUtils.validIncrementalStr("xxs$SDDd356"));
    }
}
