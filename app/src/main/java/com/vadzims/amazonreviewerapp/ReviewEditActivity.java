package com.vadzims.amazonreviewerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.VolleyLog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ReviewEditActivity extends AppCompatActivity {
    String[] status = {"ordered", "delivered", "review sent", "review got", "paid", "sold"};
    Toolbar topAppBar;
    ShapeableImageView reviewImage;
    TextInputEditText orderDateEdit, productName, price, orderNumber, groupName, contactPerson, emailAddress, sold, gotMoney;
    TextInputLayout orderDate;
    MaterialButton cancel_btn;

    LinearProgressIndicator loadingIndicator;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_edit);

        topAppBar = findViewById(R.id.topAppBar);
        cancel_btn = findViewById(R.id.cancel_button);
        loadingIndicator = findViewById(R.id.progressIndicator);
        productName = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        orderNumber = findViewById(R.id.order_number);
        groupName = findViewById(R.id.group_name);
        contactPerson = findViewById(R.id.contact_person);
        emailAddress = findViewById(R.id.emailAddress);
        orderDate = findViewById(R.id.order_date);
        orderDateEdit = findViewById(R.id.order_date_edit);
        sold = findViewById(R.id.sold);
        gotMoney = findViewById(R.id.got_money);
        reviewImage = findViewById(R.id.reviewImage);

        // datePicker init
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
//                        .setTitleText("Select date")
                        .build();

        Intent intent = getIntent();
        if (intent != null) {
            String reviewId = intent.getStringExtra("REVIEW_ID");

            if (!reviewId.equals("")) {
                System.out.println("Show loading indicator is true");
                try {
                    fetchReview(reviewId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //TODO grab this text from getString()
                topAppBar.setTitle("Edit Review");
            }

            orderDate.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                }

            });

            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
//                    System.out.println(selection);
//                    System.out.println(datePicker.getHeaderText());
//                    System.out.println(datePicker.getSelection());
                    orderDateEdit.setText(datePicker.getHeaderText());
                }
            });

        }

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                if (getCurrentFocus() != null) {
                    Utils.getInstance(getApplicationContext()).hideKeyBoard(getCurrentFocus()); // need this here to fix go back behavior with an open keyboard
                }
            }
        });

        //go back arrow
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                if (getCurrentFocus() != null) {
                    Utils.getInstance(getApplicationContext()).hideKeyBoard(getCurrentFocus()); // need this here to fix go back behavior with an open keyboard
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void showReview(JSONObject response) {
        try {
            JSONObject object = response.getJSONObject("data");

            Picasso.get().load(object.getString("imageCover")).into(reviewImage);
            productName.setText(object.getString("name"));
            price.setText(object.getString("price"));
            orderNumber.setText(object.getString("orderNumber"));
            groupName.setText(object.getString("groupName"));
            contactPerson.setText(object.getString("contactPerson"));
            emailAddress.setText(object.getString("email"));
            orderDateEdit.setText(object.getString("orderDate"));
            sold.setText(object.getString("sold"));
            gotMoney.setText(object.getString("gotMoney"));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, status);

            // drop down list
            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setHint(object.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchReview(String reviewId) throws InterruptedException {
        loadingIndicator.setVisibility(View.VISIBLE);
        try {
            Utils.getInstance(this).searchSpecificReview(reviewId, new Utils.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    VolleyLog.e("Something went wrong");
                }

                @Override
                public void onResponse(JSONObject response) {
                    showReview(response);
                    loadingIndicator.setVisibility(View.INVISIBLE);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}