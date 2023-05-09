package com.example.gptproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    Spinner spinner1, spinner2, spinner3;
    TextView textView;
    ImageView leftImageView, rightImageView, refresh;
    Button submit, redo;
    final ArrayList<String> players = new ArrayList<String>();
    final ArrayList<String> teams = new ArrayList<String>();
    final ArrayList<String> questions = new ArrayList<String>();
    ArrayList<String> spinner2List, spinner3List = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter1, arrayAdapter2, arrayAdapter3;
    static String question;

    Context context = this;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        players.addAll(Arrays.asList("Stefon Diggs", "Justin Jefferson", "Patrick Mahomes", "Derrick Henry", "DK Metcalf", "BJ Robinson", "Kyler Murray"));
        teams.addAll(Arrays.asList("Bills", "Cardinals", "Chiefs", "Falcons", "Seahawks", "Titans", "Vikings"));
        questions.addAll(Arrays.asList("Who will win more games?", "Who will score more fantasy football points?", "Who will score more touchdowns?", "Who is more likely to win MVP?"));


        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);

        textView = findViewById(R.id.textView1);

        submit = findViewById(R.id.submit);

        leftImageView = findViewById(R.id.imageView1);
        rightImageView = findViewById(R.id.imageView2);
        refresh = findViewById(R.id.refresh);
        leftImageView.setBackgroundResource(R.drawable.vikings);
        rightImageView.setBackgroundResource(R.drawable.seahawks);
        arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,questions);
        spinner1.setAdapter(arrayAdapter1);
        Random rand = new Random();
        spinner1.setSelection(rand.nextInt(questions.size()));
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText((CharSequence) parent.getSelectedItem());

                if(position==0) {
                    spinner2List = (ArrayList<String>) teams.clone();
                }

                if(position==1) {
                    spinner2List = (ArrayList<String>) players.clone();
                }

                if(position==2) {
                    spinner2List = (ArrayList<String>) players.clone();
                }

                if(position==3) {
                    spinner2List = (ArrayList<String>) players.clone();
                }

                arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,spinner2List);
                spinner2.setAdapter(arrayAdapter2);
                int randomSel = rand.nextInt(spinner2List.size());
                spinner2.setSelection(randomSel);
                spinner3List = (ArrayList<String>) spinner2List.clone();
                spinner3List.remove(randomSel);
                arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,spinner3List);
                spinner3.setAdapter(arrayAdapter3);
                spinner3.setSelection(rand.nextInt(spinner3List.size()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getSelectedItem();
                int drawableID = getResources().getIdentifier(getDrawable(selectedItem), "drawable", getPackageName());
                leftImageView.setBackgroundResource(drawableID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getSelectedItem();
                int drawableID = getResources().getIdentifier(getDrawable(selectedItem), "drawable", getPackageName());
                rightImageView.setBackgroundResource(drawableID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random = rand.nextInt(questions.size());
                spinner1.setSelection(random);
                if (spinner1.getSelectedItemPosition() == random) {
                    spinner2.setSelection(rand.nextInt(spinner2List.size()));
                    spinner3.setSelection(rand.nextInt(spinner3List.size()));
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = (String) spinner1.getSelectedItem() + " " + spinner2.getSelectedItem() + " or " + spinner3.getSelectedItem();
                try {
                    callAPI(question);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }


    void addResponse(String response) {
        runOnUiThread(new Runnable() {
            public void run() {
                Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
                myIntent.putExtra("response", response);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }



    void callAPI(String question) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "text-ada-001");
        jsonBody.put("prompt", question);
        jsonBody.put("max_tokens", 1000);
        jsonBody.put("temperature", 0);
        jsonBody.put("top_p", 1);
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder().url("https://api.openai.com/v1/completions").header("Authorization", "Bearer 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").post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response");
                }
            }
        });
    }

    public String getDrawable(String selectedItem) {
        if (selectedItem == "Bills" || selectedItem == "Stefon Diggs") {
            return "bills";
        }
        if (selectedItem == "Cardinals" || selectedItem == "Kyler Murray") {
            return "cardinals";
        }
        if (selectedItem == "Chiefs" || selectedItem == "Patrick Mahomes") {
            return "chiefs";
        }
        if (selectedItem == "Falcons" || selectedItem == "BJ Robinson") {
            return "falcons";
        }
        if (selectedItem == "Seahawks" || selectedItem == "DK Metcalf") {
            return "seahawks";
        }
        if (selectedItem == "Titans" || selectedItem == "Derrick Henry") {
            return "titans";
        }
        if (selectedItem == "Vikings" || selectedItem == "Justin Jefferson") {
            return "vikings";
        }
        return "";
    }

}