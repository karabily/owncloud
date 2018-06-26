package com.supinfo.ourcloud.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserSup.
 */
@Entity
@Table(name = "user_sup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "usersup")
public class UserSup  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "file")
    private File file;
    @OneToOne
    @MapsId
    private User userSup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserSup() {
        return userSup;
    }

    public UserSup userSup(User user) {
        this.userSup = user;
        return this;
    }

    public void setUserSup(User user) {
        this.userSup = user;
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
        UserSup userSup = (UserSup) o;
        if (userSup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userSup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserSup{" +
            "id=" + getId() +
            "}";
    }
}
