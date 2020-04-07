package com.example.problemsolver.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.problemsolver.R;

import java.util.ArrayList;


public class AreasActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    ListView lvMain;
    String[] allAreas;
    ArrayList<String> checkedAreas = new ArrayList<>();
    String name, lastName, email, number, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);

        lvMain = (ListView) findViewById(R.id.lvMain);

        // устанавливаем режим выбора пунктов списка
        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Создаем адаптер, используя массив из файла ресурсов
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.areas,
                android.R.layout.simple_list_item_multiple_choice);
        lvMain.setAdapter(adapter);

        Button btnChecked = findViewById(R.id.btnChecked);

        name = getIntent().getStringExtra("name");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        number = getIntent().getStringExtra("number");
        date = getIntent().getStringExtra("date");

        // получаем массив из файла ресурсов
        allAreas = getResources().getStringArray(R.array.areas);

        btnChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key))
                        checkedAreas.add(allAreas[key]);
                }

                Intent intent = new Intent(AreasActivity.this, RegistrationActivity.class);
                if (checkedAreas.size() != 0){
                    intent.putExtra("checkedAreas", checkedAreas);
                }

                intent.putExtra("name", name);
                intent.putExtra("lastName", lastName);
                intent.putExtra("email", email);
                intent.putExtra("number", number);
                //intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

    private void showMessage(String string){
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}
