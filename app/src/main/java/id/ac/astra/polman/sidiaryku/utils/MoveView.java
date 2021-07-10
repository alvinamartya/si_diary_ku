package id.ac.astra.polman.sidiaryku.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MoveView {
    public static void withFinish(Context context, Class destClass) {
        Intent intent = new Intent(context, destClass);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void withoutFinish(Context context, Class destClass) {
        Intent intent = new Intent(context, destClass);
        context.startActivity(intent);
    }
}
