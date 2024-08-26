package com.kosa.kosafinalprojbackend.mybatis.mappers.project;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper {

    // 저장
    Long insertProject(ProjectForm projectForm);
}
