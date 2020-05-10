package com.example.problemsolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.problemsolver.problemFeed.model.SearchCriteria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private List<SearchCriteria> searchCriteriaList = new ArrayList<>();
    private CheckBox checkBoxCreated;
    private CheckBox checkBoxInProcess;
    private CheckBox checkBoxSolved;
    private ListView areaList;
    private EditText startDate, endDate;
    private Calendar startDateCal = Calendar.getInstance();
    private Calendar endDateCal = Calendar.getInstance();
    private Long minRateValue, maxRateValue;
    private Button saveBtn;
    private String[] allAreas;

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

            minRateValue = (Long)minValue;
            maxRateValue = (Long)maxValue;
        });

        checkBoxCreated = findViewById(R.id.checkBox1);
        checkBoxInProcess = findViewById(R.id.checkBox2);
        checkBoxSolved = findViewById(R.id.checkBox3);

        areaList = findViewById(R.id.area_list);

        allAreas = getResources().getStringArray(R.array.areas);
        // устанавливаем режим выбора пунктов списка
        areaList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Создаем адаптер, используя массив из файла ресурсов
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.areas,
                android.R.layout.simple_list_item_multiple_choice);
        areaList.setAdapter(adapter);

        startDate.setOnClickListener(this::setDateStart);

        endDate.setOnClickListener(this::setDateEnd);

        saveBtn = findViewById(R.id.save_button);

        saveBtn.setOnClickListener(view -> {
            searchCriteriaList.add(new SearchCriteria("rate", ">", String.valueOf(minRateValue)));
            searchCriteriaList.add(new SearchCriteria("rate", "<", String.valueOf(maxRateValue)));

            if (checkBoxCreated.isChecked()) {
                searchCriteriaList.add(new SearchCriteria("status", "=", "created"));
            }

            if (checkBoxInProcess.isChecked()) {
                searchCriteriaList.add(new SearchCriteria("status", "=", "in_process"));
            }

            if (checkBoxSolved.isChecked()) {
                searchCriteriaList.add(new SearchCriteria("status", "=", "solved"));
            }

            searchCriteriaList.add(new SearchCriteria("creationDate", "between", startDateCal + "," + endDateCal));

            ArrayList<String> checkedAreas = new ArrayList<>();
            SparseBooleanArray sbArray = areaList.getCheckedItemPositions();
            for (int i = 0; i < sbArray.size(); i++) {
                int key = sbArray.keyAt(i);
                if (sbArray.get(key))
                    checkedAreas.add(allAreas[key]);
            }

            StringBuilder result = new StringBuilder("");

            for (String area : checkedAreas) {
                result.append(area).append(",");
            }
            if( result.length() > 0 ) {
                result.deleteCharAt(result.length() - 1);
            }

            searchCriteriaList.add(new SearchCriteria("areaName", "in", result.toString()));

        });

    }

    // диалоговое окно для выбора даты
    public void setDateStart(View v) {
        new DatePickerDialog(Main2Activity.this, start,
                startDateCal.get(Calendar.YEAR),
                startDateCal.get(Calendar.MONTH),
                startDateCal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setDateEnd(View v) {
        new DatePickerDialog(Main2Activity.this, end,
                endDateCal.get(Calendar.YEAR),
                endDateCal.get(Calendar.MONTH),
                endDateCal.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    // установка начальных даты и времени
    private void setInitialDateTimeStart() {

        startDate.setText(DateUtils.formatDateTime(Main2Activity.this,
                startDateCal.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
    }

    private void setInitialDateTimeEnd() {

        endDate.setText(DateUtils.formatDateTime(Main2Activity.this,
                endDateCal.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener start= (view, year, monthOfYear, dayOfMonth) -> {
        startDateCal.set(Calendar.YEAR, year);
        startDateCal.set(Calendar.MONTH, monthOfYear);
        startDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTimeStart();

    };

    DatePickerDialog.OnDateSetListener end= (view, year, monthOfYear, dayOfMonth) -> {
        endDateCal.set(Calendar.YEAR, year);
        endDateCal.set(Calendar.MONTH, monthOfYear);
        endDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTimeEnd();
    };
}
