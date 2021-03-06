package com.ishabaev.weather.addcity;

import com.ishabaev.weather.EspressoIdlingResource;
import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.data.source.FileSource;
import com.ishabaev.weather.data.source.RepositoryDataSource;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class AddCityPresenter implements AddCityContract.Presenter {

    private AddCityContract.View mView;
    private RepositoryDataSource mRepository;
    private FileSource mFileManager;
    private CompositeSubscription mSubscriptions;
    private Scheduler mBackgroundScheduler;
    private Scheduler mMainScheduler;

    public AddCityPresenter(AddCityContract.View view, RepositoryDataSource repository, FileSource fileManager,
                            Scheduler background, Scheduler main) {
        mView = view;
        mRepository = repository;
        mFileManager = fileManager;
        mSubscriptions = new CompositeSubscription();
        mBackgroundScheduler = background;
        mMainScheduler = main;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void onItemClick(OrmCity city) {
        mRepository.saveCity(city);
    }

    @Override
    public void textChanged(String text) {
        EspressoIdlingResource.increment();
        mSubscriptions.clear();
        mView.setCityListVisible(false);
        mView.clearCities();
        mView.setProgressBarVisible(true);
        Subscription subscription = mFileManager.searchCity(text)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(
                        city -> {
                            mView.setImageViewVisible(false);
                            mView.setSearchStateVisible(false);
                            mView.setCityListVisible(true);
                            mView.addCityToList(city);
                        },
                        throwable -> {
                            EspressoIdlingResource.decrement();
                            mView.showCouldNotFindCity();
                            mView.setSearchStateVisible(true);
                            mView.setImageViewVisible(true);
                            mView.setProgressBarVisible(false);
                            mView.setCityListVisible(false);
                            throwable.printStackTrace();
                        },
                        () -> {
                            EspressoIdlingResource.decrement();
                            mView.setProgressBarVisible(false);
                        }
                );
        mSubscriptions.add(subscription);
    }
}
