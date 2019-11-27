package pl.pbaranowski.ha.heatingplan;

import com.google.gson.Gson;
import eu.wordpro.ha.api.InvalidConfigurationException;
import eu.wordpro.ha.api.InvalidSignalException;
import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.BytesSignalProcessorData;
import eu.wordpro.ha.api.model.StringSignalProcessorData;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import pl.pbaranowski.ha.heatingplan.logic.HeatingService;
import pl.pbaranowski.ha.heatingplan.model.HeatingPlan;

import java.util.LinkedList;

public class HeatingPlanTest {



    @Test
    public void testIO(){
        HeatingPlan plan = new HeatingPlan();
        plan.defaultTemp = 20;
        Gson gson = new Gson();
        String planAsJson = gson.toJson(plan);
        HeatingService service = new HeatingService();
        try {
            service.setConfiguration(planAsJson);
        } catch (InvalidConfigurationException e) {
            Assert.isTrue(false, "Invalid configuration");
        }
        SignalProcessorData result = null;
        try {
            result = service.processInput(prepareInput("22"));
        } catch (InvalidSignalException e) {
            Assert.isTrue(false, "Invalid signal");
        }
        Assert.isTrue(result != null, "Result is null");
    }



    private LinkedList<SignalProcessorData> prepareInput(String currentTemp){
        LinkedList<SignalProcessorData> result = new LinkedList<>();
        String data = "{\"tmp\" : " + currentTemp + "}";
        SignalProcessorData input = new StringSignalProcessorData(data);
        result.add(input);
        return result;

    }

}
