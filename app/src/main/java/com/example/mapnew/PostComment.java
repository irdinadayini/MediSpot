package com.example.mapnew;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PostComment {

    public void sendPost(String name, String email, String comments, String location, Context ctx, MainActivity main) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://10.0.2.2/comments/api.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> main.showToast("Response is: " + response),
                error -> main.showToast("Error: " + error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("comments", comments);  // Sending dynamic comment
                params.put("location", location);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
