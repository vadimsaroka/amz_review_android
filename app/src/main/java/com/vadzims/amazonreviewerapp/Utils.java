package com.vadzims.amazonreviewerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private final RequestQueue requestQueue;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    @SuppressLint("StaticFieldLeak")
    private static Utils instance;
    private static final String TOKEN = "token";
    private final Context context;
    private static final String BASE_URL = "https://amzreviewserver.herokuapp.com";

    private Utils(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("alternate_db", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized Utils getInstance(Context context) {
        if (null == instance) {
            instance = new Utils(context);
        }
        return instance;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(JSONObject response) throws JSONException;
    }

    public void VerifyUser(VolleyResponseListener volleyResponseListener) throws IOException, JSONException {
        String URL = BASE_URL + "/api/v1/users/ferify/" + getUserToken();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        volleyResponseListener.onResponse(new JSONObject().put("status", response.getString("status")));
                    } else {
                        volleyResponseListener.onError(response.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(error.toString());
                volleyResponseListener.onError("Something went wrong");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + getUserToken());
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    public void saveUserToken(String email, String password) throws IOException, JSONException {

        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonobject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String URL = BASE_URL + "/api/v1/users/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonobject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        editor.putString("token", response.getString("token")).commit();
                        editor.putString("userName", response.getJSONObject("data").getJSONObject("user").getString("name")).commit();
                        editor.putString("userEmail", response.getJSONObject("data").getJSONObject("user").getString("email")).commit();
                        editor.putString("userImage", response.getJSONObject("data").getJSONObject("user").getString("photo")).commit();
                    } else {
                        System.out.println("RESPONSE: " + response.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_LONG).show();
                VolleyLog.e("Incorrect email or password");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public String getUserToken() {
        return sharedPreferences.getString("token", null);
    }

    public String getUsername() {
        return sharedPreferences.getString("userName", null);
    }

    ;

    public String getUserEmail() {
        return sharedPreferences.getString("userEmail", null);
    }

    ;

    public String getUserImage() {
        return sharedPreferences.getString("userImage", null);
    }

    ;

    public void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void searchReviewsByName(String name, VolleyResponseListener volleyResponseListener) throws IOException, JSONException {
        String URL = BASE_URL + "/api/v1/reviews/find/" + name;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (Integer.parseInt(response.getString("results")) > 0) {
                        volleyResponseListener.onResponse(response.getJSONObject("data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                VolleyLog.e("Something went wrong");
                volleyResponseListener.onError("Something went wrong");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + getUserToken());
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void searchAllReviews() throws IOException, JSONException {
        String URL = BASE_URL + "/api/v1/reviews/my/reviews/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (Integer.parseInt(response.getString("results")) > 0) {
                        editor.putString("allReviews", String.valueOf(response.getJSONObject("data"))).commit();
                    } else {
                        System.out.println("RESPONSE: " + response.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Invalid _id", Toast.LENGTH_LONG).show();
                VolleyLog.e("Invalid _id");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + getUserToken());
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public JSONArray getAllReviews() throws JSONException {
        String reviewsListString = sharedPreferences.getString("allReviews", "");
        JSONObject reviewsListJSONArray = new JSONObject(reviewsListString);

        return reviewsListJSONArray.getJSONArray("data");
    }

    public void searchOrderedReviews() throws IOException, JSONException {
        String URL = BASE_URL + "api/v1/reviews/status/ordered";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (Integer.parseInt(response.getString("results")) > 0) {
                        editor.putString("orderedReviews", String.valueOf(response.getJSONObject("data"))).commit();
                    } else {
                        editor.putString("orderedReviews", "").commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Invalid _id", Toast.LENGTH_LONG).show();
                VolleyLog.e("Invalid _id");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + getUserToken());
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public JSONArray getOrderedReviews() throws JSONException {
        String reviewsListString = sharedPreferences.getString("orderedReviews", "");
        JSONObject reviewsListJSONArray = new JSONObject(reviewsListString);

        return reviewsListJSONArray.getJSONArray("data");
    }

    public void searchPaidReviews() throws IOException, JSONException {
        String URL = BASE_URL + "/api/v1/reviews/status/paid";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (Integer.parseInt(response.getString("results")) > 0) {
                        editor.putString("paidReviews", String.valueOf(response.getJSONObject("data"))).commit();
                    } else {
                        editor.putString("paidReviews", "").commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Invalid _id", Toast.LENGTH_LONG).show();
                VolleyLog.e("Invalid _id");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + getUserToken());
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public JSONArray getPaidReviews() throws JSONException {
        String reviewsListString = sharedPreferences.getString("paidReviews", "");
        JSONObject reviewsListJSONArray = new JSONObject(reviewsListString);

        return reviewsListJSONArray.getJSONArray("data");
    }

    public void searchSoldReviews() throws IOException, JSONException {
        String URL = BASE_URL + "/api/v1/reviews/status/sold";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (Integer.parseInt(response.getString("results")) > 0) {
                        editor.putString("soldReviews", String.valueOf(response.getJSONObject("data"))).commit();
                    } else {
                        editor.putString("soldReviews", "").commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Invalid _id", Toast.LENGTH_LONG).show();
                VolleyLog.e("Invalid _id");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + getUserToken());
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public JSONArray getSoldReviews() throws JSONException {
        String reviewsListString = sharedPreferences.getString("soldReviews", "");
        JSONObject reviewsListJSONArray = new JSONObject(reviewsListString);

        return reviewsListJSONArray.getJSONArray("data");
    }


    public void searchSpecificReview(String reviewId, VolleyResponseListener volleyResponseListener) throws IOException, JSONException {
        String URL = BASE_URL + "/api/v1/reviews/my/" + reviewId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        volleyResponseListener.onResponse(response.getJSONObject("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                VolleyLog.e("Something went wrong");
                volleyResponseListener.onError("Something went wrong");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + getUserToken());
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}