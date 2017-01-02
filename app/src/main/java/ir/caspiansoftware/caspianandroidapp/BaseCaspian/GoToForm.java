package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.app.Activity;
import android.content.Intent;

import ir.caspiansoftware.caspianandroidapp.FirstActivity;
import ir.caspiansoftware.caspianandroidapp.FirstFragment;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.InitialSettingActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.InitialSettingFragment;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.LoginActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.MainActivity;

/**
 * Created by Canada on 2/9/2016.
 */
public class GoToForm {

    public static void goToLoginPage(Activity pCurrentActivity) {
        Intent i = new Intent(pCurrentActivity.getApplicationContext(), LoginActivity.class);

        // clear history of activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // start login activity
        pCurrentActivity.getApplicationContext().startActivity(i);

        pCurrentActivity.finish();
    }

    public static void goToFirstPage(Activity currentActivity) {
        Intent i = new Intent(currentActivity.getApplicationContext(), FirstActivity.class);

        //i.putExtra(InitialSettingFragment.EXTRA_USERNAME, userName);

        // clear history of activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        currentActivity.getApplicationContext().startActivity(i);

        currentActivity.finish();
    }

    public static void goToInitialSettingPage(Activity currentActivity) {
        Intent i = new Intent(currentActivity.getApplicationContext(), InitialSettingActivity.class);

        //i.putExtra(InitialSettingFragment.EXTRA_USERNAME, userName);

        // clear history of activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        currentActivity.getApplicationContext().startActivity(i);

        currentActivity.finish();
    }

    public static void goToMainPage(Activity pCurrentActivity) {
        Intent i = new Intent(pCurrentActivity.getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pCurrentActivity.getApplicationContext().startActivity(i);

        pCurrentActivity.finish();
    }

    public static void closeApp(Activity pCurrentActivity) {
        Intent i = new Intent(pCurrentActivity.getApplicationContext(), FirstActivity.class);

        // clear history of activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // set exit extra
        i.putExtra(FirstFragment.EXTRA_EXIT, true);

        // start login activity
        pCurrentActivity.getApplicationContext().startActivity(i);

        pCurrentActivity.finish();
    }

}
