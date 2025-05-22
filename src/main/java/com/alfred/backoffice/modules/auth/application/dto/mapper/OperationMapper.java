package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.application.dto.response.OperationDTO;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.OperationEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.ResourceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperationMapper {
    OperationDTO toDTO(OperationEntity operationEntity);
    List<OperationDTO> toDTOList(List<OperationEntity> operationEntityList);
}
