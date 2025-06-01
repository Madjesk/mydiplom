package com.example.mailintegration.entity;

// BusinessProcess.java

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "business_process")
public class BusinessProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;                 // «Название бизнес-процесса»

    @Column(length = 500)
    private String description;          // «Описание»

    // Каждый БП — внутри одной компании
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "businessProcess",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BusinessProcessStage> stages = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<BusinessProcessStage> getStages() {
        return stages;
    }

    public void setStages(List<BusinessProcessStage> stages) {
        this.stages = stages;
    }

    /* геттеры/сеттеры */
}

