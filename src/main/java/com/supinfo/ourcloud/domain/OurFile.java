package com.supinfo.ourcloud.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.Serializable;
import java.net.URI;


@Entity
@Table(name = "ourfile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class OurFile extends java.io.File {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name="name")
    private String name;

    public OurFile(String pathname) {
        super(pathname);
    }

    public OurFile(String parent, String child) {
        super(parent, child);
    }

    public OurFile(File parent, String child) {
        super(parent, child);
    }

    public OurFile(URI uri) {
        super(uri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OurFile authority = (OurFile) o;

        return !(name != null ? !name.equals(authority.name) : authority.name != null);
    }
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "File{" +
            "name='" + name + '\'' +
            "Size='"+getUsableSpace()+"}";
    }
}
