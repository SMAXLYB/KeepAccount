/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package life.chenshi.keepaccounts.module.common.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalUtil {

    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("###,##0.00");

    /**
     * 元转换为分
     */
    public static BigDecimal fromString(String strYuan) {
        BigDecimal result;
        if (!TextUtils.isEmpty(strYuan)) {
            result = new BigDecimal(strYuan);
        } else {
            result = new BigDecimal("0.00");
        }
        return result;
    }

    public static String formatSeparator(BigDecimal money) {
        return MONEY_FORMAT.format(money);
    }
}
