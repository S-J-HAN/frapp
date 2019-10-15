package com.itproject.frapp.ComputerVision;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FacialRecognition {

    // Interface with Azure APIs for facial recognition capabilities

    private static final String KEY = "11b7d055f5d742e4981bc64e4b3a99bf";
    private static final String URIBASE = "https://frapp-faces.cognitiveservices.azure.com/face/v1.0/";

    public static void addPersonToPersonGroup(Context context, String firebaseID) {

        // POST: create a new person and add it too our Azure persongroup instance
        JSONObject postParams = new JSONObject();
        RequestQueue r = Volley.newRequestQueue(context);

        // Send Post request
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URIBASE + "persongroups/frappfaces/persons", postParams, new Response.Listener<JSONObject>() {

            // Post was successful
            public void onResponse(JSONObject response) {
                System.out.println("ya boi successfully added a person");
                try {
                    // Retrieve their personIDs
                    String personID = response.get("personId").toString();

                    // Add their personID to firebase
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child("users").child(firebaseID).child("azurePersonID").setValue(personID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Failure Callback
                System.out.println("sigh :(");
                System.out.println("AZURE ERROR: " + error.toString());
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

            // Replace default body with url to face image
            @Override
            public byte[] getBody() {
                return ("{\"name\":\""+firebaseID+"\"}").getBytes();
            }
        };
        r.add(req);
    }

    public static void addPersonReferenceFace(Context context, String personId, String imageLink) {
        // POST: add a new face reference image for a person in our personGroup
        JSONObject postParams = new JSONObject();
        RequestQueue r = Volley.newRequestQueue(context);

        // Send Post request
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URIBASE + "persongroups/frappfaces/persons/" + personId + "/persistedFaces", postParams, new Response.Listener<JSONObject>() {

            // Post was successful
            public void onResponse(JSONObject response) {
                try {
                    // Get the new faceID, although we probably don't need to do anything with it
                    String pID = response.get("persistedFaceId").toString();

                    // Retrain our face models
                    trainFaces(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Failure Callback
                System.out.println("AZURE ERROR: " + error.toString());
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

            // Replace default body with url to face image
            @Override
            public byte[] getBody() {
                return ("{\"url\":\"" + imageLink + "\"}").getBytes();
            }
        };
        r.add(req);
    }

    public static void detectFaces(Context context, String imageLink) {
        // Detect faces in a photo and updates the firebase instance for that photo to include the
        // userIDs of detected individuals

        JSONObject postParams = new JSONObject();
        RequestQueue r = Volley.newRequestQueue(context);

        JsonArrayRequest req1 = new JsonArrayRequest(Request.Method.POST, URIBASE + "detect", new JSONArray().put(postParams), new Response.Listener<JSONArray>() {

            // Post was successful
            public void onResponse(JSONArray response) {

                try {
                    JSONObject face = response.getJSONObject(0);
                    System.out.println("WOOOOO " + face.toString());
                    System.out.println(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Failure Callback
                System.out.println("sigh :(");
                System.out.println("AZURE ERROR: " + error.toString());
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

            // Replace default body with url to face image
            @Override
            public byte[] getBody() {
                return ("{\"url\":\"" + imageLink + "\"}").getBytes();
            }
        };
        r.add(req1);
    }

    public static void tagImageWithFaces(Context context, String imageLink, String artifactID) {
        // Full facial recognition
        JSONObject postParams = new JSONObject();
        RequestQueue r = Volley.newRequestQueue(context);

        // Detect faces in the photo first
        JsonArrayRequest req1 = new JsonArrayRequest(Request.Method.POST, URIBASE + "detect", new JSONArray().put(postParams), new Response.Listener<JSONArray>() {

            // Post was successful
            public void onResponse(JSONArray response) {
                System.out.println("ya boi successfully detected a face");
                try {
                    // Get a list of all detected faces in the image in string form for the next query
                    String detectedFaces = "[";
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject face = response.getJSONObject(i);
                        if (i != 0) { detectedFaces += ","; }
                        detectedFaces += ("\"" + face.getString("faceId") + "\"");
                    }
                    detectedFaces += "]";
                    System.out.println(detectedFaces);
                    // Now search for faces we already know of
                    final String finalDetectedFaces = detectedFaces;
                    JsonArrayRequest req2 = new JsonArrayRequest(Request.Method.POST, URIBASE + "identify", new JSONArray().put(postParams), new Response.Listener<JSONArray>() {

                        // Post was successful
                        public void onResponse(JSONArray response1) {

                            // Get people inside the photo
                            ArrayList<String> detectedPeople = new ArrayList<String>();
                            for (int i = 0; i < response1.length(); i++) {
                                try {
                                    JSONArray candidates = response1.getJSONObject(i).getJSONArray("candidates");
                                    if (candidates.length() > 0) {
                                        JSONObject topCandidate = candidates.getJSONObject(0);
                                        detectedPeople.add(topCandidate.getString("personId"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Get a list of all users and match with detected personIDs
                            System.out.println("detected: " + detectedPeople.toString());
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                            dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    // A bunch of search-relevant terms that we might as well include
                                    String detectedUsers = "people,person,family";

                                    // Check every user to see if they are in the photo
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Map<String, String>  value  = (Map<String, String>) child.getValue();
                                        JSONObject jsonObject = new JSONObject(value);
                                        try {
                                            String fbUserID = child.getKey();
                                            String pid = jsonObject.get("azurePersonID").toString();
                                            if (detectedPeople.contains(pid)) {
                                                detectedUsers += ",";
                                                detectedUsers += jsonObject.get("name").toString().toLowerCase();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    dbRef.child("artifacts").child(artifactID).child("people").setValue(detectedUsers);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    System.out.println("hello REKT");
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            System.out.println("sigh 2 :(");
                            System.out.println("AZURE ERROR: " + error.toString());
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

                        // Replace default body with url to face image
                        @Override
                        public byte[] getBody() {
                            //return imageWithFaces.getBytes();
                            return ("{\"personGroupId\":\"frappfaces\", \"faceIds\":" + finalDetectedFaces +"}").getBytes();
                        }
                    };
                    r.add(req2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Failure Callback
                System.out.println("sigh 1 :(");
                System.out.println("AZURE ERROR: " + error.toString());
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

            // Replace default body with url to face image
            @Override
            public byte[] getBody() {
                return ("{\"url\":\"" + imageLink + "\"}").getBytes();
            }
        };
        r.add(req1);
    }

    public static void trainFaces(Context context) {
        // Trains the azure face instance so as to enable recognition

        // Full facial recognition
        RequestQueue r = Volley.newRequestQueue(context);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URIBASE + "persongroups/frappfaces/train", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("YAHOOOO");
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERORORORORORRORO");
                System.out.println(error);

            }
        }) {
            // Replace headers and body with API key and details
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Ocp-Apim-Subscription-Key", KEY);
                return headers;
            }
        };
        r.add(req);
    }


}
