package com.lagou.domain;

import java.time.LocalDateTime;

public class Order {
    private Integer id;
    private LocalDateTime orderTime;
    private Double total;
    private User user;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", ordertime=" + orderTime +
                ", total=" + total +
                ", user=" + user +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getOrdertime() {
        return orderTime;
    }

    public void setOrdertime(LocalDateTime ordertime) {
        this.orderTime = ordertime;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
