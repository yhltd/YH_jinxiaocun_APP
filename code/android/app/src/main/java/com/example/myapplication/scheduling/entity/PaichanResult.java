// PaichanResult.java - 增强版
package com.example.myapplication.scheduling.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PaichanResult implements Serializable {
    private String orderId;
    private String orderType;
    private String processName;
    private double processEfficiency;
    private int quantity;
    private String productionLine;
    private double lineEfficiency;
    private Date startTime;
    private Date endTime;
    private double requiredHours;
    private double actualWorkHours;
    private int totalDays;
    private Date deadline;
    private boolean isInsertOrder;
    private String note;
    private String priorityColor;
    private boolean isPartial;
    private String partialNote;
    private int remainingQuantity;
    private boolean isResumed;
    private String totalNote;
    private boolean warning;
    private int parallelCount;
    private String workTimeConfig;
    private List<Integer> allocatedQuantities;
    private boolean isUrgent;
    private Date pausedAt;
    private int completedQuantity;

    // 构造函数
    public PaichanResult() {}

    public PaichanResult(String orderId, String processName, int quantity) {
        this.orderId = orderId;
        this.processName = processName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }

    public double getProcessEfficiency() { return processEfficiency; }
    public void setProcessEfficiency(double processEfficiency) { this.processEfficiency = processEfficiency; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getProductionLine() { return productionLine; }
    public void setProductionLine(String productionLine) { this.productionLine = productionLine; }

    public double getLineEfficiency() { return lineEfficiency; }
    public void setLineEfficiency(double lineEfficiency) { this.lineEfficiency = lineEfficiency; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public double getRequiredHours() { return requiredHours; }
    public void setRequiredHours(double requiredHours) { this.requiredHours = requiredHours; }

    public double getActualWorkHours() { return actualWorkHours; }
    public void setActualWorkHours(double actualWorkHours) { this.actualWorkHours = actualWorkHours; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }

    public boolean isInsertOrder() { return isInsertOrder; }
    public void setInsertOrder(boolean insertOrder) { isInsertOrder = insertOrder; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPriorityColor() { return priorityColor; }
    public void setPriorityColor(String priorityColor) { this.priorityColor = priorityColor; }

    public boolean isPartial() { return isPartial; }
    public void setPartial(boolean partial) { isPartial = partial; }

    public String getPartialNote() { return partialNote; }
    public void setPartialNote(String partialNote) { this.partialNote = partialNote; }

    public int getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(int remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public boolean isResumed() { return isResumed; }
    public void setResumed(boolean resumed) { isResumed = resumed; }

    public String getTotalNote() { return totalNote; }
    public void setTotalNote(String totalNote) { this.totalNote = totalNote; }

    public boolean isWarning() { return warning; }
    public void setWarning(boolean warning) { this.warning = warning; }

    public int getParallelCount() { return parallelCount; }
    public void setParallelCount(int parallelCount) { this.parallelCount = parallelCount; }

    public String getWorkTimeConfig() { return workTimeConfig; }
    public void setWorkTimeConfig(String workTimeConfig) { this.workTimeConfig = workTimeConfig; }

    public List<Integer> getAllocatedQuantities() { return allocatedQuantities; }
    public void setAllocatedQuantities(List<Integer> allocatedQuantities) { this.allocatedQuantities = allocatedQuantities; }

    public boolean isUrgent() { return isUrgent; }
    public void setUrgent(boolean urgent) { isUrgent = urgent; }

    public Date getPausedAt() { return pausedAt; }
    public void setPausedAt(Date pausedAt) { this.pausedAt = pausedAt; }

    public int getCompletedQuantity() { return completedQuantity; }
    public void setCompletedQuantity(int completedQuantity) { this.completedQuantity = completedQuantity; }
}