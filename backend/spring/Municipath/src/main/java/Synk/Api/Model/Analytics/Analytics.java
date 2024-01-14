package Synk.Api.Model.Analytics;

import java.util.Map;

public class Analytics {
    private Map<String, String> resultData;

    public Analytics(Map<String, String> resultData) {
        this.resultData = resultData;
    }

    public Analytics(){
    }

    public Map<String, String> getResultData() {
        return resultData;
    }

    public void setResultData(Map<String, String> resultData) {
        this.resultData = resultData;
    }

    
}
