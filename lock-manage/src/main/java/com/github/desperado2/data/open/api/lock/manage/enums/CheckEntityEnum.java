/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package com.github.desperado2.data.open.api.lock.manage.enums;

public enum CheckEntityEnum {
    USER("user", "userService", "com.github.desperado2.open.data.platform.user.manage.entity.User");

    private String source;
    private String service;
    private String clazz;


    CheckEntityEnum(String source, String service, String clazz) {
        this.source = source;
        this.service = service;
        this.clazz = clazz;
    }

    public static CheckEntityEnum sourceOf(String source) {
        for (CheckEntityEnum sourceEnum : values()) {
            if (sourceEnum.source.equals(source)) {
                return sourceEnum;
            }
        }
        return null;
    }

    public String getService() {
        return service;
    }

    public String getClazz() {
        return clazz;
    }

    public String getSource() {
        return source;
    }
}
