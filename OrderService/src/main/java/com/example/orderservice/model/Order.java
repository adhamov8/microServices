package com.example.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID is required")
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER) // Changed to EAGER
    @JoinColumn(name = "from_station_id", nullable = false)
    @NotNull(message = "From station is required")
    private Station fromStation;

    @ManyToOne(fetch = FetchType.EAGER) // Changed to EAGER
    @JoinColumn(name = "to_station_id", nullable = false)
    @NotNull(message = "To station is required")
    private Station toStation;

    @Column(nullable = false)
    private Integer status; // 1 - check, 2 - success, 3 - rejection

    @Column(nullable = false)
    private LocalDateTime created;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
