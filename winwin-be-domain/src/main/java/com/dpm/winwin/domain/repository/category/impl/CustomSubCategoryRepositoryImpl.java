package com.dpm.winwin.domain.repository.category.impl;

import static com.dpm.winwin.domain.entity.category.QSubCategory.subCategory;

import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.repository.category.CustomSubCategoryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
@RequiredArgsConstructor
public class CustomSubCategoryRepositoryImpl implements CustomSubCategoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SubCategory> getAll(Long midCategoryId) {
        return queryFactory.selectFrom(subCategory)
            .where(midCategoryIdEq(midCategoryId))
            .fetch();
    }

    private BooleanExpression midCategoryIdEq(Long midCategoryId) {
        if (ObjectUtils.isEmpty(midCategoryId)) {
            return null;
        }
        return subCategory.midCategory.id.eq(midCategoryId);
    }
}
