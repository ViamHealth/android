package com.viamhealth.android.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.Validator;

/**
 * Created by naren on 28/11/13.
 */
public class ShareUser extends AsyncTask<Void, Void, Boolean>
{
    protected Context mContext;
    protected Application mApplication;
    protected String email;
    protected boolean mIsSelf;
    protected User mUserToShare;
    protected ProgressDialog mDialog;

    public ShareUser(Context context, Application app, User userToShare) {
        this.mContext = context;
        this.mApplication = app;
        this.mUserToShare = userToShare;
    }

    public void show(){
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.share_profile, null);

        final EditText shareTo = (EditText) dialogView.findViewById(R.id.shareTo);
        final CheckBox chkBox = (CheckBox) dialogView.findViewById(R.id.shareToSelf);
        chkBox.setText("Sharing with " + mUserToShare.getName());

        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && (shareTo.getText().toString()==null || shareTo.getText().toString().isEmpty())) {
                    if(mUserToShare.getEmail()!=null && !mUserToShare.getEmail().isEmpty()){
                        shareTo.setText(mUserToShare.getEmail());
                        shareTo.setEnabled(false);
                    }

                    //TODO when we support mobile based identification
                        /*else if(selectedUser.getProfile()!=null && selectedUser.getProfile().getMobileNumber()!=null
                                    && !selectedUser.getProfile().getMobileNumber().isEmpty())
                            shareTo.setText(selectedUser.getProfile().getMobileNumber());*/
                }else{
                    shareTo.setText("");
                    shareTo.setEnabled(true);
                }
            }
        });

        shareTo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String un = shareTo.getText().toString();
                if(un.matches("^[0-9]{1,10}$"))
                    shareTo.setInputType(InputType.TYPE_CLASS_PHONE);
                else if(Validator.isEmailValid(un))
                    shareTo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                else
                    shareTo.setError("email of abc@gmail.com or mobile number without country code like 1234512345");

                return false;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogGreenTheme);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setTitle("Share "+mUserToShare.getName()+" with ...");
        builder.setPositiveButton("share", new DialogInterface.OnClickListener() {
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
                email = shareTo.getText().toString();
                if(email==null || email.isEmpty()){
                    shareTo.setError("email is mandatory");
                    return;
                }

                if(!Validator.isEmailValid(email)){
                    shareTo.setError("email should be of type - abc@gmail.com");
                    return;
                }

                mIsSelf = chkBox.isChecked();

                dialog.dismiss();
                if(Checker.isInternetOn((Activity)mContext)){
                    execute();
                }
            }
        });
    }

    @Override
    protected void onPreExecute(){
        mDialog = new ProgressDialog(mContext, R.style.StyledProgressDialog);
        mDialog.setMessage("sharing the profile....");
        mDialog.show();
    }

    protected void onPostExecute(Boolean result) {
        if(result){
            mDialog.dismiss();
        }else{
            mDialog.dismiss();
            Toast.makeText(mContext, "Not able to share " + mUserToShare.getName() + " to " + email + "...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserEP userEP = new UserEP(mContext, mApplication);
        return userEP.shareUser(mUserToShare, email, mIsSelf);
    }
}