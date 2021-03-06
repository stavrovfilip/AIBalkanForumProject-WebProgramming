package com.webproject.aibalkanforumproject.model;

import com.webproject.aibalkanforumproject.model.enumerations.JobType;
import lombok.Data;


import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Company company;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private JobType jobType;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String knowLedgeSkillsAndAbilities;

    @Column(length = 2000)
    private String experience;

    private String salary;

    @ManyToOne
    private Location location;

    private LocalDateTime dateCreated;

    private LocalDateTime deadlineApply;

    @ManyToOne
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    private Image image;

    public Job() {
    }


    public Job(Company company,
               String title,
               JobType jobType,
               String description,
               String knowLedgeSkillsAndAbilities,
               String experience,
               String salary,
               Location location,
               LocalDateTime deadlineApply,
               Category category,
               Image image) {
        this.company = company;
        this.title = title;
        this.jobType = jobType;
        this.description = description;
        this.knowLedgeSkillsAndAbilities = knowLedgeSkillsAndAbilities;
        this.experience = experience;
        this.salary = salary;
        this.location = location;
        this.deadlineApply = deadlineApply;
        this.category = category;
        this.image = image;
        this.dateCreated = LocalDateTime.now();
    }
}
