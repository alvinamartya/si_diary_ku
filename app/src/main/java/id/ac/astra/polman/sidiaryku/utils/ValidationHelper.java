package id.ac.astra.polman.sidiaryku.utils;

import android.util.Patterns;

public class ValidationHelper {
    public static boolean matchEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean matchPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }
}
