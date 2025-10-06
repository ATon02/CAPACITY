package co.com.backend.reactive.api.helper;

import java.awt.print.Pageable;

public enum ConstHandler {

    Id_PARAM("id",null),
    IDs_PARAM("ids",""),
    PAGE_PARAM("page","0"),
    SIZE_PARAM("size","10"),
    SORT_PARAM("sort","name,asc"),
    SORT_BY_PARAM("sortBy","name"),
    SORT_DIRECTION_PARAM("sortDirection","asc")
    ;

    private final String parameterName;
    private final String defaultValue;

    ConstHandler(String parameterName, String defaultValue) {
        this.parameterName = parameterName;
        this.defaultValue = defaultValue;
    }

    public String getParameterName() {return parameterName;}

    public String getDefaultValue(){return defaultValue;}
}
