package com.beamofsoul.rabbit.primary.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.beamofsoul.rabbit.primary.entity.BaseAbstractEntity;
import com.querydsl.core.types.Path;


/**
 * QBaseAbstractEntity is a Querydsl query type for BaseAbstractEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseAbstractEntity extends EntityPathBase<BaseAbstractEntity> {

    private static final long serialVersionUID = -1136703543L;

    public static final QBaseAbstractEntity baseAbstractEntity = new QBaseAbstractEntity("baseAbstractEntity");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updatedDate = createDateTime("updatedDate", java.time.LocalDateTime.class);

    public QBaseAbstractEntity(String variable) {
        super(BaseAbstractEntity.class, forVariable(variable));
    }

    public QBaseAbstractEntity(Path<? extends BaseAbstractEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseAbstractEntity(PathMetadata metadata) {
        super(BaseAbstractEntity.class, metadata);
    }

}

