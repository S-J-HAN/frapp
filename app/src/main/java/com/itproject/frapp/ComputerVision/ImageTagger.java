package com.itproject.frapp.ComputerVision;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ImageTagger {

    // Interface with  Azure APIs to generate and store semantic tags for images
    private static final String KEY = "41d1822e642940ec97bb69f6d2b3116b";
    private static final String URIBASE = "https://frapp-cv.cognitiveservices.azure.com/vision/v1.0/";

    public static String finalTags;

    public static void tagImage(Context context, String url, String artifactID) {
        // Generates semantic tags for an artifact with image located at 'url' and automatically
        // updates the artifact's firebase db instance

        JSONObject postParams = new JSONObject();
        RequestQueue r = Volley.newRequestQueue(context);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URIBASE + "tag", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                try {
                    // Get a list of all tags
                    JSONArray tags = response.getJSONArray("tags");

                    String allTags = "photo";
                    for (int i = 0; i < tags.length(); i++) {
                        allTags += ",";
                        allTags += ((JSONObject) tags.get(i)).get("name");
                    }

                    System.out.println("ALL TAGS " + allTags);

                    finalTags = allTags;

                    // Add tags to firebase instance
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child("artifacts").child(artifactID).child("semanticTags").setValue(allTags);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR for image tagging");
                System.out.println(error);

            }
        }) {
            // Replace headers and body with API key and details
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Ocp-Apim-Subscription-Key", KEY);
                return headers;
            }

            // Replace default body with url to image
            @Override
            public byte[] getBody() {
                return ("{\"url\":\"" + url + "\"}").getBytes();
            }
        };
        r.add(req);

    }

}
