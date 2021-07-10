package id.ac.astra.polman.sidiaryku.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PopupMessage {
    // show message
    public static void show(Context context, String title, String message) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    // show message with icon
    public static void showWithIcon(Context context, String title, String message, int iconId) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(iconId)
                .show();
    }

    // show confirm message
    public static void confirm(Context context, String title, String message,
                               DialogInterface.OnClickListener positiveListener,
                               DialogInterface.OnClickListener negativeListener) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ya", positiveListener)
                .setNegativeButton("Tidak", negativeListener)
                .show();
    }

    // show confirm message with icon
    public static void confirmWithIcon(Context context, String title, String message, int iconId,
                                       DialogInterface.OnClickListener positiveListener,
                                       DialogInterface.OnClickListener negativeListener) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(iconId)
                .setPositiveButton("Ya", positiveListener)
                .setNegativeButton("Tidak", negativeListener)
                .show();
    }
}
