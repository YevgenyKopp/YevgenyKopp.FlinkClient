package ru.kopp.client.model.plan;

import java.util.List;

@SuppressWarnings("unused")
public class FlinkPlanNode {
    private String id;
    private String parallelism;
    private String operator;
    private String operator_strategy;
    private String description;
    private List<FlinkPlanNodeInput> inputs;
    private FlinkPlanOptimizerProperties optimizer_properties;
}