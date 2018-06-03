package com.supinfo.ourcloud.repository;

import com.supinfo.ourcloud.domain.OurFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.File;
import java.util.ArrayList;

public interface UploadFile extends JpaRepository<OurFile, Long> {

    @Query("select f From OurFile f")
    public ArrayList<OurFile> load();

}

