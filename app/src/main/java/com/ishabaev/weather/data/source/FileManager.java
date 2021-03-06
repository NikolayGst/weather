package com.ishabaev.weather.data.source;

import android.content.res.AssetManager;

import com.ishabaev.weather.dao.OrmCity;
import com.ishabaev.weather.util.Translate;

import java.io.InputStream;
import java.util.Scanner;

import rx.Observable;
import rx.Subscription;

public class FileManager implements FileSource {

    private static FileManager INSTANCE;
    private AssetManager mAssetManager;
    private final static String FILE_NAME = "city_list.txt";
    //private final static int LINE_SIZE = 74062;

    private FileManager(AssetManager assetManager) {
        mAssetManager = assetManager;
    }

    public static FileManager getInstance(AssetManager assetManager) {
        if (INSTANCE == null) {
            INSTANCE = new FileManager(assetManager);
        }
        return INSTANCE;
    }

    @Override
    public Observable<OrmCity> searchCity(String cityName) {
        return Observable.create(
                sub -> {
                    sub.add(new Subscription() {
                        boolean unsubscribed = false;
                        @Override
                        public void unsubscribe() {
                            unsubscribed = true;
                        }

                        @Override
                        public boolean isUnsubscribed() {
                            return unsubscribed;
                        }
                    });
                    try {
                        InputStream is = mAssetManager.open(FILE_NAME);
                        Scanner scanner = new Scanner(is);
                        int count = 0;
                        int row = 0;
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (row == 0) {
                                row++;
                                continue;
                            }
                            if (line.toLowerCase().contains(cityName.toLowerCase()) ||
                                    line.toLowerCase().contains(Translate.ru2en(cityName.toLowerCase()))) {
                                String[] cityParams = line.split("\t");
                                final OrmCity city = new OrmCity();
                                city.set_id(Long.parseLong(cityParams[0]));
                                city.setCity_name(cityParams[1]);
                                city.setLat(Double.parseDouble(cityParams[2]));
                                city.setLon(Double.parseDouble(cityParams[3]));
                                city.setCountry(cityParams[4]);
                                count++;
                                sub.onNext(city);
                            }
                            if (count > 10) {
                                sub.onCompleted();
                                break;
                            }
                            row++;
                            if(sub.isUnsubscribed()){
                                sub.onCompleted();
                                break;
                            }
                        }
                        if(count > 0) {
                            sub.onCompleted();
                        }else {
                            sub.onError(new Exception());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sub.onError(e);
                    }
                }
        );
    }
}
