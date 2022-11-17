package itca.uz.ura_cashback_2;

public class Test {
    public static void main(String[] args) {
        String phone = "^\\+998\\d{9}", phone2 = "+998904654727",
                email = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$",email2 = "Shahzodbek007@gmail.com" ;
        boolean flag ;
        flag = email.matches(email2);
        if(flag){
            System.out.println("Successfully");
        }else {
            System.out.println("Error");
        }

    }
}
