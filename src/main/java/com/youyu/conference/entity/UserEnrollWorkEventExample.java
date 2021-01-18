package com.youyu.conference.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserEnrollWorkEventExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserEnrollWorkEventExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSendUserIdIsNull() {
            addCriterion("send_user_id is null");
            return (Criteria) this;
        }

        public Criteria andSendUserIdIsNotNull() {
            addCriterion("send_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andSendUserIdEqualTo(Long value) {
            addCriterion("send_user_id =", value, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdNotEqualTo(Long value) {
            addCriterion("send_user_id <>", value, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdGreaterThan(Long value) {
            addCriterion("send_user_id >", value, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("send_user_id >=", value, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdLessThan(Long value) {
            addCriterion("send_user_id <", value, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdLessThanOrEqualTo(Long value) {
            addCriterion("send_user_id <=", value, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdIn(List<Long> values) {
            addCriterion("send_user_id in", values, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdNotIn(List<Long> values) {
            addCriterion("send_user_id not in", values, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdBetween(Long value1, Long value2) {
            addCriterion("send_user_id between", value1, value2, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andSendUserIdNotBetween(Long value1, Long value2) {
            addCriterion("send_user_id not between", value1, value2, "sendUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdIsNull() {
            addCriterion("receive_user_id is null");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdIsNotNull() {
            addCriterion("receive_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdEqualTo(Long value) {
            addCriterion("receive_user_id =", value, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdNotEqualTo(Long value) {
            addCriterion("receive_user_id <>", value, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdGreaterThan(Long value) {
            addCriterion("receive_user_id >", value, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("receive_user_id >=", value, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdLessThan(Long value) {
            addCriterion("receive_user_id <", value, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdLessThanOrEqualTo(Long value) {
            addCriterion("receive_user_id <=", value, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdIn(List<Long> values) {
            addCriterion("receive_user_id in", values, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdNotIn(List<Long> values) {
            addCriterion("receive_user_id not in", values, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdBetween(Long value1, Long value2) {
            addCriterion("receive_user_id between", value1, value2, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andReceiveUserIdNotBetween(Long value1, Long value2) {
            addCriterion("receive_user_id not between", value1, value2, "receiveUserId");
            return (Criteria) this;
        }

        public Criteria andWorkIdIsNull() {
            addCriterion("work_id is null");
            return (Criteria) this;
        }

        public Criteria andWorkIdIsNotNull() {
            addCriterion("work_id is not null");
            return (Criteria) this;
        }

        public Criteria andWorkIdEqualTo(Long value) {
            addCriterion("work_id =", value, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdNotEqualTo(Long value) {
            addCriterion("work_id <>", value, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdGreaterThan(Long value) {
            addCriterion("work_id >", value, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdGreaterThanOrEqualTo(Long value) {
            addCriterion("work_id >=", value, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdLessThan(Long value) {
            addCriterion("work_id <", value, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdLessThanOrEqualTo(Long value) {
            addCriterion("work_id <=", value, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdIn(List<Long> values) {
            addCriterion("work_id in", values, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdNotIn(List<Long> values) {
            addCriterion("work_id not in", values, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdBetween(Long value1, Long value2) {
            addCriterion("work_id between", value1, value2, "workId");
            return (Criteria) this;
        }

        public Criteria andWorkIdNotBetween(Long value1, Long value2) {
            addCriterion("work_id not between", value1, value2, "workId");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNull() {
            addCriterion("event_type is null");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNotNull() {
            addCriterion("event_type is not null");
            return (Criteria) this;
        }

        public Criteria andEventTypeEqualTo(Integer value) {
            addCriterion("event_type =", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotEqualTo(Integer value) {
            addCriterion("event_type <>", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThan(Integer value) {
            addCriterion("event_type >", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("event_type >=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThan(Integer value) {
            addCriterion("event_type <", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThanOrEqualTo(Integer value) {
            addCriterion("event_type <=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeIn(List<Integer> values) {
            addCriterion("event_type in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotIn(List<Integer> values) {
            addCriterion("event_type not in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeBetween(Integer value1, Integer value2) {
            addCriterion("event_type between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("event_type not between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("created_time is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("created_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(LocalDateTime value) {
            addCriterion("created_time =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(LocalDateTime value) {
            addCriterion("created_time <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(LocalDateTime value) {
            addCriterion("created_time >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("created_time >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(LocalDateTime value) {
            addCriterion("created_time <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("created_time <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<LocalDateTime> values) {
            addCriterion("created_time in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<LocalDateTime> values) {
            addCriterion("created_time not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("created_time between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("created_time not between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIsNull() {
            addCriterion("updated_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIsNotNull() {
            addCriterion("updated_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeEqualTo(LocalDateTime value) {
            addCriterion("updated_time =", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotEqualTo(LocalDateTime value) {
            addCriterion("updated_time <>", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeGreaterThan(LocalDateTime value) {
            addCriterion("updated_time >", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("updated_time >=", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeLessThan(LocalDateTime value) {
            addCriterion("updated_time <", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("updated_time <=", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIn(List<LocalDateTime> values) {
            addCriterion("updated_time in", values, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotIn(List<LocalDateTime> values) {
            addCriterion("updated_time not in", values, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("updated_time between", value1, value2, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("updated_time not between", value1, value2, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Boolean value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Boolean value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Boolean value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Boolean value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Boolean> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Boolean> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}