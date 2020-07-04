package com.mahesh.feedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    EditText custName;
    EditText custEmail;
    EditText custPhone;
    EditText comments;
    RatingBar overallRatingBar;

    String customerName;
    String customerEmail;
    String customerPhone;
    String overallRating;
    String overallComments;

    Button submitButton;

    private static String ADD_REVIEW = "API call to PHP file";
    private static String POST_FACEBOOK = "API call to PHP file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overallRatingBar = findViewById(R.id.overall_rating);
        final TextView overallTextView = findViewById(R.id.overall_rating_heading);
        final String overallText = overallTextView.getText().toString();

        overallRatingBar.setNumStars(5);
        overallRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int ratedValue = (int) ratingBar.getRating();
                String newOverallText = overallText + ratedValue;
                overallTextView.setText(newOverallText);
            }
        });

        custName = findViewById(R.id.customer_name);
        custEmail = findViewById(R.id.customer_email);
        custPhone = findViewById(R.id.customer_mobile);
        comments = findViewById(R.id.comments);

        submitButton = findViewById(R.id.submit_review);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName = custName.getText().toString();
                customerEmail = custEmail.getText().toString();
                customerPhone = custPhone.getText().toString();
                overallRating = ((int)overallRatingBar.getRating()) + "";
                overallComments = comments.getText().toString();

                new Review().execute();
            }
        });

    }

    class Review extends AsyncTask<String, String, String> {

        int success;
        private static final String KEY_SUCCESS = "success";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Creating the review");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put("name", customerName);
            httpParams.put("email", customerEmail);
            httpParams.put("phone", customerPhone);
            httpParams.put("rating", overallRating);
            httpParams.put("comments", overallComments);

            JSONObject jsonObject= httpJsonParser
                    .makeHttpRequest(ADD_REVIEW, "GET", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(overallComments != null && !overallComments.isEmpty()) {
                httpParams.clear();
                httpParams.put("comments", overallComments);

                httpJsonParser.makeHttpRequest(POST_FACEBOOK, "GET", httpParams);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(MainActivity.this,
                                "Review Updated", Toast.LENGTH_LONG).show();
                        custName.setText("");
                        custEmail.setText("");
                        custPhone.setText("");
                        comments.setText("");
                        overallRatingBar.setRating(0);
                        custName.requestFocus();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "ERROR",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
