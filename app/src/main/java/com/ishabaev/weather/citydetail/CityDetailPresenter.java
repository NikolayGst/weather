package com.ishabaev.weather.citydetail;

import android.content.res.Resources;

import com.ishabaev.weather.R;
import com.ishabaev.weather.dao.OrmWeather;
import com.ishabaev.weather.data.Day;
import com.ishabaev.weather.data.source.Repository;
import com.ishabaev.weather.util.DataSort;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ishabaev on 18.06.16.
 */
public class CityDetailPresenter implements CityDetailContract.Presenter {

    private CityDetailContract.View mView;
    private Repository mRepository;
    private CompositeSubscription mSubscriptions;

    public CityDetailPresenter(CityDetailContract.View cityDetailView, Repository repository) {
        mView = cityDetailView;
        mRepository = repository;
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void openCity(int cityId) {
        mView.showProgressBar(true);
        mSubscriptions.clear();
        Subscription subscription = mRepository
                .getForecast(cityId, mView.isNetworkAvailable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<OrmWeather>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<OrmWeather> ormWeathers) {
                        makeView(ormWeathers);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void makeView(List<OrmWeather> forecast) {
        DataSort.sortWeatherHour(forecast);
        mView.showProgressBar(false);
        if (forecast.size() > 0) {
            OrmWeather current = forecast.get(0);
            setTemperature(current.getTemp());
            setHummidity(current.getHumidity());
            setWind(current.getWind_speed());
            setPressure(current.getPressure());
            setDate(current.getDt());
            mView.setImage(current.getIcon() + ".jpg");
            addDaysToViewPager(forecast);
        } else {

        }
    }

    private void setTemperature(Double temp) {
        String temperature = temp > 0 ?
                "+" + Integer.toString(temp.intValue()) :
                Integer.toString(temp.intValue());
        temperature += " °C";
        mView.setTemp(temperature);
    }

    private void setHummidity(double hummidity) {
        String value = mView.getResources()
                .getString(R.string.humidity) + ": " +
                Double.toString(hummidity) + "%";
        mView.setHummidity(value);
    }

    private void setWind(Double wind) {
        Resources res = mView.getResources();
        String value = wind == null ?
                res.getString(R.string.windless) :
                res.getString(R.string.wind) + ": " +
                        Double.toString(wind) + " " +
                        res.getString(R.string.km_h);
        mView.setWindSpeed(value);
    }

    private void setPressure(Double pressure) {
        String value = pressure == null ? "" :
                mView.getResources().getString(R.string.pressure) + ": " +
                        Double.toString(pressure);
        mView.setPressure(value);
    }

    private void setDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM, HH:mm");
        String value = format.format(date);
        mView.setDate(value);
    }

    private void addDaysToViewPager(List<OrmWeather> hours) {
        List<Day> days = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(hours.get(0).getDt());
        Day day = new Day();
        day.setHours(new ArrayList<OrmWeather>());
        days.add(day);
        for (OrmWeather hourWeayher : hours) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(hourWeayher.getDt());
            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR) == 0) {
                day.getHours().add(hourWeayher);
            } else {
                day = new Day();
                day.setHours(new ArrayList<OrmWeather>());
                day.getHours().add(hourWeayher);
                days.add(day);
            }
            c1 = c2;
        }
        mView.addDays(days);
    }
}
