package io.github.mskim.comm.cms.sp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 검색 파라미터 기본 클래스
 *
 * <p>모든 검색 파라미터 클래스의 부모 클래스로, 페이지네이션 기능을 제공합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSP {

    /**
     * 페이지 번호 (1부터 시작)
     */
    private int page = 1;

    /**
     * 페이지 크기 (한 페이지당 항목 수)
     */
    private int size = 10;

    /**
     * 페이지 오프셋 계산
     *
     * <p>데이터베이스 쿼리의 OFFSET 값으로 사용됩니다.</p>
     *
     * @return 오프셋 값 (0부터 시작)
     */
    public int getOffset() {
        return (page - 1) * size;
    }
}
