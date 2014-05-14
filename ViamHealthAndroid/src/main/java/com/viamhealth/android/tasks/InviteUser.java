package com.viamhealth.android.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.Validator;

/**
 * Created by naren on 28/11/13.
 */
public class InviteUser extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog dialog;

    final private Activity activity;
    final private Application application;
    private String email;

    public InviteUser(Activity activity, Application app) {
        this.activity = activity;
        this.application = app;
    }

    public void show() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_invite_user, null);
        final EditText etEmail = (EditText) view.findViewById(R.id.email);

//        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogGreenTheme);
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(activity);
        } else {
            builder = new AlertDialog.Builder(activity,R.style.AlertDialogGreenTheme);
        }
        builder.setView(view);
        builder.setTitle("Invite User");
        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        final Button inviteButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                if (email == null || email.isEmpty()) {
                    etEmail.setError("email is mandatory");
                    return;
                }

                if (!Validator.isEmailValid(email)) {
                    etEmail.setError("email should be of type - abc@gmail.com");
                    return;
                }

                dialog.dismiss();
                if (Checker.isInternetOn(activity)) {
                    execute();
                }
            }
        });
    }

    protected void onPreExecute() {
        dialog = new ProgressDialog(this.activity, R.style.StyledProgressDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("sending the invite...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        dialog.dismiss();
        String msg;
        if (aBoolean) {
            msg = "Invite sent successfully to " + email;
        } else {
            msg = "Sorry! Couldn't sent the invite now. Please try after some time.";
        }
        Toast.makeText(this.activity, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserEP userEndPoint = new UserEP(activity, application);
        return userEndPoint.InviteUser(email);
    }

}
