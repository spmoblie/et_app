package com.elite.display01.loader;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 将一些重复的操作提出来，放到父类以免Loader里每个接口都有重复代码
 * Created by user on 05/03/18.
 */

public class ObjectLoader {

    /**
     * @param observable
     * @param <T>
     * @return
     */
    protected <T> Observable<T> observe(Observable<T> observable){
        return observable .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
