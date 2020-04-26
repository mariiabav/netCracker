package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.problemsolver.ProblemFeed.model.SearchCriteria;
import com.example.problemsolver.Registration.RegistrationActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private List<SearchCriteria> searchCriteriaList = new ArrayList<>();
    private CheckBox checkBoxCreated;
    private CheckBox checkBoxInProcess;
    private CheckBox checkBoxSolved;
    private ListView areaList;
    private EditText startDate, endDate;
    private Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_feed_settings);

        final CrystalRangeSeekbar seekBar = findViewById(R.id.rangeSeekbar);
        final TextView minValueView = findViewById(R.id.MinValue1);
        final TextView maxValueView = findViewById(R.id.MaxValue1);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        seekBar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minValueView.setText("от " + minValue);
            maxValueView.setText("до " + maxValue);
        });

        checkBoxCreated = findViewById(R.id.checkBox1);
        checkBoxInProcess = findViewById(R.id.checkBox2);
        checkBoxSolved = findViewById(R.id.checkBox3);

        if (checkBoxCreated.isChecked()) {
            searchCriteriaList.add(new SearchCriteria("status", "=", "created"));
        }

        if (checkBoxInProcess.isChecked()) {
            searchCriteriaList.add(new SearchCriteria("status", "=", "in_process"));
        }

        if (checkBoxSolved.isChecked()) {
            searchCriteriaList.add(new SearchCriteria("status", "=", "solved"));
        }

        areaList = findViewById(R.id.area_list);

        // устанавливаем режим выбора пунктов списка
        areaList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Создаем адаптер, используя массив из файла ресурсов
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.areas,
                android.R.layout.simple_list_item_multiple_choice);
        areaList.setAdapter(adapter);

        startDate.setOnClickListener(this::setDate);

        startDate.setOnClickListener(this::setDate);

    }

    // диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(ImageActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    // установка начальных даты и времени
    private void setInitialDateTime() {

        startDate.setText(DateUtils.formatDateTime(ImageActivity.this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
}




