/*
 * Copyright 2016 孙顺涛
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nebula;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;

import com.nebula.take.LaStorageFile;

/**
 * Created by stary on 2016/11/25.
 * 莳萝花，晴雨荡气，sunshuntao，qydq
 * Contact : qyddai@gmail.com
 * 说明：应用主类
 * 最后修改：on 2016/11/25.
 */

public class AnApplication extends Application {

    private static AnApplication instance;
    public static SharedPreferences sp;


    public static AnApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getSharedPreferences(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getDefaultSharedPreferences(this);
        }
    }
}
