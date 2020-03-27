package com.milad.githubmvvmtest.view;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.milad.githubmvvmtest.R;
import com.milad.githubmvvmtest.databinding.TempBinding;
import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.view.Utils.jsonUtils;
import com.milad.githubmvvmtest.viewModel.ProjectViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment {
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PROJECT_ID = "project_id";
    //private FragmentProjectDetailsBinding binding;
    private TempBinding binding;
    PieChart pieChart;
    Map<String, String> map;
    Object[] sortedArray;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.temp, container, false);
        pieChart = binding.chart;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProjectViewModel.Factory factory = new ProjectViewModel.Factory(
                getActivity().getApplication(),
                getArguments().getString(KEY_USER_ID),
                getArguments().getString(KEY_PROJECT_ID));

        final ProjectViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(ProjectViewModel.class);

        binding.setProjectViewModel(viewModel);
        binding.setIsLoading(true);

        observeProjectViewModel(viewModel);
        observeLanguageViewModel(viewModel);
    }

    private void observeProjectViewModel(final ProjectViewModel viewModel) {
        // Observe project data
        viewModel.getObservableProject().observe(this, new Observer<Project>() {
            @Override
            public void onChanged(@Nullable Project project) {
                if (project != null) {
                    binding.setIsLoading(false);
                    viewModel.setProject(project);
                }
            }
        });
    }

    private void observeLanguageViewModel(final ProjectViewModel viewModel) {
        // Observe project data
        viewModel.getObservableLanguage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String stringLanguage) {
                if (!stringLanguage.equals("{}")) {
                    binding.chart.setVisibility(View.VISIBLE);
                    binding.setIsLoading(false);
                    map = parseJson(stringLanguage);

                    sortedArray = sortMap(map); //SortMap

//                    ArrayList arrayList = convertToArray(new MapUtil().sortByValue(map));
                    ArrayList arrayList = convertToArray(sortedArray);
                    setPieChart(arrayList);
                } else {
                    binding.setIsLoading(false);
                }

            }
        });
    }

    private Object[] sortMap(Map<String, String> map) {
        Map<String, Integer> map2 = new HashMap<>();
        for (Map.Entry<String, String> item : map.entrySet()) {
            map2.put(item.getKey(), Integer.parseInt(item.getValue()));
        }

        Object[] a = map2.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });

        return a;
    }

    public static ProjectFragment forProject(String userId, String projectID) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();

        args.putString(KEY_USER_ID, userId);
        args.putString(KEY_PROJECT_ID, projectID);
        fragment.setArguments(args);

        return fragment;
    }

    //region PieChart
    private ArrayList convertToArray(Object[] map) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        int sum = 0;

        for (Object e : map) {
            sum +=(((Map.Entry<String, Integer>) e).getValue());
        }

        for (Object e : map) {
            Float value = Float.parseFloat(String.valueOf(((Map.Entry<String, Integer>) e).getValue()));

            entries.add(new PieEntry((value * 100) / sum, ((Map.Entry<String, Integer>) e).getKey()));
        }

        return entries;
    }

    private void setPieChart(ArrayList arrayList) {

        PieDataSet pieDataSet = new PieDataSet(arrayList, getArguments().getString(KEY_PROJECT_ID));
        pieDataSet.setColors(setPieChartColor());

        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setCenterText(getArguments().getString(KEY_PROJECT_ID));

        pieChart.setEntryLabelColor(Color.BLACK);
//        pieChart.getLegend().setEnabled(false);

        pieChart.setData(pieData);
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    private Map<String, String> parseJson(String stringLanguage) {
//        ArrayList<PieEntry> entries = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(stringLanguage);
            JSONArray keys = jsonObject.names();

            assert keys != null;
            for (int i = 0; i < keys.length(); ++i) {

                String key = keys.getString(i); // Here's your key
                String value = jsonObject.getString(key); // Here's your value
                map.put(key, value);
//                entries.add(new PieEntry(Float.parseFloat(value), key));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private int[] setPieChartColor() {
        int[] color = new int[sortedArray.length];
        jsonUtils jsonUtils = new jsonUtils(getContext());
        int i = 0;

        for (Object e : sortedArray) {
            color[i] = Color.parseColor(jsonUtils.getColor(((Map.Entry<String, Integer>) e).getKey()));
            i++;
        }

        return color;
    }
    //endregion
}
