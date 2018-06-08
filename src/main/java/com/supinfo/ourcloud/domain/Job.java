package com.supinfo.ourcloud.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "name_file", nullable = false)
    private byte[] nameFile;

    @Column(name = "name_file_content_type", nullable = false)
    private String nameFileContentType;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private User userToJob;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getNameFile() {
        return nameFile;
    }

    public Job nameFile(byte[] nameFile) {
        this.nameFile = nameFile;
        return this;
    }

    public void setNameFile(byte[] nameFile) {
        this.nameFile = nameFile;
    }

    public String getNameFileContentType() {
        return nameFileContentType;
    }

    public Job nameFileContentType(String nameFileContentType) {
        this.nameFileContentType = nameFileContentType;
        return this;
    }

    public void setNameFileContentType(String nameFileContentType) {
        this.nameFileContentType = nameFileContentType;
    }

    public String getName() {
        return name;
    }

    public Job name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUserToJob() {
        return userToJob;
    }

    public Job userToJob(User user) {
        this.userToJob = user;
        return this;
    }

    public void setUserToJob(User user) {
        this.userToJob = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        if (job.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), job.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", nameFile='" + getNameFile() + "'" +
            ", nameFileContentType='" + getNameFileContentType() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
