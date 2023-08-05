package com.github.desperado2.data.open.api.common.manage.utils;


/**
 * 字符串生成
 * @author tu nan
 * @date 2023/4/18
 **/
public class StrGeneralUtils {

    /**
     * 根据条件获得复杂密码
     * @param num  密码的位数 6-32位
     * @param containNum 是否包含数字
     * @param containStr 是否包含大写字母
     * @param containSpec 是否包含特殊字符
     * @return 生成的密码
     */
    public static String getPassword(int num,boolean containNum,boolean containStr,boolean containSpec) {
        String [] strs = new String[num];
        // 密码的第一位为字母
        if (containNum) {
            strs[0] = getRandomStr();
        }else {
            strs[0] = getRandomStr().toLowerCase();
        }
        // 生成从第二位开始的密码
        boolean containSpecFlag = false;
        boolean containNumFlag = false;
        boolean containStrFlag = false;
        for (int i=1;i<num;i++) {
            String funcIndex = getFormIndex();
            // funcIndex=num时候，调用生成数字的方法；=str时，生成大写字母；=spec时，生成特殊字符。
            if (funcIndex.equals("num") && containNum) {
                strs[i] = String.valueOf(getRandomNum());
                containNumFlag = true;
            } else if (funcIndex.equals("str") && containStr) {
                strs[i] = getRandomStr();
                containStrFlag = true;
            } else if (funcIndex.equals("spec") && containSpec) {
                strs[i] = getSpecStr();
                containSpecFlag = true;
            } else {
                // 当不满足生成的条件时，生成小写字母
                strs[i] = getRandomStr().toLowerCase();
            }
        }
        StringBuilder str = new StringBuilder();
        for (String s :strs) {
            str.append(s);
        }
        // 判断是否包含了特殊字符
        if(!containSpecFlag){
            str.append(getSpecStr());
        }
        if(!containNumFlag){
            str.append(getRandomNum());
        }
        if(!containStrFlag){
            str.append(getRandomStr());
        }
        return str.toString();
    }

    private static int getRandomNum() {
        return (int) (Math.random()*10);
    }
    private static String getRandomStr() {
        String [] strs= {
                "Q","W","E","R","T",
                "Y","U","I","O","P",
                "A","S","D","F","G","H","J","K",
                "L","Z","X","C","V","B","N","M"};
        int strIndex = (int) (Math.random()*26);
        int strUpper =  ( (int)(Math.random()*100) )%2;
        if (strUpper!=1) {
            return strs[strIndex].toLowerCase();
        }
        return strs[strIndex];
    }

    private static String getSpecStr() {
        String [] strs= {"!","@","#","$","%","&","^"};
        int strIndex = (int) (Math.random()*7);
        return strs[strIndex];
    }

    private static String getFormIndex() {
        int formIndex =(int)(Math.random()*16);
        if ((formIndex%3)==0) {
            return "num";
        }else if ((formIndex%5)==1) {
            return "spec";
        }else {
            return "str";
        }
    }

    public static void main(String[] args) {
        while(true){
            String password = StrGeneralUtils.getPassword(10, true, true, true);
            PasswordCheckUtils.CheckResult check = PasswordCheckUtils.check(password);
            System.out.println(password);
            if(check.getPass()){
                break;
            }
        }
    }
}
