package io.github.mskim.comm.cms.repository.specification;

import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * UserLeaveRequest 엔티티에 대한 동적 쿼리 생성을 위한 Specification
 * JPA Criteria API를 사용하여 검색 조건을 동적으로 구성
 */
public class UserLeaveRequestSpecification {

    /**
     * 검색 파라미터를 기반으로 Specification 생성
     *
     * @param sp 검색 파라미터 (UserLeaveRequestSP)
     * @return Specification 객체
     */
    public static Specification<UserLeaveRequest> createSpecification(UserLeaveRequestSP sp) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 신청 상태 필터
            if (sp.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), sp.getStatus()));
            }

            // 2. 신청자 이름 검색 (부분 일치)
            if (sp.getUserName() != null && !sp.getUserName().isBlank()) {
                predicates.add(cb.like(
                        root.get("user").get("name"),
                        "%" + sp.getUserName() + "%"
                ));
            }

            // 3. 휴가 유형 필터
            if (sp.getLeaveType() != null) {
                predicates.add(cb.equal(root.get("leaveType"), sp.getLeaveType()));
            }

            // 4. 기간 유형 필터 (종일/오전/오후)
            if (sp.getPeriodType() != null) {
                predicates.add(cb.equal(root.get("periodType"), sp.getPeriodType()));
            }

            // 5. 신청 날짜 범위 필터
            if (sp.getRequestDateStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("requestDate"),
                        sp.getRequestDateStart()
                ));
            }
            if (sp.getRequestDateEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("requestDate"),
                        sp.getRequestDateEnd()
                ));
            }

            // 6. 생성일시 범위 필터
            if (sp.getCreatedAtStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        cb.function("DATE", java.sql.Date.class, root.get("createdAt")),
                        java.sql.Date.valueOf(sp.getCreatedAtStart())
                ));
            }
            if (sp.getCreatedAtEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        cb.function("DATE", java.sql.Date.class, root.get("createdAt")),
                        java.sql.Date.valueOf(sp.getCreatedAtEnd())
                ));
            }

            // 7. Fetch Join으로 N+1 문제 방지
            if (query != null) {
                query.distinct(true); // 중복 제거
                root.fetch("user"); // User 엔티티 Fetch Join
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
