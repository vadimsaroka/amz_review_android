package com.vadzims.amazonreviewerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.vadzims.amazonreviewerapp.login.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(Utils.getInstance(this).getUserToken() != null) {
////            setContentView(R.layout.activity_logged);
//            startActivity(new Intent(getApplicationContext(), LoggedActivity.class));
//        } else {
//            setContentView(R.layout.activity_main);
//
//            final TextInputEditText passwordTextInput = findViewById(R.id.password_text_input);
//            final TextInputEditText passwordEditText = findViewById(R.id.password_edit_text);
//            MaterialButton nextButton = findViewById(R.id.next_button);
//            MaterialButton cancelButton = findViewById(R.id.cancel_button);
//            MaterialButton registerButton = findViewById(R.id.register_button);
//
//            // Set an error if the password is less than 8 characters.
//            nextButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!isPasswordValid(passwordEditText.getText())) {
//                        passwordTextInput.setError(getString(R.string.error_password));
//                    } else {
//                        passwordTextInput.setError(null); // Clear the error
//                        try {
//                            showLoadingIndicator(true);
//                            Utils.getInstance(MainActivity.this).saveUserToken(passwordTextInput.getText().toString(), passwordEditText.getText().toString());
//                            Utils.getInstance(MainActivity.this).hideKeyBoard(view);
//                            startActivity(new Intent(getApplicationContext(), LoggedActivity.class));
//                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                            showLoadingIndicator(false);
//                        } catch (IOException | JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//
//            cancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    passwordTextInput.setText("");
//                    passwordEditText.setText("");
//                    Utils.getInstance(MainActivity.this).hideKeyBoard(view);
//                }
//            });
//
//            registerButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//                }
//            });
//
//            // Clear the error once more than 8 characters are typed.
//            passwordEditText.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                    if (isPasswordValid(passwordEditText.getText())) {
//                        passwordTextInput.setError(null); //Clear the error
//                    }
//                    return false;
//                }
//            });
//        }

        try {
            Utils.getInstance(this).VerifyUser(new Utils.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    setContentView(R.layout.activity_main);

                    final TextInputEditText passwordTextInput = findViewById(R.id.password_text_input);
                    final TextInputEditText passwordEditText = findViewById(R.id.password_edit_text);
                    MaterialButton nextButton = findViewById(R.id.next_button);
                    MaterialButton cancelButton = findViewById(R.id.cancel_button);
                    MaterialButton registerButton = findViewById(R.id.register_button);

                    // Set an error if the password is less than 8 characters.
                    nextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!isPasswordValid(passwordEditText.getText())) {
                                passwordTextInput.setError(getString(R.string.error_password));
                            } else {
                                passwordTextInput.setError(null); // Clear the error
                                try {
                                    showLoadingIndicator(true);
                                    Utils.getInstance(MainActivity.this).saveUserToken(passwordTextInput.getText().toString(), passwordEditText.getText().toString());
                                    Utils.getInstance(MainActivity.this).hideKeyBoard(view);
                                    startActivity(new Intent(getApplicationContext(), LoggedActivity.class));
                                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            passwordTextInput.setText("");
                            passwordEditText.setText("");
                            Utils.getInstance(MainActivity.this).hideKeyBoard(view);
                        }
                    });

                    registerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                        }
                    });

                    // Clear the error once more than 8 characters are typed.
                    passwordEditText.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            if (isPasswordValid(passwordEditText.getText())) {
                                passwordTextInput.setError(null); //Clear the error
                            }
                            return false;
                        }
                    });
                    showLoadingIndicator(false);
                }

                @Override
                public void onResponse(JSONObject response) throws JSONException {
                    if(response.getString("status").equals("success")) {
                        startActivity(new Intent(getApplicationContext(), LoggedActivity.class));
                    }
                }
            });
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

//TODO add email validation

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    private void showLoadingIndicator(boolean isShowLoadingIndicator) {
        // final CircularProgressIndicator indicator = view.findViewById(R.id.progressIndicator);
        LinearProgressIndicator loadingIndicator = findViewById(R.id.progressIndicator);
        if (isShowLoadingIndicator) {
            loadingIndicator.setVisibility(View.VISIBLE);
        } else {
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}