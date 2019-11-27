package pl.pbaranowski.ha.heatingplan.model;

import java.util.List;

public class HeatingPlan {
    public double hysteresis = 0.1;
    public double defaultTemp; //default temp for a whole week
    public List<DayOfWeek> days;
}
