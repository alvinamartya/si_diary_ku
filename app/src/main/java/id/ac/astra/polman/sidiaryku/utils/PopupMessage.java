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

    public static void showWithAction(Context context, String title, String message,
                                              DialogInterface.OnClickListener positiveListener) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener)
                .setNegativeButton(null, null)
                .show();
    }

    public static void showWithAction(Context context, String title, String message, int iconId,
                                              DialogInterface.OnClickListener positiveListener) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener)
                .setIcon(iconId)
                .setNegativeButton(null, null)
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
                .setPositiveButton("Yes", positiveListener)
                .setNegativeButton("No", negativeListener)
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
                .setPositiveButton("Yes", positiveListener)
                .setNegativeButton("No", negativeListener)
                .show();
    }
}
