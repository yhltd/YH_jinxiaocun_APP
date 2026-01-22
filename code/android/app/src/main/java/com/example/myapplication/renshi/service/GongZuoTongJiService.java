package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.DongTaiMingXi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GongZuoTongJiService {
    private renshiBaseDao base;
    private Gson gson = new Gson();
    private static final String TITLE_SEPARATOR = "|||";
    private static final int TITLE_CONFIG_ID = 1;

    /**
     * 正确分割字符串的方法
     */
    private String[] splitFieldValues(String valueStr) {
        if (valueStr == null || valueStr.isEmpty()) {
            return new String[0];
        }
        return valueStr.split("\\|\\|\\|");
    }

    /**
     * 动态字段数据模型
     */
    public static class DynamicData {
        public int id;
        public List<String> values;
        public Map<String, Object> fieldMap;
    }

    /**
     * 加载图表数据（从动态明细表）
     */
    public Map<String, Object> loadChartData(String company) {
        Map<String, Object> result = new HashMap<>();

        try {
            company = company.replace("_hr", "");

            // 1. 获取标题配置和数据行（合并到一个查询中）
            String sql = "SELECT id, gongsi, name FROM gongzi_dongtaimingxi WHERE gongsi = ? ORDER BY id";
            base = new renshiBaseDao();
            List<DongTaiMingXi> allRecords = base.query(DongTaiMingXi.class, sql, company);

            System.out.println("查询结果数量: " + (allRecords != null ? allRecords.size() : 0));

            List<String> fields = new ArrayList<>();
            List<DynamicData> dynamicDataList = new ArrayList<>();
            int totalRows = 0;

            if (allRecords != null && !allRecords.isEmpty()) {
                // 先处理标题配置（id=1的记录）
                for (DongTaiMingXi record : allRecords) {
                    System.out.println("记录 ID: " + record.getId() + ", 内容: " + record.getName());
                    if (record.getId() == TITLE_CONFIG_ID) {
                        // 这是标题配置行
                        String titleStr = record.getName();
                        System.out.println("标题配置原始内容: " + titleStr);
                        if (titleStr != null && !titleStr.isEmpty()) {
                            // 使用正确的方法解析标题配置
                            String[] titleArray = splitFieldValues(titleStr);
                            System.out.println("分割后字段数量: " + titleArray.length);
                            for (String title : titleArray) {
                                if (title != null && !title.trim().isEmpty()) {
                                    fields.add(title.trim());
                                }
                            }
                        }
                        break; // 标题配置只有一条
                    }
                }

                System.out.println("最终字段列表: " + fields);

                // 如果没有标题配置，使用默认字段
                if (fields.isEmpty()) {
                    for (int i = 1; i <= 10; i++) {
                        fields.add("字段" + i);
                    }
                }

                // 处理数据行（排除标题配置行）
                for (DongTaiMingXi record : allRecords) {
                    if (record.getId() == TITLE_CONFIG_ID) {
                        continue; // 跳过标题配置行
                    }

                    totalRows++;

                    DynamicData data = new DynamicData();
                    data.id = record.getId();
                    String valueStr = record.getName();

                    List<String> values = new ArrayList<>();
                    if (valueStr != null && !valueStr.isEmpty()) {
                        // 使用正确的方法解析数据值
                        String[] valueArray = splitFieldValues(valueStr);
                        for (String value : valueArray) {
                            values.add(value != null ? value.trim() : "");
                        }
                    }

                    // 确保数组长度与标题一致
                    while (values.size() < fields.size()) {
                        values.add("");
                    }
                    while (values.size() > fields.size()) {
                        values.remove(values.size() - 1);
                    }

                    data.values = values;

                    // 创建字段映射
                    data.fieldMap = new HashMap<>();
                    for (int i = 0; i < fields.size(); i++) {
                        String fieldName = fields.get(i);
                        String fieldValue = i < values.size() ? values.get(i) : "";
                        data.fieldMap.put(fieldName, fieldValue);
                    }

                    dynamicDataList.add(data);
                }
            }

            // 3. 准备返回数据
            result.put("success", true);
            result.put("message", "加载成功");
            result.put("dataCount", totalRows);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("fields", fields);
            dataMap.put("data", dynamicDataList);
            result.put("data", dataMap);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "加载失败：" + e.getMessage());
        }

        return result;
    }

    /**
     * 获取数值字段列表（用于图表统计）
     */
    public List<String> getNumericFields(List<String> allFields, List<DynamicData> dataList) {
        List<String> numericFields = new ArrayList<>();

        if (allFields == null || allFields.isEmpty() || dataList == null || dataList.isEmpty()) {
            return numericFields;
        }

        // 检查每个字段是否包含数值数据
        for (String field : allFields) {
            boolean hasNumericValue = false;
            int numericCount = 0;
            int totalRows = dataList.size();

            for (DynamicData data : dataList) {
                Object valueObj = data.fieldMap.get(field);
                if (valueObj != null) {
                    String value = valueObj.toString();
                    if (!value.isEmpty()) {
                        try {
                            // 尝试转换为数字
                            Double.parseDouble(value);
                            numericCount++;
                            // 如果有30%以上的行是数值，认为该字段是数值字段
                            if (numericCount > totalRows * 0.3) {
                                hasNumericValue = true;
                                break;
                            }
                        } catch (NumberFormatException e) {
                            // 不是数值，继续检查
                        }
                    }
                }
            }

            if (hasNumericValue) {
                numericFields.add(field);
            }
        }

        return numericFields;
    }

    /**
     * 生成图表数据
     */
    public Map<String, Object> generateChartData(List<String> selectedFields, String groupField,
                                                 String chartType, List<DynamicData> dataList) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (selectedFields == null || selectedFields.isEmpty() || dataList == null || dataList.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择统计字段");
                return result;
            }

            // 如果没有分组字段，生成简单图表
            if (groupField == null || groupField.isEmpty()) {
                return generateSimpleChart(selectedFields, chartType, dataList);
            } else {
                return generateGroupedChart(selectedFields, groupField, chartType, dataList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "生成图表失败：" + e.getMessage());
        }

        return result;
    }

    /**
     * 生成简单图表（无分组）
     */
    private Map<String, Object> generateSimpleChart(List<String> selectedFields, String chartType,
                                                    List<DynamicData> dataList) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<String> labels = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                labels.add("记录" + (i + 1));
            }

            List<Map<String, Object>> datasets = new ArrayList<>();

            // 为每个选中的字段创建数据集
            for (int i = 0; i < selectedFields.size(); i++) {
                String field = selectedFields.get(i);
                List<Double> values = new ArrayList<>();

                // 收集该字段的所有数值
                for (DynamicData data : dataList) {
                    Object valueObj = data.fieldMap.get(field);
                    double value = 0;
                    if (valueObj != null) {
                        String valueStr = valueObj.toString();
                        if (!valueStr.isEmpty()) {
                            try {
                                value = Double.parseDouble(valueStr);
                            } catch (NumberFormatException e) {
                                value = 0;
                            }
                        }
                    }
                    values.add(value);
                }

                // 计算统计信息
                Map<String, Object> stats = calculateStatistics(values);

                Map<String, Object> dataset = new HashMap<>();
                dataset.put("label", field + " (平均: " +
                        String.format("%.2f", (Double) stats.get("average")) + ")");
                dataset.put("data", values);
                dataset.put("colorIndex", i);

                datasets.add(dataset);
            }

            result.put("success", true);
            result.put("labels", labels);
            result.put("datasets", datasets);
            result.put("chartType", chartType);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "生成简单图表失败：" + e.getMessage());
        }

        return result;
    }

    /**
     * 生成分组图表
     */
    private Map<String, Object> generateGroupedChart(List<String> selectedFields, String groupField,
                                                     String chartType, List<DynamicData> dataList) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 按分组字段的值分组数据
            Map<String, List<DynamicData>> groupedData = new LinkedHashMap<>();
            List<String> labels = new ArrayList<>();

            for (DynamicData data : dataList) {
                Object groupValueObj = data.fieldMap.get(groupField);
                String groupValue = groupValueObj != null ? groupValueObj.toString() : "";
                if (groupValue == null || groupValue.isEmpty()) {
                    groupValue = "未知";
                }

                if (!groupedData.containsKey(groupValue)) {
                    groupedData.put(groupValue, new ArrayList<>());
                }
                groupedData.get(groupValue).add(data);

                if (!labels.contains(groupValue)) {
                    labels.add(groupValue);
                }
            }

            // 确保标签有序
            Collections.sort(labels);

            List<Map<String, Object>> datasets = new ArrayList<>();

            // 为每个选中的字段创建数据集（排除分组字段）
            for (int i = 0; i < selectedFields.size(); i++) {
                String field = selectedFields.get(i);

                // 如果选择了分组字段作为统计字段，跳过
                if (field.equals(groupField)) {
                    continue;
                }

                List<Double> values = new ArrayList<>();

                // 计算每个分组的平均值
                for (String groupValue : labels) {
                    List<DynamicData> groupRows = groupedData.get(groupValue);
                    if (groupRows != null && !groupRows.isEmpty()) {
                        double sum = 0;
                        int count = 0;

                        for (DynamicData data : groupRows) {
                            Object valueObj = data.fieldMap.get(field);
                            if (valueObj != null) {
                                String valueStr = valueObj.toString();
                                if (!valueStr.isEmpty()) {
                                    try {
                                        sum += Double.parseDouble(valueStr);
                                        count++;
                                    } catch (NumberFormatException e) {
                                        // 忽略非数值
                                    }
                                }
                            }
                        }

                        values.add(count > 0 ? sum / count : 0);
                    } else {
                        values.add(0.0);
                    }
                }

                // 计算统计信息
                Map<String, Object> stats = calculateStatistics(values);

                Map<String, Object> dataset = new HashMap<>();
                dataset.put("label", field + " (平均: " +
                        String.format("%.2f", (Double) stats.get("average")) + ")");
                dataset.put("data", values);
                dataset.put("colorIndex", i);

                datasets.add(dataset);
            }

            // 如果数据集为空（可能只选了分组字段），提示用户
            if (datasets.isEmpty()) {
                result.put("success", false);
                result.put("message", "请至少选择一个非分组字段作为统计字段");
                return result;
            }

            result.put("success", true);
            result.put("labels", labels);
            result.put("datasets", datasets);
            result.put("chartType", chartType);
            result.put("groupField", groupField);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "生成分组图表失败：" + e.getMessage());
        }

        return result;
    }

    /**
     * 计算统计信息
     */
    private Map<String, Object> calculateStatistics(List<Double> data) {
        Map<String, Object> stats = new HashMap<>();

        if (data == null || data.isEmpty()) {
            stats.put("average", 0.0);
            stats.put("total", 0.0);
            stats.put("max", 0.0);
            stats.put("min", 0.0);
            stats.put("count", 0);
            return stats;
        }

        // 过滤有效数值
        List<Double> validData = new ArrayList<>();
        for (Double d : data) {
            if (d != null && !Double.isNaN(d)) {
                validData.add(d);
            }
        }

        if (validData.isEmpty()) {
            stats.put("average", 0.0);
            stats.put("total", 0.0);
            stats.put("max", 0.0);
            stats.put("min", 0.0);
            stats.put("count", 0);
            return stats;
        }

        double sum = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (Double d : validData) {
            sum += d;
            if (d > max) max = d;
            if (d < min) min = d;
        }

        stats.put("average", sum / validData.size());
        stats.put("total", sum);
        stats.put("max", max);
        stats.put("min", min);
        stats.put("count", validData.size());

        return stats;
    }

    /**
     * 获取图表类型选项
     */
    public String[] getChartTypeOptions() {
        return new String[]{"柱状图", "折线图"}; // 饼图和环形图暂时不实现
    }

    /**
     * 获取统计信息显示
     */
    public String getStatisticsInfo(List<String> selectedFields, List<DynamicData> dataList) {
        if (selectedFields == null || selectedFields.isEmpty() || dataList == null || dataList.isEmpty()) {
            return "暂无统计信息";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("统计信息：\n");

        for (String field : selectedFields) {
            List<Double> values = new ArrayList<>();

            for (DynamicData data : dataList) {
                Object valueObj = data.fieldMap.get(field);
                double value = 0;
                if (valueObj != null) {
                    String valueStr = valueObj.toString();
                    if (!valueStr.isEmpty()) {
                        try {
                            value = Double.parseDouble(valueStr);
                        } catch (NumberFormatException e) {
                            value = 0;
                        }
                    }
                }
                values.add(value);
            }

            Map<String, Object> stats = calculateStatistics(values);

            sb.append(field)
                    .append(": 平均 ")
                    .append(String.format("%.2f", (Double) stats.get("average")))
                    .append(", 最大 ")
                    .append(String.format("%.2f", (Double) stats.get("max")))
                    .append(", 最小 ")
                    .append(String.format("%.2f", (Double) stats.get("min")))
                    .append(", 总计 ")
                    .append(String.format("%.2f", (Double) stats.get("total")))
                    .append("\n");
        }

        return sb.toString();
    }

    /**
     * 获取数据表格内容
     */
    /**
     * 获取数据表格内容
     */
    public List<Map<String, String>> getDataTable(List<String> selectedFields, String groupField,
                                                  List<DynamicData> dataList) {
        List<Map<String, String>> tableData = new ArrayList<>();

        if (selectedFields == null || selectedFields.isEmpty() || dataList == null || dataList.isEmpty()) {
            return tableData;
        }

        // 创建字段列表（去重）
        Set<String> fieldSet = new LinkedHashSet<>(selectedFields);
        List<String> allFieldList = new ArrayList<>(fieldSet);

        // 如果没有分组字段，显示简单表格
        if (groupField == null || groupField.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                DynamicData data = dataList.get(i);
                Map<String, String> row = new HashMap<>();
                row.put("序号", String.valueOf(i + 1));

                for (String field : allFieldList) {
                    Object valueObj = data.fieldMap.get(field);
                    String value = valueObj != null ? valueObj.toString() : "";
                    row.put(field, value);
                }

                tableData.add(row);
            }
        } else {
            // 分组显示表格
            Map<String, List<DynamicData>> groupedData = new TreeMap<>();

            for (DynamicData data : dataList) {
                Object groupValueObj = data.fieldMap.get(groupField);
                String groupValue = groupValueObj != null ? groupValueObj.toString() : "";
                if (groupValue.isEmpty()) {
                    groupValue = "未知";
                }

                if (!groupedData.containsKey(groupValue)) {
                    groupedData.put(groupValue, new ArrayList<>());
                }
                groupedData.get(groupValue).add(data);
            }

            for (Map.Entry<String, List<DynamicData>> entry : groupedData.entrySet()) {
                String groupValue = entry.getKey();
                List<DynamicData> groupRows = entry.getValue();

                for (int i = 0; i < groupRows.size(); i++) {
                    DynamicData data = groupRows.get(i);
                    Map<String, String> row = new HashMap<>();

                    // 如果是第一行，显示分组值
                    if (i == 0) {
                        row.put(groupField, groupValue);
                    } else {
                        row.put(groupField, "");
                    }

                    // 添加序号
                    row.put("序号", String.valueOf(tableData.size() + 1));

                    for (String field : allFieldList) {
                        if (field.equals(groupField)) continue;

                        Object valueObj = data.fieldMap.get(field);
                        String value = valueObj != null ? valueObj.toString() : "";
                        row.put(field, value);
                    }

                    tableData.add(row);
                }
            }
        }

        return tableData;
    }
}