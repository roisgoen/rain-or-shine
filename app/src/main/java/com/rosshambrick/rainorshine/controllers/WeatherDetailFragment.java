package com.rosshambrick.rainorshine.controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rosshambrick.rainorshine.R;
import com.rosshambrick.rainorshine.core.model.entities.CityWeather;
import com.rosshambrick.rainorshine.core.model.services.WeatherRepo;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observer;
import rx.Subscription;
import rx.android.observables.AndroidObservable;

public class WeatherDetailFragment extends RainOrShineFragment implements Observer<CityWeather> {

    private static final String CITY_ID = "city_id";

    @Inject WeatherRepo mWeatherRepo;

    @InjectView(R.id.fragment_weather_detail_current_temperature) TextView mCurrentTemperature;
    @InjectView(R.id.fragment_weather_detail_high_temperature) TextView mHighTemperature;
    @InjectView(R.id.fragment_weather_detail_low_temperature) TextView mLowTemperature;

    public static Fragment newInstance(long id) {
        Fragment fragment = new WeatherDetailFragment();
        Bundle args = new Bundle();
        args.putLong(CITY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        long mCityId = getArguments().getLong(CITY_ID);

        mSubscriptions.add(AndroidObservable
                .bindFragment(this, mWeatherRepo.getCityById(mCityId))
                .subscribe(this));
    }

    @Override
    public void onNext(CityWeather cityWeather) {
        display(cityWeather);
    }

    @Override
    public void onCompleted() {
        //do nothing
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void display(CityWeather weatherData) {
        getActivity().setTitle(weatherData.getName());
        mCurrentTemperature.setText(weatherData.getFormattedCurrentTempInFahrenheit());
        mHighTemperature.setText(weatherData.getFormattedHighTempInFahrenheit());
        mLowTemperature.setText(weatherData.getFormattedLowTempInFahrenheit());
    }
}
