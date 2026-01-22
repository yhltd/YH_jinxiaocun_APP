package com.example.myapplication.renshi.service;

import android.util.Log;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiDongTaiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiGongShiGuanLi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class YhRenShiDongTaiMingXiService {
    private final String TITLE_SEPARATOR = "|||";
    private static final int MAX_RETRY_COUNT = 3;
    private static final int RETRY_DELAY_MS = 100;

    /**
     * 正确分割字符串的方法
     */
    public String[] splitFieldValues(String valueStr) {
        if (valueStr == null || valueStr.isEmpty()) {
            return new String[0];
        }
        return valueStr.split("\\|\\|\\|");
    }

    /**
     * 带重试机制的数据库执行方法
     */
    private boolean executeWithRetry(String sql, Object... params) {
        int retryCount = 0;
        while (retryCount < MAX_RETRY_COUNT) {
            try {
                renshiBaseDao base = new renshiBaseDao(); // 每次创建新的实例
                boolean result = base.execute(sql, params);
                return result;
            } catch (Exception e) {
                retryCount++;
                Log.w("Database", "执行SQL失败，重试 " + retryCount + "/" + MAX_RETRY_COUNT + ": " + e.getMessage());

                if (retryCount < MAX_RETRY_COUNT) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * retryCount); // 递增延迟
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    Log.e("Database", "SQL执行失败，已达到最大重试次数");
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 带重试机制的查询方法
     */
    private <T> List<T> queryWithRetry(Class<T> clazz, String sql, Object... params) {
        int retryCount = 0;
        while (retryCount < MAX_RETRY_COUNT) {
            try {
                renshiBaseDao base = new renshiBaseDao(); // 每次创建新的实例
                List<T> result = base.query(clazz, sql, params);
                return result;
            } catch (Exception e) {
                retryCount++;
                Log.w("Database", "查询失败，重试 " + retryCount + "/" + MAX_RETRY_COUNT + ": " + e.getMessage());

                if (retryCount < MAX_RETRY_COUNT) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * retryCount); // 递增延迟
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    Log.e("Database", "查询失败，已达到最大重试次数");
                    return new ArrayList<>();
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 获取动态明细列表
     */
    public List<YhRenShiDongTaiMingXi> getList(String company) {
        String sql = "select * from gongzi_dongtaimingxi where gongsi = ? and id > 1 order by id";
        return queryWithRetry(YhRenShiDongTaiMingXi.class, sql, company);
    }

    /**
     * 获取所有数据（包括标题配置）
     */
    public List<YhRenShiDongTaiMingXi> getAllData(String company) {
        String sql = "select * from gongzi_dongtaimingxi where gongsi = ? order by id";
        return queryWithRetry(YhRenShiDongTaiMingXi.class, sql, company);
    }

    /**
     * 获取标题配置
     */
    public YhRenShiDongTaiMingXi getTitleConfig(String company) {
        String sql = "select * from gongzi_dongtaimingxi where id = 1 and gongsi = ?";
        List<YhRenShiDongTaiMingXi> result = queryWithRetry(YhRenShiDongTaiMingXi.class, sql, company);
        return result != null && result.size() > 0 ? result.get(0) : null;
    }

    /**
     * 保存标题配置
     */
    public boolean saveTitleConfig(String company, String titleStr) {
        // 检查是否存在
        YhRenShiDongTaiMingXi existing = getTitleConfig(company);
        String sql;

        if (existing != null) {
            sql = "update gongzi_dongtaimingxi set name = ? where id = 1 and gongsi = ?";
            return executeWithRetry(sql, titleStr, company);
        } else {
            sql = "insert into gongzi_dongtaimingxi (id, gongsi, name) values (1, ?, ?)";
            return executeWithRetry(sql, company, titleStr);
        }
    }

    /**
     * 新增一行数据
     */
    public boolean insert(String company, String dataStr) {
        String sql = "insert into gongzi_dongtaimingxi (gongsi, name) values(?, ?)";
        return executeWithRetry(sql, company, dataStr);
    }

    /**
     * 更新数据行
     */
    public boolean update(int id, String company, String dataStr) {
        String sql = "update gongzi_dongtaimingxi set name = ? where id = ? and gongsi = ?";
        return executeWithRetry(sql, dataStr, id, company);
    }

    /**
     * 删除数据行
     */
    public boolean delete(int id, String company) {
        String sql = "delete from gongzi_dongtaimingxi where id = ? and gongsi = ?";
        return executeWithRetry(sql, id, company);
    }

    /**
     * 处理标题配置
     */
    public List<Map<String, Object>> processTitleConfig(String titleStr) {
        List<Map<String, Object>> dynamicTitles = new ArrayList<>();

        if (titleStr != null && !titleStr.trim().isEmpty()) {
            String[] titleArray = splitFieldValues(titleStr);

            for (int i = 0; i < titleArray.length; i++) {
                if (!titleArray[i].trim().isEmpty()) {
                    Map<String, Object> title = new HashMap<>();
                    title.put("text", titleArray[i].trim());
                    title.put("columnIndex", i);
                    title.put("type", "text");
                    title.put("isupd", true);
                    dynamicTitles.add(title);
                }
            }
        }

        // 如果没有配置，创建默认
        if (dynamicTitles.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                Map<String, Object> title = new HashMap<>();
                title.put("text", "字段" + (i + 1));
                title.put("columnIndex", i);
                title.put("type", "text");
                title.put("isupd", true);
                dynamicTitles.add(title);
            }
        }

        return dynamicTitles;
    }

    /**
     * 处理内容数据
     */
    public List<Map<String, Object>> processContentData(List<YhRenShiDongTaiMingXi> contentData,
                                                        List<Map<String, Object>> dynamicTitles) {
        List<Map<String, Object>> processedData = new ArrayList<>();

        for (YhRenShiDongTaiMingXi item : contentData) {
            Map<String, Object> dataItem = new HashMap<>();
            dataItem.put("id", item.getId());
            dataItem.put("gongsi", item.getGongsi());
            dataItem.put("name", item.getName());

            // 解析分隔符字符串
            if (item.getName() != null && !item.getName().trim().isEmpty()) {
                String[] dataArray = splitFieldValues(item.getName());

                for (int i = 0; i < dynamicTitles.size(); i++) {
                    String value = i < dataArray.length ? dataArray[i].trim() : "";
                    dataItem.put("col_" + i, value);
                }
            } else {
                // 如果没有数据，填充空值
                for (int i = 0; i < dynamicTitles.size(); i++) {
                    dataItem.put("col_" + i, "");
                }
            }

            processedData.add(dataItem);
        }

        return processedData;
    }

    /**
     * 计算公式配置相关方法
     */
    public List<YhRenShiGongShiGuanLi> getFormulaList(String company) {
        String sql = "select * from gongzi_dongtaigongshi where gongsi = ? order by id";
        return queryWithRetry(YhRenShiGongShiGuanLi.class, sql, company);
    }

    public boolean saveFormula(String zhuziduan, String gongshi, String company) {
        // 使用带重试机制的保存方法
        return saveFormulaWithRetry(zhuziduan, gongshi, company, 0);
    }

    /**
     * 带重试机制的公式保存方法
     */
    private boolean saveFormulaWithRetry(String zhuziduan, String gongshi, String company, int retryCount) {
        try {
            // 检查是否已存在
            String checkSql = "select id from gongzi_dongtaigongshi where zhuziduan = ? and gongsi = ?";
            List<YhRenShiGongShiGuanLi> existing = queryWithRetry(YhRenShiGongShiGuanLi.class, checkSql, zhuziduan, company);

            String sql;
            if (existing != null && existing.size() > 0) {
                sql = "update gongzi_dongtaigongshi set gongshi = ? where zhuziduan = ? and gongsi = ?";
                return executeWithRetry(sql, gongshi, zhuziduan, company);
            } else {
                sql = "insert into gongzi_dongtaigongshi (zhuziduan, gongshi, gongsi) values (?, ?, ?)";
                return executeWithRetry(sql, zhuziduan, gongshi, company);
            }
        } catch (Exception e) {
            Log.e("DongTaiMingXiService", "保存公式错误: " + e.getMessage());

            // 如果还有重试机会，等待后重试
            if (retryCount < 2) { // 最多重试2次
                try {
                    Thread.sleep(200 * (retryCount + 1)); // 递增等待时间
                    return saveFormulaWithRetry(zhuziduan, gongshi, company, retryCount + 1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            return false;
        }
    }

    public boolean deleteFormula(int id, String company) {
        String sql = "delete from gongzi_dongtaigongshi where id = ? and gongsi = ?";
        return executeWithRetry(sql, id, company);
    }

    /**
     * 检查字段是否在公式中被使用
     */
    public boolean isFieldUsedInFormula(String fieldName, String company) {
        List<YhRenShiGongShiGuanLi> formulaList = getFormulaList(company);
        if (formulaList == null || formulaList.isEmpty()) {
            return false;
        }

        for (YhRenShiGongShiGuanLi formula : formulaList) {
            // 检查字段是否作为目标字段或出现在公式中
            if (formula.getZhuziduan().equals(fieldName) ||
                    formula.getGongshi().contains(fieldName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 计算并更新所有数据 - 只计算配置公式涉及到的字段
     */
    public boolean calculateAndUpdateAllData(String company, List<Map<String, Object>> dynamicTitles) {
        try {
            // 获取公式配置
            List<YhRenShiGongShiGuanLi> formulaList = getFormulaList(company);
            formulaList = sortFormulasByDependency(formulaList);
            if (formulaList.isEmpty()) {
                return true; // 没有公式，无需计算
            }

            // 构建目标字段集合（只更新这些字段）
            Set<String> targetFields = new HashSet<>();
            for (YhRenShiGongShiGuanLi formula : formulaList) {
                targetFields.add(formula.getZhuziduan());
            }

            // 如果没有任何目标字段，直接返回
            if (targetFields.isEmpty()) {
                return true;
            }

            // 构建字段名到索引的映射
            Map<String, Integer> fieldNameToIndex = new HashMap<>();
            for (int i = 0; i < dynamicTitles.size(); i++) {
                String fieldName = (String) dynamicTitles.get(i).get("text");
                fieldNameToIndex.put(fieldName, i);
            }

            // 获取所有内容数据
            List<YhRenShiDongTaiMingXi> contentData = getList(company);

            for (YhRenShiDongTaiMingXi item : contentData) {
                // 解析当前行数据
                String[] dataArray = item.getName() != null ?
                        splitFieldValues(item.getName()) : new String[0];

                // 确保数组长度与标题数量一致
                while (dataArray.length < dynamicTitles.size()) {
                    String[] newArray = new String[dataArray.length + 1];
                    System.arraycopy(dataArray, 0, newArray, 0, dataArray.length);
                    newArray[dataArray.length] = "";
                    dataArray = newArray;
                }

                // 创建字段值映射（只包含当前行的值）
                Map<String, Double> fieldValues = new HashMap<>();
                for (int i = 0; i < dynamicTitles.size(); i++) {
                    String fieldName = (String) dynamicTitles.get(i).get("text");
                    String fieldValueStr = dataArray[i] != null ? dataArray[i] : "0";
                    double numericValue;
                    try {
                        numericValue = Double.parseDouble(fieldValueStr);
                    } catch (NumberFormatException e) {
                        numericValue = 0;
                    }
                    fieldValues.put(fieldName, numericValue);
                }

                // 是否发生了变更
                boolean hasChanged = false;

                // 对每个公式进行计算（注意依赖关系）
                // 为了处理字段间的依赖，可能需要多轮计算
                int maxIterations = 10; // 最大迭代次数，防止无限循环
                for (int iteration = 0; iteration < maxIterations; iteration++) {
                    boolean iterationChanged = false;

                    for (YhRenShiGongShiGuanLi formula : formulaList) {
                        String targetField = formula.getZhuziduan();
                        String expression = formula.getGongshi();

                        // 只有目标字段在fieldNameToIndex中才计算
                        if (!fieldNameToIndex.containsKey(targetField)) {
                            continue; // 目标字段不在当前标题配置中，跳过
                        }

                        // 替换表达式中的字段名
                        String workingExpression = expression;
                        for (Map.Entry<String, Double> entry : fieldValues.entrySet()) {
                            String fieldName = entry.getKey();
                            Double fieldValue = entry.getValue();
                            // 使用正则表达式确保只替换完整的字段名
                            workingExpression = workingExpression.replaceAll(
                                    "(?<![a-zA-Z0-9_])" + Pattern.quote(fieldName) + "(?![a-zA-Z0-9_])",
                                    String.valueOf(fieldValue));
                        }

                        // 计算表达式
                        double result = calculateExpression(workingExpression);

                        // 获取原值
                        Double oldValue = fieldValues.get(targetField);

                        // 检查是否有变化（考虑浮点数精度）
                        boolean valueChanged = false;
                        if (oldValue == null) {
                            valueChanged = true;
                        } else {
                            double diff = Math.abs(result - oldValue);
                            // 只有当变化超过一定阈值时才认为是真正的变化
                            valueChanged = diff > 0.0001;
                        }

                        if (valueChanged) {
                            fieldValues.put(targetField, result);

                            // 更新数据数组中对应的值（只更新目标字段）
                            int targetIndex = fieldNameToIndex.get(targetField);
                            // 格式化为字符串，保留2位小数（或者根据业务需求调整）
                            String formattedValue;
                            if (result == (long) result) {
                                formattedValue = String.format("%d", (long) result);
                            } else {
                                formattedValue = String.format("%.2f", result);
                            }
                            dataArray[targetIndex] = formattedValue;

                            hasChanged = true;
                            iterationChanged = true;
                        }
                    }

                    // 如果本轮没有变化，提前结束
                    if (!iterationChanged) {
                        break;
                    }
                }

                // 如果有字段被更新，保存数据
                if (hasChanged) {
                    // 构建新的数据字符串
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < dataArray.length; i++) {
                        if (i > 0) {
                            sb.append(TITLE_SEPARATOR);
                        }
                        sb.append(dataArray[i] != null ? dataArray[i] : "");
                    }
                    String newName = sb.toString();

                    // 使用重试机制更新数据
                    boolean updated = false;
                    int updateRetry = 0;
                    while (!updated && updateRetry < 3) {
                        updated = update(item.getId(), company, newName);
                        if (!updated) {
                            updateRetry++;
                            try {
                                Thread.sleep(100 * updateRetry);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }

                    if (!updated) {

                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DongTaiMingXiService", "计算数据错误: " + e.getMessage());
            return false;
        }
    }

    /**
     * 分析公式依赖关系，返回排序后的公式列表
     */
    private List<YhRenShiGongShiGuanLi> sortFormulasByDependency(List<YhRenShiGongShiGuanLi> formulas) {
        // 简单的依赖分析：目标字段不能依赖后续的字段
        // 这里实现一个简单的拓扑排序
        List<YhRenShiGongShiGuanLi> sortedList = new ArrayList<>();
        Set<YhRenShiGongShiGuanLi> processed = new HashSet<>();
        Set<YhRenShiGongShiGuanLi> remaining = new HashSet<>(formulas);

        int maxIterations = formulas.size() * formulas.size();
        int iteration = 0;

        while (!remaining.isEmpty() && iteration < maxIterations) {
            iteration++;

            for (YhRenShiGongShiGuanLi formula : new ArrayList<>(remaining)) {
                String targetField = formula.getZhuziduan();
                String expression = formula.getGongshi();

                // 检查这个公式的表达式是否依赖其他未处理的公式的目标字段
                boolean dependsOnUnprocessed = false;
                for (YhRenShiGongShiGuanLi otherFormula : remaining) {
                    if (formula == otherFormula) continue;

                    String otherTarget = otherFormula.getZhuziduan();
                    if (expression.contains(otherTarget)) {
                        dependsOnUnprocessed = true;
                        break;
                    }
                }

                // 如果不依赖未处理的公式，或者依赖的公式已经处理了，就处理这个公式
                if (!dependsOnUnprocessed) {
                    sortedList.add(formula);
                    processed.add(formula);
                    remaining.remove(formula);
                }
            }
        }

        // 如果还有未处理的，按原始顺序添加到末尾
        if (!remaining.isEmpty()) {
            sortedList.addAll(remaining);
        }

        return sortedList;
    }


    /**
     * 简单表达式计算 - 改进版
     */
    private double calculateExpression(String expression) {
        try {
            // 移除空格
            expression = expression.replaceAll("\\s+", "");

            // 如果表达式为空，返回0
            if (expression == null || expression.trim().isEmpty()) {
                return 0;
            }

            // 替换中文运算符
            expression = expression.replace("×", "*").replace("÷", "/");

            // 检查表达式是否包含未替换的字段名（字母和数字组合）
            // 如果有未替换的字段，说明字段值可能是0，但我们仍然需要处理
            if (expression.matches(".*[a-zA-Z_]+.*")) {
                Log.w("calculateExpression", "表达式中可能还有未替换的字段: " + expression);
                // 尝试将字母数字组合替换为0
                expression = expression.replaceAll("[a-zA-Z_][a-zA-Z0-9_]*", "0");
            }

            // 简单的表达式计算
            return evaluateSimpleExpression(expression);
        } catch (Exception e) {
            Log.e("DongTaiMingXiService", "计算表达式错误: " + expression, e);
            return 0;
        }
    }

    private double evaluateSimpleExpression(String expr) {
        try {
            // 使用递归下降解析器处理运算符优先级
            return parseExpression(expr);
        } catch (Exception e) {
            Log.e("DongTaiMingXiService", "解析表达式错误: " + expr, e);
            return 0;
        }
    }

    private double parseExpression(String expr) {
        // 处理加法和减法
        int index = findLowestPriorityOperator(expr, new char[]{'+', '-'});
        if (index != -1) {
            char op = expr.charAt(index);
            double left = parseExpression(expr.substring(0, index));
            double right = parseExpression(expr.substring(index + 1));
            if (op == '+') {
                return left + right;
            } else {
                return left - right;
            }
        }

        // 处理乘法和除法
        index = findLowestPriorityOperator(expr, new char[]{'*', '/'});
        if (index != -1) {
            char op = expr.charAt(index);
            double left = parseExpression(expr.substring(0, index));
            double right = parseExpression(expr.substring(index + 1));
            if (op == '*') {
                return left * right;
            } else {
                if (right == 0) return 0;
                return left / right;
            }
        }

        // 处理括号
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseExpression(expr.substring(1, expr.length() - 1));
        }

        // 解析数字
        try {
            return Double.parseDouble(expr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int findLowestPriorityOperator(String expr, char[] operators) {
        int parenCount = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') parenCount++;
            else if (c == '(') parenCount--;
            else if (parenCount == 0) {
                for (char op : operators) {
                    if (c == op) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}